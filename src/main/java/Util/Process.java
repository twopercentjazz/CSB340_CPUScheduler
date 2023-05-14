package Util;

import java.util.Arrays;

public class Process {

    private final String id;

    private final int[] routine;

    private State state;

    private int priority;

    private int arrivalTime;
    private int startTime;
    private int timeWaiting;


    public int currentRoutineIndex;
    private Cpu cpu;    //the cpu this process belongs to
    public Process(String id, int[] routine)
    {
        this.id = id;
        this.routine = routine;
        state = State.READY;
        priority = 5;

        arrivalTime = startTime = timeWaiting = 0;
    }

    public State getState()
    {
        return state;
    }
    public void setState(State state)
    {
        this.state = state;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Integer getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(Integer arrivalTime)
    {
        this.arrivalTime = arrivalTime;
    }

    public Integer getStartTime() {
        return startTime;
    }
    public void setStartTime(Integer startTime)
    {
        this.startTime = startTime;
    }

    public int getWaitingTime()
    {
        return timeWaiting;
    }
    public void increaseTime(int delta)
    {
        if(isCompleted()) return;   //if the process has already finished do nothin
        if(state == State.EXECUTING) //if this process is in execution mode
        {
            if(routine[currentRoutineIndex] < delta) System.out.println("Cpu time increase exceeds process runtime");
            routine[currentRoutineIndex] -= delta;  //do work
        }else if(state == State.WAITING)    //if the process is doint input output
        {
            routine[currentRoutineIndex] -= delta;  //contine getting the input output
            if(routine[currentRoutineIndex] < 0) arrivalTime = (cpu.getTime() + routine[currentRoutineIndex]);  //setting or updating the arrival Time
        }

        if(routine[currentRoutineIndex] <= 0)   //if ready to move on to next stage
        {
            //from excecuting to waiting
            if(state == State.EXECUTING)
            {
                //TODO : COME BACK
                cpu.increaseTurnAroundTime((cpu.getTime() - routine[currentRoutineIndex]) - arrivalTime);
                cpu.requestAwaiting(getId());
            }
            //from waiting to ready
            else if(state == State.WAITING) state = State.READY;
            currentRoutineIndex++;  //finally moves on
        }
    }

    public String getId()
    {
        return id;
    }

    /**
     * Sets the cpu for this process
     *
     * @param cpu  instance that the process should belong to
     */
    public void setCpu(Cpu cpu)
    {
        //avoid using this method if possible
        this.cpu = cpu;
    }

    /**
     * Retrieves the process Routine ie burst cycles and wait cycles
     * @return
     */
    public int[] getRoutine()
    {
        return routine;
    }

    public String toString()
    {
        return id + " " + Arrays.toString(routine);
    }

    /**
     * Determines if the process has completed
     * @return  true if it has
     */
    public boolean isCompleted()
    {
        return currentRoutineIndex >= routine.length;
    }

    /**
     * Possible states for the Process
     */
    public static enum State
    {
        WAITING, READY, EXECUTING;
    }
}
