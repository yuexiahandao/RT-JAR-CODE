/*     */ package com.sun.org.apache.xml.internal.security.algorithms;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
/*     */ import java.security.DigestException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Provider;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.w3c.dom.Document;
/*     */ 
/*     */ public class MessageDigestAlgorithm extends Algorithm
/*     */ {
/*     */   public static final String ALGO_ID_DIGEST_NOT_RECOMMENDED_MD5 = "http://www.w3.org/2001/04/xmldsig-more#md5";
/*     */   public static final String ALGO_ID_DIGEST_SHA1 = "http://www.w3.org/2000/09/xmldsig#sha1";
/*     */   public static final String ALGO_ID_DIGEST_SHA256 = "http://www.w3.org/2001/04/xmlenc#sha256";
/*     */   public static final String ALGO_ID_DIGEST_SHA384 = "http://www.w3.org/2001/04/xmldsig-more#sha384";
/*     */   public static final String ALGO_ID_DIGEST_SHA512 = "http://www.w3.org/2001/04/xmlenc#sha512";
/*     */   public static final String ALGO_ID_DIGEST_RIPEMD160 = "http://www.w3.org/2001/04/xmlenc#ripemd160";
/*  58 */   MessageDigest algorithm = null;
/*     */ 
/*  74 */   static ThreadLocal instances = new ThreadLocal() {
/*     */     protected Object initialValue() {
/*  76 */       return new HashMap();
/*     */     }
/*  74 */   };
/*     */ 
/*     */   private MessageDigestAlgorithm(Document paramDocument, MessageDigest paramMessageDigest, String paramString)
/*     */   {
/*  69 */     super(paramDocument, paramString);
/*     */ 
/*  71 */     this.algorithm = paramMessageDigest;
/*     */   }
/*     */ 
/*     */   public static MessageDigestAlgorithm getInstance(Document paramDocument, String paramString)
/*     */     throws XMLSignatureException
/*     */   {
/*  90 */     MessageDigest localMessageDigest = getDigestInstance(paramString);
/*  91 */     return new MessageDigestAlgorithm(paramDocument, localMessageDigest, paramString);
/*     */   }
/*     */ 
/*     */   private static MessageDigest getDigestInstance(String paramString) throws XMLSignatureException {
/*  95 */     MessageDigest localMessageDigest = (MessageDigest)((Map)instances.get()).get(paramString);
/*  96 */     if (localMessageDigest != null)
/*  97 */       return localMessageDigest;
/*  98 */     String str1 = JCEMapper.translateURItoJCEID(paramString);
/*     */     Object localObject;
/* 100 */     if (str1 == null) {
/* 101 */       localObject = new Object[] { paramString };
/* 102 */       throw new XMLSignatureException("algorithms.NoSuchMap", (Object[])localObject);
/*     */     }
/*     */ 
/* 106 */     String str2 = JCEMapper.getProviderId();
/*     */     try {
/* 108 */       if (str2 == null)
/* 109 */         localObject = MessageDigest.getInstance(str1);
/*     */       else
/* 111 */         localObject = MessageDigest.getInstance(str1, str2);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 114 */       arrayOfObject = new Object[] { str1, localNoSuchAlgorithmException.getLocalizedMessage() };
/*     */ 
/* 117 */       throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject);
/*     */     } catch (NoSuchProviderException localNoSuchProviderException) {
/* 119 */       Object[] arrayOfObject = { str1, localNoSuchProviderException.getLocalizedMessage() };
/*     */ 
/* 122 */       throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject);
/*     */     }
/* 124 */     ((Map)instances.get()).put(paramString, localObject);
/* 125 */     return localObject;
/*     */   }
/*     */ 
/*     */   public MessageDigest getAlgorithm()
/*     */   {
/* 134 */     return this.algorithm;
/*     */   }
/*     */ 
/*     */   public static boolean isEqual(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/* 146 */     return MessageDigest.isEqual(paramArrayOfByte1, paramArrayOfByte2);
/*     */   }
/*     */ 
/*     */   public byte[] digest()
/*     */   {
/* 156 */     return this.algorithm.digest();
/*     */   }
/*     */ 
/*     */   public byte[] digest(byte[] paramArrayOfByte)
/*     */   {
/* 167 */     return this.algorithm.digest(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public int digest(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws DigestException
/*     */   {
/* 182 */     return this.algorithm.digest(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public String getJCEAlgorithmString()
/*     */   {
/* 192 */     return this.algorithm.getAlgorithm();
/*     */   }
/*     */ 
/*     */   public Provider getJCEProvider()
/*     */   {
/* 202 */     return this.algorithm.getProvider();
/*     */   }
/*     */ 
/*     */   public int getDigestLength()
/*     */   {
/* 212 */     return this.algorithm.getDigestLength();
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 221 */     this.algorithm.reset();
/*     */   }
/*     */ 
/*     */   public void update(byte[] paramArrayOfByte)
/*     */   {
/* 231 */     this.algorithm.update(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public void update(byte paramByte)
/*     */   {
/* 241 */     this.algorithm.update(paramByte);
/*     */   }
/*     */ 
/*     */   public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 253 */     this.algorithm.update(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public String getBaseNamespace()
/*     */   {
/* 258 */     return "http://www.w3.org/2000/09/xmldsig#";
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 263 */     return "DigestMethod";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.algorithms.MessageDigestAlgorithm
 * JD-Core Version:    0.6.2
 */