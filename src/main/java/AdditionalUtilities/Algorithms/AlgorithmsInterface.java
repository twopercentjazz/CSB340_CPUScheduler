/** This interface represents all algorithm types. */

package AdditionalUtilities.Algorithms;
import AdditionalUtilities.Utilities.*;
import java.util.Queue;

public interface AlgorithmsInterface {

    AlgorithmTypes getAlgorithmType();

    void scheduleNextProcess();

    void dispatchNextProcess(int indexOfQueue, ProcessControlBlock running, Integer timeQuantum);

    Boolean isCompleted();

    Scheduler getScheduler();

    Dispatcher getDispatcher();

    Queue<ProcessControlBlock> getReady(int indexOfQueue);

    void runSim();
}