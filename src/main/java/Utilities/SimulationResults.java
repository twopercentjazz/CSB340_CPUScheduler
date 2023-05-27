/** This class manages all the final results for a scheduling simulation. */

package Utilities;
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

    /** Constructs a simulation results object.
     * @param input The processes
     * @param totalExecutionTime The total execution time
     * @param totalIdleTime The total idle time */
    public SimulationResults(ProcessControlBlock[] input, int totalExecutionTime, int totalIdleTime) {
        setTotalExecutionTime(totalExecutionTime);
        setTotalIdleTime(totalIdleTime);
        this.allWaitTimes = new ArrayList<>();
        this.allTurnAroundTimes = new ArrayList<>();
        this.allResponseTimes = new ArrayList<>();
        populateLists(input);
        calcResults();
    }

    /** This method records all wait, turnaround, and response times into lists.
     * @param input The processes */
    private void populateLists(ProcessControlBlock[] input) {
        for(ProcessControlBlock p : input) {
            this.allWaitTimes.add(p.getWaitingTime());
            this.allTurnAroundTimes.add(p.getTurnAroundTime());
            this.allResponseTimes.add(p.getResponseTime());
        }
    }

    /** This method gets the total execution time.
     * @return The total execution time */
    public int getTotalExecutionTime() {
        return this.totalExecutionTime;
    }

    /** This method sets the total execution time.
     * @param time total execution time */
    public void setTotalExecutionTime(int time) {
        this.totalExecutionTime = time;
    }

    /** This method gets the total idle time.
     * @return The total idle time */
    public int getTotalIdleTime() {
        return this.totalIdleTime;
    }

    /** This method sets the total idle time.
     * @param time total idle time */
    public void setTotalIdleTime(int time) {
        this.totalIdleTime = time;
    }

    /** This method gets CPU utilization.
     * @return The CPU utilization */
    public double getCpuUtilization() {
        return this.cpuUtilization;
    }

    /** This method calculates CPU utilization. */
    public void calcCpuUtilization() {
        this.cpuUtilization = ((totalExecutionTime - totalIdleTime)/ (double) totalExecutionTime);
    }

    /** This method gets the average wait time.
     * @return The average wait time */
    public double getAvgWait() {
        return this.avgWait;
    }

    /** This method calculates the average wait time. */
    public void calcAvgWait() {
        avgWait = findAvg(this.allWaitTimes);
    }

    /** This method gets the average turnaround time.
     * @return The average turnaround time */
    public double getAvgTurnAround() {
        return this.avgTurnAround;
    }

    /** This method calculates the average turnaround time. */
    public void calcAvgTurnAround() {
        avgTurnAround = findAvg(this.allTurnAroundTimes);
    }

    /** This method gets the average response time.
     * @return The average response time */
    public double getAvgResponse() {
        return this.avgResponse;
    }

    /** This method calculates the average response time. */
    public void calcAvgResponse() {
        avgResponse = findAvg(this.allResponseTimes);
    }

    /** This method calculates all results. */
    public void calcResults() {
        calcCpuUtilization();
        calcAvgWait();
        calcAvgTurnAround();
        calcAvgResponse();
    }

    /** This method calculates the average of a list of numbers.
     * @param list The list of numbers to find the average of
     * @return The average */
    private double findAvg(ArrayList<Integer> list) {
        int size = list.size();
        int sum = 0;
        for(Integer n : list) {
            sum += n;
        }
        return (double) sum / size;
    }

    /** This method gets the list of all wait times.
     * @return The list of all wait times */
    public ArrayList<Integer> getAllWaitTimes() {
        return this.allWaitTimes;
    }

    /** This method gets the list of all turnaround times.
     * @return The list of all turnaround times */
    public ArrayList<Integer> getAllTurnAroundTimes() {
        return this.allTurnAroundTimes;
    }

    /** This method gets the list of all response times.
     * @return The list of all response times */
    public ArrayList<Integer> getAllResponseTimes() {
        return this.allResponseTimes;
    }
}