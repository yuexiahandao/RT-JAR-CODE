/*    */ package com.sun.org.apache.xerces.internal.util;
/*    */ 
/*    */ public class FeatureState
/*    */ {
/*    */   public final Status status;
/*    */   public final boolean state;
/* 40 */   public static final FeatureState SET_ENABLED = new FeatureState(Status.SET, true);
/* 41 */   public static final FeatureState SET_DISABLED = new FeatureState(Status.SET, false);
/* 42 */   public static final FeatureState UNKNOWN = new FeatureState(Status.UNKNOWN, false);
/* 43 */   public static final FeatureState RECOGNIZED = new FeatureState(Status.RECOGNIZED, false);
/* 44 */   public static final FeatureState NOT_SUPPORTED = new FeatureState(Status.NOT_SUPPORTED, false);
/* 45 */   public static final FeatureState NOT_RECOGNIZED = new FeatureState(Status.NOT_RECOGNIZED, false);
/* 46 */   public static final FeatureState NOT_ALLOWED = new FeatureState(Status.NOT_ALLOWED, false);
/*    */ 
/*    */   public FeatureState(Status status, boolean state) {
/* 49 */     this.status = status;
/* 50 */     this.state = state;
/*    */   }
/*    */ 
/*    */   public static FeatureState of(Status status) {
/* 54 */     return new FeatureState(status, false);
/*    */   }
/*    */ 
/*    */   public static FeatureState is(boolean value) {
/* 58 */     return new FeatureState(Status.SET, value);
/*    */   }
/*    */ 
/*    */   public boolean isExceptional() {
/* 62 */     return this.status.isExceptional();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.FeatureState
 * JD-Core Version:    0.6.2
 */