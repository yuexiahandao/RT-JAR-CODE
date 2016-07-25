/*     */ package com.sun.xml.internal.bind.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx;
/*     */ import java.util.Enumeration;
/*     */ import javax.xml.bind.ValidationEventLocator;
/*     */ import javax.xml.bind.helpers.ValidationEventLocatorImpl;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ import org.xml.sax.helpers.NamespaceSupport;
/*     */ 
/*     */ public class DOMScanner
/*     */   implements LocatorEx, InfosetScanner
/*     */ {
/*  65 */   private Node currentNode = null;
/*     */ 
/*  68 */   private final AttributesImpl atts = new AttributesImpl();
/*     */ 
/*  71 */   private ContentHandler receiver = null;
/*     */ 
/*  73 */   private Locator locator = this;
/*     */ 
/*     */   public void setLocator(Locator loc)
/*     */   {
/*  83 */     this.locator = loc;
/*     */   }
/*     */ 
/*     */   public void scan(Object node) throws SAXException {
/*  87 */     if ((node instanceof Document))
/*  88 */       scan((Document)node);
/*     */     else
/*  90 */       scan((Element)node);
/*     */   }
/*     */ 
/*     */   public void scan(Document doc) throws SAXException
/*     */   {
/*  95 */     scan(doc.getDocumentElement());
/*     */   }
/*     */ 
/*     */   public void scan(Element e) throws SAXException {
/*  99 */     setCurrentLocation(e);
/*     */ 
/* 101 */     this.receiver.setDocumentLocator(this.locator);
/* 102 */     this.receiver.startDocument();
/*     */ 
/* 104 */     NamespaceSupport nss = new NamespaceSupport();
/* 105 */     buildNamespaceSupport(nss, e.getParentNode());
/*     */ 
/* 107 */     for (Enumeration en = nss.getPrefixes(); en.hasMoreElements(); ) {
/* 108 */       String prefix = (String)en.nextElement();
/* 109 */       this.receiver.startPrefixMapping(prefix, nss.getURI(prefix));
/*     */     }
/*     */ 
/* 112 */     visit(e);
/*     */ 
/* 114 */     for (Enumeration en = nss.getPrefixes(); en.hasMoreElements(); ) {
/* 115 */       String prefix = (String)en.nextElement();
/* 116 */       this.receiver.endPrefixMapping(prefix);
/*     */     }
/*     */ 
/* 120 */     setCurrentLocation(e);
/* 121 */     this.receiver.endDocument();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void parse(Element e, ContentHandler handler)
/*     */     throws SAXException
/*     */   {
/* 133 */     this.receiver = handler;
/*     */ 
/* 135 */     setCurrentLocation(e);
/* 136 */     this.receiver.startDocument();
/*     */ 
/* 138 */     this.receiver.setDocumentLocator(this.locator);
/* 139 */     visit(e);
/*     */ 
/* 141 */     setCurrentLocation(e);
/* 142 */     this.receiver.endDocument();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void parseWithContext(Element e, ContentHandler handler)
/*     */     throws SAXException
/*     */   {
/* 153 */     setContentHandler(handler);
/* 154 */     scan(e);
/*     */   }
/*     */ 
/*     */   private void buildNamespaceSupport(NamespaceSupport nss, Node node)
/*     */   {
/* 161 */     if ((node == null) || (node.getNodeType() != 1)) {
/* 162 */       return;
/*     */     }
/* 164 */     buildNamespaceSupport(nss, node.getParentNode());
/*     */ 
/* 166 */     nss.pushContext();
/* 167 */     NamedNodeMap atts = node.getAttributes();
/* 168 */     for (int i = 0; i < atts.getLength(); i++) {
/* 169 */       Attr a = (Attr)atts.item(i);
/* 170 */       if ("xmlns".equals(a.getPrefix())) {
/* 171 */         nss.declarePrefix(a.getLocalName(), a.getValue());
/*     */       }
/* 174 */       else if ("xmlns".equals(a.getName()))
/* 175 */         nss.declarePrefix("", a.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void visit(Element e)
/*     */     throws SAXException
/*     */   {
/* 185 */     setCurrentLocation(e);
/* 186 */     NamedNodeMap attributes = e.getAttributes();
/*     */ 
/* 188 */     this.atts.clear();
/* 189 */     int len = attributes == null ? 0 : attributes.getLength();
/*     */ 
/* 191 */     for (int i = len - 1; i >= 0; i--) {
/* 192 */       Attr a = (Attr)attributes.item(i);
/* 193 */       String name = a.getName();
/*     */ 
/* 195 */       if (name.startsWith("xmlns")) {
/* 196 */         if (name.length() == 5) {
/* 197 */           this.receiver.startPrefixMapping("", a.getValue());
/*     */         } else {
/* 199 */           String localName = a.getLocalName();
/* 200 */           if (localName == null)
/*     */           {
/* 202 */             localName = name.substring(6);
/*     */           }
/* 204 */           this.receiver.startPrefixMapping(localName, a.getValue());
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 209 */         String uri = a.getNamespaceURI();
/* 210 */         if (uri == null) uri = "";
/*     */ 
/* 212 */         String local = a.getLocalName();
/* 213 */         if (local == null) local = a.getName();
/*     */ 
/* 216 */         this.atts.addAttribute(uri, local, a.getName(), "CDATA", a.getValue());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 224 */     String uri = e.getNamespaceURI();
/* 225 */     if (uri == null) uri = "";
/* 226 */     String local = e.getLocalName();
/* 227 */     String qname = e.getTagName();
/* 228 */     if (local == null) local = qname;
/* 229 */     this.receiver.startElement(uri, local, qname, this.atts);
/*     */ 
/* 232 */     NodeList children = e.getChildNodes();
/* 233 */     int clen = children.getLength();
/* 234 */     for (int i = 0; i < clen; i++) {
/* 235 */       visit(children.item(i));
/*     */     }
/*     */ 
/* 239 */     setCurrentLocation(e);
/* 240 */     this.receiver.endElement(uri, local, qname);
/*     */ 
/* 243 */     for (int i = len - 1; i >= 0; i--) {
/* 244 */       Attr a = (Attr)attributes.item(i);
/* 245 */       String name = a.getName();
/* 246 */       if (name.startsWith("xmlns"))
/* 247 */         if (name.length() == 5)
/* 248 */           this.receiver.endPrefixMapping("");
/*     */         else
/* 250 */           this.receiver.endPrefixMapping(a.getLocalName());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void visit(Node n) throws SAXException
/*     */   {
/* 256 */     setCurrentLocation(n);
/*     */ 
/* 259 */     switch (n.getNodeType()) {
/*     */     case 3:
/*     */     case 4:
/* 262 */       String value = n.getNodeValue();
/* 263 */       this.receiver.characters(value.toCharArray(), 0, value.length());
/* 264 */       break;
/*     */     case 1:
/* 266 */       visit((Element)n);
/* 267 */       break;
/*     */     case 5:
/* 269 */       this.receiver.skippedEntity(n.getNodeName());
/* 270 */       break;
/*     */     case 7:
/* 272 */       ProcessingInstruction pi = (ProcessingInstruction)n;
/* 273 */       this.receiver.processingInstruction(pi.getTarget(), pi.getData());
/*     */     case 2:
/*     */     case 6:
/*     */     }
/*     */   }
/*     */ 
/* 279 */   private void setCurrentLocation(Node currNode) { this.currentNode = currNode; }
/*     */ 
/*     */ 
/*     */   public Node getCurrentLocation()
/*     */   {
/* 287 */     return this.currentNode;
/*     */   }
/*     */ 
/*     */   public Object getCurrentElement() {
/* 291 */     return this.currentNode;
/*     */   }
/*     */ 
/*     */   public LocatorEx getLocator() {
/* 295 */     return this;
/*     */   }
/*     */ 
/*     */   public void setContentHandler(ContentHandler handler) {
/* 299 */     this.receiver = handler;
/*     */   }
/*     */ 
/*     */   public ContentHandler getContentHandler() {
/* 303 */     return this.receiver;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 308 */     return null; } 
/* 309 */   public String getSystemId() { return null; } 
/* 310 */   public int getLineNumber() { return -1; } 
/* 311 */   public int getColumnNumber() { return -1; }
/*     */ 
/*     */   public ValidationEventLocator getLocation() {
/* 314 */     return new ValidationEventLocatorImpl(getCurrentLocation());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.unmarshaller.DOMScanner
 * JD-Core Version:    0.6.2
 */