package com.sun.beans.editors;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.List;

public class EnumEditor
        implements PropertyEditor {
    private final List<PropertyChangeListener> listeners = new ArrayList();
    private final Class type;
    private final String[] tags;
    private Object value;

    public EnumEditor(Class paramClass) {
        Object[] arrayOfObject = paramClass.getEnumConstants();
        if (arrayOfObject == null) {
            throw new IllegalArgumentException("Unsupported " + paramClass);
        }
        this.type = paramClass;
        this.tags = new String[arrayOfObject.length];
        for (int i = 0; i < arrayOfObject.length; i++)
            this.tags[i] = ((Enum) arrayOfObject[i]).name();
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object paramObject) {
        if ((paramObject != null) && (!this.type.isInstance(paramObject)))
            throw new IllegalArgumentException("Unsupported value: " + paramObject);
        Object localObject1;
        PropertyChangeListener[] arrayOfPropertyChangeListener;
        synchronized (this.listeners) {
            localObject1 = this.value;
            this.value = paramObject;

            if (paramObject == null ? localObject1 == null : paramObject.equals(localObject1)) {
                return;
            }
            int i = this.listeners.size();
            if (i == 0) {
                return;
            }
            arrayOfPropertyChangeListener = (PropertyChangeListener[]) this.listeners.toArray(new PropertyChangeListener[i]);
        }
        ???=new PropertyChangeEvent(this, null, localObject1, paramObject);
        for (Object localObject4 : arrayOfPropertyChangeListener)
            localObject4.propertyChange((PropertyChangeEvent) ? ??);
    }

    public String getAsText() {
        return this.value != null ? ((Enum) this.value).name() : null;
    }

    public void setAsText(String paramString) {
        setValue(paramString != null ? Enum.valueOf(this.type, paramString) : null);
    }

    public String[] getTags() {
        return (String[]) this.tags.clone();
    }

    public String getJavaInitializationString() {
        String str = getAsText();
        return str != null ? this.type.getName() + '.' + str : "null";
    }

    public boolean isPaintable() {
        return false;
    }

    public void paintValue(Graphics paramGraphics, Rectangle paramRectangle) {
    }

    public boolean supportsCustomEditor() {
        return false;
    }

    public Component getCustomEditor() {
        return null;
    }

    public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
        synchronized (this.listeners) {
            this.listeners.add(paramPropertyChangeListener);
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
        synchronized (this.listeners) {
            this.listeners.remove(paramPropertyChangeListener);
        }
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.editors.EnumEditor
 * JD-Core Version:    0.6.2
 */