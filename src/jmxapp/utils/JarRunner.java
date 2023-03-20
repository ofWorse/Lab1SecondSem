package jmxapp.utils;


import jmxapp.enums.Status;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarRunner {
    public static void runTask(Task task) throws MalformedURLException {
        var url = new URL("jar:file:" + task.getPathToJar() + "!/");

        try (var loader = new URLClassLoader(new URL[]{url})) {
            var clazz = loader.loadClass(task.getPathToMain());
            var obj = (Runnable) clazz.getConstructor().newInstance();

            if (task.getPeriod() == 0)
                obj.run();
            else while (!Thread.currentThread().isInterrupted()) {
                obj.run();
                Thread.sleep(task.getPeriod() * 1000L);
            }

        } catch (InterruptedException e) {
            task.setStatus(Status.CANCELLED);
        } catch (Exception e) {
            task.setStatus(Status.ERROR);
        }
    }
}
