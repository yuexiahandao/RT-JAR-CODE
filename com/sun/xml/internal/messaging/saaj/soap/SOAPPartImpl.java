/*     */ package com.sun.xml.internal.messaging.saaj.soap;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeBodyPart;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.EnvelopeImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
/*     */ import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
/*     */ import com.sun.xml.internal.messaging.saaj.util.FastInfosetReflection;
/*     */ import com.sun.xml.internal.messaging.saaj.util.JAXMStreamSource;
/*     */ import com.sun.xml.internal.messaging.saaj.util.MimeHeadersUtil;
/*     */ import com.sun.xml.internal.messaging.saaj.util.SAAJUtil;
/*     */ import com.sun.xml.internal.messaging.saaj.util.XMLDeclarationParser;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PushbackReader;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Iterator;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.activation.DataSource;
/*     */ import javax.xml.soap.MimeHeaders;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPEnvelope;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPPart;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.CDATASection;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.DOMConfiguration;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.DOMImplementation;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.DocumentFragment;
/*     */ import org.w3c.dom.DocumentType;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.EntityReference;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ import org.w3c.dom.UserDataHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ public abstract class SOAPPartImpl extends SOAPPart
/*     */   implements SOAPDocument
/*     */ {
/*  58 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap", "com.sun.xml.internal.messaging.saaj.soap.LocalStrings");
/*     */   protected MimeHeaders headers;
/*     */   protected Envelope envelope;
/*     */   protected Source source;
/*     */   protected SOAPDocumentImpl document;
/*  68 */   private boolean sourceWasSet = false;
/*     */ 
/*  71 */   protected boolean omitXmlDecl = true;
/*     */ 
/*  74 */   protected String sourceCharsetEncoding = null;
/*     */   protected MessageImpl message;
/*  83 */   static final boolean lazyContentLength = SAAJUtil.getSystemBoolean("saaj.lazy.contentlength");
/*     */ 
/*     */   protected SOAPPartImpl()
/*     */   {
/*  87 */     this(null);
/*     */   }
/*     */ 
/*     */   protected SOAPPartImpl(MessageImpl message) {
/*  91 */     this.document = new SOAPDocumentImpl(this);
/*  92 */     this.headers = new MimeHeaders();
/*  93 */     this.message = message;
/*  94 */     this.headers.setHeader("Content-Type", getContentType());
/*     */   }
/*     */   protected abstract String getContentType();
/*     */ 
/*     */   protected abstract Envelope createEnvelopeFromSource() throws SOAPException;
/*     */ 
/*     */   protected abstract Envelope createEmptyEnvelope(String paramString) throws SOAPException;
/*     */ 
/*     */   protected abstract SOAPPartImpl duplicateType();
/*     */ 
/*     */   protected String getContentTypeString() {
/* 105 */     return getContentType();
/*     */   }
/*     */ 
/*     */   public boolean isFastInfoset() {
/* 109 */     return this.message != null ? this.message.isFastInfoset() : false;
/*     */   }
/*     */ 
/*     */   public SOAPEnvelope getEnvelope()
/*     */     throws SOAPException
/*     */   {
/* 118 */     if (this.sourceWasSet) {
/* 119 */       this.sourceWasSet = false;
/*     */     }
/* 121 */     lookForEnvelope();
/* 122 */     if (this.envelope != null) {
/* 123 */       if (this.source != null) {
/* 124 */         this.document.removeChild(this.envelope);
/* 125 */         this.envelope = createEnvelopeFromSource();
/*     */       }
/* 127 */     } else if (this.source != null) {
/* 128 */       this.envelope = createEnvelopeFromSource();
/*     */     } else {
/* 130 */       this.envelope = createEmptyEnvelope(null);
/* 131 */       this.document.insertBefore(this.envelope, null);
/*     */     }
/* 133 */     return this.envelope;
/*     */   }
/*     */ 
/*     */   protected void lookForEnvelope() throws SOAPException {
/* 137 */     Element envelopeChildElement = this.document.doGetDocumentElement();
/* 138 */     if ((envelopeChildElement == null) || ((envelopeChildElement instanceof Envelope))) {
/* 139 */       this.envelope = ((EnvelopeImpl)envelopeChildElement); } else {
/* 140 */       if (!(envelopeChildElement instanceof ElementImpl)) {
/* 141 */         log.severe("SAAJ0512.soap.incorrect.factory.used");
/* 142 */         throw new SOAPExceptionImpl("Unable to create envelope: incorrect factory used during tree construction");
/*     */       }
/* 144 */       ElementImpl soapElement = (ElementImpl)envelopeChildElement;
/* 145 */       if (soapElement.getLocalName().equalsIgnoreCase("Envelope")) {
/* 146 */         String prefix = soapElement.getPrefix();
/* 147 */         String uri = prefix == null ? soapElement.getNamespaceURI() : soapElement.getNamespaceURI(prefix);
/* 148 */         if ((!uri.equals("http://schemas.xmlsoap.org/soap/envelope/")) && (!uri.equals("http://www.w3.org/2003/05/soap-envelope"))) {
/* 149 */           log.severe("SAAJ0513.soap.unknown.ns");
/* 150 */           throw new SOAPVersionMismatchException("Unable to create envelope from given source because the namespace was not recognized");
/*     */         }
/*     */       } else {
/* 153 */         log.severe("SAAJ0514.soap.root.elem.not.named.envelope");
/* 154 */         throw new SOAPExceptionImpl("Unable to create envelope from given source because the root element is not named \"Envelope\"");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeAllMimeHeaders()
/*     */   {
/* 161 */     this.headers.removeAllHeaders();
/*     */   }
/*     */ 
/*     */   public void removeMimeHeader(String header) {
/* 165 */     this.headers.removeHeader(header);
/*     */   }
/*     */ 
/*     */   public String[] getMimeHeader(String name) {
/* 169 */     return this.headers.getHeader(name);
/*     */   }
/*     */ 
/*     */   public void setMimeHeader(String name, String value) {
/* 173 */     this.headers.setHeader(name, value);
/*     */   }
/*     */ 
/*     */   public void addMimeHeader(String name, String value) {
/* 177 */     this.headers.addHeader(name, value);
/*     */   }
/*     */ 
/*     */   public Iterator getAllMimeHeaders() {
/* 181 */     return this.headers.getAllHeaders();
/*     */   }
/*     */ 
/*     */   public Iterator getMatchingMimeHeaders(String[] names) {
/* 185 */     return this.headers.getMatchingHeaders(names);
/*     */   }
/*     */ 
/*     */   public Iterator getNonMatchingMimeHeaders(String[] names) {
/* 189 */     return this.headers.getNonMatchingHeaders(names);
/*     */   }
/*     */ 
/*     */   public Source getContent() throws SOAPException {
/* 193 */     if (this.source != null) {
/* 194 */       InputStream bis = null;
/* 195 */       if ((this.source instanceof JAXMStreamSource)) {
/* 196 */         StreamSource streamSource = (StreamSource)this.source;
/* 197 */         bis = streamSource.getInputStream();
/* 198 */       } else if (FastInfosetReflection.isFastInfosetSource(this.source))
/*     */       {
/* 200 */         SAXSource saxSource = (SAXSource)this.source;
/* 201 */         bis = saxSource.getInputSource().getByteStream();
/*     */       }
/*     */ 
/* 204 */       if (bis != null) {
/*     */         try {
/* 206 */           bis.reset();
/*     */         }
/*     */         catch (IOException e)
/*     */         {
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 217 */       return this.source;
/*     */     }
/*     */ 
/* 220 */     return ((Envelope)getEnvelope()).getContent();
/*     */   }
/*     */ 
/*     */   public void setContent(Source source) throws SOAPException {
/*     */     try {
/* 225 */       if ((source instanceof StreamSource)) {
/* 226 */         InputStream is = ((StreamSource)source).getInputStream();
/* 227 */         Reader rdr = ((StreamSource)source).getReader();
/*     */ 
/* 229 */         if (is != null) {
/* 230 */           this.source = new JAXMStreamSource(is);
/* 231 */         } else if (rdr != null) {
/* 232 */           this.source = new JAXMStreamSource(rdr);
/*     */         } else {
/* 234 */           log.severe("SAAJ0544.soap.no.valid.reader.for.src");
/* 235 */           throw new SOAPExceptionImpl("Source does not have a valid Reader or InputStream");
/*     */         }
/*     */       }
/* 238 */       else if (FastInfosetReflection.isFastInfosetSource(source))
/*     */       {
/* 240 */         InputStream is = FastInfosetReflection.FastInfosetSource_getInputStream(source);
/*     */ 
/* 247 */         if (!(is instanceof ByteInputStream)) {
/* 248 */           ByteOutputStream bout = new ByteOutputStream();
/* 249 */           bout.write(is);
/*     */ 
/* 252 */           FastInfosetReflection.FastInfosetSource_setInputStream(source, bout.newInputStream());
/*     */         }
/*     */ 
/* 255 */         this.source = source;
/*     */       }
/*     */       else {
/* 258 */         this.source = source;
/*     */       }
/* 260 */       this.sourceWasSet = true;
/*     */     }
/*     */     catch (Exception ex) {
/* 263 */       ex.printStackTrace();
/*     */ 
/* 265 */       log.severe("SAAJ0545.soap.cannot.set.src.for.part");
/* 266 */       throw new SOAPExceptionImpl("Error setting the source for SOAPPart: " + ex.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public InputStream getContentAsStream() throws IOException
/*     */   {
/* 272 */     if (this.source != null) {
/* 273 */       InputStream is = null;
/*     */ 
/* 276 */       if (((this.source instanceof StreamSource)) && (!isFastInfoset())) {
/* 277 */         is = ((StreamSource)this.source).getInputStream();
/*     */       }
/* 279 */       else if ((FastInfosetReflection.isFastInfosetSource(this.source)) && (isFastInfoset()))
/*     */       {
/*     */         try
/*     */         {
/* 284 */           is = FastInfosetReflection.FastInfosetSource_getInputStream(this.source);
/*     */         }
/*     */         catch (Exception e) {
/* 287 */           throw new IOException(e.toString());
/*     */         }
/*     */       }
/*     */ 
/* 291 */       if (is != null) {
/* 292 */         if (lazyContentLength) {
/* 293 */           return is;
/*     */         }
/* 295 */         if (!(is instanceof ByteInputStream)) {
/* 296 */           log.severe("SAAJ0546.soap.stream.incorrect.type");
/* 297 */           throw new IOException("Internal error: stream not of the right type");
/*     */         }
/* 299 */         return (ByteInputStream)is;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 305 */     ByteOutputStream b = new ByteOutputStream();
/*     */ 
/* 307 */     Envelope env = null;
/*     */     try
/*     */     {
/* 310 */       env = (Envelope)getEnvelope();
/* 311 */       env.output(b, isFastInfoset());
/*     */     }
/*     */     catch (SOAPException soapException) {
/* 314 */       log.severe("SAAJ0547.soap.cannot.externalize");
/* 315 */       throw new SOAPIOException("SOAP exception while trying to externalize: ", soapException);
/*     */     }
/*     */ 
/* 320 */     return b.newInputStream();
/*     */   }
/*     */ 
/*     */   MimeBodyPart getMimePart() throws SOAPException {
/*     */     try {
/* 325 */       MimeBodyPart headerEnvelope = new MimeBodyPart();
/*     */ 
/* 327 */       headerEnvelope.setDataHandler(getDataHandler());
/* 328 */       AttachmentPartImpl.copyMimeHeaders(this.headers, headerEnvelope);
/*     */ 
/* 330 */       return headerEnvelope;
/*     */     } catch (SOAPException ex) {
/* 332 */       throw ex;
/*     */     } catch (Exception ex) {
/* 334 */       log.severe("SAAJ0548.soap.cannot.externalize.hdr");
/* 335 */       throw new SOAPExceptionImpl("Unable to externalize header", ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   MimeHeaders getMimeHeaders() {
/* 340 */     return this.headers;
/*     */   }
/*     */ 
/*     */   DataHandler getDataHandler() {
/* 344 */     DataSource ds = new DataSource() {
/*     */       public OutputStream getOutputStream() throws IOException {
/* 346 */         throw new IOException("Illegal Operation");
/*     */       }
/*     */ 
/*     */       public String getContentType() {
/* 350 */         return SOAPPartImpl.this.getContentTypeString();
/*     */       }
/*     */ 
/*     */       public String getName() {
/* 354 */         return SOAPPartImpl.this.getContentId();
/*     */       }
/*     */ 
/*     */       public InputStream getInputStream() throws IOException {
/* 358 */         return SOAPPartImpl.this.getContentAsStream();
/*     */       }
/*     */     };
/* 361 */     return new DataHandler(ds);
/*     */   }
/*     */ 
/*     */   public SOAPDocumentImpl getDocument() {
/* 365 */     handleNewSource();
/* 366 */     return this.document;
/*     */   }
/*     */ 
/*     */   public SOAPPartImpl getSOAPPart() {
/* 370 */     return this;
/*     */   }
/*     */ 
/*     */   public DocumentType getDoctype() {
/* 374 */     return this.document.getDoctype();
/*     */   }
/*     */ 
/*     */   public DOMImplementation getImplementation()
/*     */   {
/* 382 */     return this.document.getImplementation();
/*     */   }
/*     */ 
/*     */   public Element getDocumentElement()
/*     */   {
/*     */     try
/*     */     {
/* 390 */       getEnvelope();
/*     */     } catch (SOAPException e) {
/*     */     }
/* 393 */     return this.document.getDocumentElement();
/*     */   }
/*     */ 
/*     */   protected void doGetDocumentElement() {
/* 397 */     handleNewSource();
/*     */     try {
/* 399 */       lookForEnvelope();
/*     */     } catch (SOAPException e) {
/*     */     }
/*     */   }
/*     */ 
/*     */   public Element createElement(String tagName) throws DOMException {
/* 405 */     return this.document.createElement(tagName);
/*     */   }
/*     */ 
/*     */   public DocumentFragment createDocumentFragment() {
/* 409 */     return this.document.createDocumentFragment();
/*     */   }
/*     */ 
/*     */   public Text createTextNode(String data) {
/* 413 */     return this.document.createTextNode(data);
/*     */   }
/*     */ 
/*     */   public Comment createComment(String data) {
/* 417 */     return this.document.createComment(data);
/*     */   }
/*     */ 
/*     */   public CDATASection createCDATASection(String data) throws DOMException {
/* 421 */     return this.document.createCDATASection(data);
/*     */   }
/*     */ 
/*     */   public ProcessingInstruction createProcessingInstruction(String target, String data)
/*     */     throws DOMException
/*     */   {
/* 428 */     return this.document.createProcessingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   public Attr createAttribute(String name) throws DOMException {
/* 432 */     return this.document.createAttribute(name);
/*     */   }
/*     */ 
/*     */   public EntityReference createEntityReference(String name) throws DOMException
/*     */   {
/* 437 */     return this.document.createEntityReference(name);
/*     */   }
/*     */ 
/*     */   public NodeList getElementsByTagName(String tagname) {
/* 441 */     handleNewSource();
/* 442 */     return this.document.getElementsByTagName(tagname);
/*     */   }
/*     */ 
/*     */   public Node importNode(Node importedNode, boolean deep)
/*     */     throws DOMException
/*     */   {
/* 449 */     handleNewSource();
/* 450 */     return this.document.importNode(importedNode, deep);
/*     */   }
/*     */ 
/*     */   public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException
/*     */   {
/* 455 */     return this.document.createElementNS(namespaceURI, qualifiedName);
/*     */   }
/*     */ 
/*     */   public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException
/*     */   {
/* 460 */     return this.document.createAttributeNS(namespaceURI, qualifiedName);
/*     */   }
/*     */ 
/*     */   public NodeList getElementsByTagNameNS(String namespaceURI, String localName)
/*     */   {
/* 466 */     handleNewSource();
/* 467 */     return this.document.getElementsByTagNameNS(namespaceURI, localName);
/*     */   }
/*     */ 
/*     */   public Element getElementById(String elementId) {
/* 471 */     handleNewSource();
/* 472 */     return this.document.getElementById(elementId);
/*     */   }
/*     */ 
/*     */   public Node appendChild(Node newChild) throws DOMException {
/* 476 */     handleNewSource();
/* 477 */     return this.document.appendChild(newChild);
/*     */   }
/*     */ 
/*     */   public Node cloneNode(boolean deep) {
/* 481 */     handleNewSource();
/* 482 */     return this.document.cloneNode(deep);
/*     */   }
/*     */ 
/*     */   protected SOAPPartImpl doCloneNode() {
/* 486 */     handleNewSource();
/* 487 */     SOAPPartImpl newSoapPart = duplicateType();
/*     */ 
/* 489 */     newSoapPart.headers = MimeHeadersUtil.copy(this.headers);
/* 490 */     newSoapPart.source = this.source;
/* 491 */     return newSoapPart;
/*     */   }
/*     */ 
/*     */   public NamedNodeMap getAttributes() {
/* 495 */     return this.document.getAttributes();
/*     */   }
/*     */ 
/*     */   public NodeList getChildNodes() {
/* 499 */     handleNewSource();
/* 500 */     return this.document.getChildNodes();
/*     */   }
/*     */ 
/*     */   public Node getFirstChild() {
/* 504 */     handleNewSource();
/* 505 */     return this.document.getFirstChild();
/*     */   }
/*     */ 
/*     */   public Node getLastChild() {
/* 509 */     handleNewSource();
/* 510 */     return this.document.getLastChild();
/*     */   }
/*     */ 
/*     */   public String getLocalName() {
/* 514 */     return this.document.getLocalName();
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI() {
/* 518 */     return this.document.getNamespaceURI();
/*     */   }
/*     */ 
/*     */   public Node getNextSibling() {
/* 522 */     handleNewSource();
/* 523 */     return this.document.getNextSibling();
/*     */   }
/*     */ 
/*     */   public String getNodeName() {
/* 527 */     return this.document.getNodeName();
/*     */   }
/*     */ 
/*     */   public short getNodeType() {
/* 531 */     return this.document.getNodeType();
/*     */   }
/*     */ 
/*     */   public String getNodeValue() throws DOMException {
/* 535 */     return this.document.getNodeValue();
/*     */   }
/*     */ 
/*     */   public Document getOwnerDocument() {
/* 539 */     return this.document.getOwnerDocument();
/*     */   }
/*     */ 
/*     */   public Node getParentNode() {
/* 543 */     return this.document.getParentNode();
/*     */   }
/*     */ 
/*     */   public String getPrefix() {
/* 547 */     return this.document.getPrefix();
/*     */   }
/*     */ 
/*     */   public Node getPreviousSibling() {
/* 551 */     return this.document.getPreviousSibling();
/*     */   }
/*     */ 
/*     */   public boolean hasAttributes() {
/* 555 */     return this.document.hasAttributes();
/*     */   }
/*     */ 
/*     */   public boolean hasChildNodes() {
/* 559 */     handleNewSource();
/* 560 */     return this.document.hasChildNodes();
/*     */   }
/*     */ 
/*     */   public Node insertBefore(Node arg0, Node arg1)
/*     */     throws DOMException
/*     */   {
/* 567 */     handleNewSource();
/* 568 */     return this.document.insertBefore(arg0, arg1);
/*     */   }
/*     */ 
/*     */   public boolean isSupported(String arg0, String arg1) {
/* 572 */     return this.document.isSupported(arg0, arg1);
/*     */   }
/*     */ 
/*     */   public void normalize() {
/* 576 */     handleNewSource();
/* 577 */     this.document.normalize();
/*     */   }
/*     */ 
/*     */   public Node removeChild(Node arg0) throws DOMException
/*     */   {
/* 582 */     handleNewSource();
/* 583 */     return this.document.removeChild(arg0);
/*     */   }
/*     */ 
/*     */   public Node replaceChild(Node arg0, Node arg1)
/*     */     throws DOMException
/*     */   {
/* 590 */     handleNewSource();
/* 591 */     return this.document.replaceChild(arg0, arg1);
/*     */   }
/*     */ 
/*     */   public void setNodeValue(String arg0) throws DOMException {
/* 595 */     this.document.setNodeValue(arg0);
/*     */   }
/*     */ 
/*     */   public void setPrefix(String arg0) throws DOMException {
/* 599 */     this.document.setPrefix(arg0);
/*     */   }
/*     */ 
/*     */   private void handleNewSource() {
/* 603 */     if (this.sourceWasSet)
/*     */       try
/*     */       {
/* 606 */         getEnvelope();
/*     */       }
/*     */       catch (SOAPException e) {
/*     */       }
/*     */   }
/*     */ 
/*     */   protected XMLDeclarationParser lookForXmlDecl() throws SOAPException {
/* 613 */     if ((this.source != null) && ((this.source instanceof StreamSource)))
/*     */     {
/* 615 */       Reader reader = null;
/*     */ 
/* 617 */       InputStream inputStream = ((StreamSource)this.source).getInputStream();
/* 618 */       if (inputStream != null) {
/* 619 */         if (getSourceCharsetEncoding() == null)
/* 620 */           reader = new InputStreamReader(inputStream);
/*     */         else {
/*     */           try {
/* 623 */             reader = new InputStreamReader(inputStream, getSourceCharsetEncoding());
/*     */           }
/*     */           catch (UnsupportedEncodingException uee)
/*     */           {
/* 627 */             log.log(Level.SEVERE, "SAAJ0551.soap.unsupported.encoding", new Object[] { getSourceCharsetEncoding() });
/*     */ 
/* 631 */             throw new SOAPExceptionImpl("Unsupported encoding " + getSourceCharsetEncoding(), uee);
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 637 */         reader = ((StreamSource)this.source).getReader();
/*     */       }
/* 639 */       if (reader != null) {
/* 640 */         PushbackReader pushbackReader = new PushbackReader(reader, 4096);
/*     */ 
/* 642 */         XMLDeclarationParser ev = new XMLDeclarationParser(pushbackReader);
/*     */         try
/*     */         {
/* 645 */           ev.parse();
/*     */         } catch (Exception e) {
/* 647 */           log.log(Level.SEVERE, "SAAJ0552.soap.xml.decl.parsing.failed");
/*     */ 
/* 650 */           throw new SOAPExceptionImpl("XML declaration parsing failed", e);
/*     */         }
/*     */ 
/* 653 */         String xmlDecl = ev.getXmlDeclaration();
/* 654 */         if ((xmlDecl != null) && (xmlDecl.length() > 0)) {
/* 655 */           this.omitXmlDecl = false;
/*     */         }
/* 657 */         if (lazyContentLength) {
/* 658 */           this.source = new StreamSource(pushbackReader);
/*     */         }
/* 660 */         return ev;
/*     */       }
/* 662 */     } else if ((this.source == null) || (!(this.source instanceof DOMSource)));
/* 665 */     return null;
/*     */   }
/*     */ 
/*     */   public void setSourceCharsetEncoding(String charset) {
/* 669 */     this.sourceCharsetEncoding = charset;
/*     */   }
/*     */ 
/*     */   public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException
/*     */   {
/* 674 */     handleNewSource();
/* 675 */     return this.document.renameNode(n, namespaceURI, qualifiedName);
/*     */   }
/*     */ 
/*     */   public void normalizeDocument() {
/* 679 */     this.document.normalizeDocument();
/*     */   }
/*     */ 
/*     */   public DOMConfiguration getDomConfig() {
/* 683 */     return this.document.getDomConfig();
/*     */   }
/*     */ 
/*     */   public Node adoptNode(Node source) throws DOMException {
/* 687 */     handleNewSource();
/* 688 */     return this.document.adoptNode(source);
/*     */   }
/*     */ 
/*     */   public void setDocumentURI(String documentURI) {
/* 692 */     this.document.setDocumentURI(documentURI);
/*     */   }
/*     */ 
/*     */   public String getDocumentURI() {
/* 696 */     return this.document.getDocumentURI();
/*     */   }
/*     */ 
/*     */   public void setStrictErrorChecking(boolean strictErrorChecking) {
/* 700 */     this.document.setStrictErrorChecking(strictErrorChecking);
/*     */   }
/*     */ 
/*     */   public String getInputEncoding() {
/* 704 */     return this.document.getInputEncoding();
/*     */   }
/*     */ 
/*     */   public String getXmlEncoding() {
/* 708 */     return this.document.getXmlEncoding();
/*     */   }
/*     */ 
/*     */   public boolean getXmlStandalone() {
/* 712 */     return this.document.getXmlStandalone();
/*     */   }
/*     */ 
/*     */   public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
/* 716 */     this.document.setXmlStandalone(xmlStandalone);
/*     */   }
/*     */ 
/*     */   public String getXmlVersion() {
/* 720 */     return this.document.getXmlVersion();
/*     */   }
/*     */ 
/*     */   public void setXmlVersion(String xmlVersion) throws DOMException {
/* 724 */     this.document.setXmlVersion(xmlVersion);
/*     */   }
/*     */ 
/*     */   public boolean getStrictErrorChecking() {
/* 728 */     return this.document.getStrictErrorChecking();
/*     */   }
/*     */ 
/*     */   public String getBaseURI()
/*     */   {
/* 733 */     return this.document.getBaseURI();
/*     */   }
/*     */ 
/*     */   public short compareDocumentPosition(Node other) throws DOMException
/*     */   {
/* 738 */     return this.document.compareDocumentPosition(other);
/*     */   }
/*     */ 
/*     */   public String getTextContent() throws DOMException
/*     */   {
/* 743 */     return this.document.getTextContent();
/*     */   }
/*     */ 
/*     */   public void setTextContent(String textContent) throws DOMException {
/* 747 */     this.document.setTextContent(textContent);
/*     */   }
/*     */ 
/*     */   public boolean isSameNode(Node other) {
/* 751 */     return this.document.isSameNode(other);
/*     */   }
/*     */ 
/*     */   public String lookupPrefix(String namespaceURI) {
/* 755 */     return this.document.lookupPrefix(namespaceURI);
/*     */   }
/*     */ 
/*     */   public boolean isDefaultNamespace(String namespaceURI) {
/* 759 */     return this.document.isDefaultNamespace(namespaceURI);
/*     */   }
/*     */ 
/*     */   public String lookupNamespaceURI(String prefix) {
/* 763 */     return this.document.lookupNamespaceURI(prefix);
/*     */   }
/*     */ 
/*     */   public boolean isEqualNode(Node arg) {
/* 767 */     return this.document.isEqualNode(arg);
/*     */   }
/*     */ 
/*     */   public Object getFeature(String feature, String version)
/*     */   {
/* 772 */     return this.document.getFeature(feature, version);
/*     */   }
/*     */ 
/*     */   public Object setUserData(String key, Object data, UserDataHandler handler)
/*     */   {
/* 778 */     return this.document.setUserData(key, data, handler);
/*     */   }
/*     */ 
/*     */   public Object getUserData(String key) {
/* 782 */     return this.document.getUserData(key);
/*     */   }
/*     */ 
/*     */   public void recycleNode()
/*     */   {
/*     */   }
/*     */ 
/*     */   public String getValue() {
/* 790 */     return null;
/*     */   }
/*     */ 
/*     */   public void setValue(String value) {
/* 794 */     log.severe("SAAJ0571.soappart.setValue.not.defined");
/* 795 */     throw new IllegalStateException("Setting value of a soap part is not defined");
/*     */   }
/*     */ 
/*     */   public void setParentElement(SOAPElement parent) throws SOAPException {
/* 799 */     log.severe("SAAJ0570.soappart.parent.element.not.defined");
/* 800 */     throw new SOAPExceptionImpl("The parent element of a soap part is not defined");
/*     */   }
/*     */ 
/*     */   public SOAPElement getParentElement() {
/* 804 */     return null;
/*     */   }
/*     */ 
/*     */   public void detachNode()
/*     */   {
/*     */   }
/*     */ 
/*     */   public String getSourceCharsetEncoding() {
/* 812 */     return this.sourceCharsetEncoding;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.SOAPPartImpl
 * JD-Core Version:    0.6.2
 */