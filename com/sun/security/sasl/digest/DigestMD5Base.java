/*      */ package com.sun.security.sasl.digest;
/*      */ 
/*      */ import com.sun.security.sasl.util.AbstractSaslImpl;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.math.BigInteger;
/*      */ import java.security.InvalidAlgorithmParameterException;
/*      */ import java.security.InvalidKeyException;
/*      */ import java.security.Key;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.security.spec.InvalidKeySpecException;
/*      */ import java.security.spec.KeySpec;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.crypto.Cipher;
/*      */ import javax.crypto.IllegalBlockSizeException;
/*      */ import javax.crypto.Mac;
/*      */ import javax.crypto.NoSuchPaddingException;
/*      */ import javax.crypto.SecretKey;
/*      */ import javax.crypto.SecretKeyFactory;
/*      */ import javax.crypto.spec.DESKeySpec;
/*      */ import javax.crypto.spec.DESedeKeySpec;
/*      */ import javax.crypto.spec.IvParameterSpec;
/*      */ import javax.crypto.spec.SecretKeySpec;
/*      */ import javax.security.auth.callback.CallbackHandler;
/*      */ import javax.security.sasl.SaslException;
/*      */ 
/*      */ abstract class DigestMD5Base extends AbstractSaslImpl
/*      */ {
/*   83 */   private static final String DI_CLASS_NAME = DigestIntegrity.class.getName();
/*   84 */   private static final String DP_CLASS_NAME = DigestPrivacy.class.getName();
/*      */   protected static final int MAX_CHALLENGE_LENGTH = 2048;
/*      */   protected static final int MAX_RESPONSE_LENGTH = 4096;
/*      */   protected static final int DEFAULT_MAXBUF = 65536;
/*      */   protected static final int DES3 = 0;
/*      */   protected static final int RC4 = 1;
/*      */   protected static final int DES = 2;
/*      */   protected static final int RC4_56 = 3;
/*      */   protected static final int RC4_40 = 4;
/*   97 */   protected static final String[] CIPHER_TOKENS = { "3des", "rc4", "des", "rc4-56", "rc4-40" };
/*      */ 
/*  102 */   private static final String[] JCE_CIPHER_NAME = { "DESede/CBC/NoPadding", "RC4", "DES/CBC/NoPadding" };
/*      */   protected static final byte DES_3_STRENGTH = 4;
/*      */   protected static final byte RC4_STRENGTH = 4;
/*      */   protected static final byte DES_STRENGTH = 2;
/*      */   protected static final byte RC4_56_STRENGTH = 2;
/*      */   protected static final byte RC4_40_STRENGTH = 1;
/*      */   protected static final byte UNSET = 0;
/*  130 */   protected static final byte[] CIPHER_MASKS = { 4, 4, 2, 2, 1 };
/*      */   private static final String SECURITY_LAYER_MARKER = ":00000000000000000000000000000000";
/*  139 */   protected static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*      */   protected int step;
/*      */   protected CallbackHandler cbh;
/*      */   protected SecurityCtx secCtx;
/*      */   protected byte[] H_A1;
/*      */   protected byte[] nonce;
/*      */   protected String negotiatedStrength;
/*      */   protected String negotiatedCipher;
/*      */   protected String negotiatedQop;
/*      */   protected String negotiatedRealm;
/*  160 */   protected boolean useUTF8 = false;
/*  161 */   protected String encoding = "8859_1";
/*      */   protected String digestUri;
/*      */   protected String authzid;
/*  279 */   private static final char[] pem_array = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
/*      */   private static final int RAW_NONCE_SIZE = 30;
/*      */   private static final int ENCODED_NONCE_SIZE = 40;
/* 1519 */   private static final BigInteger MASK = new BigInteger("7f", 16);
/*      */ 
/*      */   protected DigestMD5Base(Map paramMap, String paramString1, int paramInt, String paramString2, CallbackHandler paramCallbackHandler)
/*      */     throws SaslException
/*      */   {
/*  180 */     super(paramMap, paramString1);
/*      */ 
/*  182 */     this.step = paramInt;
/*  183 */     this.digestUri = paramString2;
/*  184 */     this.cbh = paramCallbackHandler;
/*      */   }
/*      */ 
/*      */   public String getMechanismName()
/*      */   {
/*  193 */     return "DIGEST-MD5";
/*      */   }
/*      */ 
/*      */   public byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws SaslException
/*      */   {
/*  209 */     if (!this.completed) {
/*  210 */       throw new IllegalStateException("DIGEST-MD5 authentication not completed");
/*      */     }
/*      */ 
/*  214 */     if (this.secCtx == null) {
/*  215 */       throw new IllegalStateException("Neither integrity nor privacy was negotiated");
/*      */     }
/*      */ 
/*  219 */     return this.secCtx.unwrap(paramArrayOfByte, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws SaslException
/*      */   {
/*  235 */     if (!this.completed) {
/*  236 */       throw new IllegalStateException("DIGEST-MD5 authentication not completed");
/*      */     }
/*      */ 
/*  240 */     if (this.secCtx == null) {
/*  241 */       throw new IllegalStateException("Neither integrity nor privacy was negotiated");
/*      */     }
/*      */ 
/*  245 */     return this.secCtx.wrap(paramArrayOfByte, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void dispose() throws SaslException {
/*  249 */     if (this.secCtx != null)
/*  250 */       this.secCtx = null;
/*      */   }
/*      */ 
/*      */   public Object getNegotiatedProperty(String paramString)
/*      */   {
/*  255 */     if (this.completed) {
/*  256 */       if (paramString.equals("javax.security.sasl.strength")) {
/*  257 */         return this.negotiatedStrength;
/*      */       }
/*  259 */       return super.getNegotiatedProperty(paramString);
/*      */     }
/*      */ 
/*  262 */     throw new IllegalStateException("DIGEST-MD5 authentication not completed");
/*      */   }
/*      */ 
/*      */   protected static final byte[] generateNonce()
/*      */   {
/*  300 */     Random localRandom = new Random();
/*  301 */     byte[] arrayOfByte1 = new byte[30];
/*  302 */     localRandom.nextBytes(arrayOfByte1);
/*      */ 
/*  304 */     byte[] arrayOfByte2 = new byte[40];
/*      */ 
/*  308 */     int m = 0;
/*  309 */     for (int n = 0; n < arrayOfByte1.length; n += 3) {
/*  310 */       int i = arrayOfByte1[n];
/*  311 */       int j = arrayOfByte1[(n + 1)];
/*  312 */       int k = arrayOfByte1[(n + 2)];
/*  313 */       arrayOfByte2[(m++)] = ((byte)pem_array[(i >>> 2 & 0x3F)]);
/*  314 */       arrayOfByte2[(m++)] = ((byte)pem_array[((i << 4 & 0x30) + (j >>> 4 & 0xF))]);
/*  315 */       arrayOfByte2[(m++)] = ((byte)pem_array[((j << 2 & 0x3C) + (k >>> 6 & 0x3))]);
/*  316 */       arrayOfByte2[(m++)] = ((byte)pem_array[(k & 0x3F)]);
/*      */     }
/*      */ 
/*  319 */     return arrayOfByte2;
/*      */   }
/*      */ 
/*      */   protected static void writeQuotedStringValue(ByteArrayOutputStream paramByteArrayOutputStream, byte[] paramArrayOfByte)
/*      */   {
/*  333 */     int i = paramArrayOfByte.length;
/*      */ 
/*  335 */     for (int k = 0; k < i; k++) {
/*  336 */       int j = paramArrayOfByte[k];
/*  337 */       if (needEscape((char)j)) {
/*  338 */         paramByteArrayOutputStream.write(92);
/*      */       }
/*  340 */       paramByteArrayOutputStream.write(j);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean needEscape(String paramString)
/*      */   {
/*  347 */     int i = paramString.length();
/*  348 */     for (int j = 0; j < i; j++) {
/*  349 */       if (needEscape(paramString.charAt(j))) {
/*  350 */         return true;
/*      */       }
/*      */     }
/*  353 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean needEscape(char paramChar)
/*      */   {
/*  358 */     return (paramChar == '"') || (paramChar == '\\') || (paramChar == '') || ((paramChar >= 0) && (paramChar <= '\037') && (paramChar != '\r') && (paramChar != '\t') && (paramChar != '\n'));
/*      */   }
/*      */ 
/*      */   protected static String quotedStringValue(String paramString)
/*      */   {
/*  367 */     if (needEscape(paramString)) {
/*  368 */       int i = paramString.length();
/*  369 */       char[] arrayOfChar = new char[i + i];
/*  370 */       int j = 0;
/*      */ 
/*  372 */       for (int k = 0; k < i; k++) {
/*  373 */         char c = paramString.charAt(k);
/*  374 */         if (needEscape(c)) {
/*  375 */           arrayOfChar[(j++)] = '\\';
/*      */         }
/*  377 */         arrayOfChar[(j++)] = c;
/*      */       }
/*  379 */       return new String(arrayOfChar, 0, j);
/*      */     }
/*  381 */     return paramString;
/*      */   }
/*      */ 
/*      */   protected byte[] binaryToHex(byte[] paramArrayOfByte)
/*      */     throws UnsupportedEncodingException
/*      */   {
/*  394 */     StringBuffer localStringBuffer = new StringBuffer();
/*      */ 
/*  396 */     for (int i = 0; i < paramArrayOfByte.length; i++) {
/*  397 */       if ((paramArrayOfByte[i] & 0xFF) < 16) {
/*  398 */         localStringBuffer.append("0" + Integer.toHexString(paramArrayOfByte[i] & 0xFF));
/*      */       }
/*      */       else {
/*  401 */         localStringBuffer.append(Integer.toHexString(paramArrayOfByte[i] & 0xFF));
/*      */       }
/*      */     }
/*      */ 
/*  405 */     return localStringBuffer.toString().getBytes(this.encoding);
/*      */   }
/*      */ 
/*      */   protected byte[] stringToByte_8859_1(String paramString)
/*      */     throws SaslException
/*      */   {
/*  418 */     char[] arrayOfChar = paramString.toCharArray();
/*      */     try
/*      */     {
/*  421 */       if (this.useUTF8) {
/*  422 */         for (int i = 0; i < arrayOfChar.length; i++) {
/*  423 */           if (arrayOfChar[i] > 'ÿ') {
/*  424 */             return paramString.getBytes("UTF8");
/*      */           }
/*      */         }
/*      */       }
/*  428 */       return paramString.getBytes("8859_1");
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*  430 */       throw new SaslException("cannot encode string in UTF8 or 8859-1 (Latin-1)", localUnsupportedEncodingException);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static byte[] getPlatformCiphers()
/*      */   {
/*  436 */     byte[] arrayOfByte = new byte[CIPHER_TOKENS.length];
/*      */ 
/*  438 */     for (int i = 0; i < JCE_CIPHER_NAME.length; i++)
/*      */     {
/*      */       try
/*      */       {
/*  442 */         Cipher.getInstance(JCE_CIPHER_NAME[i]);
/*      */ 
/*  444 */         logger.log(Level.FINE, "DIGEST01:Platform supports {0}", JCE_CIPHER_NAME[i]);
/*      */         int tmp44_43 = i; arrayOfByte[tmp44_43] = ((byte)(arrayOfByte[tmp44_43] | CIPHER_MASKS[i]));
/*      */       }
/*      */       catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*      */       {
/*      */       }
/*      */       catch (NoSuchPaddingException localNoSuchPaddingException) {
/*      */       }
/*      */     }
/*  453 */     if (arrayOfByte[1] != 0)
/*      */     {
/*      */       int tmp76_75 = 3; arrayOfByte[tmp76_75] = ((byte)(arrayOfByte[tmp76_75] | CIPHER_MASKS[3]));
/*      */       int tmp88_87 = 4; arrayOfByte[tmp88_87] = ((byte)(arrayOfByte[tmp88_87] | CIPHER_MASKS[4]));
/*      */     }
/*      */ 
/*  458 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   protected byte[] generateResponseValue(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, char[] paramArrayOfChar, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt, byte[] paramArrayOfByte3)
/*      */     throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException
/*      */   {
/*  489 */     MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
/*      */ 
/*  498 */     ByteArrayOutputStream localByteArrayOutputStream1 = new ByteArrayOutputStream();
/*  499 */     localByteArrayOutputStream1.write((paramString1 + ":" + paramString2).getBytes(this.encoding));
/*  500 */     if ((paramString3.equals("auth-conf")) || (paramString3.equals("auth-int")))
/*      */     {
/*  503 */       logger.log(Level.FINE, "DIGEST04:QOP: {0}", paramString3);
/*      */ 
/*  505 */       localByteArrayOutputStream1.write(":00000000000000000000000000000000".getBytes(this.encoding));
/*      */     }
/*      */ 
/*  508 */     if (logger.isLoggable(Level.FINE)) {
/*  509 */       logger.log(Level.FINE, "DIGEST05:A2: {0}", localByteArrayOutputStream1.toString());
/*      */     }
/*      */ 
/*  512 */     localMessageDigest.update(localByteArrayOutputStream1.toByteArray());
/*  513 */     byte[] arrayOfByte3 = localMessageDigest.digest();
/*  514 */     byte[] arrayOfByte2 = binaryToHex(arrayOfByte3);
/*      */ 
/*  516 */     if (logger.isLoggable(Level.FINE)) {
/*  517 */       logger.log(Level.FINE, "DIGEST06:HEX(H(A2)): {0}", new String(arrayOfByte2));
/*      */     }
/*      */ 
/*  524 */     ByteArrayOutputStream localByteArrayOutputStream2 = new ByteArrayOutputStream();
/*  525 */     localByteArrayOutputStream2.write(stringToByte_8859_1(paramString4));
/*  526 */     localByteArrayOutputStream2.write(58);
/*      */ 
/*  528 */     localByteArrayOutputStream2.write(stringToByte_8859_1(paramString5));
/*  529 */     localByteArrayOutputStream2.write(58);
/*  530 */     localByteArrayOutputStream2.write(stringToByte_8859_1(new String(paramArrayOfChar)));
/*      */ 
/*  532 */     localMessageDigest.update(localByteArrayOutputStream2.toByteArray());
/*  533 */     arrayOfByte3 = localMessageDigest.digest();
/*      */ 
/*  535 */     if (logger.isLoggable(Level.FINE)) {
/*  536 */       logger.log(Level.FINE, "DIGEST07:H({0}) = {1}", new Object[] { localByteArrayOutputStream2.toString(), new String(binaryToHex(arrayOfByte3)) });
/*      */     }
/*      */ 
/*  545 */     ByteArrayOutputStream localByteArrayOutputStream3 = new ByteArrayOutputStream();
/*  546 */     localByteArrayOutputStream3.write(arrayOfByte3);
/*  547 */     localByteArrayOutputStream3.write(58);
/*  548 */     localByteArrayOutputStream3.write(paramArrayOfByte1);
/*  549 */     localByteArrayOutputStream3.write(58);
/*  550 */     localByteArrayOutputStream3.write(paramArrayOfByte2);
/*      */ 
/*  552 */     if (paramArrayOfByte3 != null) {
/*  553 */       localByteArrayOutputStream3.write(58);
/*  554 */       localByteArrayOutputStream3.write(paramArrayOfByte3);
/*      */     }
/*  556 */     localMessageDigest.update(localByteArrayOutputStream3.toByteArray());
/*  557 */     arrayOfByte3 = localMessageDigest.digest();
/*  558 */     this.H_A1 = arrayOfByte3;
/*  559 */     byte[] arrayOfByte1 = binaryToHex(arrayOfByte3);
/*      */ 
/*  561 */     if (logger.isLoggable(Level.FINE)) {
/*  562 */       logger.log(Level.FINE, "DIGEST08:H(A1) = {0}", new String(arrayOfByte1));
/*      */     }
/*      */ 
/*  568 */     ByteArrayOutputStream localByteArrayOutputStream4 = new ByteArrayOutputStream();
/*  569 */     localByteArrayOutputStream4.write(arrayOfByte1);
/*  570 */     localByteArrayOutputStream4.write(58);
/*  571 */     localByteArrayOutputStream4.write(paramArrayOfByte1);
/*  572 */     localByteArrayOutputStream4.write(58);
/*  573 */     localByteArrayOutputStream4.write(nonceCountToHex(paramInt).getBytes(this.encoding));
/*  574 */     localByteArrayOutputStream4.write(58);
/*  575 */     localByteArrayOutputStream4.write(paramArrayOfByte2);
/*  576 */     localByteArrayOutputStream4.write(58);
/*  577 */     localByteArrayOutputStream4.write(paramString3.getBytes(this.encoding));
/*  578 */     localByteArrayOutputStream4.write(58);
/*  579 */     localByteArrayOutputStream4.write(arrayOfByte2);
/*      */ 
/*  581 */     if (logger.isLoggable(Level.FINE)) {
/*  582 */       logger.log(Level.FINE, "DIGEST09:KD: {0}", localByteArrayOutputStream4.toString());
/*      */     }
/*      */ 
/*  585 */     localMessageDigest.update(localByteArrayOutputStream4.toByteArray());
/*  586 */     arrayOfByte3 = localMessageDigest.digest();
/*      */ 
/*  588 */     byte[] arrayOfByte4 = binaryToHex(arrayOfByte3);
/*      */ 
/*  590 */     if (logger.isLoggable(Level.FINE)) {
/*  591 */       logger.log(Level.FINE, "DIGEST10:response-value: {0}", new String(arrayOfByte4));
/*      */     }
/*      */ 
/*  594 */     return arrayOfByte4;
/*      */   }
/*      */ 
/*      */   protected static String nonceCountToHex(int paramInt)
/*      */   {
/*  604 */     String str = Integer.toHexString(paramInt);
/*  605 */     StringBuffer localStringBuffer = new StringBuffer();
/*      */ 
/*  607 */     if (str.length() < 8) {
/*  608 */       for (int i = 0; i < 8 - str.length(); i++) {
/*  609 */         localStringBuffer.append("0");
/*      */       }
/*      */     }
/*      */ 
/*  613 */     return localStringBuffer.toString() + str;
/*      */   }
/*      */ 
/*      */   protected static byte[][] parseDirectives(byte[] paramArrayOfByte, String[] paramArrayOfString, List<byte[]> paramList, int paramInt)
/*      */     throws SaslException
/*      */   {
/*  628 */     byte[][] arrayOfByte = new byte[paramArrayOfString.length][];
/*      */ 
/*  630 */     ByteArrayOutputStream localByteArrayOutputStream1 = new ByteArrayOutputStream(10);
/*  631 */     ByteArrayOutputStream localByteArrayOutputStream2 = new ByteArrayOutputStream(10);
/*  632 */     int i = 1;
/*  633 */     int j = 0;
/*  634 */     int k = 0;
/*      */ 
/*  637 */     int m = skipLws(paramArrayOfByte, 0);
/*  638 */     while (m < paramArrayOfByte.length) {
/*  639 */       byte b = paramArrayOfByte[m];
/*      */ 
/*  641 */       if (i != 0) {
/*  642 */         if (b == 44) {
/*  643 */           if (localByteArrayOutputStream1.size() != 0) {
/*  644 */             throw new SaslException("Directive key contains a ',':" + localByteArrayOutputStream1);
/*      */           }
/*      */ 
/*  648 */           m = skipLws(paramArrayOfByte, m + 1);
/*      */         }
/*  650 */         else if (b == 61) {
/*  651 */           if (localByteArrayOutputStream1.size() == 0) {
/*  652 */             throw new SaslException("Empty directive key");
/*      */           }
/*  654 */           i = 0;
/*  655 */           m = skipLws(paramArrayOfByte, m + 1);
/*      */ 
/*  658 */           if (m < paramArrayOfByte.length) {
/*  659 */             if (paramArrayOfByte[m] == 34) {
/*  660 */               j = 1;
/*  661 */               m++;
/*      */             }
/*      */           }
/*  664 */           else throw new SaslException("Valueless directive found: " + localByteArrayOutputStream1.toString());
/*      */ 
/*      */         }
/*  667 */         else if (isLws(b))
/*      */         {
/*  669 */           m = skipLws(paramArrayOfByte, m + 1);
/*      */ 
/*  672 */           if (m < paramArrayOfByte.length) {
/*  673 */             if (paramArrayOfByte[m] != 61) {
/*  674 */               throw new SaslException("'=' expected after key: " + localByteArrayOutputStream1.toString());
/*      */             }
/*      */           }
/*      */           else
/*  678 */             throw new SaslException("'=' expected after key: " + localByteArrayOutputStream1.toString());
/*      */         }
/*      */         else
/*      */         {
/*  682 */           localByteArrayOutputStream1.write(b);
/*  683 */           m++;
/*      */         }
/*  685 */       } else if (j != 0)
/*      */       {
/*  687 */         if (b == 92)
/*      */         {
/*  689 */           m++;
/*  690 */           if (m < paramArrayOfByte.length) {
/*  691 */             localByteArrayOutputStream2.write(paramArrayOfByte[m]);
/*  692 */             m++;
/*      */           }
/*      */           else {
/*  695 */             throw new SaslException("Unmatched quote found for directive: " + localByteArrayOutputStream1.toString() + " with value: " + localByteArrayOutputStream2.toString());
/*      */           }
/*      */ 
/*      */         }
/*  699 */         else if (b == 34)
/*      */         {
/*  701 */           m++;
/*  702 */           j = 0;
/*  703 */           k = 1;
/*      */         } else {
/*  705 */           localByteArrayOutputStream2.write(b);
/*  706 */           m++;
/*      */         }
/*      */       }
/*  709 */       else if ((isLws(b)) || (b == 44))
/*      */       {
/*  712 */         extractDirective(localByteArrayOutputStream1.toString(), localByteArrayOutputStream2.toByteArray(), paramArrayOfString, arrayOfByte, paramList, paramInt);
/*      */ 
/*  714 */         localByteArrayOutputStream1.reset();
/*  715 */         localByteArrayOutputStream2.reset();
/*  716 */         i = 1;
/*  717 */         j = k = 0;
/*  718 */         m = skipLws(paramArrayOfByte, m + 1);
/*      */       } else {
/*  720 */         if (k != 0) {
/*  721 */           throw new SaslException("Expecting comma or linear whitespace after quoted string: \"" + localByteArrayOutputStream2.toString() + "\"");
/*      */         }
/*      */ 
/*  725 */         localByteArrayOutputStream2.write(b);
/*  726 */         m++;
/*      */       }
/*      */     }
/*      */ 
/*  730 */     if (j != 0) {
/*  731 */       throw new SaslException("Unmatched quote found for directive: " + localByteArrayOutputStream1.toString() + " with value: " + localByteArrayOutputStream2.toString());
/*      */     }
/*      */ 
/*  737 */     if (localByteArrayOutputStream1.size() > 0) {
/*  738 */       extractDirective(localByteArrayOutputStream1.toString(), localByteArrayOutputStream2.toByteArray(), paramArrayOfString, arrayOfByte, paramList, paramInt);
/*      */     }
/*      */ 
/*  742 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   private static boolean isLws(byte paramByte)
/*      */   {
/*  749 */     switch (paramByte) {
/*      */     case 9:
/*      */     case 10:
/*      */     case 13:
/*      */     case 32:
/*  754 */       return true;
/*      */     }
/*  756 */     return false;
/*      */   }
/*      */ 
/*      */   private static int skipLws(byte[] paramArrayOfByte, int paramInt)
/*      */   {
/*  762 */     for (int i = paramInt; i < paramArrayOfByte.length; i++) {
/*  763 */       if (!isLws(paramArrayOfByte[i])) {
/*  764 */         return i;
/*      */       }
/*      */     }
/*  767 */     return i;
/*      */   }
/*      */ 
/*      */   private static void extractDirective(String paramString, byte[] paramArrayOfByte, String[] paramArrayOfString, byte[][] paramArrayOfByte1, List<byte[]> paramList, int paramInt)
/*      */     throws SaslException
/*      */   {
/*  782 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*  783 */       if (paramString.equalsIgnoreCase(paramArrayOfString[i])) {
/*  784 */         if (paramArrayOfByte1[i] == null) {
/*  785 */           paramArrayOfByte1[i] = paramArrayOfByte;
/*  786 */           if (!logger.isLoggable(Level.FINE)) break;
/*  787 */           logger.log(Level.FINE, "DIGEST11:Directive {0} = {1}", new Object[] { paramArrayOfString[i], new String(paramArrayOfByte1[i]) }); break;
/*      */         }
/*      */ 
/*  792 */         if ((paramList != null) && (i == paramInt))
/*      */         {
/*  794 */           if (paramList.size() == 0) {
/*  795 */             paramList.add(paramArrayOfByte1[i]);
/*      */           }
/*  797 */           paramList.add(paramArrayOfByte); break;
/*      */         }
/*  799 */         throw new SaslException("DIGEST-MD5: peer sent more than one " + paramString + " directive: " + new String(paramArrayOfByte));
/*      */       }
/*      */   }
/*      */ 
/*      */   private static void setParityBit(byte[] paramArrayOfByte)
/*      */   {
/* 1526 */     for (int i = 0; i < paramArrayOfByte.length; i++) {
/* 1527 */       int j = paramArrayOfByte[i] & 0xFE;
/* 1528 */       j |= Integer.bitCount(j) & 0x1 ^ 0x1;
/* 1529 */       paramArrayOfByte[i] = ((byte)j);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static byte[] addDesParity(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/* 1543 */     if (paramInt2 != 7) {
/* 1544 */       throw new IllegalArgumentException("Invalid length of DES Key Value:" + paramInt2);
/*      */     }
/*      */ 
/* 1547 */     byte[] arrayOfByte1 = new byte[7];
/* 1548 */     System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte1, 0, paramInt2);
/*      */ 
/* 1550 */     byte[] arrayOfByte2 = new byte[8];
/* 1551 */     BigInteger localBigInteger = new BigInteger(arrayOfByte1);
/*      */ 
/* 1554 */     for (int i = arrayOfByte2.length - 1; i >= 0; i--) {
/* 1555 */       arrayOfByte2[i] = localBigInteger.and(MASK).toByteArray()[0];
/*      */       int tmp96_94 = i;
/*      */       byte[] tmp96_92 = arrayOfByte2; tmp96_92[tmp96_94] = ((byte)(tmp96_92[tmp96_94] << 1));
/* 1557 */       localBigInteger = localBigInteger.shiftRight(7);
/*      */     }
/* 1559 */     setParityBit(arrayOfByte2);
/* 1560 */     return arrayOfByte2;
/*      */   }
/*      */ 
/*      */   private static SecretKey makeDesKeys(byte[] paramArrayOfByte, String paramString)
/*      */     throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException
/*      */   {
/* 1583 */     byte[] arrayOfByte1 = addDesParity(paramArrayOfByte, 0, 7);
/*      */ 
/* 1585 */     Object localObject = null;
/* 1586 */     SecretKeyFactory localSecretKeyFactory = SecretKeyFactory.getInstance(paramString);
/*      */ 
/* 1589 */     if (paramString.equals("des")) {
/* 1590 */       localObject = new DESKeySpec(arrayOfByte1, 0);
/* 1591 */       if (logger.isLoggable(Level.FINEST)) {
/* 1592 */         traceOutput(DP_CLASS_NAME, "makeDesKeys", "DIGEST42:DES key input: ", paramArrayOfByte);
/*      */ 
/* 1594 */         traceOutput(DP_CLASS_NAME, "makeDesKeys", "DIGEST43:DES key parity-adjusted: ", arrayOfByte1);
/*      */ 
/* 1596 */         traceOutput(DP_CLASS_NAME, "makeDesKeys", "DIGEST44:DES key material: ", ((DESKeySpec)localObject).getKey());
/*      */ 
/* 1598 */         logger.log(Level.FINEST, "DIGEST45: is parity-adjusted? {0}", Boolean.valueOf(DESKeySpec.isParityAdjusted(arrayOfByte1, 0)));
/*      */       }
/*      */ 
/*      */     }
/* 1602 */     else if (paramString.equals("desede"))
/*      */     {
/* 1605 */       byte[] arrayOfByte2 = addDesParity(paramArrayOfByte, 7, 7);
/*      */ 
/* 1608 */       byte[] arrayOfByte3 = new byte[arrayOfByte1.length * 2 + arrayOfByte2.length];
/* 1609 */       System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, arrayOfByte1.length);
/* 1610 */       System.arraycopy(arrayOfByte2, 0, arrayOfByte3, arrayOfByte1.length, arrayOfByte2.length);
/* 1611 */       System.arraycopy(arrayOfByte1, 0, arrayOfByte3, arrayOfByte1.length + arrayOfByte2.length, arrayOfByte1.length);
/*      */ 
/* 1614 */       localObject = new DESedeKeySpec(arrayOfByte3, 0);
/* 1615 */       if (logger.isLoggable(Level.FINEST)) {
/* 1616 */         traceOutput(DP_CLASS_NAME, "makeDesKeys", "DIGEST46:3DES key input: ", paramArrayOfByte);
/*      */ 
/* 1618 */         traceOutput(DP_CLASS_NAME, "makeDesKeys", "DIGEST47:3DES key ede: ", arrayOfByte3);
/*      */ 
/* 1620 */         traceOutput(DP_CLASS_NAME, "makeDesKeys", "DIGEST48:3DES key material: ", ((DESedeKeySpec)localObject).getKey());
/*      */ 
/* 1623 */         logger.log(Level.FINEST, "DIGEST49: is parity-adjusted? ", Boolean.valueOf(DESedeKeySpec.isParityAdjusted(arrayOfByte3, 0)));
/*      */       }
/*      */     }
/*      */     else {
/* 1627 */       throw new IllegalArgumentException("Invalid DES strength:" + paramString);
/*      */     }
/*      */ 
/* 1630 */     return localSecretKeyFactory.generateSecret((KeySpec)localObject);
/*      */   }
/*      */ 
/*      */   class DigestIntegrity
/*      */     implements SecurityCtx
/*      */   {
/*      */     private static final String CLIENT_INT_MAGIC = "Digest session key to client-to-server signing key magic constant";
/*      */     private static final String SVR_INT_MAGIC = "Digest session key to server-to-client signing key magic constant";
/*      */     protected byte[] myKi;
/*      */     protected byte[] peerKi;
/*  833 */     protected int mySeqNum = 0;
/*  834 */     protected int peerSeqNum = 0;
/*      */ 
/*  837 */     protected final byte[] messageType = new byte[2];
/*  838 */     protected final byte[] sequenceNum = new byte[4];
/*      */ 
/*      */     DigestIntegrity(boolean arg2)
/*      */       throws SaslException
/*      */     {
/*      */       try
/*      */       {
/*      */         boolean bool;
/*  851 */         generateIntegrityKeyPair(bool);
/*      */       }
/*      */       catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*  854 */         throw new SaslException("DIGEST-MD5: Error encoding strings into UTF-8", localUnsupportedEncodingException);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*  858 */         throw new SaslException("DIGEST-MD5: Error accessing buffers required to create integrity key pairs", localIOException);
/*      */       }
/*      */       catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*      */       {
/*  862 */         throw new SaslException("DIGEST-MD5: Unsupported digest algorithm used to create integrity key pairs", localNoSuchAlgorithmException);
/*      */       }
/*      */ 
/*  867 */       DigestMD5Base.intToNetworkByteOrder(1, this.messageType, 0, 2);
/*      */     }
/*      */ 
/*      */     private void generateIntegrityKeyPair(boolean paramBoolean)
/*      */       throws UnsupportedEncodingException, IOException, NoSuchAlgorithmException
/*      */     {
/*  885 */       byte[] arrayOfByte1 = "Digest session key to client-to-server signing key magic constant".getBytes(DigestMD5Base.this.encoding);
/*  886 */       byte[] arrayOfByte2 = "Digest session key to server-to-client signing key magic constant".getBytes(DigestMD5Base.this.encoding);
/*      */ 
/*  888 */       MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
/*      */ 
/*  891 */       byte[] arrayOfByte3 = new byte[DigestMD5Base.this.H_A1.length + arrayOfByte1.length];
/*      */ 
/*  894 */       System.arraycopy(DigestMD5Base.this.H_A1, 0, arrayOfByte3, 0, DigestMD5Base.this.H_A1.length);
/*  895 */       System.arraycopy(arrayOfByte1, 0, arrayOfByte3, DigestMD5Base.this.H_A1.length, arrayOfByte1.length);
/*  896 */       localMessageDigest.update(arrayOfByte3);
/*  897 */       byte[] arrayOfByte4 = localMessageDigest.digest();
/*      */ 
/*  901 */       System.arraycopy(arrayOfByte2, 0, arrayOfByte3, DigestMD5Base.this.H_A1.length, arrayOfByte2.length);
/*      */ 
/*  903 */       localMessageDigest.update(arrayOfByte3);
/*  904 */       byte[] arrayOfByte5 = localMessageDigest.digest();
/*      */ 
/*  906 */       if (DigestMD5Base.logger.isLoggable(Level.FINER)) {
/*  907 */         DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "generateIntegrityKeyPair", "DIGEST12:Kic: ", arrayOfByte4);
/*      */ 
/*  909 */         DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "generateIntegrityKeyPair", "DIGEST13:Kis: ", arrayOfByte5);
/*      */       }
/*      */ 
/*  913 */       if (paramBoolean) {
/*  914 */         this.myKi = arrayOfByte4;
/*  915 */         this.peerKi = arrayOfByte5;
/*      */       } else {
/*  917 */         this.myKi = arrayOfByte5;
/*  918 */         this.peerKi = arrayOfByte4;
/*      */       }
/*      */     }
/*      */ 
/*      */     public byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */       throws SaslException
/*      */     {
/*  937 */       if (paramInt2 == 0) {
/*  938 */         return DigestMD5Base.EMPTY_BYTE_ARRAY;
/*      */       }
/*      */ 
/*  942 */       byte[] arrayOfByte1 = new byte[paramInt2 + 10 + 2 + 4];
/*      */ 
/*  945 */       System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte1, 0, paramInt2);
/*      */ 
/*  947 */       incrementSeqNum();
/*      */ 
/*  950 */       byte[] arrayOfByte2 = getHMAC(this.myKi, this.sequenceNum, paramArrayOfByte, paramInt1, paramInt2);
/*      */ 
/*  952 */       if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
/*  953 */         DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "wrap", "DIGEST14:outgoing: ", paramArrayOfByte, paramInt1, paramInt2);
/*      */ 
/*  955 */         DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "wrap", "DIGEST15:seqNum: ", this.sequenceNum);
/*      */ 
/*  957 */         DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "wrap", "DIGEST16:MAC: ", arrayOfByte2);
/*      */       }
/*      */ 
/*  961 */       System.arraycopy(arrayOfByte2, 0, arrayOfByte1, paramInt2, 10);
/*      */ 
/*  964 */       System.arraycopy(this.messageType, 0, arrayOfByte1, paramInt2 + 10, 2);
/*      */ 
/*  967 */       System.arraycopy(this.sequenceNum, 0, arrayOfByte1, paramInt2 + 12, 4);
/*  968 */       if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
/*  969 */         DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "wrap", "DIGEST17:wrapped: ", arrayOfByte1);
/*      */       }
/*  971 */       return arrayOfByte1;
/*      */     }
/*      */ 
/*      */     public byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */       throws SaslException
/*      */     {
/*  992 */       if (paramInt2 == 0) {
/*  993 */         return DigestMD5Base.EMPTY_BYTE_ARRAY;
/*      */       }
/*      */ 
/*  997 */       byte[] arrayOfByte1 = new byte[10];
/*  998 */       byte[] arrayOfByte2 = new byte[paramInt2 - 16];
/*  999 */       byte[] arrayOfByte3 = new byte[2];
/* 1000 */       byte[] arrayOfByte4 = new byte[4];
/*      */ 
/* 1003 */       System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte2, 0, arrayOfByte2.length);
/* 1004 */       System.arraycopy(paramArrayOfByte, paramInt1 + arrayOfByte2.length, arrayOfByte1, 0, 10);
/* 1005 */       System.arraycopy(paramArrayOfByte, paramInt1 + arrayOfByte2.length + 10, arrayOfByte3, 0, 2);
/* 1006 */       System.arraycopy(paramArrayOfByte, paramInt1 + arrayOfByte2.length + 12, arrayOfByte4, 0, 4);
/*      */ 
/* 1009 */       byte[] arrayOfByte5 = getHMAC(this.peerKi, arrayOfByte4, arrayOfByte2, 0, arrayOfByte2.length);
/*      */ 
/* 1011 */       if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
/* 1012 */         DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "unwrap", "DIGEST18:incoming: ", arrayOfByte2);
/*      */ 
/* 1014 */         DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "unwrap", "DIGEST19:MAC: ", arrayOfByte1);
/*      */ 
/* 1016 */         DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "unwrap", "DIGEST20:messageType: ", arrayOfByte3);
/*      */ 
/* 1018 */         DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "unwrap", "DIGEST21:sequenceNum: ", arrayOfByte4);
/*      */ 
/* 1020 */         DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "unwrap", "DIGEST22:expectedMAC: ", arrayOfByte5);
/*      */       }
/*      */ 
/* 1025 */       if (!Arrays.equals(arrayOfByte1, arrayOfByte5))
/*      */       {
/* 1027 */         DigestMD5Base.logger.log(Level.INFO, "DIGEST23:Unmatched MACs");
/* 1028 */         return DigestMD5Base.EMPTY_BYTE_ARRAY;
/*      */       }
/*      */ 
/* 1032 */       if (this.peerSeqNum != DigestMD5Base.networkByteOrderToInt(arrayOfByte4, 0, 4)) {
/* 1033 */         throw new SaslException("DIGEST-MD5: Out of order sequencing of messages from server. Got: " + DigestMD5Base.networkByteOrderToInt(arrayOfByte4, 0, 4) + " Expected: " + this.peerSeqNum);
/*      */       }
/*      */ 
/* 1039 */       if (!Arrays.equals(this.messageType, arrayOfByte3)) {
/* 1040 */         throw new SaslException("DIGEST-MD5: invalid message type: " + DigestMD5Base.networkByteOrderToInt(arrayOfByte3, 0, 2));
/*      */       }
/*      */ 
/* 1045 */       this.peerSeqNum += 1;
/* 1046 */       return arrayOfByte2;
/*      */     }
/*      */ 
/*      */     protected byte[] getHMAC(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt1, int paramInt2)
/*      */       throws SaslException
/*      */     {
/* 1064 */       byte[] arrayOfByte1 = new byte[4 + paramInt2];
/* 1065 */       System.arraycopy(paramArrayOfByte2, 0, arrayOfByte1, 0, 4);
/* 1066 */       System.arraycopy(paramArrayOfByte3, paramInt1, arrayOfByte1, 4, paramInt2);
/*      */       try
/*      */       {
/* 1069 */         SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramArrayOfByte1, "HmacMD5");
/* 1070 */         Mac localMac = Mac.getInstance("HmacMD5");
/* 1071 */         localMac.init(localSecretKeySpec);
/* 1072 */         localMac.update(arrayOfByte1);
/* 1073 */         byte[] arrayOfByte2 = localMac.doFinal();
/*      */ 
/* 1076 */         byte[] arrayOfByte3 = new byte[10];
/* 1077 */         System.arraycopy(arrayOfByte2, 0, arrayOfByte3, 0, 10);
/*      */ 
/* 1079 */         return arrayOfByte3;
/*      */       } catch (InvalidKeyException localInvalidKeyException) {
/* 1081 */         throw new SaslException("DIGEST-MD5: Invalid bytes used for key of HMAC-MD5 hash.", localInvalidKeyException);
/*      */       }
/*      */       catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 1084 */         throw new SaslException("DIGEST-MD5: Error creating instance of MD5 digest algorithm", localNoSuchAlgorithmException);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void incrementSeqNum()
/*      */     {
/* 1093 */       DigestMD5Base.intToNetworkByteOrder(this.mySeqNum++, this.sequenceNum, 0, 4);
/*      */     }
/*      */   }
/*      */ 
/*      */   final class DigestPrivacy extends DigestMD5Base.DigestIntegrity
/*      */     implements SecurityCtx
/*      */   {
/*      */     private static final String CLIENT_CONF_MAGIC = "Digest H(A1) to client-to-server sealing key magic constant";
/*      */     private static final String SVR_CONF_MAGIC = "Digest H(A1) to server-to-client sealing key magic constant";
/*      */     private Cipher encCipher;
/*      */     private Cipher decCipher;
/*      */ 
/*      */     DigestPrivacy(boolean arg2)
/*      */       throws SaslException
/*      */     {
/* 1131 */       super(bool);
/*      */       try
/*      */       {
/* 1134 */         generatePrivacyKeyPair(bool);
/*      */       }
/*      */       catch (SaslException localSaslException) {
/* 1137 */         throw localSaslException;
/*      */       }
/*      */       catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 1140 */         throw new SaslException("DIGEST-MD5: Error encoding string value into UTF-8", localUnsupportedEncodingException);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 1144 */         throw new SaslException("DIGEST-MD5: Error accessing buffers required to generate cipher keys", localIOException);
/*      */       }
/*      */       catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 1147 */         throw new SaslException("DIGEST-MD5: Error creating instance of required cipher or digest", localNoSuchAlgorithmException);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void generatePrivacyKeyPair(boolean paramBoolean)
/*      */       throws IOException, UnsupportedEncodingException, NoSuchAlgorithmException, SaslException
/*      */     {
/* 1169 */       byte[] arrayOfByte1 = "Digest H(A1) to client-to-server sealing key magic constant".getBytes(DigestMD5Base.this.encoding);
/* 1170 */       byte[] arrayOfByte2 = "Digest H(A1) to server-to-client sealing key magic constant".getBytes(DigestMD5Base.this.encoding);
/*      */ 
/* 1173 */       MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
/*      */       int i;
/* 1176 */       if (DigestMD5Base.this.negotiatedCipher.equals(DigestMD5Base.CIPHER_TOKENS[4]))
/* 1177 */         i = 5;
/* 1178 */       else if (DigestMD5Base.this.negotiatedCipher.equals(DigestMD5Base.CIPHER_TOKENS[3]))
/* 1179 */         i = 7;
/*      */       else {
/* 1181 */         i = 16;
/*      */       }
/*      */ 
/* 1186 */       byte[] arrayOfByte3 = new byte[i + arrayOfByte1.length];
/* 1187 */       System.arraycopy(DigestMD5Base.this.H_A1, 0, arrayOfByte3, 0, i);
/*      */ 
/* 1190 */       System.arraycopy(arrayOfByte1, 0, arrayOfByte3, i, arrayOfByte1.length);
/* 1191 */       localMessageDigest.update(arrayOfByte3);
/* 1192 */       byte[] arrayOfByte4 = localMessageDigest.digest();
/*      */ 
/* 1196 */       System.arraycopy(arrayOfByte2, 0, arrayOfByte3, i, arrayOfByte2.length);
/* 1197 */       localMessageDigest.update(arrayOfByte3);
/* 1198 */       byte[] arrayOfByte5 = localMessageDigest.digest();
/*      */ 
/* 1200 */       if (DigestMD5Base.logger.isLoggable(Level.FINER)) {
/* 1201 */         DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST24:Kcc: ", arrayOfByte4);
/*      */ 
/* 1203 */         DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST25:Kcs: ", arrayOfByte5);
/*      */       }
/*      */       byte[] arrayOfByte6;
/*      */       byte[] arrayOfByte7;
/* 1210 */       if (paramBoolean) {
/* 1211 */         arrayOfByte6 = arrayOfByte4;
/* 1212 */         arrayOfByte7 = arrayOfByte5;
/*      */       } else {
/* 1214 */         arrayOfByte6 = arrayOfByte5;
/* 1215 */         arrayOfByte7 = arrayOfByte4;
/*      */       }
/*      */       try
/*      */       {
/*      */         Object localObject1;
/*      */         Object localObject2;
/* 1223 */         if (DigestMD5Base.this.negotiatedCipher.indexOf(DigestMD5Base.CIPHER_TOKENS[1]) > -1) {
/* 1224 */           this.encCipher = Cipher.getInstance("RC4");
/* 1225 */           this.decCipher = Cipher.getInstance("RC4");
/*      */ 
/* 1227 */           localObject1 = new SecretKeySpec(arrayOfByte6, "RC4");
/* 1228 */           localObject2 = new SecretKeySpec(arrayOfByte7, "RC4");
/*      */ 
/* 1230 */           this.encCipher.init(1, (Key)localObject1);
/* 1231 */           this.decCipher.init(2, (Key)localObject2);
/*      */         }
/* 1233 */         else if ((DigestMD5Base.this.negotiatedCipher.equals(DigestMD5Base.CIPHER_TOKENS[2])) || (DigestMD5Base.this.negotiatedCipher.equals(DigestMD5Base.CIPHER_TOKENS[0])))
/*      */         {
/*      */           String str1;
/*      */           String str2;
/* 1242 */           if (DigestMD5Base.this.negotiatedCipher.equals(DigestMD5Base.CIPHER_TOKENS[2])) {
/* 1243 */             str1 = "DES/CBC/NoPadding";
/* 1244 */             str2 = "des";
/*      */           }
/*      */           else {
/* 1247 */             str1 = "DESede/CBC/NoPadding";
/* 1248 */             str2 = "desede";
/*      */           }
/*      */ 
/* 1251 */           this.encCipher = Cipher.getInstance(str1);
/* 1252 */           this.decCipher = Cipher.getInstance(str1);
/*      */ 
/* 1254 */           localObject1 = DigestMD5Base.makeDesKeys(arrayOfByte6, str2);
/* 1255 */           localObject2 = DigestMD5Base.makeDesKeys(arrayOfByte7, str2);
/*      */ 
/* 1258 */           IvParameterSpec localIvParameterSpec1 = new IvParameterSpec(arrayOfByte6, 8, 8);
/* 1259 */           IvParameterSpec localIvParameterSpec2 = new IvParameterSpec(arrayOfByte7, 8, 8);
/*      */ 
/* 1262 */           this.encCipher.init(1, (Key)localObject1, localIvParameterSpec1);
/* 1263 */           this.decCipher.init(2, (Key)localObject2, localIvParameterSpec2);
/*      */ 
/* 1265 */           if (DigestMD5Base.logger.isLoggable(Level.FINER)) {
/* 1266 */             DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST26:" + DigestMD5Base.this.negotiatedCipher + " IVcc: ", localIvParameterSpec1.getIV());
/*      */ 
/* 1269 */             DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST27:" + DigestMD5Base.this.negotiatedCipher + " IVcs: ", localIvParameterSpec2.getIV());
/*      */ 
/* 1272 */             DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST28:" + DigestMD5Base.this.negotiatedCipher + " encryption key: ", ((SecretKey)localObject1).getEncoded());
/*      */ 
/* 1275 */             DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST29:" + DigestMD5Base.this.negotiatedCipher + " decryption key: ", ((SecretKey)localObject2).getEncoded());
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (InvalidKeySpecException localInvalidKeySpecException)
/*      */       {
/* 1281 */         throw new SaslException("DIGEST-MD5: Unsupported key specification used.", localInvalidKeySpecException);
/*      */       }
/*      */       catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException) {
/* 1284 */         throw new SaslException("DIGEST-MD5: Invalid cipher algorithem parameter used to create cipher instance", localInvalidAlgorithmParameterException);
/*      */       }
/*      */       catch (NoSuchPaddingException localNoSuchPaddingException) {
/* 1287 */         throw new SaslException("DIGEST-MD5: Unsupported padding used for chosen cipher", localNoSuchPaddingException);
/*      */       }
/*      */       catch (InvalidKeyException localInvalidKeyException) {
/* 1290 */         throw new SaslException("DIGEST-MD5: Invalid data used to initialize keys", localInvalidKeyException);
/*      */       }
/*      */     }
/*      */ 
/*      */     public byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */       throws SaslException
/*      */     {
/* 1313 */       if (paramInt2 == 0) {
/* 1314 */         return DigestMD5Base.EMPTY_BYTE_ARRAY;
/*      */       }
/*      */ 
/* 1318 */       incrementSeqNum();
/* 1319 */       byte[] arrayOfByte1 = getHMAC(this.myKi, this.sequenceNum, paramArrayOfByte, paramInt1, paramInt2);
/*      */ 
/* 1321 */       if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
/* 1322 */         DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "wrap", "DIGEST30:Outgoing: ", paramArrayOfByte, paramInt1, paramInt2);
/*      */ 
/* 1324 */         DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "wrap", "seqNum: ", this.sequenceNum);
/*      */ 
/* 1326 */         DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "wrap", "MAC: ", arrayOfByte1);
/*      */       }
/*      */ 
/* 1330 */       int i = this.encCipher.getBlockSize();
/*      */       byte[] arrayOfByte2;
/* 1332 */       if (i > 1) {
/* 1333 */         int j = i - (paramInt2 + 10) % i;
/* 1334 */         arrayOfByte2 = new byte[j];
/* 1335 */         for (int k = 0; k < j; k++)
/* 1336 */           arrayOfByte2[k] = ((byte)j);
/*      */       }
/*      */       else {
/* 1339 */         arrayOfByte2 = DigestMD5Base.EMPTY_BYTE_ARRAY;
/*      */       }
/*      */ 
/* 1342 */       byte[] arrayOfByte3 = new byte[paramInt2 + arrayOfByte2.length + 10];
/*      */ 
/* 1345 */       System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte3, 0, paramInt2);
/* 1346 */       System.arraycopy(arrayOfByte2, 0, arrayOfByte3, paramInt2, arrayOfByte2.length);
/* 1347 */       System.arraycopy(arrayOfByte1, 0, arrayOfByte3, paramInt2 + arrayOfByte2.length, 10);
/*      */ 
/* 1349 */       if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
/* 1350 */         DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "wrap", "DIGEST31:{msg, pad, KicMAC}: ", arrayOfByte3);
/*      */       }
/*      */ 
/*      */       byte[] arrayOfByte4;
/*      */       try
/*      */       {
/* 1358 */         arrayOfByte4 = this.encCipher.update(arrayOfByte3);
/*      */ 
/* 1360 */         if (arrayOfByte4 == null)
/*      */         {
/* 1362 */           throw new IllegalBlockSizeException("" + arrayOfByte3.length);
/*      */         }
/*      */       } catch (IllegalBlockSizeException localIllegalBlockSizeException) {
/* 1365 */         throw new SaslException("DIGEST-MD5: Invalid block size for cipher", localIllegalBlockSizeException);
/*      */       }
/*      */ 
/* 1369 */       byte[] arrayOfByte5 = new byte[arrayOfByte4.length + 2 + 4];
/* 1370 */       System.arraycopy(arrayOfByte4, 0, arrayOfByte5, 0, arrayOfByte4.length);
/* 1371 */       System.arraycopy(this.messageType, 0, arrayOfByte5, arrayOfByte4.length, 2);
/* 1372 */       System.arraycopy(this.sequenceNum, 0, arrayOfByte5, arrayOfByte4.length + 2, 4);
/*      */ 
/* 1374 */       if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
/* 1375 */         DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "wrap", "DIGEST32:Wrapped: ", arrayOfByte5);
/*      */       }
/*      */ 
/* 1378 */       return arrayOfByte5;
/*      */     }
/*      */ 
/*      */     public byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */       throws SaslException
/*      */     {
/* 1398 */       if (paramInt2 == 0) {
/* 1399 */         return DigestMD5Base.EMPTY_BYTE_ARRAY;
/*      */       }
/*      */ 
/* 1402 */       byte[] arrayOfByte1 = new byte[paramInt2 - 6];
/* 1403 */       byte[] arrayOfByte2 = new byte[2];
/* 1404 */       byte[] arrayOfByte3 = new byte[4];
/*      */ 
/* 1407 */       System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte1, 0, arrayOfByte1.length);
/*      */ 
/* 1409 */       System.arraycopy(paramArrayOfByte, paramInt1 + arrayOfByte1.length, arrayOfByte2, 0, 2);
/*      */ 
/* 1411 */       System.arraycopy(paramArrayOfByte, paramInt1 + arrayOfByte1.length + 2, arrayOfByte3, 0, 4);
/*      */ 
/* 1414 */       if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
/* 1415 */         DigestMD5Base.logger.log(Level.FINEST, "DIGEST33:Expecting sequence num: {0}", new Integer(this.peerSeqNum));
/*      */ 
/* 1418 */         DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "unwrap", "DIGEST34:incoming: ", arrayOfByte1);
/*      */       }
/*      */ 
/*      */       byte[] arrayOfByte4;
/*      */       try
/*      */       {
/* 1428 */         arrayOfByte4 = this.decCipher.update(arrayOfByte1);
/*      */ 
/* 1430 */         if (arrayOfByte4 == null)
/*      */         {
/* 1432 */           throw new IllegalBlockSizeException("" + arrayOfByte1.length);
/*      */         }
/*      */       } catch (IllegalBlockSizeException localIllegalBlockSizeException) {
/* 1435 */         throw new SaslException("DIGEST-MD5: Illegal block sizes used with chosen cipher", localIllegalBlockSizeException);
/*      */       }
/*      */ 
/* 1439 */       byte[] arrayOfByte5 = new byte[arrayOfByte4.length - 10];
/* 1440 */       byte[] arrayOfByte6 = new byte[10];
/*      */ 
/* 1442 */       System.arraycopy(arrayOfByte4, 0, arrayOfByte5, 0, arrayOfByte5.length);
/*      */ 
/* 1444 */       System.arraycopy(arrayOfByte4, arrayOfByte5.length, arrayOfByte6, 0, 10);
/*      */ 
/* 1447 */       if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
/* 1448 */         DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "unwrap", "DIGEST35:Unwrapped (w/padding): ", arrayOfByte5);
/*      */ 
/* 1450 */         DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "unwrap", "DIGEST36:MAC: ", arrayOfByte6);
/* 1451 */         DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "unwrap", "DIGEST37:messageType: ", arrayOfByte2);
/*      */ 
/* 1453 */         DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "unwrap", "DIGEST38:sequenceNum: ", arrayOfByte3);
/*      */       }
/*      */ 
/* 1457 */       int i = arrayOfByte5.length;
/* 1458 */       int j = this.decCipher.getBlockSize();
/* 1459 */       if (j > 1)
/*      */       {
/* 1461 */         i -= arrayOfByte5[(arrayOfByte5.length - 1)];
/* 1462 */         if (i < 0)
/*      */         {
/* 1464 */           if (DigestMD5Base.logger.isLoggable(Level.INFO)) {
/* 1465 */             DigestMD5Base.logger.log(Level.INFO, "DIGEST39:Incorrect padding: {0}", new Byte(arrayOfByte5[(arrayOfByte5.length - 1)]));
/*      */           }
/*      */ 
/* 1469 */           return DigestMD5Base.EMPTY_BYTE_ARRAY;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1474 */       byte[] arrayOfByte7 = getHMAC(this.peerKi, arrayOfByte3, arrayOfByte5, 0, i);
/*      */ 
/* 1477 */       if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
/* 1478 */         DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "unwrap", "DIGEST40:KisMAC: ", arrayOfByte7);
/*      */       }
/*      */ 
/* 1483 */       if (!Arrays.equals(arrayOfByte6, arrayOfByte7))
/*      */       {
/* 1485 */         DigestMD5Base.logger.log(Level.INFO, "DIGEST41:Unmatched MACs");
/* 1486 */         return DigestMD5Base.EMPTY_BYTE_ARRAY;
/*      */       }
/*      */ 
/* 1490 */       if (this.peerSeqNum != DigestMD5Base.networkByteOrderToInt(arrayOfByte3, 0, 4)) {
/* 1491 */         throw new SaslException("DIGEST-MD5: Out of order sequencing of messages from server. Got: " + DigestMD5Base.networkByteOrderToInt(arrayOfByte3, 0, 4) + " Expected: " + this.peerSeqNum);
/*      */       }
/*      */ 
/* 1498 */       if (!Arrays.equals(this.messageType, arrayOfByte2)) {
/* 1499 */         throw new SaslException("DIGEST-MD5: invalid message type: " + DigestMD5Base.networkByteOrderToInt(arrayOfByte2, 0, 2));
/*      */       }
/*      */ 
/* 1504 */       this.peerSeqNum += 1;
/*      */ 
/* 1506 */       if (i == arrayOfByte5.length) {
/* 1507 */         return arrayOfByte5;
/*      */       }
/*      */ 
/* 1510 */       byte[] arrayOfByte8 = new byte[i];
/* 1511 */       System.arraycopy(arrayOfByte5, 0, arrayOfByte8, 0, i);
/* 1512 */       return arrayOfByte8;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.digest.DigestMD5Base
 * JD-Core Version:    0.6.2
 */