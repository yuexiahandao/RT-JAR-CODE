/*     */ package sun.management.snmp.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.ref.WeakReference;
/*     */ 
/*     */ public abstract class SnmpTableCache
/*     */   implements Serializable
/*     */ {
/*     */   protected long validity;
/*     */   protected transient WeakReference<SnmpCachedData> datas;
/*     */ 
/*     */   protected boolean isObsolete(SnmpCachedData paramSnmpCachedData)
/*     */   {
/*  65 */     if (paramSnmpCachedData == null) return true;
/*  66 */     if (this.validity < 0L) return false;
/*  67 */     return System.currentTimeMillis() - paramSnmpCachedData.lastUpdated > this.validity;
/*     */   }
/*     */ 
/*     */   protected SnmpCachedData getCachedDatas()
/*     */   {
/*  77 */     if (this.datas == null) return null;
/*  78 */     SnmpCachedData localSnmpCachedData = (SnmpCachedData)this.datas.get();
/*  79 */     if ((localSnmpCachedData == null) || (isObsolete(localSnmpCachedData))) return null;
/*  80 */     return localSnmpCachedData;
/*     */   }
/*     */ 
/*     */   protected synchronized SnmpCachedData getTableDatas(Object paramObject)
/*     */   {
/*  98 */     SnmpCachedData localSnmpCachedData1 = getCachedDatas();
/*  99 */     if (localSnmpCachedData1 != null) return localSnmpCachedData1;
/* 100 */     SnmpCachedData localSnmpCachedData2 = updateCachedDatas(paramObject);
/* 101 */     if (this.validity != 0L) this.datas = new WeakReference(localSnmpCachedData2);
/* 102 */     return localSnmpCachedData2;
/*     */   }
/*     */ 
/*     */   protected abstract SnmpCachedData updateCachedDatas(Object paramObject);
/*     */ 
/*     */   public abstract SnmpTableHandler getTableHandler();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.util.SnmpTableCache
 * JD-Core Version:    0.6.2
 */