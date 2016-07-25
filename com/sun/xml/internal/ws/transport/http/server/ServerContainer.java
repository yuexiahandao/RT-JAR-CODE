/*    */ package com.sun.xml.internal.ws.transport.http.server;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.server.BoundEndpoint;
/*    */ import com.sun.xml.internal.ws.api.server.Container;
/*    */ import com.sun.xml.internal.ws.api.server.Module;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ class ServerContainer extends Container
/*    */ {
/* 42 */   private final Module module = new Module() {
/* 43 */     private final List<BoundEndpoint> endpoints = new ArrayList();
/*    */ 
/* 46 */     @NotNull
/*    */     public List<BoundEndpoint> getBoundEndpoints() { return this.endpoints; }
/*    */ 
/* 42 */   };
/*    */ 
/*    */   public <T> T getSPI(Class<T> spiType)
/*    */   {
/* 51 */     if (spiType == Module.class) {
/* 52 */       return spiType.cast(this.module);
/*    */     }
/* 54 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.http.server.ServerContainer
 * JD-Core Version:    0.6.2
 */