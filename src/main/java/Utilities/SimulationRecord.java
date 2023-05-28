/** This class manages all the records (stored using ints) for each concurrent step in a scheduling simulation. */

package Utilities;
import Algorithms.AlgorithmTypes;
import java.util.*;

public class SimulationRecord {
    private int currentExecutionTime;
    private int runningProcess;
    private LinkedHashMap<Integer, Integer> ready;
    private TreeMap<Integer, Integer> io;
    private ArrayList<Integer> completed;
    private LinkedHashMap<Integer, Integer> priority;
    private AlgorithmTypes type;

    /** Constructs an instance of a simulation record.
     * @param currExecutionTime The simulations current execution time
     * @param runningProcess The next running process
     * @param ready The ready queue of the algorithm
     * @param io The simulations current processes in IO
     * @param completed The simulations completed processes
     * @param type The algorithms type
     * @param priority The queues the processes belong to */
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

    /** This method gets the current execution time.
     * @return The current execution time */
    public int getCurrentExecutionTime() {
        return this.currentExecutionTime;
    }

    /** This method gets the pid of the next running process.
     * @return The next running processes pid */
    public int getRunningProcess() {
        return this.runningProcess;
    }

    /** This method gets the current ready queue representation, with a pid and CPU burst time.
     * @return A map of the ready process pid numbers and CPU burst times */
    public LinkedHashMap<Integer, Integer> getReady() {
        return this.ready;
    }

    /** This method gets the current IO list representation, with a pid and IO burst time.
     * @return A map of the IO process pid numbers and IO burst times */
    public TreeMap<Integer, Integer> getIo() {
        return this.io;
    }

    /** This method gets the current completed processes list representation, using pid numbers.
     * @return The pid numbers of the completed processes */
    public ArrayList<Integer> getCompleted() {
        return this.completed;
    }

    /** This method gets the current processes queue priority (for multi queue algorithms),
     * using pid numbers and priority numbers.
     * @return A map of the process pid numbers and queue priorities */
    public LinkedHashMap<Integer, Integer> getPriority() {
        return this.priority;
    }

    /** This method gets the algorithms type.
     * @return The type of the scheduling algorithm */
    public AlgorithmTypes getType() { return this.type; }
}