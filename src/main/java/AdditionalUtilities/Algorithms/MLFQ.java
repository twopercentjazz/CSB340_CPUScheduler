/** Multilevel Queue */

package AdditionalUtilities.Algorithms;
import AdditionalUtilities.Utilities.*;
import java.util.*;

public class MLFQ implements AlgorithmsInterface {
    private Scheduler schedule;
    private Dispatcher dispatch;
    private Queue<ProcessControlBlock> ready;

    public MLFQ(ArrayList<AlgorithmsInterface> ready) {
        this.schedule = new Scheduler(ready);
        this.dispatch = new Dispatcher();
        this.ready = this.schedule.getReadyList().get(this.schedule.getActiveQueue()).getReady();
    }

    /**
     * {@inheritDoc}
     */
    public AlgorithmTypes getAlgorithmType() {
        return AlgorithmTypes.MLFQ;
    }

    /**
     * {@inheritDoc}
     */
    public void scheduleNextProcess() {
        this.schedule.getNextRunningProcess(this.schedule.getReadyList().get(this.schedule.getActiveQueue()).getReady(), this.schedule.getReadyList().get(this.schedule.getActiveQueue()).getDispatcher());
        this.dispatch.setRunningProcess(this.schedule.getReadyList().get(this.schedule.getActiveQueue()).getDispatcher().getRunningProcess());

    }

    /** {@inheritDoc} */
    public void dispatchNextProcess(ProcessControlBlock running) {
        schedule.getReadyList().get(schedule.getActiveQueue()).dispatchNextProcess(running);
        this.dispatch.updateMultiTimer(schedule);
        if(running != null && running.getState() == ProcessControlBlock.ProcessState.COMPLETE) {
            schedule.flagProcessAsComplete(running);
        }
        if(schedule.getReadyList().get(schedule.getActiveQueue()).isCompleted()) {
            schedule.updateActiveQueue();
            if(!isCompleted()) {
                ready = schedule.getReadyList().get(schedule.getActiveQueue()).getReady();
                schedule.getReadyList().get(getScheduler().getActiveQueue()).getDispatcher().setExecutionTimer(dispatch.getExecutionTimer());
                schedule.getReadyList().get(getScheduler().getActiveQueue()).getDispatcher().setIdleTimer(dispatch.getIdleTimer());
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
