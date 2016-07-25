/*     */ package sun.net.httpserver;
/*     */ 
/*     */ import com.sun.net.httpserver.Headers;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.BufferOverflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.SocketChannel;
/*     */ 
/*     */ class Request
/*     */ {
/*     */   static final int BUF_LEN = 2048;
/*     */   static final byte CR = 13;
/*     */   static final byte LF = 10;
/*     */   private String startLine;
/*     */   private SocketChannel chan;
/*     */   private InputStream is;
/*     */   private OutputStream os;
/*  63 */   char[] buf = new char[2048];
/*     */   int pos;
/*     */   StringBuffer lineBuf;
/* 123 */   Headers hdrs = null;
/*     */ 
/*     */   Request(InputStream paramInputStream, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/*  50 */     this.chan = this.chan;
/*  51 */     this.is = paramInputStream;
/*  52 */     this.os = paramOutputStream;
/*     */     do {
/*  54 */       this.startLine = readLine();
/*  55 */       if (this.startLine == null) {
/*  56 */         return;
/*     */       }
/*     */     }
/*  59 */     while ((this.startLine != null) && (this.startLine.equals("")));
/*     */   }
/*     */ 
/*     */   public InputStream inputStream()
/*     */   {
/*  68 */     return this.is;
/*     */   }
/*     */ 
/*     */   public OutputStream outputStream() {
/*  72 */     return this.os;
/*     */   }
/*     */ 
/*     */   public String readLine()
/*     */     throws IOException
/*     */   {
/*  81 */     int i = 0; int j = 0;
/*  82 */     this.pos = 0; this.lineBuf = new StringBuffer();
/*  83 */     while (j == 0) {
/*  84 */       int k = this.is.read();
/*  85 */       if (k == -1) {
/*  86 */         return null;
/*     */       }
/*  88 */       if (i != 0) {
/*  89 */         if (k == 10) {
/*  90 */           j = 1;
/*     */         } else {
/*  92 */           i = 0;
/*  93 */           consume(13);
/*  94 */           consume(k);
/*     */         }
/*     */       }
/*  97 */       else if (k == 13)
/*  98 */         i = 1;
/*     */       else {
/* 100 */         consume(k);
/*     */       }
/*     */     }
/*     */ 
/* 104 */     this.lineBuf.append(this.buf, 0, this.pos);
/* 105 */     return new String(this.lineBuf);
/*     */   }
/*     */ 
/*     */   private void consume(int paramInt) {
/* 109 */     if (this.pos == 2048) {
/* 110 */       this.lineBuf.append(this.buf);
/* 111 */       this.pos = 0;
/*     */     }
/* 113 */     this.buf[(this.pos++)] = ((char)paramInt);
/*     */   }
/*     */ 
/*     */   public String requestLine()
/*     */   {
/* 120 */     return this.startLine;
/*     */   }
/*     */ 
/*     */   Headers headers()
/*     */     throws IOException
/*     */   {
/* 126 */     if (this.hdrs != null) {
/* 127 */       return this.hdrs;
/*     */     }
/* 129 */     this.hdrs = new Headers();
/*     */ 
/* 131 */     Object localObject1 = new char[10];
/* 132 */     int i = 0;
/*     */ 
/* 134 */     int j = this.is.read();
/*     */     int k;
/* 137 */     if ((j == 13) || (j == 10)) {
/* 138 */       k = this.is.read();
/* 139 */       if ((k == 13) || (k == 10)) {
/* 140 */         return this.hdrs;
/*     */       }
/* 142 */       localObject1[0] = ((char)j);
/* 143 */       i = 1;
/* 144 */       j = k;
/*     */     }
/*     */ 
/* 147 */     while ((j != 10) && (j != 13) && (j >= 0)) {
/* 148 */       k = -1;
/*     */ 
/* 150 */       int n = j > 32 ? 1 : 0;
/* 151 */       localObject1[(i++)] = ((char)j);
/*     */       int m;
/*     */       Object localObject2;
/* 153 */       while ((m = this.is.read()) >= 0) {
/* 154 */         switch (m) {
/*     */         case 58:
/* 156 */           if ((n != 0) && (i > 0))
/* 157 */             k = i;
/* 158 */           n = 0;
/* 159 */           break;
/*     */         case 9:
/* 161 */           m = 32;
/*     */         case 32:
/* 163 */           n = 0;
/* 164 */           break;
/*     */         case 10:
/*     */         case 13:
/* 167 */           j = this.is.read();
/* 168 */           if ((m == 13) && (j == 10)) {
/* 169 */             j = this.is.read();
/* 170 */             if (j == 13)
/* 171 */               j = this.is.read();
/*     */           }
/* 173 */           if ((j == 10) || (j == 13) || (j > 32)) {
/*     */             break label328;
/*     */           }
/* 176 */           m = 32;
/*     */         }
/*     */ 
/* 179 */         if (i >= localObject1.length) {
/* 180 */           localObject2 = new char[localObject1.length * 2];
/* 181 */           System.arraycopy(localObject1, 0, localObject2, 0, i);
/* 182 */           localObject1 = localObject2;
/*     */         }
/* 184 */         localObject1[(i++)] = ((char)m);
/*     */       }
/* 186 */       j = -1;
/*     */ 
/* 188 */       label328: while ((i > 0) && (localObject1[(i - 1)] <= ' ')) {
/* 189 */         i--;
/*     */       }
/* 191 */       if (k <= 0) {
/* 192 */         localObject2 = null;
/* 193 */         k = 0;
/*     */       } else {
/* 195 */         localObject2 = String.copyValueOf((char[])localObject1, 0, k);
/* 196 */         if ((k < i) && (localObject1[k] == ':'))
/* 197 */           k++;
/* 198 */         while ((k < i) && (localObject1[k] <= ' '))
/* 199 */           k++;
/*     */       }
/*     */       String str;
/* 202 */       if (k >= i)
/* 203 */         str = new String();
/*     */       else {
/* 205 */         str = String.copyValueOf((char[])localObject1, k, i - k);
/*     */       }
/* 207 */       if (this.hdrs.size() >= ServerConfig.getMaxReqHeaders()) {
/* 208 */         throw new IOException("Maximum number of request headers (sun.net.httpserver.maxReqHeaders) exceeded, " + ServerConfig.getMaxReqHeaders() + ".");
/*     */       }
/*     */ 
/* 213 */       this.hdrs.add((String)localObject2, str);
/* 214 */       i = 0;
/*     */     }
/* 216 */     return this.hdrs; } 
/*     */   static class ReadStream extends InputStream { SocketChannel channel;
/*     */     ByteBuffer chanbuf;
/*     */     byte[] one;
/* 227 */     private boolean closed = false; private boolean eof = false;
/*     */     ByteBuffer markBuf;
/*     */     boolean marked;
/*     */     boolean reset;
/*     */     int readlimit;
/*     */     static long readTimeout;
/*     */     ServerImpl server;
/*     */     static final int BUFSIZE = 8192;
/*     */ 
/* 237 */     public ReadStream(ServerImpl paramServerImpl, SocketChannel paramSocketChannel) throws IOException { this.channel = paramSocketChannel;
/* 238 */       this.server = paramServerImpl;
/* 239 */       this.chanbuf = ByteBuffer.allocate(8192);
/* 240 */       this.chanbuf.clear();
/* 241 */       this.one = new byte[1];
/* 242 */       this.closed = (this.marked = this.reset = 0); }
/*     */ 
/*     */     public synchronized int read(byte[] paramArrayOfByte) throws IOException
/*     */     {
/* 246 */       return read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */     }
/*     */ 
/*     */     public synchronized int read() throws IOException {
/* 250 */       int i = read(this.one, 0, 1);
/* 251 */       if (i == 1) {
/* 252 */         return this.one[0] & 0xFF;
/*     */       }
/* 254 */       return -1;
/*     */     }
/*     */ 
/*     */     public synchronized int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */       throws IOException
/*     */     {
/* 262 */       if (this.closed) {
/* 263 */         throw new IOException("Stream closed");
/*     */       }
/* 265 */       if (this.eof) {
/* 266 */         return -1;
/*     */       }
/*     */ 
/* 269 */       assert (this.channel.isBlocking());
/*     */ 
/* 271 */       if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt2 > paramArrayOfByte.length - paramInt1))
/* 272 */         throw new IndexOutOfBoundsException();
/*     */       int j;
/* 275 */       if (this.reset) {
/* 276 */         int i = this.markBuf.remaining();
/* 277 */         j = i > paramInt2 ? paramInt2 : i;
/* 278 */         this.markBuf.get(paramArrayOfByte, paramInt1, j);
/* 279 */         if (i == j)
/* 280 */           this.reset = false;
/*     */       }
/*     */       else {
/* 283 */         this.chanbuf.clear();
/* 284 */         if (paramInt2 < 8192) {
/* 285 */           this.chanbuf.limit(paramInt2);
/*     */         }
/*     */         do
/* 288 */           j = this.channel.read(this.chanbuf);
/* 289 */         while (j == 0);
/* 290 */         if (j == -1) {
/* 291 */           this.eof = true;
/* 292 */           return -1;
/*     */         }
/* 294 */         this.chanbuf.flip();
/* 295 */         this.chanbuf.get(paramArrayOfByte, paramInt1, j);
/*     */ 
/* 297 */         if (this.marked) {
/*     */           try {
/* 299 */             this.markBuf.put(paramArrayOfByte, paramInt1, j);
/*     */           } catch (BufferOverflowException localBufferOverflowException) {
/* 301 */             this.marked = false;
/*     */           }
/*     */         }
/*     */       }
/* 305 */       return j;
/*     */     }
/*     */ 
/*     */     public boolean markSupported() {
/* 309 */       return true;
/*     */     }
/*     */ 
/*     */     public synchronized int available() throws IOException
/*     */     {
/* 314 */       if (this.closed) {
/* 315 */         throw new IOException("Stream is closed");
/*     */       }
/* 317 */       if (this.eof) {
/* 318 */         return -1;
/*     */       }
/* 320 */       if (this.reset) {
/* 321 */         return this.markBuf.remaining();
/*     */       }
/* 323 */       return this.chanbuf.remaining();
/*     */     }
/*     */ 
/*     */     public void close() throws IOException {
/* 327 */       if (this.closed) {
/* 328 */         return;
/*     */       }
/* 330 */       this.channel.close();
/* 331 */       this.closed = true;
/*     */     }
/*     */ 
/*     */     public synchronized void mark(int paramInt) {
/* 335 */       if (this.closed)
/* 336 */         return;
/* 337 */       this.readlimit = paramInt;
/* 338 */       this.markBuf = ByteBuffer.allocate(paramInt);
/* 339 */       this.marked = true;
/* 340 */       this.reset = false;
/*     */     }
/*     */ 
/*     */     public synchronized void reset() throws IOException {
/* 344 */       if (this.closed)
/* 345 */         return;
/* 346 */       if (!this.marked)
/* 347 */         throw new IOException("Stream not marked");
/* 348 */       this.marked = false;
/* 349 */       this.reset = true;
/* 350 */       this.markBuf.flip(); }  } 
/*     */   static class WriteStream extends OutputStream { SocketChannel channel;
/*     */     ByteBuffer buf;
/*     */     SelectionKey key;
/*     */     boolean closed;
/*     */     byte[] one;
/*     */     ServerImpl server;
/*     */ 
/* 363 */     public WriteStream(ServerImpl paramServerImpl, SocketChannel paramSocketChannel) throws IOException { this.channel = paramSocketChannel;
/* 364 */       this.server = paramServerImpl;
/* 365 */       assert (paramSocketChannel.isBlocking());
/* 366 */       this.closed = false;
/* 367 */       this.one = new byte[1];
/* 368 */       this.buf = ByteBuffer.allocate(4096); }
/*     */ 
/*     */     public synchronized void write(int paramInt) throws IOException
/*     */     {
/* 372 */       this.one[0] = ((byte)paramInt);
/* 373 */       write(this.one, 0, 1);
/*     */     }
/*     */ 
/*     */     public synchronized void write(byte[] paramArrayOfByte) throws IOException {
/* 377 */       write(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */     }
/*     */ 
/*     */     public synchronized void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 381 */       int i = paramInt2;
/* 382 */       if (this.closed) {
/* 383 */         throw new IOException("stream is closed");
/*     */       }
/* 385 */       int j = this.buf.capacity();
/*     */       int k;
/* 386 */       if (j < paramInt2) {
/* 387 */         k = paramInt2 - j;
/* 388 */         this.buf = ByteBuffer.allocate(2 * (j + k));
/*     */       }
/* 390 */       this.buf.clear();
/* 391 */       this.buf.put(paramArrayOfByte, paramInt1, paramInt2);
/* 392 */       this.buf.flip();
/*     */ 
/* 394 */       while ((k = this.channel.write(this.buf)) < i) {
/* 395 */         i -= k;
/* 396 */         if (i == 0);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void close() throws IOException {
/* 402 */       if (this.closed) {
/* 403 */         return;
/*     */       }
/* 405 */       this.channel.close();
/* 406 */       this.closed = true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.Request
 * JD-Core Version:    0.6.2
 */