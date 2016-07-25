/*     */ package com.sun.org.apache.xml.internal.security.signature;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.keys.KeyInfo;
/*     */ import com.sun.org.apache.xml.internal.security.keys.content.X509Data;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.Transforms;
/*     */ import com.sun.org.apache.xml.internal.security.utils.Base64;
/*     */ import com.sun.org.apache.xml.internal.security.utils.I18n;
/*     */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*     */ import com.sun.org.apache.xml.internal.security.utils.SignerOutputStream;
/*     */ import com.sun.org.apache.xml.internal.security.utils.UnsyncBufferedOutputStream;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.Key;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.crypto.SecretKey;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ public final class XMLSignature extends SignatureElementProxy
/*     */ {
/*  85 */   static Logger log = Logger.getLogger(XMLSignature.class.getName());
/*     */   public static final String ALGO_ID_MAC_HMAC_SHA1 = "http://www.w3.org/2000/09/xmldsig#hmac-sha1";
/*     */   public static final String ALGO_ID_SIGNATURE_DSA = "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
/*     */   public static final String ALGO_ID_SIGNATURE_RSA = "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
/*     */   public static final String ALGO_ID_SIGNATURE_RSA_SHA1 = "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
/*     */   public static final String ALGO_ID_SIGNATURE_NOT_RECOMMENDED_RSA_MD5 = "http://www.w3.org/2001/04/xmldsig-more#rsa-md5";
/*     */   public static final String ALGO_ID_SIGNATURE_RSA_RIPEMD160 = "http://www.w3.org/2001/04/xmldsig-more#rsa-ripemd160";
/*     */   public static final String ALGO_ID_SIGNATURE_RSA_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
/*     */   public static final String ALGO_ID_SIGNATURE_RSA_SHA384 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha384";
/*     */   public static final String ALGO_ID_SIGNATURE_RSA_SHA512 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512";
/*     */   public static final String ALGO_ID_MAC_HMAC_NOT_RECOMMENDED_MD5 = "http://www.w3.org/2001/04/xmldsig-more#hmac-md5";
/*     */   public static final String ALGO_ID_MAC_HMAC_RIPEMD160 = "http://www.w3.org/2001/04/xmldsig-more#hmac-ripemd160";
/*     */   public static final String ALGO_ID_MAC_HMAC_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#hmac-sha256";
/*     */   public static final String ALGO_ID_MAC_HMAC_SHA384 = "http://www.w3.org/2001/04/xmldsig-more#hmac-sha384";
/*     */   public static final String ALGO_ID_MAC_HMAC_SHA512 = "http://www.w3.org/2001/04/xmldsig-more#hmac-sha512";
/*     */   public static final String ALGO_ID_SIGNATURE_ECDSA_SHA1 = "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha1";
/* 127 */   private SignedInfo _signedInfo = null;
/*     */ 
/* 130 */   private KeyInfo _keyInfo = null;
/*     */ 
/* 137 */   private boolean _followManifestsDuringValidation = false;
/*     */   private Element signatureValueElement;
/*     */ 
/*     */   public XMLSignature(Document paramDocument, String paramString1, String paramString2)
/*     */     throws XMLSecurityException
/*     */   {
/* 155 */     this(paramDocument, paramString1, paramString2, 0, "http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
/*     */   }
/*     */ 
/*     */   public XMLSignature(Document paramDocument, String paramString1, String paramString2, int paramInt)
/*     */     throws XMLSecurityException
/*     */   {
/* 171 */     this(paramDocument, paramString1, paramString2, paramInt, "http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
/*     */   }
/*     */ 
/*     */   public XMLSignature(Document paramDocument, String paramString1, String paramString2, String paramString3)
/*     */     throws XMLSecurityException
/*     */   {
/* 187 */     this(paramDocument, paramString1, paramString2, 0, paramString3);
/*     */   }
/*     */ 
/*     */   public XMLSignature(Document paramDocument, String paramString1, String paramString2, int paramInt, String paramString3)
/*     */     throws XMLSecurityException
/*     */   {
/* 204 */     super(paramDocument);
/*     */ 
/* 206 */     String str = getDefaultPrefix("http://www.w3.org/2000/09/xmldsig#");
/* 207 */     if (str == null) {
/* 208 */       this._constructionElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.w3.org/2000/09/xmldsig#");
/*     */     }
/*     */     else {
/* 211 */       this._constructionElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + str, "http://www.w3.org/2000/09/xmldsig#");
/*     */     }
/*     */ 
/* 214 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */ 
/* 216 */     this._baseURI = paramString1;
/* 217 */     this._signedInfo = new SignedInfo(this._doc, paramString2, paramInt, paramString3);
/*     */ 
/* 221 */     this._constructionElement.appendChild(this._signedInfo.getElement());
/* 222 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */ 
/* 225 */     this.signatureValueElement = XMLUtils.createElementInSignatureSpace(this._doc, "SignatureValue");
/*     */ 
/* 229 */     this._constructionElement.appendChild(this.signatureValueElement);
/* 230 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public XMLSignature(Document paramDocument, String paramString, Element paramElement1, Element paramElement2)
/*     */     throws XMLSecurityException
/*     */   {
/* 244 */     super(paramDocument);
/*     */ 
/* 246 */     String str = getDefaultPrefix("http://www.w3.org/2000/09/xmldsig#");
/* 247 */     if (str == null) {
/* 248 */       this._constructionElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.w3.org/2000/09/xmldsig#");
/*     */     }
/*     */     else {
/* 251 */       this._constructionElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + str, "http://www.w3.org/2000/09/xmldsig#");
/*     */     }
/*     */ 
/* 254 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */ 
/* 256 */     this._baseURI = paramString;
/* 257 */     this._signedInfo = new SignedInfo(this._doc, paramElement1, paramElement2);
/*     */ 
/* 259 */     this._constructionElement.appendChild(this._signedInfo.getElement());
/* 260 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */ 
/* 263 */     this.signatureValueElement = XMLUtils.createElementInSignatureSpace(this._doc, "SignatureValue");
/*     */ 
/* 267 */     this._constructionElement.appendChild(this.signatureValueElement);
/* 268 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public XMLSignature(Element paramElement, String paramString)
/*     */     throws XMLSignatureException, XMLSecurityException
/*     */   {
/* 283 */     super(paramElement, paramString);
/*     */ 
/* 286 */     Element localElement1 = XMLUtils.getNextElement(paramElement.getFirstChild());
/*     */ 
/* 290 */     if (localElement1 == null) {
/* 291 */       localObject = new Object[] { "SignedInfo", "Signature" };
/*     */ 
/* 294 */       throw new XMLSignatureException("xml.WrongContent", (Object[])localObject);
/*     */     }
/*     */ 
/* 298 */     this._signedInfo = new SignedInfo(localElement1, paramString);
/*     */ 
/* 301 */     this.signatureValueElement = XMLUtils.getNextElement(localElement1.getNextSibling());
/*     */ 
/* 305 */     if (this.signatureValueElement == null) {
/* 306 */       localObject = new Object[] { "SignatureValue", "Signature" };
/*     */ 
/* 309 */       throw new XMLSignatureException("xml.WrongContent", (Object[])localObject);
/*     */     }
/* 311 */     Object localObject = this.signatureValueElement.getAttributeNodeNS(null, "Id");
/* 312 */     if (localObject != null) {
/* 313 */       this.signatureValueElement.setIdAttributeNode((Attr)localObject, true);
/*     */     }
/*     */ 
/* 317 */     Element localElement2 = XMLUtils.getNextElement(this.signatureValueElement.getNextSibling());
/*     */ 
/* 321 */     if ((localElement2 != null) && (localElement2.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")) && (localElement2.getLocalName().equals("KeyInfo")))
/*     */     {
/* 323 */       this._keyInfo = new KeyInfo(localElement2, paramString);
/*     */     }
/*     */ 
/* 327 */     Element localElement3 = XMLUtils.getNextElement(this.signatureValueElement.getNextSibling());
/*     */ 
/* 329 */     while (localElement3 != null) {
/* 330 */       Attr localAttr = localElement3.getAttributeNodeNS(null, "Id");
/* 331 */       if (localAttr != null) {
/* 332 */         localElement3.setIdAttributeNode(localAttr, true);
/*     */       }
/*     */ 
/* 335 */       NodeList localNodeList = localElement3.getChildNodes();
/* 336 */       int i = localNodeList.getLength();
/*     */ 
/* 338 */       for (int j = 0; j < i; j++) {
/* 339 */         Node localNode = localNodeList.item(j);
/* 340 */         if (localNode.getNodeType() == 1) {
/* 341 */           Element localElement4 = (Element)localNode;
/* 342 */           String str = localElement4.getLocalName();
/* 343 */           if (str.equals("Manifest"))
/* 344 */             new Manifest(localElement4, paramString);
/* 345 */           else if (str.equals("SignatureProperties")) {
/* 346 */             new SignatureProperties(localElement4, paramString);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 351 */       localElement3 = XMLUtils.getNextElement(localElement3.getNextSibling());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setId(String paramString)
/*     */   {
/* 362 */     if (paramString != null)
/* 363 */       setLocalIdAttribute("Id", paramString);
/*     */   }
/*     */ 
/*     */   public String getId()
/*     */   {
/* 373 */     return this._constructionElement.getAttributeNS(null, "Id");
/*     */   }
/*     */ 
/*     */   public SignedInfo getSignedInfo()
/*     */   {
/* 382 */     return this._signedInfo;
/*     */   }
/*     */ 
/*     */   public byte[] getSignatureValue()
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 395 */       return Base64.decode(this.signatureValueElement);
/*     */     }
/*     */     catch (Base64DecodingException localBase64DecodingException)
/*     */     {
/* 399 */       throw new XMLSignatureException("empty", localBase64DecodingException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setSignatureValueElement(byte[] paramArrayOfByte)
/*     */   {
/* 411 */     while (this.signatureValueElement.hasChildNodes()) {
/* 412 */       this.signatureValueElement.removeChild(this.signatureValueElement.getFirstChild());
/*     */     }
/*     */ 
/* 416 */     String str = Base64.encode(paramArrayOfByte);
/*     */ 
/* 418 */     if ((str.length() > 76) && (!XMLUtils.ignoreLineBreaks())) {
/* 419 */       str = "\n" + str + "\n";
/*     */     }
/*     */ 
/* 422 */     Text localText = this._doc.createTextNode(str);
/* 423 */     this.signatureValueElement.appendChild(localText);
/*     */   }
/*     */ 
/*     */   public KeyInfo getKeyInfo()
/*     */   {
/* 437 */     if (this._keyInfo == null)
/*     */     {
/* 440 */       this._keyInfo = new KeyInfo(this._doc);
/*     */ 
/* 443 */       Element localElement1 = this._keyInfo.getElement();
/* 444 */       Element localElement2 = null;
/* 445 */       Node localNode = this._constructionElement.getFirstChild();
/* 446 */       localElement2 = XMLUtils.selectDsNode(localNode, "Object", 0);
/*     */ 
/* 448 */       if (localElement2 != null)
/*     */       {
/* 451 */         this._constructionElement.insertBefore(localElement1, localElement2);
/*     */ 
/* 453 */         XMLUtils.addReturnBeforeChild(this._constructionElement, localElement2);
/*     */       }
/*     */       else
/*     */       {
/* 457 */         this._constructionElement.appendChild(localElement1);
/* 458 */         XMLUtils.addReturnToElement(this._constructionElement);
/*     */       }
/*     */     }
/*     */ 
/* 462 */     return this._keyInfo;
/*     */   }
/*     */ 
/*     */   public void appendObject(ObjectContainer paramObjectContainer)
/*     */     throws XMLSignatureException
/*     */   {
/* 482 */     this._constructionElement.appendChild(paramObjectContainer.getElement());
/* 483 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public ObjectContainer getObjectItem(int paramInt)
/*     */   {
/* 498 */     Element localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "Object", paramInt);
/*     */     try
/*     */     {
/* 502 */       return new ObjectContainer(localElement, this._baseURI); } catch (XMLSecurityException localXMLSecurityException) {
/*     */     }
/* 504 */     return null;
/*     */   }
/*     */ 
/*     */   public int getObjectLength()
/*     */   {
/* 514 */     return length("http://www.w3.org/2000/09/xmldsig#", "Object");
/*     */   }
/*     */ 
/*     */   public void sign(Key paramKey)
/*     */     throws XMLSignatureException
/*     */   {
/* 526 */     if ((paramKey instanceof PublicKey)) {
/* 527 */       throw new IllegalArgumentException(I18n.translate("algorithms.operationOnlyVerification"));
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 534 */       SignedInfo localSignedInfo = getSignedInfo();
/* 535 */       SignatureAlgorithm localSignatureAlgorithm = localSignedInfo.getSignatureAlgorithm();
/*     */ 
/* 537 */       localSignatureAlgorithm.initSign(paramKey);
/*     */ 
/* 540 */       localSignedInfo.generateDigestValues();
/* 541 */       UnsyncBufferedOutputStream localUnsyncBufferedOutputStream = new UnsyncBufferedOutputStream(new SignerOutputStream(localSignatureAlgorithm));
/*     */       try {
/* 543 */         localUnsyncBufferedOutputStream.close();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/* 548 */       localSignedInfo.signInOctectStream(localUnsyncBufferedOutputStream);
/*     */ 
/* 550 */       byte[] arrayOfByte = localSignatureAlgorithm.sign();
/*     */ 
/* 553 */       setSignatureValueElement(arrayOfByte);
/*     */     }
/*     */     catch (CanonicalizationException localCanonicalizationException) {
/* 556 */       throw new XMLSignatureException("empty", localCanonicalizationException);
/*     */     } catch (InvalidCanonicalizerException localInvalidCanonicalizerException) {
/* 558 */       throw new XMLSignatureException("empty", localInvalidCanonicalizerException);
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/* 560 */       throw new XMLSignatureException("empty", localXMLSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addResourceResolver(ResourceResolver paramResourceResolver)
/*     */   {
/* 570 */     getSignedInfo().addResourceResolver(paramResourceResolver);
/*     */   }
/*     */ 
/*     */   public void addResourceResolver(ResourceResolverSpi paramResourceResolverSpi)
/*     */   {
/* 579 */     getSignedInfo().addResourceResolver(paramResourceResolverSpi);
/*     */   }
/*     */ 
/*     */   public boolean checkSignatureValue(X509Certificate paramX509Certificate)
/*     */     throws XMLSignatureException
/*     */   {
/* 596 */     if (paramX509Certificate != null)
/*     */     {
/* 599 */       return checkSignatureValue(paramX509Certificate.getPublicKey());
/*     */     }
/*     */ 
/* 602 */     Object[] arrayOfObject = { "Didn't get a certificate" };
/* 603 */     throw new XMLSignatureException("empty", arrayOfObject);
/*     */   }
/*     */ 
/*     */   public boolean checkSignatureValue(Key paramKey)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     Object localObject;
/* 620 */     if (paramKey == null) {
/* 621 */       localObject = new Object[] { "Didn't get a key" };
/*     */ 
/* 623 */       throw new XMLSignatureException("empty", (Object[])localObject);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 631 */       localObject = getSignedInfo();
/*     */ 
/* 634 */       SignatureAlgorithm localSignatureAlgorithm = ((SignedInfo)localObject).getSignatureAlgorithm();
/* 635 */       if (log.isLoggable(Level.FINE)) {
/* 636 */         log.log(Level.FINE, "SignatureMethodURI = " + localSignatureAlgorithm.getAlgorithmURI());
/* 637 */         log.log(Level.FINE, "jceSigAlgorithm    = " + localSignatureAlgorithm.getJCEAlgorithmString());
/* 638 */         log.log(Level.FINE, "jceSigProvider     = " + localSignatureAlgorithm.getJCEProviderName());
/* 639 */         log.log(Level.FINE, "PublicKey = " + paramKey);
/*     */       }
/* 641 */       localSignatureAlgorithm.initVerify(paramKey);
/*     */ 
/* 644 */       SignerOutputStream localSignerOutputStream = new SignerOutputStream(localSignatureAlgorithm);
/* 645 */       UnsyncBufferedOutputStream localUnsyncBufferedOutputStream = new UnsyncBufferedOutputStream(localSignerOutputStream);
/* 646 */       ((SignedInfo)localObject).signInOctectStream(localUnsyncBufferedOutputStream);
/*     */       try {
/* 648 */         localUnsyncBufferedOutputStream.close();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/*     */ 
/* 654 */       byte[] arrayOfByte = getSignatureValue();
/*     */ 
/* 658 */       if (!localSignatureAlgorithm.verify(arrayOfByte)) {
/* 659 */         log.log(Level.WARNING, "Signature verification failed.");
/* 660 */         return false;
/*     */       }
/*     */ 
/* 663 */       return ((SignedInfo)localObject).verify(this._followManifestsDuringValidation);
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/* 665 */       throw new XMLSignatureException("empty", localXMLSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addDocument(String paramString1, Transforms paramTransforms, String paramString2, String paramString3, String paramString4)
/*     */     throws XMLSignatureException
/*     */   {
/* 685 */     this._signedInfo.addDocument(this._baseURI, paramString1, paramTransforms, paramString2, paramString3, paramString4);
/*     */   }
/*     */ 
/*     */   public void addDocument(String paramString1, Transforms paramTransforms, String paramString2)
/*     */     throws XMLSignatureException
/*     */   {
/* 701 */     this._signedInfo.addDocument(this._baseURI, paramString1, paramTransforms, paramString2, null, null);
/*     */   }
/*     */ 
/*     */   public void addDocument(String paramString, Transforms paramTransforms)
/*     */     throws XMLSignatureException
/*     */   {
/* 715 */     this._signedInfo.addDocument(this._baseURI, paramString, paramTransforms, "http://www.w3.org/2000/09/xmldsig#sha1", null, null);
/*     */   }
/*     */ 
/*     */   public void addDocument(String paramString)
/*     */     throws XMLSignatureException
/*     */   {
/* 727 */     this._signedInfo.addDocument(this._baseURI, paramString, null, "http://www.w3.org/2000/09/xmldsig#sha1", null, null);
/*     */   }
/*     */ 
/*     */   public void addKeyInfo(X509Certificate paramX509Certificate)
/*     */     throws XMLSecurityException
/*     */   {
/* 740 */     X509Data localX509Data = new X509Data(this._doc);
/*     */ 
/* 742 */     localX509Data.addCertificate(paramX509Certificate);
/* 743 */     getKeyInfo().add(localX509Data);
/*     */   }
/*     */ 
/*     */   public void addKeyInfo(PublicKey paramPublicKey)
/*     */   {
/* 753 */     getKeyInfo().add(paramPublicKey);
/*     */   }
/*     */ 
/*     */   public SecretKey createSecretKey(byte[] paramArrayOfByte)
/*     */   {
/* 767 */     return getSignedInfo().createSecretKey(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public void setFollowNestedManifests(boolean paramBoolean)
/*     */   {
/* 780 */     this._followManifestsDuringValidation = paramBoolean;
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 789 */     return "Signature";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.signature.XMLSignature
 * JD-Core Version:    0.6.2
 */