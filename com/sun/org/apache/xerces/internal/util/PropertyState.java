/*    */ package com.sun.org.apache.xerces.internal.util;
/*    */ 
/*    */ public class PropertyState
/*    */ {
/*    */   public final Status status;
/*    */   public final Object state;
/* 39 */   public static final PropertyState UNKNOWN = new PropertyState(Status.UNKNOWN, null);
/* 40 */   public static final PropertyState RECOGNIZED = new PropertyState(Status.RECOGNIZED, null);
/* 41 */   public static final PropertyState NOT_SUPPORTED = new PropertyState(Status.NOT_SUPPORTED, null);
/* 42 */   public static final PropertyState NOT_RECOGNIZED = new PropertyState(Status.NOT_RECOGNIZED, null);
/* 43 */   public static final PropertyState NOT_ALLOWED = new PropertyState(Status.NOT_ALLOWED, null);
/*    */ 
/*    */   public PropertyState(Status status, Object state)
/*    */   {
/* 47 */     this.status = status;
/* 48 */     this.state = state;
/*    */   }
/*    */ 
/*    */   public static PropertyState of(Status status) {
/* 52 */     return new PropertyState(status, null);
/*    */   }
/*    */ 
/*    */   public static PropertyState is(Object value) {
/* 56 */     return new PropertyState(Status.SET, value);
/*    */   }
/*    */ 
/*    */   public boolean isExceptional() {
/* 60 */     return this.status.isExceptional();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.PropertyState
 * JD-Core Version:    0.6.2
 */