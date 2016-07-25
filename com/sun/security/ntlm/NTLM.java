/*     */ package com.sun.security.ntlm;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.util.Arrays;
/*     */ import javax.crypto.BadPaddingException;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.IllegalBlockSizeException;
/*     */ import javax.crypto.Mac;
/*     */ import javax.crypto.NoSuchPaddingException;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.SecretKeyFactory;
/*     */ import javax.crypto.spec.DESKeySpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.provider.MD4;
/*     */ 
/*     */ class NTLM
/*     */ {
/*     */   private final SecretKeyFactory fac;
/*     */   private final Cipher cipher;
/*     */   private final MessageDigest md4;
/*     */   private final Mac hmac;
/*     */   private final MessageDigest md5;
/*  57 */   private static final boolean DEBUG = System.getProperty("ntlm.debug") != null;
/*     */   final Version v;
/*     */   final boolean writeLM;
/*     */   final boolean writeNTLM;
/*     */ 
/*     */   protected NTLM(String paramString)
/*     */     throws NTLMException
/*     */   {
/*  66 */     if (paramString == null) paramString = "LMv2/NTLMv2";
/*  67 */     switch (paramString) { case "LM":
/*  68 */       this.v = Version.NTLM; this.writeLM = true; this.writeNTLM = false; break;
/*     */     case "NTLM":
/*  69 */       this.v = Version.NTLM; this.writeLM = false; this.writeNTLM = true; break;
/*     */     case "LM/NTLM":
/*  70 */       this.v = Version.NTLM; this.writeLM = (this.writeNTLM = 1); break;
/*     */     case "NTLM2":
/*  71 */       this.v = Version.NTLM2; this.writeLM = (this.writeNTLM = 1); break;
/*     */     case "LMv2":
/*  72 */       this.v = Version.NTLMv2; this.writeLM = true; this.writeNTLM = false; break;
/*     */     case "NTLMv2":
/*  73 */       this.v = Version.NTLMv2; this.writeLM = false; this.writeNTLM = true; break;
/*     */     case "LMv2/NTLMv2":
/*  74 */       this.v = Version.NTLMv2; this.writeLM = (this.writeNTLM = 1); break;
/*     */     default:
/*  75 */       throw new NTLMException(5, "Unknown version " + paramString);
/*     */     }
/*     */     try
/*     */     {
/*  79 */       this.fac = SecretKeyFactory.getInstance("DES");
/*  80 */       this.cipher = Cipher.getInstance("DES/ECB/NoPadding");
/*  81 */       this.md4 = MD4.getInstance();
/*  82 */       this.hmac = Mac.getInstance("HmacMD5");
/*  83 */       this.md5 = MessageDigest.getInstance("MD5");
/*     */     } catch (NoSuchPaddingException localNoSuchPaddingException) {
/*  85 */       throw new AssertionError();
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/*  87 */       throw new AssertionError();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void debug(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 102 */     if (DEBUG)
/* 103 */       System.out.printf(paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public void debug(byte[] paramArrayOfByte)
/*     */   {
/* 116 */     if (DEBUG)
/*     */       try {
/* 118 */         new HexDumpEncoder().encodeBuffer(paramArrayOfByte, System.out);
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   byte[] makeDesKey(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 266 */     int[] arrayOfInt = new int[paramArrayOfByte.length];
/* 267 */     for (int i = 0; i < arrayOfInt.length; i++) {
/* 268 */       arrayOfInt[i] = (paramArrayOfByte[i] < 0 ? paramArrayOfByte[i] + 256 : paramArrayOfByte[i]);
/*     */     }
/* 270 */     byte[] arrayOfByte = new byte[8];
/* 271 */     arrayOfByte[0] = ((byte)arrayOfInt[(paramInt + 0)]);
/* 272 */     arrayOfByte[1] = ((byte)(arrayOfInt[(paramInt + 0)] << 7 & 0xFF | arrayOfInt[(paramInt + 1)] >> 1));
/* 273 */     arrayOfByte[2] = ((byte)(arrayOfInt[(paramInt + 1)] << 6 & 0xFF | arrayOfInt[(paramInt + 2)] >> 2));
/* 274 */     arrayOfByte[3] = ((byte)(arrayOfInt[(paramInt + 2)] << 5 & 0xFF | arrayOfInt[(paramInt + 3)] >> 3));
/* 275 */     arrayOfByte[4] = ((byte)(arrayOfInt[(paramInt + 3)] << 4 & 0xFF | arrayOfInt[(paramInt + 4)] >> 4));
/* 276 */     arrayOfByte[5] = ((byte)(arrayOfInt[(paramInt + 4)] << 3 & 0xFF | arrayOfInt[(paramInt + 5)] >> 5));
/* 277 */     arrayOfByte[6] = ((byte)(arrayOfInt[(paramInt + 5)] << 2 & 0xFF | arrayOfInt[(paramInt + 6)] >> 6));
/* 278 */     arrayOfByte[7] = ((byte)(arrayOfInt[(paramInt + 6)] << 1 & 0xFF));
/* 279 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   byte[] calcLMHash(byte[] paramArrayOfByte) {
/* 283 */     byte[] arrayOfByte1 = { 75, 71, 83, 33, 64, 35, 36, 37 };
/* 284 */     byte[] arrayOfByte2 = new byte[14];
/* 285 */     int i = paramArrayOfByte.length;
/* 286 */     if (i > 14)
/* 287 */       i = 14;
/* 288 */     System.arraycopy(paramArrayOfByte, 0, arrayOfByte2, 0, i);
/*     */     try
/*     */     {
/* 291 */       DESKeySpec localDESKeySpec1 = new DESKeySpec(makeDesKey(arrayOfByte2, 0));
/* 292 */       DESKeySpec localDESKeySpec2 = new DESKeySpec(makeDesKey(arrayOfByte2, 7));
/*     */ 
/* 294 */       SecretKey localSecretKey1 = this.fac.generateSecret(localDESKeySpec1);
/* 295 */       SecretKey localSecretKey2 = this.fac.generateSecret(localDESKeySpec2);
/* 296 */       this.cipher.init(1, localSecretKey1);
/* 297 */       byte[] arrayOfByte3 = this.cipher.doFinal(arrayOfByte1, 0, 8);
/* 298 */       this.cipher.init(1, localSecretKey2);
/* 299 */       byte[] arrayOfByte4 = this.cipher.doFinal(arrayOfByte1, 0, 8);
/* 300 */       byte[] arrayOfByte5 = new byte[21];
/* 301 */       System.arraycopy(arrayOfByte3, 0, arrayOfByte5, 0, 8);
/* 302 */       System.arraycopy(arrayOfByte4, 0, arrayOfByte5, 8, 8);
/* 303 */       return arrayOfByte5;
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException) {
/* 306 */       if (!$assertionsDisabled) throw new AssertionError(); 
/*     */     }
/*     */     catch (InvalidKeySpecException localInvalidKeySpecException)
/*     */     {
/* 309 */       if (!$assertionsDisabled) throw new AssertionError(); 
/*     */     }
/*     */     catch (IllegalBlockSizeException localIllegalBlockSizeException)
/*     */     {
/* 312 */       if (!$assertionsDisabled) throw new AssertionError(); 
/*     */     }
/*     */     catch (BadPaddingException localBadPaddingException)
/*     */     {
/* 315 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */     }
/* 317 */     return null;
/*     */   }
/*     */ 
/*     */   byte[] calcNTHash(byte[] paramArrayOfByte) {
/* 321 */     byte[] arrayOfByte1 = this.md4.digest(paramArrayOfByte);
/* 322 */     byte[] arrayOfByte2 = new byte[21];
/* 323 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, 16);
/* 324 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   byte[] calcResponse(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/*     */     try
/*     */     {
/* 333 */       assert (paramArrayOfByte1.length == 21);
/* 334 */       DESKeySpec localDESKeySpec1 = new DESKeySpec(makeDesKey(paramArrayOfByte1, 0));
/* 335 */       DESKeySpec localDESKeySpec2 = new DESKeySpec(makeDesKey(paramArrayOfByte1, 7));
/* 336 */       DESKeySpec localDESKeySpec3 = new DESKeySpec(makeDesKey(paramArrayOfByte1, 14));
/* 337 */       SecretKey localSecretKey1 = this.fac.generateSecret(localDESKeySpec1);
/* 338 */       SecretKey localSecretKey2 = this.fac.generateSecret(localDESKeySpec2);
/* 339 */       SecretKey localSecretKey3 = this.fac.generateSecret(localDESKeySpec3);
/* 340 */       this.cipher.init(1, localSecretKey1);
/* 341 */       byte[] arrayOfByte1 = this.cipher.doFinal(paramArrayOfByte2, 0, 8);
/* 342 */       this.cipher.init(1, localSecretKey2);
/* 343 */       byte[] arrayOfByte2 = this.cipher.doFinal(paramArrayOfByte2, 0, 8);
/* 344 */       this.cipher.init(1, localSecretKey3);
/* 345 */       byte[] arrayOfByte3 = this.cipher.doFinal(paramArrayOfByte2, 0, 8);
/* 346 */       byte[] arrayOfByte4 = new byte[24];
/* 347 */       System.arraycopy(arrayOfByte1, 0, arrayOfByte4, 0, 8);
/* 348 */       System.arraycopy(arrayOfByte2, 0, arrayOfByte4, 8, 8);
/* 349 */       System.arraycopy(arrayOfByte3, 0, arrayOfByte4, 16, 8);
/* 350 */       return arrayOfByte4;
/*     */     } catch (IllegalBlockSizeException localIllegalBlockSizeException) {
/* 352 */       if (!$assertionsDisabled) throw new AssertionError(); 
/*     */     }
/* 354 */     catch (BadPaddingException localBadPaddingException) { if (!$assertionsDisabled) throw new AssertionError();  } catch (InvalidKeySpecException localInvalidKeySpecException)
/*     */     {
/* 356 */       if (!$assertionsDisabled) throw new AssertionError(); 
/*     */     }
/* 358 */     catch (InvalidKeyException localInvalidKeyException) { if (!$assertionsDisabled) throw new AssertionError();
/*     */     }
/* 360 */     return null;
/*     */   }
/*     */ 
/*     */   byte[] hmacMD5(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/*     */     try
/*     */     {
/* 367 */       SecretKeySpec localSecretKeySpec = new SecretKeySpec(Arrays.copyOf(paramArrayOfByte1, 16), "HmacMD5");
/*     */ 
/* 369 */       this.hmac.init(localSecretKeySpec);
/* 370 */       return this.hmac.doFinal(paramArrayOfByte2);
/*     */     } catch (InvalidKeyException localInvalidKeyException) {
/* 372 */       if (!$assertionsDisabled) throw new AssertionError(); 
/*     */     }
/* 374 */     catch (RuntimeException localRuntimeException) { if (!$assertionsDisabled) throw new AssertionError();
/*     */     }
/* 376 */     return null;
/*     */   }
/*     */ 
/*     */   byte[] calcV2(byte[] paramArrayOfByte1, String paramString, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
/*     */     try {
/* 381 */       byte[] arrayOfByte1 = hmacMD5(paramArrayOfByte1, paramString.getBytes("UnicodeLittleUnmarked"));
/*     */ 
/* 383 */       byte[] arrayOfByte2 = new byte[paramArrayOfByte2.length + 8];
/* 384 */       System.arraycopy(paramArrayOfByte3, 0, arrayOfByte2, 0, 8);
/* 385 */       System.arraycopy(paramArrayOfByte2, 0, arrayOfByte2, 8, paramArrayOfByte2.length);
/* 386 */       byte[] arrayOfByte3 = new byte[16 + paramArrayOfByte2.length];
/* 387 */       System.arraycopy(hmacMD5(arrayOfByte1, arrayOfByte2), 0, arrayOfByte3, 0, 16);
/* 388 */       System.arraycopy(paramArrayOfByte2, 0, arrayOfByte3, 16, paramArrayOfByte2.length);
/* 389 */       return arrayOfByte3;
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 391 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */     }
/* 393 */     return null;
/*     */   }
/*     */ 
/*     */   static byte[] ntlm2LM(byte[] paramArrayOfByte)
/*     */   {
/* 399 */     return Arrays.copyOf(paramArrayOfByte, 24);
/*     */   }
/*     */ 
/*     */   byte[] ntlm2NTLM(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
/* 403 */     byte[] arrayOfByte1 = Arrays.copyOf(paramArrayOfByte3, 16);
/* 404 */     System.arraycopy(paramArrayOfByte2, 0, arrayOfByte1, 8, 8);
/* 405 */     byte[] arrayOfByte2 = Arrays.copyOf(this.md5.digest(arrayOfByte1), 8);
/* 406 */     return calcResponse(paramArrayOfByte1, arrayOfByte2);
/*     */   }
/*     */ 
/*     */   static byte[] getP1(char[] paramArrayOfChar)
/*     */   {
/*     */     try
/*     */     {
/* 413 */       return new String(paramArrayOfChar).toUpperCase().getBytes("ISO8859_1"); } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */     }
/* 415 */     return null;
/*     */   }
/*     */ 
/*     */   static byte[] getP2(char[] paramArrayOfChar)
/*     */   {
/*     */     try {
/* 421 */       return new String(paramArrayOfChar).getBytes("UnicodeLittleUnmarked"); } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */     }
/* 423 */     return null;
/*     */   }
/*     */ 
/*     */   static class Reader
/*     */   {
/*     */     private final byte[] internal;
/*     */ 
/*     */     Reader(byte[] paramArrayOfByte)
/*     */     {
/* 133 */       this.internal = paramArrayOfByte;
/*     */     }
/*     */ 
/*     */     int readInt(int paramInt) throws NTLMException {
/*     */       try {
/* 138 */         return (this.internal[paramInt] & 0xFF) + ((this.internal[(paramInt + 1)] & 0xFF) << 8) + ((this.internal[(paramInt + 2)] & 0xFF) << 16) + ((this.internal[(paramInt + 3)] & 0xFF) << 24);
/*     */       }
/*     */       catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*     */       {
/*     */       }
/* 143 */       throw new NTLMException(1, "Input message incorrect size");
/*     */     }
/*     */ 
/*     */     int readShort(int paramInt) throws NTLMException
/*     */     {
/*     */       try
/*     */       {
/* 150 */         return (this.internal[paramInt] & 0xFF) + (this.internal[(paramInt + 1)] & 0xFF00);
/*     */       } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/*     */       }
/* 153 */       throw new NTLMException(1, "Input message incorrect size");
/*     */     }
/*     */ 
/*     */     byte[] readBytes(int paramInt1, int paramInt2) throws NTLMException
/*     */     {
/*     */       try
/*     */       {
/* 160 */         return Arrays.copyOfRange(this.internal, paramInt1, paramInt1 + paramInt2); } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/*     */       }
/* 162 */       throw new NTLMException(1, "Input message incorrect size");
/*     */     }
/*     */ 
/*     */     byte[] readSecurityBuffer(int paramInt)
/*     */       throws NTLMException
/*     */     {
/* 168 */       int i = readInt(paramInt + 4);
/* 169 */       if (i == 0) return null; try
/*     */       {
/* 171 */         return Arrays.copyOfRange(this.internal, i, i + readShort(paramInt));
/*     */       } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/*     */       }
/* 174 */       throw new NTLMException(1, "Input message incorrect size");
/*     */     }
/*     */ 
/*     */     String readSecurityBuffer(int paramInt, boolean paramBoolean)
/*     */       throws NTLMException
/*     */     {
/* 181 */       byte[] arrayOfByte = readSecurityBuffer(paramInt);
/*     */       try {
/* 183 */         return arrayOfByte == null ? null : new String(arrayOfByte, paramBoolean ? "UnicodeLittleUnmarked" : "ISO8859_1");
/*     */       } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */       }
/* 186 */       throw new NTLMException(1, "Invalid input encoding");
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Writer
/*     */   {
/*     */     private byte[] internal;
/*     */     private int current;
/*     */ 
/*     */     Writer(int paramInt1, int paramInt2)
/*     */     {
/* 206 */       assert (paramInt2 < 256);
/* 207 */       this.internal = new byte[256];
/* 208 */       this.current = paramInt2;
/* 209 */       System.arraycopy(new byte[] { 78, 84, 76, 77, 83, 83, 80, 0, (byte)paramInt1 }, 0, this.internal, 0, 9);
/*     */     }
/*     */ 
/*     */     void writeShort(int paramInt1, int paramInt2)
/*     */     {
/* 215 */       this.internal[paramInt1] = ((byte)paramInt2);
/* 216 */       this.internal[(paramInt1 + 1)] = ((byte)(paramInt2 >> 8));
/*     */     }
/*     */ 
/*     */     void writeInt(int paramInt1, int paramInt2) {
/* 220 */       this.internal[paramInt1] = ((byte)paramInt2);
/* 221 */       this.internal[(paramInt1 + 1)] = ((byte)(paramInt2 >> 8));
/* 222 */       this.internal[(paramInt1 + 2)] = ((byte)(paramInt2 >> 16));
/* 223 */       this.internal[(paramInt1 + 3)] = ((byte)(paramInt2 >> 24));
/*     */     }
/*     */ 
/*     */     void writeBytes(int paramInt, byte[] paramArrayOfByte) {
/* 227 */       System.arraycopy(paramArrayOfByte, 0, this.internal, paramInt, paramArrayOfByte.length);
/*     */     }
/*     */ 
/*     */     void writeSecurityBuffer(int paramInt, byte[] paramArrayOfByte) {
/* 231 */       if (paramArrayOfByte == null) {
/* 232 */         writeShort(paramInt + 4, this.current);
/*     */       } else {
/* 234 */         int i = paramArrayOfByte.length;
/* 235 */         if (this.current + i > this.internal.length) {
/* 236 */           this.internal = Arrays.copyOf(this.internal, this.current + i + 256);
/*     */         }
/* 238 */         writeShort(paramInt, i);
/* 239 */         writeShort(paramInt + 2, i);
/* 240 */         writeShort(paramInt + 4, this.current);
/* 241 */         System.arraycopy(paramArrayOfByte, 0, this.internal, this.current, i);
/* 242 */         this.current += i;
/*     */       }
/*     */     }
/*     */ 
/*     */     void writeSecurityBuffer(int paramInt, String paramString, boolean paramBoolean) {
/*     */       try {
/* 248 */         writeSecurityBuffer(paramInt, paramString == null ? null : paramString.getBytes(paramBoolean ? "UnicodeLittleUnmarked" : "ISO8859_1"));
/*     */       }
/*     */       catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 251 */         if (!$assertionsDisabled) throw new AssertionError(); 
/*     */       }
/*     */     }
/*     */ 
/*     */     byte[] getBytes()
/*     */     {
/* 256 */       return Arrays.copyOf(this.internal, this.current);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.ntlm.NTLM
 * JD-Core Version:    0.6.2
 */