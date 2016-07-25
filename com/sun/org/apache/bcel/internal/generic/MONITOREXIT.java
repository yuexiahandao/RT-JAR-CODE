/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*    */ 
/*    */ public class MONITOREXIT extends Instruction
/*    */   implements ExceptionThrower, StackConsumer
/*    */ {
/*    */   public MONITOREXIT()
/*    */   {
/* 70 */     super((short)195, (short)1);
/*    */   }
/*    */ 
/*    */   public Class[] getExceptions() {
/* 74 */     return new Class[] { ExceptionConstants.NULL_POINTER_EXCEPTION };
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 87 */     v.visitExceptionThrower(this);
/* 88 */     v.visitStackConsumer(this);
/* 89 */     v.visitMONITOREXIT(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.MONITOREXIT
 * JD-Core Version:    0.6.2
 */