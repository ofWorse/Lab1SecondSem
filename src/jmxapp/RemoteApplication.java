package jmxapp;

import jmxapp.utils.TaskHolderImpl;

public class RemoteApplication implements RemoteApplicationMBean {

    private final TaskHolderImpl taskHolder = new TaskHolderImpl();

    @Override
    public String submit(String name, String classpath, String mainClass, int period) {
        return taskHolder.addTask(name, classpath, mainClass, period);
    }

    @Override
    public String status() {
        return taskHolder.showStatus();
    }

    @Override
    public String cancel(String name) {
        return taskHolder.cancelTask(name);
    }

    @Override
    public void startProfiling(String name) {
        taskHolder.restartAndStartProfiling(name);
    }

    @Override
    public void stopProfiling(String name) {
        taskHolder.restartAndStopProfiling(name);
    }
}