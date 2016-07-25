/*     */ package com.sun.org.glassfish.external.statistics.impl;
/*     */ 
/*     */ import com.sun.org.glassfish.external.statistics.BoundedRangeStatistic;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class BoundedRangeStatisticImpl extends StatisticImpl
/*     */   implements BoundedRangeStatistic, InvocationHandler
/*     */ {
/*  41 */   private long lowerBound = 0L;
/*  42 */   private long upperBound = 0L;
/*  43 */   private long currentVal = 0L;
/*  44 */   private long highWaterMark = -9223372036854775808L;
/*  45 */   private long lowWaterMark = 9223372036854775807L;
/*     */   private final long initLowerBound;
/*     */   private final long initUpperBound;
/*     */   private final long initCurrentVal;
/*     */   private final long initHighWaterMark;
/*     */   private final long initLowWaterMark;
/*  53 */   private final BoundedRangeStatistic bs = (InvocationHandler)Proxy.newProxyInstance(InvocationHandler.class.getClassLoader(), new Class[] { InvocationHandler.class }, this);
/*     */ 
/*     */   public synchronized String toString()
/*     */   {
/*  60 */     return super.toString() + NEWLINE + "Current: " + getCurrent() + NEWLINE + "LowWaterMark: " + getLowWaterMark() + NEWLINE + "HighWaterMark: " + getHighWaterMark() + NEWLINE + "LowerBound: " + getLowerBound() + NEWLINE + "UpperBound: " + getUpperBound();
/*     */   }
/*     */ 
/*     */   public BoundedRangeStatisticImpl(long curVal, long highMark, long lowMark, long upper, long lower, String name, String unit, String desc, long startTime, long sampleTime)
/*     */   {
/*  73 */     super(name, unit, desc, startTime, sampleTime);
/*  74 */     this.currentVal = curVal;
/*  75 */     this.initCurrentVal = curVal;
/*  76 */     this.highWaterMark = highMark;
/*  77 */     this.initHighWaterMark = highMark;
/*  78 */     this.lowWaterMark = lowMark;
/*  79 */     this.initLowWaterMark = lowMark;
/*  80 */     this.upperBound = upper;
/*  81 */     this.initUpperBound = upper;
/*  82 */     this.lowerBound = lower;
/*  83 */     this.initLowerBound = lower;
/*     */   }
/*     */ 
/*     */   public synchronized BoundedRangeStatistic getStatistic() {
/*  87 */     return this.bs;
/*     */   }
/*     */ 
/*     */   public synchronized Map getStaticAsMap() {
/*  91 */     Map m = super.getStaticAsMap();
/*  92 */     m.put("current", Long.valueOf(getCurrent()));
/*  93 */     m.put("lowerbound", Long.valueOf(getLowerBound()));
/*  94 */     m.put("upperbound", Long.valueOf(getUpperBound()));
/*  95 */     m.put("lowwatermark", Long.valueOf(getLowWaterMark()));
/*  96 */     m.put("highwatermark", Long.valueOf(getHighWaterMark()));
/*  97 */     return m;
/*     */   }
/*     */ 
/*     */   public synchronized long getCurrent() {
/* 101 */     return this.currentVal;
/*     */   }
/*     */ 
/*     */   public synchronized void setCurrent(long curVal) {
/* 105 */     this.currentVal = curVal;
/* 106 */     this.lowWaterMark = (curVal >= this.lowWaterMark ? this.lowWaterMark : curVal);
/* 107 */     this.highWaterMark = (curVal >= this.highWaterMark ? curVal : this.highWaterMark);
/* 108 */     this.sampleTime = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */   public synchronized long getHighWaterMark() {
/* 112 */     return this.highWaterMark;
/*     */   }
/*     */ 
/*     */   public synchronized void setHighWaterMark(long hwm) {
/* 116 */     this.highWaterMark = hwm;
/*     */   }
/*     */ 
/*     */   public synchronized long getLowWaterMark() {
/* 120 */     return this.lowWaterMark;
/*     */   }
/*     */ 
/*     */   public synchronized void setLowWaterMark(long lwm) {
/* 124 */     this.lowWaterMark = lwm;
/*     */   }
/*     */ 
/*     */   public synchronized long getLowerBound() {
/* 128 */     return this.lowerBound;
/*     */   }
/*     */ 
/*     */   public synchronized long getUpperBound() {
/* 132 */     return this.upperBound;
/*     */   }
/*     */ 
/*     */   public synchronized void reset()
/*     */   {
/* 137 */     super.reset();
/* 138 */     this.lowerBound = this.initLowerBound;
/* 139 */     this.upperBound = this.initUpperBound;
/* 140 */     this.currentVal = this.initCurrentVal;
/* 141 */     this.highWaterMark = this.initHighWaterMark;
/* 142 */     this.lowWaterMark = this.initLowWaterMark;
/* 143 */     this.sampleTime = -1L;
/*     */   }
/*     */ 
/*     */   public Object invoke(Object proxy, Method m, Object[] args) throws Throwable
/*     */   {
/* 148 */     checkMethod(m);
/*     */     Object result;
/*     */     try {
/* 152 */       result = m.invoke(this, args);
/*     */     } catch (InvocationTargetException e) {
/* 154 */       throw e.getTargetException();
/*     */     } catch (Exception e) {
/* 156 */       throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
/*     */     }
/*     */ 
/* 159 */     return result;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.statistics.impl.BoundedRangeStatisticImpl
 * JD-Core Version:    0.6.2
 */