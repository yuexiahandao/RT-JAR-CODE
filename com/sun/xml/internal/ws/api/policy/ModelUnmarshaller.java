/*    */ package com.sun.xml.internal.ws.api.policy;
/*    */ 
/*    */ import com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel;
/*    */ import com.sun.xml.internal.ws.policy.sourcemodel.XmlPolicyModelUnmarshaller;
/*    */ import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
/*    */ 
/*    */ public class ModelUnmarshaller extends XmlPolicyModelUnmarshaller
/*    */ {
/* 38 */   private static final ModelUnmarshaller INSTANCE = new ModelUnmarshaller();
/*    */ 
/*    */   public static ModelUnmarshaller getUnmarshaller()
/*    */   {
/* 53 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   protected PolicySourceModel createSourceModel(NamespaceVersion nsVersion, String id, String name)
/*    */   {
/* 58 */     return SourceModel.createSourceModel(nsVersion, id, name);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.policy.ModelUnmarshaller
 * JD-Core Version:    0.6.2
 */