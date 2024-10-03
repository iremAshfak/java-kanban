package httpApiServer;

import com.sun.net.httpserver.HttpExchange;
import taskmanager.HistoryManager;
import taskmanager.InMemoryTaskManager;
import taskmanager.Task;

import java.io.IOException;
import java.util.List;

public class UserHttpHandler extends BaseHttpHandler {
    private final HistoryManager historyManager;
    private final InMemoryTaskManager taskManager;

    public UserHttpHandler(HistoryManager historyManager, InMemoryTaskManager taskManager) {
        this.historyManager = historyManager;
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        List<Task> tasks;
        String command = getCommand(exchange.getRequestURI().getPath());

        switch (command) {
            case "history":
                tasks = historyManager.getHistory();
                response = HttpTaskServer.getGson().toJson(tasks);
                break;
            case "prioritized":
                tasks = taskManager.getPrioritizedTasks().stream().toList();
                response = HttpTaskServer.getGson().toJson(tasks);
                break;
        }
        sendText(exchange, response);
    }


    protected String getCommand(String path) {
        String[] parts = path.split("/");
        if (parts.length >= 2) {
            return parts[1];
        }
        return null;
    }
}
