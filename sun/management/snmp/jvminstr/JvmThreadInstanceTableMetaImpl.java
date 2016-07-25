/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import com.sun.jmx.snmp.agent.SnmpStandardObjectServer;
/*     */ import java.lang.management.ThreadInfo;
/*     */ import java.lang.management.ThreadMXBean;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import sun.management.snmp.jvmmib.JvmThreadInstanceTableMeta;
/*     */ import sun.management.snmp.util.JvmContextFactory;
/*     */ import sun.management.snmp.util.MibLogger;
/*     */ import sun.management.snmp.util.SnmpCachedData;
/*     */ import sun.management.snmp.util.SnmpTableCache;
/*     */ import sun.management.snmp.util.SnmpTableHandler;
/*     */ 
/*     */ public class JvmThreadInstanceTableMetaImpl extends JvmThreadInstanceTableMeta
/*     */ {
/*     */   public static final int MAX_STACK_TRACE_DEPTH = 0;
/*     */   protected SnmpTableCache cache;
/* 399 */   static final MibLogger log = new MibLogger(JvmThreadInstanceTableMetaImpl.class);
/*     */ 
/*     */   static SnmpOid makeOid(long paramLong)
/*     */   {
/*  98 */     long[] arrayOfLong = new long[8];
/*  99 */     arrayOfLong[0] = (paramLong >> 56 & 0xFF);
/* 100 */     arrayOfLong[1] = (paramLong >> 48 & 0xFF);
/* 101 */     arrayOfLong[2] = (paramLong >> 40 & 0xFF);
/* 102 */     arrayOfLong[3] = (paramLong >> 32 & 0xFF);
/* 103 */     arrayOfLong[4] = (paramLong >> 24 & 0xFF);
/* 104 */     arrayOfLong[5] = (paramLong >> 16 & 0xFF);
/* 105 */     arrayOfLong[6] = (paramLong >> 8 & 0xFF);
/* 106 */     arrayOfLong[7] = (paramLong & 0xFF);
/* 107 */     return new SnmpOid(arrayOfLong);
/*     */   }
/*     */ 
/*     */   static long makeId(SnmpOid paramSnmpOid)
/*     */   {
/* 116 */     long l = 0L;
/* 117 */     long[] arrayOfLong = paramSnmpOid.longValue(false);
/*     */ 
/* 119 */     l |= arrayOfLong[0] << 56;
/* 120 */     l |= arrayOfLong[1] << 48;
/* 121 */     l |= arrayOfLong[2] << 40;
/* 122 */     l |= arrayOfLong[3] << 32;
/* 123 */     l |= arrayOfLong[4] << 24;
/* 124 */     l |= arrayOfLong[5] << 16;
/* 125 */     l |= arrayOfLong[6] << 8;
/* 126 */     l |= arrayOfLong[7];
/*     */ 
/* 128 */     return l;
/*     */   }
/*     */ 
/*     */   public JvmThreadInstanceTableMetaImpl(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/* 200 */     super(paramSnmpMib, paramSnmpStandardObjectServer);
/* 201 */     this.cache = new JvmThreadInstanceTableCache(this, ((JVM_MANAGEMENT_MIB_IMPL)paramSnmpMib).validity());
/*     */ 
/* 203 */     log.debug("JvmThreadInstanceTableMetaImpl", "Create Thread meta");
/*     */   }
/*     */ 
/*     */   protected SnmpOid getNextOid(Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 209 */     log.debug("JvmThreadInstanceTableMetaImpl", "getNextOid");
/*     */ 
/* 211 */     return getNextOid(null, paramObject);
/*     */   }
/*     */ 
/*     */   protected SnmpOid getNextOid(SnmpOid paramSnmpOid, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 217 */     log.debug("getNextOid", "previous=" + paramSnmpOid);
/*     */ 
/* 222 */     SnmpTableHandler localSnmpTableHandler = getHandler(paramObject);
/* 223 */     if (localSnmpTableHandler == null)
/*     */     {
/* 227 */       log.debug("getNextOid", "handler is null!");
/* 228 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 233 */     SnmpOid localSnmpOid = paramSnmpOid;
/*     */     while (true) {
/* 235 */       localSnmpOid = localSnmpTableHandler.getNext(localSnmpOid);
/* 236 */       if (localSnmpOid != null) {
/* 237 */         if (getJvmThreadInstance(paramObject, localSnmpOid) != null) break;
/*     */       }
/*     */     }
/* 240 */     log.debug("*** **** **** **** getNextOid", "next=" + localSnmpOid);
/*     */ 
/* 244 */     if (localSnmpOid == null) {
/* 245 */       throw new SnmpStatusException(224);
/*     */     }
/* 247 */     return localSnmpOid;
/*     */   }
/*     */ 
/*     */   protected boolean contains(SnmpOid paramSnmpOid, Object paramObject)
/*     */   {
/* 255 */     SnmpTableHandler localSnmpTableHandler = getHandler(paramObject);
/*     */ 
/* 259 */     if (localSnmpTableHandler == null)
/* 260 */       return false;
/* 261 */     if (!localSnmpTableHandler.contains(paramSnmpOid)) {
/* 262 */       return false;
/*     */     }
/* 264 */     JvmThreadInstanceEntryImpl localJvmThreadInstanceEntryImpl = getJvmThreadInstance(paramObject, paramSnmpOid);
/* 265 */     return localJvmThreadInstanceEntryImpl != null;
/*     */   }
/*     */ 
/*     */   public Object getEntry(SnmpOid paramSnmpOid)
/*     */     throws SnmpStatusException
/*     */   {
/* 272 */     log.debug("*** **** **** **** getEntry", "oid [" + paramSnmpOid + "]");
/* 273 */     if ((paramSnmpOid == null) || (paramSnmpOid.getLength() != 8)) {
/* 274 */       log.debug("getEntry", "Invalid oid [" + paramSnmpOid + "]");
/* 275 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 280 */     Map localMap = JvmContextFactory.getUserData();
/*     */ 
/* 284 */     SnmpTableHandler localSnmpTableHandler = getHandler(localMap);
/*     */ 
/* 288 */     if ((localSnmpTableHandler == null) || (!localSnmpTableHandler.contains(paramSnmpOid))) {
/* 289 */       throw new SnmpStatusException(224);
/*     */     }
/* 291 */     JvmThreadInstanceEntryImpl localJvmThreadInstanceEntryImpl = getJvmThreadInstance(localMap, paramSnmpOid);
/*     */ 
/* 293 */     if (localJvmThreadInstanceEntryImpl == null) {
/* 294 */       throw new SnmpStatusException(224);
/*     */     }
/* 296 */     return localJvmThreadInstanceEntryImpl;
/*     */   }
/*     */ 
/*     */   protected SnmpTableHandler getHandler(Object paramObject)
/*     */   {
/* 313 */     Map localMap;
/* 313 */     if ((paramObject instanceof Map)) localMap = (Map)Util.cast(paramObject); else {
/* 314 */       localMap = null;
/*     */     }
/*     */ 
/* 317 */     if (localMap != null) {
/* 318 */       localSnmpTableHandler = (SnmpTableHandler)localMap.get("JvmThreadInstanceTable.handler");
/*     */ 
/* 320 */       if (localSnmpTableHandler != null) return localSnmpTableHandler;
/*     */ 
/*     */     }
/*     */ 
/* 324 */     SnmpTableHandler localSnmpTableHandler = this.cache.getTableHandler();
/*     */ 
/* 326 */     if ((localMap != null) && (localSnmpTableHandler != null)) {
/* 327 */       localMap.put("JvmThreadInstanceTable.handler", localSnmpTableHandler);
/*     */     }
/* 329 */     return localSnmpTableHandler;
/*     */   }
/*     */ 
/*     */   private ThreadInfo getThreadInfo(long paramLong) {
/* 333 */     return JvmThreadingImpl.getThreadMXBean().getThreadInfo(paramLong, 0);
/*     */   }
/*     */ 
/*     */   private ThreadInfo getThreadInfo(SnmpOid paramSnmpOid)
/*     */   {
/* 338 */     return getThreadInfo(makeId(paramSnmpOid));
/*     */   }
/*     */ 
/*     */   private JvmThreadInstanceEntryImpl getJvmThreadInstance(Object paramObject, SnmpOid paramSnmpOid)
/*     */   {
/* 343 */     JvmThreadInstanceEntryImpl localJvmThreadInstanceEntryImpl = null;
/* 344 */     String str = null;
/* 345 */     Map localMap = null;
/* 346 */     boolean bool = log.isDebugOn();
/*     */ 
/* 348 */     if ((paramObject instanceof Map)) {
/* 349 */       localMap = (Map)Util.cast(paramObject);
/*     */ 
/* 357 */       str = "JvmThreadInstanceTable.entry." + paramSnmpOid.toString();
/*     */ 
/* 359 */       localJvmThreadInstanceEntryImpl = (JvmThreadInstanceEntryImpl)localMap.get(str);
/*     */     }
/*     */ 
/* 364 */     if (localJvmThreadInstanceEntryImpl != null) {
/* 365 */       if (bool) log.debug("*** getJvmThreadInstance", "Entry found in cache: " + str);
/*     */ 
/* 367 */       return localJvmThreadInstanceEntryImpl;
/*     */     }
/*     */ 
/* 370 */     if (bool) log.debug("*** getJvmThreadInstance", "Entry [" + paramSnmpOid + "] is not in cache");
/*     */ 
/* 375 */     ThreadInfo localThreadInfo = null;
/*     */     try {
/* 377 */       localThreadInfo = getThreadInfo(paramSnmpOid);
/*     */     } catch (RuntimeException localRuntimeException) {
/* 379 */       log.trace("*** getJvmThreadInstance", "Failed to get thread info for rowOid: " + paramSnmpOid);
/*     */ 
/* 381 */       log.debug("*** getJvmThreadInstance", localRuntimeException);
/*     */     }
/*     */ 
/* 386 */     if (localThreadInfo == null) {
/* 387 */       if (bool) log.debug("*** getJvmThreadInstance", "No entry by that oid [" + paramSnmpOid + "]");
/*     */ 
/* 389 */       return null;
/*     */     }
/*     */ 
/* 392 */     localJvmThreadInstanceEntryImpl = new JvmThreadInstanceEntryImpl(localThreadInfo, paramSnmpOid.toByte());
/* 393 */     if (localMap != null) localMap.put(str, localJvmThreadInstanceEntryImpl);
/* 394 */     if (bool) log.debug("*** getJvmThreadInstance", "Entry created for Thread OID [" + paramSnmpOid + "]");
/*     */ 
/* 396 */     return localJvmThreadInstanceEntryImpl;
/*     */   }
/*     */ 
/*     */   private static class JvmThreadInstanceTableCache extends SnmpTableCache
/*     */   {
/*     */     private final JvmThreadInstanceTableMetaImpl meta;
/*     */ 
/*     */     JvmThreadInstanceTableCache(JvmThreadInstanceTableMetaImpl paramJvmThreadInstanceTableMetaImpl, long paramLong)
/*     */     {
/* 146 */       this.validity = paramLong;
/* 147 */       this.meta = paramJvmThreadInstanceTableMetaImpl;
/*     */     }
/*     */ 
/*     */     public SnmpTableHandler getTableHandler()
/*     */     {
/* 154 */       Map localMap = JvmContextFactory.getUserData();
/* 155 */       return getTableDatas(localMap);
/*     */     }
/*     */ 
/*     */     protected SnmpCachedData updateCachedDatas(Object paramObject)
/*     */     {
/* 167 */       long[] arrayOfLong = JvmThreadingImpl.getThreadMXBean().getAllThreadIds();
/*     */ 
/* 171 */       long l = System.currentTimeMillis();
/*     */ 
/* 173 */       SnmpOid[] arrayOfSnmpOid = new SnmpOid[arrayOfLong.length];
/* 174 */       TreeMap localTreeMap = new TreeMap(SnmpCachedData.oidComparator);
/*     */ 
/* 176 */       for (int i = 0; i < arrayOfLong.length; i++) {
/* 177 */         JvmThreadInstanceTableMetaImpl.log.debug("", "Making index for thread id [" + arrayOfLong[i] + "]");
/*     */ 
/* 179 */         SnmpOid localSnmpOid = JvmThreadInstanceTableMetaImpl.makeOid(arrayOfLong[i]);
/* 180 */         localTreeMap.put(localSnmpOid, localSnmpOid);
/*     */       }
/*     */ 
/* 183 */       return new SnmpCachedData(l, localTreeMap);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmThreadInstanceTableMetaImpl
 * JD-Core Version:    0.6.2
 */