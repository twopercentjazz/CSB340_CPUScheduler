/** This class helps schedule the next running process in a simulation. */

package AdditionalUtilities.Utilities;
import AdditionalUtilities.Algorithms.AlgorithmTypes;
import AdditionalUtilities.Algorithms.AlgorithmsInterface;

import java.util.*;

public class Scheduler {
    private ArrayList<AlgorithmsInterface> ready;
    private ArrayList<ProcessControlBlock> io; //
    private ArrayList<ProcessControlBlock> active; //
    private ArrayList<ProcessControlBlock> completed; //
    private ProcessControlBlock[] finalList;
    private ArrayList<ProcessControlBlock> tempIO;
    private int readyIndex;
    private int timeQuantum;


    public Scheduler(SimulationInput input) {
        this.ready = null;
        this.tempIO = new ArrayList<>();
        this.readyIndex = 0;
        this.io = new ArrayList<>();
        this.completed = new ArrayList<>();
        this.active = input.getInput();
        this.timeQuantum = input.getGivenTimeQuantum();
        this.finalList = new ProcessControlBlock[input.getSize()];
    }

    public Scheduler(ArrayList<AlgorithmsInterface> ready) {
        this.ready = ready;
        this.readyIndex = 0;
        this.io = new ArrayList<>();
        this.completed = new ArrayList<>();
        this.active = new ArrayList<>();
        this.tempIO = new ArrayList<>();
        this.timeQuantum = 0;
        for(AlgorithmsInterface algorithm: ready) {
            for(ProcessControlBlock pcb: algorithm.getScheduler().getActive()) {
                this.active.add(pcb);
                pcb.setPriority(this.ready.indexOf(algorithm));
            }
        }
        this.finalList = new ProcessControlBlock[active.size()];
    }
    public ArrayList<AlgorithmsInterface> getReadyList() { return this.ready; }

    public ArrayList<ProcessControlBlock> getIo() { return this.io; }

    public void setIo(ArrayList<ProcessControlBlock> newIo) { this.io = newIo; }

    public ArrayList<ProcessControlBlock> getActive() { return this.active; }

    public void setActive(ArrayList<ProcessControlBlock> newActive) { this.active = newActive; }

    public ArrayList<ProcessControlBlock> getCompleted() { return this.completed; }

    public ArrayList<ProcessControlBlock> getTempIO() { return this.tempIO; }


    public int getTimeQuantum() { return this.timeQuantum; }
    public void setTimeQuantum(int time) { this.timeQuantum = time; }

    public void flagProcessAsComplete(ProcessControlBlock pcb) {
        pcb.setState(ProcessControlBlock.ProcessState.COMPLETE);
        active.remove(pcb);
        completed.add(pcb);
    }

    public void createFinalList() {
        for(ProcessControlBlock p : completed) {
            finalList[p.getPid() - 1] = p;
        }
    }

    public ProcessControlBlock[] getFinalList() {
        return this.finalList;
    }

    public void setTurnAroundTimes() {
        for(ProcessControlBlock p : finalList) {
            p.updateTurnAroundTime();
        }
    }

    public void updateIo(ProcessControlBlock pcb, Queue<ProcessControlBlock> ready, AlgorithmTypes type) {
        pcb.updateIoTime(1);
        if (pcb.getIoTime() == 0) {
            pcb.setState(ProcessControlBlock.ProcessState.READY);
            pcb.setCpuBurstTime();
            if(type == AlgorithmTypes.SJF) {
                pcb.setPriority(pcb.getCpuBurstTime());
            }
            if(type == AlgorithmTypes.multiRR || type == AlgorithmTypes.multiFCFS) {
                tempIO.add(pcb);
            } else {
                this.io.remove(pcb);
                ready.add(pcb);
            }
        }
    }

    public Queue<ProcessControlBlock> initializeReady(Queue<ProcessControlBlock> newReady, AlgorithmTypes type) {
        Queue<ProcessControlBlock> ready = newReady;
        for(ProcessControlBlock p : active) {
            p.setCpuBurstTime();
            if(type == AlgorithmTypes.SJF) {
                p.setPriority(p.getCpuBurstTime());
            }
            ready.add(p);
        }
        return ready;
    }

    public void getNextRunningProcess(Queue<ProcessControlBlock> ready, Dispatcher prepareDispatch) {
        ProcessControlBlock next = ready.poll();
        if(next != null) {
            next.setState(ProcessControlBlock.ProcessState.RUNNING);
            prepareDispatch.setRunningProcess(next);
        } else {
            prepareDispatch.setRunningProcess(null);
        }
    }

    public void syncActiveLists(Scheduler s) {
        for(AlgorithmsInterface algorithm: s.getReadyList()) {
            algorithm.getScheduler().setActive(s.getActive());
        }
    }

    public boolean isReadyListEmpty() {
        boolean empty = true;
        for(AlgorithmsInterface ready: this.getReadyList()) {
            if(ready.getReady().isEmpty()) {
                empty = false;
                break;
            }
        }
        return empty;
    }

    public boolean isQueuePriorityChanged() {
        boolean priorityChange = false;
        for(int i = 0; i < readyIndex; i++) {
            if(!getReadyList().get(i).getReady().isEmpty()) {
                priorityChange = true;
                break;
            }
        }
        return priorityChange;
    }

    public int getReadyIndex() {
        return this.readyIndex;
    }

    public void setReadyIndex(int i) {
        this.readyIndex = i;
    }

    public void incrementReadyIndex() {
        this.readyIndex++;
    }

    public void decrementReadyIndex() {
        this.readyIndex--;
    }

    public boolean isNotLastQueue() {
        return readyIndex < ready.size() - 1;
    }

    public boolean isMulti() {
        return this.ready != null;
    }
}