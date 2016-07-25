/*     */ package sun.java2d;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Composite;
/*     */ import java.awt.CompositeContext;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ import sun.awt.image.BufImgSurfaceData;
/*     */ import sun.java2d.loops.Blit;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ import sun.java2d.loops.XORComposite;
/*     */ 
/*     */ public class SunCompositeContext
/*     */   implements CompositeContext
/*     */ {
/*     */   ColorModel srcCM;
/*     */   ColorModel dstCM;
/*     */   Composite composite;
/*     */   CompositeType comptype;
/*     */ 
/*     */   public SunCompositeContext(AlphaComposite paramAlphaComposite, ColorModel paramColorModel1, ColorModel paramColorModel2)
/*     */   {
/*  49 */     if (paramColorModel1 == null) {
/*  50 */       throw new NullPointerException("Source color model cannot be null");
/*     */     }
/*  52 */     if (paramColorModel2 == null) {
/*  53 */       throw new NullPointerException("Destination color model cannot be null");
/*     */     }
/*  55 */     this.srcCM = paramColorModel1;
/*  56 */     this.dstCM = paramColorModel2;
/*  57 */     this.composite = paramAlphaComposite;
/*  58 */     this.comptype = CompositeType.forAlphaComposite(paramAlphaComposite);
/*     */   }
/*     */ 
/*     */   public SunCompositeContext(XORComposite paramXORComposite, ColorModel paramColorModel1, ColorModel paramColorModel2)
/*     */   {
/*  64 */     if (paramColorModel1 == null) {
/*  65 */       throw new NullPointerException("Source color model cannot be null");
/*     */     }
/*  67 */     if (paramColorModel2 == null) {
/*  68 */       throw new NullPointerException("Destination color model cannot be null");
/*     */     }
/*  70 */     this.srcCM = paramColorModel1;
/*  71 */     this.dstCM = paramColorModel2;
/*  72 */     this.composite = paramXORComposite;
/*  73 */     this.comptype = CompositeType.Xor;
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void compose(Raster paramRaster1, Raster paramRaster2, WritableRaster paramWritableRaster)
/*     */   {
/*  96 */     if (paramRaster2 != paramWritableRaster)
/*  97 */       paramWritableRaster.setDataElements(0, 0, paramRaster2);
/*     */     WritableRaster localWritableRaster;
/* 105 */     if ((paramRaster1 instanceof WritableRaster)) {
/* 106 */       localWritableRaster = (WritableRaster)paramRaster1;
/*     */     } else {
/* 108 */       localWritableRaster = paramRaster1.createCompatibleWritableRaster();
/* 109 */       localWritableRaster.setDataElements(0, 0, paramRaster1);
/*     */     }
/*     */ 
/* 112 */     int i = Math.min(localWritableRaster.getWidth(), paramRaster2.getWidth());
/* 113 */     int j = Math.min(localWritableRaster.getHeight(), paramRaster2.getHeight());
/*     */ 
/* 115 */     BufferedImage localBufferedImage1 = new BufferedImage(this.srcCM, localWritableRaster, this.srcCM.isAlphaPremultiplied(), null);
/*     */ 
/* 118 */     BufferedImage localBufferedImage2 = new BufferedImage(this.dstCM, paramWritableRaster, this.dstCM.isAlphaPremultiplied(), null);
/*     */ 
/* 122 */     SurfaceData localSurfaceData1 = BufImgSurfaceData.createData(localBufferedImage1);
/* 123 */     SurfaceData localSurfaceData2 = BufImgSurfaceData.createData(localBufferedImage2);
/* 124 */     Blit localBlit = Blit.getFromCache(localSurfaceData1.getSurfaceType(), this.comptype, localSurfaceData2.getSurfaceType());
/*     */ 
/* 127 */     localBlit.Blit(localSurfaceData1, localSurfaceData2, this.composite, null, 0, 0, 0, 0, i, j);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.SunCompositeContext
 * JD-Core Version:    0.6.2
 */