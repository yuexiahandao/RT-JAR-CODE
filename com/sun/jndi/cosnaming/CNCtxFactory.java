/*    */ package com.sun.jndi.cosnaming;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import javax.naming.Context;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.spi.InitialContextFactory;
/*    */ 
/*    */ public class CNCtxFactory
/*    */   implements InitialContextFactory
/*    */ {
/*    */   public Context getInitialContext(Hashtable<?, ?> paramHashtable)
/*    */     throws NamingException
/*    */   {
/* 49 */     return new CNCtx(paramHashtable);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.cosnaming.CNCtxFactory
 * JD-Core Version:    0.6.2
 */