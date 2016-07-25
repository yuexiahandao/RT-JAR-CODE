/*     */ package sun.management.snmp.util;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public abstract class SnmpNamedListTableCache extends SnmpListTableCache
/*     */ {
/*  58 */   protected TreeMap names = new TreeMap();
/*     */ 
/*  63 */   protected long last = 0L;
/*     */ 
/*  68 */   boolean wrapped = false;
/*     */ 
/* 265 */   static final MibLogger log = new MibLogger(SnmpNamedListTableCache.class);
/*     */ 
/*     */   protected abstract String getKey(Object paramObject1, List paramList, int paramInt, Object paramObject2);
/*     */ 
/*     */   protected SnmpOid makeIndex(Object paramObject1, List paramList, int paramInt, Object paramObject2)
/*     */   {
/* 104 */     if (++this.last > 4294967295L)
/*     */     {
/* 106 */       log.debug("makeIndex", "Index wrapping...");
/* 107 */       this.last = 0L;
/* 108 */       this.wrapped = true;
/*     */     }
/*     */ 
/* 112 */     if (!this.wrapped) return new SnmpOid(this.last);
/*     */ 
/* 115 */     for (int i = 1; i < 4294967295L; i++) {
/* 116 */       if (++this.last > 4294967295L) this.last = 1L;
/* 117 */       SnmpOid localSnmpOid = new SnmpOid(this.last);
/*     */ 
/* 120 */       if (this.names == null) return localSnmpOid;
/* 121 */       if (!this.names.containsValue(localSnmpOid))
/*     */       {
/* 124 */         if (paramObject1 == null) return localSnmpOid;
/* 125 */         if (!((Map)paramObject1).containsValue(localSnmpOid))
/*     */         {
/* 128 */           return localSnmpOid;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 133 */     return null;
/*     */   }
/*     */ 
/*     */   protected SnmpOid getIndex(Object paramObject1, List paramList, int paramInt, Object paramObject2)
/*     */   {
/* 156 */     String str = getKey(paramObject1, paramList, paramInt, paramObject2);
/* 157 */     Object localObject = (this.names == null) || (str == null) ? null : this.names.get(str);
/* 158 */     SnmpOid localSnmpOid = localObject != null ? (SnmpOid)localObject : makeIndex(paramObject1, paramList, paramInt, paramObject2);
/*     */ 
/* 161 */     if ((paramObject1 != null) && (str != null) && (localSnmpOid != null)) {
/* 162 */       Map localMap = (Map)Util.cast(paramObject1);
/* 163 */       localMap.put(str, localSnmpOid);
/*     */     }
/* 165 */     log.debug("getIndex", "key=" + str + ", index=" + localSnmpOid);
/* 166 */     return localSnmpOid;
/*     */   }
/*     */ 
/*     */   protected SnmpCachedData updateCachedDatas(Object paramObject, List paramList)
/*     */   {
/* 178 */     TreeMap localTreeMap = new TreeMap();
/* 179 */     SnmpCachedData localSnmpCachedData = super.updateCachedDatas(paramObject, paramList);
/*     */ 
/* 181 */     this.names = localTreeMap;
/* 182 */     return localSnmpCachedData;
/*     */   }
/*     */ 
/*     */   protected abstract List loadRawDatas(Map paramMap);
/*     */ 
/*     */   protected abstract String getRawDatasKey();
/*     */ 
/*     */   protected List getRawDatas(Map<Object, Object> paramMap, String paramString)
/*     */   {
/* 216 */     List localList = null;
/*     */ 
/* 219 */     if (paramMap != null) {
/* 220 */       localList = (List)paramMap.get(paramString);
/*     */     }
/* 222 */     if (localList == null)
/*     */     {
/* 224 */       localList = loadRawDatas(paramMap);
/*     */ 
/* 228 */       if ((localList != null) && (paramMap != null)) {
/* 229 */         paramMap.put(paramString, localList);
/*     */       }
/*     */     }
/* 232 */     return localList;
/*     */   }
/*     */ 
/*     */   protected SnmpCachedData updateCachedDatas(Object paramObject)
/*     */   {
/* 249 */     Map localMap = (paramObject instanceof Map) ? (Map)Util.cast(paramObject) : null;
/*     */ 
/* 253 */     List localList = getRawDatas(localMap, getRawDatasKey());
/*     */ 
/* 255 */     log.debug("updateCachedDatas", "rawDatas.size()=" + (localList == null ? "<no data>" : new StringBuilder().append("").append(localList.size()).toString()));
/*     */ 
/* 258 */     TreeMap localTreeMap = new TreeMap();
/* 259 */     SnmpCachedData localSnmpCachedData = super.updateCachedDatas(localTreeMap, localList);
/*     */ 
/* 261 */     this.names = localTreeMap;
/* 262 */     return localSnmpCachedData;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.util.SnmpNamedListTableCache
 * JD-Core Version:    0.6.2
 */