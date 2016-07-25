/*     */ package sun.security.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public final class ObjectIdentifier
/*     */   implements Serializable
/*     */ {
/*  60 */   private byte[] encoding = null;
/*     */   private volatile transient String stringForm;
/*     */   private static final long serialVersionUID = 8697030238860181294L;
/* 100 */   private Object components = null;
/*     */ 
/* 104 */   private int componentLen = -1;
/*     */ 
/* 107 */   private transient boolean componentsCalculated = false;
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 111 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 113 */     if (this.encoding == null)
/* 114 */       init((int[])this.components, this.componentLen);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 120 */     if (!this.componentsCalculated) {
/* 121 */       int[] arrayOfInt = toIntArray();
/* 122 */       if (arrayOfInt != null) {
/* 123 */         this.components = arrayOfInt;
/* 124 */         this.componentLen = arrayOfInt.length;
/*     */       } else {
/* 126 */         this.components = HugeOidNotSupportedByOldJDK.theOne;
/*     */       }
/* 128 */       this.componentsCalculated = true;
/*     */     }
/* 130 */     paramObjectOutputStream.defaultWriteObject();
/*     */   }
/*     */ 
/*     */   public ObjectIdentifier(String paramString)
/*     */     throws IOException
/*     */   {
/* 144 */     int i = 46;
/* 145 */     int j = 0;
/* 146 */     int k = 0;
/*     */ 
/* 148 */     int m = 0;
/* 149 */     byte[] arrayOfByte = new byte[paramString.length()];
/* 150 */     int n = 0;
/* 151 */     int i1 = 0;
/*     */     try
/*     */     {
/* 154 */       String str = null;
/*     */       do {
/* 156 */         int i2 = 0;
/* 157 */         k = paramString.indexOf(i, j);
/* 158 */         if (k == -1) {
/* 159 */           str = paramString.substring(j);
/* 160 */           i2 = paramString.length() - j;
/*     */         } else {
/* 162 */           str = paramString.substring(j, k);
/* 163 */           i2 = k - j;
/*     */         }
/*     */ 
/* 166 */         if (i2 > 9) {
/* 167 */           BigInteger localBigInteger = new BigInteger(str);
/* 168 */           if (i1 == 0) {
/* 169 */             checkFirstComponent(localBigInteger);
/* 170 */             n = localBigInteger.intValue();
/*     */           } else {
/* 172 */             if (i1 == 1) {
/* 173 */               checkSecondComponent(n, localBigInteger);
/* 174 */               localBigInteger = localBigInteger.add(BigInteger.valueOf(40 * n));
/*     */             } else {
/* 176 */               checkOtherComponent(i1, localBigInteger);
/*     */             }
/* 178 */             m += pack7Oid(localBigInteger, arrayOfByte, m);
/*     */           }
/*     */         } else {
/* 181 */           int i3 = Integer.parseInt(str);
/* 182 */           if (i1 == 0) {
/* 183 */             checkFirstComponent(i3);
/* 184 */             n = i3;
/*     */           } else {
/* 186 */             if (i1 == 1) {
/* 187 */               checkSecondComponent(n, i3);
/* 188 */               i3 += 40 * n;
/*     */             } else {
/* 190 */               checkOtherComponent(i1, i3);
/*     */             }
/* 192 */             m += pack7Oid(i3, arrayOfByte, m);
/*     */           }
/*     */         }
/* 195 */         j = k + 1;
/* 196 */         i1++;
/* 197 */       }while (k != -1);
/*     */ 
/* 199 */       checkCount(i1);
/* 200 */       this.encoding = new byte[m];
/* 201 */       System.arraycopy(arrayOfByte, 0, this.encoding, 0, m);
/* 202 */       this.stringForm = paramString;
/*     */     } catch (IOException localIOException) {
/* 204 */       throw localIOException;
/*     */     } catch (Exception localException) {
/* 206 */       throw new IOException("ObjectIdentifier() -- Invalid format: " + localException.toString(), localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ObjectIdentifier(int[] paramArrayOfInt)
/*     */     throws IOException
/*     */   {
/* 217 */     checkCount(paramArrayOfInt.length);
/* 218 */     checkFirstComponent(paramArrayOfInt[0]);
/* 219 */     checkSecondComponent(paramArrayOfInt[0], paramArrayOfInt[1]);
/* 220 */     for (int i = 2; i < paramArrayOfInt.length; i++)
/* 221 */       checkOtherComponent(i, paramArrayOfInt[i]);
/* 222 */     init(paramArrayOfInt, paramArrayOfInt.length);
/*     */   }
/*     */ 
/*     */   public ObjectIdentifier(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/* 251 */     int i = (byte)paramDerInputStream.getByte();
/* 252 */     if (i != 6) {
/* 253 */       throw new IOException("ObjectIdentifier() -- data isn't an object ID (tag = " + i + ")");
/*     */     }
/*     */ 
/* 258 */     this.encoding = new byte[paramDerInputStream.getLength()];
/* 259 */     paramDerInputStream.getBytes(this.encoding);
/* 260 */     check(this.encoding);
/*     */   }
/*     */ 
/*     */   ObjectIdentifier(DerInputBuffer paramDerInputBuffer)
/*     */     throws IOException
/*     */   {
/* 270 */     DerInputStream localDerInputStream = new DerInputStream(paramDerInputBuffer);
/* 271 */     this.encoding = new byte[localDerInputStream.available()];
/* 272 */     localDerInputStream.getBytes(this.encoding);
/* 273 */     check(this.encoding);
/*     */   }
/*     */ 
/*     */   private void init(int[] paramArrayOfInt, int paramInt) {
/* 277 */     int i = 0;
/* 278 */     byte[] arrayOfByte = new byte[paramInt * 5 + 1];
/*     */ 
/* 280 */     if (paramArrayOfInt[1] < 2147483647 - paramArrayOfInt[0] * 40) {
/* 281 */       i += pack7Oid(paramArrayOfInt[0] * 40 + paramArrayOfInt[1], arrayOfByte, i);
/*     */     } else {
/* 283 */       BigInteger localBigInteger = BigInteger.valueOf(paramArrayOfInt[1]);
/* 284 */       localBigInteger = localBigInteger.add(BigInteger.valueOf(paramArrayOfInt[0] * 40));
/* 285 */       i += pack7Oid(localBigInteger, arrayOfByte, i);
/*     */     }
/*     */ 
/* 288 */     for (int j = 2; j < paramInt; j++) {
/* 289 */       i += pack7Oid(paramArrayOfInt[j], arrayOfByte, i);
/*     */     }
/* 291 */     this.encoding = new byte[i];
/* 292 */     System.arraycopy(arrayOfByte, 0, this.encoding, 0, i);
/*     */   }
/*     */ 
/*     */   public static ObjectIdentifier newInternal(int[] paramArrayOfInt)
/*     */   {
/*     */     try
/*     */     {
/* 306 */       return new ObjectIdentifier(paramArrayOfInt);
/*     */     } catch (IOException localIOException) {
/* 308 */       throw new RuntimeException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 318 */     paramDerOutputStream.write((byte)6, this.encoding);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public boolean equals(ObjectIdentifier paramObjectIdentifier)
/*     */   {
/* 326 */     return equals(paramObjectIdentifier);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 336 */     if (this == paramObject) {
/* 337 */       return true;
/*     */     }
/* 339 */     if (!(paramObject instanceof ObjectIdentifier)) {
/* 340 */       return false;
/*     */     }
/* 342 */     ObjectIdentifier localObjectIdentifier = (ObjectIdentifier)paramObject;
/* 343 */     return Arrays.equals(this.encoding, localObjectIdentifier.encoding);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 348 */     return Arrays.hashCode(this.encoding);
/*     */   }
/*     */ 
/*     */   private int[] toIntArray()
/*     */   {
/* 358 */     int i = this.encoding.length;
/* 359 */     int[] arrayOfInt = new int[20];
/* 360 */     int j = 0;
/* 361 */     BigInteger localBigInteger1 = 0;
/* 362 */     for (BigInteger localBigInteger2 = 0; localBigInteger2 < i; localBigInteger2++) {
/* 363 */       if ((this.encoding[localBigInteger2] & 0x80) == 0)
/*     */       {
/*     */         BigInteger localBigInteger4;
/* 365 */         if (localBigInteger2 - localBigInteger1 + 1 > 4) {
/* 366 */           BigInteger localBigInteger3 = new BigInteger(pack(this.encoding, localBigInteger1, localBigInteger2 - localBigInteger1 + 1, 7, 8));
/* 367 */           if (localBigInteger1 == 0) {
/* 368 */             arrayOfInt[(j++)] = 2;
/* 369 */             localBigInteger4 = localBigInteger3.subtract(BigInteger.valueOf(80L));
/* 370 */             if (localBigInteger4.compareTo(BigInteger.valueOf(2147483647L)) == 1) {
/* 371 */               return null;
/*     */             }
/* 373 */             arrayOfInt[(j++)] = localBigInteger4.intValue();
/*     */           }
/*     */           else {
/* 376 */             if (localBigInteger3.compareTo(BigInteger.valueOf(2147483647L)) == 1) {
/* 377 */               return null;
/*     */             }
/* 379 */             arrayOfInt[(j++)] = localBigInteger3.intValue();
/*     */           }
/*     */         }
/*     */         else {
/* 383 */           int k = 0;
/* 384 */           for (localBigInteger4 = localBigInteger1; localBigInteger4 <= localBigInteger2; localBigInteger4++) {
/* 385 */             k <<= 7;
/* 386 */             int m = this.encoding[localBigInteger4];
/* 387 */             k |= m & 0x7F;
/*     */           }
/* 389 */           if (localBigInteger1 == 0) {
/* 390 */             if (k < 80) {
/* 391 */               arrayOfInt[(j++)] = (k / 40);
/* 392 */               arrayOfInt[(j++)] = (k % 40);
/*     */             } else {
/* 394 */               arrayOfInt[(j++)] = 2;
/* 395 */               arrayOfInt[(j++)] = (k - 80);
/*     */             }
/*     */           }
/* 398 */           else arrayOfInt[(j++)] = k;
/*     */         }
/*     */ 
/* 401 */         localBigInteger1 = localBigInteger2 + 1;
/*     */       }
/* 403 */       if (j >= arrayOfInt.length) {
/* 404 */         arrayOfInt = Arrays.copyOf(arrayOfInt, j + 10);
/*     */       }
/*     */     }
/* 407 */     return Arrays.copyOf(arrayOfInt, j);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 418 */     String str = this.stringForm;
/* 419 */     if (str == null) {
/* 420 */       int i = this.encoding.length;
/* 421 */       StringBuffer localStringBuffer = new StringBuffer(i * 4);
/*     */ 
/* 423 */       int j = 0;
/* 424 */       for (int k = 0; k < i; k++) {
/* 425 */         if ((this.encoding[k] & 0x80) == 0)
/*     */         {
/* 427 */           if (j != 0) {
/* 428 */             localStringBuffer.append('.');
/*     */           }
/* 430 */           if (k - j + 1 > 4) {
/* 431 */             BigInteger localBigInteger = new BigInteger(pack(this.encoding, j, k - j + 1, 7, 8));
/* 432 */             if (j == 0)
/*     */             {
/* 435 */               localStringBuffer.append("2.");
/* 436 */               localStringBuffer.append(localBigInteger.subtract(BigInteger.valueOf(80L)));
/*     */             } else {
/* 438 */               localStringBuffer.append(localBigInteger);
/*     */             }
/*     */           } else {
/* 441 */             int m = 0;
/* 442 */             for (int n = j; n <= k; n++) {
/* 443 */               m <<= 7;
/* 444 */               int i1 = this.encoding[n];
/* 445 */               m |= i1 & 0x7F;
/*     */             }
/* 447 */             if (j == 0) {
/* 448 */               if (m < 80) {
/* 449 */                 localStringBuffer.append(m / 40);
/* 450 */                 localStringBuffer.append('.');
/* 451 */                 localStringBuffer.append(m % 40);
/*     */               } else {
/* 453 */                 localStringBuffer.append("2.");
/* 454 */                 localStringBuffer.append(m - 80);
/*     */               }
/*     */             }
/* 457 */             else localStringBuffer.append(m);
/*     */           }
/*     */ 
/* 460 */           j = k + 1;
/*     */         }
/*     */       }
/* 463 */       str = localStringBuffer.toString();
/* 464 */       this.stringForm = str;
/*     */     }
/* 466 */     return str;
/*     */   }
/*     */ 
/*     */   private static byte[] pack(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 495 */     assert ((paramInt3 > 0) && (paramInt3 <= 8)) : "input NUB must be between 1 and 8";
/* 496 */     assert ((paramInt4 > 0) && (paramInt4 <= 8)) : "output NUB must be between 1 and 8";
/*     */ 
/* 498 */     if (paramInt3 == paramInt4) {
/* 499 */       return (byte[])paramArrayOfByte.clone();
/*     */     }
/*     */ 
/* 502 */     int i = paramInt2 * paramInt3;
/* 503 */     byte[] arrayOfByte = new byte[(i + paramInt4 - 1) / paramInt4];
/*     */ 
/* 506 */     int j = 0;
/*     */ 
/* 509 */     int k = (i + paramInt4 - 1) / paramInt4 * paramInt4 - i;
/*     */ 
/* 511 */     while (j < i) {
/* 512 */       int m = paramInt3 - j % paramInt3;
/* 513 */       if (m > paramInt4 - k % paramInt4)
/* 514 */         m = paramInt4 - k % paramInt4;
/*     */       int tmp153_152 = (k / paramInt4);
/*     */       byte[] tmp153_146 = arrayOfByte; tmp153_146[tmp153_152] = ((byte)(tmp153_146[tmp153_152] | (paramArrayOfByte[(paramInt1 + j / paramInt3)] + 256 >> paramInt3 - j % paramInt3 - m & (1 << m) - 1) << paramInt4 - k % paramInt4 - m));
/*     */ 
/* 522 */       j += m;
/* 523 */       k += m;
/*     */     }
/* 525 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private static int pack7Oid(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
/*     */   {
/* 538 */     byte[] arrayOfByte = pack(paramArrayOfByte1, paramInt1, paramInt2, 8, 7);
/* 539 */     int i = arrayOfByte.length - 1;
/* 540 */     for (int j = arrayOfByte.length - 2; j >= 0; j--) {
/* 541 */       if (arrayOfByte[j] != 0)
/* 542 */         i = j;
/*     */       int tmp47_45 = j;
/*     */       byte[] tmp47_43 = arrayOfByte; tmp47_43[tmp47_45] = ((byte)(tmp47_43[tmp47_45] | 0x80));
/*     */     }
/* 546 */     System.arraycopy(arrayOfByte, i, paramArrayOfByte2, paramInt3, arrayOfByte.length - i);
/* 547 */     return arrayOfByte.length - i;
/*     */   }
/*     */ 
/*     */   private static int pack8(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
/*     */   {
/* 558 */     byte[] arrayOfByte = pack(paramArrayOfByte1, paramInt1, paramInt2, 7, 8);
/* 559 */     int i = arrayOfByte.length - 1;
/* 560 */     for (int j = arrayOfByte.length - 2; j >= 0; j--) {
/* 561 */       if (arrayOfByte[j] != 0) {
/* 562 */         i = j;
/*     */       }
/*     */     }
/* 565 */     System.arraycopy(arrayOfByte, i, paramArrayOfByte2, paramInt3, arrayOfByte.length - i);
/* 566 */     return arrayOfByte.length - i;
/*     */   }
/*     */ 
/*     */   private static int pack7Oid(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */   {
/* 573 */     byte[] arrayOfByte = new byte[4];
/* 574 */     arrayOfByte[0] = ((byte)(paramInt1 >> 24));
/* 575 */     arrayOfByte[1] = ((byte)(paramInt1 >> 16));
/* 576 */     arrayOfByte[2] = ((byte)(paramInt1 >> 8));
/* 577 */     arrayOfByte[3] = ((byte)paramInt1);
/* 578 */     return pack7Oid(arrayOfByte, 0, 4, paramArrayOfByte, paramInt2);
/*     */   }
/*     */ 
/*     */   private static int pack7Oid(BigInteger paramBigInteger, byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 585 */     byte[] arrayOfByte = paramBigInteger.toByteArray();
/* 586 */     return pack7Oid(arrayOfByte, 0, arrayOfByte.length, paramArrayOfByte, paramInt);
/*     */   }
/*     */ 
/*     */   private static void check(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 602 */     int i = paramArrayOfByte.length;
/* 603 */     if ((i < 1) || ((paramArrayOfByte[(i - 1)] & 0x80) != 0))
/*     */     {
/* 605 */       throw new IOException("ObjectIdentifier() -- Invalid DER encoding, not ended");
/*     */     }
/*     */ 
/* 608 */     for (int j = 0; j < i; j++)
/*     */     {
/* 610 */       if ((paramArrayOfByte[j] == -128) && ((j == 0) || ((paramArrayOfByte[(j - 1)] & 0x80) == 0)))
/*     */       {
/* 612 */         throw new IOException("ObjectIdentifier() -- Invalid DER encoding, useless extra octet detected");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void checkCount(int paramInt) throws IOException {
/* 618 */     if (paramInt < 2)
/* 619 */       throw new IOException("ObjectIdentifier() -- Must be at least two oid components ");
/*     */   }
/*     */ 
/*     */   private static void checkFirstComponent(int paramInt) throws IOException
/*     */   {
/* 624 */     if ((paramInt < 0) || (paramInt > 2))
/* 625 */       throw new IOException("ObjectIdentifier() -- First oid component is invalid ");
/*     */   }
/*     */ 
/*     */   private static void checkFirstComponent(BigInteger paramBigInteger) throws IOException
/*     */   {
/* 630 */     if ((paramBigInteger.signum() == -1) || (paramBigInteger.compareTo(BigInteger.valueOf(2L)) == 1))
/*     */     {
/* 632 */       throw new IOException("ObjectIdentifier() -- First oid component is invalid ");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void checkSecondComponent(int paramInt1, int paramInt2) throws IOException {
/* 637 */     if ((paramInt2 < 0) || ((paramInt1 != 2) && (paramInt2 > 39)))
/* 638 */       throw new IOException("ObjectIdentifier() -- Second oid component is invalid ");
/*     */   }
/*     */ 
/*     */   private static void checkSecondComponent(int paramInt, BigInteger paramBigInteger) throws IOException
/*     */   {
/* 643 */     if ((paramBigInteger.signum() == -1) || ((paramInt != 2) && (paramBigInteger.compareTo(BigInteger.valueOf(39L)) == 1)))
/*     */     {
/* 646 */       throw new IOException("ObjectIdentifier() -- Second oid component is invalid ");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void checkOtherComponent(int paramInt1, int paramInt2) throws IOException {
/* 651 */     if (paramInt2 < 0)
/* 652 */       throw new IOException("ObjectIdentifier() -- oid component #" + (paramInt1 + 1) + " must be non-negative ");
/*     */   }
/*     */ 
/*     */   private static void checkOtherComponent(int paramInt, BigInteger paramBigInteger) throws IOException
/*     */   {
/* 657 */     if (paramBigInteger.signum() == -1)
/* 658 */       throw new IOException("ObjectIdentifier() -- oid component #" + (paramInt + 1) + " must be non-negative ");
/*     */   }
/*     */ 
/*     */   static class HugeOidNotSupportedByOldJDK
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 135 */     static HugeOidNotSupportedByOldJDK theOne = new HugeOidNotSupportedByOldJDK();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.ObjectIdentifier
 * JD-Core Version:    0.6.2
 */