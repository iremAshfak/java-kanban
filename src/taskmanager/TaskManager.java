package taskmanager;

import java.util.ArrayList;

public interface TaskManager {
    // Методы для задач
    ArrayList<Task> getTasks();

    boolean deleteTasks();

    Task getTask(Integer taskId);

    Task createTask(Task task);

    Task updateTask(Task task);

    boolean deleteTask(Integer taskId);

    // Методы для эпиков
    ArrayList<Epic> getEpics();

    boolean deleteEpics();

    Epic getEpic(Integer taskId);

    Epic createEpic(Epic epic);

    Epic updateEpic(Epic epic);

    boolean deleteEpic(Integer taskId);

    // Методы для подзадач
    ArrayList<Subtask> getSubtasks();

    boolean deleteSubtasks();

    Subtask getSubtask(Integer taskId);

    Subtask createSubtask(Subtask subtask);

    Subtask updateSubtask(Subtask subtask);

    boolean deleteSubtask(Integer taskId);

    ArrayList<Subtask> getSubtasksByEpicId(Integer epicId);
}
