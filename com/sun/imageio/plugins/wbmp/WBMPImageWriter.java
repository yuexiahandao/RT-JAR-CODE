/*     */ package com.sun.imageio.plugins.wbmp;
/*     */ 
/*     */ import com.sun.imageio.plugins.common.I18N;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.DataBufferByte;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.awt.image.MultiPixelPackedSampleModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.RenderedImage;
/*     */ import java.awt.image.SampleModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.io.IOException;
/*     */ import javax.imageio.IIOImage;
/*     */ import javax.imageio.ImageTypeSpecifier;
/*     */ import javax.imageio.ImageWriteParam;
/*     */ import javax.imageio.ImageWriter;
/*     */ import javax.imageio.metadata.IIOMetadata;
/*     */ import javax.imageio.spi.ImageWriterSpi;
/*     */ import javax.imageio.stream.ImageOutputStream;
/*     */ 
/*     */ public class WBMPImageWriter extends ImageWriter
/*     */ {
/*  66 */   private ImageOutputStream stream = null;
/*     */ 
/*     */   private static int getNumBits(int paramInt)
/*     */   {
/*  70 */     int i = 32;
/*  71 */     int j = -2147483648;
/*  72 */     while ((j != 0) && ((paramInt & j) == 0)) {
/*  73 */       i--;
/*  74 */       j >>>= 1;
/*     */     }
/*  76 */     return i;
/*     */   }
/*     */ 
/*     */   private static byte[] intToMultiByte(int paramInt)
/*     */   {
/*  81 */     int i = getNumBits(paramInt);
/*  82 */     byte[] arrayOfByte = new byte[(i + 6) / 7];
/*     */ 
/*  84 */     int j = arrayOfByte.length - 1;
/*  85 */     for (int k = 0; k <= j; k++) {
/*  86 */       arrayOfByte[k] = ((byte)(paramInt >>> (j - k) * 7 & 0x7F));
/*  87 */       if (k != j)
/*     */       {
/*     */         int tmp55_53 = k;
/*     */         byte[] tmp55_52 = arrayOfByte; tmp55_52[tmp55_53] = ((byte)(tmp55_52[tmp55_53] | 0xFFFFFF80));
/*     */       }
/*     */     }
/*     */ 
/*  92 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public WBMPImageWriter(ImageWriterSpi paramImageWriterSpi)
/*     */   {
/*  99 */     super(paramImageWriterSpi);
/*     */   }
/*     */ 
/*     */   public void setOutput(Object paramObject) {
/* 103 */     super.setOutput(paramObject);
/* 104 */     if (paramObject != null) {
/* 105 */       if (!(paramObject instanceof ImageOutputStream))
/* 106 */         throw new IllegalArgumentException(I18N.getString("WBMPImageWriter"));
/* 107 */       this.stream = ((ImageOutputStream)paramObject);
/*     */     } else {
/* 109 */       this.stream = null;
/*     */     }
/*     */   }
/*     */ 
/* 113 */   public IIOMetadata getDefaultStreamMetadata(ImageWriteParam paramImageWriteParam) { return null; }
/*     */ 
/*     */ 
/*     */   public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam)
/*     */   {
/* 118 */     WBMPMetadata localWBMPMetadata = new WBMPMetadata();
/* 119 */     localWBMPMetadata.wbmpType = 0;
/* 120 */     return localWBMPMetadata;
/*     */   }
/*     */ 
/*     */   public IIOMetadata convertStreamMetadata(IIOMetadata paramIIOMetadata, ImageWriteParam paramImageWriteParam)
/*     */   {
/* 125 */     return null;
/*     */   }
/*     */ 
/*     */   public IIOMetadata convertImageMetadata(IIOMetadata paramIIOMetadata, ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam)
/*     */   {
/* 131 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean canWriteRasters() {
/* 135 */     return true;
/*     */   }
/*     */ 
/*     */   public void write(IIOMetadata paramIIOMetadata, IIOImage paramIIOImage, ImageWriteParam paramImageWriteParam)
/*     */     throws IOException
/*     */   {
/* 142 */     if (this.stream == null) {
/* 143 */       throw new IllegalStateException(I18N.getString("WBMPImageWriter3"));
/*     */     }
/*     */ 
/* 146 */     if (paramIIOImage == null) {
/* 147 */       throw new IllegalArgumentException(I18N.getString("WBMPImageWriter4"));
/*     */     }
/*     */ 
/* 150 */     clearAbortRequest();
/* 151 */     processImageStarted(0);
/* 152 */     if (paramImageWriteParam == null) {
/* 153 */       paramImageWriteParam = getDefaultWriteParam();
/*     */     }
/* 155 */     RenderedImage localRenderedImage = null;
/* 156 */     Object localObject1 = null;
/* 157 */     boolean bool = paramIIOImage.hasRaster();
/* 158 */     Rectangle localRectangle1 = paramImageWriteParam.getSourceRegion();
/* 159 */     SampleModel localSampleModel = null;
/*     */ 
/* 161 */     if (bool) {
/* 162 */       localObject1 = paramIIOImage.getRaster();
/* 163 */       localSampleModel = ((Raster)localObject1).getSampleModel();
/*     */     } else {
/* 165 */       localRenderedImage = paramIIOImage.getRenderedImage();
/* 166 */       localSampleModel = localRenderedImage.getSampleModel();
/*     */ 
/* 168 */       localObject1 = localRenderedImage.getData();
/*     */     }
/*     */ 
/* 171 */     checkSampleModel(localSampleModel);
/* 172 */     if (localRectangle1 == null)
/* 173 */       localRectangle1 = ((Raster)localObject1).getBounds();
/*     */     else {
/* 175 */       localRectangle1 = localRectangle1.intersection(((Raster)localObject1).getBounds());
/*     */     }
/* 177 */     if (localRectangle1.isEmpty()) {
/* 178 */       throw new RuntimeException(I18N.getString("WBMPImageWriter1"));
/*     */     }
/* 180 */     int i = paramImageWriteParam.getSourceXSubsampling();
/* 181 */     int j = paramImageWriteParam.getSourceYSubsampling();
/* 182 */     int k = paramImageWriteParam.getSubsamplingXOffset();
/* 183 */     int m = paramImageWriteParam.getSubsamplingYOffset();
/*     */ 
/* 185 */     localRectangle1.translate(k, m);
/* 186 */     localRectangle1.width -= k;
/* 187 */     localRectangle1.height -= m;
/*     */ 
/* 189 */     int n = localRectangle1.x / i;
/* 190 */     int i1 = localRectangle1.y / j;
/* 191 */     int i2 = (localRectangle1.width + i - 1) / i;
/* 192 */     int i3 = (localRectangle1.height + j - 1) / j;
/*     */ 
/* 194 */     Rectangle localRectangle2 = new Rectangle(n, i1, i2, i3);
/* 195 */     localSampleModel = localSampleModel.createCompatibleSampleModel(i2, i3);
/*     */ 
/* 197 */     Object localObject2 = localSampleModel;
/*     */ 
/* 200 */     if ((localSampleModel.getDataType() != 0) || (!(localSampleModel instanceof MultiPixelPackedSampleModel)) || (((MultiPixelPackedSampleModel)localSampleModel).getDataBitOffset() != 0))
/*     */     {
/* 203 */       localObject2 = new MultiPixelPackedSampleModel(0, i2, i3, 1, i2 + 7 >> 3, 0);
/*     */     }
/*     */     WritableRaster localWritableRaster;
/*     */     Object localObject3;
/*     */     int i8;
/*     */     int i9;
/*     */     int i10;
/*     */     int i11;
/* 209 */     if (!localRectangle2.equals(localRectangle1)) {
/* 210 */       if ((i == 1) && (j == 1)) {
/* 211 */         localObject1 = ((Raster)localObject1).createChild(((Raster)localObject1).getMinX(), ((Raster)localObject1).getMinY(), i2, i3, n, i1, null);
/*     */       }
/*     */       else
/*     */       {
/* 215 */         localWritableRaster = Raster.createWritableRaster((SampleModel)localObject2, new Point(n, i1));
/*     */ 
/* 218 */         localObject3 = ((DataBufferByte)localWritableRaster.getDataBuffer()).getData();
/*     */ 
/* 220 */         i6 = i1; int i7 = localRectangle1.y; i8 = 0;
/* 221 */         for (; i6 < i1 + i3; i7 += j)
/*     */         {
/* 223 */           i9 = 0; i10 = localRectangle1.x;
/* 224 */           for (; i9 < i2; i10 += i) {
/* 225 */             i11 = ((Raster)localObject1).getSample(i10, i7, 0);
/*     */             int tmp508_507 = (i8 + (i9 >> 3));
/*     */             Object tmp508_499 = localObject3; tmp508_499[tmp508_507] = ((byte)(tmp508_499[tmp508_507] | i11 << 7 - (i9 & 0x7)));
/*     */ 
/* 224 */             i9++;
/*     */           }
/*     */ 
/* 228 */           i8 += (i2 + 7 >> 3);
/*     */ 
/* 221 */           i6++;
/*     */         }
/*     */ 
/* 230 */         localObject1 = localWritableRaster;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 235 */     if (!localObject2.equals(((Raster)localObject1).getSampleModel())) {
/* 236 */       localWritableRaster = Raster.createWritableRaster((SampleModel)localObject2, new Point(((Raster)localObject1).getMinX(), ((Raster)localObject1).getMinY()));
/*     */ 
/* 240 */       localWritableRaster.setRect((Raster)localObject1);
/* 241 */       localObject1 = localWritableRaster;
/*     */     }
/*     */ 
/* 245 */     int i4 = 0;
/* 246 */     if ((!bool) && ((localRenderedImage.getColorModel() instanceof IndexColorModel))) {
/* 247 */       localObject3 = (IndexColorModel)localRenderedImage.getColorModel();
/* 248 */       i4 = ((IndexColorModel)localObject3).getRed(0) > ((IndexColorModel)localObject3).getRed(1) ? 1 : 0;
/*     */     }
/*     */ 
/* 252 */     int i5 = ((MultiPixelPackedSampleModel)localObject2).getScanlineStride();
/*     */ 
/* 254 */     int i6 = (i2 + 7) / 8;
/* 255 */     byte[] arrayOfByte1 = ((DataBufferByte)((Raster)localObject1).getDataBuffer()).getData();
/*     */ 
/* 258 */     this.stream.write(0);
/* 259 */     this.stream.write(0);
/* 260 */     this.stream.write(intToMultiByte(i2));
/* 261 */     this.stream.write(intToMultiByte(i3));
/*     */ 
/* 264 */     if ((i4 == 0) && (i5 == i6))
/*     */     {
/* 266 */       this.stream.write(arrayOfByte1, 0, i3 * i6);
/* 267 */       processImageProgress(100.0F);
/*     */     }
/*     */     else {
/* 270 */       i8 = 0;
/* 271 */       if (i4 == 0)
/*     */       {
/* 273 */         for (i9 = 0; (i9 < i3) && 
/* 274 */           (!abortRequested()); i9++)
/*     */         {
/* 276 */           this.stream.write(arrayOfByte1, i8, i6);
/* 277 */           i8 += i5;
/* 278 */           processImageProgress(100.0F * i9 / i3);
/*     */         }
/*     */       }
/*     */       else {
/* 282 */         byte[] arrayOfByte2 = new byte[i6];
/* 283 */         for (i10 = 0; (i10 < i3) && 
/* 284 */           (!abortRequested()); i10++)
/*     */         {
/* 286 */           for (i11 = 0; i11 < i6; i11++) {
/* 287 */             arrayOfByte2[i11] = ((byte)(arrayOfByte1[(i11 + i8)] ^ 0xFFFFFFFF));
/*     */           }
/* 289 */           this.stream.write(arrayOfByte2, 0, i6);
/* 290 */           i8 += i5;
/* 291 */           processImageProgress(100.0F * i10 / i3);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 296 */     if (abortRequested()) {
/* 297 */       processWriteAborted();
/*     */     } else {
/* 299 */       processImageComplete();
/* 300 */       this.stream.flushBefore(this.stream.getStreamPosition());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 305 */     super.reset();
/* 306 */     this.stream = null;
/*     */   }
/*     */ 
/*     */   private void checkSampleModel(SampleModel paramSampleModel) {
/* 310 */     int i = paramSampleModel.getDataType();
/* 311 */     if ((i < 0) || (i > 3) || (paramSampleModel.getNumBands() != 1) || (paramSampleModel.getSampleSize(0) != 1))
/*     */     {
/* 313 */       throw new IllegalArgumentException(I18N.getString("WBMPImageWriter2"));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.wbmp.WBMPImageWriter
 * JD-Core Version:    0.6.2
 */