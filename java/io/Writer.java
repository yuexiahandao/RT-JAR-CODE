/*     */ package java.io;
/*     */ 
/*     */ public abstract class Writer
/*     */   implements Appendable, Closeable, Flushable
/*     */ {
/*     */   private char[] writeBuffer;
/*  60 */   private final int writeBufferSize = 1024;
/*     */   protected Object lock;
/*     */ 
/*     */   protected Writer()
/*     */   {
/*  76 */     this.lock = this;
/*     */   }
/*     */ 
/*     */   protected Writer(Object paramObject)
/*     */   {
/*  87 */     if (paramObject == null) {
/*  88 */       throw new NullPointerException();
/*     */     }
/*  90 */     this.lock = paramObject;
/*     */   }
/*     */ 
/*     */   public void write(int paramInt)
/*     */     throws IOException
/*     */   {
/* 108 */     synchronized (this.lock) {
/* 109 */       if (this.writeBuffer == null) {
/* 110 */         this.writeBuffer = new char[1024];
/*     */       }
/* 112 */       this.writeBuffer[0] = ((char)paramInt);
/* 113 */       write(this.writeBuffer, 0, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(char[] paramArrayOfChar)
/*     */     throws IOException
/*     */   {
/* 127 */     write(paramArrayOfChar, 0, paramArrayOfChar.length);
/*     */   }
/*     */ 
/*     */   public abstract void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   public void write(String paramString)
/*     */     throws IOException
/*     */   {
/* 157 */     write(paramString, 0, paramString.length());
/*     */   }
/*     */ 
/*     */   public void write(String paramString, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 181 */     synchronized (this.lock)
/*     */     {
/*     */       char[] arrayOfChar;
/* 183 */       if (paramInt2 <= 1024) {
/* 184 */         if (this.writeBuffer == null) {
/* 185 */           this.writeBuffer = new char[1024];
/*     */         }
/* 187 */         arrayOfChar = this.writeBuffer;
/*     */       } else {
/* 189 */         arrayOfChar = new char[paramInt2];
/*     */       }
/* 191 */       paramString.getChars(paramInt1, paramInt1 + paramInt2, arrayOfChar, 0);
/* 192 */       write(arrayOfChar, 0, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Writer append(CharSequence paramCharSequence)
/*     */     throws IOException
/*     */   {
/* 224 */     if (paramCharSequence == null)
/* 225 */       write("null");
/*     */     else
/* 227 */       write(paramCharSequence.toString());
/* 228 */     return this;
/*     */   }
/*     */ 
/*     */   public Writer append(CharSequence paramCharSequence, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 268 */     CharSequence localCharSequence = paramCharSequence == null ? "null" : paramCharSequence;
/* 269 */     write(localCharSequence.subSequence(paramInt1, paramInt2).toString());
/* 270 */     return this;
/*     */   }
/*     */ 
/*     */   public Writer append(char paramChar)
/*     */     throws IOException
/*     */   {
/* 293 */     write(paramChar);
/* 294 */     return this;
/*     */   }
/*     */ 
/*     */   public abstract void flush()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract void close()
/*     */     throws IOException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.Writer
 * JD-Core Version:    0.6.2
 */