/*     */ package javax.imageio.spi;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import javax.imageio.stream.ImageInputStream;
/*     */ 
/*     */ public abstract class ImageInputStreamSpi extends IIOServiceProvider
/*     */ {
/*     */   protected Class<?> inputClass;
/*     */ 
/*     */   protected ImageInputStreamSpi()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ImageInputStreamSpi(String paramString1, String paramString2, Class<?> paramClass)
/*     */   {
/*  90 */     super(paramString1, paramString2);
/*  91 */     this.inputClass = paramClass;
/*     */   }
/*     */ 
/*     */   public Class<?> getInputClass()
/*     */   {
/* 109 */     return this.inputClass;
/*     */   }
/*     */ 
/*     */   public boolean canUseCacheFile()
/*     */   {
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean needsCacheFile()
/*     */   {
/* 142 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract ImageInputStream createInputStreamInstance(Object paramObject, boolean paramBoolean, File paramFile)
/*     */     throws IOException;
/*     */ 
/*     */   public ImageInputStream createInputStreamInstance(Object paramObject)
/*     */     throws IOException
/*     */   {
/* 199 */     return createInputStreamInstance(paramObject, true, null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.spi.ImageInputStreamSpi
 * JD-Core Version:    0.6.2
 */