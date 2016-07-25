package com.sun.beans.editors;

public class ShortEditor extends NumberEditor {
    public String getJavaInitializationString() {
        Object localObject = getValue();
        return localObject != null ? "((short)" + localObject + ")" : "null";
    }

    public void setAsText(String paramString)
            throws IllegalArgumentException {
        setValue(paramString == null ? null : Short.decode(paramString));
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.editors.ShortEditor
 * JD-Core Version:    0.6.2
 */