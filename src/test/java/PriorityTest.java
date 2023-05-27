import AdditionalExperimentation.SchedulingAlgorithms.Priority;
import Algorithms.AlgorithmTypes;
import Utilities.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriorityTest {


    @Test
    public void PriorityTest() {
        ProcessControlBlock p1 = new ProcessControlBlock(1, new int[]{5, 2, 3});
        ProcessControlBlock p2 = new ProcessControlBlock(2, new int[]{3, 3, 1});
        ProcessControlBlock p3 = new ProcessControlBlock(3, new int[]{5, 2, 7});
        ArrayList<ProcessControlBlock> input = new ArrayList<>(Arrays.asList(p1,p2,p3));

        CpuSchedulerSimulation Priority = new CpuSchedulerSimulation(new SimulationInput(input, new int[]{3,2,1}),
                AlgorithmTypes.Priority, SchedulingTypes.PREEMPTIVE);
                // new CpuSchedulerSimulation(new ArrayList<>(Arrays.asList(new SimulationInput(input), new int[]{3,2,1})), AlgorithmTypes.Priority, SchedulingTypes.PREEMPTIVE);

        Priority.runSim();

        SimulationResults result = Priority.getResults();

        assertEquals(0.92, result.getCpuUtilization(), 0.01d);
        assertEquals(9.33, result.getAvgWait(), 0.01d);
        assertEquals(19.67, result.getAvgTurnAround(), 0.01d);
        assertEquals(6.67, result.getAvgResponse(), 0.01d);
    }

}
