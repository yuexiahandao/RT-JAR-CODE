/*    */ package com.sun.org.apache.xerces.internal.util;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ public final class DOMInputSource extends XMLInputSource
/*    */ {
/*    */   private Node fNode;
/*    */ 
/*    */   public DOMInputSource()
/*    */   {
/* 35 */     this(null);
/*    */   }
/*    */ 
/*    */   public DOMInputSource(Node node) {
/* 39 */     super(null, getSystemIdFromNode(node), null);
/* 40 */     this.fNode = node;
/*    */   }
/*    */ 
/*    */   public DOMInputSource(Node node, String systemId) {
/* 44 */     super(null, systemId, null);
/* 45 */     this.fNode = node;
/*    */   }
/*    */ 
/*    */   public Node getNode() {
/* 49 */     return this.fNode;
/*    */   }
/*    */ 
/*    */   public void setNode(Node node) {
/* 53 */     this.fNode = node;
/*    */   }
/*    */ 
/*    */   private static String getSystemIdFromNode(Node node) {
/* 57 */     if (node != null) {
/*    */       try {
/* 59 */         return node.getBaseURI();
/*    */       }
/*    */       catch (NoSuchMethodError e)
/*    */       {
/* 65 */         return null;
/*    */       }
/*    */       catch (Exception e)
/*    */       {
/* 70 */         return null;
/*    */       }
/*    */     }
/* 73 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.DOMInputSource
 * JD-Core Version:    0.6.2
 */