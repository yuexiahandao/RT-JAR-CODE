/*     */ package com.sun.xml.internal.bind.marshaller;
/*     */ 
/*     */ import com.sun.istack.internal.FinalArrayList;
/*     */ import com.sun.xml.internal.bind.util.Which;
/*     */ import java.util.Stack;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class SAX2DOMEx
/*     */   implements ContentHandler
/*     */ {
/*  57 */   private Node node = null;
/*     */   private boolean isConsolidate;
/*  59 */   protected final Stack<Node> nodeStack = new Stack();
/*  60 */   private final FinalArrayList<String> unprocessedNamespaces = new FinalArrayList();
/*     */   protected final Document document;
/*     */ 
/*     */   public SAX2DOMEx(Node node)
/*     */   {
/*  71 */     this(node, false);
/*     */   }
/*     */ 
/*     */   public SAX2DOMEx(Node node, boolean isConsolidate)
/*     */   {
/*  79 */     this.node = node;
/*  80 */     this.isConsolidate = isConsolidate;
/*  81 */     this.nodeStack.push(this.node);
/*     */ 
/*  83 */     if ((node instanceof Document))
/*  84 */       this.document = ((Document)node);
/*     */     else
/*  86 */       this.document = node.getOwnerDocument();
/*     */   }
/*     */ 
/*     */   public SAX2DOMEx()
/*     */     throws ParserConfigurationException
/*     */   {
/*  94 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  95 */     factory.setNamespaceAware(true);
/*  96 */     factory.setValidating(false);
/*     */ 
/*  98 */     this.document = factory.newDocumentBuilder().newDocument();
/*  99 */     this.node = this.document;
/* 100 */     this.nodeStack.push(this.document);
/*     */   }
/*     */ 
/*     */   public final Element getCurrentElement() {
/* 104 */     return (Element)this.nodeStack.peek();
/*     */   }
/*     */ 
/*     */   public Node getDOM() {
/* 108 */     return this.node;
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
/*     */   protected void namespace(Element element, String prefix, String uri)
/*     */   {
/*     */     String qname;
/*     */     String qname;
/* 119 */     if (("".equals(prefix)) || (prefix == null))
/* 120 */       qname = "xmlns";
/*     */     else {
/* 122 */       qname = "xmlns:" + prefix;
/*     */     }
/*     */ 
/* 128 */     if (element.hasAttributeNS("http://www.w3.org/2000/xmlns/", qname))
/*     */     {
/* 136 */       element.removeAttributeNS("http://www.w3.org/2000/xmlns/", qname);
/*     */     }
/*     */ 
/* 140 */     element.setAttributeNS("http://www.w3.org/2000/xmlns/", qname, uri);
/*     */   }
/*     */ 
/*     */   public void startElement(String namespace, String localName, String qName, Attributes attrs) {
/* 144 */     Node parent = (Node)this.nodeStack.peek();
/*     */ 
/* 148 */     Element element = this.document.createElementNS(namespace, qName);
/*     */ 
/* 150 */     if (element == null)
/*     */     {
/* 153 */       throw new AssertionError(Messages.format("SAX2DOMEx.DomImplDoesntSupportCreateElementNs", this.document.getClass().getName(), Which.which(this.document.getClass())));
/*     */     }
/*     */ 
/* 160 */     for (int i = 0; i < this.unprocessedNamespaces.size(); i += 2) {
/* 161 */       String prefix = (String)this.unprocessedNamespaces.get(i + 0);
/* 162 */       String uri = (String)this.unprocessedNamespaces.get(i + 1);
/*     */ 
/* 164 */       namespace(element, prefix, uri);
/*     */     }
/* 166 */     this.unprocessedNamespaces.clear();
/*     */ 
/* 169 */     if (attrs != null) {
/* 170 */       int length = attrs.getLength();
/* 171 */       for (int i = 0; i < length; i++) {
/* 172 */         String namespaceuri = attrs.getURI(i);
/* 173 */         String value = attrs.getValue(i);
/* 174 */         String qname = attrs.getQName(i);
/* 175 */         element.setAttributeNS(namespaceuri, qname, value);
/*     */       }
/*     */     }
/*     */ 
/* 179 */     parent.appendChild(element);
/*     */ 
/* 181 */     this.nodeStack.push(element);
/*     */   }
/*     */ 
/*     */   public void endElement(String namespace, String localName, String qName) {
/* 185 */     this.nodeStack.pop();
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length) {
/* 189 */     characters(new String(ch, start, length));
/*     */   }
/*     */ 
/*     */   protected Text characters(String s) {
/* 193 */     Node parent = (Node)this.nodeStack.peek();
/* 194 */     Node lastChild = parent.getLastChild();
/*     */     Text text;
/* 196 */     if ((this.isConsolidate) && (lastChild != null) && (lastChild.getNodeType() == 3)) {
/* 197 */       Text text = (Text)lastChild;
/* 198 */       text.appendData(s);
/*     */     } else {
/* 200 */       text = this.document.createTextNode(s);
/* 201 */       parent.appendChild(text);
/*     */     }
/* 203 */     return text;
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) {
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data) throws SAXException {
/* 210 */     Node parent = (Node)this.nodeStack.peek();
/* 211 */     Node n = this.document.createProcessingInstruction(target, data);
/* 212 */     parent.appendChild(n);
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator) {
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name) {
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri) {
/* 222 */     this.unprocessedNamespaces.add(prefix);
/* 223 */     this.unprocessedNamespaces.add(uri);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.marshaller.SAX2DOMEx
 * JD-Core Version:    0.6.2
 */