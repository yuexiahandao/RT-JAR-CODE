/*    */ package com.sun.xml.internal.ws.encoding.policy;
/*    */ 
/*    */ import com.sun.xml.internal.ws.policy.PolicyAssertion;
/*    */ import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator;
/*    */ import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator.Fitness;
/*    */ import java.util.ArrayList;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class EncodingPolicyValidator
/*    */   implements PolicyAssertionValidator
/*    */ {
/* 42 */   private static final ArrayList<QName> serverSideSupportedAssertions = new ArrayList(3);
/* 43 */   private static final ArrayList<QName> clientSideSupportedAssertions = new ArrayList(4);
/*    */ 
/*    */   public PolicyAssertionValidator.Fitness validateClientSide(PolicyAssertion assertion)
/*    */   {
/* 61 */     return clientSideSupportedAssertions.contains(assertion.getName()) ? PolicyAssertionValidator.Fitness.SUPPORTED : PolicyAssertionValidator.Fitness.UNKNOWN;
/*    */   }
/*    */ 
/*    */   public PolicyAssertionValidator.Fitness validateServerSide(PolicyAssertion assertion) {
/* 65 */     QName assertionName = assertion.getName();
/* 66 */     if (serverSideSupportedAssertions.contains(assertionName))
/* 67 */       return PolicyAssertionValidator.Fitness.SUPPORTED;
/* 68 */     if (clientSideSupportedAssertions.contains(assertionName)) {
/* 69 */       return PolicyAssertionValidator.Fitness.UNSUPPORTED;
/*    */     }
/* 71 */     return PolicyAssertionValidator.Fitness.UNKNOWN;
/*    */   }
/*    */ 
/*    */   public String[] declareSupportedDomains()
/*    */   {
/* 76 */     return new String[] { "http://schemas.xmlsoap.org/ws/2004/09/policy/optimizedmimeserialization", "http://schemas.xmlsoap.org/ws/2004/09/policy/encoding", "http://java.sun.com/xml/ns/wsit/2006/09/policy/encoding/client", "http://java.sun.com/xml/ns/wsit/2006/09/policy/fastinfoset/service" };
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 46 */     serverSideSupportedAssertions.add(EncodingConstants.OPTIMIZED_MIME_SERIALIZATION_ASSERTION);
/* 47 */     serverSideSupportedAssertions.add(EncodingConstants.UTF816FFFE_CHARACTER_ENCODING_ASSERTION);
/* 48 */     serverSideSupportedAssertions.add(EncodingConstants.OPTIMIZED_FI_SERIALIZATION_ASSERTION);
/*    */ 
/* 50 */     clientSideSupportedAssertions.add(EncodingConstants.SELECT_OPTIMAL_ENCODING_ASSERTION);
/* 51 */     clientSideSupportedAssertions.addAll(serverSideSupportedAssertions);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.policy.EncodingPolicyValidator
 * JD-Core Version:    0.6.2
 */