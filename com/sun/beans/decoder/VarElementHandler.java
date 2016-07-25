package com.sun.beans.decoder;

final class VarElementHandler extends ElementHandler {
    private ValueObject value;

    public void addAttribute(String paramString1, String paramString2) {
        if (paramString1.equals("idref"))
            this.value = ValueObjectImpl.create(getVariable(paramString2));
        else
            super.addAttribute(paramString1, paramString2);
    }

    protected ValueObject getValueObject() {
        if (this.value == null) {
            throw new IllegalArgumentException("Variable name is not set");
        }
        return this.value;
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.VarElementHandler
 * JD-Core Version:    0.6.2
 */