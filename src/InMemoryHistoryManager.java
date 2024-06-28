import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Node first;
    private Node last;

    private static Map<Integer, Node> nodeMap = new HashMap<>();
    private static ArrayList<Task> history = new ArrayList<>();
    private static ArrayList<Node> nodes = new ArrayList<>();
    private static ArrayList<Task> tasks = new ArrayList<>();

    public Map<Integer, Node> getNodeMap() {
        return nodeMap;
    }

    @Override
    public void add(Task task) {
        Node node = linkLast(task);
        removeNode(node);
        nodeMap.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.remove(id);
        if (node == null) {
            return;
        }
        removeNode(node);
    }

    private void removeNode(Node node) {
        if (node.prev == null && node.next == null) {
            first = null;
            last = null;
        } else if (node.next == null) {
            last = node.prev;
        } else if (node.prev == null) {
            first = node.next;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        for (Map.Entry<Integer, Node> entry : nodeMap.entrySet()) {
            history.add(entry.getValue().value);
        }
        return history;
    }

    public static class Node {
        Node prev;
        Node next;
        Task value;

        Node(Task value) {
            this.value = value;
        }
    }

    private Node linkLast(Task task) {
        Node node = new Node(task);
        if (first == null) {
            node.prev = null;
            first = node;
        } else {
             node.prev = last;
             node.prev.next = node;
        }
        last = node;
        nodes.add(node);
        return node;
    }

    private ArrayList<Task> getTasks(ArrayList<Node> nodes) {
        for (Node node : nodes) {
            tasks.add(node.value);
        }
        return tasks;
    }
}

