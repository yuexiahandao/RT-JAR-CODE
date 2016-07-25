/*    */ package com.sun.xml.internal.ws.api.server;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.server.DefaultResourceInjector;
/*    */ 
/*    */ public abstract class ResourceInjector
/*    */ {
/* 72 */   public static final ResourceInjector STANDALONE = new DefaultResourceInjector();
/*    */ 
/*    */   public abstract void inject(@NotNull WSWebServiceContext paramWSWebServiceContext, @NotNull Object paramObject);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.server.ResourceInjector
 * JD-Core Version:    0.6.2
 */