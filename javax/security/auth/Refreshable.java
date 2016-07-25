package javax.security.auth;

public abstract interface Refreshable
{
  public abstract boolean isCurrent();

  public abstract void refresh()
    throws RefreshFailedException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.Refreshable
 * JD-Core Version:    0.6.2
 */