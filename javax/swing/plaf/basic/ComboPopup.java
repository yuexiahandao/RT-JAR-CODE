package javax.swing.plaf.basic;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JList;

public abstract interface ComboPopup
{
  public abstract void show();

  public abstract void hide();

  public abstract boolean isVisible();

  public abstract JList getList();

  public abstract MouseListener getMouseListener();

  public abstract MouseMotionListener getMouseMotionListener();

  public abstract KeyListener getKeyListener();

  public abstract void uninstallingUI();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.ComboPopup
 * JD-Core Version:    0.6.2
 */