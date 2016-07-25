/*     */ package com.sun.org.apache.xml.internal.security.utils;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.math.BigInteger;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ public class Base64
/*     */ {
/*     */   public static final int BASE64DEFAULTLENGTH = 76;
/*     */   private static final int BASELENGTH = 255;
/*     */   private static final int LOOKUPLENGTH = 64;
/*     */   private static final int TWENTYFOURBITGROUP = 24;
/*     */   private static final int EIGHTBIT = 8;
/*     */   private static final int SIXTEENBIT = 16;
/*     */   private static final int FOURBYTE = 4;
/*     */   private static final int SIGN = -128;
/*     */   private static final char PAD = '=';
/* 317 */   private static final byte[] base64Alphabet = new byte['ÿ'];
/* 318 */   private static final char[] lookUpBase64Alphabet = new char[64];
/*     */ 
/*     */   static final byte[] getBytes(BigInteger paramBigInteger, int paramInt)
/*     */   {
/*  71 */     paramInt = paramInt + 7 >> 3 << 3;
/*     */ 
/*  73 */     if (paramInt < paramBigInteger.bitLength()) {
/*  74 */       throw new IllegalArgumentException(I18n.translate("utils.Base64.IllegalBitlength"));
/*     */     }
/*     */ 
/*  78 */     byte[] arrayOfByte1 = paramBigInteger.toByteArray();
/*     */ 
/*  80 */     if ((paramBigInteger.bitLength() % 8 != 0) && (paramBigInteger.bitLength() / 8 + 1 == paramInt / 8))
/*     */     {
/*  82 */       return arrayOfByte1;
/*     */     }
/*     */ 
/*  86 */     int i = 0;
/*  87 */     int j = arrayOfByte1.length;
/*     */ 
/*  89 */     if (paramBigInteger.bitLength() % 8 == 0) {
/*  90 */       i = 1;
/*     */ 
/*  92 */       j--;
/*     */     }
/*     */ 
/*  95 */     int k = paramInt / 8 - j;
/*  96 */     byte[] arrayOfByte2 = new byte[paramInt / 8];
/*     */ 
/*  98 */     System.arraycopy(arrayOfByte1, i, arrayOfByte2, k, j);
/*     */ 
/* 100 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   public static final String encode(BigInteger paramBigInteger)
/*     */   {
/* 111 */     return encode(getBytes(paramBigInteger, paramBigInteger.bitLength()));
/*     */   }
/*     */ 
/*     */   public static final byte[] encode(BigInteger paramBigInteger, int paramInt)
/*     */   {
/* 128 */     paramInt = paramInt + 7 >> 3 << 3;
/*     */ 
/* 130 */     if (paramInt < paramBigInteger.bitLength()) {
/* 131 */       throw new IllegalArgumentException(I18n.translate("utils.Base64.IllegalBitlength"));
/*     */     }
/*     */ 
/* 135 */     byte[] arrayOfByte1 = paramBigInteger.toByteArray();
/*     */ 
/* 137 */     if ((paramBigInteger.bitLength() % 8 != 0) && (paramBigInteger.bitLength() / 8 + 1 == paramInt / 8))
/*     */     {
/* 139 */       return arrayOfByte1;
/*     */     }
/*     */ 
/* 143 */     int i = 0;
/* 144 */     int j = arrayOfByte1.length;
/*     */ 
/* 146 */     if (paramBigInteger.bitLength() % 8 == 0) {
/* 147 */       i = 1;
/*     */ 
/* 149 */       j--;
/*     */     }
/*     */ 
/* 152 */     int k = paramInt / 8 - j;
/* 153 */     byte[] arrayOfByte2 = new byte[paramInt / 8];
/*     */ 
/* 155 */     System.arraycopy(arrayOfByte1, i, arrayOfByte2, k, j);
/*     */ 
/* 157 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   public static final BigInteger decodeBigIntegerFromElement(Element paramElement)
/*     */     throws Base64DecodingException
/*     */   {
/* 170 */     return new BigInteger(1, decode(paramElement));
/*     */   }
/*     */ 
/*     */   public static final BigInteger decodeBigIntegerFromText(Text paramText)
/*     */     throws Base64DecodingException
/*     */   {
/* 182 */     return new BigInteger(1, decode(paramText.getData()));
/*     */   }
/*     */ 
/*     */   public static final void fillElementWithBigInteger(Element paramElement, BigInteger paramBigInteger)
/*     */   {
/* 195 */     String str = encode(paramBigInteger);
/*     */ 
/* 197 */     if (str.length() > 76) {
/* 198 */       str = "\n" + str + "\n";
/*     */     }
/*     */ 
/* 201 */     Document localDocument = paramElement.getOwnerDocument();
/* 202 */     Text localText = localDocument.createTextNode(str);
/*     */ 
/* 204 */     paramElement.appendChild(localText);
/*     */   }
/*     */ 
/*     */   public static final byte[] decode(Element paramElement)
/*     */     throws Base64DecodingException
/*     */   {
/* 220 */     Node localNode = paramElement.getFirstChild();
/* 221 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 223 */     while (localNode != null) {
/* 224 */       if (localNode.getNodeType() == 3) {
/* 225 */         Text localText = (Text)localNode;
/*     */ 
/* 227 */         localStringBuffer.append(localText.getData());
/*     */       }
/* 229 */       localNode = localNode.getNextSibling();
/*     */     }
/*     */ 
/* 232 */     return decode(localStringBuffer.toString());
/*     */   }
/*     */ 
/*     */   public static final Element encodeToElement(Document paramDocument, String paramString, byte[] paramArrayOfByte)
/*     */   {
/* 247 */     Element localElement = XMLUtils.createElementInSignatureSpace(paramDocument, paramString);
/* 248 */     Text localText = paramDocument.createTextNode(encode(paramArrayOfByte));
/*     */ 
/* 250 */     localElement.appendChild(localText);
/*     */ 
/* 252 */     return localElement;
/*     */   }
/*     */ 
/*     */   public static final byte[] decode(byte[] paramArrayOfByte)
/*     */     throws Base64DecodingException
/*     */   {
/* 265 */     return decodeInternal(paramArrayOfByte, -1);
/*     */   }
/*     */ 
/*     */   public static final String encode(byte[] paramArrayOfByte)
/*     */   {
/* 278 */     return XMLUtils.ignoreLineBreaks() ? encode(paramArrayOfByte, 2147483647) : encode(paramArrayOfByte, 76);
/*     */   }
/*     */ 
/*     */   public static final byte[] decode(BufferedReader paramBufferedReader)
/*     */     throws IOException, Base64DecodingException
/*     */   {
/* 297 */     UnsyncByteArrayOutputStream localUnsyncByteArrayOutputStream = new UnsyncByteArrayOutputStream();
/*     */     String str;
/* 300 */     while (null != (str = paramBufferedReader.readLine())) {
/* 301 */       byte[] arrayOfByte = decode(str);
/*     */ 
/* 303 */       localUnsyncByteArrayOutputStream.write(arrayOfByte);
/*     */     }
/*     */ 
/* 306 */     return localUnsyncByteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   protected static final boolean isWhiteSpace(byte paramByte)
/*     */   {
/* 353 */     return (paramByte == 32) || (paramByte == 13) || (paramByte == 10) || (paramByte == 9);
/*     */   }
/*     */ 
/*     */   protected static final boolean isPad(byte paramByte) {
/* 357 */     return paramByte == 61;
/*     */   }
/*     */ 
/*     */   public static final String encode(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 377 */     if (paramInt < 4) {
/* 378 */       paramInt = 2147483647;
/*     */     }
/*     */ 
/* 381 */     if (paramArrayOfByte == null) {
/* 382 */       return null;
/*     */     }
/* 384 */     int i = paramArrayOfByte.length * 8;
/* 385 */     if (i == 0) {
/* 386 */       return "";
/*     */     }
/*     */ 
/* 389 */     int j = i % 24;
/* 390 */     int k = i / 24;
/* 391 */     int m = j != 0 ? k + 1 : k;
/* 392 */     int n = paramInt / 4;
/* 393 */     int i1 = (m - 1) / n;
/* 394 */     char[] arrayOfChar = null;
/*     */ 
/* 396 */     arrayOfChar = new char[m * 4 + i1];
/*     */ 
/* 398 */     int i2 = 0; int i3 = 0; int i4 = 0; int i5 = 0; int i6 = 0;
/*     */ 
/* 400 */     int i7 = 0;
/* 401 */     int i8 = 0;
/* 402 */     int i9 = 0;
/*     */     int i11;
/*     */     int i12;
/* 405 */     for (int i10 = 0; i10 < i1; i10++) {
/* 406 */       for (i11 = 0; i11 < 19; i11++) {
/* 407 */         i4 = paramArrayOfByte[(i8++)];
/* 408 */         i5 = paramArrayOfByte[(i8++)];
/* 409 */         i6 = paramArrayOfByte[(i8++)];
/*     */ 
/* 412 */         i3 = (byte)(i5 & 0xF);
/* 413 */         i2 = (byte)(i4 & 0x3);
/*     */ 
/* 415 */         i12 = (i4 & 0xFFFFFF80) == 0 ? (byte)(i4 >> 2) : (byte)(i4 >> 2 ^ 0xC0);
/*     */ 
/* 417 */         int i13 = (i5 & 0xFFFFFF80) == 0 ? (byte)(i5 >> 4) : (byte)(i5 >> 4 ^ 0xF0);
/* 418 */         int i14 = (i6 & 0xFFFFFF80) == 0 ? (byte)(i6 >> 6) : (byte)(i6 >> 6 ^ 0xFC);
/*     */ 
/* 421 */         arrayOfChar[(i7++)] = lookUpBase64Alphabet[i12];
/* 422 */         arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i13 | i2 << 4)];
/* 423 */         arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i3 << 2 | i14)];
/* 424 */         arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i6 & 0x3F)];
/*     */ 
/* 426 */         i9++;
/*     */       }
/* 428 */       arrayOfChar[(i7++)] = '\n';
/*     */     }
/*     */ 
/* 431 */     for (; i9 < k; i9++) {
/* 432 */       i4 = paramArrayOfByte[(i8++)];
/* 433 */       i5 = paramArrayOfByte[(i8++)];
/* 434 */       i6 = paramArrayOfByte[(i8++)];
/*     */ 
/* 437 */       i3 = (byte)(i5 & 0xF);
/* 438 */       i2 = (byte)(i4 & 0x3);
/*     */ 
/* 440 */       i10 = (i4 & 0xFFFFFF80) == 0 ? (byte)(i4 >> 2) : (byte)(i4 >> 2 ^ 0xC0);
/*     */ 
/* 442 */       i11 = (i5 & 0xFFFFFF80) == 0 ? (byte)(i5 >> 4) : (byte)(i5 >> 4 ^ 0xF0);
/* 443 */       i12 = (i6 & 0xFFFFFF80) == 0 ? (byte)(i6 >> 6) : (byte)(i6 >> 6 ^ 0xFC);
/*     */ 
/* 446 */       arrayOfChar[(i7++)] = lookUpBase64Alphabet[i10];
/* 447 */       arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i11 | i2 << 4)];
/* 448 */       arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i3 << 2 | i12)];
/* 449 */       arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i6 & 0x3F)];
/*     */     }
/*     */ 
/* 453 */     if (j == 8) {
/* 454 */       i4 = paramArrayOfByte[i8];
/* 455 */       i2 = (byte)(i4 & 0x3);
/* 456 */       i10 = (i4 & 0xFFFFFF80) == 0 ? (byte)(i4 >> 2) : (byte)(i4 >> 2 ^ 0xC0);
/* 457 */       arrayOfChar[(i7++)] = lookUpBase64Alphabet[i10];
/* 458 */       arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i2 << 4)];
/* 459 */       arrayOfChar[(i7++)] = '=';
/* 460 */       arrayOfChar[(i7++)] = '=';
/* 461 */     } else if (j == 16) {
/* 462 */       i4 = paramArrayOfByte[i8];
/* 463 */       i5 = paramArrayOfByte[(i8 + 1)];
/* 464 */       i3 = (byte)(i5 & 0xF);
/* 465 */       i2 = (byte)(i4 & 0x3);
/*     */ 
/* 467 */       i10 = (i4 & 0xFFFFFF80) == 0 ? (byte)(i4 >> 2) : (byte)(i4 >> 2 ^ 0xC0);
/* 468 */       i11 = (i5 & 0xFFFFFF80) == 0 ? (byte)(i5 >> 4) : (byte)(i5 >> 4 ^ 0xF0);
/*     */ 
/* 470 */       arrayOfChar[(i7++)] = lookUpBase64Alphabet[i10];
/* 471 */       arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i11 | i2 << 4)];
/* 472 */       arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i3 << 2)];
/* 473 */       arrayOfChar[(i7++)] = '=';
/*     */     }
/*     */ 
/* 478 */     return new String(arrayOfChar);
/*     */   }
/*     */ 
/*     */   public static final byte[] decode(String paramString)
/*     */     throws Base64DecodingException
/*     */   {
/* 490 */     if (paramString == null)
/* 491 */       return null;
/* 492 */     byte[] arrayOfByte = new byte[paramString.length()];
/* 493 */     int i = getBytesInternal(paramString, arrayOfByte);
/* 494 */     return decodeInternal(arrayOfByte, i);
/*     */   }
/*     */ 
/*     */   protected static final int getBytesInternal(String paramString, byte[] paramArrayOfByte) {
/* 498 */     int i = paramString.length();
/*     */ 
/* 500 */     int j = 0;
/* 501 */     for (int k = 0; k < i; k++) {
/* 502 */       byte b = (byte)paramString.charAt(k);
/* 503 */       if (!isWhiteSpace(b))
/* 504 */         paramArrayOfByte[(j++)] = b;
/*     */     }
/* 506 */     return j;
/*     */   }
/*     */ 
/*     */   protected static final byte[] decodeInternal(byte[] paramArrayOfByte, int paramInt) throws Base64DecodingException
/*     */   {
/* 511 */     if (paramInt == -1) {
/* 512 */       paramInt = removeWhiteSpace(paramArrayOfByte);
/*     */     }
/* 514 */     if (paramInt % 4 != 0) {
/* 515 */       throw new Base64DecodingException("decoding.divisible.four");
/*     */     }
/*     */ 
/* 519 */     int i = paramInt / 4;
/*     */ 
/* 521 */     if (i == 0) {
/* 522 */       return new byte[0];
/*     */     }
/* 524 */     byte[] arrayOfByte = null;
/* 525 */     int j = 0; int k = 0; int m = 0; int n = 0;
/*     */ 
/* 528 */     int i1 = 0;
/* 529 */     int i2 = 0;
/* 530 */     int i3 = 0;
/*     */ 
/* 533 */     i3 = (i - 1) * 4;
/* 534 */     i2 = (i - 1) * 3;
/*     */ 
/* 536 */     j = base64Alphabet[paramArrayOfByte[(i3++)]];
/* 537 */     k = base64Alphabet[paramArrayOfByte[(i3++)]];
/* 538 */     if ((j == -1) || (k == -1))
/* 539 */       throw new Base64DecodingException("decoding.general");
/*     */     byte b1;
/* 544 */     m = base64Alphabet[(b1 = paramArrayOfByte[(i3++)])];
/*     */     byte b2;
/* 545 */     n = base64Alphabet[(b2 = paramArrayOfByte[(i3++)])];
/* 546 */     if ((m == -1) || (n == -1))
/*     */     {
/* 548 */       if ((isPad(b1)) && (isPad(b2))) {
/* 549 */         if ((k & 0xF) != 0)
/* 550 */           throw new Base64DecodingException("decoding.general");
/* 551 */         arrayOfByte = new byte[i2 + 1];
/* 552 */         arrayOfByte[i2] = ((byte)(j << 2 | k >> 4));
/* 553 */       } else if ((!isPad(b1)) && (isPad(b2))) {
/* 554 */         if ((m & 0x3) != 0)
/* 555 */           throw new Base64DecodingException("decoding.general");
/* 556 */         arrayOfByte = new byte[i2 + 2];
/* 557 */         arrayOfByte[(i2++)] = ((byte)(j << 2 | k >> 4));
/* 558 */         arrayOfByte[i2] = ((byte)((k & 0xF) << 4 | m >> 2 & 0xF));
/*     */       } else {
/* 560 */         throw new Base64DecodingException("decoding.general");
/*     */       }
/*     */     }
/*     */     else {
/* 564 */       arrayOfByte = new byte[i2 + 3];
/* 565 */       arrayOfByte[(i2++)] = ((byte)(j << 2 | k >> 4));
/* 566 */       arrayOfByte[(i2++)] = ((byte)((k & 0xF) << 4 | m >> 2 & 0xF));
/* 567 */       arrayOfByte[(i2++)] = ((byte)(m << 6 | n));
/*     */     }
/* 569 */     i2 = 0;
/* 570 */     i3 = 0;
/*     */ 
/* 572 */     for (i1 = i - 1; i1 > 0; i1--) {
/* 573 */       j = base64Alphabet[paramArrayOfByte[(i3++)]];
/* 574 */       k = base64Alphabet[paramArrayOfByte[(i3++)]];
/* 575 */       m = base64Alphabet[paramArrayOfByte[(i3++)]];
/* 576 */       n = base64Alphabet[paramArrayOfByte[(i3++)]];
/*     */ 
/* 578 */       if ((j == -1) || (k == -1) || (m == -1) || (n == -1))
/*     */       {
/* 582 */         throw new Base64DecodingException("decoding.general");
/*     */       }
/*     */ 
/* 585 */       arrayOfByte[(i2++)] = ((byte)(j << 2 | k >> 4));
/* 586 */       arrayOfByte[(i2++)] = ((byte)((k & 0xF) << 4 | m >> 2 & 0xF));
/* 587 */       arrayOfByte[(i2++)] = ((byte)(m << 6 | n));
/*     */     }
/* 589 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public static final void decode(String paramString, OutputStream paramOutputStream)
/*     */     throws Base64DecodingException, IOException
/*     */   {
/* 601 */     byte[] arrayOfByte = new byte[paramString.length()];
/* 602 */     int i = getBytesInternal(paramString, arrayOfByte);
/* 603 */     decode(arrayOfByte, paramOutputStream, i);
/*     */   }
/*     */ 
/*     */   public static final void decode(byte[] paramArrayOfByte, OutputStream paramOutputStream)
/*     */     throws Base64DecodingException, IOException
/*     */   {
/* 615 */     decode(paramArrayOfByte, paramOutputStream, -1);
/*     */   }
/*     */ 
/*     */   protected static final void decode(byte[] paramArrayOfByte, OutputStream paramOutputStream, int paramInt)
/*     */     throws Base64DecodingException, IOException
/*     */   {
/* 621 */     if (paramInt == -1) {
/* 622 */       paramInt = removeWhiteSpace(paramArrayOfByte);
/*     */     }
/* 624 */     if (paramInt % 4 != 0) {
/* 625 */       throw new Base64DecodingException("decoding.divisible.four");
/*     */     }
/*     */ 
/* 629 */     int i = paramInt / 4;
/*     */ 
/* 631 */     if (i == 0) {
/* 632 */       return;
/*     */     }
/*     */ 
/* 635 */     int j = 0; int k = 0; int m = 0; int n = 0;
/*     */ 
/* 637 */     int i1 = 0;
/*     */ 
/* 639 */     int i2 = 0;
/*     */ 
/* 642 */     for (i1 = i - 1; i1 > 0; i1--) {
/* 643 */       j = base64Alphabet[paramArrayOfByte[(i2++)]];
/* 644 */       k = base64Alphabet[paramArrayOfByte[(i2++)]];
/* 645 */       m = base64Alphabet[paramArrayOfByte[(i2++)]];
/* 646 */       n = base64Alphabet[paramArrayOfByte[(i2++)]];
/* 647 */       if ((j == -1) || (k == -1) || (m == -1) || (n == -1))
/*     */       {
/* 651 */         throw new Base64DecodingException("decoding.general");
/*     */       }
/*     */ 
/* 655 */       paramOutputStream.write((byte)(j << 2 | k >> 4));
/* 656 */       paramOutputStream.write((byte)((k & 0xF) << 4 | m >> 2 & 0xF));
/* 657 */       paramOutputStream.write((byte)(m << 6 | n));
/*     */     }
/* 659 */     j = base64Alphabet[paramArrayOfByte[(i2++)]];
/* 660 */     k = base64Alphabet[paramArrayOfByte[(i2++)]];
/*     */ 
/* 663 */     if ((j == -1) || (k == -1))
/*     */     {
/* 665 */       throw new Base64DecodingException("decoding.general");
/*     */     }
/*     */     byte b1;
/* 669 */     m = base64Alphabet[(b1 = paramArrayOfByte[(i2++)])];
/*     */     byte b2;
/* 670 */     n = base64Alphabet[(b2 = paramArrayOfByte[(i2++)])];
/* 671 */     if ((m == -1) || (n == -1))
/*     */     {
/* 673 */       if ((isPad(b1)) && (isPad(b2))) {
/* 674 */         if ((k & 0xF) != 0)
/* 675 */           throw new Base64DecodingException("decoding.general");
/* 676 */         paramOutputStream.write((byte)(j << 2 | k >> 4));
/* 677 */       } else if ((!isPad(b1)) && (isPad(b2))) {
/* 678 */         if ((m & 0x3) != 0)
/* 679 */           throw new Base64DecodingException("decoding.general");
/* 680 */         paramOutputStream.write((byte)(j << 2 | k >> 4));
/* 681 */         paramOutputStream.write((byte)((k & 0xF) << 4 | m >> 2 & 0xF));
/*     */       } else {
/* 683 */         throw new Base64DecodingException("decoding.general");
/*     */       }
/*     */     }
/*     */     else {
/* 687 */       paramOutputStream.write((byte)(j << 2 | k >> 4));
/* 688 */       paramOutputStream.write((byte)((k & 0xF) << 4 | m >> 2 & 0xF));
/* 689 */       paramOutputStream.write((byte)(m << 6 | n));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final void decode(InputStream paramInputStream, OutputStream paramOutputStream)
/*     */     throws Base64DecodingException, IOException
/*     */   {
/* 705 */     int i = 0; int j = 0; int k = 0; int m = 0;
/*     */ 
/* 707 */     int n = 0;
/* 708 */     byte[] arrayOfByte = new byte[4];
/*     */     int i1;
/* 711 */     while ((i1 = paramInputStream.read()) > 0) {
/* 712 */       b1 = (byte)i1;
/* 713 */       if (!isWhiteSpace(b1))
/*     */       {
/* 716 */         if (isPad(b1)) {
/* 717 */           arrayOfByte[(n++)] = b1;
/* 718 */           if (n != 3) break;
/* 719 */           arrayOfByte[(n++)] = ((byte)paramInputStream.read()); break;
/*     */         }
/*     */ 
/* 724 */         if ((arrayOfByte[(n++)] = b1) == -1) {
/* 725 */           throw new Base64DecodingException("decoding.general");
/*     */         }
/*     */ 
/* 728 */         if (n == 4)
/*     */         {
/* 731 */           n = 0;
/* 732 */           i = base64Alphabet[arrayOfByte[0]];
/* 733 */           j = base64Alphabet[arrayOfByte[1]];
/* 734 */           k = base64Alphabet[arrayOfByte[2]];
/* 735 */           m = base64Alphabet[arrayOfByte[3]];
/*     */ 
/* 737 */           paramOutputStream.write((byte)(i << 2 | j >> 4));
/* 738 */           paramOutputStream.write((byte)((j & 0xF) << 4 | k >> 2 & 0xF));
/* 739 */           paramOutputStream.write((byte)(k << 6 | m));
/*     */         }
/*     */       }
/*     */     }
/* 743 */     byte b1 = arrayOfByte[0]; int i2 = arrayOfByte[1]; byte b2 = arrayOfByte[2]; byte b3 = arrayOfByte[3];
/* 744 */     i = base64Alphabet[b1];
/* 745 */     j = base64Alphabet[i2];
/* 746 */     k = base64Alphabet[b2];
/* 747 */     m = base64Alphabet[b3];
/* 748 */     if ((k == -1) || (m == -1))
/*     */     {
/* 750 */       if ((isPad(b2)) && (isPad(b3))) {
/* 751 */         if ((j & 0xF) != 0)
/* 752 */           throw new Base64DecodingException("decoding.general");
/* 753 */         paramOutputStream.write((byte)(i << 2 | j >> 4));
/* 754 */       } else if ((!isPad(b2)) && (isPad(b3))) {
/* 755 */         k = base64Alphabet[b2];
/* 756 */         if ((k & 0x3) != 0)
/* 757 */           throw new Base64DecodingException("decoding.general");
/* 758 */         paramOutputStream.write((byte)(i << 2 | j >> 4));
/* 759 */         paramOutputStream.write((byte)((j & 0xF) << 4 | k >> 2 & 0xF));
/*     */       } else {
/* 761 */         throw new Base64DecodingException("decoding.general");
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 766 */       paramOutputStream.write((byte)(i << 2 | j >> 4));
/* 767 */       paramOutputStream.write((byte)((j & 0xF) << 4 | k >> 2 & 0xF));
/* 768 */       paramOutputStream.write((byte)(k << 6 | m));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static final int removeWhiteSpace(byte[] paramArrayOfByte)
/*     */   {
/* 780 */     if (paramArrayOfByte == null) {
/* 781 */       return 0;
/*     */     }
/*     */ 
/* 784 */     int i = 0;
/* 785 */     int j = paramArrayOfByte.length;
/* 786 */     for (int k = 0; k < j; k++) {
/* 787 */       byte b = paramArrayOfByte[k];
/* 788 */       if (!isWhiteSpace(b))
/* 789 */         paramArrayOfByte[(i++)] = b;
/*     */     }
/* 791 */     return i;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 322 */     for (int i = 0; i < 255; i++) {
/* 323 */       base64Alphabet[i] = -1;
/*     */     }
/* 325 */     for (i = 90; i >= 65; i--) {
/* 326 */       base64Alphabet[i] = ((byte)(i - 65));
/*     */     }
/* 328 */     for (i = 122; i >= 97; i--) {
/* 329 */       base64Alphabet[i] = ((byte)(i - 97 + 26));
/*     */     }
/*     */ 
/* 332 */     for (i = 57; i >= 48; i--) {
/* 333 */       base64Alphabet[i] = ((byte)(i - 48 + 52));
/*     */     }
/*     */ 
/* 336 */     base64Alphabet[43] = 62;
/* 337 */     base64Alphabet[47] = 63;
/*     */ 
/* 339 */     for (i = 0; i <= 25; i++) {
/* 340 */       lookUpBase64Alphabet[i] = ((char)(65 + i));
/*     */     }
/* 342 */     i = 26; for (int j = 0; i <= 51; j++) {
/* 343 */       lookUpBase64Alphabet[i] = ((char)(97 + j));
/*     */ 
/* 342 */       i++;
/*     */     }
/*     */ 
/* 345 */     i = 52; for (j = 0; i <= 61; j++) {
/* 346 */       lookUpBase64Alphabet[i] = ((char)(48 + j));
/*     */ 
/* 345 */       i++;
/*     */     }
/* 347 */     lookUpBase64Alphabet[62] = '+';
/* 348 */     lookUpBase64Alphabet[63] = '/';
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.Base64
 * JD-Core Version:    0.6.2
 */