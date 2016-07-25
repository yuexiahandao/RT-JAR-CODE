/*    */ package com.sun.xml.internal.ws.developer;
/*    */ 
/*    */ import com.sun.org.glassfish.gmbal.ManagedAttribute;
/*    */ import com.sun.org.glassfish.gmbal.ManagedData;
/*    */ import javax.xml.ws.WebServiceFeature;
/*    */ 
/*    */ @ManagedData
/*    */ public final class BindingTypeFeature extends WebServiceFeature
/*    */ {
/*    */   public static final String ID = "http://jax-ws.dev.java.net/features/binding";
/*    */   private final String bindingId;
/*    */ 
/*    */   public BindingTypeFeature(String bindingId)
/*    */   {
/* 49 */     this.bindingId = bindingId;
/*    */   }
/*    */ 
/*    */   @ManagedAttribute
/*    */   public String getID() {
/* 54 */     return "http://jax-ws.dev.java.net/features/binding";
/*    */   }
/*    */ 
/*    */   @ManagedAttribute
/*    */   public String getBindingId() {
/* 59 */     return this.bindingId;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.developer.BindingTypeFeature
 * JD-Core Version:    0.6.2
 */