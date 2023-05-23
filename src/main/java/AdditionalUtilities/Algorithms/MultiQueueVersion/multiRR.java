/** Round Robin */

package AdditionalUtilities.Algorithms.MultiQueueVersion;

import AdditionalUtilities.Algorithms.AlgorithmTypes;
import AdditionalUtilities.Algorithms.AlgorithmsInterface;
import AdditionalUtilities.Utilities.Dispatcher;
import AdditionalUtilities.Utilities.ProcessControlBlock;
import AdditionalUtilities.Utilities.Scheduler;
import AdditionalUtilities.Utilities.SimulationInput;

import java.util.LinkedList;
import java.util.Queue;

public class multiRR implements AlgorithmsInterface {
    private Scheduler schedule;
    private Dispatcher dispatch;
    private Queue<ProcessControlBlock> ready;
    private int timeQuantum;

    public multiRR(SimulationInput input) {
        this.schedule = new Scheduler(input);
        this.dispatch = new Dispatcher();
        this.timeQuantum = input.getGivenTimeQuantum();
        this.ready = this.schedule.initializeReady(new LinkedList<>(), getAlgorithmType());
    }

    /** {@inheritDoc} */
    public AlgorithmTypes getAlgorithmType() {
        return AlgorithmTypes.multiRR;
    }

    /** {@inheritDoc} */
    public void scheduleNextProcess() { this.schedule.getNextRunningProcess(ready, dispatch); }

    /** {@inheritDoc} */
    public void dispatchNextProcess(ProcessControlBlock running) {
        if (running == null) {
            this.dispatch.contextSwitchIdle(ready, schedule, getAlgorithmType());
        } else {
            boolean preemptQueue = false;
            boolean preemptProcess = true;
            this.dispatch.updateResponseTime();
            for(int i = 0; i < this.timeQuantum; i++) {
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
                        pcb.updateCpuBurstTime(1);
                        if(pcb.getCpuBurstTime() == 0) {
                            preemptProcess = false;
                        }
                    }
                }

                if(!preemptProcess) {
                    this.dispatch.contextSwitchFinishCpuBurst(schedule, running);
                    break;
                }
                if(preemptQueue) {
                    preemptProcess = false;
                    break;
                }

            }
            if(preemptProcess) {
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