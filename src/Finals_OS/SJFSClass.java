package Finals_OS;

public class SJFSClass extends SchedulingAlgorithm {

    // Constructor to initialize the SJFSClass with an array of processes
    public SJFSClass(Process[] processes) {
        super(processes);
    }

    @Override
    public void schedule() {
        try {
            int completed = 0; // Number of completed processes
            boolean[] isCompleted = new boolean[processes.length]; // Array to track completed processes
            currentTime = findMinArrivalTime(); // Initialize current time to the minimum arrival time

            // Check for initial idle time
            if (currentTime > 0) {
                addIdleTimeToGanttChart(0, currentTime); // Add initial idle time to Gantt chart
            }

            while (completed < processes.length) {
                int minBurstIndex = -1; // Index of the process with the minimum burst time
                int minBurst = Integer.MAX_VALUE; // Minimum burst time

                // Find the process with the minimum burst time that has arrived
                for (int i = 0; i < processes.length; i++) {
                    if (!isCompleted[i] && processes[i].AT <= currentTime) {
                        if (processes[i].BT < minBurst) {
                            minBurst = processes[i].BT;
                            minBurstIndex = i;
                        }
                    }
                }

                if (minBurstIndex == -1) {
                    // If no process is ready, increment currentTime and add idle time
                    int nextArrivalTime = findNextArrivalTime(currentTime); // Find the next arrival time
                    addIdleTimeToGanttChart(currentTime, nextArrivalTime); // Add idle time to Gantt chart
                    currentTime = nextArrivalTime; // Update current time
                    continue;
                }

                // Process the selected process
                Process p = processes[minBurstIndex];
                ganttChart.add(new GanttChart(p.pid, currentTime, currentTime + p.BT)); // Add to Gantt chart
                currentTime += p.BT; // Update current time
                p.completionTime = currentTime; // Set completion time
                isCompleted[minBurstIndex] = true; // Mark process as completed
                completed++; // Increment completed count
            }

            calculateMetrics(); // Calculate metrics after scheduling
        } catch (Exception e) {
            System.err.println("Error during SJF scheduling: " + e.getMessage());
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