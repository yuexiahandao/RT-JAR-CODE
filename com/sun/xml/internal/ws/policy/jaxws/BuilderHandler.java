/*     */ package com.sun.xml.internal.ws.policy.jaxws;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.policy.ModelTranslator;
/*     */ import com.sun.xml.internal.ws.policy.Policy;
/*     */ import com.sun.xml.internal.ws.policy.PolicyException;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMapExtender;
/*     */ import com.sun.xml.internal.ws.policy.PolicySubject;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel;
/*     */ import com.sun.xml.internal.ws.resources.PolicyMessages;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ 
/*     */ abstract class BuilderHandler
/*     */ {
/*  47 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(BuilderHandler.class);
/*     */   Map<String, PolicySourceModel> policyStore;
/*     */   Collection<String> policyURIs;
/*     */   Object policySubject;
/*     */ 
/*     */   BuilderHandler(Collection<String> policyURIs, Map<String, PolicySourceModel> policyStore, Object policySubject)
/*     */   {
/*  57 */     this.policyStore = policyStore;
/*  58 */     this.policyURIs = policyURIs;
/*  59 */     this.policySubject = policySubject;
/*     */   }
/*     */ 
/*     */   final void populate(PolicyMapExtender policyMapExtender) throws PolicyException {
/*  63 */     if (null == policyMapExtender) {
/*  64 */       throw ((PolicyException)LOGGER.logSevereException(new PolicyException(PolicyMessages.WSP_1006_POLICY_MAP_EXTENDER_CAN_NOT_BE_NULL())));
/*     */     }
/*     */ 
/*  67 */     doPopulate(policyMapExtender);
/*     */   }
/*     */ 
/*     */   protected abstract void doPopulate(PolicyMapExtender paramPolicyMapExtender) throws PolicyException;
/*     */ 
/*     */   final Collection<Policy> getPolicies() throws PolicyException {
/*  73 */     if (null == this.policyURIs) {
/*  74 */       throw ((PolicyException)LOGGER.logSevereException(new PolicyException(PolicyMessages.WSP_1004_POLICY_URIS_CAN_NOT_BE_NULL())));
/*     */     }
/*  76 */     if (null == this.policyStore) {
/*  77 */       throw ((PolicyException)LOGGER.logSevereException(new PolicyException(PolicyMessages.WSP_1010_NO_POLICIES_DEFINED())));
/*     */     }
/*     */ 
/*  80 */     Collection result = new ArrayList(this.policyURIs.size());
/*     */ 
/*  82 */     for (String policyURI : this.policyURIs) {
/*  83 */       PolicySourceModel sourceModel = (PolicySourceModel)this.policyStore.get(policyURI);
/*  84 */       if (sourceModel == null) {
/*  85 */         throw ((PolicyException)LOGGER.logSevereException(new PolicyException(PolicyMessages.WSP_1005_POLICY_REFERENCE_DOES_NOT_EXIST(policyURI))));
/*     */       }
/*  87 */       result.add(ModelTranslator.getTranslator().translate(sourceModel));
/*     */     }
/*     */ 
/*  91 */     return result;
/*     */   }
/*     */ 
/*     */   final Collection<PolicySubject> getPolicySubjects() throws PolicyException {
/*  95 */     Collection policies = getPolicies();
/*  96 */     Collection result = new ArrayList(policies.size());
/*  97 */     for (Policy policy : policies) {
/*  98 */       result.add(new PolicySubject(this.policySubject, policy));
/*     */     }
/* 100 */     return result;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.jaxws.BuilderHandler
 * JD-Core Version:    0.6.2
 */