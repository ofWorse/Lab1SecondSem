package jmxapp.utils;

import jmxapp.ProfilingTasks;
import jmxapp.enums.Status;

import java.net.MalformedURLException;
import java.net.URLClassLoader;

public class Task {
    private int period;
    private String pathToMain;
    private String pathToJar;
    private Thread thread;
    private String name;
    private Status status;
    private ClassLoader classLoader;

    public void runTask() {
        thread = new Thread(() -> {
            try {
                JarRunner.runTask(this);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        status = Status.RUNNING;
    }
    public void cancel() {
        thread.interrupt();
    }

    public void restartWithProfiling() {
        ProfilingTasks.tasks.add(this);
        cancel();
        try {
            thread.join();
        } catch (InterruptedException e) {}
        runTask();
    }

    public void restartWithNoProfiling() {
        ProfilingTasks.tasks.remove(this);
        cancel();
        try {
            thread.join();
        } catch (InterruptedException e) {}
        runTask();
    }

    public void setPeriod(int period) {
        this.period = Math.max(period, 0);
    }
    public int getPeriod() { return period; }
    public void setPathToMain(String pathToMain) { this.pathToMain = pathToMain; }
    public String getPathToMain() { return pathToMain; }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() { return this.name; }
    public void setStatus(Status status) {
        this.status = status;
    }
    public String getPathToJar() {
        return pathToJar;
    }
    public void setPathToJar(String pathToJar) {
        this.pathToJar = pathToJar;
    }
    public Status getStatus() {
        return this.status;
    }
    public ClassLoader getClassLoader() {
        return classLoader;
    }
    public void setClassLoader(URLClassLoader loader) {
        this.classLoader = loader;
    }
}
