package com.sun.beans.editors;

public class DoubleEditor extends NumberEditor {
    public void setAsText(String paramString)
            throws IllegalArgumentException {
        setValue(paramString == null ? null : Double.valueOf(paramString));
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.editors.DoubleEditor
 * JD-Core Version:    0.6.2
 */