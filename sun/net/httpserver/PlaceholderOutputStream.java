/*     */ package sun.net.httpserver;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ class PlaceholderOutputStream extends OutputStream
/*     */ {
/*     */   OutputStream wrapped;
/*     */ 
/*     */   PlaceholderOutputStream(OutputStream paramOutputStream)
/*     */   {
/* 415 */     this.wrapped = paramOutputStream;
/*     */   }
/*     */ 
/*     */   void setWrappedStream(OutputStream paramOutputStream) {
/* 419 */     this.wrapped = paramOutputStream;
/*     */   }
/*     */ 
/*     */   boolean isWrapped() {
/* 423 */     return this.wrapped != null;
/*     */   }
/*     */ 
/*     */   private void checkWrap() throws IOException {
/* 427 */     if (this.wrapped == null)
/* 428 */       throw new IOException("response headers not sent yet");
/*     */   }
/*     */ 
/*     */   public void write(int paramInt) throws IOException
/*     */   {
/* 433 */     checkWrap();
/* 434 */     this.wrapped.write(paramInt);
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte) throws IOException {
/* 438 */     checkWrap();
/* 439 */     this.wrapped.write(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 443 */     checkWrap();
/* 444 */     this.wrapped.write(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void flush() throws IOException {
/* 448 */     checkWrap();
/* 449 */     this.wrapped.flush();
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 453 */     checkWrap();
/* 454 */     this.wrapped.close();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.PlaceholderOutputStream
 * JD-Core Version:    0.6.2
 */