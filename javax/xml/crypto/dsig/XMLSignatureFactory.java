/*     */ package javax.xml.crypto.dsig;
/*     */ 
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Provider;
/*     */ import java.util.List;
/*     */ import javax.xml.crypto.Data;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.NoSuchMechanismException;
/*     */ import javax.xml.crypto.URIDereferencer;
/*     */ import javax.xml.crypto.XMLStructure;
/*     */ import javax.xml.crypto.dsig.keyinfo.KeyInfo;
/*     */ import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
/*     */ import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.DigestMethodParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.TransformParameterSpec;
/*     */ import sun.security.jca.GetInstance;
/*     */ import sun.security.jca.GetInstance.Instance;
/*     */ 
/*     */ public abstract class XMLSignatureFactory
/*     */ {
/*     */   private String mechanismType;
/*     */   private Provider provider;
/*     */ 
/*     */   public static XMLSignatureFactory getInstance(String paramString)
/*     */   {
/* 190 */     if (paramString == null)
/* 191 */       throw new NullPointerException("mechanismType cannot be null");
/*     */     GetInstance.Instance localInstance;
/*     */     try
/*     */     {
/* 195 */       localInstance = GetInstance.getInstance("XMLSignatureFactory", null, paramString);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 198 */       throw new NoSuchMechanismException(localNoSuchAlgorithmException);
/*     */     }
/* 200 */     XMLSignatureFactory localXMLSignatureFactory = (XMLSignatureFactory)localInstance.impl;
/* 201 */     localXMLSignatureFactory.mechanismType = paramString;
/* 202 */     localXMLSignatureFactory.provider = localInstance.provider;
/* 203 */     return localXMLSignatureFactory;
/*     */   }
/*     */ 
/*     */   public static XMLSignatureFactory getInstance(String paramString, Provider paramProvider)
/*     */   {
/* 229 */     if (paramString == null)
/* 230 */       throw new NullPointerException("mechanismType cannot be null");
/* 231 */     if (paramProvider == null) {
/* 232 */       throw new NullPointerException("provider cannot be null");
/*     */     }
/*     */     GetInstance.Instance localInstance;
/*     */     try
/*     */     {
/* 237 */       localInstance = GetInstance.getInstance("XMLSignatureFactory", null, paramString, paramProvider);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 240 */       throw new NoSuchMechanismException(localNoSuchAlgorithmException);
/*     */     }
/* 242 */     XMLSignatureFactory localXMLSignatureFactory = (XMLSignatureFactory)localInstance.impl;
/* 243 */     localXMLSignatureFactory.mechanismType = paramString;
/* 244 */     localXMLSignatureFactory.provider = localInstance.provider;
/* 245 */     return localXMLSignatureFactory;
/*     */   }
/*     */ 
/*     */   public static XMLSignatureFactory getInstance(String paramString1, String paramString2)
/*     */     throws NoSuchProviderException
/*     */   {
/* 275 */     if (paramString1 == null)
/* 276 */       throw new NullPointerException("mechanismType cannot be null");
/* 277 */     if (paramString2 == null)
/* 278 */       throw new NullPointerException("provider cannot be null");
/* 279 */     if (paramString2.length() == 0) {
/* 280 */       throw new NoSuchProviderException();
/*     */     }
/*     */     GetInstance.Instance localInstance;
/*     */     try
/*     */     {
/* 285 */       localInstance = GetInstance.getInstance("XMLSignatureFactory", null, paramString1, paramString2);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 288 */       throw new NoSuchMechanismException(localNoSuchAlgorithmException);
/*     */     }
/* 290 */     XMLSignatureFactory localXMLSignatureFactory = (XMLSignatureFactory)localInstance.impl;
/* 291 */     localXMLSignatureFactory.mechanismType = paramString1;
/* 292 */     localXMLSignatureFactory.provider = localInstance.provider;
/* 293 */     return localXMLSignatureFactory;
/*     */   }
/*     */ 
/*     */   public static XMLSignatureFactory getInstance()
/*     */   {
/* 318 */     return getInstance("DOM");
/*     */   }
/*     */ 
/*     */   public final String getMechanismType()
/*     */   {
/* 329 */     return this.mechanismType;
/*     */   }
/*     */ 
/*     */   public final Provider getProvider()
/*     */   {
/* 338 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public abstract XMLSignature newXMLSignature(SignedInfo paramSignedInfo, KeyInfo paramKeyInfo);
/*     */ 
/*     */   public abstract XMLSignature newXMLSignature(SignedInfo paramSignedInfo, KeyInfo paramKeyInfo, List paramList, String paramString1, String paramString2);
/*     */ 
/*     */   public abstract Reference newReference(String paramString, DigestMethod paramDigestMethod);
/*     */ 
/*     */   public abstract Reference newReference(String paramString1, DigestMethod paramDigestMethod, List paramList, String paramString2, String paramString3);
/*     */ 
/*     */   public abstract Reference newReference(String paramString1, DigestMethod paramDigestMethod, List paramList, String paramString2, String paramString3, byte[] paramArrayOfByte);
/*     */ 
/*     */   public abstract Reference newReference(String paramString1, DigestMethod paramDigestMethod, List paramList1, Data paramData, List paramList2, String paramString2, String paramString3);
/*     */ 
/*     */   public abstract SignedInfo newSignedInfo(CanonicalizationMethod paramCanonicalizationMethod, SignatureMethod paramSignatureMethod, List paramList);
/*     */ 
/*     */   public abstract SignedInfo newSignedInfo(CanonicalizationMethod paramCanonicalizationMethod, SignatureMethod paramSignatureMethod, List paramList, String paramString);
/*     */ 
/*     */   public abstract XMLObject newXMLObject(List paramList, String paramString1, String paramString2, String paramString3);
/*     */ 
/*     */   public abstract Manifest newManifest(List paramList);
/*     */ 
/*     */   public abstract Manifest newManifest(List paramList, String paramString);
/*     */ 
/*     */   public abstract SignatureProperty newSignatureProperty(List paramList, String paramString1, String paramString2);
/*     */ 
/*     */   public abstract SignatureProperties newSignatureProperties(List paramList, String paramString);
/*     */ 
/*     */   public abstract DigestMethod newDigestMethod(String paramString, DigestMethodParameterSpec paramDigestMethodParameterSpec)
/*     */     throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;
/*     */ 
/*     */   public abstract SignatureMethod newSignatureMethod(String paramString, SignatureMethodParameterSpec paramSignatureMethodParameterSpec)
/*     */     throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;
/*     */ 
/*     */   public abstract Transform newTransform(String paramString, TransformParameterSpec paramTransformParameterSpec)
/*     */     throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;
/*     */ 
/*     */   public abstract Transform newTransform(String paramString, XMLStructure paramXMLStructure)
/*     */     throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;
/*     */ 
/*     */   public abstract CanonicalizationMethod newCanonicalizationMethod(String paramString, C14NMethodParameterSpec paramC14NMethodParameterSpec)
/*     */     throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;
/*     */ 
/*     */   public abstract CanonicalizationMethod newCanonicalizationMethod(String paramString, XMLStructure paramXMLStructure)
/*     */     throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;
/*     */ 
/*     */   public final KeyInfoFactory getKeyInfoFactory()
/*     */   {
/* 736 */     return KeyInfoFactory.getInstance(getMechanismType(), getProvider());
/*     */   }
/*     */ 
/*     */   public abstract XMLSignature unmarshalXMLSignature(XMLValidateContext paramXMLValidateContext)
/*     */     throws MarshalException;
/*     */ 
/*     */   public abstract XMLSignature unmarshalXMLSignature(XMLStructure paramXMLStructure)
/*     */     throws MarshalException;
/*     */ 
/*     */   public abstract boolean isFeatureSupported(String paramString);
/*     */ 
/*     */   public abstract URIDereferencer getURIDereferencer();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.XMLSignatureFactory
 * JD-Core Version:    0.6.2
 */