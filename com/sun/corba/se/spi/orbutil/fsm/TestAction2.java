/*     */ package com.sun.corba.se.spi.orbutil.fsm;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ class TestAction2
/*     */   implements Action
/*     */ {
/*     */   private State oldState;
/*     */   private State newState;
/*     */ 
/*     */   public void doIt(FSM paramFSM, Input paramInput)
/*     */   {
/*  90 */     System.out.println("TestAction2:");
/*  91 */     System.out.println("\toldState = " + this.oldState);
/*  92 */     System.out.println("\tnewState = " + this.newState);
/*  93 */     System.out.println("\tinput    = " + paramInput);
/*  94 */     if (this.oldState != paramFSM.getState())
/*  95 */       throw new Error("Unexpected old State " + paramFSM.getState());
/*     */   }
/*     */ 
/*     */   public TestAction2(State paramState1, State paramState2)
/*     */   {
/* 100 */     this.oldState = paramState1;
/* 101 */     this.newState = paramState2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orbutil.fsm.TestAction2
 * JD-Core Version:    0.6.2
 */