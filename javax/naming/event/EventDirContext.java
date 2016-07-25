package javax.naming.event;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;

public abstract interface EventDirContext extends EventContext, DirContext
{
  public abstract void addNamingListener(Name paramName, String paramString, SearchControls paramSearchControls, NamingListener paramNamingListener)
    throws NamingException;

  public abstract void addNamingListener(String paramString1, String paramString2, SearchControls paramSearchControls, NamingListener paramNamingListener)
    throws NamingException;

  public abstract void addNamingListener(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls, NamingListener paramNamingListener)
    throws NamingException;

  public abstract void addNamingListener(String paramString1, String paramString2, Object[] paramArrayOfObject, SearchControls paramSearchControls, NamingListener paramNamingListener)
    throws NamingException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.event.EventDirContext
 * JD-Core Version:    0.6.2
 */