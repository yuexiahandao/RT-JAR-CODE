/*     */ package java.awt.image.renderable;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DataBuffer;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.RenderedImage;
/*     */ import java.awt.image.SampleModel;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class RenderableImageProducer
/*     */   implements ImageProducer, Runnable
/*     */ {
/*     */   RenderableImage rdblImage;
/*     */   RenderContext rc;
/*  71 */   Vector ics = new Vector();
/*     */ 
/*     */   public RenderableImageProducer(RenderableImage paramRenderableImage, RenderContext paramRenderContext)
/*     */   {
/*  82 */     this.rdblImage = paramRenderableImage;
/*  83 */     this.rc = paramRenderContext;
/*     */   }
/*     */ 
/*     */   public synchronized void setRenderContext(RenderContext paramRenderContext)
/*     */   {
/*  92 */     this.rc = paramRenderContext;
/*     */   }
/*     */ 
/*     */   public synchronized void addConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 102 */     if (!this.ics.contains(paramImageConsumer))
/* 103 */       this.ics.addElement(paramImageConsumer);
/*     */   }
/*     */ 
/*     */   public synchronized boolean isConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 115 */     return this.ics.contains(paramImageConsumer);
/*     */   }
/*     */ 
/*     */   public synchronized void removeConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 125 */     this.ics.removeElement(paramImageConsumer);
/*     */   }
/*     */ 
/*     */   public synchronized void startProduction(ImageConsumer paramImageConsumer)
/*     */   {
/* 136 */     addConsumer(paramImageConsumer);
/*     */ 
/* 138 */     Thread localThread = new Thread(this, "RenderableImageProducer Thread");
/* 139 */     localThread.start();
/*     */   }
/*     */ 
/*     */   public void requestTopDownLeftRightResend(ImageConsumer paramImageConsumer)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     RenderedImage localRenderedImage;
/* 160 */     if (this.rc != null)
/* 161 */       localRenderedImage = this.rdblImage.createRendering(this.rc);
/*     */     else {
/* 163 */       localRenderedImage = this.rdblImage.createDefaultRendering();
/*     */     }
/*     */ 
/* 167 */     ColorModel localColorModel = localRenderedImage.getColorModel();
/* 168 */     Raster localRaster = localRenderedImage.getData();
/* 169 */     SampleModel localSampleModel = localRaster.getSampleModel();
/* 170 */     DataBuffer localDataBuffer = localRaster.getDataBuffer();
/*     */ 
/* 172 */     if (localColorModel == null) {
/* 173 */       localColorModel = ColorModel.getRGBdefault();
/*     */     }
/* 175 */     int i = localRaster.getMinX();
/* 176 */     int j = localRaster.getMinY();
/* 177 */     int k = localRaster.getWidth();
/* 178 */     int m = localRaster.getHeight();
/*     */ 
/* 183 */     Enumeration localEnumeration = this.ics.elements();
/*     */     ImageConsumer localImageConsumer;
/* 184 */     while (localEnumeration.hasMoreElements()) {
/* 185 */       localImageConsumer = (ImageConsumer)localEnumeration.nextElement();
/* 186 */       localImageConsumer.setDimensions(k, m);
/* 187 */       localImageConsumer.setHints(30);
/*     */     }
/*     */ 
/* 195 */     int[] arrayOfInt1 = new int[k];
/*     */ 
/* 197 */     int i2 = localSampleModel.getNumBands();
/* 198 */     int[] arrayOfInt2 = new int[i2];
/* 199 */     for (int i1 = 0; i1 < m; i1++) {
/* 200 */       for (int n = 0; n < k; n++) {
/* 201 */         localSampleModel.getPixel(n, i1, arrayOfInt2, localDataBuffer);
/* 202 */         arrayOfInt1[n] = localColorModel.getDataElement(arrayOfInt2, 0);
/*     */       }
/*     */ 
/* 205 */       localEnumeration = this.ics.elements();
/* 206 */       while (localEnumeration.hasMoreElements()) {
/* 207 */         localImageConsumer = (ImageConsumer)localEnumeration.nextElement();
/* 208 */         localImageConsumer.setPixels(0, i1, k, 1, localColorModel, arrayOfInt1, 0, k);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 213 */     localEnumeration = this.ics.elements();
/* 214 */     while (localEnumeration.hasMoreElements()) {
/* 215 */       localImageConsumer = (ImageConsumer)localEnumeration.nextElement();
/* 216 */       localImageConsumer.imageComplete(3);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.renderable.RenderableImageProducer
 * JD-Core Version:    0.6.2
 */