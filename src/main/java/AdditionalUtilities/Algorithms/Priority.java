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

    /**
     * Put all ready processes into a "ready" queue
     * Use a priority queue so processes with higher priority is at the front
     *
     * @param input
     * @return
     */
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

        if(running != null) { // if already chosen a running process
            int time = running.getCpuBurstTime();

            // put process on CPU if it hasn't been.
            if(!running.getHasBeenOnCpu()) {
                running.updateResponseTime(this.dispatch.getExecutionTimer());
                running.updateHasBeenOnCpu();
            }

            // Tick the timer and update all active processes
            // Stop until the running process changes state, or a preempt process is found
            for(int i = 0; i < time; i++) {
                // Update processes in Active queue. That includes IO, Ready, or running processes
                for(ProcessControlBlock pcb : this.schedule.getActive()) {
                    if(pcb.getState() == ProcessControlBlock.ProcessState.WAITING) {
                        // Update processes in Waiting state
                        updateIo(pcb);
                        // If one becomes ready and have higher priority than running process, preempt is found
                        if(ready.peek() != null && ready.peek().getPriority() < running.getPriority()) {
                            preempt = true;
                        }
                    } else if(pcb.getState() == ProcessControlBlock.ProcessState.READY) {
                        // For ready but not running processes, just increase waiting time
                        pcb.updateWaitingTime(1);
                    } else {
                        // For the running process, update execution time and burst time
                        this.dispatch.updateExecutionTimer(1);
                        pcb.updateCpuBurstTime(1);
                    }
                }

                // When preempt is found, move running process back to Ready queue. Then stop the current timer ticking.
                if(preempt == true) {
                    running.setState(ProcessControlBlock.ProcessState.READY);
                    this.ready.add(running);
                    break;
                }
            }

            // When ticking stop, handles 3 scenarios:
            // - If a preempt is found, immediately stop dispatcher and continue with scheduler
            // - If no preempt and running process has no more burst, mark it as complete
            // - If no preempt and running process still has more bursts, put it into IO list in scheduler
            if(preempt == false) {
                if(running.isFinalBurst()) {
                    this.schedule.flagProcessAsComplete(running);
                } else {
                    running.setIoTime();
                    running.setState(ProcessControlBlock.ProcessState.WAITING);
                    this.schedule.getIo().add(running);
                }
            }
        } else { // in case there is no running process from the start
            // Update the processes in IO until one become ready
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

    /**
     * Update a process in IO.
     * Use on all Waiting processes every time the timer increases.
     * @param pcb a process in IO
     */
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
