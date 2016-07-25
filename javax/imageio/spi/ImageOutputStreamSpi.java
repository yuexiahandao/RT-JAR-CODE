/*     */ package javax.imageio.spi;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import javax.imageio.stream.ImageOutputStream;
/*     */ 
/*     */ public abstract class ImageOutputStreamSpi extends IIOServiceProvider
/*     */ {
/*     */   protected Class<?> outputClass;
/*     */ 
/*     */   protected ImageOutputStreamSpi()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ImageOutputStreamSpi(String paramString1, String paramString2, Class<?> paramClass)
/*     */   {
/*  91 */     super(paramString1, paramString2);
/*  92 */     this.outputClass = paramClass;
/*     */   }
/*     */ 
/*     */   public Class<?> getOutputClass()
/*     */   {
/* 111 */     return this.outputClass;
/*     */   }
/*     */ 
/*     */   public boolean canUseCacheFile()
/*     */   {
/* 128 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean needsCacheFile()
/*     */   {
/* 142 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract ImageOutputStream createOutputStreamInstance(Object paramObject, boolean paramBoolean, File paramFile)
/*     */     throws IOException;
/*     */ 
/*     */   public ImageOutputStream createOutputStreamInstance(Object paramObject)
/*     */     throws IOException
/*     */   {
/* 198 */     return createOutputStreamInstance(paramObject, true, null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.spi.ImageOutputStreamSpi
 * JD-Core Version:    0.6.2
 */