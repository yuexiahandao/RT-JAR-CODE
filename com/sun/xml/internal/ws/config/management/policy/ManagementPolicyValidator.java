/*    */ package com.sun.xml.internal.ws.config.management.policy;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.config.management.policy.ManagedClientAssertion;
/*    */ import com.sun.xml.internal.ws.api.config.management.policy.ManagedServiceAssertion;
/*    */ import com.sun.xml.internal.ws.policy.PolicyAssertion;
/*    */ import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator;
/*    */ import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator.Fitness;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class ManagementPolicyValidator
/*    */   implements PolicyAssertionValidator
/*    */ {
/*    */   public PolicyAssertionValidator.Fitness validateClientSide(PolicyAssertion assertion)
/*    */   {
/* 45 */     QName assertionName = assertion.getName();
/* 46 */     if (ManagedClientAssertion.MANAGED_CLIENT_QNAME.equals(assertionName)) {
/* 47 */       return PolicyAssertionValidator.Fitness.SUPPORTED;
/*    */     }
/* 49 */     if (ManagedServiceAssertion.MANAGED_SERVICE_QNAME.equals(assertionName)) {
/* 50 */       return PolicyAssertionValidator.Fitness.UNSUPPORTED;
/*    */     }
/*    */ 
/* 53 */     return PolicyAssertionValidator.Fitness.UNKNOWN;
/*    */   }
/*    */ 
/*    */   public PolicyAssertionValidator.Fitness validateServerSide(PolicyAssertion assertion)
/*    */   {
/* 58 */     QName assertionName = assertion.getName();
/* 59 */     if (ManagedServiceAssertion.MANAGED_SERVICE_QNAME.equals(assertionName)) {
/* 60 */       return PolicyAssertionValidator.Fitness.SUPPORTED;
/*    */     }
/* 62 */     if (ManagedClientAssertion.MANAGED_CLIENT_QNAME.equals(assertionName)) {
/* 63 */       return PolicyAssertionValidator.Fitness.UNSUPPORTED;
/*    */     }
/*    */ 
/* 66 */     return PolicyAssertionValidator.Fitness.UNKNOWN;
/*    */   }
/*    */ 
/*    */   public String[] declareSupportedDomains()
/*    */   {
/* 71 */     return new String[] { "http://java.sun.com/xml/ns/metro/management" };
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.config.management.policy.ManagementPolicyValidator
 * JD-Core Version:    0.6.2
 */