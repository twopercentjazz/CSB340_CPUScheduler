/** Shortest job First
 * @author Riko */

package SchedulingAlgorithms;

import Util.Cpu;
import Util.Process;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class SJF extends Algorithm{

    HashMap<String, Integer> processDurations;

    public SJF(Cpu cpu) {
        super(cpu);
        processDurations = new HashMap<>();
    }

    @Override
    public void runExperiment() {

        Process curr = null;
        while(!isAllProcessesComplete())
        {
            curr = getNextShortestJob();
            if(curr == null)
            {
                cpu.increaseTime(1);    //there are no jobs currently running
                continue;
            }
            cpu.requestExecuting(curr.getId());
            cpu.increaseTime(curr.getRoutine()[curr.currentRoutineIndex]);
        }

        System.out.println(cpu.getCpuUtilization());
        System.out.println(cpu.getAvgTurnAroundTime());
        System.out.println(cpu.getAvgResponseTime());
        System.out.println(cpu.getAvgWaitTime());
    }

    private Process getNextShortestJob()
    {
        Process shortest = null;
        for (Process p : processList) {
            if(p.isCompleted() || p.getState() != Process.State.READY) continue;
            if(shortest == null && p.getRoutine()[p.currentRoutineIndex] > 0) shortest = p;
            if(shortest != null && p.getRoutine()[p.currentRoutineIndex] < shortest.getRoutine()[shortest.currentRoutineIndex]) shortest = p;
        }
        return shortest;
    }
}
