package java.util;

public abstract interface ListIterator<E> extends Iterator<E>
{
  public abstract boolean hasNext();

  public abstract E next();

  public abstract boolean hasPrevious();

  public abstract E previous();

  public abstract int nextIndex();

  public abstract int previousIndex();

  public abstract void remove();

  public abstract void set(E paramE);

  public abstract void add(E paramE);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.ListIterator
 * JD-Core Version:    0.6.2
 */