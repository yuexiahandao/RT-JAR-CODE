/*     */ package com.sun.jndi.cosnaming;
/*     */ 
/*     */ import com.sun.jndi.toolkit.url.UrlUtil;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ public final class IiopUrl
/*     */ {
/*     */   private static final int DEFAULT_IIOPNAME_PORT = 9999;
/*     */   private static final int DEFAULT_IIOP_PORT = 900;
/*     */   private static final String DEFAULT_HOST = "localhost";
/*     */   private Vector addresses;
/*     */   private String stringName;
/*     */ 
/*     */   public Vector getAddresses()
/*     */   {
/* 153 */     return this.addresses;
/*     */   }
/*     */ 
/*     */   public String getStringName()
/*     */   {
/* 161 */     return this.stringName;
/*     */   }
/*     */ 
/*     */   public Name getCosName() throws NamingException {
/* 165 */     return CNCtx.parser.parse(this.stringName);
/*     */   }
/*     */ 
/*     */   public IiopUrl(String paramString)
/*     */     throws MalformedURLException
/*     */   {
/*     */     boolean bool;
/*     */     int i;
/* 172 */     if (paramString.startsWith("iiopname://")) {
/* 173 */       bool = false;
/* 174 */       i = 11;
/* 175 */     } else if (paramString.startsWith("iiop://")) {
/* 176 */       bool = true;
/* 177 */       i = 7;
/*     */     } else {
/* 179 */       throw new MalformedURLException("Invalid iiop/iiopname URL: " + paramString);
/*     */     }
/* 181 */     int j = paramString.indexOf('/', i);
/* 182 */     if (j < 0) {
/* 183 */       j = paramString.length();
/* 184 */       this.stringName = "";
/*     */     } else {
/* 186 */       this.stringName = UrlUtil.decode(paramString.substring(j + 1));
/*     */     }
/* 188 */     this.addresses = new Vector(3);
/* 189 */     if (bool)
/*     */     {
/* 191 */       this.addresses.addElement(new Address(paramString.substring(i, j), bool));
/*     */     }
/*     */     else {
/* 194 */       StringTokenizer localStringTokenizer = new StringTokenizer(paramString.substring(i, j), ",");
/*     */ 
/* 196 */       while (localStringTokenizer.hasMoreTokens()) {
/* 197 */         this.addresses.addElement(new Address(localStringTokenizer.nextToken(), bool));
/*     */       }
/* 199 */       if (this.addresses.size() == 0)
/* 200 */         this.addresses.addElement(new Address("", bool));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Address
/*     */   {
/*  75 */     public int port = -1;
/*     */     public int major;
/*     */     public int minor;
/*     */     public String host;
/*     */ 
/*     */     public Address(String paramString, boolean paramBoolean)
/*     */       throws MalformedURLException
/*     */     {
/*     */       int j;
/*  86 */       if ((paramBoolean) || ((j = paramString.indexOf('@')) < 0)) {
/*  87 */         this.major = 1;
/*  88 */         this.minor = 0;
/*  89 */         i = 0;
/*     */       } else {
/*  91 */         k = paramString.indexOf('.');
/*  92 */         if (k < 0) {
/*  93 */           throw new MalformedURLException("invalid version: " + paramString);
/*     */         }
/*     */         try
/*     */         {
/*  97 */           this.major = Integer.parseInt(paramString.substring(0, k));
/*  98 */           this.minor = Integer.parseInt(paramString.substring(k + 1, j));
/*     */         } catch (NumberFormatException localNumberFormatException) {
/* 100 */           throw new MalformedURLException("Nonnumeric version: " + paramString);
/*     */         }
/*     */ 
/* 103 */         i = j + 1;
/*     */       }
/*     */ 
/* 107 */       int k = paramString.indexOf('/', i);
/* 108 */       if (k < 0)
/* 109 */         k = paramString.length();
/*     */       int m;
/* 111 */       if (paramString.startsWith("[", i)) {
/* 112 */         m = paramString.indexOf(']', i + 1);
/* 113 */         if ((m < 0) || (m > k)) {
/* 114 */           throw new IllegalArgumentException("IiopURL: name is an Invalid URL: " + paramString);
/*     */         }
/*     */ 
/* 119 */         this.host = paramString.substring(i, m + 1);
/* 120 */         i = m + 1;
/*     */       } else {
/* 122 */         m = paramString.indexOf(':', i);
/* 123 */         int n = (m < 0) || (m > k) ? k : m;
/*     */ 
/* 126 */         if (i < n) {
/* 127 */           this.host = paramString.substring(i, n);
/*     */         }
/* 129 */         i = n;
/*     */       }
/* 131 */       if (i + 1 < k) {
/* 132 */         if (paramString.startsWith(":", i)) {
/* 133 */           i++;
/* 134 */           this.port = Integer.parseInt(paramString.substring(i, k));
/*     */         }
/*     */         else {
/* 137 */           throw new IllegalArgumentException("IiopURL: name is an Invalid URL: " + paramString);
/*     */         }
/*     */       }
/*     */ 
/* 141 */       int i = k;
/* 142 */       if (("".equals(this.host)) || (this.host == null)) {
/* 143 */         this.host = "localhost";
/*     */       }
/* 145 */       if (this.port == -1)
/* 146 */         this.port = (paramBoolean ? 900 : 9999);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.cosnaming.IiopUrl
 * JD-Core Version:    0.6.2
 */