package httpserver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import taskmanager.Epic;
import taskmanager.InMemoryTaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class EpicHttpHandler extends BaseHttpHandler {


    public EpicHttpHandler(InMemoryTaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Integer id = getIdFromPath(exchange.getRequestURI().getPath());
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (id == null) {
                    ArrayList<Epic> epics = taskManager.getEpics();
                    String response = HttpTaskServer.getGson().toJson(epics, new TypeToken<ArrayList<Epic>>() {
                    }.getType());
                    sendText(exchange, response);
                } else {
                    Epic epic = taskManager.getEpic(id);
                    String response = HttpTaskServer.getGson().toJson(epic, Epic.class);
                    sendText(exchange, response);
                }
                break;
            case "POST":
                Epic epic = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8), Epic.class);
                if (id == null) {
                    taskManager.createEpic(epic);
                    String response = HttpTaskServer.getGson().toJson(epic, Epic.class);
                    sendTextPosted(exchange, response);
                } else {
                    taskManager.updateEpic(epic);
                    String response = HttpTaskServer.getGson().toJson(epic, Epic.class);
                    sendTextPosted(exchange, response);
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
