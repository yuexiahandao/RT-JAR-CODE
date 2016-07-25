/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import org.w3c.dom.CharacterData;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ public class TextImpl extends CharacterDataImpl
/*     */   implements CharacterData, Text
/*     */ {
/*     */   static final long serialVersionUID = -5294980852957403469L;
/*     */ 
/*     */   public TextImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public TextImpl(CoreDocumentImpl ownerDoc, String data)
/*     */   {
/*  70 */     super(ownerDoc, data);
/*     */   }
/*     */ 
/*     */   public void setValues(CoreDocumentImpl ownerDoc, String data)
/*     */   {
/*  81 */     this.flags = 0;
/*  82 */     this.nextSibling = null;
/*  83 */     this.previousSibling = null;
/*  84 */     setOwnerDocument(ownerDoc);
/*  85 */     this.data = data;
/*     */   }
/*     */ 
/*     */   public short getNodeType()
/*     */   {
/*  96 */     return 3;
/*     */   }
/*     */ 
/*     */   public String getNodeName()
/*     */   {
/* 101 */     return "#text";
/*     */   }
/*     */ 
/*     */   public void setIgnorableWhitespace(boolean ignore)
/*     */   {
/* 109 */     if (needsSyncData()) {
/* 110 */       synchronizeData();
/*     */     }
/* 112 */     isIgnorableWhitespace(ignore);
/*     */   }
/*     */ 
/*     */   public boolean isElementContentWhitespace()
/*     */   {
/* 129 */     if (needsSyncData()) {
/* 130 */       synchronizeData();
/*     */     }
/* 132 */     return internalIsIgnorableWhitespace();
/*     */   }
/*     */ 
/*     */   public String getWholeText()
/*     */   {
/* 144 */     if (needsSyncData()) {
/* 145 */       synchronizeData();
/*     */     }
/*     */ 
/* 148 */     if (this.fBufferStr == null) {
/* 149 */       this.fBufferStr = new StringBuffer();
/*     */     }
/*     */     else {
/* 152 */       this.fBufferStr.setLength(0);
/*     */     }
/* 154 */     if ((this.data != null) && (this.data.length() != 0)) {
/* 155 */       this.fBufferStr.append(this.data);
/*     */     }
/*     */ 
/* 159 */     getWholeTextBackward(getPreviousSibling(), this.fBufferStr, getParentNode());
/* 160 */     String temp = this.fBufferStr.toString();
/*     */ 
/* 163 */     this.fBufferStr.setLength(0);
/*     */ 
/* 166 */     getWholeTextForward(getNextSibling(), this.fBufferStr, getParentNode());
/*     */ 
/* 168 */     return temp + this.fBufferStr.toString();
/*     */   }
/*     */ 
/*     */   protected void insertTextContent(StringBuffer buf)
/*     */     throws DOMException
/*     */   {
/* 179 */     String content = getNodeValue();
/* 180 */     if (content != null)
/* 181 */       buf.insert(0, content);
/*     */   }
/*     */ 
/*     */   private boolean getWholeTextForward(Node node, StringBuffer buffer, Node parent)
/*     */   {
/* 197 */     boolean inEntRef = false;
/*     */ 
/* 199 */     if (parent != null) {
/* 200 */       inEntRef = parent.getNodeType() == 5;
/*     */     }
/*     */ 
/* 203 */     while (node != null) {
/* 204 */       short type = node.getNodeType();
/* 205 */       if (type == 5) {
/* 206 */         if (getWholeTextForward(node.getFirstChild(), buffer, node)) {
/* 207 */           return true;
/*     */         }
/*     */       }
/* 210 */       else if ((type == 3) || (type == 4))
/*     */       {
/* 212 */         ((NodeImpl)node).getTextContent(buffer);
/*     */       }
/*     */       else {
/* 215 */         return true;
/*     */       }
/*     */ 
/* 218 */       node = node.getNextSibling();
/*     */     }
/*     */ 
/* 224 */     if (inEntRef) {
/* 225 */       getWholeTextForward(parent.getNextSibling(), buffer, parent.getParentNode());
/* 226 */       return true;
/*     */     }
/*     */ 
/* 229 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean getWholeTextBackward(Node node, StringBuffer buffer, Node parent)
/*     */   {
/* 245 */     boolean inEntRef = false;
/* 246 */     if (parent != null) {
/* 247 */       inEntRef = parent.getNodeType() == 5;
/*     */     }
/*     */ 
/* 250 */     while (node != null) {
/* 251 */       short type = node.getNodeType();
/* 252 */       if (type == 5) {
/* 253 */         if (getWholeTextBackward(node.getLastChild(), buffer, node)) {
/* 254 */           return true;
/*     */         }
/*     */       }
/* 257 */       else if ((type == 3) || (type == 4))
/*     */       {
/* 259 */         ((TextImpl)node).insertTextContent(buffer);
/*     */       }
/*     */       else {
/* 262 */         return true;
/*     */       }
/*     */ 
/* 265 */       node = node.getPreviousSibling();
/*     */     }
/*     */ 
/* 271 */     if (inEntRef) {
/* 272 */       getWholeTextBackward(parent.getPreviousSibling(), buffer, parent.getParentNode());
/* 273 */       return true;
/*     */     }
/*     */ 
/* 276 */     return false;
/*     */   }
/*     */ 
/*     */   public Text replaceWholeText(String content)
/*     */     throws DOMException
/*     */   {
/* 292 */     if (needsSyncData()) {
/* 293 */       synchronizeData();
/*     */     }
/*     */ 
/* 297 */     Node parent = getParentNode();
/* 298 */     if ((content == null) || (content.length() == 0))
/*     */     {
/* 300 */       if (parent != null) {
/* 301 */         parent.removeChild(this);
/*     */       }
/* 303 */       return null;
/*     */     }
/*     */ 
/* 307 */     if (ownerDocument().errorChecking) {
/* 308 */       if (!canModifyPrev(this)) {
/* 309 */         throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
/*     */       }
/*     */ 
/* 316 */       if (!canModifyNext(this)) {
/* 317 */         throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 325 */     Text currentNode = null;
/* 326 */     if (isReadOnly()) {
/* 327 */       Text newNode = ownerDocument().createTextNode(content);
/* 328 */       if (parent != null) {
/* 329 */         parent.insertBefore(newNode, this);
/* 330 */         parent.removeChild(this);
/* 331 */         currentNode = newNode;
/*     */       } else {
/* 333 */         return newNode;
/*     */       }
/*     */     } else {
/* 336 */       setData(content);
/* 337 */       currentNode = this;
/*     */     }
/*     */ 
/* 341 */     Node prev = currentNode.getPreviousSibling();
/* 342 */     while (prev != null)
/*     */     {
/* 347 */       if ((prev.getNodeType() != 3) && (prev.getNodeType() != 4) && ((prev.getNodeType() != 5) || (!hasTextOnlyChildren(prev)))) {
/*     */         break;
/*     */       }
/* 350 */       parent.removeChild(prev);
/* 351 */       prev = currentNode;
/*     */ 
/* 355 */       prev = prev.getPreviousSibling();
/*     */     }
/*     */ 
/* 359 */     Node next = currentNode.getNextSibling();
/* 360 */     while (next != null)
/*     */     {
/* 365 */       if ((next.getNodeType() != 3) && (next.getNodeType() != 4) && ((next.getNodeType() != 5) || (!hasTextOnlyChildren(next)))) {
/*     */         break;
/*     */       }
/* 368 */       parent.removeChild(next);
/* 369 */       next = currentNode;
/*     */ 
/* 373 */       next = next.getNextSibling();
/*     */     }
/*     */ 
/* 376 */     return currentNode;
/*     */   }
/*     */ 
/*     */   private boolean canModifyPrev(Node node)
/*     */   {
/* 399 */     boolean textLastChild = false;
/*     */ 
/* 401 */     Node prev = node.getPreviousSibling();
/*     */ 
/* 403 */     while (prev != null)
/*     */     {
/* 405 */       short type = prev.getNodeType();
/*     */ 
/* 407 */       if (type == 5)
/*     */       {
/* 410 */         Node lastChild = prev.getLastChild();
/*     */ 
/* 414 */         if (lastChild == null) {
/* 415 */           return false;
/*     */         }
/*     */ 
/* 421 */         while (lastChild != null) {
/* 422 */           short lType = lastChild.getNodeType();
/*     */ 
/* 424 */           if ((lType == 3) || (lType == 4))
/*     */           {
/* 426 */             textLastChild = true;
/* 427 */           } else if (lType == 5) {
/* 428 */             if (!canModifyPrev(lastChild)) {
/* 429 */               return false;
/*     */             }
/*     */ 
/* 434 */             textLastChild = true;
/*     */           }
/*     */           else
/*     */           {
/* 440 */             if (textLastChild) {
/* 441 */               return false;
/*     */             }
/* 443 */             return true;
/*     */           }
/*     */ 
/* 446 */           lastChild = lastChild.getPreviousSibling();
/*     */         }
/* 448 */       } else if ((type != 3) && (type != 4))
/*     */       {
/* 455 */         return true;
/*     */       }
/*     */ 
/* 458 */       prev = prev.getPreviousSibling();
/*     */     }
/*     */ 
/* 461 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean canModifyNext(Node node)
/*     */   {
/* 484 */     boolean textFirstChild = false;
/*     */ 
/* 486 */     Node next = node.getNextSibling();
/* 487 */     while (next != null)
/*     */     {
/* 489 */       short type = next.getNodeType();
/*     */ 
/* 491 */       if (type == 5)
/*     */       {
/* 494 */         Node firstChild = next.getFirstChild();
/*     */ 
/* 498 */         if (firstChild == null) {
/* 499 */           return false;
/*     */         }
/*     */ 
/* 505 */         while (firstChild != null) {
/* 506 */           short lType = firstChild.getNodeType();
/*     */ 
/* 508 */           if ((lType == 3) || (lType == 4))
/*     */           {
/* 510 */             textFirstChild = true;
/* 511 */           } else if (lType == 5) {
/* 512 */             if (!canModifyNext(firstChild)) {
/* 513 */               return false;
/*     */             }
/*     */ 
/* 518 */             textFirstChild = true;
/*     */           }
/*     */           else
/*     */           {
/* 523 */             if (textFirstChild) {
/* 524 */               return false;
/*     */             }
/* 526 */             return true;
/*     */           }
/*     */ 
/* 529 */           firstChild = firstChild.getNextSibling();
/*     */         }
/* 531 */       } else if ((type != 3) && (type != 4))
/*     */       {
/* 538 */         return true;
/*     */       }
/*     */ 
/* 541 */       next = next.getNextSibling();
/*     */     }
/*     */ 
/* 544 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean hasTextOnlyChildren(Node node)
/*     */   {
/* 555 */     Node child = node;
/*     */ 
/* 557 */     if (child == null) {
/* 558 */       return false;
/*     */     }
/*     */ 
/* 561 */     child = child.getFirstChild();
/* 562 */     while (child != null) {
/* 563 */       int type = child.getNodeType();
/*     */ 
/* 565 */       if (type == 5) {
/* 566 */         return hasTextOnlyChildren(child);
/*     */       }
/* 568 */       if ((type != 3) && (type != 4) && (type != 5))
/*     */       {
/* 571 */         return false;
/*     */       }
/* 573 */       child = child.getNextSibling();
/*     */     }
/* 575 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isIgnorableWhitespace()
/*     */   {
/* 584 */     if (needsSyncData()) {
/* 585 */       synchronizeData();
/*     */     }
/* 587 */     return internalIsIgnorableWhitespace();
/*     */   }
/*     */ 
/*     */   public Text splitText(int offset)
/*     */     throws DOMException
/*     */   {
/* 617 */     if (isReadOnly()) {
/* 618 */       throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
/*     */     }
/*     */ 
/* 623 */     if (needsSyncData()) {
/* 624 */       synchronizeData();
/*     */     }
/* 626 */     if ((offset < 0) || (offset > this.data.length())) {
/* 627 */       throw new DOMException((short)1, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", null));
/*     */     }
/*     */ 
/* 632 */     Text newText = getOwnerDocument().createTextNode(this.data.substring(offset));
/*     */ 
/* 634 */     setNodeValue(this.data.substring(0, offset));
/*     */ 
/* 637 */     Node parentNode = getParentNode();
/* 638 */     if (parentNode != null) {
/* 639 */       parentNode.insertBefore(newText, this.nextSibling);
/*     */     }
/*     */ 
/* 642 */     return newText;
/*     */   }
/*     */ 
/*     */   public void replaceData(String value)
/*     */   {
/* 651 */     this.data = value;
/*     */   }
/*     */ 
/*     */   public String removeData()
/*     */   {
/* 660 */     String olddata = this.data;
/* 661 */     this.data = "";
/* 662 */     return olddata;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.TextImpl
 * JD-Core Version:    0.6.2
 */