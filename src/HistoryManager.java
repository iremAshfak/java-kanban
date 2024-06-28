import java.util.ArrayList;
import java.util.Map;

public interface HistoryManager {

    void add(Task task);

    void remove(int id);

    ArrayList<Task> getHistory();

    Map<Integer, InMemoryHistoryManager.Node> getNodeMap();
}
