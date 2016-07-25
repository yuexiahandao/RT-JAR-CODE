/*     */ package com.sun.xml.internal.ws.api.client;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.WSFeatureList;
/*     */ import com.sun.xml.internal.ws.developer.WSBindingProvider;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ 
/*     */ public abstract class ServiceInterceptor
/*     */ {
/*     */   public List<WebServiceFeature> preCreateBinding(@NotNull WSPortInfo port, @Nullable Class<?> serviceEndpointInterface, @NotNull WSFeatureList defaultFeatures)
/*     */   {
/*  78 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   public void postCreateProxy(@NotNull WSBindingProvider bp, @NotNull Class<?> serviceEndpointInterface)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void postCreateDispatch(@NotNull WSBindingProvider bp)
/*     */   {
/*     */   }
/*     */ 
/*     */   public static ServiceInterceptor aggregate(ServiceInterceptor[] interceptors)
/*     */   {
/* 106 */     if (interceptors.length == 1)
/* 107 */       return interceptors[0];
/* 108 */     return new ServiceInterceptor() {
/*     */       public List<WebServiceFeature> preCreateBinding(@NotNull WSPortInfo port, @Nullable Class<?> portInterface, @NotNull WSFeatureList defaultFeatures) {
/* 110 */         List r = new ArrayList();
/* 111 */         for (ServiceInterceptor si : this.val$interceptors)
/* 112 */           r.addAll(si.preCreateBinding(port, portInterface, defaultFeatures));
/* 113 */         return r;
/*     */       }
/*     */ 
/*     */       public void postCreateProxy(@NotNull WSBindingProvider bp, @NotNull Class<?> serviceEndpointInterface) {
/* 117 */         for (ServiceInterceptor si : this.val$interceptors)
/* 118 */           si.postCreateProxy(bp, serviceEndpointInterface);
/*     */       }
/*     */ 
/*     */       public void postCreateDispatch(@NotNull WSBindingProvider bp) {
/* 122 */         for (ServiceInterceptor si : this.val$interceptors)
/* 123 */           si.postCreateDispatch(bp);
/*     */       }
/*     */     };
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.client.ServiceInterceptor
 * JD-Core Version:    0.6.2
 */