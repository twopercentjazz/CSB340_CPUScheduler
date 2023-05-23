/** Multilevel Queue */

package AdditionalUtilities.Algorithms;
import AdditionalUtilities.Utilities.*;
import java.util.*;

public class MLFQ implements AlgorithmsInterface {
    private Scheduler schedule;
    private Dispatcher dispatch;
    private Queue<ProcessControlBlock> ready;

    public MLFQ(ArrayList<AlgorithmsInterface> ready) {
        this.schedule = new Scheduler(ready);
        this.dispatch = new Dispatcher();
        this.ready = this.schedule.getReadyList().get(this.schedule.getReadyIndex()).getReady();
    }

    /**
     * {@inheritDoc}
     */
    public AlgorithmTypes getAlgorithmType() {
        return AlgorithmTypes.MLFQ;
    }

    /**
     * {@inheritDoc}
     */
    public void scheduleNextProcess() {









        //ProcessControlBlock next = this.ready.poll();
        ProcessControlBlock next = this.schedule.getReadyList().get(this.schedule.getReadyIndex()).getReady().poll();
        if(next != null) {
            next.setState(ProcessControlBlock.ProcessState.RUNNING);
            this.dispatch.setRunningProcess(next);
        } else {
            this.dispatch.setRunningProcess(null);
        }






        /*
        ready = this.schedule.getReadyList().get(this.schedule.getReadyIndex()).getReady();

        this.schedule.getReadyList().get(schedule.getReadyIndex()).scheduleNextProcess();

        //this.schedule.getNextRunningProcess( ready, this.schedule.getReadyList().get(this.schedule.getReadyIndex()).getDispatcher());
        this.dispatch.setRunningProcess(this.schedule.getReadyList().get(this.schedule.getReadyIndex()).getDispatcher().getRunningProcess());
/*

         */
        //this.schedule.syncActiveLists(this.schedule);
    }

    /** {@inheritDoc} */
    public void dispatchNextProcess(ProcessControlBlock running) {
        if (running == null) {
            //this.dispatch.contextSwitchIdle(ready, schedule, getAlgorithmType());
            //while(ready.isEmpty()) {
            while(this.schedule.isReadyListEmpty()) {
                this.dispatch.updateExecutionTimer(1);
                this.dispatch.updateIdleTimer(1);
                for(ProcessControlBlock pcb : this.schedule.getActive()) {
                    if (pcb.getState() == ProcessControlBlock.ProcessState.WAITING) {
                        //schedule.updateIo(pcb, ready, type);
                        pcb.updateIoTime(1);
                        if (pcb.getIoTime() == 0) {
                            pcb.setState(ProcessControlBlock.ProcessState.READY);
                            pcb.setCpuBurstTime();
                            this.schedule.getIo().remove(pcb);
                            //ready.add(pcb);
                            this.schedule.getReadyList().get(pcb.getPriority()).getReady().add(pcb);
                        }
                    }
                }
            }
        } else {
            int time;

            //if(schedule.getReadyList().get(schedule.getReadyIndex()).getAlgorithmType() == AlgorithmTypes.FCFS) {
            if(schedule.getReadyIndex() == schedule.getReadyList().size() - 1) {
                time = running.getCpuBurstTime();

            } else {
                time = schedule.getReadyList().get(schedule.getReadyIndex()).getScheduler().getTimeQuantum();

            }

            //System.out.println(time);

            boolean queuePreempt = false;
            boolean rrPreempt = true;
            this.dispatch.updateResponseTime();
            for(int i = 0; i < time; i++) {
                for(ProcessControlBlock pcb : this.schedule.getActive()) {
                    if(pcb.getState() == ProcessControlBlock.ProcessState.WAITING) {
                        //this.schedule.updateIo(pcb, ready, getAlgorithmType());
                        pcb.updateIoTime(1);
                        if (pcb.getIoTime() == 0) {
                            pcb.setState(ProcessControlBlock.ProcessState.READY);
                            pcb.setCpuBurstTime();
                            this.schedule.getIo().remove(pcb);
                            schedule.getReadyList().get(pcb.getPriority()).getReady().add(pcb);
                            if(schedule.isQueuePriorityChanged()) {
                                queuePreempt = true;
                            }
                        }
                    } else if(pcb.getState() == ProcessControlBlock.ProcessState.READY) {
                        pcb.updateWaitingTime(1);
                    } else {
                        this.dispatch.updateExecutionTimer(1);
                        pcb.updateCpuBurstTime(1);
                        if(pcb.getCpuBurstTime() == 0) {
                            rrPreempt = false;
                        }



                    }
                }

                if(!rrPreempt) {
                    //this.dispatch.contextSwitchFinishCpuBurst(schedule, running);
                    if(running.isFinalBurst()) {
                        schedule.flagProcessAsComplete(running);
                    } else {
                        running.setIoTime();
                        running.setState(ProcessControlBlock.ProcessState.WAITING);
                        schedule.getIo().add(running);
                    }
                    break;
                }
                if(queuePreempt) {

                    running.setState(ProcessControlBlock.ProcessState.READY);
                    this.schedule.getReadyList().get(running.getPriority()).getReady().add(running);

                    break;
                }
            }
            if(rrPreempt && !queuePreempt) {
                //this.dispatch.contextSwitchPreemptProcess(running, ready, schedule, getAlgorithmType());
                running.setState(ProcessControlBlock.ProcessState.READY);

                //ready.add(running);
                if(running.getPriority() == schedule.getReadyList().size() - 1) {  // schedule.getReadyIndex()
                    //ready.add(running);
                    this.schedule.getReadyList().get(running.getPriority()).getReady().add(running);
                } else {
                    running.updatePriority(1);
                    this.schedule.getReadyList().get(running.getPriority()).getReady().add(running);
                }

            }


        }

        for(AlgorithmsInterface algorithm: schedule.getReadyList()) {
            if(!algorithm.getReady().isEmpty()) {
                schedule.setReadyIndex(schedule.getReadyList().indexOf(algorithm));
                break;
            }
        }
        //this.ready = this.schedule.getReadyList().get(this.schedule.getReadyIndex()).getReady();














        /*
        for(AlgorithmsInterface a: schedule.getReadyList()) {
            System.out.print("[" + schedule.getReadyList().indexOf(a) + "] ");
            if(!a.getReady().isEmpty()) {
                for(ProcessControlBlock p: a.getReady()) {
                    System.out.print(p.getPid() + " ");
                }
            }
            System.out.println();
        }

        schedule.getReadyList().get(1).getReady().add(running);

        for(AlgorithmsInterface a: schedule.getReadyList()) {
            System.out.print("[" + schedule.getReadyList().indexOf(a) + "] ");
            if(!a.getReady().isEmpty()) {
                for(ProcessControlBlock p: a.getReady()) {
                    System.out.print(p.getPid() + " ");
                }
            }
            System.out.println();
        }

        for(ProcessControlBlock p: schedule.getActive()) {
            schedule.getCompleted().add(p);
        }
        schedule.getActive().clear();
        /*
         */



        ///////////////////////////////////////////////
        /*
        //int time = dispatch.getExecutionTimer();
        int execution = schedule.getReadyList().get(schedule.getReadyIndex()).getDispatcher().getExecutionTimer();
        int idle = schedule.getReadyList().get(schedule.getReadyIndex()).getDispatcher().getIdleTimer();

        schedule.getReadyList().get(schedule.getReadyIndex()).dispatchNextProcess(running);

        this.dispatch.syncMultiQueueTimers(schedule, execution, idle);



        //this.dispatch.updateMultiQueueWaitTimes(schedule, time);


        if(running != null) {
            if(running.getState() == ProcessControlBlock.ProcessState.COMPLETE) {
                schedule.flagProcessAsComplete(running);
            } else if(running.getState() == ProcessControlBlock.ProcessState.READY) {
                //if(schedule.getQueues().size() == 0) {

                if(running.getPriority() < schedule.getReadyList().size() - 1) {
                    running.updatePriority(1);
                }
                this.schedule.getReadyList().get(running.getPriority()).getReady().add(dispatch.getPreempt());

                /*
                if(running.getPriority() == schedule.getReadyList().size() - 1) {  // schedule.getReadyIndex()
                    ready.add(dispatch.getPreempt());
                } else {
                    running.updatePriority(1);

                    //dispatch.getPreempt().updatePriority(1);
                    //this.schedule.getQueues().peek().add(dispatch.getPreempt());
                    this.schedule.getReadyList().get(running.getPriority()).getReady().add(dispatch.getPreempt());
                }

                 //////////////////////////////////////////


            } else {
                schedule.getIo().add(running);
            }
        }

        //io
        if(!schedule.getReadyList().get(schedule.getReadyIndex()).getScheduler().getTempIO().isEmpty()) {
            for(ProcessControlBlock pcb: schedule.getReadyList().get(schedule.getReadyIndex()).getScheduler().getTempIO()) {
                schedule.getIo().remove(pcb);
                schedule.getReadyList().get(pcb.getPriority()).getReady().add(pcb);
            }
            schedule.getReadyList().get(schedule.getReadyIndex()).getScheduler().getTempIO().clear();
        }

        for(AlgorithmsInterface algorithm: schedule.getReadyList()) {
            if(!algorithm.getReady().isEmpty()) {
                schedule.setReadyIndex(schedule.getReadyList().indexOf(algorithm));
                break;
            }
        }

        for(AlgorithmsInterface a: schedule.getReadyList()) {
            System.out.print("[" + schedule.getReadyList().indexOf(a) + "] ");
            if(!a.getReady().isEmpty()) {
                for(ProcessControlBlock p: a.getReady()) {
                    System.out.print(p.getPid() + " ");
                }
            }
            System.out.println();
        }


*/






        /*
        if(schedule.getReadyList().get(schedule.getReadyIndex()).isCompleted()) {
            if(!isCompleted()) {
                ready = this.schedule.getQueues().poll();
                schedule.incrementReadyIndex();
                //schedule.getReadyList().get(getScheduler().getReadyIndex()).getDispatcher().setExecutionTimer(dispatch.getExecutionTimer());
                //schedule.getReadyList().get(getScheduler().getReadyIndex()).getDispatcher().setIdleTimer(dispatch.getIdleTimer());
            }
        }
        */
    }

    /** {@inheritDoc} */
    public Boolean isCompleted() {
        return this.schedule.getActive().isEmpty();
    }

    /** {@inheritDoc} */
    public Scheduler getScheduler() {
        return this.schedule;
    }

    /** {@inheritDoc} */
    public Dispatcher getDispatcher() {
        return this.dispatch;
    }

    /** {@inheritDoc} */
    public Queue<ProcessControlBlock> getReady() { return this.ready; }
}
