/** This class helps dispatch the next running process in a simulation (context switching). */

package Utilities;
import Algorithms.AlgorithmTypes;
import Algorithms.AlgorithmsInterface;

import java.util.Queue;

public class Dispatcher {
    private int executionTimer;
    private int idleTimer;
    private ProcessControlBlock runningProcess;

    public Dispatcher() {
        this.executionTimer = 0;
        this.idleTimer = 0;
        this.runningProcess = null;
    }

    public int getExecutionTimer() {
        return this.executionTimer;
    }

    public void updateExecutionTimer(int time) {
        this.executionTimer += time;
    }

    public void setExecutionTimer(int time) { this.executionTimer = time; }

    public int getIdleTimer() {
        return this.idleTimer;
    }

    public void updateIdleTimer(int time) {
        this.idleTimer += time;
    }

    public ProcessControlBlock getRunningProcess() {
        return this.runningProcess;
    }

    public void setRunningProcess(ProcessControlBlock pcb) {
        this.runningProcess = pcb;
    }

    public void updateResponseTime() {
        if(!runningProcess.getHasBeenOnCpu()) {
            runningProcess.updateResponseTime(this.executionTimer);
            runningProcess.updateHasBeenOnCpu();
        }
    }

    public void contextSwitchIdle(Queue<ProcessControlBlock> ready, Scheduler schedule, AlgorithmTypes type) {
        while(ready.isEmpty()) {
            updateExecutionTimer(1);
            updateIdleTimer(1);
            for(ProcessControlBlock pcb : schedule.getActive()) {
                if (pcb.getState() == ProcessControlBlock.ProcessState.WAITING) {
                    schedule.updateIo(pcb, ready, type);
                }
            }
        }
    }

    public void contextSwitchIdle(Scheduler schedule, AlgorithmTypes type) {
        while(schedule.isReadyListEmpty()) {
            updateExecutionTimer(1);
            updateIdleTimer(1);
            for(ProcessControlBlock pcb : schedule.getActive()) {
                if (pcb.getState() == ProcessControlBlock.ProcessState.WAITING) {
                    schedule.updateIo(pcb, schedule.getOtherReady(pcb), type);
                }
            }
        }
    }

    public void contextSwitchFinishCpuBurst(Scheduler schedule, ProcessControlBlock running) {
        if(running.isFinalBurst()) {
            schedule.flagProcessAsComplete(running);
        } else {
            running.setIoTime();
            running.setState(ProcessControlBlock.ProcessState.WAITING);
            schedule.getIo().add(running);
        }
    }

    public void contextSwitchPreemptProcess(ProcessControlBlock running, Queue<ProcessControlBlock> ready) {
        running.setState(ProcessControlBlock.ProcessState.READY);
        ready.add(running);
    }

    public void contextSwitchPreemptProcess(ProcessControlBlock running, Scheduler s, AlgorithmTypes type) {
        running.setState(ProcessControlBlock.ProcessState.READY);
        if(type == AlgorithmTypes.MLFQ && running.getPriority() != s.getReadyList().size() - 1) {
            running.updatePriority(1);
        }
        s.getOtherReady(running).add(running);
    }
}