package taskManager;

import static java.lang.Integer.parseInt;

public class CSVFormatter {

    private CSVFormatter() {
    }

    public static String toString(Task task) {
        return task.getType() + "," +
                task.getId() + "," +
                task.getName() + "," +
                task.getDescription() + "," +
                task.getStatus() + "," +
                task.getEpicId();
    }

    public static Task fromString(String csvRow) {
        String[] splitTask = csvRow.split(",");
        if (splitTask[0].equals(String.valueOf(Type.TASK))) {
            Task task = new Task();
            task.setId(parseInt(splitTask[1]));
            task.setName(splitTask[2]);
            task.setDescription(splitTask[3]);
            task.setStatus(Status.valueOf(splitTask[4]));
            return task;
        } else if (splitTask[0].equals(String.valueOf(Type.EPIC))) {
            Epic epic = new Epic();
            epic.setId(parseInt(splitTask[1]));
            epic.setName(splitTask[2]);
            epic.setDescription(splitTask[3]);
            epic.setStatus(Status.valueOf(splitTask[4]));
            return epic;
        } else {
            Subtask subtask = new Subtask();
            subtask.setId(parseInt(splitTask[1]));
            subtask.setName(splitTask[2]);
            subtask.setDescription(splitTask[3]);
            subtask.setStatus(Status.valueOf(splitTask[4]));
            subtask.setEpicId(parseInt(splitTask[5]));
            return subtask;
        }
    }

    public static String getHeader() {
        return "type,id,name,description,status,epicId";
    }
}