/** Multilevel Feedback Queue CPU Scheduling Algorithm
 * @author Chris */

package AdditionalUtilities.Algorithms;
import AdditionalUtilities.Utilities.*;
import java.util.*;

public class MLFQ implements AlgorithmsInterface {
    private Scheduler schedule;
    private Dispatcher dispatch;
    private Queue<ProcessControlBlock> ready;

    /** Constructor for MLFQ algorithm.
     * @param ready the multiple types of algorithms (with input processes) used to schedule */
    public MLFQ(ArrayList<AlgorithmsInterface> ready) {
        this.schedule = new Scheduler(ready);
        this.dispatch = new Dispatcher();
        this.ready = this.schedule.getReadyList().get(this.schedule.getReadyIndex()).getReady();
    }

    /** {@inheritDoc} */
    public AlgorithmTypes getAlgorithmType() {
        return AlgorithmTypes.MLFQ;
    }

    /** {@inheritDoc} */
    public void scheduleNextProcess() {
        ProcessControlBlock next = this.schedule.getReadyList().get(this.schedule.getReadyIndex()).getReady().poll();
        if(next != null) {
            next.setState(ProcessControlBlock.ProcessState.RUNNING);
            this.dispatch.setRunningProcess(next);
        } else {
            this.dispatch.setRunningProcess(null);
        }
    }

    /** {@inheritDoc} */
    public void dispatchNextProcess(ProcessControlBlock running) {
        if (running == null) {
            while(this.schedule.isReadyListEmpty()) {
                this.dispatch.updateExecutionTimer(1);
                this.dispatch.updateIdleTimer(1);
                for(ProcessControlBlock pcb : this.schedule.getActive()) {
                    if (pcb.getState() == ProcessControlBlock.ProcessState.WAITING) {
                        pcb.updateIoTime(1);
                        if (pcb.getIoTime() == 0) {
                            pcb.setState(ProcessControlBlock.ProcessState.READY);
                            pcb.setCpuBurstTime();
                            this.schedule.getIo().remove(pcb);
                            this.schedule.getReadyList().get(pcb.getPriority()).getReady().add(pcb);
                        }
                    }
                }
            }
        } else {
            int time;
            if(schedule.getReadyIndex() == schedule.getReadyList().size() - 1) {
                time = running.getCpuBurstTime();
            } else {
                time = schedule.getReadyList().get(schedule.getReadyIndex()).getScheduler().getTimeQuantum();
            }
            boolean queuePreempt = false;
            boolean rrPreempt = true;
            this.dispatch.updateResponseTime();
            for(int i = 0; i < time; i++) {
                for(ProcessControlBlock pcb : this.schedule.getActive()) {
                    if(pcb.getState() == ProcessControlBlock.ProcessState.WAITING) {
                        pcb.updateIoTime(1);
                        if (pcb.getIoTime() == 0) {
                            pcb.setState(ProcessControlBlock.ProcessState.READY);
                            pcb.setCpuBurstTime();
                            this.schedule.getIo().remove(pcb);
                            schedule.getReadyList().get(pcb.getPriority()).getReady().add(pcb);
                            if(schedule.isQueuePriorityChanged()) {
                                queuePreempt = true;
                            }
                        }
                    } else if(pcb.getState() == ProcessControlBlock.ProcessState.READY) {
                        pcb.updateWaitingTime(1);
                    } else {
                        this.dispatch.updateExecutionTimer(1);
                        pcb.updateCpuBurstTime(1);
                        if(pcb.getCpuBurstTime() == 0) {
                            rrPreempt = false;
                        }
                    }
                }
                if(!rrPreempt) {
                    if(running.isFinalBurst()) {
                        schedule.flagProcessAsComplete(running);
                    } else {
                        running.setIoTime();
                        running.setState(ProcessControlBlock.ProcessState.WAITING);
                        schedule.getIo().add(running);
                    }
                    break;
                }
                if(queuePreempt) {

                    running.setState(ProcessControlBlock.ProcessState.READY);
                    this.schedule.getReadyList().get(running.getPriority()).getReady().add(running);

                    break;
                }
            }
            if(rrPreempt && !queuePreempt) {
                running.setState(ProcessControlBlock.ProcessState.READY);
                if(running.getPriority() == schedule.getReadyList().size() - 1) {
                    this.schedule.getReadyList().get(running.getPriority()).getReady().add(running);
                } else {
                    running.updatePriority(1);
                    this.schedule.getReadyList().get(running.getPriority()).getReady().add(running);
                }
            }
        }
        for(AlgorithmsInterface algorithm: schedule.getReadyList()) {
            if(!algorithm.getReady().isEmpty()) {
                schedule.setReadyIndex(schedule.getReadyList().indexOf(algorithm));
                break;
            }
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
    public Queue<ProcessControlBlock> getReady() { return this.ready; }
}
