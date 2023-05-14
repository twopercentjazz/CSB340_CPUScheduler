/** This class manages all the records for each concurrent step in a scheduling simulation. */

package AdditionalUtilities.Utilities;
import java.util.*;

public class SimulationRecord {
    private int currentExecutionTime;
    private int runningProcess;
    private LinkedHashMap<Integer, Integer> ready;
    private TreeMap<Integer, Integer> io;
    private ArrayList<Integer> completed;

    public SimulationRecord(int currExecutionTime, int runningProcess, LinkedHashMap<Integer, Integer> ready,
                            TreeMap<Integer, Integer> io, ArrayList<Integer> completed) {
        this.currentExecutionTime = currExecutionTime;
        this.runningProcess = runningProcess;
        this.ready = ready;
        this.io = io;
        this.completed = completed;
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
}