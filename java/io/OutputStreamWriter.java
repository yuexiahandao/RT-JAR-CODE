/*     */ package java.io;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import sun.nio.cs.StreamEncoder;
/*     */ 
/*     */ public class OutputStreamWriter extends Writer
/*     */ {
/*     */   private final StreamEncoder se;
/*     */ 
/*     */   public OutputStreamWriter(OutputStream paramOutputStream, String paramString)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*  97 */     super(paramOutputStream);
/*  98 */     if (paramString == null)
/*  99 */       throw new NullPointerException("charsetName");
/* 100 */     this.se = StreamEncoder.forOutputStreamWriter(paramOutputStream, this, paramString);
/*     */   }
/*     */ 
/*     */   public OutputStreamWriter(OutputStream paramOutputStream)
/*     */   {
/* 109 */     super(paramOutputStream);
/*     */     try {
/* 111 */       this.se = StreamEncoder.forOutputStreamWriter(paramOutputStream, this, (String)null);
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 113 */       throw new Error(localUnsupportedEncodingException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public OutputStreamWriter(OutputStream paramOutputStream, Charset paramCharset)
/*     */   {
/* 130 */     super(paramOutputStream);
/* 131 */     if (paramCharset == null)
/* 132 */       throw new NullPointerException("charset");
/* 133 */     this.se = StreamEncoder.forOutputStreamWriter(paramOutputStream, this, paramCharset);
/*     */   }
/*     */ 
/*     */   public OutputStreamWriter(OutputStream paramOutputStream, CharsetEncoder paramCharsetEncoder)
/*     */   {
/* 149 */     super(paramOutputStream);
/* 150 */     if (paramCharsetEncoder == null)
/* 151 */       throw new NullPointerException("charset encoder");
/* 152 */     this.se = StreamEncoder.forOutputStreamWriter(paramOutputStream, this, paramCharsetEncoder);
/*     */   }
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 176 */     return this.se.getEncoding();
/*     */   }
/*     */ 
/*     */   void flushBuffer()
/*     */     throws IOException
/*     */   {
/* 185 */     this.se.flushBuffer();
/*     */   }
/*     */ 
/*     */   public void write(int paramInt)
/*     */     throws IOException
/*     */   {
/* 194 */     this.se.write(paramInt);
/*     */   }
/*     */ 
/*     */   public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 207 */     this.se.write(paramArrayOfChar, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void write(String paramString, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 220 */     this.se.write(paramString, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 229 */     this.se.flush();
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 233 */     this.se.close();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.OutputStreamWriter
 * JD-Core Version:    0.6.2
 */