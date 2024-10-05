package httpserver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import taskmanager.InMemoryTaskManager;
import taskmanager.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SubtaskHttpHandler extends BaseHttpHandler {


    public SubtaskHttpHandler(InMemoryTaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Integer id = getIdFromPath(exchange.getRequestURI().getPath());
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (id == null) {
                    ArrayList<Subtask> subtasks = taskManager.getSubtasks();
                    String response = HttpTaskServer.getGson().toJson(subtasks, new TypeToken<ArrayList<Subtask>>() {
                    }.getType());
                    sendText(exchange, response);
                } else {
                    Subtask subtask = taskManager.getSubtask(id);
                    String response = HttpTaskServer.getGson().toJson(subtask, Subtask.class);
                    sendText(exchange, response);
                }
                break;
            case "POST":
                Subtask subtask = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8), Subtask.class);
                if (id == null) {
                    taskManager.createSubtask(subtask);
                    String response = HttpTaskServer.getGson().toJson(subtask, Subtask.class);
                    sendTextPosted(exchange, response);
                } else {
                    taskManager.updateSubtask(subtask);
                    String response = HttpTaskServer.getGson().toJson(subtask, Subtask.class);
                    sendTextPosted(exchange, response);
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
