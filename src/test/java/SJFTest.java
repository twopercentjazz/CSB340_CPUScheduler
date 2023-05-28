import Algorithms.AlgorithmTypes;
import Utilities.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SJFTest {

    static SimulationResults result;

    @BeforeEach
    void setUp()
    {
        ProcessControlBlock p1 = new ProcessControlBlock(1, new int[]{5, 2, 3});
        ProcessControlBlock p2 = new ProcessControlBlock(2, new int[]{3, 3, 1});
        ProcessControlBlock p3 = new ProcessControlBlock(3, new int[]{5, 2, 7});

        ArrayList<ProcessControlBlock> input = new ArrayList<>(Arrays.asList(p1,p2,p3));

        CpuSchedulerSimulation SJF = new CpuSchedulerSimulation(new SimulationInput(input, new int[]{3,2,1}),
                AlgorithmTypes.SJF, SchedulingTypes.NON_PREEMPTIVE);

        SJF.runSim();

        result = SJF.getResults();
    }

    @Test
    void CpuUtilizationTest()
    {
        assertEquals(1, result.getCpuUtilization(), 0.1d);
    }

    @Test
    void AvgWaitTimeTest()
    {
        assertEquals(7.61, result.getAvgWait(), 0.1d);
    }

    @Test
    void AvgTurnAroundTime()
    {
        assertEquals(18, result.getAvgTurnAround(), 0.1d);
    }

    @Test
    void AvgResponseTime()
    {
        assertEquals(4, result.getAvgResponse(), 0.1d);
    }
}
