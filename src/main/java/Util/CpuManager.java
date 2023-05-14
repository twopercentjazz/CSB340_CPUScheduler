package Util;

import SchedulingAlgorithms.Algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * CPU MANAGER CLASS
 * Allows access to the other cpu
 */
public class CpuManager {

    private static final HashMap<Integer, Cpu> cpuMap;
    private static final HashMap<Cpu, List<Process>> cpuProcessMap;

    private static final HashMap<String ,Process> processMap;

    static
    {
        cpuMap = new HashMap<Integer, Cpu>();
        cpuProcessMap = new HashMap<Cpu, List<Process>>();
        processMap = new HashMap<>();

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
                    processMap.put(processId, new Process(processId, routine, 5));
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

    /**
     * Creates a Cpu manager that has exactly one of each algorithm
     */
    public CpuManager()
    {
        this(Arrays.stream(Algorithm.Type.values()).toList());
    }

    /**
     * Creates a Cpu manager that has a single algorithm
     * @param algorithmType
     */
    public CpuManager(Algorithm.Type algorithmType)
    {
        this(new LinkedList<>(Arrays.asList(algorithmType)));
    }

    /**
     * Creates a Cpu manager that has A list of Algorithms
     * @param algorithmList algorithm types to run
     */
    public CpuManager(List<Algorithm.Type> algorithmList)
    {
        int count = 0;
        for (Algorithm.Type type : algorithmList) {
            cpuMap.put(count, new Cpu(count, processesCopy(), type));
            count++;
        }
    }

    /**
     * Creates a thread for each Algorithm and runs the experiment on every cpu
     */
    public void runExperiments()
    {
        cpuMap.forEach( (id, cpu) -> new Thread(cpu).start());
    }

    /**
     * Creates a copy of the process to avoid unwanted object manipulation
     *
     * @return  a list of the same processes as different objects
     */
    private List<Process> processesCopy()
    {
        List<Process> copy = new LinkedList<>();

        processMap.values().stream().forEach(p -> {
            copy.add(new Process(p.getId(), Arrays.copyOf(p.getRoutine(), p.getRoutine().length), p.getPriority()));
        });

        return copy;
    }


}
