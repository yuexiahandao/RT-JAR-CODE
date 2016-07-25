/*     */ package java.util.zip;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class DeflaterInputStream extends FilterInputStream
/*     */ {
/*     */   protected final Deflater def;
/*     */   protected final byte[] buf;
/*  52 */   private byte[] rbuf = new byte[1];
/*     */ 
/*  55 */   private boolean usesDefaultDeflater = false;
/*     */ 
/*  58 */   private boolean reachEOF = false;
/*     */ 
/*     */   private void ensureOpen()
/*     */     throws IOException
/*     */   {
/*  64 */     if (this.in == null)
/*  65 */       throw new IOException("Stream closed");
/*     */   }
/*     */ 
/*     */   public DeflaterInputStream(InputStream paramInputStream)
/*     */   {
/*  77 */     this(paramInputStream, new Deflater());
/*  78 */     this.usesDefaultDeflater = true;
/*     */   }
/*     */ 
/*     */   public DeflaterInputStream(InputStream paramInputStream, Deflater paramDeflater)
/*     */   {
/*  90 */     this(paramInputStream, paramDeflater, 512);
/*     */   }
/*     */ 
/*     */   public DeflaterInputStream(InputStream paramInputStream, Deflater paramDeflater, int paramInt)
/*     */   {
/* 104 */     super(paramInputStream);
/*     */ 
/* 107 */     if (paramInputStream == null)
/* 108 */       throw new NullPointerException("Null input");
/* 109 */     if (paramDeflater == null)
/* 110 */       throw new NullPointerException("Null deflater");
/* 111 */     if (paramInt < 1) {
/* 112 */       throw new IllegalArgumentException("Buffer size < 1");
/*     */     }
/*     */ 
/* 115 */     this.def = paramDeflater;
/* 116 */     this.buf = new byte[paramInt];
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 126 */     if (this.in != null)
/*     */       try
/*     */       {
/* 129 */         if (this.usesDefaultDeflater) {
/* 130 */           this.def.end();
/*     */         }
/*     */ 
/* 133 */         this.in.close();
/*     */       } finally {
/* 135 */         this.in = null;
/*     */       }
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 151 */     int i = read(this.rbuf, 0, 1);
/* 152 */     if (i <= 0)
/* 153 */       return -1;
/* 154 */     return this.rbuf[0] & 0xFF;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 173 */     ensureOpen();
/* 174 */     if (paramArrayOfByte == null)
/* 175 */       throw new NullPointerException("Null buffer for read");
/* 176 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt2 > paramArrayOfByte.length - paramInt1))
/* 177 */       throw new IndexOutOfBoundsException();
/* 178 */     if (paramInt2 == 0) {
/* 179 */       return 0;
/*     */     }
/*     */ 
/* 183 */     int i = 0;
/* 184 */     while ((paramInt2 > 0) && (!this.def.finished()))
/*     */     {
/* 188 */       if (this.def.needsInput()) {
/* 189 */         j = this.in.read(this.buf, 0, this.buf.length);
/* 190 */         if (j < 0)
/*     */         {
/* 192 */           this.def.finish();
/* 193 */         } else if (j > 0) {
/* 194 */           this.def.setInput(this.buf, 0, j);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 199 */       int j = this.def.deflate(paramArrayOfByte, paramInt1, paramInt2);
/* 200 */       i += j;
/* 201 */       paramInt1 += j;
/* 202 */       paramInt2 -= j;
/*     */     }
/* 204 */     if ((i == 0) && (this.def.finished())) {
/* 205 */       this.reachEOF = true;
/* 206 */       i = -1;
/*     */     }
/*     */ 
/* 209 */     return i;
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 225 */     if (paramLong < 0L) {
/* 226 */       throw new IllegalArgumentException("negative skip length");
/*     */     }
/* 228 */     ensureOpen();
/*     */ 
/* 231 */     if (this.rbuf.length < 512) {
/* 232 */       this.rbuf = new byte[512];
/*     */     }
/* 234 */     int i = (int)Math.min(paramLong, 2147483647L);
/* 235 */     long l = 0L;
/* 236 */     while (i > 0)
/*     */     {
/* 238 */       int j = read(this.rbuf, 0, i <= this.rbuf.length ? i : this.rbuf.length);
/*     */ 
/* 240 */       if (j < 0) {
/*     */         break;
/*     */       }
/* 243 */       l += j;
/* 244 */       i -= j;
/*     */     }
/* 246 */     return l;
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 260 */     ensureOpen();
/* 261 */     if (this.reachEOF) {
/* 262 */       return 0;
/*     */     }
/* 264 */     return 1;
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 274 */     return false;
/*     */   }
/*     */ 
/*     */   public void mark(int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 292 */     throw new IOException("mark/reset not supported");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.zip.DeflaterInputStream
 * JD-Core Version:    0.6.2
 */