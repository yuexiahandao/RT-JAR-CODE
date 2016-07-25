/*     */ package com.sun.corba.se.spi.monitoring;
/*     */ 
/*     */ public abstract class MonitoredAttributeBase
/*     */   implements MonitoredAttribute
/*     */ {
/*     */   String name;
/*     */   MonitoredAttributeInfo attributeInfo;
/*     */ 
/*     */   public MonitoredAttributeBase(String paramString, MonitoredAttributeInfo paramMonitoredAttributeInfo)
/*     */   {
/*  48 */     this.name = paramString;
/*  49 */     this.attributeInfo = paramMonitoredAttributeInfo;
/*     */   }
/*     */ 
/*     */   MonitoredAttributeBase(String paramString)
/*     */   {
/*  57 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   void setMonitoredAttributeInfo(MonitoredAttributeInfo paramMonitoredAttributeInfo)
/*     */   {
/*  66 */     this.attributeInfo = paramMonitoredAttributeInfo;
/*     */   }
/*     */ 
/*     */   public void clearState()
/*     */   {
/*     */   }
/*     */ 
/*     */   public abstract Object getValue();
/*     */ 
/*     */   public void setValue(Object paramObject)
/*     */   {
/*  92 */     if (!this.attributeInfo.isWritable()) {
/*  93 */       throw new IllegalStateException("The Attribute " + this.name + " is not Writable...");
/*     */     }
/*     */ 
/*  96 */     throw new IllegalStateException("The method implementation is not provided for the attribute " + this.name);
/*     */   }
/*     */ 
/*     */   public MonitoredAttributeInfo getAttributeInfo()
/*     */   {
/* 106 */     return this.attributeInfo;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 113 */     return this.name;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.monitoring.MonitoredAttributeBase
 * JD-Core Version:    0.6.2
 */