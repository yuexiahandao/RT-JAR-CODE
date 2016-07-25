/*     */ package com.sun.xml.internal.ws.api.addressing;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.message.AbstractHeaderImpl;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.SOAPEnvelope;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPHeader;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.soap.SOAPPart;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ final class EPRHeader extends AbstractHeaderImpl
/*     */ {
/*     */   private final String nsUri;
/*     */   private final String localName;
/*     */   private final WSEndpointReference epr;
/*     */ 
/*     */   EPRHeader(QName tagName, WSEndpointReference epr)
/*     */   {
/*  61 */     this.nsUri = tagName.getNamespaceURI();
/*  62 */     this.localName = tagName.getLocalPart();
/*  63 */     this.epr = epr;
/*     */   }
/*     */   @NotNull
/*     */   public String getNamespaceURI() {
/*  67 */     return this.nsUri;
/*     */   }
/*     */   @NotNull
/*     */   public String getLocalPart() {
/*  71 */     return this.localName;
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public String getAttribute(@NotNull String nsUri, @NotNull String localName) {
/*     */     try {
/*  77 */       XMLStreamReader sr = this.epr.read("EndpointReference");
/*  78 */       while (sr.getEventType() != 1) {
/*  79 */         sr.next();
/*     */       }
/*  81 */       return sr.getAttributeValue(nsUri, localName);
/*     */     }
/*     */     catch (XMLStreamException e) {
/*  84 */       throw new AssertionError(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public XMLStreamReader readHeader() throws XMLStreamException {
/*  89 */     return this.epr.read(this.localName);
/*     */   }
/*     */ 
/*     */   public void writeTo(XMLStreamWriter w) throws XMLStreamException {
/*  93 */     this.epr.writeTo(this.localName, w);
/*     */   }
/*     */ 
/*     */   public void writeTo(SOAPMessage saaj)
/*     */     throws SOAPException
/*     */   {
/*     */     try
/*     */     {
/* 101 */       Transformer t = XmlUtil.newTransformer();
/* 102 */       SOAPHeader header = saaj.getSOAPHeader();
/* 103 */       if (header == null)
/* 104 */         header = saaj.getSOAPPart().getEnvelope().addHeader();
/* 105 */       t.transform(this.epr.asSource(this.localName), new DOMResult(header));
/*     */     } catch (Exception e) {
/* 107 */       throw new SOAPException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
/* 112 */     this.epr.writeTo(this.localName, contentHandler, errorHandler, true);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.addressing.EPRHeader
 * JD-Core Version:    0.6.2
 */