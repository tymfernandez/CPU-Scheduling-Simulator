package Finals_OS;

import java.util.*;

public class RoundRobinClass extends SchedulingAlgorithm {

    private int timeQuantum; // Time quantum for the Round Robin scheduling algorithm

    public RoundRobinClass(Process[] processes, int timeQuantum) {
        super(processes); // Initialize the base class with the list of processes
        this.timeQuantum = timeQuantum; // Set the time quantum
    }

    @Override
    public void schedule() {
        try {
            // Array to keep track of remaining burst time for each process
            int[] remainingTime = new int[processes.length];
            for (int i = 0; i < processes.length; i++) {
                remainingTime[i] = processes[i].BT; // Initialize remaining time with burst time
            }

            Queue<Integer> readyQueue = new LinkedList<>(); // Queue to manage ready processes
            boolean[] inQueue = new boolean[processes.length]; // Tracks if a process is in the ready queue
            int completed = 0; // Counter for completed processes
            currentTime = findMinArrivalTime(); // Start time is the earliest arrival time

            // Check for initial idle time before the first process arrives
            if (currentTime > 0) {
                addIdleTimeToGanttChart(0, currentTime); // Add idle time to the Gantt Chart
            }

            while (completed < processes.length) { // Loop until all processes are completed
                // Add processes that have arrived to the ready queue
                for (int i = 0; i < processes.length; i++) {
                    if (processes[i].AT <= currentTime && remainingTime[i] > 0 && !inQueue[i]) {
                        readyQueue.offer(i); // Add process index to the queue
                        inQueue[i] = true; // Mark process as in the queue
                    }
                }

                if (readyQueue.isEmpty()) {
                    // If no process is ready, move time forward to the next arrival time
                    int nextArrivalTime = findNextArrivalTime(currentTime);
                    addIdleTimeToGanttChart(currentTime, nextArrivalTime); // Add idle time to Gantt Chart
                    currentTime = nextArrivalTime; // Update the current time
                    continue;
                }

                // Get the next process from the ready queue
                int currentProcessIndex = readyQueue.poll();
                inQueue[currentProcessIndex] = false; // Mark process as not in the queue

                // Execute the process for the time quantum or remaining time, whichever is smaller
                int executionTime = Math.min(timeQuantum, remainingTime[currentProcessIndex]);
                addToGanttChart(processes[currentProcessIndex].pid, currentTime, currentTime + executionTime);
                currentTime += executionTime; // Update current time
                remainingTime[currentProcessIndex] -= executionTime; // Decrease the remaining time for the process

                if (remainingTime[currentProcessIndex] == 0) {
                    // If the process is completed
                    processes[currentProcessIndex].completionTime = currentTime; // Set completion time
                    processes[currentProcessIndex].turnAroundTime = processes[currentProcessIndex].completionTime
                            - processes[currentProcessIndex].AT; // Calculate turnaround time
                    processes[currentProcessIndex].waitingTime = processes[currentProcessIndex].turnAroundTime
                            - processes[currentProcessIndex].BT; // Calculate waiting time
                    completed++; // Increment the completed process count
                } else {
                    // If the process is not completed, re-add it to the ready queue
                    readyQueue.offer(currentProcessIndex);
                    inQueue[currentProcessIndex] = true;
                }
            }
        } catch (Exception e) {
            // Handle any errors during scheduling
            System.err.println("Error during Round Robin scheduling: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Find the minimum arrival time of all processes
    private int findMinArrivalTime() {
        int minArrivalTime = Integer.MAX_VALUE;
        for (Process p : processes) {
            if (p.AT < minArrivalTime) {
                minArrivalTime = p.AT;
            }
        }
        return minArrivalTime;
    }

    // Find the next arrival time after the current time
    private int findNextArrivalTime(int currentTime) {
        int nextArrivalTime = Integer.MAX_VALUE;
        for (Process p : processes) {
            if (p.AT > currentTime && p.AT < nextArrivalTime) {
                nextArrivalTime = p.AT;
            }
        }
        return nextArrivalTime;
    }

    @Override
    public void displayResults() {
        try {
            // Display table header
            System.out.println("\nP\tAT\tBT\tCT\tTAT\tWT");
            for (Process p : processes) {
                // Print details of each process
                System.out.printf("%-2s\t%-2d\t%-2d\t%-3d\t%-3d\t%-3d\n",
                        p.pid, p.AT, p.BT, p.completionTime,
                        p.turnAroundTime, p.waitingTime);
            }

            // Display calculated averages and CPU utilization
            System.out.printf("\nAveTAT: %.2f ms\n", avgTurnAroundTime);
            System.out.printf("AveWT: %.2f ms\n", avgWaitingTime);
            System.out.printf("CPU Util: %.2f%%\n", cpuUtilization);
            displayGanttChart(); // Display the Gantt Chart
        } catch (Exception e) {
            // Handle any errors during result display
            System.err.println("Error displaying results: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
