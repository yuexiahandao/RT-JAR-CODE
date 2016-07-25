/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class ORBAlreadyRegistered extends UserException
/*    */ {
/* 13 */   public String orbId = null;
/*    */ 
/*    */   public ORBAlreadyRegistered()
/*    */   {
/* 17 */     super(ORBAlreadyRegisteredHelper.id());
/*    */   }
/*    */ 
/*    */   public ORBAlreadyRegistered(String paramString)
/*    */   {
/* 22 */     super(ORBAlreadyRegisteredHelper.id());
/* 23 */     this.orbId = paramString;
/*    */   }
/*    */ 
/*    */   public ORBAlreadyRegistered(String paramString1, String paramString2)
/*    */   {
/* 29 */     super(ORBAlreadyRegisteredHelper.id() + "  " + paramString1);
/* 30 */     this.orbId = paramString2;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.ORBAlreadyRegistered
 * JD-Core Version:    0.6.2
 */