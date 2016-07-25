/*    */ package com.sun.corba.se.impl.oa.poa;
/*    */ 
/*    */ import org.omg.CORBA.LocalObject;
/*    */ import org.omg.CORBA.Policy;
/*    */ import org.omg.PortableServer.ImplicitActivationPolicy;
/*    */ import org.omg.PortableServer.ImplicitActivationPolicyValue;
/*    */ 
/*    */ final class ImplicitActivationPolicyImpl extends LocalObject
/*    */   implements ImplicitActivationPolicy
/*    */ {
/*    */   private ImplicitActivationPolicyValue value;
/*    */ 
/*    */   public ImplicitActivationPolicyImpl(ImplicitActivationPolicyValue paramImplicitActivationPolicyValue)
/*    */   {
/* 37 */     this.value = paramImplicitActivationPolicyValue;
/*    */   }
/*    */ 
/*    */   public ImplicitActivationPolicyValue value() {
/* 41 */     return this.value;
/*    */   }
/*    */ 
/*    */   public int policy_type()
/*    */   {
/* 46 */     return 20;
/*    */   }
/*    */ 
/*    */   public Policy copy() {
/* 50 */     return new ImplicitActivationPolicyImpl(this.value);
/*    */   }
/*    */ 
/*    */   public void destroy() {
/* 54 */     this.value = null;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 61 */     return "ImplicitActivationPolicy[" + (this.value.value() == 0 ? "IMPLICIT_ACTIVATION" : "NO_IMPLICIT_ACTIVATION]");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.ImplicitActivationPolicyImpl
 * JD-Core Version:    0.6.2
 */