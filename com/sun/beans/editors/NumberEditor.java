package com.sun.beans.editors;

import java.beans.PropertyEditorSupport;

public abstract class NumberEditor extends PropertyEditorSupport {
    public String getJavaInitializationString() {
        Object localObject = getValue();
        return localObject != null ? localObject.toString() : "null";
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.editors.NumberEditor
 * JD-Core Version:    0.6.2
 */