package AdditionalUtilities.Algorithms;

import AdditionalUtilities.Utilities.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Algorithm implements AlgorithmsInterface{

    protected Scheduler schedule;
    protected Dispatcher dispatch;
    protected final List<Queue<ProcessControlBlock>> readyQueues;

    protected SimulationResults results;
    protected CpuSchedulerSimulation simulation;

    public Algorithm(SimulationInput input, CpuSchedulerSimulation simulation)
    {
        readyQueues = new ArrayList<>();
        readyQueues.add(initializeReady(input.getInput()));
        schedule = new Scheduler(input);
        dispatch = new Dispatcher();
        this.simulation = simulation;

    }

    public Algorithm(List<Queue<ProcessControlBlock>> readyQueues)
    {
        this.readyQueues = readyQueues;
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


    @Override
    /** {@inheritDoc} */
    public Queue<ProcessControlBlock> getReady(int indexOfQueue) {
        if(indexOfQueue >= readyQueues.size()) throw new IndexOutOfBoundsException("That index of the queue list doesnt not exists");
        return this.readyQueues.get(indexOfQueue);
    }

    protected Queue<ProcessControlBlock> initializeReady(ArrayList<ProcessControlBlock> input) {
        Queue<ProcessControlBlock> temp = new LinkedList<>();
        for(ProcessControlBlock p : input) {
            temp.add(p);
            p.setCpuBurstTime();
        }
        return temp;
    }
    protected void updateIo(int indexOfQueue,ProcessControlBlock pcb) {
        if(indexOfQueue >= readyQueues.size()) throw new IndexOutOfBoundsException("That index of the queue list doesnt not exists");
        pcb.updateIoTime(1);
        if (pcb.getIoTime() == 0) {
            pcb.setState(ProcessControlBlock.ProcessState.READY);
            pcb.setCpuBurstTime();
            this.schedule.getIo().remove(pcb);
            this.readyQueues.get(indexOfQueue).add(pcb);
        }
    }

    @Override
    /** {@inheritDoc} */
    public void dispatchNextProcess(int indexOfQueue, ProcessControlBlock running, Integer timeQuantum) {
        if(indexOfQueue >= readyQueues.size()) throw new IndexOutOfBoundsException("That index of the queue list doesnt not exists");
        if(running != null) {
            int time = running.getCpuBurstTime();
            if(timeQuantum != null) if (time > timeQuantum) time = timeQuantum;
            if(!running.getHasBeenOnCpu()) {
                running.updateResponseTime(this.dispatch.getExecutionTimer());
                running.updateHasBeenOnCpu();
            }
            for(int i = 0; i < time; i++) {
                for(ProcessControlBlock pcb : this.schedule.getActive()) {
                    if(pcb.getState() == ProcessControlBlock.ProcessState.WAITING) {
                        updateIo(indexOfQueue, pcb);
                    } else if(pcb.getState() == ProcessControlBlock.ProcessState.READY) {
                        pcb.updateWaitingTime(1);
                    } else {
                        this.dispatch.updateExecutionTimer(1);
                    }
                }
            }
            if(running.isFinalBurst()) {
                this.schedule.flagProcessAsComplete(running);
            } else {
                running.setIoTime();
                running.setState(ProcessControlBlock.ProcessState.WAITING);
                this.schedule.getIo().add(running);
            }
        } else {
            while(this.readyQueues.get(indexOfQueue).isEmpty()) {
                this.dispatch.updateExecutionTimer(1);
                this.dispatch.updateIdleTimer(1);
                for(ProcessControlBlock pcb : this.schedule.getActive()) {
                    if (pcb.getState() == ProcessControlBlock.ProcessState.WAITING) {
                        updateIo(indexOfQueue, pcb);
                    }
                }
            }
        }
    }
}
