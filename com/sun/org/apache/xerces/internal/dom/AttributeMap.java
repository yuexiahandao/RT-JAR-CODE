/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class AttributeMap extends NamedNodeMapImpl
/*     */ {
/*     */   static final long serialVersionUID = 8872606282138665383L;
/*     */ 
/*     */   protected AttributeMap(ElementImpl ownerNode, NamedNodeMapImpl defaults)
/*     */   {
/*  56 */     super(ownerNode);
/*  57 */     if (defaults != null)
/*     */     {
/*  59 */       cloneContent(defaults);
/*  60 */       if (this.nodes != null)
/*  61 */         hasDefaults(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Node setNamedItem(Node arg)
/*     */     throws DOMException
/*     */   {
/*  78 */     boolean errCheck = this.ownerNode.ownerDocument().errorChecking;
/*  79 */     if (errCheck) {
/*  80 */       if (isReadOnly()) {
/*  81 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/*  82 */         throw new DOMException((short)7, msg);
/*     */       }
/*  84 */       if (arg.getOwnerDocument() != this.ownerNode.ownerDocument()) {
/*  85 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
/*  86 */         throw new DOMException((short)4, msg);
/*     */       }
/*  88 */       if (arg.getNodeType() != 2) {
/*  89 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null);
/*  90 */         throw new DOMException((short)3, msg);
/*     */       }
/*     */     }
/*  93 */     AttrImpl argn = (AttrImpl)arg;
/*     */ 
/*  95 */     if (argn.isOwned()) {
/*  96 */       if ((errCheck) && (argn.getOwnerElement() != this.ownerNode)) {
/*  97 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INUSE_ATTRIBUTE_ERR", null);
/*  98 */         throw new DOMException((short)10, msg);
/*     */       }
/*     */ 
/* 101 */       return arg;
/*     */     }
/*     */ 
/* 106 */     argn.ownerNode = this.ownerNode;
/* 107 */     argn.isOwned(true);
/*     */ 
/* 109 */     int i = findNamePoint(argn.getNodeName(), 0);
/* 110 */     AttrImpl previous = null;
/* 111 */     if (i >= 0) {
/* 112 */       previous = (AttrImpl)this.nodes.get(i);
/* 113 */       this.nodes.set(i, arg);
/* 114 */       previous.ownerNode = this.ownerNode.ownerDocument();
/* 115 */       previous.isOwned(false);
/*     */ 
/* 117 */       previous.isSpecified(true);
/*     */     } else {
/* 119 */       i = -1 - i;
/* 120 */       if (null == this.nodes) {
/* 121 */         this.nodes = new ArrayList(5);
/*     */       }
/* 123 */       this.nodes.add(i, arg);
/*     */     }
/*     */ 
/* 127 */     this.ownerNode.ownerDocument().setAttrNode(argn, previous);
/*     */ 
/* 131 */     if (!argn.isNormalized()) {
/* 132 */       this.ownerNode.isNormalized(false);
/*     */     }
/* 134 */     return previous;
/*     */   }
/*     */ 
/*     */   public Node setNamedItemNS(Node arg)
/*     */     throws DOMException
/*     */   {
/* 148 */     boolean errCheck = this.ownerNode.ownerDocument().errorChecking;
/* 149 */     if (errCheck) {
/* 150 */       if (isReadOnly()) {
/* 151 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/* 152 */         throw new DOMException((short)7, msg);
/*     */       }
/* 154 */       if (arg.getOwnerDocument() != this.ownerNode.ownerDocument()) {
/* 155 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
/* 156 */         throw new DOMException((short)4, msg);
/*     */       }
/* 158 */       if (arg.getNodeType() != 2) {
/* 159 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null);
/* 160 */         throw new DOMException((short)3, msg);
/*     */       }
/*     */     }
/* 163 */     AttrImpl argn = (AttrImpl)arg;
/*     */ 
/* 165 */     if (argn.isOwned()) {
/* 166 */       if ((errCheck) && (argn.getOwnerElement() != this.ownerNode)) {
/* 167 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INUSE_ATTRIBUTE_ERR", null);
/* 168 */         throw new DOMException((short)10, msg);
/*     */       }
/*     */ 
/* 171 */       return arg;
/*     */     }
/*     */ 
/* 175 */     argn.ownerNode = this.ownerNode;
/* 176 */     argn.isOwned(true);
/*     */ 
/* 178 */     int i = findNamePoint(argn.getNamespaceURI(), argn.getLocalName());
/* 179 */     AttrImpl previous = null;
/* 180 */     if (i >= 0) {
/* 181 */       previous = (AttrImpl)this.nodes.get(i);
/* 182 */       this.nodes.set(i, arg);
/* 183 */       previous.ownerNode = this.ownerNode.ownerDocument();
/* 184 */       previous.isOwned(false);
/*     */ 
/* 186 */       previous.isSpecified(true);
/*     */     }
/*     */     else
/*     */     {
/* 190 */       i = findNamePoint(arg.getNodeName(), 0);
/* 191 */       if (i >= 0) {
/* 192 */         previous = (AttrImpl)this.nodes.get(i);
/* 193 */         this.nodes.add(i, arg);
/*     */       } else {
/* 195 */         i = -1 - i;
/* 196 */         if (null == this.nodes) {
/* 197 */           this.nodes = new ArrayList(5);
/*     */         }
/* 199 */         this.nodes.add(i, arg);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 205 */     this.ownerNode.ownerDocument().setAttrNode(argn, previous);
/*     */ 
/* 209 */     if (!argn.isNormalized()) {
/* 210 */       this.ownerNode.isNormalized(false);
/*     */     }
/* 212 */     return previous;
/*     */   }
/*     */ 
/*     */   public Node removeNamedItem(String name)
/*     */     throws DOMException
/*     */   {
/* 231 */     return internalRemoveNamedItem(name, true);
/*     */   }
/*     */ 
/*     */   Node safeRemoveNamedItem(String name)
/*     */   {
/* 239 */     return internalRemoveNamedItem(name, false);
/*     */   }
/*     */ 
/*     */   protected Node removeItem(Node item, boolean addDefault)
/*     */     throws DOMException
/*     */   {
/* 258 */     int index = -1;
/* 259 */     if (this.nodes != null) {
/* 260 */       int size = this.nodes.size();
/* 261 */       for (int i = 0; i < size; i++) {
/* 262 */         if (this.nodes.get(i) == item) {
/* 263 */           index = i;
/* 264 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 268 */     if (index < 0) {
/* 269 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
/* 270 */       throw new DOMException((short)8, msg);
/*     */     }
/*     */ 
/* 273 */     return remove((AttrImpl)item, index, addDefault);
/*     */   }
/*     */ 
/*     */   protected final Node internalRemoveNamedItem(String name, boolean raiseEx)
/*     */   {
/* 281 */     if (isReadOnly()) {
/* 282 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/* 283 */       throw new DOMException((short)7, msg);
/*     */     }
/* 285 */     int i = findNamePoint(name, 0);
/* 286 */     if (i < 0) {
/* 287 */       if (raiseEx) {
/* 288 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
/* 289 */         throw new DOMException((short)8, msg);
/*     */       }
/* 291 */       return null;
/*     */     }
/*     */ 
/* 295 */     return remove((AttrImpl)this.nodes.get(i), i, true);
/*     */   }
/*     */ 
/*     */   private final Node remove(AttrImpl attr, int index, boolean addDefault)
/*     */   {
/* 302 */     CoreDocumentImpl ownerDocument = this.ownerNode.ownerDocument();
/* 303 */     String name = attr.getNodeName();
/* 304 */     if (attr.isIdAttribute()) {
/* 305 */       ownerDocument.removeIdentifier(attr.getValue());
/*     */     }
/*     */ 
/* 308 */     if ((hasDefaults()) && (addDefault))
/*     */     {
/* 310 */       NamedNodeMapImpl defaults = ((ElementImpl)this.ownerNode).getDefaultAttributes();
/*     */       Node d;
/* 314 */       if ((defaults != null) && ((d = defaults.getNamedItem(name)) != null) && (findNamePoint(name, index + 1) < 0))
/*     */       {
/* 317 */         NodeImpl clone = (NodeImpl)d.cloneNode(true);
/* 318 */         if (d.getLocalName() != null)
/*     */         {
/* 323 */           ((AttrNSImpl)clone).namespaceURI = attr.getNamespaceURI();
/*     */         }
/* 325 */         clone.ownerNode = this.ownerNode;
/* 326 */         clone.isOwned(true);
/* 327 */         clone.isSpecified(false);
/*     */ 
/* 329 */         this.nodes.set(index, clone);
/* 330 */         if (attr.isIdAttribute())
/* 331 */           ownerDocument.putIdentifier(clone.getNodeValue(), (ElementImpl)this.ownerNode);
/*     */       }
/*     */       else
/*     */       {
/* 335 */         this.nodes.remove(index);
/*     */       }
/*     */     } else {
/* 338 */       this.nodes.remove(index);
/*     */     }
/*     */ 
/* 344 */     attr.ownerNode = ownerDocument;
/* 345 */     attr.isOwned(false);
/*     */ 
/* 349 */     attr.isSpecified(true);
/* 350 */     attr.isIdAttribute(false);
/*     */ 
/* 353 */     ownerDocument.removedAttrNode(attr, this.ownerNode, name);
/*     */ 
/* 355 */     return attr;
/*     */   }
/*     */ 
/*     */   public Node removeNamedItemNS(String namespaceURI, String name)
/*     */     throws DOMException
/*     */   {
/* 376 */     return internalRemoveNamedItemNS(namespaceURI, name, true);
/*     */   }
/*     */ 
/*     */   Node safeRemoveNamedItemNS(String namespaceURI, String name)
/*     */   {
/* 384 */     return internalRemoveNamedItemNS(namespaceURI, name, false);
/*     */   }
/*     */ 
/*     */   protected final Node internalRemoveNamedItemNS(String namespaceURI, String name, boolean raiseEx)
/*     */   {
/* 396 */     CoreDocumentImpl ownerDocument = this.ownerNode.ownerDocument();
/* 397 */     if ((ownerDocument.errorChecking) && (isReadOnly())) {
/* 398 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/* 399 */       throw new DOMException((short)7, msg);
/*     */     }
/* 401 */     int i = findNamePoint(namespaceURI, name);
/* 402 */     if (i < 0) {
/* 403 */       if (raiseEx) {
/* 404 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
/* 405 */         throw new DOMException((short)8, msg);
/*     */       }
/* 407 */       return null;
/*     */     }
/*     */ 
/* 411 */     AttrImpl n = (AttrImpl)this.nodes.get(i);
/*     */ 
/* 413 */     if (n.isIdAttribute()) {
/* 414 */       ownerDocument.removeIdentifier(n.getValue());
/*     */     }
/*     */ 
/* 417 */     String nodeName = n.getNodeName();
/* 418 */     if (hasDefaults()) {
/* 419 */       NamedNodeMapImpl defaults = ((ElementImpl)this.ownerNode).getDefaultAttributes();
/*     */       Node d;
/* 421 */       if ((defaults != null) && ((d = defaults.getNamedItem(nodeName)) != null))
/*     */       {
/* 424 */         int j = findNamePoint(nodeName, 0);
/* 425 */         if ((j >= 0) && (findNamePoint(nodeName, j + 1) < 0)) {
/* 426 */           NodeImpl clone = (NodeImpl)d.cloneNode(true);
/* 427 */           clone.ownerNode = this.ownerNode;
/* 428 */           if (d.getLocalName() != null)
/*     */           {
/* 433 */             ((AttrNSImpl)clone).namespaceURI = namespaceURI;
/*     */           }
/* 435 */           clone.isOwned(true);
/* 436 */           clone.isSpecified(false);
/* 437 */           this.nodes.set(i, clone);
/* 438 */           if (clone.isIdAttribute())
/* 439 */             ownerDocument.putIdentifier(clone.getNodeValue(), (ElementImpl)this.ownerNode);
/*     */         }
/*     */         else
/*     */         {
/* 443 */           this.nodes.remove(i);
/*     */         }
/*     */       } else {
/* 446 */         this.nodes.remove(i);
/*     */       }
/*     */     } else {
/* 449 */       this.nodes.remove(i);
/*     */     }
/*     */ 
/* 455 */     n.ownerNode = ownerDocument;
/* 456 */     n.isOwned(false);
/*     */ 
/* 459 */     n.isSpecified(true);
/*     */ 
/* 461 */     n.isIdAttribute(false);
/*     */ 
/* 464 */     ownerDocument.removedAttrNode(n, this.ownerNode, name);
/*     */ 
/* 466 */     return n;
/*     */   }
/*     */ 
/*     */   public NamedNodeMapImpl cloneMap(NodeImpl ownerNode)
/*     */   {
/* 480 */     AttributeMap newmap = new AttributeMap((ElementImpl)ownerNode, null);
/*     */ 
/* 482 */     newmap.hasDefaults(hasDefaults());
/* 483 */     newmap.cloneContent(this);
/* 484 */     return newmap;
/*     */   }
/*     */ 
/*     */   protected void cloneContent(NamedNodeMapImpl srcmap)
/*     */   {
/* 491 */     List srcnodes = srcmap.nodes;
/* 492 */     if (srcnodes != null) {
/* 493 */       int size = srcnodes.size();
/* 494 */       if (size != 0) {
/* 495 */         if (this.nodes == null) {
/* 496 */           this.nodes = new ArrayList(size);
/*     */         }
/*     */         else {
/* 499 */           this.nodes.clear();
/*     */         }
/* 501 */         for (int i = 0; i < size; i++) {
/* 502 */           NodeImpl n = (NodeImpl)srcnodes.get(i);
/* 503 */           NodeImpl clone = (NodeImpl)n.cloneNode(true);
/* 504 */           clone.isSpecified(n.isSpecified());
/* 505 */           this.nodes.add(clone);
/* 506 */           clone.ownerNode = this.ownerNode;
/* 507 */           clone.isOwned(true);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void moveSpecifiedAttributes(AttributeMap srcmap)
/*     */   {
/* 518 */     int nsize = srcmap.nodes != null ? srcmap.nodes.size() : 0;
/* 519 */     for (int i = nsize - 1; i >= 0; i--) {
/* 520 */       AttrImpl attr = (AttrImpl)srcmap.nodes.get(i);
/* 521 */       if (attr.isSpecified()) {
/* 522 */         srcmap.remove(attr, i, false);
/* 523 */         if (attr.getLocalName() != null) {
/* 524 */           setNamedItem(attr);
/*     */         }
/*     */         else
/* 527 */           setNamedItemNS(attr);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void reconcileDefaults(NamedNodeMapImpl defaults)
/*     */   {
/* 541 */     int nsize = this.nodes != null ? this.nodes.size() : 0;
/* 542 */     for (int i = nsize - 1; i >= 0; i--) {
/* 543 */       AttrImpl attr = (AttrImpl)this.nodes.get(i);
/* 544 */       if (!attr.isSpecified()) {
/* 545 */         remove(attr, i, false);
/*     */       }
/*     */     }
/*     */ 
/* 549 */     if (defaults == null) {
/* 550 */       return;
/*     */     }
/* 552 */     if ((this.nodes == null) || (this.nodes.size() == 0)) {
/* 553 */       cloneContent(defaults);
/*     */     }
/*     */     else {
/* 556 */       int dsize = defaults.nodes.size();
/* 557 */       for (int n = 0; n < dsize; n++) {
/* 558 */         AttrImpl d = (AttrImpl)defaults.nodes.get(n);
/* 559 */         int i = findNamePoint(d.getNodeName(), 0);
/* 560 */         if (i < 0) {
/* 561 */           i = -1 - i;
/* 562 */           NodeImpl clone = (NodeImpl)d.cloneNode(true);
/* 563 */           clone.ownerNode = this.ownerNode;
/* 564 */           clone.isOwned(true);
/* 565 */           clone.isSpecified(false);
/* 566 */           this.nodes.add(i, clone);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final int addItem(Node arg)
/*     */   {
/* 575 */     AttrImpl argn = (AttrImpl)arg;
/*     */ 
/* 578 */     argn.ownerNode = this.ownerNode;
/* 579 */     argn.isOwned(true);
/*     */ 
/* 581 */     int i = findNamePoint(argn.getNamespaceURI(), argn.getLocalName());
/* 582 */     if (i >= 0) {
/* 583 */       this.nodes.set(i, arg);
/*     */     }
/*     */     else
/*     */     {
/* 588 */       i = findNamePoint(argn.getNodeName(), 0);
/* 589 */       if (i >= 0) {
/* 590 */         this.nodes.add(i, arg);
/*     */       }
/*     */       else {
/* 593 */         i = -1 - i;
/* 594 */         if (null == this.nodes) {
/* 595 */           this.nodes = new ArrayList(5);
/*     */         }
/* 597 */         this.nodes.add(i, arg);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 602 */     this.ownerNode.ownerDocument().setAttrNode(argn, null);
/* 603 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.AttributeMap
 * JD-Core Version:    0.6.2
 */