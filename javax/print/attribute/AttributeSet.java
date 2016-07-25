package javax.print.attribute;

public abstract interface AttributeSet
{
  public abstract Attribute get(Class<?> paramClass);

  public abstract boolean add(Attribute paramAttribute);

  public abstract boolean remove(Class<?> paramClass);

  public abstract boolean remove(Attribute paramAttribute);

  public abstract boolean containsKey(Class<?> paramClass);

  public abstract boolean containsValue(Attribute paramAttribute);

  public abstract boolean addAll(AttributeSet paramAttributeSet);

  public abstract int size();

  public abstract Attribute[] toArray();

  public abstract void clear();

  public abstract boolean isEmpty();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.AttributeSet
 * JD-Core Version:    0.6.2
 */