/*     */ package com.sun.corba.se.spi.monitoring;
/*     */ 
/*     */ public class StatisticMonitoredAttribute extends MonitoredAttributeBase
/*     */ {
/*     */   private StatisticsAccumulator statisticsAccumulator;
/*     */   private Object mutex;
/*     */ 
/*     */   public StatisticMonitoredAttribute(String paramString1, String paramString2, StatisticsAccumulator paramStatisticsAccumulator, Object paramObject)
/*     */   {
/*  88 */     super(paramString1);
/*  89 */     MonitoredAttributeInfoFactory localMonitoredAttributeInfoFactory = MonitoringFactories.getMonitoredAttributeInfoFactory();
/*     */ 
/*  91 */     MonitoredAttributeInfo localMonitoredAttributeInfo = localMonitoredAttributeInfoFactory.createMonitoredAttributeInfo(paramString2, String.class, false, true);
/*     */ 
/*  94 */     setMonitoredAttributeInfo(localMonitoredAttributeInfo);
/*  95 */     this.statisticsAccumulator = paramStatisticsAccumulator;
/*  96 */     this.mutex = paramObject;
/*     */   }
/*     */ 
/*     */   public Object getValue()
/*     */   {
/* 107 */     synchronized (this.mutex) {
/* 108 */       return this.statisticsAccumulator.getValue();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clearState()
/*     */   {
/* 117 */     synchronized (this.mutex) {
/* 118 */       this.statisticsAccumulator.clearState();
/*     */     }
/*     */   }
/*     */ 
/*     */   public StatisticsAccumulator getStatisticsAccumulator()
/*     */   {
/* 128 */     return this.statisticsAccumulator;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.monitoring.StatisticMonitoredAttribute
 * JD-Core Version:    0.6.2
 */