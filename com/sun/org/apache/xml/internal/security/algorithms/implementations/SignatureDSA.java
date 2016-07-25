/*     */ package com.sun.org.apache.xml.internal.security.algorithms.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.Base64;
/*     */ import java.io.IOException;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.Provider;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.Signature;
/*     */ import java.security.SignatureException;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class SignatureDSA extends SignatureAlgorithmSpi
/*     */ {
/*  47 */   static Logger log = Logger.getLogger(SignatureDSA.class.getName());
/*     */   public static final String _URI = "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
/*  54 */   private Signature _signatureAlgorithm = null;
/*     */ 
/*     */   protected String engineGetURI()
/*     */   {
/*  62 */     return "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
/*     */   }
/*     */ 
/*     */   public SignatureDSA()
/*     */     throws XMLSignatureException
/*     */   {
/*  72 */     String str1 = JCEMapper.translateURItoJCEID("http://www.w3.org/2000/09/xmldsig#dsa-sha1");
/*  73 */     if (log.isLoggable(Level.FINE)) {
/*  74 */       log.log(Level.FINE, "Created SignatureDSA using " + str1);
/*     */     }
/*  76 */     String str2 = JCEMapper.getProviderId();
/*     */     try {
/*  78 */       if (str2 == null)
/*  79 */         this._signatureAlgorithm = Signature.getInstance(str1);
/*     */       else
/*  81 */         this._signatureAlgorithm = Signature.getInstance(str1, str2);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */     {
/*  85 */       arrayOfObject = new Object[] { str1, localNoSuchAlgorithmException.getLocalizedMessage() };
/*  86 */       throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject);
/*     */     } catch (NoSuchProviderException localNoSuchProviderException) {
/*  88 */       Object[] arrayOfObject = { str1, localNoSuchProviderException.getLocalizedMessage() };
/*  89 */       throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineSetParameter(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 100 */       this._signatureAlgorithm.setParameter(paramAlgorithmParameterSpec);
/*     */     } catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException) {
/* 102 */       throw new XMLSignatureException("empty", localInvalidAlgorithmParameterException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean engineVerify(byte[] paramArrayOfByte)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 113 */       if (log.isLoggable(Level.FINE)) {
/* 114 */         log.log(Level.FINE, "Called DSA.verify() on " + Base64.encode(paramArrayOfByte));
/*     */       }
/* 116 */       byte[] arrayOfByte = convertXMLDSIGtoASN1(paramArrayOfByte);
/*     */ 
/* 118 */       return this._signatureAlgorithm.verify(arrayOfByte);
/*     */     } catch (SignatureException localSignatureException) {
/* 120 */       throw new XMLSignatureException("empty", localSignatureException);
/*     */     } catch (IOException localIOException) {
/* 122 */       throw new XMLSignatureException("empty", localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineInitVerify(Key paramKey)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     Object localObject;
/* 131 */     if (!(paramKey instanceof PublicKey)) {
/* 132 */       String str = paramKey.getClass().getName();
/* 133 */       localObject = PublicKey.class.getName();
/* 134 */       Object[] arrayOfObject = { str, localObject };
/*     */ 
/* 136 */       throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 141 */       this._signatureAlgorithm.initVerify((PublicKey)paramKey);
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException)
/*     */     {
/* 145 */       localObject = this._signatureAlgorithm;
/*     */       try {
/* 147 */         this._signatureAlgorithm = Signature.getInstance(this._signatureAlgorithm.getAlgorithm());
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 152 */         if (log.isLoggable(Level.FINE)) {
/* 153 */           log.log(Level.FINE, "Exception when reinstantiating Signature:" + localException);
/*     */         }
/* 155 */         this._signatureAlgorithm = ((Signature)localObject);
/*     */       }
/* 157 */       throw new XMLSignatureException("empty", localInvalidKeyException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected byte[] engineSign()
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 167 */       byte[] arrayOfByte = this._signatureAlgorithm.sign();
/*     */ 
/* 169 */       return convertASN1toXMLDSIG(arrayOfByte);
/*     */     } catch (IOException localIOException) {
/* 171 */       throw new XMLSignatureException("empty", localIOException);
/*     */     } catch (SignatureException localSignatureException) {
/* 173 */       throw new XMLSignatureException("empty", localSignatureException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineInitSign(Key paramKey, SecureRandom paramSecureRandom)
/*     */     throws XMLSignatureException
/*     */   {
/* 183 */     if (!(paramKey instanceof PrivateKey)) {
/* 184 */       String str1 = paramKey.getClass().getName();
/* 185 */       String str2 = PrivateKey.class.getName();
/* 186 */       Object[] arrayOfObject = { str1, str2 };
/*     */ 
/* 188 */       throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 193 */       this._signatureAlgorithm.initSign((PrivateKey)paramKey, paramSecureRandom);
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException) {
/* 196 */       throw new XMLSignatureException("empty", localInvalidKeyException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineInitSign(Key paramKey)
/*     */     throws XMLSignatureException
/*     */   {
/* 205 */     if (!(paramKey instanceof PrivateKey)) {
/* 206 */       String str1 = paramKey.getClass().getName();
/* 207 */       String str2 = PrivateKey.class.getName();
/* 208 */       Object[] arrayOfObject = { str1, str2 };
/*     */ 
/* 210 */       throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 215 */       this._signatureAlgorithm.initSign((PrivateKey)paramKey);
/*     */     } catch (InvalidKeyException localInvalidKeyException) {
/* 217 */       throw new XMLSignatureException("empty", localInvalidKeyException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineUpdate(byte[] paramArrayOfByte)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 226 */       this._signatureAlgorithm.update(paramArrayOfByte);
/*     */     } catch (SignatureException localSignatureException) {
/* 228 */       throw new XMLSignatureException("empty", localSignatureException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineUpdate(byte paramByte)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 237 */       this._signatureAlgorithm.update(paramByte);
/*     */     } catch (SignatureException localSignatureException) {
/* 239 */       throw new XMLSignatureException("empty", localSignatureException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 249 */       this._signatureAlgorithm.update(paramArrayOfByte, paramInt1, paramInt2);
/*     */     } catch (SignatureException localSignatureException) {
/* 251 */       throw new XMLSignatureException("empty", localSignatureException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String engineGetJCEAlgorithmString()
/*     */   {
/* 261 */     return this._signatureAlgorithm.getAlgorithm();
/*     */   }
/*     */ 
/*     */   protected String engineGetJCEProviderName()
/*     */   {
/* 270 */     return this._signatureAlgorithm.getProvider().getName();
/*     */   }
/*     */ 
/*     */   private static byte[] convertASN1toXMLDSIG(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 288 */     int i = paramArrayOfByte[3];
/*     */ 
/* 291 */     for (int j = i; (j > 0) && (paramArrayOfByte[(4 + i - j)] == 0); j--);
/* 293 */     int k = paramArrayOfByte[(5 + i)];
/*     */ 
/* 296 */     int m = k;
/* 297 */     while ((m > 0) && (paramArrayOfByte[(6 + i + k - m)] == 0)) m--;
/*     */ 
/* 299 */     if ((paramArrayOfByte[0] != 48) || (paramArrayOfByte[1] != paramArrayOfByte.length - 2) || (paramArrayOfByte[2] != 2) || (j > 20) || (paramArrayOfByte[(4 + i)] != 2) || (m > 20))
/*     */     {
/* 302 */       throw new IOException("Invalid ASN.1 format of DSA signature");
/*     */     }
/* 304 */     byte[] arrayOfByte = new byte[40];
/*     */ 
/* 306 */     System.arraycopy(paramArrayOfByte, 4 + i - j, arrayOfByte, 20 - j, j);
/*     */ 
/* 308 */     System.arraycopy(paramArrayOfByte, 6 + i + k - m, arrayOfByte, 40 - m, m);
/*     */ 
/* 311 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private static byte[] convertXMLDSIGtoASN1(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 329 */     if (paramArrayOfByte.length != 40) {
/* 330 */       throw new IOException("Invalid XMLDSIG format of DSA signature");
/*     */     }
/*     */ 
/* 335 */     for (int i = 20; (i > 0) && (paramArrayOfByte[(20 - i)] == 0); i--);
/* 337 */     int j = i;
/*     */ 
/* 339 */     if (paramArrayOfByte[(20 - i)] < 0) {
/* 340 */       j++;
/*     */     }
/*     */ 
/* 345 */     for (int k = 20; (k > 0) && (paramArrayOfByte[(40 - k)] == 0); k--);
/* 347 */     int m = k;
/*     */ 
/* 349 */     if (paramArrayOfByte[(40 - k)] < 0) {
/* 350 */       m++;
/*     */     }
/*     */ 
/* 353 */     byte[] arrayOfByte = new byte[6 + j + m];
/*     */ 
/* 355 */     arrayOfByte[0] = 48;
/* 356 */     arrayOfByte[1] = ((byte)(4 + j + m));
/* 357 */     arrayOfByte[2] = 2;
/* 358 */     arrayOfByte[3] = ((byte)j);
/*     */ 
/* 360 */     System.arraycopy(paramArrayOfByte, 20 - i, arrayOfByte, 4 + j - i, i);
/*     */ 
/* 362 */     arrayOfByte[(4 + j)] = 2;
/* 363 */     arrayOfByte[(5 + j)] = ((byte)m);
/*     */ 
/* 365 */     System.arraycopy(paramArrayOfByte, 40 - k, arrayOfByte, 6 + j + m - k, k);
/*     */ 
/* 367 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   protected void engineSetHMACOutputLength(int paramInt)
/*     */     throws XMLSignatureException
/*     */   {
/* 378 */     throw new XMLSignatureException("algorithms.HMACOutputLengthOnlyForHMAC");
/*     */   }
/*     */ 
/*     */   protected void engineInitSign(Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws XMLSignatureException
/*     */   {
/* 392 */     throw new XMLSignatureException("algorithms.CannotUseAlgorithmParameterSpecOnDSA");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureDSA
 * JD-Core Version:    0.6.2
 */