/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import com.sun.jmx.snmp.agent.SnmpStandardObjectServer;
/*     */ import java.io.Serializable;
/*     */ import java.lang.management.MemoryManagerMXBean;
/*     */ import java.lang.management.MemoryPoolMXBean;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import sun.management.snmp.jvmmib.JvmMemMgrPoolRelTableMeta;
/*     */ import sun.management.snmp.util.JvmContextFactory;
/*     */ import sun.management.snmp.util.MibLogger;
/*     */ import sun.management.snmp.util.SnmpCachedData;
/*     */ import sun.management.snmp.util.SnmpTableCache;
/*     */ import sun.management.snmp.util.SnmpTableHandler;
/*     */ 
/*     */ public class JvmMemMgrPoolRelTableMetaImpl extends JvmMemMgrPoolRelTableMeta
/*     */   implements Serializable
/*     */ {
/*     */   protected SnmpTableCache cache;
/* 300 */   private transient JvmMemManagerTableMetaImpl managers = null;
/* 301 */   private transient JvmMemPoolTableMetaImpl pools = null;
/*     */ 
/* 517 */   static final MibLogger log = new MibLogger(JvmMemMgrPoolRelTableMetaImpl.class);
/*     */ 
/*     */   public JvmMemMgrPoolRelTableMetaImpl(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/* 312 */     super(paramSnmpMib, paramSnmpStandardObjectServer);
/* 313 */     this.cache = new JvmMemMgrPoolRelTableCache(this, ((JVM_MANAGEMENT_MIB_IMPL)paramSnmpMib).validity());
/*     */   }
/*     */ 
/*     */   private final JvmMemManagerTableMetaImpl getManagers(SnmpMib paramSnmpMib)
/*     */   {
/* 322 */     if (this.managers == null) {
/* 323 */       this.managers = ((JvmMemManagerTableMetaImpl)paramSnmpMib.getRegisteredTableMeta("JvmMemManagerTable"));
/*     */     }
/*     */ 
/* 326 */     return this.managers;
/*     */   }
/*     */ 
/*     */   private final JvmMemPoolTableMetaImpl getPools(SnmpMib paramSnmpMib)
/*     */   {
/* 333 */     if (this.pools == null) {
/* 334 */       this.pools = ((JvmMemPoolTableMetaImpl)paramSnmpMib.getRegisteredTableMeta("JvmMemPoolTable"));
/*     */     }
/*     */ 
/* 337 */     return this.pools;
/*     */   }
/*     */ 
/*     */   protected SnmpTableHandler getManagerHandler(Object paramObject)
/*     */   {
/* 344 */     JvmMemManagerTableMetaImpl localJvmMemManagerTableMetaImpl = getManagers(this.theMib);
/* 345 */     return localJvmMemManagerTableMetaImpl.getHandler(paramObject);
/*     */   }
/*     */ 
/*     */   protected SnmpTableHandler getPoolHandler(Object paramObject)
/*     */   {
/* 352 */     JvmMemPoolTableMetaImpl localJvmMemPoolTableMetaImpl = getPools(this.theMib);
/* 353 */     return localJvmMemPoolTableMetaImpl.getHandler(paramObject);
/*     */   }
/*     */ 
/*     */   protected SnmpOid getNextOid(Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 360 */     return getNextOid(null, paramObject);
/*     */   }
/*     */ 
/*     */   protected SnmpOid getNextOid(SnmpOid paramSnmpOid, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 366 */     boolean bool = log.isDebugOn();
/* 367 */     if (bool) log.debug("getNextOid", "previous=" + paramSnmpOid);
/*     */ 
/* 372 */     SnmpTableHandler localSnmpTableHandler = getHandler(paramObject);
/* 373 */     if (localSnmpTableHandler == null)
/*     */     {
/* 377 */       if (bool) log.debug("getNextOid", "handler is null!");
/* 378 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 383 */     SnmpOid localSnmpOid = localSnmpTableHandler.getNext(paramSnmpOid);
/* 384 */     if (bool) log.debug("getNextOid", "next=" + localSnmpOid);
/*     */ 
/* 388 */     if (localSnmpOid == null) {
/* 389 */       throw new SnmpStatusException(224);
/*     */     }
/* 391 */     return localSnmpOid;
/*     */   }
/*     */ 
/*     */   protected boolean contains(SnmpOid paramSnmpOid, Object paramObject)
/*     */   {
/* 400 */     SnmpTableHandler localSnmpTableHandler = getHandler(paramObject);
/*     */ 
/* 404 */     if (localSnmpTableHandler == null) {
/* 405 */       return false;
/*     */     }
/* 407 */     return localSnmpTableHandler.contains(paramSnmpOid);
/*     */   }
/*     */ 
/*     */   public Object getEntry(SnmpOid paramSnmpOid)
/*     */     throws SnmpStatusException
/*     */   {
/* 414 */     if ((paramSnmpOid == null) || (paramSnmpOid.getLength() < 2)) {
/* 415 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 419 */     Map localMap = JvmContextFactory.getUserData();
/*     */ 
/* 426 */     long l1 = paramSnmpOid.getOidArc(0);
/* 427 */     long l2 = paramSnmpOid.getOidArc(1);
/*     */ 
/* 435 */     String str = "JvmMemMgrPoolRelTable.entry." + l1 + "." + l2;
/*     */ 
/* 441 */     if (localMap != null) {
/* 442 */       localObject1 = localMap.get(str);
/* 443 */       if (localObject1 != null) return localObject1;
/*     */ 
/*     */     }
/*     */ 
/* 450 */     Object localObject1 = getHandler(localMap);
/*     */ 
/* 454 */     if (localObject1 == null) {
/* 455 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 459 */     Object localObject2 = ((SnmpTableHandler)localObject1).getData(paramSnmpOid);
/*     */ 
/* 463 */     if (!(localObject2 instanceof JvmMemMgrPoolRelEntryImpl)) {
/* 464 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 469 */     JvmMemMgrPoolRelEntryImpl localJvmMemMgrPoolRelEntryImpl = (JvmMemMgrPoolRelEntryImpl)localObject2;
/*     */ 
/* 477 */     if ((localMap != null) && (localJvmMemMgrPoolRelEntryImpl != null)) {
/* 478 */       localMap.put(str, localJvmMemMgrPoolRelEntryImpl);
/*     */     }
/*     */ 
/* 481 */     return localJvmMemMgrPoolRelEntryImpl;
/*     */   }
/*     */ 
/*     */   protected SnmpTableHandler getHandler(Object paramObject)
/*     */   {
/* 498 */     Map localMap;
/* 498 */     if ((paramObject instanceof Map)) localMap = (Map)Util.cast(paramObject); else {
/* 499 */       localMap = null;
/*     */     }
/*     */ 
/* 502 */     if (localMap != null) {
/* 503 */       localSnmpTableHandler = (SnmpTableHandler)localMap.get("JvmMemMgrPoolRelTable.handler");
/*     */ 
/* 505 */       if (localSnmpTableHandler != null) return localSnmpTableHandler;
/*     */ 
/*     */     }
/*     */ 
/* 509 */     SnmpTableHandler localSnmpTableHandler = this.cache.getTableHandler();
/*     */ 
/* 511 */     if ((localMap != null) && (localSnmpTableHandler != null)) {
/* 512 */       localMap.put("JvmMemMgrPoolRelTable.handler", localSnmpTableHandler);
/*     */     }
/* 514 */     return localSnmpTableHandler;
/*     */   }
/*     */ 
/*     */   private static class JvmMemMgrPoolRelTableCache extends SnmpTableCache
/*     */   {
/*     */     private final JvmMemMgrPoolRelTableMetaImpl meta;
/*     */ 
/*     */     JvmMemMgrPoolRelTableCache(JvmMemMgrPoolRelTableMetaImpl paramJvmMemMgrPoolRelTableMetaImpl, long paramLong)
/*     */     {
/*  82 */       this.validity = paramLong;
/*  83 */       this.meta = paramJvmMemMgrPoolRelTableMetaImpl;
/*     */     }
/*     */ 
/*     */     public SnmpTableHandler getTableHandler()
/*     */     {
/*  90 */       Map localMap = JvmContextFactory.getUserData();
/*  91 */       return getTableDatas(localMap);
/*     */     }
/*     */ 
/*     */     private static Map<String, SnmpOid> buildPoolIndexMap(SnmpTableHandler paramSnmpTableHandler)
/*     */     {
/* 100 */       if ((paramSnmpTableHandler instanceof SnmpCachedData)) {
/* 101 */         return buildPoolIndexMap((SnmpCachedData)paramSnmpTableHandler);
/*     */       }
/*     */ 
/* 104 */       HashMap localHashMap = new HashMap();
/* 105 */       SnmpOid localSnmpOid = null;
/* 106 */       while ((localSnmpOid = paramSnmpTableHandler.getNext(localSnmpOid)) != null) {
/* 107 */         MemoryPoolMXBean localMemoryPoolMXBean = (MemoryPoolMXBean)paramSnmpTableHandler.getData(localSnmpOid);
/*     */ 
/* 109 */         if (localMemoryPoolMXBean != null) {
/* 110 */           String str = localMemoryPoolMXBean.getName();
/* 111 */           if (str != null)
/* 112 */             localHashMap.put(str, localSnmpOid); 
/*     */         }
/*     */       }
/* 114 */       return localHashMap;
/*     */     }
/*     */ 
/*     */     private static Map<String, SnmpOid> buildPoolIndexMap(SnmpCachedData paramSnmpCachedData)
/*     */     {
/* 123 */       if (paramSnmpCachedData == null) return Collections.emptyMap();
/* 124 */       SnmpOid[] arrayOfSnmpOid = paramSnmpCachedData.indexes;
/* 125 */       Object[] arrayOfObject = paramSnmpCachedData.datas;
/* 126 */       int i = arrayOfSnmpOid.length;
/* 127 */       HashMap localHashMap = new HashMap(i);
/* 128 */       for (int j = 0; j < i; j++) {
/* 129 */         SnmpOid localSnmpOid = arrayOfSnmpOid[j];
/* 130 */         if (localSnmpOid != null) {
/* 131 */           MemoryPoolMXBean localMemoryPoolMXBean = (MemoryPoolMXBean)arrayOfObject[j];
/*     */ 
/* 133 */           if (localMemoryPoolMXBean != null) {
/* 134 */             String str = localMemoryPoolMXBean.getName();
/* 135 */             if (str != null)
/* 136 */               localHashMap.put(str, localSnmpOid); 
/*     */           }
/*     */         }
/*     */       }
/* 138 */       return localHashMap;
/*     */     }
/*     */ 
/*     */     protected SnmpCachedData updateCachedDatas(Object paramObject)
/*     */     {
/* 157 */       SnmpTableHandler localSnmpTableHandler1 = this.meta.getManagerHandler(paramObject);
/*     */ 
/* 161 */       SnmpTableHandler localSnmpTableHandler2 = this.meta.getPoolHandler(paramObject);
/*     */ 
/* 165 */       long l = System.currentTimeMillis();
/*     */ 
/* 168 */       Map localMap = buildPoolIndexMap(localSnmpTableHandler2);
/*     */ 
/* 173 */       TreeMap localTreeMap = new TreeMap(SnmpCachedData.oidComparator);
/*     */ 
/* 175 */       updateTreeMap(localTreeMap, paramObject, localSnmpTableHandler1, localSnmpTableHandler2, localMap);
/*     */ 
/* 177 */       return new SnmpCachedData(l, localTreeMap);
/*     */     }
/*     */ 
/*     */     protected String[] getMemoryPools(Object paramObject, MemoryManagerMXBean paramMemoryManagerMXBean, long paramLong)
/*     */     {
/* 187 */       String str = "JvmMemManager." + paramLong + ".getMemoryPools";
/*     */ 
/* 190 */       String[] arrayOfString = null;
/* 191 */       if ((paramObject instanceof Map)) {
/* 192 */         arrayOfString = (String[])((Map)paramObject).get(str);
/* 193 */         if (arrayOfString != null) return arrayOfString;
/*     */       }
/*     */ 
/* 196 */       if (paramMemoryManagerMXBean != null) {
/* 197 */         arrayOfString = paramMemoryManagerMXBean.getMemoryPoolNames();
/*     */       }
/* 199 */       if ((arrayOfString != null) && ((paramObject instanceof Map))) {
/* 200 */         Map localMap = (Map)Util.cast(paramObject);
/* 201 */         localMap.put(str, arrayOfString);
/*     */       }
/*     */ 
/* 204 */       return arrayOfString;
/*     */     }
/*     */ 
/*     */     protected void updateTreeMap(TreeMap<SnmpOid, Object> paramTreeMap, Object paramObject, MemoryManagerMXBean paramMemoryManagerMXBean, SnmpOid paramSnmpOid, Map paramMap)
/*     */     {
/*     */       long l1;
/*     */       try
/*     */       {
/* 216 */         l1 = paramSnmpOid.getOidArc(0);
/*     */       } catch (SnmpStatusException localSnmpStatusException1) {
/* 218 */         JvmMemMgrPoolRelTableMetaImpl.log.debug("updateTreeMap", "Bad MemoryManager OID index: " + paramSnmpOid);
/*     */ 
/* 220 */         JvmMemMgrPoolRelTableMetaImpl.log.debug("updateTreeMap", localSnmpStatusException1);
/* 221 */         return;
/*     */       }
/*     */ 
/* 226 */       String[] arrayOfString = getMemoryPools(paramObject, paramMemoryManagerMXBean, l1);
/* 227 */       if ((arrayOfString == null) || (arrayOfString.length < 1)) return;
/*     */ 
/* 229 */       String str1 = paramMemoryManagerMXBean.getName();
/* 230 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 231 */         String str2 = arrayOfString[i];
/* 232 */         if (str2 != null) {
/* 233 */           SnmpOid localSnmpOid1 = (SnmpOid)paramMap.get(str2);
/* 234 */           if (localSnmpOid1 != null)
/*     */           {
/*     */             long l2;
/*     */             try
/*     */             {
/* 240 */               l2 = localSnmpOid1.getOidArc(0);
/*     */             } catch (SnmpStatusException localSnmpStatusException2) {
/* 242 */               JvmMemMgrPoolRelTableMetaImpl.log.debug("updateTreeMap", "Bad MemoryPool OID index: " + localSnmpOid1);
/*     */ 
/* 244 */               JvmMemMgrPoolRelTableMetaImpl.log.debug("updateTreeMap", localSnmpStatusException2);
/* 245 */               continue;
/*     */             }
/*     */ 
/* 250 */             long[] arrayOfLong = { l1, l2 };
/*     */ 
/* 252 */             SnmpOid localSnmpOid2 = new SnmpOid(arrayOfLong);
/*     */ 
/* 254 */             paramTreeMap.put(localSnmpOid2, new JvmMemMgrPoolRelEntryImpl(str1, str2, (int)l1, (int)l2));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     protected void updateTreeMap(TreeMap<SnmpOid, Object> paramTreeMap, Object paramObject, SnmpTableHandler paramSnmpTableHandler1, SnmpTableHandler paramSnmpTableHandler2, Map paramMap)
/*     */     {
/* 265 */       if ((paramSnmpTableHandler1 instanceof SnmpCachedData)) {
/* 266 */         updateTreeMap(paramTreeMap, paramObject, (SnmpCachedData)paramSnmpTableHandler1, paramSnmpTableHandler2, paramMap);
/*     */ 
/* 268 */         return;
/*     */       }
/*     */ 
/* 271 */       SnmpOid localSnmpOid = null;
/* 272 */       while ((localSnmpOid = paramSnmpTableHandler1.getNext(localSnmpOid)) != null) {
/* 273 */         MemoryManagerMXBean localMemoryManagerMXBean = (MemoryManagerMXBean)paramSnmpTableHandler1.getData(localSnmpOid);
/*     */ 
/* 275 */         if (localMemoryManagerMXBean != null)
/* 276 */           updateTreeMap(paramTreeMap, paramObject, localMemoryManagerMXBean, localSnmpOid, paramMap);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected void updateTreeMap(TreeMap<SnmpOid, Object> paramTreeMap, Object paramObject, SnmpCachedData paramSnmpCachedData, SnmpTableHandler paramSnmpTableHandler, Map paramMap)
/*     */     {
/* 285 */       SnmpOid[] arrayOfSnmpOid = paramSnmpCachedData.indexes;
/* 286 */       Object[] arrayOfObject = paramSnmpCachedData.datas;
/* 287 */       int i = arrayOfSnmpOid.length;
/* 288 */       for (int j = i - 1; j > -1; j--) {
/* 289 */         MemoryManagerMXBean localMemoryManagerMXBean = (MemoryManagerMXBean)arrayOfObject[j];
/*     */ 
/* 291 */         if (localMemoryManagerMXBean != null)
/* 292 */           updateTreeMap(paramTreeMap, paramObject, localMemoryManagerMXBean, arrayOfSnmpOid[j], paramMap);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmMemMgrPoolRelTableMetaImpl
 * JD-Core Version:    0.6.2
 */