package javax.swing.undo;

public abstract interface UndoableEdit
{
  public abstract void undo()
    throws CannotUndoException;

  public abstract boolean canUndo();

  public abstract void redo()
    throws CannotRedoException;

  public abstract boolean canRedo();

  public abstract void die();

  public abstract boolean addEdit(UndoableEdit paramUndoableEdit);

  public abstract boolean replaceEdit(UndoableEdit paramUndoableEdit);

  public abstract boolean isSignificant();

  public abstract String getPresentationName();

  public abstract String getUndoPresentationName();

  public abstract String getRedoPresentationName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.undo.UndoableEdit
 * JD-Core Version:    0.6.2
 */