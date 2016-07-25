/*     */ package java.io;
/*     */ 
/*     */ public class CharArrayReader extends Reader
/*     */ {
/*     */   protected char[] buf;
/*     */   protected int pos;
/*  43 */   protected int markedPos = 0;
/*     */   protected int count;
/*     */ 
/*     */   public CharArrayReader(char[] paramArrayOfChar)
/*     */   {
/*  56 */     this.buf = paramArrayOfChar;
/*  57 */     this.pos = 0;
/*  58 */     this.count = paramArrayOfChar.length;
/*     */   }
/*     */ 
/*     */   public CharArrayReader(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/*  79 */     if ((paramInt1 < 0) || (paramInt1 > paramArrayOfChar.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 < 0))
/*     */     {
/*  81 */       throw new IllegalArgumentException();
/*     */     }
/*  83 */     this.buf = paramArrayOfChar;
/*  84 */     this.pos = paramInt1;
/*  85 */     this.count = Math.min(paramInt1 + paramInt2, paramArrayOfChar.length);
/*  86 */     this.markedPos = paramInt1;
/*     */   }
/*     */ 
/*     */   private void ensureOpen() throws IOException
/*     */   {
/*  91 */     if (this.buf == null)
/*  92 */       throw new IOException("Stream closed");
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 101 */     synchronized (this.lock) {
/* 102 */       ensureOpen();
/* 103 */       if (this.pos >= this.count) {
/* 104 */         return -1;
/*     */       }
/* 106 */       return this.buf[(this.pos++)];
/*     */     }
/*     */   }
/*     */ 
/*     */   public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 121 */     synchronized (this.lock) {
/* 122 */       ensureOpen();
/* 123 */       if ((paramInt1 < 0) || (paramInt1 > paramArrayOfChar.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfChar.length) || (paramInt1 + paramInt2 < 0))
/*     */       {
/* 125 */         throw new IndexOutOfBoundsException();
/* 126 */       }if (paramInt2 == 0) {
/* 127 */         return 0;
/*     */       }
/*     */ 
/* 130 */       if (this.pos >= this.count) {
/* 131 */         return -1;
/*     */       }
/* 133 */       if (this.pos + paramInt2 > this.count) {
/* 134 */         paramInt2 = this.count - this.pos;
/*     */       }
/* 136 */       if (paramInt2 <= 0) {
/* 137 */         return 0;
/*     */       }
/* 139 */       System.arraycopy(this.buf, this.pos, paramArrayOfChar, paramInt1, paramInt2);
/* 140 */       this.pos += paramInt2;
/* 141 */       return paramInt2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 158 */     synchronized (this.lock) {
/* 159 */       ensureOpen();
/* 160 */       if (this.pos + paramLong > this.count) {
/* 161 */         paramLong = this.count - this.pos;
/*     */       }
/* 163 */       if (paramLong < 0L) {
/* 164 */         return 0L;
/*     */       }
/* 166 */       this.pos = ((int)(this.pos + paramLong));
/* 167 */       return paramLong;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean ready()
/*     */     throws IOException
/*     */   {
/* 178 */     synchronized (this.lock) {
/* 179 */       ensureOpen();
/* 180 */       return this.count - this.pos > 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 188 */     return true;
/*     */   }
/*     */ 
/*     */   public void mark(int paramInt)
/*     */     throws IOException
/*     */   {
/* 204 */     synchronized (this.lock) {
/* 205 */       ensureOpen();
/* 206 */       this.markedPos = this.pos;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 217 */     synchronized (this.lock) {
/* 218 */       ensureOpen();
/* 219 */       this.pos = this.markedPos;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 230 */     this.buf = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.CharArrayReader
 * JD-Core Version:    0.6.2
 */