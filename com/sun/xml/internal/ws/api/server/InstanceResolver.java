/*     */ package com.sun.xml.internal.ws.api.server;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.resources.ServerMessages;
/*     */ import com.sun.xml.internal.ws.resources.WsservletMessages;
/*     */ import com.sun.xml.internal.ws.server.ServerRtException;
/*     */ import com.sun.xml.internal.ws.server.SingletonResolver;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.ws.Provider;
/*     */ import javax.xml.ws.WebServiceContext;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public abstract class InstanceResolver<T>
/*     */ {
/* 257 */   private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.server");
/*     */ 
/*     */   @NotNull
/*     */   public abstract T resolve(@NotNull Packet paramPacket);
/*     */ 
/*     */   public void postInvoke(@NotNull Packet request, @NotNull T servant)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void start(@NotNull WSWebServiceContext wsc, @NotNull WSEndpoint endpoint)
/*     */   {
/* 120 */     start(wsc);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void start(@NotNull WebServiceContext wsc)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/*     */   }
/*     */ 
/*     */   public static <T> InstanceResolver<T> createSingleton(T singleton)
/*     */   {
/* 146 */     assert (singleton != null);
/* 147 */     InstanceResolver ir = createFromInstanceResolverAnnotation(singleton.getClass());
/* 148 */     if (ir == null)
/* 149 */       ir = new SingletonResolver(singleton);
/* 150 */     return ir;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public static <T> InstanceResolver<T> createDefault(@NotNull Class<T> clazz, boolean bool)
/*     */   {
/* 160 */     return createDefault(clazz);
/*     */   }
/*     */ 
/*     */   public static <T> InstanceResolver<T> createDefault(@NotNull Class<T> clazz)
/*     */   {
/* 167 */     InstanceResolver ir = createFromInstanceResolverAnnotation(clazz);
/* 168 */     if (ir == null)
/* 169 */       ir = new SingletonResolver(createNewInstance(clazz));
/* 170 */     return ir;
/*     */   }
/*     */ 
/*     */   public static <T> InstanceResolver<T> createFromInstanceResolverAnnotation(@NotNull Class<T> clazz)
/*     */   {
/* 178 */     for (Annotation a : clazz.getAnnotations()) {
/* 179 */       InstanceResolverAnnotation ira = (InstanceResolverAnnotation)a.annotationType().getAnnotation(InstanceResolverAnnotation.class);
/* 180 */       if (ira != null) {
/* 181 */         Class ir = ira.value();
/*     */         try {
/* 183 */           return (InstanceResolver)ir.getConstructor(new Class[] { Class.class }).newInstance(new Object[] { clazz });
/*     */         } catch (InstantiationException e) {
/* 185 */           throw new WebServiceException(ServerMessages.FAILED_TO_INSTANTIATE_INSTANCE_RESOLVER(ir.getName(), a.annotationType(), clazz.getName()));
/*     */         }
/*     */         catch (IllegalAccessException e) {
/* 188 */           throw new WebServiceException(ServerMessages.FAILED_TO_INSTANTIATE_INSTANCE_RESOLVER(ir.getName(), a.annotationType(), clazz.getName()));
/*     */         }
/*     */         catch (InvocationTargetException e) {
/* 191 */           throw new WebServiceException(ServerMessages.FAILED_TO_INSTANTIATE_INSTANCE_RESOLVER(ir.getName(), a.annotationType(), clazz.getName()));
/*     */         }
/*     */         catch (NoSuchMethodException e) {
/* 194 */           throw new WebServiceException(ServerMessages.FAILED_TO_INSTANTIATE_INSTANCE_RESOLVER(ir.getName(), a.annotationType(), clazz.getName()));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 199 */     return null;
/*     */   }
/*     */ 
/*     */   protected static <T> T createNewInstance(Class<T> cl) {
/*     */     try {
/* 204 */       return cl.newInstance();
/*     */     } catch (InstantiationException e) {
/* 206 */       logger.log(Level.SEVERE, e.getMessage(), e);
/* 207 */       throw new ServerRtException(WsservletMessages.ERROR_IMPLEMENTOR_FACTORY_NEW_INSTANCE_FAILED(cl), new Object[0]);
/*     */     }
/*     */     catch (IllegalAccessException e) {
/* 210 */       logger.log(Level.SEVERE, e.getMessage(), e);
/* 211 */     }throw new ServerRtException(WsservletMessages.ERROR_IMPLEMENTOR_FACTORY_NEW_INSTANCE_FAILED(cl), new Object[0]);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Invoker createInvoker()
/*     */   {
/* 220 */     return new Invoker()
/*     */     {
/*     */       public void start(@NotNull WSWebServiceContext wsc, @NotNull WSEndpoint endpoint) {
/* 223 */         InstanceResolver.this.start(wsc, endpoint);
/*     */       }
/*     */ 
/*     */       public void dispose()
/*     */       {
/* 228 */         InstanceResolver.this.dispose();
/*     */       }
/*     */ 
/*     */       public Object invoke(Packet p, Method m, Object[] args) throws InvocationTargetException, IllegalAccessException
/*     */       {
/* 233 */         Object t = InstanceResolver.this.resolve(p);
/*     */         try {
/* 235 */           return MethodUtil.invoke(t, m, args);
/*     */         } finally {
/* 237 */           InstanceResolver.this.postInvoke(p, t);
/*     */         }
/*     */       }
/*     */ 
/*     */       public <U> U invokeProvider(@NotNull Packet p, U arg)
/*     */       {
/* 243 */         Object t = InstanceResolver.this.resolve(p);
/*     */         try {
/* 245 */           return ((Provider)t).invoke(arg);
/*     */         } finally {
/* 247 */           InstanceResolver.this.postInvoke(p, t);
/*     */         }
/*     */       }
/*     */ 
/*     */       public String toString() {
/* 252 */         return "Default Invoker over " + InstanceResolver.this.toString();
/*     */       }
/*     */     };
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.server.InstanceResolver
 * JD-Core Version:    0.6.2
 */