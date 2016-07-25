package sun.dc.path;

public abstract interface FastPathProducer
{
  public abstract void getBox(float[] paramArrayOfFloat)
    throws PathError;

  public abstract void sendTo(PathConsumer paramPathConsumer)
    throws PathError, PathException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.dc.path.FastPathProducer
 * JD-Core Version:    0.6.2
 */