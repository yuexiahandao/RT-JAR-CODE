/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class SnmpScopedPduPacket extends SnmpPdu
/*     */   implements Serializable
/*     */ {
/*  55 */   public int msgMaxSize = 0;
/*     */ 
/*  60 */   public int msgId = 0;
/*     */ 
/*  75 */   public byte msgFlags = 0;
/*     */ 
/*  80 */   public int msgSecurityModel = 0;
/*     */ 
/*  85 */   public byte[] contextEngineId = null;
/*     */ 
/*  90 */   public byte[] contextName = null;
/*     */ 
/*  96 */   public SnmpSecurityParameters securityParameters = null;
/*     */ 
/*     */   protected SnmpScopedPduPacket()
/*     */   {
/* 102 */     this.version = 3;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpScopedPduPacket
 * JD-Core Version:    0.6.2
 */