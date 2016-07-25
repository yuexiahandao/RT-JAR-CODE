/*    */ package com.sun.corba.se.impl.ior.iiop;
/*    */ 
/*    */ import com.sun.corba.se.spi.orbutil.closure.Closure;
/*    */ 
/*    */ public final class IIOPAddressClosureImpl extends IIOPAddressBase
/*    */ {
/*    */   private Closure host;
/*    */   private Closure port;
/*    */ 
/*    */   public IIOPAddressClosureImpl(Closure paramClosure1, Closure paramClosure2)
/*    */   {
/* 45 */     this.host = paramClosure1;
/* 46 */     this.port = paramClosure2;
/*    */   }
/*    */ 
/*    */   public String getHost()
/*    */   {
/* 51 */     return (String)this.host.evaluate();
/*    */   }
/*    */ 
/*    */   public int getPort()
/*    */   {
/* 56 */     Integer localInteger = (Integer)this.port.evaluate();
/* 57 */     return localInteger.intValue();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.iiop.IIOPAddressClosureImpl
 * JD-Core Version:    0.6.2
 */