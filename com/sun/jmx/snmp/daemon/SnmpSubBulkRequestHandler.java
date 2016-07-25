/*     */ package com.sun.jmx.snmp.daemon;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import com.sun.jmx.snmp.SnmpEngine;
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpPdu;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.SnmpValue;
/*     */ import com.sun.jmx.snmp.SnmpVarBind;
/*     */ import com.sun.jmx.snmp.ThreadContext;
/*     */ import com.sun.jmx.snmp.agent.SnmpMibAgent;
/*     */ import com.sun.jmx.snmp.internal.SnmpIncomingRequest;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ class SnmpSubBulkRequestHandler extends SnmpSubRequestHandler
/*     */ {
/*  56 */   private SnmpAdaptorServer server = null;
/*     */ 
/* 335 */   protected int nonRepeat = 0;
/*     */ 
/* 337 */   protected int maxRepeat = 0;
/*     */ 
/* 342 */   protected int globalR = 0;
/*     */ 
/* 344 */   protected int size = 0;
/*     */ 
/*     */   protected SnmpSubBulkRequestHandler(SnmpEngine paramSnmpEngine, SnmpAdaptorServer paramSnmpAdaptorServer, SnmpIncomingRequest paramSnmpIncomingRequest, SnmpMibAgent paramSnmpMibAgent, SnmpPdu paramSnmpPdu, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  70 */     super(paramSnmpEngine, paramSnmpIncomingRequest, paramSnmpMibAgent, paramSnmpPdu);
/*  71 */     init(paramSnmpAdaptorServer, paramSnmpPdu, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   protected SnmpSubBulkRequestHandler(SnmpAdaptorServer paramSnmpAdaptorServer, SnmpMibAgent paramSnmpMibAgent, SnmpPdu paramSnmpPdu, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  84 */     super(paramSnmpMibAgent, paramSnmpPdu);
/*  85 */     init(paramSnmpAdaptorServer, paramSnmpPdu, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*  90 */     this.size = this.varBind.size();
/*     */     try
/*     */     {
/*  96 */       ThreadContext localThreadContext = ThreadContext.push("SnmpUserData", this.data);
/*     */       try
/*     */       {
/*  99 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 100 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "run", "[" + Thread.currentThread() + "]:getBulk operation on " + this.agent.getMibName());
/*     */         }
/*     */ 
/* 104 */         this.agent.getBulk(createMibRequest(this.varBind, this.version, this.data), this.nonRepeat, this.maxRepeat);
/*     */       }
/*     */       finally {
/* 107 */         ThreadContext.restore(localThreadContext);
/*     */       }
/*     */     }
/*     */     catch (SnmpStatusException localSnmpStatusException)
/*     */     {
/* 112 */       this.errorStatus = localSnmpStatusException.getStatus();
/* 113 */       this.errorIndex = localSnmpStatusException.getErrorIndex();
/* 114 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 115 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSubRequestHandler.class.getName(), "run", "[" + Thread.currentThread() + "]:an Snmp error occured during the operation", localSnmpStatusException);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 121 */       this.errorStatus = 5;
/* 122 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 123 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSubRequestHandler.class.getName(), "run", "[" + Thread.currentThread() + "]:a generic error occured during the operation", localException);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 128 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER))
/* 129 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "run", "[" + Thread.currentThread() + "]:operation completed");
/*     */   }
/*     */ 
/*     */   private void init(SnmpAdaptorServer paramSnmpAdaptorServer, SnmpPdu paramSnmpPdu, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 140 */     this.server = paramSnmpAdaptorServer;
/* 141 */     this.nonRepeat = paramInt1;
/* 142 */     this.maxRepeat = paramInt2;
/* 143 */     this.globalR = paramInt3;
/*     */ 
/* 145 */     int i = this.translation.length;
/* 146 */     SnmpVarBind[] arrayOfSnmpVarBind = paramSnmpPdu.varBindList;
/* 147 */     SnmpSubRequestHandler.NonSyncVector localNonSyncVector = (SnmpSubRequestHandler.NonSyncVector)this.varBind;
/*     */ 
/* 149 */     for (int j = 0; j < i; j++) {
/* 150 */       this.translation[j] = j;
/*     */ 
/* 154 */       SnmpVarBind localSnmpVarBind = new SnmpVarBind(arrayOfSnmpVarBind[j].oid, arrayOfSnmpVarBind[j].value);
/*     */ 
/* 156 */       localNonSyncVector.addNonSyncElement(localSnmpVarBind);
/*     */     }
/*     */   }
/*     */ 
/*     */   private SnmpVarBind findVarBind(SnmpVarBind paramSnmpVarBind1, SnmpVarBind paramSnmpVarBind2)
/*     */   {
/* 166 */     if (paramSnmpVarBind1 == null) return null;
/*     */ 
/* 168 */     if (paramSnmpVarBind2.oid == null) {
/* 169 */       return paramSnmpVarBind1;
/*     */     }
/*     */ 
/* 172 */     if (paramSnmpVarBind1.value == SnmpVarBind.endOfMibView) return paramSnmpVarBind2;
/*     */ 
/* 174 */     if (paramSnmpVarBind2.value == SnmpVarBind.endOfMibView) return paramSnmpVarBind1;
/*     */ 
/* 176 */     SnmpValue localSnmpValue = paramSnmpVarBind2.value;
/*     */ 
/* 178 */     int i = paramSnmpVarBind1.oid.compareTo(paramSnmpVarBind2.oid);
/* 179 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 180 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "findVarBind", "Comparing OID element : " + paramSnmpVarBind1.oid + " with result : " + paramSnmpVarBind2.oid);
/*     */ 
/* 183 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "findVarBind", "Values element : " + paramSnmpVarBind1.value + " result : " + paramSnmpVarBind2.value);
/*     */     }
/*     */ 
/* 187 */     if (i < 0)
/*     */     {
/* 190 */       return paramSnmpVarBind1;
/*     */     }
/*     */ 
/* 193 */     if (i == 0)
/*     */     {
/* 196 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 197 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "findVarBind", " oid overlapping. Oid : " + paramSnmpVarBind1.oid + "value :" + paramSnmpVarBind1.value);
/*     */ 
/* 200 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "findVarBind", "Already present varBind : " + paramSnmpVarBind2);
/*     */       }
/*     */ 
/* 204 */       SnmpOid localSnmpOid = paramSnmpVarBind2.oid;
/* 205 */       SnmpMibAgent localSnmpMibAgent = this.server.getAgentMib(localSnmpOid);
/*     */ 
/* 207 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 208 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "findVarBind", "Deeper agent : " + localSnmpMibAgent);
/*     */       }
/*     */ 
/* 211 */       if (localSnmpMibAgent == this.agent) {
/* 212 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 213 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "findVarBind", "The current agent is the deeper one. Update the value with the current one");
/*     */         }
/*     */ 
/* 216 */         return paramSnmpVarBind1;
/*     */       }
/* 218 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 219 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "findVarBind", "The current agent is not the deeper one. return the previous one.");
/*     */       }
/*     */ 
/* 222 */       return paramSnmpVarBind2;
/*     */     }
/*     */ 
/* 247 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 248 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "findVarBind", "The right varBind is the already present one");
/*     */     }
/*     */ 
/* 251 */     return paramSnmpVarBind2;
/*     */   }
/*     */ 
/*     */   protected void updateResult(SnmpVarBind[] paramArrayOfSnmpVarBind)
/*     */   {
/* 266 */     Enumeration localEnumeration = this.varBind.elements();
/* 267 */     int i = paramArrayOfSnmpVarBind.length;
/*     */ 
/* 270 */     for (int j = 0; j < this.size; j++)
/*     */     {
/* 273 */       if (!localEnumeration.hasMoreElements()) {
/* 274 */         return;
/*     */       }
/*     */ 
/* 278 */       k = this.translation[j];
/* 279 */       if (k >= i) {
/* 280 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 281 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSubRequestHandler.class.getName(), "updateResult", "Position '" + k + "' is out of bound...");
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 287 */         SnmpVarBind localSnmpVarBind1 = (SnmpVarBind)localEnumeration.nextElement();
/*     */ 
/* 289 */         if (localSnmpVarBind1 != null) {
/* 290 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 291 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "updateResult", "Non repeaters Current element : " + localSnmpVarBind1 + " from agent : " + this.agent);
/*     */           }
/*     */ 
/* 295 */           SnmpVarBind localSnmpVarBind2 = findVarBind(localSnmpVarBind1, paramArrayOfSnmpVarBind[k]);
/*     */ 
/* 297 */           if (localSnmpVarBind2 != null)
/*     */           {
/* 299 */             paramArrayOfSnmpVarBind[k] = localSnmpVarBind2;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 304 */     j = this.size - this.nonRepeat;
/* 305 */     for (int k = 2; k <= this.maxRepeat; k++)
/* 306 */       for (int m = 0; m < j; m++) {
/* 307 */         int n = (k - 1) * this.globalR + this.translation[(this.nonRepeat + m)];
/* 308 */         if (n >= i)
/* 309 */           return;
/* 310 */         if (!localEnumeration.hasMoreElements())
/* 311 */           return;
/* 312 */         SnmpVarBind localSnmpVarBind3 = (SnmpVarBind)localEnumeration.nextElement();
/*     */ 
/* 314 */         if (localSnmpVarBind3 != null) {
/* 315 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 316 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "updateResult", "Repeaters Current element : " + localSnmpVarBind3 + " from agent : " + this.agent);
/*     */           }
/*     */ 
/* 320 */           SnmpVarBind localSnmpVarBind4 = findVarBind(localSnmpVarBind3, paramArrayOfSnmpVarBind[n]);
/*     */ 
/* 322 */           if (localSnmpVarBind4 != null)
/*     */           {
/* 324 */             paramArrayOfSnmpVarBind[n] = localSnmpVarBind4;
/*     */           }
/*     */         }
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.daemon.SnmpSubBulkRequestHandler
 * JD-Core Version:    0.6.2
 */