/** This class represents a process control block, which are the simulations inputs. */

package AdditionalUtilities.Utilities;
import java.util.*;

public class ProcessControlBlock implements Comparable<ProcessControlBlock> {
    private int pid;
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

    public ProcessControlBlock(int pid) {
        this.pid = pid;
        this.arrivalTime = 0;
        this.waitingTime = 0;
        this.turnAroundTime = 0;
        this.responseTime = 0;
        this.state = ProcessState.READY;
        this.cpuBurstTime = 0;
        this.ioTime = 0;
        this.hasBeenOnCpu = false;
    }
    public ProcessControlBlock(int pid, int[] cycle) {
        this(pid);
        this.cycle = createCycleQueue(cycle);
    }

    public ProcessControlBlock(int pid, Queue<Integer> cycle) {
        this(pid);
        this.cycle = cycle;
        this.cycleSum = getCycleSum(cycle);
    }

    private int getCycleSum(Queue<Integer> cycle) { //total cpuBurst + total io
        int sum = 0;
        for(Integer i : cycle) {
            sum += i;
        }
        return sum;
    }

    private Queue<Integer> createCycleQueue(int[] cycle) {
        Queue<Integer> temp = new LinkedList<>();
        for(Integer i : cycle) {
            temp.add(i);
        }
        return temp;
    }

    public int getPid() {
        return this.pid;
    }

    public int getArrivalTime() {
        return this.arrivalTime;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getWaitingTime() {
        return this.waitingTime;
    }

    public void updateWaitingTime(int time) {
        this.waitingTime += time;
    }

    public int getTurnAroundTime() {
        return this.turnAroundTime;
    }

    public void updateTurnAroundTime() {
        this.turnAroundTime = this.cycleSum + this.waitingTime;
    }

    public int getResponseTime() {
        return this.responseTime;
    }

    public void updateResponseTime(int time) {
        this.responseTime = time;
    }

    private int getNext() {
        return cycle.poll();
    }

    public int getCpuBurstTime() {
        return this.cpuBurstTime;
    }

    public void updateCpuBurstTime(int time) {
        this.cpuBurstTime -= time;
    }

    public void setCpuBurstTime() {
        this.cpuBurstTime = getNext();
    }

    public int getIoTime() {
        return this.ioTime;
    }

    public void updateIoTime(int time) {
        this.ioTime -= time;
    }

    public void setIoTime() {
        this.ioTime = getNext();
    }

    public ProcessState getState() {
        return this.state;
    }

    public void setState(ProcessState state) {
        this.state = state;
    }

    public Boolean getHasBeenOnCpu() {
        return this.hasBeenOnCpu;
    }

    public void updateHasBeenOnCpu() {
        this.hasBeenOnCpu = true;
    }

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

    public enum ProcessState {RUNNING, WAITING, READY, COMPLETE }

    public ProcessControlBlock copyProcess() {
        return new ProcessControlBlock(this.pid, this.cycle);
    }

    public Boolean isFinalBurst() {
        return this.cycle.isEmpty();
    }
}

