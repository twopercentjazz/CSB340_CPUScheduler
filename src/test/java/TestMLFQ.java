/** MLFQ Tests
 * @author Chris */

import Algorithms.AlgorithmTypes;
import Utilities.CpuSchedulerSimulation;
import Utilities.ProcessControlBlock;
import Utilities.SchedulingTypes;
import Utilities.SimulationInput;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TestMLFQ {
    @Test
    @DisplayName("Test MLFQ Results")
    void TestMLFQResults() {
        // Run simulation with test Processes
        ProcessControlBlock p1 = new ProcessControlBlock(1, new int[]{6, 4, 8});
        ProcessControlBlock p2 = new ProcessControlBlock(2, new int[]{3, 10, 2});
        ArrayList<ProcessControlBlock> input1 = new ArrayList<>(Arrays.asList(p1,p2));
        CpuSchedulerSimulation test =  new CpuSchedulerSimulation(new ArrayList<>(Arrays.asList(new SimulationInput
                (input1, 5), new SimulationInput(new ArrayList<>(),10), new
                SimulationInput(new ArrayList<>()))), AlgorithmTypes.MLFQ, SchedulingTypes.PREEMPTIVE,
                new ArrayList<>(Arrays.asList(AlgorithmTypes.RR, AlgorithmTypes.RR, AlgorithmTypes.FCFS)));
        test.runSim();

        /** Test CPU Utilization */
        Assertions.assertEquals(19.0/23.0, test.getResults().getCpuUtilization());

        /** Test Average Wait Times */
        Assertions.assertEquals(10.0/2.0, test.getResults().getAvgWait());

        /** Test Average Turnaround Times */
        Assertions.assertEquals(43.0/2.0, test.getResults().getAvgTurnAround());

        /** Test Average Response Times */
        Assertions.assertEquals(5.0/2.0, test.getResults().getAvgResponse());
    }
}
