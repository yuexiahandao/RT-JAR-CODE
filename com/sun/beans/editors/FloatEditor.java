package com.sun.beans.editors;

public class FloatEditor extends NumberEditor {
    public String getJavaInitializationString() {
        Object localObject = getValue();
        return localObject != null ? localObject + "F" : "null";
    }

    public void setAsText(String paramString)
            throws IllegalArgumentException {
        setValue(paramString == null ? null : Float.valueOf(paramString));
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.editors.FloatEditor
 * JD-Core Version:    0.6.2
 */