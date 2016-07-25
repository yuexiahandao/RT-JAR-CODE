/*    */ package com.sun.xml.internal.ws.policy.sourcemodel;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public final class PolicySourceModelContext
/*    */ {
/*    */   Map<URI, PolicySourceModel> policyModels;
/*    */ 
/*    */   private Map<URI, PolicySourceModel> getModels()
/*    */   {
/* 48 */     if (null == this.policyModels) {
/* 49 */       this.policyModels = new HashMap();
/*    */     }
/* 51 */     return this.policyModels;
/*    */   }
/*    */ 
/*    */   public void addModel(URI modelUri, PolicySourceModel model) {
/* 55 */     getModels().put(modelUri, model);
/*    */   }
/*    */ 
/*    */   public static PolicySourceModelContext createContext() {
/* 59 */     return new PolicySourceModelContext();
/*    */   }
/*    */ 
/*    */   public boolean containsModel(URI modelUri) {
/* 63 */     return getModels().containsKey(modelUri);
/*    */   }
/*    */ 
/*    */   PolicySourceModel retrieveModel(URI modelUri) {
/* 67 */     return (PolicySourceModel)getModels().get(modelUri);
/*    */   }
/*    */ 
/*    */   PolicySourceModel retrieveModel(URI modelUri, URI digestAlgorithm, String digest)
/*    */   {
/* 72 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 77 */     return "PolicySourceModelContext: policyModels = " + this.policyModels;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModelContext
 * JD-Core Version:    0.6.2
 */