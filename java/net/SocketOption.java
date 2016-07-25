package java.net;

public abstract interface SocketOption<T>
{
  public abstract String name();

  public abstract Class<T> type();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.SocketOption
 * JD-Core Version:    0.6.2
 */