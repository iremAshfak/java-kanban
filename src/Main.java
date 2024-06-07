public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task1 = new Task("Погладить шторы", "Нужен утюг", Status.NEW);
        Task task2 = new Task("Купить лейку", "Продается на wb", Status.NEW);
        Epic epic1 = new Epic("Написать и защитить диплом", "По лингвистике");
        Epic epic2 = new Epic("Сделать ТЗ-4", "В Яндекс Практикуме");
        Subtask subtask1 = new Subtask("Составить оглавление", "Отправить научнику", Status.NEW, 2);
        Subtask subtask2 = new Subtask("Написать первую часть", "Это теоретическая глава", Status.NEW, 2);
        Subtask subtask3 = new Subtask("Протестировать программу", "Дописать Main", Status.IN_PROGRESS, 3);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        System.out.println("// Тест 1: Вывести списки задач, подзадач и эпиков");
        System.out.println();
        System.out.println("Список задач:");
        System.out.println(taskManager.getTasks());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpics());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubtasks());
        System.out.println();

        taskManager.getTask(0);
        taskManager.getTask(1);
        taskManager.getEpic(3);
        taskManager.getSubtask(6);

        System.out.println("//Тест 2: Получаем историю просмотров задач:");
        System.out.println();
        System.out.println(historyManager.getHistory());
        System.out.println();


        System.out.println("//Тест 3: Изменяем статусы задач и подзадач, проверяем статус эпика");
        System.out.println();
        Task updated_task1 = new Task("Погладить шторы", "Нужен утюг", Status.IN_PROGRESS, 1);
        Subtask updated_subtask1 = new Subtask("Составить оглавление", "Отправить научнику", Status.IN_PROGRESS, 4,2);
        System.out.println("Изменяем статусы задач:");
        System.out.println("Обновленная задача: " + taskManager.updateTask(updated_task1));
        System.out.println("Обновленная подзадача: " + taskManager.updateSubtask(updated_subtask1));
        System.out.println("Проверяем статус эпика: " + taskManager.getEpic(2));
        System.out.println();

        System.out.println("// Тест 4: Удаляем задачу и один из эпиков: ");
        System.out.println();
        taskManager.deleteTask(0);
        System.out.println("В списке задач остались следующие задачи: " + taskManager.getTasks());
        taskManager.deleteEpic(3);
        System.out.println("В списке эпиков остался следующие эпики: " + taskManager.getEpics());
        System.out.println();

        System.out.println("// Тест 5: Выводим список подзадач определенного эпика");
        System.out.println();
        System.out.println("Список подзадач эпика: " + taskManager.getSubtasksByEpicId(2));

        System.out.println("// Тест 6: Выводим списки всех типов задач после всех изменений");
        System.out.println();
        System.out.println("Список задач:");
        System.out.println(taskManager.getTasks());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpics());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubtasks());
        System.out.println();

        System.out.println("// Тест 7: Удаляем все задачи, эпики, подзадачи");
        System.out.println();
        taskManager.deleteTasks();
        taskManager.deleteEpics();
        taskManager.deleteSubtasks();
        System.out.println("Список задач:");
        System.out.println(taskManager.getTasks());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpics());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubtasks());
    }
}