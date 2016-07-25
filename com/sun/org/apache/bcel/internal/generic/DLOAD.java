/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class DLOAD extends LoadInstruction
/*    */ {
/*    */   DLOAD()
/*    */   {
/* 73 */     super((short)24, (short)38);
/*    */   }
/*    */ 
/*    */   public DLOAD(int n)
/*    */   {
/* 80 */     super((short)24, (short)38, n);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 92 */     super.accept(v);
/* 93 */     v.visitDLOAD(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.DLOAD
 * JD-Core Version:    0.6.2
 */