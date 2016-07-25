/*     */ package javax.imageio.spi;
/*     */ 
/*     */ import java.awt.image.RenderedImage;
/*     */ import java.io.IOException;
/*     */ import javax.imageio.ImageTypeSpecifier;
/*     */ import javax.imageio.ImageWriter;
/*     */ import javax.imageio.stream.ImageOutputStream;
/*     */ 
/*     */ public abstract class ImageWriterSpi extends ImageReaderWriterSpi
/*     */ {
/*     */ 
/*     */   @Deprecated
/*  86 */   public static final Class[] STANDARD_OUTPUT_TYPE = { ImageOutputStream.class };
/*     */ 
/*  93 */   protected Class[] outputTypes = null;
/*     */ 
/* 100 */   protected String[] readerSpiNames = null;
/*     */ 
/* 106 */   private Class writerClass = null;
/*     */ 
/*     */   protected ImageWriterSpi()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ImageWriterSpi(String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String paramString3, Class[] paramArrayOfClass, String[] paramArrayOfString4, boolean paramBoolean1, String paramString4, String paramString5, String[] paramArrayOfString5, String[] paramArrayOfString6, boolean paramBoolean2, String paramString6, String paramString7, String[] paramArrayOfString7, String[] paramArrayOfString8)
/*     */   {
/* 213 */     super(paramString1, paramString2, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, paramString3, paramBoolean1, paramString4, paramString5, paramArrayOfString5, paramArrayOfString6, paramBoolean2, paramString6, paramString7, paramArrayOfString7, paramArrayOfString8);
/*     */ 
/* 226 */     if (paramArrayOfClass == null) {
/* 227 */       throw new IllegalArgumentException("outputTypes == null!");
/*     */     }
/*     */ 
/* 230 */     if (paramArrayOfClass.length == 0) {
/* 231 */       throw new IllegalArgumentException("outputTypes.length == 0!");
/*     */     }
/*     */ 
/* 235 */     this.outputTypes = (paramArrayOfClass == STANDARD_OUTPUT_TYPE ? new Class[] { ImageOutputStream.class } : (Class[])paramArrayOfClass.clone());
/*     */ 
/* 240 */     if ((paramArrayOfString4 != null) && (paramArrayOfString4.length > 0))
/* 241 */       this.readerSpiNames = ((String[])paramArrayOfString4.clone());
/*     */   }
/*     */ 
/*     */   public boolean isFormatLossless()
/*     */   {
/* 254 */     return true;
/*     */   }
/*     */ 
/*     */   public Class[] getOutputTypes()
/*     */   {
/* 271 */     return (Class[])this.outputTypes.clone();
/*     */   }
/*     */ 
/*     */   public abstract boolean canEncodeImage(ImageTypeSpecifier paramImageTypeSpecifier);
/*     */ 
/*     */   public boolean canEncodeImage(RenderedImage paramRenderedImage)
/*     */   {
/* 332 */     return canEncodeImage(ImageTypeSpecifier.createFromRenderedImage(paramRenderedImage));
/*     */   }
/*     */ 
/*     */   public ImageWriter createWriterInstance()
/*     */     throws IOException
/*     */   {
/* 351 */     return createWriterInstance(null);
/*     */   }
/*     */ 
/*     */   public abstract ImageWriter createWriterInstance(Object paramObject)
/*     */     throws IOException;
/*     */ 
/*     */   public boolean isOwnWriter(ImageWriter paramImageWriter)
/*     */   {
/* 395 */     if (paramImageWriter == null) {
/* 396 */       throw new IllegalArgumentException("writer == null!");
/*     */     }
/* 398 */     String str = paramImageWriter.getClass().getName();
/* 399 */     return str.equals(this.pluginClassName);
/*     */   }
/*     */ 
/*     */   public String[] getImageReaderSpiNames()
/*     */   {
/* 437 */     return this.readerSpiNames == null ? null : (String[])this.readerSpiNames.clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.spi.ImageWriterSpi
 * JD-Core Version:    0.6.2
 */