/*     */ package com.sun.xml.internal.txw2.output;
/*     */ 
/*     */ import com.sun.xml.internal.txw2.TxwException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Stack;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ 
/*     */ class Dom2SaxAdapter
/*     */   implements ContentHandler, LexicalHandler
/*     */ {
/*     */   private final Node _node;
/* 138 */   private final Stack _nodeStk = new Stack();
/*     */   private boolean inCDATA;
/*     */   private final Document _document;
/* 282 */   private ArrayList unprocessedNamespaces = new ArrayList();
/*     */ 
/*     */   public final Element getCurrentElement()
/*     */   {
/* 142 */     return (Element)this._nodeStk.peek();
/*     */   }
/*     */ 
/*     */   public Dom2SaxAdapter(Node node)
/*     */   {
/* 156 */     this._node = node;
/* 157 */     this._nodeStk.push(this._node);
/*     */ 
/* 159 */     if ((node instanceof Document))
/* 160 */       this._document = ((Document)node);
/*     */     else
/* 162 */       this._document = node.getOwnerDocument();
/*     */   }
/*     */ 
/*     */   public Dom2SaxAdapter()
/*     */     throws ParserConfigurationException
/*     */   {
/* 169 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/* 170 */     factory.setNamespaceAware(true);
/* 171 */     factory.setValidating(false);
/*     */ 
/* 173 */     this._document = factory.newDocumentBuilder().newDocument();
/* 174 */     this._node = this._document;
/* 175 */     this._nodeStk.push(this._document);
/*     */   }
/*     */ 
/*     */   public Node getDOM() {
/* 179 */     return this._node;
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startElement(String namespace, String localName, String qName, Attributes attrs)
/*     */   {
/* 192 */     Element element = this._document.createElementNS(namespace, qName);
/*     */ 
/* 194 */     if (element == null)
/*     */     {
/* 197 */       throw new TxwException("Your DOM provider doesn't support the createElementNS method properly");
/*     */     }
/*     */ 
/* 201 */     for (int i = 0; i < this.unprocessedNamespaces.size(); i += 2) {
/* 202 */       String prefix = (String)this.unprocessedNamespaces.get(i + 0);
/* 203 */       String uri = (String)this.unprocessedNamespaces.get(i + 1);
/*     */       String qname;
/*     */       String qname;
/* 206 */       if (("".equals(prefix)) || (prefix == null))
/* 207 */         qname = "xmlns";
/*     */       else {
/* 209 */         qname = "xmlns:" + prefix;
/*     */       }
/*     */ 
/* 214 */       if (element.hasAttributeNS("http://www.w3.org/2000/xmlns/", qname))
/*     */       {
/* 222 */         element.removeAttributeNS("http://www.w3.org/2000/xmlns/", qname);
/*     */       }
/*     */ 
/* 226 */       element.setAttributeNS("http://www.w3.org/2000/xmlns/", qname, uri);
/*     */     }
/* 228 */     this.unprocessedNamespaces.clear();
/*     */ 
/* 231 */     int length = attrs.getLength();
/* 232 */     for (int i = 0; i < length; i++) {
/* 233 */       String namespaceuri = attrs.getURI(i);
/* 234 */       String value = attrs.getValue(i);
/* 235 */       String qname = attrs.getQName(i);
/* 236 */       element.setAttributeNS(namespaceuri, qname, value);
/*     */     }
/*     */ 
/* 239 */     getParent().appendChild(element);
/*     */ 
/* 241 */     this._nodeStk.push(element);
/*     */   }
/*     */ 
/*     */   private final Node getParent() {
/* 245 */     return (Node)this._nodeStk.peek();
/*     */   }
/*     */ 
/*     */   public void endElement(String namespace, String localName, String qName) {
/* 249 */     this._nodeStk.pop();
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */   {
/*     */     Node text;
/*     */     Node text;
/* 255 */     if (this.inCDATA)
/* 256 */       text = this._document.createCDATASection(new String(ch, start, length));
/*     */     else
/* 258 */       text = this._document.createTextNode(new String(ch, start, length));
/* 259 */     getParent().appendChild(text);
/*     */   }
/*     */ 
/*     */   public void comment(char[] ch, int start, int length) throws SAXException {
/* 263 */     getParent().appendChild(this._document.createComment(new String(ch, start, length)));
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data) throws SAXException
/*     */   {
/* 272 */     Node node = this._document.createProcessingInstruction(target, data);
/* 273 */     getParent().appendChild(node);
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri) {
/* 285 */     this.unprocessedNamespaces.add(prefix);
/* 286 */     this.unprocessedNamespaces.add(uri);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix) {
/*     */   }
/*     */ 
/*     */   public void startDTD(String name, String publicId, String systemId) throws SAXException {
/*     */   }
/*     */ 
/*     */   public void endDTD() throws SAXException {
/*     */   }
/*     */ 
/*     */   public void startEntity(String name) throws SAXException {
/*     */   }
/*     */ 
/*     */   public void endEntity(String name) throws SAXException {
/*     */   }
/*     */ 
/*     */   public void startCDATA() throws SAXException {
/* 305 */     this.inCDATA = true;
/*     */   }
/*     */ 
/*     */   public void endCDATA() throws SAXException {
/* 309 */     this.inCDATA = false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.output.Dom2SaxAdapter
 * JD-Core Version:    0.6.2
 */