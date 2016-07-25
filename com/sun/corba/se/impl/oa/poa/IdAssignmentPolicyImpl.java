/*    */ package com.sun.corba.se.impl.oa.poa;
/*    */ 
/*    */ import org.omg.CORBA.LocalObject;
/*    */ import org.omg.CORBA.Policy;
/*    */ import org.omg.PortableServer.IdAssignmentPolicy;
/*    */ import org.omg.PortableServer.IdAssignmentPolicyValue;
/*    */ 
/*    */ final class IdAssignmentPolicyImpl extends LocalObject
/*    */   implements IdAssignmentPolicy
/*    */ {
/*    */   private IdAssignmentPolicyValue value;
/*    */ 
/*    */   public IdAssignmentPolicyImpl(IdAssignmentPolicyValue paramIdAssignmentPolicyValue)
/*    */   {
/* 36 */     this.value = paramIdAssignmentPolicyValue;
/*    */   }
/*    */ 
/*    */   public IdAssignmentPolicyValue value() {
/* 40 */     return this.value;
/*    */   }
/*    */ 
/*    */   public int policy_type()
/*    */   {
/* 45 */     return 19;
/*    */   }
/*    */ 
/*    */   public Policy copy() {
/* 49 */     return new IdAssignmentPolicyImpl(this.value);
/*    */   }
/*    */ 
/*    */   public void destroy() {
/* 53 */     this.value = null;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 60 */     return "IdAssignmentPolicy[" + (this.value.value() == 0 ? "USER_ID" : "SYSTEM_ID]");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.IdAssignmentPolicyImpl
 * JD-Core Version:    0.6.2
 */