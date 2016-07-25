/*     */ package com.sun.org.glassfish.external.statistics.impl;
/*     */ 
/*     */ import com.sun.org.glassfish.external.statistics.Statistic;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ public abstract class StatisticImpl
/*     */   implements Statistic
/*     */ {
/*     */   private final String statisticName;
/*     */   private final String statisticUnit;
/*     */   private final String statisticDesc;
/*  42 */   protected long sampleTime = -1L;
/*     */   private long startTime;
/*     */   public static final String UNIT_COUNT = "count";
/*     */   public static final String UNIT_SECOND = "second";
/*     */   public static final String UNIT_MILLISECOND = "millisecond";
/*     */   public static final String UNIT_MICROSECOND = "microsecond";
/*     */   public static final String UNIT_NANOSECOND = "nanosecond";
/*     */   public static final String START_TIME = "starttime";
/*     */   public static final String LAST_SAMPLE_TIME = "lastsampletime";
/*  52 */   protected final Map<String, Object> statMap = new ConcurrentHashMap();
/*     */ 
/*  54 */   protected static final String NEWLINE = System.getProperty("line.separator");
/*     */ 
/*     */   protected StatisticImpl(String name, String unit, String desc, long start_time, long sample_time)
/*     */   {
/*  59 */     if (isValidString(name))
/*  60 */       this.statisticName = name;
/*     */     else {
/*  62 */       this.statisticName = "name";
/*     */     }
/*     */ 
/*  65 */     if (isValidString(unit))
/*  66 */       this.statisticUnit = unit;
/*     */     else {
/*  68 */       this.statisticUnit = "unit";
/*     */     }
/*     */ 
/*  71 */     if (isValidString(desc))
/*  72 */       this.statisticDesc = desc;
/*     */     else {
/*  74 */       this.statisticDesc = "description";
/*     */     }
/*     */ 
/*  77 */     this.startTime = start_time;
/*  78 */     this.sampleTime = sample_time;
/*     */   }
/*     */ 
/*     */   protected StatisticImpl(String name, String unit, String desc) {
/*  82 */     this(name, unit, desc, System.currentTimeMillis(), System.currentTimeMillis());
/*     */   }
/*     */ 
/*     */   public synchronized Map getStaticAsMap() {
/*  86 */     if (isValidString(this.statisticName)) {
/*  87 */       this.statMap.put("name", this.statisticName);
/*     */     }
/*  89 */     if (isValidString(this.statisticUnit)) {
/*  90 */       this.statMap.put("unit", this.statisticUnit);
/*     */     }
/*  92 */     if (isValidString(this.statisticDesc)) {
/*  93 */       this.statMap.put("description", this.statisticDesc);
/*     */     }
/*  95 */     this.statMap.put("starttime", Long.valueOf(this.startTime));
/*  96 */     this.statMap.put("lastsampletime", Long.valueOf(this.sampleTime));
/*  97 */     return this.statMap;
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 101 */     return this.statisticName;
/*     */   }
/*     */ 
/*     */   public String getDescription() {
/* 105 */     return this.statisticDesc;
/*     */   }
/*     */ 
/*     */   public String getUnit() {
/* 109 */     return this.statisticUnit;
/*     */   }
/*     */ 
/*     */   public synchronized long getLastSampleTime() {
/* 113 */     return this.sampleTime;
/*     */   }
/*     */ 
/*     */   public synchronized long getStartTime() {
/* 117 */     return this.startTime;
/*     */   }
/*     */ 
/*     */   public synchronized void reset() {
/* 121 */     this.startTime = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */   public synchronized String toString() {
/* 125 */     return "Statistic " + getClass().getName() + NEWLINE + "Name: " + getName() + NEWLINE + "Description: " + getDescription() + NEWLINE + "Unit: " + getUnit() + NEWLINE + "LastSampleTime: " + getLastSampleTime() + NEWLINE + "StartTime: " + getStartTime();
/*     */   }
/*     */ 
/*     */   protected static boolean isValidString(String str)
/*     */   {
/* 134 */     return (str != null) && (str.length() > 0);
/*     */   }
/*     */ 
/*     */   protected void checkMethod(Method method) {
/* 138 */     if ((method == null) || (method.getDeclaringClass() == null) || (!Statistic.class.isAssignableFrom(method.getDeclaringClass())) || (Modifier.isStatic(method.getModifiers())))
/*     */     {
/* 141 */       throw new RuntimeException("Invalid method on invoke");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.statistics.impl.StatisticImpl
 * JD-Core Version:    0.6.2
 */