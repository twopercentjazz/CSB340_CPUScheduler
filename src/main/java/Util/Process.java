package Util;

import java.util.Arrays;

public class Process {

    private final String id;

    private final int[] routine;

    private State state;

    private int priority;

    private Integer timeCompleted;

    private int responseTime;


    public int currentRoutineIndex;
    private Cpu cpu;    //the cpu this process belongs to
    public Process(String id, int[] routine, int priority)
    {
        this.id = id;
        this.routine = routine;
        state = State.READY;
        this.priority = priority;

        responseTime = -1;
        timeCompleted = null;
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


    public void increaseTime(int delta)
    {
        if(isCompleted()) return;   //if the process has already finished do nothing
        if(state == State.EXECUTING) //if this process is in execution mode
        {
            if(routine[currentRoutineIndex] < delta) System.out.println("Cpu time increase exceeds process runtime");
            routine[currentRoutineIndex] -= delta;  //do work
        }else if(state == State.WAITING)    //if the process is doing input output
        {
            routine[currentRoutineIndex] -= delta;  //continue getting the input output
        }

        if(routine[currentRoutineIndex] <= 0)   //if ready to move on to next stage
        {
            //from executing to waiting
            if(state == State.EXECUTING)
            {
                //turnAround += (cpu.getTime() + routine[currentRoutineIndex] - arrivalTime);
                cpu.requestAwaiting(getId());
            }
            //from waiting to ready
            else if(state == State.WAITING)
            {
                cpu.requestReady(getId());
            }
            currentRoutineIndex++;  //finally moves on
            if(isCompleted() && timeCompleted == null) timeCompleted = cpu.getTime() + routine[currentRoutineIndex-1];

        }
    }

    public int getResponseTime()
    {
        return responseTime;
    }

    public void setResponseTime(int responseTime)
    {
        this.responseTime = responseTime;
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

    public int getTimeCompleted()
    {
        return (timeCompleted == null) ? -1 : (int) timeCompleted;
    }

    /**
     * Possible states for the Process
     */
    public static enum State
    {
        WAITING, READY, EXECUTING;
    }
}
