package httpserver;
import taskmanager.InMemoryTaskManager;
import taskmanager.Managers;
import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    public static final int PORT = 8080;

    private final HttpServer httpServer;
    private final InMemoryTaskManager taskManager;
    private final Gson gson;

    public HttpTaskServer(InMemoryTaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать сервер");
        }

        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        this.httpServer.createContext("/tasks", new TaskHttpHandler(taskManager, gson));
        this.httpServer.createContext("/epics", new EpicHttpHandler(taskManager, gson));
        this.httpServer.createContext("/subtasks", new SubtaskHttpHandler(taskManager, gson));
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    public static void main(String[] args) throws IOException {
        InMemoryTaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    public static Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter()).create();
    }

}
