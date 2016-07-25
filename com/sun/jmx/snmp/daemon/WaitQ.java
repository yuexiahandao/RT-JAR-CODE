/*     */ package com.sun.jmx.snmp.daemon;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.util.Vector;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ class WaitQ extends Vector<SnmpInformRequest>
/*     */ {
/* 312 */   boolean isBeingDestroyed = false;
/*     */ 
/*     */   WaitQ(int paramInt1, int paramInt2)
/*     */   {
/* 227 */     super(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public synchronized void addWaiting(SnmpInformRequest paramSnmpInformRequest)
/*     */   {
/* 232 */     long l = paramSnmpInformRequest.getAbsMaxTimeToWait();
/*     */ 
/* 234 */     for (int i = size(); (i > 0) && 
/* 235 */       (l >= getRequestAt(i - 1).getAbsMaxTimeToWait()); i--);
/* 238 */     if (i == size()) {
/* 239 */       addElement(paramSnmpInformRequest);
/* 240 */       notifyClients();
/*     */     } else {
/* 242 */       insertElementAt(paramSnmpInformRequest, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized boolean waitUntilReady() {
/*     */     while (true) {
/* 248 */       if (this.isBeingDestroyed == true)
/* 249 */         return false;
/* 250 */       long l1 = 0L;
/* 251 */       if (!isEmpty()) {
/* 252 */         long l2 = System.currentTimeMillis();
/* 253 */         SnmpInformRequest localSnmpInformRequest = (SnmpInformRequest)lastElement();
/* 254 */         l1 = localSnmpInformRequest.getAbsMaxTimeToWait() - l2;
/* 255 */         if (l1 <= 0L) {
/* 256 */           return true;
/*     */         }
/*     */       }
/* 259 */       waitOnThisQueue(l1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized SnmpInformRequest getTimeoutRequests() {
/* 264 */     if (waitUntilReady() == true) {
/* 265 */       SnmpInformRequest localSnmpInformRequest = (SnmpInformRequest)lastElement();
/* 266 */       this.elementCount -= 1;
/* 267 */       return localSnmpInformRequest;
/*     */     }
/*     */ 
/* 270 */     return null;
/*     */   }
/*     */ 
/*     */   private synchronized void notifyClients()
/*     */   {
/* 275 */     notifyAll();
/*     */   }
/*     */ 
/*     */   public synchronized void waitOnThisQueue(long paramLong) {
/* 279 */     if ((paramLong == 0L) && (!isEmpty()) && 
/* 280 */       (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))) {
/* 281 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpQManager.class.getName(), "waitOnThisQueue", "[" + Thread.currentThread().toString() + "]:" + "Fatal BUG :: Blocking on waitq permenantly. But size = " + size());
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 288 */       wait(paramLong);
/*     */     } catch (InterruptedException localInterruptedException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   public SnmpInformRequest getRequestAt(int paramInt) {
/* 294 */     return (SnmpInformRequest)elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized SnmpInformRequest removeRequest(long paramLong) {
/* 298 */     int i = size();
/* 299 */     for (int j = 0; j < i; j++) {
/* 300 */       SnmpInformRequest localSnmpInformRequest = getRequestAt(j);
/* 301 */       if (paramLong == localSnmpInformRequest.getRequestId()) {
/* 302 */         removeElementAt(j);
/* 303 */         return localSnmpInformRequest;
/*     */       }
/*     */     }
/* 306 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.daemon.WaitQ
 * JD-Core Version:    0.6.2
 */