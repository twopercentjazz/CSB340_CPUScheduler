/** This class helps dispatch the next running process in a simulation (context switching),
 *  and keeps track of the timers used for the simulation. */

package Utilities;
import Algorithms.AlgorithmTypes;
import Algorithms.AlgorithmsInterface;

import java.util.Queue;

public class Dispatcher {
    private int executionTimer;
    private int idleTimer;
    private ProcessControlBlock runningProcess;

    /** Constructs a new dispatcher for an algorithm. */
    public Dispatcher() {
        this.executionTimer = 0;
        this.idleTimer = 0;
        this.runningProcess = null;
    }

    /** This method gets the execution timer.
     * @return The execution time */
    public int getExecutionTimer() {
        return this.executionTimer;
    }

    /** This method updates the execution timer.
     * @param time The time to increase the execution timer by */
    public void updateExecutionTimer(int time) { this.executionTimer += time; }

    /** This method gets the idle timer.
     * @return The idle time */
    public int getIdleTimer() { return this.idleTimer; }

    /** This method updates the idle timer.
     * @param time The time to increase the idle timer by */
    public void updateIdleTimer(int time) { this.idleTimer += time; }

    /** This method gets the next running process.
     * @return The scheduled next running process (to be dispatched) */
    public ProcessControlBlock getRunningProcess() { return this.runningProcess; }

    /** This method sets the next running process.
     * @param pcb The next running process */
    public void setRunningProcess(ProcessControlBlock pcb) { this.runningProcess = pcb; }

    /** This method updates the response time for a running process. */
    public void updateResponseTime() {
        if(!this.runningProcess.getHasBeenOnCpu()) {
            this.runningProcess.updateResponseTime(this.executionTimer);
            this.runningProcess.updateHasBeenOnCpu();
        }
    }

    /** This method context switches an idle running process, updating the processes in I/O.
     *  This version of the method is used for FCFS, Priority, RR and SJF.
     * @param ready The algorithms ready queue
     * @param schedule The algorithms scheduler
     * @param type The algorithms type */
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

    /** This method context switches an idle running process, updating the processes in I/O.
     *  This version of the method is used for MLQ and MLFQ.
     * @param schedule The algorithms scheduler
     * @param type The algorithms type */
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

    /** This method context switches a process that finishes a CPU burst.
     * @param schedule The algorithms scheduler
     * @param running The running process that finished a CPU burst */
    public void contextSwitchFinishCpuBurst(Scheduler schedule, ProcessControlBlock running) {
        if(running.isFinalBurst()) {
            schedule.flagProcessAsComplete(running);
        } else {
            running.setIoTime();
            running.setState(ProcessControlBlock.ProcessState.WAITING);
            schedule.getIo().add(running);
        }
    }

    /** This method preempts a process off the CPU.
     * This version is for algorithms with a single ready queue.
     * @param running The running process being preempted
     * @param ready The ready list to add back into */
    public void contextSwitchPreemptProcess(ProcessControlBlock running, Queue<ProcessControlBlock> ready) {
        running.setState(ProcessControlBlock.ProcessState.READY);
        ready.add(running);
    }

    /** This method preempts a process off the CPU.
     * This version is for algorithms with a multiple ready queues.
     * @param running The running process being preempted
     * @param schedule The algorithms scheduler
     * @param type The algorithms type */
    public void contextSwitchPreemptProcess(ProcessControlBlock running, Scheduler schedule, AlgorithmTypes type) {
        running.setState(ProcessControlBlock.ProcessState.READY);
        if(type == AlgorithmTypes.MLFQ && running.getPriority() != schedule.getReadyList().size() - 1) {
            running.updatePriority(1);
        }
        schedule.getOtherReady(running).add(running);
    }
}