package java.util;

public abstract interface SortedSet<E> extends Set<E>
{
  public abstract Comparator<? super E> comparator();

  public abstract SortedSet<E> subSet(E paramE1, E paramE2);

  public abstract SortedSet<E> headSet(E paramE);

  public abstract SortedSet<E> tailSet(E paramE);

  public abstract E first();

  public abstract E last();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.SortedSet
 * JD-Core Version:    0.6.2
 */