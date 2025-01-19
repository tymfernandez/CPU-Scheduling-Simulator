package Finals_OS;

public class Process {
    String pid;
    int AT; // Arrival Time
    int BT; // Burst Time
    int priority; // Priority (used for priority scheduling)
    int completionTime;
    int waitingTime;
    int turnAroundTime;
    int responsetime = -1; // Used for preemptive algorithms
    int tempburstTime; // Copy of BT for calculations

    public Process(String pid, int AT, int BT, int priority) {
        this.pid = pid;
        this.AT = AT;
        this.BT = BT;
        this.priority = priority;
        this.tempburstTime = BT; // Initialize temp BT
    }
}
