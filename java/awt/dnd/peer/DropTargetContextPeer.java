package java.awt.dnd.peer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.InvalidDnDOperationException;

public abstract interface DropTargetContextPeer
{
  public abstract void setTargetActions(int paramInt);

  public abstract int getTargetActions();

  public abstract DropTarget getDropTarget();

  public abstract DataFlavor[] getTransferDataFlavors();

  public abstract Transferable getTransferable()
    throws InvalidDnDOperationException;

  public abstract boolean isTransferableJVMLocal();

  public abstract void acceptDrag(int paramInt);

  public abstract void rejectDrag();

  public abstract void acceptDrop(int paramInt);

  public abstract void rejectDrop();

  public abstract void dropComplete(boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.dnd.peer.DropTargetContextPeer
 * JD-Core Version:    0.6.2
 */