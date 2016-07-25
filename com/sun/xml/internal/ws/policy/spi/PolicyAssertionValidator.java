/*    */ package com.sun.xml.internal.ws.policy.spi;
/*    */ 
/*    */ import com.sun.xml.internal.ws.policy.PolicyAssertion;
/*    */ 
/*    */ public abstract interface PolicyAssertionValidator
/*    */ {
/*    */   public abstract Fitness validateClientSide(PolicyAssertion paramPolicyAssertion);
/*    */ 
/*    */   public abstract Fitness validateServerSide(PolicyAssertion paramPolicyAssertion);
/*    */ 
/*    */   public abstract String[] declareSupportedDomains();
/*    */ 
/*    */   public static enum Fitness
/*    */   {
/* 38 */     UNKNOWN, 
/* 39 */     INVALID, 
/* 40 */     UNSUPPORTED, 
/* 41 */     SUPPORTED;
/*    */ 
/*    */     public Fitness combine(Fitness other) {
/* 44 */       if (compareTo(other) < 0) {
/* 45 */         return other;
/*    */       }
/* 47 */       return this;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator
 * JD-Core Version:    0.6.2
 */