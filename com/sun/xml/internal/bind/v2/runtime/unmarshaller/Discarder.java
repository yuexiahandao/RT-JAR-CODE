/*    */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*    */ 
/*    */ public final class Discarder extends Loader
/*    */ {
/* 39 */   public static final Loader INSTANCE = new Discarder();
/*    */ 
/*    */   private Discarder() {
/* 42 */     super(false);
/*    */   }
/*    */ 
/*    */   public void childElement(UnmarshallingContext.State state, TagName ea)
/*    */   {
/* 47 */     state.setTarget(null);
/*    */ 
/* 49 */     state.setLoader(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.Discarder
 * JD-Core Version:    0.6.2
 */