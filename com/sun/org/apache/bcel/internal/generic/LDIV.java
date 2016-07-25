/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*    */ 
/*    */ public class LDIV extends ArithmeticInstruction
/*    */   implements ExceptionThrower
/*    */ {
/*    */   public LDIV()
/*    */   {
/* 70 */     super((short)109);
/*    */   }
/*    */ 
/*    */   public Class[] getExceptions() {
/* 74 */     return new Class[] { ExceptionConstants.ARITHMETIC_EXCEPTION };
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 87 */     v.visitExceptionThrower(this);
/* 88 */     v.visitTypedInstruction(this);
/* 89 */     v.visitStackProducer(this);
/* 90 */     v.visitStackConsumer(this);
/* 91 */     v.visitArithmeticInstruction(this);
/* 92 */     v.visitLDIV(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.LDIV
 * JD-Core Version:    0.6.2
 */