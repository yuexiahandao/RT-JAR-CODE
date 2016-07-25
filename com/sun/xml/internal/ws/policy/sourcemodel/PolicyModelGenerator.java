/*     */ package com.sun.xml.internal.ws.policy.sourcemodel;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.AssertionSet;
/*     */ import com.sun.xml.internal.ws.policy.NestedPolicy;
/*     */ import com.sun.xml.internal.ws.policy.Policy;
/*     */ import com.sun.xml.internal.ws.policy.PolicyAssertion;
/*     */ import com.sun.xml.internal.ws.policy.PolicyException;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public abstract class PolicyModelGenerator
/*     */ {
/*  49 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(PolicyModelGenerator.class);
/*     */ 
/*     */   public static PolicyModelGenerator getGenerator()
/*     */   {
/*  64 */     return getNormalizedGenerator(new PolicySourceModelCreator());
/*     */   }
/*     */ 
/*     */   protected static PolicyModelGenerator getCompactGenerator(PolicySourceModelCreator creator)
/*     */   {
/*  75 */     return new CompactModelGenerator(creator);
/*     */   }
/*     */ 
/*     */   protected static PolicyModelGenerator getNormalizedGenerator(PolicySourceModelCreator creator)
/*     */   {
/*  86 */     return new NormalizedModelGenerator(creator);
/*     */   }
/*     */ 
/*     */   public abstract PolicySourceModel translate(Policy paramPolicy)
/*     */     throws PolicyException;
/*     */ 
/*     */   protected abstract ModelNode translate(ModelNode paramModelNode, NestedPolicy paramNestedPolicy);
/*     */ 
/*     */   protected void translate(ModelNode node, AssertionSet assertions)
/*     */   {
/* 119 */     for (PolicyAssertion assertion : assertions) {
/* 120 */       AssertionData data = AssertionData.createAssertionData(assertion.getName(), assertion.getValue(), assertion.getAttributes(), assertion.isOptional(), assertion.isIgnorable());
/* 121 */       ModelNode assertionNode = node.createChildAssertionNode(data);
/* 122 */       if (assertion.hasNestedPolicy()) {
/* 123 */         translate(assertionNode, assertion.getNestedPolicy());
/*     */       }
/* 125 */       if (assertion.hasParameters())
/* 126 */         translate(assertionNode, assertion.getParametersIterator());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void translate(ModelNode assertionNode, Iterator<PolicyAssertion> assertionParametersIterator)
/*     */   {
/* 138 */     while (assertionParametersIterator.hasNext()) {
/* 139 */       PolicyAssertion assertionParameter = (PolicyAssertion)assertionParametersIterator.next();
/* 140 */       AssertionData data = AssertionData.createAssertionParameterData(assertionParameter.getName(), assertionParameter.getValue(), assertionParameter.getAttributes());
/* 141 */       ModelNode assertionParameterNode = assertionNode.createChildAssertionParameterNode(data);
/* 142 */       if (assertionParameter.hasNestedPolicy()) {
/* 143 */         throw ((IllegalStateException)LOGGER.logSevereException(new IllegalStateException(LocalizationMessages.WSP_0005_UNEXPECTED_POLICY_ELEMENT_FOUND_IN_ASSERTION_PARAM(assertionParameter))));
/*     */       }
/* 145 */       if (assertionParameter.hasNestedAssertions())
/* 146 */         translate(assertionParameterNode, assertionParameter.getNestedAssertionsIterator());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class PolicySourceModelCreator
/*     */   {
/*     */     protected PolicySourceModel create(Policy policy)
/*     */     {
/* 165 */       return PolicySourceModel.createPolicySourceModel(policy.getNamespaceVersion(), policy.getId(), policy.getName());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelGenerator
 * JD-Core Version:    0.6.2
 */