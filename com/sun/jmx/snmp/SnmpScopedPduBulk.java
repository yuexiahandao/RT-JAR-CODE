/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ public class SnmpScopedPduBulk extends SnmpScopedPduPacket
/*     */   implements SnmpPduBulkType
/*     */ {
/*     */   private static final long serialVersionUID = -1648623646227038885L;
/*     */   int nonRepeaters;
/*     */   int maxRepetitions;
/*     */ 
/*     */   public SnmpScopedPduBulk()
/*     */   {
/*  56 */     this.type = 165;
/*  57 */     this.version = 3;
/*     */   }
/*     */ 
/*     */   public void setMaxRepetitions(int paramInt)
/*     */   {
/*  65 */     this.maxRepetitions = paramInt;
/*     */   }
/*     */ 
/*     */   public void setNonRepeaters(int paramInt)
/*     */   {
/*  73 */     this.nonRepeaters = paramInt;
/*     */   }
/*     */ 
/*     */   public int getMaxRepetitions()
/*     */   {
/*  80 */     return this.maxRepetitions;
/*     */   }
/*     */ 
/*     */   public int getNonRepeaters()
/*     */   {
/*  86 */     return this.nonRepeaters;
/*     */   }
/*     */ 
/*     */   public SnmpPdu getResponsePdu()
/*     */   {
/*  93 */     SnmpScopedPduRequest localSnmpScopedPduRequest = new SnmpScopedPduRequest();
/*  94 */     localSnmpScopedPduRequest.address = this.address;
/*  95 */     localSnmpScopedPduRequest.port = this.port;
/*  96 */     localSnmpScopedPduRequest.version = this.version;
/*  97 */     localSnmpScopedPduRequest.requestId = this.requestId;
/*  98 */     localSnmpScopedPduRequest.msgId = this.msgId;
/*  99 */     localSnmpScopedPduRequest.msgMaxSize = this.msgMaxSize;
/* 100 */     localSnmpScopedPduRequest.msgFlags = this.msgFlags;
/* 101 */     localSnmpScopedPduRequest.msgSecurityModel = this.msgSecurityModel;
/* 102 */     localSnmpScopedPduRequest.contextEngineId = this.contextEngineId;
/* 103 */     localSnmpScopedPduRequest.contextName = this.contextName;
/* 104 */     localSnmpScopedPduRequest.securityParameters = this.securityParameters;
/* 105 */     localSnmpScopedPduRequest.type = 162;
/* 106 */     localSnmpScopedPduRequest.errorStatus = 0;
/* 107 */     localSnmpScopedPduRequest.errorIndex = 0;
/* 108 */     return localSnmpScopedPduRequest;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpScopedPduBulk
 * JD-Core Version:    0.6.2
 */