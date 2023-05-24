/** Driver for CPU Scheduler Project to test and display results. */

import AdditionalUtilities.Algorithms.AlgorithmTypes;
import AdditionalUtilities.Utilities.*;
import java.io.*;
import java.util.*;

public class SimulationDriver {
    public static void main(String[] args) throws FileNotFoundException {
        // create Processes with given process data
        ProcessControlBlock p1 = new ProcessControlBlock(1, new int[]{5, 27, 3, 31, 5, 43, 4, 18, 6, 22, 4, 26, 3, 24, 4});
        ProcessControlBlock p2 = new ProcessControlBlock(2, new int[]{4, 48, 5, 44, 7, 42, 12, 37, 9, 76, 4, 41, 9, 31, 7, 43, 8});
        ProcessControlBlock p3 = new ProcessControlBlock(3, new int[]{8, 33, 12, 41, 18, 65, 14, 21, 4, 61, 15, 18, 14, 26, 5, 31, 6});
        ProcessControlBlock p4 = new ProcessControlBlock(4, new int[]{3, 35, 4, 41, 5, 45, 3, 51, 4, 61, 5, 54, 6, 82, 5, 77, 3});
        ProcessControlBlock p5 = new ProcessControlBlock(5, new int[]{16, 24, 17, 21, 5, 36, 16, 26, 7, 31, 13, 28, 11, 21, 6, 13, 3, 11, 4});
        ProcessControlBlock p6 = new ProcessControlBlock(6, new int[]{11, 22, 4, 8, 5, 10, 6, 12, 7, 14, 9, 18, 12, 24, 15, 30, 8});
        ProcessControlBlock p7 = new ProcessControlBlock(7, new int[]{14, 46, 17, 41, 11, 42, 15, 21, 4, 32, 7, 19, 16, 33, 10});
        ProcessControlBlock p8 = new ProcessControlBlock(8, new int[]{4, 14, 5, 33, 6, 51, 14, 73, 16, 87, 6});

        // Add processes for simulation into list
        ArrayList<ProcessControlBlock> input = new ArrayList<>(Arrays.asList(p1,p2,p3,p4,p5,p6,p7,p8));

        // List used to print in a specified order
        ArrayList<AlgorithmTypes> order = new ArrayList<>(Arrays.asList(AlgorithmTypes.FCFS, AlgorithmTypes.SJF,
                AlgorithmTypes.Priority, AlgorithmTypes.RR, AlgorithmTypes.MLQ, AlgorithmTypes.MLFQ));

        // create results map
        HashMap<AlgorithmTypes, CpuSchedulerSimulation> resultsMap = new HashMap<>();

        // put simulations into results map
        resultsMap.put(AlgorithmTypes.FCFS, new CpuSchedulerSimulation(new SimulationInput(deepCopy(input)),
                AlgorithmTypes.FCFS, SchedulingTypes.NON_PREEMPTIVE));
        resultsMap.put(AlgorithmTypes.SJF, new CpuSchedulerSimulation(new SimulationInput(deepCopy(input)),
                AlgorithmTypes.SJF, SchedulingTypes.NON_PREEMPTIVE));
        resultsMap.put(AlgorithmTypes.Priority, new CpuSchedulerSimulation(new SimulationInput(deepCopy(input),
                new int[]{3,6,5,4,1,2,8,7}), AlgorithmTypes.Priority, SchedulingTypes.PREEMPTIVE));
        resultsMap.put(AlgorithmTypes.RR, new CpuSchedulerSimulation(new SimulationInput(deepCopy(input),
                5), AlgorithmTypes.RR, SchedulingTypes.PREEMPTIVE));
        resultsMap.put(AlgorithmTypes.MLQ, new CpuSchedulerSimulation(new ArrayList<>(Arrays.asList(new
                SimulationInput (deepCopy(input, 0, 3), 4), new SimulationInput(deepCopy(input,
                4,7)))), AlgorithmTypes.MLQ, SchedulingTypes.PREEMPTIVE, new
                ArrayList<>(Arrays.asList(AlgorithmTypes.RR, AlgorithmTypes.FCFS))));
        resultsMap.put(AlgorithmTypes.MLFQ, new CpuSchedulerSimulation(new ArrayList<>(Arrays.asList(new SimulationInput
                (deepCopy(input), 5), new SimulationInput(new ArrayList<>(),10), new
                SimulationInput(new ArrayList<>()))), AlgorithmTypes.MLFQ, SchedulingTypes.PREEMPTIVE,
                new ArrayList<>(Arrays.asList(AlgorithmTypes.RR, AlgorithmTypes.RR, AlgorithmTypes.FCFS))));

        // run simulations and create output
        for(CpuSchedulerSimulation sim: resultsMap.values()) {
            sim.runSim();
            // set flag to 1 for detailed results, set flag to 0 for summary only
            createResultsFile(sim, "src/main/resources/OutputFiles/" + sim.getAlgorithmType() + ".txt", 1);
            printToConsole(sim, 0);
        }
        printResultsToCsvSet1(resultsMap, order);
        printResultsToCsvSet2(resultsMap, order);
    }

    /** This method prints the final results of a simulation instance.
     * @param results */
    public static void printResults(SimulationResults results) {
        System.out.println(SimulationOutput.displayResults(results));
    }

    /** This method prints the detailed record from running a simulation.
     * @param records */
    public static void printRecord(ArrayList<SimulationRecord> records) {
        for(SimulationRecord rec: records) {
            System.out.println(SimulationOutput.displayRecord(rec));
        }
    }

    /** This method prints a simulations records and results to console.
     * @param sim scheduler algorithm simulation
     * @param flag 1 for records and results, 0 for just results */
    public static void printToConsole(CpuSchedulerSimulation sim, int flag) {
        System.out.println(header(sim));
        if(flag == 1) {
            printRecord(sim.getRecords());
        }
        printResults(sim.getResults());
    }

    /** This method prints a simulations records and results to file.
     * @param sim scheduler algorithm simulation
     * @param file name of output file
     * @param flag 1 for records and results, 0 for just results
     * @throws FileNotFoundException */
    public static void createResultsFile(CpuSchedulerSimulation sim, String file, int flag) throws FileNotFoundException {
        PrintStream output = new PrintStream(file);
        output.println(header(sim));
        if(flag == 1) {
            for(SimulationRecord rec: sim.getRecords()) {
                output.println(SimulationOutput.displayRecord(rec));
            }
        }
        output.println(SimulationOutput.displayResults(sim.getResults()));
        System.out.println("\n\nFile Successfully Created: " + file);
    }

    /** This method prints an algorithm type header for the output
     * @param sim scheduler algorithm simulation
     * @return header
     */
    public static String header(CpuSchedulerSimulation sim) {
        return headerLine() + "\n\t\t\t\t\t" + sim.getAlgorithmType() + " " +
                sim.getSchedulingType() + " RESULTS\n" + headerLine();
    }

    /** This method creates a line for the header.
     * @return header line */
    public static String headerLine() {
        return "-----------------------------------------------------------------";
    }

    /** This method makes a deep copy of the input array of process control blocks.
     * @param original the given input
     * @return a deep copy of all the inputs */
    public static ArrayList<ProcessControlBlock> deepCopy(ArrayList<ProcessControlBlock> original) {
        ArrayList<ProcessControlBlock> temp = new ArrayList<>();
        for(ProcessControlBlock pcb: original) {
            temp.add(pcb.copy());
        }
        return temp;
    }

    /** This method makes a deep copy of part of the input array of process control blocks given a range.
     * @param original the given input
     * @param start index to start copy at
     * @param end index to end copy at
     * @return a deep copy of all the inputs in a given range */
    public static ArrayList<ProcessControlBlock> deepCopy(ArrayList<ProcessControlBlock> original, int start, int end) {
        ArrayList<ProcessControlBlock> temp = new ArrayList<>();
        for(int i = start; i <= end; i++) {
            temp.add(original.get(i).copy());
        }
        return temp;
    }

    /** This method prints a csv for the first table for the report.
     * @param results A summary of the results from a simulation
     * @param order The order to display results
     * @throws FileNotFoundException */
    public static void printResultsToCsvSet1(HashMap<AlgorithmTypes, CpuSchedulerSimulation> results,
                                             ArrayList<AlgorithmTypes> order) throws FileNotFoundException {
        String fileName = "src/main/resources/OutputFiles/ResultsSet1.csv";
        PrintStream output = new PrintStream(fileName);
        output.print("\n,");
        for(AlgorithmTypes header: order) {
            output.print(header + ",");
        }
        output.print("\nCPU Utilization,");
        for(AlgorithmTypes header: order) {
            output.printf("%.4f%%,", results.get(header).getResults().getCpuUtilization() * 100);
        }
        output.print("\nAVG Waiting Time,");
        for(AlgorithmTypes header: order) {
            output.printf("%.3f,", results.get(header).getResults().getAvgWait());
        }
        output.print("\nAVG Turnaround Time,");
        for(AlgorithmTypes header: order) {
            output.printf("%.3f,", results.get(header).getResults().getAvgTurnAround());
        }
        output.print("\nAVG Response Time,");
        for(AlgorithmTypes header: order) {
            output.printf("%.3f,", results.get(header).getResults().getAvgResponse());
        }
    }

    /** This method prints a csv for the second table for the report.
     * @param results A summary of the results from a simulation
     * @param order The order to display results
     * @throws FileNotFoundException */
    public static void printResultsToCsvSet2(HashMap<AlgorithmTypes, CpuSchedulerSimulation> results,
                                             ArrayList<AlgorithmTypes> order) throws FileNotFoundException {
        String fileName = "src/main/resources/OutputFiles/ResultsSet2.csv";
        PrintStream output = new PrintStream(fileName);
        output.print("\n,");
        for(AlgorithmTypes header: order) {
            output.print(header + ",,,");
        }
        output.print("\n,");
        for(AlgorithmTypes header: order) {
            output.printf("CPU Utilization %.4f%%,,,", results.get(header).getResults().getCpuUtilization() * 100);
        }
        output.print("\n,");
        for(AlgorithmTypes header: order) {
            output.print("AvgWait,AvgTurnAround,AvgResponse,");
        }
        for(int i = 0; i < 8; i++) {
            output.print("\nP" + (i + 1) + ",");
            for(AlgorithmTypes header: order) {
                output.print(results.get(header).getResults().getAllWaitTimes().get(i) + ","
                        + results.get(header).getResults().getAllTurnAroundTimes().get(i) + ","
                        + results.get(header).getResults().getAllResponseTimes().get(i) + ",");
            }
        }
        output.print("\nAVG,");
        for(AlgorithmTypes header: order) {
            output.printf("%.3f,", results.get(header).getResults().getAvgWait());
            output.printf("%.3f,", results.get(header).getResults().getAvgTurnAround());
            output.printf("%.3f,", results.get(header).getResults().getAvgResponse());
        }
    }
}