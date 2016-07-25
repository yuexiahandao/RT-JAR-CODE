/*     */ package java.io;
/*     */ 
/*     */ @Deprecated
/*     */ public class LineNumberInputStream extends FilterInputStream
/*     */ {
/*  52 */   int pushBack = -1;
/*     */   int lineNumber;
/*     */   int markLineNumber;
/*  55 */   int markPushBack = -1;
/*     */ 
/*     */   public LineNumberInputStream(InputStream paramInputStream)
/*     */   {
/*  64 */     super(paramInputStream);
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  91 */     int i = this.pushBack;
/*     */ 
/*  93 */     if (i != -1)
/*  94 */       this.pushBack = -1;
/*     */     else {
/*  96 */       i = this.in.read();
/*     */     }
/*     */ 
/*  99 */     switch (i) {
/*     */     case 13:
/* 101 */       this.pushBack = this.in.read();
/* 102 */       if (this.pushBack == 10) {
/* 103 */         this.pushBack = -1;
/*     */       }
/*     */     case 10:
/* 106 */       this.lineNumber += 1;
/* 107 */       return 10;
/*     */     }
/* 109 */     return i;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 130 */     if (paramArrayOfByte == null)
/* 131 */       throw new NullPointerException();
/* 132 */     if ((paramInt1 < 0) || (paramInt1 > paramArrayOfByte.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 < 0))
/*     */     {
/* 134 */       throw new IndexOutOfBoundsException();
/* 135 */     }if (paramInt2 == 0) {
/* 136 */       return 0;
/*     */     }
/*     */ 
/* 139 */     int i = read();
/* 140 */     if (i == -1) {
/* 141 */       return -1;
/*     */     }
/* 143 */     paramArrayOfByte[paramInt1] = ((byte)i);
/*     */ 
/* 145 */     int j = 1;
/*     */     try {
/* 147 */       for (; j < paramInt2; j++) {
/* 148 */         i = read();
/* 149 */         if (i == -1) {
/*     */           break;
/*     */         }
/* 152 */         if (paramArrayOfByte != null)
/* 153 */           paramArrayOfByte[(paramInt1 + j)] = ((byte)i);
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException) {
/*     */     }
/* 158 */     return j;
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 179 */     int i = 2048;
/* 180 */     long l = paramLong;
/*     */ 
/* 184 */     if (paramLong <= 0L) {
/* 185 */       return 0L;
/*     */     }
/*     */ 
/* 188 */     byte[] arrayOfByte = new byte[i];
/* 189 */     while (l > 0L) {
/* 190 */       int j = read(arrayOfByte, 0, (int)Math.min(i, l));
/* 191 */       if (j < 0) {
/*     */         break;
/*     */       }
/* 194 */       l -= j;
/*     */     }
/*     */ 
/* 197 */     return paramLong - l;
/*     */   }
/*     */ 
/*     */   public void setLineNumber(int paramInt)
/*     */   {
/* 207 */     this.lineNumber = paramInt;
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 217 */     return this.lineNumber;
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 240 */     return this.pushBack == -1 ? super.available() / 2 : super.available() / 2 + 1;
/*     */   }
/*     */ 
/*     */   public void mark(int paramInt)
/*     */   {
/* 259 */     this.markLineNumber = this.lineNumber;
/* 260 */     this.markPushBack = this.pushBack;
/* 261 */     this.in.mark(paramInt);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 288 */     this.lineNumber = this.markLineNumber;
/* 289 */     this.pushBack = this.markPushBack;
/* 290 */     this.in.reset();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.LineNumberInputStream
 * JD-Core Version:    0.6.2
 */