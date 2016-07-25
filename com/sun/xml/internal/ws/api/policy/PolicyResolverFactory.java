/*    */ package com.sun.xml.internal.ws.api.policy;
/*    */ 
/*    */ import com.sun.xml.internal.ws.policy.jaxws.DefaultPolicyResolver;
/*    */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*    */ 
/*    */ public abstract class PolicyResolverFactory
/*    */ {
/* 61 */   public static final PolicyResolver DEFAULT_POLICY_RESOLVER = new DefaultPolicyResolver();
/*    */ 
/*    */   public abstract PolicyResolver doCreate();
/*    */ 
/*    */   public static PolicyResolver create()
/*    */   {
/* 46 */     for (PolicyResolverFactory factory : ServiceFinder.find(PolicyResolverFactory.class)) {
/* 47 */       PolicyResolver policyResolver = factory.doCreate();
/* 48 */       if (policyResolver != null) {
/* 49 */         return policyResolver;
/*    */       }
/*    */     }
/*    */ 
/* 53 */     return DEFAULT_POLICY_RESOLVER;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.policy.PolicyResolverFactory
 * JD-Core Version:    0.6.2
 */