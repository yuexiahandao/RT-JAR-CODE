package java.util;

public abstract interface Iterator<E>
{
  public abstract boolean hasNext();

  public abstract E next();

  public abstract void remove();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Iterator
 * JD-Core Version:    0.6.2
 */