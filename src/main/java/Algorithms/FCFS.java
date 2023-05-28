/** First Come, First Served CPU Scheduling Algorithm (Non-Preemptive).
 * @author Chris */

package Algorithms;
import Utilities.*;
import java.util.*;

public class FCFS implements AlgorithmsInterface {
    private Scheduler schedule;
    private Dispatcher dispatch;
    private Queue<ProcessControlBlock> ready;

    /** Constructor for FCFS scheduling algorithm.
     * @param input the processes to schedule */
    public FCFS(SimulationInput input) {
        this.schedule = new Scheduler(input);
        this.dispatch = new Dispatcher();
        this.ready = this.schedule.initializeReady(new LinkedList<>(), getAlgorithmType());
    }

    /** {@inheritDoc} */
    public AlgorithmTypes getAlgorithmType() {
        return AlgorithmTypes.FCFS;
    }

    /** {@inheritDoc} */
    public void scheduleNextProcess() { this.schedule.getNextRunningProcess(this.ready, this.dispatch); }

    /** {@inheritDoc} */
    public void dispatchNextProcess(ProcessControlBlock running) {
        // if idle
        if (running == null) {
            this.dispatch.contextSwitchIdle(this.ready, this.schedule, getAlgorithmType());
        // if not idle
        } else {
            this.dispatch.updateResponseTime();
            int time = running.getCpuBurstTime();
            for(int i = 0; i < time; i++) {
                for(ProcessControlBlock pcb : this.schedule.getActive()) {
                    if(pcb.getState() == ProcessControlBlock.ProcessState.WAITING) {
                        this.schedule.updateIo(pcb, this.ready, getAlgorithmType());
                    } else if(pcb.getState() == ProcessControlBlock.ProcessState.READY) {
                        pcb.updateWaitingTime(1);
                    } else {
                        this.dispatch.updateExecutionTimer(1);
                    }
                }
            }
            this.dispatch.contextSwitchFinishCpuBurst(this.schedule, running);
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
