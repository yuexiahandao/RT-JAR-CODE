/*     */ package com.sun.xml.internal.ws.policy;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class PolicySubject
/*     */ {
/*  42 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(PolicySubject.class);
/*     */ 
/*  44 */   private final List<Policy> policies = new LinkedList();
/*     */   private final Object subject;
/*     */ 
/*     */   public PolicySubject(Object subject, Policy policy)
/*     */     throws IllegalArgumentException
/*     */   {
/*  56 */     if ((subject == null) || (policy == null)) {
/*  57 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0021_SUBJECT_AND_POLICY_PARAM_MUST_NOT_BE_NULL(subject, policy))));
/*     */     }
/*     */ 
/*  60 */     this.subject = subject;
/*  61 */     attach(policy);
/*     */   }
/*     */ 
/*     */   public PolicySubject(Object subject, Collection<Policy> policies)
/*     */     throws IllegalArgumentException
/*     */   {
/*  74 */     if ((subject == null) || (policies == null)) {
/*  75 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0062_INPUT_PARAMS_MUST_NOT_BE_NULL())));
/*     */     }
/*     */ 
/*  78 */     if (policies.isEmpty()) {
/*  79 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0064_INITIAL_POLICY_COLLECTION_MUST_NOT_BE_EMPTY())));
/*     */     }
/*     */ 
/*  82 */     this.subject = subject;
/*  83 */     this.policies.addAll(policies);
/*     */   }
/*     */ 
/*     */   public void attach(Policy policy)
/*     */   {
/*  94 */     if (policy == null) {
/*  95 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0038_POLICY_TO_ATTACH_MUST_NOT_BE_NULL())));
/*     */     }
/*  97 */     this.policies.add(policy);
/*     */   }
/*     */ 
/*     */   public Policy getEffectivePolicy(PolicyMerger merger)
/*     */     throws PolicyException
/*     */   {
/* 107 */     return merger.merge(this.policies);
/*     */   }
/*     */ 
/*     */   public Object getSubject()
/*     */   {
/* 118 */     return this.subject;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 126 */     return toString(0, new StringBuffer()).toString();
/*     */   }
/*     */ 
/*     */   StringBuffer toString(int indentLevel, StringBuffer buffer)
/*     */   {
/* 137 */     String indent = PolicyUtils.Text.createIndent(indentLevel);
/* 138 */     String innerIndent = PolicyUtils.Text.createIndent(indentLevel + 1);
/*     */ 
/* 140 */     buffer.append(indent).append("policy subject {").append(PolicyUtils.Text.NEW_LINE);
/* 141 */     buffer.append(innerIndent).append("subject = '").append(this.subject).append('\'').append(PolicyUtils.Text.NEW_LINE);
/* 142 */     for (Policy policy : this.policies) {
/* 143 */       policy.toString(indentLevel + 1, buffer).append(PolicyUtils.Text.NEW_LINE);
/*     */     }
/* 145 */     buffer.append(indent).append('}');
/*     */ 
/* 147 */     return buffer;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.PolicySubject
 * JD-Core Version:    0.6.2
 */