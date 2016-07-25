/*    */ package com.sun.xml.internal.ws.encoding.policy;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.fastinfoset.FastInfosetFeature;
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
/*    */ 
/*    */ public class FastInfosetFeatureConfigurator
/*    */   implements PolicyFeatureConfigurator
/*    */ {
/* 53 */   public static final QName enabled = new QName("enabled");
/*    */ 
/*    */   public Collection<WebServiceFeature> getFeatures(PolicyMapKey key, PolicyMap policyMap)
/*    */     throws PolicyException
/*    */   {
/* 63 */     Collection features = new LinkedList();
/* 64 */     if ((key != null) && (policyMap != null)) {
/* 65 */       Policy policy = policyMap.getEndpointEffectivePolicy(key);
/* 66 */       if ((null != policy) && (policy.contains(EncodingConstants.OPTIMIZED_FI_SERIALIZATION_ASSERTION))) {
/* 67 */         Iterator assertions = policy.iterator();
/* 68 */         while (assertions.hasNext()) {
/* 69 */           AssertionSet assertionSet = (AssertionSet)assertions.next();
/* 70 */           Iterator policyAssertion = assertionSet.iterator();
/* 71 */           while (policyAssertion.hasNext()) {
/* 72 */             PolicyAssertion assertion = (PolicyAssertion)policyAssertion.next();
/* 73 */             if (EncodingConstants.OPTIMIZED_FI_SERIALIZATION_ASSERTION.equals(assertion.getName())) {
/* 74 */               String value = assertion.getAttributeValue(enabled);
/* 75 */               boolean isFastInfosetEnabled = Boolean.valueOf(value.trim()).booleanValue();
/* 76 */               features.add(new FastInfosetFeature(isFastInfosetEnabled));
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/* 82 */     return features;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.policy.FastInfosetFeatureConfigurator
 * JD-Core Version:    0.6.2
 */