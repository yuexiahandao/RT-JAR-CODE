/*     */ package com.sun.jmx.snmp.daemon;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ final class SnmpSendServer extends Thread
/*     */ {
/*  30 */   private int intervalRange = 5000;
/*     */   private Vector readyPool;
/*  33 */   SnmpQManager snmpq = null;
/*     */ 
/*  38 */   boolean isBeingDestroyed = false;
/*     */ 
/*     */   public SnmpSendServer(ThreadGroup paramThreadGroup, SnmpQManager paramSnmpQManager)
/*     */   {
/*  44 */     super(paramThreadGroup, "SnmpSendServer");
/*  45 */     this.snmpq = paramSnmpQManager;
/*  46 */     start();
/*     */   }
/*     */ 
/*     */   public synchronized void stopSendServer()
/*     */   {
/*  51 */     if (isAlive()) {
/*  52 */       interrupt();
/*     */       try
/*     */       {
/*  56 */         join();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void run() {
/*  64 */     Thread.currentThread().setPriority(5);
/*     */ 
/*  66 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER))
/*  67 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSendServer.class.getName(), "run", "Thread Started");
/*     */     try
/*     */     {
/*     */       while (true)
/*     */       {
/*  72 */         prepareAndSendRequest();
/*  73 */         if (this.isBeingDestroyed == true) break;
/*     */       }
/*     */     } catch (Exception localException) {
/*     */       while (true) if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  77 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSendServer.class.getName(), "run", "Exception in send server", localException);
/*     */         }
/*     */ 
/*     */     }
/*     */     catch (ThreadDeath localThreadDeath)
/*     */     {
/*  83 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  84 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSendServer.class.getName(), "run", "Exiting... Fatal error");
/*     */       }
/*     */ 
/*  87 */       throw localThreadDeath;
/*     */     } catch (OutOfMemoryError localOutOfMemoryError) {
/*     */       while (true) if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/*  90 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSendServer.class.getName(), "run", "Out of memory");
/*     */     }
/*     */     catch (Error localError)
/*     */     {
/*  94 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  95 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSendServer.class.getName(), "run", "Got unexpected error", localError);
/*     */       }
/*     */ 
/*  98 */       throw localError;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void prepareAndSendRequest()
/*     */   {
/* 105 */     if ((this.readyPool == null) || (this.readyPool.isEmpty()))
/*     */     {
/* 107 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 108 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSendServer.class.getName(), "prepareAndSendRequest", "Blocking for inform requests");
/*     */       }
/*     */ 
/* 111 */       this.readyPool = this.snmpq.getAllOutstandingRequest(this.intervalRange);
/* 112 */       if (this.isBeingDestroyed != true);
/*     */     }
/* 115 */     else if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 116 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSendServer.class.getName(), "prepareAndSendRequest", "Inform requests from a previous block left unprocessed. Will try again");
/*     */     }
/*     */ 
/* 121 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 122 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSendServer.class.getName(), "prepareAndSendRequest", "List of inform requests to send : " + reqListToString(this.readyPool));
/*     */     }
/*     */ 
/* 126 */     synchronized (this) {
/* 127 */       if (this.readyPool.size() < 2)
/*     */       {
/* 129 */         fireRequestList(this.readyPool);
/* 130 */         return;
/*     */       }
/*     */ 
/* 133 */       while (!this.readyPool.isEmpty()) {
/* 134 */         SnmpInformRequest localSnmpInformRequest = (SnmpInformRequest)this.readyPool.lastElement();
/* 135 */         if ((localSnmpInformRequest != null) && (localSnmpInformRequest.inProgress())) {
/* 136 */           fireRequest(localSnmpInformRequest);
/*     */         }
/* 138 */         this.readyPool.removeElementAt(this.readyPool.size() - 1);
/*     */       }
/* 140 */       this.readyPool.removeAllElements();
/*     */     }
/*     */   }
/*     */ 
/*     */   void fireRequest(SnmpInformRequest paramSnmpInformRequest)
/*     */   {
/* 148 */     if ((paramSnmpInformRequest != null) && (paramSnmpInformRequest.inProgress())) {
/* 149 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 150 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSendServer.class.getName(), "fireRequest", "Firing inform request directly. -> " + paramSnmpInformRequest.getRequestId());
/*     */       }
/*     */ 
/* 153 */       paramSnmpInformRequest.action();
/*     */     }
/*     */   }
/*     */ 
/*     */   void fireRequestList(Vector paramVector)
/*     */   {
/* 159 */     while (!paramVector.isEmpty()) {
/* 160 */       SnmpInformRequest localSnmpInformRequest = (SnmpInformRequest)paramVector.lastElement();
/* 161 */       if ((localSnmpInformRequest != null) && (localSnmpInformRequest.inProgress()))
/* 162 */         fireRequest(localSnmpInformRequest);
/* 163 */       paramVector.removeElementAt(paramVector.size() - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   final String reqListToString(Vector paramVector) {
/* 168 */     StringBuffer localStringBuffer = new StringBuffer(paramVector.size() * 100);
/*     */ 
/* 170 */     Enumeration localEnumeration = paramVector.elements();
/* 171 */     while (localEnumeration.hasMoreElements()) {
/* 172 */       localObject = (SnmpInformRequest)localEnumeration.nextElement();
/* 173 */       localStringBuffer.append("InformRequestId -> ");
/* 174 */       localStringBuffer.append(((SnmpInformRequest)localObject).getRequestId());
/* 175 */       localStringBuffer.append(" / Destination -> ");
/* 176 */       localStringBuffer.append(((SnmpInformRequest)localObject).getAddress());
/* 177 */       localStringBuffer.append(". ");
/*     */     }
/* 179 */     Object localObject = localStringBuffer.toString();
/* 180 */     localStringBuffer = null;
/* 181 */     return localObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.daemon.SnmpSendServer
 * JD-Core Version:    0.6.2
 */