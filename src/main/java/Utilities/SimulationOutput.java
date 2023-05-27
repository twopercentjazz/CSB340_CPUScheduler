/** This class creates strings representations of a simulations results and records for output. */

package Utilities;
import Algorithms.AlgorithmTypes;

public class SimulationOutput {

    /** This method displays the final results.
     * @param results The results from a scheduling simulation
     * @return A string representation of the results */
    public static String displayResults(SimulationResults results) {
        String result = "";
        String labels = processList(results);
        result += String.format("\n%-20s", "Finished");
        result += String.format("\n\n%-20s%d", "Total Time:", results.getTotalExecutionTime());
        result += String.format("\n%-20s%.4f%%", "CPU Utilization:", results.getCpuUtilization() * 100);
        result += String.format("\n\n%-20s%s", "Waiting Times", labels);
        result += String.format("\n%-20s", "");
        for(Integer i : results.getAllWaitTimes()) {
            result += String.format("%-5d", i);
        }
        result += String.format("\n%-20s%.3f", "Average Wait:", results.getAvgWait());
        result += String.format("\n\n%-20s%s", "Turnaround Times", labels);
        result += String.format("\n%-20s", "");
        for(Integer i : results.getAllTurnAroundTimes()) {
            result += String.format("%-5d", i);
        }
        result += String.format("\n%-20s%.3f", "Average Turnaround:", results.getAvgTurnAround());
        result += String.format("\n\n%-20s%s", "Response Times", labels);
        result += String.format("\n%-20s", "");
        for(Integer i : results.getAllResponseTimes()) {
            result += String.format("%-5d", i);
        }
        result += String.format("\n%-20s%.3f\n", "Average Response:", results.getAvgResponse());
        return result;
    }

    /** This method creates labels for the list of processes used in the simulation.
     * @param results The results from a scheduling simulation
     * @return A formatted list of processes */
    public static String processList(SimulationResults results) {
        String temp = "";
        for(int i = 1; i <= results.getAllWaitTimes().size(); i++) {
            temp += String.format("P%-4d", i);
        }
        return temp;
    }

    /** This method displays a record (for one concurrent step of the simulation).
     * @param record The record from a scheduling simulation
     * @return A string representation of the record */
    public static String displayRecord(SimulationRecord record) {
        String rec = "";
        rec += String.format("\n\n\n%-14s%d", "Current Time:", record.getCurrentExecutionTime());
        String process = "";
        if(record.getRunningProcess() == -1) {
            process += String.format("%s", "[idle]");
        } else {
            process += String.format("P%d", record.getRunningProcess());
        }
        rec += String.format("\n\n%-26s%s", "Next process on the CPU:", process);
        rec += line();
        rec += String.format("\n\n%s\n\n", "List of processes in the ready queue:");
        rec += String.format("%-8s%-11s%-9s", "", "Process", "Burst");
        if(record.getType() == AlgorithmTypes.MLQ || record.getType() == AlgorithmTypes.MLFQ) {
            rec += String.format("%s", "Queue");
        }
        if(record.getReady().isEmpty()) {
            rec += String.format("\n%-14s%s", "", "[empty]");
        } else {
            if(record.getType() != AlgorithmTypes.MLQ && record.getType() != AlgorithmTypes.MLFQ) {
                for(Integer i : record.getReady().keySet()) {
                    rec += String.format("\n%-10sP%-9d%d", "", i, record.getReady().get(i));
                }
            } else {
                for(Integer i : record.getReady().keySet()) {
                    rec += String.format("\n%-10sP%-9d%-10dQ%d", "", i, record.getReady().get(i),
                            (record.getPriority().get(i) + 1));
                }
            }
        }
        rec += line();
        rec += String.format("\n\n%s\n\n", "List of processes in I/O:");
        rec += String.format("%-8s%-11s%-9s", "", "Process", "Remaining I/O time");
        if(record.getIo().isEmpty()) {
            rec += String.format("\n%-14s%s", "", "[empty]");
        } else {
            for(Integer i : record.getIo().keySet()) {
                rec += String.format("\n%-10sP%-9d%d", "", i, record.getIo().get(i));
            }
        }
        if(!record.getCompleted().isEmpty()) {
            rec += line();
            rec += String.format("\n\n%-12s", "Completed:");
            for(Integer i : record.getCompleted()) {
                rec += String.format("P%-3d", i);
            }
        }
        rec += String.format("\n\n%s", "::::::::::::::::::::::::::::::::::::::::::::::::::");
        return rec;
    }

    /** This method draws a line for formatting output.
     * @return The line */
    public static String line() {
        return "\n..................................................";
    }
}