/*     */ package java.io;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class BufferedWriter extends Writer
/*     */ {
/*     */   private Writer out;
/*     */   private char[] cb;
/*     */   private int nChars;
/*     */   private int nextChar;
/*  73 */   private static int defaultCharBufferSize = 8192;
/*     */   private String lineSeparator;
/*     */ 
/*     */   public BufferedWriter(Writer paramWriter)
/*     */   {
/*  88 */     this(paramWriter, defaultCharBufferSize);
/*     */   }
/*     */ 
/*     */   public BufferedWriter(Writer paramWriter, int paramInt)
/*     */   {
/* 101 */     super(paramWriter);
/* 102 */     if (paramInt <= 0)
/* 103 */       throw new IllegalArgumentException("Buffer size <= 0");
/* 104 */     this.out = paramWriter;
/* 105 */     this.cb = new char[paramInt];
/* 106 */     this.nChars = paramInt;
/* 107 */     this.nextChar = 0;
/*     */ 
/* 109 */     this.lineSeparator = ((String)AccessController.doPrivileged(new GetPropertyAction("line.separator")));
/*     */   }
/*     */ 
/*     */   private void ensureOpen()
/*     */     throws IOException
/*     */   {
/* 115 */     if (this.out == null)
/* 116 */       throw new IOException("Stream closed");
/*     */   }
/*     */ 
/*     */   void flushBuffer()
/*     */     throws IOException
/*     */   {
/* 125 */     synchronized (this.lock) {
/* 126 */       ensureOpen();
/* 127 */       if (this.nextChar == 0)
/* 128 */         return;
/* 129 */       this.out.write(this.cb, 0, this.nextChar);
/* 130 */       this.nextChar = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(int paramInt)
/*     */     throws IOException
/*     */   {
/* 140 */     synchronized (this.lock) {
/* 141 */       ensureOpen();
/* 142 */       if (this.nextChar >= this.nChars)
/* 143 */         flushBuffer();
/* 144 */       this.cb[(this.nextChar++)] = ((char)paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   private int min(int paramInt1, int paramInt2)
/*     */   {
/* 153 */     if (paramInt1 < paramInt2) return paramInt1;
/* 154 */     return paramInt2;
/*     */   }
/*     */ 
/*     */   public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 174 */     synchronized (this.lock) {
/* 175 */       ensureOpen();
/* 176 */       if ((paramInt1 < 0) || (paramInt1 > paramArrayOfChar.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfChar.length) || (paramInt1 + paramInt2 < 0))
/*     */       {
/* 178 */         throw new IndexOutOfBoundsException();
/* 179 */       }if (paramInt2 == 0) {
/* 180 */         return;
/*     */       }
/*     */ 
/* 183 */       if (paramInt2 >= this.nChars)
/*     */       {
/* 187 */         flushBuffer();
/* 188 */         this.out.write(paramArrayOfChar, paramInt1, paramInt2);
/* 189 */         return;
/*     */       }
/*     */ 
/* 192 */       int i = paramInt1; int j = paramInt1 + paramInt2;
/* 193 */       while (i < j) {
/* 194 */         int k = min(this.nChars - this.nextChar, j - i);
/* 195 */         System.arraycopy(paramArrayOfChar, i, this.cb, this.nextChar, k);
/* 196 */         i += k;
/* 197 */         this.nextChar += k;
/* 198 */         if (this.nextChar >= this.nChars)
/* 199 */           flushBuffer();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(String paramString, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 220 */     synchronized (this.lock) {
/* 221 */       ensureOpen();
/*     */ 
/* 223 */       int i = paramInt1; int j = paramInt1 + paramInt2;
/* 224 */       while (i < j) {
/* 225 */         int k = min(this.nChars - this.nextChar, j - i);
/* 226 */         paramString.getChars(i, i + k, this.cb, this.nextChar);
/* 227 */         i += k;
/* 228 */         this.nextChar += k;
/* 229 */         if (this.nextChar >= this.nChars)
/* 230 */           flushBuffer();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void newLine()
/*     */     throws IOException
/*     */   {
/* 243 */     write(this.lineSeparator);
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 252 */     synchronized (this.lock) {
/* 253 */       flushBuffer();
/* 254 */       this.out.flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 259 */     synchronized (this.lock) {
/* 260 */       if (this.out == null)
/* 261 */         return;
/*     */       try
/*     */       {
/* 264 */         flushBuffer();
/*     */       } finally {
/* 266 */         this.out.close();
/* 267 */         this.out = null;
/* 268 */         this.cb = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.BufferedWriter
 * JD-Core Version:    0.6.2
 */