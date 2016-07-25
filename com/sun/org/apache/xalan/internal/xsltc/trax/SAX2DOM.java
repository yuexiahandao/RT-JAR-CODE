/*     */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.Constants;
/*     */ import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.ext.Locator2;
/*     */ 
/*     */ public class SAX2DOM
/*     */   implements ContentHandler, LexicalHandler, Constants
/*     */ {
/*  56 */   private Node _root = null;
/*  57 */   private Document _document = null;
/*  58 */   private Node _nextSibling = null;
/*  59 */   private Stack _nodeStk = new Stack();
/*  60 */   private Vector _namespaceDecls = null;
/*  61 */   private Node _lastSibling = null;
/*  62 */   private Locator locator = null;
/*  63 */   private boolean needToSetDocumentInfo = true;
/*     */ 
/*  66 */   private StringBuilder _textBuffer = new StringBuilder();
/*  67 */   private Node _nextSiblingCache = null;
/*     */ 
/*  73 */   private DocumentBuilderFactory _factory = DocumentBuilderFactory.newInstance();
/*     */ 
/*  75 */   private boolean _internal = true;
/*     */ 
/*     */   public SAX2DOM(boolean useServicesMachnism) throws ParserConfigurationException {
/*  78 */     this._document = createDocument(useServicesMachnism);
/*  79 */     this._root = this._document;
/*     */   }
/*     */ 
/*     */   public SAX2DOM(Node root, Node nextSibling, boolean useServicesMachnism) throws ParserConfigurationException {
/*  83 */     this._root = root;
/*  84 */     if ((root instanceof Document)) {
/*  85 */       this._document = ((Document)root);
/*     */     }
/*  87 */     else if (root != null) {
/*  88 */       this._document = root.getOwnerDocument();
/*     */     }
/*     */     else {
/*  91 */       this._document = createDocument(useServicesMachnism);
/*  92 */       this._root = this._document;
/*     */     }
/*     */ 
/*  95 */     this._nextSibling = nextSibling;
/*     */   }
/*     */ 
/*     */   public SAX2DOM(Node root, boolean useServicesMachnism) throws ParserConfigurationException {
/*  99 */     this(root, null, useServicesMachnism);
/*     */   }
/*     */ 
/*     */   public Node getDOM() {
/* 103 */     return this._root;
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */   {
/* 108 */     if (length == 0) {
/* 109 */       return;
/*     */     }
/*     */ 
/* 112 */     Node last = (Node)this._nodeStk.peek();
/*     */ 
/* 115 */     if (last != this._document) {
/* 116 */       this._nextSiblingCache = this._nextSibling;
/* 117 */       this._textBuffer.append(ch, start, length);
/*     */     }
/*     */   }
/*     */ 
/* 121 */   private void appendTextNode() { if (this._textBuffer.length() > 0) {
/* 122 */       Node last = (Node)this._nodeStk.peek();
/* 123 */       if ((last == this._root) && (this._nextSiblingCache != null)) {
/* 124 */         this._lastSibling = last.insertBefore(this._document.createTextNode(this._textBuffer.toString()), this._nextSiblingCache);
/*     */       }
/*     */       else {
/* 127 */         this._lastSibling = last.appendChild(this._document.createTextNode(this._textBuffer.toString()));
/*     */       }
/* 129 */       this._textBuffer.setLength(0);
/*     */     } }
/*     */ 
/*     */   public void startDocument() {
/* 133 */     this._nodeStk.push(this._root);
/*     */   }
/*     */ 
/*     */   public void endDocument() {
/* 137 */     this._nodeStk.pop();
/*     */   }
/*     */ 
/*     */   private void setDocumentInfo()
/*     */   {
/* 142 */     if (this.locator == null) return; try
/*     */     {
/* 144 */       this._document.setXmlVersion(((Locator2)this.locator).getXMLVersion());
/*     */     }
/*     */     catch (ClassCastException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startElement(String namespace, String localName, String qName, Attributes attrs) {
/* 152 */     appendTextNode();
/* 153 */     if (this.needToSetDocumentInfo) {
/* 154 */       setDocumentInfo();
/* 155 */       this.needToSetDocumentInfo = false;
/*     */     }
/*     */ 
/* 158 */     Element tmp = this._document.createElementNS(namespace, qName);
/*     */ 
/* 161 */     if (this._namespaceDecls != null) {
/* 162 */       int nDecls = this._namespaceDecls.size();
/* 163 */       for (int i = 0; i < nDecls; i++) {
/* 164 */         String prefix = (String)this._namespaceDecls.elementAt(i++);
/*     */ 
/* 166 */         if ((prefix == null) || (prefix.equals(""))) {
/* 167 */           tmp.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", (String)this._namespaceDecls.elementAt(i));
/*     */         }
/*     */         else
/*     */         {
/* 171 */           tmp.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix, (String)this._namespaceDecls.elementAt(i));
/*     */         }
/*     */       }
/*     */ 
/* 175 */       this._namespaceDecls.clear();
/*     */     }
/*     */ 
/* 192 */     int nattrs = attrs.getLength();
/* 193 */     for (int i = 0; i < nattrs; i++)
/*     */     {
/* 195 */       String attQName = attrs.getQName(i);
/* 196 */       String attURI = attrs.getURI(i);
/* 197 */       if (attrs.getLocalName(i).equals("")) {
/* 198 */         tmp.setAttribute(attQName, attrs.getValue(i));
/* 199 */         if (attrs.getType(i).equals("ID"))
/* 200 */           tmp.setIdAttribute(attQName, true);
/*     */       }
/*     */       else {
/* 203 */         tmp.setAttributeNS(attURI, attQName, attrs.getValue(i));
/* 204 */         if (attrs.getType(i).equals("ID")) {
/* 205 */           tmp.setIdAttributeNS(attURI, attrs.getLocalName(i), true);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 212 */     Node last = (Node)this._nodeStk.peek();
/*     */ 
/* 216 */     if ((last == this._root) && (this._nextSibling != null))
/* 217 */       last.insertBefore(tmp, this._nextSibling);
/*     */     else {
/* 219 */       last.appendChild(tmp);
/*     */     }
/*     */ 
/* 222 */     this._nodeStk.push(tmp);
/* 223 */     this._lastSibling = null;
/*     */   }
/*     */ 
/*     */   public void endElement(String namespace, String localName, String qName) {
/* 227 */     appendTextNode();
/* 228 */     this._nodeStk.pop();
/* 229 */     this._lastSibling = null;
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri) {
/* 233 */     if (this._namespaceDecls == null) {
/* 234 */       this._namespaceDecls = new Vector(2);
/*     */     }
/* 236 */     this._namespaceDecls.addElement(prefix);
/* 237 */     this._namespaceDecls.addElement(uri);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */   {
/* 255 */     appendTextNode();
/* 256 */     Node last = (Node)this._nodeStk.peek();
/* 257 */     ProcessingInstruction pi = this._document.createProcessingInstruction(target, data);
/*     */ 
/* 259 */     if (pi != null) {
/* 260 */       if ((last == this._root) && (this._nextSibling != null))
/* 261 */         last.insertBefore(pi, this._nextSibling);
/*     */       else {
/* 263 */         last.appendChild(pi);
/*     */       }
/* 265 */       this._lastSibling = pi;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/* 274 */     this.locator = locator;
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void comment(char[] ch, int start, int length)
/*     */   {
/* 289 */     appendTextNode();
/* 290 */     Node last = (Node)this._nodeStk.peek();
/* 291 */     Comment comment = this._document.createComment(new String(ch, start, length));
/* 292 */     if (comment != null) {
/* 293 */       if ((last == this._root) && (this._nextSibling != null))
/* 294 */         last.insertBefore(comment, this._nextSibling);
/*     */       else {
/* 296 */         last.appendChild(comment);
/*     */       }
/* 298 */       this._lastSibling = comment;
/*     */     }
/*     */   }
/*     */   public void startCDATA() {
/*     */   }
/*     */   public void endCDATA() {
/*     */   }
/*     */   public void startEntity(String name) {
/*     */   }
/*     */   public void endDTD() {
/*     */   }
/*     */   public void endEntity(String name) {
/*     */   }
/*     */   public void startDTD(String name, String publicId, String systemId) throws SAXException {  }
/*     */ 
/* 312 */   private Document createDocument(boolean useServicesMachnism) throws ParserConfigurationException { if (this._factory == null) {
/* 313 */       if (useServicesMachnism)
/* 314 */         this._factory = DocumentBuilderFactory.newInstance();
/* 315 */       if (!(this._factory instanceof DocumentBuilderFactoryImpl)) {
/* 316 */         this._internal = false;
/*     */       }
/*     */       else
/* 319 */         this._factory = DocumentBuilderFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl", SAX2DOM.class.getClassLoader());
/*     */     }
/*     */     Document doc;
/*     */     Document doc;
/* 325 */     if (this._internal)
/*     */     {
/* 327 */       doc = this._factory.newDocumentBuilder().newDocument();
/*     */     }
/* 329 */     else synchronized (SAX2DOM.class) {
/* 330 */         doc = this._factory.newDocumentBuilder().newDocument();
/*     */       }
/*     */ 
/* 333 */     return doc;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.SAX2DOM
 * JD-Core Version:    0.6.2
 */