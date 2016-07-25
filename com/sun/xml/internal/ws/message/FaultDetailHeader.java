/*     */ package com.sun.xml.internal.ws.message;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.SOAPEnvelope;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPHeader;
/*     */ import javax.xml.soap.SOAPHeaderElement;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.soap.SOAPPart;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class FaultDetailHeader extends AbstractHeaderImpl
/*     */ {
/*     */   private AddressingVersion av;
/*     */   private String wrapper;
/*  52 */   private String problemValue = null;
/*     */ 
/*     */   public FaultDetailHeader(AddressingVersion av, String wrapper, QName problemHeader) {
/*  55 */     this.av = av;
/*  56 */     this.wrapper = wrapper;
/*  57 */     this.problemValue = problemHeader.toString();
/*     */   }
/*     */ 
/*     */   public FaultDetailHeader(AddressingVersion av, String wrapper, String problemValue) {
/*  61 */     this.av = av;
/*  62 */     this.wrapper = wrapper;
/*  63 */     this.problemValue = problemValue;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public String getNamespaceURI()
/*     */   {
/*  69 */     return this.av.nsUri;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public String getLocalPart()
/*     */   {
/*  75 */     return this.av.faultDetailTag.getLocalPart();
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public String getAttribute(@NotNull String nsUri, @NotNull String localName) {
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLStreamReader readHeader() throws XMLStreamException {
/*  84 */     MutableXMLStreamBuffer buf = new MutableXMLStreamBuffer();
/*  85 */     XMLStreamWriter w = buf.createFromXMLStreamWriter();
/*  86 */     writeTo(w);
/*  87 */     return buf.readAsXMLStreamReader();
/*     */   }
/*     */ 
/*     */   public void writeTo(XMLStreamWriter w) throws XMLStreamException {
/*  91 */     w.writeStartElement("", this.av.faultDetailTag.getLocalPart(), this.av.faultDetailTag.getNamespaceURI());
/*  92 */     w.writeDefaultNamespace(this.av.nsUri);
/*  93 */     w.writeStartElement("", this.wrapper, this.av.nsUri);
/*  94 */     w.writeCharacters(this.problemValue);
/*  95 */     w.writeEndElement();
/*  96 */     w.writeEndElement();
/*     */   }
/*     */ 
/*     */   public void writeTo(SOAPMessage saaj) throws SOAPException {
/* 100 */     SOAPHeader header = saaj.getSOAPHeader();
/* 101 */     if (header == null)
/* 102 */       header = saaj.getSOAPPart().getEnvelope().addHeader();
/* 103 */     SOAPHeaderElement she = header.addHeaderElement(this.av.faultDetailTag);
/* 104 */     she = header.addHeaderElement(new QName(this.av.nsUri, this.wrapper));
/* 105 */     she.addTextNode(this.problemValue);
/*     */   }
/*     */ 
/*     */   public void writeTo(ContentHandler h, ErrorHandler errorHandler) throws SAXException {
/* 109 */     String nsUri = this.av.nsUri;
/* 110 */     String ln = this.av.faultDetailTag.getLocalPart();
/*     */ 
/* 112 */     h.startPrefixMapping("", nsUri);
/* 113 */     h.startElement(nsUri, ln, ln, EMPTY_ATTS);
/* 114 */     h.startElement(nsUri, this.wrapper, this.wrapper, EMPTY_ATTS);
/* 115 */     h.characters(this.problemValue.toCharArray(), 0, this.problemValue.length());
/* 116 */     h.endElement(nsUri, this.wrapper, this.wrapper);
/* 117 */     h.endElement(nsUri, ln, ln);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.FaultDetailHeader
 * JD-Core Version:    0.6.2
 */