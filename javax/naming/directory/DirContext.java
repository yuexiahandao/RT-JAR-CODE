package javax.naming.directory;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public abstract interface DirContext extends Context
{
  public static final int ADD_ATTRIBUTE = 1;
  public static final int REPLACE_ATTRIBUTE = 2;
  public static final int REMOVE_ATTRIBUTE = 3;

  public abstract Attributes getAttributes(Name paramName)
    throws NamingException;

  public abstract Attributes getAttributes(String paramString)
    throws NamingException;

  public abstract Attributes getAttributes(Name paramName, String[] paramArrayOfString)
    throws NamingException;

  public abstract Attributes getAttributes(String paramString, String[] paramArrayOfString)
    throws NamingException;

  public abstract void modifyAttributes(Name paramName, int paramInt, Attributes paramAttributes)
    throws NamingException;

  public abstract void modifyAttributes(String paramString, int paramInt, Attributes paramAttributes)
    throws NamingException;

  public abstract void modifyAttributes(Name paramName, ModificationItem[] paramArrayOfModificationItem)
    throws NamingException;

  public abstract void modifyAttributes(String paramString, ModificationItem[] paramArrayOfModificationItem)
    throws NamingException;

  public abstract void bind(Name paramName, Object paramObject, Attributes paramAttributes)
    throws NamingException;

  public abstract void bind(String paramString, Object paramObject, Attributes paramAttributes)
    throws NamingException;

  public abstract void rebind(Name paramName, Object paramObject, Attributes paramAttributes)
    throws NamingException;

  public abstract void rebind(String paramString, Object paramObject, Attributes paramAttributes)
    throws NamingException;

  public abstract DirContext createSubcontext(Name paramName, Attributes paramAttributes)
    throws NamingException;

  public abstract DirContext createSubcontext(String paramString, Attributes paramAttributes)
    throws NamingException;

  public abstract DirContext getSchema(Name paramName)
    throws NamingException;

  public abstract DirContext getSchema(String paramString)
    throws NamingException;

  public abstract DirContext getSchemaClassDefinition(Name paramName)
    throws NamingException;

  public abstract DirContext getSchemaClassDefinition(String paramString)
    throws NamingException;

  public abstract NamingEnumeration<SearchResult> search(Name paramName, Attributes paramAttributes, String[] paramArrayOfString)
    throws NamingException;

  public abstract NamingEnumeration<SearchResult> search(String paramString, Attributes paramAttributes, String[] paramArrayOfString)
    throws NamingException;

  public abstract NamingEnumeration<SearchResult> search(Name paramName, Attributes paramAttributes)
    throws NamingException;

  public abstract NamingEnumeration<SearchResult> search(String paramString, Attributes paramAttributes)
    throws NamingException;

  public abstract NamingEnumeration<SearchResult> search(Name paramName, String paramString, SearchControls paramSearchControls)
    throws NamingException;

  public abstract NamingEnumeration<SearchResult> search(String paramString1, String paramString2, SearchControls paramSearchControls)
    throws NamingException;

  public abstract NamingEnumeration<SearchResult> search(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls)
    throws NamingException;

  public abstract NamingEnumeration<SearchResult> search(String paramString1, String paramString2, Object[] paramArrayOfObject, SearchControls paramSearchControls)
    throws NamingException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.directory.DirContext
 * JD-Core Version:    0.6.2
 */