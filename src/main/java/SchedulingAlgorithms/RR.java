/** Round Robin
 * @author Riko */

package SchedulingAlgorithms;

import Util.Cpu;

public class RR extends Algorithm{
        public RR(Cpu cpu) {
                super(cpu);
        }

        @Override
        public void runExperiment() {
                System.out.println("Also running");
        }
}
