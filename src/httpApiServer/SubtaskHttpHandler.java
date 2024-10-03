package httpApiServer;

import com.sun.net.httpserver.HttpExchange;
import taskmanager.Subtask;
import taskmanager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHttpHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public SubtaskHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Integer id = getIdFromPath(exchange.getRequestURI().getPath());
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (id == null) {
                    List<Subtask> subtasks = taskManager.getSubtasks();
                    String response = HttpTaskServer.getGson().toJson(subtasks);
                    sendText(exchange, response);
                } else {
                    Subtask subtask = taskManager.getSubtask(id);
                    String response = HttpTaskServer.getGson().toJson(subtask);
                    sendText(exchange, response);
                }
                break;
            case "POST":
                Subtask subtask = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8), Subtask.class);
                if (id == null) {
                    taskManager.createSubtask(subtask);
                    String response = HttpTaskServer.getGson().toJson(subtask);
                    sendText(exchange, response);
                } else {
                    taskManager.updateSubtask(subtask);
                    String response = HttpTaskServer.getGson().toJson(subtask);
                    sendText(exchange, response);
                }
                break;
            case "DELETE":
                taskManager.deleteSubtask(id);
                sendText(exchange, "");
                break;
            default:
                throw new RuntimeException();
        }
    }
}
