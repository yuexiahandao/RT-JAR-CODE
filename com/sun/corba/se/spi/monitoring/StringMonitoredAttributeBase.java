/*    */ package com.sun.corba.se.spi.monitoring;
/*    */ 
/*    */ public abstract class StringMonitoredAttributeBase extends MonitoredAttributeBase
/*    */ {
/*    */   public StringMonitoredAttributeBase(String paramString1, String paramString2)
/*    */   {
/* 64 */     super(paramString1);
/* 65 */     MonitoredAttributeInfoFactory localMonitoredAttributeInfoFactory = MonitoringFactories.getMonitoredAttributeInfoFactory();
/*    */ 
/* 67 */     MonitoredAttributeInfo localMonitoredAttributeInfo = localMonitoredAttributeInfoFactory.createMonitoredAttributeInfo(paramString2, String.class, false, false);
/*    */ 
/* 69 */     setMonitoredAttributeInfo(localMonitoredAttributeInfo);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.monitoring.StringMonitoredAttributeBase
 * JD-Core Version:    0.6.2
 */