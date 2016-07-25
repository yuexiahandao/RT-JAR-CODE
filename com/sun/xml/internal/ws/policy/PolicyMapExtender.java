/*    */ package com.sun.xml.internal.ws.policy;
/*    */ 
/*    */ public final class PolicyMapExtender extends PolicyMapMutator
/*    */ {
/*    */   public static PolicyMapExtender createPolicyMapExtender()
/*    */   {
/* 43 */     return new PolicyMapExtender();
/*    */   }
/*    */ 
/*    */   public void putServiceSubject(PolicyMapKey key, PolicySubject subject) {
/* 47 */     getMap().putSubject(PolicyMap.ScopeType.SERVICE, key, subject);
/*    */   }
/*    */ 
/*    */   public void putEndpointSubject(PolicyMapKey key, PolicySubject subject) {
/* 51 */     getMap().putSubject(PolicyMap.ScopeType.ENDPOINT, key, subject);
/*    */   }
/*    */ 
/*    */   public void putOperationSubject(PolicyMapKey key, PolicySubject subject) {
/* 55 */     getMap().putSubject(PolicyMap.ScopeType.OPERATION, key, subject);
/*    */   }
/*    */ 
/*    */   public void putInputMessageSubject(PolicyMapKey key, PolicySubject subject) {
/* 59 */     getMap().putSubject(PolicyMap.ScopeType.INPUT_MESSAGE, key, subject);
/*    */   }
/*    */ 
/*    */   public void putOutputMessageSubject(PolicyMapKey key, PolicySubject subject) {
/* 63 */     getMap().putSubject(PolicyMap.ScopeType.OUTPUT_MESSAGE, key, subject);
/*    */   }
/*    */ 
/*    */   public void putFaultMessageSubject(PolicyMapKey key, PolicySubject subject) {
/* 67 */     getMap().putSubject(PolicyMap.ScopeType.FAULT_MESSAGE, key, subject);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.PolicyMapExtender
 * JD-Core Version:    0.6.2
 */