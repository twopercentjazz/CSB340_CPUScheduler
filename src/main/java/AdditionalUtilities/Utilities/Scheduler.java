/** This class helps schedule the next running process in a simulation. */

package AdditionalUtilities.Utilities;
import java.util.ArrayList;

public class Scheduler {
    private ArrayList<ProcessControlBlock> io; //
    private ArrayList<ProcessControlBlock> active; //
    private ArrayList<ProcessControlBlock> completed; //
    private ProcessControlBlock[] finalList;

    public Scheduler(SimulationInput input) {
        this.io = new ArrayList<>();
        this.completed = new ArrayList<>();
        this.active = input.getInput();
        this.finalList = new ProcessControlBlock[input.getSize()];
    }

    public ArrayList<ProcessControlBlock> getIo() { return this.io; }

    public ArrayList<ProcessControlBlock> getActive() {
        return this.active;
    }

    public ArrayList<ProcessControlBlock> getCompleted() {
        return this.completed;
    }

    /**
     * Move a process from active to completed list.
     * @param pcb a recently completed process
     */
    public void flagProcessAsComplete(ProcessControlBlock pcb) {
        pcb.setState(ProcessControlBlock.ProcessState.COMPLETE);
        active.remove(pcb);
        completed.add(pcb);
    }

    /**
     * Create a final list of completed processes.
     * Useful for calculating results.
     */
    public void createFinalList() {
        for(ProcessControlBlock p : completed) {
            finalList[p.getPid() - 1] = p;
        }
    }

    public ProcessControlBlock[] getFinalList() {
        return this.finalList;
    }

    /**
     * Make sure all processes have turn around times are updated.
     * Use before calculating simulation results.
     */
    public void setTurnAroundTimes() {
        for(ProcessControlBlock p : finalList) {
            p.updateTurnAroundTime();
        }
    }
}