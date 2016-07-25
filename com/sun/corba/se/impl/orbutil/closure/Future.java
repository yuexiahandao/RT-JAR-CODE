/*    */ package com.sun.corba.se.impl.orbutil.closure;
/*    */ 
/*    */ import com.sun.corba.se.spi.orbutil.closure.Closure;
/*    */ 
/*    */ public class Future
/*    */   implements Closure
/*    */ {
/*    */   private boolean evaluated;
/*    */   private Closure closure;
/*    */   private Object value;
/*    */ 
/*    */   public Future(Closure paramClosure)
/*    */   {
/* 37 */     this.evaluated = false;
/* 38 */     this.closure = paramClosure;
/* 39 */     this.value = null;
/*    */   }
/*    */ 
/*    */   public synchronized Object evaluate()
/*    */   {
/* 44 */     if (!this.evaluated) {
/* 45 */       this.evaluated = true;
/* 46 */       this.value = this.closure.evaluate();
/*    */     }
/*    */ 
/* 49 */     return this.value;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.closure.Future
 * JD-Core Version:    0.6.2
 */