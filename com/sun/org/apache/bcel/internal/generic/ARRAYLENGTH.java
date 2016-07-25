/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*    */ 
/*    */ public class ARRAYLENGTH extends Instruction
/*    */   implements ExceptionThrower, StackProducer
/*    */ {
/*    */   public ARRAYLENGTH()
/*    */   {
/* 72 */     super((short)190, (short)1);
/*    */   }
/*    */ 
/*    */   public Class[] getExceptions()
/*    */   {
/* 78 */     return new Class[] { ExceptionConstants.NULL_POINTER_EXCEPTION };
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 91 */     v.visitExceptionThrower(this);
/* 92 */     v.visitStackProducer(this);
/* 93 */     v.visitARRAYLENGTH(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH
 * JD-Core Version:    0.6.2
 */