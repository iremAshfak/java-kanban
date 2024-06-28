import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ManagersTest {

    @Test
    void testGetDefaultInMemoryTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        assertEquals(taskManager.getClass(), InMemoryTaskManager.class);
    }

    @Test
    void testGetDefaultInMemoryHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertEquals(historyManager.getClass(), InMemoryHistoryManager.class);
    }

}