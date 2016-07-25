/*     */ package com.sun.xml.internal.ws.policy;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ final class PolicyScope
/*     */ {
/*  42 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(PolicyScope.class);
/*     */ 
/*  44 */   private final List<PolicySubject> subjects = new LinkedList();
/*     */ 
/*     */   PolicyScope(List<PolicySubject> initialSubjects) {
/*  47 */     if ((initialSubjects != null) && (!initialSubjects.isEmpty()))
/*  48 */       this.subjects.addAll(initialSubjects);
/*     */   }
/*     */ 
/*     */   void attach(PolicySubject subject)
/*     */   {
/*  53 */     if (subject == null) {
/*  54 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0020_SUBJECT_PARAM_MUST_NOT_BE_NULL())));
/*     */     }
/*     */ 
/*  57 */     this.subjects.add(subject);
/*     */   }
/*     */ 
/*     */   void dettachAllSubjects() {
/*  61 */     this.subjects.clear();
/*     */   }
/*     */ 
/*     */   Policy getEffectivePolicy(PolicyMerger merger)
/*     */     throws PolicyException
/*     */   {
/*  70 */     LinkedList policies = new LinkedList();
/*  71 */     for (PolicySubject subject : this.subjects) {
/*  72 */       policies.add(subject.getEffectivePolicy(merger));
/*     */     }
/*  74 */     return merger.merge(policies);
/*     */   }
/*     */ 
/*     */   Collection<PolicySubject> getPolicySubjects()
/*     */   {
/*  83 */     return this.subjects;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  92 */     return toString(0, new StringBuffer()).toString();
/*     */   }
/*     */ 
/*     */   StringBuffer toString(int indentLevel, StringBuffer buffer)
/*     */   {
/* 103 */     String indent = PolicyUtils.Text.createIndent(indentLevel);
/*     */ 
/* 105 */     buffer.append(indent).append("policy scope {").append(PolicyUtils.Text.NEW_LINE);
/* 106 */     for (PolicySubject policySubject : this.subjects) {
/* 107 */       policySubject.toString(indentLevel + 1, buffer).append(PolicyUtils.Text.NEW_LINE);
/*     */     }
/* 109 */     buffer.append(indent).append('}');
/*     */ 
/* 111 */     return buffer;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.PolicyScope
 * JD-Core Version:    0.6.2
 */