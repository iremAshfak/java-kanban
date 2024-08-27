public class Subtask extends Task {
    private Integer epicId;
    private Type type = Type.SUBTASK;

    public Subtask(String name, String description, Status status, Integer epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, Integer id, Integer epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public Subtask() {

    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }
}