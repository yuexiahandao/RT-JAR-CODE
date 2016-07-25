/*     */ package sun.security.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Date;
/*     */ import sun.util.calendar.CalendarDate;
/*     */ import sun.util.calendar.CalendarSystem;
/*     */ import sun.util.calendar.Gregorian;
/*     */ 
/*     */ class DerInputBuffer extends ByteArrayInputStream
/*     */   implements Cloneable
/*     */ {
/*     */   DerInputBuffer(byte[] paramArrayOfByte)
/*     */   {
/*  47 */     super(paramArrayOfByte);
/*     */   }
/*     */   DerInputBuffer(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/*  50 */     super(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   DerInputBuffer dup() {
/*     */     try {
/*  55 */       DerInputBuffer localDerInputBuffer = (DerInputBuffer)clone();
/*     */ 
/*  57 */       localDerInputBuffer.mark(2147483647);
/*  58 */       return localDerInputBuffer;
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*  60 */       throw new IllegalArgumentException(localCloneNotSupportedException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   byte[] toByteArray() {
/*  65 */     int i = available();
/*  66 */     if (i <= 0)
/*  67 */       return null;
/*  68 */     byte[] arrayOfByte = new byte[i];
/*     */ 
/*  70 */     System.arraycopy(this.buf, this.pos, arrayOfByte, 0, i);
/*  71 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   int peek() throws IOException {
/*  75 */     if (this.pos >= this.count) {
/*  76 */       throw new IOException("out of data");
/*     */     }
/*  78 */     return this.buf[this.pos];
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  86 */     if ((paramObject instanceof DerInputBuffer)) {
/*  87 */       return equals((DerInputBuffer)paramObject);
/*     */     }
/*  89 */     return false;
/*     */   }
/*     */ 
/*     */   boolean equals(DerInputBuffer paramDerInputBuffer) {
/*  93 */     if (this == paramDerInputBuffer) {
/*  94 */       return true;
/*     */     }
/*  96 */     int i = available();
/*  97 */     if (paramDerInputBuffer.available() != i)
/*  98 */       return false;
/*  99 */     for (int j = 0; j < i; j++) {
/* 100 */       if (this.buf[(this.pos + j)] != paramDerInputBuffer.buf[(paramDerInputBuffer.pos + j)]) {
/* 101 */         return false;
/*     */       }
/*     */     }
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 113 */     int i = 0;
/*     */ 
/* 115 */     int j = available();
/* 116 */     int k = this.pos;
/*     */ 
/* 118 */     for (int m = 0; m < j; m++)
/* 119 */       i += this.buf[(k + m)] * m;
/* 120 */     return i;
/*     */   }
/*     */ 
/*     */   void truncate(int paramInt) throws IOException {
/* 124 */     if (paramInt > available())
/* 125 */       throw new IOException("insufficient data");
/* 126 */     this.count = (this.pos + paramInt);
/*     */   }
/*     */ 
/*     */   BigInteger getBigInteger(int paramInt, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 138 */     if (paramInt > available()) {
/* 139 */       throw new IOException("short read of integer");
/*     */     }
/* 141 */     if (paramInt == 0) {
/* 142 */       throw new IOException("Invalid encoding: zero length Int value");
/*     */     }
/*     */ 
/* 145 */     byte[] arrayOfByte = new byte[paramInt];
/*     */ 
/* 147 */     System.arraycopy(this.buf, this.pos, arrayOfByte, 0, paramInt);
/* 148 */     skip(paramInt);
/*     */ 
/* 150 */     if (paramBoolean) {
/* 151 */       return new BigInteger(1, arrayOfByte);
/*     */     }
/* 153 */     return new BigInteger(arrayOfByte);
/*     */   }
/*     */ 
/*     */   public int getInteger(int paramInt)
/*     */     throws IOException
/*     */   {
/* 168 */     BigInteger localBigInteger = getBigInteger(paramInt, false);
/* 169 */     if (localBigInteger.compareTo(BigInteger.valueOf(-2147483648L)) < 0) {
/* 170 */       throw new IOException("Integer below minimum valid value");
/*     */     }
/* 172 */     if (localBigInteger.compareTo(BigInteger.valueOf(2147483647L)) > 0) {
/* 173 */       throw new IOException("Integer exceeds maximum valid value");
/*     */     }
/* 175 */     return localBigInteger.intValue();
/*     */   }
/*     */ 
/*     */   public byte[] getBitString(int paramInt)
/*     */     throws IOException
/*     */   {
/* 183 */     if (paramInt > available()) {
/* 184 */       throw new IOException("short read of bit string");
/*     */     }
/* 186 */     if (paramInt == 0) {
/* 187 */       throw new IOException("Invalid encoding: zero length bit string");
/*     */     }
/*     */ 
/* 190 */     int i = this.buf[this.pos];
/* 191 */     if ((i < 0) || (i > 7)) {
/* 192 */       throw new IOException("Invalid number of padding bits");
/*     */     }
/*     */ 
/* 195 */     byte[] arrayOfByte = new byte[paramInt - 1];
/* 196 */     System.arraycopy(this.buf, this.pos + 1, arrayOfByte, 0, paramInt - 1);
/* 197 */     if (i != 0)
/*     */     {
/*     */       int tmp94_93 = (paramInt - 2);
/*     */       byte[] tmp94_90 = arrayOfByte; tmp94_90[tmp94_93] = ((byte)(tmp94_90[tmp94_93] & 255 << i));
/*     */     }
/* 201 */     skip(paramInt);
/* 202 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   byte[] getBitString()
/*     */     throws IOException
/*     */   {
/* 209 */     return getBitString(available());
/*     */   }
/*     */ 
/*     */   BitArray getUnalignedBitString()
/*     */     throws IOException
/*     */   {
/* 217 */     if (this.pos >= this.count) {
/* 218 */       return null;
/*     */     }
/*     */ 
/* 223 */     int i = available();
/* 224 */     int j = this.buf[this.pos] & 0xFF;
/* 225 */     if (j > 7) {
/* 226 */       throw new IOException("Invalid value for unused bits: " + j);
/*     */     }
/* 228 */     byte[] arrayOfByte = new byte[i - 1];
/*     */ 
/* 230 */     int k = arrayOfByte.length == 0 ? 0 : arrayOfByte.length * 8 - j;
/*     */ 
/* 232 */     System.arraycopy(this.buf, this.pos + 1, arrayOfByte, 0, i - 1);
/*     */ 
/* 234 */     BitArray localBitArray = new BitArray(k, arrayOfByte);
/* 235 */     this.pos = this.count;
/* 236 */     return localBitArray;
/*     */   }
/*     */ 
/*     */   public Date getUTCTime(int paramInt)
/*     */     throws IOException
/*     */   {
/* 245 */     if (paramInt > available()) {
/* 246 */       throw new IOException("short read of DER UTC Time");
/*     */     }
/* 248 */     if ((paramInt < 11) || (paramInt > 17)) {
/* 249 */       throw new IOException("DER UTC Time length error");
/*     */     }
/* 251 */     return getTime(paramInt, false);
/*     */   }
/*     */ 
/*     */   public Date getGeneralizedTime(int paramInt)
/*     */     throws IOException
/*     */   {
/* 260 */     if (paramInt > available()) {
/* 261 */       throw new IOException("short read of DER Generalized Time");
/*     */     }
/* 263 */     if ((paramInt < 13) || (paramInt > 23)) {
/* 264 */       throw new IOException("DER Generalized Time length error");
/*     */     }
/* 266 */     return getTime(paramInt, true);
/*     */   }
/*     */ 
/*     */   private Date getTime(int paramInt, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 297 */     String str = null;
/*     */     int i;
/* 299 */     if (paramBoolean) {
/* 300 */       str = "Generalized";
/* 301 */       i = 1000 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 302 */       i += 100 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 303 */       i += 10 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 304 */       i += Character.digit((char)this.buf[(this.pos++)], 10);
/* 305 */       paramInt -= 2;
/*     */     } else {
/* 307 */       str = "UTC";
/* 308 */       i = 10 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 309 */       i += Character.digit((char)this.buf[(this.pos++)], 10);
/*     */ 
/* 311 */       if (i < 50)
/* 312 */         i += 2000;
/*     */       else {
/* 314 */         i += 1900;
/*     */       }
/*     */     }
/* 317 */     int j = 10 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 318 */     j += Character.digit((char)this.buf[(this.pos++)], 10);
/*     */ 
/* 320 */     int k = 10 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 321 */     k += Character.digit((char)this.buf[(this.pos++)], 10);
/*     */ 
/* 323 */     int m = 10 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 324 */     m += Character.digit((char)this.buf[(this.pos++)], 10);
/*     */ 
/* 326 */     int n = 10 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 327 */     n += Character.digit((char)this.buf[(this.pos++)], 10);
/*     */ 
/* 329 */     paramInt -= 10;
/*     */ 
/* 337 */     int i2 = 0;
/*     */     int i1;
/* 338 */     if ((paramInt > 2) && (paramInt < 12)) {
/* 339 */       i1 = 10 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 340 */       i1 += Character.digit((char)this.buf[(this.pos++)], 10);
/* 341 */       paramInt -= 2;
/*     */ 
/* 343 */       if ((this.buf[this.pos] == 46) || (this.buf[this.pos] == 44)) {
/* 344 */         paramInt--;
/* 345 */         this.pos += 1;
/*     */ 
/* 347 */         int i3 = 0;
/* 348 */         int i4 = this.pos;
/*     */ 
/* 350 */         while ((this.buf[i4] != 90) && (this.buf[i4] != 43) && (this.buf[i4] != 45))
/*     */         {
/* 352 */           i4++;
/* 353 */           i3++;
/*     */         }
/* 355 */         switch (i3) {
/*     */         case 3:
/* 357 */           i2 += 100 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 358 */           i2 += 10 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 359 */           i2 += Character.digit((char)this.buf[(this.pos++)], 10);
/* 360 */           break;
/*     */         case 2:
/* 362 */           i2 += 100 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 363 */           i2 += 10 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 364 */           break;
/*     */         case 1:
/* 366 */           i2 += 100 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 367 */           break;
/*     */         default:
/* 369 */           throw new IOException("Parse " + str + " time, unsupported precision for seconds value");
/*     */         }
/*     */ 
/* 372 */         paramInt -= i3;
/*     */       }
/*     */     } else {
/* 375 */       i1 = 0;
/*     */     }
/* 377 */     if ((j == 0) || (k == 0) || (j > 12) || (k > 31) || (m >= 24) || (n >= 60) || (i1 >= 60))
/*     */     {
/* 380 */       throw new IOException("Parse " + str + " time, invalid format");
/*     */     }
/*     */ 
/* 386 */     Gregorian localGregorian = CalendarSystem.getGregorianCalendar();
/* 387 */     CalendarDate localCalendarDate = localGregorian.newCalendarDate(null);
/* 388 */     localCalendarDate.setDate(i, j, k);
/* 389 */     localCalendarDate.setTimeOfDay(m, n, i1, i2);
/* 390 */     long l = localGregorian.getTime(localCalendarDate);
/*     */ 
/* 395 */     if ((paramInt != 1) && (paramInt != 5))
/* 396 */       throw new IOException("Parse " + str + " time, invalid offset");
/*     */     int i5;
/*     */     int i6;
/* 400 */     switch (this.buf[(this.pos++)]) {
/*     */     case 43:
/* 402 */       i5 = 10 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 403 */       i5 += Character.digit((char)this.buf[(this.pos++)], 10);
/* 404 */       i6 = 10 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 405 */       i6 += Character.digit((char)this.buf[(this.pos++)], 10);
/*     */ 
/* 407 */       if ((i5 >= 24) || (i6 >= 60)) {
/* 408 */         throw new IOException("Parse " + str + " time, +hhmm");
/*     */       }
/* 410 */       l -= (i5 * 60 + i6) * 60 * 1000;
/* 411 */       break;
/*     */     case 45:
/* 414 */       i5 = 10 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 415 */       i5 += Character.digit((char)this.buf[(this.pos++)], 10);
/* 416 */       i6 = 10 * Character.digit((char)this.buf[(this.pos++)], 10);
/* 417 */       i6 += Character.digit((char)this.buf[(this.pos++)], 10);
/*     */ 
/* 419 */       if ((i5 >= 24) || (i6 >= 60)) {
/* 420 */         throw new IOException("Parse " + str + " time, -hhmm");
/*     */       }
/* 422 */       l += (i5 * 60 + i6) * 60 * 1000;
/* 423 */       break;
/*     */     case 90:
/* 426 */       break;
/*     */     default:
/* 429 */       throw new IOException("Parse " + str + " time, garbage offset");
/*     */     }
/* 431 */     return new Date(l);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.DerInputBuffer
 * JD-Core Version:    0.6.2
 */