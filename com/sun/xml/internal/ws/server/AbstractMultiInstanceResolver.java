/*    */ package com.sun.xml.internal.ws.server;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.server.ResourceInjector;
/*    */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*    */ import com.sun.xml.internal.ws.api.server.WSWebServiceContext;
/*    */ import java.lang.reflect.Method;
/*    */ import javax.annotation.PostConstruct;
/*    */ import javax.annotation.PreDestroy;
/*    */ 
/*    */ public abstract class AbstractMultiInstanceResolver<T> extends AbstractInstanceResolver<T>
/*    */ {
/*    */   protected final Class<T> clazz;
/*    */   private WSWebServiceContext webServiceContext;
/*    */   protected WSEndpoint owner;
/*    */   private final Method postConstructMethod;
/*    */   private final Method preDestroyMethod;
/*    */   private ResourceInjector resourceInjector;
/*    */ 
/*    */   public AbstractMultiInstanceResolver(Class<T> clazz)
/*    */   {
/* 54 */     this.clazz = clazz;
/*    */ 
/* 56 */     this.postConstructMethod = findAnnotatedMethod(clazz, PostConstruct.class);
/* 57 */     this.preDestroyMethod = findAnnotatedMethod(clazz, PreDestroy.class);
/*    */   }
/*    */ 
/*    */   protected final void prepare(T t)
/*    */   {
/* 65 */     assert (this.webServiceContext != null);
/*    */ 
/* 67 */     this.resourceInjector.inject(this.webServiceContext, t);
/* 68 */     invokeMethod(this.postConstructMethod, t, new Object[0]);
/*    */   }
/*    */ 
/*    */   protected final T create()
/*    */   {
/* 75 */     Object t = createNewInstance(this.clazz);
/* 76 */     prepare(t);
/* 77 */     return t;
/*    */   }
/*    */ 
/*    */   public void start(WSWebServiceContext wsc, WSEndpoint endpoint)
/*    */   {
/* 82 */     this.resourceInjector = getResourceInjector(endpoint);
/* 83 */     this.webServiceContext = wsc;
/* 84 */     this.owner = endpoint;
/*    */   }
/*    */ 
/*    */   protected final void dispose(T instance) {
/* 88 */     invokeMethod(this.preDestroyMethod, instance, new Object[0]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.AbstractMultiInstanceResolver
 * JD-Core Version:    0.6.2
 */