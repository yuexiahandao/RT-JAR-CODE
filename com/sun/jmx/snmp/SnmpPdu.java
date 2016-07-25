/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ 
/*     */ public abstract class SnmpPdu
/*     */   implements SnmpDefinitions, Serializable
/*     */ {
/*  55 */   public int type = 0;
/*     */ 
/*  62 */   public int version = 0;
/*     */   public SnmpVarBind[] varBindList;
/*  76 */   public int requestId = 0;
/*     */   public InetAddress address;
/*  92 */   public int port = 0;
/*     */ 
/*     */   public static String pduTypeToString(int paramInt)
/*     */   {
/* 102 */     switch (paramInt) {
/*     */     case 160:
/* 104 */       return "SnmpGet";
/*     */     case 161:
/* 106 */       return "SnmpGetNext";
/*     */     case 253:
/* 108 */       return "SnmpWalk(*)";
/*     */     case 163:
/* 110 */       return "SnmpSet";
/*     */     case 162:
/* 112 */       return "SnmpResponse";
/*     */     case 164:
/* 114 */       return "SnmpV1Trap";
/*     */     case 167:
/* 116 */       return "SnmpV2Trap";
/*     */     case 165:
/* 118 */       return "SnmpGetBulk";
/*     */     case 166:
/* 120 */       return "SnmpInform";
/*     */     }
/* 122 */     return "Unknown Command = " + paramInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpPdu
 * JD-Core Version:    0.6.2
 */