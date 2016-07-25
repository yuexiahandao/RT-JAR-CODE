package com.sun.beans.decoder;

public class StringElementHandler extends ElementHandler {
    private StringBuilder sb = new StringBuilder();
    private ValueObject value = ValueObjectImpl.NULL;

    public final void addCharacter(char paramChar) {
        if (this.sb == null) {
            throw new IllegalStateException("Could not add chararcter to evaluated string element");
        }
        this.sb.append(paramChar);
    }

    protected final void addArgument(Object paramObject) {
        if (this.sb == null) {
            throw new IllegalStateException("Could not add argument to evaluated string element");
        }
        this.sb.append(paramObject);
    }

    protected final ValueObject getValueObject() {
        if (this.sb != null) {
            try {
                this.value = ValueObjectImpl.create(getValue(this.sb.toString()));
            } catch (RuntimeException localRuntimeException) {
                getOwner().handleException(localRuntimeException);
            } finally {
                this.sb = null;
            }
        }
        return this.value;
    }

    protected Object getValue(String paramString) {
        return paramString;
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.StringElementHandler
 * JD-Core Version:    0.6.2
 */