package javax.swing.event;

import java.util.EventListener;

public abstract interface UndoableEditListener extends EventListener
{
  public abstract void undoableEditHappened(UndoableEditEvent paramUndoableEditEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.UndoableEditListener
 * JD-Core Version:    0.6.2
 */