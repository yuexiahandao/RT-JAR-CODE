/*     */ package com.sun.jmx.snmp.internal;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpEngineId;
/*     */ import com.sun.jmx.snmp.SnmpUnknownModelLcdException;
/*     */ import com.sun.jmx.snmp.SnmpUnknownSubSystemException;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public abstract class SnmpLcd
/*     */ {
/*  59 */   private Hashtable<SnmpSubSystem, SubSysLcdManager> subs = new Hashtable();
/*     */ 
/*     */   public abstract int getEngineBoots();
/*     */ 
/*     */   public abstract String getEngineId();
/*     */ 
/*     */   public abstract void storeEngineBoots(int paramInt);
/*     */ 
/*     */   public abstract void storeEngineId(SnmpEngineId paramSnmpEngineId);
/*     */ 
/*     */   public void addModelLcd(SnmpSubSystem paramSnmpSubSystem, int paramInt, SnmpModelLcd paramSnmpModelLcd)
/*     */   {
/*  94 */     SubSysLcdManager localSubSysLcdManager = (SubSysLcdManager)this.subs.get(paramSnmpSubSystem);
/*  95 */     if (localSubSysLcdManager == null) {
/*  96 */       localSubSysLcdManager = new SubSysLcdManager();
/*  97 */       this.subs.put(paramSnmpSubSystem, localSubSysLcdManager);
/*     */     }
/*     */ 
/* 100 */     localSubSysLcdManager.addModelLcd(paramInt, paramSnmpModelLcd);
/*     */   }
/*     */ 
/*     */   public void removeModelLcd(SnmpSubSystem paramSnmpSubSystem, int paramInt)
/*     */     throws SnmpUnknownModelLcdException, SnmpUnknownSubSystemException
/*     */   {
/* 111 */     SubSysLcdManager localSubSysLcdManager = (SubSysLcdManager)this.subs.get(paramSnmpSubSystem);
/* 112 */     if (localSubSysLcdManager != null) {
/* 113 */       SnmpModelLcd localSnmpModelLcd = localSubSysLcdManager.removeModelLcd(paramInt);
/* 114 */       if (localSnmpModelLcd == null)
/* 115 */         throw new SnmpUnknownModelLcdException("Model : " + paramInt);
/*     */     }
/*     */     else
/*     */     {
/* 119 */       throw new SnmpUnknownSubSystemException(paramSnmpSubSystem.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public SnmpModelLcd getModelLcd(SnmpSubSystem paramSnmpSubSystem, int paramInt)
/*     */   {
/* 130 */     SubSysLcdManager localSubSysLcdManager = (SubSysLcdManager)this.subs.get(paramSnmpSubSystem);
/*     */ 
/* 132 */     if (localSubSysLcdManager == null) return null;
/*     */ 
/* 134 */     return localSubSysLcdManager.getModelLcd(paramInt);
/*     */   }
/*     */ 
/*     */   class SubSysLcdManager
/*     */   {
/*  41 */     private Hashtable<Integer, SnmpModelLcd> models = new Hashtable();
/*     */ 
/*     */     SubSysLcdManager() {
/*     */     }
/*     */     public void addModelLcd(int paramInt, SnmpModelLcd paramSnmpModelLcd) {
/*  46 */       this.models.put(new Integer(paramInt), paramSnmpModelLcd);
/*     */     }
/*     */ 
/*     */     public SnmpModelLcd getModelLcd(int paramInt) {
/*  50 */       return (SnmpModelLcd)this.models.get(new Integer(paramInt));
/*     */     }
/*     */ 
/*     */     public SnmpModelLcd removeModelLcd(int paramInt) {
/*  54 */       return (SnmpModelLcd)this.models.remove(new Integer(paramInt));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.internal.SnmpLcd
 * JD-Core Version:    0.6.2
 */