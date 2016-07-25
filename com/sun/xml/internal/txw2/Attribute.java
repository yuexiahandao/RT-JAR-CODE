/*    */ package com.sun.xml.internal.txw2;
/*    */ 
/*    */ final class Attribute
/*    */ {
/*    */   final String nsUri;
/*    */   final String localName;
/*    */   Attribute next;
/* 43 */   final StringBuilder value = new StringBuilder();
/*    */ 
/*    */   Attribute(String nsUri, String localName) {
/* 46 */     assert ((nsUri != null) && (localName != null));
/*    */ 
/* 48 */     this.nsUri = nsUri;
/* 49 */     this.localName = localName;
/*    */   }
/*    */ 
/*    */   boolean hasName(String nsUri, String localName) {
/* 53 */     return (this.localName.equals(localName)) && (this.nsUri.equals(nsUri));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.Attribute
 * JD-Core Version:    0.6.2
 */