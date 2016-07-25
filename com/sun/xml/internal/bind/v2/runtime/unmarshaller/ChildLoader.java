/*    */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*    */ 
/*    */ public final class ChildLoader
/*    */ {
/*    */   public final Loader loader;
/*    */   public final Receiver receiver;
/*    */ 
/*    */   public ChildLoader(Loader loader, Receiver receiver)
/*    */   {
/* 40 */     assert (loader != null);
/* 41 */     this.loader = loader;
/* 42 */     this.receiver = receiver;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader
 * JD-Core Version:    0.6.2
 */