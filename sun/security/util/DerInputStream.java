/*     */ package sun.security.util;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Date;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class DerInputStream
/*     */ {
/*     */   DerInputBuffer buffer;
/*     */   public byte tag;
/*     */ 
/*     */   public DerInputStream(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  80 */     init(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public DerInputStream(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  95 */     init(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private void init(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 102 */     if ((paramInt1 + 2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 > paramArrayOfByte.length)) {
/* 103 */       throw new IOException("Encoding bytes too short");
/*     */     }
/*     */ 
/* 106 */     if (DerIndefLenConverter.isIndefinite(paramArrayOfByte[(paramInt1 + 1)])) {
/* 107 */       byte[] arrayOfByte = new byte[paramInt2];
/* 108 */       System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
/*     */ 
/* 110 */       DerIndefLenConverter localDerIndefLenConverter = new DerIndefLenConverter();
/* 111 */       this.buffer = new DerInputBuffer(localDerIndefLenConverter.convert(arrayOfByte));
/*     */     } else {
/* 113 */       this.buffer = new DerInputBuffer(paramArrayOfByte, paramInt1, paramInt2);
/* 114 */     }this.buffer.mark(2147483647);
/*     */   }
/*     */ 
/*     */   DerInputStream(DerInputBuffer paramDerInputBuffer) {
/* 118 */     this.buffer = paramDerInputBuffer;
/* 119 */     this.buffer.mark(2147483647);
/*     */   }
/*     */ 
/*     */   public DerInputStream subStream(int paramInt, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 134 */     DerInputBuffer localDerInputBuffer = this.buffer.dup();
/*     */ 
/* 136 */     localDerInputBuffer.truncate(paramInt);
/* 137 */     if (paramBoolean) {
/* 138 */       this.buffer.skip(paramInt);
/*     */     }
/* 140 */     return new DerInputStream(localDerInputBuffer);
/*     */   }
/*     */ 
/*     */   public byte[] toByteArray()
/*     */   {
/* 148 */     return this.buffer.toByteArray();
/*     */   }
/*     */ 
/*     */   public int getInteger()
/*     */     throws IOException
/*     */   {
/* 167 */     if (this.buffer.read() != 2) {
/* 168 */       throw new IOException("DER input, Integer tag error");
/*     */     }
/* 170 */     return this.buffer.getInteger(getLength(this.buffer));
/*     */   }
/*     */ 
/*     */   public BigInteger getBigInteger()
/*     */     throws IOException
/*     */   {
/* 179 */     if (this.buffer.read() != 2) {
/* 180 */       throw new IOException("DER input, Integer tag error");
/*     */     }
/* 182 */     return this.buffer.getBigInteger(getLength(this.buffer), false);
/*     */   }
/*     */ 
/*     */   public BigInteger getPositiveBigInteger()
/*     */     throws IOException
/*     */   {
/* 193 */     if (this.buffer.read() != 2) {
/* 194 */       throw new IOException("DER input, Integer tag error");
/*     */     }
/* 196 */     return this.buffer.getBigInteger(getLength(this.buffer), true);
/*     */   }
/*     */ 
/*     */   public int getEnumerated()
/*     */     throws IOException
/*     */   {
/* 205 */     if (this.buffer.read() != 10) {
/* 206 */       throw new IOException("DER input, Enumerated tag error");
/*     */     }
/* 208 */     return this.buffer.getInteger(getLength(this.buffer));
/*     */   }
/*     */ 
/*     */   public byte[] getBitString()
/*     */     throws IOException
/*     */   {
/* 216 */     if (this.buffer.read() != 3) {
/* 217 */       throw new IOException("DER input not an bit string");
/*     */     }
/* 219 */     return this.buffer.getBitString(getLength(this.buffer));
/*     */   }
/*     */ 
/*     */   public BitArray getUnalignedBitString()
/*     */     throws IOException
/*     */   {
/* 227 */     if (this.buffer.read() != 3) {
/* 228 */       throw new IOException("DER input not a bit string");
/*     */     }
/* 230 */     int i = getLength(this.buffer) - 1;
/*     */ 
/* 236 */     int j = i * 8 - this.buffer.read();
/*     */ 
/* 238 */     byte[] arrayOfByte = new byte[i];
/*     */ 
/* 240 */     if ((i != 0) && (this.buffer.read(arrayOfByte) != i))
/* 241 */       throw new IOException("short read of DER bit string");
/* 242 */     return new BitArray(j, arrayOfByte);
/*     */   }
/*     */ 
/*     */   public byte[] getOctetString()
/*     */     throws IOException
/*     */   {
/* 249 */     if (this.buffer.read() != 4) {
/* 250 */       throw new IOException("DER input not an octet string");
/*     */     }
/* 252 */     int i = getLength(this.buffer);
/* 253 */     byte[] arrayOfByte = new byte[i];
/* 254 */     if ((i != 0) && (this.buffer.read(arrayOfByte) != i)) {
/* 255 */       throw new IOException("short read of DER octet string");
/*     */     }
/* 257 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public void getBytes(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 264 */     if ((paramArrayOfByte.length != 0) && (this.buffer.read(paramArrayOfByte) != paramArrayOfByte.length))
/* 265 */       throw new IOException("short read of DER octet string");
/*     */   }
/*     */ 
/*     */   public void getNull()
/*     */     throws IOException
/*     */   {
/* 273 */     if ((this.buffer.read() != 5) || (this.buffer.read() != 0))
/* 274 */       throw new IOException("getNull, bad data");
/*     */   }
/*     */ 
/*     */   public ObjectIdentifier getOID()
/*     */     throws IOException
/*     */   {
/* 281 */     return new ObjectIdentifier(this);
/*     */   }
/*     */ 
/*     */   public DerValue[] getSequence(int paramInt)
/*     */     throws IOException
/*     */   {
/* 295 */     this.tag = ((byte)this.buffer.read());
/* 296 */     if (this.tag != 48)
/* 297 */       throw new IOException("Sequence tag error");
/* 298 */     return readVector(paramInt);
/*     */   }
/*     */ 
/*     */   public DerValue[] getSet(int paramInt)
/*     */     throws IOException
/*     */   {
/* 312 */     this.tag = ((byte)this.buffer.read());
/* 313 */     if (this.tag != 49)
/* 314 */       throw new IOException("Set tag error");
/* 315 */     return readVector(paramInt);
/*     */   }
/*     */ 
/*     */   public DerValue[] getSet(int paramInt, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 331 */     this.tag = ((byte)this.buffer.read());
/* 332 */     if ((!paramBoolean) && 
/* 333 */       (this.tag != 49)) {
/* 334 */       throw new IOException("Set tag error");
/*     */     }
/*     */ 
/* 337 */     return readVector(paramInt);
/*     */   }
/*     */ 
/*     */   protected DerValue[] readVector(int paramInt)
/*     */     throws IOException
/*     */   {
/* 348 */     int i = (byte)this.buffer.read();
/* 349 */     int j = getLength(i & 0xFF, this.buffer);
/*     */ 
/* 351 */     if (j == -1)
/*     */     {
/* 353 */       int k = this.buffer.available();
/* 354 */       int m = 2;
/* 355 */       byte[] arrayOfByte = new byte[k + m];
/* 356 */       arrayOfByte[0] = this.tag;
/* 357 */       arrayOfByte[1] = i;
/* 358 */       DataInputStream localDataInputStream = new DataInputStream(this.buffer);
/* 359 */       localDataInputStream.readFully(arrayOfByte, m, k);
/* 360 */       localDataInputStream.close();
/* 361 */       localObject = new DerIndefLenConverter();
/* 362 */       this.buffer = new DerInputBuffer(((DerIndefLenConverter)localObject).convert(arrayOfByte));
/* 363 */       if (this.tag != this.buffer.read()) {
/* 364 */         throw new IOException("Indefinite length encoding not supported");
/*     */       }
/* 366 */       j = getLength(this.buffer);
/*     */     }
/*     */ 
/* 369 */     if (j == 0)
/*     */     {
/* 372 */       return new DerValue[0];
/*     */     }
/*     */     DerInputStream localDerInputStream;
/* 378 */     if (this.buffer.available() == j)
/* 379 */       localDerInputStream = this;
/*     */     else {
/* 381 */       localDerInputStream = subStream(j, true);
/*     */     }
/*     */ 
/* 386 */     Vector localVector = new Vector(paramInt);
/*     */     do
/*     */     {
/* 390 */       DerValue localDerValue = new DerValue(localDerInputStream.buffer);
/* 391 */       localVector.addElement(localDerValue);
/* 392 */     }while (localDerInputStream.available() > 0);
/*     */ 
/* 394 */     if (localDerInputStream.available() != 0) {
/* 395 */       throw new IOException("extra data at end of vector");
/*     */     }
/*     */ 
/* 400 */     int i1 = localVector.size();
/* 401 */     Object localObject = new DerValue[i1];
/*     */ 
/* 403 */     for (int n = 0; n < i1; n++) {
/* 404 */       localObject[n] = ((DerValue)localVector.elementAt(n));
/*     */     }
/* 406 */     return localObject;
/*     */   }
/*     */ 
/*     */   public DerValue getDerValue()
/*     */     throws IOException
/*     */   {
/* 417 */     return new DerValue(this.buffer);
/*     */   }
/*     */ 
/*     */   public String getUTF8String()
/*     */     throws IOException
/*     */   {
/* 424 */     return readString((byte)12, "UTF-8", "UTF8");
/*     */   }
/*     */ 
/*     */   public String getPrintableString()
/*     */     throws IOException
/*     */   {
/* 431 */     return readString((byte)19, "Printable", "ASCII");
/*     */   }
/*     */ 
/*     */   public String getT61String()
/*     */     throws IOException
/*     */   {
/* 442 */     return readString((byte)20, "T61", "ISO-8859-1");
/*     */   }
/*     */ 
/*     */   public String getIA5String()
/*     */     throws IOException
/*     */   {
/* 449 */     return readString((byte)22, "IA5", "ASCII");
/*     */   }
/*     */ 
/*     */   public String getBMPString()
/*     */     throws IOException
/*     */   {
/* 456 */     return readString((byte)30, "BMP", "UnicodeBigUnmarked");
/*     */   }
/*     */ 
/*     */   public String getGeneralString()
/*     */     throws IOException
/*     */   {
/* 464 */     return readString((byte)27, "General", "ASCII");
/*     */   }
/*     */ 
/*     */   private String readString(byte paramByte, String paramString1, String paramString2)
/*     */     throws IOException
/*     */   {
/* 479 */     if (this.buffer.read() != paramByte) {
/* 480 */       throw new IOException("DER input not a " + paramString1 + " string");
/*     */     }
/*     */ 
/* 483 */     int i = getLength(this.buffer);
/* 484 */     byte[] arrayOfByte = new byte[i];
/* 485 */     if ((i != 0) && (this.buffer.read(arrayOfByte) != i)) {
/* 486 */       throw new IOException("short read of DER " + paramString1 + " string");
/*     */     }
/*     */ 
/* 489 */     return new String(arrayOfByte, paramString2);
/*     */   }
/*     */ 
/*     */   public Date getUTCTime()
/*     */     throws IOException
/*     */   {
/* 496 */     if (this.buffer.read() != 23)
/* 497 */       throw new IOException("DER input, UTCtime tag invalid ");
/* 498 */     return this.buffer.getUTCTime(getLength(this.buffer));
/*     */   }
/*     */ 
/*     */   public Date getGeneralizedTime()
/*     */     throws IOException
/*     */   {
/* 505 */     if (this.buffer.read() != 24)
/* 506 */       throw new IOException("DER input, GeneralizedTime tag invalid ");
/* 507 */     return this.buffer.getGeneralizedTime(getLength(this.buffer));
/*     */   }
/*     */ 
/*     */   int getByte()
/*     */     throws IOException
/*     */   {
/* 515 */     return 0xFF & this.buffer.read();
/*     */   }
/*     */ 
/*     */   public int peekByte() throws IOException {
/* 519 */     return this.buffer.peek();
/*     */   }
/*     */ 
/*     */   int getLength() throws IOException
/*     */   {
/* 524 */     return getLength(this.buffer);
/*     */   }
/*     */ 
/*     */   static int getLength(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 535 */     return getLength(paramInputStream.read(), paramInputStream);
/*     */   }
/*     */ 
/*     */   static int getLength(int paramInt, InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 548 */     int j = paramInt;
/*     */     int i;
/* 549 */     if ((j & 0x80) == 0) {
/* 550 */       i = j;
/*     */     } else {
/* 552 */       j &= 127;
/*     */ 
/* 558 */       if (j == 0)
/* 559 */         return -1;
/* 560 */       if ((j < 0) || (j > 4)) {
/* 561 */         throw new IOException("DerInputStream.getLength(): lengthTag=" + j + ", " + (j < 0 ? "incorrect DER encoding." : "too big."));
/*     */       }
/*     */ 
/* 565 */       for (i = 0; j > 0; j--) {
/* 566 */         i <<= 8;
/* 567 */         i += (0xFF & paramInputStream.read());
/*     */       }
/* 569 */       if (i < 0) {
/* 570 */         throw new IOException("DerInputStream.getLength(): Invalid length bytes");
/*     */       }
/*     */     }
/*     */ 
/* 574 */     return i;
/*     */   }
/*     */ 
/*     */   public void mark(int paramInt)
/*     */   {
/* 581 */     this.buffer.mark(paramInt);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 589 */     this.buffer.reset();
/*     */   }
/*     */ 
/*     */   public int available()
/*     */   {
/* 597 */     return this.buffer.available();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.DerInputStream
 * JD-Core Version:    0.6.2
 */