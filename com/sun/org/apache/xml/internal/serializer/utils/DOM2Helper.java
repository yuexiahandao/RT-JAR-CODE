/*     */ package com.sun.org.apache.xml.internal.serializer.utils;
/*     */ 
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public final class DOM2Helper
/*     */ {
/*     */   public String getLocalNameOfNode(Node n)
/*     */   {
/*  86 */     String name = n.getLocalName();
/*     */ 
/*  88 */     return null == name ? getLocalNameOfNodeFallback(n) : name;
/*     */   }
/*     */ 
/*     */   private String getLocalNameOfNodeFallback(Node n)
/*     */   {
/* 105 */     String qname = n.getNodeName();
/* 106 */     int index = qname.indexOf(':');
/*     */ 
/* 108 */     return index < 0 ? qname : qname.substring(index + 1);
/*     */   }
/*     */ 
/*     */   public String getNamespaceOfNode(Node n)
/*     */   {
/* 128 */     return n.getNamespaceURI();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.utils.DOM2Helper
 * JD-Core Version:    0.6.2
 */