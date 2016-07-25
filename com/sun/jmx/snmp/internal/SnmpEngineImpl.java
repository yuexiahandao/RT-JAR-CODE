/*     */ package com.sun.jmx.snmp.internal;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import com.sun.jmx.snmp.SnmpBadSecurityLevelException;
/*     */ import com.sun.jmx.snmp.SnmpEngine;
/*     */ import com.sun.jmx.snmp.SnmpEngineFactory;
/*     */ import com.sun.jmx.snmp.SnmpEngineId;
/*     */ import com.sun.jmx.snmp.SnmpUsmKeyHandler;
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class SnmpEngineImpl
/*     */   implements SnmpEngine, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2564301391365614725L;
/*     */   public static final int noAuthNoPriv = 0;
/*     */   public static final int authNoPriv = 1;
/*     */   public static final int authPriv = 3;
/*     */   public static final int reportableFlag = 4;
/*     */   public static final int authMask = 1;
/*     */   public static final int privMask = 2;
/*     */   public static final int authPrivMask = 3;
/* 108 */   private SnmpEngineId engineid = null;
/* 109 */   private SnmpEngineFactory factory = null;
/* 110 */   private long startTime = 0L;
/*     */ 
/* 112 */   private int boot = 0;
/* 113 */   private boolean checkOid = false;
/*     */ 
/* 115 */   private transient SnmpUsmKeyHandler usmKeyHandler = null;
/* 116 */   private transient SnmpLcd lcd = null;
/*     */ 
/* 118 */   private transient SnmpSecuritySubSystem securitySub = null;
/*     */ 
/* 120 */   private transient SnmpMsgProcessingSubSystem messageSub = null;
/*     */ 
/* 122 */   private transient SnmpAccessControlSubSystem accessSub = null;
/*     */ 
/*     */   public synchronized int getEngineTime()
/*     */   {
/* 130 */     long l = System.currentTimeMillis() / 1000L - this.startTime;
/* 131 */     if (l > 2147483647L)
/*     */     {
/* 134 */       this.startTime = (System.currentTimeMillis() / 1000L);
/*     */ 
/* 137 */       if (this.boot != 2147483647) {
/* 138 */         this.boot += 1;
/*     */       }
/* 140 */       storeNBBoots(this.boot);
/*     */     }
/*     */ 
/* 143 */     return (int)(System.currentTimeMillis() / 1000L - this.startTime);
/*     */   }
/*     */ 
/*     */   public SnmpEngineId getEngineId()
/*     */   {
/* 151 */     return this.engineid;
/*     */   }
/*     */ 
/*     */   public SnmpUsmKeyHandler getUsmKeyHandler()
/*     */   {
/* 159 */     return this.usmKeyHandler;
/*     */   }
/*     */ 
/*     */   public SnmpLcd getLcd()
/*     */   {
/* 167 */     return this.lcd;
/*     */   }
/*     */ 
/*     */   public int getEngineBoots()
/*     */   {
/* 174 */     return this.boot;
/*     */   }
/*     */ 
/*     */   public SnmpEngineImpl(SnmpEngineFactory paramSnmpEngineFactory, SnmpLcd paramSnmpLcd, SnmpEngineId paramSnmpEngineId)
/*     */     throws UnknownHostException
/*     */   {
/* 195 */     init(paramSnmpLcd, paramSnmpEngineFactory);
/* 196 */     initEngineID();
/* 197 */     if (this.engineid == null) {
/* 198 */       if (paramSnmpEngineId != null)
/* 199 */         this.engineid = paramSnmpEngineId;
/*     */       else
/* 201 */         this.engineid = SnmpEngineId.createEngineId();
/*     */     }
/* 203 */     paramSnmpLcd.storeEngineId(this.engineid);
/* 204 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINER))
/* 205 */       JmxProperties.SNMP_LOGGER.logp(Level.FINER, SnmpEngineImpl.class.getName(), "SnmpEngineImpl(SnmpEngineFactory,SnmpLcd,SnmpEngineId)", "LOCAL ENGINE ID: " + this.engineid);
/*     */   }
/*     */ 
/*     */   public SnmpEngineImpl(SnmpEngineFactory paramSnmpEngineFactory, SnmpLcd paramSnmpLcd, InetAddress paramInetAddress, int paramInt)
/*     */     throws UnknownHostException
/*     */   {
/* 228 */     init(paramSnmpLcd, paramSnmpEngineFactory);
/* 229 */     initEngineID();
/* 230 */     if (this.engineid == null) {
/* 231 */       this.engineid = SnmpEngineId.createEngineId(paramInetAddress, paramInt);
/*     */     }
/* 233 */     paramSnmpLcd.storeEngineId(this.engineid);
/*     */ 
/* 235 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINER))
/* 236 */       JmxProperties.SNMP_LOGGER.logp(Level.FINER, SnmpEngineImpl.class.getName(), "SnmpEngineImpl(SnmpEngineFactory,SnmpLcd,InetAddress,int)", "LOCAL ENGINE ID: " + this.engineid + " / " + "LOCAL ENGINE NB BOOTS: " + this.boot + " / " + "LOCAL ENGINE START TIME: " + getEngineTime());
/*     */   }
/*     */ 
/*     */   public SnmpEngineImpl(SnmpEngineFactory paramSnmpEngineFactory, SnmpLcd paramSnmpLcd, int paramInt)
/*     */     throws UnknownHostException
/*     */   {
/* 260 */     init(paramSnmpLcd, paramSnmpEngineFactory);
/* 261 */     initEngineID();
/* 262 */     if (this.engineid == null) {
/* 263 */       this.engineid = SnmpEngineId.createEngineId(paramInt);
/*     */     }
/* 265 */     paramSnmpLcd.storeEngineId(this.engineid);
/*     */ 
/* 267 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINER))
/* 268 */       JmxProperties.SNMP_LOGGER.logp(Level.FINER, SnmpEngineImpl.class.getName(), "SnmpEngineImpl(SnmpEngineFactory,SnmpLcd,int)", "LOCAL ENGINE ID: " + this.engineid + " / " + "LOCAL ENGINE NB BOOTS: " + this.boot + " / " + "LOCAL ENGINE START TIME: " + getEngineTime());
/*     */   }
/*     */ 
/*     */   public SnmpEngineImpl(SnmpEngineFactory paramSnmpEngineFactory, SnmpLcd paramSnmpLcd)
/*     */     throws UnknownHostException
/*     */   {
/* 290 */     init(paramSnmpLcd, paramSnmpEngineFactory);
/* 291 */     initEngineID();
/* 292 */     if (this.engineid == null) {
/* 293 */       this.engineid = SnmpEngineId.createEngineId();
/*     */     }
/* 295 */     paramSnmpLcd.storeEngineId(this.engineid);
/*     */ 
/* 297 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINER))
/* 298 */       JmxProperties.SNMP_LOGGER.logp(Level.FINER, SnmpEngineImpl.class.getName(), "SnmpEngineImpl(SnmpEngineFactory,SnmpLcd)", "LOCAL ENGINE ID: " + this.engineid + " / " + "LOCAL ENGINE NB BOOTS: " + this.boot + " / " + "LOCAL ENGINE START TIME: " + getEngineTime());
/*     */   }
/*     */ 
/*     */   public synchronized void activateCheckOid()
/*     */   {
/* 310 */     this.checkOid = true;
/*     */   }
/*     */ 
/*     */   public synchronized void deactivateCheckOid()
/*     */   {
/* 317 */     this.checkOid = false;
/*     */   }
/*     */ 
/*     */   public synchronized boolean isCheckOidActivated()
/*     */   {
/* 324 */     return this.checkOid;
/*     */   }
/*     */ 
/*     */   private void storeNBBoots(int paramInt)
/*     */   {
/* 329 */     if ((paramInt < 0) || (paramInt == 2147483647)) {
/* 330 */       paramInt = 2147483647;
/* 331 */       this.lcd.storeEngineBoots(paramInt);
/*     */     }
/*     */     else {
/* 334 */       this.lcd.storeEngineBoots(paramInt + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void init(SnmpLcd paramSnmpLcd, SnmpEngineFactory paramSnmpEngineFactory) {
/* 339 */     this.factory = paramSnmpEngineFactory;
/* 340 */     this.lcd = paramSnmpLcd;
/* 341 */     this.boot = paramSnmpLcd.getEngineBoots();
/*     */ 
/* 343 */     if ((this.boot == -1) || (this.boot == 0)) {
/* 344 */       this.boot = 1;
/*     */     }
/* 346 */     storeNBBoots(this.boot);
/*     */ 
/* 348 */     this.startTime = (System.currentTimeMillis() / 1000L);
/*     */   }
/*     */ 
/*     */   void setUsmKeyHandler(SnmpUsmKeyHandler paramSnmpUsmKeyHandler)
/*     */   {
/* 353 */     this.usmKeyHandler = paramSnmpUsmKeyHandler;
/*     */   }
/*     */ 
/*     */   private void initEngineID() throws UnknownHostException
/*     */   {
/* 358 */     String str = this.lcd.getEngineId();
/* 359 */     if (str != null)
/* 360 */       this.engineid = SnmpEngineId.createEngineId(str);
/*     */   }
/*     */ 
/*     */   public SnmpMsgProcessingSubSystem getMsgProcessingSubSystem()
/*     */   {
/* 370 */     return this.messageSub;
/*     */   }
/*     */ 
/*     */   public void setMsgProcessingSubSystem(SnmpMsgProcessingSubSystem paramSnmpMsgProcessingSubSystem)
/*     */   {
/* 378 */     this.messageSub = paramSnmpMsgProcessingSubSystem;
/*     */   }
/*     */ 
/*     */   public SnmpSecuritySubSystem getSecuritySubSystem()
/*     */   {
/* 386 */     return this.securitySub;
/*     */   }
/*     */ 
/*     */   public void setSecuritySubSystem(SnmpSecuritySubSystem paramSnmpSecuritySubSystem)
/*     */   {
/* 393 */     this.securitySub = paramSnmpSecuritySubSystem;
/*     */   }
/*     */ 
/*     */   public void setAccessControlSubSystem(SnmpAccessControlSubSystem paramSnmpAccessControlSubSystem)
/*     */   {
/* 400 */     this.accessSub = paramSnmpAccessControlSubSystem;
/*     */   }
/*     */ 
/*     */   public SnmpAccessControlSubSystem getAccessControlSubSystem()
/*     */   {
/* 408 */     return this.accessSub;
/*     */   }
/*     */ 
/*     */   public static void checkSecurityLevel(byte paramByte)
/*     */     throws SnmpBadSecurityLevelException
/*     */   {
/* 416 */     int i = paramByte & 0x3;
/* 417 */     if (((i & 0x2) != 0) && 
/* 418 */       ((i & 0x1) == 0))
/* 419 */       throw new SnmpBadSecurityLevelException("Security level: noAuthPriv!!!");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.internal.SnmpEngineImpl
 * JD-Core Version:    0.6.2
 */