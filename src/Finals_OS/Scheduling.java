/*
    * CPU Scheduling Simulator (Finals Project)
     ----------------------------------------
 * John Tymothy Fernandez
 * Kenneth Hular
 * Marc Daniel Enguero
 * Jam Vincent Jaspe Guevarra
 * Christian Louie Corpuz
 * 
 * BSCS 3-2
 */


// Main Class
package Finals_OS;

import java.util.*;

public class Scheduling {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                // Display the welcome message and menu options
                System.out.println("\n----------------------------------------");
                System.out.println("\nWelcome to CPU Scheduling Simulator!");
                System.out.println("\nCPU Scheduling Algorithms:");
                System.out.println("1. First Come First Serve (FCFS)");
                System.out.println("2. Shortest Job First (SJF)");
                System.out.println("3. Shortest Remaining Time First (SRTF)");
                System.out.println("4. Priority Scheduling (Non-Preemptive)");
                System.out.println("5. Priority Scheduling (Preemptive)");
                System.out.println("6. Round Robin");
                System.out.println("7. Exit");
                System.out.println("\n----------------------------------------");

                // Prompt the user to choose an algorithm
                System.out.print("\nChoose an algorithm (1-7): ");
                int choice = -1;
                while (true) {
                    try {
                        choice = sc.nextInt();
                        if (choice >= 1 && choice <= 7) {
                            break;
                        } else {
                            System.out.print("Invalid choice! Please choose a valid algorithm (1-7): ");
                        }
                    } catch (InputMismatchException e) {
                        System.out.print("Invalid input! Please enter a number between 1 and 7: ");
                        sc.next(); // Clear the invalid input
                    }
                }
                // Exit the program if the user chooses option 7
                if (choice == 7) {
                    System.out.println("Exiting Program...");
                    break;
                }
                // Prompt the user to enter the number of processes
                System.out.print("Enter the number of processes: ");
                int n = sc.nextInt();

                Process[] processes = new Process[n];

                for (int i = 0; i < n; i++) {
                    String pid = "P" + (i + 1);
                    int priority = 0;

                    // Only ask for priority if the chosen algorithm is priority scheduling
                    if (choice == 4 || choice == 5) {
                        System.out.print("Enter priority for process " + pid + ": ");
                        priority = sc.nextInt();
                    }

                    // Collect process details from the user
                    System.out.print("Enter Arrival Time (AT) for process " + pid + ": ");
                    int AT = sc.nextInt();

                    System.out.print("Enter Burst Time (BT) for process " + pid + ": ");
                    int BT = sc.nextInt();

                    // Ensure priority is only set when relevant
                    processes[i] = new Process(pid, AT, BT, priority);
                }

                SchedulingAlgorithm scheduler = null;

                // Instantiate the appropriate scheduling algorithm based on user choice
                switch (choice) {
                    case 1:
                        scheduler = new FCFSClass(processes);
                        break;
                    case 2:
                        scheduler = new SJFSClass(processes); // Ensure this class exists
                        break;
                    case 3:
                        scheduler = new SRTFClass(processes); // Ensure this class exists
                        break;
                    case 4:
                        scheduler = new PrioritySchedulingClass(processes, false); // Non-Preemptive
                        break;
                    case 5:
                        scheduler = new PrioritySchedulingClass(processes, true); // Preemptive
                        break;
                    case 6:
                        System.out.print("Enter time quantum: ");
                        int timeQuantum = sc.nextInt();
                        scheduler = new RoundRobinClass(processes, timeQuantum); // Ensure this class exists
                        break;
                    default:
                        System.out.println("Invalid choice! Please select a valid option.");
                        continue;
                }

                // Schedule the processes and display the results
                if (scheduler != null) {
                    scheduler.schedule();
                    scheduler.displayResults();
                }

                // Ask the user if they want to simulate another algorithm
                System.out.print("\nDo you want to simulate another algorithm? (y/n): ");
                sc.nextLine(); // Consume the newline character
                String anotherSimulation = sc.nextLine().trim().toLowerCase();
                if (!anotherSimulation.equals("y")) {
                    System.out.println("Exiting Program...");
                    break;
                }

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
                sc.next(); // Clear the invalid input
            }
        }

        sc.close();
    }
}
