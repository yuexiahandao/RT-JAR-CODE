/*    */ package com.sun.corba.se.impl.monitoring;
/*    */ 
/*    */ import com.sun.corba.se.spi.monitoring.MonitoringManager;
/*    */ import com.sun.corba.se.spi.monitoring.MonitoringManagerFactory;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class MonitoringManagerFactoryImpl
/*    */   implements MonitoringManagerFactory
/*    */ {
/* 34 */   private HashMap monitoringManagerTable = new HashMap();
/*    */ 
/*    */   public synchronized MonitoringManager createMonitoringManager(String paramString1, String paramString2)
/*    */   {
/* 39 */     MonitoringManagerImpl localMonitoringManagerImpl = null;
/* 40 */     localMonitoringManagerImpl = (MonitoringManagerImpl)this.monitoringManagerTable.get(paramString1);
/* 41 */     if (localMonitoringManagerImpl == null) {
/* 42 */       localMonitoringManagerImpl = new MonitoringManagerImpl(paramString1, paramString2);
/* 43 */       this.monitoringManagerTable.put(paramString1, localMonitoringManagerImpl);
/*    */     }
/* 45 */     return localMonitoringManagerImpl;
/*    */   }
/*    */ 
/*    */   public synchronized void remove(String paramString) {
/* 49 */     this.monitoringManagerTable.remove(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.monitoring.MonitoringManagerFactoryImpl
 * JD-Core Version:    0.6.2
 */