/*    */ package com.sun.corba.se.impl.oa.poa;
/*    */ 
/*    */ import org.omg.CORBA.LocalObject;
/*    */ import org.omg.CORBA.Policy;
/*    */ import org.omg.PortableServer.LifespanPolicy;
/*    */ import org.omg.PortableServer.LifespanPolicyValue;
/*    */ 
/*    */ final class LifespanPolicyImpl extends LocalObject
/*    */   implements LifespanPolicy
/*    */ {
/*    */   private LifespanPolicyValue value;
/*    */ 
/*    */   public LifespanPolicyImpl(LifespanPolicyValue paramLifespanPolicyValue)
/*    */   {
/* 35 */     this.value = paramLifespanPolicyValue;
/*    */   }
/*    */ 
/*    */   public LifespanPolicyValue value() {
/* 39 */     return this.value;
/*    */   }
/*    */ 
/*    */   public int policy_type()
/*    */   {
/* 44 */     return 17;
/*    */   }
/*    */ 
/*    */   public Policy copy() {
/* 48 */     return new LifespanPolicyImpl(this.value);
/*    */   }
/*    */ 
/*    */   public void destroy() {
/* 52 */     this.value = null;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 59 */     return "LifespanPolicy[" + (this.value.value() == 0 ? "TRANSIENT" : "PERSISTENT]");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.LifespanPolicyImpl
 * JD-Core Version:    0.6.2
 */