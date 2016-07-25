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
/*    */ class NormalizedModelGenerator extends PolicyModelGenerator
/*    */ {
/* 43 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(NormalizedModelGenerator.class);
/*    */   private final PolicyModelGenerator.PolicySourceModelCreator sourceModelCreator;
/*    */ 
/*    */   NormalizedModelGenerator(PolicyModelGenerator.PolicySourceModelCreator sourceModelCreator)
/*    */   {
/* 49 */     this.sourceModelCreator = sourceModelCreator;
/*    */   }
/*    */ 
/*    */   public PolicySourceModel translate(Policy policy) throws PolicyException
/*    */   {
/* 54 */     LOGGER.entering(new Object[] { policy });
/*    */ 
/* 56 */     PolicySourceModel model = null;
/*    */     ModelNode exactlyOneNode;
/* 58 */     if (policy == null) {
/* 59 */       LOGGER.fine(LocalizationMessages.WSP_0047_POLICY_IS_NULL_RETURNING());
/*    */     } else {
/* 61 */       model = this.sourceModelCreator.create(policy);
/* 62 */       ModelNode rootNode = model.getRootNode();
/* 63 */       exactlyOneNode = rootNode.createChildExactlyOneNode();
/* 64 */       for (AssertionSet set : policy) {
/* 65 */         alternativeNode = exactlyOneNode.createChildAllNode();
/* 66 */         for (PolicyAssertion assertion : set) {
/* 67 */           AssertionData data = AssertionData.createAssertionData(assertion.getName(), assertion.getValue(), assertion.getAttributes(), assertion.isOptional(), assertion.isIgnorable());
/* 68 */           ModelNode assertionNode = alternativeNode.createChildAssertionNode(data);
/* 69 */           if (assertion.hasNestedPolicy()) {
/* 70 */             translate(assertionNode, assertion.getNestedPolicy());
/*    */           }
/* 72 */           if (assertion.hasParameters())
/* 73 */             translate(assertionNode, assertion.getParametersIterator());
/*    */         }
/*    */       }
/*    */     }
/*    */     ModelNode alternativeNode;
/* 79 */     LOGGER.exiting(model);
/* 80 */     return model;
/*    */   }
/*    */ 
/*    */   protected ModelNode translate(ModelNode parentAssertion, NestedPolicy policy)
/*    */   {
/* 85 */     ModelNode nestedPolicyRoot = parentAssertion.createChildPolicyNode();
/* 86 */     ModelNode exactlyOneNode = nestedPolicyRoot.createChildExactlyOneNode();
/* 87 */     AssertionSet set = policy.getAssertionSet();
/* 88 */     ModelNode alternativeNode = exactlyOneNode.createChildAllNode();
/* 89 */     translate(alternativeNode, set);
/* 90 */     return nestedPolicyRoot;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.sourcemodel.NormalizedModelGenerator
 * JD-Core Version:    0.6.2
 */