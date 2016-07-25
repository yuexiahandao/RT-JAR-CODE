/*    */ package com.sun.corba.se.spi.orbutil.closure;
/*    */ 
/*    */ import com.sun.corba.se.impl.orbutil.closure.Constant;
/*    */ import com.sun.corba.se.impl.orbutil.closure.Future;
/*    */ 
/*    */ public abstract class ClosureFactory
/*    */ {
/*    */   public static Closure makeConstant(Object paramObject)
/*    */   {
/* 36 */     return new Constant(paramObject);
/*    */   }
/*    */ 
/*    */   public static Closure makeFuture(Closure paramClosure)
/*    */   {
/* 41 */     return new Future(paramClosure);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orbutil.closure.ClosureFactory
 * JD-Core Version:    0.6.2
 */