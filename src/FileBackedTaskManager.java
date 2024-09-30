import java.io.BufferedWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.nio.file.Path;
import java.io.IOException;
import java.io.FileWriter;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path file;

    public FileBackedTaskManager(Path file) {
        super();
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(Path file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        int maxId = 0;
        try {
            List<String> lines = Files.readAllLines(file);
            for (int i = 1; i < lines.size(); i++) {
                Task task = CSVFormatter.fromString(lines.get(i));
                switch (task.getType()) {
                    case TASK -> fileBackedTaskManager.tasks.put(task.getId(), task);
                    case EPIC -> fileBackedTaskManager.epics.put(task.getId(), (Epic) task);
                    case SUBTASK -> fileBackedTaskManager.subtasks.put(task.getId(), (Subtask) task);
                }
                if (maxId < task.getId()) {
                    maxId = task.getId();
                }
            }

        } catch (IOException e) {
            throw ManagerSaveException.loadException(e);
        }
        fileBackedTaskManager.setNextId(maxId + 1);
        return fileBackedTaskManager;
    }


    @Override
    public boolean deleteTasks() {
        boolean isEmpty = super.deleteTasks();
        save();
        return isEmpty;
    }

    @Override
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task);
        save();
        return createdTask;
    }

    @Override
    public Task updateTask(Task task) {
        Task updatedTask = super.updateTask(task);
        save();
        return updatedTask;
    }

    @Override
    public boolean deleteTask(Integer taskId) {
        boolean isEmpty = super.deleteTask(taskId);
        save();
        return isEmpty;
    }

    @Override
    public boolean deleteEpics() {
        boolean isEmpty = super.deleteEpics();
        save();
        return isEmpty;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createdEpic = super.createEpic(epic);
        save();
        return createdEpic;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic updatedEpic = super.updateEpic(epic);
        save();
        return updatedEpic;
    }

    @Override
    public boolean deleteEpic(Integer taskId) {
        boolean isEmpty = super.deleteEpic(taskId);
        save();
        return isEmpty;
    }

    @Override
    public boolean deleteSubtasks() {
        boolean isEmpty = super.deleteSubtasks();
        save();
        return isEmpty;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask createdSubtask = super.createSubtask(subtask);
        save();
        return createdSubtask;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Subtask updatedSubtask = super.updateSubtask(subtask);
        save();
        return updatedSubtask;
    }

    @Override
    public boolean deleteSubtask(Integer taskId) {
        boolean isEmpty = super.deleteSubtask(taskId);
        save();
        return isEmpty;
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.toFile()))) {
            bw.write(CSVFormatter.getHeader());
            bw.newLine();

            for (HashMap.Entry<Integer, Task> elem: tasks.entrySet()) {
                bw.write(CSVFormatter.toString(elem.getValue()));
                bw.newLine();
            }

            for (HashMap.Entry<Integer, Epic> elem: epics.entrySet()) {
                bw.write(CSVFormatter.toString(elem.getValue()));
                bw.newLine();
            }

            for (HashMap.Entry<Integer, Subtask> elem: subtasks.entrySet()) {
                bw.write(CSVFormatter.toString(elem.getValue()));
                bw.newLine();
            }

        } catch (IOException e) {
            throw ManagerSaveException.saveException(e);
        }
    }
}