/*    */ package com.sun.xml.internal.ws.util.xml;
/*    */ 
/*    */ import javax.xml.stream.Location;
/*    */ 
/*    */ public final class DummyLocation
/*    */   implements Location
/*    */ {
/* 38 */   public static final Location INSTANCE = new DummyLocation();
/*    */ 
/*    */   public int getCharacterOffset() {
/* 41 */     return -1;
/*    */   }
/*    */   public int getColumnNumber() {
/* 44 */     return -1;
/*    */   }
/*    */   public int getLineNumber() {
/* 47 */     return -1;
/*    */   }
/*    */   public String getPublicId() {
/* 50 */     return null;
/*    */   }
/*    */   public String getSystemId() {
/* 53 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.xml.DummyLocation
 * JD-Core Version:    0.6.2
 */