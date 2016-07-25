/*     */ package sun.management.snmp.util;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public abstract class SnmpListTableCache extends SnmpTableCache
/*     */ {
/*     */   protected abstract SnmpOid getIndex(Object paramObject1, List paramList, int paramInt, Object paramObject2);
/*     */ 
/*     */   protected Object getData(Object paramObject1, List paramList, int paramInt, Object paramObject2)
/*     */   {
/*  80 */     return paramObject2;
/*     */   }
/*     */ 
/*     */   protected SnmpCachedData updateCachedDatas(Object paramObject, List paramList)
/*     */   {
/*  99 */     int i = paramList == null ? 0 : paramList.size();
/* 100 */     if (i == 0) return null;
/*     */ 
/* 102 */     long l = System.currentTimeMillis();
/* 103 */     Iterator localIterator = paramList.iterator();
/* 104 */     TreeMap localTreeMap = new TreeMap(SnmpCachedData.oidComparator);
/*     */ 
/* 106 */     for (int j = 0; localIterator.hasNext(); j++) {
/* 107 */       Object localObject1 = localIterator.next();
/* 108 */       SnmpOid localSnmpOid = getIndex(paramObject, paramList, j, localObject1);
/* 109 */       Object localObject2 = getData(paramObject, paramList, j, localObject1);
/* 110 */       if (localSnmpOid != null) {
/* 111 */         localTreeMap.put(localSnmpOid, localObject2);
/*     */       }
/*     */     }
/* 114 */     return new SnmpCachedData(l, localTreeMap);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.util.SnmpListTableCache
 * JD-Core Version:    0.6.2
 */