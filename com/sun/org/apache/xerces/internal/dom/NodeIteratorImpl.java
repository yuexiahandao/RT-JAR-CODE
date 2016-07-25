/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.traversal.NodeFilter;
/*     */ import org.w3c.dom.traversal.NodeIterator;
/*     */ 
/*     */ public class NodeIteratorImpl
/*     */   implements NodeIterator
/*     */ {
/*     */   private DocumentImpl fDocument;
/*     */   private Node fRoot;
/*  53 */   private int fWhatToShow = -1;
/*     */   private NodeFilter fNodeFilter;
/*  57 */   private boolean fDetach = false;
/*     */   private Node fCurrentNode;
/*  81 */   private boolean fForward = true;
/*     */   private boolean fEntityReferenceExpansion;
/*     */ 
/*     */   public NodeIteratorImpl(DocumentImpl document, Node root, int whatToShow, NodeFilter nodeFilter, boolean entityReferenceExpansion)
/*     */   {
/*  96 */     this.fDocument = document;
/*  97 */     this.fRoot = root;
/*  98 */     this.fCurrentNode = null;
/*  99 */     this.fWhatToShow = whatToShow;
/* 100 */     this.fNodeFilter = nodeFilter;
/* 101 */     this.fEntityReferenceExpansion = entityReferenceExpansion;
/*     */   }
/*     */ 
/*     */   public Node getRoot() {
/* 105 */     return this.fRoot;
/*     */   }
/*     */ 
/*     */   public int getWhatToShow()
/*     */   {
/* 114 */     return this.fWhatToShow;
/*     */   }
/*     */ 
/*     */   public NodeFilter getFilter()
/*     */   {
/* 119 */     return this.fNodeFilter;
/*     */   }
/*     */ 
/*     */   public boolean getExpandEntityReferences()
/*     */   {
/* 124 */     return this.fEntityReferenceExpansion;
/*     */   }
/*     */ 
/*     */   public Node nextNode()
/*     */   {
/* 133 */     if (this.fDetach) {
/* 134 */       throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*     */     }
/*     */ 
/* 140 */     if (this.fRoot == null) return null;
/*     */ 
/* 142 */     Node nextNode = this.fCurrentNode;
/* 143 */     boolean accepted = false;
/*     */ 
/* 146 */     while (!accepted)
/*     */     {
/* 149 */       if ((!this.fForward) && (nextNode != null))
/*     */       {
/* 151 */         nextNode = this.fCurrentNode;
/*     */       }
/* 154 */       else if ((!this.fEntityReferenceExpansion) && (nextNode != null) && (nextNode.getNodeType() == 5))
/*     */       {
/* 157 */         nextNode = nextNode(nextNode, false);
/*     */       }
/* 159 */       else nextNode = nextNode(nextNode, true);
/*     */ 
/* 163 */       this.fForward = true;
/*     */ 
/* 166 */       if (nextNode == null) return null;
/*     */ 
/* 169 */       accepted = acceptNode(nextNode);
/* 170 */       if (accepted)
/*     */       {
/* 172 */         this.fCurrentNode = nextNode;
/* 173 */         return this.fCurrentNode;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 180 */     return null;
/*     */   }
/*     */ 
/*     */   public Node previousNode()
/*     */   {
/* 189 */     if (this.fDetach) {
/* 190 */       throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*     */     }
/*     */ 
/* 196 */     if ((this.fRoot == null) || (this.fCurrentNode == null)) return null;
/*     */ 
/* 198 */     Node previousNode = this.fCurrentNode;
/* 199 */     boolean accepted = false;
/*     */ 
/* 202 */     while (!accepted)
/*     */     {
/* 204 */       if ((this.fForward) && (previousNode != null))
/*     */       {
/* 206 */         previousNode = this.fCurrentNode;
/*     */       }
/*     */       else {
/* 209 */         previousNode = previousNode(previousNode);
/*     */       }
/*     */ 
/* 213 */       this.fForward = false;
/*     */ 
/* 217 */       if (previousNode == null) return null;
/*     */ 
/* 220 */       accepted = acceptNode(previousNode);
/* 221 */       if (accepted)
/*     */       {
/* 223 */         this.fCurrentNode = previousNode;
/* 224 */         return this.fCurrentNode;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 229 */     return null;
/*     */   }
/*     */ 
/*     */   boolean acceptNode(Node node)
/*     */   {
/* 235 */     if (this.fNodeFilter == null) {
/* 236 */       return (this.fWhatToShow & 1 << node.getNodeType() - 1) != 0;
/*     */     }
/* 238 */     return ((this.fWhatToShow & 1 << node.getNodeType() - 1) != 0) && (this.fNodeFilter.acceptNode(node) == 1);
/*     */   }
/*     */ 
/*     */   Node matchNodeOrParent(Node node)
/*     */   {
/* 247 */     if (this.fCurrentNode == null) return null;
/*     */ 
/* 251 */     for (Node n = this.fCurrentNode; n != this.fRoot; n = n.getParentNode()) {
/* 252 */       if (node == n) return n;
/*     */     }
/* 254 */     return null;
/*     */   }
/*     */ 
/*     */   Node nextNode(Node node, boolean visitChildren)
/*     */   {
/* 265 */     if (node == null) return this.fRoot;
/*     */ 
/* 269 */     if (visitChildren)
/*     */     {
/* 271 */       if (node.hasChildNodes()) {
/* 272 */         Node result = node.getFirstChild();
/* 273 */         return result;
/*     */       }
/*     */     }
/*     */ 
/* 277 */     if (node == this.fRoot) {
/* 278 */       return null;
/*     */     }
/*     */ 
/* 282 */     Node result = node.getNextSibling();
/* 283 */     if (result != null) return result;
/*     */ 
/* 287 */     Node parent = node.getParentNode();
/* 288 */     while ((parent != null) && (parent != this.fRoot)) {
/* 289 */       result = parent.getNextSibling();
/* 290 */       if (result != null) {
/* 291 */         return result;
/*     */       }
/* 293 */       parent = parent.getParentNode();
/*     */     }
/*     */ 
/* 299 */     return null;
/*     */   }
/*     */ 
/*     */   Node previousNode(Node node)
/*     */   {
/* 310 */     if (node == this.fRoot) return null;
/*     */ 
/* 313 */     Node result = node.getPreviousSibling();
/* 314 */     if (result == null)
/*     */     {
/* 316 */       result = node.getParentNode();
/* 317 */       return result;
/*     */     }
/*     */ 
/* 321 */     if ((result.hasChildNodes()) && ((this.fEntityReferenceExpansion) || (result == null) || (result.getNodeType() != 5)))
/*     */     {
/* 327 */       while (result.hasChildNodes()) {
/* 328 */         result = result.getLastChild();
/*     */       }
/*     */     }
/*     */ 
/* 332 */     return result;
/*     */   }
/*     */ 
/*     */   public void removeNode(Node node)
/*     */   {
/* 343 */     if (node == null) return;
/*     */ 
/* 345 */     Node deleted = matchNodeOrParent(node);
/*     */ 
/* 347 */     if (deleted == null) return;
/*     */ 
/* 349 */     if (this.fForward) {
/* 350 */       this.fCurrentNode = previousNode(deleted);
/*     */     }
/*     */     else
/*     */     {
/* 354 */       Node next = nextNode(deleted, false);
/* 355 */       if (next != null)
/*     */       {
/* 357 */         this.fCurrentNode = next;
/*     */       }
/*     */       else
/*     */       {
/* 361 */         this.fCurrentNode = previousNode(deleted);
/* 362 */         this.fForward = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void detach()
/*     */   {
/* 370 */     this.fDetach = true;
/* 371 */     this.fDocument.removeNodeIterator(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.NodeIteratorImpl
 * JD-Core Version:    0.6.2
 */