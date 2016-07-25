/*     */ package javax.sql.rowset.serial;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.sql.Blob;
/*     */ import java.sql.SQLException;
/*     */ 
/*     */ public class SerialBlob
/*     */   implements Blob, Serializable, Cloneable
/*     */ {
/*     */   private byte[] buf;
/*     */   private Blob blob;
/*     */   private long len;
/*     */   private long origLen;
/*     */   static final long serialVersionUID = -8144641928112860441L;
/*     */ 
/*     */   public SerialBlob(byte[] paramArrayOfByte)
/*     */     throws SerialException, SQLException
/*     */   {
/* 100 */     this.len = paramArrayOfByte.length;
/* 101 */     this.buf = new byte[(int)this.len];
/* 102 */     for (int i = 0; i < this.len; i++) {
/* 103 */       this.buf[i] = paramArrayOfByte[i];
/*     */     }
/* 105 */     this.origLen = this.len;
/*     */   }
/*     */ 
/*     */   public SerialBlob(Blob paramBlob)
/*     */     throws SerialException, SQLException
/*     */   {
/* 130 */     if (paramBlob == null) {
/* 131 */       throw new SQLException("Cannot instantiate a SerialBlob object with a null Blob object");
/*     */     }
/*     */ 
/* 135 */     this.len = paramBlob.length();
/* 136 */     this.buf = paramBlob.getBytes(1L, (int)this.len);
/* 137 */     this.blob = paramBlob;
/*     */ 
/* 141 */     this.origLen = this.len;
/*     */   }
/*     */ 
/*     */   public byte[] getBytes(long paramLong, int paramInt)
/*     */     throws SerialException
/*     */   {
/* 165 */     if (paramInt > this.len) {
/* 166 */       paramInt = (int)this.len;
/*     */     }
/*     */ 
/* 169 */     if ((paramLong < 1L) || (this.len - paramLong < 0L)) {
/* 170 */       throw new SerialException("Invalid arguments: position cannot be less than 1 or greater than the length of the SerialBlob");
/*     */     }
/*     */ 
/* 174 */     paramLong -= 1L;
/*     */ 
/* 176 */     byte[] arrayOfByte = new byte[paramInt];
/*     */ 
/* 178 */     for (int i = 0; i < paramInt; i++) {
/* 179 */       arrayOfByte[i] = this.buf[((int)paramLong)];
/* 180 */       paramLong += 1L;
/*     */     }
/* 182 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public long length()
/*     */     throws SerialException
/*     */   {
/* 194 */     return this.len;
/*     */   }
/*     */ 
/*     */   public InputStream getBinaryStream()
/*     */     throws SerialException
/*     */   {
/* 209 */     ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(this.buf);
/* 210 */     return localByteArrayInputStream;
/*     */   }
/*     */ 
/*     */   public long position(byte[] paramArrayOfByte, long paramLong)
/*     */     throws SerialException, SQLException
/*     */   {
/* 235 */     if ((paramLong < 1L) || (paramLong > this.len)) {
/* 236 */       return -1L;
/*     */     }
/*     */ 
/* 239 */     int i = (int)paramLong - 1;
/* 240 */     int j = 0;
/* 241 */     long l = paramArrayOfByte.length;
/*     */ 
/* 243 */     while (i < this.len) {
/* 244 */       if (paramArrayOfByte[j] == this.buf[i]) {
/* 245 */         if (j + 1 == l) {
/* 246 */           return i + 1 - (l - 1L);
/*     */         }
/* 248 */         j++; i++;
/* 249 */       } else if (paramArrayOfByte[j] != this.buf[i]) {
/* 250 */         i++;
/*     */       }
/*     */     }
/* 253 */     return -1L;
/*     */   }
/*     */ 
/*     */   public long position(Blob paramBlob, long paramLong)
/*     */     throws SerialException, SQLException
/*     */   {
/* 278 */     return position(paramBlob.getBytes(1L, (int)paramBlob.length()), paramLong);
/*     */   }
/*     */ 
/*     */   public int setBytes(long paramLong, byte[] paramArrayOfByte)
/*     */     throws SerialException, SQLException
/*     */   {
/* 302 */     return setBytes(paramLong, paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public int setBytes(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws SerialException, SQLException
/*     */   {
/* 338 */     if ((paramInt1 < 0) || (paramInt1 > paramArrayOfByte.length)) {
/* 339 */       throw new SerialException("Invalid offset in byte array set");
/*     */     }
/*     */ 
/* 342 */     if ((paramLong < 1L) || (paramLong > length())) {
/* 343 */       throw new SerialException("Invalid position in BLOB object set");
/*     */     }
/*     */ 
/* 346 */     if (paramInt2 > this.origLen) {
/* 347 */       throw new SerialException("Buffer is not sufficient to hold the value");
/*     */     }
/*     */ 
/* 350 */     if (paramInt2 + paramInt1 > paramArrayOfByte.length) {
/* 351 */       throw new SerialException("Invalid OffSet. Cannot have combined offset and length that is greater that the Blob buffer");
/*     */     }
/*     */ 
/* 355 */     int i = 0;
/* 356 */     paramLong -= 1L;
/* 357 */     while ((i < paramInt2) || (paramInt1 + i + 1 < paramArrayOfByte.length - paramInt1)) {
/* 358 */       this.buf[((int)paramLong + i)] = paramArrayOfByte[(paramInt1 + i)];
/* 359 */       i++;
/*     */     }
/* 361 */     return i;
/*     */   }
/*     */ 
/*     */   public OutputStream setBinaryStream(long paramLong)
/*     */     throws SerialException, SQLException
/*     */   {
/* 385 */     if (this.blob.setBinaryStream(paramLong) != null) {
/* 386 */       return this.blob.setBinaryStream(paramLong);
/*     */     }
/* 388 */     throw new SerialException("Unsupported operation. SerialBlob cannot return a writable binary stream, unless instantiated with a Blob object that provides a setBinaryStream() implementation");
/*     */   }
/*     */ 
/*     */   public void truncate(long paramLong)
/*     */     throws SerialException
/*     */   {
/* 406 */     if (paramLong > this.len) {
/* 407 */       throw new SerialException("Length more than what can be truncated");
/*     */     }
/* 409 */     if ((int)paramLong == 0) {
/* 410 */       this.buf = new byte[0];
/* 411 */       this.len = paramLong;
/*     */     } else {
/* 413 */       this.len = paramLong;
/* 414 */       this.buf = getBytes(1L, (int)this.len);
/*     */     }
/*     */   }
/*     */ 
/*     */   public InputStream getBinaryStream(long paramLong1, long paramLong2)
/*     */     throws SQLException
/*     */   {
/* 434 */     throw new UnsupportedOperationException("Not supported");
/*     */   }
/*     */ 
/*     */   public void free()
/*     */     throws SQLException
/*     */   {
/* 449 */     throw new UnsupportedOperationException("Not supported");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.serial.SerialBlob
 * JD-Core Version:    0.6.2
 */