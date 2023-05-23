/** Round Robin */

package AdditionalUtilities.Algorithms;
import AdditionalUtilities.Utilities.*;
import java.util.*;

public class RR implements AlgorithmsInterface {
    private Scheduler schedule;
    private Dispatcher dispatch;
    private Queue<ProcessControlBlock> ready;
    private int timeQuantum;

    public RR(SimulationInput input) {
        this.schedule = new Scheduler(input);
        this.dispatch = new Dispatcher();
        this.timeQuantum = input.getGivenTimeQuantum();
        this.ready = this.schedule.initializeReady(new LinkedList<>(), getAlgorithmType());
    }

    /** {@inheritDoc} */
    public AlgorithmTypes getAlgorithmType() {
        return AlgorithmTypes.RR;
    }

    /** {@inheritDoc} */
    public void scheduleNextProcess() { this.schedule.getNextRunningProcess(ready, dispatch); }

    /** {@inheritDoc} */
    public void dispatchNextProcess(ProcessControlBlock running) {
        if (running == null) {
            this.dispatch.contextSwitchIdle(ready, schedule, getAlgorithmType());
        } else {
            boolean preempt = true;
            this.dispatch.updateResponseTime();
            for(int i = 0; i < this.timeQuantum; i++) {
                for(ProcessControlBlock pcb : this.schedule.getActive()) {
                    if(pcb.getState() == ProcessControlBlock.ProcessState.WAITING) {
                        this.schedule.updateIo(pcb, ready, getAlgorithmType());
                    } else if(pcb.getState() == ProcessControlBlock.ProcessState.READY) {
                        pcb.updateWaitingTime(1);
                    } else {
                        this.dispatch.updateExecutionTimer(1);
                        pcb.updateCpuBurstTime(1);
                        if(pcb.getCpuBurstTime() == 0) {
                            preempt = false;
                        }
                    }
                }
                if(!preempt) {
                    this.dispatch.contextSwitchFinishCpuBurst(schedule, running);
                    break;
                }
            }
            if(preempt) {
                this.dispatch.contextSwitchPreemptProcess(running, ready, schedule, getAlgorithmType());
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