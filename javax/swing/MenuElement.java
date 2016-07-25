package javax.swing;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract interface MenuElement
{
  public abstract void processMouseEvent(MouseEvent paramMouseEvent, MenuElement[] paramArrayOfMenuElement, MenuSelectionManager paramMenuSelectionManager);

  public abstract void processKeyEvent(KeyEvent paramKeyEvent, MenuElement[] paramArrayOfMenuElement, MenuSelectionManager paramMenuSelectionManager);

  public abstract void menuSelectionChanged(boolean paramBoolean);

  public abstract MenuElement[] getSubElements();

  public abstract Component getComponent();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.MenuElement
 * JD-Core Version:    0.6.2
 */