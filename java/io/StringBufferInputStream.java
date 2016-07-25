/*     */ package java.io;
/*     */ 
/*     */ @Deprecated
/*     */ public class StringBufferInputStream extends InputStream
/*     */ {
/*     */   protected String buffer;
/*     */   protected int pos;
/*     */   protected int count;
/*     */ 
/*     */   public StringBufferInputStream(String paramString)
/*     */   {
/*  73 */     this.buffer = paramString;
/*  74 */     this.count = paramString.length();
/*     */   }
/*     */ 
/*     */   public synchronized int read()
/*     */   {
/*  92 */     return this.pos < this.count ? this.buffer.charAt(this.pos++) & 0xFF : -1;
/*     */   }
/*     */ 
/*     */   public synchronized int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 112 */     if (paramArrayOfByte == null)
/* 113 */       throw new NullPointerException();
/* 114 */     if ((paramInt1 < 0) || (paramInt1 > paramArrayOfByte.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 < 0))
/*     */     {
/* 116 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 118 */     if (this.pos >= this.count) {
/* 119 */       return -1;
/*     */     }
/* 121 */     if (this.pos + paramInt2 > this.count) {
/* 122 */       paramInt2 = this.count - this.pos;
/*     */     }
/* 124 */     if (paramInt2 <= 0) {
/* 125 */       return 0;
/*     */     }
/* 127 */     String str = this.buffer;
/* 128 */     int i = paramInt2;
/*     */     while (true) { i--; if (i < 0) break;
/* 130 */       paramArrayOfByte[(paramInt1++)] = ((byte)str.charAt(this.pos++));
/*     */     }
/*     */ 
/* 133 */     return paramInt2;
/*     */   }
/*     */ 
/*     */   public synchronized long skip(long paramLong)
/*     */   {
/* 144 */     if (paramLong < 0L) {
/* 145 */       return 0L;
/*     */     }
/* 147 */     if (paramLong > this.count - this.pos) {
/* 148 */       paramLong = this.count - this.pos;
/*     */     }
/* 150 */     this.pos = ((int)(this.pos + paramLong));
/* 151 */     return paramLong;
/*     */   }
/*     */ 
/*     */   public synchronized int available()
/*     */   {
/* 162 */     return this.count - this.pos;
/*     */   }
/*     */ 
/*     */   public synchronized void reset()
/*     */   {
/* 170 */     this.pos = 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.StringBufferInputStream
 * JD-Core Version:    0.6.2
 */