package javax.swing.event;

import java.util.EventListener;

public abstract interface CellEditorListener extends EventListener
{
  public abstract void editingStopped(ChangeEvent paramChangeEvent);

  public abstract void editingCanceled(ChangeEvent paramChangeEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.CellEditorListener
 * JD-Core Version:    0.6.2
 */