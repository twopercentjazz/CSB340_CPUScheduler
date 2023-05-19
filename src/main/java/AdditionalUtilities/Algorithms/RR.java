/** Round Robin */

package AdditionalUtilities.Algorithms;

import AdditionalUtilities.Utilities.CpuSchedulerSimulation;
import AdditionalUtilities.Utilities.ProcessControlBlock;
import AdditionalUtilities.Utilities.SimulationInput;
import AdditionalUtilities.Utilities.SimulationResults;

public class RR extends Algorithm {

    private static final int TIME_QUANTUM = 5;
    public RR(SimulationInput input, CpuSchedulerSimulation simulation) {
        super(input, simulation);
    }
    @Override
    public void runSim() {
        while(!isCompleted()) {
            scheduleNextProcess();
            simulation.createRecord(0);
            dispatchNextProcess(0, getDispatcher().getRunningProcess(), TIME_QUANTUM);
        }
        getDispatcher().setRunningProcess(null);
        simulation.createRecord(0);
        getScheduler().createFinalList();
        getScheduler().setTurnAroundTimes();
        simulation.setResults(new SimulationResults(getScheduler().getFinalList(),
                getDispatcher().getExecutionTimer(),
                getDispatcher().getIdleTimer()));
    }

    @Override
    public AlgorithmTypes getAlgorithmType() {
        return AlgorithmTypes.RR;
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