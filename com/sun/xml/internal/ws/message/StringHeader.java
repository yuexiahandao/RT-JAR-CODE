/*     */ package com.sun.xml.internal.ws.message;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
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
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ public class StringHeader extends AbstractHeaderImpl
/*     */ {
/*     */   protected final QName name;
/*     */   protected final String value;
/*  64 */   protected boolean mustUnderstand = false;
/*     */   protected SOAPVersion soapVersion;
/*     */   protected static final String MUST_UNDERSTAND = "mustUnderstand";
/*     */   protected static final String S12_MUST_UNDERSTAND_TRUE = "true";
/*     */   protected static final String S11_MUST_UNDERSTAND_TRUE = "1";
/*     */ 
/*     */   public StringHeader(@NotNull QName name, @NotNull String value)
/*     */   {
/*  68 */     assert (name != null);
/*  69 */     assert (value != null);
/*  70 */     this.name = name;
/*  71 */     this.value = value;
/*     */   }
/*     */ 
/*     */   public StringHeader(@NotNull QName name, @NotNull String value, @NotNull SOAPVersion soapVersion, boolean mustUnderstand) {
/*  75 */     this.name = name;
/*  76 */     this.value = value;
/*  77 */     this.soapVersion = soapVersion;
/*  78 */     this.mustUnderstand = mustUnderstand;
/*     */   }
/*     */   @NotNull
/*     */   public String getNamespaceURI() {
/*  82 */     return this.name.getNamespaceURI();
/*     */   }
/*     */   @NotNull
/*     */   public String getLocalPart() {
/*  86 */     return this.name.getLocalPart();
/*     */   }
/*     */   @Nullable
/*     */   public String getAttribute(@NotNull String nsUri, @NotNull String localName) {
/*  90 */     if ((this.mustUnderstand) && (this.soapVersion.nsUri.equals(nsUri)) && ("mustUnderstand".equals(localName))) {
/*  91 */       return getMustUnderstandLiteral(this.soapVersion);
/*     */     }
/*  93 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLStreamReader readHeader() throws XMLStreamException {
/*  97 */     MutableXMLStreamBuffer buf = new MutableXMLStreamBuffer();
/*  98 */     XMLStreamWriter w = buf.createFromXMLStreamWriter();
/*  99 */     writeTo(w);
/* 100 */     return buf.readAsXMLStreamReader();
/*     */   }
/*     */ 
/*     */   public void writeTo(XMLStreamWriter w) throws XMLStreamException {
/* 104 */     w.writeStartElement("", this.name.getLocalPart(), this.name.getNamespaceURI());
/* 105 */     w.writeDefaultNamespace(this.name.getNamespaceURI());
/* 106 */     if (this.mustUnderstand)
/*     */     {
/* 112 */       w.writeNamespace("S", this.soapVersion.nsUri);
/* 113 */       w.writeAttribute("S", this.soapVersion.nsUri, "mustUnderstand", getMustUnderstandLiteral(this.soapVersion));
/*     */     }
/*     */ 
/* 118 */     w.writeCharacters(this.value);
/* 119 */     w.writeEndElement();
/*     */   }
/*     */ 
/*     */   public void writeTo(SOAPMessage saaj) throws SOAPException {
/* 123 */     SOAPHeader header = saaj.getSOAPHeader();
/* 124 */     if (header == null)
/* 125 */       header = saaj.getSOAPPart().getEnvelope().addHeader();
/* 126 */     SOAPHeaderElement she = header.addHeaderElement(this.name);
/* 127 */     if (this.mustUnderstand) {
/* 128 */       she.setMustUnderstand(true);
/*     */     }
/* 130 */     she.addTextNode(this.value);
/*     */   }
/*     */ 
/*     */   public void writeTo(ContentHandler h, ErrorHandler errorHandler) throws SAXException {
/* 134 */     String nsUri = this.name.getNamespaceURI();
/* 135 */     String ln = this.name.getLocalPart();
/*     */ 
/* 137 */     h.startPrefixMapping("", nsUri);
/* 138 */     if (this.mustUnderstand) {
/* 139 */       AttributesImpl attributes = new AttributesImpl();
/* 140 */       attributes.addAttribute(this.soapVersion.nsUri, "mustUnderstand", "S:mustUnderstand", "CDATA", getMustUnderstandLiteral(this.soapVersion));
/* 141 */       h.startElement(nsUri, ln, ln, attributes);
/*     */     } else {
/* 143 */       h.startElement(nsUri, ln, ln, EMPTY_ATTS);
/*     */     }
/* 145 */     h.characters(this.value.toCharArray(), 0, this.value.length());
/* 146 */     h.endElement(nsUri, ln, ln);
/*     */   }
/*     */ 
/*     */   private static String getMustUnderstandLiteral(SOAPVersion sv) {
/* 150 */     if (sv == SOAPVersion.SOAP_12) {
/* 151 */       return "true";
/*     */     }
/* 153 */     return "1";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.StringHeader
 * JD-Core Version:    0.6.2
 */