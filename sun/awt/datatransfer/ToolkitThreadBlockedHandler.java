package sun.awt.datatransfer;

public abstract interface ToolkitThreadBlockedHandler
{
  public abstract void lock();

  public abstract void unlock();

  public abstract void enter();

  public abstract void exit();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.datatransfer.ToolkitThreadBlockedHandler
 * JD-Core Version:    0.6.2
 */