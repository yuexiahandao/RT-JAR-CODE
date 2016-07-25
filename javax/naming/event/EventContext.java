package javax.naming.event;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;

public abstract interface EventContext extends Context
{
  public static final int OBJECT_SCOPE = 0;
  public static final int ONELEVEL_SCOPE = 1;
  public static final int SUBTREE_SCOPE = 2;

  public abstract void addNamingListener(Name paramName, int paramInt, NamingListener paramNamingListener)
    throws NamingException;

  public abstract void addNamingListener(String paramString, int paramInt, NamingListener paramNamingListener)
    throws NamingException;

  public abstract void removeNamingListener(NamingListener paramNamingListener)
    throws NamingException;

  public abstract boolean targetMustExist()
    throws NamingException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.event.EventContext
 * JD-Core Version:    0.6.2
 */