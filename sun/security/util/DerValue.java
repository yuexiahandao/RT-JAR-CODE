/*     */ package sun.security.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Date;
/*     */ import sun.misc.IOUtils;
/*     */ 
/*     */ public class DerValue
/*     */ {
/*     */   public static final byte TAG_UNIVERSAL = 0;
/*     */   public static final byte TAG_APPLICATION = 64;
/*     */   public static final byte TAG_CONTEXT = -128;
/*     */   public static final byte TAG_PRIVATE = -64;
/*     */   public byte tag;
/*     */   protected DerInputBuffer buffer;
/*     */   public final DerInputStream data;
/*     */   private int length;
/*     */   public static final byte tag_Boolean = 1;
/*     */   public static final byte tag_Integer = 2;
/*     */   public static final byte tag_BitString = 3;
/*     */   public static final byte tag_OctetString = 4;
/*     */   public static final byte tag_Null = 5;
/*     */   public static final byte tag_ObjectId = 6;
/*     */   public static final byte tag_Enumerated = 10;
/*     */   public static final byte tag_UTF8String = 12;
/*     */   public static final byte tag_PrintableString = 19;
/*     */   public static final byte tag_T61String = 20;
/*     */   public static final byte tag_IA5String = 22;
/*     */   public static final byte tag_UtcTime = 23;
/*     */   public static final byte tag_GeneralizedTime = 24;
/*     */   public static final byte tag_GeneralString = 27;
/*     */   public static final byte tag_UniversalString = 28;
/*     */   public static final byte tag_BMPString = 30;
/*     */   public static final byte tag_Sequence = 48;
/*     */   public static final byte tag_SequenceOf = 48;
/*     */   public static final byte tag_Set = 49;
/*     */   public static final byte tag_SetOf = 49;
/*     */ 
/*     */   public boolean isUniversal()
/*     */   {
/* 167 */     return (this.tag & 0xC0) == 0;
/*     */   }
/*     */ 
/*     */   public boolean isApplication()
/*     */   {
/* 172 */     return (this.tag & 0xC0) == 64;
/*     */   }
/*     */ 
/*     */   public boolean isContextSpecific()
/*     */   {
/* 178 */     return (this.tag & 0xC0) == 128;
/*     */   }
/*     */ 
/*     */   public boolean isContextSpecific(byte paramByte)
/*     */   {
/* 184 */     if (!isContextSpecific()) {
/* 185 */       return false;
/*     */     }
/* 187 */     return (this.tag & 0x1F) == paramByte;
/*     */   }
/*     */   boolean isPrivate() {
/* 190 */     return (this.tag & 0xC0) == 192;
/*     */   }
/*     */   public boolean isConstructed() {
/* 193 */     return (this.tag & 0x20) == 32;
/*     */   }
/*     */ 
/*     */   public boolean isConstructed(byte paramByte)
/*     */   {
/* 199 */     if (!isConstructed()) {
/* 200 */       return false;
/*     */     }
/* 202 */     return (this.tag & 0x1F) == paramByte;
/*     */   }
/*     */ 
/*     */   public DerValue(String paramString)
/*     */     throws IOException
/*     */   {
/* 209 */     int i = 1;
/* 210 */     for (int j = 0; j < paramString.length(); j++) {
/* 211 */       if (!isPrintableStringChar(paramString.charAt(j))) {
/* 212 */         i = 0;
/* 213 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 217 */     this.data = init((byte)(i != 0 ? 19 : 12), paramString);
/*     */   }
/*     */ 
/*     */   public DerValue(byte paramByte, String paramString)
/*     */     throws IOException
/*     */   {
/* 226 */     this.data = init(paramByte, paramString);
/*     */   }
/*     */ 
/*     */   public DerValue(byte paramByte, byte[] paramArrayOfByte)
/*     */   {
/* 236 */     this.tag = paramByte;
/* 237 */     this.buffer = new DerInputBuffer((byte[])paramArrayOfByte.clone());
/* 238 */     this.length = paramArrayOfByte.length;
/* 239 */     this.data = new DerInputStream(this.buffer);
/* 240 */     this.data.mark(2147483647);
/*     */   }
/*     */ 
/*     */   DerValue(DerInputBuffer paramDerInputBuffer)
/*     */     throws IOException
/*     */   {
/* 250 */     this.tag = ((byte)paramDerInputBuffer.read());
/* 251 */     int i = (byte)paramDerInputBuffer.read();
/* 252 */     this.length = DerInputStream.getLength(i & 0xFF, paramDerInputBuffer);
/* 253 */     if (this.length == -1) {
/* 254 */       DerInputBuffer localDerInputBuffer = paramDerInputBuffer.dup();
/* 255 */       int j = localDerInputBuffer.available();
/* 256 */       int k = 2;
/* 257 */       byte[] arrayOfByte = new byte[j + k];
/* 258 */       arrayOfByte[0] = this.tag;
/* 259 */       arrayOfByte[1] = i;
/* 260 */       DataInputStream localDataInputStream = new DataInputStream(localDerInputBuffer);
/* 261 */       localDataInputStream.readFully(arrayOfByte, k, j);
/* 262 */       localDataInputStream.close();
/* 263 */       DerIndefLenConverter localDerIndefLenConverter = new DerIndefLenConverter();
/* 264 */       localDerInputBuffer = new DerInputBuffer(localDerIndefLenConverter.convert(arrayOfByte));
/* 265 */       if (this.tag != localDerInputBuffer.read()) {
/* 266 */         throw new IOException("Indefinite length encoding not supported");
/*     */       }
/* 268 */       this.length = DerInputStream.getLength(localDerInputBuffer);
/* 269 */       this.buffer = localDerInputBuffer.dup();
/* 270 */       this.buffer.truncate(this.length);
/* 271 */       this.data = new DerInputStream(this.buffer);
/*     */ 
/* 275 */       paramDerInputBuffer.skip(this.length + k);
/*     */     }
/*     */     else {
/* 278 */       this.buffer = paramDerInputBuffer.dup();
/* 279 */       this.buffer.truncate(this.length);
/* 280 */       this.data = new DerInputStream(this.buffer);
/*     */ 
/* 282 */       paramDerInputBuffer.skip(this.length);
/*     */     }
/*     */   }
/*     */ 
/*     */   public DerValue(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 294 */     this.data = init(true, new ByteArrayInputStream(paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public DerValue(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 307 */     this.data = init(true, new ByteArrayInputStream(paramArrayOfByte, paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   public DerValue(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 320 */     this.data = init(false, paramInputStream);
/*     */   }
/*     */ 
/*     */   private DerInputStream init(byte paramByte, String paramString) throws IOException {
/* 324 */     String str = null;
/*     */ 
/* 326 */     this.tag = paramByte;
/*     */ 
/* 328 */     switch (paramByte) {
/*     */     case 19:
/*     */     case 22:
/*     */     case 27:
/* 332 */       str = "ASCII";
/* 333 */       break;
/*     */     case 20:
/* 335 */       str = "ISO-8859-1";
/* 336 */       break;
/*     */     case 30:
/* 338 */       str = "UnicodeBigUnmarked";
/* 339 */       break;
/*     */     case 12:
/* 341 */       str = "UTF8";
/* 342 */       break;
/*     */     case 13:
/*     */     case 14:
/*     */     case 15:
/*     */     case 16:
/*     */     case 17:
/*     */     case 18:
/*     */     case 21:
/*     */     case 23:
/*     */     case 24:
/*     */     case 25:
/*     */     case 26:
/*     */     case 28:
/*     */     case 29:
/*     */     default:
/* 346 */       throw new IllegalArgumentException("Unsupported DER string type");
/*     */     }
/*     */ 
/* 349 */     byte[] arrayOfByte = paramString.getBytes(str);
/* 350 */     this.length = arrayOfByte.length;
/* 351 */     this.buffer = new DerInputBuffer(arrayOfByte);
/* 352 */     DerInputStream localDerInputStream = new DerInputStream(this.buffer);
/* 353 */     localDerInputStream.mark(2147483647);
/* 354 */     return localDerInputStream;
/*     */   }
/*     */ 
/*     */   private DerInputStream init(boolean paramBoolean, InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 363 */     this.tag = ((byte)paramInputStream.read());
/* 364 */     int i = (byte)paramInputStream.read();
/* 365 */     this.length = DerInputStream.getLength(i & 0xFF, paramInputStream);
/* 366 */     if (this.length == -1) {
/* 367 */       int j = paramInputStream.available();
/* 368 */       int k = 2;
/* 369 */       byte[] arrayOfByte2 = new byte[j + k];
/* 370 */       arrayOfByte2[0] = this.tag;
/* 371 */       arrayOfByte2[1] = i;
/* 372 */       DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
/* 373 */       localDataInputStream.readFully(arrayOfByte2, k, j);
/* 374 */       localDataInputStream.close();
/* 375 */       DerIndefLenConverter localDerIndefLenConverter = new DerIndefLenConverter();
/* 376 */       paramInputStream = new ByteArrayInputStream(localDerIndefLenConverter.convert(arrayOfByte2));
/* 377 */       if (this.tag != paramInputStream.read()) {
/* 378 */         throw new IOException("Indefinite length encoding not supported");
/*     */       }
/* 380 */       this.length = DerInputStream.getLength(paramInputStream);
/*     */     }
/*     */ 
/* 383 */     if ((paramBoolean) && (paramInputStream.available() != this.length)) {
/* 384 */       throw new IOException("extra data given to DerValue constructor");
/*     */     }
/* 386 */     byte[] arrayOfByte1 = IOUtils.readFully(paramInputStream, this.length, true);
/*     */ 
/* 388 */     this.buffer = new DerInputBuffer(arrayOfByte1);
/* 389 */     return new DerInputStream(this.buffer);
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 397 */     paramDerOutputStream.write(this.tag);
/* 398 */     paramDerOutputStream.putLength(this.length);
/*     */ 
/* 400 */     if (this.length > 0) {
/* 401 */       byte[] arrayOfByte = new byte[this.length];
/*     */ 
/* 403 */       synchronized (this.data) {
/* 404 */         this.buffer.reset();
/* 405 */         if (this.buffer.read(arrayOfByte) != this.length) {
/* 406 */           throw new IOException("short DER value read (encode)");
/*     */         }
/* 408 */         paramDerOutputStream.write(arrayOfByte);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final DerInputStream getData() {
/* 414 */     return this.data;
/*     */   }
/*     */ 
/*     */   public final byte getTag() {
/* 418 */     return this.tag;
/*     */   }
/*     */ 
/*     */   public boolean getBoolean()
/*     */     throws IOException
/*     */   {
/* 427 */     if (this.tag != 1) {
/* 428 */       throw new IOException("DerValue.getBoolean, not a BOOLEAN " + this.tag);
/*     */     }
/* 430 */     if (this.length != 1) {
/* 431 */       throw new IOException("DerValue.getBoolean, invalid length " + this.length);
/*     */     }
/*     */ 
/* 434 */     if (this.buffer.read() != 0) {
/* 435 */       return true;
/*     */     }
/* 437 */     return false;
/*     */   }
/*     */ 
/*     */   public ObjectIdentifier getOID()
/*     */     throws IOException
/*     */   {
/* 446 */     if (this.tag != 6)
/* 447 */       throw new IOException("DerValue.getOID, not an OID " + this.tag);
/* 448 */     return new ObjectIdentifier(this.buffer);
/*     */   }
/*     */ 
/*     */   private byte[] append(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
/* 452 */     if (paramArrayOfByte1 == null) {
/* 453 */       return paramArrayOfByte2;
/*     */     }
/* 455 */     byte[] arrayOfByte = new byte[paramArrayOfByte1.length + paramArrayOfByte2.length];
/* 456 */     System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 0, paramArrayOfByte1.length);
/* 457 */     System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, paramArrayOfByte1.length, paramArrayOfByte2.length);
/*     */ 
/* 459 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public byte[] getOctetString()
/*     */     throws IOException
/*     */   {
/* 470 */     if ((this.tag != 4) && (!isConstructed((byte)4))) {
/* 471 */       throw new IOException("DerValue.getOctetString, not an Octet String: " + this.tag);
/*     */     }
/*     */ 
/* 474 */     byte[] arrayOfByte = new byte[this.length];
/*     */ 
/* 477 */     if (this.length == 0) {
/* 478 */       return arrayOfByte;
/*     */     }
/* 480 */     if (this.buffer.read(arrayOfByte) != this.length)
/* 481 */       throw new IOException("short read on DerValue buffer");
/* 482 */     if (isConstructed()) {
/* 483 */       DerInputStream localDerInputStream = new DerInputStream(arrayOfByte);
/* 484 */       arrayOfByte = null;
/* 485 */       while (localDerInputStream.available() != 0) {
/* 486 */         arrayOfByte = append(arrayOfByte, localDerInputStream.getOctetString());
/*     */       }
/*     */     }
/* 489 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public int getInteger()
/*     */     throws IOException
/*     */   {
/* 498 */     if (this.tag != 2) {
/* 499 */       throw new IOException("DerValue.getInteger, not an int " + this.tag);
/*     */     }
/* 501 */     return this.buffer.getInteger(this.data.available());
/*     */   }
/*     */ 
/*     */   public BigInteger getBigInteger()
/*     */     throws IOException
/*     */   {
/* 510 */     if (this.tag != 2)
/* 511 */       throw new IOException("DerValue.getBigInteger, not an int " + this.tag);
/* 512 */     return this.buffer.getBigInteger(this.data.available(), false);
/*     */   }
/*     */ 
/*     */   public BigInteger getPositiveBigInteger()
/*     */     throws IOException
/*     */   {
/* 523 */     if (this.tag != 2)
/* 524 */       throw new IOException("DerValue.getBigInteger, not an int " + this.tag);
/* 525 */     return this.buffer.getBigInteger(this.data.available(), true);
/*     */   }
/*     */ 
/*     */   public int getEnumerated()
/*     */     throws IOException
/*     */   {
/* 534 */     if (this.tag != 10) {
/* 535 */       throw new IOException("DerValue.getEnumerated, incorrect tag: " + this.tag);
/*     */     }
/*     */ 
/* 538 */     return this.buffer.getInteger(this.data.available());
/*     */   }
/*     */ 
/*     */   public byte[] getBitString()
/*     */     throws IOException
/*     */   {
/* 547 */     if (this.tag != 3) {
/* 548 */       throw new IOException("DerValue.getBitString, not a bit string " + this.tag);
/*     */     }
/*     */ 
/* 551 */     return this.buffer.getBitString();
/*     */   }
/*     */ 
/*     */   public BitArray getUnalignedBitString()
/*     */     throws IOException
/*     */   {
/* 560 */     if (this.tag != 3) {
/* 561 */       throw new IOException("DerValue.getBitString, not a bit string " + this.tag);
/*     */     }
/*     */ 
/* 564 */     return this.buffer.getUnalignedBitString();
/*     */   }
/*     */ 
/*     */   public String getAsString()
/*     */     throws IOException
/*     */   {
/* 573 */     if (this.tag == 12)
/* 574 */       return getUTF8String();
/* 575 */     if (this.tag == 19)
/* 576 */       return getPrintableString();
/* 577 */     if (this.tag == 20)
/* 578 */       return getT61String();
/* 579 */     if (this.tag == 22) {
/* 580 */       return getIA5String();
/*     */     }
/*     */ 
/* 585 */     if (this.tag == 30)
/* 586 */       return getBMPString();
/* 587 */     if (this.tag == 27) {
/* 588 */       return getGeneralString();
/*     */     }
/* 590 */     return null;
/*     */   }
/*     */ 
/*     */   public byte[] getBitString(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 601 */     if ((!paramBoolean) && 
/* 602 */       (this.tag != 3)) {
/* 603 */       throw new IOException("DerValue.getBitString, not a bit string " + this.tag);
/*     */     }
/*     */ 
/* 606 */     return this.buffer.getBitString();
/*     */   }
/*     */ 
/*     */   public BitArray getUnalignedBitString(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 618 */     if ((!paramBoolean) && 
/* 619 */       (this.tag != 3)) {
/* 620 */       throw new IOException("DerValue.getBitString, not a bit string " + this.tag);
/*     */     }
/*     */ 
/* 623 */     return this.buffer.getUnalignedBitString();
/*     */   }
/*     */ 
/*     */   public byte[] getDataBytes()
/*     */     throws IOException
/*     */   {
/* 631 */     byte[] arrayOfByte = new byte[this.length];
/* 632 */     synchronized (this.data) {
/* 633 */       this.data.reset();
/* 634 */       this.data.getBytes(arrayOfByte);
/*     */     }
/* 636 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public String getPrintableString()
/*     */     throws IOException
/*     */   {
/* 646 */     if (this.tag != 19) {
/* 647 */       throw new IOException("DerValue.getPrintableString, not a string " + this.tag);
/*     */     }
/*     */ 
/* 650 */     return new String(getDataBytes(), "ASCII");
/*     */   }
/*     */ 
/*     */   public String getT61String()
/*     */     throws IOException
/*     */   {
/* 659 */     if (this.tag != 20) {
/* 660 */       throw new IOException("DerValue.getT61String, not T61 " + this.tag);
/*     */     }
/*     */ 
/* 663 */     return new String(getDataBytes(), "ISO-8859-1");
/*     */   }
/*     */ 
/*     */   public String getIA5String()
/*     */     throws IOException
/*     */   {
/* 672 */     if (this.tag != 22) {
/* 673 */       throw new IOException("DerValue.getIA5String, not IA5 " + this.tag);
/*     */     }
/*     */ 
/* 676 */     return new String(getDataBytes(), "ASCII");
/*     */   }
/*     */ 
/*     */   public String getBMPString()
/*     */     throws IOException
/*     */   {
/* 686 */     if (this.tag != 30) {
/* 687 */       throw new IOException("DerValue.getBMPString, not BMP " + this.tag);
/*     */     }
/*     */ 
/* 692 */     return new String(getDataBytes(), "UnicodeBigUnmarked");
/*     */   }
/*     */ 
/*     */   public String getUTF8String()
/*     */     throws IOException
/*     */   {
/* 702 */     if (this.tag != 12) {
/* 703 */       throw new IOException("DerValue.getUTF8String, not UTF-8 " + this.tag);
/*     */     }
/*     */ 
/* 706 */     return new String(getDataBytes(), "UTF8");
/*     */   }
/*     */ 
/*     */   public String getGeneralString()
/*     */     throws IOException
/*     */   {
/* 716 */     if (this.tag != 27) {
/* 717 */       throw new IOException("DerValue.getGeneralString, not GeneralString " + this.tag);
/*     */     }
/*     */ 
/* 720 */     return new String(getDataBytes(), "ASCII");
/*     */   }
/*     */ 
/*     */   public Date getUTCTime()
/*     */     throws IOException
/*     */   {
/* 729 */     if (this.tag != 23) {
/* 730 */       throw new IOException("DerValue.getUTCTime, not a UtcTime: " + this.tag);
/*     */     }
/* 732 */     return this.buffer.getUTCTime(this.data.available());
/*     */   }
/*     */ 
/*     */   public Date getGeneralizedTime()
/*     */     throws IOException
/*     */   {
/* 741 */     if (this.tag != 24) {
/* 742 */       throw new IOException("DerValue.getGeneralizedTime, not a GeneralizedTime: " + this.tag);
/*     */     }
/*     */ 
/* 745 */     return this.buffer.getGeneralizedTime(this.data.available());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 755 */     if ((paramObject instanceof DerValue)) {
/* 756 */       return equals((DerValue)paramObject);
/*     */     }
/* 758 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(DerValue paramDerValue)
/*     */   {
/* 769 */     if (this == paramDerValue) {
/* 770 */       return true;
/*     */     }
/* 772 */     if (this.tag != paramDerValue.tag) {
/* 773 */       return false;
/*     */     }
/* 775 */     if (this.data == paramDerValue.data) {
/* 776 */       return true;
/*     */     }
/*     */ 
/* 780 */     return System.identityHashCode(this.data) > System.identityHashCode(paramDerValue.data) ? doEquals(this, paramDerValue) : doEquals(paramDerValue, this);
/*     */   }
/*     */ 
/*     */   private static boolean doEquals(DerValue paramDerValue1, DerValue paramDerValue2)
/*     */   {
/* 790 */     synchronized (paramDerValue1.data) {
/* 791 */       synchronized (paramDerValue2.data) {
/* 792 */         paramDerValue1.data.reset();
/* 793 */         paramDerValue2.data.reset();
/* 794 */         return paramDerValue1.buffer.equals(paramDerValue2.buffer);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*     */     try
/*     */     {
/* 807 */       String str = getAsString();
/* 808 */       if (str != null)
/* 809 */         return "\"" + str + "\"";
/* 810 */       if (this.tag == 5)
/* 811 */         return "[DerValue, null]";
/* 812 */       if (this.tag == 6) {
/* 813 */         return "OID." + getOID();
/*     */       }
/*     */ 
/* 817 */       return "[DerValue, tag = " + this.tag + ", length = " + this.length + "]";
/*     */     } catch (IOException localIOException) {
/*     */     }
/* 820 */     throw new IllegalArgumentException("misformatted DER value");
/*     */   }
/*     */ 
/*     */   public byte[] toByteArray()
/*     */     throws IOException
/*     */   {
/* 831 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*     */ 
/* 833 */     encode(localDerOutputStream);
/* 834 */     this.data.reset();
/* 835 */     return localDerOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public DerInputStream toDerInputStream()
/*     */     throws IOException
/*     */   {
/* 845 */     if ((this.tag == 48) || (this.tag == 49))
/* 846 */       return new DerInputStream(this.buffer);
/* 847 */     throw new IOException("toDerInputStream rejects tag type " + this.tag);
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/* 854 */     return this.length;
/*     */   }
/*     */ 
/*     */   public static boolean isPrintableStringChar(char paramChar)
/*     */   {
/* 875 */     if (((paramChar >= 'a') && (paramChar <= 'z')) || ((paramChar >= 'A') && (paramChar <= 'Z')) || ((paramChar >= '0') && (paramChar <= '9')))
/*     */     {
/* 877 */       return true;
/*     */     }
/* 879 */     switch (paramChar) {
/*     */     case ' ':
/*     */     case '\'':
/*     */     case '(':
/*     */     case ')':
/*     */     case '+':
/*     */     case ',':
/*     */     case '-':
/*     */     case '.':
/*     */     case '/':
/*     */     case ':':
/*     */     case '=':
/*     */     case '?':
/* 892 */       return true;
/*     */     case '!':
/*     */     case '"':
/*     */     case '#':
/*     */     case '$':
/*     */     case '%':
/*     */     case '&':
/*     */     case '*':
/*     */     case '0':
/*     */     case '1':
/*     */     case '2':
/*     */     case '3':
/*     */     case '4':
/*     */     case '5':
/*     */     case '6':
/*     */     case '7':
/*     */     case '8':
/*     */     case '9':
/*     */     case ';':
/*     */     case '<':
/* 894 */     case '>': } return false;
/*     */   }
/*     */ 
/*     */   public static byte createTag(byte paramByte1, boolean paramBoolean, byte paramByte2)
/*     */   {
/* 909 */     byte b = (byte)(paramByte1 | paramByte2);
/* 910 */     if (paramBoolean) {
/* 911 */       b = (byte)(b | 0x20);
/*     */     }
/* 913 */     return b;
/*     */   }
/*     */ 
/*     */   public void resetTag(byte paramByte)
/*     */   {
/* 923 */     this.tag = paramByte;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 932 */     return toString().hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.DerValue
 * JD-Core Version:    0.6.2
 */