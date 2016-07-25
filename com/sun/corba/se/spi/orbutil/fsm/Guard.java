/*    */ package com.sun.corba.se.spi.orbutil.fsm;
/*    */ 
/*    */ public abstract interface Guard
/*    */ {
/*    */   public abstract Result evaluate(FSM paramFSM, Input paramInput);
/*    */ 
/*    */   public static final class Complement extends GuardBase
/*    */   {
/*    */     private Guard guard;
/*    */ 
/*    */     public Complement(GuardBase paramGuardBase)
/*    */     {
/* 39 */       super();
/* 40 */       this.guard = paramGuardBase;
/*    */     }
/*    */ 
/*    */     public Guard.Result evaluate(FSM paramFSM, Input paramInput)
/*    */     {
/* 45 */       return this.guard.evaluate(paramFSM, paramInput).complement();
/*    */     }
/*    */   }
/*    */ 
/*    */   public static final class Result
/*    */   {
/*    */     private String name;
/* 77 */     public static final Result ENABLED = new Result("ENABLED");
/* 78 */     public static final Result DISABLED = new Result("DISABLED");
/* 79 */     public static final Result DEFERED = new Result("DEFERED");
/*    */ 
/*    */     private Result(String paramString)
/*    */     {
/* 54 */       this.name = paramString;
/*    */     }
/*    */ 
/*    */     public static Result convert(boolean paramBoolean)
/*    */     {
/* 59 */       return paramBoolean ? ENABLED : DISABLED;
/*    */     }
/*    */ 
/*    */     public Result complement()
/*    */     {
/* 64 */       if (this == ENABLED)
/* 65 */         return DISABLED;
/* 66 */       if (this == DISABLED) {
/* 67 */         return ENABLED;
/*    */       }
/* 69 */       return DEFERED;
/*    */     }
/*    */ 
/*    */     public String toString()
/*    */     {
/* 74 */       return "Guard.Result[" + this.name + "]";
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orbutil.fsm.Guard
 * JD-Core Version:    0.6.2
 */