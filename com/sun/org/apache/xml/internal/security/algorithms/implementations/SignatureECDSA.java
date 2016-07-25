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
/*     */ public abstract class SignatureECDSA extends SignatureAlgorithmSpi
/*     */ {
/*  51 */   static Logger log = Logger.getLogger(SignatureECDSA.class.getName());
/*     */ 
/*  58 */   private Signature _signatureAlgorithm = null;
/*     */ 
/*     */   public abstract String engineGetURI();
/*     */ 
/*     */   private static byte[] convertASN1toXMLDSIG(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  76 */     int i = paramArrayOfByte[3];
/*     */ 
/*  79 */     for (int j = i; (j > 0) && (paramArrayOfByte[(4 + i - j)] == 0); j--);
/*  81 */     int k = paramArrayOfByte[(5 + i)];
/*     */ 
/*  84 */     int m = k;
/*  85 */     while ((m > 0) && (paramArrayOfByte[(6 + i + k - m)] == 0)) m--;
/*     */ 
/*  87 */     if ((paramArrayOfByte[0] != 48) || (paramArrayOfByte[1] != paramArrayOfByte.length - 2) || (paramArrayOfByte[2] != 2) || (j > 24) || (paramArrayOfByte[(4 + i)] != 2) || (m > 24))
/*     */     {
/*  90 */       throw new IOException("Invalid ASN.1 format of ECDSA signature");
/*     */     }
/*  92 */     byte[] arrayOfByte = new byte[48];
/*     */ 
/*  94 */     System.arraycopy(paramArrayOfByte, 4 + i - j, arrayOfByte, 24 - j, j);
/*     */ 
/*  96 */     System.arraycopy(paramArrayOfByte, 6 + i + k - m, arrayOfByte, 48 - m, m);
/*     */ 
/*  99 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private static byte[] convertXMLDSIGtoASN1(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 118 */     if (paramArrayOfByte.length != 48) {
/* 119 */       throw new IOException("Invalid XMLDSIG format of ECDSA signature");
/*     */     }
/*     */ 
/* 124 */     for (int i = 24; (i > 0) && (paramArrayOfByte[(24 - i)] == 0); i--);
/* 126 */     int j = i;
/*     */ 
/* 128 */     if (paramArrayOfByte[(24 - i)] < 0) {
/* 129 */       j++;
/*     */     }
/*     */ 
/* 134 */     for (int k = 24; (k > 0) && (paramArrayOfByte[(48 - k)] == 0); k--);
/* 136 */     int m = k;
/*     */ 
/* 138 */     if (paramArrayOfByte[(48 - k)] < 0) {
/* 139 */       m++;
/*     */     }
/*     */ 
/* 142 */     byte[] arrayOfByte = new byte[6 + j + m];
/*     */ 
/* 144 */     arrayOfByte[0] = 48;
/* 145 */     arrayOfByte[1] = ((byte)(4 + j + m));
/* 146 */     arrayOfByte[2] = 2;
/* 147 */     arrayOfByte[3] = ((byte)j);
/*     */ 
/* 149 */     System.arraycopy(paramArrayOfByte, 24 - i, arrayOfByte, 4 + j - i, i);
/*     */ 
/* 151 */     arrayOfByte[(4 + j)] = 2;
/* 152 */     arrayOfByte[(5 + j)] = ((byte)m);
/*     */ 
/* 154 */     System.arraycopy(paramArrayOfByte, 48 - k, arrayOfByte, 6 + j + m - k, k);
/*     */ 
/* 156 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public SignatureECDSA()
/*     */     throws XMLSignatureException
/*     */   {
/* 166 */     String str1 = JCEMapper.translateURItoJCEID(engineGetURI());
/*     */ 
/* 168 */     if (log.isLoggable(Level.FINE))
/* 169 */       log.log(Level.FINE, "Created SignatureECDSA using " + str1);
/* 170 */     String str2 = JCEMapper.getProviderId();
/*     */     try {
/* 172 */       if (str2 == null)
/* 173 */         this._signatureAlgorithm = Signature.getInstance(str1);
/*     */       else
/* 175 */         this._signatureAlgorithm = Signature.getInstance(str1, str2);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 178 */       arrayOfObject = new Object[] { str1, localNoSuchAlgorithmException.getLocalizedMessage() };
/*     */ 
/* 181 */       throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject);
/*     */     } catch (NoSuchProviderException localNoSuchProviderException) {
/* 183 */       Object[] arrayOfObject = { str1, localNoSuchProviderException.getLocalizedMessage() };
/*     */ 
/* 186 */       throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineSetParameter(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 195 */       this._signatureAlgorithm.setParameter(paramAlgorithmParameterSpec);
/*     */     } catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException) {
/* 197 */       throw new XMLSignatureException("empty", localInvalidAlgorithmParameterException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean engineVerify(byte[] paramArrayOfByte)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 206 */       byte[] arrayOfByte = convertXMLDSIGtoASN1(paramArrayOfByte);
/*     */ 
/* 208 */       if (log.isLoggable(Level.FINE)) {
/* 209 */         log.log(Level.FINE, "Called ECDSA.verify() on " + Base64.encode(paramArrayOfByte));
/*     */       }
/* 211 */       return this._signatureAlgorithm.verify(arrayOfByte);
/*     */     } catch (SignatureException localSignatureException) {
/* 213 */       throw new XMLSignatureException("empty", localSignatureException);
/*     */     } catch (IOException localIOException) {
/* 215 */       throw new XMLSignatureException("empty", localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineInitVerify(Key paramKey)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     Object localObject;
/* 222 */     if (!(paramKey instanceof PublicKey)) {
/* 223 */       String str = paramKey.getClass().getName();
/* 224 */       localObject = PublicKey.class.getName();
/* 225 */       Object[] arrayOfObject = { str, localObject };
/*     */ 
/* 227 */       throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 232 */       this._signatureAlgorithm.initVerify((PublicKey)paramKey);
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException)
/*     */     {
/* 236 */       localObject = this._signatureAlgorithm;
/*     */       try {
/* 238 */         this._signatureAlgorithm = Signature.getInstance(this._signatureAlgorithm.getAlgorithm());
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 243 */         if (log.isLoggable(Level.FINE)) {
/* 244 */           log.log(Level.FINE, "Exception when reinstantiating Signature:" + localException);
/*     */         }
/* 246 */         this._signatureAlgorithm = ((Signature)localObject);
/*     */       }
/* 248 */       throw new XMLSignatureException("empty", localInvalidKeyException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected byte[] engineSign() throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 256 */       byte[] arrayOfByte = this._signatureAlgorithm.sign();
/*     */ 
/* 258 */       return convertASN1toXMLDSIG(arrayOfByte);
/*     */     } catch (SignatureException localSignatureException) {
/* 260 */       throw new XMLSignatureException("empty", localSignatureException);
/*     */     } catch (IOException localIOException) {
/* 262 */       throw new XMLSignatureException("empty", localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineInitSign(Key paramKey, SecureRandom paramSecureRandom)
/*     */     throws XMLSignatureException
/*     */   {
/* 270 */     if (!(paramKey instanceof PrivateKey)) {
/* 271 */       String str1 = paramKey.getClass().getName();
/* 272 */       String str2 = PrivateKey.class.getName();
/* 273 */       Object[] arrayOfObject = { str1, str2 };
/*     */ 
/* 275 */       throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 280 */       this._signatureAlgorithm.initSign((PrivateKey)paramKey, paramSecureRandom);
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException) {
/* 283 */       throw new XMLSignatureException("empty", localInvalidKeyException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineInitSign(Key paramKey)
/*     */     throws XMLSignatureException
/*     */   {
/* 290 */     if (!(paramKey instanceof PrivateKey)) {
/* 291 */       String str1 = paramKey.getClass().getName();
/* 292 */       String str2 = PrivateKey.class.getName();
/* 293 */       Object[] arrayOfObject = { str1, str2 };
/*     */ 
/* 295 */       throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 300 */       this._signatureAlgorithm.initSign((PrivateKey)paramKey);
/*     */     } catch (InvalidKeyException localInvalidKeyException) {
/* 302 */       throw new XMLSignatureException("empty", localInvalidKeyException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineUpdate(byte[] paramArrayOfByte) throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 310 */       this._signatureAlgorithm.update(paramArrayOfByte);
/*     */     } catch (SignatureException localSignatureException) {
/* 312 */       throw new XMLSignatureException("empty", localSignatureException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineUpdate(byte paramByte) throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 320 */       this._signatureAlgorithm.update(paramByte);
/*     */     } catch (SignatureException localSignatureException) {
/* 322 */       throw new XMLSignatureException("empty", localSignatureException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 331 */       this._signatureAlgorithm.update(paramArrayOfByte, paramInt1, paramInt2);
/*     */     } catch (SignatureException localSignatureException) {
/* 333 */       throw new XMLSignatureException("empty", localSignatureException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String engineGetJCEAlgorithmString()
/*     */   {
/* 339 */     return this._signatureAlgorithm.getAlgorithm();
/*     */   }
/*     */ 
/*     */   protected String engineGetJCEProviderName()
/*     */   {
/* 344 */     return this._signatureAlgorithm.getProvider().getName();
/*     */   }
/*     */ 
/*     */   protected void engineSetHMACOutputLength(int paramInt)
/*     */     throws XMLSignatureException
/*     */   {
/* 350 */     throw new XMLSignatureException("algorithms.HMACOutputLengthOnlyForHMAC");
/*     */   }
/*     */ 
/*     */   protected void engineInitSign(Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws XMLSignatureException
/*     */   {
/* 357 */     throw new XMLSignatureException("algorithms.CannotUseAlgorithmParameterSpecOnRSA");
/*     */   }
/*     */ 
/*     */   public static class SignatureECDSASHA1 extends SignatureECDSA
/*     */   {
/*     */     public SignatureECDSASHA1()
/*     */       throws XMLSignatureException
/*     */     {
/*     */     }
/*     */ 
/*     */     public String engineGetURI()
/*     */     {
/* 380 */       return "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha1";
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureECDSA
 * JD-Core Version:    0.6.2
 */