/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*    */ 
/*    */ public class IDIV extends ArithmeticInstruction
/*    */   implements ExceptionThrower
/*    */ {
/*    */   public IDIV()
/*    */   {
/* 71 */     super((short)108);
/*    */   }
/*    */ 
/*    */   public Class[] getExceptions()
/*    */   {
/* 77 */     return new Class[] { ExceptionConstants.ARITHMETIC_EXCEPTION };
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 90 */     v.visitExceptionThrower(this);
/* 91 */     v.visitTypedInstruction(this);
/* 92 */     v.visitStackProducer(this);
/* 93 */     v.visitStackConsumer(this);
/* 94 */     v.visitArithmeticInstruction(this);
/* 95 */     v.visitIDIV(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.IDIV
 * JD-Core Version:    0.6.2
 */