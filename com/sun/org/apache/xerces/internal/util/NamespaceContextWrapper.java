/*    */ package com.sun.org.apache.xerces.internal.util;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class NamespaceContextWrapper
/*    */   implements javax.xml.namespace.NamespaceContext
/*    */ {
/*    */   private com.sun.org.apache.xerces.internal.xni.NamespaceContext fNamespaceContext;
/*    */ 
/*    */   public NamespaceContextWrapper(NamespaceSupport namespaceContext)
/*    */   {
/* 51 */     this.fNamespaceContext = namespaceContext;
/*    */   }
/*    */ 
/*    */   public String getNamespaceURI(String prefix) {
/* 55 */     if (prefix == null) {
/* 56 */       throw new IllegalArgumentException("Prefix can't be null");
/*    */     }
/* 58 */     return this.fNamespaceContext.getURI(prefix.intern());
/*    */   }
/*    */ 
/*    */   public String getPrefix(String namespaceURI) {
/* 62 */     if (namespaceURI == null) {
/* 63 */       throw new IllegalArgumentException("URI can't be null.");
/*    */     }
/* 65 */     return this.fNamespaceContext.getPrefix(namespaceURI.intern());
/*    */   }
/*    */ 
/*    */   public Iterator getPrefixes(String namespaceURI)
/*    */   {
/* 73 */     if (namespaceURI == null) {
/* 74 */       throw new IllegalArgumentException("URI can't be null.");
/*    */     }
/*    */ 
/* 77 */     Vector vector = ((NamespaceSupport)this.fNamespaceContext).getPrefixes(namespaceURI.intern());
/*    */ 
/* 79 */     return vector.iterator();
/*    */   }
/*    */ 
/*    */   public com.sun.org.apache.xerces.internal.xni.NamespaceContext getNamespaceContext()
/*    */   {
/* 87 */     return this.fNamespaceContext;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.NamespaceContextWrapper
 * JD-Core Version:    0.6.2
 */