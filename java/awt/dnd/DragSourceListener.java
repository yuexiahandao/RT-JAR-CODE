package java.awt.dnd;

import java.util.EventListener;

public abstract interface DragSourceListener extends EventListener
{
  public abstract void dragEnter(DragSourceDragEvent paramDragSourceDragEvent);

  public abstract void dragOver(DragSourceDragEvent paramDragSourceDragEvent);

  public abstract void dropActionChanged(DragSourceDragEvent paramDragSourceDragEvent);

  public abstract void dragExit(DragSourceEvent paramDragSourceEvent);

  public abstract void dragDropEnd(DragSourceDropEvent paramDragSourceDropEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.dnd.DragSourceListener
 * JD-Core Version:    0.6.2
 */