/*    */ package com.sun.jmx.snmp;
/*    */ 
/*    */ public class SnmpScopedPduRequest extends SnmpScopedPduPacket
/*    */   implements SnmpPduRequestType
/*    */ {
/*    */   private static final long serialVersionUID = 6463060973056773680L;
/* 37 */   int errorStatus = 0;
/*    */ 
/* 39 */   int errorIndex = 0;
/*    */ 
/*    */   public void setErrorIndex(int paramInt)
/*    */   {
/* 48 */     this.errorIndex = paramInt;
/*    */   }
/*    */ 
/*    */   public void setErrorStatus(int paramInt)
/*    */   {
/* 56 */     this.errorStatus = paramInt;
/*    */   }
/*    */ 
/*    */   public int getErrorIndex()
/*    */   {
/* 65 */     return this.errorIndex;
/*    */   }
/*    */ 
/*    */   public int getErrorStatus()
/*    */   {
/* 71 */     return this.errorStatus;
/*    */   }
/*    */ 
/*    */   public SnmpPdu getResponsePdu()
/*    */   {
/* 78 */     SnmpScopedPduRequest localSnmpScopedPduRequest = new SnmpScopedPduRequest();
/* 79 */     localSnmpScopedPduRequest.address = this.address;
/* 80 */     localSnmpScopedPduRequest.port = this.port;
/* 81 */     localSnmpScopedPduRequest.version = this.version;
/* 82 */     localSnmpScopedPduRequest.requestId = this.requestId;
/* 83 */     localSnmpScopedPduRequest.msgId = this.msgId;
/* 84 */     localSnmpScopedPduRequest.msgMaxSize = this.msgMaxSize;
/* 85 */     localSnmpScopedPduRequest.msgFlags = this.msgFlags;
/* 86 */     localSnmpScopedPduRequest.msgSecurityModel = this.msgSecurityModel;
/* 87 */     localSnmpScopedPduRequest.contextEngineId = this.contextEngineId;
/* 88 */     localSnmpScopedPduRequest.contextName = this.contextName;
/* 89 */     localSnmpScopedPduRequest.securityParameters = this.securityParameters;
/* 90 */     localSnmpScopedPduRequest.type = 162;
/* 91 */     localSnmpScopedPduRequest.errorStatus = 0;
/* 92 */     localSnmpScopedPduRequest.errorIndex = 0;
/* 93 */     return localSnmpScopedPduRequest;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpScopedPduRequest
 * JD-Core Version:    0.6.2
 */