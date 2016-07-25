/*     */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.serializer.NamespaceMappings;
/*     */ import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
/*     */ import java.io.IOException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.ext.Locator2;
/*     */ 
/*     */ public class DOM2TO
/*     */   implements XMLReader, Locator2
/*     */ {
/*     */   private static final String EMPTYSTRING = "";
/*     */   private static final String XMLNS_PREFIX = "xmlns";
/*     */   private Node _dom;
/*     */   private SerializationHandler _handler;
/*  64 */   private String xmlVersion = null;
/*     */ 
/*  66 */   private String xmlEncoding = null;
/*     */ 
/*     */   public DOM2TO(Node root, SerializationHandler handler)
/*     */   {
/*  70 */     this._dom = root;
/*  71 */     this._handler = handler;
/*     */   }
/*     */ 
/*     */   public ContentHandler getContentHandler() {
/*  75 */     return null;
/*     */   }
/*     */ 
/*     */   public void setContentHandler(ContentHandler handler)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void parse(InputSource unused) throws IOException, SAXException {
/*  83 */     parse(this._dom);
/*     */   }
/*     */ 
/*     */   public void parse() throws IOException, SAXException
/*     */   {
/*  88 */     if (this._dom != null) {
/*  89 */       boolean isIncomplete = this._dom.getNodeType() != 9;
/*     */ 
/*  92 */       if (isIncomplete) {
/*  93 */         this._handler.startDocument();
/*  94 */         parse(this._dom);
/*  95 */         this._handler.endDocument();
/*     */       }
/*     */       else {
/*  98 */         parse(this._dom);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parse(Node node)
/*     */     throws IOException, SAXException
/*     */   {
/* 110 */     if (node == null)
/*     */       return;
/*     */     Node next;
/* 112 */     switch (node.getNodeType())
/*     */     {
/*     */     case 2:
/*     */     case 5:
/*     */     case 6:
/*     */     case 10:
/*     */     case 12:
/* 119 */       break;
/*     */     case 4:
/* 121 */       this._handler.startCDATA();
/* 122 */       this._handler.characters(node.getNodeValue());
/* 123 */       this._handler.endCDATA();
/* 124 */       break;
/*     */     case 8:
/* 127 */       this._handler.comment(node.getNodeValue());
/* 128 */       break;
/*     */     case 9:
/* 131 */       setDocumentInfo((Document)node);
/* 132 */       this._handler.setDocumentLocator(this);
/* 133 */       this._handler.startDocument();
/* 134 */       next = node.getFirstChild();
/* 135 */       while (next != null) {
/* 136 */         parse(next);
/* 137 */         next = next.getNextSibling();
/*     */       }
/* 139 */       this._handler.endDocument();
/* 140 */       break;
/*     */     case 11:
/* 143 */       next = node.getFirstChild();
/*     */     case 1:
/*     */     case 7:
/*     */     case 3:
/* 144 */       while (next != null) {
/* 145 */         parse(next);
/* 146 */         next = next.getNextSibling(); continue;
/*     */ 
/* 152 */         String qname = node.getNodeName();
/* 153 */         this._handler.startElement(null, null, qname);
/*     */ 
/* 157 */         NamedNodeMap map = node.getAttributes();
/* 158 */         int length = map.getLength();
/*     */ 
/* 161 */         for (int i = 0; i < length; i++) {
/* 162 */           Node attr = map.item(i);
/* 163 */           String qnameAttr = attr.getNodeName();
/*     */ 
/* 166 */           if (qnameAttr.startsWith("xmlns")) {
/* 167 */             String uriAttr = attr.getNodeValue();
/* 168 */             int colon = qnameAttr.lastIndexOf(':');
/* 169 */             String prefix = colon > 0 ? qnameAttr.substring(colon + 1) : "";
/*     */ 
/* 171 */             this._handler.namespaceAfterStartElement(prefix, uriAttr);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 176 */         NamespaceMappings nm = new NamespaceMappings();
/* 177 */         for (int i = 0; i < length; i++) {
/* 178 */           Node attr = map.item(i);
/* 179 */           String qnameAttr = attr.getNodeName();
/*     */ 
/* 182 */           if (!qnameAttr.startsWith("xmlns")) {
/* 183 */             String uriAttr = attr.getNamespaceURI();
/*     */ 
/* 185 */             if ((uriAttr != null) && (!uriAttr.equals(""))) {
/* 186 */               int colon = qnameAttr.lastIndexOf(':');
/*     */ 
/* 192 */               String newPrefix = nm.lookupPrefix(uriAttr);
/* 193 */               if (newPrefix == null)
/* 194 */                 newPrefix = nm.generateNextPrefix();
/* 195 */               String prefix = colon > 0 ? qnameAttr.substring(0, colon) : newPrefix;
/*     */ 
/* 197 */               this._handler.namespaceAfterStartElement(prefix, uriAttr);
/* 198 */               this._handler.addAttribute(prefix + ":" + qnameAttr, attr.getNodeValue());
/*     */             }
/*     */             else {
/* 201 */               this._handler.addAttribute(qnameAttr, attr.getNodeValue());
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 207 */         String uri = node.getNamespaceURI();
/* 208 */         String localName = node.getLocalName();
/*     */ 
/* 211 */         if (uri != null) {
/* 212 */           int colon = qname.lastIndexOf(':');
/* 213 */           String prefix = colon > 0 ? qname.substring(0, colon) : "";
/* 214 */           this._handler.namespaceAfterStartElement(prefix, uri);
/*     */         }
/* 221 */         else if ((uri == null) && (localName != null)) {
/* 222 */           String prefix = "";
/* 223 */           this._handler.namespaceAfterStartElement(prefix, "");
/*     */         }
/*     */ 
/* 228 */         next = node.getFirstChild();
/* 229 */         while (next != null) {
/* 230 */           parse(next);
/* 231 */           next = next.getNextSibling();
/*     */         }
/*     */ 
/* 235 */         this._handler.endElement(qname);
/* 236 */         break;
/*     */ 
/* 239 */         this._handler.processingInstruction(node.getNodeName(), node.getNodeValue());
/*     */ 
/* 241 */         break;
/*     */ 
/* 244 */         this._handler.characters(node.getNodeValue());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public DTDHandler getDTDHandler()
/*     */   {
/* 254 */     return null;
/*     */   }
/*     */ 
/*     */   public ErrorHandler getErrorHandler()
/*     */   {
/* 262 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean getFeature(String name)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 272 */     return false;
/*     */   }
/*     */ 
/*     */   public void setFeature(String name, boolean value)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void parse(String sysId)
/*     */     throws IOException, SAXException
/*     */   {
/* 289 */     throw new IOException("This method is not yet implemented.");
/*     */   }
/*     */ 
/*     */   public void setDTDHandler(DTDHandler handler)
/*     */     throws NullPointerException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(EntityResolver resolver)
/*     */     throws NullPointerException
/*     */   {
/*     */   }
/*     */ 
/*     */   public EntityResolver getEntityResolver()
/*     */   {
/* 313 */     return null;
/*     */   }
/*     */ 
/*     */   public void setErrorHandler(ErrorHandler handler)
/*     */     throws NullPointerException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 340 */     return null;
/*     */   }
/*     */ 
/*     */   public int getColumnNumber()
/*     */   {
/* 348 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 356 */     return 0;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 364 */     return null;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 372 */     return null;
/*     */   }
/*     */ 
/*     */   private void setDocumentInfo(Document document)
/*     */   {
/* 377 */     if (!document.getXmlStandalone())
/* 378 */       this._handler.setStandalone(Boolean.toString(document.getXmlStandalone()));
/* 379 */     setXMLVersion(document.getXmlVersion());
/* 380 */     setEncoding(document.getXmlEncoding());
/*     */   }
/*     */ 
/*     */   public String getXMLVersion() {
/* 384 */     return this.xmlVersion;
/*     */   }
/*     */ 
/*     */   private void setXMLVersion(String version) {
/* 388 */     if (version != null) {
/* 389 */       this.xmlVersion = version;
/* 390 */       this._handler.setVersion(this.xmlVersion);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getEncoding() {
/* 395 */     return this.xmlEncoding;
/*     */   }
/*     */ 
/*     */   private void setEncoding(String encoding) {
/* 399 */     if (encoding != null) {
/* 400 */       this.xmlEncoding = encoding;
/* 401 */       this._handler.setEncoding(encoding);
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getNodeTypeFromCode(short code)
/*     */   {
/* 407 */     String retval = null;
/* 408 */     switch (code) {
/*     */     case 2:
/* 410 */       retval = "ATTRIBUTE_NODE"; break;
/*     */     case 4:
/* 412 */       retval = "CDATA_SECTION_NODE"; break;
/*     */     case 8:
/* 414 */       retval = "COMMENT_NODE"; break;
/*     */     case 11:
/* 416 */       retval = "DOCUMENT_FRAGMENT_NODE"; break;
/*     */     case 9:
/* 418 */       retval = "DOCUMENT_NODE"; break;
/*     */     case 10:
/* 420 */       retval = "DOCUMENT_TYPE_NODE"; break;
/*     */     case 1:
/* 422 */       retval = "ELEMENT_NODE"; break;
/*     */     case 6:
/* 424 */       retval = "ENTITY_NODE"; break;
/*     */     case 5:
/* 426 */       retval = "ENTITY_REFERENCE_NODE"; break;
/*     */     case 12:
/* 428 */       retval = "NOTATION_NODE"; break;
/*     */     case 7:
/* 430 */       retval = "PROCESSING_INSTRUCTION_NODE"; break;
/*     */     case 3:
/* 432 */       retval = "TEXT_NODE";
/*     */     }
/* 434 */     return retval;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.DOM2TO
 * JD-Core Version:    0.6.2
 */