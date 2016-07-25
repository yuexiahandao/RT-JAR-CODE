/*    */ package com.sun.corba.se.spi.orbutil.fsm;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ class TestAction1
/*    */   implements Action
/*    */ {
/*    */   private State oldState;
/*    */   private Input label;
/*    */   private State newState;
/*    */ 
/*    */   public void doIt(FSM paramFSM, Input paramInput)
/*    */   {
/* 61 */     System.out.println("TestAction1:");
/* 62 */     System.out.println("\tlabel    = " + this.label);
/* 63 */     System.out.println("\toldState = " + this.oldState);
/* 64 */     System.out.println("\tnewState = " + this.newState);
/* 65 */     if (this.label != paramInput)
/* 66 */       throw new Error("Unexcepted Input " + paramInput);
/* 67 */     if (this.oldState != paramFSM.getState())
/* 68 */       throw new Error("Unexpected old State " + paramFSM.getState());
/*    */   }
/*    */ 
/*    */   public TestAction1(State paramState1, Input paramInput, State paramState2)
/*    */   {
/* 73 */     this.oldState = paramState1;
/* 74 */     this.newState = paramState2;
/* 75 */     this.label = paramInput;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orbutil.fsm.TestAction1
 * JD-Core Version:    0.6.2
 */