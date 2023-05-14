/** This class helps dispatch the next running process in a simulation (context switching). */

package AdditionalUtilities.Utilities;

public class Dispatcher {
    private int executionTimer;
    private int idleTimer;
    private ProcessControlBlock runningProcess;

    public Dispatcher() {
        this.executionTimer = 0;
        this.idleTimer = 0;
        this.runningProcess = null;
    }

    public int getExecutionTimer() {
        return this.executionTimer;
    }

    public void updateExecutionTimer(int time) {
        this.executionTimer += time;
    }

    public int getIdleTimer() {
        return this.idleTimer;
    }

    public void updateIdleTimer(int time) {
        this.idleTimer += time;
    }

    public ProcessControlBlock getRunningProcess() {
        return this.runningProcess;
    }

    public void setRunningProcess(ProcessControlBlock pcb) {
        this.runningProcess = pcb;
    }
}