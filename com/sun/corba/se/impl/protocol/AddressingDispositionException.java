/*    */ package com.sun.corba.se.impl.protocol;
/*    */ 
/*    */ public class AddressingDispositionException extends RuntimeException
/*    */ {
/* 47 */   private short expectedAddrDisp = 0;
/*    */ 
/*    */   public AddressingDispositionException(short paramShort) {
/* 50 */     this.expectedAddrDisp = paramShort;
/*    */   }
/*    */ 
/*    */   public short expectedAddrDisp() {
/* 54 */     return this.expectedAddrDisp;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.AddressingDispositionException
 * JD-Core Version:    0.6.2
 */