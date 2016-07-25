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
/*     */ public class ProblemActionHeader extends AbstractHeaderImpl
/*     */ {
/*     */ 
/*     */   @NotNull
/*     */   protected String action;
/*     */   protected String soapAction;
/*     */ 
/*     */   @NotNull
/*     */   protected AddressingVersion av;
/*     */   private static final String actionLocalName = "Action";
/*     */   private static final String soapActionLocalName = "SoapAction";
/*     */ 
/*     */   public ProblemActionHeader(@NotNull String action, @NotNull AddressingVersion av)
/*     */   {
/*  59 */     this(action, null, av);
/*     */   }
/*     */ 
/*     */   public ProblemActionHeader(@NotNull String action, String soapAction, @NotNull AddressingVersion av) {
/*  63 */     assert (action != null);
/*  64 */     assert (av != null);
/*  65 */     this.action = action;
/*  66 */     this.soapAction = soapAction;
/*  67 */     this.av = av;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public String getNamespaceURI()
/*     */   {
/*  73 */     return this.av.nsUri;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public String getLocalPart()
/*     */   {
/*  79 */     return "ProblemAction";
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public String getAttribute(@NotNull String nsUri, @NotNull String localName) {
/*  84 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLStreamReader readHeader() throws XMLStreamException {
/*  88 */     MutableXMLStreamBuffer buf = new MutableXMLStreamBuffer();
/*  89 */     XMLStreamWriter w = buf.createFromXMLStreamWriter();
/*  90 */     writeTo(w);
/*  91 */     return buf.readAsXMLStreamReader();
/*     */   }
/*     */ 
/*     */   public void writeTo(XMLStreamWriter w) throws XMLStreamException {
/*  95 */     w.writeStartElement("", getLocalPart(), getNamespaceURI());
/*  96 */     w.writeDefaultNamespace(getNamespaceURI());
/*  97 */     w.writeStartElement("Action");
/*  98 */     w.writeCharacters(this.action);
/*  99 */     w.writeEndElement();
/* 100 */     if (this.soapAction != null) {
/* 101 */       w.writeStartElement("SoapAction");
/* 102 */       w.writeCharacters(this.soapAction);
/* 103 */       w.writeEndElement();
/*     */     }
/* 105 */     w.writeEndElement();
/*     */   }
/*     */ 
/*     */   public void writeTo(SOAPMessage saaj) throws SOAPException {
/* 109 */     SOAPHeader header = saaj.getSOAPHeader();
/* 110 */     if (header == null)
/* 111 */       header = saaj.getSOAPPart().getEnvelope().addHeader();
/* 112 */     SOAPHeaderElement she = header.addHeaderElement(new QName(getNamespaceURI(), getLocalPart()));
/* 113 */     she.addChildElement("Action");
/* 114 */     she.addTextNode(this.action);
/* 115 */     if (this.soapAction != null) {
/* 116 */       she.addChildElement("SoapAction");
/* 117 */       she.addTextNode(this.soapAction);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTo(ContentHandler h, ErrorHandler errorHandler) throws SAXException {
/* 122 */     String nsUri = getNamespaceURI();
/* 123 */     String ln = getLocalPart();
/*     */ 
/* 125 */     h.startPrefixMapping("", nsUri);
/* 126 */     h.startElement(nsUri, ln, ln, EMPTY_ATTS);
/* 127 */     h.startElement(nsUri, "Action", "Action", EMPTY_ATTS);
/* 128 */     h.characters(this.action.toCharArray(), 0, this.action.length());
/* 129 */     h.endElement(nsUri, "Action", "Action");
/* 130 */     if (this.soapAction != null) {
/* 131 */       h.startElement(nsUri, "SoapAction", "SoapAction", EMPTY_ATTS);
/* 132 */       h.characters(this.soapAction.toCharArray(), 0, this.soapAction.length());
/* 133 */       h.endElement(nsUri, "SoapAction", "SoapAction");
/*     */     }
/* 135 */     h.endElement(nsUri, ln, ln);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.ProblemActionHeader
 * JD-Core Version:    0.6.2
 */