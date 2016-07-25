/*     */ package com.sun.org.apache.xml.internal.security.keys.content;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509CRL;
/*     */ import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
/*     */ import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509IssuerSerial;
/*     */ import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SKI;
/*     */ import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SubjectName;
/*     */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.math.BigInteger;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class X509Data extends SignatureElementProxy
/*     */   implements KeyInfoContent
/*     */ {
/*  49 */   static Logger log = Logger.getLogger(X509Data.class.getName());
/*     */ 
/*     */   public X509Data(Document paramDocument)
/*     */   {
/*  59 */     super(paramDocument);
/*     */ 
/*  61 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public X509Data(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/*  74 */     super(paramElement, paramString);
/*  75 */     Node localNode = this._constructionElement.getFirstChild();
/*  76 */     while (localNode != null) {
/*  77 */       if (localNode.getNodeType() != 1) {
/*  78 */         localNode = localNode.getNextSibling();
/*     */       }
/*     */       else {
/*  81 */         return;
/*     */       }
/*     */     }
/*  84 */     Object[] arrayOfObject = { "Elements", "X509Data" };
/*  85 */     throw new XMLSecurityException("xml.WrongContent", arrayOfObject);
/*     */   }
/*     */ 
/*     */   public void addIssuerSerial(String paramString, BigInteger paramBigInteger)
/*     */   {
/*  96 */     add(new XMLX509IssuerSerial(this._doc, paramString, paramBigInteger));
/*     */   }
/*     */ 
/*     */   public void addIssuerSerial(String paramString1, String paramString2)
/*     */   {
/* 107 */     add(new XMLX509IssuerSerial(this._doc, paramString1, paramString2));
/*     */   }
/*     */ 
/*     */   public void addIssuerSerial(String paramString, int paramInt)
/*     */   {
/* 118 */     add(new XMLX509IssuerSerial(this._doc, paramString, paramInt));
/*     */   }
/*     */ 
/*     */   public void add(XMLX509IssuerSerial paramXMLX509IssuerSerial)
/*     */   {
/* 129 */     this._constructionElement.appendChild(paramXMLX509IssuerSerial.getElement());
/*     */ 
/* 131 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public void addSKI(byte[] paramArrayOfByte)
/*     */   {
/* 140 */     add(new XMLX509SKI(this._doc, paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public void addSKI(X509Certificate paramX509Certificate)
/*     */     throws XMLSecurityException
/*     */   {
/* 151 */     add(new XMLX509SKI(this._doc, paramX509Certificate));
/*     */   }
/*     */ 
/*     */   public void add(XMLX509SKI paramXMLX509SKI)
/*     */   {
/* 160 */     this._constructionElement.appendChild(paramXMLX509SKI.getElement());
/* 161 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public void addSubjectName(String paramString)
/*     */   {
/* 170 */     add(new XMLX509SubjectName(this._doc, paramString));
/*     */   }
/*     */ 
/*     */   public void addSubjectName(X509Certificate paramX509Certificate)
/*     */   {
/* 179 */     add(new XMLX509SubjectName(this._doc, paramX509Certificate));
/*     */   }
/*     */ 
/*     */   public void add(XMLX509SubjectName paramXMLX509SubjectName)
/*     */   {
/* 188 */     this._constructionElement.appendChild(paramXMLX509SubjectName.getElement());
/* 189 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public void addCertificate(X509Certificate paramX509Certificate)
/*     */     throws XMLSecurityException
/*     */   {
/* 200 */     add(new XMLX509Certificate(this._doc, paramX509Certificate));
/*     */   }
/*     */ 
/*     */   public void addCertificate(byte[] paramArrayOfByte)
/*     */   {
/* 209 */     add(new XMLX509Certificate(this._doc, paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public void add(XMLX509Certificate paramXMLX509Certificate)
/*     */   {
/* 218 */     this._constructionElement.appendChild(paramXMLX509Certificate.getElement());
/* 219 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public void addCRL(byte[] paramArrayOfByte)
/*     */   {
/* 228 */     add(new XMLX509CRL(this._doc, paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public void add(XMLX509CRL paramXMLX509CRL)
/*     */   {
/* 237 */     this._constructionElement.appendChild(paramXMLX509CRL.getElement());
/* 238 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public void addUnknownElement(Element paramElement)
/*     */   {
/* 247 */     this._constructionElement.appendChild(paramElement);
/* 248 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public int lengthIssuerSerial()
/*     */   {
/* 257 */     return length("http://www.w3.org/2000/09/xmldsig#", "X509IssuerSerial");
/*     */   }
/*     */ 
/*     */   public int lengthSKI()
/*     */   {
/* 267 */     return length("http://www.w3.org/2000/09/xmldsig#", "X509SKI");
/*     */   }
/*     */ 
/*     */   public int lengthSubjectName()
/*     */   {
/* 276 */     return length("http://www.w3.org/2000/09/xmldsig#", "X509SubjectName");
/*     */   }
/*     */ 
/*     */   public int lengthCertificate()
/*     */   {
/* 286 */     return length("http://www.w3.org/2000/09/xmldsig#", "X509Certificate");
/*     */   }
/*     */ 
/*     */   public int lengthCRL()
/*     */   {
/* 296 */     return length("http://www.w3.org/2000/09/xmldsig#", "X509CRL");
/*     */   }
/*     */ 
/*     */   public int lengthUnknownElement()
/*     */   {
/* 306 */     int i = 0;
/* 307 */     Node localNode = this._constructionElement.getFirstChild();
/* 308 */     while (localNode != null)
/*     */     {
/* 310 */       if ((localNode.getNodeType() == 1) && (!localNode.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")))
/*     */       {
/* 312 */         i++;
/*     */       }
/* 314 */       localNode = localNode.getNextSibling();
/*     */     }
/*     */ 
/* 317 */     return i;
/*     */   }
/*     */ 
/*     */   public XMLX509IssuerSerial itemIssuerSerial(int paramInt)
/*     */     throws XMLSecurityException
/*     */   {
/* 330 */     Element localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "X509IssuerSerial", paramInt);
/*     */ 
/* 334 */     if (localElement != null) {
/* 335 */       return new XMLX509IssuerSerial(localElement, this._baseURI);
/*     */     }
/* 337 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLX509SKI itemSKI(int paramInt)
/*     */     throws XMLSecurityException
/*     */   {
/* 349 */     Element localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "X509SKI", paramInt);
/*     */ 
/* 352 */     if (localElement != null) {
/* 353 */       return new XMLX509SKI(localElement, this._baseURI);
/*     */     }
/* 355 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLX509SubjectName itemSubjectName(int paramInt)
/*     */     throws XMLSecurityException
/*     */   {
/* 368 */     Element localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "X509SubjectName", paramInt);
/*     */ 
/* 371 */     if (localElement != null) {
/* 372 */       return new XMLX509SubjectName(localElement, this._baseURI);
/*     */     }
/* 374 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLX509Certificate itemCertificate(int paramInt)
/*     */     throws XMLSecurityException
/*     */   {
/* 387 */     Element localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "X509Certificate", paramInt);
/*     */ 
/* 390 */     if (localElement != null) {
/* 391 */       return new XMLX509Certificate(localElement, this._baseURI);
/*     */     }
/* 393 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLX509CRL itemCRL(int paramInt)
/*     */     throws XMLSecurityException
/*     */   {
/* 405 */     Element localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "X509CRL", paramInt);
/*     */ 
/* 408 */     if (localElement != null) {
/* 409 */       return new XMLX509CRL(localElement, this._baseURI);
/*     */     }
/* 411 */     return null;
/*     */   }
/*     */ 
/*     */   public Element itemUnknownElement(int paramInt)
/*     */   {
/* 422 */     log.log(Level.FINE, "itemUnknownElement not implemented:" + paramInt);
/* 423 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean containsIssuerSerial()
/*     */   {
/* 432 */     return lengthIssuerSerial() > 0;
/*     */   }
/*     */ 
/*     */   public boolean containsSKI()
/*     */   {
/* 441 */     return lengthSKI() > 0;
/*     */   }
/*     */ 
/*     */   public boolean containsSubjectName()
/*     */   {
/* 450 */     return lengthSubjectName() > 0;
/*     */   }
/*     */ 
/*     */   public boolean containsCertificate()
/*     */   {
/* 459 */     return lengthCertificate() > 0;
/*     */   }
/*     */ 
/*     */   public boolean containsCRL()
/*     */   {
/* 468 */     return lengthCRL() > 0;
/*     */   }
/*     */ 
/*     */   public boolean containsUnknownElement()
/*     */   {
/* 477 */     return lengthUnknownElement() > 0;
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 482 */     return "X509Data";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.content.X509Data
 * JD-Core Version:    0.6.2
 */