package jmxapp;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class Main {
    public static void main(String[] args) throws Exception {

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("jmxapp:type=RemoteApplication");
        var mBean = new RemoteApplication();
        mbs.registerMBean(mBean, name);

        System.out.println("приложение JMX запущено...");
        ProfilingTasks.s = "after main";
        Thread.sleep(Long.MAX_VALUE);
    }
}
