package taskmanager;

public class ManagerSaveException extends RuntimeException {

    private static final String MSG_SAVE = "Ошибка в процессе сохранения";
    private static final String MSG_LOAD = "Ошибка в процессе загрузки";

    public static ManagerSaveException saveException(Exception e) {
        return new ManagerSaveException(MSG_SAVE, e);
    }

    public static ManagerSaveException loadException(Exception e) {
        return new ManagerSaveException(MSG_LOAD, e);
    }

    private ManagerSaveException(String msg, Exception e) {
        super(msg, e);
    }

}
