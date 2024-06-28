import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskManager taskManager = Managers.getDefault();

    @Test
    void addTaskInHistory() {
        Task task = new Task("Test addTaskInHistory", "Test addTaskInHistory description", Status.NEW);
        historyManager.add(task);
        final ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void historySizeShouldBeUnlimited() {
        for (int i = 0; i < 1000; i++) {
            Task task = new Task("Test historySizeShouldBeUnlimited", "Test historySizeShouldBeUnlimited description", Status.NEW);
            taskManager.createTask(task);
            taskManager.getTask(task.getId());
        }
        final ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1000, history.size(), "Размер истории все еще ограничен");
    }

    @Test
    void noRepeatsInHistory() {
        Task task = new Task("Test noRepeatsInHistory", "Test noRepeatsInHistory description", Status.NEW);
        taskManager.createTask(task);
        taskManager.getTask(task.getId());
        taskManager.getTask(task.getId()); //просматриваем одну и ту же задачу 2 раза и проверяем историю

        final ArrayList<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "Предыдущий просмотр одной и той же задачи не удаляется");
    }

    @Test
    void noTaskInHistoryAfterDeleting() {
        Task task = new Task("Test noTaskInHistoryAfterDeleting", "Test noTaskInHistoryAfterDeleting description", Status.NEW);
        taskManager.createTask(task);
        taskManager.getTask(task.getId());
        taskManager.deleteTask(task.getId()); // удаляем просмотренную задачу и проверяем историю

        final ArrayList<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "После удаления задачи она не удалилась из истории");
    }
}