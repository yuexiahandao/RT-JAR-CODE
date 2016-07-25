/*     */ package com.sun.xml.internal.ws.addressing.policy;
/*     */ 
/*     */ import com.sun.xml.internal.ws.addressing.W3CAddressingMetadataConstants;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.policy.NestedPolicy;
/*     */ import com.sun.xml.internal.ws.policy.PolicyAssertion;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator;
/*     */ import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator.Fitness;
/*     */ import java.util.ArrayList;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class AddressingPolicyValidator
/*     */   implements PolicyAssertionValidator
/*     */ {
/*  48 */   private static final ArrayList<QName> supportedAssertions = new ArrayList();
/*     */ 
/* 102 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(AddressingPolicyValidator.class);
/*     */ 
/*     */   public PolicyAssertionValidator.Fitness validateClientSide(PolicyAssertion assertion)
/*     */   {
/*  64 */     return supportedAssertions.contains(assertion.getName()) ? PolicyAssertionValidator.Fitness.SUPPORTED : PolicyAssertionValidator.Fitness.UNKNOWN;
/*     */   }
/*     */ 
/*     */   public PolicyAssertionValidator.Fitness validateServerSide(PolicyAssertion assertion) {
/*  68 */     if (!supportedAssertions.contains(assertion.getName())) {
/*  69 */       return PolicyAssertionValidator.Fitness.UNKNOWN;
/*     */     }
/*     */ 
/*  72 */     if (assertion.getName().equals(W3CAddressingMetadataConstants.WSAM_ADDRESSING_ASSERTION)) {
/*  73 */       NestedPolicy nestedPolicy = assertion.getNestedPolicy();
/*  74 */       if (nestedPolicy != null) {
/*  75 */         boolean requiresAnonymousResponses = false;
/*  76 */         boolean requiresNonAnonymousResponses = false;
/*  77 */         for (PolicyAssertion nestedAsser : nestedPolicy.getAssertionSet()) {
/*  78 */           if (nestedAsser.getName().equals(W3CAddressingMetadataConstants.WSAM_ANONYMOUS_NESTED_ASSERTION)) {
/*  79 */             requiresAnonymousResponses = true;
/*  80 */           } else if (nestedAsser.getName().equals(W3CAddressingMetadataConstants.WSAM_NONANONYMOUS_NESTED_ASSERTION)) {
/*  81 */             requiresNonAnonymousResponses = true;
/*     */           } else {
/*  83 */             LOGGER.warning("Found unsupported assertion:\n" + nestedAsser + "\nnested into assertion:\n" + assertion);
/*  84 */             return PolicyAssertionValidator.Fitness.UNSUPPORTED;
/*     */           }
/*     */         }
/*     */ 
/*  88 */         if ((requiresAnonymousResponses) && (requiresNonAnonymousResponses)) {
/*  89 */           LOGGER.warning("Only one among AnonymousResponses and NonAnonymousResponses can be nested in an Addressing assertion");
/*  90 */           return PolicyAssertionValidator.Fitness.INVALID;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  95 */     return PolicyAssertionValidator.Fitness.SUPPORTED;
/*     */   }
/*     */ 
/*     */   public String[] declareSupportedDomains() {
/*  99 */     return new String[] { AddressingVersion.MEMBER.policyNsUri, AddressingVersion.W3C.policyNsUri, "http://www.w3.org/2007/05/addressing/metadata" };
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  51 */     supportedAssertions.add(new QName(AddressingVersion.MEMBER.policyNsUri, "UsingAddressing"));
/*  52 */     supportedAssertions.add(W3CAddressingMetadataConstants.WSAM_ADDRESSING_ASSERTION);
/*  53 */     supportedAssertions.add(W3CAddressingMetadataConstants.WSAM_ANONYMOUS_NESTED_ASSERTION);
/*  54 */     supportedAssertions.add(W3CAddressingMetadataConstants.WSAM_NONANONYMOUS_NESTED_ASSERTION);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.policy.AddressingPolicyValidator
 * JD-Core Version:    0.6.2
 */