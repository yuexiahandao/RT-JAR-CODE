package com.sun.beans.decoder;

import com.sun.beans.finder.MethodFinder;

import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import sun.reflect.misc.MethodUtil;

final class PropertyElementHandler extends AccessorElementHandler {
    static final String GETTER = "get";
    static final String SETTER = "set";
    private Integer index;

    public void addAttribute(String paramString1, String paramString2) {
        if (paramString1.equals("index"))
            this.index = Integer.valueOf(paramString2);
        else
            super.addAttribute(paramString1, paramString2);
    }

    protected boolean isArgument() {
        return false;
    }

    protected Object getValue(String paramString) {
        try {
            return getPropertyValue(getContextBean(), paramString, this.index);
        } catch (Exception localException) {
            getOwner().handleException(localException);
        }
        return null;
    }

    protected void setValue(String paramString, Object paramObject) {
        try {
            setPropertyValue(getContextBean(), paramString, this.index, paramObject);
        } catch (Exception localException) {
            getOwner().handleException(localException);
        }
    }

    private static Object getPropertyValue(Object paramObject, String paramString, Integer paramInteger)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException, NoSuchMethodException {
        Class localClass = paramObject.getClass();
        if (paramInteger == null)
            return MethodUtil.invoke(findGetter(localClass, paramString, new Class[0]), paramObject, new Object[0]);
        if ((localClass.isArray()) && (paramString == null)) {
            return Array.get(paramObject, paramInteger.intValue());
        }
        return MethodUtil.invoke(findGetter(localClass, paramString, new Class[]{Integer.TYPE}), paramObject, new Object[]{paramInteger});
    }

    private static void setPropertyValue(Object paramObject1, String paramString, Integer paramInteger, Object paramObject2)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException, NoSuchMethodException {
        Class localClass = paramObject1.getClass();
        Object localObject = paramObject2 != null ? paramObject2.getClass() : null;

        if (paramInteger == null)
            MethodUtil.invoke(findSetter(localClass, paramString, new Class[]{localObject}), paramObject1, new Object[]{paramObject2});
        else if ((localClass.isArray()) && (paramString == null))
            Array.set(paramObject1, paramInteger.intValue(), paramObject2);
        else
            MethodUtil.invoke(findSetter(localClass, paramString, new Class[]{Integer.TYPE, localObject}), paramObject1, new Object[]{paramInteger, paramObject2});
    }

    private static Method findGetter(Class<?> paramClass, String paramString, Class<?>[] paramArrayOfClass)
            throws IntrospectionException, NoSuchMethodException {
        if (paramString == null) {
            return MethodFinder.findInstanceMethod(paramClass, "get", paramArrayOfClass);
        }
        PropertyDescriptor localPropertyDescriptor = getProperty(paramClass, paramString);
        Object localObject;
        if (paramArrayOfClass.length == 0) {
            localObject = localPropertyDescriptor.getReadMethod();
            if (localObject != null)
                return localObject;
        } else if ((localPropertyDescriptor instanceof IndexedPropertyDescriptor)) {
            localObject = (IndexedPropertyDescriptor) localPropertyDescriptor;
            Method localMethod = ((IndexedPropertyDescriptor) localObject).getIndexedReadMethod();
            if (localMethod != null) {
                return localMethod;
            }
        }
        throw new IntrospectionException("Could not find getter for the " + paramString + " property");
    }

    private static Method findSetter(Class<?> paramClass, String paramString, Class<?>[] paramArrayOfClass)
            throws IntrospectionException, NoSuchMethodException {
        if (paramString == null) {
            return MethodFinder.findInstanceMethod(paramClass, "set", paramArrayOfClass);
        }
        PropertyDescriptor localPropertyDescriptor = getProperty(paramClass, paramString);
        Object localObject;
        if (paramArrayOfClass.length == 1) {
            localObject = localPropertyDescriptor.getWriteMethod();
            if (localObject != null)
                return localObject;
        } else if ((localPropertyDescriptor instanceof IndexedPropertyDescriptor)) {
            localObject = (IndexedPropertyDescriptor) localPropertyDescriptor;
            Method localMethod = ((IndexedPropertyDescriptor) localObject).getIndexedWriteMethod();
            if (localMethod != null) {
                return localMethod;
            }
        }
        throw new IntrospectionException("Could not find setter for the " + paramString + " property");
    }

    private static PropertyDescriptor getProperty(Class<?> paramClass, String paramString)
            throws IntrospectionException {
        for (PropertyDescriptor localPropertyDescriptor : Introspector.getBeanInfo(paramClass).getPropertyDescriptors()) {
            if (paramString.equals(localPropertyDescriptor.getName())) {
                return localPropertyDescriptor;
            }
        }
        throw new IntrospectionException("Could not find the " + paramString + " property descriptor");
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.PropertyElementHandler
 * JD-Core Version:    0.6.2
 */