import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskManager taskManager = Managers.getDefault();

    @Test
    void addTaskInHistory() {
        Task task = new Task("Test createTask", "Test createTask description", Status.NEW);
        historyManager.add(task);
        final ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void historySizeShouldBeEquals10() {
        for (int i = 0; i < 12; i++) {
            Task task = new Task("Test createTask", "Test createTask description", Status.NEW);
            taskManager.createTask(task);
            taskManager.getTask(task.getId());
        }
        final ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(10, history.size(), "История не пустая.");
    }

}