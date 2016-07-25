/*    */ package com.sun.corba.se.impl.oa.poa;
/*    */ 
/*    */ import org.omg.CORBA.LocalObject;
/*    */ import org.omg.CORBA.Policy;
/*    */ import org.omg.PortableServer.IdUniquenessPolicy;
/*    */ import org.omg.PortableServer.IdUniquenessPolicyValue;
/*    */ 
/*    */ final class IdUniquenessPolicyImpl extends LocalObject
/*    */   implements IdUniquenessPolicy
/*    */ {
/*    */   private IdUniquenessPolicyValue value;
/*    */ 
/*    */   public IdUniquenessPolicyImpl(IdUniquenessPolicyValue paramIdUniquenessPolicyValue)
/*    */   {
/* 35 */     this.value = paramIdUniquenessPolicyValue;
/*    */   }
/*    */ 
/*    */   public IdUniquenessPolicyValue value() {
/* 39 */     return this.value;
/*    */   }
/*    */ 
/*    */   public int policy_type()
/*    */   {
/* 44 */     return 18;
/*    */   }
/*    */ 
/*    */   public Policy copy() {
/* 48 */     return new IdUniquenessPolicyImpl(this.value);
/*    */   }
/*    */ 
/*    */   public void destroy() {
/* 52 */     this.value = null;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 59 */     return "IdUniquenessPolicy[" + (this.value.value() == 0 ? "UNIQUE_ID" : "MULTIPLE_ID]");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.IdUniquenessPolicyImpl
 * JD-Core Version:    0.6.2
 */