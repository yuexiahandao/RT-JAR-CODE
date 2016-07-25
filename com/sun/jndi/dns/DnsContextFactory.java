/*     */ package com.sun.jndi.dns;
/*     */ 
/*     */ import com.sun.jndi.toolkit.url.UrlUtil;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.naming.ConfigurationException;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.spi.InitialContextFactory;
/*     */ import sun.net.dns.ResolverConfiguration;
/*     */ 
/*     */ public class DnsContextFactory
/*     */   implements InitialContextFactory
/*     */ {
/*     */   private static final String DEFAULT_URL = "dns:";
/*     */   private static final int DEFAULT_PORT = 53;
/*     */ 
/*     */   public Context getInitialContext(Hashtable<?, ?> paramHashtable)
/*     */     throws NamingException
/*     */   {
/*  61 */     if (paramHashtable == null) {
/*  62 */       paramHashtable = new Hashtable(5);
/*     */     }
/*  64 */     return urlToContext(getInitCtxUrl(paramHashtable), paramHashtable);
/*     */   }
/*     */ 
/*     */   public static DnsContext getContext(String paramString, String[] paramArrayOfString, Hashtable<?, ?> paramHashtable)
/*     */     throws NamingException
/*     */   {
/*  70 */     return new DnsContext(paramString, paramArrayOfString, paramHashtable);
/*     */   }
/*     */ 
/*     */   public static DnsContext getContext(String paramString, DnsUrl[] paramArrayOfDnsUrl, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/*  81 */     String[] arrayOfString = serversForUrls(paramArrayOfDnsUrl);
/*  82 */     DnsContext localDnsContext = getContext(paramString, arrayOfString, paramHashtable);
/*  83 */     if (platformServersUsed(paramArrayOfDnsUrl)) {
/*  84 */       localDnsContext.setProviderUrl(constructProviderUrl(paramString, arrayOfString));
/*     */     }
/*  86 */     return localDnsContext;
/*     */   }
/*     */ 
/*     */   public static boolean platformServersAvailable()
/*     */   {
/*  93 */     return !filterNameServers(ResolverConfiguration.open().nameservers(), true).isEmpty();
/*     */   }
/*     */ 
/*     */   private static Context urlToContext(String paramString, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/*     */     DnsUrl[] arrayOfDnsUrl;
/*     */     try
/*     */     {
/* 103 */       arrayOfDnsUrl = DnsUrl.fromList(paramString);
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 105 */       throw new ConfigurationException(localMalformedURLException.getMessage());
/*     */     }
/* 107 */     if (arrayOfDnsUrl.length == 0) {
/* 108 */       throw new ConfigurationException("Invalid DNS pseudo-URL(s): " + paramString);
/*     */     }
/*     */ 
/* 111 */     String str = arrayOfDnsUrl[0].getDomain();
/*     */ 
/* 114 */     for (int i = 1; i < arrayOfDnsUrl.length; i++) {
/* 115 */       if (!str.equalsIgnoreCase(arrayOfDnsUrl[i].getDomain())) {
/* 116 */         throw new ConfigurationException("Conflicting domains: " + paramString);
/*     */       }
/*     */     }
/*     */ 
/* 120 */     return getContext(str, arrayOfDnsUrl, paramHashtable);
/*     */   }
/*     */ 
/*     */   private static String[] serversForUrls(DnsUrl[] paramArrayOfDnsUrl)
/*     */     throws NamingException
/*     */   {
/* 134 */     if (paramArrayOfDnsUrl.length == 0) {
/* 135 */       throw new ConfigurationException("DNS pseudo-URL required");
/*     */     }
/*     */ 
/* 138 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 140 */     for (int i = 0; i < paramArrayOfDnsUrl.length; i++) {
/* 141 */       String str = paramArrayOfDnsUrl[i].getHost();
/* 142 */       int j = paramArrayOfDnsUrl[i].getPort();
/*     */ 
/* 144 */       if ((str == null) && (j < 0))
/*     */       {
/* 148 */         List localList = filterNameServers(ResolverConfiguration.open().nameservers(), false);
/*     */ 
/* 150 */         if (!localList.isEmpty()) {
/* 151 */           localArrayList.addAll(localList);
/* 152 */           continue;
/*     */         }
/*     */       }
/*     */ 
/* 156 */       if (str == null) {
/* 157 */         str = "localhost";
/*     */       }
/* 159 */       localArrayList.add(str + ":" + j);
/*     */     }
/*     */ 
/* 163 */     return (String[])localArrayList.toArray(new String[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   private static boolean platformServersUsed(DnsUrl[] paramArrayOfDnsUrl)
/*     */   {
/* 171 */     if (!platformServersAvailable()) {
/* 172 */       return false;
/*     */     }
/* 174 */     for (int i = 0; i < paramArrayOfDnsUrl.length; i++) {
/* 175 */       if ((paramArrayOfDnsUrl[i].getHost() == null) && (paramArrayOfDnsUrl[i].getPort() < 0))
/*     */       {
/* 177 */         return true;
/*     */       }
/*     */     }
/* 180 */     return false;
/*     */   }
/*     */ 
/*     */   private static String constructProviderUrl(String paramString, String[] paramArrayOfString)
/*     */   {
/* 192 */     String str = "";
/* 193 */     if (!paramString.equals(".")) {
/*     */       try {
/* 195 */         str = "/" + UrlUtil.encode(paramString, "ISO-8859-1");
/*     */       }
/*     */       catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*     */       {
/*     */       }
/*     */     }
/* 201 */     StringBuffer localStringBuffer = new StringBuffer();
/* 202 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 203 */       if (i > 0) {
/* 204 */         localStringBuffer.append(' ');
/*     */       }
/* 206 */       localStringBuffer.append("dns://").append(paramArrayOfString[i]).append(str);
/*     */     }
/* 208 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static String getInitCtxUrl(Hashtable paramHashtable)
/*     */   {
/* 216 */     String str = (String)paramHashtable.get("java.naming.provider.url");
/* 217 */     return str != null ? str : "dns:";
/*     */   }
/*     */ 
/*     */   private static List filterNameServers(List paramList, boolean paramBoolean)
/*     */   {
/* 227 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 228 */     if ((localSecurityManager == null) || (paramList == null) || (paramList.isEmpty())) {
/* 229 */       return paramList;
/*     */     }
/* 231 */     ArrayList localArrayList = new ArrayList();
/* 232 */     for (Iterator localIterator = paramList.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 233 */       if ((localObject instanceof String)) {
/* 234 */         String str1 = (String)localObject;
/* 235 */         int i = str1.indexOf(':', str1.indexOf(93) + 1);
/*     */ 
/* 238 */         int j = i < 0 ? 53 : Integer.parseInt(str1.substring(i + 1));
/*     */ 
/* 242 */         String str2 = i < 0 ? str1 : str1.substring(0, i);
/*     */         try
/*     */         {
/* 246 */           localSecurityManager.checkConnect(str2, j);
/* 247 */           localArrayList.add(str1);
/* 248 */           if (paramBoolean)
/* 249 */             return localArrayList;
/*     */         }
/*     */         catch (SecurityException localSecurityException)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/* 256 */     return localArrayList;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.dns.DnsContextFactory
 * JD-Core Version:    0.6.2
 */