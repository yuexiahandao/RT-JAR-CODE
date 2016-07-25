/*    */ package com.sun.xml.internal.ws.policy.sourcemodel;
/*    */ 
/*    */ import com.sun.xml.internal.ws.policy.AssertionSet;
/*    */ import com.sun.xml.internal.ws.policy.PolicyAssertion;
/*    */ import com.sun.xml.internal.ws.policy.spi.AssertionCreationException;
/*    */ import com.sun.xml.internal.ws.policy.spi.PolicyAssertionCreator;
/*    */ import java.util.Collection;
/*    */ 
/*    */ class DefaultPolicyAssertionCreator
/*    */   implements PolicyAssertionCreator
/*    */ {
/*    */   public String[] getSupportedDomainNamespaceURIs()
/*    */   {
/* 61 */     return null;
/*    */   }
/*    */ 
/*    */   public PolicyAssertion createAssertion(AssertionData data, Collection<PolicyAssertion> assertionParameters, AssertionSet nestedAlternative, PolicyAssertionCreator defaultCreator)
/*    */     throws AssertionCreationException
/*    */   {
/* 68 */     return new DefaultPolicyAssertion(data, assertionParameters, nestedAlternative);
/*    */   }
/*    */ 
/*    */   private static final class DefaultPolicyAssertion extends PolicyAssertion
/*    */   {
/*    */     DefaultPolicyAssertion(AssertionData data, Collection<PolicyAssertion> assertionParameters, AssertionSet nestedAlternative)
/*    */     {
/* 46 */       super(assertionParameters, nestedAlternative);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.sourcemodel.DefaultPolicyAssertionCreator
 * JD-Core Version:    0.6.2
 */