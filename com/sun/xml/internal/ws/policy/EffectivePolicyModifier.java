/*     */ package com.sun.xml.internal.ws.policy;
/*     */ 
/*     */ public final class EffectivePolicyModifier extends PolicyMapMutator
/*     */ {
/*     */   public static EffectivePolicyModifier createEffectivePolicyModifier()
/*     */   {
/*  36 */     return new EffectivePolicyModifier();
/*     */   }
/*     */ 
/*     */   public void setNewEffectivePolicyForServiceScope(PolicyMapKey key, Policy newEffectivePolicy)
/*     */   {
/*  58 */     getMap().setNewEffectivePolicyForScope(PolicyMap.ScopeType.SERVICE, key, newEffectivePolicy);
/*     */   }
/*     */ 
/*     */   public void setNewEffectivePolicyForEndpointScope(PolicyMapKey key, Policy newEffectivePolicy)
/*     */   {
/*  72 */     getMap().setNewEffectivePolicyForScope(PolicyMap.ScopeType.ENDPOINT, key, newEffectivePolicy);
/*     */   }
/*     */ 
/*     */   public void setNewEffectivePolicyForOperationScope(PolicyMapKey key, Policy newEffectivePolicy)
/*     */   {
/*  87 */     getMap().setNewEffectivePolicyForScope(PolicyMap.ScopeType.OPERATION, key, newEffectivePolicy);
/*     */   }
/*     */ 
/*     */   public void setNewEffectivePolicyForInputMessageScope(PolicyMapKey key, Policy newEffectivePolicy)
/*     */   {
/* 102 */     getMap().setNewEffectivePolicyForScope(PolicyMap.ScopeType.INPUT_MESSAGE, key, newEffectivePolicy);
/*     */   }
/*     */ 
/*     */   public void setNewEffectivePolicyForOutputMessageScope(PolicyMapKey key, Policy newEffectivePolicy)
/*     */   {
/* 117 */     getMap().setNewEffectivePolicyForScope(PolicyMap.ScopeType.OUTPUT_MESSAGE, key, newEffectivePolicy);
/*     */   }
/*     */ 
/*     */   public void setNewEffectivePolicyForFaultMessageScope(PolicyMapKey key, Policy newEffectivePolicy)
/*     */   {
/* 132 */     getMap().setNewEffectivePolicyForScope(PolicyMap.ScopeType.FAULT_MESSAGE, key, newEffectivePolicy);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.EffectivePolicyModifier
 * JD-Core Version:    0.6.2
 */