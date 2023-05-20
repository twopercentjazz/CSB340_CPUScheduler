/** Driver for CPU Scheduler Project to test and display results. */

import AdditionalUtilities.Algorithms.AlgorithmTypes;
import AdditionalUtilities.Utilities.*;
import java.io.*;
import java.util.*;

public class SimulationDriver {
    public static void main(String[] args) throws FileNotFoundException, CloneNotSupportedException {
        // create Processes with given process data
        ProcessControlBlock p1 = new ProcessControlBlock(1, new int[]{5, 27, 3, 31, 5, 43, 4, 18, 6, 22, 4, 26, 3, 24, 4});
        ProcessControlBlock p2 = new ProcessControlBlock(2, new int[]{4, 48, 5, 44, 7, 42, 12, 37, 9, 76, 4, 41, 9, 31, 7, 43, 8});
        ProcessControlBlock p3 = new ProcessControlBlock(3, new int[]{8, 33, 12, 41, 18, 65, 14, 21, 4, 61, 15, 18, 14, 26, 5, 31, 6});
        ProcessControlBlock p4 = new ProcessControlBlock(4, new int[]{3, 35, 4, 41, 5, 45, 3, 51, 4, 61, 5, 54, 6, 82, 5, 77, 3});
        ProcessControlBlock p5 = new ProcessControlBlock(5, new int[]{16, 24, 17, 21, 5, 36, 16, 26, 7, 31, 13, 28, 11, 21, 6, 13, 3, 11, 4});
        ProcessControlBlock p6 = new ProcessControlBlock(6, new int[]{11, 22, 4, 8, 5, 10, 6, 12, 7, 14, 9, 18, 12, 24, 15, 30, 8});
        ProcessControlBlock p7 = new ProcessControlBlock(7, new int[]{14, 46, 17, 41, 11, 42, 15, 21, 4, 32, 7, 19, 16, 33, 10});
        ProcessControlBlock p8 = new ProcessControlBlock(8, new int[]{4, 14, 5, 33, 6, 51, 14, 73, 16, 87, 6});
        // to do: make deep copy
        ProcessControlBlock p9 = new ProcessControlBlock(1, new int[]{5, 27, 3, 31, 5, 43, 4, 18, 6, 22, 4, 26, 3, 24, 4});
        ProcessControlBlock p10 = new ProcessControlBlock(2, new int[]{4, 48, 5, 44, 7, 42, 12, 37, 9, 76, 4, 41, 9, 31, 7, 43, 8});
        ProcessControlBlock p11 = new ProcessControlBlock(3, new int[]{8, 33, 12, 41, 18, 65, 14, 21, 4, 61, 15, 18, 14, 26, 5, 31, 6});
        ProcessControlBlock p12 = new ProcessControlBlock(4, new int[]{3, 35, 4, 41, 5, 45, 3, 51, 4, 61, 5, 54, 6, 82, 5, 77, 3});
        ProcessControlBlock p13 = new ProcessControlBlock(5, new int[]{16, 24, 17, 21, 5, 36, 16, 26, 7, 31, 13, 28, 11, 21, 6, 13, 3, 11, 4});
        ProcessControlBlock p14 = new ProcessControlBlock(6, new int[]{11, 22, 4, 8, 5, 10, 6, 12, 7, 14, 9, 18, 12, 24, 15, 30, 8});
        ProcessControlBlock p15 = new ProcessControlBlock(7, new int[]{14, 46, 17, 41, 11, 42, 15, 21, 4, 32, 7, 19, 16, 33, 10});
        ProcessControlBlock p16 = new ProcessControlBlock(8, new int[]{4, 14, 5, 33, 6, 51, 14, 73, 16, 87, 6});
        // to do: make deep copy
        ProcessControlBlock p17 = new ProcessControlBlock(1, new int[]{5, 27, 3, 31, 5, 43, 4, 18, 6, 22, 4, 26, 3, 24, 4});
        ProcessControlBlock p18 = new ProcessControlBlock(2, new int[]{4, 48, 5, 44, 7, 42, 12, 37, 9, 76, 4, 41, 9, 31, 7, 43, 8});
        ProcessControlBlock p19 = new ProcessControlBlock(3, new int[]{8, 33, 12, 41, 18, 65, 14, 21, 4, 61, 15, 18, 14, 26, 5, 31, 6});
        ProcessControlBlock p20 = new ProcessControlBlock(4, new int[]{3, 35, 4, 41, 5, 45, 3, 51, 4, 61, 5, 54, 6, 82, 5, 77, 3});
        ProcessControlBlock p21 = new ProcessControlBlock(5, new int[]{16, 24, 17, 21, 5, 36, 16, 26, 7, 31, 13, 28, 11, 21, 6, 13, 3, 11, 4});
        ProcessControlBlock p22 = new ProcessControlBlock(6, new int[]{11, 22, 4, 8, 5, 10, 6, 12, 7, 14, 9, 18, 12, 24, 15, 30, 8});
        ProcessControlBlock p23 = new ProcessControlBlock(7, new int[]{14, 46, 17, 41, 11, 42, 15, 21, 4, 32, 7, 19, 16, 33, 10});
        ProcessControlBlock p24 = new ProcessControlBlock(8, new int[]{4, 14, 5, 33, 6, 51, 14, 73, 16, 87, 6});
        // to do: make deep copy
        ProcessControlBlock p25 = new ProcessControlBlock(1, new int[]{5, 27, 3, 31, 5, 43, 4, 18, 6, 22, 4, 26, 3, 24, 4});
        ProcessControlBlock p26 = new ProcessControlBlock(2, new int[]{4, 48, 5, 44, 7, 42, 12, 37, 9, 76, 4, 41, 9, 31, 7, 43, 8});
        ProcessControlBlock p27 = new ProcessControlBlock(3, new int[]{8, 33, 12, 41, 18, 65, 14, 21, 4, 61, 15, 18, 14, 26, 5, 31, 6});
        ProcessControlBlock p28 = new ProcessControlBlock(4, new int[]{3, 35, 4, 41, 5, 45, 3, 51, 4, 61, 5, 54, 6, 82, 5, 77, 3});
        ProcessControlBlock p29 = new ProcessControlBlock(5, new int[]{16, 24, 17, 21, 5, 36, 16, 26, 7, 31, 13, 28, 11, 21, 6, 13, 3, 11, 4});
        ProcessControlBlock p30 = new ProcessControlBlock(6, new int[]{11, 22, 4, 8, 5, 10, 6, 12, 7, 14, 9, 18, 12, 24, 15, 30, 8});
        ProcessControlBlock p31 = new ProcessControlBlock(7, new int[]{14, 46, 17, 41, 11, 42, 15, 21, 4, 32, 7, 19, 16, 33, 10});
        ProcessControlBlock p32 = new ProcessControlBlock(8, new int[]{4, 14, 5, 33, 6, 51, 14, 73, 16, 87, 6});
        // to do: make deep copy
        //mlq1
        ProcessControlBlock p33 = new ProcessControlBlock(1, new int[]{5, 27, 3, 31, 5, 43, 4, 18, 6, 22, 4, 26, 3, 24, 4});
        ProcessControlBlock p34 = new ProcessControlBlock(2, new int[]{4, 48, 5, 44, 7, 42, 12, 37, 9, 76, 4, 41, 9, 31, 7, 43, 8});
        ProcessControlBlock p35 = new ProcessControlBlock(3, new int[]{8, 33, 12, 41, 18, 65, 14, 21, 4, 61, 15, 18, 14, 26, 5, 31, 6});
        ProcessControlBlock p36 = new ProcessControlBlock(4, new int[]{3, 35, 4, 41, 5, 45, 3, 51, 4, 61, 5, 54, 6, 82, 5, 77, 3});
        //mlq2
        ProcessControlBlock p37 = new ProcessControlBlock(5, new int[]{16, 24, 17, 21, 5, 36, 16, 26, 7, 31, 13, 28, 11, 21, 6, 13, 3, 11, 4});
        ProcessControlBlock p38 = new ProcessControlBlock(6, new int[]{11, 22, 4, 8, 5, 10, 6, 12, 7, 14, 9, 18, 12, 24, 15, 30, 8});
        ProcessControlBlock p39 = new ProcessControlBlock(7, new int[]{14, 46, 17, 41, 11, 42, 15, 21, 4, 32, 7, 19, 16, 33, 10});
        ProcessControlBlock p40 = new ProcessControlBlock(8, new int[]{4, 14, 5, 33, 6, 51, 14, 73, 16, 87, 6});

        // to do: make deep copy
        ProcessControlBlock p41 = new ProcessControlBlock(1, new int[]{5, 27, 3, 31, 5, 43, 4, 18, 6, 22, 4, 26, 3, 24, 4});
        ProcessControlBlock p42 = new ProcessControlBlock(2, new int[]{4, 48, 5, 44, 7, 42, 12, 37, 9, 76, 4, 41, 9, 31, 7, 43, 8});
        ProcessControlBlock p43 = new ProcessControlBlock(3, new int[]{8, 33, 12, 41, 18, 65, 14, 21, 4, 61, 15, 18, 14, 26, 5, 31, 6});
        ProcessControlBlock p44 = new ProcessControlBlock(4, new int[]{3, 35, 4, 41, 5, 45, 3, 51, 4, 61, 5, 54, 6, 82, 5, 77, 3});
        ProcessControlBlock p45 = new ProcessControlBlock(5, new int[]{16, 24, 17, 21, 5, 36, 16, 26, 7, 31, 13, 28, 11, 21, 6, 13, 3, 11, 4});
        ProcessControlBlock p46 = new ProcessControlBlock(6, new int[]{11, 22, 4, 8, 5, 10, 6, 12, 7, 14, 9, 18, 12, 24, 15, 30, 8});
        ProcessControlBlock p47 = new ProcessControlBlock(7, new int[]{14, 46, 17, 41, 11, 42, 15, 21, 4, 32, 7, 19, 16, 33, 10});
        ProcessControlBlock p48 = new ProcessControlBlock(8, new int[]{4, 14, 5, 33, 6, 51, 14, 73, 16, 87, 6});

        // Add processes for simulation into list (to do: deep copy)
        ArrayList<ProcessControlBlock> input1 = new ArrayList<>(Arrays.asList(p1,p2,p3,p4,p5,p6,p7,p8));
        ArrayList<ProcessControlBlock> input2 = new ArrayList<>(Arrays.asList(p9,p10,p11,p12,p13,p14,p15,p16));
        ArrayList<ProcessControlBlock> input3 = new ArrayList<>(Arrays.asList(p17,p18,p19,p20,p21,p22,p23,p24));
        ArrayList<ProcessControlBlock> input4 = new ArrayList<>(Arrays.asList(p25,p26,p27,p28,p29,p30,p31,p32));

        ArrayList<ProcessControlBlock> input5 = new ArrayList<>(Arrays.asList(p33,p34,p35,p36));
        ArrayList<ProcessControlBlock> input6 = new ArrayList<>(Arrays.asList(p37,p38,p39,p40));

        ArrayList<ProcessControlBlock> input7 = new ArrayList<>(Arrays.asList(p41,p42,p43,p44,p45,p46,p47,p48));


        ArrayList<AlgorithmTypes> order = new ArrayList<>(Arrays.asList(AlgorithmTypes.FCFS, AlgorithmTypes.SJF,
                AlgorithmTypes.Priority, AlgorithmTypes.RR, AlgorithmTypes.MLQ, AlgorithmTypes.MLFQ));


        HashMap<AlgorithmTypes, CpuSchedulerSimulation> resultsMap = new HashMap<>();
        resultsMap.put(AlgorithmTypes.FCFS, new CpuSchedulerSimulation(new SimulationInput(deepCopy(input1)),
                AlgorithmTypes.FCFS, SchedulingTypes.NON_PREEMPTIVE));
        resultsMap.put(AlgorithmTypes.SJF, new CpuSchedulerSimulation(new SimulationInput(deepCopy(input2)),
                AlgorithmTypes.SJF, SchedulingTypes.NON_PREEMPTIVE));
        resultsMap.put(AlgorithmTypes.Priority, new CpuSchedulerSimulation(new SimulationInput(deepCopy(input3),
                new int[]{3,6,5,4,1,2,8,7}), AlgorithmTypes.Priority, SchedulingTypes.PREEMPTIVE));
        resultsMap.put(AlgorithmTypes.RR, new CpuSchedulerSimulation(new SimulationInput(deepCopy(input4), 5),
                AlgorithmTypes.RR, SchedulingTypes.PREEMPTIVE));
        resultsMap.put(AlgorithmTypes.MLQ, new CpuSchedulerSimulation(new ArrayList<>(Arrays.asList(new SimulationInput
                (deepCopy(input5), 4), new SimulationInput(deepCopy(input6)))), AlgorithmTypes.MLQ,
                SchedulingTypes.PREEMPTIVE, new ArrayList<>(Arrays.asList(AlgorithmTypes.RR, AlgorithmTypes.FCFS))));
        resultsMap.put(AlgorithmTypes.MLFQ, new CpuSchedulerSimulation(new ArrayList<>(Arrays.asList(new SimulationInput
                (deepCopy(input7), 5), new SimulationInput(new ArrayList<>(),10), new
                SimulationInput(new ArrayList<>()))), AlgorithmTypes.MLFQ, SchedulingTypes.PREEMPTIVE,
                new ArrayList<>(Arrays.asList(AlgorithmTypes.RR, AlgorithmTypes.RR, AlgorithmTypes.FCFS))));


        for(CpuSchedulerSimulation sim: resultsMap.values()) {
            sim.runSim();
            createResultsFile(sim.getResults(), sim.getRecords(),
                    "src/main/resources/OutputFiles/" + sim.getAlgorithmType() + ".txt",1);
            printHeader(sim);
            //printRecord(sim.getRecords());
            printResults(sim.getResults());
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

    /** This method prints a simulations records and results to file.
     * @param results
     * @param records
     * @param file
     * @param flag 1 for records and results, 0 for just results
     * @throws FileNotFoundException */
    public static void createResultsFile(SimulationResults results, ArrayList<SimulationRecord> records,
                                         String file, int flag) throws FileNotFoundException {
        PrintStream output = new PrintStream(new File(file));
        output.println(file + "\n");
        if(flag == 1) {
            for(SimulationRecord rec: records) {
                output.println(SimulationOutput.displayRecord(rec));
            }
        }
        output.println(SimulationOutput.displayResults(results));
        System.out.println("File Successfully Created: " + file);
    }

    public static void printHeader(CpuSchedulerSimulation sim) {
        System.out.println("\n" + sim.getAlgorithmType() + " " + sim.getSchedulingType() +
                "\n-----------------------------");
    }

    public static ArrayList<ProcessControlBlock> deepCopy(ArrayList<ProcessControlBlock> original) throws CloneNotSupportedException {
        ArrayList<ProcessControlBlock> temp = new ArrayList<>();
        for(ProcessControlBlock pcb: original) {
            temp.add((ProcessControlBlock)pcb.clone());
        }
        return temp;
    }


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