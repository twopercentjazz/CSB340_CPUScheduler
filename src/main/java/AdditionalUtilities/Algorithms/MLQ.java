/** Multilevel Queue */

package AdditionalUtilities.Algorithms;

import AdditionalUtilities.Utilities.CpuSchedulerSimulation;
import AdditionalUtilities.Utilities.SimulationInput;
import AdditionalUtilities.Utilities.SimulationResults;

public class MLQ extends Algorithm{

    public MLQ(SimulationInput input, CpuSchedulerSimulation simulation) {
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

    @Override
    public AlgorithmTypes getAlgorithmType() {
        return AlgorithmTypes.MLQ;
    }

    @Override
    public void scheduleNextProcess() {

    }
}
