/*     */ package sun.net.smtp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ class SmtpPrintStream extends PrintStream
/*     */ {
/*     */   private SmtpClient target;
/* 215 */   private int lastc = 10;
/*     */ 
/*     */   SmtpPrintStream(OutputStream paramOutputStream, SmtpClient paramSmtpClient) throws UnsupportedEncodingException {
/* 218 */     super(paramOutputStream, false, paramSmtpClient.getEncoding());
/* 219 */     this.target = paramSmtpClient;
/*     */   }
/*     */ 
/*     */   public void close() {
/* 223 */     if (this.target == null)
/* 224 */       return;
/* 225 */     if (this.lastc != 10)
/* 226 */       write(10);
/*     */     try
/*     */     {
/* 229 */       this.target.issueCommand(".\r\n", 250);
/* 230 */       this.target.message = null;
/* 231 */       this.out = null;
/* 232 */       this.target = null;
/*     */     }
/*     */     catch (IOException localIOException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(int paramInt) {
/*     */     try {
/* 240 */       if ((this.lastc == 10) && (paramInt == 46)) {
/* 241 */         this.out.write(46);
/*     */       }
/*     */ 
/* 245 */       if ((paramInt == 10) && (this.lastc != 13)) {
/* 246 */         this.out.write(13);
/*     */       }
/* 248 */       this.out.write(paramInt);
/* 249 */       this.lastc = paramInt;
/*     */     } catch (IOException localIOException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/*     */     try {
/* 256 */       int i = this.lastc;
/*     */       while (true) { paramInt2--; if (paramInt2 < 0) break;
/* 258 */         int j = paramArrayOfByte[(paramInt1++)];
/*     */ 
/* 261 */         if ((i == 10) && (j == 46)) {
/* 262 */           this.out.write(46);
/*     */         }
/*     */ 
/* 265 */         if ((j == 10) && (i != 13)) {
/* 266 */           this.out.write(13);
/*     */         }
/* 268 */         this.out.write(j);
/* 269 */         i = j;
/*     */       }
/* 271 */       this.lastc = i; } catch (IOException localIOException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void print(String paramString) {
/* 276 */     int i = paramString.length();
/* 277 */     for (int j = 0; j < i; j++)
/* 278 */       write(paramString.charAt(j));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.smtp.SmtpPrintStream
 * JD-Core Version:    0.6.2
 */