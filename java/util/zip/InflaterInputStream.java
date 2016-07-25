/*     */ package java.util.zip;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class InflaterInputStream extends FilterInputStream
/*     */ {
/*     */   protected Inflater inf;
/*     */   protected byte[] buf;
/*     */   protected int len;
/*  58 */   private boolean closed = false;
/*     */ 
/*  60 */   private boolean reachEOF = false;
/*     */ 
/* 101 */   boolean usesDefaultInflater = false;
/*     */ 
/* 112 */   private byte[] singleByteBuf = new byte[1];
/*     */ 
/* 187 */   private byte[] b = new byte[512];
/*     */ 
/*     */   private void ensureOpen()
/*     */     throws IOException
/*     */   {
/*  66 */     if (this.closed)
/*  67 */       throw new IOException("Stream closed");
/*     */   }
/*     */ 
/*     */   public InflaterInputStream(InputStream paramInputStream, Inflater paramInflater, int paramInt)
/*     */   {
/*  81 */     super(paramInputStream);
/*  82 */     if ((paramInputStream == null) || (paramInflater == null))
/*  83 */       throw new NullPointerException();
/*  84 */     if (paramInt <= 0) {
/*  85 */       throw new IllegalArgumentException("buffer size <= 0");
/*     */     }
/*  87 */     this.inf = paramInflater;
/*  88 */     this.buf = new byte[paramInt];
/*     */   }
/*     */ 
/*     */   public InflaterInputStream(InputStream paramInputStream, Inflater paramInflater)
/*     */   {
/*  98 */     this(paramInputStream, paramInflater, 512);
/*     */   }
/*     */ 
/*     */   public InflaterInputStream(InputStream paramInputStream)
/*     */   {
/* 108 */     this(paramInputStream, new Inflater());
/* 109 */     this.usesDefaultInflater = true;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 121 */     ensureOpen();
/* 122 */     return read(this.singleByteBuf, 0, 1) == -1 ? -1 : this.singleByteBuf[0] & 0xFF;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 142 */     ensureOpen();
/* 143 */     if (paramArrayOfByte == null)
/* 144 */       throw new NullPointerException();
/* 145 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt2 > paramArrayOfByte.length - paramInt1))
/* 146 */       throw new IndexOutOfBoundsException();
/* 147 */     if (paramInt2 == 0)
/* 148 */       return 0;
/*     */     try
/*     */     {
/*     */       int i;
/* 152 */       while ((i = this.inf.inflate(paramArrayOfByte, paramInt1, paramInt2)) == 0) {
/* 153 */         if ((this.inf.finished()) || (this.inf.needsDictionary())) {
/* 154 */           this.reachEOF = true;
/* 155 */           return -1;
/*     */         }
/* 157 */         if (this.inf.needsInput()) {
/* 158 */           fill();
/*     */         }
/*     */       }
/* 161 */       return i;
/*     */     } catch (DataFormatException localDataFormatException) {
/* 163 */       String str = localDataFormatException.getMessage();
/* 164 */       if (str != null) tmpTernaryOp = str;  } throw new ZipException("Invalid ZLIB data format");
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 179 */     ensureOpen();
/* 180 */     if (this.reachEOF) {
/* 181 */       return 0;
/*     */     }
/* 183 */     return 1;
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 197 */     if (paramLong < 0L) {
/* 198 */       throw new IllegalArgumentException("negative skip length");
/*     */     }
/* 200 */     ensureOpen();
/* 201 */     int i = (int)Math.min(paramLong, 2147483647L);
/* 202 */     int j = 0;
/* 203 */     while (j < i) {
/* 204 */       int k = i - j;
/* 205 */       if (k > this.b.length) {
/* 206 */         k = this.b.length;
/*     */       }
/* 208 */       k = read(this.b, 0, k);
/* 209 */       if (k == -1) {
/* 210 */         this.reachEOF = true;
/* 211 */         break;
/*     */       }
/* 213 */       j += k;
/*     */     }
/* 215 */     return j;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 224 */     if (!this.closed) {
/* 225 */       if (this.usesDefaultInflater)
/* 226 */         this.inf.end();
/* 227 */       this.in.close();
/* 228 */       this.closed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void fill()
/*     */     throws IOException
/*     */   {
/* 237 */     ensureOpen();
/* 238 */     this.len = this.in.read(this.buf, 0, this.buf.length);
/* 239 */     if (this.len == -1) {
/* 240 */       throw new EOFException("Unexpected end of ZLIB input stream");
/*     */     }
/* 242 */     this.inf.setInput(this.buf, 0, this.len);
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 257 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized void mark(int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   public synchronized void reset()
/*     */     throws IOException
/*     */   {
/* 286 */     throw new IOException("mark/reset not supported");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.zip.InflaterInputStream
 * JD-Core Version:    0.6.2
 */