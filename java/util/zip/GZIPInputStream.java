/*     */ package java.util.zip;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.SequenceInputStream;
/*     */ 
/*     */ public class GZIPInputStream extends InflaterInputStream
/*     */ {
/*  47 */   protected CRC32 crc = new CRC32();
/*     */   protected boolean eos;
/*  54 */   private boolean closed = false;
/*     */   public static final int GZIP_MAGIC = 35615;
/*     */   private static final int FTEXT = 1;
/*     */   private static final int FHCRC = 2;
/*     */   private static final int FEXTRA = 4;
/*     */   private static final int FNAME = 8;
/*     */   private static final int FCOMMENT = 16;
/* 274 */   private byte[] tmpbuf = new byte[''];
/*     */ 
/*     */   private void ensureOpen()
/*     */     throws IOException
/*     */   {
/*  60 */     if (this.closed)
/*  61 */       throw new IOException("Stream closed");
/*     */   }
/*     */ 
/*     */   public GZIPInputStream(InputStream paramInputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/*  76 */     super(paramInputStream, new Inflater(true), paramInt);
/*  77 */     this.usesDefaultInflater = true;
/*  78 */     readHeader(paramInputStream);
/*     */   }
/*     */ 
/*     */   public GZIPInputStream(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  90 */     this(paramInputStream, 512);
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 112 */     ensureOpen();
/* 113 */     if (this.eos) {
/* 114 */       return -1;
/*     */     }
/* 116 */     int i = super.read(paramArrayOfByte, paramInt1, paramInt2);
/* 117 */     if (i == -1) {
/* 118 */       if (readTrailer())
/* 119 */         this.eos = true;
/*     */       else
/* 121 */         return read(paramArrayOfByte, paramInt1, paramInt2);
/*     */     }
/* 123 */     else this.crc.update(paramArrayOfByte, paramInt1, i);
/*     */ 
/* 125 */     return i;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 134 */     if (!this.closed) {
/* 135 */       super.close();
/* 136 */       this.eos = true;
/* 137 */       this.closed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private int readHeader(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 160 */     CheckedInputStream localCheckedInputStream = new CheckedInputStream(paramInputStream, this.crc);
/* 161 */     this.crc.reset();
/*     */ 
/* 163 */     if (readUShort(localCheckedInputStream) != 35615) {
/* 164 */       throw new ZipException("Not in GZIP format");
/*     */     }
/*     */ 
/* 167 */     if (readUByte(localCheckedInputStream) != 8) {
/* 168 */       throw new ZipException("Unsupported compression method");
/*     */     }
/*     */ 
/* 171 */     int i = readUByte(localCheckedInputStream);
/*     */ 
/* 173 */     skipBytes(localCheckedInputStream, 6);
/* 174 */     int j = 10;
/*     */     int k;
/* 176 */     if ((i & 0x4) == 4) {
/* 177 */       k = readUShort(localCheckedInputStream);
/* 178 */       skipBytes(localCheckedInputStream, k);
/* 179 */       j += k + 2;
/*     */     }
/*     */ 
/* 182 */     if ((i & 0x8) == 8) {
/*     */       do
/* 184 */         j++;
/* 185 */       while (readUByte(localCheckedInputStream) != 0);
/*     */     }
/*     */ 
/* 188 */     if ((i & 0x10) == 16) {
/*     */       do
/* 190 */         j++;
/* 191 */       while (readUByte(localCheckedInputStream) != 0);
/*     */     }
/*     */ 
/* 194 */     if ((i & 0x2) == 2) {
/* 195 */       k = (int)this.crc.getValue() & 0xFFFF;
/* 196 */       if (readUShort(localCheckedInputStream) != k) {
/* 197 */         throw new ZipException("Corrupt GZIP header");
/*     */       }
/* 199 */       j += 2;
/*     */     }
/* 201 */     this.crc.reset();
/* 202 */     return j;
/*     */   }
/*     */ 
/*     */   private boolean readTrailer()
/*     */     throws IOException
/*     */   {
/* 211 */     Object localObject = this.in;
/* 212 */     int i = this.inf.getRemaining();
/* 213 */     if (i > 0) {
/* 214 */       localObject = new SequenceInputStream(new ByteArrayInputStream(this.buf, this.len - i, i), (InputStream)localObject);
/*     */     }
/*     */ 
/* 218 */     if ((readUInt((InputStream)localObject) != this.crc.getValue()) || (readUInt((InputStream)localObject) != (this.inf.getBytesWritten() & 0xFFFFFFFF)))
/*     */     {
/* 221 */       throw new ZipException("Corrupt GZIP trailer");
/*     */     }
/*     */ 
/* 227 */     if ((this.in.available() > 0) || (i > 26)) {
/* 228 */       int j = 8;
/*     */       try {
/* 230 */         j += readHeader((InputStream)localObject);
/*     */       } catch (IOException localIOException) {
/* 232 */         return true;
/*     */       }
/* 234 */       this.inf.reset();
/* 235 */       if (i > j)
/* 236 */         this.inf.setInput(this.buf, this.len - i + j, i - j);
/* 237 */       return false;
/*     */     }
/* 239 */     return true;
/*     */   }
/*     */ 
/*     */   private long readUInt(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 246 */     long l = readUShort(paramInputStream);
/* 247 */     return readUShort(paramInputStream) << 16 | l;
/*     */   }
/*     */ 
/*     */   private int readUShort(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 254 */     int i = readUByte(paramInputStream);
/* 255 */     return readUByte(paramInputStream) << 8 | i;
/*     */   }
/*     */ 
/*     */   private int readUByte(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 262 */     int i = paramInputStream.read();
/* 263 */     if (i == -1) {
/* 264 */       throw new EOFException();
/*     */     }
/* 266 */     if ((i < -1) || (i > 255))
/*     */     {
/* 268 */       throw new IOException(this.in.getClass().getName() + ".read() returned value out of range -1..255: " + i);
/*     */     }
/*     */ 
/* 271 */     return i;
/*     */   }
/*     */ 
/*     */   private void skipBytes(InputStream paramInputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/* 281 */     while (paramInt > 0) {
/* 282 */       int i = paramInputStream.read(this.tmpbuf, 0, paramInt < this.tmpbuf.length ? paramInt : this.tmpbuf.length);
/* 283 */       if (i == -1) {
/* 284 */         throw new EOFException();
/*     */       }
/* 286 */       paramInt -= i;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.zip.GZIPInputStream
 * JD-Core Version:    0.6.2
 */