/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class NamedNodeMapImpl
/*     */   implements NamedNodeMap, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -7039242451046758020L;
/*     */   protected short flags;
/*     */   protected static final short READONLY = 1;
/*     */   protected static final short CHANGED = 2;
/*     */   protected static final short HASDEFAULTS = 4;
/*     */   protected List nodes;
/*     */   protected NodeImpl ownerNode;
/*     */ 
/*     */   protected NamedNodeMapImpl(NodeImpl ownerNode)
/*     */   {
/*  93 */     this.ownerNode = ownerNode;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 107 */     return this.nodes != null ? this.nodes.size() : 0;
/*     */   }
/*     */ 
/*     */   public Node item(int index)
/*     */   {
/* 125 */     return (this.nodes != null) && (index < this.nodes.size()) ? (Node)this.nodes.get(index) : null;
/*     */   }
/*     */ 
/*     */   public Node getNamedItem(String name)
/*     */   {
/* 138 */     int i = findNamePoint(name, 0);
/* 139 */     return i < 0 ? null : (Node)this.nodes.get(i);
/*     */   }
/*     */ 
/*     */   public Node getNamedItemNS(String namespaceURI, String localName)
/*     */   {
/* 156 */     int i = findNamePoint(namespaceURI, localName);
/* 157 */     return i < 0 ? null : (Node)this.nodes.get(i);
/*     */   }
/*     */ 
/*     */   public Node setNamedItem(Node arg)
/*     */     throws DOMException
/*     */   {
/* 181 */     CoreDocumentImpl ownerDocument = this.ownerNode.ownerDocument();
/* 182 */     if (ownerDocument.errorChecking) {
/* 183 */       if (isReadOnly()) {
/* 184 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/* 185 */         throw new DOMException((short)7, msg);
/*     */       }
/* 187 */       if (arg.getOwnerDocument() != ownerDocument) {
/* 188 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
/* 189 */         throw new DOMException((short)4, msg);
/*     */       }
/*     */     }
/*     */ 
/* 193 */     int i = findNamePoint(arg.getNodeName(), 0);
/* 194 */     NodeImpl previous = null;
/* 195 */     if (i >= 0) {
/* 196 */       previous = (NodeImpl)this.nodes.get(i);
/* 197 */       this.nodes.set(i, arg);
/*     */     } else {
/* 199 */       i = -1 - i;
/* 200 */       if (null == this.nodes) {
/* 201 */         this.nodes = new ArrayList(5);
/*     */       }
/* 203 */       this.nodes.add(i, arg);
/*     */     }
/* 205 */     return previous;
/*     */   }
/*     */ 
/*     */   public Node setNamedItemNS(Node arg)
/*     */     throws DOMException
/*     */   {
/* 223 */     CoreDocumentImpl ownerDocument = this.ownerNode.ownerDocument();
/* 224 */     if (ownerDocument.errorChecking) {
/* 225 */       if (isReadOnly()) {
/* 226 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/* 227 */         throw new DOMException((short)7, msg);
/*     */       }
/*     */ 
/* 230 */       if (arg.getOwnerDocument() != ownerDocument) {
/* 231 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
/* 232 */         throw new DOMException((short)4, msg);
/*     */       }
/*     */     }
/*     */ 
/* 236 */     int i = findNamePoint(arg.getNamespaceURI(), arg.getLocalName());
/* 237 */     NodeImpl previous = null;
/* 238 */     if (i >= 0) {
/* 239 */       previous = (NodeImpl)this.nodes.get(i);
/* 240 */       this.nodes.set(i, arg);
/*     */     }
/*     */     else
/*     */     {
/* 244 */       i = findNamePoint(arg.getNodeName(), 0);
/* 245 */       if (i >= 0) {
/* 246 */         previous = (NodeImpl)this.nodes.get(i);
/* 247 */         this.nodes.add(i, arg);
/*     */       } else {
/* 249 */         i = -1 - i;
/* 250 */         if (null == this.nodes) {
/* 251 */           this.nodes = new ArrayList(5);
/*     */         }
/* 253 */         this.nodes.add(i, arg);
/*     */       }
/*     */     }
/* 256 */     return previous;
/*     */   }
/*     */ 
/*     */   public Node removeNamedItem(String name)
/*     */     throws DOMException
/*     */   {
/* 269 */     if (isReadOnly()) {
/* 270 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/* 271 */       throw new DOMException((short)7, msg);
/*     */     }
/*     */ 
/* 275 */     int i = findNamePoint(name, 0);
/* 276 */     if (i < 0) {
/* 277 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
/* 278 */       throw new DOMException((short)8, msg);
/*     */     }
/*     */ 
/* 281 */     NodeImpl n = (NodeImpl)this.nodes.get(i);
/* 282 */     this.nodes.remove(i);
/*     */ 
/* 284 */     return n;
/*     */   }
/*     */ 
/*     */   public Node removeNamedItemNS(String namespaceURI, String name)
/*     */     throws DOMException
/*     */   {
/* 305 */     if (isReadOnly()) {
/* 306 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/* 307 */       throw new DOMException((short)7, msg);
/*     */     }
/*     */ 
/* 311 */     int i = findNamePoint(namespaceURI, name);
/* 312 */     if (i < 0) {
/* 313 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
/* 314 */       throw new DOMException((short)8, msg);
/*     */     }
/*     */ 
/* 317 */     NodeImpl n = (NodeImpl)this.nodes.get(i);
/* 318 */     this.nodes.remove(i);
/*     */ 
/* 320 */     return n;
/*     */   }
/*     */ 
/*     */   public NamedNodeMapImpl cloneMap(NodeImpl ownerNode)
/*     */   {
/* 334 */     NamedNodeMapImpl newmap = new NamedNodeMapImpl(ownerNode);
/* 335 */     newmap.cloneContent(this);
/* 336 */     return newmap;
/*     */   }
/*     */ 
/*     */   protected void cloneContent(NamedNodeMapImpl srcmap) {
/* 340 */     List srcnodes = srcmap.nodes;
/* 341 */     if (srcnodes != null) {
/* 342 */       int size = srcnodes.size();
/* 343 */       if (size != 0) {
/* 344 */         if (this.nodes == null) {
/* 345 */           this.nodes = new ArrayList(size);
/*     */         }
/*     */         else {
/* 348 */           this.nodes.clear();
/*     */         }
/* 350 */         for (int i = 0; i < size; i++) {
/* 351 */           NodeImpl n = (NodeImpl)srcmap.nodes.get(i);
/* 352 */           NodeImpl clone = (NodeImpl)n.cloneNode(true);
/* 353 */           clone.isSpecified(n.isSpecified());
/* 354 */           this.nodes.add(clone);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void setReadOnly(boolean readOnly, boolean deep)
/*     */   {
/* 376 */     isReadOnly(readOnly);
/* 377 */     if ((deep) && (this.nodes != null))
/* 378 */       for (int i = this.nodes.size() - 1; i >= 0; i--)
/* 379 */         ((NodeImpl)this.nodes.get(i)).setReadOnly(readOnly, deep);
/*     */   }
/*     */ 
/*     */   boolean getReadOnly()
/*     */   {
/* 389 */     return isReadOnly();
/*     */   }
/*     */ 
/*     */   protected void setOwnerDocument(CoreDocumentImpl doc)
/*     */   {
/* 402 */     if (this.nodes != null) {
/* 403 */       int size = this.nodes.size();
/* 404 */       for (int i = 0; i < size; i++)
/* 405 */         ((NodeImpl)item(i)).setOwnerDocument(doc);
/*     */     }
/*     */   }
/*     */ 
/*     */   final boolean isReadOnly()
/*     */   {
/* 411 */     return (this.flags & 0x1) != 0;
/*     */   }
/*     */ 
/*     */   final void isReadOnly(boolean value) {
/* 415 */     this.flags = ((short)(value ? this.flags | 0x1 : this.flags & 0xFFFFFFFE));
/*     */   }
/*     */ 
/*     */   final boolean changed() {
/* 419 */     return (this.flags & 0x2) != 0;
/*     */   }
/*     */ 
/*     */   final void changed(boolean value) {
/* 423 */     this.flags = ((short)(value ? this.flags | 0x2 : this.flags & 0xFFFFFFFD));
/*     */   }
/*     */ 
/*     */   final boolean hasDefaults() {
/* 427 */     return (this.flags & 0x4) != 0;
/*     */   }
/*     */ 
/*     */   final void hasDefaults(boolean value) {
/* 431 */     this.flags = ((short)(value ? this.flags | 0x4 : this.flags & 0xFFFFFFFB));
/*     */   }
/*     */ 
/*     */   protected int findNamePoint(String name, int start)
/*     */   {
/* 453 */     int i = 0;
/* 454 */     if (this.nodes != null) {
/* 455 */       int first = start;
/* 456 */       int last = this.nodes.size() - 1;
/*     */ 
/* 458 */       while (first <= last) {
/* 459 */         i = (first + last) / 2;
/* 460 */         int test = name.compareTo(((Node)this.nodes.get(i)).getNodeName());
/* 461 */         if (test == 0) {
/* 462 */           return i;
/*     */         }
/* 464 */         if (test < 0) {
/* 465 */           last = i - 1;
/*     */         }
/*     */         else {
/* 468 */           first = i + 1;
/*     */         }
/*     */       }
/*     */ 
/* 472 */       if (first > i) {
/* 473 */         i = first;
/*     */       }
/*     */     }
/*     */ 
/* 477 */     return -1 - i;
/*     */   }
/*     */ 
/*     */   protected int findNamePoint(String namespaceURI, String name)
/*     */   {
/* 486 */     if (this.nodes == null) return -1;
/* 487 */     if (name == null) return -1;
/*     */ 
/* 496 */     int size = this.nodes.size();
/* 497 */     for (int i = 0; i < size; i++) {
/* 498 */       NodeImpl a = (NodeImpl)this.nodes.get(i);
/* 499 */       String aNamespaceURI = a.getNamespaceURI();
/* 500 */       String aLocalName = a.getLocalName();
/* 501 */       if (namespaceURI == null) {
/* 502 */         if ((aNamespaceURI == null) && ((name.equals(aLocalName)) || ((aLocalName == null) && (name.equals(a.getNodeName())))))
/*     */         {
/* 507 */           return i;
/*     */         }
/* 509 */       } else if ((namespaceURI.equals(aNamespaceURI)) && (name.equals(aLocalName)))
/*     */       {
/* 512 */         return i;
/*     */       }
/*     */     }
/* 515 */     return -1;
/*     */   }
/*     */ 
/*     */   protected boolean precedes(Node a, Node b)
/*     */   {
/* 522 */     if (this.nodes != null) {
/* 523 */       int size = this.nodes.size();
/* 524 */       for (int i = 0; i < size; i++) {
/* 525 */         Node n = (Node)this.nodes.get(i);
/* 526 */         if (n == a) return true;
/* 527 */         if (n == b) return false;
/*     */       }
/*     */     }
/* 530 */     return false;
/*     */   }
/*     */ 
/*     */   protected void removeItem(int index)
/*     */   {
/* 538 */     if ((this.nodes != null) && (index < this.nodes.size()))
/* 539 */       this.nodes.remove(index);
/*     */   }
/*     */ 
/*     */   protected Object getItem(int index)
/*     */   {
/* 545 */     if (this.nodes != null) {
/* 546 */       return this.nodes.get(index);
/*     */     }
/* 548 */     return null;
/*     */   }
/*     */ 
/*     */   protected int addItem(Node arg) {
/* 552 */     int i = findNamePoint(arg.getNamespaceURI(), arg.getLocalName());
/* 553 */     if (i >= 0) {
/* 554 */       this.nodes.set(i, arg);
/*     */     }
/*     */     else
/*     */     {
/* 559 */       i = findNamePoint(arg.getNodeName(), 0);
/* 560 */       if (i >= 0) {
/* 561 */         this.nodes.add(i, arg);
/*     */       }
/*     */       else {
/* 564 */         i = -1 - i;
/* 565 */         if (null == this.nodes) {
/* 566 */           this.nodes = new ArrayList(5);
/*     */         }
/* 568 */         this.nodes.add(i, arg);
/*     */       }
/*     */     }
/* 571 */     return i;
/*     */   }
/*     */ 
/*     */   protected ArrayList cloneMap(ArrayList list)
/*     */   {
/* 581 */     if (list == null) {
/* 582 */       list = new ArrayList(5);
/*     */     }
/* 584 */     list.clear();
/* 585 */     if (this.nodes != null) {
/* 586 */       int size = this.nodes.size();
/* 587 */       for (int i = 0; i < size; i++) {
/* 588 */         list.add(this.nodes.get(i));
/*     */       }
/*     */     }
/* 591 */     return list;
/*     */   }
/*     */ 
/*     */   protected int getNamedItemIndex(String namespaceURI, String localName) {
/* 595 */     return findNamePoint(namespaceURI, localName);
/*     */   }
/*     */ 
/*     */   public void removeAll()
/*     */   {
/* 602 */     if (this.nodes != null)
/* 603 */       this.nodes.clear();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream in)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 609 */     in.defaultReadObject();
/* 610 */     if (this.nodes != null)
/* 611 */       this.nodes = new ArrayList(this.nodes);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream out) throws IOException
/*     */   {
/* 616 */     List oldNodes = this.nodes;
/*     */     try {
/* 618 */       if (oldNodes != null) {
/* 619 */         this.nodes = new Vector(oldNodes);
/*     */       }
/* 621 */       out.defaultWriteObject();
/*     */     }
/*     */     finally
/*     */     {
/* 626 */       this.nodes = oldNodes;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.NamedNodeMapImpl
 * JD-Core Version:    0.6.2
 */