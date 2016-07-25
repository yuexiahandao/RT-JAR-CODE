package java.security;

public abstract interface Guard
{
  public abstract void checkGuard(Object paramObject)
    throws SecurityException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.Guard
 * JD-Core Version:    0.6.2
 */