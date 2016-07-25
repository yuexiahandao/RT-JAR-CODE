/*     */ package com.sun.jmx.snmp.daemon;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import com.sun.jmx.snmp.SnmpDefinitions;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.SnmpVarBindList;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketException;
/*     */ import java.util.Collection;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Stack;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ class SnmpSession
/*     */   implements SnmpDefinitions, Runnable
/*     */ {
/*     */   protected transient SnmpAdaptorServer adaptor;
/*  57 */   protected transient SnmpSocket informSocket = null;
/*     */ 
/*  61 */   private transient Hashtable<SnmpInformRequest, SnmpInformRequest> informRequestList = new Hashtable();
/*     */ 
/*  67 */   private transient Stack<SnmpInformRequest> informRespq = new Stack();
/*     */ 
/*  75 */   private transient Thread myThread = null;
/*     */   private transient SnmpInformRequest syncInformReq;
/*  82 */   SnmpQManager snmpQman = null;
/*     */ 
/*  84 */   private boolean isBeingCancelled = false;
/*     */ 
/*     */   public SnmpSession(SnmpAdaptorServer paramSnmpAdaptorServer)
/*     */     throws SocketException
/*     */   {
/*  95 */     this.adaptor = paramSnmpAdaptorServer;
/*  96 */     this.snmpQman = new SnmpQManager();
/*  97 */     SnmpResponseHandler localSnmpResponseHandler = new SnmpResponseHandler(paramSnmpAdaptorServer, this.snmpQman);
/*  98 */     initialize(paramSnmpAdaptorServer, localSnmpResponseHandler);
/*     */   }
/*     */ 
/*     */   public SnmpSession()
/*     */     throws SocketException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected synchronized void initialize(SnmpAdaptorServer paramSnmpAdaptorServer, SnmpResponseHandler paramSnmpResponseHandler)
/*     */     throws SocketException
/*     */   {
/* 115 */     this.informSocket = new SnmpSocket(paramSnmpResponseHandler, paramSnmpAdaptorServer.getAddress(), paramSnmpAdaptorServer.getBufferSize().intValue());
/*     */ 
/* 117 */     this.myThread = new Thread(this, "SnmpSession");
/* 118 */     this.myThread.start();
/*     */   }
/*     */ 
/*     */   synchronized boolean isSessionActive()
/*     */   {
/* 127 */     return (this.adaptor.isActive()) && (this.myThread != null) && (this.myThread.isAlive());
/*     */   }
/*     */ 
/*     */   SnmpSocket getSocket()
/*     */   {
/* 135 */     return this.informSocket;
/*     */   }
/*     */ 
/*     */   SnmpQManager getSnmpQManager()
/*     */   {
/* 143 */     return this.snmpQman;
/*     */   }
/*     */ 
/*     */   private synchronized boolean syncInProgress()
/*     */   {
/* 151 */     return this.syncInformReq != null;
/*     */   }
/*     */ 
/*     */   private synchronized void setSyncMode(SnmpInformRequest paramSnmpInformRequest) {
/* 155 */     this.syncInformReq = paramSnmpInformRequest;
/*     */   }
/*     */ 
/*     */   private synchronized void resetSyncMode() {
/* 159 */     if (this.syncInformReq == null)
/* 160 */       return;
/* 161 */     this.syncInformReq = null;
/* 162 */     if (thisSessionContext())
/* 163 */       return;
/* 164 */     notifyAll();
/*     */   }
/*     */ 
/*     */   boolean thisSessionContext()
/*     */   {
/* 175 */     return Thread.currentThread() == this.myThread;
/*     */   }
/*     */ 
/*     */   SnmpInformRequest makeAsyncRequest(InetAddress paramInetAddress, String paramString, SnmpInformHandler paramSnmpInformHandler, SnmpVarBindList paramSnmpVarBindList, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 192 */     if (!isSessionActive()) {
/* 193 */       throw new SnmpStatusException("SNMP adaptor server not ONLINE");
/*     */     }
/* 195 */     SnmpInformRequest localSnmpInformRequest = new SnmpInformRequest(this, this.adaptor, paramInetAddress, paramString, paramInt, paramSnmpInformHandler);
/* 196 */     localSnmpInformRequest.start(paramSnmpVarBindList);
/* 197 */     return localSnmpInformRequest;
/*     */   }
/*     */ 
/*     */   void waitForResponse(SnmpInformRequest paramSnmpInformRequest, long paramLong)
/*     */   {
/* 207 */     if (!paramSnmpInformRequest.inProgress())
/* 208 */       return;
/* 209 */     setSyncMode(paramSnmpInformRequest);
/* 210 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/* 211 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSession.class.getName(), "waitForResponse", "Session switching to sync mode for inform request " + paramSnmpInformRequest.getRequestId());
/*     */     long l;
/* 215 */     if (paramLong <= 0L)
/* 216 */       l = System.currentTimeMillis() + 6000000L;
/*     */     else {
/* 218 */       l = System.currentTimeMillis() + paramLong;
/*     */     }
/* 220 */     while ((paramSnmpInformRequest.inProgress()) || (syncInProgress())) {
/* 221 */       paramLong = l - System.currentTimeMillis();
/* 222 */       if (paramLong <= 0L)
/*     */         break;
/* 224 */       synchronized (this) {
/* 225 */         if (!this.informRespq.removeElement(paramSnmpInformRequest)) {
/*     */           try {
/* 227 */             wait(paramLong);
/*     */           } catch (InterruptedException localInterruptedException) {
/*     */           }
/* 230 */           continue;
/*     */         }
/*     */       }
/*     */       try {
/* 234 */         processResponse(paramSnmpInformRequest);
/*     */       } catch (Exception localException) {
/* 236 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 237 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSession.class.getName(), "waitForResponse", "Got unexpected exception", localException);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 242 */     resetSyncMode();
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 252 */     this.myThread = Thread.currentThread();
/* 253 */     this.myThread.setPriority(5);
/*     */ 
/* 255 */     SnmpInformRequest localSnmpInformRequest = null;
/* 256 */     while (this.myThread != null) {
/*     */       try {
/* 258 */         localSnmpInformRequest = nextResponse();
/* 259 */         if (localSnmpInformRequest != null)
/* 260 */           processResponse(localSnmpInformRequest);
/*     */       }
/*     */       catch (ThreadDeath localThreadDeath) {
/* 263 */         this.myThread = null;
/* 264 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 265 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSession.class.getName(), "run", "ThreadDeath, session thread unexpectedly shutting down");
/*     */         }
/*     */ 
/* 268 */         throw localThreadDeath;
/*     */       }
/*     */     }
/* 271 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 272 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSession.class.getName(), "run", "Session thread shutting down");
/*     */     }
/*     */ 
/* 275 */     this.myThread = null;
/*     */   }
/*     */ 
/*     */   private void processResponse(SnmpInformRequest paramSnmpInformRequest)
/*     */   {
/* 280 */     while ((paramSnmpInformRequest != null) && (this.myThread != null))
/*     */       try {
/* 282 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 283 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSession.class.getName(), "processResponse", "Processing response to req = " + paramSnmpInformRequest.getRequestId());
/*     */         }
/*     */ 
/* 286 */         paramSnmpInformRequest.processResponse();
/* 287 */         paramSnmpInformRequest = null;
/*     */       } catch (Exception localException) {
/* 289 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 290 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSession.class.getName(), "processResponse", "Got unexpected exception", localException);
/*     */         }
/*     */ 
/* 293 */         paramSnmpInformRequest = null;
/*     */       } catch (OutOfMemoryError localOutOfMemoryError) {
/* 295 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 296 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSession.class.getName(), "processResponse", "Out of memory error in session thread", localOutOfMemoryError);
/*     */         }
/*     */ 
/* 299 */         Thread.currentThread(); Thread.yield();
/*     */       }
/*     */   }
/*     */ 
/*     */   synchronized void addInformRequest(SnmpInformRequest paramSnmpInformRequest)
/*     */     throws SnmpStatusException
/*     */   {
/* 317 */     if (!isSessionActive()) {
/* 318 */       throw new SnmpStatusException("SNMP adaptor is not ONLINE or session is dead...");
/*     */     }
/* 320 */     this.informRequestList.put(paramSnmpInformRequest, paramSnmpInformRequest);
/*     */   }
/*     */ 
/*     */   synchronized void removeInformRequest(SnmpInformRequest paramSnmpInformRequest)
/*     */   {
/* 330 */     if (!this.isBeingCancelled) {
/* 331 */       this.informRequestList.remove(paramSnmpInformRequest);
/*     */     }
/* 333 */     if ((this.syncInformReq != null) && (this.syncInformReq == paramSnmpInformRequest))
/* 334 */       resetSyncMode();
/*     */   }
/*     */ 
/*     */   private void cancelAllRequests()
/*     */   {
/*     */     SnmpInformRequest[] arrayOfSnmpInformRequest;
/* 344 */     synchronized (this)
/*     */     {
/* 346 */       if (this.informRequestList.isEmpty()) {
/* 347 */         return;
/*     */       }
/*     */ 
/* 350 */       this.isBeingCancelled = true;
/*     */ 
/* 352 */       arrayOfSnmpInformRequest = new SnmpInformRequest[this.informRequestList.size()];
/* 353 */       Iterator localIterator = this.informRequestList.values().iterator();
/* 354 */       int j = 0;
/* 355 */       while (localIterator.hasNext()) {
/* 356 */         SnmpInformRequest localSnmpInformRequest = (SnmpInformRequest)localIterator.next();
/* 357 */         arrayOfSnmpInformRequest[(j++)] = localSnmpInformRequest;
/* 358 */         localIterator.remove();
/*     */       }
/* 360 */       this.informRequestList.clear();
/*     */     }
/*     */ 
/* 363 */     for (int i = 0; i < arrayOfSnmpInformRequest.length; i++)
/* 364 */       arrayOfSnmpInformRequest[i].cancelRequest();
/*     */   }
/*     */ 
/*     */   void addResponse(SnmpInformRequest paramSnmpInformRequest)
/*     */   {
/* 375 */     SnmpInformRequest localSnmpInformRequest = paramSnmpInformRequest;
/* 376 */     if (isSessionActive()) {
/* 377 */       synchronized (this) {
/* 378 */         this.informRespq.push(paramSnmpInformRequest);
/* 379 */         notifyAll();
/*     */       }
/*     */     }
/* 382 */     else if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/* 383 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSession.class.getName(), "addResponse", "Adaptor not ONLINE or session thread dead, so inform response is dropped..." + paramSnmpInformRequest.getRequestId());
/*     */   }
/*     */ 
/*     */   private synchronized SnmpInformRequest nextResponse()
/*     */   {
/* 392 */     if (this.informRespq.isEmpty())
/*     */       try {
/* 394 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 395 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSession.class.getName(), "nextResponse", "Blocking for response");
/*     */         }
/*     */ 
/* 398 */         wait();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {
/*     */       }
/* 402 */     if (this.informRespq.isEmpty())
/* 403 */       return null;
/* 404 */     SnmpInformRequest localSnmpInformRequest = (SnmpInformRequest)this.informRespq.firstElement();
/* 405 */     this.informRespq.removeElementAt(0);
/* 406 */     return localSnmpInformRequest;
/*     */   }
/*     */ 
/*     */   private synchronized void cancelAllResponses() {
/* 410 */     if (this.informRespq != null) {
/* 411 */       this.syncInformReq = null;
/* 412 */       this.informRespq.removeAllElements();
/* 413 */       notifyAll();
/*     */     }
/*     */   }
/*     */ 
/*     */   final void destroySession()
/*     */   {
/* 423 */     cancelAllRequests();
/* 424 */     cancelAllResponses();
/* 425 */     synchronized (this) {
/* 426 */       this.informSocket.close();
/* 427 */       this.informSocket = null;
/*     */     }
/* 429 */     this.snmpQman.stopQThreads();
/* 430 */     this.snmpQman = null;
/* 431 */     killSessionThread();
/*     */   }
/*     */ 
/*     */   private synchronized void killSessionThread()
/*     */   {
/* 440 */     if ((this.myThread != null) && (this.myThread.isAlive())) {
/* 441 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 442 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSession.class.getName(), "killSessionThread", "Destroying session");
/*     */       }
/*     */ 
/* 445 */       if (!thisSessionContext()) {
/* 446 */         this.myThread = null;
/* 447 */         notifyAll();
/*     */       } else {
/* 449 */         this.myThread = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void finalize()
/*     */   {
/* 462 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 463 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSession.class.getName(), "finalize", "Shutting all servers");
/*     */     }
/*     */ 
/* 467 */     if (this.informRespq != null)
/* 468 */       this.informRespq.removeAllElements();
/* 469 */     this.informRespq = null;
/* 470 */     if (this.informSocket != null)
/* 471 */       this.informSocket.close();
/* 472 */     this.informSocket = null;
/*     */ 
/* 474 */     this.snmpQman = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.daemon.SnmpSession
 * JD-Core Version:    0.6.2
 */