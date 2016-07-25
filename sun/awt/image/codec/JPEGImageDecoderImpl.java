/*     */ package sun.awt.image.codec;
/*     */ 
/*     */ import com.sun.image.codec.jpeg.ImageFormatException;
/*     */ import com.sun.image.codec.jpeg.JPEGDecodeParam;
/*     */ import com.sun.image.codec.jpeg.JPEGImageDecoder;
/*     */ import java.awt.Point;
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ComponentColorModel;
/*     */ import java.awt.image.DataBufferByte;
/*     */ import java.awt.image.DataBufferInt;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ 
/*     */ public class JPEGImageDecoderImpl
/*     */   implements JPEGImageDecoder
/*     */ {
/*  96 */   private static final Class InputStreamClass = InputStream.class;
/*     */ 
/* 102 */   private JPEGDecodeParam param = null;
/* 103 */   private InputStream input = null;
/* 104 */   private WritableRaster aRas = null;
/* 105 */   private BufferedImage aBufImg = null;
/* 106 */   private ColorModel cm = null;
/* 107 */   private boolean unpack = false;
/* 108 */   private boolean flip = false;
/*     */ 
/*     */   public JPEGImageDecoderImpl(InputStream paramInputStream)
/*     */   {
/* 124 */     if (paramInputStream == null) {
/* 125 */       throw new IllegalArgumentException("InputStream is null.");
/*     */     }
/* 127 */     this.input = paramInputStream;
/* 128 */     initDecoder(InputStreamClass);
/*     */   }
/*     */ 
/*     */   public JPEGImageDecoderImpl(InputStream paramInputStream, JPEGDecodeParam paramJPEGDecodeParam)
/*     */   {
/* 140 */     this(paramInputStream);
/* 141 */     setJPEGDecodeParam(paramJPEGDecodeParam);
/*     */   }
/*     */ 
/*     */   public JPEGDecodeParam getJPEGDecodeParam()
/*     */   {
/* 149 */     if (this.param != null) {
/* 150 */       return (JPEGDecodeParam)this.param.clone();
/*     */     }
/* 152 */     return null;
/*     */   }
/*     */ 
/*     */   public void setJPEGDecodeParam(JPEGDecodeParam paramJPEGDecodeParam)
/*     */   {
/* 161 */     this.param = ((JPEGDecodeParam)paramJPEGDecodeParam.clone());
/*     */   }
/*     */ 
/*     */   public synchronized InputStream getInputStream()
/*     */   {
/* 169 */     return this.input;
/*     */   }
/*     */ 
/*     */   public synchronized Raster decodeAsRaster()
/*     */     throws ImageFormatException
/*     */   {
/*     */     try
/*     */     {
/* 185 */       this.param = readJPEGStream(this.input, this.param, false);
/*     */     } catch (IOException localIOException) {
/* 187 */       System.out.println("Can't open input Stream" + localIOException);
/* 188 */       localIOException.printStackTrace();
/*     */     }
/*     */ 
/* 191 */     return this.aRas;
/*     */   }
/*     */ 
/*     */   public synchronized BufferedImage decodeAsBufferedImage()
/*     */     throws ImageFormatException
/*     */   {
/*     */     try
/*     */     {
/* 208 */       this.param = readJPEGStream(this.input, this.param, true);
/*     */     } catch (IOException localIOException) {
/* 210 */       System.out.println("Can't open input Stream" + localIOException);
/* 211 */       localIOException.printStackTrace();
/*     */     }
/* 213 */     return this.aBufImg;
/*     */   }
/*     */ 
/*     */   private native void initDecoder(Class paramClass);
/*     */ 
/*     */   private synchronized native JPEGDecodeParam readJPEGStream(InputStream paramInputStream, JPEGDecodeParam paramJPEGDecodeParam, boolean paramBoolean)
/*     */     throws IOException, ImageFormatException;
/*     */ 
/*     */   private void readTables()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 245 */       this.param = readJPEGStream(this.input, null, false);
/*     */     }
/*     */     catch (ImageFormatException localImageFormatException) {
/* 248 */       localImageFormatException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private int getDecodedColorModel(int paramInt, boolean paramBoolean)
/*     */     throws ImageFormatException
/*     */   {
/* 258 */     int[] arrayOfInt1 = { 8 };
/* 259 */     int[] arrayOfInt2 = { 8, 8, 8 };
/* 260 */     int[] arrayOfInt3 = { 8, 8, 8, 8 };
/*     */ 
/* 262 */     this.cm = null;
/* 263 */     this.unpack = false;
/* 264 */     this.flip = false;
/*     */ 
/* 266 */     if (!paramBoolean) return paramInt;
/*     */ 
/* 268 */     switch (paramInt) {
/*     */     case 1:
/* 270 */       this.cm = new ComponentColorModel(ColorSpace.getInstance(1003), arrayOfInt1, false, false, 1, 0);
/*     */ 
/* 274 */       return paramInt;
/*     */     case 5:
/* 276 */       this.cm = new ComponentColorModel(ColorSpace.getInstance(1002), arrayOfInt2, false, false, 1, 0);
/*     */ 
/* 280 */       return paramInt;
/*     */     case 10:
/* 282 */       this.cm = new ComponentColorModel(ColorSpace.getInstance(1002), arrayOfInt3, true, false, 3, 0);
/*     */ 
/* 286 */       return paramInt;
/*     */     case 2:
/*     */     case 3:
/* 290 */       this.unpack = true;
/* 291 */       this.cm = new DirectColorModel(24, 16711680, 65280, 255);
/* 292 */       return 2;
/*     */     case 8:
/*     */     case 9:
/* 296 */       this.flip = true;
/*     */     case 6:
/*     */     case 7:
/* 299 */       this.unpack = true;
/* 300 */       this.cm = new DirectColorModel(ColorSpace.getInstance(1000), 32, 16711680, 65280, 255, -16777216, false, 3);
/*     */ 
/* 304 */       return 6;
/*     */     case 0:
/*     */     case 4:
/*     */     case 11:
/*     */     }
/*     */ 
/* 310 */     throw new ImageFormatException("Can't construct a BufferedImage for given COLOR_ID");
/*     */   }
/*     */ 
/*     */   private Object allocateDataBuffer(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*     */     Object localObject;
/* 329 */     if (this.unpack)
/*     */     {
/*     */       int[] arrayOfInt;
/* 330 */       if (paramInt3 == 3) {
/* 331 */         arrayOfInt = new int[] { 16711680, 65280, 255 };
/* 332 */         this.aRas = Raster.createPackedRaster(3, paramInt1, paramInt2, arrayOfInt, new Point(0, 0));
/*     */       }
/* 334 */       else if (paramInt3 == 4) {
/* 335 */         arrayOfInt = new int[] { 16711680, 65280, 255, -16777216 };
/* 336 */         this.aRas = Raster.createPackedRaster(3, paramInt1, paramInt2, arrayOfInt, new Point(0, 0));
/*     */       }
/*     */       else {
/* 339 */         throw new ImageFormatException("Can't unpack with anything other than 3 or 4 components");
/*     */       }
/*     */ 
/* 342 */       localObject = ((DataBufferInt)this.aRas.getDataBuffer()).getData();
/*     */     } else {
/* 344 */       this.aRas = Raster.createInterleavedRaster(0, paramInt1, paramInt2, paramInt3, new Point(0, 0));
/*     */ 
/* 347 */       localObject = ((DataBufferByte)this.aRas.getDataBuffer()).getData();
/*     */     }
/*     */ 
/* 350 */     if (this.cm != null) {
/* 351 */       this.aBufImg = new BufferedImage(this.cm, this.aRas, true, null);
/*     */     }
/* 353 */     return localObject;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 112 */     AccessController.doPrivileged(new LoadLibraryAction("jpeg"));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.codec.JPEGImageDecoderImpl
 * JD-Core Version:    0.6.2
 */