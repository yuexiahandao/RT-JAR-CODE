/*     */ package com.sun.xml.internal.ws.policy;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ 
/*     */ public final class PolicyMerger
/*     */ {
/*  40 */   private static final PolicyMerger merger = new PolicyMerger();
/*     */ 
/*     */   public static PolicyMerger getMerger()
/*     */   {
/*  55 */     return merger;
/*     */   }
/*     */ 
/*     */   public Policy merge(Collection<Policy> policies)
/*     */   {
/*  71 */     if ((policies == null) || (policies.isEmpty()))
/*  72 */       return null;
/*  73 */     if (policies.size() == 1) {
/*  74 */       return (Policy)policies.iterator().next();
/*     */     }
/*     */ 
/*  77 */     Collection alternativeSets = new LinkedList();
/*  78 */     StringBuilder id = new StringBuilder();
/*  79 */     NamespaceVersion mergedVersion = ((Policy)policies.iterator().next()).getNamespaceVersion();
/*  80 */     for (Policy policy : policies) {
/*  81 */       alternativeSets.add(policy.getContent());
/*  82 */       if (mergedVersion.compareTo(policy.getNamespaceVersion()) < 0) {
/*  83 */         mergedVersion = policy.getNamespaceVersion();
/*     */       }
/*  85 */       String policyId = policy.getId();
/*  86 */       if (policyId != null) {
/*  87 */         if (id.length() > 0) {
/*  88 */           id.append('-');
/*     */         }
/*  90 */         id.append(policyId);
/*     */       }
/*     */     }
/*     */ 
/*  94 */     Collection combinedAlternatives = PolicyUtils.Collections.combine(null, alternativeSets, false);
/*     */ 
/*  96 */     if ((combinedAlternatives == null) || (combinedAlternatives.isEmpty())) {
/*  97 */       return Policy.createNullPolicy(mergedVersion, null, id.length() == 0 ? null : id.toString());
/*     */     }
/*  99 */     Collection mergedSetList = new ArrayList(combinedAlternatives.size());
/* 100 */     for (Collection toBeMerged : combinedAlternatives) {
/* 101 */       mergedSetList.add(AssertionSet.createMergedAssertionSet(toBeMerged));
/*     */     }
/* 103 */     return Policy.createPolicy(mergedVersion, null, id.length() == 0 ? null : id.toString(), mergedSetList);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.PolicyMerger
 * JD-Core Version:    0.6.2
 */