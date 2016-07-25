/*     */ package javax.imageio.stream;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ class MemoryCache
/*     */ {
/*     */   private static final int BUFFER_LENGTH = 8192;
/*  61 */   private ArrayList cache = new ArrayList();
/*     */ 
/*  63 */   private long cacheStart = 0L;
/*     */ 
/*  68 */   private long length = 0L;
/*     */ 
/*     */   private byte[] getCacheBlock(long paramLong) throws IOException {
/*  71 */     long l = paramLong - this.cacheStart;
/*  72 */     if (l > 2147483647L)
/*     */     {
/*  75 */       throw new IOException("Cache addressing limit exceeded!");
/*     */     }
/*  77 */     return (byte[])this.cache.get((int)l);
/*     */   }
/*     */ 
/*     */   public long loadFromStream(InputStream paramInputStream, long paramLong)
/*     */     throws IOException
/*     */   {
/*  89 */     if (paramLong < this.length) {
/*  90 */       return paramLong;
/*     */     }
/*     */ 
/*  93 */     int i = (int)(this.length % 8192L);
/*  94 */     byte[] arrayOfByte = null;
/*     */ 
/*  96 */     long l = paramLong - this.length;
/*  97 */     if (i != 0) {
/*  98 */       arrayOfByte = getCacheBlock(this.length / 8192L);
/*     */     }
/*     */ 
/* 101 */     while (l > 0L) {
/* 102 */       if (arrayOfByte == null) {
/*     */         try {
/* 104 */           arrayOfByte = new byte[8192];
/*     */         } catch (OutOfMemoryError localOutOfMemoryError) {
/* 106 */           throw new IOException("No memory left for cache!");
/*     */         }
/* 108 */         i = 0;
/*     */       }
/*     */ 
/* 111 */       int j = 8192 - i;
/* 112 */       int k = (int)Math.min(l, j);
/* 113 */       k = paramInputStream.read(arrayOfByte, i, k);
/* 114 */       if (k == -1) {
/* 115 */         return this.length;
/*     */       }
/*     */ 
/* 118 */       if (i == 0) {
/* 119 */         this.cache.add(arrayOfByte);
/*     */       }
/*     */ 
/* 122 */       l -= k;
/* 123 */       this.length += k;
/* 124 */       i += k;
/*     */ 
/* 126 */       if (i >= 8192)
/*     */       {
/* 129 */         arrayOfByte = null;
/*     */       }
/*     */     }
/*     */ 
/* 133 */     return paramLong;
/*     */   }
/*     */ 
/*     */   public void writeToStream(OutputStream paramOutputStream, long paramLong1, long paramLong2)
/*     */     throws IOException
/*     */   {
/* 149 */     if (paramLong1 + paramLong2 > this.length) {
/* 150 */       throw new IndexOutOfBoundsException("Argument out of cache");
/*     */     }
/* 152 */     if ((paramLong1 < 0L) || (paramLong2 < 0L)) {
/* 153 */       throw new IndexOutOfBoundsException("Negative pos or len");
/*     */     }
/* 155 */     if (paramLong2 == 0L) {
/* 156 */       return;
/*     */     }
/*     */ 
/* 159 */     long l = paramLong1 / 8192L;
/* 160 */     if (l < this.cacheStart) {
/* 161 */       throw new IndexOutOfBoundsException("pos already disposed");
/*     */     }
/* 163 */     int i = (int)(paramLong1 % 8192L);
/*     */ 
/* 165 */     byte[] arrayOfByte = getCacheBlock(l++);
/* 166 */     while (paramLong2 > 0L) {
/* 167 */       if (arrayOfByte == null) {
/* 168 */         arrayOfByte = getCacheBlock(l++);
/* 169 */         i = 0;
/*     */       }
/* 171 */       int j = (int)Math.min(paramLong2, 8192 - i);
/* 172 */       paramOutputStream.write(arrayOfByte, i, j);
/* 173 */       arrayOfByte = null;
/* 174 */       paramLong2 -= j;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void pad(long paramLong)
/*     */     throws IOException
/*     */   {
/* 182 */     long l1 = this.cacheStart + this.cache.size() - 1L;
/* 183 */     long l2 = paramLong / 8192L;
/* 184 */     long l3 = l2 - l1;
/* 185 */     for (long l4 = 0L; l4 < l3; l4 += 1L)
/*     */       try {
/* 187 */         this.cache.add(new byte[8192]);
/*     */       } catch (OutOfMemoryError localOutOfMemoryError) {
/* 189 */         throw new IOException("No memory left for cache!");
/*     */       }
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong)
/*     */     throws IOException
/*     */   {
/* 211 */     if (paramArrayOfByte == null) {
/* 212 */       throw new NullPointerException("b == null!");
/*     */     }
/*     */ 
/* 215 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramLong < 0L) || (paramInt1 + paramInt2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 < 0))
/*     */     {
/* 217 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */ 
/* 221 */     long l = paramLong + paramInt2 - 1L;
/* 222 */     if (l >= this.length) {
/* 223 */       pad(l);
/* 224 */       this.length = (l + 1L);
/*     */     }
/*     */ 
/* 228 */     int i = (int)(paramLong % 8192L);
/* 229 */     while (paramInt2 > 0) {
/* 230 */       byte[] arrayOfByte = getCacheBlock(paramLong / 8192L);
/* 231 */       int j = Math.min(paramInt2, 8192 - i);
/* 232 */       System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, i, j);
/*     */ 
/* 234 */       paramLong += j;
/* 235 */       paramInt1 += j;
/* 236 */       paramInt2 -= j;
/* 237 */       i = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(int paramInt, long paramLong)
/*     */     throws IOException
/*     */   {
/* 253 */     if (paramLong < 0L) {
/* 254 */       throw new ArrayIndexOutOfBoundsException("pos < 0");
/*     */     }
/*     */ 
/* 258 */     if (paramLong >= this.length) {
/* 259 */       pad(paramLong);
/* 260 */       this.length = (paramLong + 1L);
/*     */     }
/*     */ 
/* 264 */     byte[] arrayOfByte = getCacheBlock(paramLong / 8192L);
/* 265 */     int i = (int)(paramLong % 8192L);
/* 266 */     arrayOfByte[i] = ((byte)paramInt);
/*     */   }
/*     */ 
/*     */   public long getLength()
/*     */   {
/* 275 */     return this.length;
/*     */   }
/*     */ 
/*     */   public int read(long paramLong)
/*     */     throws IOException
/*     */   {
/* 284 */     if (paramLong >= this.length) {
/* 285 */       return -1;
/*     */     }
/*     */ 
/* 288 */     byte[] arrayOfByte = getCacheBlock(paramLong / 8192L);
/* 289 */     if (arrayOfByte == null) {
/* 290 */       return -1;
/*     */     }
/*     */ 
/* 293 */     return arrayOfByte[((int)(paramLong % 8192L))] & 0xFF;
/*     */   }
/*     */ 
/*     */   public void read(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong)
/*     */     throws IOException
/*     */   {
/* 310 */     if (paramArrayOfByte == null) {
/* 311 */       throw new NullPointerException("b == null!");
/*     */     }
/*     */ 
/* 314 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramLong < 0L) || (paramInt1 + paramInt2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 < 0))
/*     */     {
/* 316 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 318 */     if (paramLong + paramInt2 > this.length) {
/* 319 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */ 
/* 322 */     long l = paramLong / 8192L;
/* 323 */     int i = (int)paramLong % 8192;
/* 324 */     while (paramInt2 > 0) {
/* 325 */       int j = Math.min(paramInt2, 8192 - i);
/* 326 */       byte[] arrayOfByte = getCacheBlock(l++);
/* 327 */       System.arraycopy(arrayOfByte, i, paramArrayOfByte, paramInt1, j);
/*     */ 
/* 329 */       paramInt2 -= j;
/* 330 */       paramInt1 += j;
/* 331 */       i = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void disposeBefore(long paramLong)
/*     */   {
/* 343 */     long l1 = paramLong / 8192L;
/* 344 */     if (l1 < this.cacheStart) {
/* 345 */       throw new IndexOutOfBoundsException("pos already disposed");
/*     */     }
/* 347 */     long l2 = Math.min(l1 - this.cacheStart, this.cache.size());
/* 348 */     for (long l3 = 0L; l3 < l2; l3 += 1L) {
/* 349 */       this.cache.remove(0);
/*     */     }
/* 351 */     this.cacheStart = l1;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 360 */     this.cache.clear();
/* 361 */     this.cacheStart = 0L;
/* 362 */     this.length = 0L;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.stream.MemoryCache
 * JD-Core Version:    0.6.2
 */