/*     */ package java.lang;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.Arrays;
/*     */ import sun.misc.MessageUtils;
/*     */ import sun.nio.cs.ArrayDecoder;
/*     */ import sun.nio.cs.ArrayEncoder;
/*     */ import sun.nio.cs.HistoricallyNamedCharset;
/*     */ 
/*     */ class StringCoding
/*     */ {
/*  55 */   private static final ThreadLocal<SoftReference<StringDecoder>> decoder = new ThreadLocal();
/*     */ 
/*  57 */   private static final ThreadLocal<SoftReference<StringEncoder>> encoder = new ThreadLocal();
/*     */ 
/*  60 */   private static boolean warnUnsupportedCharset = true;
/*     */ 
/*     */   private static <T> T deref(ThreadLocal<SoftReference<T>> paramThreadLocal) {
/*  63 */     SoftReference localSoftReference = (SoftReference)paramThreadLocal.get();
/*  64 */     if (localSoftReference == null)
/*  65 */       return null;
/*  66 */     return localSoftReference.get();
/*     */   }
/*     */ 
/*     */   private static <T> void set(ThreadLocal<SoftReference<T>> paramThreadLocal, T paramT) {
/*  70 */     paramThreadLocal.set(new SoftReference(paramT));
/*     */   }
/*     */ 
/*     */   private static byte[] safeTrim(byte[] paramArrayOfByte, int paramInt, Charset paramCharset, boolean paramBoolean)
/*     */   {
/*  76 */     if ((paramInt == paramArrayOfByte.length) && ((paramBoolean) || (System.getSecurityManager() == null))) {
/*  77 */       return paramArrayOfByte;
/*     */     }
/*  79 */     return Arrays.copyOf(paramArrayOfByte, paramInt);
/*     */   }
/*     */ 
/*     */   private static char[] safeTrim(char[] paramArrayOfChar, int paramInt, Charset paramCharset, boolean paramBoolean)
/*     */   {
/*  86 */     if ((paramInt == paramArrayOfChar.length) && ((paramBoolean) || (System.getSecurityManager() == null))) {
/*  87 */       return paramArrayOfChar;
/*     */     }
/*  89 */     return Arrays.copyOf(paramArrayOfChar, paramInt);
/*     */   }
/*     */ 
/*     */   private static int scale(int paramInt, float paramFloat)
/*     */   {
/*  95 */     return (int)(paramInt * paramFloat);
/*     */   }
/*     */ 
/*     */   private static Charset lookupCharset(String paramString) {
/*  99 */     if (Charset.isSupported(paramString)) {
/*     */       try {
/* 101 */         return Charset.forName(paramString);
/*     */       } catch (UnsupportedCharsetException localUnsupportedCharsetException) {
/* 103 */         throw new Error(localUnsupportedCharsetException);
/*     */       }
/*     */     }
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   private static void warnUnsupportedCharset(String paramString) {
/* 110 */     if (warnUnsupportedCharset)
/*     */     {
/* 114 */       MessageUtils.err("WARNING: Default charset " + paramString + " not supported, using ISO-8859-1 instead");
/*     */ 
/* 116 */       warnUnsupportedCharset = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   static char[] decode(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 179 */     StringDecoder localStringDecoder = (StringDecoder)deref(decoder);
/* 180 */     String str = paramString == null ? "ISO-8859-1" : paramString;
/* 181 */     if ((localStringDecoder == null) || ((!str.equals(localStringDecoder.requestedCharsetName())) && (!str.equals(localStringDecoder.charsetName()))))
/*     */     {
/* 183 */       localStringDecoder = null;
/*     */       try {
/* 185 */         Charset localCharset = lookupCharset(str);
/* 186 */         if (localCharset != null)
/* 187 */           localStringDecoder = new StringDecoder(localCharset, str, null); 
/*     */       } catch (IllegalCharsetNameException localIllegalCharsetNameException) {  }
/*     */ 
/* 189 */       if (localStringDecoder == null)
/* 190 */         throw new UnsupportedEncodingException(str);
/* 191 */       set(decoder, localStringDecoder);
/*     */     }
/* 193 */     return localStringDecoder.decode(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   static char[] decode(Charset paramCharset, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 213 */     CharsetDecoder localCharsetDecoder = paramCharset.newDecoder();
/* 214 */     int i = scale(paramInt2, localCharsetDecoder.maxCharsPerByte());
/* 215 */     char[] arrayOfChar = new char[i];
/* 216 */     if (paramInt2 == 0)
/* 217 */       return arrayOfChar;
/* 218 */     boolean bool = false;
/* 219 */     if (System.getSecurityManager() != null) {
/* 220 */       if ((bool = paramCharset.getClass().getClassLoader0() == null ? 1 : 0) == 0) {
/* 221 */         paramArrayOfByte = Arrays.copyOfRange(paramArrayOfByte, paramInt1, paramInt1 + paramInt2);
/* 222 */         paramInt1 = 0;
/*     */       }
/*     */     }
/* 225 */     localCharsetDecoder.onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).reset();
/*     */ 
/* 228 */     if ((localCharsetDecoder instanceof ArrayDecoder)) {
/* 229 */       int j = ((ArrayDecoder)localCharsetDecoder).decode(paramArrayOfByte, paramInt1, paramInt2, arrayOfChar);
/* 230 */       return safeTrim(arrayOfChar, j, paramCharset, bool);
/*     */     }
/* 232 */     ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte, paramInt1, paramInt2);
/* 233 */     CharBuffer localCharBuffer = CharBuffer.wrap(arrayOfChar);
/*     */     try {
/* 235 */       CoderResult localCoderResult = localCharsetDecoder.decode(localByteBuffer, localCharBuffer, true);
/* 236 */       if (!localCoderResult.isUnderflow())
/* 237 */         localCoderResult.throwException();
/* 238 */       localCoderResult = localCharsetDecoder.flush(localCharBuffer);
/* 239 */       if (!localCoderResult.isUnderflow())
/* 240 */         localCoderResult.throwException();
/*     */     }
/*     */     catch (CharacterCodingException localCharacterCodingException)
/*     */     {
/* 244 */       throw new Error(localCharacterCodingException);
/*     */     }
/* 246 */     return safeTrim(arrayOfChar, localCharBuffer.position(), paramCharset, bool);
/*     */   }
/*     */ 
/*     */   static char[] decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 251 */     String str = Charset.defaultCharset().name();
/*     */     try
/*     */     {
/* 254 */       return decode(str, paramArrayOfByte, paramInt1, paramInt2);
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
/* 256 */       warnUnsupportedCharset(str);
/*     */       try
/*     */       {
/* 259 */         return decode("ISO-8859-1", paramArrayOfByte, paramInt1, paramInt2);
/*     */       }
/*     */       catch (UnsupportedEncodingException localUnsupportedEncodingException2)
/*     */       {
/* 263 */         MessageUtils.err("ISO-8859-1 charset not available: " + localUnsupportedEncodingException2.toString());
/*     */ 
/* 267 */         System.exit(1); } 
/* 268 */     }return null;
/*     */   }
/*     */ 
/*     */   static byte[] encode(String paramString, char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 330 */     StringEncoder localStringEncoder = (StringEncoder)deref(encoder);
/* 331 */     String str = paramString == null ? "ISO-8859-1" : paramString;
/* 332 */     if ((localStringEncoder == null) || ((!str.equals(localStringEncoder.requestedCharsetName())) && (!str.equals(localStringEncoder.charsetName()))))
/*     */     {
/* 334 */       localStringEncoder = null;
/*     */       try {
/* 336 */         Charset localCharset = lookupCharset(str);
/* 337 */         if (localCharset != null)
/* 338 */           localStringEncoder = new StringEncoder(localCharset, str, null); 
/*     */       } catch (IllegalCharsetNameException localIllegalCharsetNameException) {  }
/*     */ 
/* 340 */       if (localStringEncoder == null)
/* 341 */         throw new UnsupportedEncodingException(str);
/* 342 */       set(encoder, localStringEncoder);
/*     */     }
/* 344 */     return localStringEncoder.encode(paramArrayOfChar, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   static byte[] encode(Charset paramCharset, char[] paramArrayOfChar, int paramInt1, int paramInt2) {
/* 348 */     CharsetEncoder localCharsetEncoder = paramCharset.newEncoder();
/* 349 */     int i = scale(paramInt2, localCharsetEncoder.maxBytesPerChar());
/* 350 */     byte[] arrayOfByte = new byte[i];
/* 351 */     if (paramInt2 == 0)
/* 352 */       return arrayOfByte;
/* 353 */     boolean bool = false;
/* 354 */     if (System.getSecurityManager() != null) {
/* 355 */       if ((bool = paramCharset.getClass().getClassLoader0() == null ? 1 : 0) == 0) {
/* 356 */         paramArrayOfChar = Arrays.copyOfRange(paramArrayOfChar, paramInt1, paramInt1 + paramInt2);
/* 357 */         paramInt1 = 0;
/*     */       }
/*     */     }
/* 360 */     localCharsetEncoder.onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).reset();
/*     */ 
/* 363 */     if ((localCharsetEncoder instanceof ArrayEncoder)) {
/* 364 */       int j = ((ArrayEncoder)localCharsetEncoder).encode(paramArrayOfChar, paramInt1, paramInt2, arrayOfByte);
/* 365 */       return safeTrim(arrayOfByte, j, paramCharset, bool);
/*     */     }
/* 367 */     ByteBuffer localByteBuffer = ByteBuffer.wrap(arrayOfByte);
/* 368 */     CharBuffer localCharBuffer = CharBuffer.wrap(paramArrayOfChar, paramInt1, paramInt2);
/*     */     try {
/* 370 */       CoderResult localCoderResult = localCharsetEncoder.encode(localCharBuffer, localByteBuffer, true);
/* 371 */       if (!localCoderResult.isUnderflow())
/* 372 */         localCoderResult.throwException();
/* 373 */       localCoderResult = localCharsetEncoder.flush(localByteBuffer);
/* 374 */       if (!localCoderResult.isUnderflow())
/* 375 */         localCoderResult.throwException();
/*     */     } catch (CharacterCodingException localCharacterCodingException) {
/* 377 */       throw new Error(localCharacterCodingException);
/*     */     }
/* 379 */     return safeTrim(arrayOfByte, localByteBuffer.position(), paramCharset, bool);
/*     */   }
/*     */ 
/*     */   static byte[] encode(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 384 */     String str = Charset.defaultCharset().name();
/*     */     try
/*     */     {
/* 387 */       return encode(str, paramArrayOfChar, paramInt1, paramInt2);
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
/* 389 */       warnUnsupportedCharset(str);
/*     */       try
/*     */       {
/* 392 */         return encode("ISO-8859-1", paramArrayOfChar, paramInt1, paramInt2);
/*     */       }
/*     */       catch (UnsupportedEncodingException localUnsupportedEncodingException2)
/*     */       {
/* 396 */         MessageUtils.err("ISO-8859-1 charset not available: " + localUnsupportedEncodingException2.toString());
/*     */ 
/* 400 */         System.exit(1); } 
/* 401 */     }return null;
/*     */   }
/*     */ 
/*     */   private static class StringDecoder
/*     */   {
/*     */     private final String requestedCharsetName;
/*     */     private final Charset cs;
/*     */     private final CharsetDecoder cd;
/*     */     private final boolean isTrusted;
/*     */ 
/*     */     private StringDecoder(Charset paramCharset, String paramString)
/*     */     {
/* 129 */       this.requestedCharsetName = paramString;
/* 130 */       this.cs = paramCharset;
/* 131 */       this.cd = paramCharset.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
/*     */ 
/* 134 */       this.isTrusted = (paramCharset.getClass().getClassLoader0() == null);
/*     */     }
/*     */ 
/*     */     String charsetName() {
/* 138 */       if ((this.cs instanceof HistoricallyNamedCharset))
/* 139 */         return ((HistoricallyNamedCharset)this.cs).historicalName();
/* 140 */       return this.cs.name();
/*     */     }
/*     */ 
/*     */     final String requestedCharsetName() {
/* 144 */       return this.requestedCharsetName;
/*     */     }
/*     */ 
/*     */     char[] decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 148 */       int i = StringCoding.scale(paramInt2, this.cd.maxCharsPerByte());
/* 149 */       char[] arrayOfChar = new char[i];
/* 150 */       if (paramInt2 == 0)
/* 151 */         return arrayOfChar;
/* 152 */       if ((this.cd instanceof ArrayDecoder)) {
/* 153 */         int j = ((ArrayDecoder)this.cd).decode(paramArrayOfByte, paramInt1, paramInt2, arrayOfChar);
/* 154 */         return StringCoding.safeTrim(arrayOfChar, j, this.cs, this.isTrusted);
/*     */       }
/* 156 */       this.cd.reset();
/* 157 */       ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte, paramInt1, paramInt2);
/* 158 */       CharBuffer localCharBuffer = CharBuffer.wrap(arrayOfChar);
/*     */       try {
/* 160 */         CoderResult localCoderResult = this.cd.decode(localByteBuffer, localCharBuffer, true);
/* 161 */         if (!localCoderResult.isUnderflow())
/* 162 */           localCoderResult.throwException();
/* 163 */         localCoderResult = this.cd.flush(localCharBuffer);
/* 164 */         if (!localCoderResult.isUnderflow())
/* 165 */           localCoderResult.throwException();
/*     */       }
/*     */       catch (CharacterCodingException localCharacterCodingException)
/*     */       {
/* 169 */         throw new Error(localCharacterCodingException);
/*     */       }
/* 171 */       return StringCoding.safeTrim(arrayOfChar, localCharBuffer.position(), this.cs, this.isTrusted);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class StringEncoder
/*     */   {
/*     */     private Charset cs;
/*     */     private CharsetEncoder ce;
/*     */     private final String requestedCharsetName;
/*     */     private final boolean isTrusted;
/*     */ 
/*     */     private StringEncoder(Charset paramCharset, String paramString)
/*     */     {
/* 280 */       this.requestedCharsetName = paramString;
/* 281 */       this.cs = paramCharset;
/* 282 */       this.ce = paramCharset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
/*     */ 
/* 285 */       this.isTrusted = (paramCharset.getClass().getClassLoader0() == null);
/*     */     }
/*     */ 
/*     */     String charsetName() {
/* 289 */       if ((this.cs instanceof HistoricallyNamedCharset))
/* 290 */         return ((HistoricallyNamedCharset)this.cs).historicalName();
/* 291 */       return this.cs.name();
/*     */     }
/*     */ 
/*     */     final String requestedCharsetName() {
/* 295 */       return this.requestedCharsetName;
/*     */     }
/*     */ 
/*     */     byte[] encode(char[] paramArrayOfChar, int paramInt1, int paramInt2) {
/* 299 */       int i = StringCoding.scale(paramInt2, this.ce.maxBytesPerChar());
/* 300 */       byte[] arrayOfByte = new byte[i];
/* 301 */       if (paramInt2 == 0)
/* 302 */         return arrayOfByte;
/* 303 */       if ((this.ce instanceof ArrayEncoder)) {
/* 304 */         int j = ((ArrayEncoder)this.ce).encode(paramArrayOfChar, paramInt1, paramInt2, arrayOfByte);
/* 305 */         return StringCoding.safeTrim(arrayOfByte, j, this.cs, this.isTrusted);
/*     */       }
/* 307 */       this.ce.reset();
/* 308 */       ByteBuffer localByteBuffer = ByteBuffer.wrap(arrayOfByte);
/* 309 */       CharBuffer localCharBuffer = CharBuffer.wrap(paramArrayOfChar, paramInt1, paramInt2);
/*     */       try {
/* 311 */         CoderResult localCoderResult = this.ce.encode(localCharBuffer, localByteBuffer, true);
/* 312 */         if (!localCoderResult.isUnderflow())
/* 313 */           localCoderResult.throwException();
/* 314 */         localCoderResult = this.ce.flush(localByteBuffer);
/* 315 */         if (!localCoderResult.isUnderflow())
/* 316 */           localCoderResult.throwException();
/*     */       }
/*     */       catch (CharacterCodingException localCharacterCodingException)
/*     */       {
/* 320 */         throw new Error(localCharacterCodingException);
/*     */       }
/* 322 */       return StringCoding.safeTrim(arrayOfByte, localByteBuffer.position(), this.cs, this.isTrusted);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.StringCoding
 * JD-Core Version:    0.6.2
 */