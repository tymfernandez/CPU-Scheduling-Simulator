package Finals_OS;

// Inner class to represent Gantt chart entries
public class GanttChart {
    String label;
    int startTime;
    int endTime;

    public GanttChart(String label, int startTime, int endTime) {
        this.label = label;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}