/*     */ package com.sun.xml.internal.ws.message.stream;
/*     */ 
/*     */ import com.sun.istack.internal.FinalArrayList;
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBufferSource;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.message.AbstractHeaderImpl;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.soap.SOAPEnvelope;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPHeader;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.soap.SOAPPart;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract class StreamHeader extends AbstractHeaderImpl
/*     */ {
/*     */   protected final XMLStreamBuffer _mark;
/*     */   protected boolean _isMustUnderstand;
/*     */ 
/*     */   @NotNull
/*     */   protected String _role;
/*     */   protected boolean _isRelay;
/*     */   protected String _localName;
/*     */   protected String _namespaceURI;
/*     */   private final FinalArrayList<Attribute> attributes;
/*     */ 
/*     */   protected StreamHeader(XMLStreamReader reader, XMLStreamBuffer mark)
/*     */   {
/* 115 */     assert ((reader != null) && (mark != null));
/* 116 */     this._mark = mark;
/* 117 */     this._localName = reader.getLocalName();
/* 118 */     this._namespaceURI = reader.getNamespaceURI();
/* 119 */     this.attributes = processHeaderAttributes(reader);
/*     */   }
/*     */ 
/*     */   protected StreamHeader(XMLStreamReader reader)
/*     */     throws XMLStreamException
/*     */   {
/* 131 */     this._localName = reader.getLocalName();
/* 132 */     this._namespaceURI = reader.getNamespaceURI();
/* 133 */     this.attributes = processHeaderAttributes(reader);
/*     */ 
/* 135 */     this._mark = XMLStreamBuffer.createNewBufferFromXMLStreamReader(reader);
/*     */   }
/*     */ 
/*     */   public final boolean isIgnorable(@NotNull SOAPVersion soapVersion, @NotNull Set<String> roles)
/*     */   {
/* 140 */     if (!this._isMustUnderstand) return true;
/*     */ 
/* 142 */     if (roles == null) {
/* 143 */       return true;
/*     */     }
/*     */ 
/* 146 */     return !roles.contains(this._role);
/*     */   }
/*     */   @NotNull
/*     */   public String getRole(@NotNull SOAPVersion soapVersion) {
/* 150 */     assert (this._role != null);
/* 151 */     return this._role;
/*     */   }
/*     */ 
/*     */   public boolean isRelay() {
/* 155 */     return this._isRelay;
/*     */   }
/*     */   @NotNull
/*     */   public String getNamespaceURI() {
/* 159 */     return this._namespaceURI;
/*     */   }
/*     */   @NotNull
/*     */   public String getLocalPart() {
/* 163 */     return this._localName;
/*     */   }
/*     */ 
/*     */   public String getAttribute(String nsUri, String localName) {
/* 167 */     if (this.attributes != null) {
/* 168 */       for (int i = this.attributes.size() - 1; i >= 0; i--) {
/* 169 */         Attribute a = (Attribute)this.attributes.get(i);
/* 170 */         if ((a.localName.equals(localName)) && (a.nsUri.equals(nsUri)))
/* 171 */           return a.value;
/*     */       }
/*     */     }
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLStreamReader readHeader()
/*     */     throws XMLStreamException
/*     */   {
/* 181 */     return this._mark.readAsXMLStreamReader();
/*     */   }
/*     */ 
/*     */   public void writeTo(XMLStreamWriter w) throws XMLStreamException {
/* 185 */     if (this._mark.getInscopeNamespaces().size() > 0)
/* 186 */       this._mark.writeToXMLStreamWriter(w, true);
/*     */     else
/* 188 */       this._mark.writeToXMLStreamWriter(w);
/*     */   }
/*     */ 
/*     */   public void writeTo(SOAPMessage saaj)
/*     */     throws SOAPException
/*     */   {
/*     */     try
/*     */     {
/* 196 */       TransformerFactory tf = TransformerFactory.newInstance();
/* 197 */       Transformer t = tf.newTransformer();
/* 198 */       XMLStreamBufferSource source = new XMLStreamBufferSource(this._mark);
/* 199 */       DOMResult result = new DOMResult();
/* 200 */       t.transform(source, result);
/* 201 */       Node d = result.getNode();
/* 202 */       if (d.getNodeType() == 9)
/* 203 */         d = d.getFirstChild();
/* 204 */       SOAPHeader header = saaj.getSOAPHeader();
/* 205 */       if (header == null)
/* 206 */         header = saaj.getSOAPPart().getEnvelope().addHeader();
/* 207 */       Node node = header.getOwnerDocument().importNode(d, true);
/* 208 */       header.appendChild(node);
/*     */     } catch (Exception e) {
/* 210 */       throw new SOAPException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
/* 215 */     this._mark.writeTo(contentHandler);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public WSEndpointReference readAsEPR(AddressingVersion expected)
/*     */     throws XMLStreamException
/*     */   {
/* 226 */     return new WSEndpointReference(this._mark, expected);
/*     */   }
/*     */ 
/*     */   protected abstract FinalArrayList<Attribute> processHeaderAttributes(XMLStreamReader paramXMLStreamReader);
/*     */ 
/*     */   private static String fixNull(String s)
/*     */   {
/* 235 */     if (s == null) return "";
/* 236 */     return s;
/*     */   }
/*     */ 
/*     */   protected static final class Attribute
/*     */   {
/*     */     final String nsUri;
/*     */     final String localName;
/*     */     final String value;
/*     */ 
/*     */     public Attribute(String nsUri, String localName, String value)
/*     */     {
/*  89 */       this.nsUri = StreamHeader.fixNull(nsUri);
/*  90 */       this.localName = localName;
/*  91 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.stream.StreamHeader
 * JD-Core Version:    0.6.2
 */