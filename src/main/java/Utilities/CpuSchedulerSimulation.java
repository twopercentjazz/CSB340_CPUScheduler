/** This class represents an instance of a CPU scheduling simulation. This class runs instances of
 *  the scheduling algorithms (with given input processes) and saves the results. */

package Utilities;
import Algorithms.*;
import java.util.*;

public class CpuSchedulerSimulation {
    private SimulationInput input;
    private AlgorithmTypes algorithmType;
    private SchedulingTypes schedulingType;
    private AlgorithmsInterface algorithm;
    private SimulationResults results;
    private ArrayList<SimulationRecord> records;
    private ArrayList<AlgorithmTypes> multiList;
    private ArrayList<SimulationInput> multiInput;

    /** This constructs a new scheduler simulation (for FCFS, Priority, RR, SJF).
     * @param input The processes to schedule
     * @param algorithmType The type of scheduling algorithm being used
     * @param schedulingType either preemptive or non-preemptive */
    public CpuSchedulerSimulation(SimulationInput input, AlgorithmTypes algorithmType, SchedulingTypes schedulingType) {
        this.input = input;
        this.algorithmType = algorithmType;
        this.schedulingType = schedulingType;
        assignAlgorithm();
        this.results = null;
        this.records = new ArrayList<>();
        this.multiList = null;
        this.multiInput = null;
    }

    /** This constructs a new scheduler simulation with multiple queues (for MLQ, MLFQ).
     * @param multiInput The processes to schedule for each queue
     * @param algorithmType The type of scheduling algorithm being used
     * @param schedulingType Either preemptive or non-preemptive
     * @param multiList The types of scheduling algorithms used for each queue */
    public CpuSchedulerSimulation(ArrayList<SimulationInput> multiInput, AlgorithmTypes algorithmType, SchedulingTypes schedulingType,
                                  ArrayList<AlgorithmTypes> multiList) {
        this.input = null;
        this.multiInput = multiInput;
        this.multiList = multiList;
        this.algorithmType = algorithmType;
        this.schedulingType = schedulingType;
        assignAlgorithm();
        this.results = null;
        this.records = new ArrayList<>();
    }

    /** This method assigns an instance of a scheduling algorithm for the simulation based
     * on the given algorithm type. */
    private void assignAlgorithm() {
        if(algorithmType == AlgorithmTypes.FCFS) {
            this.algorithm = new FCFS(input);
        } else if(algorithmType == AlgorithmTypes.Priority) {
            this.algorithm = new Priority(input);
        } else if(algorithmType == AlgorithmTypes.SJF) {
            this.algorithm = new SJF(input);
        } else if(algorithmType == AlgorithmTypes.RR) {
            this.algorithm = new RR(input);
        } else if(algorithmType == AlgorithmTypes.MLQ) {
            this.algorithm = new MLQ(assignMultiAlgorithm());
        } else {
            this.algorithm = new MLFQ(assignMultiAlgorithm());
        }
    }

    /** This method is used to assign the algorithms that are used for the multiple queues of MLQ and MLFQ.
     * @return A list of the algorithm types (queues) in priority order. */
    private ArrayList<AlgorithmsInterface> assignMultiAlgorithm() {
        ArrayList<AlgorithmsInterface> readyList = new ArrayList<>();
        for(int i = 0; i < this.multiList.size(); i++) {
            if(this.multiList.get(i) == AlgorithmTypes.RR) {
                readyList.add(new RR(this.multiInput.get(i)));
            } else {
                readyList.add(new FCFS(this.multiInput.get(i)));
            }
        }
        return readyList;
    }

    /** This method gets the algorithm type.
     * @return The algorithm type */
    public AlgorithmTypes getAlgorithmType() { return this.algorithmType; }

    /** This method gets the scheduling type.
     * @return The algorithms scheduling type */
    public SchedulingTypes getSchedulingType() { return this.schedulingType; }

    /** This method gets the input for scheduling.
     * @return The algorithms scheduling input */
    public SimulationInput getInput() { return this.input; }

    /** This method gets the results for a scheduling simulation.
     * @return The scheduling simulation results */
    public SimulationResults getResults() { return this.results; }

    /** This method gets the (step-by-step) records for a scheduling simulation.
     * @return The scheduling simulation records */
    public ArrayList<SimulationRecord> getRecords() { return this.records; }

    /** This method runs an instance of a simulation. */
    public void runSim() {
        while(!this.algorithm.isCompleted()) {
            this.algorithm.scheduleNextProcess();
            createRecord();
            this.algorithm.dispatchNextProcess(this.algorithm.getDispatcher().getRunningProcess());
        }
        this.algorithm.getDispatcher().setRunningProcess(null);
        createRecord();
        this.algorithm.getScheduler().createFinalList();
        this.algorithm.getScheduler().setTurnAroundTimes();
        this.results = new SimulationResults(this.algorithm.getScheduler().getFinalList(),
                this.algorithm.getDispatcher().getExecutionTimer(),
                this.algorithm.getDispatcher().getIdleTimer());
    }

    /** This method creates the (step-by-step) records for a scheduling simulation. */
    public void createRecord() {
        LinkedHashMap<Integer, Integer> readyList = new LinkedHashMap<>();
        LinkedHashMap<Integer, Integer> QueueList = new LinkedHashMap<>();
        Queue<ProcessControlBlock> temp = new LinkedList<>();
        if(this.multiList == null) {
            while(!this.algorithm.getReady().isEmpty()) {
                ProcessControlBlock p = this.algorithm.getReady().poll();
                if(p != null) {
                    readyList.put(p.getPid(), p.getCpuBurstTime());
                    QueueList.put(p.getPid(), p.getPriority());
                    temp.add(p);
                }
            }
            while(!temp.isEmpty()) {
                this.algorithm.getReady().add(temp.poll());
            }
        } else {
            for(AlgorithmsInterface alg: this.algorithm.getScheduler().getReadyList()) {
                while(!alg.getReady().isEmpty()) {
                    ProcessControlBlock p = alg.getReady().poll();
                    if(p != null) {
                        readyList.put(p.getPid(), p.getCpuBurstTime());
                        QueueList.put(p.getPid(), p.getPriority());
                        temp.add(p);
                    }
                }
                while(!temp.isEmpty()) {
                    alg.getReady().add(temp.poll());
                }
            }
        }
        TreeMap<Integer, Integer> ioList = new TreeMap<>();
        for(ProcessControlBlock p : this.algorithm.getScheduler().getIo()) {
            ioList.put(p.getPid(), p.getIoTime());
        }
        ArrayList<Integer> completed = new ArrayList<>();
        for(ProcessControlBlock p : this.algorithm.getScheduler().getCompleted()) {
            completed.add(p.getPid());
        }
        Collections.sort(completed);
        int currProcess = -1;
        if(this.algorithm.getDispatcher().getRunningProcess() != null) {
            currProcess = this.algorithm.getDispatcher().getRunningProcess().getPid();
        }
        this.records.add(new SimulationRecord(this.algorithm.getDispatcher().getExecutionTimer(),
                currProcess, readyList, ioList, completed, this.algorithmType, QueueList));
    }
}