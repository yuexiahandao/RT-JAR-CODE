/*    */ package com.sun.xml.internal.ws.util;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import javax.xml.ws.handler.Handler;
/*    */ 
/*    */ public class HandlerAnnotationInfo
/*    */ {
/*    */   private List<Handler> handlers;
/*    */   private Set<String> roles;
/*    */ 
/*    */   public List<Handler> getHandlers()
/*    */   {
/* 54 */     return this.handlers;
/*    */   }
/*    */ 
/*    */   public void setHandlers(List<Handler> handlers)
/*    */   {
/* 63 */     this.handlers = handlers;
/*    */   }
/*    */ 
/*    */   public Set<String> getRoles()
/*    */   {
/* 72 */     return this.roles;
/*    */   }
/*    */ 
/*    */   public void setRoles(Set<String> roles)
/*    */   {
/* 81 */     this.roles = roles;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.HandlerAnnotationInfo
 * JD-Core Version:    0.6.2
 */