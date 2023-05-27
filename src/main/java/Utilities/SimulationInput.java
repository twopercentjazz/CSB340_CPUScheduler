/** This class manages the given inputs to run the simulations with. */

package Utilities;
import java.util.ArrayList;

public class SimulationInput {
    private ArrayList<ProcessControlBlock> input;
    private int size;
    private int[] givenInitialPriorities;
    private int givenTimeQuantum;

    /** Constructs an input object without priorities or a time quantum.
     * @param input The input processes */
    public SimulationInput(ArrayList<ProcessControlBlock> input) {
        this.input = input;
        this.givenInitialPriorities = null;
        this.size = input.size();
    }

    /** Constructs an input object with priorities.
     * @param input The input processes
     * @param givenPriorities The given priorities */
    public SimulationInput(ArrayList<ProcessControlBlock> input, int[] givenPriorities) {
        this(input);
        this.givenInitialPriorities = givenPriorities;
        setInitialPriorities();
    }

    /** Constructs an input object with a time quantum.
     * @param input The input processes
     * @param givenTimeQuantum The given time quantum */
    public SimulationInput(ArrayList<ProcessControlBlock> input, int givenTimeQuantum) {
        this(input);
        this.givenTimeQuantum = givenTimeQuantum;
    }

    /** This method gets the processes used as input.
     * @return The processes used as input */
    public ArrayList<ProcessControlBlock> getInput() {
        return this.input;
    }

    /** This method gets the number of processes used (the size of the input list).
     * @return The number of processes used */
    public int getSize() {
        return this.size;
    }

    /** This method gets the list of initial priorities.
     * @return The list of initial priorities */
    public int[] getGivenInitialPriorities() { return this.givenInitialPriorities; }

    /** This method gets the given time quantum.
     * @return The given time quantum */
    public int getGivenTimeQuantum() { return this.givenTimeQuantum; }

    /** This method sets the processes priorities with the given initial priorities. */
    public void setInitialPriorities() {
        for(int i = 0; i < input.size(); i++) {
            input.get(i).setPriority(givenInitialPriorities[i]);
        }
    }
}