/*    */ package com.sun.jndi.url.dns;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import javax.naming.ConfigurationException;
/*    */ import javax.naming.Context;
/*    */ import javax.naming.Name;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.spi.ObjectFactory;
/*    */ 
/*    */ public class dnsURLContextFactory
/*    */   implements ObjectFactory
/*    */ {
/*    */   public Object getObjectInstance(Object paramObject, Name paramName, Context paramContext, Hashtable<?, ?> paramHashtable)
/*    */     throws NamingException
/*    */   {
/* 52 */     if (paramObject == null)
/* 53 */       return new dnsURLContext(paramHashtable);
/* 54 */     if ((paramObject instanceof String))
/* 55 */       return getUsingURL((String)paramObject, paramHashtable);
/* 56 */     if ((paramObject instanceof String[])) {
/* 57 */       return getUsingURLs((String[])paramObject, paramHashtable);
/*    */     }
/* 59 */     throw new ConfigurationException("dnsURLContextFactory.getObjectInstance: argument must be a DNS URL String or an array of them");
/*    */   }
/*    */ 
/*    */   private static Object getUsingURL(String paramString, Hashtable paramHashtable)
/*    */     throws NamingException
/*    */   {
/* 68 */     dnsURLContext localdnsURLContext = new dnsURLContext(paramHashtable);
/*    */     try {
/* 70 */       return localdnsURLContext.lookup(paramString);
/*    */     } finally {
/* 72 */       localdnsURLContext.close();
/*    */     }
/*    */   }
/*    */ 
/*    */   private static Object getUsingURLs(String[] paramArrayOfString, Hashtable paramHashtable)
/*    */     throws NamingException
/*    */   {
/* 84 */     if (paramArrayOfString.length == 0) {
/* 85 */       throw new ConfigurationException("dnsURLContextFactory: empty URL array");
/*    */     }
/*    */ 
/* 88 */     dnsURLContext localdnsURLContext = new dnsURLContext(paramHashtable);
/*    */     try {
/* 90 */       Object localObject1 = null;
/* 91 */       for (int i = 0; i < paramArrayOfString.length; i++) {
/*    */         try {
/* 93 */           return localdnsURLContext.lookup(paramArrayOfString[i]);
/*    */         } catch (NamingException localNamingException) {
/* 95 */           localObject1 = localNamingException;
/*    */         }
/*    */       }
/* 98 */       throw localObject1;
/*    */     } finally {
/* 100 */       localdnsURLContext.close();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.url.dns.dnsURLContextFactory
 * JD-Core Version:    0.6.2
 */