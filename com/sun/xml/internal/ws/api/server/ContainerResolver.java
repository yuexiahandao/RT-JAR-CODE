/*    */ package com.sun.xml.internal.ws.api.server;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ 
/*    */ public abstract class ContainerResolver
/*    */ {
/* 49 */   private static final ContainerResolver NONE = new ContainerResolver() {
/*    */     public Container getContainer() {
/* 51 */       return Container.NONE;
/*    */     }
/* 49 */   };
/*    */ 
/* 55 */   private static volatile ContainerResolver theResolver = NONE;
/*    */ 
/*    */   public static void setInstance(ContainerResolver resolver)
/*    */   {
/* 64 */     if (resolver == null)
/* 65 */       resolver = NONE;
/* 66 */     theResolver = resolver;
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public static ContainerResolver getInstance()
/*    */   {
/* 75 */     return theResolver;
/*    */   }
/*    */ 
/*    */   public static ContainerResolver getDefault()
/*    */   {
/* 84 */     return NONE;
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public abstract Container getContainer();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.server.ContainerResolver
 * JD-Core Version:    0.6.2
 */