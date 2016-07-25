/*     */ package sun.management.snmp.util;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class SnmpCachedData
/*     */   implements SnmpTableHandler
/*     */ {
/*  47 */   public static final Comparator<SnmpOid> oidComparator = new Comparator()
/*     */   {
/*     */     public int compare(SnmpOid paramAnonymousSnmpOid1, SnmpOid paramAnonymousSnmpOid2) {
/*  50 */       return paramAnonymousSnmpOid1.compareTo(paramAnonymousSnmpOid2);
/*     */     }
/*     */     public boolean equals(Object paramAnonymousObject1, Object paramAnonymousObject2) {
/*  53 */       if (paramAnonymousObject1 == paramAnonymousObject2) return true;
/*  54 */       return paramAnonymousObject1.equals(paramAnonymousObject2);
/*     */     }
/*  47 */   };
/*     */   public final long lastUpdated;
/*     */   public final SnmpOid[] indexes;
/*     */   public final Object[] datas;
/*     */ 
/*     */   public SnmpCachedData(long paramLong, SnmpOid[] paramArrayOfSnmpOid, Object[] paramArrayOfObject)
/*     */   {
/*  71 */     this.lastUpdated = paramLong;
/*  72 */     this.indexes = paramArrayOfSnmpOid;
/*  73 */     this.datas = paramArrayOfObject;
/*     */   }
/*     */ 
/*     */   public SnmpCachedData(long paramLong, TreeMap<SnmpOid, Object> paramTreeMap)
/*     */   {
/*  86 */     this(paramLong, paramTreeMap, true);
/*     */   }
/*     */ 
/*     */   public SnmpCachedData(long paramLong, TreeMap<SnmpOid, Object> paramTreeMap, boolean paramBoolean)
/*     */   {
/* 100 */     int i = paramTreeMap.size();
/* 101 */     this.lastUpdated = paramLong;
/* 102 */     this.indexes = new SnmpOid[i];
/* 103 */     this.datas = new Object[i];
/*     */ 
/* 105 */     if (paramBoolean) {
/* 106 */       paramTreeMap.keySet().toArray(this.indexes);
/* 107 */       paramTreeMap.values().toArray(this.datas);
/*     */     } else {
/* 109 */       paramTreeMap.values().toArray(this.datas);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int find(SnmpOid paramSnmpOid)
/*     */   {
/* 135 */     return Arrays.binarySearch(this.indexes, paramSnmpOid, oidComparator);
/*     */   }
/*     */ 
/*     */   public Object getData(SnmpOid paramSnmpOid)
/*     */   {
/* 140 */     int i = find(paramSnmpOid);
/* 141 */     if ((i < 0) || (i >= this.datas.length)) return null;
/* 142 */     return this.datas[i];
/*     */   }
/*     */ 
/*     */   public SnmpOid getNext(SnmpOid paramSnmpOid)
/*     */   {
/* 147 */     if (paramSnmpOid == null) {
/* 148 */       if (this.indexes.length > 0) return this.indexes[0];
/* 149 */       return null;
/*     */     }
/* 151 */     int i = find(paramSnmpOid);
/* 152 */     if (i > -1) {
/* 153 */       if (i < this.indexes.length - 1) return this.indexes[(i + 1)];
/* 154 */       return null;
/*     */     }
/* 156 */     int j = -i - 1;
/* 157 */     if ((j > -1) && (j < this.indexes.length))
/* 158 */       return this.indexes[j];
/* 159 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean contains(SnmpOid paramSnmpOid)
/*     */   {
/* 164 */     int i = find(paramSnmpOid);
/* 165 */     return (i > -1) && (i < this.indexes.length);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.util.SnmpCachedData
 * JD-Core Version:    0.6.2
 */