/** Multilevel Feedback Queue CPU Scheduling Algorithm
 * @author Chris */

package Algorithms;
import Utilities.*;
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
        this.ready = schedule.getCurrReady();
    }

    /** {@inheritDoc} */
    public AlgorithmTypes getAlgorithmType() {
        return AlgorithmTypes.MLFQ;
    }

    /** {@inheritDoc} */
    public void scheduleNextProcess() {
        this.schedule.getNextRunningProcess(this.ready, this.dispatch);
    }

    /** {@inheritDoc} */
    public void dispatchNextProcess(ProcessControlBlock running) {
        if (running == null) {
            dispatch.contextSwitchIdle(this.schedule, getAlgorithmType());
        } else {
            int time = schedule.getTimeToProcess(running);
            boolean queuePreempt = false;
            boolean rrPreempt = true;
            this.dispatch.updateResponseTime();
            for(int i = 0; i < time; i++) {
                for(ProcessControlBlock pcb : this.schedule.getActive()) {
                    if(pcb.getState() == ProcessControlBlock.ProcessState.WAITING) {
                        this.schedule.updateIo(pcb, this.schedule.getOtherReady(pcb), getAlgorithmType());
                        if(schedule.isQueuePriorityChanged()) {
                            queuePreempt = true;
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
                    this.dispatch.contextSwitchFinishCpuBurst(this.schedule, running);
                    break;
                }
                if(queuePreempt) {
                    running.setState(ProcessControlBlock.ProcessState.READY);
                    this.schedule.getOtherReady(running).add(running);
                    break;
                }
            }
            if(rrPreempt && !queuePreempt) {
                dispatch.contextSwitchPreemptProcess(running, this.schedule);
            }
        }
        ready = this.schedule.getNextReady();
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
