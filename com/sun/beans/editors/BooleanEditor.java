package com.sun.beans.editors;

import java.beans.PropertyEditorSupport;

public class BooleanEditor extends PropertyEditorSupport {
    public String getJavaInitializationString() {
        Object localObject = getValue();
        return localObject != null ? localObject.toString() : "null";
    }

    public String getAsText() {
        Object localObject = getValue();
        return (localObject instanceof Boolean) ? getValidName(((Boolean) localObject).booleanValue()) : null;
    }

    public void setAsText(String paramString)
            throws IllegalArgumentException {
        if (paramString == null)
            setValue(null);
        else if (isValidName(true, paramString))
            setValue(Boolean.TRUE);
        else if (isValidName(false, paramString))
            setValue(Boolean.FALSE);
        else
            throw new IllegalArgumentException(paramString);
    }

    public String[] getTags() {
        return new String[]{getValidName(true), getValidName(false)};
    }

    private String getValidName(boolean paramBoolean) {
        return paramBoolean ? "True" : "False";
    }

    private boolean isValidName(boolean paramBoolean, String paramString) {
        return getValidName(paramBoolean).equalsIgnoreCase(paramString);
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.editors.BooleanEditor
 * JD-Core Version:    0.6.2
 */