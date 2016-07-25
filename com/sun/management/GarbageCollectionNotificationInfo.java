/*     */ package com.sun.management;
/*     */ 
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import javax.management.openmbean.CompositeDataView;
/*     */ import javax.management.openmbean.CompositeType;
/*     */ import sun.management.GarbageCollectionNotifInfoCompositeData;
/*     */ 
/*     */ public class GarbageCollectionNotificationInfo
/*     */   implements CompositeDataView
/*     */ {
/*     */   private final String gcName;
/*     */   private final String gcAction;
/*     */   private final String gcCause;
/*     */   private final GcInfo gcInfo;
/*     */   private final CompositeData cdata;
/*     */   public static final String GARBAGE_COLLECTION_NOTIFICATION = "com.sun.management.gc.notification";
/*     */ 
/*     */   public GarbageCollectionNotificationInfo(String paramString1, String paramString2, String paramString3, GcInfo paramGcInfo)
/*     */   {
/* 118 */     if (paramString1 == null) {
/* 119 */       throw new NullPointerException("Null gcName");
/*     */     }
/* 121 */     if (paramString2 == null) {
/* 122 */       throw new NullPointerException("Null gcAction");
/*     */     }
/* 124 */     if (paramString3 == null) {
/* 125 */       throw new NullPointerException("Null gcCause");
/*     */     }
/* 127 */     this.gcName = paramString1;
/* 128 */     this.gcAction = paramString2;
/* 129 */     this.gcCause = paramString3;
/* 130 */     this.gcInfo = paramGcInfo;
/* 131 */     this.cdata = new GarbageCollectionNotifInfoCompositeData(this);
/*     */   }
/*     */ 
/*     */   GarbageCollectionNotificationInfo(CompositeData paramCompositeData) {
/* 135 */     GarbageCollectionNotifInfoCompositeData.validateCompositeData(paramCompositeData);
/*     */ 
/* 137 */     this.gcName = GarbageCollectionNotifInfoCompositeData.getGcName(paramCompositeData);
/* 138 */     this.gcAction = GarbageCollectionNotifInfoCompositeData.getGcAction(paramCompositeData);
/* 139 */     this.gcCause = GarbageCollectionNotifInfoCompositeData.getGcCause(paramCompositeData);
/* 140 */     this.gcInfo = GarbageCollectionNotifInfoCompositeData.getGcInfo(paramCompositeData);
/* 141 */     this.cdata = paramCompositeData;
/*     */   }
/*     */ 
/*     */   public String getGcName()
/*     */   {
/* 150 */     return this.gcName;
/*     */   }
/*     */ 
/*     */   public String getGcAction()
/*     */   {
/* 159 */     return this.gcAction;
/*     */   }
/*     */ 
/*     */   public String getGcCause()
/*     */   {
/* 168 */     return this.gcCause;
/*     */   }
/*     */ 
/*     */   public GcInfo getGcInfo()
/*     */   {
/* 178 */     return this.gcInfo;
/*     */   }
/*     */ 
/*     */   public static GarbageCollectionNotificationInfo from(CompositeData paramCompositeData)
/*     */   {
/* 222 */     if (paramCompositeData == null) {
/* 223 */       return null;
/*     */     }
/*     */ 
/* 226 */     if ((paramCompositeData instanceof GarbageCollectionNotifInfoCompositeData)) {
/* 227 */       return ((GarbageCollectionNotifInfoCompositeData)paramCompositeData).getGarbageCollectionNotifInfo();
/*     */     }
/* 229 */     return new GarbageCollectionNotificationInfo(paramCompositeData);
/*     */   }
/*     */ 
/*     */   public CompositeData toCompositeData(CompositeType paramCompositeType)
/*     */   {
/* 234 */     return this.cdata;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.management.GarbageCollectionNotificationInfo
 * JD-Core Version:    0.6.2
 */