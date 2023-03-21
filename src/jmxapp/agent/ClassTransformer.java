package jmxapp.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.LoaderClassPath;
import jmxapp.ProfilingTasks;

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
            var javaClassname = classname.replaceAll("/", ".");
            var task = ProfilingTasks.tasks.stream()
                    .filter(t -> t.getPathToMain().equals(javaClassname)
                            && t.getClassLoader() == classLoader)
                    .findAny()
                    .orElse(null);
            if (task != null) {
                System.out.println("in instrumentation 9 of " + classname);
                var pool = new ClassPool();
                pool.appendClassPath(new LoaderClassPath(classLoader));
                CtClass clazz = pool.get(javaClassname);

                clazz.addField(CtField.make("long __profile_start = 0L;", clazz));
                clazz.addField(CtField.make("long __profile_end = 0L;", clazz));

                var method = clazz.getDeclaredMethod("call");
                method.insertBefore("__profile_start = System.currentTimeMillis();");
                method.insertAfter("__profile_end = System.currentTimeMillis();");
                method.insertAfter(String.format("""
                        System.out.println("time elapsed for task %s: " + 
                            (__profile_end - __profile_start) + " ms");
                        """, task.getName()));

                return clazz.toBytecode();
            }
        } catch (Throwable e) {
            System.out.println("Instrumentation error: " + e.getMessage());
            try (var writer = new PrintWriter(System.out)) {
                e.printStackTrace(writer);
            }
        }
        return classFileBuffer;
    }
}
