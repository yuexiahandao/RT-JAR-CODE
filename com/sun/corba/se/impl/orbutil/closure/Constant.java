/*    */ package com.sun.corba.se.impl.orbutil.closure;
/*    */ 
/*    */ import com.sun.corba.se.spi.orbutil.closure.Closure;
/*    */ 
/*    */ public class Constant
/*    */   implements Closure
/*    */ {
/*    */   private Object value;
/*    */ 
/*    */   public Constant(Object paramObject)
/*    */   {
/* 35 */     this.value = paramObject;
/*    */   }
/*    */ 
/*    */   public Object evaluate()
/*    */   {
/* 40 */     return this.value;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.closure.Constant
 * JD-Core Version:    0.6.2
 */