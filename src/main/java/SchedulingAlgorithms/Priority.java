/** Priority Scheduling
 * @author Yen */

package SchedulingAlgorithms;

import Util.Cpu;
import Util.Process;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Priority extends Algorithm{

    public Priority(Cpu cpu) {
        super(cpu);
    }

    @Override
    public void runExperiment() {

        Process curr = null;
        while(!isAllProcessesComplete())
        {
            curr = getHighestPriorityJob();
            if(curr == null) //there are no jobs currently running
            {
                cpu.increaseTime(1);
                continue;
            }

            cpu.requestExecuting(curr.getId());
            cpu.increaseTime(curr.getRoutine()[curr.currentRoutineIndex]);
        }

        System.out.println("Priority algorithm:" +
                "\nCPU Utilization: " + cpu.getCpuUtilization() +
                "\nAvg turn around time: %d" + cpu.getAvgTurnAroundTime() +
                "\nAvg response time: %d" + cpu.getAvgResponseTime() +
                "\nWait time: " + cpu.getAvgWaitTime());
    }

    /**
     * Among Ready processes, use a PriorityQueue to find the next one to be run
     *
     * @return a process to be run. Return null if no process is ready.
     */
    private Process getHighestPriorityJob()
    {
        Process highestPriority = null;

        PriorityQueue<Process> queue = new PriorityQueue<>((Comparator.comparingInt(Process::getPriority)));

        /*System.out.println(String.format(
                "Time %d Processes in queue (id - state - priority - startTime)",
                cpu.getTime()));
        */
        for (Process p : cpu.getProcessHashMap().values()) {
            /*System.out.println(String.format("%s - %s - %d - %d - %s",
                    p.getId(), p.getState().toString(), p.getPriority(), p.getStartTime(), Arrays.toString(p.getRoutine())));
            */
            if (p.getState() == Process.State.READY) {
                queue.add(p);
            }
        }
        highestPriority = queue.peek();
        return highestPriority;
    }

}

