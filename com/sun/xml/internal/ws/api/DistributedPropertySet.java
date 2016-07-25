/*     */ package com.sun.xml.internal.ws.api;
/*     */ 
/*     */ import com.sun.istack.internal.FinalArrayList;
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class DistributedPropertySet extends PropertySet
/*     */ {
/*  79 */   private final FinalArrayList<PropertySet> satellites = new FinalArrayList();
/*     */ 
/*     */   public void addSatellite(@NotNull PropertySet satellite) {
/*  82 */     this.satellites.add(satellite);
/*     */   }
/*     */ 
/*     */   public void removeSatellite(@NotNull PropertySet satellite) {
/*  86 */     this.satellites.remove(satellite);
/*     */   }
/*     */ 
/*     */   public void copySatelliteInto(@NotNull DistributedPropertySet r) {
/*  90 */     r.satellites.addAll(this.satellites);
/*     */   }
/*     */   @Nullable
/*     */   public <T extends PropertySet> T getSatellite(Class<T> satelliteClass) {
/*  94 */     for (PropertySet child : this.satellites) {
/*  95 */       if (satelliteClass.isInstance(child)) {
/*  96 */         return (PropertySet)satelliteClass.cast(child);
/*     */       }
/*     */ 
/*  99 */       if (DistributedPropertySet.class.isInstance(child)) {
/* 100 */         PropertySet satellite = ((DistributedPropertySet)DistributedPropertySet.class.cast(child)).getSatellite(satelliteClass);
/* 101 */         if (satellite != null) {
/* 102 */           return satellite;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 107 */     return null;
/*     */   }
/*     */ 
/*     */   public Object get(Object key)
/*     */   {
/* 113 */     for (PropertySet child : this.satellites) {
/* 114 */       if (child.supports(key)) {
/* 115 */         return child.get(key);
/*     */       }
/*     */     }
/*     */ 
/* 119 */     return super.get(key);
/*     */   }
/*     */ 
/*     */   public Object put(String key, Object value)
/*     */   {
/* 125 */     for (PropertySet child : this.satellites) {
/* 126 */       if (child.supports(key)) {
/* 127 */         return child.put(key, value);
/*     */       }
/*     */     }
/*     */ 
/* 131 */     return super.put(key, value);
/*     */   }
/*     */ 
/*     */   public boolean supports(Object key)
/*     */   {
/* 137 */     for (PropertySet child : this.satellites) {
/* 138 */       if (child.supports(key)) {
/* 139 */         return true;
/*     */       }
/*     */     }
/* 142 */     return super.supports(key);
/*     */   }
/*     */ 
/*     */   public Object remove(Object key)
/*     */   {
/* 148 */     for (PropertySet child : this.satellites) {
/* 149 */       if (child.supports(key)) {
/* 150 */         return child.remove(key);
/*     */       }
/*     */     }
/* 153 */     return super.remove(key);
/*     */   }
/*     */ 
/*     */   void createEntrySet(Set<Map.Entry<String, Object>> core)
/*     */   {
/* 158 */     super.createEntrySet(core);
/* 159 */     for (PropertySet child : this.satellites)
/* 160 */       child.createEntrySet(core);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.DistributedPropertySet
 * JD-Core Version:    0.6.2
 */