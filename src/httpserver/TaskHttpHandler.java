package httpserver;

import com.sun.net.httpserver.HttpExchange;
import taskmanager.Task;
import taskmanager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHttpHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public TaskHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Integer id = getIdFromPath(exchange.getRequestURI().getPath());
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (id == null) {
                    List<Task> tasks = taskManager.getTasks();
                    String response = HttpTaskServer.getGson().toJson(tasks);
                    sendText(exchange, response);
                } else {
                    Task task = taskManager.getTask(id);
                    String response = HttpTaskServer.getGson().toJson(task);
                    sendText(exchange, response);
                }
                break;
            case "POST":
                Task task = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8), Task.class);
                if (id == null) {
                    taskManager.createTask(task);
                    String response = HttpTaskServer.getGson().toJson(task);
                    sendText(exchange, response);
                } else {
                    taskManager.updateTask(task);
                    String response = HttpTaskServer.getGson().toJson(task);
                    sendText(exchange, response);
                }
                break;
            case "DELETE":
                taskManager.deleteTask(id);
                sendText(exchange, "");
                break;
            default:
                throw new RuntimeException();
        }
    }
}