/*     */ package com.sun.jndi.cosnaming;
/*     */ 
/*     */ import com.sun.jndi.toolkit.url.UrlUtil;
/*     */ import java.net.MalformedURLException;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ public final class CorbanameUrl
/*     */ {
/*     */   private String stringName;
/*     */   private String location;
/*     */ 
/*     */   public String getStringName()
/*     */   {
/*  77 */     return this.stringName;
/*     */   }
/*     */ 
/*     */   public Name getCosName() throws NamingException {
/*  81 */     return CNCtx.parser.parse(this.stringName);
/*     */   }
/*     */ 
/*     */   public String getLocation() {
/*  85 */     return "corbaloc:" + this.location;
/*     */   }
/*     */ 
/*     */   public CorbanameUrl(String paramString) throws MalformedURLException
/*     */   {
/*  90 */     if (!paramString.startsWith("corbaname:")) {
/*  91 */       throw new MalformedURLException("Invalid corbaname URL: " + paramString);
/*     */     }
/*     */ 
/*  94 */     int i = 10;
/*     */ 
/*  96 */     int j = paramString.indexOf('#', i);
/*  97 */     if (j < 0) {
/*  98 */       j = paramString.length();
/*  99 */       this.stringName = "";
/*     */     } else {
/* 101 */       this.stringName = UrlUtil.decode(paramString.substring(j + 1));
/*     */     }
/* 103 */     this.location = paramString.substring(i, j);
/*     */ 
/* 105 */     int k = this.location.indexOf("/");
/* 106 */     if (k >= 0)
/*     */     {
/* 108 */       if (k == this.location.length() - 1)
/* 109 */         this.location += "NameService";
/*     */     }
/*     */     else
/* 112 */       this.location += "/NameService";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.cosnaming.CorbanameUrl
 * JD-Core Version:    0.6.2
 */