/*     */ package java.io;
/*     */ 
/*     */ public class LineNumberReader extends BufferedReader
/*     */ {
/*  53 */   private int lineNumber = 0;
/*     */   private int markedLineNumber;
/*     */   private boolean skipLF;
/*     */   private boolean markedSkipLF;
/*     */   private static final int maxSkipBufferSize = 8192;
/* 211 */   private char[] skipBuffer = null;
/*     */ 
/*     */   public LineNumberReader(Reader paramReader)
/*     */   {
/*  72 */     super(paramReader);
/*     */   }
/*     */ 
/*     */   public LineNumberReader(Reader paramReader, int paramInt)
/*     */   {
/*  86 */     super(paramReader, paramInt);
/*     */   }
/*     */ 
/*     */   public void setLineNumber(int paramInt)
/*     */   {
/*  98 */     this.lineNumber = paramInt;
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 109 */     return this.lineNumber;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 124 */     synchronized (this.lock) {
/* 125 */       int i = super.read();
/* 126 */       if (this.skipLF) {
/* 127 */         if (i == 10)
/* 128 */           i = super.read();
/* 129 */         this.skipLF = false;
/*     */       }
/* 131 */       switch (i) {
/*     */       case 13:
/* 133 */         this.skipLF = true;
/*     */       case 10:
/* 135 */         this.lineNumber += 1;
/* 136 */         return 10;
/*     */       }
/* 138 */       return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 163 */     synchronized (this.lock) {
/* 164 */       int i = super.read(paramArrayOfChar, paramInt1, paramInt2);
/*     */ 
/* 166 */       for (int j = paramInt1; j < paramInt1 + i; j++) {
/* 167 */         int k = paramArrayOfChar[j];
/* 168 */         if (this.skipLF) { this.skipLF = false;
/* 170 */           if (k == 10);
/*     */         }
/*     */         else {
/* 173 */           switch (k) {
/*     */           case 13:
/* 175 */             this.skipLF = true;
/*     */           case 10:
/* 177 */             this.lineNumber += 1;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 182 */       return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String readLine()
/*     */     throws IOException
/*     */   {
/* 198 */     synchronized (this.lock) {
/* 199 */       String str = super.readLine(this.skipLF);
/* 200 */       this.skipLF = false;
/* 201 */       if (str != null)
/* 202 */         this.lineNumber += 1;
/* 203 */       return str;
/*     */     }
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 228 */     if (paramLong < 0L)
/* 229 */       throw new IllegalArgumentException("skip() value is negative");
/* 230 */     int i = (int)Math.min(paramLong, 8192L);
/* 231 */     synchronized (this.lock) {
/* 232 */       if ((this.skipBuffer == null) || (this.skipBuffer.length < i))
/* 233 */         this.skipBuffer = new char[i];
/* 234 */       long l = paramLong;
/* 235 */       while (l > 0L) {
/* 236 */         int j = read(this.skipBuffer, 0, (int)Math.min(l, i));
/* 237 */         if (j == -1)
/*     */           break;
/* 239 */         l -= j;
/*     */       }
/* 241 */       return paramLong - l;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mark(int paramInt)
/*     */     throws IOException
/*     */   {
/* 259 */     synchronized (this.lock) {
/* 260 */       super.mark(paramInt);
/* 261 */       this.markedLineNumber = this.lineNumber;
/* 262 */       this.markedSkipLF = this.skipLF;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 274 */     synchronized (this.lock) {
/* 275 */       super.reset();
/* 276 */       this.lineNumber = this.markedLineNumber;
/* 277 */       this.skipLF = this.markedSkipLF;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.LineNumberReader
 * JD-Core Version:    0.6.2
 */