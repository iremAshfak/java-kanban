import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import taskmanager.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    @ParameterizedTest
    @MethodSource("provideParameters")
    void fullTestScenario(HistoryManager historyManager) {

        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task(
                "Задача 1",
                "Тестовая задача 1");
        task1.setStartTime(LocalDateTime.parse("2024-07-25T13:50:21"));
        task1.setDuration(Duration.ofDays(3));
        taskManager.createTask(task1);

        Epic epic1 = new Epic(
                "Эпик 1",
                "Тестовый эпик 1");
        taskManager.createEpic(epic1);
        Subtask subtask1ForEpic1 = new Subtask(
                "Подзадача 1.1",
                "Тестовая подзадача 1 для эпика 1",
                epic1.getId());
        subtask1ForEpic1.setStartTime(LocalDateTime.parse("2024-07-19T13:50:21"));
        subtask1ForEpic1.setDuration(Duration.ofDays(2));

        historyManager.add(epic1);
        historyManager.add(task1);

        assertEquals(historyManager.getHistory().getLast().getId(), task1.getId(),
                "Некорректный последний элемент");

        assertEquals(historyManager.getHistory().getFirst().getId(), epic1
                        .getId(),
                "Некорректный первый элемент");

    }
    private static Stream<Arguments> provideParameters() {
        return Stream.of(Arguments.of(Managers.getDefaultHistory()));
    }
}