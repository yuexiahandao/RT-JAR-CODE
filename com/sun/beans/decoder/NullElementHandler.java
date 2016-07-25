package com.sun.beans.decoder;

class NullElementHandler extends ElementHandler
        implements ValueObject {
    protected final ValueObject getValueObject() {
        return this;
    }

    public Object getValue() {
        return null;
    }

    public final boolean isVoid() {
        return false;
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.NullElementHandler
 * JD-Core Version:    0.6.2
 */