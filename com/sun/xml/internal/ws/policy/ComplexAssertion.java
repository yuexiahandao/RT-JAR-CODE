/*    */ package com.sun.xml.internal.ws.policy;
/*    */ 
/*    */ import com.sun.xml.internal.ws.policy.sourcemodel.AssertionData;
/*    */ import java.util.Collection;
/*    */ 
/*    */ public abstract class ComplexAssertion extends PolicyAssertion
/*    */ {
/*    */   private final NestedPolicy nestedPolicy;
/*    */ 
/*    */   protected ComplexAssertion()
/*    */   {
/* 44 */     this.nestedPolicy = NestedPolicy.createNestedPolicy(AssertionSet.emptyAssertionSet());
/*    */   }
/*    */ 
/*    */   protected ComplexAssertion(AssertionData data, Collection<? extends PolicyAssertion> assertionParameters, AssertionSet nestedAlternative) {
/* 48 */     super(data, assertionParameters);
/*    */ 
/* 50 */     AssertionSet nestedSet = nestedAlternative != null ? nestedAlternative : AssertionSet.emptyAssertionSet();
/* 51 */     this.nestedPolicy = NestedPolicy.createNestedPolicy(nestedSet);
/*    */   }
/*    */ 
/*    */   public final boolean hasNestedPolicy()
/*    */   {
/* 56 */     return true;
/*    */   }
/*    */ 
/*    */   public final NestedPolicy getNestedPolicy()
/*    */   {
/* 61 */     return this.nestedPolicy;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.ComplexAssertion
 * JD-Core Version:    0.6.2
 */