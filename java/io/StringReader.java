/*     */ package java.io;
/*     */ 
/*     */ public class StringReader extends Reader
/*     */ {
/*     */   private String str;
/*     */   private int length;
/*  40 */   private int next = 0;
/*  41 */   private int mark = 0;
/*     */ 
/*     */   public StringReader(String paramString)
/*     */   {
/*  49 */     this.str = paramString;
/*  50 */     this.length = paramString.length();
/*     */   }
/*     */ 
/*     */   private void ensureOpen() throws IOException
/*     */   {
/*  55 */     if (this.str == null)
/*  56 */       throw new IOException("Stream closed");
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  68 */     synchronized (this.lock) {
/*  69 */       ensureOpen();
/*  70 */       if (this.next >= this.length)
/*  71 */         return -1;
/*  72 */       return this.str.charAt(this.next++);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  89 */     synchronized (this.lock) {
/*  90 */       ensureOpen();
/*  91 */       if ((paramInt1 < 0) || (paramInt1 > paramArrayOfChar.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfChar.length) || (paramInt1 + paramInt2 < 0))
/*     */       {
/*  93 */         throw new IndexOutOfBoundsException();
/*  94 */       }if (paramInt2 == 0) {
/*  95 */         return 0;
/*     */       }
/*  97 */       if (this.next >= this.length)
/*  98 */         return -1;
/*  99 */       int i = Math.min(this.length - this.next, paramInt2);
/* 100 */       this.str.getChars(this.next, this.next + i, paramArrayOfChar, paramInt1);
/* 101 */       this.next += i;
/* 102 */       return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 123 */     synchronized (this.lock) {
/* 124 */       ensureOpen();
/* 125 */       if (this.next >= this.length) {
/* 126 */         return 0L;
/*     */       }
/* 128 */       long l = Math.min(this.length - this.next, paramLong);
/* 129 */       l = Math.max(-this.next, l);
/* 130 */       this.next = ((int)(this.next + l));
/* 131 */       return l;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean ready()
/*     */     throws IOException
/*     */   {
/* 143 */     synchronized (this.lock) {
/* 144 */       ensureOpen();
/* 145 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 153 */     return true;
/*     */   }
/*     */ 
/*     */   public void mark(int paramInt)
/*     */     throws IOException
/*     */   {
/* 170 */     if (paramInt < 0) {
/* 171 */       throw new IllegalArgumentException("Read-ahead limit < 0");
/*     */     }
/* 173 */     synchronized (this.lock) {
/* 174 */       ensureOpen();
/* 175 */       this.mark = this.next;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 186 */     synchronized (this.lock) {
/* 187 */       ensureOpen();
/* 188 */       this.next = this.mark;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 199 */     this.str = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.StringReader
 * JD-Core Version:    0.6.2
 */