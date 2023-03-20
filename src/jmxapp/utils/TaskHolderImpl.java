package jmxapp.utils;

import jmxapp.enums.Status;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TaskHolderImpl {
    //                                  Runnable status
    //                                  обертка для запуска задач, которая загружает
    //                                  с помощью  jar classloader чтобы ловить все исключения
    protected ConcurrentHashMap<String, Task> tasks =
            new ConcurrentHashMap<>();

    //                                         jar              main
    public String addTask(String name, String classpath, String mainClass, int period) {
        putTaskToHash(name, classpath, mainClass, period);
        tasks.get(name).runTask();
        return "Задача установлена!";
    }

    // TODO!!!
    private void putTaskToHash(String name, String classpath, String mainClass, int period) {
        var currentTask = new Task();
        currentTask.setName(name);
        currentTask.setPathToJar(classpath);
        currentTask.setPathToMain(mainClass);
        currentTask.setPeriod(period);
        tasks.put(name, currentTask);
    }

    public String showStatus() {
        if(tasks.isEmpty()) return Status.EMPTY.getTaskStatus();
        return tasks.values().stream()
                .sorted(Comparator.comparing(Task::getName))
                .map(task -> task.getName() + ": " + task.getStatus())
                .collect(Collectors.joining("\n"));
    }

    public String cancelTask(String name) {
        if(tasks.containsKey(name)) {
            tasks.get(name).cancel();
            return "поток: " + name + " " + tasks.get(name).getStatus();
        } else return "такого потока не существует.";
    }

    public void restartAndStartProfiling(String name) {
        if(tasks.containsKey(name)) {
            tasks.get(name).restartWithProfiling();
        }
    }

    public void restartAndStopProfiling(String name) {
        if(tasks.containsKey(name)) {
            tasks.get(name).restartWithNoProfiling();
        }
    }
}
