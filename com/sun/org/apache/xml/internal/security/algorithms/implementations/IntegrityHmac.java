/*     */ package com.sun.org.apache.xml.internal.security.algorithms.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.MessageDigestAlgorithm;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Provider;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.crypto.Mac;
/*     */ import javax.crypto.SecretKey;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ public abstract class IntegrityHmac extends SignatureAlgorithmSpi
/*     */ {
/*  53 */   static Logger log = Logger.getLogger(IntegrityHmacSHA1.class.getName());
/*     */ 
/*  69 */   private Mac _macAlgorithm = null;
/*  70 */   private boolean _HMACOutputLengthSet = false;
/*     */ 
/*  73 */   int _HMACOutputLength = 0;
/*     */ 
/*     */   public abstract String engineGetURI();
/*     */ 
/*     */   abstract int getDigestLength();
/*     */ 
/*     */   public IntegrityHmac()
/*     */     throws XMLSignatureException
/*     */   {
/*  82 */     String str = JCEMapper.translateURItoJCEID(engineGetURI());
/*  83 */     if (log.isLoggable(Level.FINE))
/*  84 */       log.log(Level.FINE, "Created IntegrityHmacSHA1 using " + str);
/*     */     try
/*     */     {
/*  87 */       this._macAlgorithm = Mac.getInstance(str);
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/*  89 */       Object[] arrayOfObject = { str, localNoSuchAlgorithmException.getLocalizedMessage() };
/*     */ 
/*  92 */       throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineSetParameter(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws XMLSignatureException
/*     */   {
/* 105 */     throw new XMLSignatureException("empty");
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 109 */     this._HMACOutputLength = 0;
/* 110 */     this._HMACOutputLengthSet = false;
/* 111 */     this._macAlgorithm.reset();
/*     */   }
/*     */ 
/*     */   protected boolean engineVerify(byte[] paramArrayOfByte)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 126 */       if ((this._HMACOutputLengthSet) && (this._HMACOutputLength < getDigestLength())) {
/* 127 */         if (log.isLoggable(Level.FINE)) {
/* 128 */           log.log(Level.FINE, "HMACOutputLength must not be less than " + getDigestLength());
/*     */         }
/*     */ 
/* 131 */         throw new XMLSignatureException("errorMessages.XMLSignatureException");
/*     */       }
/* 133 */       byte[] arrayOfByte = this._macAlgorithm.doFinal();
/* 134 */       return MessageDigestAlgorithm.isEqual(arrayOfByte, paramArrayOfByte);
/*     */     }
/*     */     catch (IllegalStateException localIllegalStateException) {
/* 137 */       throw new XMLSignatureException("empty", localIllegalStateException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineInitVerify(Key paramKey)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     Object localObject;
/* 150 */     if (!(paramKey instanceof SecretKey)) {
/* 151 */       String str = paramKey.getClass().getName();
/* 152 */       localObject = SecretKey.class.getName();
/* 153 */       Object[] arrayOfObject = { str, localObject };
/*     */ 
/* 155 */       throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 160 */       this._macAlgorithm.init(paramKey);
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException)
/*     */     {
/* 164 */       localObject = this._macAlgorithm;
/*     */       try {
/* 166 */         this._macAlgorithm = Mac.getInstance(this._macAlgorithm.getAlgorithm());
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 170 */         if (log.isLoggable(Level.FINE)) {
/* 171 */           log.log(Level.FINE, "Exception when reinstantiating Mac:" + localException);
/*     */         }
/* 173 */         this._macAlgorithm = ((Mac)localObject);
/*     */       }
/* 175 */       throw new XMLSignatureException("empty", localInvalidKeyException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected byte[] engineSign()
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 189 */       if ((this._HMACOutputLengthSet) && (this._HMACOutputLength < getDigestLength())) {
/* 190 */         if (log.isLoggable(Level.FINE)) {
/* 191 */           log.log(Level.FINE, "HMACOutputLength must not be less than " + getDigestLength());
/*     */         }
/*     */ 
/* 194 */         throw new XMLSignatureException("errorMessages.XMLSignatureException");
/*     */       }
/* 196 */       return this._macAlgorithm.doFinal();
/*     */     }
/*     */     catch (IllegalStateException localIllegalStateException) {
/* 199 */       throw new XMLSignatureException("empty", localIllegalStateException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static byte[] reduceBitLength(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 213 */     int i = paramInt / 8;
/* 214 */     int j = paramInt % 8;
/* 215 */     byte[] arrayOfByte1 = new byte[i + (j == 0 ? 0 : 1)];
/*     */ 
/* 219 */     System.arraycopy(paramArrayOfByte, 0, arrayOfByte1, 0, i);
/*     */ 
/* 221 */     if (j > 0) {
/* 222 */       byte[] arrayOfByte2 = { 0, -128, -64, -32, -16, -8, -4, -2 };
/*     */ 
/* 225 */       arrayOfByte1[i] = ((byte)(paramArrayOfByte[i] & arrayOfByte2[j]));
/*     */     }
/*     */ 
/* 228 */     return arrayOfByte1;
/*     */   }
/*     */ 
/*     */   protected void engineInitSign(Key paramKey)
/*     */     throws XMLSignatureException
/*     */   {
/* 239 */     if (!(paramKey instanceof SecretKey)) {
/* 240 */       String str1 = paramKey.getClass().getName();
/* 241 */       String str2 = SecretKey.class.getName();
/* 242 */       Object[] arrayOfObject = { str1, str2 };
/*     */ 
/* 244 */       throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 249 */       this._macAlgorithm.init(paramKey);
/*     */     } catch (InvalidKeyException localInvalidKeyException) {
/* 251 */       throw new XMLSignatureException("empty", localInvalidKeyException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineInitSign(Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws XMLSignatureException
/*     */   {
/* 266 */     if (!(paramKey instanceof SecretKey)) {
/* 267 */       String str1 = paramKey.getClass().getName();
/* 268 */       String str2 = SecretKey.class.getName();
/* 269 */       Object[] arrayOfObject = { str1, str2 };
/*     */ 
/* 271 */       throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 276 */       this._macAlgorithm.init(paramKey, paramAlgorithmParameterSpec);
/*     */     } catch (InvalidKeyException localInvalidKeyException) {
/* 278 */       throw new XMLSignatureException("empty", localInvalidKeyException);
/*     */     } catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException) {
/* 280 */       throw new XMLSignatureException("empty", localInvalidAlgorithmParameterException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineInitSign(Key paramKey, SecureRandom paramSecureRandom)
/*     */     throws XMLSignatureException
/*     */   {
/* 293 */     throw new XMLSignatureException("algorithms.CannotUseSecureRandomOnMAC");
/*     */   }
/*     */ 
/*     */   protected void engineUpdate(byte[] paramArrayOfByte)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 306 */       this._macAlgorithm.update(paramArrayOfByte);
/*     */     } catch (IllegalStateException localIllegalStateException) {
/* 308 */       throw new XMLSignatureException("empty", localIllegalStateException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineUpdate(byte paramByte)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 322 */       this._macAlgorithm.update(paramByte);
/*     */     } catch (IllegalStateException localIllegalStateException) {
/* 324 */       throw new XMLSignatureException("empty", localIllegalStateException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 341 */       this._macAlgorithm.update(paramArrayOfByte, paramInt1, paramInt2);
/*     */     } catch (IllegalStateException localIllegalStateException) {
/* 343 */       throw new XMLSignatureException("empty", localIllegalStateException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String engineGetJCEAlgorithmString()
/*     */   {
/* 354 */     log.log(Level.FINE, "engineGetJCEAlgorithmString()");
/*     */ 
/* 356 */     return this._macAlgorithm.getAlgorithm();
/*     */   }
/*     */ 
/*     */   protected String engineGetJCEProviderName()
/*     */   {
/* 365 */     return this._macAlgorithm.getProvider().getName();
/*     */   }
/*     */ 
/*     */   protected void engineSetHMACOutputLength(int paramInt)
/*     */   {
/* 374 */     this._HMACOutputLength = paramInt;
/* 375 */     this._HMACOutputLengthSet = true;
/*     */   }
/*     */ 
/*     */   protected void engineGetContextFromElement(Element paramElement)
/*     */   {
/* 385 */     super.engineGetContextFromElement(paramElement);
/*     */ 
/* 387 */     if (paramElement == null) {
/* 388 */       throw new IllegalArgumentException("element null");
/*     */     }
/*     */ 
/* 391 */     Text localText = XMLUtils.selectDsNodeText(paramElement.getFirstChild(), "HMACOutputLength", 0);
/*     */ 
/* 394 */     if (localText != null) {
/* 395 */       this._HMACOutputLength = Integer.parseInt(localText.getData());
/* 396 */       this._HMACOutputLengthSet = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void engineAddContextToElement(Element paramElement)
/*     */   {
/* 408 */     if (paramElement == null) {
/* 409 */       throw new IllegalArgumentException("null element");
/*     */     }
/*     */ 
/* 412 */     if (this._HMACOutputLengthSet) {
/* 413 */       Document localDocument = paramElement.getOwnerDocument();
/* 414 */       Element localElement = XMLUtils.createElementInSignatureSpace(localDocument, "HMACOutputLength");
/*     */ 
/* 416 */       Text localText = localDocument.createTextNode(new Integer(this._HMACOutputLength).toString());
/*     */ 
/* 419 */       localElement.appendChild(localText);
/* 420 */       XMLUtils.addReturnToElement(paramElement);
/* 421 */       paramElement.appendChild(localElement);
/* 422 */       XMLUtils.addReturnToElement(paramElement);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class IntegrityHmacMD5 extends IntegrityHmac
/*     */   {
/*     */     public IntegrityHmacMD5()
/*     */       throws XMLSignatureException
/*     */     {
/*     */     }
/*     */ 
/*     */     public String engineGetURI()
/*     */     {
/* 604 */       return "http://www.w3.org/2001/04/xmldsig-more#hmac-md5";
/*     */     }
/*     */ 
/*     */     int getDigestLength() {
/* 608 */       return 128;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class IntegrityHmacRIPEMD160 extends IntegrityHmac
/*     */   {
/*     */     public IntegrityHmacRIPEMD160()
/*     */       throws XMLSignatureException
/*     */     {
/*     */     }
/*     */ 
/*     */     public String engineGetURI()
/*     */     {
/* 573 */       return "http://www.w3.org/2001/04/xmldsig-more#hmac-ripemd160";
/*     */     }
/*     */ 
/*     */     int getDigestLength() {
/* 577 */       return 160;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class IntegrityHmacSHA1 extends IntegrityHmac
/*     */   {
/*     */     public IntegrityHmacSHA1()
/*     */       throws XMLSignatureException
/*     */     {
/*     */     }
/*     */ 
/*     */     public String engineGetURI()
/*     */     {
/* 449 */       return "http://www.w3.org/2000/09/xmldsig#hmac-sha1";
/*     */     }
/*     */ 
/*     */     int getDigestLength() {
/* 453 */       return 160;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class IntegrityHmacSHA256 extends IntegrityHmac
/*     */   {
/*     */     public IntegrityHmacSHA256()
/*     */       throws XMLSignatureException
/*     */     {
/*     */     }
/*     */ 
/*     */     public String engineGetURI()
/*     */     {
/* 480 */       return "http://www.w3.org/2001/04/xmldsig-more#hmac-sha256";
/*     */     }
/*     */ 
/*     */     int getDigestLength() {
/* 484 */       return 256;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class IntegrityHmacSHA384 extends IntegrityHmac
/*     */   {
/*     */     public IntegrityHmacSHA384()
/*     */       throws XMLSignatureException
/*     */     {
/*     */     }
/*     */ 
/*     */     public String engineGetURI()
/*     */     {
/* 511 */       return "http://www.w3.org/2001/04/xmldsig-more#hmac-sha384";
/*     */     }
/*     */ 
/*     */     int getDigestLength() {
/* 515 */       return 384;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class IntegrityHmacSHA512 extends IntegrityHmac
/*     */   {
/*     */     public IntegrityHmacSHA512()
/*     */       throws XMLSignatureException
/*     */     {
/*     */     }
/*     */ 
/*     */     public String engineGetURI()
/*     */     {
/* 542 */       return "http://www.w3.org/2001/04/xmldsig-more#hmac-sha512";
/*     */     }
/*     */ 
/*     */     int getDigestLength() {
/* 546 */       return 512;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac
 * JD-Core Version:    0.6.2
 */