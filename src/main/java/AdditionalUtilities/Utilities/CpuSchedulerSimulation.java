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

        switch (algorithmType)
        {
            case SJF -> algorithm = new SJF(input, this);
            case RR -> algorithm = new RR(input, this);
            case FCFS -> algorithm = new FCFS(input, this);
            case P -> algorithm = new Priority(input, this);
            case MLQ -> algorithm = new MLQ(input, this);
            case MLFQ -> algorithm = new MLFQ(input, this);
        }
    }

    public SimulationInput getInput() {
        return this.input;
    }

    public SimulationResults getResults() {
        return this.results;
    }
    public void setResults(SimulationResults results)
    {
        this.results = results;
    }

    public ArrayList<SimulationRecord> getRecords() {
        return this.records;
    }

    public void runSim() {
        algorithm.runSim();
    }

    public void createRecord(int indexOfQueue) {
        LinkedHashMap<Integer, Integer> readyList = new LinkedHashMap<>();
        for(ProcessControlBlock p : this.algorithm.getReady(indexOfQueue)) {
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

    public AlgorithmTypes getAlgorithmType()
    {
        return algorithmType;
    }
}