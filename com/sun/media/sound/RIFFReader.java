/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public final class RIFFReader extends InputStream
/*     */ {
/*     */   private final RIFFReader root;
/*  39 */   private long filepointer = 0L;
/*     */   private final String fourcc;
/*  41 */   private String riff_type = null;
/*  42 */   private long ckSize = 0L;
/*     */   private InputStream stream;
/*     */   private long avail;
/*  45 */   private RIFFReader lastiterator = null;
/*     */ 
/*     */   public RIFFReader(InputStream paramInputStream) throws IOException
/*     */   {
/*  49 */     if ((paramInputStream instanceof RIFFReader))
/*  50 */       this.root = ((RIFFReader)paramInputStream).root;
/*     */     else {
/*  52 */       this.root = this;
/*  54 */     }this.stream = paramInputStream;
/*  55 */     this.avail = 2147483647L;
/*  56 */     this.ckSize = 2147483647L;
/*     */     int i;
/*     */     while (true) {
/*  61 */       i = read();
/*  62 */       if (i == -1) {
/*  63 */         this.fourcc = "";
/*     */ 
/*  66 */         this.riff_type = null;
/*  67 */         this.avail = 0L;
/*  68 */         return;
/*     */       }
/*  70 */       if (i != 0) {
/*  71 */         break;
/*     */       }
/*     */     }
/*  74 */     byte[] arrayOfByte1 = new byte[4];
/*  75 */     arrayOfByte1[0] = ((byte)i);
/*  76 */     readFully(arrayOfByte1, 1, 3);
/*  77 */     this.fourcc = new String(arrayOfByte1, "ascii");
/*  78 */     this.ckSize = readUnsignedInt();
/*     */ 
/*  80 */     this.avail = this.ckSize;
/*     */ 
/*  82 */     if ((getFormat().equals("RIFF")) || (getFormat().equals("LIST"))) {
/*  83 */       byte[] arrayOfByte2 = new byte[4];
/*  84 */       readFully(arrayOfByte2);
/*  85 */       this.riff_type = new String(arrayOfByte2, "ascii");
/*     */     }
/*     */   }
/*     */ 
/*     */   public long getFilePointer() throws IOException {
/*  90 */     return this.root.filepointer;
/*     */   }
/*     */ 
/*     */   public boolean hasNextChunk() throws IOException {
/*  94 */     if (this.lastiterator != null)
/*  95 */       this.lastiterator.finish();
/*  96 */     return this.avail != 0L;
/*     */   }
/*     */ 
/*     */   public RIFFReader nextChunk() throws IOException {
/* 100 */     if (this.lastiterator != null)
/* 101 */       this.lastiterator.finish();
/* 102 */     if (this.avail == 0L)
/* 103 */       return null;
/* 104 */     this.lastiterator = new RIFFReader(this);
/* 105 */     return this.lastiterator;
/*     */   }
/*     */ 
/*     */   public String getFormat() {
/* 109 */     return this.fourcc;
/*     */   }
/*     */ 
/*     */   public String getType() {
/* 113 */     return this.riff_type;
/*     */   }
/*     */ 
/*     */   public long getSize() {
/* 117 */     return this.ckSize;
/*     */   }
/*     */ 
/*     */   public int read() throws IOException {
/* 121 */     if (this.avail == 0L)
/* 122 */       return -1;
/* 123 */     int i = this.stream.read();
/* 124 */     if (i == -1)
/* 125 */       return -1;
/* 126 */     this.avail -= 1L;
/* 127 */     this.filepointer += 1L;
/* 128 */     return i;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 132 */     if (this.avail == 0L)
/* 133 */       return -1;
/* 134 */     if (paramInt2 > this.avail) {
/* 135 */       i = this.stream.read(paramArrayOfByte, paramInt1, (int)this.avail);
/* 136 */       if (i != -1)
/* 137 */         this.filepointer += i;
/* 138 */       this.avail = 0L;
/* 139 */       return i;
/*     */     }
/* 141 */     int i = this.stream.read(paramArrayOfByte, paramInt1, paramInt2);
/* 142 */     if (i == -1)
/* 143 */       return -1;
/* 144 */     this.avail -= i;
/* 145 */     this.filepointer += i;
/* 146 */     return i;
/*     */   }
/*     */ 
/*     */   public final void readFully(byte[] paramArrayOfByte) throws IOException
/*     */   {
/* 151 */     readFully(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public final void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 155 */     if (paramInt2 < 0)
/* 156 */       throw new IndexOutOfBoundsException();
/* 157 */     while (paramInt2 > 0) {
/* 158 */       int i = read(paramArrayOfByte, paramInt1, paramInt2);
/* 159 */       if (i < 0)
/* 160 */         throw new EOFException();
/* 161 */       if (i == 0)
/* 162 */         Thread.yield();
/* 163 */       paramInt1 += i;
/* 164 */       paramInt2 -= i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final long skipBytes(long paramLong) throws IOException {
/* 169 */     if (paramLong < 0L)
/* 170 */       return 0L;
/* 171 */     long l1 = 0L;
/* 172 */     while (l1 != paramLong) {
/* 173 */       long l2 = skip(paramLong - l1);
/* 174 */       if (l2 < 0L)
/*     */         break;
/* 176 */       if (l2 == 0L)
/* 177 */         Thread.yield();
/* 178 */       l1 += l2;
/*     */     }
/* 180 */     return l1;
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong) throws IOException {
/* 184 */     if (this.avail == 0L)
/* 185 */       return -1L;
/* 186 */     if (paramLong > this.avail) {
/* 187 */       l = this.stream.skip(this.avail);
/* 188 */       if (l != -1L)
/* 189 */         this.filepointer += l;
/* 190 */       this.avail = 0L;
/* 191 */       return l;
/*     */     }
/* 193 */     long l = this.stream.skip(paramLong);
/* 194 */     if (l == -1L)
/* 195 */       return -1L;
/* 196 */     this.avail -= l;
/* 197 */     this.filepointer += l;
/* 198 */     return l;
/*     */   }
/*     */ 
/*     */   public int available()
/*     */   {
/* 203 */     return (int)this.avail;
/*     */   }
/*     */ 
/*     */   public void finish() throws IOException {
/* 207 */     if (this.avail != 0L)
/* 208 */       skipBytes(this.avail);
/*     */   }
/*     */ 
/*     */   public String readString(int paramInt)
/*     */     throws IOException
/*     */   {
/* 214 */     byte[] arrayOfByte = new byte[paramInt];
/* 215 */     readFully(arrayOfByte);
/* 216 */     for (int i = 0; i < arrayOfByte.length; i++) {
/* 217 */       if (arrayOfByte[i] == 0) {
/* 218 */         return new String(arrayOfByte, 0, i, "ascii");
/*     */       }
/*     */     }
/* 221 */     return new String(arrayOfByte, "ascii");
/*     */   }
/*     */ 
/*     */   public byte readByte() throws IOException
/*     */   {
/* 226 */     int i = read();
/* 227 */     if (i < 0)
/* 228 */       throw new EOFException();
/* 229 */     return (byte)i;
/*     */   }
/*     */ 
/*     */   public short readShort() throws IOException
/*     */   {
/* 234 */     int i = read();
/* 235 */     int j = read();
/* 236 */     if (i < 0)
/* 237 */       throw new EOFException();
/* 238 */     if (j < 0)
/* 239 */       throw new EOFException();
/* 240 */     return (short)(i | j << 8);
/*     */   }
/*     */ 
/*     */   public int readInt() throws IOException
/*     */   {
/* 245 */     int i = read();
/* 246 */     int j = read();
/* 247 */     int k = read();
/* 248 */     int m = read();
/* 249 */     if (i < 0)
/* 250 */       throw new EOFException();
/* 251 */     if (j < 0)
/* 252 */       throw new EOFException();
/* 253 */     if (k < 0)
/* 254 */       throw new EOFException();
/* 255 */     if (m < 0)
/* 256 */       throw new EOFException();
/* 257 */     return i + (j << 8) | k << 16 | m << 24;
/*     */   }
/*     */ 
/*     */   public long readLong() throws IOException
/*     */   {
/* 262 */     long l1 = read();
/* 263 */     long l2 = read();
/* 264 */     long l3 = read();
/* 265 */     long l4 = read();
/* 266 */     long l5 = read();
/* 267 */     long l6 = read();
/* 268 */     long l7 = read();
/* 269 */     long l8 = read();
/* 270 */     if (l1 < 0L)
/* 271 */       throw new EOFException();
/* 272 */     if (l2 < 0L)
/* 273 */       throw new EOFException();
/* 274 */     if (l3 < 0L)
/* 275 */       throw new EOFException();
/* 276 */     if (l4 < 0L)
/* 277 */       throw new EOFException();
/* 278 */     if (l5 < 0L)
/* 279 */       throw new EOFException();
/* 280 */     if (l6 < 0L)
/* 281 */       throw new EOFException();
/* 282 */     if (l7 < 0L)
/* 283 */       throw new EOFException();
/* 284 */     if (l8 < 0L)
/* 285 */       throw new EOFException();
/* 286 */     return l1 | l2 << 8 | l3 << 16 | l4 << 24 | l5 << 32 | l6 << 40 | l7 << 48 | l8 << 56;
/*     */   }
/*     */ 
/*     */   public int readUnsignedByte()
/*     */     throws IOException
/*     */   {
/* 292 */     int i = read();
/* 293 */     if (i < 0)
/* 294 */       throw new EOFException();
/* 295 */     return i;
/*     */   }
/*     */ 
/*     */   public int readUnsignedShort() throws IOException
/*     */   {
/* 300 */     int i = read();
/* 301 */     int j = read();
/* 302 */     if (i < 0)
/* 303 */       throw new EOFException();
/* 304 */     if (j < 0)
/* 305 */       throw new EOFException();
/* 306 */     return i | j << 8;
/*     */   }
/*     */ 
/*     */   public long readUnsignedInt() throws IOException
/*     */   {
/* 311 */     long l1 = read();
/* 312 */     long l2 = read();
/* 313 */     long l3 = read();
/* 314 */     long l4 = read();
/* 315 */     if (l1 < 0L)
/* 316 */       throw new EOFException();
/* 317 */     if (l2 < 0L)
/* 318 */       throw new EOFException();
/* 319 */     if (l3 < 0L)
/* 320 */       throw new EOFException();
/* 321 */     if (l4 < 0L)
/* 322 */       throw new EOFException();
/* 323 */     return l1 + (l2 << 8) | l3 << 16 | l4 << 24;
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 327 */     finish();
/* 328 */     if (this == this.root)
/* 329 */       this.stream.close();
/* 330 */     this.stream = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.RIFFReader
 * JD-Core Version:    0.6.2
 */