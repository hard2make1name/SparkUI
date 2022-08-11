
package none.spark.injection.handler;

import none.spark.injection.remapper.Remapper;
import org.objectweb.asm.Type;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

public class AbstractJavaLinkerHandler {

    public static String addMember(Class<?> clazz, String name, AccessibleObject accessibleObject) {
        if (!(accessibleObject instanceof Method)) {
            return name;
        }

        Class<?> currentClass = clazz;
        while (!"java.lang.Object".equals(currentClass.getName())) {
            String remapped = Remapper.remapMethod(currentClass, name, Type.getMethodDescriptor((Method) accessibleObject));

            if (!name.equals(remapped)) {
                System.out.print(remapped+"\n");
                return remapped;
            }

            if (currentClass.getSuperclass() == null) {
                break;
            }

            currentClass = currentClass.getSuperclass();
        }
        System.out.print(name+"\n");
        return name;
    }

    public static String addMember(Class<?> clazz, String name) {

        Class<?> currentClass = clazz;
        while (!"java.lang.Object".equals(currentClass.getName())) {
            String remapped = Remapper.remapField(currentClass, name);

            if (!name.equals(remapped)) {
                System.out.print(remapped+"\n");
                return remapped;
            }

            if (currentClass.getSuperclass() == null) {
                break;
            }

            currentClass = currentClass.getSuperclass();
        }
        System.out.print(name+"\n");
        return name;
    }

    public static String setPropertyGetter(Class<?> clazz, String name) {

        Class<?> currentClass = clazz;
        while (!"java.lang.Object".equals(currentClass.getName())) {
            String remapped = Remapper.remapField(currentClass, name);

            if (!name.equals(remapped)) {
                System.out.print(remapped+"\n");
                return remapped;
            }

            if (currentClass.getSuperclass() == null) {
                break;
            }

            currentClass = currentClass.getSuperclass();
        }
        System.out.print(name+"\n");
        return name;
    }
}