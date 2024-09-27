import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FileBackedTaskManagerTest {

    File file;

    @Test
    void checkEpicStatusTest() throws IOException {
        this.file = File.createTempFile("time", "filee");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file.toPath());

        Task task1 = new Task("Погладить шторы", "Нужен утюг", Status.NEW);
        Epic epic1 = new Epic("Написать и защитить диплом", "По лингвистике");
        Epic epic2 = new Epic("Сделать ТЗ-4", "В Яндекс Практикуме");
        Subtask subtask1 = new Subtask("Составить оглавление", "Отправить научнику", Status.NEW, 2);

        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        String content = Files.readString(file.toPath());

        FileBackedTaskManager secondTaskManager = FileBackedTaskManager.loadFromFile(file.toPath());

        secondTaskManager.createTask(task1);
        secondTaskManager.createEpic(epic1);
        secondTaskManager.createEpic(epic2);
        secondTaskManager.createSubtask(subtask1);
        String secondContent = Files.readString(file.toPath()); // создаем идентичные задачи в том же порядке

        assertNotEquals(content, secondContent, "Задачи из первого taskManager не были сохранены");
        /* если бы задачи из первого taskManager не сохранялись и не использовались для создания secondTaskManager,
         содержимое файлов было бы одинаковым. */
    }
}
