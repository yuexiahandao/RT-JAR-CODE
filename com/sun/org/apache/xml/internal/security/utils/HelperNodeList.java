/*    */ package com.sun.org.apache.xml.internal.security.utils;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.NodeList;
/*    */ 
/*    */ public class HelperNodeList
/*    */   implements NodeList
/*    */ {
/* 37 */   ArrayList nodes = new ArrayList(20);
/* 38 */   boolean _allNodesMustHaveSameParent = false;
/*    */ 
/*    */   public HelperNodeList()
/*    */   {
/* 44 */     this(false);
/*    */   }
/*    */ 
/*    */   public HelperNodeList(boolean paramBoolean)
/*    */   {
/* 52 */     this._allNodesMustHaveSameParent = paramBoolean;
/*    */   }
/*    */ 
/*    */   public Node item(int paramInt)
/*    */   {
/* 65 */     return (Node)this.nodes.get(paramInt);
/*    */   }
/*    */ 
/*    */   public int getLength()
/*    */   {
/* 74 */     return this.nodes.size();
/*    */   }
/*    */ 
/*    */   public void appendChild(Node paramNode)
/*    */     throws IllegalArgumentException
/*    */   {
/* 84 */     if ((this._allNodesMustHaveSameParent) && (getLength() > 0) && 
/* 85 */       (item(0).getParentNode() != paramNode.getParentNode())) {
/* 86 */       throw new IllegalArgumentException("Nodes have not the same Parent");
/*    */     }
/*    */ 
/* 89 */     this.nodes.add(paramNode);
/*    */   }
/*    */ 
/*    */   public Document getOwnerDocument()
/*    */   {
/* 96 */     if (getLength() == 0) {
/* 97 */       return null;
/*    */     }
/* 99 */     return XMLUtils.getOwnerDocument(item(0));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.HelperNodeList
 * JD-Core Version:    0.6.2
 */