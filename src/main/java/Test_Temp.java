/** Driver for CPU Scheduler Project to test and display results. */
import AdditionalUtilities.Algorithms.AlgorithmTypes;
import AdditionalUtilities.Utilities.*;
import java.io.*;
import java.util.*;
public class Test_Temp {
    private final static HashMap<String, ProcessControlBlock> processMap;
    private final static HashMap<String, Integer[]> processRoutine;
    static
    {
        processMap = new HashMap<>();
        processRoutine = new HashMap<>();
        //Reads in the list of processes and creates the objects
        try (Scanner in = new Scanner(new File("src/main/resources/processList")))
        {
            String line = "";
            String processId;
            //parsing out the different processes
            while (in.hasNextLine() && !(line = in.nextLine()).contains("For")){
                if(line.isEmpty()) continue;
                processId = line.substring(0, line.indexOf('{')).strip();
                line = line.substring(line.indexOf('{')+1, line.indexOf('}'));
                int[] routine;
                try {
                    routine = Arrays.stream(line.replaceAll(" ", "").split(",")).mapToInt(Integer::parseInt).toArray();
                    processMap.put(processId, new ProcessControlBlock(Integer.parseInt(processId.replaceAll("[^0-9]", "")), routine));
                    processRoutine.put(processId, Arrays.stream(routine).boxed().toArray(Integer[]::new));
                }catch (NumberFormatException e)
                {
                    System.out.println(String.format("The Process %s has a routine [%s] that contains burst cycles and waiting cycles that are not numbers", processId, line));
                    System.exit(1);
                }
            }
            //handle the priority setting for each algorithm
            String[] processes = line.substring(line.indexOf('{')+1, line.indexOf('}')).replaceAll(" ", "").split(",");
            line = line.substring(line.indexOf('}')+1);
            int[] priorities = Arrays.stream(line.substring(line.indexOf('{') + 1, line.indexOf("}")).replaceAll(" ", "").split(",")).mapToInt(Integer::parseInt).toArray();
            if(processes.length != priorities.length)
            {
                System.out.println("Failed trying to set priorities, Process Id list was not the same length as Priorities list");
                System.exit(1);
            }
            for(int i = 0; i < processes.length; i++)
            {
                if(!processMap.containsKey(processes[i]))
                {
                    System.out.println(String.format("Failed trying to set priorities failed to find process with id = %s", processes[i]));
                    System.exit(1);
                }
                processMap.get(processes[i]).setPriority(priorities[i]);
            }
        } catch (FileNotFoundException e)
        {
            System.out.println("ProcessList.txt file was not found in resources");
        }
    }
    private static ArrayList<ProcessControlBlock> processControlBlocksDeepCopy()
    {
        ArrayList<ProcessControlBlock> lst = new ArrayList<>();
        processMap.forEach((id, process) -> {
            lst.add(new ProcessControlBlock(process.getPid(), Arrays.stream(processRoutine.get(id)).mapToInt(i -> i).toArray()));
            lst.get(lst.size()-1).setPriority(process.getPriority());
        });
        return lst;
    }
    public static void main(String[] args) throws FileNotFoundException {
        List<CpuSchedulerSimulation> listOfSimulation = new LinkedList<>();
        // create new simulation objects for each algorithm
        listOfSimulation.add(new CpuSchedulerSimulation(new SimulationInput(processControlBlocksDeepCopy()),
                AlgorithmTypes.FCFS, SchedulingTypes.NON_PREEMPTIVE));
        listOfSimulation.add(new CpuSchedulerSimulation(new SimulationInput(processControlBlocksDeepCopy()),
                AlgorithmTypes.SJF, SchedulingTypes.NON_PREEMPTIVE));
        listOfSimulation.add(new CpuSchedulerSimulation(new SimulationInput(processControlBlocksDeepCopy(),
                new int[]{3,6,5,4,1,2,8,7}), AlgorithmTypes.Priority, SchedulingTypes.PREEMPTIVE));
        listOfSimulation.add(new CpuSchedulerSimulation(new SimulationInput(processControlBlocksDeepCopy()),
                AlgorithmTypes.RR, SchedulingTypes.NON_PREEMPTIVE));
        // run each simulation
        listOfSimulation.forEach(sim -> sim.runSim());
        // display results to console or file
        // for testing comment out adding the other algorithms to the list
        listOfSimulation.forEach(sim ->{
            try {
                createResultsFile(sim.getResults(), sim.getRecords(),
                        String.format("src/main/java/OutputFiles/%s.txt", sim.getAlgorithmType()),1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // [To also print to console uncomment below]
            // printRecord(sim.getRecords());
            // printResults(sim.getResults());
        });
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
}
