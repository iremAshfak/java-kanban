import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    File file;

    @BeforeEach
    void beforeEach() throws Exception {
        this.file = File.createTempFile("tasks", ".csv");
        assertNotNull(file, "Файл не создан");
        assertTrue(file.exists(), "Файл не создан");
    }

    @Test
    void fileBackedTaskManagerTest() throws IOException {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file.toPath());

        Task task1 = new Task("Погладить шторы", "Нужен утюг", Status.NEW);
        Epic epic1 = new Epic("Написать и защитить диплом", "По лингвистике");
        Epic epic2 = new Epic("Сделать ТЗ-4", "В Яндекс Практикуме");
        Subtask subtask1 = new Subtask("Составить оглавление", "Отправить научнику", Status.NEW, 2);

        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);

        FileBackedTaskManager secondTaskManager = FileBackedTaskManager.loadFromFile(file.toPath());
        ArrayList<Task> tasksFromTaskManager = secondTaskManager.getTasks();
        ArrayList<Task> tasksFromSecondTaskManager = taskManager.getTasks();

        assertEquals(tasksFromTaskManager,tasksFromSecondTaskManager); // сравнила содержимое двух менеджеров
    }
}
