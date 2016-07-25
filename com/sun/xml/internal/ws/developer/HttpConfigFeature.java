/*     */ package com.sun.xml.internal.ws.developer;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.net.CookieHandler;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ 
/*     */ public final class HttpConfigFeature extends WebServiceFeature
/*     */ {
/*     */   public static final String ID = "http://jax-ws.java.net/features/http-config";
/*  81 */   private static final Constructor cookieManagerConstructor = tempConstructor;
/*  82 */   private static final Object cookiePolicy = tempPolicy;
/*     */   private final CookieHandler cookieJar;
/*     */ 
/*     */   public HttpConfigFeature()
/*     */   {
/*  88 */     this(getInternalCookieHandler());
/*     */   }
/*     */ 
/*     */   public HttpConfigFeature(CookieHandler cookieJar) {
/*  92 */     this.enabled = true;
/*  93 */     this.cookieJar = cookieJar;
/*     */   }
/*     */ 
/*     */   private static CookieHandler getInternalCookieHandler() {
/*     */     try {
/*  98 */       return (CookieHandler)cookieManagerConstructor.newInstance(new Object[] { null, cookiePolicy });
/*     */     } catch (Exception e) {
/* 100 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getID() {
/* 105 */     return "http://jax-ws.java.net/features/http-config";
/*     */   }
/*     */ 
/*     */   public CookieHandler getCookieHandler() {
/* 109 */     return this.cookieJar;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     Constructor tempConstructor;
/*     */     Object tempPolicy;
/*     */     try
/*     */     {
/*  59 */       Class policyClass = Class.forName("java.net.CookiePolicy");
/*  60 */       Class storeClass = Class.forName("java.net.CookieStore");
/*  61 */       tempConstructor = Class.forName("java.net.CookieManager").getConstructor(new Class[] { storeClass, policyClass });
/*     */ 
/*  64 */       tempPolicy = policyClass.getField("ACCEPT_ALL").get(null);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */       try
/*     */       {
/*  71 */         Class policyClass = Class.forName("com.sun.xml.internal.ws.transport.http.client.CookiePolicy");
/*  72 */         Class storeClass = Class.forName("com.sun.xml.internal.ws.transport.http.client.CookieStore");
/*  73 */         tempConstructor = Class.forName("com.sun.xml.internal.ws.transport.http.client.CookieManager").getConstructor(new Class[] { storeClass, policyClass });
/*     */ 
/*  76 */         tempPolicy = policyClass.getField("ACCEPT_ALL").get(null);
/*     */       } catch (Exception ce) {
/*  78 */         throw new WebServiceException(ce);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.developer.HttpConfigFeature
 * JD-Core Version:    0.6.2
 */