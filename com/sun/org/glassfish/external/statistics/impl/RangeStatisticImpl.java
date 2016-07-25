/*     */ package com.sun.org.glassfish.external.statistics.impl;
/*     */ 
/*     */ import com.sun.org.glassfish.external.statistics.RangeStatistic;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class RangeStatisticImpl extends StatisticImpl
/*     */   implements RangeStatistic, InvocationHandler
/*     */ {
/*  39 */   private long currentVal = 0L;
/*  40 */   private long highWaterMark = -9223372036854775808L;
/*  41 */   private long lowWaterMark = 9223372036854775807L;
/*     */   private final long initCurrentVal;
/*     */   private final long initHighWaterMark;
/*     */   private final long initLowWaterMark;
/*  46 */   private final RangeStatistic rs = (InvocationHandler)Proxy.newProxyInstance(InvocationHandler.class.getClassLoader(), new Class[] { InvocationHandler.class }, this);
/*     */ 
/*     */   public RangeStatisticImpl(long curVal, long highMark, long lowMark, String name, String unit, String desc, long startTime, long sampleTime)
/*     */   {
/*  55 */     super(name, unit, desc, startTime, sampleTime);
/*  56 */     this.currentVal = curVal;
/*  57 */     this.initCurrentVal = curVal;
/*  58 */     this.highWaterMark = highMark;
/*  59 */     this.initHighWaterMark = highMark;
/*  60 */     this.lowWaterMark = lowMark;
/*  61 */     this.initLowWaterMark = lowMark;
/*     */   }
/*     */ 
/*     */   public synchronized RangeStatistic getStatistic() {
/*  65 */     return this.rs;
/*     */   }
/*     */ 
/*     */   public synchronized Map getStaticAsMap() {
/*  69 */     Map m = super.getStaticAsMap();
/*  70 */     m.put("current", Long.valueOf(getCurrent()));
/*  71 */     m.put("lowwatermark", Long.valueOf(getLowWaterMark()));
/*  72 */     m.put("highwatermark", Long.valueOf(getHighWaterMark()));
/*  73 */     return m;
/*     */   }
/*     */ 
/*     */   public synchronized long getCurrent() {
/*  77 */     return this.currentVal;
/*     */   }
/*     */ 
/*     */   public synchronized void setCurrent(long curVal) {
/*  81 */     this.currentVal = curVal;
/*  82 */     this.lowWaterMark = (curVal >= this.lowWaterMark ? this.lowWaterMark : curVal);
/*  83 */     this.highWaterMark = (curVal >= this.highWaterMark ? curVal : this.highWaterMark);
/*  84 */     this.sampleTime = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */   public synchronized long getHighWaterMark()
/*     */   {
/*  91 */     return this.highWaterMark;
/*     */   }
/*     */ 
/*     */   public synchronized void setHighWaterMark(long hwm) {
/*  95 */     this.highWaterMark = hwm;
/*     */   }
/*     */ 
/*     */   public synchronized long getLowWaterMark()
/*     */   {
/* 102 */     return this.lowWaterMark;
/*     */   }
/*     */ 
/*     */   public synchronized void setLowWaterMark(long lwm) {
/* 106 */     this.lowWaterMark = lwm;
/*     */   }
/*     */ 
/*     */   public synchronized void reset()
/*     */   {
/* 111 */     super.reset();
/* 112 */     this.currentVal = this.initCurrentVal;
/* 113 */     this.highWaterMark = this.initHighWaterMark;
/* 114 */     this.lowWaterMark = this.initLowWaterMark;
/* 115 */     this.sampleTime = -1L;
/*     */   }
/*     */ 
/*     */   public synchronized String toString() {
/* 119 */     return super.toString() + NEWLINE + "Current: " + getCurrent() + NEWLINE + "LowWaterMark: " + getLowWaterMark() + NEWLINE + "HighWaterMark: " + getHighWaterMark();
/*     */   }
/*     */ 
/*     */   public Object invoke(Object proxy, Method m, Object[] args)
/*     */     throws Throwable
/*     */   {
/* 127 */     checkMethod(m);
/*     */     Object result;
/*     */     try
/*     */     {
/* 131 */       result = m.invoke(this, args);
/*     */     } catch (InvocationTargetException e) {
/* 133 */       throw e.getTargetException();
/*     */     } catch (Exception e) {
/* 135 */       throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
/*     */     }
/*     */ 
/* 138 */     return result;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.statistics.impl.RangeStatisticImpl
 * JD-Core Version:    0.6.2
 */