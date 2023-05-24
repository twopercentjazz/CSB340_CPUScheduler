/** Round Robin
 * @author Riko */

package AdditionalExperimentation.SchedulingAlgorithms;

import AdditionalExperimentation.Util.Cpu;
import AdditionalExperimentation.Util.Process;

public class RR extends Algorithm{

        private static final int TIME_QUANTUM = 5;

        private int currentIndex;

        public RR(Cpu cpu) {
                super(cpu);
                currentIndex = 0;
        }

        @Override
        public void runExperiment() {

                Process curr = getNextReadyJob();
                Process next;

                writeToFile(cpu.getSnapShot(curr));

                while(!isAllProcessesComplete())
                {
                        next = getNextReadyJob();
                        if(curr == null)
                        {
                                writeToFile(cpu.getSnapShot(next));
                                curr = next;
                                cpu.increaseTime(1);
                                continue;
                        }
                        cpu.requestExecuting(curr.getId());
                        cpu.increaseTime(Math.min(curr.getRoutine()[curr.currentRoutineIndex], TIME_QUANTUM));
                        if(curr.getState() == Process.State.EXECUTING)
                        {
                                cpu.requestReady(curr.getId());
                        }
                        writeToFile(cpu.getSnapShot(next));
                        curr = next;
                }

                System.out.println(cpu.getPerformanceShot());
        }

        private Process getNextReadyJob()
        {
                if(currentIndex == processList.size()) currentIndex = 0;

                if(processList.stream().filter(P -> P.getState() == Process.State.READY).toList().size() == 0) return null;

                Process curr = processList.get(currentIndex);
                while(curr.isCompleted() || curr.getState() != Process.State.READY)
                {
                        if(++currentIndex >= processList.size()) currentIndex = 0;
                        curr = processList.get(currentIndex);
                }

                currentIndex++;

                return curr;
        }
}
