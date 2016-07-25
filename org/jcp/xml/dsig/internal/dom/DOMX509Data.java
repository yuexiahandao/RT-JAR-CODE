/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.Base64;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.security.cert.CRLException;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509CRL;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.XMLStructure;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dsig.keyinfo.X509Data;
/*     */ import javax.xml.crypto.dsig.keyinfo.X509IssuerSerial;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public final class DOMX509Data extends DOMStructure
/*     */   implements X509Data
/*     */ {
/*     */   private final List content;
/*     */   private CertificateFactory cf;
/*     */ 
/*     */   public DOMX509Data(List paramList)
/*     */   {
/*  73 */     if (paramList == null) {
/*  74 */       throw new NullPointerException("content cannot be null");
/*     */     }
/*  76 */     ArrayList localArrayList = new ArrayList(paramList);
/*  77 */     if (localArrayList.isEmpty()) {
/*  78 */       throw new IllegalArgumentException("content cannot be empty");
/*     */     }
/*  80 */     int i = 0; for (int j = localArrayList.size(); i < j; i++) {
/*  81 */       Object localObject = localArrayList.get(i);
/*  82 */       if ((localObject instanceof String))
/*  83 */         new X500Principal((String)localObject);
/*  84 */       else if ((!(localObject instanceof byte[])) && (!(localObject instanceof X509Certificate)) && (!(localObject instanceof X509CRL)) && (!(localObject instanceof XMLStructure)))
/*     */       {
/*  88 */         throw new ClassCastException("content[" + i + "] is not a valid X509Data type");
/*     */       }
/*     */     }
/*     */ 
/*  92 */     this.content = Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   public DOMX509Data(Element paramElement)
/*     */     throws MarshalException
/*     */   {
/* 103 */     NodeList localNodeList = paramElement.getChildNodes();
/* 104 */     int i = localNodeList.getLength();
/* 105 */     ArrayList localArrayList = new ArrayList(i);
/* 106 */     for (int j = 0; j < i; j++) {
/* 107 */       Node localNode = localNodeList.item(j);
/*     */ 
/* 109 */       if (localNode.getNodeType() == 1)
/*     */       {
/* 113 */         Element localElement = (Element)localNode;
/* 114 */         String str = localElement.getLocalName();
/* 115 */         if (str.equals("X509Certificate"))
/* 116 */           localArrayList.add(unmarshalX509Certificate(localElement));
/* 117 */         else if (str.equals("X509IssuerSerial"))
/* 118 */           localArrayList.add(new DOMX509IssuerSerial(localElement));
/* 119 */         else if (str.equals("X509SubjectName"))
/* 120 */           localArrayList.add(localElement.getFirstChild().getNodeValue());
/* 121 */         else if (str.equals("X509SKI"))
/*     */           try {
/* 123 */             localArrayList.add(Base64.decode(localElement));
/*     */           } catch (Base64DecodingException localBase64DecodingException) {
/* 125 */             throw new MarshalException("cannot decode X509SKI", localBase64DecodingException);
/*     */           }
/* 127 */         else if (str.equals("X509CRL"))
/* 128 */           localArrayList.add(unmarshalX509CRL(localElement));
/*     */         else
/* 130 */           localArrayList.add(new javax.xml.crypto.dom.DOMStructure(localElement));
/*     */       }
/*     */     }
/* 133 */     this.content = Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   public List getContent() {
/* 137 */     return this.content;
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*     */   {
/* 142 */     Document localDocument = DOMUtils.getOwnerDocument(paramNode);
/*     */ 
/* 144 */     Element localElement = DOMUtils.createElement(localDocument, "X509Data", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 148 */     int i = 0; for (int j = this.content.size(); i < j; i++) {
/* 149 */       Object localObject = this.content.get(i);
/* 150 */       if ((localObject instanceof X509Certificate))
/* 151 */         marshalCert((X509Certificate)localObject, localElement, localDocument, paramString);
/* 152 */       else if ((localObject instanceof XMLStructure)) {
/* 153 */         if ((localObject instanceof X509IssuerSerial)) {
/* 154 */           ((DOMX509IssuerSerial)localObject).marshal(localElement, paramString, paramDOMCryptoContext);
/*     */         }
/*     */         else {
/* 157 */           javax.xml.crypto.dom.DOMStructure localDOMStructure = (javax.xml.crypto.dom.DOMStructure)localObject;
/*     */ 
/* 159 */           DOMUtils.appendChild(localElement, localDOMStructure.getNode());
/*     */         }
/* 161 */       } else if ((localObject instanceof byte[]))
/* 162 */         marshalSKI((byte[])localObject, localElement, localDocument, paramString);
/* 163 */       else if ((localObject instanceof String))
/* 164 */         marshalSubjectName((String)localObject, localElement, localDocument, paramString);
/* 165 */       else if ((localObject instanceof X509CRL)) {
/* 166 */         marshalCRL((X509CRL)localObject, localElement, localDocument, paramString);
/*     */       }
/*     */     }
/*     */ 
/* 170 */     paramNode.appendChild(localElement);
/*     */   }
/*     */ 
/*     */   private void marshalSKI(byte[] paramArrayOfByte, Node paramNode, Document paramDocument, String paramString)
/*     */   {
/* 176 */     Element localElement = DOMUtils.createElement(paramDocument, "X509SKI", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 178 */     localElement.appendChild(paramDocument.createTextNode(Base64.encode(paramArrayOfByte)));
/* 179 */     paramNode.appendChild(localElement);
/*     */   }
/*     */ 
/*     */   private void marshalSubjectName(String paramString1, Node paramNode, Document paramDocument, String paramString2)
/*     */   {
/* 185 */     Element localElement = DOMUtils.createElement(paramDocument, "X509SubjectName", "http://www.w3.org/2000/09/xmldsig#", paramString2);
/*     */ 
/* 187 */     localElement.appendChild(paramDocument.createTextNode(paramString1));
/* 188 */     paramNode.appendChild(localElement);
/*     */   }
/*     */ 
/*     */   private void marshalCert(X509Certificate paramX509Certificate, Node paramNode, Document paramDocument, String paramString)
/*     */     throws MarshalException
/*     */   {
/* 194 */     Element localElement = DOMUtils.createElement(paramDocument, "X509Certificate", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */     try
/*     */     {
/* 197 */       localElement.appendChild(paramDocument.createTextNode(Base64.encode(paramX509Certificate.getEncoded())));
/*     */     }
/*     */     catch (CertificateEncodingException localCertificateEncodingException) {
/* 200 */       throw new MarshalException("Error encoding X509Certificate", localCertificateEncodingException);
/*     */     }
/* 202 */     paramNode.appendChild(localElement);
/*     */   }
/*     */ 
/*     */   private void marshalCRL(X509CRL paramX509CRL, Node paramNode, Document paramDocument, String paramString)
/*     */     throws MarshalException
/*     */   {
/* 208 */     Element localElement = DOMUtils.createElement(paramDocument, "X509CRL", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */     try
/*     */     {
/* 211 */       localElement.appendChild(paramDocument.createTextNode(Base64.encode(paramX509CRL.getEncoded())));
/*     */     }
/*     */     catch (CRLException localCRLException) {
/* 214 */       throw new MarshalException("Error encoding X509CRL", localCRLException);
/*     */     }
/* 216 */     paramNode.appendChild(localElement);
/*     */   }
/*     */ 
/*     */   private X509Certificate unmarshalX509Certificate(Element paramElement) throws MarshalException
/*     */   {
/*     */     try {
/* 222 */       ByteArrayInputStream localByteArrayInputStream = unmarshalBase64Binary(paramElement);
/* 223 */       return (X509Certificate)this.cf.generateCertificate(localByteArrayInputStream);
/*     */     } catch (CertificateException localCertificateException) {
/* 225 */       throw new MarshalException("Cannot create X509Certificate", localCertificateException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private X509CRL unmarshalX509CRL(Element paramElement) throws MarshalException {
/*     */     try {
/* 231 */       ByteArrayInputStream localByteArrayInputStream = unmarshalBase64Binary(paramElement);
/* 232 */       return (X509CRL)this.cf.generateCRL(localByteArrayInputStream);
/*     */     } catch (CRLException localCRLException) {
/* 234 */       throw new MarshalException("Cannot create X509CRL", localCRLException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private ByteArrayInputStream unmarshalBase64Binary(Element paramElement) throws MarshalException
/*     */   {
/*     */     try {
/* 241 */       if (this.cf == null) {
/* 242 */         this.cf = CertificateFactory.getInstance("X.509");
/*     */       }
/* 244 */       return new ByteArrayInputStream(Base64.decode(paramElement));
/*     */     } catch (CertificateException localCertificateException) {
/* 246 */       throw new MarshalException("Cannot create CertificateFactory", localCertificateException);
/*     */     } catch (Base64DecodingException localBase64DecodingException) {
/* 248 */       throw new MarshalException("Cannot decode Base64-encoded val", localBase64DecodingException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 253 */     if (this == paramObject) {
/* 254 */       return true;
/*     */     }
/*     */ 
/* 257 */     if (!(paramObject instanceof X509Data)) {
/* 258 */       return false;
/*     */     }
/* 260 */     X509Data localX509Data = (X509Data)paramObject;
/*     */ 
/* 262 */     List localList = localX509Data.getContent();
/* 263 */     int i = this.content.size();
/* 264 */     if (i != localList.size()) {
/* 265 */       return false;
/*     */     }
/*     */ 
/* 268 */     for (int j = 0; j < i; j++) {
/* 269 */       Object localObject1 = this.content.get(j);
/* 270 */       Object localObject2 = localList.get(j);
/* 271 */       if ((localObject1 instanceof byte[])) {
/* 272 */         if ((!(localObject2 instanceof byte[])) || (!Arrays.equals((byte[])localObject1, (byte[])localObject2)))
/*     */         {
/* 274 */           return false;
/*     */         }
/*     */       }
/* 277 */       else if (!localObject1.equals(localObject2)) {
/* 278 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 283 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMX509Data
 * JD-Core Version:    0.6.2
 */