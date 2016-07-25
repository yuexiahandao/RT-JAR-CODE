/*     */ package com.sun.corba.se.spi.monitoring;
/*     */ 
/*     */ import com.sun.corba.se.impl.monitoring.MonitoredAttributeInfoFactoryImpl;
/*     */ import com.sun.corba.se.impl.monitoring.MonitoredObjectFactoryImpl;
/*     */ import com.sun.corba.se.impl.monitoring.MonitoringManagerFactoryImpl;
/*     */ 
/*     */ public class MonitoringFactories
/*     */ {
/*  47 */   private static final MonitoredObjectFactoryImpl monitoredObjectFactory = new MonitoredObjectFactoryImpl();
/*     */ 
/*  50 */   private static final MonitoredAttributeInfoFactoryImpl monitoredAttributeInfoFactory = new MonitoredAttributeInfoFactoryImpl();
/*     */ 
/*  52 */   private static final MonitoringManagerFactoryImpl monitoringManagerFactory = new MonitoringManagerFactoryImpl();
/*     */ 
/*     */   public static MonitoredObjectFactory getMonitoredObjectFactory()
/*     */   {
/*  69 */     return monitoredObjectFactory;
/*     */   }
/*     */ 
/*     */   public static MonitoredAttributeInfoFactory getMonitoredAttributeInfoFactory()
/*     */   {
/*  91 */     return monitoredAttributeInfoFactory;
/*     */   }
/*     */ 
/*     */   public static MonitoringManagerFactory getMonitoringManagerFactory()
/*     */   {
/* 109 */     return monitoringManagerFactory;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.monitoring.MonitoringFactories
 * JD-Core Version:    0.6.2
 */