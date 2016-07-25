/*    */ package com.sun.jndi.url.dns;
/*    */ 
/*    */ import com.sun.jndi.dns.DnsContextFactory;
/*    */ import com.sun.jndi.dns.DnsUrl;
/*    */ import com.sun.jndi.toolkit.url.GenericURLDirContext;
/*    */ import java.net.MalformedURLException;
/*    */ import java.util.Hashtable;
/*    */ import javax.naming.CompositeName;
/*    */ import javax.naming.InvalidNameException;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.spi.ResolveResult;
/*    */ 
/*    */ public class dnsURLContext extends GenericURLDirContext
/*    */ {
/*    */   public dnsURLContext(Hashtable paramHashtable)
/*    */   {
/* 49 */     super(paramHashtable);
/*    */   }
/*    */ 
/*    */   protected ResolveResult getRootURLContext(String paramString, Hashtable paramHashtable)
/*    */     throws NamingException
/*    */   {
/*    */     DnsUrl localDnsUrl;
/*    */     try
/*    */     {
/* 62 */       localDnsUrl = new DnsUrl(paramString);
/*    */     } catch (MalformedURLException localMalformedURLException) {
/* 64 */       throw new InvalidNameException(localMalformedURLException.getMessage());
/*    */     }
/*    */ 
/* 67 */     DnsUrl[] arrayOfDnsUrl = { localDnsUrl };
/* 68 */     String str = localDnsUrl.getDomain();
/*    */ 
/* 70 */     return new ResolveResult(DnsContextFactory.getContext(".", arrayOfDnsUrl, paramHashtable), new CompositeName().add(str));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.url.dns.dnsURLContext
 * JD-Core Version:    0.6.2
 */