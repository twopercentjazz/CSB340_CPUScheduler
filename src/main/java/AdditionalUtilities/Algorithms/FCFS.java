/** First Come, First Served CPU Scheduling Algorithm
 * @author Chris */

package AdditionalUtilities.Algorithms;
import AdditionalUtilities.Utilities.*;
import java.util.*;

public class FCFS extends Algorithm {

    public FCFS(SimulationInput input, CpuSchedulerSimulation simulation) {
        super(input, simulation);
    }

    @Override
    public void runSim() {
        while(!isCompleted()) {
            scheduleNextProcess();
            simulation.createRecord(0);
            dispatchNextProcess(0, getDispatcher().getRunningProcess(), null);
        }
        getDispatcher().setRunningProcess(null);
        simulation.createRecord(0);
        getScheduler().createFinalList();
        getScheduler().setTurnAroundTimes();
        simulation.setResults(new SimulationResults(getScheduler().getFinalList(),
                getDispatcher().getExecutionTimer(),
                getDispatcher().getIdleTimer()));
    }

    /** {@inheritDoc} */
    public AlgorithmTypes getAlgorithmType() {
        return AlgorithmTypes.FCFS;
    }

    /** {@inheritDoc} */
    public void scheduleNextProcess() {
        ProcessControlBlock next = readyQueues.get(0).poll();
        if(next != null) {
            next.setState(ProcessControlBlock.ProcessState.RUNNING);
            this.dispatch.setRunningProcess(next);
        } else {
            this.dispatch.setRunningProcess(null);
        }
    }
}
