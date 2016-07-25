/*    */ package com.sun.corba.se.impl.ior;
/*    */ 
/*    */ public class ObjectAdapterIdNumber extends ObjectAdapterIdArray
/*    */ {
/*    */   private int poaid;
/*    */ 
/*    */   public ObjectAdapterIdNumber(int paramInt)
/*    */   {
/* 42 */     super("OldRootPOA", Integer.toString(paramInt));
/* 43 */     this.poaid = paramInt;
/*    */   }
/*    */ 
/*    */   public int getOldPOAId()
/*    */   {
/* 48 */     return this.poaid;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.ObjectAdapterIdNumber
 * JD-Core Version:    0.6.2
 */