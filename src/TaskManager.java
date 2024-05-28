import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId;
    private Integer taskId;

    // Методы для задач
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public boolean deleteTasks() {
        tasks.clear();
        return tasks.isEmpty();
    }

    public Task getTask(Integer taskId) {
        return tasks.get(taskId);
    }

    public Task createTask(Task task) {
        task.setId(getNextId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Task updateTask(Task task) {
        taskId = task.getId();
        if (taskId == null || !tasks.containsKey(taskId)) {
            return null;
        }
        tasks.put(taskId, task);
        return task;
    }

    public boolean deleteTask(Integer taskId) {
        return tasks.remove(taskId) != null;
    }

    // Методы для эпиков
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public boolean deleteEpics() {
        epics.clear();
        deleteSubtasks(); /* Т.к. подзадачи не могут существовать без эпика, при удалении всех эпиков,
        удаляем также и подзадачи */
        return epics.isEmpty();
    }

    public Epic getEpic(Integer taskId) {
        return epics.get(taskId);
    }

    public Epic createEpic(Epic epic) {
        epic.setStatus(Status.NEW); // У всех новых эпиков статус NEW устанавливается по умолчанию
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Epic updateEpic(Epic epic) {
        taskId = epic.getId();
        if (taskId == null || !epics.containsKey(taskId)) {
            return null;
        }
        epics.put(taskId, epic);
        return epic;
    }

    public boolean deleteEpic(Integer taskId) {
        Epic epic = epics.get(taskId);
        for (Integer subtaskId : epic.getSubtasksIds()) {
            subtasks.remove(subtaskId); // При удалении эпика удаляем также все его подзадачи
        }
        return epics.remove(taskId) != null;
    }

    // Методы для подзадач
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public boolean deleteSubtasks() {
        subtasks.clear();
        for (HashMap.Entry<Integer, Epic> entry : epics.entrySet()) {
            Epic epic = entry.getValue();
            if (!epic.getSubtasksIds().isEmpty()) {  // Если у эпика есть хотя бы 1 подзадача
                epic.getSubtasksIds().clear(); // очищаем список подзадач у эпика
                checkEpicStatus(epic); //обновляем статус эпика
            }
        }
        return subtasks.isEmpty();
    }

    public Subtask getSubtask(Integer taskId) {
        return subtasks.get(taskId);
    }

    public Subtask createSubtask(Subtask subtask) {
        for (HashMap.Entry<Integer, Epic> entry : epics.entrySet()) { // Для каждой пары id и эпика
            if (subtask.getEpicId() == entry.getKey()) { // если подзадача принадлежит данному эпику
                Epic epic = entry.getValue();
                subtask.setId(getNextId()); // присваиваем подзадаче новый id
                epic.addSubtasksId(subtask.getId()); // добавляем подзадачу в список нужного эпика
                subtasks.put(subtask.getId(), subtask); // добавляем подзадачу в список всех подзадач
                checkEpicStatus(epic); // проверяем статус эпика после всех манипуляций
            }
        }
        return subtask;
    }


    public Subtask updateSubtask(Subtask subtask) {
        taskId = subtask.getId();
        if (taskId == null || !subtasks.containsKey(taskId)) {
            return null;
        }
        subtasks.put(taskId, subtask);
        for (HashMap.Entry<Integer, Epic> entry : epics.entrySet()) {
            if (subtask.getEpicId() == entry.getKey()) { // если подзадача принадлежит данному эпику
                Epic epic = entry.getValue();
                checkEpicStatus(epic); // проверяем статус эпика после всех манипуляций
            }
        }
        return subtask;
    }

    public boolean deleteSubtask(Integer taskId) {
        int epicId = subtasks.get(taskId).getEpicId(); // находим id эпика подзадачи
        epics.get(epicId).getSubtasksIds().remove("taskId"); // удаляем из списка эпика нужную подзадачу
        checkEpicStatus(epics.get(epicId)); // проверяем статус эпика после всех манипуляций
        return subtasks.remove(taskId) != null;
    }

    public ArrayList<Subtask> getSubtasksByEpicId(Integer epicId) {
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        Epic epic = getEpic(epicId);
        for (Integer subtaskId : epic.getSubtasksIds()) {
            Subtask subtask = getSubtask(subtaskId);
            subtasksByEpic.add(subtask);
        }
        return subtasksByEpic;
    }

    private void checkEpicStatus(Epic epic) { // Метод для проверки статуса эпика
        int countNew = 0;
        int countDone = 0;
        int countInProgress = 0;
        if (epic.getSubtasksIds().isEmpty()) {
            return;
        }
        for (Integer subtaskId : epic.getSubtasksIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask.getStatus() == Status.NEW) {
                countNew++;
            } else if (subtask.getStatus() == Status.DONE) {
                countDone++;
            } else if (subtask.getStatus() == Status.IN_PROGRESS) {
                countInProgress++;
            }
        }
        if (countDone == epic.getSubtasksIds().size()) { // Если все подзадачи имеют статус DONE
            epic.setStatus(Status.DONE);
        } else if (countNew == epic.getSubtasksIds().size()) { // Если все подзадачи имеют статус NEW
            epic.setStatus(Status.NEW);
        } else { // В любом другом случае
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private int getNextId() {
        return nextId++;
    }
}
