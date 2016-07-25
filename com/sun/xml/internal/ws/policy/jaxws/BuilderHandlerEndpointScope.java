/*    */ package com.sun.xml.internal.ws.policy.jaxws;
/*    */ 
/*    */ import com.sun.xml.internal.ws.policy.PolicyException;
/*    */ import com.sun.xml.internal.ws.policy.PolicyMap;
/*    */ import com.sun.xml.internal.ws.policy.PolicyMapExtender;
/*    */ import com.sun.xml.internal.ws.policy.PolicyMapKey;
/*    */ import com.sun.xml.internal.ws.policy.PolicySubject;
/*    */ import com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ final class BuilderHandlerEndpointScope extends BuilderHandler
/*    */ {
/*    */   private final QName service;
/*    */   private final QName port;
/*    */ 
/*    */   BuilderHandlerEndpointScope(Collection<String> policyURIs, Map<String, PolicySourceModel> policyStore, Object policySubject, QName service, QName port)
/*    */   {
/* 50 */     super(policyURIs, policyStore, policySubject);
/* 51 */     this.service = service;
/* 52 */     this.port = port;
/*    */   }
/*    */ 
/*    */   protected void doPopulate(PolicyMapExtender policyMapExtender) throws PolicyException {
/* 56 */     PolicyMapKey mapKey = PolicyMap.createWsdlEndpointScopeKey(this.service, this.port);
/* 57 */     for (PolicySubject subject : getPolicySubjects())
/* 58 */       policyMapExtender.putEndpointSubject(mapKey, subject);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 64 */     return this.service.toString() + ":" + this.port.toString();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.jaxws.BuilderHandlerEndpointScope
 * JD-Core Version:    0.6.2
 */