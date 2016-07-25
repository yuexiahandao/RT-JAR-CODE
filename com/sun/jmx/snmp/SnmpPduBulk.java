/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ public class SnmpPduBulk extends SnmpPduPacket
/*     */   implements SnmpPduBulkType
/*     */ {
/*     */   private static final long serialVersionUID = -7431306775883371046L;
/*     */   public int nonRepeaters;
/*     */   public int maxRepetitions;
/*     */ 
/*     */   public SnmpPduBulk()
/*     */   {
/*  70 */     this.type = 165;
/*  71 */     this.version = 1;
/*     */   }
/*     */ 
/*     */   public void setMaxRepetitions(int paramInt)
/*     */   {
/*  79 */     this.maxRepetitions = paramInt;
/*     */   }
/*     */ 
/*     */   public void setNonRepeaters(int paramInt)
/*     */   {
/*  87 */     this.nonRepeaters = paramInt;
/*     */   }
/*     */ 
/*     */   public int getMaxRepetitions()
/*     */   {
/*  94 */     return this.maxRepetitions;
/*     */   }
/*     */ 
/*     */   public int getNonRepeaters()
/*     */   {
/* 100 */     return this.nonRepeaters;
/*     */   }
/*     */ 
/*     */   public SnmpPdu getResponsePdu()
/*     */   {
/* 107 */     SnmpPduRequest localSnmpPduRequest = new SnmpPduRequest();
/* 108 */     localSnmpPduRequest.address = this.address;
/* 109 */     localSnmpPduRequest.port = this.port;
/* 110 */     localSnmpPduRequest.version = this.version;
/* 111 */     localSnmpPduRequest.community = this.community;
/* 112 */     localSnmpPduRequest.type = 162;
/* 113 */     localSnmpPduRequest.requestId = this.requestId;
/* 114 */     localSnmpPduRequest.errorStatus = 0;
/* 115 */     localSnmpPduRequest.errorIndex = 0;
/*     */ 
/* 117 */     return localSnmpPduRequest;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpPduBulk
 * JD-Core Version:    0.6.2
 */