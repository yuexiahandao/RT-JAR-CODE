/*     */ package com.sun.xml.internal.ws.api.addressing;
/*     */ 
/*     */ import com.sun.istack.internal.FinalArrayList;
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBufferException;
/*     */ import com.sun.xml.internal.ws.message.AbstractHeaderImpl;
/*     */ import com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.SOAPEnvelope;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPHeader;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.soap.SOAPPart;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.stream.util.StreamReaderDelegate;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ final class OutboundReferenceParameterHeader extends AbstractHeaderImpl
/*     */ {
/*     */   private final XMLStreamBuffer infoset;
/*     */   private final String nsUri;
/*     */   private final String localName;
/*     */   private FinalArrayList<Attribute> attributes;
/*     */   private static final String TRUE_VALUE = "1";
/*     */   private static final String IS_REFERENCE_PARAMETER = "IsReferenceParameter";
/*     */ 
/*     */   OutboundReferenceParameterHeader(XMLStreamBuffer infoset, String nsUri, String localName)
/*     */   {
/*  78 */     this.infoset = infoset;
/*  79 */     this.nsUri = nsUri;
/*  80 */     this.localName = localName;
/*     */   }
/*     */   @NotNull
/*     */   public String getNamespaceURI() {
/*  84 */     return this.nsUri;
/*     */   }
/*     */   @NotNull
/*     */   public String getLocalPart() {
/*  88 */     return this.localName;
/*     */   }
/*     */ 
/*     */   public String getAttribute(String nsUri, String localName) {
/*  92 */     if (this.attributes == null)
/*  93 */       parseAttributes();
/*  94 */     for (int i = this.attributes.size() - 1; i >= 0; i--) {
/*  95 */       Attribute a = (Attribute)this.attributes.get(i);
/*  96 */       if ((a.localName.equals(localName)) && (a.nsUri.equals(nsUri)))
/*  97 */         return a.value;
/*     */     }
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */   private void parseAttributes()
/*     */   {
/*     */     try
/*     */     {
/* 110 */       XMLStreamReader reader = readHeader();
/* 111 */       reader.nextTag();
/*     */ 
/* 113 */       this.attributes = new FinalArrayList();
/* 114 */       boolean refParamAttrWritten = false;
/* 115 */       for (int i = 0; i < reader.getAttributeCount(); i++) {
/* 116 */         String localName = reader.getAttributeLocalName(i);
/* 117 */         String namespaceURI = reader.getAttributeNamespace(i);
/* 118 */         String value = reader.getAttributeValue(i);
/* 119 */         if ((namespaceURI.equals(AddressingVersion.W3C.nsUri)) && (localName.equals("IS_REFERENCE_PARAMETER")))
/* 120 */           refParamAttrWritten = true;
/* 121 */         this.attributes.add(new Attribute(namespaceURI, localName, value));
/*     */       }
/*     */ 
/* 124 */       if (!refParamAttrWritten)
/* 125 */         this.attributes.add(new Attribute(AddressingVersion.W3C.nsUri, "IsReferenceParameter", "1"));
/*     */     } catch (XMLStreamException e) {
/* 127 */       throw new WebServiceException("Unable to read the attributes for {" + this.nsUri + "}" + this.localName + " header", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public XMLStreamReader readHeader() throws XMLStreamException {
/* 132 */     return new StreamReaderDelegate(this.infoset.readAsXMLStreamReader()) {
/* 133 */       int state = 0;
/*     */ 
/* 135 */       public int next() throws XMLStreamException { return check(super.next()); }
/*     */ 
/*     */       public int nextTag() throws XMLStreamException
/*     */       {
/* 139 */         return check(super.nextTag());
/*     */       }
/*     */ 
/*     */       private int check(int type) {
/* 143 */         switch (this.state) {
/*     */         case 0:
/* 145 */           if (type == 1)
/* 146 */             this.state = 1; break;
/*     */         case 1:
/* 149 */           this.state = 2;
/*     */         }
/*     */ 
/* 152 */         return type;
/*     */       }
/*     */ 
/*     */       public int getAttributeCount() {
/* 156 */         if (this.state == 1) return super.getAttributeCount() + 1;
/* 157 */         return super.getAttributeCount();
/*     */       }
/*     */ 
/*     */       public String getAttributeLocalName(int index) {
/* 161 */         if ((this.state == 1) && (index == super.getAttributeCount())) {
/* 162 */           return "IsReferenceParameter";
/*     */         }
/* 164 */         return super.getAttributeLocalName(index);
/*     */       }
/*     */ 
/*     */       public String getAttributeNamespace(int index) {
/* 168 */         if ((this.state == 1) && (index == super.getAttributeCount())) {
/* 169 */           return AddressingVersion.W3C.nsUri;
/*     */         }
/* 171 */         return super.getAttributeNamespace(index);
/*     */       }
/*     */ 
/*     */       public String getAttributePrefix(int index) {
/* 175 */         if ((this.state == 1) && (index == super.getAttributeCount())) {
/* 176 */           return "wsa";
/*     */         }
/* 178 */         return super.getAttributePrefix(index);
/*     */       }
/*     */ 
/*     */       public String getAttributeType(int index) {
/* 182 */         if ((this.state == 1) && (index == super.getAttributeCount())) {
/* 183 */           return "CDATA";
/*     */         }
/* 185 */         return super.getAttributeType(index);
/*     */       }
/*     */ 
/*     */       public String getAttributeValue(int index) {
/* 189 */         if ((this.state == 1) && (index == super.getAttributeCount())) {
/* 190 */           return "1";
/*     */         }
/* 192 */         return super.getAttributeValue(index);
/*     */       }
/*     */ 
/*     */       public QName getAttributeName(int index) {
/* 196 */         if ((this.state == 1) && (index == super.getAttributeCount())) {
/* 197 */           return new QName(AddressingVersion.W3C.nsUri, "IsReferenceParameter", "wsa");
/*     */         }
/* 199 */         return super.getAttributeName(index);
/*     */       }
/*     */ 
/*     */       public String getAttributeValue(String namespaceUri, String localName) {
/* 203 */         if ((this.state == 1) && (localName.equals("IsReferenceParameter")) && (namespaceUri.equals(AddressingVersion.W3C.nsUri))) {
/* 204 */           return "1";
/*     */         }
/* 206 */         return super.getAttributeValue(namespaceUri, localName);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public void writeTo(XMLStreamWriter w) throws XMLStreamException {
/* 212 */     this.infoset.writeToXMLStreamWriter(new XMLStreamWriterFilter(w) {
/* 213 */       private boolean root = true;
/* 214 */       private boolean onRootEl = true;
/*     */ 
/* 216 */       public void writeStartElement(String localName) throws XMLStreamException { super.writeStartElement(localName);
/* 217 */         writeAddedAttribute(); }
/*     */ 
/*     */       private void writeAddedAttribute() throws XMLStreamException
/*     */       {
/* 221 */         if (!this.root) {
/* 222 */           this.onRootEl = false;
/* 223 */           return;
/*     */         }
/* 225 */         this.root = false;
/* 226 */         writeNamespace("wsa", AddressingVersion.W3C.nsUri);
/* 227 */         super.writeAttribute("wsa", AddressingVersion.W3C.nsUri, "IsReferenceParameter", "1");
/*     */       }
/*     */ 
/*     */       public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
/* 231 */         super.writeStartElement(namespaceURI, localName);
/* 232 */         writeAddedAttribute();
/*     */       }
/*     */ 
/*     */       public void writeStartElement(String prefix, String localName, String namespaceURI)
/*     */         throws XMLStreamException
/*     */       {
/* 238 */         boolean prefixDeclared = isPrefixDeclared(prefix, namespaceURI);
/* 239 */         super.writeStartElement(prefix, localName, namespaceURI);
/* 240 */         if ((!prefixDeclared) && (!prefix.equals("")))
/* 241 */           super.writeNamespace(prefix, namespaceURI);
/* 242 */         writeAddedAttribute();
/*     */       }
/*     */ 
/*     */       public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
/* 246 */         if (isPrefixDeclared(prefix, namespaceURI))
/*     */         {
/* 248 */           return;
/*     */         }
/* 250 */         super.writeNamespace(prefix, namespaceURI);
/*     */       }
/*     */ 
/*     */       public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException
/*     */       {
/* 255 */         if ((this.onRootEl) && (namespaceURI.equals(AddressingVersion.W3C.nsUri)) && (localName.equals("IsReferenceParameter")))
/*     */         {
/* 257 */           return;
/*     */         }
/* 259 */         this.writer.writeAttribute(prefix, namespaceURI, localName, value);
/*     */       }
/*     */ 
/*     */       public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException
/*     */       {
/* 264 */         this.writer.writeAttribute(namespaceURI, localName, value);
/*     */       }
/*     */       private boolean isPrefixDeclared(String prefix, String namespaceURI) {
/* 267 */         return namespaceURI.equals(getNamespaceContext().getNamespaceURI(prefix));
/*     */       }
/*     */     }
/*     */     , true);
/*     */   }
/*     */ 
/*     */   public void writeTo(SOAPMessage saaj)
/*     */     throws SOAPException
/*     */   {
/*     */     try
/*     */     {
/* 277 */       SOAPHeader header = saaj.getSOAPHeader();
/* 278 */       if (header == null)
/* 279 */         header = saaj.getSOAPPart().getEnvelope().addHeader();
/* 280 */       Element node = (Element)this.infoset.writeTo(header);
/* 281 */       node.setAttributeNS(AddressingVersion.W3C.nsUri, AddressingVersion.W3C.getPrefix() + ":" + "IsReferenceParameter", "1");
/*     */     } catch (XMLStreamBufferException e) {
/* 283 */       throw new SOAPException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler)
/*     */     throws SAXException
/*     */   {
/* 319 */     this.infoset.writeTo(new XMLFilterImpl()
/*     */     {
/* 290 */       private int depth = 0;
/*     */ 
/* 292 */       public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException { if (this.depth++ == 0)
/*     */         {
/* 294 */           super.startPrefixMapping("wsa", AddressingVersion.W3C.nsUri);
/*     */ 
/* 297 */           if (atts.getIndex(AddressingVersion.W3C.nsUri, "IsReferenceParameter") == -1) {
/* 298 */             AttributesImpl atts2 = new AttributesImpl(atts);
/* 299 */             atts2.addAttribute(AddressingVersion.W3C.nsUri, "IsReferenceParameter", "wsa:IsReferenceParameter", "CDATA", "1");
/*     */ 
/* 305 */             atts = atts2;
/*     */           }
/*     */         }
/*     */ 
/* 309 */         super.startElement(uri, localName, qName, atts); }
/*     */ 
/*     */       public void endElement(String uri, String localName, String qName) throws SAXException
/*     */       {
/* 313 */         super.endElement(uri, localName, qName);
/* 314 */         if (--this.depth == 0)
/* 315 */           super.endPrefixMapping("wsa");
/*     */       }
/*     */     }
/*     */     , errorHandler);
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
/* 335 */       this.nsUri = fixNull(nsUri);
/* 336 */       this.localName = localName;
/* 337 */       this.value = value;
/*     */     }
/*     */ 
/*     */     private static String fixNull(String s)
/*     */     {
/* 344 */       if (s == null) return "";
/* 345 */       return s;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.addressing.OutboundReferenceParameterHeader
 * JD-Core Version:    0.6.2
 */