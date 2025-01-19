package Finals_OS;

import java.util.*;

public class FCFSClass extends SchedulingAlgorithm {

    public FCFSClass(Process[] processes) {
        super(processes); // Initialize the base class with the list of processes
    }

    @Override
    public void schedule() {
        try {
            // Sort processes by arrival time (AT) in ascending order
            Arrays.sort(processes, Comparator.comparingInt(p -> p.AT));

            // Check for initial idle time before the first process arrives
            if (processes[0].AT > 0) {
                addIdleTimeToGanttChart(0, processes[0].AT); // Add idle time to Gantt Chart
            }

            currentTime = processes[0].AT; // Set the current time to the arrival time of the first process

            for (Process p : processes) {
                // If the CPU is idle (currentTime < process arrival time), add idle time
                if (currentTime < p.AT) {
                    addIdleTimeToGanttChart(currentTime, p.AT); // Handle idle time between processes
                    currentTime = p.AT; // Update the current time to the process's arrival time
                }

                // Add process execution to the Gantt Chart
                addToGanttChart(p.pid, currentTime, currentTime + p.BT);

                // Update current time and process metrics
                currentTime += p.BT; // Advance the current time by the burst time (BT)
                p.completionTime = currentTime; // Set the completion time
                p.turnAroundTime = p.completionTime - p.AT; // Calculate Turnaround Time (TAT)
                p.waitingTime = p.turnAroundTime - p.BT; // Calculate Waiting Time (WT)
            }

            calculateMetrics(); // Calculate average metrics (TAT, WT, CPU Utilization)
        } catch (Exception e) {
            // Handle any errors that occur during the scheduling process
            System.err.println("Error during FCFS scheduling: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
