/*     */ package javax.sql.rowset.serial;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.CharArrayReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Serializable;
/*     */ import java.io.Writer;
/*     */ import java.sql.Clob;
/*     */ import java.sql.SQLException;
/*     */ 
/*     */ public class SerialClob
/*     */   implements Clob, Serializable, Cloneable
/*     */ {
/*     */   private char[] buf;
/*     */   private final Clob clob;
/*     */   private long len;
/*     */   private final long origLen;
/*     */   static final long serialVersionUID = -1662519690087375313L;
/*     */ 
/*     */   public SerialClob(char[] paramArrayOfChar)
/*     */     throws SerialException, SQLException
/*     */   {
/* 101 */     this.len = paramArrayOfChar.length;
/* 102 */     this.buf = new char[(int)this.len];
/* 103 */     for (int i = 0; i < this.len; i++) {
/* 104 */       this.buf[i] = paramArrayOfChar[i];
/*     */     }
/* 106 */     this.origLen = this.len;
/* 107 */     this.clob = null;
/*     */   }
/*     */ 
/*     */   public SerialClob(Clob paramClob)
/*     */     throws SerialException, SQLException
/*     */   {
/* 138 */     if (paramClob == null) {
/* 139 */       throw new SQLException("Cannot instantiate a SerialClob object with a null Clob object");
/*     */     }
/*     */ 
/* 142 */     this.len = paramClob.length();
/* 143 */     this.clob = paramClob;
/* 144 */     this.buf = new char[(int)this.len];
/* 145 */     int i = 0;
/* 146 */     int j = 0;
/*     */     try {
/* 148 */       Reader localReader = paramClob.getCharacterStream(); Object localObject1 = null;
/*     */       try { if (localReader == null) {
/* 150 */           throw new SQLException("Invalid Clob object. The call to getCharacterStream returned null which cannot be serialized.");
/*     */         }
/*     */ 
/* 156 */         Object localObject2 = paramClob.getAsciiStream(); Object localObject3 = null;
/*     */         try { if (localObject2 == null)
/* 158 */             throw new SQLException("Invalid Clob object. The call to getAsciiStream returned null which cannot be serialized.");
/*     */         }
/*     */         catch (Throwable localThrowable4)
/*     */         {
/* 156 */           localObject3 = localThrowable4; throw localThrowable4;
/*     */         }
/*     */         finally
/*     */         {
/* 161 */           if (localObject2 != null) if (localObject3 != null) try { ((InputStream)localObject2).close(); } catch (Throwable localThrowable7) { localObject3.addSuppressed(localThrowable7); } else ((InputStream)localObject2).close();
/*     */         }
/* 163 */         localObject2 = new BufferedReader(localReader); localObject3 = null;
/*     */         try {
/*     */           do { i = ((Reader)localObject2).read(this.buf, j, (int)(this.len - j));
/* 166 */             j += i; }
/* 167 */           while (i > 0);
/*     */         }
/*     */         catch (Throwable localThrowable6)
/*     */         {
/* 163 */           localObject3 = localThrowable6; throw localThrowable6;
/*     */         }
/*     */         finally
/*     */         {
/* 168 */           if (localObject2 != null) if (localObject3 != null) try { ((Reader)localObject2).close(); } catch (Throwable localThrowable8) { localObject3.addSuppressed(localThrowable8); } else ((Reader)localObject2).close();
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable2)
/*     */       {
/* 148 */         localObject1 = localThrowable2; throw localThrowable2;
/*     */       }
/*     */       finally
/*     */       {
/* 169 */         if (localReader != null) if (localObject1 != null) try { localReader.close(); } catch (Throwable localThrowable9) { localObject1.addSuppressed(localThrowable9); } else localReader.close();  
/*     */       } } catch (IOException localIOException) { throw new SerialException("SerialClob: " + localIOException.getMessage()); }
/*     */ 
/*     */ 
/* 173 */     this.origLen = this.len;
/*     */   }
/*     */ 
/*     */   public long length()
/*     */     throws SerialException
/*     */   {
/* 185 */     return this.len;
/*     */   }
/*     */ 
/*     */   public Reader getCharacterStream()
/*     */     throws SerialException
/*     */   {
/* 199 */     return new CharArrayReader(this.buf);
/*     */   }
/*     */ 
/*     */   public InputStream getAsciiStream()
/*     */     throws SerialException, SQLException
/*     */   {
/* 219 */     if (this.clob != null) {
/* 220 */       return this.clob.getAsciiStream();
/*     */     }
/* 222 */     throw new SerialException("Unsupported operation. SerialClob cannot return a the CLOB value as an ascii stream, unless instantiated with a fully implemented Clob object.");
/*     */   }
/*     */ 
/*     */   public String getSubString(long paramLong, int paramInt)
/*     */     throws SerialException
/*     */   {
/* 254 */     if ((paramLong < 1L) || (paramLong > length())) {
/* 255 */       throw new SerialException("Invalid position in BLOB object set");
/*     */     }
/*     */ 
/* 258 */     if (paramLong - 1L + paramInt > length()) {
/* 259 */       throw new SerialException("Invalid position and substring length");
/*     */     }
/*     */     try
/*     */     {
/* 263 */       return new String(this.buf, (int)paramLong - 1, paramInt);
/*     */     }
/*     */     catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException) {
/* 266 */       throw new SerialException("StringIndexOutOfBoundsException: " + localStringIndexOutOfBoundsException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public long position(String paramString, long paramLong)
/*     */     throws SerialException, SQLException
/*     */   {
/* 296 */     if ((paramLong < 1L) || (paramLong > this.len)) {
/* 297 */       return -1L;
/*     */     }
/*     */ 
/* 300 */     char[] arrayOfChar = paramString.toCharArray();
/*     */ 
/* 302 */     int i = (int)paramLong - 1;
/* 303 */     int j = 0;
/* 304 */     long l = arrayOfChar.length;
/*     */ 
/* 306 */     while (i < this.len) {
/* 307 */       if (arrayOfChar[j] == this.buf[i]) {
/* 308 */         if (j + 1 == l) {
/* 309 */           return i + 1 - (l - 1L);
/*     */         }
/* 311 */         j++; i++;
/*     */       }
/* 313 */       else if (arrayOfChar[j] != this.buf[i]) {
/* 314 */         i++;
/*     */       }
/*     */     }
/* 317 */     return -1L;
/*     */   }
/*     */ 
/*     */   public long position(Clob paramClob, long paramLong)
/*     */     throws SerialException, SQLException
/*     */   {
/* 341 */     return position(paramClob.getSubString(1L, (int)paramClob.length()), paramLong);
/*     */   }
/*     */ 
/*     */   public int setString(long paramLong, String paramString)
/*     */     throws SerialException
/*     */   {
/* 363 */     return setString(paramLong, paramString, 0, paramString.length());
/*     */   }
/*     */ 
/*     */   public int setString(long paramLong, String paramString, int paramInt1, int paramInt2)
/*     */     throws SerialException
/*     */   {
/* 389 */     String str = paramString.substring(paramInt1);
/* 390 */     char[] arrayOfChar = str.toCharArray();
/*     */ 
/* 392 */     if ((paramInt1 < 0) || (paramInt1 > paramString.length())) {
/* 393 */       throw new SerialException("Invalid offset in byte array set");
/*     */     }
/*     */ 
/* 396 */     if ((paramLong < 1L) || (paramLong > length())) {
/* 397 */       throw new SerialException("Invalid position in BLOB object set");
/*     */     }
/*     */ 
/* 400 */     if (paramInt2 > this.origLen) {
/* 401 */       throw new SerialException("Buffer is not sufficient to hold the value");
/*     */     }
/*     */ 
/* 404 */     if (paramInt2 + paramInt1 > paramString.length())
/*     */     {
/* 406 */       throw new SerialException("Invalid OffSet. Cannot have combined offset  and length that is greater that the Blob buffer");
/*     */     }
/*     */ 
/* 410 */     int i = 0;
/* 411 */     paramLong -= 1L;
/* 412 */     while ((i < paramInt2) || (paramInt1 + i + 1 < paramString.length() - paramInt1)) {
/* 413 */       this.buf[((int)paramLong + i)] = arrayOfChar[(paramInt1 + i)];
/* 414 */       i++;
/*     */     }
/* 416 */     return i;
/*     */   }
/*     */ 
/*     */   public OutputStream setAsciiStream(long paramLong)
/*     */     throws SerialException, SQLException
/*     */   {
/* 439 */     if (this.clob.setAsciiStream(paramLong) != null) {
/* 440 */       return this.clob.setAsciiStream(paramLong);
/*     */     }
/* 442 */     throw new SerialException("Unsupported operation. SerialClob cannot return a writable ascii stream\n unless instantiated with a Clob object that has a setAsciiStream() implementation");
/*     */   }
/*     */ 
/*     */   public Writer setCharacterStream(long paramLong)
/*     */     throws SerialException, SQLException
/*     */   {
/* 469 */     if (this.clob.setCharacterStream(paramLong) != null) {
/* 470 */       return this.clob.setCharacterStream(paramLong);
/*     */     }
/* 472 */     throw new SerialException("Unsupported operation. SerialClob cannot return a writable character stream\n unless instantiated with a Clob object that has a setCharacterStream implementation");
/*     */   }
/*     */ 
/*     */   public void truncate(long paramLong)
/*     */     throws SerialException
/*     */   {
/* 492 */     if (paramLong > this.len) {
/* 493 */       throw new SerialException("Length more than what can be truncated");
/*     */     }
/*     */ 
/* 496 */     this.len = paramLong;
/*     */ 
/* 499 */     if (this.len == 0L)
/* 500 */       this.buf = new char[0];
/*     */     else
/* 502 */       this.buf = getSubString(1L, (int)this.len).toCharArray();
/*     */   }
/*     */ 
/*     */   public Reader getCharacterStream(long paramLong1, long paramLong2)
/*     */     throws SQLException
/*     */   {
/* 510 */     throw new UnsupportedOperationException("Not supported");
/*     */   }
/*     */ 
/*     */   public void free() throws SQLException {
/* 514 */     throw new UnsupportedOperationException("Not supported");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.serial.SerialClob
 * JD-Core Version:    0.6.2
 */