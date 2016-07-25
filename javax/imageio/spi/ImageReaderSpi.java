/*     */ package javax.imageio.spi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.imageio.ImageReader;
/*     */ import javax.imageio.stream.ImageInputStream;
/*     */ 
/*     */ public abstract class ImageReaderSpi extends ImageReaderWriterSpi
/*     */ {
/*     */ 
/*     */   @Deprecated
/*  84 */   public static final Class[] STANDARD_INPUT_TYPE = { ImageInputStream.class };
/*     */ 
/*  91 */   protected Class[] inputTypes = null;
/*     */ 
/*  98 */   protected String[] writerSpiNames = null;
/*     */ 
/* 104 */   private Class readerClass = null;
/*     */ 
/*     */   protected ImageReaderSpi()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ImageReaderSpi(String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String paramString3, Class[] paramArrayOfClass, String[] paramArrayOfString4, boolean paramBoolean1, String paramString4, String paramString5, String[] paramArrayOfString5, String[] paramArrayOfString6, boolean paramBoolean2, String paramString6, String paramString7, String[] paramArrayOfString7, String[] paramArrayOfString8)
/*     */   {
/* 212 */     super(paramString1, paramString2, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, paramString3, paramBoolean1, paramString4, paramString5, paramArrayOfString5, paramArrayOfString6, paramBoolean2, paramString6, paramString7, paramArrayOfString7, paramArrayOfString8);
/*     */ 
/* 225 */     if (paramArrayOfClass == null) {
/* 226 */       throw new IllegalArgumentException("inputTypes == null!");
/*     */     }
/*     */ 
/* 229 */     if (paramArrayOfClass.length == 0) {
/* 230 */       throw new IllegalArgumentException("inputTypes.length == 0!");
/*     */     }
/*     */ 
/* 234 */     this.inputTypes = (paramArrayOfClass == STANDARD_INPUT_TYPE ? new Class[] { ImageInputStream.class } : (Class[])paramArrayOfClass.clone());
/*     */ 
/* 239 */     if ((paramArrayOfString4 != null) && (paramArrayOfString4.length > 0))
/* 240 */       this.writerSpiNames = ((String[])paramArrayOfString4.clone());
/*     */   }
/*     */ 
/*     */   public Class[] getInputTypes()
/*     */   {
/* 258 */     return (Class[])this.inputTypes.clone();
/*     */   }
/*     */ 
/*     */   public abstract boolean canDecodeInput(Object paramObject)
/*     */     throws IOException;
/*     */ 
/*     */   public ImageReader createReaderInstance()
/*     */     throws IOException
/*     */   {
/* 320 */     return createReaderInstance(null);
/*     */   }
/*     */ 
/*     */   public abstract ImageReader createReaderInstance(Object paramObject)
/*     */     throws IOException;
/*     */ 
/*     */   public boolean isOwnReader(ImageReader paramImageReader)
/*     */   {
/* 369 */     if (paramImageReader == null) {
/* 370 */       throw new IllegalArgumentException("reader == null!");
/*     */     }
/* 372 */     String str = paramImageReader.getClass().getName();
/* 373 */     return str.equals(this.pluginClassName);
/*     */   }
/*     */ 
/*     */   public String[] getImageWriterSpiNames()
/*     */   {
/* 410 */     return this.writerSpiNames == null ? null : (String[])this.writerSpiNames.clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.spi.ImageReaderSpi
 * JD-Core Version:    0.6.2
 */