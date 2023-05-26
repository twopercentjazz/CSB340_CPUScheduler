import Algorithms.AlgorithmTypes;
import Utilities.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MLQTest {


    @Test
    public void MLQTest() {
        ProcessControlBlock p1 = new ProcessControlBlock(1, new int[]{5, 2, 3});
        ProcessControlBlock p2 = new ProcessControlBlock(2, new int[]{3, 3, 1});
        ProcessControlBlock p3 = new ProcessControlBlock(3, new int[]{5, 2, 7});
        ArrayList<ProcessControlBlock> input1 = new ArrayList<>(Arrays.asList(p1,p2));
        ArrayList<ProcessControlBlock> input2 = new ArrayList<>(Arrays.asList(p3));

        CpuSchedulerSimulation MLQ = new CpuSchedulerSimulation(new ArrayList<>(Arrays.asList(new
                SimulationInput(input1, 4), new SimulationInput(input2))), AlgorithmTypes.MLQ, SchedulingTypes.PREEMPTIVE, new
                ArrayList<>(Arrays.asList(AlgorithmTypes.RR, AlgorithmTypes.FCFS)));
        MLQ.runSim();

        SimulationResults result = MLQ.getResults();
        /*System.out.println(result.getCpuUtilization());
        System.out.println(result.getAvgWait());
        System.out.println(result.getAvgTurnAround());
        System.out.println(result.getAvgResponse());*/


        assertEquals(0.86, result.getCpuUtilization(), 0.01d);
        assertEquals(8.0, result.getAvgWait());
        assertEquals(18.33, result.getAvgTurnAround(), 0.01d);
        assertEquals(6.0, result.getAvgResponse());
    }

}
