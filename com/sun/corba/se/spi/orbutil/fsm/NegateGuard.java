/*     */ package com.sun.corba.se.spi.orbutil.fsm;
/*     */ 
/*     */ class NegateGuard
/*     */   implements Guard
/*     */ {
/*     */   Guard guard;
/*     */ 
/*     */   public NegateGuard(Guard paramGuard)
/*     */   {
/* 130 */     this.guard = paramGuard;
/*     */   }
/*     */ 
/*     */   public Guard.Result evaluate(FSM paramFSM, Input paramInput)
/*     */   {
/* 135 */     return this.guard.evaluate(paramFSM, paramInput).complement();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orbutil.fsm.NegateGuard
 * JD-Core Version:    0.6.2
 */