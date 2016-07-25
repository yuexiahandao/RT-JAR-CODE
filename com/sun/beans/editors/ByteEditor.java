package com.sun.beans.editors;

public class ByteEditor extends NumberEditor {
    public String getJavaInitializationString() {
        Object localObject = getValue();
        return localObject != null ? "((byte)" + localObject + ")" : "null";
    }

    public void setAsText(String paramString)
            throws IllegalArgumentException {
        setValue(paramString == null ? null : Byte.decode(paramString));
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.editors.ByteEditor
 * JD-Core Version:    0.6.2
 */