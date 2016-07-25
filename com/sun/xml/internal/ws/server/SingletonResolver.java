/*    */ package com.sun.xml.internal.ws.server;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.server.ResourceInjector;
/*    */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*    */ import com.sun.xml.internal.ws.api.server.WSWebServiceContext;
/*    */ import javax.annotation.PostConstruct;
/*    */ import javax.annotation.PreDestroy;
/*    */ 
/*    */ public final class SingletonResolver<T> extends AbstractInstanceResolver<T>
/*    */ {
/*    */ 
/*    */   @NotNull
/*    */   private final T singleton;
/*    */ 
/*    */   public SingletonResolver(@NotNull T singleton)
/*    */   {
/* 46 */     this.singleton = singleton;
/*    */   }
/*    */   @NotNull
/*    */   public T resolve(Packet request) {
/* 50 */     return this.singleton;
/*    */   }
/*    */ 
/*    */   public void start(WSWebServiceContext wsc, WSEndpoint endpoint) {
/* 54 */     getResourceInjector(endpoint).inject(wsc, this.singleton);
/*    */ 
/* 56 */     invokeMethod(findAnnotatedMethod(this.singleton.getClass(), PostConstruct.class), this.singleton, new Object[0]);
/*    */   }
/*    */ 
/*    */   public void dispose() {
/* 60 */     invokeMethod(findAnnotatedMethod(this.singleton.getClass(), PreDestroy.class), this.singleton, new Object[0]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.SingletonResolver
 * JD-Core Version:    0.6.2
 */