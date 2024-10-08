import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(task -> task.startTime));
    protected int nextId;
    private Integer taskId;

    // Методы для задач
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public boolean deleteTasks() {
        tasks.forEach((key, value) -> {
            prioritizedTasks.remove(value);
        });
        tasks.clear();
        return tasks.isEmpty();
    }

    @Override
    public Task getTask(Integer taskId) {
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public Task createTask(Task task) {
        if (isTasksOverlapped(task)) {
            throw new RuntimeException("Задача не может быть добавлена из-за пересечения во времени с другой задачей");
        }
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        task.setId(getNextId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        taskId = task.getId();
        if (taskId == null || !tasks.containsKey(taskId)) {
            return null;
        }
        if (isTasksOverlapped(task)) {
            throw new RuntimeException("Задача не может быть добавлена из-за пересечения во времени с другой задачей");
        }
        tasks.put(taskId, task);
        return task;
    }

    @Override
    public boolean deleteTask(Integer taskId) {
        prioritizedTasks.remove(tasks.get(taskId));
        return tasks.remove(taskId) != null;
    }

    // Методы для эпиков
    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public boolean deleteEpics() {
        epics.forEach((key, value) -> {
            prioritizedTasks.remove(value);
        });
        epics.clear();
        deleteSubtasks(); /* Т.к. подзадачи не могут существовать без эпика, при удалении всех эпиков,
        удаляем также и подзадачи */
        return epics.isEmpty();
    }

    @Override
    public Epic getEpic(Integer taskId) {
        historyManager.add(epics.get(taskId));
        return epics.get(taskId);
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (epic.getStartTime() != null) {
            prioritizedTasks.add(epic);
        }
        epic.setStatus(Status.NEW); // У всех новых эпиков статус NEW устанавливается по умолчанию
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        taskId = epic.getId();
        if (taskId == null || !epics.containsKey(taskId)) {
            return null;
        }
        epics.put(taskId, epic);
        return epic;
    }

    @Override
    public boolean deleteEpic(Integer taskId) {
        Epic epic = epics.get(taskId);
        epic.getSubtasksIds().forEach(subtaskId -> {
            subtasks.remove(subtaskId);
            prioritizedTasks.remove(subtasks.get(subtaskId));
        });
        return epics.remove(taskId) != null;
    }

    // Методы для подзадач
    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public boolean deleteSubtasks() {
        epics.values().forEach(epic -> {
            epic.getSubtasksIds().clear();
            checkEpicStatus(epic);
        });
        subtasks.values().forEach(prioritizedTasks::remove);
        subtasks.clear();

        return subtasks.isEmpty();
    }

    @Override
    public Subtask getSubtask(Integer taskId) {
        historyManager.add(subtasks.get(taskId));
        return subtasks.get(taskId);
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (isTasksOverlapped(subtask)) {
            throw new RuntimeException("Задача не может быть добавлена из-за пересечения во времени с другой задачей");
        }
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
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

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        taskId = subtask.getId();
        if (taskId == null || !subtasks.containsKey(taskId)) {
            return null;
        }
        if (isTasksOverlapped(subtask)) {
            throw new RuntimeException("Задача не может быть добавлена из-за пересечения во времени с другой задачей");
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

    @Override
    public boolean deleteSubtask(Integer taskId) {
        int epicId = subtasks.get(taskId).getEpicId(); // находим id эпика подзадачи
        epics.get(epicId).getSubtasksIds().remove("taskId"); // удаляем из списка эпика нужную подзадачу
        checkEpicStatus(epics.get(epicId));
        prioritizedTasks.remove(subtasks.get(taskId));// проверяем статус эпика после всех манипуляций
        return subtasks.remove(taskId) != null;
    }

    @Override
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

    protected void setNextId(int i) {
        nextId = i;
    }

    private boolean isPeriodsOverlapped(LocalDateTime startTime1, LocalDateTime endTime1,
                                        LocalDateTime startTime2, LocalDateTime endTime2) {
        return endTime1.isAfter(startTime1) && startTime2.isBefore(endTime1);
    }

    private Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public boolean isTasksOverlapped(Task anotherTask) {
        if (anotherTask.getStartTime() == null || anotherTask.getDuration() == null) {
            return false;
        }

        return getPrioritizedTasks().stream()
                .filter(task -> !task.equals(anotherTask))
                .anyMatch(task -> isPeriodsOverlapped(anotherTask.getStartTime(), anotherTask.getEndTime(),
                        task.getStartTime(), task.getEndTime()));

    }

}