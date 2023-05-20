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
    private ArrayList<AlgorithmTypes> multiList;
    private ArrayList<SimulationInput> multiInput;

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

    private void assignAlgorithm() {
        if(algorithmType == AlgorithmTypes.FCFS) {
            this.algorithm = new FCFS(input);
        } else if(algorithmType == AlgorithmTypes.Priority) {
            this.algorithm = new Priority(input);
        } else if(algorithmType == AlgorithmTypes.SJF) {
            this.algorithm = new SJF(input);
        } else if(algorithmType == AlgorithmTypes.RR) {
            this.algorithm = new RR(input);
        } else if(algorithmType == AlgorithmTypes.MLQ){
            this.algorithm = new MLQ(assignMultiAlgorithm());
        } else {
            this.algorithm = new MLFQ(assignMultiAlgorithm());
        }
    }

    private ArrayList<AlgorithmsInterface> assignMultiAlgorithm() {
        ArrayList<AlgorithmsInterface> readyList = new ArrayList<>();
        for(int i = 0; i < multiList.size(); i++) {
            if(multiList.get(i) == AlgorithmTypes.RR) {
                readyList.add(new RR(multiInput.get(i)));
            } else {
                readyList.add(new FCFS(multiInput.get(i)));
            }
        }
        return readyList;
    }

    public AlgorithmTypes getAlgorithmType() {
        return this.algorithmType;
    }

    public SchedulingTypes getSchedulingType() {
        return this.schedulingType;
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
        LinkedHashMap<Integer, Integer> priorityList = new LinkedHashMap<>();
        Queue<ProcessControlBlock> temp = new LinkedList<>();
        if(multiList == null) {
            while(!this.algorithm.getReady().isEmpty()) {
                ProcessControlBlock p = this.algorithm.getReady().poll();
                readyList.put(p.getPid(), p.getCpuBurstTime());
                priorityList.put(p.getPid(), p.getPriority());
                temp.add(p);
            }
            while(!temp.isEmpty()) {
                this.algorithm.getReady().add(temp.poll());
            }
        } else {
            for(AlgorithmsInterface alg: algorithm.getScheduler().getReadyList()) {
                while(!alg.getReady().isEmpty()) {
                    ProcessControlBlock p = alg.getReady().poll();
                    readyList.put(p.getPid(), p.getCpuBurstTime());
                    priorityList.put(p.getPid(), p.getPriority());
                    temp.add(p);
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
                currProcess, readyList, ioList, completed, algorithmType, priorityList));
    }
}