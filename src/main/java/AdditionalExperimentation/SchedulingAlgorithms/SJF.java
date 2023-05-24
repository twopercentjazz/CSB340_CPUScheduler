/** Shortest job First
 * @author Riko */

package AdditionalExperimentation.SchedulingAlgorithms;

import AdditionalExperimentation.Util.Cpu;
import AdditionalExperimentation.Util.Process;

import java.util.HashMap;

public class SJF extends Algorithm{

    HashMap<String, Integer> processDurations;

    public SJF(Cpu cpu) {
        super(cpu);
        processDurations = new HashMap<>();
    }

    @Override
    public void runExperiment() {

        Process curr = getNextShortestJob(null);
        Process next;

        writeToFile(cpu.getSnapShot(curr));

        while(!isAllProcessesComplete())
        {

            next = getNextShortestJob(curr);

            if(curr == null)
            {
                writeToFile(cpu.getSnapShot(next));
                curr = next;
                cpu.increaseTime(1);    //there are no jobs currently running
                continue;
            }
            cpu.requestExecuting(curr.getId());
            cpu.increaseTime(curr.getRoutine()[curr.currentRoutineIndex]);
            writeToFile(cpu.getSnapShot(next));
            curr = next;
        }

        System.out.println(cpu.getPerformanceShot());
    }

    private Process getNextShortestJob(Process curr)
    {
        Process shortest = null;
        for (Process p : processList) {
            if(p.isCompleted() || p.getState() != Process.State.READY || p == curr) continue;
            if(shortest == null && p.getRoutine()[p.currentRoutineIndex] > 0) shortest = p;
            if(shortest != null && p.getRoutine()[p.currentRoutineIndex] < shortest.getRoutine()[shortest.currentRoutineIndex]) shortest = p;
        }
        return shortest;
    }
}
