/*     */ package sun.security.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ public class DerOutputStream extends ByteArrayOutputStream
/*     */   implements DerEncoder
/*     */ {
/* 359 */   private static ByteArrayLexOrder lexOrder = new ByteArrayLexOrder();
/*     */ 
/* 365 */   private static ByteArrayTagOrder tagOrder = new ByteArrayTagOrder();
/*     */ 
/*     */   public DerOutputStream(int paramInt)
/*     */   {
/*  61 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public DerOutputStream()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void write(byte paramByte, byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  78 */     write(paramByte);
/*  79 */     putLength(paramArrayOfByte.length);
/*  80 */     write(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public void write(byte paramByte, DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/*  93 */     write(paramByte);
/*  94 */     putLength(paramDerOutputStream.count);
/*  95 */     write(paramDerOutputStream.buf, 0, paramDerOutputStream.count);
/*     */   }
/*     */ 
/*     */   public void writeImplicit(byte paramByte, DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 117 */     write(paramByte);
/* 118 */     write(paramDerOutputStream.buf, 1, paramDerOutputStream.count - 1);
/*     */   }
/*     */ 
/*     */   public void putDerValue(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/* 125 */     paramDerValue.encode(this);
/*     */   }
/*     */ 
/*     */   public void putBoolean(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 140 */     write(1);
/* 141 */     putLength(1);
/* 142 */     if (paramBoolean)
/* 143 */       write(255);
/*     */     else
/* 145 */       write(0);
/*     */   }
/*     */ 
/*     */   public void putEnumerated(int paramInt)
/*     */     throws IOException
/*     */   {
/* 154 */     write(10);
/* 155 */     putIntegerContents(paramInt);
/*     */   }
/*     */ 
/*     */   public void putInteger(BigInteger paramBigInteger)
/*     */     throws IOException
/*     */   {
/* 164 */     write(2);
/* 165 */     byte[] arrayOfByte = paramBigInteger.toByteArray();
/* 166 */     putLength(arrayOfByte.length);
/* 167 */     write(arrayOfByte, 0, arrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public void putInteger(Integer paramInteger)
/*     */     throws IOException
/*     */   {
/* 175 */     putInteger(paramInteger.intValue());
/*     */   }
/*     */ 
/*     */   public void putInteger(int paramInt)
/*     */     throws IOException
/*     */   {
/* 183 */     write(2);
/* 184 */     putIntegerContents(paramInt);
/*     */   }
/*     */ 
/*     */   private void putIntegerContents(int paramInt) throws IOException
/*     */   {
/* 189 */     byte[] arrayOfByte = new byte[4];
/* 190 */     int i = 0;
/*     */ 
/* 194 */     arrayOfByte[3] = ((byte)(paramInt & 0xFF));
/* 195 */     arrayOfByte[2] = ((byte)((paramInt & 0xFF00) >>> 8));
/* 196 */     arrayOfByte[1] = ((byte)((paramInt & 0xFF0000) >>> 16));
/* 197 */     arrayOfByte[0] = ((byte)((paramInt & 0xFF000000) >>> 24));
/*     */ 
/* 202 */     if (arrayOfByte[0] == -1)
/*     */     {
/* 206 */       for (j = 0; (j < 3) && 
/* 207 */         (arrayOfByte[j] == -1) && ((arrayOfByte[(j + 1)] & 0x80) == 128); j++)
/*     */       {
/* 209 */         i++;
/*     */       }
/*     */ 
/*     */     }
/* 213 */     else if (arrayOfByte[0] == 0)
/*     */     {
/* 217 */       for (j = 0; (j < 3) && 
/* 218 */         (arrayOfByte[j] == 0) && ((arrayOfByte[(j + 1)] & 0x80) == 0); j++)
/*     */       {
/* 220 */         i++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 226 */     putLength(4 - i);
/* 227 */     for (int j = i; j < 4; j++)
/* 228 */       write(arrayOfByte[j]);
/*     */   }
/*     */ 
/*     */   public void putBitString(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 238 */     write(3);
/* 239 */     putLength(paramArrayOfByte.length + 1);
/* 240 */     write(0);
/* 241 */     write(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public void putUnalignedBitString(BitArray paramBitArray)
/*     */     throws IOException
/*     */   {
/* 251 */     byte[] arrayOfByte = paramBitArray.toByteArray();
/*     */ 
/* 253 */     write(3);
/* 254 */     putLength(arrayOfByte.length + 1);
/* 255 */     write(arrayOfByte.length * 8 - paramBitArray.length());
/* 256 */     write(arrayOfByte);
/*     */   }
/*     */ 
/*     */   public void putTruncatedUnalignedBitString(BitArray paramBitArray)
/*     */     throws IOException
/*     */   {
/* 266 */     putUnalignedBitString(paramBitArray.truncate());
/*     */   }
/*     */ 
/*     */   public void putOctetString(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 275 */     write((byte)4, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public void putNull()
/*     */     throws IOException
/*     */   {
/* 283 */     write(5);
/* 284 */     putLength(0);
/*     */   }
/*     */ 
/*     */   public void putOID(ObjectIdentifier paramObjectIdentifier)
/*     */     throws IOException
/*     */   {
/* 292 */     paramObjectIdentifier.encode(this);
/*     */   }
/*     */ 
/*     */   public void putSequence(DerValue[] paramArrayOfDerValue)
/*     */     throws IOException
/*     */   {
/* 301 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*     */ 
/* 304 */     for (int i = 0; i < paramArrayOfDerValue.length; i++) {
/* 305 */       paramArrayOfDerValue[i].encode(localDerOutputStream);
/*     */     }
/* 307 */     write((byte)48, localDerOutputStream);
/*     */   }
/*     */ 
/*     */   public void putSet(DerValue[] paramArrayOfDerValue)
/*     */     throws IOException
/*     */   {
/* 318 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*     */ 
/* 321 */     for (int i = 0; i < paramArrayOfDerValue.length; i++) {
/* 322 */       paramArrayOfDerValue[i].encode(localDerOutputStream);
/*     */     }
/* 324 */     write((byte)49, localDerOutputStream);
/*     */   }
/*     */ 
/*     */   public void putOrderedSetOf(byte paramByte, DerEncoder[] paramArrayOfDerEncoder)
/*     */     throws IOException
/*     */   {
/* 338 */     putOrderedSet(paramByte, paramArrayOfDerEncoder, lexOrder);
/*     */   }
/*     */ 
/*     */   public void putOrderedSet(byte paramByte, DerEncoder[] paramArrayOfDerEncoder)
/*     */     throws IOException
/*     */   {
/* 352 */     putOrderedSet(paramByte, paramArrayOfDerEncoder, tagOrder);
/*     */   }
/*     */ 
/*     */   private void putOrderedSet(byte paramByte, DerEncoder[] paramArrayOfDerEncoder, Comparator<byte[]> paramComparator)
/*     */     throws IOException
/*     */   {
/* 375 */     DerOutputStream[] arrayOfDerOutputStream = new DerOutputStream[paramArrayOfDerEncoder.length];
/*     */ 
/* 377 */     for (int i = 0; i < paramArrayOfDerEncoder.length; i++) {
/* 378 */       arrayOfDerOutputStream[i] = new DerOutputStream();
/* 379 */       paramArrayOfDerEncoder[i].derEncode(arrayOfDerOutputStream[i]);
/*     */     }
/*     */ 
/* 383 */     byte[][] arrayOfByte = new byte[arrayOfDerOutputStream.length][];
/* 384 */     for (int j = 0; j < arrayOfDerOutputStream.length; j++) {
/* 385 */       arrayOfByte[j] = arrayOfDerOutputStream[j].toByteArray();
/*     */     }
/* 387 */     Arrays.sort(arrayOfByte, paramComparator);
/*     */ 
/* 389 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 390 */     for (int k = 0; k < arrayOfDerOutputStream.length; k++) {
/* 391 */       localDerOutputStream.write(arrayOfByte[k]);
/*     */     }
/* 393 */     write(paramByte, localDerOutputStream);
/*     */   }
/*     */ 
/*     */   public void putUTF8String(String paramString)
/*     */     throws IOException
/*     */   {
/* 401 */     writeString(paramString, (byte)12, "UTF8");
/*     */   }
/*     */ 
/*     */   public void putPrintableString(String paramString)
/*     */     throws IOException
/*     */   {
/* 408 */     writeString(paramString, (byte)19, "ASCII");
/*     */   }
/*     */ 
/*     */   public void putT61String(String paramString)
/*     */     throws IOException
/*     */   {
/* 419 */     writeString(paramString, (byte)20, "ISO-8859-1");
/*     */   }
/*     */ 
/*     */   public void putIA5String(String paramString)
/*     */     throws IOException
/*     */   {
/* 426 */     writeString(paramString, (byte)22, "ASCII");
/*     */   }
/*     */ 
/*     */   public void putBMPString(String paramString)
/*     */     throws IOException
/*     */   {
/* 433 */     writeString(paramString, (byte)30, "UnicodeBigUnmarked");
/*     */   }
/*     */ 
/*     */   public void putGeneralString(String paramString)
/*     */     throws IOException
/*     */   {
/* 440 */     writeString(paramString, (byte)27, "ASCII");
/*     */   }
/*     */ 
/*     */   private void writeString(String paramString1, byte paramByte, String paramString2)
/*     */     throws IOException
/*     */   {
/* 454 */     byte[] arrayOfByte = paramString1.getBytes(paramString2);
/* 455 */     write(paramByte);
/* 456 */     putLength(arrayOfByte.length);
/* 457 */     write(arrayOfByte);
/*     */   }
/*     */ 
/*     */   public void putUTCTime(Date paramDate)
/*     */     throws IOException
/*     */   {
/* 467 */     putTime(paramDate, (byte)23);
/*     */   }
/*     */ 
/*     */   public void putGeneralizedTime(Date paramDate)
/*     */     throws IOException
/*     */   {
/* 477 */     putTime(paramDate, (byte)24);
/*     */   }
/*     */ 
/*     */   private void putTime(Date paramDate, byte paramByte)
/*     */     throws IOException
/*     */   {
/* 493 */     TimeZone localTimeZone = TimeZone.getTimeZone("GMT");
/* 494 */     String str = null;
/*     */ 
/* 496 */     if (paramByte == 23) {
/* 497 */       str = "yyMMddHHmmss'Z'";
/*     */     } else {
/* 499 */       paramByte = 24;
/* 500 */       str = "yyyyMMddHHmmss'Z'";
/*     */     }
/*     */ 
/* 503 */     SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(str, Locale.US);
/* 504 */     localSimpleDateFormat.setTimeZone(localTimeZone);
/* 505 */     byte[] arrayOfByte = localSimpleDateFormat.format(paramDate).getBytes("ISO-8859-1");
/*     */ 
/* 511 */     write(paramByte);
/* 512 */     putLength(arrayOfByte.length);
/* 513 */     write(arrayOfByte);
/*     */   }
/*     */ 
/*     */   public void putLength(int paramInt)
/*     */     throws IOException
/*     */   {
/* 523 */     if (paramInt < 128) {
/* 524 */       write((byte)paramInt);
/*     */     }
/* 526 */     else if (paramInt < 256) {
/* 527 */       write(-127);
/* 528 */       write((byte)paramInt);
/*     */     }
/* 530 */     else if (paramInt < 65536) {
/* 531 */       write(-126);
/* 532 */       write((byte)(paramInt >> 8));
/* 533 */       write((byte)paramInt);
/*     */     }
/* 535 */     else if (paramInt < 16777216) {
/* 536 */       write(-125);
/* 537 */       write((byte)(paramInt >> 16));
/* 538 */       write((byte)(paramInt >> 8));
/* 539 */       write((byte)paramInt);
/*     */     }
/*     */     else {
/* 542 */       write(-124);
/* 543 */       write((byte)(paramInt >> 24));
/* 544 */       write((byte)(paramInt >> 16));
/* 545 */       write((byte)(paramInt >> 8));
/* 546 */       write((byte)paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void putTag(byte paramByte1, boolean paramBoolean, byte paramByte2)
/*     */   {
/* 560 */     int i = (byte)(paramByte1 | paramByte2);
/* 561 */     if (paramBoolean) {
/* 562 */       i = (byte)(i | 0x20);
/*     */     }
/* 564 */     write(i);
/*     */   }
/*     */ 
/*     */   public void derEncode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 574 */     paramOutputStream.write(toByteArray());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.DerOutputStream
 * JD-Core Version:    0.6.2
 */