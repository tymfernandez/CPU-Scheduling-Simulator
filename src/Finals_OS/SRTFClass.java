package Finals_OS;

public class SRTFClass extends SchedulingAlgorithm {

    public SRTFClass(Process[] processes) {
        super(processes);
    }

    @Override
    public void schedule() {
        try {
            int completed = 0; // Number of completed processes
            int[] remainingTime = new int[processes.length]; // Array to track remaining burst time for each process
            for (int i = 0; i < processes.length; i++) {
                remainingTime[i] = processes[i].BT; // Initialize remaining time with burst time
            }

            currentTime = findMinArrivalTime(); // Initialize current time to the minimum arrival time
            int prevProcess = -1; // Track the previous process

            // Check for initial idle time
            if (currentTime > 0) {
                addIdleTimeToGanttChart(0, currentTime); // Add initial idle time to Gantt chart
            }

            while (completed < processes.length) {
                int minRemainingIndex = -1; // Index of the process with the minimum remaining time
                int minRemaining = Integer.MAX_VALUE; // Minimum remaining time

                // Find the process with the minimum remaining time that has arrived
                for (int i = 0; i < processes.length; i++) {
                    if (remainingTime[i] > 0 && processes[i].AT <= currentTime) {
                        if (remainingTime[i] < minRemaining) {
                            minRemaining = remainingTime[i];
                            minRemainingIndex = i;
                        }
                    }
                }

                if (minRemainingIndex == -1) {
                    // If no process is ready, increment currentTime and add idle time
                    int nextArrivalTime = findNextArrivalTime(currentTime); // Find the next arrival time
                    addIdleTimeToGanttChart(currentTime, nextArrivalTime); // Add idle time to Gantt chart
                    currentTime = nextArrivalTime; // Update current time
                    continue;
                }

                // Process the selected process
                Process p = processes[minRemainingIndex];
                if (prevProcess != minRemainingIndex) {
                    ganttChart.add(new GanttChart(p.pid, currentTime, currentTime + 1)); // Add to Gantt chart
                } else {
                    ganttChart.get(ganttChart.size() - 1).endTime++; // Extend the current Gantt chart entry
                }
                remainingTime[minRemainingIndex]--; // Decrement remaining time
                currentTime++; // Increment current time

                if (remainingTime[minRemainingIndex] == 0) {
                    p.completionTime = currentTime; // Set completion time
                    completed++; // Increment completed count
                }

                prevProcess = minRemainingIndex; // Update previous process
            }

            calculateMetrics(); // Calculate metrics after scheduling
        } catch (Exception e) {
            System.err.println("Error during SRTF scheduling: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Find the minimum arrival time among all processes
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
}