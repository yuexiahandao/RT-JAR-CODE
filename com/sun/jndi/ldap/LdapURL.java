/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import com.sun.jndi.toolkit.url.Uri;
/*     */ import com.sun.jndi.toolkit.url.UrlUtil;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ public final class LdapURL extends Uri
/*     */ {
/*  70 */   private boolean useSsl = false;
/*  71 */   private String DN = null;
/*  72 */   private String attributes = null;
/*  73 */   private String scope = null;
/*  74 */   private String filter = null;
/*  75 */   private String extensions = null;
/*     */ 
/*     */   public LdapURL(String paramString)
/*     */     throws NamingException
/*     */   {
/*     */     try
/*     */     {
/*  85 */       init(paramString);
/*  86 */       this.useSsl = this.scheme.equalsIgnoreCase("ldaps");
/*     */ 
/*  88 */       if ((!this.scheme.equalsIgnoreCase("ldap")) && (!this.useSsl)) {
/*  89 */         throw new MalformedURLException("Not an LDAP URL: " + paramString);
/*     */       }
/*     */ 
/*  92 */       parsePathAndQuery();
/*     */     }
/*     */     catch (MalformedURLException localMalformedURLException) {
/*  95 */       localNamingException = new NamingException("Cannot parse url: " + paramString);
/*  96 */       localNamingException.setRootCause(localMalformedURLException);
/*  97 */       throw localNamingException;
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*  99 */       NamingException localNamingException = new NamingException("Cannot parse url: " + paramString);
/* 100 */       localNamingException.setRootCause(localUnsupportedEncodingException);
/* 101 */       throw localNamingException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean useSsl()
/*     */   {
/* 109 */     return this.useSsl;
/*     */   }
/*     */ 
/*     */   public String getDN()
/*     */   {
/* 116 */     return this.DN;
/*     */   }
/*     */ 
/*     */   public String getAttributes()
/*     */   {
/* 123 */     return this.attributes;
/*     */   }
/*     */ 
/*     */   public String getScope()
/*     */   {
/* 130 */     return this.scope;
/*     */   }
/*     */ 
/*     */   public String getFilter()
/*     */   {
/* 137 */     return this.filter;
/*     */   }
/*     */ 
/*     */   public String getExtensions()
/*     */   {
/* 144 */     return this.extensions;
/*     */   }
/*     */ 
/*     */   public static String[] fromList(String paramString)
/*     */     throws NamingException
/*     */   {
/* 152 */     String[] arrayOfString1 = new String[(paramString.length() + 1) / 2];
/* 153 */     int i = 0;
/* 154 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, " ");
/*     */ 
/* 156 */     while (localStringTokenizer.hasMoreTokens()) {
/* 157 */       arrayOfString1[(i++)] = localStringTokenizer.nextToken();
/*     */     }
/* 159 */     String[] arrayOfString2 = new String[i];
/* 160 */     System.arraycopy(arrayOfString1, 0, arrayOfString2, 0, i);
/* 161 */     return arrayOfString2;
/*     */   }
/*     */ 
/*     */   public static boolean hasQueryComponents(String paramString)
/*     */   {
/* 168 */     return paramString.lastIndexOf('?') != -1;
/*     */   }
/*     */ 
/*     */   static String toUrlString(String paramString1, int paramInt, String paramString2, boolean paramBoolean)
/*     */   {
/*     */     try
/*     */     {
/* 180 */       String str1 = paramString1 != null ? paramString1 : "";
/* 181 */       if ((str1.indexOf(':') != -1) && (str1.charAt(0) != '[')) {
/* 182 */         str1 = "[" + str1 + "]";
/*     */       }
/* 184 */       String str2 = paramInt != -1 ? ":" + paramInt : "";
/* 185 */       String str3 = paramString2 != null ? "/" + UrlUtil.encode(paramString2, "UTF8") : "";
/*     */ 
/* 187 */       return "ldap://" + str1 + str2 + str3;
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */     }
/* 190 */     throw new IllegalStateException("UTF-8 encoding unavailable");
/*     */   }
/*     */ 
/*     */   private void parsePathAndQuery()
/*     */     throws MalformedURLException, UnsupportedEncodingException
/*     */   {
/* 203 */     if (this.path.equals("")) {
/* 204 */       return;
/*     */     }
/*     */ 
/* 207 */     this.DN = (this.path.startsWith("/") ? this.path.substring(1) : this.path);
/* 208 */     if (this.DN.length() > 0) {
/* 209 */       this.DN = UrlUtil.decode(this.DN, "UTF8");
/*     */     }
/*     */ 
/* 214 */     if (this.query == null) {
/* 215 */       return;
/*     */     }
/*     */ 
/* 218 */     int i = this.query.indexOf('?', 1);
/*     */ 
/* 220 */     if (i < 0) {
/* 221 */       this.attributes = this.query.substring(1);
/* 222 */       return;
/* 223 */     }if (i != 1) {
/* 224 */       this.attributes = this.query.substring(1, i);
/*     */     }
/*     */ 
/* 227 */     int j = this.query.indexOf('?', i + 1);
/*     */ 
/* 229 */     if (j < 0) {
/* 230 */       this.scope = this.query.substring(i + 1);
/* 231 */       return;
/* 232 */     }if (j != i + 1) {
/* 233 */       this.scope = this.query.substring(i + 1, j);
/*     */     }
/*     */ 
/* 236 */     int k = this.query.indexOf('?', j + 1);
/*     */ 
/* 238 */     if (k < 0) {
/* 239 */       this.filter = this.query.substring(j + 1);
/*     */     } else {
/* 241 */       if (k != j + 1) {
/* 242 */         this.filter = this.query.substring(j + 1, k);
/*     */       }
/* 244 */       this.extensions = this.query.substring(k + 1);
/* 245 */       if (this.extensions.length() > 0) {
/* 246 */         this.extensions = UrlUtil.decode(this.extensions, "UTF8");
/*     */       }
/*     */     }
/* 249 */     if ((this.filter != null) && (this.filter.length() > 0))
/* 250 */       this.filter = UrlUtil.decode(this.filter, "UTF8");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapURL
 * JD-Core Version:    0.6.2
 */