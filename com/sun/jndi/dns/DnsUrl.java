/*     */ package com.sun.jndi.dns;
/*     */ 
/*     */ import com.sun.jndi.toolkit.url.Uri;
/*     */ import com.sun.jndi.toolkit.url.UrlUtil;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class DnsUrl extends Uri
/*     */ {
/*     */   private String domain;
/*     */ 
/*     */   public static DnsUrl[] fromList(String paramString)
/*     */     throws MalformedURLException
/*     */   {
/*  69 */     DnsUrl[] arrayOfDnsUrl1 = new DnsUrl[(paramString.length() + 1) / 2];
/*  70 */     int i = 0;
/*  71 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, " ");
/*     */ 
/*  73 */     while (localStringTokenizer.hasMoreTokens()) {
/*  74 */       arrayOfDnsUrl1[(i++)] = new DnsUrl(localStringTokenizer.nextToken());
/*     */     }
/*  76 */     DnsUrl[] arrayOfDnsUrl2 = new DnsUrl[i];
/*  77 */     System.arraycopy(arrayOfDnsUrl1, 0, arrayOfDnsUrl2, 0, i);
/*  78 */     return arrayOfDnsUrl2;
/*     */   }
/*     */ 
/*     */   public DnsUrl(String paramString) throws MalformedURLException {
/*  82 */     super(paramString);
/*     */ 
/*  84 */     if (!this.scheme.equals("dns")) {
/*  85 */       throw new MalformedURLException(paramString + " is not a valid DNS pseudo-URL");
/*     */     }
/*     */ 
/*  89 */     this.domain = (this.path.startsWith("/") ? this.path.substring(1) : this.path);
/*     */ 
/*  92 */     this.domain = (this.domain.equals("") ? "." : UrlUtil.decode(this.domain));
/*     */   }
/*     */ 
/*     */   public String getDomain()
/*     */   {
/* 106 */     return this.domain;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.dns.DnsUrl
 * JD-Core Version:    0.6.2
 */