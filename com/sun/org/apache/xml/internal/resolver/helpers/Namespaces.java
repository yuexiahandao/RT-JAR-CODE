/*     */ package com.sun.org.apache.xml.internal.resolver.helpers;
/*     */ 
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class Namespaces
/*     */ {
/*     */   public static String getPrefix(Element element)
/*     */   {
/*  47 */     String name = element.getTagName();
/*  48 */     String prefix = "";
/*     */ 
/*  50 */     if (name.indexOf(':') > 0) {
/*  51 */       prefix = name.substring(0, name.indexOf(':'));
/*     */     }
/*     */ 
/*  54 */     return prefix;
/*     */   }
/*     */ 
/*     */   public static String getLocalName(Element element)
/*     */   {
/*  65 */     String name = element.getTagName();
/*     */ 
/*  67 */     if (name.indexOf(':') > 0) {
/*  68 */       name = name.substring(name.indexOf(':') + 1);
/*     */     }
/*     */ 
/*  71 */     return name;
/*     */   }
/*     */ 
/*     */   public static String getNamespaceURI(Node node, String prefix)
/*     */   {
/*  84 */     if ((node == null) || (node.getNodeType() != 1)) {
/*  85 */       return null;
/*     */     }
/*     */ 
/*  88 */     if (prefix.equals("")) {
/*  89 */       if (((Element)node).hasAttribute("xmlns"))
/*  90 */         return ((Element)node).getAttribute("xmlns");
/*     */     }
/*     */     else {
/*  93 */       String nsattr = "xmlns:" + prefix;
/*  94 */       if (((Element)node).hasAttribute(nsattr)) {
/*  95 */         return ((Element)node).getAttribute(nsattr);
/*     */       }
/*     */     }
/*     */ 
/*  99 */     return getNamespaceURI(node.getParentNode(), prefix);
/*     */   }
/*     */ 
/*     */   public static String getNamespaceURI(Element element)
/*     */   {
/* 111 */     String prefix = getPrefix(element);
/* 112 */     return getNamespaceURI(element, prefix);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.helpers.Namespaces
 * JD-Core Version:    0.6.2
 */