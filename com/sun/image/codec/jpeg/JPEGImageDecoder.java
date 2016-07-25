package com.sun.image.codec.jpeg;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.InputStream;

public abstract interface JPEGImageDecoder
{
  public abstract JPEGDecodeParam getJPEGDecodeParam();

  public abstract void setJPEGDecodeParam(JPEGDecodeParam paramJPEGDecodeParam);

  public abstract InputStream getInputStream();

  public abstract Raster decodeAsRaster()
    throws IOException, ImageFormatException;

  public abstract BufferedImage decodeAsBufferedImage()
    throws IOException, ImageFormatException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.image.codec.jpeg.JPEGImageDecoder
 * JD-Core Version:    0.6.2
 */