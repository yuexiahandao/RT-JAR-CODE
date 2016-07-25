package com.sun.image.codec.jpeg;

public abstract interface JPEGEncodeParam extends Cloneable, JPEGDecodeParam
{
  public abstract Object clone();

  public abstract void setHorizontalSubsampling(int paramInt1, int paramInt2);

  public abstract void setVerticalSubsampling(int paramInt1, int paramInt2);

  public abstract void setQTable(int paramInt, JPEGQTable paramJPEGQTable);

  public abstract void setDCHuffmanTable(int paramInt, JPEGHuffmanTable paramJPEGHuffmanTable);

  public abstract void setACHuffmanTable(int paramInt, JPEGHuffmanTable paramJPEGHuffmanTable);

  public abstract void setDCHuffmanComponentMapping(int paramInt1, int paramInt2);

  public abstract void setACHuffmanComponentMapping(int paramInt1, int paramInt2);

  public abstract void setQTableComponentMapping(int paramInt1, int paramInt2);

  public abstract void setImageInfoValid(boolean paramBoolean);

  public abstract void setTableInfoValid(boolean paramBoolean);

  public abstract void setMarkerData(int paramInt, byte[][] paramArrayOfByte);

  public abstract void addMarkerData(int paramInt, byte[] paramArrayOfByte);

  public abstract void setRestartInterval(int paramInt);

  public abstract void setDensityUnit(int paramInt);

  public abstract void setXDensity(int paramInt);

  public abstract void setYDensity(int paramInt);

  public abstract void setQuality(float paramFloat, boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.image.codec.jpeg.JPEGEncodeParam
 * JD-Core Version:    0.6.2
 */