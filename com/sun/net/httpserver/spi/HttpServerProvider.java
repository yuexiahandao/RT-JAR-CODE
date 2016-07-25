/*     */ package com.sun.net.httpserver.spi;
/*     */ 
/*     */ import com.sun.net.httpserver.HttpServer;
/*     */ import com.sun.net.httpserver.HttpsServer;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Iterator;
/*     */ import sun.misc.Service;
/*     */ import sun.misc.ServiceConfigurationError;
/*     */ import sun.net.httpserver.DefaultHttpServerProvider;
/*     */ 
/*     */ public abstract class HttpServerProvider
/*     */ {
/*  63 */   private static final Object lock = new Object();
/*  64 */   private static HttpServerProvider provider = null;
/*     */ 
/*     */   public abstract HttpServer createHttpServer(InetSocketAddress paramInetSocketAddress, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract HttpsServer createHttpsServer(InetSocketAddress paramInetSocketAddress, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   protected HttpServerProvider()
/*     */   {
/*  74 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  75 */     if (localSecurityManager != null)
/*  76 */       localSecurityManager.checkPermission(new RuntimePermission("httpServerProvider"));
/*     */   }
/*     */ 
/*     */   private static boolean loadProviderFromProperty() {
/*  80 */     String str = System.getProperty("com.sun.net.httpserver.HttpServerProvider");
/*  81 */     if (str == null)
/*  82 */       return false;
/*     */     try {
/*  84 */       Class localClass = Class.forName(str, true, ClassLoader.getSystemClassLoader());
/*     */ 
/*  86 */       provider = (HttpServerProvider)localClass.newInstance();
/*  87 */       return true;
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/*  89 */       throw new ServiceConfigurationError(localClassNotFoundException);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/*  91 */       throw new ServiceConfigurationError(localIllegalAccessException);
/*     */     } catch (InstantiationException localInstantiationException) {
/*  93 */       throw new ServiceConfigurationError(localInstantiationException);
/*     */     } catch (SecurityException localSecurityException) {
/*  95 */       throw new ServiceConfigurationError(localSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean loadProviderAsService() {
/* 100 */     Iterator localIterator = Service.providers(HttpServerProvider.class, ClassLoader.getSystemClassLoader());
/*     */     do
/*     */       try
/*     */       {
/* 104 */         if (!localIterator.hasNext())
/* 105 */           return false;
/* 106 */         provider = (HttpServerProvider)localIterator.next();
/* 107 */         return true; } catch (ServiceConfigurationError localServiceConfigurationError) {
/*     */       }
/* 109 */     while ((localServiceConfigurationError.getCause() instanceof SecurityException));
/*     */ 
/* 113 */     throw localServiceConfigurationError;
/*     */   }
/*     */ 
/*     */   public static HttpServerProvider provider()
/*     */   {
/* 154 */     synchronized (lock) {
/* 155 */       if (provider != null)
/* 156 */         return provider;
/* 157 */       return (HttpServerProvider)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Object run() {
/* 160 */           if (HttpServerProvider.access$000())
/* 161 */             return HttpServerProvider.provider;
/* 162 */           if (HttpServerProvider.access$200())
/* 163 */             return HttpServerProvider.provider;
/* 164 */           HttpServerProvider.access$102(new DefaultHttpServerProvider());
/* 165 */           return HttpServerProvider.provider;
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.httpserver.spi.HttpServerProvider
 * JD-Core Version:    0.6.2
 */