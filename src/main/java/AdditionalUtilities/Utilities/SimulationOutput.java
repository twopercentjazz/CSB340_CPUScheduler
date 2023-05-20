/** This class creates strings representations of a simulations results and records for output. */

package AdditionalUtilities.Utilities;

import AdditionalUtilities.Algorithms.AlgorithmTypes;

public class SimulationOutput {

    public static String displayResults(SimulationResults results) {
        String result = "";
        String labels = processList(results);
        String space = "\t\t\t\t\t";
        result += "\nFinished\n";
        result += "\nTotal Time:\t\t\t" + results.getTotalExecutionTime();
        result += String.format("\nCPU Utilization:\t%.4f%%", results.getCpuUtilization() * 100);
        result += "\n\nWaiting Times\t\t" + labels;
        result += "\n" + space;
        for(Integer i : results.getAllWaitTimes()) {
            result += i + "\t";
        }
        result += "\nAverage Wait:\t\t" + results.getAvgWait();
        result += "\n\nTurnaround Times\t" + labels;
        result += "\n" + space;
        for(Integer i : results.getAllTurnAroundTimes()) {
            result += i + "\t";
        }
        result += "\nAverage Turnaround:\t" + results.getAvgTurnAround();
        result += "\n\nResponse Times\t\t" + labels;
        result += "\n" + space;
        for(Integer i : results.getAllResponseTimes()) {
            result += i + "\t";
        }
        result += "\nAverage Response:\t" + results.getAvgResponse() + "\n";
        return result;
    }

    public static String processList(SimulationResults results) {
        String temp = "";
        for(int i = 1; i <= results.getAllWaitTimes().size(); i++) {
            temp += "P" + i + "\t";
        }
        return temp;
    }

    public static String displayRecord(SimulationRecord record) {
        String rec = "";
        rec += "\n\nCurrent Time: " + record.getCurrentExecutionTime();
        String process = "";
        if(record.getRunningProcess() == -1) {
            process += "[idle]";
        } else {
            process += "P" + record.getRunningProcess();
        }
        rec += "\n\nNext process on the CPU:  " + process;
        rec += "\n" + line();
        rec += "\n\nList of processes in the ready queue:\n";
        rec += "\n\t\tProcess\t\tBurst";
        if(record.getType() == AlgorithmTypes.MLQ || record.getType() == AlgorithmTypes.MLFQ) {
            rec += "\t\tQueue";
        }
        if(record.getReady().isEmpty()) {
            rec += "\n\t\t\t [empty]";
        } else {
            if(record.getType() != AlgorithmTypes.MLQ && record.getType() != AlgorithmTypes.MLFQ) {
                for(Integer i : record.getReady().keySet()) {
                    rec += "\n\t\t  P" + i + "\t\t  " + record.getReady().get(i);
                }
            } else {
                for(Integer i : record.getReady().keySet()) {
                    rec += "\n\t\t  P" + i + "\t\t  " + record.getReady().get(i) + "\t\t\t  Q" + (record.getPriority().get(i) + 1);
                }
            }
        }
        rec += "\n" + line();
        rec += "\n\nList of processes in I/O:\n";
        rec += "\n\t\tProcess\t\tRemaining I/O time";
        if(record.getIo().isEmpty()) {
            rec += "\n\t\t\t [empty]";
        } else {
            for(Integer i : record.getIo().keySet()) {
                rec += "\n\t\t  P" + i + "\t\t  " + record.getIo().get(i);
            }
        }
        if(!record.getCompleted().isEmpty()) {
            rec += "\n" + line();
            rec += "\n\nCompleted:\t";
            for(Integer i : record.getCompleted()) {
                rec += "P" + i + "\t";
            }
        }
        rec += "\n\n" + "::::::::::::::::::::::::::::::::::::::::::::::::::";
        return rec;
    }

    public static String line() {
        return "..................................................";
    }
}