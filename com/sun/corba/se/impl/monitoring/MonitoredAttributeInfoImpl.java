/*    */ package com.sun.corba.se.impl.monitoring;
/*    */ 
/*    */ import com.sun.corba.se.spi.monitoring.MonitoredAttributeInfo;
/*    */ 
/*    */ public class MonitoredAttributeInfoImpl
/*    */   implements MonitoredAttributeInfo
/*    */ {
/*    */   private final String description;
/*    */   private final Class type;
/*    */   private final boolean writableFlag;
/*    */   private final boolean statisticFlag;
/*    */ 
/*    */   MonitoredAttributeInfoImpl(String paramString, Class paramClass, boolean paramBoolean1, boolean paramBoolean2)
/*    */   {
/* 38 */     this.description = paramString;
/* 39 */     this.type = paramClass;
/* 40 */     this.writableFlag = paramBoolean1;
/* 41 */     this.statisticFlag = paramBoolean2;
/*    */   }
/*    */ 
/*    */   public String getDescription() {
/* 45 */     return this.description;
/*    */   }
/*    */ 
/*    */   public Class type() {
/* 49 */     return this.type;
/*    */   }
/*    */ 
/*    */   public boolean isWritable() {
/* 53 */     return this.writableFlag;
/*    */   }
/*    */ 
/*    */   public boolean isStatistic() {
/* 57 */     return this.statisticFlag;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.monitoring.MonitoredAttributeInfoImpl
 * JD-Core Version:    0.6.2
 */