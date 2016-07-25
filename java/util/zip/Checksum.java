package java.util.zip;

public abstract interface Checksum
{
  public abstract void update(int paramInt);

  public abstract void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  public abstract long getValue();

  public abstract void reset();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.zip.Checksum
 * JD-Core Version:    0.6.2
 */