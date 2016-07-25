/*     */ package com.sun.jmx.snmp.daemon;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.util.Vector;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ class SendQ extends Vector<SnmpInformRequest>
/*     */ {
/* 217 */   boolean isBeingDestroyed = false;
/*     */ 
/*     */   SendQ(int paramInt1, int paramInt2)
/*     */   {
/* 118 */     super(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private synchronized void notifyClients() {
/* 122 */     notifyAll();
/*     */   }
/*     */ 
/*     */   public synchronized void addRequest(SnmpInformRequest paramSnmpInformRequest)
/*     */   {
/* 127 */     long l = paramSnmpInformRequest.getAbsNextPollTime();
/*     */ 
/* 130 */     for (int i = size(); (i > 0) && 
/* 131 */       (l >= getRequestAt(i - 1).getAbsNextPollTime()); i--);
/* 134 */     if (i == size()) {
/* 135 */       addElement(paramSnmpInformRequest);
/* 136 */       notifyClients();
/*     */     } else {
/* 138 */       insertElementAt(paramSnmpInformRequest, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized boolean waitUntilReady() {
/*     */     while (true) {
/* 144 */       if (this.isBeingDestroyed == true)
/* 145 */         return false;
/* 146 */       long l1 = 0L;
/* 147 */       if (!isEmpty()) {
/* 148 */         long l2 = System.currentTimeMillis();
/* 149 */         SnmpInformRequest localSnmpInformRequest = (SnmpInformRequest)lastElement();
/* 150 */         l1 = localSnmpInformRequest.getAbsNextPollTime() - l2;
/* 151 */         if (l1 <= 0L) {
/* 152 */           return true;
/*     */         }
/*     */       }
/* 155 */       waitOnThisQueue(l1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized Vector getAllOutstandingRequest(long paramLong)
/*     */   {
/* 161 */     Vector localVector = new Vector();
/*     */ 
/* 163 */     while (waitUntilReady() == true) {
/* 164 */       long l = System.currentTimeMillis() + paramLong;
/*     */ 
/* 166 */       for (int i = size(); i > 0; i--) {
/* 167 */         SnmpInformRequest localSnmpInformRequest = getRequestAt(i - 1);
/* 168 */         if (localSnmpInformRequest.getAbsNextPollTime() > l)
/*     */           break;
/* 170 */         localVector.addElement(localSnmpInformRequest);
/*     */       }
/*     */ 
/* 173 */       if (!localVector.isEmpty()) {
/* 174 */         this.elementCount -= localVector.size();
/* 175 */         return localVector;
/*     */       }
/*     */     }
/*     */ 
/* 179 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized void waitOnThisQueue(long paramLong)
/*     */   {
/* 184 */     if ((paramLong == 0L) && (!isEmpty()) && 
/* 185 */       (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))) {
/* 186 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpQManager.class.getName(), "waitOnThisQueue", "[" + Thread.currentThread().toString() + "]:" + "Fatal BUG :: Blocking on newq permenantly. But size = " + size());
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 193 */       wait(paramLong);
/*     */     } catch (InterruptedException localInterruptedException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   public SnmpInformRequest getRequestAt(int paramInt) {
/* 199 */     return (SnmpInformRequest)elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized SnmpInformRequest removeRequest(long paramLong) {
/* 203 */     int i = size();
/* 204 */     for (int j = 0; j < i; j++) {
/* 205 */       SnmpInformRequest localSnmpInformRequest = getRequestAt(j);
/* 206 */       if (paramLong == localSnmpInformRequest.getRequestId()) {
/* 207 */         removeElementAt(j);
/* 208 */         return localSnmpInformRequest;
/*     */       }
/*     */     }
/* 211 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.daemon.SendQ
 * JD-Core Version:    0.6.2
 */