package SchedulingAlgorithms;

import Util.Cpu;
import Util.Process;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class Algorithm implements AlgorithmInterface{

    protected Cpu cpu;
    protected List<Process> processList;

    public Algorithm(Cpu cpu)
    {
     this.cpu = cpu;
     processList = new ArrayList<>(cpu.getProcessHashMap().values().stream().toList());
     processList.forEach(p -> p.setCpu(cpu));
    }

    public boolean isAllProcessesComplete()
    {
        return processList.stream().filter(p -> p.getRoutine()[p.getRoutine().length-1] > 0).toList().size() == 0;
    }

    public static enum Type
    {
        FCFS, MLFQ, MLQ, RR, SJF, Priority;
    }
}
