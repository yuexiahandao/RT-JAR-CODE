package com.sun.beans.finder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import sun.reflect.misc.ReflectUtil;

public final class FieldFinder {
    public static Field findField(Class<?> paramClass, String paramString)
            throws NoSuchFieldException {
        if (paramString == null) {
            throw new IllegalArgumentException("Field name is not set");
        }
        Field localField = paramClass.getField(paramString);
        if (!Modifier.isPublic(localField.getModifiers())) {
            throw new NoSuchFieldException("Field '" + paramString + "' is not public");
        }
        paramClass = localField.getDeclaringClass();
        if ((!Modifier.isPublic(paramClass.getModifiers())) || (!ReflectUtil.isPackageAccessible(paramClass))) {
            throw new NoSuchFieldException("Field '" + paramString + "' is not accessible");
        }
        return localField;
    }

    public static Field findInstanceField(Class<?> paramClass, String paramString)
            throws NoSuchFieldException {
        Field localField = findField(paramClass, paramString);
        if (Modifier.isStatic(localField.getModifiers())) {
            throw new NoSuchFieldException("Field '" + paramString + "' is static");
        }
        return localField;
    }

    public static Field findStaticField(Class<?> paramClass, String paramString)
            throws NoSuchFieldException {
        Field localField = findField(paramClass, paramString);
        if (!Modifier.isStatic(localField.getModifiers())) {
            throw new NoSuchFieldException("Field '" + paramString + "' is not static");
        }
        return localField;
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.FieldFinder
 * JD-Core Version:    0.6.2
 */