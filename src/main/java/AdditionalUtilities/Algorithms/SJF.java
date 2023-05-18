/** Shortest job First */

package AdditionalUtilities.Algorithms;
import AdditionalUtilities.Utilities.*;
import java.util.*;

public class SJF implements AlgorithmsInterface {
    private Scheduler schedule;
    private Dispatcher dispatch;
    private Queue<ProcessControlBlock> ready;

    public SJF(SimulationInput input) {
        this.schedule = new Scheduler(input);
        this.dispatch = new Dispatcher();
        this.ready = this.schedule.initializeReady(new PriorityQueue<>(), getAlgorithmType());
    }

    /** {@inheritDoc} */
    public AlgorithmTypes getAlgorithmType() {
        return AlgorithmTypes.SJF;
    }

    /** {@inheritDoc} */
    public void scheduleNextProcess() { this.schedule.getNextRunningProcess(ready, dispatch); }

    /** {@inheritDoc} */
    public void dispatchNextProcess(ProcessControlBlock running) {
        if (running == null) {
            this.dispatch.contextSwitchIdle(ready, schedule, getAlgorithmType());
        } else {
            this.dispatch.updateResponseTime();
            for(int i = 0; i < running.getCpuBurstTime(); i++) {
                for(ProcessControlBlock pcb : this.schedule.getActive()) {
                    if(pcb.getState() == ProcessControlBlock.ProcessState.WAITING) {
                        this.schedule.updateIo(pcb, ready, getAlgorithmType());
                    } else if(pcb.getState() == ProcessControlBlock.ProcessState.READY) {
                        pcb.updateWaitingTime(1);
                    } else {
                        this.dispatch.updateExecutionTimer(1);
                    }
                }
            }
            dispatch.contextSwitchFinishCpuBurst(schedule, running);
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