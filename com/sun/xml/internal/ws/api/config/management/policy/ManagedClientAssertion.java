/*    */ package com.sun.xml.internal.ws.api.config.management.policy;
/*    */ 
/*    */ import com.sun.istack.internal.logging.Logger;
/*    */ import com.sun.xml.internal.ws.api.client.WSPortInfo;
/*    */ import com.sun.xml.internal.ws.policy.PolicyAssertion;
/*    */ import com.sun.xml.internal.ws.policy.PolicyMap;
/*    */ import com.sun.xml.internal.ws.policy.sourcemodel.AssertionData;
/*    */ import com.sun.xml.internal.ws.policy.spi.AssertionCreationException;
/*    */ import com.sun.xml.internal.ws.resources.ManagementMessages;
/*    */ import java.util.Collection;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ 
/*    */ public class ManagedClientAssertion extends ManagementAssertion
/*    */ {
/* 48 */   public static final QName MANAGED_CLIENT_QNAME = new QName("http://java.sun.com/xml/ns/metro/management", "ManagedClient");
/*    */ 
/* 51 */   private static final Logger LOGGER = Logger.getLogger(ManagedClientAssertion.class);
/*    */ 
/*    */   public static ManagedClientAssertion getAssertion(WSPortInfo portInfo)
/*    */     throws WebServiceException
/*    */   {
/* 61 */     LOGGER.entering(new Object[] { portInfo });
/*    */ 
/* 65 */     PolicyMap policyMap = portInfo.getPolicyMap();
/* 66 */     ManagedClientAssertion assertion = (ManagedClientAssertion)ManagementAssertion.getAssertion(MANAGED_CLIENT_QNAME, policyMap, portInfo.getServiceName(), portInfo.getPortName(), ManagedClientAssertion.class);
/*    */ 
/* 68 */     LOGGER.exiting(assertion);
/* 69 */     return assertion;
/*    */   }
/*    */ 
/*    */   public ManagedClientAssertion(AssertionData data, Collection<PolicyAssertion> assertionParameters) throws AssertionCreationException
/*    */   {
/* 74 */     super(MANAGED_CLIENT_QNAME, data, assertionParameters);
/*    */   }
/*    */ 
/*    */   public boolean isManagementEnabled()
/*    */   {
/* 83 */     String management = getAttributeValue(MANAGEMENT_ATTRIBUTE_QNAME);
/* 84 */     if ((management != null) && (
/* 85 */       (management.trim().toLowerCase().equals("on")) || (Boolean.parseBoolean(management)))) {
/* 86 */       LOGGER.warning(ManagementMessages.WSM_1006_CLIENT_MANAGEMENT_ENABLED());
/*    */     }
/*    */ 
/* 89 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.config.management.policy.ManagedClientAssertion
 * JD-Core Version:    0.6.2
 */