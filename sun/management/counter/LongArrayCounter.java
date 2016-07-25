package sun.management.counter;

public abstract interface LongArrayCounter extends Counter
{
  public abstract long[] longArrayValue();

  public abstract long longAt(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.counter.LongArrayCounter
 * JD-Core Version:    0.6.2
 */