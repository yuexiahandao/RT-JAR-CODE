/*    */ package com.sun.xml.internal.ws.util.xml;
/*    */ 
/*    */ public final class CDATA
/*    */ {
/*    */   private String _text;
/*    */ 
/*    */   public CDATA(String text)
/*    */   {
/* 34 */     this._text = text;
/*    */   }
/*    */ 
/*    */   public String getText() {
/* 38 */     return this._text;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj) {
/* 42 */     if (obj == null) {
/* 43 */       return false;
/*    */     }
/* 45 */     if (!(obj instanceof CDATA)) {
/* 46 */       return false;
/*    */     }
/* 48 */     CDATA cdata = (CDATA)obj;
/*    */ 
/* 50 */     return this._text.equals(cdata._text);
/*    */   }
/*    */ 
/*    */   public int hashCode() {
/* 54 */     return this._text.hashCode();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.xml.CDATA
 * JD-Core Version:    0.6.2
 */