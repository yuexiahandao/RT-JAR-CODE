/*    */ package com.sun.org.apache.xml.internal.security.utils;
/*    */ 
/*    */ import com.sun.org.apache.xpath.internal.CachedXPathAPI;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import org.w3c.dom.Document;
/*    */ 
/*    */ public class CachedXPathAPIHolder
/*    */ {
/* 31 */   static ThreadLocal local = new ThreadLocal();
/* 32 */   static ThreadLocal localDoc = new ThreadLocal();
/*    */ 
/*    */   public static void setDoc(Document paramDocument)
/*    */   {
/* 39 */     if (localDoc.get() != paramDocument) {
/* 40 */       CachedXPathAPI localCachedXPathAPI = (CachedXPathAPI)local.get();
/* 41 */       if (localCachedXPathAPI == null) {
/* 42 */         localCachedXPathAPI = new CachedXPathAPI();
/* 43 */         local.set(localCachedXPathAPI);
/* 44 */         localDoc.set(paramDocument);
/* 45 */         return;
/*    */       }
/*    */ 
/* 48 */       localCachedXPathAPI.getXPathContext().reset();
/* 49 */       localDoc.set(paramDocument);
/*    */     }
/*    */   }
/*    */ 
/*    */   public static CachedXPathAPI getCachedXPathAPI()
/*    */   {
/* 57 */     CachedXPathAPI localCachedXPathAPI = (CachedXPathAPI)local.get();
/* 58 */     if (localCachedXPathAPI == null) {
/* 59 */       localCachedXPathAPI = new CachedXPathAPI();
/* 60 */       local.set(localCachedXPathAPI);
/* 61 */       localDoc.set(null);
/*    */     }
/* 63 */     return localCachedXPathAPI;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.CachedXPathAPIHolder
 * JD-Core Version:    0.6.2
 */