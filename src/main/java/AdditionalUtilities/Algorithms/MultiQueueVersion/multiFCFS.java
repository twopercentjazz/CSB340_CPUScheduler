/** First Come, First Served CPU Scheduling Algorithm
 * @author Chris */

package AdditionalUtilities.Algorithms.MultiQueueVersion;

import AdditionalUtilities.Algorithms.AlgorithmTypes;
import AdditionalUtilities.Algorithms.AlgorithmsInterface;
import AdditionalUtilities.Utilities.Dispatcher;
import AdditionalUtilities.Utilities.ProcessControlBlock;
import AdditionalUtilities.Utilities.Scheduler;
import AdditionalUtilities.Utilities.SimulationInput;

import java.util.LinkedList;
import java.util.Queue;

public class multiFCFS implements AlgorithmsInterface {
    private Scheduler schedule;
    private Dispatcher dispatch;
    private Queue<ProcessControlBlock> ready;

    public multiFCFS(SimulationInput input) {
        this.schedule = new Scheduler(input);
        this.dispatch = new Dispatcher();
        this.ready = this.schedule.initializeReady(new LinkedList<>(), getAlgorithmType());
    }

    /** {@inheritDoc} */
    public AlgorithmTypes getAlgorithmType() {
        return AlgorithmTypes.multiFCFS;
    }

    /** {@inheritDoc} */
    public void scheduleNextProcess() { this.schedule.getNextRunningProcess(ready, dispatch); }

    /** {@inheritDoc} */
    public void dispatchNextProcess(ProcessControlBlock running) {
        if (running == null) {
            this.dispatch.contextSwitchIdle(ready, schedule, getAlgorithmType());
        } else {
            boolean preemptQueue = false;
            this.dispatch.updateResponseTime();
            int time = running.getCpuBurstTime();
            for(int i = 0; i < time; i++) {
                for(ProcessControlBlock pcb : this.schedule.getActive()) {
                    if(pcb.getState() == ProcessControlBlock.ProcessState.WAITING) {
                        this.schedule.updateIo(pcb, ready, getAlgorithmType());
                        if(!this.schedule.getTempIO().isEmpty()) {
                            preemptQueue = true;
                        }
                    } else if(pcb.getState() == ProcessControlBlock.ProcessState.READY) {
                        pcb.updateWaitingTime(1);
                    } else {
                        this.dispatch.updateExecutionTimer(1);
                    }
                }
                if(preemptQueue) {
                    break;
                }
            }
            if(!preemptQueue) {
                this.dispatch.contextSwitchFinishCpuBurst(schedule, running);
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
    public Queue<ProcessControlBlock> getReady() {
        return this.ready;
    }
}
