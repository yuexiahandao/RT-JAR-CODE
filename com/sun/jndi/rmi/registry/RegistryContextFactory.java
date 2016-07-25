/*     */ package com.sun.jndi.rmi.registry;
/*     */ 
/*     */ import com.sun.jndi.url.rmi.rmiURLContextFactory;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.ConfigurationException;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.NotContextException;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.StringRefAddr;
/*     */ import javax.naming.spi.InitialContextFactory;
/*     */ import javax.naming.spi.ObjectFactory;
/*     */ 
/*     */ public class RegistryContextFactory
/*     */   implements ObjectFactory, InitialContextFactory
/*     */ {
/*     */   public static final String ADDRESS_TYPE = "URL";
/*     */ 
/*     */   public Context getInitialContext(Hashtable<?, ?> paramHashtable)
/*     */     throws NamingException
/*     */   {
/*  66 */     if (paramHashtable != null) {
/*  67 */       paramHashtable = (Hashtable)paramHashtable.clone();
/*     */     }
/*  69 */     return URLToContext(getInitCtxURL(paramHashtable), paramHashtable);
/*     */   }
/*     */ 
/*     */   public Object getObjectInstance(Object paramObject, Name paramName, Context paramContext, Hashtable<?, ?> paramHashtable)
/*     */     throws NamingException
/*     */   {
/*  76 */     if (!isRegistryRef(paramObject)) {
/*  77 */       return null;
/*     */     }
/*     */ 
/*  90 */     Object localObject = URLsToObject(getURLs((Reference)paramObject), paramHashtable);
/*  91 */     if ((localObject instanceof RegistryContext)) {
/*  92 */       RegistryContext localRegistryContext = (RegistryContext)localObject;
/*  93 */       localRegistryContext.reference = ((Reference)paramObject);
/*     */     }
/*  95 */     return localObject;
/*     */   }
/*     */ 
/*     */   private static Context URLToContext(String paramString, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 101 */     rmiURLContextFactory localrmiURLContextFactory = new rmiURLContextFactory();
/* 102 */     Object localObject = localrmiURLContextFactory.getObjectInstance(paramString, null, null, paramHashtable);
/*     */ 
/* 104 */     if ((localObject instanceof Context)) {
/* 105 */       return (Context)localObject;
/*     */     }
/* 107 */     throw new NotContextException(paramString);
/*     */   }
/*     */ 
/*     */   private static Object URLsToObject(String[] paramArrayOfString, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 114 */     rmiURLContextFactory localrmiURLContextFactory = new rmiURLContextFactory();
/* 115 */     return localrmiURLContextFactory.getObjectInstance(paramArrayOfString, null, null, paramHashtable);
/*     */   }
/*     */ 
/*     */   private static String getInitCtxURL(Hashtable paramHashtable)
/*     */   {
/* 126 */     String str = null;
/* 127 */     if (paramHashtable != null) {
/* 128 */       str = (String)paramHashtable.get("java.naming.provider.url");
/*     */     }
/* 130 */     return str != null ? str : "rmi:";
/*     */   }
/*     */ 
/*     */   private static boolean isRegistryRef(Object paramObject)
/*     */   {
/* 138 */     if (!(paramObject instanceof Reference)) {
/* 139 */       return false;
/*     */     }
/* 141 */     String str = RegistryContextFactory.class.getName();
/* 142 */     Reference localReference = (Reference)paramObject;
/*     */ 
/* 144 */     return str.equals(localReference.getFactoryClassName());
/*     */   }
/*     */ 
/*     */   private static String[] getURLs(Reference paramReference)
/*     */     throws NamingException
/*     */   {
/* 152 */     int i = 0;
/* 153 */     String[] arrayOfString = new String[paramReference.size()];
/*     */ 
/* 155 */     Enumeration localEnumeration = paramReference.getAll();
/* 156 */     while (localEnumeration.hasMoreElements()) {
/* 157 */       localObject = (RefAddr)localEnumeration.nextElement();
/*     */ 
/* 159 */       if (((localObject instanceof StringRefAddr)) && (((RefAddr)localObject).getType().equals("URL")))
/*     */       {
/* 162 */         arrayOfString[(i++)] = ((String)((RefAddr)localObject).getContent());
/*     */       }
/*     */     }
/* 165 */     if (i == 0) {
/* 166 */       throw new ConfigurationException("Reference contains no valid addresses");
/*     */     }
/*     */ 
/* 171 */     if (i == paramReference.size()) {
/* 172 */       return arrayOfString;
/*     */     }
/* 174 */     Object localObject = new String[i];
/* 175 */     System.arraycopy(arrayOfString, 0, localObject, 0, i);
/* 176 */     return localObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.rmi.registry.RegistryContextFactory
 * JD-Core Version:    0.6.2
 */