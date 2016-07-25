/*     */ package sun.management.snmp.util;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public final class SnmpLoadedClassData extends SnmpCachedData
/*     */ {
/*     */   public SnmpLoadedClassData(long paramLong, TreeMap<SnmpOid, Object> paramTreeMap)
/*     */   {
/*  56 */     super(paramLong, paramTreeMap, false);
/*     */   }
/*     */ 
/*     */   public final Object getData(SnmpOid paramSnmpOid)
/*     */   {
/*  62 */     int i = 0;
/*     */     try
/*     */     {
/*  65 */       i = (int)paramSnmpOid.getOidArc(0);
/*     */     } catch (SnmpStatusException localSnmpStatusException) {
/*  67 */       return null;
/*     */     }
/*     */ 
/*  70 */     if (i >= this.datas.length) return null;
/*  71 */     return this.datas[i];
/*     */   }
/*     */ 
/*     */   public final SnmpOid getNext(SnmpOid paramSnmpOid)
/*     */   {
/*  76 */     int i = 0;
/*  77 */     if ((paramSnmpOid == null) && 
/*  78 */       (this.datas != null) && (this.datas.length >= 1))
/*  79 */       return new SnmpOid(0L);
/*     */     try
/*     */     {
/*  82 */       i = (int)paramSnmpOid.getOidArc(0);
/*     */     } catch (SnmpStatusException localSnmpStatusException) {
/*  84 */       return null;
/*     */     }
/*     */ 
/*  87 */     if (i < this.datas.length - 1) {
/*  88 */       return new SnmpOid(i + 1);
/*     */     }
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   public final boolean contains(SnmpOid paramSnmpOid)
/*     */   {
/*  95 */     int i = 0;
/*     */     try
/*     */     {
/*  98 */       i = (int)paramSnmpOid.getOidArc(0);
/*     */     } catch (SnmpStatusException localSnmpStatusException) {
/* 100 */       return false;
/*     */     }
/*     */ 
/* 103 */     return i < this.datas.length;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.util.SnmpLoadedClassData
 * JD-Core Version:    0.6.2
 */