/** This class manages the given inputs to run the simulations with. */

package AdditionalUtilities.Utilities;
import java.util.ArrayList;

public class SimulationInput {
    private ArrayList<ProcessControlBlock> input;
    private int size;
    private int[] givenInitialPriorities;
    private int givenTimeQuantum;

    public SimulationInput(ArrayList<ProcessControlBlock> input) {
        this.input = input;
        this.givenInitialPriorities = null;
        this.size = input.size();
    }

    public SimulationInput(ArrayList<ProcessControlBlock> input, int[] givenPriorities) {
        this(input);
        this.givenInitialPriorities = givenPriorities;
        setInitialPriorities();
    }

    public SimulationInput(ArrayList<ProcessControlBlock> input, int givenTimeQuantum) {
        this(input);
        this.givenTimeQuantum = givenTimeQuantum;
    }

    public ArrayList<ProcessControlBlock> getInput() {
        return this.input;
    }

    public int getSize() {
        return this.size;
    }

    public int[] getGivenInitialPriorities() {
        return this.givenInitialPriorities;
    }

    public int getGivenTimeQuantum() { return this.givenTimeQuantum; }

    public void setInitialPriorities() {
        for(int i = 0; i < input.size(); i++) {
            input.get(i).setPriority(givenInitialPriorities[i]);
        }
    }
}