/*     */ package com.sun.org.apache.xml.internal.security.algorithms;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class JCEMapper
/*     */ {
/*  40 */   private static Logger log = Logger.getLogger(JCEMapper.class.getName());
/*     */ 
/*  43 */   private static Map<String, Algorithm> algorithmsMap = new ConcurrentHashMap();
/*     */ 
/*  46 */   private static String providerName = null;
/*     */ 
/*     */   public static void register(String paramString, Algorithm paramAlgorithm)
/*     */   {
/*  57 */     JavaUtils.checkRegisterPermission();
/*  58 */     algorithmsMap.put(paramString, paramAlgorithm);
/*     */   }
/*     */ 
/*     */   public static void registerDefaultAlgorithms()
/*     */   {
/*  65 */     algorithmsMap.put("http://www.w3.org/2001/04/xmldsig-more#md5", new Algorithm("", "MD5", "MessageDigest"));
/*     */ 
/*  69 */     algorithmsMap.put("http://www.w3.org/2001/04/xmlenc#ripemd160", new Algorithm("", "RIPEMD160", "MessageDigest"));
/*     */ 
/*  73 */     algorithmsMap.put("http://www.w3.org/2000/09/xmldsig#sha1", new Algorithm("", "SHA-1", "MessageDigest"));
/*     */ 
/*  77 */     algorithmsMap.put("http://www.w3.org/2001/04/xmlenc#sha256", new Algorithm("", "SHA-256", "MessageDigest"));
/*     */ 
/*  81 */     algorithmsMap.put("http://www.w3.org/2001/04/xmldsig-more#sha384", new Algorithm("", "SHA-384", "MessageDigest"));
/*     */ 
/*  85 */     algorithmsMap.put("http://www.w3.org/2001/04/xmlenc#sha512", new Algorithm("", "SHA-512", "MessageDigest"));
/*     */ 
/*  89 */     algorithmsMap.put("http://www.w3.org/2000/09/xmldsig#dsa-sha1", new Algorithm("", "SHA1withDSA", "Signature"));
/*     */ 
/*  93 */     algorithmsMap.put("http://www.w3.org/2001/04/xmldsig-more#rsa-md5", new Algorithm("", "MD5withRSA", "Signature"));
/*     */ 
/*  97 */     algorithmsMap.put("http://www.w3.org/2001/04/xmldsig-more#rsa-ripemd160", new Algorithm("", "RIPEMD160withRSA", "Signature"));
/*     */ 
/* 101 */     algorithmsMap.put("http://www.w3.org/2000/09/xmldsig#rsa-sha1", new Algorithm("", "SHA1withRSA", "Signature"));
/*     */ 
/* 105 */     algorithmsMap.put("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256", new Algorithm("", "SHA256withRSA", "Signature"));
/*     */ 
/* 109 */     algorithmsMap.put("http://www.w3.org/2001/04/xmldsig-more#rsa-sha384", new Algorithm("", "SHA384withRSA", "Signature"));
/*     */ 
/* 113 */     algorithmsMap.put("http://www.w3.org/2001/04/xmldsig-more#rsa-sha512", new Algorithm("", "SHA512withRSA", "Signature"));
/*     */ 
/* 117 */     algorithmsMap.put("http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha1", new Algorithm("", "SHA1withECDSA", "Signature"));
/*     */ 
/* 121 */     algorithmsMap.put("http://www.w3.org/2001/04/xmldsig-more#hmac-md5", new Algorithm("", "HmacMD5", "Mac"));
/*     */ 
/* 125 */     algorithmsMap.put("http://www.w3.org/2001/04/xmldsig-more#hmac-ripemd160", new Algorithm("", "HMACRIPEMD160", "Mac"));
/*     */ 
/* 129 */     algorithmsMap.put("http://www.w3.org/2000/09/xmldsig#hmac-sha1", new Algorithm("", "HmacSHA1", "Mac"));
/*     */ 
/* 133 */     algorithmsMap.put("http://www.w3.org/2001/04/xmldsig-more#hmac-sha256", new Algorithm("", "HmacSHA256", "Mac"));
/*     */ 
/* 137 */     algorithmsMap.put("http://www.w3.org/2001/04/xmldsig-more#hmac-sha384", new Algorithm("", "HmacSHA384", "Mac"));
/*     */ 
/* 141 */     algorithmsMap.put("http://www.w3.org/2001/04/xmldsig-more#hmac-sha512", new Algorithm("", "HmacSHA512", "Mac"));
/*     */ 
/* 145 */     algorithmsMap.put("http://www.w3.org/2001/04/xmlenc#tripledes-cbc", new Algorithm("DESede", "DESede/CBC/ISO10126Padding", "BlockEncryption", 192));
/*     */ 
/* 149 */     algorithmsMap.put("http://www.w3.org/2001/04/xmlenc#aes128-cbc", new Algorithm("AES", "AES/CBC/ISO10126Padding", "BlockEncryption", 128));
/*     */ 
/* 153 */     algorithmsMap.put("http://www.w3.org/2001/04/xmlenc#aes192-cbc", new Algorithm("AES", "AES/CBC/ISO10126Padding", "BlockEncryption", 192));
/*     */ 
/* 157 */     algorithmsMap.put("http://www.w3.org/2001/04/xmlenc#aes256-cbc", new Algorithm("AES", "AES/CBC/ISO10126Padding", "BlockEncryption", 256));
/*     */ 
/* 161 */     algorithmsMap.put("http://www.w3.org/2001/04/xmlenc#rsa-1_5", new Algorithm("RSA", "RSA/ECB/PKCS1Padding", "KeyTransport"));
/*     */ 
/* 165 */     algorithmsMap.put("http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p", new Algorithm("RSA", "RSA/ECB/OAEPWithSHA1AndMGF1Padding", "KeyTransport"));
/*     */ 
/* 169 */     algorithmsMap.put("http://www.w3.org/2001/04/xmlenc#dh", new Algorithm("", "", "KeyAgreement"));
/*     */ 
/* 173 */     algorithmsMap.put("http://www.w3.org/2001/04/xmlenc#kw-tripledes", new Algorithm("DESede", "DESedeWrap", "SymmetricKeyWrap", 192));
/*     */ 
/* 177 */     algorithmsMap.put("http://www.w3.org/2001/04/xmlenc#kw-aes128", new Algorithm("AES", "AESWrap", "SymmetricKeyWrap", 128));
/*     */ 
/* 181 */     algorithmsMap.put("http://www.w3.org/2001/04/xmlenc#kw-aes192", new Algorithm("AES", "AESWrap", "SymmetricKeyWrap", 192));
/*     */ 
/* 185 */     algorithmsMap.put("http://www.w3.org/2001/04/xmlenc#kw-aes256", new Algorithm("AES", "AESWrap", "SymmetricKeyWrap", 256));
/*     */   }
/*     */ 
/*     */   public static String translateURItoJCEID(String paramString)
/*     */   {
/* 198 */     if (log.isLoggable(Level.FINE)) {
/* 199 */       log.log(Level.FINE, "Request for URI " + paramString);
/*     */     }
/*     */ 
/* 202 */     Algorithm localAlgorithm = (Algorithm)algorithmsMap.get(paramString);
/* 203 */     if (localAlgorithm != null) {
/* 204 */       return localAlgorithm.jceName;
/*     */     }
/* 206 */     return null;
/*     */   }
/*     */ 
/*     */   public static String getAlgorithmClassFromURI(String paramString)
/*     */   {
/* 215 */     if (log.isLoggable(Level.FINE)) {
/* 216 */       log.log(Level.FINE, "Request for URI " + paramString);
/*     */     }
/*     */ 
/* 219 */     Algorithm localAlgorithm = (Algorithm)algorithmsMap.get(paramString);
/* 220 */     if (localAlgorithm != null) {
/* 221 */       return localAlgorithm.algorithmClass;
/*     */     }
/* 223 */     return null;
/*     */   }
/*     */ 
/*     */   public static int getKeyLengthFromURI(String paramString)
/*     */   {
/* 233 */     if (log.isLoggable(Level.FINE)) {
/* 234 */       log.log(Level.FINE, "Request for URI " + paramString);
/*     */     }
/* 236 */     Algorithm localAlgorithm = (Algorithm)algorithmsMap.get(paramString);
/* 237 */     if (localAlgorithm != null) {
/* 238 */       return localAlgorithm.keyLength;
/*     */     }
/* 240 */     return 0;
/*     */   }
/*     */ 
/*     */   public static String getJCEKeyAlgorithmFromURI(String paramString)
/*     */   {
/* 250 */     if (log.isLoggable(Level.FINE)) {
/* 251 */       log.log(Level.FINE, "Request for URI " + paramString);
/*     */     }
/* 253 */     Algorithm localAlgorithm = (Algorithm)algorithmsMap.get(paramString);
/* 254 */     if (localAlgorithm != null) {
/* 255 */       return localAlgorithm.requiredKey;
/*     */     }
/* 257 */     return null;
/*     */   }
/*     */ 
/*     */   public static String getProviderId()
/*     */   {
/* 265 */     return providerName;
/*     */   }
/*     */ 
/*     */   public static void setProviderId(String paramString)
/*     */   {
/* 275 */     JavaUtils.checkRegisterPermission();
/* 276 */     providerName = paramString;
/*     */   }
/*     */ 
/*     */   public static class Algorithm
/*     */   {
/*     */     final String requiredKey;
/*     */     final String jceName;
/*     */     final String algorithmClass;
/*     */     final int keyLength;
/*     */ 
/*     */     public Algorithm(Element paramElement)
/*     */     {
/* 294 */       this.requiredKey = paramElement.getAttribute("RequiredKey");
/* 295 */       this.jceName = paramElement.getAttribute("JCEName");
/* 296 */       this.algorithmClass = paramElement.getAttribute("AlgorithmClass");
/* 297 */       if (paramElement.hasAttribute("KeyLength"))
/* 298 */         this.keyLength = Integer.parseInt(paramElement.getAttribute("KeyLength"));
/*     */       else
/* 300 */         this.keyLength = 0;
/*     */     }
/*     */ 
/*     */     public Algorithm(String paramString1, String paramString2)
/*     */     {
/* 305 */       this(paramString1, paramString2, null, 0);
/*     */     }
/*     */ 
/*     */     public Algorithm(String paramString1, String paramString2, String paramString3) {
/* 309 */       this(paramString1, paramString2, paramString3, 0);
/*     */     }
/*     */ 
/*     */     public Algorithm(String paramString1, String paramString2, int paramInt) {
/* 313 */       this(paramString1, paramString2, null, paramInt);
/*     */     }
/*     */ 
/*     */     public Algorithm(String paramString1, String paramString2, String paramString3, int paramInt) {
/* 317 */       this.requiredKey = paramString1;
/* 318 */       this.jceName = paramString2;
/* 319 */       this.algorithmClass = paramString3;
/* 320 */       this.keyLength = paramInt;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.algorithms.JCEMapper
 * JD-Core Version:    0.6.2
 */