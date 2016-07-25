/*     */ package sun.net.www.http;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ public class ChunkedOutputStream extends PrintStream
/*     */ {
/*     */   static final int DEFAULT_CHUNK_SIZE = 4096;
/*  37 */   private static final byte[] CRLF = { 13, 10 };
/*  38 */   private static final int CRLF_SIZE = CRLF.length;
/*  39 */   private static final byte[] FOOTER = CRLF;
/*  40 */   private static final int FOOTER_SIZE = CRLF_SIZE;
/*  41 */   private static final byte[] EMPTY_CHUNK_HEADER = getHeader(0);
/*  42 */   private static final int EMPTY_CHUNK_HEADER_SIZE = getHeaderSize(0);
/*     */   private byte[] buf;
/*     */   private int size;
/*     */   private int count;
/*     */   private int spaceInCurrentChunk;
/*     */   private PrintStream out;
/*     */   private int preferredChunkDataSize;
/*     */   private int preferedHeaderSize;
/*     */   private int preferredChunkGrossSize;
/*     */   private byte[] completeHeader;
/*     */ 
/*     */   private static int getHeaderSize(int paramInt)
/*     */   {
/*  66 */     return Integer.toHexString(paramInt).length() + CRLF_SIZE;
/*     */   }
/*     */ 
/*     */   private static byte[] getHeader(int paramInt)
/*     */   {
/*     */     try {
/*  72 */       String str = Integer.toHexString(paramInt);
/*  73 */       byte[] arrayOfByte1 = str.getBytes("US-ASCII");
/*  74 */       byte[] arrayOfByte2 = new byte[getHeaderSize(paramInt)];
/*  75 */       for (int i = 0; i < arrayOfByte1.length; i++)
/*  76 */         arrayOfByte2[i] = arrayOfByte1[i];
/*  77 */       arrayOfByte2[arrayOfByte1.length] = CRLF[0];
/*  78 */       arrayOfByte2[(arrayOfByte1.length + 1)] = CRLF[1];
/*  79 */       return arrayOfByte2;
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*  82 */       throw new InternalError(localUnsupportedEncodingException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public ChunkedOutputStream(PrintStream paramPrintStream) {
/*  87 */     this(paramPrintStream, 4096);
/*     */   }
/*     */ 
/*     */   public ChunkedOutputStream(PrintStream paramPrintStream, int paramInt) {
/*  91 */     super(paramPrintStream);
/*  92 */     this.out = paramPrintStream;
/*     */ 
/*  94 */     if (paramInt <= 0) {
/*  95 */       paramInt = 4096;
/*     */     }
/*     */ 
/* 107 */     if (paramInt > 0) {
/* 108 */       int i = paramInt - getHeaderSize(paramInt) - FOOTER_SIZE;
/* 109 */       if (getHeaderSize(i + 1) < getHeaderSize(paramInt)) {
/* 110 */         i++;
/*     */       }
/* 112 */       paramInt = i;
/*     */     }
/*     */ 
/* 115 */     if (paramInt > 0)
/* 116 */       this.preferredChunkDataSize = paramInt;
/*     */     else {
/* 118 */       this.preferredChunkDataSize = (4096 - getHeaderSize(4096) - FOOTER_SIZE);
/*     */     }
/*     */ 
/* 122 */     this.preferedHeaderSize = getHeaderSize(this.preferredChunkDataSize);
/* 123 */     this.preferredChunkGrossSize = (this.preferedHeaderSize + this.preferredChunkDataSize + FOOTER_SIZE);
/*     */ 
/* 125 */     this.completeHeader = getHeader(this.preferredChunkDataSize);
/*     */ 
/* 128 */     this.buf = new byte[this.preferredChunkGrossSize];
/* 129 */     reset();
/*     */   }
/*     */ 
/*     */   private void flush(boolean paramBoolean)
/*     */   {
/* 143 */     if (this.spaceInCurrentChunk == 0)
/*     */     {
/* 145 */       this.out.write(this.buf, 0, this.preferredChunkGrossSize);
/* 146 */       this.out.flush();
/* 147 */       reset();
/* 148 */     } else if (paramBoolean)
/*     */     {
/* 150 */       if (this.size > 0)
/*     */       {
/* 154 */         int i = this.preferedHeaderSize - getHeaderSize(this.size);
/*     */ 
/* 158 */         System.arraycopy(getHeader(this.size), 0, this.buf, i, getHeaderSize(this.size));
/*     */ 
/* 162 */         this.buf[(this.count++)] = FOOTER[0];
/* 163 */         this.buf[(this.count++)] = FOOTER[1];
/*     */ 
/* 166 */         this.out.write(this.buf, i, this.count - i);
/*     */       }
/*     */       else {
/* 169 */         this.out.write(EMPTY_CHUNK_HEADER, 0, EMPTY_CHUNK_HEADER_SIZE);
/*     */       }
/*     */ 
/* 172 */       this.out.flush();
/* 173 */       reset();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean checkError()
/*     */   {
/* 179 */     return this.out.checkError();
/*     */   }
/*     */ 
/*     */   private void ensureOpen()
/*     */   {
/* 184 */     if (this.out == null)
/* 185 */       setError();
/*     */   }
/*     */ 
/*     */   public synchronized void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 201 */     ensureOpen();
/* 202 */     if ((paramInt1 < 0) || (paramInt1 > paramArrayOfByte.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 < 0))
/*     */     {
/* 204 */       throw new IndexOutOfBoundsException();
/* 205 */     }if (paramInt2 == 0) {
/* 206 */       return;
/*     */     }
/*     */ 
/* 215 */     int i = paramInt2;
/* 216 */     int j = paramInt1;
/*     */     do
/*     */     {
/* 220 */       if (i >= this.spaceInCurrentChunk)
/*     */       {
/* 223 */         for (int k = 0; k < this.completeHeader.length; k++) {
/* 224 */           this.buf[k] = this.completeHeader[k];
/*     */         }
/*     */ 
/* 227 */         System.arraycopy(paramArrayOfByte, j, this.buf, this.count, this.spaceInCurrentChunk);
/* 228 */         j += this.spaceInCurrentChunk;
/* 229 */         i -= this.spaceInCurrentChunk;
/* 230 */         this.count += this.spaceInCurrentChunk;
/*     */ 
/* 233 */         this.buf[(this.count++)] = FOOTER[0];
/* 234 */         this.buf[(this.count++)] = FOOTER[1];
/* 235 */         this.spaceInCurrentChunk = 0;
/*     */ 
/* 237 */         flush(false);
/* 238 */         if (checkError()) {
/* 239 */           break;
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 249 */         System.arraycopy(paramArrayOfByte, j, this.buf, this.count, i);
/* 250 */         this.count += i;
/* 251 */         this.size += i;
/* 252 */         this.spaceInCurrentChunk -= i;
/* 253 */         i = 0;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 258 */     while (i > 0);
/*     */   }
/*     */ 
/*     */   public synchronized void write(int paramInt)
/*     */   {
/* 263 */     byte[] arrayOfByte = { (byte)paramInt };
/* 264 */     write(arrayOfByte, 0, 1);
/*     */   }
/*     */ 
/*     */   public synchronized void reset() {
/* 268 */     this.count = this.preferedHeaderSize;
/* 269 */     this.size = 0;
/* 270 */     this.spaceInCurrentChunk = this.preferredChunkDataSize;
/*     */   }
/*     */ 
/*     */   public int size() {
/* 274 */     return this.size;
/*     */   }
/*     */ 
/*     */   public synchronized void close()
/*     */   {
/* 279 */     ensureOpen();
/*     */ 
/* 282 */     if (this.size > 0) {
/* 283 */       flush(true);
/*     */     }
/*     */ 
/* 287 */     flush(true);
/*     */ 
/* 290 */     this.out = null;
/*     */   }
/*     */ 
/*     */   public synchronized void flush()
/*     */   {
/* 295 */     ensureOpen();
/* 296 */     if (this.size > 0)
/* 297 */       flush(true);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.http.ChunkedOutputStream
 * JD-Core Version:    0.6.2
 */