/*     */ package sun.net.www.http;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class PosterOutputStream extends ByteArrayOutputStream
/*     */ {
/*     */   private boolean closed;
/*     */ 
/*     */   public PosterOutputStream()
/*     */   {
/*  51 */     super(256);
/*     */   }
/*     */ 
/*     */   public synchronized void write(int paramInt)
/*     */   {
/*  60 */     if (this.closed) {
/*  61 */       return;
/*     */     }
/*  63 */     super.write(paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*  75 */     if (this.closed) {
/*  76 */       return;
/*     */     }
/*  78 */     super.write(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public synchronized void reset()
/*     */   {
/*  91 */     if (this.closed) {
/*  92 */       return;
/*     */     }
/*  94 */     super.reset();
/*     */   }
/*     */ 
/*     */   public synchronized void close()
/*     */     throws IOException
/*     */   {
/* 102 */     this.closed = true;
/* 103 */     super.close();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.http.PosterOutputStream
 * JD-Core Version:    0.6.2
 */