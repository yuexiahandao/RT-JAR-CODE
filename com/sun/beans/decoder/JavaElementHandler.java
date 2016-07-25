package com.sun.beans.decoder;

import java.beans.XMLDecoder;

final class JavaElementHandler extends ElementHandler {
    private Class<?> type;
    private ValueObject value;

    public void addAttribute(String paramString1, String paramString2) {
        if (!paramString1.equals("version")) {
            if (paramString1.equals("class")) {
                this.type = getOwner().findClass(paramString2);
            } else super.addAttribute(paramString1, paramString2);
        }
    }

    protected void addArgument(Object paramObject) {
        getOwner().addObject(paramObject);
    }

    protected boolean isArgument() {
        return false;
    }

    protected ValueObject getValueObject() {
        if (this.value == null) {
            this.value = ValueObjectImpl.create(getValue());
        }
        return this.value;
    }

    private Object getValue() {
        Object localObject = getOwner().getOwner();
        if ((this.type == null) || (isValid(localObject))) {
            return localObject;
        }
        if ((localObject instanceof XMLDecoder)) {
            XMLDecoder localXMLDecoder = (XMLDecoder) localObject;
            localObject = localXMLDecoder.getOwner();
            if (isValid(localObject)) {
                return localObject;
            }
        }
        throw new IllegalStateException("Unexpected owner class: " + localObject.getClass().getName());
    }

    private boolean isValid(Object paramObject) {
        return (paramObject == null) || (this.type.isInstance(paramObject));
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.JavaElementHandler
 * JD-Core Version:    0.6.2
 */