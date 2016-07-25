/*     */ package com.sun.xml.internal.ws.policy;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ 
/*     */ public final class PolicyIntersector
/*     */ {
/*  48 */   private static final PolicyIntersector STRICT_INTERSECTOR = new PolicyIntersector(CompatibilityMode.STRICT);
/*  49 */   private static final PolicyIntersector LAX_INTERSECTOR = new PolicyIntersector(CompatibilityMode.LAX);
/*  50 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(PolicyIntersector.class);
/*     */   private CompatibilityMode mode;
/*     */ 
/*     */   private PolicyIntersector(CompatibilityMode intersectionMode)
/*     */   {
/*  59 */     this.mode = intersectionMode;
/*     */   }
/*     */ 
/*     */   public static PolicyIntersector createStrictPolicyIntersector()
/*     */   {
/*  68 */     return STRICT_INTERSECTOR;
/*     */   }
/*     */ 
/*     */   public static PolicyIntersector createLaxPolicyIntersector()
/*     */   {
/*  77 */     return LAX_INTERSECTOR;
/*     */   }
/*     */ 
/*     */   public Policy intersect(Policy[] policies)
/*     */   {
/*  91 */     if ((policies == null) || (policies.length == 0))
/*  92 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0056_NEITHER_NULL_NOR_EMPTY_POLICY_COLLECTION_EXPECTED())));
/*  93 */     if (policies.length == 1) {
/*  94 */       return policies[0];
/*     */     }
/*     */ 
/*  99 */     boolean found = false;
/* 100 */     boolean allPoliciesEmpty = true;
/* 101 */     NamespaceVersion latestVersion = null;
/* 102 */     for (Policy tested : policies) {
/* 103 */       if (tested.isEmpty()) {
/* 104 */         found = true;
/*     */       } else {
/* 106 */         if (tested.isNull()) {
/* 107 */           found = true;
/*     */         }
/* 109 */         allPoliciesEmpty = false;
/*     */       }
/* 111 */       if (latestVersion == null)
/* 112 */         latestVersion = tested.getNamespaceVersion();
/* 113 */       else if (latestVersion.compareTo(tested.getNamespaceVersion()) < 0) {
/* 114 */         latestVersion = tested.getNamespaceVersion();
/*     */       }
/*     */ 
/* 117 */       if ((found) && (!allPoliciesEmpty)) {
/* 118 */         return Policy.createNullPolicy(latestVersion, null, null);
/*     */       }
/*     */     }
/* 121 */     latestVersion = latestVersion != null ? latestVersion : NamespaceVersion.getLatestVersion();
/* 122 */     if (allPoliciesEmpty) {
/* 123 */       return Policy.createEmptyPolicy(latestVersion, null, null);
/*     */     }
/*     */ 
/* 127 */     List finalAlternatives = new LinkedList(policies[0].getContent());
/* 128 */     Queue testedAlternatives = new LinkedList();
/* 129 */     List alternativesToMerge = new ArrayList(2);
/* 130 */     for (int i = 1; i < policies.length; i++) {
/* 131 */       Collection currentAlternatives = policies[i].getContent();
/*     */ 
/* 133 */       testedAlternatives.clear();
/* 134 */       testedAlternatives.addAll(finalAlternatives);
/* 135 */       finalAlternatives.clear();
/*     */       AssertionSet testedAlternative;
/* 138 */       while ((testedAlternative = (AssertionSet)testedAlternatives.poll()) != null) {
/* 139 */         for (AssertionSet currentAlternative : currentAlternatives) {
/* 140 */           if (testedAlternative.isCompatibleWith(currentAlternative, this.mode)) {
/* 141 */             alternativesToMerge.add(testedAlternative);
/* 142 */             alternativesToMerge.add(currentAlternative);
/* 143 */             finalAlternatives.add(AssertionSet.createMergedAssertionSet(alternativesToMerge));
/* 144 */             alternativesToMerge.clear();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 150 */     return Policy.createPolicy(latestVersion, null, null, finalAlternatives);
/*     */   }
/*     */ 
/*     */   static enum CompatibilityMode
/*     */   {
/*  44 */     STRICT, 
/*  45 */     LAX;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.PolicyIntersector
 * JD-Core Version:    0.6.2
 */