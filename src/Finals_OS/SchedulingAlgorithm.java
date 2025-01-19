package Finals_OS;

import java.util.*;

/**
 * Abstract class that defines a general structure for CPU scheduling
 * algorithms.
 */
public abstract class SchedulingAlgorithm {
    protected Process[] processes; // Array of processes to be scheduled
    protected List<Process> inputOrderProcesses; // List of processes in their input order
    protected List<GanttChart> ganttChart; // Gantt Chart entries for visualization
    protected int currentTime; // Keeps track of the current time in the scheduling
    protected double avgTurnAroundTime; // Average Turnaround Time
    protected double avgWaitingTime; // Average Waiting Time
    protected double cpuUtilization; // CPU Utilization percentage

    /**
     * Constructor to initialize the scheduling algorithm with a set of processes.
     * 
     * @param processes Array of processes to schedule
     */
    public SchedulingAlgorithm(Process[] processes) {
        this.processes = processes;
        this.inputOrderProcesses = new ArrayList<>();
        for (Process p : processes) {
            this.inputOrderProcesses.add(p); // Save the input order of processes
        }
        this.ganttChart = new ArrayList<>();
        this.currentTime = 0;
        this.avgTurnAroundTime = 0;
        this.avgWaitingTime = 0;
        this.cpuUtilization = 0;
    }

    /**
     * Abstract method to be implemented by subclasses to define specific scheduling
     * logic.
     */
    public abstract void schedule();

    /**
     * Calculates the metrics such as Turnaround Time, Waiting Time, and CPU
     * Utilization.
     */
    protected void calculateMetrics() {
        try {
            int totalTurnAroundTime = 0;
            int totalWaitingTime = 0;
            int totalBurstTime = 0;

            // Calculate turnaround time, waiting time, and aggregate metrics
            for (Process p : processes) {
                p.turnAroundTime = p.completionTime - p.AT; // TAT = CT - AT
                p.waitingTime = p.turnAroundTime - p.BT; // WT = TAT - BT
                totalTurnAroundTime += p.turnAroundTime;
                totalWaitingTime += p.waitingTime;
                totalBurstTime += p.BT;
            }

            // Compute averages and CPU utilization
            avgTurnAroundTime = (double) totalTurnAroundTime / processes.length;
            avgWaitingTime = (double) totalWaitingTime / processes.length;
            cpuUtilization = (double) totalBurstTime / currentTime * 100;
        } catch (Exception e) {
            System.err.println("Error calculating metrics: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays the scheduling results including process details and metrics.
     */
    public void displayResults() {
        try {
            // Print the process details in a tabular format
            System.out.println("\nP\tAT\tBT\tCT\tTAT\tWT");
            for (Process p : inputOrderProcesses) {
                System.out.printf("%-2s\t%-2d\t%-2d\t%-3d\t%-3d\t%-3d\n",
                        p.pid, p.AT, p.BT, p.completionTime,
                        p.turnAroundTime, p.waitingTime);
            }

            // Print average metrics
            System.out.printf("\nAveTAT: %.2f ms\n", avgTurnAroundTime);
            System.out.printf("AveWT: %.2f ms\n", avgWaitingTime);
            System.out.printf("CPU Util: %.2f%%\n", cpuUtilization);

            // Display the Gantt Chart
            displayGanttChart();
        } catch (Exception e) {
            System.err.println("Error displaying results: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays the Gantt Chart.
     */
    protected void displayGanttChart() {
        try {
            System.out.println("\nGantt Chart:");

            // Print Gantt Chart process labels
            for (GanttChart entry : ganttChart) {
                System.out.printf("| %-5s ", entry.label);
            }
            System.out.println("|");

            // Print Gantt Chart time markers
            for (GanttChart entry : ganttChart) {
                System.out.printf("%-7d ", entry.startTime);
            }
            System.out.println(currentTime); // Final end time
        } catch (Exception e) {
            System.err.println("Error displaying Gantt chart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Adds an entry for a process to the Gantt Chart.
     * 
     * @param processId ID of the process
     * @param startTime Start time of the process
     * @param endTime   End time of the process
     */
    protected void addToGanttChart(String processId, int startTime, int endTime) {
        try {
            ganttChart.add(new GanttChart(processId, startTime, endTime));
        } catch (Exception e) {
            System.err.println("Error adding to Gantt chart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Adds an entry for idle time to the Gantt Chart.
     * 
     * @param startTime Start time of the idle period
     * @param endTime   End time of the idle period
     */
    protected void addIdleTimeToGanttChart(int startTime, int endTime) {
        try {
            ganttChart.add(new GanttChart("////", startTime, endTime)); // "////" denotes idle time
        } catch (Exception e) {
            System.err.println("Error adding idle time to Gantt chart: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
