package javax.naming.directory;

import java.io.Serializable;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public abstract interface Attribute extends Cloneable, Serializable
{
  public static final long serialVersionUID = 8707690322213556804L;

  public abstract NamingEnumeration<?> getAll()
    throws NamingException;

  public abstract Object get()
    throws NamingException;

  public abstract int size();

  public abstract String getID();

  public abstract boolean contains(Object paramObject);

  public abstract boolean add(Object paramObject);

  public abstract boolean remove(Object paramObject);

  public abstract void clear();

  public abstract DirContext getAttributeSyntaxDefinition()
    throws NamingException;

  public abstract DirContext getAttributeDefinition()
    throws NamingException;

  public abstract Object clone();

  public abstract boolean isOrdered();

  public abstract Object get(int paramInt)
    throws NamingException;

  public abstract Object remove(int paramInt);

  public abstract void add(int paramInt, Object paramObject);

  public abstract Object set(int paramInt, Object paramObject);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.directory.Attribute
 * JD-Core Version:    0.6.2
 */