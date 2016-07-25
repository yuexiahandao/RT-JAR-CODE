/*     */ package com.sun.management;
/*     */ 
/*     */ import java.lang.management.MemoryUsage;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import javax.management.openmbean.CompositeDataView;
/*     */ import javax.management.openmbean.CompositeType;
/*     */ import sun.management.GcInfoBuilder;
/*     */ import sun.management.GcInfoCompositeData;
/*     */ 
/*     */ public class GcInfo
/*     */   implements CompositeData, CompositeDataView
/*     */ {
/*     */   private final long index;
/*     */   private final long startTime;
/*     */   private final long endTime;
/*     */   private final Map<String, MemoryUsage> usageBeforeGc;
/*     */   private final Map<String, MemoryUsage> usageAfterGc;
/*     */   private final Object[] extAttributes;
/*     */   private final CompositeData cdata;
/*     */   private final GcInfoBuilder builder;
/*     */ 
/*     */   private GcInfo(GcInfoBuilder paramGcInfoBuilder, long paramLong1, long paramLong2, long paramLong3, MemoryUsage[] paramArrayOfMemoryUsage1, MemoryUsage[] paramArrayOfMemoryUsage2, Object[] paramArrayOfObject)
/*     */   {
/*  82 */     this.builder = paramGcInfoBuilder;
/*  83 */     this.index = paramLong1;
/*  84 */     this.startTime = paramLong2;
/*  85 */     this.endTime = paramLong3;
/*  86 */     String[] arrayOfString = paramGcInfoBuilder.getPoolNames();
/*  87 */     this.usageBeforeGc = new HashMap(arrayOfString.length);
/*  88 */     this.usageAfterGc = new HashMap(arrayOfString.length);
/*  89 */     for (int i = 0; i < arrayOfString.length; i++) {
/*  90 */       this.usageBeforeGc.put(arrayOfString[i], paramArrayOfMemoryUsage1[i]);
/*  91 */       this.usageAfterGc.put(arrayOfString[i], paramArrayOfMemoryUsage2[i]);
/*     */     }
/*  93 */     this.extAttributes = paramArrayOfObject;
/*  94 */     this.cdata = new GcInfoCompositeData(this, paramGcInfoBuilder, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   private GcInfo(CompositeData paramCompositeData) {
/*  98 */     GcInfoCompositeData.validateCompositeData(paramCompositeData);
/*     */ 
/* 100 */     this.index = GcInfoCompositeData.getId(paramCompositeData);
/* 101 */     this.startTime = GcInfoCompositeData.getStartTime(paramCompositeData);
/* 102 */     this.endTime = GcInfoCompositeData.getEndTime(paramCompositeData);
/* 103 */     this.usageBeforeGc = GcInfoCompositeData.getMemoryUsageBeforeGc(paramCompositeData);
/* 104 */     this.usageAfterGc = GcInfoCompositeData.getMemoryUsageAfterGc(paramCompositeData);
/* 105 */     this.extAttributes = null;
/* 106 */     this.builder = null;
/* 107 */     this.cdata = paramCompositeData;
/*     */   }
/*     */ 
/*     */   public long getId()
/*     */   {
/* 118 */     return this.index;
/*     */   }
/*     */ 
/*     */   public long getStartTime()
/*     */   {
/* 128 */     return this.startTime;
/*     */   }
/*     */ 
/*     */   public long getEndTime()
/*     */   {
/* 138 */     return this.endTime;
/*     */   }
/*     */ 
/*     */   public long getDuration()
/*     */   {
/* 147 */     return this.endTime - this.startTime;
/*     */   }
/*     */ 
/*     */   public Map<String, MemoryUsage> getMemoryUsageBeforeGc()
/*     */   {
/* 162 */     return Collections.unmodifiableMap(this.usageBeforeGc);
/*     */   }
/*     */ 
/*     */   public Map<String, MemoryUsage> getMemoryUsageAfterGc()
/*     */   {
/* 177 */     return Collections.unmodifiableMap(this.usageAfterGc);
/*     */   }
/*     */ 
/*     */   public static GcInfo from(CompositeData paramCompositeData)
/*     */   {
/* 224 */     if (paramCompositeData == null) {
/* 225 */       return null;
/*     */     }
/*     */ 
/* 228 */     if ((paramCompositeData instanceof GcInfoCompositeData)) {
/* 229 */       return ((GcInfoCompositeData)paramCompositeData).getGcInfo();
/*     */     }
/* 231 */     return new GcInfo(paramCompositeData);
/*     */   }
/*     */ 
/*     */   public boolean containsKey(String paramString)
/*     */   {
/* 238 */     return this.cdata.containsKey(paramString);
/*     */   }
/*     */ 
/*     */   public boolean containsValue(Object paramObject) {
/* 242 */     return this.cdata.containsValue(paramObject);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 246 */     return this.cdata.equals(paramObject);
/*     */   }
/*     */ 
/*     */   public Object get(String paramString) {
/* 250 */     return this.cdata.get(paramString);
/*     */   }
/*     */ 
/*     */   public Object[] getAll(String[] paramArrayOfString) {
/* 254 */     return this.cdata.getAll(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public CompositeType getCompositeType() {
/* 258 */     return this.cdata.getCompositeType();
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 262 */     return this.cdata.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 266 */     return this.cdata.toString();
/*     */   }
/*     */ 
/*     */   public Collection values() {
/* 270 */     return this.cdata.values();
/*     */   }
/*     */ 
/*     */   public CompositeData toCompositeData(CompositeType paramCompositeType)
/*     */   {
/* 286 */     return this.cdata;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.management.GcInfo
 * JD-Core Version:    0.6.2
 */