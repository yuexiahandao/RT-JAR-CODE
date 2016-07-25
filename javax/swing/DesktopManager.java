package javax.swing;

public abstract interface DesktopManager
{
  public abstract void openFrame(JInternalFrame paramJInternalFrame);

  public abstract void closeFrame(JInternalFrame paramJInternalFrame);

  public abstract void maximizeFrame(JInternalFrame paramJInternalFrame);

  public abstract void minimizeFrame(JInternalFrame paramJInternalFrame);

  public abstract void iconifyFrame(JInternalFrame paramJInternalFrame);

  public abstract void deiconifyFrame(JInternalFrame paramJInternalFrame);

  public abstract void activateFrame(JInternalFrame paramJInternalFrame);

  public abstract void deactivateFrame(JInternalFrame paramJInternalFrame);

  public abstract void beginDraggingFrame(JComponent paramJComponent);

  public abstract void dragFrame(JComponent paramJComponent, int paramInt1, int paramInt2);

  public abstract void endDraggingFrame(JComponent paramJComponent);

  public abstract void beginResizingFrame(JComponent paramJComponent, int paramInt);

  public abstract void resizeFrame(JComponent paramJComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

  public abstract void endResizingFrame(JComponent paramJComponent);

  public abstract void setBoundsForFrame(JComponent paramJComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.DesktopManager
 * JD-Core Version:    0.6.2
 */