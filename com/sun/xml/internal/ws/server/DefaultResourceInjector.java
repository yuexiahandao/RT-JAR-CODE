/*    */ package com.sun.xml.internal.ws.server;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.server.ResourceInjector;
/*    */ import com.sun.xml.internal.ws.api.server.WSWebServiceContext;
/*    */ import javax.xml.ws.WebServiceContext;
/*    */ 
/*    */ public final class DefaultResourceInjector extends ResourceInjector
/*    */ {
/*    */   public void inject(@NotNull WSWebServiceContext context, @NotNull Object instance)
/*    */   {
/* 42 */     AbstractInstanceResolver.buildInjectionPlan(instance.getClass(), WebServiceContext.class, false).inject(instance, context);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.DefaultResourceInjector
 * JD-Core Version:    0.6.2
 */