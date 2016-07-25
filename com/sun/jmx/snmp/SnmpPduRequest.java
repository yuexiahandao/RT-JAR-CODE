/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ public class SnmpPduRequest extends SnmpPduPacket
/*     */   implements SnmpPduRequestType
/*     */ {
/*     */   private static final long serialVersionUID = 2218754017025258979L;
/*  53 */   public int errorStatus = 0;
/*     */ 
/*  62 */   public int errorIndex = 0;
/*     */ 
/*     */   public void setErrorIndex(int paramInt)
/*     */   {
/*  69 */     this.errorIndex = paramInt;
/*     */   }
/*     */ 
/*     */   public void setErrorStatus(int paramInt)
/*     */   {
/*  77 */     this.errorStatus = paramInt;
/*     */   }
/*     */ 
/*     */   public int getErrorIndex()
/*     */   {
/*  84 */     return this.errorIndex;
/*     */   }
/*     */ 
/*     */   public int getErrorStatus()
/*     */   {
/*  90 */     return this.errorStatus;
/*     */   }
/*     */ 
/*     */   public SnmpPdu getResponsePdu()
/*     */   {
/*  97 */     SnmpPduRequest localSnmpPduRequest = new SnmpPduRequest();
/*  98 */     localSnmpPduRequest.address = this.address;
/*  99 */     localSnmpPduRequest.port = this.port;
/* 100 */     localSnmpPduRequest.version = this.version;
/* 101 */     localSnmpPduRequest.community = this.community;
/* 102 */     localSnmpPduRequest.type = 162;
/* 103 */     localSnmpPduRequest.requestId = this.requestId;
/* 104 */     localSnmpPduRequest.errorStatus = 0;
/* 105 */     localSnmpPduRequest.errorIndex = 0;
/*     */ 
/* 107 */     return localSnmpPduRequest;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpPduRequest
 * JD-Core Version:    0.6.2
 */