/** This class helps schedule the next running process in a simulation. */

package Utilities;
import Algorithms.AlgorithmTypes;
import Algorithms.AlgorithmsInterface;

import java.util.*;

public class Scheduler {
    private ArrayList<AlgorithmsInterface> ready;
    private ArrayList<ProcessControlBlock> io; //
    private ArrayList<ProcessControlBlock> active; //
    private ArrayList<ProcessControlBlock> completed; //
    private ProcessControlBlock[] finalList;
    private int readyIndex;
    private int timeQuantum;


    /** Constructs a new scheduler for algorithms with single queues (FCFS, Priority, RR, SJF).
     * @param input The simulations input processes. */
    public Scheduler(SimulationInput input) {
        this.ready = null;
        this.readyIndex = 0;
        this.io = new ArrayList<>();
        this.completed = new ArrayList<>();
        this.active = input.getInput();
        this.timeQuantum = input.getGivenTimeQuantum();
        this.finalList = new ProcessControlBlock[input.getSize()];
    }

    /** Constructs a new scheduler for algorithms with multiple queues (MLQ and MLFQ).
     * @param ready The simulations algorithms used for each queue (with input processes). */
    public Scheduler(ArrayList<AlgorithmsInterface> ready) {
        this.ready = ready;
        this.readyIndex = 0;
        this.io = new ArrayList<>();
        this.completed = new ArrayList<>();
        this.active = new ArrayList<>();
        this.timeQuantum = 0;
        for(AlgorithmsInterface algorithm: ready) {
            for(ProcessControlBlock pcb: algorithm.getScheduler().getActive()) {
                this.active.add(pcb);
                pcb.setPriority(this.ready.indexOf(algorithm));
            }
        }
        this.finalList = new ProcessControlBlock[active.size()];
    }

    /** This method gets the list of the multi queue algorithms (for MLQ and MLFQ) used to access each ready queue.
     * @return the list of multi queue algorithms */
    public ArrayList<AlgorithmsInterface> getReadyList() { return this.ready; }

    /** This method gets the current list of processes in IO.
     * @return The list of processes in IO */
    public ArrayList<ProcessControlBlock> getIo() { return this.io; }


    /** This method gets the current list of active processes (aka processes not completed yet).
     * @return The list of active processes */
    public ArrayList<ProcessControlBlock> getActive() { return this.active; }

    /** This method gets the current list of completed processes.
     * @return The list of completed processes */
    public ArrayList<ProcessControlBlock> getCompleted() { return this.completed; }

    /** This method gets the given time quantum (used for RR).
     * @return The time quantum. */
    public int getTimeQuantum() { return this.timeQuantum; }

    /** This method flags a process as complete when it has finished all CPU/IO bursts.
     * @param pcb The running process */
    public void flagProcessAsComplete(ProcessControlBlock pcb) {
        pcb.setState(ProcessControlBlock.ProcessState.COMPLETE);
        active.remove(pcb);
        completed.add(pcb);
    }

    /** This method creates a list used to format the output of a simulation. */
    public void createFinalList() {
        for(ProcessControlBlock p : completed) {
            finalList[p.getPid() - 1] = p;
        }
    }

    /** This method gets the list used to format the output of a simulation.
     * @return A list of ordered processes */
    public ProcessControlBlock[] getFinalList() {
        return this.finalList;
    }

    /** This method sets all turnaround times (after all the processes are complete). */
    public void setTurnAroundTimes() {
        for(ProcessControlBlock p : finalList) {
            p.updateTurnAroundTime();
        }
    }

    /** This method updates processes in IO.
     * If the process is scheduled with SJF and finishes IO, priorities are also set to the current CPU burst.
     * @param pcb A process in IO
     * @param ready The scheduling algorithms ready list
     * @param type The scheduling algorithms type */
    public void updateIo(ProcessControlBlock pcb, Queue<ProcessControlBlock> ready, AlgorithmTypes type) {
        pcb.updateIoTime(1);
        if (pcb.getIoTime() == 0) {
            pcb.setState(ProcessControlBlock.ProcessState.READY);
            pcb.setCpuBurstTime();
            if(type == AlgorithmTypes.SJF) {
                pcb.setPriority(pcb.getCpuBurstTime());
            }
            this.io.remove(pcb);
            ready.add(pcb);
        }
    }

    /** This process initializes an algorithms ready queue (also setting the initial CPU burst times).
     * If the process is scheduled with SJF, priorities are also set to the current CPU burst.
     * @param newReady A new queue
     * @param type The algorithms type
     * @return A ready queue initialized with given processes */
    public Queue<ProcessControlBlock> initializeReady(Queue<ProcessControlBlock> newReady, AlgorithmTypes type) {
        Queue<ProcessControlBlock> ready = newReady;
        for(ProcessControlBlock p : active) {
            p.setCpuBurstTime();
            if(type == AlgorithmTypes.SJF) {
                p.setPriority(p.getCpuBurstTime());
            }
            ready.add(p);
        }
        return ready;
    }

    /** This method gets the next running process from the ready queue or "null" if there are no
     * ready processes to schedule.
     * @param ready The ready queue
     * @param prepareDispatch The algorithms dispatcher */
    public void getNextRunningProcess(Queue<ProcessControlBlock> ready, Dispatcher prepareDispatch) {
        ProcessControlBlock next = ready.poll();
        if(next != null) {
            next.setState(ProcessControlBlock.ProcessState.RUNNING);
            prepareDispatch.setRunningProcess(next);
        } else {
            prepareDispatch.setRunningProcess(null);
        }
    }

    /** This method flags if there are no processes ready in a multi queue algorithm.
     * @return True if there are no processes ready to be scheduled */
    public boolean isReadyListEmpty() {
        boolean empty = true;
        for(int i = 0; i < getReadyList().size(); i++) {
            if(!getReadyList().get(i).getReady().isEmpty()) {
                empty = false;
                break;
            }
        }
        return empty;
    }

    /** This method flags when a process coming off IO preempts the running process in
     * multi queue algorithms (indicating a queue priority change).
     * @return True if running process is preempted */
    public boolean isQueuePriorityChanged() {
        boolean priorityChange = false;
        for(int i = 0; i < this.readyIndex; i++) {
            if(!getReadyList().get(i).getReady().isEmpty()) {
                priorityChange = true;
                break;
            }
        }
        return priorityChange;
    }

    /** This method gets the index of the current ready queue in a multi queue algorithm.
     * @return The index of the current ready queue (from the ready list) */
    public int getReadyIndex() {
        return this.readyIndex;
    }

    /** This method sets the index of the next current ready queue in multi queue algorithms.
     * @param i The new index of the next current ready queue */
    public void setReadyIndex(int i) {
        this.readyIndex = i;
    }

    /** This method gets the current ready queue in multi queue algorithms (using the ready index).
     * @return The current ready queue */
    public Queue<ProcessControlBlock> getCurrReady() {
        return getReadyList().get(getReadyIndex()).getReady();
    }

    /** This method gets the ready queue that a process belongs to in multi queue algorithms
     * (using a given processes priority)
     * @param pcb The process used to get the ready queue
     * @return The ready queue the process belongs to */
    public Queue<ProcessControlBlock> getOtherReady(ProcessControlBlock pcb) {
        return getReadyList().get(pcb.getPriority()).getReady();
    }

    /** This method gets the current time quantum for the current ready queue (for MLQ and MLFQ).
     * @return The time quantum. */
    public int getCurrTimeQuantum() {
        return getReadyList().get(getReadyIndex()).getScheduler().getTimeQuantum();
    }

    /** This method gets the next ready queue (the highest priority queue with processes waiting)
     * in a multi queue algorithm.
     * @return The next ready queue to schedule from */
    public Queue<ProcessControlBlock> getNextReady() {
        for(AlgorithmsInterface algorithm: getReadyList()) {
            if(!algorithm.getReady().isEmpty()) {
                setReadyIndex(getReadyList().indexOf(algorithm));
                break;
            }
        }
        return getCurrReady();
    }

    /** This method gets the current time quantum or CPU burst used to schedule multi queue algorithms.
     * @param running The next running process
     * @return The time used for dispatching the next running process */
    public int getTimeToProcess(ProcessControlBlock running) {
        int time;
        if(getReadyIndex() == getReadyList().size() - 1) {
            time = running.getCpuBurstTime();
        } else {
            time = getCurrTimeQuantum();
        }
        return time;
    }
}