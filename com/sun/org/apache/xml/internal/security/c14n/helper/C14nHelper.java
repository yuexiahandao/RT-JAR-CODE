/*     */ package com.sun.org.apache.xml.internal.security.c14n.helper;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ 
/*     */ public class C14nHelper
/*     */ {
/*     */   public static boolean namespaceIsRelative(Attr paramAttr)
/*     */   {
/*  55 */     return !namespaceIsAbsolute(paramAttr);
/*     */   }
/*     */ 
/*     */   public static boolean namespaceIsRelative(String paramString)
/*     */   {
/*  65 */     return !namespaceIsAbsolute(paramString);
/*     */   }
/*     */ 
/*     */   public static boolean namespaceIsAbsolute(Attr paramAttr)
/*     */   {
/*  75 */     return namespaceIsAbsolute(paramAttr.getValue());
/*     */   }
/*     */ 
/*     */   public static boolean namespaceIsAbsolute(String paramString)
/*     */   {
/*  87 */     if (paramString.length() == 0) {
/*  88 */       return true;
/*     */     }
/*  90 */     return paramString.indexOf(':') > 0;
/*     */   }
/*     */ 
/*     */   public static void assertNotRelativeNS(Attr paramAttr)
/*     */     throws CanonicalizationException
/*     */   {
/* 103 */     if (paramAttr == null) {
/* 104 */       return;
/*     */     }
/*     */ 
/* 107 */     String str1 = paramAttr.getNodeName();
/* 108 */     boolean bool1 = str1.equals("xmlns");
/* 109 */     boolean bool2 = str1.startsWith("xmlns:");
/*     */ 
/* 111 */     if (((bool1) || (bool2)) && 
/* 112 */       (namespaceIsRelative(paramAttr))) {
/* 113 */       String str2 = paramAttr.getOwnerElement().getTagName();
/* 114 */       String str3 = paramAttr.getValue();
/* 115 */       Object[] arrayOfObject = { str2, str1, str3 };
/*     */ 
/* 117 */       throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", arrayOfObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void checkTraversability(Document paramDocument)
/*     */     throws CanonicalizationException
/*     */   {
/* 133 */     if (!paramDocument.isSupported("Traversal", "2.0")) {
/* 134 */       Object[] arrayOfObject = { paramDocument.getImplementation().getClass().getName() };
/*     */ 
/* 137 */       throw new CanonicalizationException("c14n.Canonicalizer.TraversalNotSupported", arrayOfObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void checkForRelativeNamespace(Element paramElement)
/*     */     throws CanonicalizationException
/*     */   {
/* 153 */     if (paramElement != null) {
/* 154 */       NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
/*     */ 
/* 156 */       for (int i = 0; i < localNamedNodeMap.getLength(); i++)
/* 157 */         assertNotRelativeNS((Attr)localNamedNodeMap.item(i));
/*     */     }
/*     */     else {
/* 160 */       throw new CanonicalizationException("Called checkForRelativeNamespace() on null");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.c14n.helper.C14nHelper
 * JD-Core Version:    0.6.2
 */