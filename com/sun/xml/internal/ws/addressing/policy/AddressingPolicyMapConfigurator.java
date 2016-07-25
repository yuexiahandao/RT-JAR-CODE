/*     */ package com.sun.xml.internal.ws.addressing.policy;
/*     */ 
/*     */ import com.sun.xml.internal.ws.addressing.W3CAddressingMetadataConstants;
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
/*     */ import java.util.Collections;
/*     */ import java.util.logging.Level;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.soap.AddressingFeature;
/*     */ import javax.xml.ws.soap.AddressingFeature.Responses;
/*     */ 
/*     */ public class AddressingPolicyMapConfigurator
/*     */   implements PolicyMapConfigurator
/*     */ {
/*  57 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(AddressingPolicyMapConfigurator.class);
/*     */ 
/*     */   public Collection<PolicySubject> update(PolicyMap policyMap, SEIModel model, WSBinding wsBinding)
/*     */     throws PolicyException
/*     */   {
/*  86 */     LOGGER.entering(new Object[] { policyMap, model, wsBinding });
/*     */ 
/*  88 */     Collection subjects = new ArrayList();
/*  89 */     if (policyMap != null) {
/*  90 */       AddressingFeature addressingFeature = (AddressingFeature)wsBinding.getFeature(AddressingFeature.class);
/*  91 */       if (LOGGER.isLoggable(Level.FINEST)) {
/*  92 */         LOGGER.finest("addressingFeature = " + addressingFeature);
/*     */       }
/*  94 */       if ((addressingFeature != null) && (addressingFeature.isEnabled()))
/*     */       {
/*  96 */         addWsamAddressing(subjects, policyMap, model, addressingFeature);
/*     */       }
/*     */     }
/*  99 */     LOGGER.exiting(subjects);
/* 100 */     return subjects;
/*     */   }
/*     */ 
/*     */   private void addWsamAddressing(Collection<PolicySubject> subjects, PolicyMap policyMap, SEIModel model, AddressingFeature addressingFeature) throws PolicyException
/*     */   {
/* 105 */     QName bindingName = model.getBoundPortTypeName();
/* 106 */     WsdlBindingSubject wsdlSubject = WsdlBindingSubject.createBindingSubject(bindingName);
/* 107 */     Policy addressingPolicy = createWsamAddressingPolicy(bindingName, addressingFeature);
/* 108 */     PolicySubject addressingPolicySubject = new PolicySubject(wsdlSubject, addressingPolicy);
/* 109 */     subjects.add(addressingPolicySubject);
/* 110 */     if (LOGGER.isLoggable(Level.FINE))
/* 111 */       LOGGER.fine("Added addressing policy with ID \"" + addressingPolicy.getIdOrName() + "\" to binding element \"" + bindingName + "\"");
/*     */   }
/*     */ 
/*     */   private Policy createWsamAddressingPolicy(QName bindingName, AddressingFeature af)
/*     */   {
/* 119 */     ArrayList assertionSets = new ArrayList(1);
/* 120 */     ArrayList assertions = new ArrayList(1);
/* 121 */     AssertionData addressingData = AssertionData.createAssertionData(W3CAddressingMetadataConstants.WSAM_ADDRESSING_ASSERTION);
/*     */ 
/* 123 */     if (!af.isRequired())
/* 124 */       addressingData.setOptionalAttribute(true);
/*     */     try
/*     */     {
/* 127 */       AddressingFeature.Responses responses = af.getResponses();
/* 128 */       if (responses == AddressingFeature.Responses.ANONYMOUS) {
/* 129 */         AssertionData nestedAsserData = AssertionData.createAssertionData(W3CAddressingMetadataConstants.WSAM_ANONYMOUS_NESTED_ASSERTION);
/* 130 */         PolicyAssertion nestedAsser = new AddressingAssertion(nestedAsserData, null);
/* 131 */         assertions.add(new AddressingAssertion(addressingData, AssertionSet.createAssertionSet(Collections.singleton(nestedAsser))));
/* 132 */       } else if (responses == AddressingFeature.Responses.NON_ANONYMOUS) {
/* 133 */         AssertionData nestedAsserData = AssertionData.createAssertionData(W3CAddressingMetadataConstants.WSAM_NONANONYMOUS_NESTED_ASSERTION);
/* 134 */         PolicyAssertion nestedAsser = new AddressingAssertion(nestedAsserData, null);
/* 135 */         assertions.add(new AddressingAssertion(addressingData, AssertionSet.createAssertionSet(Collections.singleton(nestedAsser))));
/*     */       } else {
/* 137 */         assertions.add(new AddressingAssertion(addressingData, AssertionSet.createAssertionSet(null)));
/*     */       }
/*     */     }
/*     */     catch (NoSuchMethodError e)
/*     */     {
/* 142 */       assertions.add(new AddressingAssertion(addressingData, AssertionSet.createAssertionSet(null)));
/*     */     }
/* 144 */     assertionSets.add(AssertionSet.createAssertionSet(assertions));
/* 145 */     return Policy.createPolicy(null, bindingName.getLocalPart() + "_WSAM_Addressing_Policy", assertionSets);
/*     */   }
/*     */ 
/*     */   private static final class AddressingAssertion extends PolicyAssertion
/*     */   {
/*     */     AddressingAssertion(AssertionData assertionData, AssertionSet nestedAlternative)
/*     */     {
/*  67 */       super(null, nestedAlternative);
/*     */     }
/*     */ 
/*     */     AddressingAssertion(AssertionData assertionData)
/*     */     {
/*  76 */       super(null, null);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.policy.AddressingPolicyMapConfigurator
 * JD-Core Version:    0.6.2
 */