/** Priority Scheduling
 * @author Yen */

package SchedulingAlgorithms;

import Util.Cpu;
import Util.Process;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Priority extends Algorithm{

    public Priority(Cpu cpu) {
        super(cpu);
    }

    @Override
    public void runExperiment() {

        Process curr =  getHighestPriorityJob(null);
        Process next = null;
        writeToFile(cpu.getSnapShot(curr));

        while(!isAllProcessesComplete())
        {
            next = getHighestPriorityJob(curr);
            if(curr == null) //there are no jobs currently running
            {
                writeToFile(cpu.getSnapShot(next));
                curr = next;
                cpu.increaseTime(1);
                continue;
            }
            cpu.requestExecuting(curr.getId());
            cpu.increaseTime(curr.getRoutine()[curr.currentRoutineIndex]);
            writeToFile(cpu.getSnapShot(next));
            curr = next;
        }

        System.out.println(cpu.getPerformanceShot());
    }

    /**
     * Among Ready processes, use a PriorityQueue to find the next one to be run
     *
     * @return a process to be run. Return null if no process is ready.
     */
    private Process getHighestPriorityJob(Process curr)
    {
        PriorityQueue<Process> queue = new PriorityQueue<>((Comparator.comparingInt(Process::getPriority)));

        /*System.out.println(String.format(
                "Time %d Processes in queue (id - state - priority - startTime)",
                cpu.getTime()));
        */
        processList.stream().filter(p -> p.getState() == Process.State.READY && p != curr).forEach(p -> queue.add(p));
        return queue.peek();
    }

}

