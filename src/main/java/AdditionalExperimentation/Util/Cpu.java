package AdditionalExperimentation.Util;

import AdditionalExperimentation.SchedulingAlgorithms.*;

import java.util.HashMap;
import java.util.List;

/**
 * CPU class
 * Representation of a cpu with only one core
 */
public class Cpu implements Runnable{
    private final int id;

    private final HashMap<String, Process> processMap;
    private int timeWork;
    private int timeIdle;

    private Process processWorking;

    private final Algorithm.Type algorithmType;

    public Cpu(int id, List<Process> processList, Algorithm.Type algorithmType)
    {
        this.id = id;
        timeWork = timeIdle = 0;
        processWorking = null;
        this.algorithmType = algorithmType;
        processMap = new HashMap<>();
        processList.forEach(process -> processMap.put(process.getId(), process));
    }

    /**
     * This will increase the time of all processes
     * This will also increase the cpu work time and idle time
     * As well as update the Processes when they have to switch states
     *
     * @param delta time to increase by
     */
    public void increaseTime(int delta)
    {
        if(processWorking == null || processWorking.isCompleted())  //when there are no processes running
        {
            increaseIdleTime(delta);    //all the delta time is idle time
            processMap.values().forEach(v -> v.increaseTime(delta));    //update all the processes
        }else   //there is a processes in the executing state
        {
            int actualTimeNeeded = Math.min(delta, processWorking.getRoutine()[processWorking.currentRoutineIndex]);    //the time the process needs
            increaseWorkTime(actualTimeNeeded); //the cpu will be working
            increaseIdleTime(delta - actualTimeNeeded); //the rest of the time it is in the idle state
            processMap.values().stream().filter(p -> !p.equals(processWorking)).forEach(p -> p.increaseTime(delta));    //update all the process that are not running
            processWorking.increaseTime(actualTimeNeeded);  //update the process that was running
            if(processWorking != null && processWorking.isCompleted()) processWorking = null; //if the process running finished set the processWorking to null
        }
    }

    /**
     * Increases the cpu productive time
     * @param delta the time to increase by
     */
    public void increaseWorkTime(int delta)
    {
        timeWork += delta;
    }

    /**
     * Increases the cpu lazy time
     * @param delta the time to increase by
     */
    public void increaseIdleTime(int delta)
    {
        timeIdle += delta;
    }

    /**
     * Used to Put a certain processes in the ready state
     * @param processId that processes id
     * @return  the process whose state has been changed
     */
    public Process requestReady(String processId)
    {
        ensureProcessExists(processId);
        processMap.get(processId).setState(Process.State.READY);
        return processMap.get(processId);
    }

    /**
     * Used to put a certain process in the waiting state
     * @param processId that processes id
     * @return the processes whose state has been changed
     */
    public Process requestAwaiting(String processId)
    {
        ensureProcessExists(processId);
        processMap.get(processId).setState(Process.State.WAITING);

        if(processWorking == processMap.get(processId)) processWorking = null;

        return processMap.get(processId);
    }

    /**
     * Used to put a certain process in the executing state
     * @param processId that processes id
     * @return the process whose state has been changed
     */
    public Process requestExecuting(String processId)
    {
        ensureProcessExists(processId);
        processMap.get(processId).setState(Process.State.EXECUTING);
        processWorking = processMap.get(processId);
        if(processWorking.getResponseTime() < 0) processWorking.setResponseTime(getTime());
        return processMap.get(processId);
    }

    /**
     * Retrieves a process with a specific id
     * @param processId the id
     * @return  the process
     */
    public Process getProcess(String processId)
    {
        return processMap.get(processId);
    }

    public HashMap<String, Process> getProcessHashMap()
    {
        return processMap;
    }

    /**
     * Retrieves the cpu utilization
     * @return  the cpu's utilization
     */
    public double getCpuUtilization()
    {
        return (double) Math.round((timeWork / (double) (getTime())) * 10000) /100;
    }

    /**
     * Retrieves the cpu average waiting time
     * @return  the cpu's average waiting time
     */
    public double getAvgWaitTime()
    {
        double sum = 0;
        int burstTime;
        for(Process p : processMap.values())
        {
            burstTime = 0;
            for(int i = 0; i < p.getRoutine().length; i+=2) burstTime += CpuManager.processMap.get(p.getId()).getRoutine()[i];
            sum += p.getTimeCompleted() - burstTime;
        }
        return sum/processMap.size();
    }

    /**
     * Retrieves the cpu's average turn around time
     * @return  the average turn around time
     */
    public double getAvgTurnAroundTime()
    {
        double sum = 0;
        for(Process p : processMap.values())
        {
            sum += p.getTimeCompleted();
        }
        return sum/processMap.size();
    }

    /**
     * Retrieves the average response time
     * @return  the average response time
     */
    public double getAvgResponseTime()
    {
        double sum = 0;
        for(Process p : processMap.values())
        {
            sum += p.getResponseTime();
        }
        return sum/processMap.size();
    }

    /**
     * Determines if the process exists
     * @param processId the id to look for
     * @return  true when it does exist
     */
    public boolean processExists(String processId)
    {
        return processMap.get(processId) != null;
    }

    /**
     * Ensures that the process does exist before moving on
     * @param processId
     */
    public void ensureProcessExists(String processId)
    {
        if(!processExists(processId)) throw new IllegalArgumentException("No Process Found With That Id");
    }

    /**
     * Retrieves the current cpu time
     * @return  the time
     */
    public int getTime()
    {
        return timeWork + timeIdle;
    }

    private List<Process> getProcessesInReady()
    {
        return processMap.values().stream().filter(p -> p.getState() == Process.State.READY).toList();
    }

    private List<Process> getProcessesInIO()
    {
        return  processMap.values().stream().filter(p -> p.getState() == Process.State.WAITING).toList();
    }

    public String getSnapShot(Process next)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("+------------------------------------------------+\n");
        sb.append(String.format("|Current Time: %d\n", getTime()));
        sb.append(String.format("|Next process on the CPU: %s\n", (next != null) ? next.getId() : (!isAllProcessesComplete()) ? "No Process Scheduled Next" : "All Processes Complete"));
        sb.append("+------------------------------------------------+\n");
        sb.append("|List of processes in the ready queue:\n");
        sb.append("|\t\tProcess\tBurst\n");

        getProcessesInReady().stream().filter(p -> p != next && !p.isCompleted()).forEach(p -> sb.append(String.format("|\t\t  %s\t  %d\n", p.getId(), p.getRoutine()[p.currentRoutineIndex])));

        sb.append("+------------------------------------------------+\n");
        sb.append("|List of processes in I/O:\n");
        sb.append("|\t\tProcess\tBurst\n");

        getProcessesInIO().stream().filter(p -> !p.isCompleted()).forEach(p -> sb.append(String.format("|\t\t  %s\t  %d\n", p.getId(), p.getRoutine()[p.currentRoutineIndex])));

        sb.append("+------------------------------------------------+\n");
        return sb.toString();
    }

    public String getPerformanceShot()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("+------------------------------------------------+\n");
        sb.append("|Algorithm Type: " + algorithmType.toString() + String.format("\tCpu :%d\n", getId()));
        sb.append("|Current Time : " + getTime()+"\n");
        sb.append("|CPU utilization: " + getCpuUtilization() +"%\n");
        sb.append("|Avg turn around time: " + getAvgTurnAroundTime() + "\n");
        sb.append("|Avg response time: " + getAvgResponseTime() + "\n");
        sb.append("|Avg wait time: " + getAvgWaitTime()+"\n");
        sb.append("+------------------------------------------------+\n");
        return sb.toString();
    }

    /**
     * Run the algorithm
     */
    public void run()
    {
        switch (algorithmType){
            case SJF -> new SJF(this).runExperiment();
            case RR -> new RR(this).runExperiment();
            case MLQ -> new MLQ(this).runExperiment();
            //case FCFS -> new FCFS(this).runExperiment();
            case MLFQ -> new MLFQ(this).runExperiment();
            case Priority -> new Priority(this).runExperiment();
            default -> System.out.println(String.format("The Algorithm type %s has yet to be added in the Cpu Run Method\nGo Added it if you wish to have the ability to run it in a thread", algorithmType));
        }
    }

    /**
     * Retrieves the cpu id
     * @return
     */
    public int getId()
    {
        return id;
    }

    public boolean isAllProcessesComplete()
    {
        for(Process p : processMap.values()) if(!p.isCompleted()) return false;
        return true;
    }

    public Algorithm.Type getAlgorithmType()
    {
        return algorithmType;
    }
}
