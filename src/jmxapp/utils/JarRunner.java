package jmxapp.utils;


import jmxapp.enums.Status;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.Callable;

public class JarRunner {
    public static void runTask(Task task) throws MalformedURLException {
        var url = new URL("jar:file:" + task.getPathToJar() + "!/");

        try (var loader = new URLClassLoader(new URL[]{url})) {
            task.setClassLoader(loader);
            var clazz = loader.loadClass(task.getPathToMain());
            var obj = (Callable<Void>) clazz.getConstructor().newInstance();

            if (task.getPeriod() == 0)
                obj.call();
            else while (!Thread.currentThread().isInterrupted()) {
                obj.call();
                Thread.sleep(task.getPeriod() * 1000L);
            }
        } catch (InterruptedException e) {
            System.out.println("task interrupted");
            task.setStatus(Status.CANCELLED);
        } catch (Exception e) {
            System.out.println("task excepted");
            task.setStatus(Status.ERROR);
        }
    }
}
