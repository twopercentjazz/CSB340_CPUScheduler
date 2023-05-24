/** This interface represents all algorithm types. */

package AdditionalUtilities.Algorithms;
import AdditionalUtilities.Utilities.*;
import java.util.Queue;

public interface AlgorithmsInterface {

    /** This method gets the type of scheduling algorithm.
     * @return the algorithm type */
    AlgorithmTypes getAlgorithmType();

    /** This method schedules the next running process. */
    void scheduleNextProcess();

    /** This method dispatches the next running process, performing context switches.
     * @param running the next running process */
    void dispatchNextProcess(ProcessControlBlock running);

    /** This method signals when all scheduling is completed.
     * @return true if there are no more processes to schedule */
    Boolean isCompleted();

    /** This method gets the scheduler class that the algorithm uses.
     * @return the algorithms scheduler */
    Scheduler getScheduler();

    /** This method gets the dispatcher class that the algorithm uses.
     * @return the algorithms dispatcher */
    Dispatcher getDispatcher();

    /** This method gets the algorithms ready queue.
     * @return ready queue of algorithm. */
    Queue<ProcessControlBlock> getReady();
}