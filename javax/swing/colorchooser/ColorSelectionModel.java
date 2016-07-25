package javax.swing.colorchooser;

import java.awt.Color;
import javax.swing.event.ChangeListener;

public abstract interface ColorSelectionModel
{
  public abstract Color getSelectedColor();

  public abstract void setSelectedColor(Color paramColor);

  public abstract void addChangeListener(ChangeListener paramChangeListener);

  public abstract void removeChangeListener(ChangeListener paramChangeListener);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.ColorSelectionModel
 * JD-Core Version:    0.6.2
 */