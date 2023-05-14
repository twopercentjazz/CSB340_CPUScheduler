/** Priority Scheduling Preemptive*/

package AdditionalUtilities.Algorithms;
import AdditionalUtilities.Utilities.*;
import java.util.*;

public class Priority implements AlgorithmsInterface {
    private Scheduler schedule;
    private Dispatcher dispatch;
    private Queue<ProcessControlBlock> ready;

    public Priority(SimulationInput input) {
        this.ready = initializeReady(input.getInput());
        this.schedule = new Scheduler(input);
        this.dispatch = new Dispatcher();
    }

    private Queue<ProcessControlBlock> initializeReady(ArrayList<ProcessControlBlock> input) {
        Queue<ProcessControlBlock> temp = new PriorityQueue<>();
        for(ProcessControlBlock p : input) {
            temp.add(p);
            p.setCpuBurstTime();
        }
        return temp;
    }

    /** {@inheritDoc} */
    public AlgorithmTypes getAlgorithmType() {
        return AlgorithmTypes.P;
    }

    /** {@inheritDoc} */
    public void scheduleNextProcess() {
        ProcessControlBlock next = ready.poll();
        if(next != null) {
            next.setState(ProcessControlBlock.ProcessState.RUNNING);
            this.dispatch.setRunningProcess(next);
        } else {
            this.dispatch.setRunningProcess(null);
        }
    }

    /** {@inheritDoc} */
    public void dispatchNextProcess(ProcessControlBlock running) {
        Boolean preempt = false;
        if(running != null) {
            int time = running.getCpuBurstTime();
            if(!running.getHasBeenOnCpu()) {
                running.updateResponseTime(this.dispatch.getExecutionTimer());
                running.updateHasBeenOnCpu();
            }
            for(int i = 0; i < time; i++) {
                for(ProcessControlBlock pcb : this.schedule.getActive()) {
                    if(pcb.getState() == ProcessControlBlock.ProcessState.WAITING) {
                        updateIo(pcb);
                        if(ready.peek() != null && ready.peek().getPriority() < running.getPriority()) {
                            preempt = true;
                        }
                    } else if(pcb.getState() == ProcessControlBlock.ProcessState.READY) {
                        pcb.updateWaitingTime(1);
                    } else {
                        this.dispatch.updateExecutionTimer(1);
                        pcb.updateCpuBurstTime(1);
                    }
                }
                if(preempt == true) {
                    running.setState(ProcessControlBlock.ProcessState.READY);
                    this.ready.add(running);
                    break;
                }
            }
            if(preempt == false) {
                if(running.isFinalBurst()) {
                    this.schedule.flagProcessAsComplete(running);
                } else {
                    running.setIoTime();
                    running.setState(ProcessControlBlock.ProcessState.WAITING);
                    this.schedule.getIo().add(running);
                }
            }
        } else {
            while(this.ready.isEmpty()) {
                this.dispatch.updateExecutionTimer(1);
                this.dispatch.updateIdleTimer(1);
                for(ProcessControlBlock pcb : this.schedule.getActive()) {
                    if (pcb.getState() == ProcessControlBlock.ProcessState.WAITING) {
                        updateIo(pcb);
                    }
                }
            }
        }
    }

    private void updateIo(ProcessControlBlock pcb) {
        pcb.updateIoTime(1);
        if (pcb.getIoTime() == 0) {
            pcb.setState(ProcessControlBlock.ProcessState.READY);
            pcb.setCpuBurstTime();
            this.schedule.getIo().remove(pcb);
            this.ready.add(pcb);
        }
    }

    /** {@inheritDoc} */
    public Boolean isCompleted() {
        return this.schedule.getActive().isEmpty();
    }

    /** {@inheritDoc} */
    public Scheduler getScheduler() {
        return this.schedule;
    }

    /** {@inheritDoc} */
    public Dispatcher getDispatcher() {
        return this.dispatch;
    }

    /** {@inheritDoc} */
    public Queue<ProcessControlBlock> getReady() {
        return this.ready;
    }
}
