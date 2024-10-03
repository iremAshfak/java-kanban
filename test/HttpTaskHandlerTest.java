import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import httpserver.HttpTaskServer;
import taskmanager.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static java.net.http.HttpClient.newHttpClient;
import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskHandlerTest {

    // создаём экземпляр InMemoryTaskManager
    InMemoryTaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteTasks();
        manager.deleteSubtasks();
        manager.deleteEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test 2", "Testing task 2");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(10));

        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test 2", "Testing task 2");
        task.setDuration(Duration.ofMinutes(5));
        task.setStartTime(LocalDateTime.now());

        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1).build();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");

        //Обновляем задачу
        task.setId(tasksFromManager.getFirst().getId());
        task.setStatus(Status.DONE);
        // конвертируем её в JSON
        taskJson = gson.toJson(task);
        // создаём запрос
        url = URI.create("http://localhost:8080/tasks/" + tasksFromManager.getFirst().getId());
        request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        // вызываем рест, отвечающий за обновление задач
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        //Проверяем, что статус задачи обновился
        tasksFromManager = manager.getTasks();
        assertEquals(Status.DONE, tasksFromManager.getFirst().getStatus(), "Некорректный статус");
    }

    @Test
    public void testGetTasksList() throws IOException, InterruptedException {
        // создаём задачу
        Task task_1 = new Task("Test 1", "Testing task 1");
        task_1.setDuration(Duration.ofMinutes(5));
        task_1.setStartTime(LocalDateTime.now());

        // конвертируем её в JSON
        String taskJson = gson.toJson(task_1);

        // создаём HTTP-клиент и запрос
        HttpClient client = newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // создаём задачу
        Task task_2 = new Task("Test 2", "Testing task 2");
        task_2.setDuration(Duration.ofMinutes(10));

        // конвертируем её в JSON
        taskJson = gson.toJson(task_2);

        url = URI.create("http://localhost:8080/tasks");
        request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        //Получаем список задач
        request = HttpRequest.newBuilder().uri(url).GET().build();
        // вызываем рест, отвечающий за получение списка задач
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        List<Task> tasksList = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());

        assertNotNull(tasksList, "Задачи не возвращаются");
        assertEquals(2, tasksList.size(), "Некорректное количество задач");
        assertEquals("Test 1", tasksList.getFirst().getName(), "Некорректное имя задачи");
        assertEquals("Test 2", tasksList.getLast().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        // создаём задачу
        Task task_1 = new Task("Test 1", "Testing task 1");
        task_1.setDuration(Duration.ofMinutes(5));
        task_1.setStartTime(LocalDateTime.now());

        // конвертируем её в JSON
        String taskJson = gson.toJson(task_1);

        // создаём HTTP-клиент и запрос
        HttpClient client = newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // создаём задачу
        Task task_2 = new Task("Test 2", "Testing task 2");
        task_2.setDuration(Duration.ofMinutes(10));

        // конвертируем её в JSON
        taskJson = gson.toJson(task_2);

        url = URI.create("http://localhost:8080/tasks");
        request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        //Получаем список задач
        request = HttpRequest.newBuilder().uri(url).GET().build();
        // вызываем рест, отвечающий за получение списка
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        List<Task> tasksList = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());

        assertNotNull(tasksList, "Задачи не возвращаются");
        assertEquals(2, tasksList.size(), "Некорректное количество задач");

        url = URI.create("http://localhost:8080/tasks/" + tasksList.getLast().getId());
        // Получаем задачу по ID
        request = HttpRequest.newBuilder().uri(url).GET().build();
        // вызываем рест, отвечающий за получение задачи по id
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        Task task = gson.fromJson(response.body(), Task.class);
        assertEquals("Test 2", task.getName(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test 2", "Testing task 2");
        task.setDuration(Duration.ofMinutes(5));
        task.setStartTime(LocalDateTime.now());

        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");

        url = URI.create("http://localhost:8080/tasks/" + tasksFromManager.getFirst().getId());
        request = HttpRequest.newBuilder().uri(url).DELETE().build();

        // вызываем рест, отвечающий за удаление задач
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        tasksFromManager = manager.getTasks();
        assertTrue(tasksFromManager.isEmpty(), "Задачи не удаляются");
    }
}
