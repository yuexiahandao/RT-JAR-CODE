/*     */ package com.sun.jmx.snmp.IPAcl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ 
/*     */ final class ASCII_CharStream
/*     */ {
/*     */   public static final boolean staticFlag = false;
/*     */   int bufsize;
/*     */   int available;
/*     */   int tokenBegin;
/*  40 */   public int bufpos = -1;
/*     */   private int[] bufline;
/*     */   private int[] bufcolumn;
/*  44 */   private int column = 0;
/*  45 */   private int line = 1;
/*     */ 
/*  47 */   private boolean prevCharIsCR = false;
/*  48 */   private boolean prevCharIsLF = false;
/*     */   private Reader inputStream;
/*     */   private char[] buffer;
/*  53 */   private int maxNextCharInd = 0;
/*  54 */   private int inBuf = 0;
/*     */ 
/*     */   private final void ExpandBuff(boolean paramBoolean)
/*     */   {
/*  58 */     char[] arrayOfChar = new char[this.bufsize + 2048];
/*  59 */     int[] arrayOfInt1 = new int[this.bufsize + 2048];
/*  60 */     int[] arrayOfInt2 = new int[this.bufsize + 2048];
/*     */     try
/*     */     {
/*  64 */       if (paramBoolean)
/*     */       {
/*  66 */         System.arraycopy(this.buffer, this.tokenBegin, arrayOfChar, 0, this.bufsize - this.tokenBegin);
/*  67 */         System.arraycopy(this.buffer, 0, arrayOfChar, this.bufsize - this.tokenBegin, this.bufpos);
/*     */ 
/*  69 */         this.buffer = arrayOfChar;
/*     */ 
/*  71 */         System.arraycopy(this.bufline, this.tokenBegin, arrayOfInt1, 0, this.bufsize - this.tokenBegin);
/*  72 */         System.arraycopy(this.bufline, 0, arrayOfInt1, this.bufsize - this.tokenBegin, this.bufpos);
/*  73 */         this.bufline = arrayOfInt1;
/*     */ 
/*  75 */         System.arraycopy(this.bufcolumn, this.tokenBegin, arrayOfInt2, 0, this.bufsize - this.tokenBegin);
/*  76 */         System.arraycopy(this.bufcolumn, 0, arrayOfInt2, this.bufsize - this.tokenBegin, this.bufpos);
/*  77 */         this.bufcolumn = arrayOfInt2;
/*     */ 
/*  79 */         this.maxNextCharInd = (this.bufpos += this.bufsize - this.tokenBegin);
/*     */       }
/*     */       else
/*     */       {
/*  83 */         System.arraycopy(this.buffer, this.tokenBegin, arrayOfChar, 0, this.bufsize - this.tokenBegin);
/*  84 */         this.buffer = arrayOfChar;
/*     */ 
/*  86 */         System.arraycopy(this.bufline, this.tokenBegin, arrayOfInt1, 0, this.bufsize - this.tokenBegin);
/*  87 */         this.bufline = arrayOfInt1;
/*     */ 
/*  89 */         System.arraycopy(this.bufcolumn, this.tokenBegin, arrayOfInt2, 0, this.bufsize - this.tokenBegin);
/*  90 */         this.bufcolumn = arrayOfInt2;
/*     */ 
/*  92 */         this.maxNextCharInd = (this.bufpos -= this.tokenBegin);
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/*  97 */       throw new Error(localThrowable.getMessage());
/*     */     }
/*     */ 
/* 101 */     this.bufsize += 2048;
/* 102 */     this.available = this.bufsize;
/* 103 */     this.tokenBegin = 0;
/*     */   }
/*     */ 
/*     */   private final void FillBuff() throws IOException
/*     */   {
/* 108 */     if (this.maxNextCharInd == this.available)
/*     */     {
/* 110 */       if (this.available == this.bufsize)
/*     */       {
/* 112 */         if (this.tokenBegin > 2048)
/*     */         {
/* 114 */           this.bufpos = (this.maxNextCharInd = 0);
/* 115 */           this.available = this.tokenBegin;
/*     */         }
/* 117 */         else if (this.tokenBegin < 0) {
/* 118 */           this.bufpos = (this.maxNextCharInd = 0);
/*     */         } else {
/* 120 */           ExpandBuff(false);
/*     */         }
/* 122 */       } else if (this.available > this.tokenBegin)
/* 123 */         this.available = this.bufsize;
/* 124 */       else if (this.tokenBegin - this.available < 2048)
/* 125 */         ExpandBuff(true);
/*     */       else
/* 127 */         this.available = this.tokenBegin;
/*     */     }
/*     */     try
/*     */     {
/*     */       int i;
/* 132 */       if ((i = this.inputStream.read(this.buffer, this.maxNextCharInd, this.available - this.maxNextCharInd)) == -1)
/*     */       {
/* 135 */         this.inputStream.close();
/* 136 */         throw new IOException();
/*     */       }
/*     */ 
/* 139 */       this.maxNextCharInd += i;
/* 140 */       return;
/*     */     }
/*     */     catch (IOException localIOException) {
/* 143 */       this.bufpos -= 1;
/* 144 */       backup(0);
/* 145 */       if (this.tokenBegin == -1)
/* 146 */         this.tokenBegin = this.bufpos;
/* 147 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final char BeginToken() throws IOException
/*     */   {
/* 153 */     this.tokenBegin = -1;
/* 154 */     char c = readChar();
/* 155 */     this.tokenBegin = this.bufpos;
/*     */ 
/* 157 */     return c;
/*     */   }
/*     */ 
/*     */   private final void UpdateLineColumn(char paramChar)
/*     */   {
/* 162 */     this.column += 1;
/*     */ 
/* 164 */     if (this.prevCharIsLF)
/*     */     {
/* 166 */       this.prevCharIsLF = false;
/* 167 */       this.line += (this.column = 1);
/*     */     }
/* 169 */     else if (this.prevCharIsCR)
/*     */     {
/* 171 */       this.prevCharIsCR = false;
/* 172 */       if (paramChar == '\n')
/*     */       {
/* 174 */         this.prevCharIsLF = true;
/*     */       }
/*     */       else {
/* 177 */         this.line += (this.column = 1);
/*     */       }
/*     */     }
/* 180 */     switch (paramChar)
/*     */     {
/*     */     case '\r':
/* 183 */       this.prevCharIsCR = true;
/* 184 */       break;
/*     */     case '\n':
/* 186 */       this.prevCharIsLF = true;
/* 187 */       break;
/*     */     case '\t':
/* 189 */       this.column -= 1;
/* 190 */       this.column += 8 - (this.column & 0x7);
/* 191 */       break;
/*     */     case '\013':
/*     */     case '\f':
/*     */     }
/*     */ 
/* 196 */     this.bufline[this.bufpos] = this.line;
/* 197 */     this.bufcolumn[this.bufpos] = this.column;
/*     */   }
/*     */ 
/*     */   public final char readChar() throws IOException
/*     */   {
/* 202 */     if (this.inBuf > 0)
/*     */     {
/* 204 */       this.inBuf -= 1;
/* 205 */       return (char)(0xFF & this.buffer[(++this.bufpos)]);
/*     */     }
/*     */ 
/* 208 */     if (++this.bufpos >= this.maxNextCharInd) {
/* 209 */       FillBuff();
/*     */     }
/* 211 */     char c = (char)(0xFF & this.buffer[this.bufpos]);
/*     */ 
/* 213 */     UpdateLineColumn(c);
/* 214 */     return c;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final int getColumn()
/*     */   {
/* 223 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final int getLine()
/*     */   {
/* 232 */     return this.bufline[this.bufpos];
/*     */   }
/*     */ 
/*     */   public final int getEndColumn() {
/* 236 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */ 
/*     */   public final int getEndLine() {
/* 240 */     return this.bufline[this.bufpos];
/*     */   }
/*     */ 
/*     */   public final int getBeginColumn() {
/* 244 */     return this.bufcolumn[this.tokenBegin];
/*     */   }
/*     */ 
/*     */   public final int getBeginLine() {
/* 248 */     return this.bufline[this.tokenBegin];
/*     */   }
/*     */ 
/*     */   public final void backup(int paramInt)
/*     */   {
/* 253 */     this.inBuf += paramInt;
/* 254 */     if (this.bufpos -= paramInt < 0)
/* 255 */       this.bufpos += this.bufsize;
/*     */   }
/*     */ 
/*     */   public ASCII_CharStream(Reader paramReader, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 261 */     this.inputStream = paramReader;
/* 262 */     this.line = paramInt1;
/* 263 */     this.column = (paramInt2 - 1);
/*     */ 
/* 265 */     this.available = (this.bufsize = paramInt3);
/* 266 */     this.buffer = new char[paramInt3];
/* 267 */     this.bufline = new int[paramInt3];
/* 268 */     this.bufcolumn = new int[paramInt3];
/*     */   }
/*     */ 
/*     */   public ASCII_CharStream(Reader paramReader, int paramInt1, int paramInt2)
/*     */   {
/* 274 */     this(paramReader, paramInt1, paramInt2, 4096);
/*     */   }
/*     */ 
/*     */   public void ReInit(Reader paramReader, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 279 */     this.inputStream = paramReader;
/* 280 */     this.line = paramInt1;
/* 281 */     this.column = (paramInt2 - 1);
/*     */ 
/* 283 */     if ((this.buffer == null) || (paramInt3 != this.buffer.length))
/*     */     {
/* 285 */       this.available = (this.bufsize = paramInt3);
/* 286 */       this.buffer = new char[paramInt3];
/* 287 */       this.bufline = new int[paramInt3];
/* 288 */       this.bufcolumn = new int[paramInt3];
/*     */     }
/* 290 */     this.prevCharIsLF = (this.prevCharIsCR = 0);
/* 291 */     this.tokenBegin = (this.inBuf = this.maxNextCharInd = 0);
/* 292 */     this.bufpos = -1;
/*     */   }
/*     */ 
/*     */   public void ReInit(Reader paramReader, int paramInt1, int paramInt2)
/*     */   {
/* 298 */     ReInit(paramReader, paramInt1, paramInt2, 4096);
/*     */   }
/*     */ 
/*     */   public ASCII_CharStream(InputStream paramInputStream, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 303 */     this(new InputStreamReader(paramInputStream), paramInt1, paramInt2, 4096);
/*     */   }
/*     */ 
/*     */   public ASCII_CharStream(InputStream paramInputStream, int paramInt1, int paramInt2)
/*     */   {
/* 309 */     this(paramInputStream, paramInt1, paramInt2, 4096);
/*     */   }
/*     */ 
/*     */   public void ReInit(InputStream paramInputStream, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 315 */     ReInit(new InputStreamReader(paramInputStream), paramInt1, paramInt2, 4096);
/*     */   }
/*     */ 
/*     */   public void ReInit(InputStream paramInputStream, int paramInt1, int paramInt2)
/*     */   {
/* 320 */     ReInit(paramInputStream, paramInt1, paramInt2, 4096);
/*     */   }
/*     */ 
/*     */   public final String GetImage() {
/* 324 */     if (this.bufpos >= this.tokenBegin) {
/* 325 */       return new String(this.buffer, this.tokenBegin, this.bufpos - this.tokenBegin + 1);
/*     */     }
/* 327 */     return new String(this.buffer, this.tokenBegin, this.bufsize - this.tokenBegin) + new String(this.buffer, 0, this.bufpos + 1);
/*     */   }
/*     */ 
/*     */   public final char[] GetSuffix(int paramInt)
/*     */   {
/* 333 */     char[] arrayOfChar = new char[paramInt];
/*     */ 
/* 335 */     if (this.bufpos + 1 >= paramInt) {
/* 336 */       System.arraycopy(this.buffer, this.bufpos - paramInt + 1, arrayOfChar, 0, paramInt);
/*     */     }
/*     */     else {
/* 339 */       System.arraycopy(this.buffer, this.bufsize - (paramInt - this.bufpos - 1), arrayOfChar, 0, paramInt - this.bufpos - 1);
/*     */ 
/* 341 */       System.arraycopy(this.buffer, 0, arrayOfChar, paramInt - this.bufpos - 1, this.bufpos + 1);
/*     */     }
/*     */ 
/* 344 */     return arrayOfChar;
/*     */   }
/*     */ 
/*     */   public void Done()
/*     */   {
/* 349 */     this.buffer = null;
/* 350 */     this.bufline = null;
/* 351 */     this.bufcolumn = null;
/*     */   }
/*     */ 
/*     */   public void adjustBeginLineColumn(int paramInt1, int paramInt2)
/*     */   {
/* 359 */     int i = this.tokenBegin;
/*     */     int j;
/* 362 */     if (this.bufpos >= this.tokenBegin)
/*     */     {
/* 364 */       j = this.bufpos - this.tokenBegin + this.inBuf + 1;
/*     */     }
/*     */     else
/*     */     {
/* 368 */       j = this.bufsize - this.tokenBegin + this.bufpos + 1 + this.inBuf;
/*     */     }
/*     */ 
/* 371 */     int k = 0; int m = 0; int n = 0;
/* 372 */     int i1 = 0; int i2 = 0;
/*     */ 
/* 374 */     while ((k < j) && (this.bufline[(m = i % this.bufsize)] == this.bufline[(n = ++i % this.bufsize)]))
/*     */     {
/* 377 */       this.bufline[m] = paramInt1;
/* 378 */       i1 = i2 + this.bufcolumn[n] - this.bufcolumn[m];
/* 379 */       this.bufcolumn[m] = (paramInt2 + i2);
/* 380 */       i2 = i1;
/* 381 */       k++;
/*     */     }
/*     */ 
/* 384 */     if (k < j)
/*     */     {
/* 386 */       this.bufline[m] = (paramInt1++);
/* 387 */       this.bufcolumn[m] = (paramInt2 + i2);
/*     */ 
/* 389 */       while (k++ < j)
/*     */       {
/* 391 */         if (this.bufline[(m = i % this.bufsize)] != this.bufline[(++i % this.bufsize)])
/* 392 */           this.bufline[m] = (paramInt1++);
/*     */         else {
/* 394 */           this.bufline[m] = paramInt1;
/*     */         }
/*     */       }
/*     */     }
/* 398 */     this.line = this.bufline[m];
/* 399 */     this.column = this.bufcolumn[m];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.ASCII_CharStream
 * JD-Core Version:    0.6.2
 */