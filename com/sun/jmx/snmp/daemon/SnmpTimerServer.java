/*    */ package com.sun.jmx.snmp.daemon;
/*    */ 
/*    */ import com.sun.jmx.defaults.JmxProperties;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ final class SnmpTimerServer extends Thread
/*    */ {
/* 23 */   private SnmpInformRequest req = null;
/*    */ 
/* 25 */   SnmpQManager snmpq = null;
/*    */ 
/* 30 */   boolean isBeingDestroyed = false;
/*    */ 
/*    */   public SnmpTimerServer(ThreadGroup paramThreadGroup, SnmpQManager paramSnmpQManager)
/*    */   {
/* 36 */     super(paramThreadGroup, "SnmpTimerServer");
/* 37 */     setName("SnmpTimerServer");
/* 38 */     this.snmpq = paramSnmpQManager;
/* 39 */     start();
/*    */   }
/*    */ 
/*    */   public synchronized void stopTimerServer()
/*    */   {
/* 44 */     if (isAlive()) {
/* 45 */       interrupt();
/*    */       try
/*    */       {
/* 49 */         join();
/*    */       }
/*    */       catch (InterruptedException localInterruptedException) {
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   public void run() {
/* 57 */     Thread.currentThread().setPriority(5);
/*    */ 
/* 59 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 60 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpTimerServer.class.getName(), "run", "Timer Thread started");
/*    */     }
/*    */ 
/*    */     while (true)
/*    */     {
/*    */       try
/*    */       {
/* 67 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 68 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpTimerServer.class.getName(), "run", "Blocking for inform requests");
/*    */         }
/*    */ 
/* 71 */         if (this.req == null) {
/* 72 */           this.req = this.snmpq.getTimeoutRequests();
/*    */         }
/* 74 */         if ((this.req != null) && (this.req.inProgress())) {
/* 75 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 76 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpTimerServer.class.getName(), "run", "Handle timeout inform request " + this.req.getRequestId());
/*    */           }
/*    */ 
/* 79 */           this.req.action();
/* 80 */           this.req = null;
/*    */         }
/* 82 */         if (this.isBeingDestroyed == true) break;
/*    */       }
/*    */       catch (Exception localException) {
/* 85 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/* 86 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpTimerServer.class.getName(), "run", "Got unexpected exception", localException);
/*    */       }
/*    */       catch (ThreadDeath localThreadDeath)
/*    */       {
/* 90 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 91 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpTimerServer.class.getName(), "run", "ThreadDeath, timer server unexpectedly shutting down", localThreadDeath);
/*    */         }
/*    */ 
/* 94 */         throw localThreadDeath;
/*    */       } catch (OutOfMemoryError localOutOfMemoryError) {
/* 96 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 97 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpTimerServer.class.getName(), "run", "OutOfMemoryError", localOutOfMemoryError);
/*    */         }
/*    */ 
/* 100 */         yield();
/*    */       } catch (Error localError) {
/* 102 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/* 103 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpTimerServer.class.getName(), "run", "Received Internal error", localError);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.daemon.SnmpTimerServer
 * JD-Core Version:    0.6.2
 */