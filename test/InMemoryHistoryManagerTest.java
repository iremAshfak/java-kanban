import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TaskManager taskManager = Managers.getDefault();


    @Test
    void noRepeatsInHistory() {
        Task task = new Task("Test noRepeatsInHistory", "Test noRepeatsInHistory description", Status.NEW);
        taskManager.createTask(task);
        taskManager.getTask(task.getId());
        taskManager.getTask(task.getId()); //просматриваем одну и ту же задачу 2 раза и проверяем историю

        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "Предыдущий просмотр одной и той же задачи не удаляется");
    }

    @Test
    void noTaskInHistoryAfterDeleting() {
        Task task = new Task("Test noTaskInHistoryAfterDeleting", "Test noTaskInHistoryAfterDeleting description", Status.NEW);
        taskManager.createTask(task);
        taskManager.getTask(task.getId());
        taskManager.deleteTask(task.getId()); // удаляем просмотренную задачу и проверяем историю

        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "После удаления задачи она не удалилась из истории");
    }


    @Test
    void historySizeShouldBeUnlimited() {
        for (int i = 0; i < 100; i++) {
            Task task = new Task("Test historySizeShouldBeUnlimited", "Test historySizeShouldBeUnlimited description", Status.NEW);
            taskManager.createTask(task);
            taskManager.getTask(task.getId());
        }
        ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(101, history.size(), "Размер истории все еще ограничен");
    }

}