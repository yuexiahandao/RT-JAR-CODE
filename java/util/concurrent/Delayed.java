package java.util.concurrent;

public abstract interface Delayed extends Comparable<Delayed>
{
  public abstract long getDelay(TimeUnit paramTimeUnit);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.Delayed
 * JD-Core Version:    0.6.2
 */