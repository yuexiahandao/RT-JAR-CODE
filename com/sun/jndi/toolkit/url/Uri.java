/*     */ package com.sun.jndi.toolkit.url;
/*     */ 
/*     */ import java.net.MalformedURLException;
/*     */ 
/*     */ public class Uri
/*     */ {
/*     */   protected String uri;
/*     */   protected String scheme;
/* 110 */   protected String host = null;
/* 111 */   protected int port = -1;
/*     */   protected boolean hasAuthority;
/*     */   protected String path;
/* 114 */   protected String query = null;
/*     */ 
/*     */   public Uri(String paramString)
/*     */     throws MalformedURLException
/*     */   {
/* 121 */     init(paramString);
/*     */   }
/*     */ 
/*     */   protected Uri()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void init(String paramString)
/*     */     throws MalformedURLException
/*     */   {
/* 137 */     this.uri = paramString;
/* 138 */     parse(paramString);
/*     */   }
/*     */ 
/*     */   public String getScheme()
/*     */   {
/* 145 */     return this.scheme;
/*     */   }
/*     */ 
/*     */   public String getHost()
/*     */   {
/* 155 */     return this.host;
/*     */   }
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 163 */     return this.port;
/*     */   }
/*     */ 
/*     */   public String getPath()
/*     */   {
/* 173 */     return this.path;
/*     */   }
/*     */ 
/*     */   public String getQuery()
/*     */   {
/* 181 */     return this.query;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 188 */     return this.uri;
/*     */   }
/*     */ 
/*     */   private void parse(String paramString)
/*     */     throws MalformedURLException
/*     */   {
/* 197 */     int i = paramString.indexOf(':');
/* 198 */     if (i < 0) {
/* 199 */       throw new MalformedURLException("Invalid URI: " + paramString);
/*     */     }
/* 201 */     this.scheme = paramString.substring(0, i);
/* 202 */     i++;
/*     */ 
/* 204 */     this.hasAuthority = paramString.startsWith("//", i);
/* 205 */     if (this.hasAuthority) {
/* 206 */       i += 2;
/* 207 */       j = paramString.indexOf('/', i);
/* 208 */       if (j < 0)
/* 209 */         j = paramString.length();
/*     */       int k;
/* 211 */       if (paramString.startsWith("[", i)) {
/* 212 */         k = paramString.indexOf(']', i + 1);
/* 213 */         if ((k < 0) || (k > j)) {
/* 214 */           throw new MalformedURLException("Invalid URI: " + paramString);
/*     */         }
/* 216 */         this.host = paramString.substring(i, k + 1);
/* 217 */         i = k + 1;
/*     */       } else {
/* 219 */         k = paramString.indexOf(':', i);
/* 220 */         int m = (k < 0) || (k > j) ? j : k;
/*     */ 
/* 223 */         if (i < m) {
/* 224 */           this.host = paramString.substring(i, m);
/*     */         }
/* 226 */         i = m;
/*     */       }
/*     */ 
/* 229 */       if ((i + 1 < j) && (paramString.startsWith(":", i)))
/*     */       {
/* 231 */         i++;
/* 232 */         this.port = Integer.parseInt(paramString.substring(i, j));
/*     */       }
/* 234 */       i = j;
/*     */     }
/* 236 */     int j = paramString.indexOf('?', i);
/* 237 */     if (j < 0) {
/* 238 */       this.path = paramString.substring(i);
/*     */     } else {
/* 240 */       this.path = paramString.substring(i, j);
/* 241 */       this.query = paramString.substring(j);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.url.Uri
 * JD-Core Version:    0.6.2
 */