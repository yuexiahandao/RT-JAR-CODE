/*    */ package com.sun.jndi.url.iiop;
/*    */ 
/*    */ import com.sun.jndi.cosnaming.CNCtx;
/*    */ import java.util.Hashtable;
/*    */ import javax.naming.Context;
/*    */ import javax.naming.Name;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.spi.ObjectFactory;
/*    */ import javax.naming.spi.ResolveResult;
/*    */ 
/*    */ public class iiopURLContextFactory
/*    */   implements ObjectFactory
/*    */ {
/*    */   public Object getObjectInstance(Object paramObject, Name paramName, Context paramContext, Hashtable<?, ?> paramHashtable)
/*    */     throws Exception
/*    */   {
/* 47 */     if (paramObject == null) {
/* 48 */       return new iiopURLContext(paramHashtable);
/*    */     }
/* 50 */     if ((paramObject instanceof String))
/* 51 */       return getUsingURL((String)paramObject, paramHashtable);
/* 52 */     if ((paramObject instanceof String[])) {
/* 53 */       return getUsingURLs((String[])paramObject, paramHashtable);
/*    */     }
/* 55 */     throw new IllegalArgumentException("iiopURLContextFactory.getObjectInstance: argument must be a URL String or array of URLs");
/*    */   }
/*    */ 
/*    */   static ResolveResult getUsingURLIgnoreRest(String paramString, Hashtable paramHashtable)
/*    */     throws NamingException
/*    */   {
/* 73 */     return CNCtx.createUsingURL(paramString, paramHashtable);
/*    */   }
/*    */ 
/*    */   private static Object getUsingURL(String paramString, Hashtable paramHashtable) throws NamingException
/*    */   {
/* 78 */     ResolveResult localResolveResult = getUsingURLIgnoreRest(paramString, paramHashtable);
/*    */ 
/* 80 */     Context localContext = (Context)localResolveResult.getResolvedObj();
/*    */     try {
/* 82 */       return localContext.lookup(localResolveResult.getRemainingName());
/*    */     } finally {
/* 84 */       localContext.close();
/*    */     }
/*    */   }
/*    */ 
/*    */   private static Object getUsingURLs(String[] paramArrayOfString, Hashtable paramHashtable) {
/* 89 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 90 */       String str = paramArrayOfString[i];
/*    */       try {
/* 92 */         Object localObject = getUsingURL(str, paramHashtable);
/* 93 */         if (localObject != null)
/* 94 */           return localObject;
/*    */       }
/*    */       catch (NamingException localNamingException) {
/*    */       }
/*    */     }
/* 99 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.url.iiop.iiopURLContextFactory
 * JD-Core Version:    0.6.2
 */