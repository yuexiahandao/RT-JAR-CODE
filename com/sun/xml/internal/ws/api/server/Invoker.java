/*     */ package com.sun.xml.internal.ws.api.server;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.xml.ws.Provider;
/*     */ import javax.xml.ws.WebServiceContext;
/*     */ 
/*     */ public abstract class Invoker
/*     */ {
/*     */   private static final Method invokeMethod;
/*     */   private static final Method asyncInvokeMethod;
/*     */ 
/*     */   public void start(@NotNull WSWebServiceContext wsc, @NotNull WSEndpoint endpoint)
/*     */   {
/*  64 */     start(wsc);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void start(@NotNull WebServiceContext wsc)
/*     */   {
/*  72 */     throw new IllegalStateException("deprecated version called");
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/*     */   }
/*     */ 
/*     */   public abstract Object invoke(@NotNull Packet paramPacket, @NotNull Method paramMethod, @NotNull Object[] paramArrayOfObject)
/*     */     throws InvocationTargetException, IllegalAccessException;
/*     */ 
/*     */   public <T> T invokeProvider(@NotNull Packet p, T arg)
/*     */     throws IllegalAccessException, InvocationTargetException
/*     */   {
/*  96 */     return invoke(p, invokeMethod, new Object[] { arg });
/*     */   }
/*     */ 
/*     */   public <T> void invokeAsyncProvider(@NotNull Packet p, T arg, AsyncProviderCallback cbak, WebServiceContext ctxt)
/*     */     throws IllegalAccessException, InvocationTargetException
/*     */   {
/* 104 */     invoke(p, asyncInvokeMethod, new Object[] { arg, cbak, ctxt });
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 111 */       invokeMethod = Provider.class.getMethod("invoke", new Class[] { Object.class });
/*     */     } catch (NoSuchMethodException e) {
/* 113 */       throw new AssertionError(e);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 121 */       asyncInvokeMethod = AsyncProvider.class.getMethod("invoke", new Class[] { Object.class, AsyncProviderCallback.class, WebServiceContext.class });
/*     */     } catch (NoSuchMethodException e) {
/* 123 */       throw new AssertionError(e);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.server.Invoker
 * JD-Core Version:    0.6.2
 */