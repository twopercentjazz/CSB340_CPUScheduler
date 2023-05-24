package AdditionalExperimentation.SchedulingAlgorithms;

import AdditionalExperimentation.Util.Cpu;
import AdditionalExperimentation.Util.Process;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Algorithm implements AlgorithmInterface{

    protected Cpu cpu;
    protected List<Process> processList;
    private PrintWriter pw;

    public Algorithm(Cpu cpu)
    {
     this.cpu = cpu;
     processList = new ArrayList<>(cpu.getProcessHashMap().values().stream().toList());
     processList.forEach(p -> p.setCpu(cpu));
        try {
            File file = new File("src/main/resources/"+cpu.getAlgorithmType()+"_Cpu_"+cpu.getId()+".txt");
            file.createNewFile();
            pw = new PrintWriter(new FileOutputStream(file, false));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAllProcessesComplete()
    {
        return processList.stream().filter(p -> p.getRoutine()[p.getRoutine().length-1] > 0).toList().isEmpty();
    }

    public void writeToFile(String str)
    {
        pw.write(str);
        pw.flush();
    }

    public static enum Type
    {
        FCFS, MLFQ, MLQ, RR, SJF, Priority;
    }
}
