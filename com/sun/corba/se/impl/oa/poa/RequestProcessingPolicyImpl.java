/*    */ package com.sun.corba.se.impl.oa.poa;
/*    */ 
/*    */ import org.omg.CORBA.LocalObject;
/*    */ import org.omg.CORBA.Policy;
/*    */ import org.omg.PortableServer.RequestProcessingPolicy;
/*    */ import org.omg.PortableServer.RequestProcessingPolicyValue;
/*    */ 
/*    */ public class RequestProcessingPolicyImpl extends LocalObject
/*    */   implements RequestProcessingPolicy
/*    */ {
/*    */   private RequestProcessingPolicyValue value;
/*    */ 
/*    */   public RequestProcessingPolicyImpl(RequestProcessingPolicyValue paramRequestProcessingPolicyValue)
/*    */   {
/* 36 */     this.value = paramRequestProcessingPolicyValue;
/*    */   }
/*    */ 
/*    */   public RequestProcessingPolicyValue value() {
/* 40 */     return this.value;
/*    */   }
/*    */ 
/*    */   public int policy_type()
/*    */   {
/* 45 */     return 22;
/*    */   }
/*    */ 
/*    */   public Policy copy() {
/* 49 */     return new RequestProcessingPolicyImpl(this.value);
/*    */   }
/*    */ 
/*    */   public void destroy() {
/* 53 */     this.value = null;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 60 */     String str = null;
/* 61 */     switch (this.value.value()) {
/*    */     case 0:
/* 63 */       str = "USE_ACTIVE_OBJECT_MAP_ONLY";
/* 64 */       break;
/*    */     case 1:
/* 66 */       str = "USE_DEFAULT_SERVANT";
/* 67 */       break;
/*    */     case 2:
/* 69 */       str = "USE_SERVANT_MANAGER";
/*    */     }
/*    */ 
/* 73 */     return "RequestProcessingPolicy[" + str + "]";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.RequestProcessingPolicyImpl
 * JD-Core Version:    0.6.2
 */