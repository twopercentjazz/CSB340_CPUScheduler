/** This class represents a process control block, which are the scheduling simulations inputs. */

package Utilities;
import java.util.*;

public class ProcessControlBlock implements Comparable<ProcessControlBlock> {
    private int pid;
    private int[] input;
    private Queue<Integer> cycle;
    private int arrivalTime;
    private int priority;
    private int waitingTime;
    private int turnAroundTime;
    private int responseTime;
    private int cpuBurstTime;
    private int ioTime;
    private ProcessState state;
    private Boolean hasBeenOnCpu;
    private int cycleSum;

    /** Constructs a new Process Control Block.
     * @param pid The process id number
     * @param input List of given CPU/IO burst times. */
    public ProcessControlBlock(int pid, int[] input) {
        this.pid = pid;
        this.arrivalTime = 0;
        this.waitingTime = 0;
        this.turnAroundTime = 0;
        this.responseTime = 0;
        this.state = ProcessState.READY;
        this.cpuBurstTime = 0;
        this.ioTime = 0;
        this.hasBeenOnCpu = false;
        this.input = input;
        this.cycle = createCycleQueue(input);
        this.cycleSum = getCycleSum(cycle);
    }

    /** This method calculates a processes total sum of both CPU burst times and I/O burst times.
     * @param cycle The initial list of CPU/IO burst times
     * @return The sum of both CPU burst times and I/O burst times */
    private int getCycleSum(Queue<Integer> cycle) { //total cpuBurst + total io
        int sum = 0;
        for(Integer i : cycle) {
            sum += i;
        }
        return sum;
    }

    /** This method creates a queue of CPU/IO bursts (for the simulation) from the given input burst cycle.
     * @param cycle The given (input) burst cycle list
     * @return A queue containing the given burst cycle */
    private Queue<Integer> createCycleQueue(int[] cycle) {
        Queue<Integer> temp = new LinkedList<>();
        for(Integer i : cycle) {
            temp.add(i);
        }
        return temp;
    }

    /** This method gets a processes id number.
     * @return The processes id number */
    public int getPid() { return this.pid; }

    /** This method gets a processes arrival time
     * @return The processes arrival time */
    public int getArrivalTime() { return this.arrivalTime; }

    /** This method gets a processes priority number.
     * @return The processes priority number */
    public int getPriority() { return this.priority; }

    /** This method sets a processes priority number.
     * @param priority The processes new priority number */
    public void setPriority(int priority) { this.priority = priority; }

    /** This method updates a processes priority number.
     * @param priority The processes old priority incremented by the given number */
    public void updatePriority(int priority) { this.priority += priority; }

    /** This method gets a processes priority number.
     * @return The processes priority number */
    public int getWaitingTime() { return this.waitingTime; }

    /** This method updates a processes waiting time.
     * @param time The processes old wait time incremented by the given time */
    public void updateWaitingTime(int time) { this.waitingTime += time; }

    /** This method gets a processes turnaround time.
     * @return The processes turnaround time */
    public int getTurnAroundTime() { return this.turnAroundTime; }

    /** This method calculates and updates a processes turnaround time. */
    public void updateTurnAroundTime() { this.turnAroundTime = this.cycleSum + this.waitingTime; }

    /** This method gets a processes response time.
     * @return The processes response time */
    public int getResponseTime() { return this.responseTime; }

    /** This method updates a processes response time with the given time. */
    public void updateResponseTime(int time) { this.responseTime = time; }

    /** This method gets the next CPU/IO burst time for the process.
     * @return The next CPU/IO burst time */
    private int getNext() { return cycle.poll(); }

    /** This method gets a processes current CPU burst time.
     * @return The current CPU burst time */
    public int getCpuBurstTime() { return this.cpuBurstTime; }

    /** This method updates a processes current CPU burst by subtracting the given time. */
    public void updateCpuBurstTime(int time) { this.cpuBurstTime -= time; }

    /** This method sets a processes next CPU burst time. */
    public void setCpuBurstTime() { this.cpuBurstTime = getNext(); }

    /** This method gets a processes current IO burst time.
     * @return The current IO burst time */
    public int getIoTime() { return this.ioTime; }

    /** This method updates a processes current IO burst by subtracting the given time. */
    public void updateIoTime(int time) { this.ioTime -= time; }

    /** This method sets a processes next IO burst time. */
    public void setIoTime() { this.ioTime = getNext(); }

    /** This method gets a processes current state (running, waiting, ready, complete).
     * @return The processes state */
    public ProcessState getState() { return this.state; }

    /** This method sets a processes current state with the given state. */
    public void setState(ProcessState state) { this.state = state; }

    /** This method indicates if a process has had the CPU.
     * @return True if the process has been on the CPU */
    public Boolean getHasBeenOnCpu() { return this.hasBeenOnCpu; }

    /** This method flags the process when it has been on the CPU. */
    public void updateHasBeenOnCpu() { this.hasBeenOnCpu = true; }

    /** This method over-rides compareTo to sort processes by their priority.
     * @param other the process to be compared.
     * @return An int that represents the compare to relationship */
    @Override
    public int compareTo(ProcessControlBlock other) {
        if(this.priority == other.getPriority()) {
            return 0;
        } else if(this.priority > other.getPriority()) {
            return 1;
        } else {
            return -1;
        }
    }

    /** This enum represents all the process states. */
    public enum ProcessState { RUNNING, WAITING, READY, COMPLETE }

    /** This method copies a process control block.
     * @return A copy of this process control block */
    public ProcessControlBlock copy() { return new ProcessControlBlock(this.pid, this.input); }

    /** This method flags when the process is on its final CPU burst.
     * @return True if there are no bursts remaining */
    public Boolean isFinalBurst() { return this.cycle.isEmpty(); }
}

