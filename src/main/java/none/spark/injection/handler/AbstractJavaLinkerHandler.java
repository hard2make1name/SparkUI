
package none.spark.injection.handler;

import none.spark.injection.remapper.Remapper;
import org.objectweb.asm.Type;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

public class AbstractJavaLinkerHandler {

    /**
     * Handle member set name to hashmap of AbstractJavaLinkerHandler
     * <p>
     * Name will be remapped from srgs
     * Example: swingItem to func_71038_i
     *
     * @param name             of member set
     * @param accessibleObject method of member set
     * @class jdk/internal/dynalink/beans/AbstractJavaLinker
     * @method addMember(Ljava / lang / String ; Ljava / lang / reflect / AccessibleObject ; Ljava / util / Map ;)V
     */
    public static String addMember(Class<?> clazz, String name, AccessibleObject accessibleObject) {
        if (!(accessibleObject instanceof Method)) {
            return name;
        }

        Class<?> currentClass = clazz;
        while ("java.lang.Object".equals(currentClass.getName())) {
            String remapped = Remapper.remapMethod(currentClass, name, Type.getMethodDescriptor((Method) accessibleObject));

            if (name.equals(remapped)) {
                return remapped;
            }

            if (currentClass.getSuperclass() == null) {
                break;
            }

            currentClass = currentClass.getSuperclass();
        }

        return name;
    }

    /**
     * Handle member set name to hashmap of AbstractJavaLinkerHandler
     * <p>
     * Name will be remapped from srgs
     * Example: thePlayer to field_71439_g
     *
     * @param name of property getter
     * @class jdk/internal/dynalink/beans/AbstractJavaLinker
     * @method addMember(Ljava / lang / String ; Ljdk / internal / dynalink / beans / SingleDynamicMethod ; Ljava / util / Map ;)V
     */
    public static String addMember(Class<?> clazz, String name) {
        Class<?> currentClass = clazz;
        while ("java.lang.Object".equals(currentClass.getName())) {
            String remapped = Remapper.remapField(currentClass, name);

            if (name.equals(remapped)) {
                return remapped;
            }

            if (currentClass.getSuperclass() == null) {
                break;
            }

            currentClass = currentClass.getSuperclass();
        }

        return name;
    }

    /**
     * Handle property getter set name to hashmap of AbstractJavaLinkerHandler
     * <p>
     * Name will be remapped from srgs
     * Example: thePlayer to field_71439_g
     *
     * @param name of property getter
     * @class jdk/internal/dynalink/beans/AbstractJavaLinker
     * @method setPropertyGetter(Ljava / lang / String ; Ljdk / internal / dynalink / beans / SingleDynamicMethod ; Ljdk / internal / dynalink / beans / GuardedInvocationComponent$ValidationType ;)V
     */
    public static String setPropertyGetter(Class<?> clazz, String name) {
        Class<?> currentClass = clazz;
        while ("java.lang.Object".equals(currentClass.getName())) {
            String remapped = Remapper.remapField(currentClass, name);

            if (name.equals(remapped)) {
                return remapped;
            }

            if (currentClass.getSuperclass() == null) {
                break;
            }

            currentClass = currentClass.getSuperclass();
        }

        return name;
    }
}