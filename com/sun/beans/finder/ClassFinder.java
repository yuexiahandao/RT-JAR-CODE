package com.sun.beans.finder;

import sun.reflect.misc.ReflectUtil;

public final class ClassFinder {
    public static Class<?> findClass(String paramString)
            throws ClassNotFoundException {
        ReflectUtil.checkPackageAccess(paramString);
        try {
            ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
            if (localClassLoader == null) {
                localClassLoader = ClassLoader.getSystemClassLoader();
            }
            if (localClassLoader != null)
                return Class.forName(paramString, false, localClassLoader);
        } catch (ClassNotFoundException localClassNotFoundException) {
        } catch (SecurityException localSecurityException) {
        }
        return Class.forName(paramString);
    }

    public static Class<?> findClass(String paramString, ClassLoader paramClassLoader)
            throws ClassNotFoundException {
        ReflectUtil.checkPackageAccess(paramString);
        if (paramClassLoader != null)
            try {
                return Class.forName(paramString, false, paramClassLoader);
            } catch (ClassNotFoundException localClassNotFoundException) {
            } catch (SecurityException localSecurityException) {
            }
        return findClass(paramString);
    }

    public static Class<?> resolveClass(String paramString)
            throws ClassNotFoundException {
        return resolveClass(paramString, null);
    }

    public static Class<?> resolveClass(String paramString, ClassLoader paramClassLoader)
            throws ClassNotFoundException {
        Class localClass = PrimitiveTypeMap.getType(paramString);
        return localClass == null ? findClass(paramString, paramClassLoader) : localClass;
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.ClassFinder
 * JD-Core Version:    0.6.2
 */