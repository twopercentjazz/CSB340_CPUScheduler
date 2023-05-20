/** This class manages all the records for each concurrent step in a scheduling simulation. */

package AdditionalUtilities.Utilities;
import AdditionalUtilities.Algorithms.AlgorithmTypes;

import java.util.*;

public class SimulationRecord {
    private int currentExecutionTime;
    private int runningProcess;
    private LinkedHashMap<Integer, Integer> ready;
    private TreeMap<Integer, Integer> io;
    private ArrayList<Integer> completed;
    private LinkedHashMap<Integer, Integer> priority;
    private AlgorithmTypes type;

    public SimulationRecord(int currExecutionTime, int runningProcess, LinkedHashMap<Integer, Integer> ready,
                            TreeMap<Integer, Integer> io, ArrayList<Integer> completed, AlgorithmTypes type,
                            LinkedHashMap<Integer, Integer> priority) {
        this.currentExecutionTime = currExecutionTime;
        this.runningProcess = runningProcess;
        this.ready = ready;
        this.io = io;
        this.completed = completed;
        this.type = type;
        this.priority = priority;
    }

    public int getCurrentExecutionTime() {
        return this.currentExecutionTime;
    }

    public int getRunningProcess() {
        return this.runningProcess;
    }

    public LinkedHashMap<Integer, Integer> getReady() {
        return this.ready;
    }

    public TreeMap<Integer, Integer> getIo() {
        return this.io;
    }

    public ArrayList<Integer> getCompleted() {
        return this.completed;
    }
    public LinkedHashMap<Integer, Integer> getPriority() {
        return this.priority;
    }
    public AlgorithmTypes getType() { return this.type; }
}