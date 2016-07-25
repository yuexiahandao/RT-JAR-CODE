/*     */ package com.sun.xml.internal.ws.policy;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.ServiceProvider;
/*     */ import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator;
/*     */ import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator.Fitness;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ 
/*     */ public class AssertionValidationProcessor
/*     */ {
/*  43 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(AssertionValidationProcessor.class);
/*     */ 
/*  45 */   private final Collection<PolicyAssertionValidator> validators = new LinkedList();
/*     */ 
/*     */   private AssertionValidationProcessor()
/*     */     throws PolicyException
/*     */   {
/*  55 */     this(null);
/*     */   }
/*     */ 
/*     */   protected AssertionValidationProcessor(Collection<PolicyAssertionValidator> policyValidators)
/*     */     throws PolicyException
/*     */   {
/*  70 */     for (PolicyAssertionValidator validator : (PolicyAssertionValidator[])PolicyUtils.ServiceProvider.load(PolicyAssertionValidator.class)) {
/*  71 */       this.validators.add(validator);
/*     */     }
/*  73 */     if (policyValidators != null) {
/*  74 */       for (PolicyAssertionValidator validator : policyValidators) {
/*  75 */         this.validators.add(validator);
/*     */       }
/*     */     }
/*  78 */     if (this.validators.size() == 0)
/*  79 */       throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0076_NO_SERVICE_PROVIDERS_FOUND(PolicyAssertionValidator.class.getName()))));
/*     */   }
/*     */ 
/*     */   public static AssertionValidationProcessor getInstance()
/*     */     throws PolicyException
/*     */   {
/*  93 */     return new AssertionValidationProcessor();
/*     */   }
/*     */ 
/*     */   public PolicyAssertionValidator.Fitness validateClientSide(PolicyAssertion assertion)
/*     */     throws PolicyException
/*     */   {
/* 105 */     PolicyAssertionValidator.Fitness assertionFitness = PolicyAssertionValidator.Fitness.UNKNOWN;
/* 106 */     for (PolicyAssertionValidator validator : this.validators) {
/* 107 */       assertionFitness = assertionFitness.combine(validator.validateClientSide(assertion));
/* 108 */       if (assertionFitness == PolicyAssertionValidator.Fitness.SUPPORTED)
/*     */       {
/*     */         break;
/*     */       }
/*     */     }
/* 113 */     return assertionFitness;
/*     */   }
/*     */ 
/*     */   public PolicyAssertionValidator.Fitness validateServerSide(PolicyAssertion assertion)
/*     */     throws PolicyException
/*     */   {
/* 125 */     PolicyAssertionValidator.Fitness assertionFitness = PolicyAssertionValidator.Fitness.UNKNOWN;
/* 126 */     for (PolicyAssertionValidator validator : this.validators) {
/* 127 */       assertionFitness = assertionFitness.combine(validator.validateServerSide(assertion));
/* 128 */       if (assertionFitness == PolicyAssertionValidator.Fitness.SUPPORTED)
/*     */       {
/*     */         break;
/*     */       }
/*     */     }
/* 133 */     return assertionFitness;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.AssertionValidationProcessor
 * JD-Core Version:    0.6.2
 */