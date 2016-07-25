/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class SnmpPduFactoryBER
/*     */   implements SnmpPduFactory, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -3525318344000547635L;
/*     */ 
/*     */   public SnmpPdu decodeSnmpPdu(SnmpMsg paramSnmpMsg)
/*     */     throws SnmpStatusException
/*     */   {
/*  93 */     return paramSnmpMsg.decodeSnmpPdu();
/*     */   }
/*     */ 
/*     */   public SnmpMsg encodeSnmpPdu(SnmpPdu paramSnmpPdu, int paramInt)
/*     */     throws SnmpStatusException, SnmpTooBigException
/*     */   {
/*     */     Object localObject;
/* 115 */     switch (paramSnmpPdu.version) {
/*     */     case 0:
/*     */     case 1:
/* 118 */       localObject = new SnmpMessage();
/* 119 */       ((SnmpMessage)localObject).encodeSnmpPdu((SnmpPduPacket)paramSnmpPdu, paramInt);
/* 120 */       return localObject;
/*     */     case 3:
/* 123 */       localObject = new SnmpV3Message();
/* 124 */       ((SnmpV3Message)localObject).encodeSnmpPdu(paramSnmpPdu, paramInt);
/* 125 */       return localObject;
/*     */     case 2:
/*     */     }
/* 128 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpPduFactoryBER
 * JD-Core Version:    0.6.2
 */