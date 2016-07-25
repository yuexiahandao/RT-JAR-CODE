/*    */ package com.sun.corba.se.impl.oa.poa;
/*    */ 
/*    */ import org.omg.CORBA.LocalObject;
/*    */ import org.omg.CORBA.Policy;
/*    */ import org.omg.PortableServer.ThreadPolicy;
/*    */ import org.omg.PortableServer.ThreadPolicyValue;
/*    */ 
/*    */ final class ThreadPolicyImpl extends LocalObject
/*    */   implements ThreadPolicy
/*    */ {
/*    */   private ThreadPolicyValue value;
/*    */ 
/*    */   public ThreadPolicyImpl(ThreadPolicyValue paramThreadPolicyValue)
/*    */   {
/* 35 */     this.value = paramThreadPolicyValue;
/*    */   }
/*    */ 
/*    */   public ThreadPolicyValue value() {
/* 39 */     return this.value;
/*    */   }
/*    */ 
/*    */   public int policy_type()
/*    */   {
/* 44 */     return 16;
/*    */   }
/*    */ 
/*    */   public Policy copy() {
/* 48 */     return new ThreadPolicyImpl(this.value);
/*    */   }
/*    */ 
/*    */   public void destroy() {
/* 52 */     this.value = null;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 59 */     return "ThreadPolicy[" + (this.value.value() == 1 ? "SINGLE_THREAD_MODEL" : "ORB_CTRL_MODEL]");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.ThreadPolicyImpl
 * JD-Core Version:    0.6.2
 */