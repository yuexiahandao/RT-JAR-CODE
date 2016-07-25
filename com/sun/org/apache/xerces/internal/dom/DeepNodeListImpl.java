/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public class DeepNodeListImpl
/*     */   implements NodeList
/*     */ {
/*     */   protected NodeImpl rootNode;
/*     */   protected String tagName;
/*  85 */   protected int changes = 0;
/*     */   protected Vector nodes;
/*     */   protected String nsName;
/*  89 */   protected boolean enableNS = false;
/*     */ 
/*     */   public DeepNodeListImpl(NodeImpl rootNode, String tagName)
/*     */   {
/*  97 */     this.rootNode = rootNode;
/*  98 */     this.tagName = tagName;
/*  99 */     this.nodes = new Vector();
/*     */   }
/*     */ 
/*     */   public DeepNodeListImpl(NodeImpl rootNode, String nsName, String tagName)
/*     */   {
/* 105 */     this(rootNode, tagName);
/* 106 */     this.nsName = ((nsName != null) && (!nsName.equals("")) ? nsName : null);
/* 107 */     this.enableNS = true;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 117 */     item(2147483647);
/* 118 */     return this.nodes.size();
/*     */   }
/*     */ 
/*     */   public Node item(int index)
/*     */   {
/* 126 */     if (this.rootNode.changes() != this.changes) {
/* 127 */       this.nodes = new Vector();
/* 128 */       this.changes = this.rootNode.changes();
/*     */     }
/*     */ 
/* 132 */     if (index < this.nodes.size())
/* 133 */       return (Node)this.nodes.elementAt(index);
/*     */     Node thisNode;
/*     */     Node thisNode;
/* 139 */     if (this.nodes.size() == 0)
/* 140 */       thisNode = this.rootNode;
/*     */     else {
/* 142 */       thisNode = (NodeImpl)this.nodes.lastElement();
/*     */     }
/*     */ 
/* 145 */     while ((thisNode != null) && (index >= this.nodes.size())) {
/* 146 */       thisNode = nextMatchingElementAfter(thisNode);
/* 147 */       if (thisNode != null) {
/* 148 */         this.nodes.addElement(thisNode);
/*     */       }
/*     */     }
/*     */ 
/* 152 */     return thisNode;
/*     */   }
/*     */ 
/*     */   protected Node nextMatchingElementAfter(Node current)
/*     */   {
/* 169 */     while (current != null)
/*     */     {
/* 171 */       if (current.hasChildNodes()) {
/* 172 */         current = current.getFirstChild();
/*     */       }
/*     */       else
/*     */       {
/*     */         Node next;
/* 176 */         if ((current != this.rootNode) && (null != (next = current.getNextSibling()))) {
/* 177 */           current = next;
/*     */         }
/*     */         else
/*     */         {
/* 182 */           Node next = null;
/*     */ 
/* 184 */           for (; current != this.rootNode; 
/* 184 */             current = current.getParentNode())
/*     */           {
/* 186 */             next = current.getNextSibling();
/* 187 */             if (next != null)
/*     */               break;
/*     */           }
/* 190 */           current = next;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 195 */       if ((current != this.rootNode) && (current != null) && (current.getNodeType() == 1))
/*     */       {
/* 198 */         if (!this.enableNS) {
/* 199 */           if ((this.tagName.equals("*")) || (((ElementImpl)current).getTagName().equals(this.tagName)))
/*     */           {
/* 202 */             return current;
/*     */           }
/*     */ 
/*     */         }
/* 206 */         else if (this.tagName.equals("*")) {
/* 207 */           if ((this.nsName != null) && (this.nsName.equals("*"))) {
/* 208 */             return current;
/*     */           }
/* 210 */           ElementImpl el = (ElementImpl)current;
/* 211 */           if (((this.nsName == null) && (el.getNamespaceURI() == null)) || ((this.nsName != null) && (this.nsName.equals(el.getNamespaceURI()))))
/*     */           {
/* 216 */             return current;
/*     */           }
/*     */         }
/*     */         else {
/* 220 */           ElementImpl el = (ElementImpl)current;
/* 221 */           if ((el.getLocalName() != null) && (el.getLocalName().equals(this.tagName)))
/*     */           {
/* 223 */             if ((this.nsName != null) && (this.nsName.equals("*"))) {
/* 224 */               return current;
/*     */             }
/* 226 */             if (((this.nsName == null) && (el.getNamespaceURI() == null)) || ((this.nsName != null) && (this.nsName.equals(el.getNamespaceURI()))))
/*     */             {
/* 231 */               return current;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 243 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DeepNodeListImpl
 * JD-Core Version:    0.6.2
 */