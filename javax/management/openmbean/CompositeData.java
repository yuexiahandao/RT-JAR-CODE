package javax.management.openmbean;

import java.util.Collection;

public abstract interface CompositeData
{
  public abstract CompositeType getCompositeType();

  public abstract Object get(String paramString);

  public abstract Object[] getAll(String[] paramArrayOfString);

  public abstract boolean containsKey(String paramString);

  public abstract boolean containsValue(Object paramObject);

  public abstract Collection<?> values();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();

  public abstract String toString();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.openmbean.CompositeData
 * JD-Core Version:    0.6.2
 */