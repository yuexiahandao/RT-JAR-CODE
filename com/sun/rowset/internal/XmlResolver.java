/*    */ package com.sun.rowset.internal;
/*    */ 
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.InputSource;
/*    */ 
/*    */ public class XmlResolver
/*    */   implements EntityResolver
/*    */ {
/*    */   public InputSource resolveEntity(String paramString1, String paramString2)
/*    */   {
/* 42 */     String str = paramString2.substring(paramString2.lastIndexOf("/"));
/*    */ 
/* 44 */     if (paramString2.startsWith("http://java.sun.com/xml/ns/jdbc")) {
/* 45 */       return new InputSource(getClass().getResourceAsStream(str));
/*    */     }
/*    */ 
/* 49 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rowset.internal.XmlResolver
 * JD-Core Version:    0.6.2
 */