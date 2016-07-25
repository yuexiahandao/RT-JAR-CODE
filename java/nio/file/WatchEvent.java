package java.nio.file;

public abstract interface WatchEvent<T>
{
  public abstract Kind<T> kind();

  public abstract int count();

  public abstract T context();

  public static abstract interface Kind<T>
  {
    public abstract String name();

    public abstract Class<T> type();
  }

  public static abstract interface Modifier
  {
    public abstract String name();
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.WatchEvent
 * JD-Core Version:    0.6.2
 */