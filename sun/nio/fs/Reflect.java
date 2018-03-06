package sun.nio.fs;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

class Reflect {
    /**
     * 高权限运行
     * @param paramAccessibleObject
     */
    private static void setAccessible(AccessibleObject paramAccessibleObject) {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                this.val$ao.setAccessible(true);
                return null;
            }
        });
    }

    /**
     * 通过className和fieldName找到对应的field
     * @param className
     * @param fieldName
     * @return
     */
    static Field lookupField(String className, String fieldName) {
        try {
            Class localClass = Class.forName(className);
            Field localField = localClass.getDeclaredField(fieldName);
            setAccessible(localField);
            return localField;
        } catch (ClassNotFoundException localClassNotFoundException) {
            throw new AssertionError(localClassNotFoundException);
        } catch (NoSuchFieldException localNoSuchFieldException) {
            throw new AssertionError(localNoSuchFieldException);
        }
    }
}
