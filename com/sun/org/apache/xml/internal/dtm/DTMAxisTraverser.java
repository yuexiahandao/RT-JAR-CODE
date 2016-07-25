/*    */ package com.sun.org.apache.xml.internal.dtm;
/*    */ 
/*    */ public abstract class DTMAxisTraverser
/*    */ {
/*    */   public int first(int context)
/*    */   {
/* 63 */     return next(context, context);
/*    */   }
/*    */ 
/*    */   public int first(int context, int extendedTypeID)
/*    */   {
/* 82 */     return next(context, context, extendedTypeID);
/*    */   }
/*    */ 
/*    */   public abstract int next(int paramInt1, int paramInt2);
/*    */ 
/*    */   public abstract int next(int paramInt1, int paramInt2, int paramInt3);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
 * JD-Core Version:    0.6.2
 */