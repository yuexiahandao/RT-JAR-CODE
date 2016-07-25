/*     */ package com.sun.jndi.url.rmi;
/*     */ 
/*     */ import com.sun.jndi.rmi.registry.RegistryContext;
/*     */ import com.sun.jndi.toolkit.url.GenericURLContext;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.spi.ResolveResult;
/*     */ 
/*     */ public class rmiURLContext extends GenericURLContext
/*     */ {
/*     */   public rmiURLContext(Hashtable paramHashtable)
/*     */   {
/*  52 */     super(paramHashtable);
/*     */   }
/*     */ 
/*     */   protected ResolveResult getRootURLContext(String paramString, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/*  63 */     if (!paramString.startsWith("rmi:")) {
/*  64 */       throw new IllegalArgumentException("rmiURLContext: name is not an RMI URL: " + paramString);
/*     */     }
/*     */ 
/*  70 */     String str1 = null;
/*  71 */     int i = -1;
/*  72 */     String str2 = null;
/*     */ 
/*  74 */     int j = 4;
/*     */ 
/*  76 */     if (paramString.startsWith("//", j)) {
/*  77 */       j += 2;
/*  78 */       int k = paramString.indexOf('/', j);
/*  79 */       if (k < 0)
/*  80 */         k = paramString.length();
/*     */       int m;
/*  82 */       if (paramString.startsWith("[", j)) {
/*  83 */         m = paramString.indexOf(']', j + 1);
/*  84 */         if ((m < 0) || (m > k)) {
/*  85 */           throw new IllegalArgumentException("rmiURLContext: name is an Invalid URL: " + paramString);
/*     */         }
/*     */ 
/*  88 */         str1 = paramString.substring(j, m + 1);
/*  89 */         j = m + 1;
/*     */       } else {
/*  91 */         m = paramString.indexOf(':', j);
/*  92 */         int n = (m < 0) || (m > k) ? k : m;
/*     */ 
/*  95 */         if (j < n) {
/*  96 */           str1 = paramString.substring(j, n);
/*     */         }
/*  98 */         j = n;
/*     */       }
/* 100 */       if (j + 1 < k) {
/* 101 */         if (paramString.startsWith(":", j)) {
/* 102 */           j++;
/* 103 */           i = Integer.parseInt(paramString.substring(j, k));
/*     */         } else {
/* 105 */           throw new IllegalArgumentException("rmiURLContext: name is an Invalid URL: " + paramString);
/*     */         }
/*     */       }
/*     */ 
/* 109 */       j = k;
/*     */     }
/* 111 */     if ("".equals(str1)) {
/* 112 */       str1 = null;
/*     */     }
/* 114 */     if (paramString.startsWith("/", j)) {
/* 115 */       j++;
/*     */     }
/* 117 */     if (j < paramString.length()) {
/* 118 */       str2 = paramString.substring(j);
/*     */     }
/*     */ 
/* 122 */     CompositeName localCompositeName = new CompositeName();
/* 123 */     if (str2 != null) {
/* 124 */       localCompositeName.add(str2);
/*     */     }
/*     */ 
/* 132 */     RegistryContext localRegistryContext = new RegistryContext(str1, i, paramHashtable);
/*     */ 
/* 134 */     return new ResolveResult(localRegistryContext, localCompositeName);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.url.rmi.rmiURLContext
 * JD-Core Version:    0.6.2
 */