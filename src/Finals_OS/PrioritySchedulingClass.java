package Finals_OS;

public class PrioritySchedulingClass extends SchedulingAlgorithm {

    private boolean isPreemptive; // Determines if the scheduling is preemptive or non-preemptive

    public PrioritySchedulingClass(Process[] processes, boolean isPreemptive) {
        super(processes);
        this.isPreemptive = isPreemptive; // Initialize preemptive mode
    }

    @Override
    public void schedule() {
        try {
            if (isPreemptive) {
                schedulePreemptive(); // Execute preemptive scheduling logic
            } else {
                scheduleNonPreemptive(); // Execute non-preemptive scheduling logic
            }
            calculateMetrics(); // Calculate performance metrics after scheduling
        } catch (Exception e) {
            System.err.println("Error during scheduling: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Handles preemptive priority scheduling
    private void schedulePreemptive() {
        try {
            int completed = 0; // Tracks the number of completed processes
            int[] remainingTime = new int[processes.length]; // Remaining burst times for each process

            // Initialize remaining burst times
            for (int i = 0; i < processes.length; i++) {
                remainingTime[i] = processes[i].BT;
            }

            currentTime = findMinArrivalTime(); // Start at the earliest arrival time
            int prevProcess = -1; // Tracks the previously executed process

            // Handle initial idle time if the CPU starts idle
            if (currentTime > 0) {
                addIdleTimeToGanttChart(0, currentTime);
            }

            while (completed < processes.length) {
                int highestPriorityIndex = -1; // Index of the highest-priority process
                int highestPriority = Integer.MAX_VALUE; // Lower value indicates higher priority

                // Find the highest-priority ready process
                for (int i = 0; i < processes.length; i++) {
                    if (remainingTime[i] > 0 && processes[i].AT <= currentTime) {
                        if (processes[i].priority < highestPriority) {
                            highestPriority = processes[i].priority;
                            highestPriorityIndex = i;
                        }
                    }
                }

                if (highestPriorityIndex == -1) {
                    // If no process is ready, handle idle time and wait for the next arrival
                    int nextArrivalTime = findNextArrivalTime(currentTime);
                    addIdleTimeToGanttChart(currentTime, nextArrivalTime);
                    currentTime = nextArrivalTime;
                    continue;
                }

                // Add or update Gantt Chart based on process execution
                if (prevProcess != highestPriorityIndex) {
                    ganttChart.add(new GanttChart(processes[highestPriorityIndex].pid, currentTime, currentTime + 1));
                    prevProcess = highestPriorityIndex;
                } else {
                    ganttChart.get(ganttChart.size() - 1).endTime++;
                }

                remainingTime[highestPriorityIndex]--; // Decrement remaining time for the process
                currentTime++; // Increment the current time

                // Check if the process is completed
                if (remainingTime[highestPriorityIndex] == 0) {
                    processes[highestPriorityIndex].completionTime = currentTime;
                    completed++;
                    prevProcess = -1; // Reset previous process tracking
                }
            }
        } catch (Exception e) {
            System.err.println("Error during preemptive scheduling: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Handles non-preemptive priority scheduling
    private void scheduleNonPreemptive() {
        try {
            currentTime = findMinArrivalTime(); // Start at the earliest arrival time

            // Handle initial idle time if the CPU starts idle
            if (currentTime > 0) {
                addIdleTimeToGanttChart(0, currentTime);
            }

            while (true) {
                int highestPriorityIndex = -1;
                int highestPriority = Integer.MAX_VALUE;

                // Find the highest-priority ready process
                for (int i = 0; i < processes.length; i++) {
                    if (processes[i].AT <= currentTime && processes[i].completionTime == 0) {
                        if (processes[i].priority < highestPriority) {
                            highestPriority = processes[i].priority;
                            highestPriorityIndex = i;
                        }
                    }
                }

                if (highestPriorityIndex == -1) {
                    // Handle idle time and move to the next arrival if no process is ready
                    int nextArrivalTime = findNextArrivalTime(currentTime);
                    if (nextArrivalTime == Integer.MAX_VALUE) {
                        break; // Exit if all processes are completed
                    }
                    addIdleTimeToGanttChart(currentTime, nextArrivalTime);
                    currentTime = nextArrivalTime;
                    continue;
                }

                // Schedule the highest-priority process
                Process p = processes[highestPriorityIndex];
                ganttChart.add(new GanttChart(p.pid, currentTime, currentTime + p.BT));
                currentTime += p.BT; // Update current time
                p.completionTime = currentTime; // Set completion time
            }
        } catch (Exception e) {
            System.err.println("Error during non-preemptive scheduling: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Finds the earliest arrival time among all processes
    private int findMinArrivalTime() {
        int minArrivalTime = Integer.MAX_VALUE;
        for (Process p : processes) {
            if (p.AT < minArrivalTime) {
                minArrivalTime = p.AT;
            }
        }
        return minArrivalTime;
    }

    // Finds the next arrival time after the current time
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
            // Display process details including priority
            System.out.println("\nP\tPriority\tAT\tBT\tCT\tTAT\tWT");
            char processID = 'A';
            for (Process p : processes) {
                System.out.printf("%-2s\t%-8d\t%-2d\t%-2d\t%-3d\t%-3d\t%-3d\n",
                        processID, p.priority, p.AT, p.BT, p.completionTime,
                        p.turnAroundTime, p.waitingTime);
                processID++;
            }

            // Display average metrics and Gantt Chart
            System.out.printf("\nAveTAT: %.2f ms\n", avgTurnAroundTime);
            System.out.printf("AveWT: %.2f ms\n", avgWaitingTime);
            System.out.printf("CPU Util: %.2f%%\n", cpuUtilization);
            displayGanttChart();
        } catch (Exception e) {
            System.err.println("Error displaying results: " + e.getMessage());
            e.printStackTrace();
        }
    }
}