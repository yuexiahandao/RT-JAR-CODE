/*     */ package sun.net.httpserver;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ class ChunkedInputStream extends LeftOverInputStream
/*     */ {
/*     */   private int remaining;
/*  42 */   private boolean needToReadHeader = true;
/*     */   static final char CR = '\r';
/*     */   static final char LF = '\n';
/*     */   private static final int MAX_CHUNK_HEADER_SIZE = 2050;
/*     */ 
/*     */   ChunkedInputStream(ExchangeImpl paramExchangeImpl, InputStream paramInputStream)
/*     */   {
/*  35 */     super(paramExchangeImpl, paramInputStream);
/*     */   }
/*     */ 
/*     */   private int numeric(char[] paramArrayOfChar, int paramInt)
/*     */     throws IOException
/*     */   {
/*  52 */     assert (paramArrayOfChar.length >= paramInt);
/*  53 */     int i = 0;
/*  54 */     for (int j = 0; j < paramInt; j++) {
/*  55 */       int k = paramArrayOfChar[j];
/*  56 */       int m = 0;
/*  57 */       if ((k >= 48) && (k <= 57))
/*  58 */         m = k - 48;
/*  59 */       else if ((k >= 97) && (k <= 102))
/*  60 */         m = k - 97 + 10;
/*  61 */       else if ((k >= 65) && (k <= 70))
/*  62 */         m = k - 65 + 10;
/*     */       else {
/*  64 */         throw new IOException("invalid chunk length");
/*     */       }
/*  66 */       i = i * 16 + m;
/*     */     }
/*  68 */     return i;
/*     */   }
/*     */ 
/*     */   private int readChunkHeader()
/*     */     throws IOException
/*     */   {
/*  75 */     int i = 0;
/*     */ 
/*  77 */     char[] arrayOfChar = new char[16];
/*  78 */     int k = 0;
/*  79 */     int m = 0;
/*  80 */     int n = 0;
/*     */     int j;
/*  82 */     while ((j = this.in.read()) != -1) {
/*  83 */       int i1 = (char)j;
/*  84 */       n++;
/*  85 */       if ((k == arrayOfChar.length - 1) || (n > 2050))
/*     */       {
/*  88 */         throw new IOException("invalid chunk header");
/*     */       }
/*  90 */       if (i != 0) {
/*  91 */         if (i1 == 10) {
/*  92 */           int i2 = numeric(arrayOfChar, k);
/*  93 */           return i2;
/*     */         }
/*  95 */         i = 0;
/*     */ 
/*  97 */         if (m == 0) {
/*  98 */           arrayOfChar[(k++)] = i1;
/*     */         }
/*     */       }
/* 101 */       else if (i1 == 13) {
/* 102 */         i = 1;
/* 103 */       } else if (i1 == 59) {
/* 104 */         m = 1;
/* 105 */       } else if (m == 0) {
/* 106 */         arrayOfChar[(k++)] = i1;
/*     */       }
/*     */     }
/*     */ 
/* 110 */     throw new IOException("end of stream reading chunk header");
/*     */   }
/*     */ 
/*     */   protected int readImpl(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 114 */     if (this.eof) {
/* 115 */       return -1;
/*     */     }
/* 117 */     if (this.needToReadHeader) {
/* 118 */       this.remaining = readChunkHeader();
/* 119 */       if (this.remaining == 0) {
/* 120 */         this.eof = true;
/* 121 */         consumeCRLF();
/* 122 */         this.t.getServerImpl().requestCompleted(this.t.getConnection());
/* 123 */         return -1;
/*     */       }
/* 125 */       this.needToReadHeader = false;
/*     */     }
/* 127 */     if (paramInt2 > this.remaining) {
/* 128 */       paramInt2 = this.remaining;
/*     */     }
/* 130 */     int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/* 131 */     if (i > -1) {
/* 132 */       this.remaining -= i;
/*     */     }
/* 134 */     if (this.remaining == 0) {
/* 135 */       this.needToReadHeader = true;
/* 136 */       consumeCRLF();
/*     */     }
/* 138 */     return i;
/*     */   }
/*     */ 
/*     */   private void consumeCRLF() throws IOException
/*     */   {
/* 143 */     int i = (char)this.in.read();
/* 144 */     if (i != 13) {
/* 145 */       throw new IOException("invalid chunk end");
/*     */     }
/* 147 */     i = (char)this.in.read();
/* 148 */     if (i != 10)
/* 149 */       throw new IOException("invalid chunk end");
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 160 */     if ((this.eof) || (this.closed)) {
/* 161 */       return 0;
/*     */     }
/* 163 */     int i = this.in.available();
/* 164 */     return i > this.remaining ? this.remaining : i;
/*     */   }
/*     */ 
/*     */   public boolean isDataBuffered()
/*     */     throws IOException
/*     */   {
/* 172 */     assert (this.eof);
/* 173 */     return this.in.available() > 0;
/*     */   }
/*     */   public boolean markSupported() {
/* 176 */     return false;
/*     */   }
/*     */   public void mark(int paramInt) {
/*     */   }
/*     */ 
/*     */   public void reset() throws IOException {
/* 182 */     throw new IOException("mark/reset not supported");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.ChunkedInputStream
 * JD-Core Version:    0.6.2
 */