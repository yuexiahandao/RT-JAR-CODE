package sun.misc;

public abstract interface JavaAWTAccess
{
  public abstract Object getAppletContext();

  public abstract Object get(Object paramObject);

  public abstract void put(Object paramObject1, Object paramObject2);

  public abstract void remove(Object paramObject);

  public abstract boolean isDisposed();

  public abstract boolean isMainAppContext();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.JavaAWTAccess
 * JD-Core Version:    0.6.2
 */