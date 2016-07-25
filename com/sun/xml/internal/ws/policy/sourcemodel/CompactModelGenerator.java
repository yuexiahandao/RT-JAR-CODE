/*    */ package com.sun.xml.internal.ws.policy.sourcemodel;
/*    */ 
/*    */ import com.sun.xml.internal.ws.policy.AssertionSet;
/*    */ import com.sun.xml.internal.ws.policy.NestedPolicy;
/*    */ import com.sun.xml.internal.ws.policy.Policy;
/*    */ import com.sun.xml.internal.ws.policy.PolicyAssertion;
/*    */ import com.sun.xml.internal.ws.policy.PolicyException;
/*    */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*    */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*    */ 
/*    */ class CompactModelGenerator extends PolicyModelGenerator
/*    */ {
/* 44 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(CompactModelGenerator.class);
/*    */   private final PolicyModelGenerator.PolicySourceModelCreator sourceModelCreator;
/*    */ 
/*    */   CompactModelGenerator(PolicyModelGenerator.PolicySourceModelCreator sourceModelCreator)
/*    */   {
/* 50 */     this.sourceModelCreator = sourceModelCreator;
/*    */   }
/*    */ 
/*    */   public PolicySourceModel translate(Policy policy) throws PolicyException
/*    */   {
/* 55 */     LOGGER.entering(new Object[] { policy });
/*    */ 
/* 57 */     PolicySourceModel model = null;
/*    */     ModelNode rootNode;
/*    */     int numberOfAssertionSets;
/*    */     ModelNode alternativeNode;
/* 59 */     if (policy == null) {
/* 60 */       LOGGER.fine(LocalizationMessages.WSP_0047_POLICY_IS_NULL_RETURNING());
/*    */     } else {
/* 62 */       model = this.sourceModelCreator.create(policy);
/* 63 */       rootNode = model.getRootNode();
/* 64 */       numberOfAssertionSets = policy.getNumberOfAssertionSets();
/* 65 */       if (numberOfAssertionSets > 1) {
/* 66 */         rootNode = rootNode.createChildExactlyOneNode();
/*    */       }
/* 68 */       alternativeNode = rootNode;
/* 69 */       for (AssertionSet set : policy) {
/* 70 */         if (numberOfAssertionSets > 1) {
/* 71 */           alternativeNode = rootNode.createChildAllNode();
/*    */         }
/* 73 */         for (PolicyAssertion assertion : set) {
/* 74 */           AssertionData data = AssertionData.createAssertionData(assertion.getName(), assertion.getValue(), assertion.getAttributes(), assertion.isOptional(), assertion.isIgnorable());
/* 75 */           ModelNode assertionNode = alternativeNode.createChildAssertionNode(data);
/* 76 */           if (assertion.hasNestedPolicy()) {
/* 77 */             translate(assertionNode, assertion.getNestedPolicy());
/*    */           }
/* 79 */           if (assertion.hasParameters()) {
/* 80 */             translate(assertionNode, assertion.getParametersIterator());
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */ 
/* 86 */     LOGGER.exiting(model);
/* 87 */     return model;
/*    */   }
/*    */ 
/*    */   protected ModelNode translate(ModelNode parentAssertion, NestedPolicy policy)
/*    */   {
/* 92 */     ModelNode nestedPolicyRoot = parentAssertion.createChildPolicyNode();
/* 93 */     AssertionSet set = policy.getAssertionSet();
/* 94 */     translate(nestedPolicyRoot, set);
/* 95 */     return nestedPolicyRoot;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.sourcemodel.CompactModelGenerator
 * JD-Core Version:    0.6.2
 */