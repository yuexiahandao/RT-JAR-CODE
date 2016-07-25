package sun.java2d.pipe;

public abstract interface AATileGenerator
{
  public abstract int getTileWidth();

  public abstract int getTileHeight();

  public abstract int getTypicalAlpha();

  public abstract void nextTile();

  public abstract void getAlpha(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  public abstract void dispose();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.AATileGenerator
 * JD-Core Version:    0.6.2
 */