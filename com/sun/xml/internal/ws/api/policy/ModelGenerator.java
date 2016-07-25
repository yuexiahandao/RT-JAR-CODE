/*    */ package com.sun.xml.internal.ws.api.policy;
/*    */ 
/*    */ import com.sun.xml.internal.ws.policy.Policy;
/*    */ import com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelGenerator;
/*    */ import com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelGenerator.PolicySourceModelCreator;
/*    */ import com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel;
/*    */ 
/*    */ public abstract class ModelGenerator extends PolicyModelGenerator
/*    */ {
/* 38 */   private static final SourceModelCreator CREATOR = new SourceModelCreator();
/*    */ 
/*    */   public static PolicyModelGenerator getGenerator()
/*    */   {
/* 53 */     return PolicyModelGenerator.getCompactGenerator(CREATOR);
/*    */   }
/*    */ 
/*    */   protected static class SourceModelCreator extends PolicyModelGenerator.PolicySourceModelCreator
/*    */   {
/*    */     protected PolicySourceModel create(Policy policy)
/*    */     {
/* 61 */       return SourceModel.createPolicySourceModel(policy.getNamespaceVersion(), policy.getId(), policy.getName());
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.policy.ModelGenerator
 * JD-Core Version:    0.6.2
 */