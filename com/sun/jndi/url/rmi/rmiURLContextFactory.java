/*    */ package com.sun.jndi.url.rmi;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import javax.naming.ConfigurationException;
/*    */ import javax.naming.Context;
/*    */ import javax.naming.Name;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.spi.ObjectFactory;
/*    */ 
/*    */ public class rmiURLContextFactory
/*    */   implements ObjectFactory
/*    */ {
/*    */   public Object getObjectInstance(Object paramObject, Name paramName, Context paramContext, Hashtable<?, ?> paramHashtable)
/*    */     throws NamingException
/*    */   {
/* 53 */     if (paramObject == null)
/* 54 */       return new rmiURLContext(paramHashtable);
/* 55 */     if ((paramObject instanceof String))
/* 56 */       return getUsingURL((String)paramObject, paramHashtable);
/* 57 */     if ((paramObject instanceof String[])) {
/* 58 */       return getUsingURLs((String[])paramObject, paramHashtable);
/*    */     }
/* 60 */     throw new ConfigurationException("rmiURLContextFactory.getObjectInstance: argument must be an RMI URL String or an array of them");
/*    */   }
/*    */ 
/*    */   private static Object getUsingURL(String paramString, Hashtable paramHashtable)
/*    */     throws NamingException
/*    */   {
/* 69 */     rmiURLContext localrmiURLContext = new rmiURLContext(paramHashtable);
/*    */     try {
/* 71 */       return localrmiURLContext.lookup(paramString);
/*    */     } finally {
/* 73 */       localrmiURLContext.close();
/*    */     }
/*    */   }
/*    */ 
/*    */   private static Object getUsingURLs(String[] paramArrayOfString, Hashtable paramHashtable)
/*    */     throws NamingException
/*    */   {
/* 85 */     if (paramArrayOfString.length == 0) {
/* 86 */       throw new ConfigurationException("rmiURLContextFactory: empty URL array");
/*    */     }
/*    */ 
/* 89 */     rmiURLContext localrmiURLContext = new rmiURLContext(paramHashtable);
/*    */     try {
/* 91 */       Object localObject1 = null;
/* 92 */       for (int i = 0; i < paramArrayOfString.length; i++) {
/*    */         try {
/* 94 */           return localrmiURLContext.lookup(paramArrayOfString[i]);
/*    */         } catch (NamingException localNamingException) {
/* 96 */           localObject1 = localNamingException;
/*    */         }
/*    */       }
/* 99 */       throw localObject1;
/*    */     } finally {
/* 101 */       localrmiURLContext.close();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.url.rmi.rmiURLContextFactory
 * JD-Core Version:    0.6.2
 */