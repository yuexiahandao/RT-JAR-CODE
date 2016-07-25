/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.List;
/*     */ import javax.xml.crypto.Data;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.URIDereferencer;
/*     */ import javax.xml.crypto.XMLStructure;
/*     */ import javax.xml.crypto.dom.DOMStructure;
/*     */ import javax.xml.crypto.dsig.CanonicalizationMethod;
/*     */ import javax.xml.crypto.dsig.DigestMethod;
/*     */ import javax.xml.crypto.dsig.Manifest;
/*     */ import javax.xml.crypto.dsig.Reference;
/*     */ import javax.xml.crypto.dsig.SignatureMethod;
/*     */ import javax.xml.crypto.dsig.SignatureProperties;
/*     */ import javax.xml.crypto.dsig.SignatureProperty;
/*     */ import javax.xml.crypto.dsig.SignedInfo;
/*     */ import javax.xml.crypto.dsig.Transform;
/*     */ import javax.xml.crypto.dsig.TransformService;
/*     */ import javax.xml.crypto.dsig.XMLObject;
/*     */ import javax.xml.crypto.dsig.XMLSignature;
/*     */ import javax.xml.crypto.dsig.XMLSignatureFactory;
/*     */ import javax.xml.crypto.dsig.XMLValidateContext;
/*     */ import javax.xml.crypto.dsig.dom.DOMValidateContext;
/*     */ import javax.xml.crypto.dsig.keyinfo.KeyInfo;
/*     */ import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.DigestMethodParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.TransformParameterSpec;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public final class DOMXMLSignatureFactory extends XMLSignatureFactory
/*     */ {
/*     */   public XMLSignature newXMLSignature(SignedInfo paramSignedInfo, KeyInfo paramKeyInfo)
/*     */   {
/*  56 */     return new DOMXMLSignature(paramSignedInfo, paramKeyInfo, null, null, null);
/*     */   }
/*     */ 
/*     */   public XMLSignature newXMLSignature(SignedInfo paramSignedInfo, KeyInfo paramKeyInfo, List paramList, String paramString1, String paramString2)
/*     */   {
/*  61 */     return new DOMXMLSignature(paramSignedInfo, paramKeyInfo, paramList, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public Reference newReference(String paramString, DigestMethod paramDigestMethod) {
/*  65 */     return newReference(paramString, paramDigestMethod, null, null, null);
/*     */   }
/*     */ 
/*     */   public Reference newReference(String paramString1, DigestMethod paramDigestMethod, List paramList, String paramString2, String paramString3)
/*     */   {
/*  70 */     return new DOMReference(paramString1, paramString2, paramDigestMethod, paramList, paramString3, getProvider());
/*     */   }
/*     */ 
/*     */   public Reference newReference(String paramString1, DigestMethod paramDigestMethod, List paramList1, Data paramData, List paramList2, String paramString2, String paramString3)
/*     */   {
/*  76 */     if (paramList1 == null) {
/*  77 */       throw new NullPointerException("appliedTransforms cannot be null");
/*     */     }
/*  79 */     if (paramList1.isEmpty()) {
/*  80 */       throw new NullPointerException("appliedTransforms cannot be empty");
/*     */     }
/*  82 */     if (paramData == null) {
/*  83 */       throw new NullPointerException("result cannot be null");
/*     */     }
/*  85 */     return new DOMReference(paramString1, paramString2, paramDigestMethod, paramList1, paramData, paramList2, paramString3, getProvider());
/*     */   }
/*     */ 
/*     */   public Reference newReference(String paramString1, DigestMethod paramDigestMethod, List paramList, String paramString2, String paramString3, byte[] paramArrayOfByte)
/*     */   {
/*  91 */     if (paramArrayOfByte == null) {
/*  92 */       throw new NullPointerException("digestValue cannot be null");
/*     */     }
/*  94 */     return new DOMReference(paramString1, paramString2, paramDigestMethod, null, null, paramList, paramString3, paramArrayOfByte, getProvider());
/*     */   }
/*     */ 
/*     */   public SignedInfo newSignedInfo(CanonicalizationMethod paramCanonicalizationMethod, SignatureMethod paramSignatureMethod, List paramList)
/*     */   {
/* 100 */     return newSignedInfo(paramCanonicalizationMethod, paramSignatureMethod, paramList, null);
/*     */   }
/*     */ 
/*     */   public SignedInfo newSignedInfo(CanonicalizationMethod paramCanonicalizationMethod, SignatureMethod paramSignatureMethod, List paramList, String paramString)
/*     */   {
/* 105 */     return new DOMSignedInfo(paramCanonicalizationMethod, paramSignatureMethod, paramList, paramString);
/*     */   }
/*     */ 
/*     */   public XMLObject newXMLObject(List paramList, String paramString1, String paramString2, String paramString3)
/*     */   {
/* 111 */     return new DOMXMLObject(paramList, paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   public Manifest newManifest(List paramList) {
/* 115 */     return newManifest(paramList, null);
/*     */   }
/*     */ 
/*     */   public Manifest newManifest(List paramList, String paramString) {
/* 119 */     return new DOMManifest(paramList, paramString);
/*     */   }
/*     */ 
/*     */   public SignatureProperties newSignatureProperties(List paramList, String paramString) {
/* 123 */     return new DOMSignatureProperties(paramList, paramString);
/*     */   }
/*     */ 
/*     */   public SignatureProperty newSignatureProperty(List paramList, String paramString1, String paramString2)
/*     */   {
/* 128 */     return new DOMSignatureProperty(paramList, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public XMLSignature unmarshalXMLSignature(XMLValidateContext paramXMLValidateContext)
/*     */     throws MarshalException
/*     */   {
/* 134 */     if (paramXMLValidateContext == null) {
/* 135 */       throw new NullPointerException("context cannot be null");
/*     */     }
/* 137 */     return unmarshal(((DOMValidateContext)paramXMLValidateContext).getNode(), paramXMLValidateContext);
/*     */   }
/*     */ 
/*     */   public XMLSignature unmarshalXMLSignature(XMLStructure paramXMLStructure)
/*     */     throws MarshalException
/*     */   {
/* 143 */     if (paramXMLStructure == null) {
/* 144 */       throw new NullPointerException("xmlStructure cannot be null");
/*     */     }
/* 146 */     return unmarshal(((DOMStructure)paramXMLStructure).getNode(), null);
/*     */   }
/*     */ 
/*     */   private XMLSignature unmarshal(Node paramNode, XMLValidateContext paramXMLValidateContext)
/*     */     throws MarshalException
/*     */   {
/* 154 */     paramNode.normalize();
/*     */ 
/* 156 */     Element localElement = null;
/* 157 */     if (paramNode.getNodeType() == 9)
/* 158 */       localElement = ((Document)paramNode).getDocumentElement();
/* 159 */     else if (paramNode.getNodeType() == 1)
/* 160 */       localElement = (Element)paramNode;
/*     */     else {
/* 162 */       throw new MarshalException("Signature element is not a proper Node");
/*     */     }
/*     */ 
/* 167 */     String str = localElement.getLocalName();
/* 168 */     if (str == null) {
/* 169 */       throw new MarshalException("Document implementation must support DOM Level 2 and be namespace aware");
/*     */     }
/*     */ 
/* 172 */     if (str.equals("Signature")) {
/* 173 */       return new DOMXMLSignature(localElement, paramXMLValidateContext, getProvider());
/*     */     }
/* 175 */     throw new MarshalException("invalid Signature tag: " + str);
/*     */   }
/*     */ 
/*     */   public boolean isFeatureSupported(String paramString)
/*     */   {
/* 180 */     if (paramString == null) {
/* 181 */       throw new NullPointerException();
/*     */     }
/* 183 */     return false;
/*     */   }
/*     */ 
/*     */   public DigestMethod newDigestMethod(String paramString, DigestMethodParameterSpec paramDigestMethodParameterSpec)
/*     */     throws NoSuchAlgorithmException, InvalidAlgorithmParameterException
/*     */   {
/* 190 */     if (paramString == null) {
/* 191 */       throw new NullPointerException();
/*     */     }
/* 193 */     if (paramString.equals("http://www.w3.org/2000/09/xmldsig#sha1"))
/* 194 */       return new DOMDigestMethod.SHA1(paramDigestMethodParameterSpec);
/* 195 */     if (paramString.equals("http://www.w3.org/2001/04/xmlenc#sha256"))
/* 196 */       return new DOMDigestMethod.SHA256(paramDigestMethodParameterSpec);
/* 197 */     if (paramString.equals("http://www.w3.org/2001/04/xmldsig-more#sha384"))
/* 198 */       return new DOMDigestMethod.SHA384(paramDigestMethodParameterSpec);
/* 199 */     if (paramString.equals("http://www.w3.org/2001/04/xmlenc#sha512")) {
/* 200 */       return new DOMDigestMethod.SHA512(paramDigestMethodParameterSpec);
/*     */     }
/* 202 */     throw new NoSuchAlgorithmException("unsupported algorithm");
/*     */   }
/*     */ 
/*     */   public SignatureMethod newSignatureMethod(String paramString, SignatureMethodParameterSpec paramSignatureMethodParameterSpec)
/*     */     throws NoSuchAlgorithmException, InvalidAlgorithmParameterException
/*     */   {
/* 209 */     if (paramString == null) {
/* 210 */       throw new NullPointerException();
/*     */     }
/* 212 */     if (paramString.equals("http://www.w3.org/2000/09/xmldsig#rsa-sha1"))
/* 213 */       return new DOMSignatureMethod.SHA1withRSA(paramSignatureMethodParameterSpec);
/* 214 */     if (paramString.equals("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"))
/* 215 */       return new DOMSignatureMethod.SHA256withRSA(paramSignatureMethodParameterSpec);
/* 216 */     if (paramString.equals("http://www.w3.org/2001/04/xmldsig-more#rsa-sha384"))
/* 217 */       return new DOMSignatureMethod.SHA384withRSA(paramSignatureMethodParameterSpec);
/* 218 */     if (paramString.equals("http://www.w3.org/2001/04/xmldsig-more#rsa-sha512"))
/* 219 */       return new DOMSignatureMethod.SHA512withRSA(paramSignatureMethodParameterSpec);
/* 220 */     if (paramString.equals("http://www.w3.org/2000/09/xmldsig#dsa-sha1"))
/* 221 */       return new DOMSignatureMethod.SHA1withDSA(paramSignatureMethodParameterSpec);
/* 222 */     if (paramString.equals("http://www.w3.org/2000/09/xmldsig#hmac-sha1"))
/* 223 */       return new DOMHMACSignatureMethod.SHA1(paramSignatureMethodParameterSpec);
/* 224 */     if (paramString.equals("http://www.w3.org/2001/04/xmldsig-more#hmac-sha256"))
/* 225 */       return new DOMHMACSignatureMethod.SHA256(paramSignatureMethodParameterSpec);
/* 226 */     if (paramString.equals("http://www.w3.org/2001/04/xmldsig-more#hmac-sha384"))
/* 227 */       return new DOMHMACSignatureMethod.SHA384(paramSignatureMethodParameterSpec);
/* 228 */     if (paramString.equals("http://www.w3.org/2001/04/xmldsig-more#hmac-sha512")) {
/* 229 */       return new DOMHMACSignatureMethod.SHA512(paramSignatureMethodParameterSpec);
/*     */     }
/* 231 */     throw new NoSuchAlgorithmException("unsupported algorithm");
/*     */   }
/*     */ 
/*     */   public Transform newTransform(String paramString, TransformParameterSpec paramTransformParameterSpec)
/*     */     throws NoSuchAlgorithmException, InvalidAlgorithmParameterException
/*     */   {
/*     */     TransformService localTransformService;
/*     */     try
/*     */     {
/* 240 */       localTransformService = TransformService.getInstance(paramString, "DOM");
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 242 */       localTransformService = TransformService.getInstance(paramString, "DOM", getProvider());
/*     */     }
/* 244 */     localTransformService.init(paramTransformParameterSpec);
/* 245 */     return new DOMTransform(localTransformService);
/*     */   }
/*     */ 
/*     */   public Transform newTransform(String paramString, XMLStructure paramXMLStructure) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException
/*     */   {
/*     */     TransformService localTransformService;
/*     */     try
/*     */     {
/* 253 */       localTransformService = TransformService.getInstance(paramString, "DOM");
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 255 */       localTransformService = TransformService.getInstance(paramString, "DOM", getProvider());
/*     */     }
/* 257 */     if (paramXMLStructure == null)
/* 258 */       localTransformService.init(null);
/*     */     else {
/* 260 */       localTransformService.init(paramXMLStructure, null);
/*     */     }
/* 262 */     return new DOMTransform(localTransformService);
/*     */   }
/*     */ 
/*     */   public CanonicalizationMethod newCanonicalizationMethod(String paramString, C14NMethodParameterSpec paramC14NMethodParameterSpec) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException
/*     */   {
/*     */     TransformService localTransformService;
/*     */     try
/*     */     {
/* 270 */       localTransformService = TransformService.getInstance(paramString, "DOM");
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 272 */       localTransformService = TransformService.getInstance(paramString, "DOM", getProvider());
/*     */     }
/* 274 */     localTransformService.init(paramC14NMethodParameterSpec);
/* 275 */     return new DOMCanonicalizationMethod(localTransformService);
/*     */   }
/*     */ 
/*     */   public CanonicalizationMethod newCanonicalizationMethod(String paramString, XMLStructure paramXMLStructure) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException
/*     */   {
/*     */     TransformService localTransformService;
/*     */     try
/*     */     {
/* 283 */       localTransformService = TransformService.getInstance(paramString, "DOM");
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 285 */       localTransformService = TransformService.getInstance(paramString, "DOM", getProvider());
/*     */     }
/* 287 */     if (paramXMLStructure == null)
/* 288 */       localTransformService.init(null);
/*     */     else {
/* 290 */       localTransformService.init(paramXMLStructure, null);
/*     */     }
/* 292 */     return new DOMCanonicalizationMethod(localTransformService);
/*     */   }
/*     */ 
/*     */   public URIDereferencer getURIDereferencer() {
/* 296 */     return DOMURIDereferencer.INSTANCE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMXMLSignatureFactory
 * JD-Core Version:    0.6.2
 */