package taskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("Погладить шторы", "Нужен утюг", Status.NEW);
        Task task2 = new Task("Купить лейку", "Продается на wb", Status.NEW);
        Epic epic1 = new Epic("Написать и защитить диплом", "По лингвистике");
        Epic epic2 = new Epic("Сделать ТЗ-4", "В Яндекс Практикуме");
        Subtask subtask1 = new Subtask("Составить оглавление", "Отправить научнику", Status.NEW, 2);
        Subtask subtask2 = new Subtask("Написать первую часть", "Это теоретическая глава", Status.NEW, 2);
        Subtask subtask3 = new Subtask("Протестировать программу", "Дописать taskManager.Main", Status.IN_PROGRESS, 3);

        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createEpic(epic2);
        inMemoryTaskManager.createSubtask(subtask1);
        inMemoryTaskManager.createSubtask(subtask2);
        inMemoryTaskManager.createSubtask(subtask3);

        System.out.println("// Тест 1: Вывести списки задач, подзадач и эпиков");
        System.out.println();
        System.out.println("Список задач:");
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println("Список эпиков:");
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println("Список подзадач:");
        System.out.println(inMemoryTaskManager.getSubtasks());
        System.out.println();

        inMemoryTaskManager.getTask(0);
        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getEpic(3);
        inMemoryTaskManager.getSubtask(6);

        System.out.println("//Тест 2: Получаем историю просмотров задач:");
        System.out.println();
        System.out.println(historyManager.getHistory());
        System.out.println();


        System.out.println("//Тест 3: Изменяем статусы задач и подзадач, проверяем статус эпика");
        System.out.println();
        Task updatedTask = new Task("Погладить шторы", "Нужен утюг", Status.IN_PROGRESS, 1);
        Subtask updatedSubtask = new Subtask("Составить оглавление", "Отправить научнику", Status.IN_PROGRESS, 4,2);
        System.out.println("Изменяем статусы задач:");
        System.out.println("Обновленная задача: " + inMemoryTaskManager.updateTask(updatedTask));
        System.out.println("Обновленная подзадача: " + inMemoryTaskManager.updateSubtask(updatedSubtask));
        System.out.println("Проверяем статус эпика: " + inMemoryTaskManager.getEpic(2));
        System.out.println();

        System.out.println("// Тест 4: Удаляем задачу и один из эпиков: ");
        System.out.println();
        inMemoryTaskManager.deleteTask(0);
        System.out.println("В списке задач остались следующие задачи: " + inMemoryTaskManager.getTasks());
        inMemoryTaskManager.deleteEpic(3);
        System.out.println("В списке эпиков остался следующие эпики: " + inMemoryTaskManager.getEpics());
        System.out.println();

        System.out.println("// Тест 5: Выводим список подзадач определенного эпика");
        System.out.println();
        System.out.println("Список подзадач эпика: " + inMemoryTaskManager.getSubtasksByEpicId(2));

        System.out.println("// Тест 6: Выводим списки всех типов задач после всех изменений");
        System.out.println();
        System.out.println("Список задач:");
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println("Список эпиков:");
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println("Список подзадач:");
        System.out.println(inMemoryTaskManager.getSubtasks());
        System.out.println();

        inMemoryTaskManager.deleteTasks();
        System.out.println("Список задач:");
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println("Список эпиков:");
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println("Список подзадач:");
        System.out.println(inMemoryTaskManager.getSubtasks());

        task1.setStartTime(LocalDateTime.parse("2024-07-25T13:50:21"));
        task1.setDuration(Duration.ofDays(3));
        task2.setStartTime(LocalDateTime.parse("2024-07-19T13:50:21"));
        task2.setDuration(Duration.ofDays(2));
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        System.out.println("Список задач по времени выполнения:");
        System.out.println(inMemoryTaskManager.prioritizedTasks);
    }
}