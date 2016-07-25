/*     */ package com.sun.imageio.plugins.wbmp;
/*     */ 
/*     */ import com.sun.imageio.plugins.common.I18N;
/*     */ import com.sun.imageio.plugins.common.ReaderUtil;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.DataBufferByte;
/*     */ import java.awt.image.MultiPixelPackedSampleModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import javax.imageio.IIOException;
/*     */ import javax.imageio.ImageReadParam;
/*     */ import javax.imageio.ImageReader;
/*     */ import javax.imageio.ImageTypeSpecifier;
/*     */ import javax.imageio.metadata.IIOMetadata;
/*     */ import javax.imageio.spi.ImageReaderSpi;
/*     */ import javax.imageio.stream.ImageInputStream;
/*     */ 
/*     */ public class WBMPImageReader extends ImageReader
/*     */ {
/*  57 */   private ImageInputStream iis = null;
/*     */ 
/*  60 */   private boolean gotHeader = false;
/*     */   private int width;
/*     */   private int height;
/*     */   private int wbmpType;
/*     */   private WBMPMetadata metadata;
/*     */ 
/*     */   public WBMPImageReader(ImageReaderSpi paramImageReaderSpi)
/*     */   {
/*  76 */     super(paramImageReaderSpi);
/*     */   }
/*     */ 
/*     */   public void setInput(Object paramObject, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*  83 */     super.setInput(paramObject, paramBoolean1, paramBoolean2);
/*  84 */     this.iis = ((ImageInputStream)paramObject);
/*  85 */     this.gotHeader = false;
/*     */   }
/*     */ 
/*     */   public int getNumImages(boolean paramBoolean) throws IOException
/*     */   {
/*  90 */     if (this.iis == null) {
/*  91 */       throw new IllegalStateException(I18N.getString("GetNumImages0"));
/*     */     }
/*  93 */     if ((this.seekForwardOnly) && (paramBoolean)) {
/*  94 */       throw new IllegalStateException(I18N.getString("GetNumImages1"));
/*     */     }
/*  96 */     return 1;
/*     */   }
/*     */ 
/*     */   public int getWidth(int paramInt) throws IOException {
/* 100 */     checkIndex(paramInt);
/* 101 */     readHeader();
/* 102 */     return this.width;
/*     */   }
/*     */ 
/*     */   public int getHeight(int paramInt) throws IOException {
/* 106 */     checkIndex(paramInt);
/* 107 */     readHeader();
/* 108 */     return this.height;
/*     */   }
/*     */ 
/*     */   public boolean isRandomAccessEasy(int paramInt) throws IOException {
/* 112 */     checkIndex(paramInt);
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */   private void checkIndex(int paramInt) {
/* 117 */     if (paramInt != 0)
/* 118 */       throw new IndexOutOfBoundsException(I18N.getString("WBMPImageReader0"));
/*     */   }
/*     */ 
/*     */   public void readHeader() throws IOException
/*     */   {
/* 123 */     if (this.gotHeader) {
/* 124 */       return;
/*     */     }
/* 126 */     if (this.iis == null) {
/* 127 */       throw new IllegalStateException("Input source not set!");
/*     */     }
/*     */ 
/* 130 */     this.metadata = new WBMPMetadata();
/*     */ 
/* 132 */     this.wbmpType = this.iis.readByte();
/* 133 */     int i = this.iis.readByte();
/*     */ 
/* 136 */     if ((i != 0) || (!isValidWbmpType(this.wbmpType)))
/*     */     {
/* 139 */       throw new IIOException(I18N.getString("WBMPImageReader2"));
/*     */     }
/*     */ 
/* 142 */     this.metadata.wbmpType = this.wbmpType;
/*     */ 
/* 145 */     this.width = ReaderUtil.readMultiByteInteger(this.iis);
/* 146 */     this.metadata.width = this.width;
/*     */ 
/* 149 */     this.height = ReaderUtil.readMultiByteInteger(this.iis);
/* 150 */     this.metadata.height = this.height;
/*     */ 
/* 152 */     this.gotHeader = true;
/*     */   }
/*     */ 
/*     */   public Iterator getImageTypes(int paramInt) throws IOException
/*     */   {
/* 157 */     checkIndex(paramInt);
/* 158 */     readHeader();
/*     */ 
/* 160 */     BufferedImage localBufferedImage = new BufferedImage(1, 1, 12);
/*     */ 
/* 162 */     ArrayList localArrayList = new ArrayList(1);
/* 163 */     localArrayList.add(new ImageTypeSpecifier(localBufferedImage));
/* 164 */     return localArrayList.iterator();
/*     */   }
/*     */ 
/*     */   public ImageReadParam getDefaultReadParam() {
/* 168 */     return new ImageReadParam();
/*     */   }
/*     */ 
/*     */   public IIOMetadata getImageMetadata(int paramInt) throws IOException
/*     */   {
/* 173 */     checkIndex(paramInt);
/* 174 */     if (this.metadata == null) {
/* 175 */       readHeader();
/*     */     }
/* 177 */     return this.metadata;
/*     */   }
/*     */ 
/*     */   public IIOMetadata getStreamMetadata() throws IOException {
/* 181 */     return null;
/*     */   }
/*     */ 
/*     */   public BufferedImage read(int paramInt, ImageReadParam paramImageReadParam)
/*     */     throws IOException
/*     */   {
/* 187 */     if (this.iis == null) {
/* 188 */       throw new IllegalStateException(I18N.getString("WBMPImageReader1"));
/*     */     }
/*     */ 
/* 191 */     checkIndex(paramInt);
/* 192 */     clearAbortRequest();
/* 193 */     processImageStarted(paramInt);
/* 194 */     if (paramImageReadParam == null) {
/* 195 */       paramImageReadParam = getDefaultReadParam();
/*     */     }
/*     */ 
/* 198 */     readHeader();
/*     */ 
/* 200 */     Rectangle localRectangle1 = new Rectangle(0, 0, 0, 0);
/* 201 */     Rectangle localRectangle2 = new Rectangle(0, 0, 0, 0);
/*     */ 
/* 203 */     computeRegions(paramImageReadParam, this.width, this.height, paramImageReadParam.getDestination(), localRectangle1, localRectangle2);
/*     */ 
/* 208 */     int i = paramImageReadParam.getSourceXSubsampling();
/* 209 */     int j = paramImageReadParam.getSourceYSubsampling();
/* 210 */     int k = paramImageReadParam.getSubsamplingXOffset();
/* 211 */     int m = paramImageReadParam.getSubsamplingYOffset();
/*     */ 
/* 214 */     BufferedImage localBufferedImage = paramImageReadParam.getDestination();
/*     */ 
/* 216 */     if (localBufferedImage == null) {
/* 217 */       localBufferedImage = new BufferedImage(localRectangle2.x + localRectangle2.width, localRectangle2.y + localRectangle2.height, 12);
/*     */     }
/*     */ 
/* 221 */     int n = (localRectangle2.equals(new Rectangle(0, 0, this.width, this.height))) && (localRectangle2.equals(new Rectangle(0, 0, localBufferedImage.getWidth(), localBufferedImage.getHeight()))) ? 1 : 0;
/*     */ 
/* 226 */     WritableRaster localWritableRaster = localBufferedImage.getWritableTile(0, 0);
/*     */ 
/* 229 */     MultiPixelPackedSampleModel localMultiPixelPackedSampleModel = (MultiPixelPackedSampleModel)localBufferedImage.getSampleModel();
/*     */ 
/* 232 */     if (n != 0) {
/* 233 */       if (abortRequested()) {
/* 234 */         processReadAborted();
/* 235 */         return localBufferedImage;
/*     */       }
/*     */ 
/* 239 */       this.iis.read(((DataBufferByte)localWritableRaster.getDataBuffer()).getData(), 0, this.height * localMultiPixelPackedSampleModel.getScanlineStride());
/*     */ 
/* 241 */       processImageUpdate(localBufferedImage, 0, 0, this.width, this.height, 1, 1, new int[] { 0 });
/*     */ 
/* 245 */       processImageProgress(100.0F);
/*     */     } else {
/* 247 */       int i1 = (this.width + 7) / 8;
/* 248 */       byte[] arrayOfByte1 = new byte[i1];
/* 249 */       byte[] arrayOfByte2 = ((DataBufferByte)localWritableRaster.getDataBuffer()).getData();
/* 250 */       int i2 = localMultiPixelPackedSampleModel.getScanlineStride();
/* 251 */       this.iis.skipBytes(i1 * localRectangle1.y);
/* 252 */       int i3 = i1 * (j - 1);
/*     */ 
/* 255 */       int[] arrayOfInt1 = new int[localRectangle2.width];
/* 256 */       int[] arrayOfInt2 = new int[localRectangle2.width];
/* 257 */       int[] arrayOfInt3 = new int[localRectangle2.width];
/* 258 */       int[] arrayOfInt4 = new int[localRectangle2.width];
/*     */ 
/* 260 */       int i4 = localRectangle2.x; int i5 = localRectangle1.x; int i6 = 0;
/*     */ 
/* 262 */       for (; i4 < localRectangle2.x + localRectangle2.width; 
/* 262 */         i5 += i) {
/* 263 */         arrayOfInt3[i6] = (i5 >> 3);
/* 264 */         arrayOfInt1[i6] = (7 - (i5 & 0x7));
/* 265 */         arrayOfInt4[i6] = (i4 >> 3);
/* 266 */         arrayOfInt2[i6] = (7 - (i4 & 0x7));
/*     */ 
/* 262 */         i4++; i6++;
/*     */       }
/*     */ 
/* 269 */       i4 = 0; i5 = localRectangle1.y;
/* 270 */       i6 = localRectangle2.y * i2;
/* 271 */       for (; i4 < localRectangle2.height; i5 += j)
/*     */       {
/* 273 */         if (abortRequested())
/*     */           break;
/* 275 */         this.iis.read(arrayOfByte1, 0, i1);
/* 276 */         for (int i7 = 0; i7 < localRectangle2.width; i7++)
/*     */         {
/* 278 */           int i8 = arrayOfByte1[arrayOfInt3[i7]] >> arrayOfInt1[i7] & 0x1;
/*     */           int tmp609_608 = (i6 + arrayOfInt4[i7]);
/*     */           byte[] tmp609_599 = arrayOfByte2; tmp609_599[tmp609_608] = ((byte)(tmp609_599[tmp609_608] | i8 << arrayOfInt2[i7]));
/*     */         }
/*     */ 
/* 282 */         i6 += i2;
/* 283 */         this.iis.skipBytes(i3);
/* 284 */         processImageUpdate(localBufferedImage, 0, i4, localRectangle2.width, 1, 1, 1, new int[] { 0 });
/*     */ 
/* 288 */         processImageProgress(100.0F * i4 / localRectangle2.height);
/*     */ 
/* 271 */         i4++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 292 */     if (abortRequested())
/* 293 */       processReadAborted();
/*     */     else
/* 295 */       processImageComplete();
/* 296 */     return localBufferedImage;
/*     */   }
/*     */ 
/*     */   public boolean canReadRaster() {
/* 300 */     return true;
/*     */   }
/*     */ 
/*     */   public Raster readRaster(int paramInt, ImageReadParam paramImageReadParam) throws IOException
/*     */   {
/* 305 */     BufferedImage localBufferedImage = read(paramInt, paramImageReadParam);
/* 306 */     return localBufferedImage.getData();
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 310 */     super.reset();
/* 311 */     this.iis = null;
/* 312 */     this.gotHeader = false;
/*     */   }
/*     */ 
/*     */   boolean isValidWbmpType(int paramInt)
/*     */   {
/* 320 */     return paramInt == 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.wbmp.WBMPImageReader
 * JD-Core Version:    0.6.2
 */