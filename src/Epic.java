import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksIds = new ArrayList<>();
    private Type type = Type.EPIC;

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic() {

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

}
