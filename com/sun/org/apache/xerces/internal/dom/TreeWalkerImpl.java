/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.traversal.NodeFilter;
/*     */ import org.w3c.dom.traversal.TreeWalker;
/*     */ 
/*     */ public class TreeWalkerImpl
/*     */   implements TreeWalker
/*     */ {
/*  41 */   private boolean fEntityReferenceExpansion = false;
/*     */ 
/*  43 */   int fWhatToShow = -1;
/*     */   NodeFilter fNodeFilter;
/*     */   Node fCurrentNode;
/*     */   Node fRoot;
/*     */ 
/*     */   public TreeWalkerImpl(Node root, int whatToShow, NodeFilter nodeFilter, boolean entityReferenceExpansion)
/*     */   {
/*  67 */     this.fCurrentNode = root;
/*  68 */     this.fRoot = root;
/*  69 */     this.fWhatToShow = whatToShow;
/*  70 */     this.fNodeFilter = nodeFilter;
/*  71 */     this.fEntityReferenceExpansion = entityReferenceExpansion;
/*     */   }
/*     */ 
/*     */   public Node getRoot() {
/*  75 */     return this.fRoot;
/*     */   }
/*     */ 
/*     */   public int getWhatToShow()
/*     */   {
/*  80 */     return this.fWhatToShow;
/*     */   }
/*     */ 
/*     */   public void setWhatShow(int whatToShow) {
/*  84 */     this.fWhatToShow = whatToShow;
/*     */   }
/*     */ 
/*     */   public NodeFilter getFilter() {
/*  88 */     return this.fNodeFilter;
/*     */   }
/*     */ 
/*     */   public boolean getExpandEntityReferences()
/*     */   {
/*  93 */     return this.fEntityReferenceExpansion;
/*     */   }
/*     */ 
/*     */   public Node getCurrentNode()
/*     */   {
/*  98 */     return this.fCurrentNode;
/*     */   }
/*     */ 
/*     */   public void setCurrentNode(Node node) {
/* 102 */     if (node == null) {
/* 103 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
/* 104 */       throw new DOMException((short)9, msg);
/*     */     }
/*     */ 
/* 107 */     this.fCurrentNode = node;
/*     */   }
/*     */ 
/*     */   public Node parentNode()
/*     */   {
/* 116 */     if (this.fCurrentNode == null) return null;
/*     */ 
/* 118 */     Node node = getParentNode(this.fCurrentNode);
/* 119 */     if (node != null) {
/* 120 */       this.fCurrentNode = node;
/*     */     }
/* 122 */     return node;
/*     */   }
/*     */ 
/*     */   public Node firstChild()
/*     */   {
/* 132 */     if (this.fCurrentNode == null) return null;
/*     */ 
/* 134 */     Node node = getFirstChild(this.fCurrentNode);
/* 135 */     if (node != null) {
/* 136 */       this.fCurrentNode = node;
/*     */     }
/* 138 */     return node;
/*     */   }
/*     */ 
/*     */   public Node lastChild()
/*     */   {
/* 146 */     if (this.fCurrentNode == null) return null;
/*     */ 
/* 148 */     Node node = getLastChild(this.fCurrentNode);
/* 149 */     if (node != null) {
/* 150 */       this.fCurrentNode = node;
/*     */     }
/* 152 */     return node;
/*     */   }
/*     */ 
/*     */   public Node previousSibling()
/*     */   {
/* 161 */     if (this.fCurrentNode == null) return null;
/*     */ 
/* 163 */     Node node = getPreviousSibling(this.fCurrentNode);
/* 164 */     if (node != null) {
/* 165 */       this.fCurrentNode = node;
/*     */     }
/* 167 */     return node;
/*     */   }
/*     */ 
/*     */   public Node nextSibling()
/*     */   {
/* 175 */     if (this.fCurrentNode == null) return null;
/*     */ 
/* 177 */     Node node = getNextSibling(this.fCurrentNode);
/* 178 */     if (node != null) {
/* 179 */       this.fCurrentNode = node;
/*     */     }
/* 181 */     return node;
/*     */   }
/*     */ 
/*     */   public Node previousNode()
/*     */   {
/* 191 */     if (this.fCurrentNode == null) return null;
/*     */ 
/* 194 */     Node result = getPreviousSibling(this.fCurrentNode);
/* 195 */     if (result == null) {
/* 196 */       result = getParentNode(this.fCurrentNode);
/* 197 */       if (result != null) {
/* 198 */         this.fCurrentNode = result;
/* 199 */         return this.fCurrentNode;
/*     */       }
/* 201 */       return null;
/*     */     }
/*     */ 
/* 205 */     Node lastChild = getLastChild(result);
/*     */ 
/* 207 */     Node prev = lastChild;
/* 208 */     while (lastChild != null) {
/* 209 */       prev = lastChild;
/* 210 */       lastChild = getLastChild(prev);
/*     */     }
/*     */ 
/* 213 */     lastChild = prev;
/*     */ 
/* 216 */     if (lastChild != null) {
/* 217 */       this.fCurrentNode = lastChild;
/* 218 */       return this.fCurrentNode;
/*     */     }
/*     */ 
/* 222 */     if (result != null) {
/* 223 */       this.fCurrentNode = result;
/* 224 */       return this.fCurrentNode;
/*     */     }
/*     */ 
/* 228 */     return null;
/*     */   }
/*     */ 
/*     */   public Node nextNode()
/*     */   {
/* 237 */     if (this.fCurrentNode == null) return null;
/*     */ 
/* 239 */     Node result = getFirstChild(this.fCurrentNode);
/*     */ 
/* 241 */     if (result != null) {
/* 242 */       this.fCurrentNode = result;
/* 243 */       return result;
/*     */     }
/*     */ 
/* 246 */     result = getNextSibling(this.fCurrentNode);
/*     */ 
/* 248 */     if (result != null) {
/* 249 */       this.fCurrentNode = result;
/* 250 */       return result;
/*     */     }
/*     */ 
/* 254 */     Node parent = getParentNode(this.fCurrentNode);
/* 255 */     while (parent != null) {
/* 256 */       result = getNextSibling(parent);
/* 257 */       if (result != null) {
/* 258 */         this.fCurrentNode = result;
/* 259 */         return result;
/*     */       }
/* 261 */       parent = getParentNode(parent);
/*     */     }
/*     */ 
/* 266 */     return null;
/*     */   }
/*     */ 
/*     */   Node getParentNode(Node node)
/*     */   {
/* 276 */     if ((node == null) || (node == this.fRoot)) return null;
/*     */ 
/* 278 */     Node newNode = node.getParentNode();
/* 279 */     if (newNode == null) return null;
/*     */ 
/* 281 */     int accept = acceptNode(newNode);
/*     */ 
/* 283 */     if (accept == 1) {
/* 284 */       return newNode;
/*     */     }
/*     */ 
/* 288 */     return getParentNode(newNode);
/*     */   }
/*     */ 
/*     */   Node getNextSibling(Node node)
/*     */   {
/* 300 */     return getNextSibling(node, this.fRoot);
/*     */   }
/*     */ 
/*     */   Node getNextSibling(Node node, Node root)
/*     */   {
/* 311 */     if ((node == null) || (node == root)) return null;
/*     */ 
/* 313 */     Node newNode = node.getNextSibling();
/* 314 */     if (newNode == null)
/*     */     {
/* 316 */       newNode = node.getParentNode();
/*     */ 
/* 318 */       if ((newNode == null) || (newNode == root)) return null;
/*     */ 
/* 320 */       int parentAccept = acceptNode(newNode);
/*     */ 
/* 322 */       if (parentAccept == 3) {
/* 323 */         return getNextSibling(newNode, root);
/*     */       }
/*     */ 
/* 326 */       return null;
/*     */     }
/*     */ 
/* 329 */     int accept = acceptNode(newNode);
/*     */ 
/* 331 */     if (accept == 1) {
/* 332 */       return newNode;
/*     */     }
/* 334 */     if (accept == 3) {
/* 335 */       Node fChild = getFirstChild(newNode);
/* 336 */       if (fChild == null) {
/* 337 */         return getNextSibling(newNode, root);
/*     */       }
/* 339 */       return fChild;
/*     */     }
/*     */ 
/* 344 */     return getNextSibling(newNode, root);
/*     */   }
/*     */ 
/*     */   Node getPreviousSibling(Node node)
/*     */   {
/* 355 */     return getPreviousSibling(node, this.fRoot);
/*     */   }
/*     */ 
/*     */   Node getPreviousSibling(Node node, Node root)
/*     */   {
/* 366 */     if ((node == null) || (node == root)) return null;
/*     */ 
/* 368 */     Node newNode = node.getPreviousSibling();
/* 369 */     if (newNode == null)
/*     */     {
/* 371 */       newNode = node.getParentNode();
/* 372 */       if ((newNode == null) || (newNode == root)) return null;
/*     */ 
/* 374 */       int parentAccept = acceptNode(newNode);
/*     */ 
/* 376 */       if (parentAccept == 3) {
/* 377 */         return getPreviousSibling(newNode, root);
/*     */       }
/*     */ 
/* 380 */       return null;
/*     */     }
/*     */ 
/* 383 */     int accept = acceptNode(newNode);
/*     */ 
/* 385 */     if (accept == 1) {
/* 386 */       return newNode;
/*     */     }
/* 388 */     if (accept == 3) {
/* 389 */       Node fChild = getLastChild(newNode);
/* 390 */       if (fChild == null) {
/* 391 */         return getPreviousSibling(newNode, root);
/*     */       }
/* 393 */       return fChild;
/*     */     }
/*     */ 
/* 398 */     return getPreviousSibling(newNode, root);
/*     */   }
/*     */ 
/*     */   Node getFirstChild(Node node)
/*     */   {
/* 409 */     if (node == null) return null;
/*     */ 
/* 411 */     if ((!this.fEntityReferenceExpansion) && (node.getNodeType() == 5))
/*     */     {
/* 413 */       return null;
/* 414 */     }Node newNode = node.getFirstChild();
/* 415 */     if (newNode == null) return null;
/* 416 */     int accept = acceptNode(newNode);
/*     */ 
/* 418 */     if (accept == 1) {
/* 419 */       return newNode;
/*     */     }
/* 421 */     if ((accept == 3) && (newNode.hasChildNodes()))
/*     */     {
/* 424 */       Node fChild = getFirstChild(newNode);
/*     */ 
/* 426 */       if (fChild == null) {
/* 427 */         return getNextSibling(newNode, node);
/*     */       }
/* 429 */       return fChild;
/*     */     }
/*     */ 
/* 434 */     return getNextSibling(newNode, node);
/*     */   }
/*     */ 
/*     */   Node getLastChild(Node node)
/*     */   {
/* 447 */     if (node == null) return null;
/*     */ 
/* 449 */     if ((!this.fEntityReferenceExpansion) && (node.getNodeType() == 5))
/*     */     {
/* 451 */       return null;
/*     */     }
/* 453 */     Node newNode = node.getLastChild();
/* 454 */     if (newNode == null) return null;
/*     */ 
/* 456 */     int accept = acceptNode(newNode);
/*     */ 
/* 458 */     if (accept == 1) {
/* 459 */       return newNode;
/*     */     }
/* 461 */     if ((accept == 3) && (newNode.hasChildNodes()))
/*     */     {
/* 464 */       Node lChild = getLastChild(newNode);
/* 465 */       if (lChild == null) {
/* 466 */         return getPreviousSibling(newNode, node);
/*     */       }
/* 468 */       return lChild;
/*     */     }
/*     */ 
/* 473 */     return getPreviousSibling(newNode, node);
/*     */   }
/*     */ 
/*     */   short acceptNode(Node node)
/*     */   {
/* 491 */     if (this.fNodeFilter == null) {
/* 492 */       if ((this.fWhatToShow & 1 << node.getNodeType() - 1) != 0) {
/* 493 */         return 1;
/*     */       }
/* 495 */       return 3;
/*     */     }
/*     */ 
/* 498 */     if ((this.fWhatToShow & 1 << node.getNodeType() - 1) != 0) {
/* 499 */       return this.fNodeFilter.acceptNode(node);
/*     */     }
/*     */ 
/* 503 */     return 3;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.TreeWalkerImpl
 * JD-Core Version:    0.6.2
 */