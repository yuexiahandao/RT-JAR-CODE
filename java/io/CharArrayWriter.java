/*     */ package java.io;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class CharArrayWriter extends Writer
/*     */ {
/*     */   protected char[] buf;
/*     */   protected int count;
/*     */ 
/*     */   public CharArrayWriter()
/*     */   {
/*  58 */     this(32);
/*     */   }
/*     */ 
/*     */   public CharArrayWriter(int paramInt)
/*     */   {
/*  68 */     if (paramInt < 0) {
/*  69 */       throw new IllegalArgumentException("Negative initial size: " + paramInt);
/*     */     }
/*     */ 
/*  72 */     this.buf = new char[paramInt];
/*     */   }
/*     */ 
/*     */   public void write(int paramInt)
/*     */   {
/*  79 */     synchronized (this.lock) {
/*  80 */       int i = this.count + 1;
/*  81 */       if (i > this.buf.length) {
/*  82 */         this.buf = Arrays.copyOf(this.buf, Math.max(this.buf.length << 1, i));
/*     */       }
/*  84 */       this.buf[this.count] = ((char)paramInt);
/*  85 */       this.count = i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/*  96 */     if ((paramInt1 < 0) || (paramInt1 > paramArrayOfChar.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfChar.length) || (paramInt1 + paramInt2 < 0))
/*     */     {
/*  98 */       throw new IndexOutOfBoundsException();
/*  99 */     }if (paramInt2 == 0) {
/* 100 */       return;
/*     */     }
/* 102 */     synchronized (this.lock) {
/* 103 */       int i = this.count + paramInt2;
/* 104 */       if (i > this.buf.length) {
/* 105 */         this.buf = Arrays.copyOf(this.buf, Math.max(this.buf.length << 1, i));
/*     */       }
/* 107 */       System.arraycopy(paramArrayOfChar, paramInt1, this.buf, this.count, paramInt2);
/* 108 */       this.count = i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 119 */     synchronized (this.lock) {
/* 120 */       int i = this.count + paramInt2;
/* 121 */       if (i > this.buf.length) {
/* 122 */         this.buf = Arrays.copyOf(this.buf, Math.max(this.buf.length << 1, i));
/*     */       }
/* 124 */       paramString.getChars(paramInt1, paramInt1 + paramInt2, this.buf, this.count);
/* 125 */       this.count = i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTo(Writer paramWriter)
/*     */     throws IOException
/*     */   {
/* 136 */     synchronized (this.lock) {
/* 137 */       paramWriter.write(this.buf, 0, this.count);
/*     */     }
/*     */   }
/*     */ 
/*     */   public CharArrayWriter append(CharSequence paramCharSequence)
/*     */   {
/* 166 */     String str = paramCharSequence == null ? "null" : paramCharSequence.toString();
/* 167 */     write(str, 0, str.length());
/* 168 */     return this;
/*     */   }
/*     */ 
/*     */   public CharArrayWriter append(CharSequence paramCharSequence, int paramInt1, int paramInt2)
/*     */   {
/* 204 */     String str = (paramCharSequence == null ? "null" : paramCharSequence).subSequence(paramInt1, paramInt2).toString();
/* 205 */     write(str, 0, str.length());
/* 206 */     return this;
/*     */   }
/*     */ 
/*     */   public CharArrayWriter append(char paramChar)
/*     */   {
/* 226 */     write(paramChar);
/* 227 */     return this;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 235 */     this.count = 0;
/*     */   }
/*     */ 
/*     */   public char[] toCharArray()
/*     */   {
/* 244 */     synchronized (this.lock) {
/* 245 */       return Arrays.copyOf(this.buf, this.count);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 255 */     return this.count;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 263 */     synchronized (this.lock) {
/* 264 */       return new String(this.buf, 0, this.count);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.CharArrayWriter
 * JD-Core Version:    0.6.2
 */