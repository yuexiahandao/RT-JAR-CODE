package sun.reflect;

abstract interface ByteVector
{
  public abstract int getLength();

  public abstract byte get(int paramInt);

  public abstract void put(int paramInt, byte paramByte);

  public abstract void add(byte paramByte);

  public abstract void trim();

  public abstract byte[] getData();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.ByteVector
 * JD-Core Version:    0.6.2
 */