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
/*     */ import java.util.Vector;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ class SnmpSubNextRequestHandler extends SnmpSubRequestHandler
/*     */ {
/*  56 */   private SnmpAdaptorServer server = null;
/*     */ 
/*     */   protected SnmpSubNextRequestHandler(SnmpAdaptorServer paramSnmpAdaptorServer, SnmpMibAgent paramSnmpMibAgent, SnmpPdu paramSnmpPdu)
/*     */   {
/*  64 */     super(paramSnmpMibAgent, paramSnmpPdu);
/*  65 */     init(paramSnmpPdu, paramSnmpAdaptorServer);
/*     */   }
/*     */ 
/*     */   protected SnmpSubNextRequestHandler(SnmpEngine paramSnmpEngine, SnmpAdaptorServer paramSnmpAdaptorServer, SnmpIncomingRequest paramSnmpIncomingRequest, SnmpMibAgent paramSnmpMibAgent, SnmpPdu paramSnmpPdu)
/*     */   {
/*  73 */     super(paramSnmpEngine, paramSnmpIncomingRequest, paramSnmpMibAgent, paramSnmpPdu);
/*  74 */     init(paramSnmpPdu, paramSnmpAdaptorServer);
/*  75 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/*  76 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSubNextRequestHandler.class.getName(), "SnmpSubNextRequestHandler", "Constructor : " + this);
/*     */   }
/*     */ 
/*     */   private void init(SnmpPdu paramSnmpPdu, SnmpAdaptorServer paramSnmpAdaptorServer)
/*     */   {
/*  82 */     this.server = paramSnmpAdaptorServer;
/*     */ 
/*  86 */     int i = this.translation.length;
/*  87 */     SnmpVarBind[] arrayOfSnmpVarBind = paramSnmpPdu.varBindList;
/*  88 */     SnmpSubRequestHandler.NonSyncVector localNonSyncVector = (SnmpSubRequestHandler.NonSyncVector)this.varBind;
/*     */ 
/*  90 */     for (int j = 0; j < i; j++) {
/*  91 */       this.translation[j] = j;
/*     */ 
/*  95 */       SnmpVarBind localSnmpVarBind = new SnmpVarBind(arrayOfSnmpVarBind[j].oid, arrayOfSnmpVarBind[j].value);
/*     */ 
/*  97 */       localNonSyncVector.addNonSyncElement(localSnmpVarBind);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/* 105 */       ThreadContext localThreadContext = ThreadContext.push("SnmpUserData", this.data);
/*     */       try
/*     */       {
/* 108 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 109 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "run", "[" + Thread.currentThread() + "]:getNext operation on " + this.agent.getMibName());
/*     */         }
/*     */ 
/* 117 */         this.agent.getNext(createMibRequest(this.varBind, 1, this.data));
/*     */       } finally {
/* 119 */         ThreadContext.restore(localThreadContext);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (SnmpStatusException localSnmpStatusException)
/*     */     {
/* 125 */       this.errorStatus = localSnmpStatusException.getStatus();
/* 126 */       this.errorIndex = localSnmpStatusException.getErrorIndex();
/* 127 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 128 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSubRequestHandler.class.getName(), "run", "[" + Thread.currentThread() + "]:an Snmp error occured during the operation", localSnmpStatusException);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 134 */       this.errorStatus = 5;
/* 135 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 136 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSubRequestHandler.class.getName(), "run", "[" + Thread.currentThread() + "]:a generic error occured during the operation", localException);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 141 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER))
/* 142 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "run", "[" + Thread.currentThread() + "]:operation completed");
/*     */   }
/*     */ 
/*     */   protected void updateRequest(SnmpVarBind paramSnmpVarBind, int paramInt)
/*     */   {
/* 151 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 152 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSubRequestHandler.class.getName(), "updateRequest", "Copy :" + paramSnmpVarBind);
/*     */     }
/*     */ 
/* 155 */     int i = this.varBind.size();
/* 156 */     this.translation[i] = paramInt;
/* 157 */     SnmpVarBind localSnmpVarBind = new SnmpVarBind(paramSnmpVarBind.oid, paramSnmpVarBind.value);
/*     */ 
/* 159 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 160 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSubRequestHandler.class.getName(), "updateRequest", "Copied :" + localSnmpVarBind);
/*     */     }
/*     */ 
/* 164 */     this.varBind.addElement(localSnmpVarBind);
/*     */   }
/*     */ 
/*     */   protected void updateResult(SnmpVarBind[] paramArrayOfSnmpVarBind)
/*     */   {
/* 175 */     int i = this.varBind.size();
/* 176 */     for (int j = 0; j < i; j++)
/*     */     {
/* 179 */       int k = this.translation[j];
/* 180 */       SnmpVarBind localSnmpVarBind1 = (SnmpVarBind)((SnmpSubRequestHandler.NonSyncVector)this.varBind).elementAtNonSync(j);
/*     */ 
/* 183 */       SnmpVarBind localSnmpVarBind2 = paramArrayOfSnmpVarBind[k];
/* 184 */       if (localSnmpVarBind2 == null) {
/* 185 */         paramArrayOfSnmpVarBind[k] = localSnmpVarBind1;
/*     */       }
/*     */       else
/*     */       {
/* 194 */         SnmpValue localSnmpValue = localSnmpVarBind2.value;
/* 195 */         if ((localSnmpValue == null) || (localSnmpValue == SnmpVarBind.endOfMibView))
/*     */         {
/* 197 */           if ((localSnmpVarBind1 != null) && (localSnmpVarBind1.value != SnmpVarBind.endOfMibView))
/*     */           {
/* 199 */             paramArrayOfSnmpVarBind[k] = localSnmpVarBind1;
/*     */           }
/*     */ 
/*     */         }
/* 207 */         else if (localSnmpVarBind1 != null)
/*     */         {
/* 210 */           if (localSnmpVarBind1.value != SnmpVarBind.endOfMibView)
/*     */           {
/* 215 */             int m = localSnmpVarBind1.oid.compareTo(localSnmpVarBind2.oid);
/* 216 */             if (m < 0)
/*     */             {
/* 219 */               paramArrayOfSnmpVarBind[k] = localSnmpVarBind1;
/*     */             }
/* 222 */             else if (m == 0)
/*     */             {
/* 225 */               if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 226 */                 JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "updateResult", " oid overlapping. Oid : " + localSnmpVarBind1.oid + "value :" + localSnmpVarBind1.value);
/*     */ 
/* 229 */                 JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "updateResult", "Already present varBind : " + localSnmpVarBind2);
/*     */               }
/*     */ 
/* 234 */               SnmpOid localSnmpOid = localSnmpVarBind2.oid;
/* 235 */               SnmpMibAgent localSnmpMibAgent = this.server.getAgentMib(localSnmpOid);
/*     */ 
/* 237 */               if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 238 */                 JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "updateResult", "Deeper agent : " + localSnmpMibAgent);
/*     */               }
/*     */ 
/* 241 */               if (localSnmpMibAgent == this.agent) {
/* 242 */                 if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 243 */                   JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSubRequestHandler.class.getName(), "updateResult", "The current agent is the deeper one. Update the value with the current one");
/*     */                 }
/*     */ 
/* 246 */                 paramArrayOfSnmpVarBind[k].value = localSnmpVarBind1.value;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.daemon.SnmpSubNextRequestHandler
 * JD-Core Version:    0.6.2
 */