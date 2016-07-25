/*     */ package com.sun.xml.internal.ws.message.jaxb;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.XMLStreamException2;
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBufferResult;
/*     */ import com.sun.xml.internal.ws.message.AbstractHeaderImpl;
/*     */ import com.sun.xml.internal.ws.message.RootElementSniffer;
/*     */ import com.sun.xml.internal.ws.streaming.XMLStreamWriterUtil;
/*     */ import java.io.OutputStream;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.util.JAXBResult;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.SOAPEnvelope;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPHeader;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.soap.SOAPPart;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public final class JAXBHeader extends AbstractHeaderImpl
/*     */ {
/*     */   private final Object jaxbObject;
/*     */   private final Bridge bridge;
/*     */   private String nsUri;
/*     */   private String localName;
/*     */   private Attributes atts;
/*     */   private XMLStreamBuffer infoset;
/*     */ 
/*     */   public JAXBHeader(JAXBRIContext context, Object jaxbObject)
/*     */   {
/*  83 */     this.jaxbObject = jaxbObject;
/*  84 */     this.bridge = new MarshallerBridge(context);
/*     */ 
/*  86 */     if ((jaxbObject instanceof JAXBElement)) {
/*  87 */       JAXBElement e = (JAXBElement)jaxbObject;
/*  88 */       this.nsUri = e.getName().getNamespaceURI();
/*  89 */       this.localName = e.getName().getLocalPart();
/*     */     }
/*     */   }
/*     */ 
/*     */   public JAXBHeader(Bridge bridge, Object jaxbObject) {
/*  94 */     this.jaxbObject = jaxbObject;
/*  95 */     this.bridge = bridge;
/*     */ 
/*  97 */     QName tagName = bridge.getTypeReference().tagName;
/*  98 */     this.nsUri = tagName.getNamespaceURI();
/*  99 */     this.localName = tagName.getLocalPart();
/*     */   }
/*     */ 
/*     */   private void parse()
/*     */   {
/* 106 */     RootElementSniffer sniffer = new RootElementSniffer();
/*     */     try {
/* 108 */       this.bridge.marshal(this.jaxbObject, sniffer);
/*     */     }
/*     */     catch (JAXBException e)
/*     */     {
/* 116 */       this.nsUri = sniffer.getNsUri();
/* 117 */       this.localName = sniffer.getLocalName();
/* 118 */       this.atts = sniffer.getAttributes();
/*     */     }
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public String getNamespaceURI() {
/* 124 */     if (this.nsUri == null)
/* 125 */       parse();
/* 126 */     return this.nsUri;
/*     */   }
/*     */   @NotNull
/*     */   public String getLocalPart() {
/* 130 */     if (this.localName == null)
/* 131 */       parse();
/* 132 */     return this.localName;
/*     */   }
/*     */ 
/*     */   public String getAttribute(String nsUri, String localName) {
/* 136 */     if (this.atts == null)
/* 137 */       parse();
/* 138 */     return this.atts.getValue(nsUri, localName);
/*     */   }
/*     */ 
/*     */   public XMLStreamReader readHeader() throws XMLStreamException {
/*     */     try {
/* 143 */       if (this.infoset == null) {
/* 144 */         XMLStreamBufferResult sbr = new XMLStreamBufferResult();
/* 145 */         this.bridge.marshal(this.jaxbObject, sbr);
/* 146 */         this.infoset = sbr.getXMLStreamBuffer();
/*     */       }
/* 148 */       return this.infoset.readAsXMLStreamReader();
/*     */     } catch (JAXBException e) {
/* 150 */       throw new XMLStreamException2(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> T readAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
/*     */     try {
/* 156 */       JAXBResult r = new JAXBResult(unmarshaller);
/*     */ 
/* 158 */       r.getHandler().startDocument();
/* 159 */       this.bridge.marshal(this.jaxbObject, r);
/* 160 */       r.getHandler().endDocument();
/* 161 */       return r.getResult();
/*     */     } catch (SAXException e) {
/* 163 */       throw new JAXBException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> T readAsJAXB(Bridge<T> bridge) throws JAXBException {
/* 168 */     return bridge.unmarshal(new JAXBBridgeSource(this.bridge, this.jaxbObject));
/*     */   }
/*     */ 
/*     */   public void writeTo(XMLStreamWriter sw) throws XMLStreamException
/*     */   {
/*     */     try {
/* 174 */       OutputStream os = XMLStreamWriterUtil.getOutputStream(sw);
/* 175 */       if (os != null)
/* 176 */         this.bridge.marshal(this.jaxbObject, os, sw.getNamespaceContext());
/*     */       else
/* 178 */         this.bridge.marshal(this.jaxbObject, sw);
/*     */     }
/*     */     catch (JAXBException e) {
/* 181 */       throw new XMLStreamException2(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTo(SOAPMessage saaj) throws SOAPException {
/*     */     try {
/* 187 */       SOAPHeader header = saaj.getSOAPHeader();
/* 188 */       if (header == null)
/* 189 */         header = saaj.getSOAPPart().getEnvelope().addHeader();
/* 190 */       this.bridge.marshal(this.jaxbObject, header);
/*     */     } catch (JAXBException e) {
/* 192 */       throw new SOAPException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
/*     */     try {
/* 198 */       this.bridge.marshal(this.jaxbObject, contentHandler);
/*     */     } catch (JAXBException e) {
/* 200 */       SAXParseException x = new SAXParseException(e.getMessage(), null, null, -1, -1, e);
/* 201 */       errorHandler.fatalError(x);
/* 202 */       throw x;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.jaxb.JAXBHeader
 * JD-Core Version:    0.6.2
 */