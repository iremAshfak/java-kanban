package httpApiServer;

import com.sun.net.httpserver.HttpExchange;
import taskmanager.Epic;
import taskmanager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHttpHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public EpicHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Integer id = getIdFromPath(exchange.getRequestURI().getPath());
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (id == null) {
                    List<Epic> epics = taskManager.getEpics();
                    String response = HttpTaskServer.getGson().toJson(epics);
                    sendText(exchange, response);
                } else {
                    Epic epic = taskManager.getEpic(id);
                    String response = HttpTaskServer.getGson().toJson(epic);
                    sendText(exchange, response);
                }
                break;
            case "POST":
                Epic epic = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8), Epic.class);
                if (id == null) {
                    taskManager.createEpic(epic);
                    String response = HttpTaskServer.getGson().toJson(epic);
                    sendText(exchange, response);
                } else {
                    taskManager.updateEpic(epic);
                    String response = HttpTaskServer.getGson().toJson(epic);
                    sendText(exchange, response);
                }
                break;
            case "DELETE":
                taskManager.deleteEpic(id);
                sendText(exchange, "");
                break;
            default:
                throw new RuntimeException();
        }
    }
}
