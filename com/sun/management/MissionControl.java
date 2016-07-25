/*    */ package com.sun.management;
/*    */ 
/*    */ import com.oracle.jrockit.jfr.FlightRecorder;
/*    */ import com.sun.jmx.mbeanserver.Util;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedActionException;
/*    */ import java.security.PrivilegedExceptionAction;
/*    */ import javax.management.MBeanServer;
/*    */ import javax.management.MalformedObjectNameException;
/*    */ import javax.management.ObjectName;
/*    */ import javax.management.StandardMBean;
/*    */ 
/*    */ public final class MissionControl extends StandardMBean
/*    */   implements MissionControlMXBean
/*    */ {
/* 30 */   private static final ObjectName MBEAN_NAME = Util.newObjectName("com.sun.management:type=MissionControl");
/*    */   private MBeanServer server;
/*    */ 
/*    */   public MissionControl()
/*    */   {
/* 39 */     super(MissionControlMXBean.class, true);
/*    */   }
/*    */ 
/*    */   public ObjectName preRegister(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*    */     throws Exception
/*    */   {
/* 45 */     this.server = paramMBeanServer;
/* 46 */     return MBEAN_NAME;
/*    */   }
/*    */ 
/*    */   public void unregisterMBeans()
/*    */   {
/* 51 */     doPrivileged(new PrivilegedExceptionAction() {
/*    */       public Void run() {
/* 53 */         FlightRecorder.unregisterWithMBeanServer(MissionControl.this.server);
/* 54 */         return null;
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   public void registerMBeans()
/*    */   {
/* 61 */     doPrivileged(new PrivilegedExceptionAction() {
/*    */       public Void run() throws MalformedObjectNameException {
/* 63 */         if (!MissionControl.this.server.isRegistered(new ObjectName("com.oracle.jrockit:type=FlightRecorder")))
/*    */         {
/* 66 */           if (FlightRecorder.isActive()) {
/* 67 */             FlightRecorder.registerWithMBeanServer(MissionControl.this.server);
/*    */           }
/*    */         }
/* 70 */         return null;
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   private void doPrivileged(PrivilegedExceptionAction<Void> paramPrivilegedExceptionAction) {
/*    */     try {
/* 77 */       AccessController.doPrivileged(paramPrivilegedExceptionAction);
/*    */     }
/*    */     catch (PrivilegedActionException localPrivilegedActionException)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.management.MissionControl
 * JD-Core Version:    0.6.2
 */