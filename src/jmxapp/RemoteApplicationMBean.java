package jmxapp;

import java.io.IOException;

public interface RemoteApplicationMBean {
    String submit(String name, String classpath, String mainClass, int period) throws ClassNotFoundException, IOException, InterruptedException;
    String status();
    String cancel(String name) throws InterruptedException;

    void startProfiling(String name);
    void stopProfiling(String name);
}
