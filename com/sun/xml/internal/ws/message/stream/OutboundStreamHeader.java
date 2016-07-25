/*     */ package com.sun.xml.internal.ws.message.stream;
/*     */ 
/*     */ import com.sun.istack.internal.FinalArrayList;
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBufferException;
/*     */ import com.sun.xml.internal.ws.message.AbstractHeaderImpl;
/*     */ import javax.xml.soap.SOAPEnvelope;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPHeader;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.soap.SOAPPart;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class OutboundStreamHeader extends AbstractHeaderImpl
/*     */ {
/*     */   private final XMLStreamBuffer infoset;
/*     */   private final String nsUri;
/*     */   private final String localName;
/*     */   private FinalArrayList<Attribute> attributes;
/*     */   private static final String TRUE_VALUE = "1";
/*     */   private static final String IS_REFERENCE_PARAMETER = "IsReferenceParameter";
/*     */ 
/*     */   public OutboundStreamHeader(XMLStreamBuffer infoset, String nsUri, String localName)
/*     */   {
/*  67 */     this.infoset = infoset;
/*  68 */     this.nsUri = nsUri;
/*  69 */     this.localName = localName;
/*     */   }
/*     */   @NotNull
/*     */   public String getNamespaceURI() {
/*  73 */     return this.nsUri;
/*     */   }
/*     */   @NotNull
/*     */   public String getLocalPart() {
/*  77 */     return this.localName;
/*     */   }
/*     */ 
/*     */   public String getAttribute(String nsUri, String localName) {
/*  81 */     if (this.attributes == null)
/*  82 */       parseAttributes();
/*  83 */     for (int i = this.attributes.size() - 1; i >= 0; i--) {
/*  84 */       Attribute a = (Attribute)this.attributes.get(i);
/*  85 */       if ((a.localName.equals(localName)) && (a.nsUri.equals(nsUri)))
/*  86 */         return a.value;
/*     */     }
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */   private void parseAttributes()
/*     */   {
/*     */     try
/*     */     {
/*  99 */       XMLStreamReader reader = readHeader();
/*     */ 
/* 101 */       this.attributes = new FinalArrayList();
/*     */ 
/* 103 */       for (int i = 0; i < reader.getAttributeCount(); i++) {
/* 104 */         String localName = reader.getAttributeLocalName(i);
/* 105 */         String namespaceURI = reader.getAttributeNamespace(i);
/* 106 */         String value = reader.getAttributeValue(i);
/*     */ 
/* 108 */         this.attributes.add(new Attribute(namespaceURI, localName, value));
/*     */       }
/*     */     } catch (XMLStreamException e) {
/* 111 */       throw new WebServiceException("Unable to read the attributes for {" + this.nsUri + "}" + this.localName + " header", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public XMLStreamReader readHeader() throws XMLStreamException {
/* 116 */     return this.infoset.readAsXMLStreamReader();
/*     */   }
/*     */ 
/*     */   public void writeTo(XMLStreamWriter w) throws XMLStreamException {
/* 120 */     this.infoset.writeToXMLStreamWriter(w, true);
/*     */   }
/*     */ 
/*     */   public void writeTo(SOAPMessage saaj) throws SOAPException {
/*     */     try {
/* 125 */       SOAPHeader header = saaj.getSOAPHeader();
/* 126 */       if (header == null)
/* 127 */         header = saaj.getSOAPPart().getEnvelope().addHeader();
/* 128 */       this.infoset.writeTo(header);
/*     */     } catch (XMLStreamBufferException e) {
/* 130 */       throw new SOAPException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
/* 135 */     this.infoset.writeTo(contentHandler, errorHandler);
/*     */   }
/*     */ 
/*     */   static final class Attribute
/*     */   {
/*     */     final String nsUri;
/*     */     final String localName;
/*     */     final String value;
/*     */ 
/*     */     public Attribute(String nsUri, String localName, String value)
/*     */     {
/* 151 */       this.nsUri = fixNull(nsUri);
/* 152 */       this.localName = localName;
/* 153 */       this.value = value;
/*     */     }
/*     */ 
/*     */     private static String fixNull(String s)
/*     */     {
/* 160 */       if (s == null) return "";
/* 161 */       return s;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.stream.OutboundStreamHeader
 * JD-Core Version:    0.6.2
 */