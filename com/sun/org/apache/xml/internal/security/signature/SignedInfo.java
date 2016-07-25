/*     */ package com.sun.org.apache.xml.internal.security.signature;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.params.InclusiveNamespaces;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class SignedInfo extends Manifest
/*     */ {
/*  54 */   private SignatureAlgorithm _signatureAlgorithm = null;
/*     */ 
/*  57 */   private byte[] _c14nizedBytes = null;
/*     */   private Element c14nMethod;
/*     */   private Element signatureMethod;
/*     */ 
/*     */   public SignedInfo(Document paramDocument)
/*     */     throws XMLSecurityException
/*     */   {
/*  71 */     this(paramDocument, "http://www.w3.org/2000/09/xmldsig#dsa-sha1", "http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
/*     */   }
/*     */ 
/*     */   public SignedInfo(Document paramDocument, String paramString1, String paramString2)
/*     */     throws XMLSecurityException
/*     */   {
/*  89 */     this(paramDocument, paramString1, 0, paramString2);
/*     */   }
/*     */ 
/*     */   public SignedInfo(Document paramDocument, String paramString1, int paramInt, String paramString2)
/*     */     throws XMLSecurityException
/*     */   {
/* 107 */     super(paramDocument);
/*     */ 
/* 109 */     this.c14nMethod = XMLUtils.createElementInSignatureSpace(this._doc, "CanonicalizationMethod");
/*     */ 
/* 112 */     this.c14nMethod.setAttributeNS(null, "Algorithm", paramString2);
/*     */ 
/* 114 */     this._constructionElement.appendChild(this.c14nMethod);
/* 115 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */ 
/* 117 */     if (paramInt > 0) {
/* 118 */       this._signatureAlgorithm = new SignatureAlgorithm(this._doc, paramString1, paramInt);
/*     */     }
/*     */     else {
/* 121 */       this._signatureAlgorithm = new SignatureAlgorithm(this._doc, paramString1);
/*     */     }
/*     */ 
/* 125 */     this.signatureMethod = this._signatureAlgorithm.getElement();
/* 126 */     this._constructionElement.appendChild(this.signatureMethod);
/* 127 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public SignedInfo(Document paramDocument, Element paramElement1, Element paramElement2)
/*     */     throws XMLSecurityException
/*     */   {
/* 139 */     super(paramDocument);
/*     */ 
/* 141 */     this.c14nMethod = paramElement2;
/* 142 */     this._constructionElement.appendChild(this.c14nMethod);
/* 143 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */ 
/* 145 */     this._signatureAlgorithm = new SignatureAlgorithm(paramElement1, null);
/*     */ 
/* 148 */     this.signatureMethod = this._signatureAlgorithm.getElement();
/* 149 */     this._constructionElement.appendChild(this.signatureMethod);
/*     */ 
/* 151 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public SignedInfo(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/* 167 */     super(paramElement, paramString);
/*     */ 
/* 173 */     this.c14nMethod = XMLUtils.getNextElement(paramElement.getFirstChild());
/* 174 */     String str = getCanonicalizationMethodURI();
/* 175 */     if ((!str.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315")) && (!str.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments")) && (!str.equals("http://www.w3.org/2001/10/xml-exc-c14n#")) && (!str.equals("http://www.w3.org/2001/10/xml-exc-c14n#WithComments")))
/*     */     {
/*     */       try
/*     */       {
/* 182 */         Canonicalizer localCanonicalizer = Canonicalizer.getInstance(getCanonicalizationMethodURI());
/*     */ 
/* 185 */         this._c14nizedBytes = localCanonicalizer.canonicalizeSubtree(this._constructionElement);
/*     */ 
/* 187 */         DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
/*     */ 
/* 189 */         localDocumentBuilderFactory.setNamespaceAware(true);
/* 190 */         localDocumentBuilderFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE.booleanValue());
/*     */ 
/* 192 */         DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
/* 193 */         Document localDocument = localDocumentBuilder.parse(new ByteArrayInputStream(this._c14nizedBytes));
/*     */ 
/* 195 */         Node localNode = this._doc.importNode(localDocument.getDocumentElement(), true);
/*     */ 
/* 198 */         this._constructionElement.getParentNode().replaceChild(localNode, this._constructionElement);
/*     */ 
/* 201 */         this._constructionElement = ((Element)localNode);
/*     */       } catch (ParserConfigurationException localParserConfigurationException) {
/* 203 */         throw new XMLSecurityException("empty", localParserConfigurationException);
/*     */       } catch (IOException localIOException) {
/* 205 */         throw new XMLSecurityException("empty", localIOException);
/*     */       } catch (SAXException localSAXException) {
/* 207 */         throw new XMLSecurityException("empty", localSAXException);
/*     */       }
/*     */     }
/* 210 */     this.signatureMethod = XMLUtils.getNextElement(this.c14nMethod.getNextSibling());
/* 211 */     this._signatureAlgorithm = new SignatureAlgorithm(this.signatureMethod, getBaseURI());
/*     */   }
/*     */ 
/*     */   public boolean verify()
/*     */     throws MissingResourceFailureException, XMLSecurityException
/*     */   {
/* 224 */     return super.verifyReferences(false);
/*     */   }
/*     */ 
/*     */   public boolean verify(boolean paramBoolean)
/*     */     throws MissingResourceFailureException, XMLSecurityException
/*     */   {
/* 237 */     return super.verifyReferences(paramBoolean);
/*     */   }
/*     */ 
/*     */   public byte[] getCanonicalizedOctetStream()
/*     */     throws CanonicalizationException, InvalidCanonicalizerException, XMLSecurityException
/*     */   {
/* 252 */     if (this._c14nizedBytes == null)
/*     */     {
/* 254 */       localObject = Canonicalizer.getInstance(getCanonicalizationMethodURI());
/*     */ 
/* 257 */       this._c14nizedBytes = ((Canonicalizer)localObject).canonicalizeSubtree(this._constructionElement);
/*     */     }
/*     */ 
/* 262 */     Object localObject = new byte[this._c14nizedBytes.length];
/*     */ 
/* 264 */     System.arraycopy(this._c14nizedBytes, 0, localObject, 0, localObject.length);
/*     */ 
/* 266 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void signInOctectStream(OutputStream paramOutputStream)
/*     */     throws CanonicalizationException, InvalidCanonicalizerException, XMLSecurityException
/*     */   {
/* 280 */     if (this._c14nizedBytes == null) {
/* 281 */       Canonicalizer localCanonicalizer = Canonicalizer.getInstance(getCanonicalizationMethodURI());
/*     */ 
/* 283 */       localCanonicalizer.setWriter(paramOutputStream);
/* 284 */       String str = getInclusiveNamespaces();
/*     */ 
/* 286 */       if (str == null)
/* 287 */         localCanonicalizer.canonicalizeSubtree(this._constructionElement);
/*     */       else
/* 289 */         localCanonicalizer.canonicalizeSubtree(this._constructionElement, str);
/*     */     } else {
/*     */       try {
/* 292 */         paramOutputStream.write(this._c14nizedBytes);
/*     */       } catch (IOException localIOException) {
/* 294 */         throw new RuntimeException("" + localIOException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getCanonicalizationMethodURI()
/*     */   {
/* 307 */     return this.c14nMethod.getAttributeNS(null, "Algorithm");
/*     */   }
/*     */ 
/*     */   public String getSignatureMethodURI()
/*     */   {
/* 317 */     Element localElement = getSignatureMethodElement();
/*     */ 
/* 319 */     if (localElement != null) {
/* 320 */       return localElement.getAttributeNS(null, "Algorithm");
/*     */     }
/*     */ 
/* 323 */     return null;
/*     */   }
/*     */ 
/*     */   public Element getSignatureMethodElement()
/*     */   {
/* 332 */     return this.signatureMethod;
/*     */   }
/*     */ 
/*     */   public SecretKey createSecretKey(byte[] paramArrayOfByte)
/*     */   {
/* 345 */     return new SecretKeySpec(paramArrayOfByte, this._signatureAlgorithm.getJCEAlgorithmString());
/*     */   }
/*     */ 
/*     */   protected SignatureAlgorithm getSignatureAlgorithm()
/*     */   {
/* 351 */     return this._signatureAlgorithm;
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 359 */     return "SignedInfo";
/*     */   }
/*     */ 
/*     */   public String getInclusiveNamespaces()
/*     */   {
/* 366 */     String str1 = this.c14nMethod.getAttributeNS(null, "Algorithm");
/* 367 */     if ((!str1.equals("http://www.w3.org/2001/10/xml-exc-c14n#")) && (!str1.equals("http://www.w3.org/2001/10/xml-exc-c14n#WithComments")))
/*     */     {
/* 369 */       return null;
/*     */     }
/*     */ 
/* 372 */     Element localElement = XMLUtils.getNextElement(this.c14nMethod.getFirstChild());
/*     */ 
/* 375 */     if (localElement != null)
/*     */     {
/*     */       try
/*     */       {
/* 379 */         return new InclusiveNamespaces(localElement, "http://www.w3.org/2001/10/xml-exc-c14n#").getInclusiveNamespaces();
/*     */       }
/*     */       catch (XMLSecurityException localXMLSecurityException)
/*     */       {
/* 385 */         return null;
/*     */       }
/*     */     }
/* 388 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.signature.SignedInfo
 * JD-Core Version:    0.6.2
 */