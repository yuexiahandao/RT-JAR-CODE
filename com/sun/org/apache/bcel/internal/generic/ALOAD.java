/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class ALOAD extends LoadInstruction
/*    */ {
/*    */   ALOAD()
/*    */   {
/* 73 */     super((short)25, (short)42);
/*    */   }
/*    */ 
/*    */   public ALOAD(int n)
/*    */   {
/* 80 */     super((short)25, (short)42, n);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 92 */     super.accept(v);
/* 93 */     v.visitALOAD(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.ALOAD
 * JD-Core Version:    0.6.2
 */