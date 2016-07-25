/*     */ package javax.print;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public abstract class StreamPrintService
/*     */   implements PrintService
/*     */ {
/*     */   private OutputStream outStream;
/*  59 */   private boolean disposed = false;
/*     */ 
/*     */   private StreamPrintService()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected StreamPrintService(OutputStream paramOutputStream)
/*     */   {
/*  70 */     this.outStream = paramOutputStream;
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */   {
/*  79 */     return this.outStream;
/*     */   }
/*     */ 
/*     */   public abstract String getOutputFormat();
/*     */ 
/*     */   public void dispose()
/*     */   {
/*  99 */     this.disposed = true;
/*     */   }
/*     */ 
/*     */   public boolean isDisposed()
/*     */   {
/* 111 */     return this.disposed;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.StreamPrintService
 * JD-Core Version:    0.6.2
 */