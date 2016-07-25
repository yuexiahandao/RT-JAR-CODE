/*    */ package com.sun.jndi.url.iiop;
/*    */ 
/*    */ import com.sun.jndi.cosnaming.CorbanameUrl;
/*    */ import com.sun.jndi.cosnaming.IiopUrl;
/*    */ import com.sun.jndi.toolkit.url.GenericURLContext;
/*    */ import java.net.MalformedURLException;
/*    */ import java.util.Hashtable;
/*    */ import javax.naming.InvalidNameException;
/*    */ import javax.naming.Name;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.spi.ResolveResult;
/*    */ 
/*    */ public class iiopURLContext extends GenericURLContext
/*    */ {
/*    */   iiopURLContext(Hashtable paramHashtable)
/*    */   {
/* 46 */     super(paramHashtable);
/*    */   }
/*    */ 
/*    */   protected ResolveResult getRootURLContext(String paramString, Hashtable paramHashtable)
/*    */     throws NamingException
/*    */   {
/* 61 */     return iiopURLContextFactory.getUsingURLIgnoreRest(paramString, paramHashtable);
/*    */   }
/*    */ 
/*    */   protected Name getURLSuffix(String paramString1, String paramString2)
/*    */     throws NamingException
/*    */   {
/*    */     try
/*    */     {
/*    */       Object localObject;
/* 71 */       if ((paramString2.startsWith("iiop://")) || (paramString2.startsWith("iiopname://"))) {
/* 72 */         localObject = new IiopUrl(paramString2);
/* 73 */         return ((IiopUrl)localObject).getCosName();
/* 74 */       }if (paramString2.startsWith("corbaname:")) {
/* 75 */         localObject = new CorbanameUrl(paramString2);
/* 76 */         return ((CorbanameUrl)localObject).getCosName();
/*    */       }
/* 78 */       throw new MalformedURLException("Not a valid URL: " + paramString2);
/*    */     }
/*    */     catch (MalformedURLException localMalformedURLException) {
/* 81 */       throw new InvalidNameException(localMalformedURLException.getMessage());
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.url.iiop.iiopURLContext
 * JD-Core Version:    0.6.2
 */