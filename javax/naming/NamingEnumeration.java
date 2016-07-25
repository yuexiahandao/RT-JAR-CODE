package javax.naming;

import java.util.Enumeration;

public abstract interface NamingEnumeration<T> extends Enumeration<T>
{
  public abstract T next()
    throws NamingException;

  public abstract boolean hasMore()
    throws NamingException;

  public abstract void close()
    throws NamingException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.NamingEnumeration
 * JD-Core Version:    0.6.2
 */