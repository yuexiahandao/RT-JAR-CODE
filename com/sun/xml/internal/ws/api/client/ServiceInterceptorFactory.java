/*     */ package com.sun.xml.internal.ws.api.client;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.WSService;
/*     */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class ServiceInterceptorFactory
/*     */ {
/*  79 */   private static ThreadLocal<Set<ServiceInterceptorFactory>> threadLocalFactories = new ThreadLocal() {
/*     */     protected Set<ServiceInterceptorFactory> initialValue() {
/*  81 */       return new HashSet();
/*     */     }
/*  79 */   };
/*     */ 
/*     */   public abstract ServiceInterceptor create(@NotNull WSService paramWSService);
/*     */ 
/*     */   @NotNull
/*     */   public static ServiceInterceptor load(@NotNull WSService service, @Nullable ClassLoader cl)
/*     */   {
/*  66 */     List l = new ArrayList();
/*     */ 
/*  69 */     for (ServiceInterceptorFactory f : ServiceFinder.find(ServiceInterceptorFactory.class)) {
/*  70 */       l.add(f.create(service));
/*     */     }
/*     */ 
/*  73 */     for (ServiceInterceptorFactory f : (Set)threadLocalFactories.get()) {
/*  74 */       l.add(f.create(service));
/*     */     }
/*  76 */     return ServiceInterceptor.aggregate((ServiceInterceptor[])l.toArray(new ServiceInterceptor[l.size()]));
/*     */   }
/*     */ 
/*     */   public static boolean registerForThread(ServiceInterceptorFactory factory)
/*     */   {
/*  93 */     return ((Set)threadLocalFactories.get()).add(factory);
/*     */   }
/*     */ 
/*     */   public static boolean unregisterForThread(ServiceInterceptorFactory factory)
/*     */   {
/* 100 */     return ((Set)threadLocalFactories.get()).remove(factory);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.client.ServiceInterceptorFactory
 * JD-Core Version:    0.6.2
 */