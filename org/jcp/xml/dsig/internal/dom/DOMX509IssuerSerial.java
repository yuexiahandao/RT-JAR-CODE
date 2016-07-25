/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dsig.keyinfo.X509IssuerSerial;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public final class DOMX509IssuerSerial extends DOMStructure
/*     */   implements X509IssuerSerial
/*     */ {
/*     */   private final String issuerName;
/*     */   private final BigInteger serialNumber;
/*     */ 
/*     */   public DOMX509IssuerSerial(String paramString, BigInteger paramBigInteger)
/*     */   {
/*  64 */     if (paramString == null) {
/*  65 */       throw new NullPointerException("issuerName cannot be null");
/*     */     }
/*  67 */     if (paramBigInteger == null) {
/*  68 */       throw new NullPointerException("serialNumber cannot be null");
/*     */     }
/*     */ 
/*  71 */     new X500Principal(paramString);
/*  72 */     this.issuerName = paramString;
/*  73 */     this.serialNumber = paramBigInteger;
/*     */   }
/*     */ 
/*     */   public DOMX509IssuerSerial(Element paramElement)
/*     */   {
/*  82 */     Element localElement1 = DOMUtils.getFirstChildElement(paramElement);
/*  83 */     Element localElement2 = DOMUtils.getNextSiblingElement(localElement1);
/*  84 */     this.issuerName = localElement1.getFirstChild().getNodeValue();
/*  85 */     this.serialNumber = new BigInteger(localElement2.getFirstChild().getNodeValue());
/*     */   }
/*     */ 
/*     */   public String getIssuerName() {
/*  89 */     return this.issuerName;
/*     */   }
/*     */ 
/*     */   public BigInteger getSerialNumber() {
/*  93 */     return this.serialNumber;
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*     */   {
/*  98 */     Document localDocument = DOMUtils.getOwnerDocument(paramNode);
/*     */ 
/* 100 */     Element localElement1 = DOMUtils.createElement(localDocument, "X509IssuerSerial", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 102 */     Element localElement2 = DOMUtils.createElement(localDocument, "X509IssuerName", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 104 */     Element localElement3 = DOMUtils.createElement(localDocument, "X509SerialNumber", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 106 */     localElement2.appendChild(localDocument.createTextNode(this.issuerName));
/* 107 */     localElement3.appendChild(localDocument.createTextNode(this.serialNumber.toString()));
/* 108 */     localElement1.appendChild(localElement2);
/* 109 */     localElement1.appendChild(localElement3);
/* 110 */     paramNode.appendChild(localElement1);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 114 */     if (this == paramObject) {
/* 115 */       return true;
/*     */     }
/* 117 */     if (!(paramObject instanceof X509IssuerSerial)) {
/* 118 */       return false;
/*     */     }
/* 120 */     X509IssuerSerial localX509IssuerSerial = (X509IssuerSerial)paramObject;
/* 121 */     return (this.issuerName.equals(localX509IssuerSerial.getIssuerName())) && (this.serialNumber.equals(localX509IssuerSerial.getSerialNumber()));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMX509IssuerSerial
 * JD-Core Version:    0.6.2
 */