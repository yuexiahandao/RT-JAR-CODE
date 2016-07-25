/*     */ package javax.xml.crypto.dsig.keyinfo;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.KeyException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Provider;
/*     */ import java.security.PublicKey;
/*     */ import java.util.List;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.NoSuchMechanismException;
/*     */ import javax.xml.crypto.URIDereferencer;
/*     */ import javax.xml.crypto.XMLStructure;
/*     */ import sun.security.jca.GetInstance;
/*     */ import sun.security.jca.GetInstance.Instance;
/*     */ 
/*     */ public abstract class KeyInfoFactory
/*     */ {
/*     */   private String mechanismType;
/*     */   private Provider provider;
/*     */ 
/*     */   public static KeyInfoFactory getInstance(String paramString)
/*     */   {
/* 145 */     if (paramString == null)
/* 146 */       throw new NullPointerException("mechanismType cannot be null");
/*     */     GetInstance.Instance localInstance;
/*     */     try
/*     */     {
/* 150 */       localInstance = GetInstance.getInstance("KeyInfoFactory", null, paramString);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 153 */       throw new NoSuchMechanismException(localNoSuchAlgorithmException);
/*     */     }
/* 155 */     KeyInfoFactory localKeyInfoFactory = (KeyInfoFactory)localInstance.impl;
/* 156 */     localKeyInfoFactory.mechanismType = paramString;
/* 157 */     localKeyInfoFactory.provider = localInstance.provider;
/* 158 */     return localKeyInfoFactory;
/*     */   }
/*     */ 
/*     */   public static KeyInfoFactory getInstance(String paramString, Provider paramProvider)
/*     */   {
/* 184 */     if (paramString == null)
/* 185 */       throw new NullPointerException("mechanismType cannot be null");
/* 186 */     if (paramProvider == null) {
/* 187 */       throw new NullPointerException("provider cannot be null");
/*     */     }
/*     */     GetInstance.Instance localInstance;
/*     */     try
/*     */     {
/* 192 */       localInstance = GetInstance.getInstance("KeyInfoFactory", null, paramString, paramProvider);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 195 */       throw new NoSuchMechanismException(localNoSuchAlgorithmException);
/*     */     }
/* 197 */     KeyInfoFactory localKeyInfoFactory = (KeyInfoFactory)localInstance.impl;
/* 198 */     localKeyInfoFactory.mechanismType = paramString;
/* 199 */     localKeyInfoFactory.provider = localInstance.provider;
/* 200 */     return localKeyInfoFactory;
/*     */   }
/*     */ 
/*     */   public static KeyInfoFactory getInstance(String paramString1, String paramString2)
/*     */     throws NoSuchProviderException
/*     */   {
/* 230 */     if (paramString1 == null)
/* 231 */       throw new NullPointerException("mechanismType cannot be null");
/* 232 */     if (paramString2 == null)
/* 233 */       throw new NullPointerException("provider cannot be null");
/* 234 */     if (paramString2.length() == 0) {
/* 235 */       throw new NoSuchProviderException();
/*     */     }
/*     */     GetInstance.Instance localInstance;
/*     */     try
/*     */     {
/* 240 */       localInstance = GetInstance.getInstance("KeyInfoFactory", null, paramString1, paramString2);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 243 */       throw new NoSuchMechanismException(localNoSuchAlgorithmException);
/*     */     }
/* 245 */     KeyInfoFactory localKeyInfoFactory = (KeyInfoFactory)localInstance.impl;
/* 246 */     localKeyInfoFactory.mechanismType = paramString1;
/* 247 */     localKeyInfoFactory.provider = localInstance.provider;
/* 248 */     return localKeyInfoFactory;
/*     */   }
/*     */ 
/*     */   public static KeyInfoFactory getInstance()
/*     */   {
/* 272 */     return getInstance("DOM");
/*     */   }
/*     */ 
/*     */   public final String getMechanismType()
/*     */   {
/* 283 */     return this.mechanismType;
/*     */   }
/*     */ 
/*     */   public final Provider getProvider()
/*     */   {
/* 292 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public abstract KeyInfo newKeyInfo(List paramList);
/*     */ 
/*     */   public abstract KeyInfo newKeyInfo(List paramList, String paramString);
/*     */ 
/*     */   public abstract KeyName newKeyName(String paramString);
/*     */ 
/*     */   public abstract KeyValue newKeyValue(PublicKey paramPublicKey)
/*     */     throws KeyException;
/*     */ 
/*     */   public abstract PGPData newPGPData(byte[] paramArrayOfByte);
/*     */ 
/*     */   public abstract PGPData newPGPData(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, List paramList);
/*     */ 
/*     */   public abstract PGPData newPGPData(byte[] paramArrayOfByte, List paramList);
/*     */ 
/*     */   public abstract RetrievalMethod newRetrievalMethod(String paramString);
/*     */ 
/*     */   public abstract RetrievalMethod newRetrievalMethod(String paramString1, String paramString2, List paramList);
/*     */ 
/*     */   public abstract X509Data newX509Data(List paramList);
/*     */ 
/*     */   public abstract X509IssuerSerial newX509IssuerSerial(String paramString, BigInteger paramBigInteger);
/*     */ 
/*     */   public abstract boolean isFeatureSupported(String paramString);
/*     */ 
/*     */   public abstract URIDereferencer getURIDereferencer();
/*     */ 
/*     */   public abstract KeyInfo unmarshalKeyInfo(XMLStructure paramXMLStructure)
/*     */     throws MarshalException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
 * JD-Core Version:    0.6.2
 */