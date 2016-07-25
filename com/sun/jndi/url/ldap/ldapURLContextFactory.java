/*    */ package com.sun.jndi.url.ldap;
/*    */ 
/*    */ import com.sun.jndi.ldap.LdapCtx;
/*    */ import com.sun.jndi.ldap.LdapCtxFactory;
/*    */ import com.sun.jndi.ldap.LdapURL;
/*    */ import java.util.Hashtable;
/*    */ import javax.naming.CompositeName;
/*    */ import javax.naming.Context;
/*    */ import javax.naming.Name;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.spi.ObjectFactory;
/*    */ import javax.naming.spi.ResolveResult;
/*    */ 
/*    */ public class ldapURLContextFactory
/*    */   implements ObjectFactory
/*    */ {
/*    */   public Object getObjectInstance(Object paramObject, Name paramName, Context paramContext, Hashtable<?, ?> paramHashtable)
/*    */     throws Exception
/*    */   {
/* 49 */     if (paramObject == null) {
/* 50 */       return new ldapURLContext(paramHashtable);
/*    */     }
/* 52 */     return LdapCtxFactory.getLdapCtxInstance(paramObject, paramHashtable);
/*    */   }
/*    */ 
/*    */   static ResolveResult getUsingURLIgnoreRootDN(String paramString, Hashtable paramHashtable)
/*    */     throws NamingException
/*    */   {
/* 58 */     LdapURL localLdapURL = new LdapURL(paramString);
/* 59 */     LdapCtx localLdapCtx = new LdapCtx("", localLdapURL.getHost(), localLdapURL.getPort(), paramHashtable, localLdapURL.useSsl());
/*    */ 
/* 61 */     String str = localLdapURL.getDN() != null ? localLdapURL.getDN() : "";
/*    */ 
/* 64 */     CompositeName localCompositeName = new CompositeName();
/* 65 */     if (!"".equals(str))
/*    */     {
/* 67 */       localCompositeName.add(str);
/*    */     }
/*    */ 
/* 70 */     return new ResolveResult(localLdapCtx, localCompositeName);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.url.ldap.ldapURLContextFactory
 * JD-Core Version:    0.6.2
 */