package javax.management.openmbean;

import java.util.Collection;
import java.util.Set;

public abstract interface TabularData
{
  public abstract TabularType getTabularType();

  public abstract Object[] calculateIndex(CompositeData paramCompositeData);

  public abstract int size();

  public abstract boolean isEmpty();

  public abstract boolean containsKey(Object[] paramArrayOfObject);

  public abstract boolean containsValue(CompositeData paramCompositeData);

  public abstract CompositeData get(Object[] paramArrayOfObject);

  public abstract void put(CompositeData paramCompositeData);

  public abstract CompositeData remove(Object[] paramArrayOfObject);

  public abstract void putAll(CompositeData[] paramArrayOfCompositeData);

  public abstract void clear();

  public abstract Set<?> keySet();

  public abstract Collection<?> values();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();

  public abstract String toString();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.openmbean.TabularData
 * JD-Core Version:    0.6.2
 */