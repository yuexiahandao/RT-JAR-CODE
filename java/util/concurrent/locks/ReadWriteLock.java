package java.util.concurrent.locks;

public abstract interface ReadWriteLock
{
  public abstract Lock readLock();

  public abstract Lock writeLock();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.locks.ReadWriteLock
 * JD-Core Version:    0.6.2
 */