/*    */ package com.sun.org.apache.xerces.internal.util;
/*    */ 
/*    */ public enum Status
/*    */ {
/* 35 */   SET((short)-3, false), 
/* 36 */   UNKNOWN((short)-2, false), 
/* 37 */   RECOGNIZED((short)-1, false), 
/* 38 */   NOT_SUPPORTED((short)0, true), 
/* 39 */   NOT_RECOGNIZED((short)1, true), 
/* 40 */   NOT_ALLOWED((short)2, true);
/*    */ 
/*    */   private final short type;
/*    */   private boolean isExceptional;
/*    */ 
/*    */   private Status(short type, boolean isExceptional)
/*    */   {
/* 48 */     this.type = type;
/* 49 */     this.isExceptional = isExceptional;
/*    */   }
/*    */ 
/*    */   public short getType() {
/* 53 */     return this.type;
/*    */   }
/*    */ 
/*    */   public boolean isExceptional() {
/* 57 */     return this.isExceptional;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.Status
 * JD-Core Version:    0.6.2
 */