/*     */ package com.sun.org.apache.xml.internal.security.algorithms;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac.IntegrityHmacMD5;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac.IntegrityHmacRIPEMD160;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac.IntegrityHmacSHA1;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac.IntegrityHmacSHA256;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac.IntegrityHmacSHA384;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac.IntegrityHmacSHA512;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureBaseRSA.SignatureRSAMD5;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureBaseRSA.SignatureRSARIPEMD160;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureBaseRSA.SignatureRSASHA1;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureBaseRSA.SignatureRSASHA256;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureBaseRSA.SignatureRSASHA384;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureBaseRSA.SignatureRSASHA512;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureDSA;
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureECDSA.SignatureECDSASHA1;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.AlgorithmAlreadyRegisteredException;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
/*     */ import java.security.Key;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class SignatureAlgorithm extends Algorithm
/*     */ {
/*  54 */   private static Logger log = Logger.getLogger(SignatureAlgorithm.class.getName());
/*     */ 
/*  58 */   private static Map<String, Class<? extends SignatureAlgorithmSpi>> algorithmHash = new ConcurrentHashMap();
/*     */   private final SignatureAlgorithmSpi signatureAlgorithm;
/*     */   private final String algorithmURI;
/*     */ 
/*     */   public SignatureAlgorithm(Document paramDocument, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/*  74 */     super(paramDocument, paramString);
/*  75 */     this.algorithmURI = paramString;
/*     */ 
/*  77 */     this.signatureAlgorithm = getSignatureAlgorithmSpi(paramString);
/*  78 */     this.signatureAlgorithm.engineGetContextFromElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public SignatureAlgorithm(Document paramDocument, String paramString, int paramInt)
/*     */     throws XMLSecurityException
/*     */   {
/*  92 */     super(paramDocument, paramString);
/*  93 */     this.algorithmURI = paramString;
/*     */ 
/*  95 */     this.signatureAlgorithm = getSignatureAlgorithmSpi(paramString);
/*  96 */     this.signatureAlgorithm.engineGetContextFromElement(this._constructionElement);
/*     */ 
/*  98 */     this.signatureAlgorithm.engineSetHMACOutputLength(paramInt);
/*  99 */     ((IntegrityHmac)this.signatureAlgorithm).engineAddContextToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public SignatureAlgorithm(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/* 110 */     this(paramElement, paramString, false);
/*     */   }
/*     */ 
/*     */   public SignatureAlgorithm(Element paramElement, String paramString, boolean paramBoolean)
/*     */     throws XMLSecurityException
/*     */   {
/* 124 */     super(paramElement, paramString);
/* 125 */     this.algorithmURI = getURI();
/*     */ 
/* 127 */     Attr localAttr = paramElement.getAttributeNodeNS(null, "Id");
/* 128 */     if (localAttr != null) {
/* 129 */       paramElement.setIdAttributeNode(localAttr, true);
/*     */     }
/*     */ 
/* 132 */     if ((paramBoolean) && (("http://www.w3.org/2001/04/xmldsig-more#hmac-md5".equals(this.algorithmURI)) || ("http://www.w3.org/2001/04/xmldsig-more#rsa-md5".equals(this.algorithmURI))))
/*     */     {
/* 134 */       Object[] arrayOfObject = { this.algorithmURI };
/*     */ 
/* 136 */       throw new XMLSecurityException("signature.signatureAlgorithm", arrayOfObject);
/*     */     }
/*     */ 
/* 139 */     this.signatureAlgorithm = getSignatureAlgorithmSpi(this.algorithmURI);
/* 140 */     this.signatureAlgorithm.engineGetContextFromElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   private static SignatureAlgorithmSpi getSignatureAlgorithmSpi(String paramString)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 149 */       Class localClass = (Class)algorithmHash.get(paramString);
/*     */ 
/* 151 */       if (log.isLoggable(Level.FINE)) {
/* 152 */         log.log(Level.FINE, "Create URI \"" + paramString + "\" class \"" + localClass + "\"");
/*     */       }
/*     */ 
/* 155 */       return (SignatureAlgorithmSpi)localClass.newInstance();
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 157 */       arrayOfObject = new Object[] { paramString, localIllegalAccessException.getMessage() };
/* 158 */       throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject, localIllegalAccessException);
/*     */     } catch (InstantiationException localInstantiationException) {
/* 160 */       arrayOfObject = new Object[] { paramString, localInstantiationException.getMessage() };
/* 161 */       throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject, localInstantiationException);
/*     */     } catch (NullPointerException localNullPointerException) {
/* 163 */       Object[] arrayOfObject = { paramString, localNullPointerException.getMessage() };
/* 164 */       throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject, localNullPointerException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] sign()
/*     */     throws XMLSignatureException
/*     */   {
/* 177 */     return this.signatureAlgorithm.engineSign();
/*     */   }
/*     */ 
/*     */   public String getJCEAlgorithmString()
/*     */   {
/* 187 */     return this.signatureAlgorithm.engineGetJCEAlgorithmString();
/*     */   }
/*     */ 
/*     */   public String getJCEProviderName()
/*     */   {
/* 196 */     return this.signatureAlgorithm.engineGetJCEProviderName();
/*     */   }
/*     */ 
/*     */   public void update(byte[] paramArrayOfByte)
/*     */     throws XMLSignatureException
/*     */   {
/* 207 */     this.signatureAlgorithm.engineUpdate(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public void update(byte paramByte)
/*     */     throws XMLSignatureException
/*     */   {
/* 218 */     this.signatureAlgorithm.engineUpdate(paramByte);
/*     */   }
/*     */ 
/*     */   public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws XMLSignatureException
/*     */   {
/* 231 */     this.signatureAlgorithm.engineUpdate(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void initSign(Key paramKey)
/*     */     throws XMLSignatureException
/*     */   {
/* 242 */     this.signatureAlgorithm.engineInitSign(paramKey);
/*     */   }
/*     */ 
/*     */   public void initSign(Key paramKey, SecureRandom paramSecureRandom)
/*     */     throws XMLSignatureException
/*     */   {
/* 255 */     this.signatureAlgorithm.engineInitSign(paramKey, paramSecureRandom);
/*     */   }
/*     */ 
/*     */   public void initSign(Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws XMLSignatureException
/*     */   {
/* 269 */     this.signatureAlgorithm.engineInitSign(paramKey, paramAlgorithmParameterSpec);
/*     */   }
/*     */ 
/*     */   public void setParameter(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws XMLSignatureException
/*     */   {
/* 281 */     this.signatureAlgorithm.engineSetParameter(paramAlgorithmParameterSpec);
/*     */   }
/*     */ 
/*     */   public void initVerify(Key paramKey)
/*     */     throws XMLSignatureException
/*     */   {
/* 292 */     this.signatureAlgorithm.engineInitVerify(paramKey);
/*     */   }
/*     */ 
/*     */   public boolean verify(byte[] paramArrayOfByte)
/*     */     throws XMLSignatureException
/*     */   {
/* 305 */     return this.signatureAlgorithm.engineVerify(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public final String getURI()
/*     */   {
/* 314 */     return this._constructionElement.getAttributeNS(null, "Algorithm");
/*     */   }
/*     */ 
/*     */   public static void register(String paramString1, String paramString2)
/*     */     throws AlgorithmAlreadyRegisteredException, ClassNotFoundException, XMLSignatureException
/*     */   {
/* 332 */     JavaUtils.checkRegisterPermission();
/* 333 */     if (log.isLoggable(Level.FINE)) {
/* 334 */       log.log(Level.FINE, "Try to register " + paramString1 + " " + paramString2);
/*     */     }
/*     */ 
/* 338 */     Class localClass = (Class)algorithmHash.get(paramString1);
/*     */     Object localObject;
/* 339 */     if (localClass != null) {
/* 340 */       localObject = new Object[] { paramString1, localClass };
/* 341 */       throw new AlgorithmAlreadyRegisteredException("algorithm.alreadyRegistered", (Object[])localObject);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 346 */       localObject = ClassLoaderUtils.loadClass(paramString2, SignatureAlgorithm.class);
/*     */ 
/* 349 */       algorithmHash.put(paramString1, localObject);
/*     */     } catch (NullPointerException localNullPointerException) {
/* 351 */       Object[] arrayOfObject = { paramString1, localNullPointerException.getMessage() };
/* 352 */       throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject, localNullPointerException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void register(String paramString, Class<? extends SignatureAlgorithmSpi> paramClass)
/*     */     throws AlgorithmAlreadyRegisteredException, ClassNotFoundException, XMLSignatureException
/*     */   {
/* 370 */     JavaUtils.checkRegisterPermission();
/* 371 */     if (log.isLoggable(Level.FINE)) {
/* 372 */       log.log(Level.FINE, "Try to register " + paramString + " " + paramClass);
/*     */     }
/*     */ 
/* 376 */     Class localClass = (Class)algorithmHash.get(paramString);
/* 377 */     if (localClass != null) {
/* 378 */       Object[] arrayOfObject = { paramString, localClass };
/* 379 */       throw new AlgorithmAlreadyRegisteredException("algorithm.alreadyRegistered", arrayOfObject);
/*     */     }
/*     */ 
/* 383 */     algorithmHash.put(paramString, paramClass);
/*     */   }
/*     */ 
/*     */   public static void registerDefaultAlgorithms()
/*     */   {
/* 390 */     algorithmHash.put("http://www.w3.org/2000/09/xmldsig#dsa-sha1", SignatureDSA.class);
/*     */ 
/* 393 */     algorithmHash.put("http://www.w3.org/2000/09/xmldsig#rsa-sha1", SignatureBaseRSA.SignatureRSASHA1.class);
/*     */ 
/* 396 */     algorithmHash.put("http://www.w3.org/2000/09/xmldsig#hmac-sha1", IntegrityHmac.IntegrityHmacSHA1.class);
/*     */ 
/* 399 */     algorithmHash.put("http://www.w3.org/2001/04/xmldsig-more#rsa-md5", SignatureBaseRSA.SignatureRSAMD5.class);
/*     */ 
/* 403 */     algorithmHash.put("http://www.w3.org/2001/04/xmldsig-more#rsa-ripemd160", SignatureBaseRSA.SignatureRSARIPEMD160.class);
/*     */ 
/* 407 */     algorithmHash.put("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256", SignatureBaseRSA.SignatureRSASHA256.class);
/*     */ 
/* 410 */     algorithmHash.put("http://www.w3.org/2001/04/xmldsig-more#rsa-sha384", SignatureBaseRSA.SignatureRSASHA384.class);
/*     */ 
/* 413 */     algorithmHash.put("http://www.w3.org/2001/04/xmldsig-more#rsa-sha512", SignatureBaseRSA.SignatureRSASHA512.class);
/*     */ 
/* 416 */     algorithmHash.put("http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha1", SignatureECDSA.SignatureECDSASHA1.class);
/*     */ 
/* 419 */     algorithmHash.put("http://www.w3.org/2001/04/xmldsig-more#hmac-md5", IntegrityHmac.IntegrityHmacMD5.class);
/*     */ 
/* 422 */     algorithmHash.put("http://www.w3.org/2001/04/xmldsig-more#hmac-ripemd160", IntegrityHmac.IntegrityHmacRIPEMD160.class);
/*     */ 
/* 425 */     algorithmHash.put("http://www.w3.org/2001/04/xmldsig-more#hmac-sha256", IntegrityHmac.IntegrityHmacSHA256.class);
/*     */ 
/* 428 */     algorithmHash.put("http://www.w3.org/2001/04/xmldsig-more#hmac-sha384", IntegrityHmac.IntegrityHmacSHA384.class);
/*     */ 
/* 431 */     algorithmHash.put("http://www.w3.org/2001/04/xmldsig-more#hmac-sha512", IntegrityHmac.IntegrityHmacSHA512.class);
/*     */   }
/*     */ 
/*     */   public String getBaseNamespace()
/*     */   {
/* 442 */     return "http://www.w3.org/2000/09/xmldsig#";
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 451 */     return "SignatureMethod";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm
 * JD-Core Version:    0.6.2
 */