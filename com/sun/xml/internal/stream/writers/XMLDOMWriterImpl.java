/*     */ package com.sun.xml.internal.stream.writers;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.CDATASection;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.EntityReference;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.helpers.NamespaceSupport;
/*     */ 
/*     */ public class XMLDOMWriterImpl
/*     */   implements XMLStreamWriter
/*     */ {
/*  63 */   private Document ownerDoc = null;
/*  64 */   private Node currentNode = null;
/*  65 */   private Node node = null;
/*  66 */   private NamespaceSupport namespaceContext = null;
/*  67 */   private Method mXmlVersion = null;
/*  68 */   private boolean[] needContextPop = null;
/*  69 */   private StringBuffer stringBuffer = null;
/*  70 */   private int resizeValue = 20;
/*  71 */   private int depth = 0;
/*     */ 
/*     */   public XMLDOMWriterImpl(DOMResult result)
/*     */   {
/*  78 */     this.node = result.getNode();
/*  79 */     if (this.node.getNodeType() == 9) {
/*  80 */       this.ownerDoc = ((Document)this.node);
/*  81 */       this.currentNode = this.ownerDoc;
/*     */     } else {
/*  83 */       this.ownerDoc = this.node.getOwnerDocument();
/*  84 */       this.currentNode = this.node;
/*     */     }
/*  86 */     getDLThreeMethods();
/*  87 */     this.stringBuffer = new StringBuffer();
/*  88 */     this.needContextPop = new boolean[this.resizeValue];
/*  89 */     this.namespaceContext = new NamespaceSupport();
/*     */   }
/*     */ 
/*     */   private void getDLThreeMethods() {
/*     */     try {
/*  94 */       this.mXmlVersion = this.ownerDoc.getClass().getMethod("setXmlVersion", new Class[] { String.class });
/*     */     }
/*     */     catch (NoSuchMethodException mex) {
/*  97 */       this.mXmlVersion = null;
/*     */     }
/*     */     catch (SecurityException se) {
/* 100 */       this.mXmlVersion = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws XMLStreamException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws XMLStreamException
/*     */   {
/*     */   }
/*     */ 
/*     */   public NamespaceContext getNamespaceContext()
/*     */   {
/* 126 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPrefix(String namespaceURI)
/*     */     throws XMLStreamException
/*     */   {
/* 136 */     String prefix = null;
/* 137 */     if (this.namespaceContext != null) {
/* 138 */       prefix = this.namespaceContext.getPrefix(namespaceURI);
/*     */     }
/* 140 */     return prefix;
/*     */   }
/*     */ 
/*     */   public Object getProperty(String str)
/*     */     throws IllegalArgumentException
/*     */   {
/* 150 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void setDefaultNamespace(String uri)
/*     */     throws XMLStreamException
/*     */   {
/* 159 */     this.namespaceContext.declarePrefix("", uri);
/* 160 */     if (this.needContextPop[this.depth] == 0)
/* 161 */       this.needContextPop[this.depth] = true;
/*     */   }
/*     */ 
/*     */   public void setNamespaceContext(NamespaceContext namespaceContext)
/*     */     throws XMLStreamException
/*     */   {
/* 171 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void setPrefix(String prefix, String uri)
/*     */     throws XMLStreamException
/*     */   {
/* 181 */     if (prefix == null) {
/* 182 */       throw new XMLStreamException("Prefix cannot be null");
/*     */     }
/* 184 */     this.namespaceContext.declarePrefix(prefix, uri);
/* 185 */     if (this.needContextPop[this.depth] == 0)
/* 186 */       this.needContextPop[this.depth] = true;
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String localName, String value)
/*     */     throws XMLStreamException
/*     */   {
/* 198 */     if (this.currentNode.getNodeType() == 1) {
/* 199 */       Attr attr = this.ownerDoc.createAttribute(localName);
/* 200 */       attr.setValue(value);
/* 201 */       ((Element)this.currentNode).setAttributeNode(attr);
/*     */     }
/*     */     else {
/* 204 */       throw new IllegalStateException("Current DOM Node type  is " + this.currentNode.getNodeType() + "and does not allow attributes to be set ");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String namespaceURI, String localName, String value)
/*     */     throws XMLStreamException
/*     */   {
/* 217 */     if (this.currentNode.getNodeType() == 1) {
/* 218 */       String prefix = null;
/* 219 */       if (namespaceURI == null) {
/* 220 */         throw new XMLStreamException("NamespaceURI cannot be null");
/*     */       }
/* 222 */       if (localName == null) {
/* 223 */         throw new XMLStreamException("Local name cannot be null");
/*     */       }
/* 225 */       if (this.namespaceContext != null) {
/* 226 */         prefix = this.namespaceContext.getPrefix(namespaceURI);
/*     */       }
/*     */ 
/* 229 */       if (prefix == null) {
/* 230 */         throw new XMLStreamException("Namespace URI " + namespaceURI + "is not bound to any prefix");
/*     */       }
/*     */ 
/* 234 */       String qualifiedName = null;
/* 235 */       if (prefix.equals(""))
/* 236 */         qualifiedName = localName;
/*     */       else {
/* 238 */         qualifiedName = getQName(prefix, localName);
/*     */       }
/* 240 */       Attr attr = this.ownerDoc.createAttributeNS(namespaceURI, qualifiedName);
/* 241 */       attr.setValue(value);
/* 242 */       ((Element)this.currentNode).setAttributeNode(attr);
/*     */     }
/*     */     else {
/* 245 */       throw new IllegalStateException("Current DOM Node type  is " + this.currentNode.getNodeType() + "and does not allow attributes to be set ");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String prefix, String namespaceURI, String localName, String value)
/*     */     throws XMLStreamException
/*     */   {
/* 259 */     if (this.currentNode.getNodeType() == 1) {
/* 260 */       if (namespaceURI == null) {
/* 261 */         throw new XMLStreamException("NamespaceURI cannot be null");
/*     */       }
/* 263 */       if (localName == null) {
/* 264 */         throw new XMLStreamException("Local name cannot be null");
/*     */       }
/* 266 */       if (prefix == null) {
/* 267 */         throw new XMLStreamException("prefix cannot be null");
/*     */       }
/* 269 */       String qualifiedName = null;
/* 270 */       if (prefix.equals("")) {
/* 271 */         qualifiedName = localName;
/*     */       }
/*     */       else {
/* 274 */         qualifiedName = getQName(prefix, localName);
/*     */       }
/* 276 */       Attr attr = this.ownerDoc.createAttributeNS(namespaceURI, qualifiedName);
/* 277 */       attr.setValue(value);
/* 278 */       ((Element)this.currentNode).setAttributeNodeNS(attr);
/*     */     }
/*     */     else {
/* 281 */       throw new IllegalStateException("Current DOM Node type  is " + this.currentNode.getNodeType() + "and does not allow attributes to be set ");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeCData(String data)
/*     */     throws XMLStreamException
/*     */   {
/* 293 */     if (data == null) {
/* 294 */       throw new XMLStreamException("CDATA cannot be null");
/*     */     }
/*     */ 
/* 297 */     CDATASection cdata = this.ownerDoc.createCDATASection(data);
/* 298 */     getNode().appendChild(cdata);
/*     */   }
/*     */ 
/*     */   public void writeCharacters(String charData)
/*     */     throws XMLStreamException
/*     */   {
/* 308 */     Text text = this.ownerDoc.createTextNode(charData);
/* 309 */     this.currentNode.appendChild(text);
/*     */   }
/*     */ 
/*     */   public void writeCharacters(char[] values, int param, int param2)
/*     */     throws XMLStreamException
/*     */   {
/* 322 */     Text text = this.ownerDoc.createTextNode(new String(values, param, param2));
/* 323 */     this.currentNode.appendChild(text);
/*     */   }
/*     */ 
/*     */   public void writeComment(String str)
/*     */     throws XMLStreamException
/*     */   {
/* 333 */     Comment comment = this.ownerDoc.createComment(str);
/* 334 */     getNode().appendChild(comment);
/*     */   }
/*     */ 
/*     */   public void writeDTD(String str)
/*     */     throws XMLStreamException
/*     */   {
/* 343 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void writeDefaultNamespace(String namespaceURI)
/*     */     throws XMLStreamException
/*     */   {
/* 352 */     if (this.currentNode.getNodeType() == 1) {
/* 353 */       String qname = "xmlns";
/* 354 */       ((Element)this.currentNode).setAttributeNS("http://www.w3.org/2000/xmlns/", qname, namespaceURI);
/*     */     }
/*     */     else {
/* 357 */       throw new IllegalStateException("Current DOM Node type  is " + this.currentNode.getNodeType() + "and does not allow attributes to be set ");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String localName)
/*     */     throws XMLStreamException
/*     */   {
/* 368 */     if (this.ownerDoc != null) {
/* 369 */       Element element = this.ownerDoc.createElement(localName);
/* 370 */       if (this.currentNode != null)
/* 371 */         this.currentNode.appendChild(element);
/*     */       else
/* 373 */         this.ownerDoc.appendChild(element);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String namespaceURI, String localName)
/*     */     throws XMLStreamException
/*     */   {
/* 386 */     if (this.ownerDoc != null) {
/* 387 */       String qualifiedName = null;
/* 388 */       String prefix = null;
/* 389 */       if (namespaceURI == null) {
/* 390 */         throw new XMLStreamException("NamespaceURI cannot be null");
/*     */       }
/* 392 */       if (localName == null) {
/* 393 */         throw new XMLStreamException("Local name cannot be null");
/*     */       }
/*     */ 
/* 396 */       if (this.namespaceContext != null) {
/* 397 */         prefix = this.namespaceContext.getPrefix(namespaceURI);
/*     */       }
/* 399 */       if (prefix == null) {
/* 400 */         throw new XMLStreamException("Namespace URI " + namespaceURI + "is not bound to any prefix");
/*     */       }
/*     */ 
/* 403 */       if ("".equals(prefix)) {
/* 404 */         qualifiedName = localName;
/*     */       }
/*     */       else {
/* 407 */         qualifiedName = getQName(prefix, localName);
/*     */       }
/*     */ 
/* 410 */       Element element = this.ownerDoc.createElementNS(namespaceURI, qualifiedName);
/* 411 */       if (this.currentNode != null)
/* 412 */         this.currentNode.appendChild(element);
/*     */       else
/* 414 */         this.ownerDoc.appendChild(element);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String prefix, String localName, String namespaceURI)
/*     */     throws XMLStreamException
/*     */   {
/* 428 */     if (this.ownerDoc != null) {
/* 429 */       if (namespaceURI == null) {
/* 430 */         throw new XMLStreamException("NamespaceURI cannot be null");
/*     */       }
/* 432 */       if (localName == null) {
/* 433 */         throw new XMLStreamException("Local name cannot be null");
/*     */       }
/* 435 */       if (prefix == null) {
/* 436 */         throw new XMLStreamException("Prefix cannot be null");
/*     */       }
/* 438 */       String qualifiedName = null;
/* 439 */       if ("".equals(prefix))
/* 440 */         qualifiedName = localName;
/*     */       else {
/* 442 */         qualifiedName = getQName(prefix, localName);
/*     */       }
/* 444 */       Element el = this.ownerDoc.createElementNS(namespaceURI, qualifiedName);
/* 445 */       if (this.currentNode != null)
/* 446 */         this.currentNode.appendChild(el);
/*     */       else
/* 448 */         this.ownerDoc.appendChild(el);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeEndDocument()
/*     */     throws XMLStreamException
/*     */   {
/* 460 */     this.currentNode = null;
/* 461 */     for (int i = 0; i < this.depth; i++) {
/* 462 */       if (this.needContextPop[this.depth] != 0) {
/* 463 */         this.needContextPop[this.depth] = false;
/* 464 */         this.namespaceContext.popContext();
/*     */       }
/* 466 */       this.depth -= 1;
/*     */     }
/* 468 */     this.depth = 0;
/*     */   }
/*     */ 
/*     */   public void writeEndElement()
/*     */     throws XMLStreamException
/*     */   {
/* 476 */     Node node = this.currentNode.getParentNode();
/* 477 */     if (this.currentNode.getNodeType() == 9)
/* 478 */       this.currentNode = null;
/*     */     else {
/* 480 */       this.currentNode = node;
/*     */     }
/* 482 */     if (this.needContextPop[this.depth] != 0) {
/* 483 */       this.needContextPop[this.depth] = false;
/* 484 */       this.namespaceContext.popContext();
/*     */     }
/* 486 */     this.depth -= 1;
/*     */   }
/*     */ 
/*     */   public void writeEntityRef(String name)
/*     */     throws XMLStreamException
/*     */   {
/* 495 */     EntityReference er = this.ownerDoc.createEntityReference(name);
/* 496 */     this.currentNode.appendChild(er);
/*     */   }
/*     */ 
/*     */   public void writeNamespace(String prefix, String namespaceURI)
/*     */     throws XMLStreamException
/*     */   {
/* 508 */     if (prefix == null) {
/* 509 */       throw new XMLStreamException("prefix cannot be null");
/*     */     }
/*     */ 
/* 512 */     if (namespaceURI == null) {
/* 513 */       throw new XMLStreamException("NamespaceURI cannot be null");
/*     */     }
/*     */ 
/* 516 */     String qname = null;
/*     */ 
/* 518 */     if (prefix.equals(""))
/* 519 */       qname = "xmlns";
/*     */     else {
/* 521 */       qname = getQName("xmlns", prefix);
/*     */     }
/*     */ 
/* 524 */     ((Element)this.currentNode).setAttributeNS("http://www.w3.org/2000/xmlns/", qname, namespaceURI);
/*     */   }
/*     */ 
/*     */   public void writeProcessingInstruction(String target)
/*     */     throws XMLStreamException
/*     */   {
/* 533 */     if (target == null) {
/* 534 */       throw new XMLStreamException("Target cannot be null");
/*     */     }
/* 536 */     ProcessingInstruction pi = this.ownerDoc.createProcessingInstruction(target, "");
/* 537 */     this.currentNode.appendChild(pi);
/*     */   }
/*     */ 
/*     */   public void writeProcessingInstruction(String target, String data)
/*     */     throws XMLStreamException
/*     */   {
/* 547 */     if (target == null) {
/* 548 */       throw new XMLStreamException("Target cannot be null");
/*     */     }
/* 550 */     ProcessingInstruction pi = this.ownerDoc.createProcessingInstruction(target, data);
/* 551 */     this.currentNode.appendChild(pi);
/*     */   }
/*     */ 
/*     */   public void writeStartDocument()
/*     */     throws XMLStreamException
/*     */   {
/*     */     try
/*     */     {
/* 561 */       if (this.mXmlVersion != null)
/* 562 */         this.mXmlVersion.invoke(this.ownerDoc, new Object[] { "1.0" });
/*     */     }
/*     */     catch (IllegalAccessException iae) {
/* 565 */       throw new XMLStreamException(iae);
/*     */     } catch (InvocationTargetException ite) {
/* 567 */       throw new XMLStreamException(ite);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeStartDocument(String version)
/*     */     throws XMLStreamException
/*     */   {
/*     */     try
/*     */     {
/* 579 */       if (this.mXmlVersion != null)
/* 580 */         this.mXmlVersion.invoke(this.ownerDoc, new Object[] { version });
/*     */     }
/*     */     catch (IllegalAccessException iae) {
/* 583 */       throw new XMLStreamException(iae);
/*     */     } catch (InvocationTargetException ite) {
/* 585 */       throw new XMLStreamException(ite);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeStartDocument(String encoding, String version)
/*     */     throws XMLStreamException
/*     */   {
/*     */     try
/*     */     {
/* 598 */       if (this.mXmlVersion != null)
/* 599 */         this.mXmlVersion.invoke(this.ownerDoc, new Object[] { version });
/*     */     }
/*     */     catch (IllegalAccessException iae) {
/* 602 */       throw new XMLStreamException(iae);
/*     */     } catch (InvocationTargetException ite) {
/* 604 */       throw new XMLStreamException(ite);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String localName)
/*     */     throws XMLStreamException
/*     */   {
/* 615 */     if (this.ownerDoc != null) {
/* 616 */       Element element = this.ownerDoc.createElement(localName);
/* 617 */       if (this.currentNode != null)
/* 618 */         this.currentNode.appendChild(element);
/*     */       else {
/* 620 */         this.ownerDoc.appendChild(element);
/*     */       }
/* 622 */       this.currentNode = element;
/*     */     }
/* 624 */     if (this.needContextPop[this.depth] != 0) {
/* 625 */       this.namespaceContext.pushContext();
/*     */     }
/* 627 */     incDepth();
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String namespaceURI, String localName)
/*     */     throws XMLStreamException
/*     */   {
/* 637 */     if (this.ownerDoc != null) {
/* 638 */       String qualifiedName = null;
/* 639 */       String prefix = null;
/*     */ 
/* 641 */       if (namespaceURI == null) {
/* 642 */         throw new XMLStreamException("NamespaceURI cannot be null");
/*     */       }
/* 644 */       if (localName == null) {
/* 645 */         throw new XMLStreamException("Local name cannot be null");
/*     */       }
/*     */ 
/* 648 */       if (this.namespaceContext != null) {
/* 649 */         prefix = this.namespaceContext.getPrefix(namespaceURI);
/*     */       }
/* 651 */       if (prefix == null) {
/* 652 */         throw new XMLStreamException("Namespace URI " + namespaceURI + "is not bound to any prefix");
/*     */       }
/*     */ 
/* 655 */       if ("".equals(prefix))
/* 656 */         qualifiedName = localName;
/*     */       else {
/* 658 */         qualifiedName = getQName(prefix, localName);
/*     */       }
/*     */ 
/* 661 */       Element element = this.ownerDoc.createElementNS(namespaceURI, qualifiedName);
/*     */ 
/* 663 */       if (this.currentNode != null)
/* 664 */         this.currentNode.appendChild(element);
/*     */       else {
/* 666 */         this.ownerDoc.appendChild(element);
/*     */       }
/* 668 */       this.currentNode = element;
/*     */     }
/* 670 */     if (this.needContextPop[this.depth] != 0) {
/* 671 */       this.namespaceContext.pushContext();
/*     */     }
/* 673 */     incDepth();
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String prefix, String localName, String namespaceURI)
/*     */     throws XMLStreamException
/*     */   {
/* 685 */     if (this.ownerDoc != null) {
/* 686 */       String qname = null;
/* 687 */       if (namespaceURI == null) {
/* 688 */         throw new XMLStreamException("NamespaceURI cannot be null");
/*     */       }
/* 690 */       if (localName == null) {
/* 691 */         throw new XMLStreamException("Local name cannot be null");
/*     */       }
/* 693 */       if (prefix == null) {
/* 694 */         throw new XMLStreamException("Prefix cannot be null");
/*     */       }
/*     */ 
/* 697 */       if (prefix.equals(""))
/* 698 */         qname = localName;
/*     */       else {
/* 700 */         qname = getQName(prefix, localName);
/*     */       }
/*     */ 
/* 703 */       Element el = this.ownerDoc.createElementNS(namespaceURI, qname);
/*     */ 
/* 705 */       if (this.currentNode != null)
/* 706 */         this.currentNode.appendChild(el);
/*     */       else {
/* 708 */         this.ownerDoc.appendChild(el);
/*     */       }
/* 710 */       this.currentNode = el;
/* 711 */       if (this.needContextPop[this.depth] != 0) {
/* 712 */         this.namespaceContext.pushContext();
/*     */       }
/* 714 */       incDepth();
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getQName(String prefix, String localName)
/*     */   {
/* 720 */     this.stringBuffer.setLength(0);
/* 721 */     this.stringBuffer.append(prefix);
/* 722 */     this.stringBuffer.append(":");
/* 723 */     this.stringBuffer.append(localName);
/* 724 */     return this.stringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private Node getNode() {
/* 728 */     if (this.currentNode == null) {
/* 729 */       return this.ownerDoc;
/*     */     }
/* 731 */     return this.currentNode;
/*     */   }
/*     */ 
/*     */   private void incDepth() {
/* 735 */     this.depth += 1;
/* 736 */     if (this.depth == this.needContextPop.length) {
/* 737 */       boolean[] array = new boolean[this.depth + this.resizeValue];
/* 738 */       System.arraycopy(this.needContextPop, 0, array, 0, this.depth);
/* 739 */       this.needContextPop = array;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.writers.XMLDOMWriterImpl
 * JD-Core Version:    0.6.2
 */