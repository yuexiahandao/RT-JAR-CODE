/*     */ package com.sun.org.glassfish.external.statistics.impl;
/*     */ 
/*     */ import com.sun.org.glassfish.external.statistics.AverageRangeStatistic;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class AverageRangeStatisticImpl extends StatisticImpl
/*     */   implements AverageRangeStatistic, InvocationHandler
/*     */ {
/*  45 */   private long currentVal = 0L;
/*  46 */   private long highWaterMark = -9223372036854775808L;
/*  47 */   private long lowWaterMark = 9223372036854775807L;
/*  48 */   private long numberOfSamples = 0L;
/*  49 */   private long runningTotal = 0L;
/*     */   private final long initCurrentVal;
/*     */   private final long initHighWaterMark;
/*     */   private final long initLowWaterMark;
/*     */   private final long initNumberOfSamples;
/*     */   private final long initRunningTotal;
/*  57 */   private final AverageRangeStatistic as = (InvocationHandler)Proxy.newProxyInstance(InvocationHandler.class.getClassLoader(), new Class[] { InvocationHandler.class }, this);
/*     */ 
/*     */   public AverageRangeStatisticImpl(long curVal, long highMark, long lowMark, String name, String unit, String desc, long startTime, long sampleTime)
/*     */   {
/*  66 */     super(name, unit, desc, startTime, sampleTime);
/*  67 */     this.currentVal = curVal;
/*  68 */     this.initCurrentVal = curVal;
/*  69 */     this.highWaterMark = highMark;
/*  70 */     this.initHighWaterMark = highMark;
/*  71 */     this.lowWaterMark = lowMark;
/*  72 */     this.initLowWaterMark = lowMark;
/*  73 */     this.numberOfSamples = 0L;
/*  74 */     this.initNumberOfSamples = this.numberOfSamples;
/*  75 */     this.runningTotal = 0L;
/*  76 */     this.initRunningTotal = this.runningTotal;
/*     */   }
/*     */ 
/*     */   public synchronized AverageRangeStatistic getStatistic() {
/*  80 */     return this.as;
/*     */   }
/*     */ 
/*     */   public synchronized String toString() {
/*  84 */     return super.toString() + NEWLINE + "Current: " + getCurrent() + NEWLINE + "LowWaterMark: " + getLowWaterMark() + NEWLINE + "HighWaterMark: " + getHighWaterMark() + NEWLINE + "Average:" + getAverage();
/*     */   }
/*     */ 
/*     */   public synchronized Map getStaticAsMap()
/*     */   {
/*  92 */     Map m = super.getStaticAsMap();
/*  93 */     m.put("current", Long.valueOf(getCurrent()));
/*  94 */     m.put("lowwatermark", Long.valueOf(getLowWaterMark()));
/*  95 */     m.put("highwatermark", Long.valueOf(getHighWaterMark()));
/*  96 */     m.put("average", Long.valueOf(getAverage()));
/*  97 */     return m;
/*     */   }
/*     */ 
/*     */   public synchronized void reset() {
/* 101 */     super.reset();
/* 102 */     this.currentVal = this.initCurrentVal;
/* 103 */     this.highWaterMark = this.initHighWaterMark;
/* 104 */     this.lowWaterMark = this.initLowWaterMark;
/* 105 */     this.numberOfSamples = this.initNumberOfSamples;
/* 106 */     this.runningTotal = this.initRunningTotal;
/* 107 */     this.sampleTime = -1L;
/*     */   }
/*     */ 
/*     */   public synchronized long getAverage() {
/* 111 */     if (this.numberOfSamples == 0L) {
/* 112 */       return -1L;
/*     */     }
/* 114 */     return this.runningTotal / this.numberOfSamples;
/*     */   }
/*     */ 
/*     */   public synchronized long getCurrent()
/*     */   {
/* 119 */     return this.currentVal;
/*     */   }
/*     */ 
/*     */   public synchronized void setCurrent(long curVal) {
/* 123 */     this.currentVal = curVal;
/* 124 */     this.lowWaterMark = (curVal >= this.lowWaterMark ? this.lowWaterMark : curVal);
/* 125 */     this.highWaterMark = (curVal >= this.highWaterMark ? curVal : this.highWaterMark);
/* 126 */     this.numberOfSamples += 1L;
/* 127 */     this.runningTotal += curVal;
/* 128 */     this.sampleTime = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */   public synchronized long getHighWaterMark() {
/* 132 */     return this.highWaterMark;
/*     */   }
/*     */ 
/*     */   public synchronized long getLowWaterMark() {
/* 136 */     return this.lowWaterMark;
/*     */   }
/*     */ 
/*     */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
/*     */   {
/* 141 */     checkMethod(method);
/*     */     Object result;
/*     */     try {
/* 145 */       result = method.invoke(this, args);
/*     */     } catch (InvocationTargetException e) {
/* 147 */       throw e.getTargetException();
/*     */     } catch (Exception e) {
/* 149 */       throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
/*     */     }
/*     */ 
/* 152 */     return result;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.statistics.impl.AverageRangeStatisticImpl
 * JD-Core Version:    0.6.2
 */