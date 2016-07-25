/*     */ package com.sun.xml.internal.ws.policy;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator.Fitness;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ 
/*     */ public class EffectiveAlternativeSelector
/*     */ {
/* 139 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(EffectiveAlternativeSelector.class);
/*     */ 
/*     */   public static void doSelection(EffectivePolicyModifier modifier)
/*     */     throws PolicyException
/*     */   {
/* 152 */     AssertionValidationProcessor validationProcessor = AssertionValidationProcessor.getInstance();
/* 153 */     selectAlternatives(modifier, validationProcessor);
/*     */   }
/*     */ 
/*     */   protected static void selectAlternatives(EffectivePolicyModifier modifier, AssertionValidationProcessor validationProcessor)
/*     */     throws PolicyException
/*     */   {
/* 167 */     PolicyMap map = modifier.getMap();
/* 168 */     for (PolicyMapKey mapKey : map.getAllServiceScopeKeys()) {
/* 169 */       Policy oldPolicy = map.getServiceEffectivePolicy(mapKey);
/* 170 */       modifier.setNewEffectivePolicyForServiceScope(mapKey, selectBestAlternative(oldPolicy, validationProcessor));
/*     */     }
/* 172 */     for (PolicyMapKey mapKey : map.getAllEndpointScopeKeys()) {
/* 173 */       Policy oldPolicy = map.getEndpointEffectivePolicy(mapKey);
/* 174 */       modifier.setNewEffectivePolicyForEndpointScope(mapKey, selectBestAlternative(oldPolicy, validationProcessor));
/*     */     }
/* 176 */     for (PolicyMapKey mapKey : map.getAllOperationScopeKeys()) {
/* 177 */       Policy oldPolicy = map.getOperationEffectivePolicy(mapKey);
/* 178 */       modifier.setNewEffectivePolicyForOperationScope(mapKey, selectBestAlternative(oldPolicy, validationProcessor));
/*     */     }
/* 180 */     for (PolicyMapKey mapKey : map.getAllInputMessageScopeKeys()) {
/* 181 */       Policy oldPolicy = map.getInputMessageEffectivePolicy(mapKey);
/* 182 */       modifier.setNewEffectivePolicyForInputMessageScope(mapKey, selectBestAlternative(oldPolicy, validationProcessor));
/*     */     }
/* 184 */     for (PolicyMapKey mapKey : map.getAllOutputMessageScopeKeys()) {
/* 185 */       Policy oldPolicy = map.getOutputMessageEffectivePolicy(mapKey);
/* 186 */       modifier.setNewEffectivePolicyForOutputMessageScope(mapKey, selectBestAlternative(oldPolicy, validationProcessor));
/*     */     }
/* 188 */     for (PolicyMapKey mapKey : map.getAllFaultMessageScopeKeys()) {
/* 189 */       Policy oldPolicy = map.getFaultMessageEffectivePolicy(mapKey);
/* 190 */       modifier.setNewEffectivePolicyForFaultMessageScope(mapKey, selectBestAlternative(oldPolicy, validationProcessor));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Policy selectBestAlternative(Policy policy, AssertionValidationProcessor validationProcessor) throws PolicyException {
/* 195 */     AssertionSet bestAlternative = null;
/* 196 */     AlternativeFitness bestAlternativeFitness = AlternativeFitness.UNEVALUATED;
/* 197 */     for (AssertionSet alternative : policy) {
/* 198 */       AlternativeFitness alternativeFitness = alternative.isEmpty() ? AlternativeFitness.SUPPORTED_EMPTY : AlternativeFitness.UNEVALUATED;
/* 199 */       for (PolicyAssertion assertion : alternative)
/*     */       {
/* 201 */         PolicyAssertionValidator.Fitness assertionFitness = validationProcessor.validateClientSide(assertion);
/* 202 */         switch (1.$SwitchMap$com$sun$xml$internal$ws$policy$spi$PolicyAssertionValidator$Fitness[assertionFitness.ordinal()]) {
/*     */         case 1:
/*     */         case 2:
/*     */         case 4:
/* 206 */           LOGGER.warning(LocalizationMessages.WSP_0075_PROBLEMATIC_ASSERTION_STATE(assertion.getName(), assertionFitness));
/* 207 */           break;
/*     */         case 3:
/*     */         }
/*     */ 
/* 212 */         alternativeFitness = alternativeFitness.combine(assertionFitness);
/*     */       }
/*     */ 
/* 215 */       if (bestAlternativeFitness.compareTo(alternativeFitness) < 0)
/*     */       {
/* 217 */         bestAlternative = alternative;
/* 218 */         bestAlternativeFitness = alternativeFitness;
/*     */       }
/*     */ 
/* 221 */       if (bestAlternativeFitness == AlternativeFitness.SUPPORTED)
/*     */       {
/*     */         break;
/*     */       }
/*     */     }
/*     */ 
/* 227 */     switch (1.$SwitchMap$com$sun$xml$internal$ws$policy$EffectiveAlternativeSelector$AlternativeFitness[bestAlternativeFitness.ordinal()]) {
/*     */     case 1:
/* 229 */       throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0053_INVALID_CLIENT_SIDE_ALTERNATIVE())));
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/* 233 */       LOGGER.warning(LocalizationMessages.WSP_0019_SUBOPTIMAL_ALTERNATIVE_SELECTED(bestAlternativeFitness));
/* 234 */       break;
/*     */     }
/*     */ 
/* 239 */     Collection alternativeSet = null;
/* 240 */     if (bestAlternative != null)
/*     */     {
/* 242 */       alternativeSet = new LinkedList();
/* 243 */       alternativeSet.add(bestAlternative);
/*     */     }
/* 245 */     return Policy.createPolicy(policy.getNamespaceVersion(), policy.getName(), policy.getId(), alternativeSet);
/*     */   }
/*     */ 
/*     */   private static abstract enum AlternativeFitness
/*     */   {
/*  48 */     UNEVALUATED, 
/*     */ 
/*  64 */     INVALID, 
/*     */ 
/*  69 */     UNKNOWN, 
/*     */ 
/*  85 */     UNSUPPORTED, 
/*     */ 
/* 100 */     PARTIALLY_SUPPORTED, 
/*     */ 
/* 114 */     SUPPORTED_EMPTY, 
/*     */ 
/* 120 */     SUPPORTED;
/*     */ 
/*     */     abstract AlternativeFitness combine(PolicyAssertionValidator.Fitness paramFitness);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector
 * JD-Core Version:    0.6.2
 */