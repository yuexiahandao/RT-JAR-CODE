package java.util;

public abstract interface NavigableSet<E> extends SortedSet<E>
{
  public abstract E lower(E paramE);

  public abstract E floor(E paramE);

  public abstract E ceiling(E paramE);

  public abstract E higher(E paramE);

  public abstract E pollFirst();

  public abstract E pollLast();

  public abstract Iterator<E> iterator();

  public abstract NavigableSet<E> descendingSet();

  public abstract Iterator<E> descendingIterator();

  public abstract NavigableSet<E> subSet(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2);

  public abstract NavigableSet<E> headSet(E paramE, boolean paramBoolean);

  public abstract NavigableSet<E> tailSet(E paramE, boolean paramBoolean);

  public abstract SortedSet<E> subSet(E paramE1, E paramE2);

  public abstract SortedSet<E> headSet(E paramE);

  public abstract SortedSet<E> tailSet(E paramE);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.NavigableSet
 * JD-Core Version:    0.6.2
 */