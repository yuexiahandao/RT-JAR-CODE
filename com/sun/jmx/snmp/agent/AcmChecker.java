/*     */ package com.sun.jmx.snmp.agent;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpPdu;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.SnmpUnknownModelException;
/*     */ import com.sun.jmx.snmp.internal.SnmpAccessControlModel;
/*     */ import com.sun.jmx.snmp.internal.SnmpAccessControlSubSystem;
/*     */ import com.sun.jmx.snmp.internal.SnmpEngineImpl;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ class AcmChecker
/*     */ {
/* 243 */   SnmpAccessControlModel model = null;
/* 244 */   String principal = null;
/* 245 */   int securityLevel = -1;
/* 246 */   int version = -1;
/* 247 */   int pduType = -1;
/* 248 */   int securityModel = -1;
/* 249 */   byte[] contextName = null;
/* 250 */   SnmpEngineImpl engine = null;
/* 251 */   LongList l = null;
/*     */ 
/* 253 */   AcmChecker(SnmpMibRequest paramSnmpMibRequest) { this.engine = ((SnmpEngineImpl)paramSnmpMibRequest.getEngine());
/*     */ 
/* 255 */     if ((this.engine != null) && 
/* 256 */       (this.engine.isCheckOidActivated()))
/*     */       try {
/* 258 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 259 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "AcmChecker(SnmpMibRequest)", "SNMP V3 Access Control to be done");
/*     */         }
/*     */ 
/* 264 */         this.model = ((SnmpAccessControlModel)this.engine.getAccessControlSubSystem().getModel(3));
/*     */ 
/* 267 */         this.principal = paramSnmpMibRequest.getPrincipal();
/* 268 */         this.securityLevel = paramSnmpMibRequest.getSecurityLevel();
/* 269 */         this.pduType = paramSnmpMibRequest.getPdu().type;
/* 270 */         this.version = paramSnmpMibRequest.getRequestPduVersion();
/* 271 */         this.securityModel = paramSnmpMibRequest.getSecurityModel();
/* 272 */         this.contextName = paramSnmpMibRequest.getAccessContextName();
/* 273 */         this.l = new LongList();
/* 274 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 275 */           StringBuilder localStringBuilder = new StringBuilder().append("Will check oid for : principal : ").append(this.principal).append("; securityLevel : ").append(this.securityLevel).append("; pduType : ").append(this.pduType).append("; version : ").append(this.version).append("; securityModel : ").append(this.securityModel).append("; contextName : ").append(this.contextName);
/*     */ 
/* 283 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "AcmChecker(SnmpMibRequest)", localStringBuilder.toString());
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (SnmpUnknownModelException localSnmpUnknownModelException)
/*     */       {
/* 289 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/* 290 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "AcmChecker(SnmpMibRequest)", "Unknown Model, no ACM check.");
/*     */       }
/*     */   }
/*     */ 
/*     */   void add(int paramInt, long paramLong)
/*     */   {
/* 301 */     if (this.model != null)
/* 302 */       this.l.add(paramInt, paramLong);
/*     */   }
/*     */ 
/*     */   void remove(int paramInt) {
/* 306 */     if (this.model != null)
/* 307 */       this.l.remove(paramInt);
/*     */   }
/*     */ 
/*     */   void add(int paramInt1, long[] paramArrayOfLong, int paramInt2, int paramInt3)
/*     */   {
/* 312 */     if (this.model != null)
/* 313 */       this.l.add(paramInt1, paramArrayOfLong, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   void remove(int paramInt1, int paramInt2) {
/* 317 */     if (this.model != null)
/* 318 */       this.l.remove(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   void checkCurrentOid() throws SnmpStatusException {
/* 322 */     if (this.model != null) {
/* 323 */       SnmpOid localSnmpOid = new SnmpOid(this.l.toArray());
/* 324 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 325 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "checkCurrentOid", "Checking access for : " + localSnmpOid);
/*     */       }
/*     */ 
/* 328 */       this.model.checkAccess(this.version, this.principal, this.securityLevel, this.pduType, this.securityModel, this.contextName, localSnmpOid);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.AcmChecker
 * JD-Core Version:    0.6.2
 */