/*    */ package com.sun.corba.se.impl.monitoring;
/*    */ 
/*    */ import com.sun.corba.se.spi.monitoring.MonitoredObject;
/*    */ import com.sun.corba.se.spi.monitoring.MonitoredObjectFactory;
/*    */ 
/*    */ public class MonitoredObjectFactoryImpl
/*    */   implements MonitoredObjectFactory
/*    */ {
/*    */   public MonitoredObject createMonitoredObject(String paramString1, String paramString2)
/*    */   {
/* 36 */     return new MonitoredObjectImpl(paramString1, paramString2);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.monitoring.MonitoredObjectFactoryImpl
 * JD-Core Version:    0.6.2
 */