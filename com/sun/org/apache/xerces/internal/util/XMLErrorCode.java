/*    */ package com.sun.org.apache.xerces.internal.util;
/*    */ 
/*    */ final class XMLErrorCode
/*    */ {
/*    */   private String fDomain;
/*    */   private String fKey;
/*    */ 
/*    */   public XMLErrorCode(String domain, String key)
/*    */   {
/* 49 */     this.fDomain = domain;
/* 50 */     this.fKey = key;
/*    */   }
/*    */ 
/*    */   public void setValues(String domain, String key)
/*    */   {
/* 60 */     this.fDomain = domain;
/* 61 */     this.fKey = key;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 70 */     if (!(obj instanceof XMLErrorCode))
/* 71 */       return false;
/* 72 */     XMLErrorCode err = (XMLErrorCode)obj;
/* 73 */     return (this.fDomain.equals(err.fDomain)) && (this.fKey.equals(err.fKey));
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 82 */     return this.fDomain.hashCode() + this.fKey.hashCode();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.XMLErrorCode
 * JD-Core Version:    0.6.2
 */