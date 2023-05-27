/** FCFS Tests
 * @author Chris */

import Algorithms.AlgorithmTypes;
import Utilities.*;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TestFCFS {
    @Test
    @DisplayName("Test FCFS Results")
    void TestFCFSResults() {
        // Run simulation with test Processes
        ProcessControlBlock p1 = new ProcessControlBlock(1, new int[]{6, 4, 8});
        ProcessControlBlock p2 = new ProcessControlBlock(2, new int[]{3, 10, 2});
        ArrayList<ProcessControlBlock> input1 = new ArrayList<>(Arrays.asList(p1,p2));
        CpuSchedulerSimulation test =  new CpuSchedulerSimulation(new SimulationInput(input1), AlgorithmTypes.FCFS,
                SchedulingTypes.NON_PREEMPTIVE);
        test.runSim();

        /** Test CPU Utilization */
        Assertions.assertEquals(19.0/21.0, test.getResults().getCpuUtilization());

        /** Test Average Wait Times */
        Assertions.assertEquals(6.0/2.0, test.getResults().getAvgWait());

        /** Test Average Turnaround Times */
        Assertions.assertEquals(39.0/2.0, test.getResults().getAvgTurnAround());

        /** Test Average Response Times */
        Assertions.assertEquals(6.0/2.0, test.getResults().getAvgResponse());
    }
}
