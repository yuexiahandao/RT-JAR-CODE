/*     */ package java.io;
/*     */ 
/*     */ public class PushbackReader extends FilterReader
/*     */ {
/*     */   private char[] buf;
/*     */   private int pos;
/*     */ 
/*     */   public PushbackReader(Reader paramReader, int paramInt)
/*     */   {
/*  53 */     super(paramReader);
/*  54 */     if (paramInt <= 0) {
/*  55 */       throw new IllegalArgumentException("size <= 0");
/*     */     }
/*  57 */     this.buf = new char[paramInt];
/*  58 */     this.pos = paramInt;
/*     */   }
/*     */ 
/*     */   public PushbackReader(Reader paramReader)
/*     */   {
/*  67 */     this(paramReader, 1);
/*     */   }
/*     */ 
/*     */   private void ensureOpen() throws IOException
/*     */   {
/*  72 */     if (this.buf == null)
/*  73 */       throw new IOException("Stream closed");
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  85 */     synchronized (this.lock) {
/*  86 */       ensureOpen();
/*  87 */       if (this.pos < this.buf.length) {
/*  88 */         return this.buf[(this.pos++)];
/*     */       }
/*  90 */       return super.read();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 107 */     synchronized (this.lock) {
/* 108 */       ensureOpen();
/*     */       try {
/* 110 */         if (paramInt2 <= 0) {
/* 111 */           if (paramInt2 < 0)
/* 112 */             throw new IndexOutOfBoundsException();
/* 113 */           if ((paramInt1 < 0) || (paramInt1 > paramArrayOfChar.length)) {
/* 114 */             throw new IndexOutOfBoundsException();
/*     */           }
/* 116 */           return 0;
/*     */         }
/* 118 */         int i = this.buf.length - this.pos;
/* 119 */         if (i > 0) {
/* 120 */           if (paramInt2 < i)
/* 121 */             i = paramInt2;
/* 122 */           System.arraycopy(this.buf, this.pos, paramArrayOfChar, paramInt1, i);
/* 123 */           this.pos += i;
/* 124 */           paramInt1 += i;
/* 125 */           paramInt2 -= i;
/*     */         }
/* 127 */         if (paramInt2 > 0) {
/* 128 */           paramInt2 = super.read(paramArrayOfChar, paramInt1, paramInt2);
/* 129 */           if (paramInt2 == -1) {
/* 130 */             return i == 0 ? -1 : i;
/*     */           }
/* 132 */           return i + paramInt2;
/*     */         }
/* 134 */         return i;
/*     */       } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 136 */         throw new IndexOutOfBoundsException();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unread(int paramInt)
/*     */     throws IOException
/*     */   {
/* 152 */     synchronized (this.lock) {
/* 153 */       ensureOpen();
/* 154 */       if (this.pos == 0)
/* 155 */         throw new IOException("Pushback buffer overflow");
/* 156 */       this.buf[(--this.pos)] = ((char)paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unread(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 175 */     synchronized (this.lock) {
/* 176 */       ensureOpen();
/* 177 */       if (paramInt2 > this.pos)
/* 178 */         throw new IOException("Pushback buffer overflow");
/* 179 */       this.pos -= paramInt2;
/* 180 */       System.arraycopy(paramArrayOfChar, paramInt1, this.buf, this.pos, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unread(char[] paramArrayOfChar)
/*     */     throws IOException
/*     */   {
/* 196 */     unread(paramArrayOfChar, 0, paramArrayOfChar.length);
/*     */   }
/*     */ 
/*     */   public boolean ready()
/*     */     throws IOException
/*     */   {
/* 205 */     synchronized (this.lock) {
/* 206 */       ensureOpen();
/* 207 */       return (this.pos < this.buf.length) || (super.ready());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mark(int paramInt)
/*     */     throws IOException
/*     */   {
/* 218 */     throw new IOException("mark/reset not supported");
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 228 */     throw new IOException("mark/reset not supported");
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 236 */     return false;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 248 */     super.close();
/* 249 */     this.buf = null;
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 264 */     if (paramLong < 0L)
/* 265 */       throw new IllegalArgumentException("skip value is negative");
/* 266 */     synchronized (this.lock) {
/* 267 */       ensureOpen();
/* 268 */       int i = this.buf.length - this.pos;
/* 269 */       if (i > 0) {
/* 270 */         if (paramLong <= i) {
/* 271 */           this.pos = ((int)(this.pos + paramLong));
/* 272 */           return paramLong;
/*     */         }
/* 274 */         this.pos = this.buf.length;
/* 275 */         paramLong -= i;
/*     */       }
/*     */ 
/* 278 */       return i + super.skip(paramLong);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.PushbackReader
 * JD-Core Version:    0.6.2
 */