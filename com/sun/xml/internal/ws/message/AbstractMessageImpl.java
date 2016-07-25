/*     */ package com.sun.xml.internal.ws.message;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.bind.marshaller.SAX2DOMEx;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.message.Attachment;
/*     */ import com.sun.xml.internal.ws.api.message.Header;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.soap.AttachmentPart;
/*     */ import javax.xml.soap.MessageFactory;
/*     */ import javax.xml.soap.MimeHeaders;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ public abstract class AbstractMessageImpl extends Message
/*     */ {
/*     */   protected final SOAPVersion soapVersion;
/* 220 */   protected static final AttributesImpl EMPTY_ATTS = new AttributesImpl();
/* 221 */   protected static final LocatorImpl NULL_LOCATOR = new LocatorImpl();
/*     */ 
/*     */   protected AbstractMessageImpl(SOAPVersion soapVersion)
/*     */   {
/*  83 */     this.soapVersion = soapVersion;
/*     */   }
/*     */ 
/*     */   protected AbstractMessageImpl(AbstractMessageImpl that)
/*     */   {
/*  90 */     this.soapVersion = that.soapVersion;
/*     */   }
/*     */ 
/*     */   public Source readEnvelopeAsSource() {
/*  94 */     return new SAXSource(new XMLReaderImpl(this), XMLReaderImpl.THE_SOURCE);
/*     */   }
/*     */ 
/*     */   public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
/*  98 */     if (hasAttachments())
/*  99 */       unmarshaller.setAttachmentUnmarshaller(new AttachmentUnmarshallerImpl(getAttachments()));
/*     */     try {
/* 101 */       return unmarshaller.unmarshal(readPayloadAsSource());
/*     */     } finally {
/* 103 */       unmarshaller.setAttachmentUnmarshaller(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
/* 108 */     return bridge.unmarshal(readPayloadAsSource(), hasAttachments() ? new AttachmentUnmarshallerImpl(getAttachments()) : null);
/*     */   }
/*     */ 
/*     */   public void writeTo(XMLStreamWriter w)
/*     */     throws XMLStreamException
/*     */   {
/* 116 */     String soapNsUri = this.soapVersion.nsUri;
/* 117 */     w.writeStartDocument();
/* 118 */     w.writeStartElement("S", "Envelope", soapNsUri);
/* 119 */     w.writeNamespace("S", soapNsUri);
/* 120 */     if (hasHeaders()) {
/* 121 */       w.writeStartElement("S", "Header", soapNsUri);
/* 122 */       HeaderList headers = getHeaders();
/* 123 */       int len = headers.size();
/* 124 */       for (int i = 0; i < len; i++) {
/* 125 */         headers.get(i).writeTo(w);
/*     */       }
/* 127 */       w.writeEndElement();
/*     */     }
/*     */ 
/* 130 */     w.writeStartElement("S", "Body", soapNsUri);
/*     */ 
/* 132 */     writePayloadTo(w);
/*     */ 
/* 134 */     w.writeEndElement();
/* 135 */     w.writeEndElement();
/* 136 */     w.writeEndDocument();
/*     */   }
/*     */ 
/*     */   public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler)
/*     */     throws SAXException
/*     */   {
/* 143 */     String soapNsUri = this.soapVersion.nsUri;
/*     */ 
/* 145 */     contentHandler.setDocumentLocator(NULL_LOCATOR);
/* 146 */     contentHandler.startDocument();
/* 147 */     contentHandler.startPrefixMapping("S", soapNsUri);
/* 148 */     contentHandler.startElement(soapNsUri, "Envelope", "S:Envelope", EMPTY_ATTS);
/* 149 */     if (hasHeaders()) {
/* 150 */       contentHandler.startElement(soapNsUri, "Header", "S:Header", EMPTY_ATTS);
/* 151 */       HeaderList headers = getHeaders();
/* 152 */       int len = headers.size();
/* 153 */       for (int i = 0; i < len; i++)
/*     */       {
/* 155 */         headers.get(i).writeTo(contentHandler, errorHandler);
/*     */       }
/* 157 */       contentHandler.endElement(soapNsUri, "Header", "S:Header");
/*     */     }
/*     */ 
/* 160 */     contentHandler.startElement(soapNsUri, "Body", "S:Body", EMPTY_ATTS);
/* 161 */     writePayloadTo(contentHandler, errorHandler, true);
/* 162 */     contentHandler.endElement(soapNsUri, "Body", "S:Body");
/* 163 */     contentHandler.endElement(soapNsUri, "Envelope", "S:Envelope");
/*     */   }
/*     */ 
/*     */   protected abstract void writePayloadTo(ContentHandler paramContentHandler, ErrorHandler paramErrorHandler, boolean paramBoolean)
/*     */     throws SAXException;
/*     */ 
/*     */   public SOAPMessage readAsSOAPMessage()
/*     */     throws SOAPException
/*     */   {
/* 180 */     SOAPMessage msg = this.soapVersion.saajMessageFactory.createMessage();
/* 181 */     SAX2DOMEx s2d = new SAX2DOMEx(msg.getSOAPPart());
/*     */     try {
/* 183 */       writeTo(s2d, XmlUtil.DRACONIAN_ERROR_HANDLER);
/*     */     } catch (SAXException e) {
/* 185 */       throw new SOAPException(e);
/*     */     }
/* 187 */     for (Attachment att : getAttachments()) {
/* 188 */       AttachmentPart part = msg.createAttachmentPart();
/* 189 */       part.setDataHandler(att.asDataHandler());
/* 190 */       part.setContentId('<' + att.getContentId() + '>');
/* 191 */       msg.addAttachmentPart(part);
/*     */     }
/* 193 */     return msg;
/*     */   }
/*     */ 
/*     */   public SOAPMessage readAsSOAPMessage(Packet packet, boolean inbound)
/*     */     throws SOAPException
/*     */   {
/* 200 */     SOAPMessage msg = readAsSOAPMessage();
/* 201 */     Map headers = null;
/* 202 */     String key = inbound ? "com.sun.xml.internal.ws.api.message.packet.inbound.transport.headers" : "com.sun.xml.internal.ws.api.message.packet.outbound.transport.headers";
/* 203 */     if (packet.supports(key))
/* 204 */       headers = (Map)packet.get(key);
/*     */     Iterator i$;
/* 206 */     if (headers != null)
/* 207 */       for (i$ = headers.entrySet().iterator(); i$.hasNext(); ) { e = (Map.Entry)i$.next();
/* 208 */         if (!((String)e.getKey()).equalsIgnoreCase("Content-Type"))
/* 209 */           for (String value : (List)e.getValue())
/* 210 */             msg.getMimeHeaders().addHeader((String)e.getKey(), value);
/*     */       }
/*     */     Map.Entry e;
/* 215 */     msg.saveChanges();
/* 216 */     return msg;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.AbstractMessageImpl
 * JD-Core Version:    0.6.2
 */