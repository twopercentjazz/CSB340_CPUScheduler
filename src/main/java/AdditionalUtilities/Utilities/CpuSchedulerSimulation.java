/** This class represents an instance of a CPU scheduling simulation. */

package AdditionalUtilities.Utilities;
import AdditionalUtilities.Algorithms.*;
import java.util.*;

public class CpuSchedulerSimulation {
    private SimulationInput input;
    private AlgorithmTypes algorithmType;
    private SchedulingTypes schedulingType;
    private AlgorithmsInterface algorithm;
    private SimulationResults results;
    private ArrayList<SimulationRecord> records;

    public CpuSchedulerSimulation(SimulationInput input, AlgorithmTypes algorithmType, SchedulingTypes schedulingType) {
        this.input = input;
        this.algorithmType = algorithmType;
        this.schedulingType = schedulingType;
        assignAlgorithm(input, algorithmType);
        this.results = null;
        this.records = new ArrayList<>();
    }

    private void assignAlgorithm(SimulationInput input, AlgorithmTypes algorithmType) {
        if(algorithmType == AlgorithmTypes.FCFS) {
            this.algorithm = new FCFS(input);
        } else if(algorithmType == AlgorithmTypes.P) {
            this.algorithm = new Priority(input);
        } else if(algorithmType == AlgorithmTypes.SJF) {
            this.algorithm = new SJF(input);
        }
        // add RR
        // add MLQ
        // add MLFQ
    }

    public SimulationInput getInput() {
        return this.input;
    }

    public SimulationResults getResults() {
        return this.results;
    }

    public ArrayList<SimulationRecord> getRecords() {
        return this.records;
    }

    public void runSim() {
        while(!algorithm.isCompleted()) {
            this.algorithm.scheduleNextProcess();
            createRecord();
            this.algorithm.dispatchNextProcess(this.algorithm.getDispatcher().getRunningProcess());
        }
        this.algorithm.getDispatcher().setRunningProcess(null);
        createRecord();
        this.algorithm.getScheduler().createFinalList();
        this.algorithm.getScheduler().setTurnAroundTimes();
        results = new SimulationResults(algorithm.getScheduler().getFinalList(),
                algorithm.getDispatcher().getExecutionTimer(),
                algorithm.getDispatcher().getIdleTimer());
    }

    public void createRecord() {
        LinkedHashMap<Integer, Integer> readyList = new LinkedHashMap<>();
        for(ProcessControlBlock p : this.algorithm.getReady()) {
            readyList.put(p.getPid(), p.getCpuBurstTime());
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
        this.records.add(new SimulationRecord(this.algorithm.getDispatcher().getExecutionTimer(), currProcess,
                readyList, ioList, completed));
    }
}