import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        File file = new File(String.valueOf(File.createTempFile("time", "file")));
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toPath());
        Task task1 = new Task("Погладить шторы", "Нужен утюг", Status.NEW);
        Task task2 = new Task("Купить лейку", "Продается на wb", Status.NEW);
        Epic epic1 = new Epic("Написать и защитить диплом", "По лингвистике");
        Epic epic2 = new Epic("Сделать ТЗ-4", "В Яндекс Практикуме");
        Subtask subtask1 = new Subtask("Составить оглавление", "Отправить научнику", Status.NEW, 2);
        Subtask subtask2 = new Subtask("Написать первую часть", "Это теоретическая глава", Status.NEW, 2);
        Subtask subtask3 = new Subtask("Протестировать программу", "Дописать Main", Status.IN_PROGRESS, 3);


        fileBackedTaskManager.createTask(task1);
        fileBackedTaskManager.createTask(task2);
        fileBackedTaskManager.createEpic(epic1);
        fileBackedTaskManager.createEpic(epic2);
        fileBackedTaskManager.createSubtask(subtask1);
        fileBackedTaskManager.createSubtask(subtask2);
        fileBackedTaskManager.createSubtask(subtask3);

        System.out.println("// Тест 1: Вывести списки задач, подзадач и эпиков");
        System.out.println();
        System.out.println("Список задач:");
        System.out.println(fileBackedTaskManager.getTasks());
        System.out.println("Список эпиков:");
        System.out.println(fileBackedTaskManager.getEpics());
        System.out.println("Список подзадач:");
        System.out.println(fileBackedTaskManager.getSubtasks());
        System.out.println();

        fileBackedTaskManager.getTask(0);
        fileBackedTaskManager.getTask(1);
        fileBackedTaskManager.getEpic(3);
        fileBackedTaskManager.getSubtask(6);

        System.out.println("//Тест 2: Получаем историю просмотров задач:");
        System.out.println();
        System.out.println(historyManager.getHistory());
        System.out.println();


        System.out.println("//Тест 3: Изменяем статусы задач и подзадач, проверяем статус эпика");
        System.out.println();
        Task updatedTask = new Task("Погладить шторы", "Нужен утюг", Status.IN_PROGRESS, 1);
        Subtask updatedSubtask = new Subtask("Составить оглавление", "Отправить научнику", Status.IN_PROGRESS, 4,2);
        System.out.println("Изменяем статусы задач:");
        System.out.println("Обновленная задача: " + fileBackedTaskManager.updateTask(updatedTask));
        System.out.println("Обновленная подзадача: " + fileBackedTaskManager.updateSubtask(updatedSubtask));
        System.out.println("Проверяем статус эпика: " + fileBackedTaskManager.getEpic(2));
        System.out.println();

        System.out.println("// Тест 4: Удаляем задачу и один из эпиков: ");
        System.out.println();
        fileBackedTaskManager.deleteTask(0);
        System.out.println("В списке задач остались следующие задачи: " + fileBackedTaskManager.getTasks());
        fileBackedTaskManager.deleteEpic(3);
        System.out.println("В списке эпиков остался следующие эпики: " + fileBackedTaskManager.getEpics());
        System.out.println();

        System.out.println("// Тест 5: Выводим список подзадач определенного эпика");
        System.out.println();
        System.out.println("Список подзадач эпика: " + fileBackedTaskManager.getSubtasksByEpicId(2));

        System.out.println("// Тест 6: Выводим списки всех типов задач после всех изменений");
        System.out.println();
        System.out.println("Список задач:");
        System.out.println(fileBackedTaskManager.getTasks());
        System.out.println("Список эпиков:");
        System.out.println(fileBackedTaskManager.getEpics());
        System.out.println("Список подзадач:");
        System.out.println(fileBackedTaskManager.getSubtasks());
        System.out.println();

        fileBackedTaskManager.deleteTasks();
        System.out.println("Список задач:");
        System.out.println(fileBackedTaskManager.getTasks());
        System.out.println("Список эпиков:");
        System.out.println(fileBackedTaskManager.getEpics());
        System.out.println("Список подзадач:");
        System.out.println(fileBackedTaskManager.getSubtasks());

        FileBackedTaskManager secondFileBackedTaskManager = fileBackedTaskManager.loadFromFile(file);
        Task secondTask = new Task("Доделать ТЗ", "Яндекс Практикума", Status.NEW);
        secondFileBackedTaskManager.createTask(secondTask);
    }
}