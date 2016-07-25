/*    */ package com.sun.xml.internal.ws.encoding.policy;
/*    */ 
/*    */ import com.sun.xml.internal.ws.policy.AssertionSet;
/*    */ import com.sun.xml.internal.ws.policy.Policy;
/*    */ import com.sun.xml.internal.ws.policy.PolicyAssertion;
/*    */ import com.sun.xml.internal.ws.policy.PolicyException;
/*    */ import com.sun.xml.internal.ws.policy.PolicyMap;
/*    */ import com.sun.xml.internal.ws.policy.PolicyMapKey;
/*    */ import com.sun.xml.internal.ws.policy.jaxws.spi.PolicyFeatureConfigurator;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedList;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.ws.WebServiceFeature;
/*    */ import javax.xml.ws.soap.MTOMFeature;
/*    */ 
/*    */ public class MtomFeatureConfigurator
/*    */   implements PolicyFeatureConfigurator
/*    */ {
/*    */   public Collection<WebServiceFeature> getFeatures(PolicyMapKey key, PolicyMap policyMap)
/*    */     throws PolicyException
/*    */   {
/* 64 */     Collection features = new LinkedList();
/* 65 */     if ((key != null) && (policyMap != null)) {
/* 66 */       Policy policy = policyMap.getEndpointEffectivePolicy(key);
/* 67 */       if ((null != policy) && (policy.contains(EncodingConstants.OPTIMIZED_MIME_SERIALIZATION_ASSERTION))) {
/* 68 */         Iterator assertions = policy.iterator();
/* 69 */         while (assertions.hasNext()) {
/* 70 */           AssertionSet assertionSet = (AssertionSet)assertions.next();
/* 71 */           Iterator policyAssertion = assertionSet.iterator();
/* 72 */           while (policyAssertion.hasNext()) {
/* 73 */             PolicyAssertion assertion = (PolicyAssertion)policyAssertion.next();
/* 74 */             if (EncodingConstants.OPTIMIZED_MIME_SERIALIZATION_ASSERTION.equals(assertion.getName())) {
/* 75 */               features.add(new MTOMFeature(true));
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/* 81 */     return features;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.policy.MtomFeatureConfigurator
 * JD-Core Version:    0.6.2
 */