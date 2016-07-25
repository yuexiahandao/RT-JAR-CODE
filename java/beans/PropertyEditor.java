package java.beans;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract interface PropertyEditor
{
  public abstract void setValue(Object paramObject);

  public abstract Object getValue();

  public abstract boolean isPaintable();

  public abstract void paintValue(Graphics paramGraphics, Rectangle paramRectangle);

  public abstract String getJavaInitializationString();

  public abstract String getAsText();

  public abstract void setAsText(String paramString)
    throws IllegalArgumentException;

  public abstract String[] getTags();

  public abstract Component getCustomEditor();

  public abstract boolean supportsCustomEditor();

  public abstract void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);

  public abstract void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.PropertyEditor
 * JD-Core Version:    0.6.2
 */