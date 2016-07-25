package sun.management.counter;

public abstract interface ByteArrayCounter extends Counter
{
  public abstract byte[] byteArrayValue();

  public abstract byte byteAt(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.counter.ByteArrayCounter
 * JD-Core Version:    0.6.2
 */