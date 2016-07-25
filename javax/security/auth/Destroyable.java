package javax.security.auth;

public abstract interface Destroyable
{
  public abstract void destroy()
    throws DestroyFailedException;

  public abstract boolean isDestroyed();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.Destroyable
 * JD-Core Version:    0.6.2
 */