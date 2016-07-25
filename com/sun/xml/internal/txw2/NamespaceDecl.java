/*    */ package com.sun.xml.internal.txw2;
/*    */ 
/*    */ final class NamespaceDecl
/*    */ {
/*    */   final String uri;
/*    */   boolean requirePrefix;
/*    */   final String dummyPrefix;
/*    */   final char uniqueId;
/*    */   String prefix;
/*    */   boolean declared;
/*    */   NamespaceDecl next;
/*    */ 
/*    */   NamespaceDecl(char uniqueId, String uri, String prefix, boolean requirePrefix)
/*    */   {
/* 62 */     this.dummyPrefix = (2 + '\000' + uniqueId);
/* 63 */     this.uri = uri;
/* 64 */     this.prefix = prefix;
/* 65 */     this.requirePrefix = requirePrefix;
/* 66 */     this.uniqueId = uniqueId;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.NamespaceDecl
 * JD-Core Version:    0.6.2
 */