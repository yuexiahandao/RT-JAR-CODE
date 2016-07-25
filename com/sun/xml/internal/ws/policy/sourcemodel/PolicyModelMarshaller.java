/*    */ package com.sun.xml.internal.ws.policy.sourcemodel;
/*    */ 
/*    */ import com.sun.xml.internal.ws.policy.PolicyException;
/*    */ import java.util.Collection;
/*    */ 
/*    */ public abstract class PolicyModelMarshaller
/*    */ {
/* 38 */   private static final PolicyModelMarshaller defaultXmlMarshaller = new XmlPolicyModelMarshaller(false);
/* 39 */   private static final PolicyModelMarshaller invisibleAssertionXmlMarshaller = new XmlPolicyModelMarshaller(true);
/*    */ 
/*    */   public abstract void marshal(PolicySourceModel paramPolicySourceModel, Object paramObject)
/*    */     throws PolicyException;
/*    */ 
/*    */   public abstract void marshal(Collection<PolicySourceModel> paramCollection, Object paramObject)
/*    */     throws PolicyException;
/*    */ 
/*    */   public static PolicyModelMarshaller getXmlMarshaller(boolean marshallInvisible)
/*    */   {
/* 78 */     return marshallInvisible ? invisibleAssertionXmlMarshaller : defaultXmlMarshaller;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelMarshaller
 * JD-Core Version:    0.6.2
 */