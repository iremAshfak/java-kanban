import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksIds = new ArrayList<>();
    private LocalDateTime endTime;
    private Type type = Type.EPIC;

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic() {

    }

    public Epic(Epic epic) {
        super(epic.getName(), epic.getDescription());
        this.type = epic.getType();
        this.duration = epic.getDuration();
        this.startTime = epic.getStartTime();
        this.endTime = epic.getStartTime();
    }

    private void updateDatesAndDuration(HashMap<Integer, Subtask> subtasks) {
        this.startTime = subtasks.values().stream()
                .filter(subtask -> subtask.startTime != null)
                .min(Comparator.comparing(subtask -> subtask.startTime))
                .map(subtask -> subtask.startTime)
                .orElse(null);

        this.duration = Duration.ofMinutes(
                subtasks.values().stream()
                        .filter(subtask -> subtask.duration != null)
                        .mapToLong(subtask -> subtask.duration.toMinutes())
                        .sum()
        );

        this.endTime = subtasks.values().stream()
                .filter(subtask -> subtask.startTime != null)
                .max(Comparator.comparing(Task::getEndTime))
                .map(Task::getEndTime)
                .orElse(null);
    }


    @Override
    public Type getType() {
        return type;
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void addSubtasksId(Integer subtaskId) {
        subtasksIds.add(subtaskId);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

}
