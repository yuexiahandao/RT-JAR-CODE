/*     */ package com.sun.org.apache.xml.internal.security.algorithms.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
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
/*     */ public abstract class SignatureBaseRSA extends SignatureAlgorithmSpi
/*     */ {
/*  46 */   static Logger log = Logger.getLogger(SignatureBaseRSA.class.getName());
/*     */ 
/*  54 */   private Signature _signatureAlgorithm = null;
/*     */ 
/*     */   public abstract String engineGetURI();
/*     */ 
/*     */   public SignatureBaseRSA()
/*     */     throws XMLSignatureException
/*     */   {
/*  63 */     String str1 = JCEMapper.translateURItoJCEID(engineGetURI());
/*     */ 
/*  65 */     if (log.isLoggable(Level.FINE))
/*  66 */       log.log(Level.FINE, "Created SignatureRSA using " + str1);
/*  67 */     String str2 = JCEMapper.getProviderId();
/*     */     try {
/*  69 */       if (str2 == null)
/*  70 */         this._signatureAlgorithm = Signature.getInstance(str1);
/*     */       else
/*  72 */         this._signatureAlgorithm = Signature.getInstance(str1, str2);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/*  75 */       arrayOfObject = new Object[] { str1, localNoSuchAlgorithmException.getLocalizedMessage() };
/*     */ 
/*  77 */       throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject);
/*     */     } catch (NoSuchProviderException localNoSuchProviderException) {
/*  79 */       Object[] arrayOfObject = { str1, localNoSuchProviderException.getLocalizedMessage() };
/*     */ 
/*  81 */       throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineSetParameter(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/*  90 */       this._signatureAlgorithm.setParameter(paramAlgorithmParameterSpec);
/*     */     } catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException) {
/*  92 */       throw new XMLSignatureException("empty", localInvalidAlgorithmParameterException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean engineVerify(byte[] paramArrayOfByte)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 101 */       return this._signatureAlgorithm.verify(paramArrayOfByte);
/*     */     } catch (SignatureException localSignatureException) {
/* 103 */       throw new XMLSignatureException("empty", localSignatureException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineInitVerify(Key paramKey)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     Object localObject;
/* 110 */     if (!(paramKey instanceof PublicKey)) {
/* 111 */       String str = paramKey.getClass().getName();
/* 112 */       localObject = PublicKey.class.getName();
/* 113 */       Object[] arrayOfObject = { str, localObject };
/*     */ 
/* 115 */       throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 120 */       this._signatureAlgorithm.initVerify((PublicKey)paramKey);
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException)
/*     */     {
/* 124 */       localObject = this._signatureAlgorithm;
/*     */       try {
/* 126 */         this._signatureAlgorithm = Signature.getInstance(this._signatureAlgorithm.getAlgorithm());
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 131 */         if (log.isLoggable(Level.FINE)) {
/* 132 */           log.log(Level.FINE, "Exception when reinstantiating Signature:" + localException);
/*     */         }
/* 134 */         this._signatureAlgorithm = ((Signature)localObject);
/*     */       }
/* 136 */       throw new XMLSignatureException("empty", localInvalidKeyException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected byte[] engineSign() throws XMLSignatureException
/*     */   {
/*     */     try {
/* 143 */       return this._signatureAlgorithm.sign();
/*     */     } catch (SignatureException localSignatureException) {
/* 145 */       throw new XMLSignatureException("empty", localSignatureException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineInitSign(Key paramKey, SecureRandom paramSecureRandom)
/*     */     throws XMLSignatureException
/*     */   {
/* 153 */     if (!(paramKey instanceof PrivateKey)) {
/* 154 */       String str1 = paramKey.getClass().getName();
/* 155 */       String str2 = PrivateKey.class.getName();
/* 156 */       Object[] arrayOfObject = { str1, str2 };
/*     */ 
/* 158 */       throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 163 */       this._signatureAlgorithm.initSign((PrivateKey)paramKey, paramSecureRandom);
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException) {
/* 166 */       throw new XMLSignatureException("empty", localInvalidKeyException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineInitSign(Key paramKey)
/*     */     throws XMLSignatureException
/*     */   {
/* 173 */     if (!(paramKey instanceof PrivateKey)) {
/* 174 */       String str1 = paramKey.getClass().getName();
/* 175 */       String str2 = PrivateKey.class.getName();
/* 176 */       Object[] arrayOfObject = { str1, str2 };
/*     */ 
/* 178 */       throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 183 */       this._signatureAlgorithm.initSign((PrivateKey)paramKey);
/*     */     } catch (InvalidKeyException localInvalidKeyException) {
/* 185 */       throw new XMLSignatureException("empty", localInvalidKeyException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineUpdate(byte[] paramArrayOfByte) throws XMLSignatureException
/*     */   {
/*     */     try {
/* 192 */       this._signatureAlgorithm.update(paramArrayOfByte);
/*     */     } catch (SignatureException localSignatureException) {
/* 194 */       throw new XMLSignatureException("empty", localSignatureException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineUpdate(byte paramByte) throws XMLSignatureException
/*     */   {
/*     */     try {
/* 201 */       this._signatureAlgorithm.update(paramByte);
/*     */     } catch (SignatureException localSignatureException) {
/* 203 */       throw new XMLSignatureException("empty", localSignatureException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 211 */       this._signatureAlgorithm.update(paramArrayOfByte, paramInt1, paramInt2);
/*     */     } catch (SignatureException localSignatureException) {
/* 213 */       throw new XMLSignatureException("empty", localSignatureException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String engineGetJCEAlgorithmString()
/*     */   {
/* 219 */     return this._signatureAlgorithm.getAlgorithm();
/*     */   }
/*     */ 
/*     */   protected String engineGetJCEProviderName()
/*     */   {
/* 224 */     return this._signatureAlgorithm.getProvider().getName();
/*     */   }
/*     */ 
/*     */   protected void engineSetHMACOutputLength(int paramInt)
/*     */     throws XMLSignatureException
/*     */   {
/* 230 */     throw new XMLSignatureException("algorithms.HMACOutputLengthOnlyForHMAC");
/*     */   }
/*     */ 
/*     */   protected void engineInitSign(Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws XMLSignatureException
/*     */   {
/* 238 */     throw new XMLSignatureException("algorithms.CannotUseAlgorithmParameterSpecOnRSA");
/*     */   }
/*     */ 
/*     */   public static class SignatureRSAMD5 extends SignatureBaseRSA
/*     */   {
/*     */     public SignatureRSAMD5()
/*     */       throws XMLSignatureException
/*     */     {
/*     */     }
/*     */ 
/*     */     public String engineGetURI()
/*     */     {
/* 376 */       return "http://www.w3.org/2001/04/xmldsig-more#rsa-md5";
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class SignatureRSARIPEMD160 extends SignatureBaseRSA
/*     */   {
/*     */     public SignatureRSARIPEMD160()
/*     */       throws XMLSignatureException
/*     */     {
/*     */     }
/*     */ 
/*     */     public String engineGetURI()
/*     */     {
/* 353 */       return "http://www.w3.org/2001/04/xmldsig-more#rsa-ripemd160";
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class SignatureRSASHA1 extends SignatureBaseRSA
/*     */   {
/*     */     public SignatureRSASHA1()
/*     */       throws XMLSignatureException
/*     */     {
/*     */     }
/*     */ 
/*     */     public String engineGetURI()
/*     */     {
/* 261 */       return "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class SignatureRSASHA256 extends SignatureBaseRSA
/*     */   {
/*     */     public SignatureRSASHA256()
/*     */       throws XMLSignatureException
/*     */     {
/*     */     }
/*     */ 
/*     */     public String engineGetURI()
/*     */     {
/* 284 */       return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class SignatureRSASHA384 extends SignatureBaseRSA
/*     */   {
/*     */     public SignatureRSASHA384()
/*     */       throws XMLSignatureException
/*     */     {
/*     */     }
/*     */ 
/*     */     public String engineGetURI()
/*     */     {
/* 307 */       return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha384";
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class SignatureRSASHA512 extends SignatureBaseRSA
/*     */   {
/*     */     public SignatureRSASHA512()
/*     */       throws XMLSignatureException
/*     */     {
/*     */     }
/*     */ 
/*     */     public String engineGetURI()
/*     */     {
/* 330 */       return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512";
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureBaseRSA
 * JD-Core Version:    0.6.2
 */