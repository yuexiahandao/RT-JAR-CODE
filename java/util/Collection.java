package java.util;

public abstract interface Collection<E> extends Iterable<E>
{
  public abstract int size();

  public abstract boolean isEmpty();

  public abstract boolean contains(Object paramObject);

  public abstract Iterator<E> iterator();

  public abstract Object[] toArray();

  public abstract <T> T[] toArray(T[] paramArrayOfT);

  public abstract boolean add(E paramE);

  public abstract boolean remove(Object paramObject);

  public abstract boolean containsAll(Collection<?> paramCollection);

  public abstract boolean addAll(Collection<? extends E> paramCollection);

  public abstract boolean removeAll(Collection<?> paramCollection);

  public abstract boolean retainAll(Collection<?> paramCollection);

  public abstract void clear();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Collection
 * JD-Core Version:    0.6.2
 */