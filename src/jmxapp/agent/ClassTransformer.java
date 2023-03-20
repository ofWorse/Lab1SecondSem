package jmxapp.agent;

import javassist.*;
import jmxapp.ListOfInstrumentedTasks;

import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class ClassTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader classLoader,
                            String classname,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classFileBuffer) {
        try {
            try {
                if (ListOfInstrumentedTasks.tasks
                        .contains(classname.replaceAll("/", "."))) {
                    System.out.println("in instrumentation of " + classname);
                    ClassPool pool = ClassPool.getDefault();
                    CtClass clazz = pool.get(classname.replaceAll("/", "."));

                    var method = clazz.getDeclaredMethod("run");
                    method.insertBefore("double start = System.currentTimeMillis();");
                    method.insertAfter("double end = System.currentTimeMillis();");
                    method.insertAfter("System.out.println(\"(\" + (start - end) \")\");");

                    return clazz.toBytecode();
                } else return classFileBuffer;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Throwable e) {
            System.out.println("Error: " + e.getMessage());
            try (var writer = new PrintWriter(System.out)) {
                e.printStackTrace(writer);
            }
        }
        return classFileBuffer;
    }
}
