/*    */ package com.sun.xml.internal.ws.policy;
/*    */ 
/*    */ import com.sun.xml.internal.ws.policy.sourcemodel.AssertionData;
/*    */ import java.util.Collection;
/*    */ 
/*    */ public abstract class SimpleAssertion extends PolicyAssertion
/*    */ {
/*    */   protected SimpleAssertion()
/*    */   {
/*    */   }
/*    */ 
/*    */   protected SimpleAssertion(AssertionData data, Collection<? extends PolicyAssertion> assertionParameters)
/*    */   {
/* 43 */     super(data, assertionParameters);
/*    */   }
/*    */ 
/*    */   public final boolean hasNestedPolicy()
/*    */   {
/* 48 */     return false;
/*    */   }
/*    */ 
/*    */   public final NestedPolicy getNestedPolicy()
/*    */   {
/* 53 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.SimpleAssertion
 * JD-Core Version:    0.6.2
 */