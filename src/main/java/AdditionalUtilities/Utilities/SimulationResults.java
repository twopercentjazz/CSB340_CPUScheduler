/** This class manages all the final results from a scheduling simulation. */

package AdditionalUtilities.Utilities;
import java.util.ArrayList;

public class SimulationResults {
    private int totalExecutionTime;
    private int totalIdleTime;
    private double cpuUtilization;
    private ArrayList<Integer> allWaitTimes;
    private ArrayList<Integer> allTurnAroundTimes;
    private ArrayList<Integer> allResponseTimes;
    private double avgWait;
    private double avgTurnAround;
    private double avgResponse;

    public SimulationResults(ProcessControlBlock[] input, int totalExecutionTime, int totalIdleTime) {
        setTotalExecutionTime(totalExecutionTime);
        setTotalIdleTime(totalIdleTime);
        this.allWaitTimes = new ArrayList<>();
        this.allTurnAroundTimes = new ArrayList<>();
        this.allResponseTimes = new ArrayList<>();
        populateLists(input);
        calcResults();
    }

    private void populateLists(ProcessControlBlock[] input) {
        for(ProcessControlBlock p : input) {
            this.allWaitTimes.add(p.getWaitingTime());
            this.allTurnAroundTimes.add(p.getTurnAroundTime());
            this.allResponseTimes.add(p.getResponseTime());
        }
    }

    public int getTotalExecutionTime() {
        return this.totalExecutionTime;
    }

    public void setTotalExecutionTime(int time) {
        this.totalExecutionTime = time;
    }

    public int getTotalIdleTime() {
        return this.totalIdleTime;
    }

    public void setTotalIdleTime(int time) {
        this.totalIdleTime = time;
    }

    public double getCpuUtilization() {
        return this.cpuUtilization;
    }

    public void calcCpuUtilization() {
        this.cpuUtilization = ((totalExecutionTime - totalIdleTime)/ (double) totalExecutionTime);
    }

    public double getAvgWait() {
        return this.avgWait;
    }
    public void calcAvgWait() {
        avgWait = findAvg(this.allWaitTimes);
    }

    public double getAvgTurnAround() {
        return this.avgTurnAround;
    }

    public void calcAvgTurnAround() {
        avgTurnAround = findAvg(this.allTurnAroundTimes);
    }

    public double getAvgResponse() {
        return this.avgResponse;
    }

    public void calcAvgResponse() {
        avgResponse = findAvg(this.allResponseTimes);
    }

    public void calcResults() {
        calcCpuUtilization();
        calcAvgWait();
        calcAvgTurnAround();
        calcAvgResponse();
    }

    private double findAvg(ArrayList<Integer> list) {
        int size = list.size();
        int sum = 0;
        for(Integer n : list) {
            sum += n;
        }
        return (double) sum / size;
    }

    public ArrayList<Integer> getAllWaitTimes() {
        return this.allWaitTimes;
    }

    public ArrayList<Integer> getAllTurnAroundTimes() {
        return this.allTurnAroundTimes;
    }

    public ArrayList<Integer> getAllResponseTimes() {
        return this.allResponseTimes;
    }
}