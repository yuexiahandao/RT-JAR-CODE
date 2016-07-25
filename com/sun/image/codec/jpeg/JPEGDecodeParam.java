package com.sun.image.codec.jpeg;

public abstract interface JPEGDecodeParam extends Cloneable
{
  public static final int COLOR_ID_UNKNOWN = 0;
  public static final int COLOR_ID_GRAY = 1;
  public static final int COLOR_ID_RGB = 2;
  public static final int COLOR_ID_YCbCr = 3;
  public static final int COLOR_ID_CMYK = 4;
  public static final int COLOR_ID_PYCC = 5;
  public static final int COLOR_ID_RGBA = 6;
  public static final int COLOR_ID_YCbCrA = 7;
  public static final int COLOR_ID_RGBA_INVERTED = 8;
  public static final int COLOR_ID_YCbCrA_INVERTED = 9;
  public static final int COLOR_ID_PYCCA = 10;
  public static final int COLOR_ID_YCCK = 11;
  public static final int NUM_COLOR_ID = 12;
  public static final int NUM_TABLES = 4;
  public static final int DENSITY_UNIT_ASPECT_RATIO = 0;
  public static final int DENSITY_UNIT_DOTS_INCH = 1;
  public static final int DENSITY_UNIT_DOTS_CM = 2;
  public static final int NUM_DENSITY_UNIT = 3;
  public static final int APP0_MARKER = 224;
  public static final int APP1_MARKER = 225;
  public static final int APP2_MARKER = 226;
  public static final int APP3_MARKER = 227;
  public static final int APP4_MARKER = 228;
  public static final int APP5_MARKER = 229;
  public static final int APP6_MARKER = 230;
  public static final int APP7_MARKER = 231;
  public static final int APP8_MARKER = 232;
  public static final int APP9_MARKER = 233;
  public static final int APPA_MARKER = 234;
  public static final int APPB_MARKER = 235;
  public static final int APPC_MARKER = 236;
  public static final int APPD_MARKER = 237;
  public static final int APPE_MARKER = 238;
  public static final int APPF_MARKER = 239;
  public static final int COMMENT_MARKER = 254;

  public abstract Object clone();

  public abstract int getWidth();

  public abstract int getHeight();

  public abstract int getHorizontalSubsampling(int paramInt);

  public abstract int getVerticalSubsampling(int paramInt);

  public abstract JPEGQTable getQTable(int paramInt);

  public abstract JPEGQTable getQTableForComponent(int paramInt);

  public abstract JPEGHuffmanTable getDCHuffmanTable(int paramInt);

  public abstract JPEGHuffmanTable getDCHuffmanTableForComponent(int paramInt);

  public abstract JPEGHuffmanTable getACHuffmanTable(int paramInt);

  public abstract JPEGHuffmanTable getACHuffmanTableForComponent(int paramInt);

  public abstract int getDCHuffmanComponentMapping(int paramInt);

  public abstract int getACHuffmanComponentMapping(int paramInt);

  public abstract int getQTableComponentMapping(int paramInt);

  public abstract boolean isImageInfoValid();

  public abstract boolean isTableInfoValid();

  public abstract boolean getMarker(int paramInt);

  public abstract byte[][] getMarkerData(int paramInt);

  public abstract int getEncodedColorID();

  public abstract int getNumComponents();

  public abstract int getRestartInterval();

  public abstract int getDensityUnit();

  public abstract int getXDensity();

  public abstract int getYDensity();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.image.codec.jpeg.JPEGDecodeParam
 * JD-Core Version:    0.6.2
 */