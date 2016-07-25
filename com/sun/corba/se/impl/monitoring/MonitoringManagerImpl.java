/*    */ package com.sun.corba.se.impl.monitoring;
/*    */ 
/*    */ import com.sun.corba.se.spi.monitoring.MonitoredObject;
/*    */ import com.sun.corba.se.spi.monitoring.MonitoredObjectFactory;
/*    */ import com.sun.corba.se.spi.monitoring.MonitoringFactories;
/*    */ import com.sun.corba.se.spi.monitoring.MonitoringManager;
/*    */ import com.sun.corba.se.spi.monitoring.MonitoringManagerFactory;
/*    */ 
/*    */ public class MonitoringManagerImpl
/*    */   implements MonitoringManager
/*    */ {
/*    */   private final MonitoredObject rootMonitoredObject;
/*    */ 
/*    */   MonitoringManagerImpl(String paramString1, String paramString2)
/*    */   {
/* 38 */     MonitoredObjectFactory localMonitoredObjectFactory = MonitoringFactories.getMonitoredObjectFactory();
/*    */ 
/* 40 */     this.rootMonitoredObject = localMonitoredObjectFactory.createMonitoredObject(paramString1, paramString2);
/*    */   }
/*    */ 
/*    */   public void clearState()
/*    */   {
/* 45 */     this.rootMonitoredObject.clearState();
/*    */   }
/*    */ 
/*    */   public MonitoredObject getRootMonitoredObject() {
/* 49 */     return this.rootMonitoredObject;
/*    */   }
/*    */ 
/*    */   public void close() {
/* 53 */     MonitoringManagerFactory localMonitoringManagerFactory = MonitoringFactories.getMonitoringManagerFactory();
/*    */ 
/* 55 */     localMonitoringManagerFactory.remove(this.rootMonitoredObject.getName());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.monitoring.MonitoringManagerImpl
 * JD-Core Version:    0.6.2
 */