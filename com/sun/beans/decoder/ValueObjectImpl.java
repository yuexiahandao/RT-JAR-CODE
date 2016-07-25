package com.sun.beans.decoder;

final class ValueObjectImpl
        implements ValueObject {
    static final ValueObject NULL = new ValueObjectImpl(null);
    static final ValueObject VOID = new ValueObjectImpl();
    private Object value;
    private boolean isVoid;

    static ValueObject create(Object paramObject) {
        return paramObject != null ? new ValueObjectImpl(paramObject) : NULL;
    }

    private ValueObjectImpl() {
        this.isVoid = true;
    }

    private ValueObjectImpl(Object paramObject) {
        this.value = paramObject;
    }

    public Object getValue() {
        return this.value;
    }

    public boolean isVoid() {
        return this.isVoid;
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.ValueObjectImpl
 * JD-Core Version:    0.6.2
 */