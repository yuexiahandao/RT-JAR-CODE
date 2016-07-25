/*     */ package com.sun.xml.internal.ws.encoding.policy;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.policy.AssertionSet;
/*     */ import com.sun.xml.internal.ws.policy.Policy;
/*     */ import com.sun.xml.internal.ws.policy.PolicyAssertion;
/*     */ import com.sun.xml.internal.ws.policy.PolicyException;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMap;
/*     */ import com.sun.xml.internal.ws.policy.PolicySubject;
/*     */ import com.sun.xml.internal.ws.policy.jaxws.spi.PolicyMapConfigurator;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.AssertionData;
/*     */ import com.sun.xml.internal.ws.policy.subject.WsdlBindingSubject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.logging.Level;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.soap.MTOMFeature;
/*     */ 
/*     */ public class MtomPolicyMapConfigurator
/*     */   implements PolicyMapConfigurator
/*     */ {
/*  56 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(MtomPolicyMapConfigurator.class);
/*     */ 
/*     */   public Collection<PolicySubject> update(PolicyMap policyMap, SEIModel model, WSBinding wsBinding)
/*     */     throws PolicyException
/*     */   {
/*  90 */     LOGGER.entering(new Object[] { policyMap, model, wsBinding });
/*     */ 
/*  92 */     Collection subjects = new ArrayList();
/*  93 */     if (policyMap != null) {
/*  94 */       MTOMFeature mtomFeature = (MTOMFeature)wsBinding.getFeature(MTOMFeature.class);
/*  95 */       if (LOGGER.isLoggable(Level.FINEST)) {
/*  96 */         LOGGER.finest("mtomFeature = " + mtomFeature);
/*     */       }
/*  98 */       if ((mtomFeature != null) && (mtomFeature.isEnabled())) {
/*  99 */         QName bindingName = model.getBoundPortTypeName();
/* 100 */         WsdlBindingSubject wsdlSubject = WsdlBindingSubject.createBindingSubject(bindingName);
/* 101 */         Policy mtomPolicy = createMtomPolicy(bindingName);
/* 102 */         PolicySubject mtomPolicySubject = new PolicySubject(wsdlSubject, mtomPolicy);
/* 103 */         subjects.add(mtomPolicySubject);
/* 104 */         if (LOGGER.isLoggable(Level.FINEST)) {
/* 105 */           LOGGER.fine("Added MTOM policy with ID \"" + mtomPolicy.getIdOrName() + "\" to binding element \"" + bindingName + "\"");
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 110 */     LOGGER.exiting(subjects);
/* 111 */     return subjects;
/*     */   }
/*     */ 
/*     */   private Policy createMtomPolicy(QName bindingName)
/*     */   {
/* 122 */     ArrayList assertionSets = new ArrayList(1);
/* 123 */     ArrayList assertions = new ArrayList(1);
/* 124 */     assertions.add(new MtomAssertion());
/* 125 */     assertionSets.add(AssertionSet.createAssertionSet(assertions));
/* 126 */     return Policy.createPolicy(null, bindingName.getLocalPart() + "_MTOM_Policy", assertionSets);
/*     */   }
/*     */ 
/*     */   static class MtomAssertion extends PolicyAssertion
/*     */   {
/*  62 */     private static final AssertionData mtomData = AssertionData.createAssertionData(EncodingConstants.OPTIMIZED_MIME_SERIALIZATION_ASSERTION);
/*     */ 
/*     */     MtomAssertion()
/*     */     {
/*  71 */       super(null, null);
/*     */     }
/*     */ 
/*     */     static
/*     */     {
/*  67 */       mtomData.setOptionalAttribute(true);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.policy.MtomPolicyMapConfigurator
 * JD-Core Version:    0.6.2
 */