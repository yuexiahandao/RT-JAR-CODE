/*    */ package com.sun.jndi.cosnaming;
/*    */ 
/*    */ import com.sun.jndi.toolkit.corba.CorbaUtils;
/*    */ import java.rmi.Remote;
/*    */ import java.util.Hashtable;
/*    */ import javax.naming.ConfigurationException;
/*    */ import javax.naming.Context;
/*    */ import javax.naming.Name;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.spi.StateFactory;
/*    */ 
/*    */ public class RemoteToCorba
/*    */   implements StateFactory
/*    */ {
/*    */   public java.lang.Object getStateToBind(java.lang.Object paramObject, Name paramName, Context paramContext, Hashtable<?, ?> paramHashtable)
/*    */     throws NamingException
/*    */   {
/* 67 */     if ((paramObject instanceof org.omg.CORBA.Object))
/*    */     {
/* 69 */       return null;
/*    */     }
/*    */ 
/* 72 */     if ((paramObject instanceof Remote))
/*    */     {
/*    */       try
/*    */       {
/* 78 */         return CorbaUtils.remoteToCorba((Remote)paramObject, ((CNCtx)paramContext)._orb);
/*    */       }
/*    */       catch (ClassNotFoundException localClassNotFoundException)
/*    */       {
/* 82 */         throw new ConfigurationException("javax.rmi packages not available");
/*    */       }
/*    */     }
/*    */ 
/* 86 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.cosnaming.RemoteToCorba
 * JD-Core Version:    0.6.2
 */