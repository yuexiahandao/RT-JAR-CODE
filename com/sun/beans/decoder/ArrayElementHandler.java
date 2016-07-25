package com.sun.beans.decoder;

import java.lang.reflect.Array;

final class ArrayElementHandler extends NewElementHandler {
    private Integer length;

    public void addAttribute(String paramString1, String paramString2) {
        if (paramString1.equals("length"))
            this.length = Integer.valueOf(paramString2);
        else
            super.addAttribute(paramString1, paramString2);
    }

    public void startElement() {
        if (this.length != null)
            getValueObject();
    }

    protected boolean isArgument() {
        return true;
    }

    protected ValueObject getValueObject(Class<?> paramClass, Object[] paramArrayOfObject) {
        if (paramClass == null) {
            paramClass = Object.class;
        }
        if (this.length != null) {
            return ValueObjectImpl.create(Array.newInstance(paramClass, this.length.intValue()));
        }
        Object localObject = Array.newInstance(paramClass, paramArrayOfObject.length);
        for (int i = 0; i < paramArrayOfObject.length; i++) {
            Array.set(localObject, i, paramArrayOfObject[i]);
        }
        return ValueObjectImpl.create(localObject);
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.ArrayElementHandler
 * JD-Core Version:    0.6.2
 */