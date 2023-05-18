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
        // Add processes for simulation into list (to do: deep copy)
        ArrayList<ProcessControlBlock> input1 = new ArrayList<>(Arrays.asList(p1,p2,p3,p4,p5,p6,p7,p8));
        ArrayList<ProcessControlBlock> input2 = new ArrayList<>(Arrays.asList(p9,p10,p11,p12,p13,p14,p15,p16));
        ArrayList<ProcessControlBlock> input3 = new ArrayList<>(Arrays.asList(p17,p18,p19,p20,p21,p22,p23,p24));
        ArrayList<ProcessControlBlock> input4 = new ArrayList<>(Arrays.asList(p25,p26,p27,p28,p29,p30,p31,p32));


        // create new simulation objects for each algorithm
        CpuSchedulerSimulation fcfs = new CpuSchedulerSimulation(new SimulationInput(deepCopy(input1)),
                AlgorithmTypes.FCFS, SchedulingTypes.NON_PREEMPTIVE);
        CpuSchedulerSimulation sjf = new CpuSchedulerSimulation(new SimulationInput(deepCopy(input2)),
                AlgorithmTypes.SJF, SchedulingTypes.NON_PREEMPTIVE);
        CpuSchedulerSimulation priority = new CpuSchedulerSimulation(new SimulationInput(deepCopy(input3),
                new int[]{3,6,5,4,1,2,8,7}), AlgorithmTypes.Priority, SchedulingTypes.PREEMPTIVE);
        CpuSchedulerSimulation rr = new CpuSchedulerSimulation(new SimulationInput(deepCopy(input4), 5),
                AlgorithmTypes.RR, SchedulingTypes.PREEMPTIVE);


        // run each simulation
        fcfs.runSim();
        sjf.runSim();
        priority.runSim();
        rr.runSim();


        // display results to console and file.
        // file results in src/main/resources/OutputFiles

        // first come, first served (non-preemptive) print to file
        createResultsFile(fcfs.getResults(), fcfs.getRecords(),
                "src/main/resources/OutputFiles/fcfs.txt",1);
        // [To also print records to console uncomment below]
        printHeader(fcfs);
        // printRecord(fcfs.getRecords());
        printResults(fcfs.getResults());

        // shortest job first (non-preemptive) print to file
        createResultsFile(sjf.getResults(), sjf.getRecords(),
                "src/main/resources/OutputFiles/sjf.txt",1);
        // [To also print records to console uncomment below]
        printHeader(sjf);
        // printRecord(sjf.getRecords());
        printResults(sjf.getResults());

        // priority (preemptive) print to file
        createResultsFile(priority.getResults(), priority.getRecords(),
                "src/main/resources/OutputFiles/priority.txt",1);
        // [To also print records to console uncomment below]
        printHeader(priority);
        // printRecord(priority.getRecords());
        printResults(priority.getResults());

        // rr
        createResultsFile(rr.getResults(), rr.getRecords(),
                "src/main/resources/OutputFiles/rr.txt",1);
        // [To also print records to console uncomment below]
        printHeader(rr);
        // printRecord(rr.getRecords());
        printResults(rr.getResults());
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
}