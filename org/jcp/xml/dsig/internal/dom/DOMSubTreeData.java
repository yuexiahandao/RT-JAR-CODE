/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.xml.crypto.NodeSetData;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class DOMSubTreeData
/*     */   implements NodeSetData
/*     */ {
/*     */   private boolean excludeComments;
/*     */   private Iterator ni;
/*     */   private Node root;
/*     */ 
/*     */   public DOMSubTreeData(Node paramNode, boolean paramBoolean)
/*     */   {
/*  52 */     this.root = paramNode;
/*  53 */     this.ni = new DelayedNodeIterator(paramNode, paramBoolean);
/*  54 */     this.excludeComments = paramBoolean;
/*     */   }
/*     */ 
/*     */   public Iterator iterator() {
/*  58 */     return this.ni;
/*     */   }
/*     */ 
/*     */   public Node getRoot() {
/*  62 */     return this.root;
/*     */   }
/*     */ 
/*     */   public boolean excludeComments() {
/*  66 */     return this.excludeComments;
/*     */   }
/*     */ 
/*     */   static class DelayedNodeIterator implements Iterator
/*     */   {
/*     */     private Node root;
/*     */     private List nodeSet;
/*     */     private ListIterator li;
/*     */     private boolean withComments;
/*     */ 
/*     */     DelayedNodeIterator(Node paramNode, boolean paramBoolean) {
/*  80 */       this.root = paramNode;
/*  81 */       this.withComments = (!paramBoolean);
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/*  85 */       if (this.nodeSet == null) {
/*  86 */         this.nodeSet = dereferenceSameDocumentURI(this.root);
/*  87 */         this.li = this.nodeSet.listIterator();
/*     */       }
/*  89 */       return this.li.hasNext();
/*     */     }
/*     */ 
/*     */     public Object next() {
/*  93 */       if (this.nodeSet == null) {
/*  94 */         this.nodeSet = dereferenceSameDocumentURI(this.root);
/*  95 */         this.li = this.nodeSet.listIterator();
/*     */       }
/*  97 */       if (this.li.hasNext()) {
/*  98 */         return (Node)this.li.next();
/*     */       }
/* 100 */       throw new NoSuchElementException();
/*     */     }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 105 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     private List dereferenceSameDocumentURI(Node paramNode)
/*     */     {
/* 116 */       ArrayList localArrayList = new ArrayList();
/* 117 */       if (paramNode != null) {
/* 118 */         nodeSetMinusCommentNodes(paramNode, localArrayList, null);
/*     */       }
/* 120 */       return localArrayList;
/*     */     }
/*     */ 
/*     */     private void nodeSetMinusCommentNodes(Node paramNode1, List paramList, Node paramNode2)
/*     */     {
/* 134 */       switch (paramNode1.getNodeType()) {
/*     */       case 1:
/* 136 */         NamedNodeMap localNamedNodeMap = paramNode1.getAttributes();
/* 137 */         if (localNamedNodeMap != null) {
/* 138 */           int i = 0; for (int j = localNamedNodeMap.getLength(); i < j; i++) {
/* 139 */             paramList.add(localNamedNodeMap.item(i));
/*     */           }
/*     */         }
/* 142 */         paramList.add(paramNode1);
/*     */       case 9:
/* 144 */         Object localObject = null;
/* 145 */         for (Node localNode = paramNode1.getFirstChild(); localNode != null; 
/* 146 */           localNode = localNode.getNextSibling()) {
/* 147 */           nodeSetMinusCommentNodes(localNode, paramList, (Node)localObject);
/* 148 */           localObject = localNode;
/*     */         }
/* 150 */         break;
/*     */       case 3:
/*     */       case 4:
/* 155 */         if ((paramNode2 != null) && ((paramNode2.getNodeType() == 3) || (paramNode2.getNodeType() == 4)))
/*     */         {
/* 157 */           return;
/*     */         }
/*     */       case 7:
/* 160 */         paramList.add(paramNode1);
/* 161 */         break;
/*     */       case 8:
/* 163 */         if (this.withComments)
/* 164 */           paramList.add(paramNode1);
/*     */         break;
/*     */       case 2:
/*     */       case 5:
/*     */       case 6:
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMSubTreeData
 * JD-Core Version:    0.6.2
 */