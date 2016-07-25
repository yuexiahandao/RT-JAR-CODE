package com.sun.beans.decoder;

import com.sun.beans.finder.ConstructorFinder;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

class NewElementHandler extends ElementHandler {
    private List<Object> arguments = new ArrayList();
    private ValueObject value = ValueObjectImpl.VOID;
    private Class<?> type;

    public void addAttribute(String paramString1, String paramString2) {
        if (paramString1.equals("class"))
            this.type = getOwner().findClass(paramString2);
        else
            super.addAttribute(paramString1, paramString2);
    }

    protected final void addArgument(Object paramObject) {
        if (this.arguments == null) {
            throw new IllegalStateException("Could not add argument to evaluated element");
        }
        this.arguments.add(paramObject);
    }

    protected final Object getContextBean() {
        return this.type != null ? this.type : super.getContextBean();
    }

    protected final ValueObject getValueObject() {
        if (this.arguments != null) {
            try {
                this.value = getValueObject(this.type, this.arguments.toArray());
            } catch (Exception localException) {
                getOwner().handleException(localException);
            } finally {
                this.arguments = null;
            }
        }
        return this.value;
    }

    ValueObject getValueObject(Class<?> paramClass, Object[] paramArrayOfObject)
            throws Exception {
        if (paramClass == null) {
            throw new IllegalArgumentException("Class name is not set");
        }
        Class[] arrayOfClass = getArgumentTypes(paramArrayOfObject);
        Constructor localConstructor = ConstructorFinder.findConstructor(paramClass, arrayOfClass);
        if (localConstructor.isVarArgs()) {
            paramArrayOfObject = getArguments(paramArrayOfObject, localConstructor.getParameterTypes());
        }
        return ValueObjectImpl.create(localConstructor.newInstance(paramArrayOfObject));
    }

    static Class<?>[] getArgumentTypes(Object[] paramArrayOfObject) {
        Class[] arrayOfClass = new Class[paramArrayOfObject.length];
        for (int i = 0; i < paramArrayOfObject.length; i++) {
            if (paramArrayOfObject[i] != null) {
                arrayOfClass[i] = paramArrayOfObject[i].getClass();
            }
        }
        return arrayOfClass;
    }

    static Object[] getArguments(Object[] paramArrayOfObject, Class<?>[] paramArrayOfClass) {
        int i = paramArrayOfClass.length - 1;
        if (paramArrayOfClass.length == paramArrayOfObject.length) {
            Object localObject1 = paramArrayOfObject[i];
            if (localObject1 == null) {
                return paramArrayOfObject;
            }
            localObject2 = paramArrayOfClass[i];
            if (((Class) localObject2).isAssignableFrom(localObject1.getClass())) {
                return paramArrayOfObject;
            }
        }
        int j = paramArrayOfObject.length - i;
        Object localObject2 = paramArrayOfClass[i].getComponentType();
        Object localObject3 = Array.newInstance((Class) localObject2, j);
        System.arraycopy(paramArrayOfObject, i, localObject3, 0, j);

        Object[] arrayOfObject = new Object[paramArrayOfClass.length];
        System.arraycopy(paramArrayOfObject, 0, arrayOfObject, 0, i);
        arrayOfObject[i] = localObject3;
        return arrayOfObject;
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.NewElementHandler
 * JD-Core Version:    0.6.2
 */