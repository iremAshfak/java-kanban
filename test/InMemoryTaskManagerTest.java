import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class InMemoryTaskManagerTest {

    TaskManager taskManager = Managers.getDefault();

    @Test
    public void createTaskTest() {
        Task task = new Task("Test createTask", "Test createTask description", Status.NEW);
        final int taskId = taskManager.createTask(task).getId();
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final ArrayList<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    public void createEpicTest() {
        Epic epic = new Epic("Test createEpic", "Test createEpic description");
        final int epicId = taskManager.createEpic(epic).getId();
        final Epic savedEpic = taskManager.getEpic(epicId);
        assertNotNull(savedEpic, "Эпик не найдена.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final ArrayList<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.getFirst(), "Эпики не совпадают.");
    }

    @Test
    public void createSubtaskTest() {
        taskManager.createEpic(new Epic("Test createSubtask", "Test createSubtask description"));
        Subtask subtask = new Subtask("Test createSubtask", "Test createSubtask description", Status.NEW, 0);
        final int subtaskId = taskManager.createSubtask(subtask).getId();
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);
        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final ArrayList<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.getFirst(), "Подзадачи не совпадают.");
    }

    @Test
    public void updateTaskTest() {
        Task task = new Task("Test updateTask", "Test updateTask description", Status.NEW);
        taskManager.createTask(task);
        Task updatedTask = new Task("Test updateTask", "Test updateTask description", Status.IN_PROGRESS, task.getId());
        taskManager.updateTask(updatedTask);
        final ArrayList<Task> tasks = taskManager.getTasks();
        Task savedTask = taskManager.getTask(task.getId());

        assertEquals(updatedTask, savedTask, "Задачи не совпадают.");

        assertEquals(1, tasks.size(), "Неверное количество задач.");
    }

    @Test
    public void updateSubtaskTest() {
        Epic epic = new Epic("Test updateSubtask", "Test updateSubtask description");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Test updateSubtask", "Test updateSubtask description", Status.NEW, epic.getId());
        taskManager.createSubtask(subtask);
        Subtask updatedSubtask = new Subtask("Test updateSubtask", "Test updateSubtask description", Status.IN_PROGRESS, subtask.getId(),epic.getId());
        taskManager.updateSubtask(updatedSubtask);
        final ArrayList<Subtask> subtasks = taskManager.getSubtasks();
        Subtask savedSubtask = taskManager.getSubtask(subtask.getId());

        assertEquals(updatedSubtask, savedSubtask, "Задачи не совпадают.");

        assertEquals(1, subtasks.size(), "Неверное количество задач.");
    }

    @Test
    public void deleteTaskTest() {
        Task task1 = new Task("Test deleteTask1", "Test deleteTask1 description", Status.NEW);
        Task task2 = new Task("Test deleteTask2", "Test deleteTask2 description", Status.NEW);
        final int taskId1 = taskManager.createTask(task1).getId();
        final int taskId2 = taskManager.createTask(task2).getId();
        taskManager.deleteTask(taskId1);
        assertEquals(1, taskManager.getTasks().size(), "Задача не была удалена");

        taskManager.deleteTasks();
        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    public void deleteEpicTest() {
        Epic epic1 = new Epic("Test deleteEpic1", "Test deleteEpic1 description");
        Epic epic2 = new Epic("Test deleteEpic2", "Test deleteEpic2 description");
        final int epicId1 = taskManager.createEpic(epic1).getId();
        final int epicId2 = taskManager.createEpic(epic2).getId();
        taskManager.deleteEpic(epicId1);
        ArrayList<Epic> epics = taskManager.getEpics();
        assertEquals(1, taskManager.getEpics().size(), "Эпик не был удален");

        taskManager.deleteEpics();
        assertTrue(taskManager.getEpics().isEmpty());
    }

    @Test
    public void deleteSubtaskTest() {
        Epic epic = new Epic("Test updateEpic", "Test updateEpic description");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Test deleteSubtask1", "Test deleteSubtask1 description", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Test deleteSubtask2", "Test deleteSubtask2 description", Status.NEW, epic.getId());
        final int subtaskId1 = taskManager.createSubtask(subtask1).getId();
        final int subtaskId2 = taskManager.createSubtask(subtask2).getId();
        taskManager.deleteSubtask(subtaskId1);
        ArrayList<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(1, taskManager.getSubtasks().size(), "Задача не была удалена");

        taskManager.deleteSubtasks();
        assertTrue(taskManager.getSubtasks().isEmpty());
    }

    @Test
    public void task1ShouldBeEqualTask2() {
        Task task1 = new Task("Test task1EqualTask2", "Test task1EqualTask2 description", Status.NEW);
        taskManager.createTask(task1);
        Task task2 = new Task("Task1EqualTask2", "Task1EqualTask2", Status.NEW, task1.getId());
        assertTrue(task1.getId().equals(task2.getId()));
    }

    @Test
    public void subtask1ShouldBeEqualSubtask2() {
        Epic epic = new Epic("Epic", "Test Epic");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Test subtask1EqualSubtask2", "Test subtask1EqualSubtask2 description", Status.NEW, epic.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask1EqualSubtask2", "Subtask1EqualSubtask2", Status.NEW, subtask1.getId(), epic.getId());
        assertTrue(subtask1.getId().equals(subtask2.getId()));
    }

    @Test
    public void EpicShouldBeEmpty() {
        Epic epic = new Epic("Epic", "Test Epic");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Test EpicShouldBeEmpty", "Test EpicShouldBeEmpty description", Status.NEW, epic.getId());
        taskManager.createSubtask(subtask1);
        taskManager.deleteSubtask(subtask1.getId());
        assertTrue(epic.getSubtasksIds().isEmpty());
    }

    /* @Test
    public void epicAsSubtaskShouldBeFailed() {
        Epic epic = taskManager.createEpic(new Epic("Test epicAsSubtask", "Test epicAsSubtask description"));
        taskManager.createSubtask(epic);
    }

    @Test
    public void subtaskAsEpicShouldBeFailed() {
        Subtask subtask = new Subtask("Test subtaskAsEpic", "Test subtaskAsEpic description", Status.NEW, subtask.getId());
    }
*/
}