/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import com.sun.jmx.snmp.agent.SnmpStandardObjectServer;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.MemoryPoolMXBean;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import sun.management.snmp.jvmmib.JvmMemPoolTableMeta;
/*     */ import sun.management.snmp.util.JvmContextFactory;
/*     */ import sun.management.snmp.util.MibLogger;
/*     */ import sun.management.snmp.util.SnmpNamedListTableCache;
/*     */ import sun.management.snmp.util.SnmpTableCache;
/*     */ import sun.management.snmp.util.SnmpTableHandler;
/*     */ 
/*     */ public class JvmMemPoolTableMetaImpl extends JvmMemPoolTableMeta
/*     */ {
/*     */   protected SnmpTableCache cache;
/* 305 */   static final MibLogger log = new MibLogger(JvmMemPoolTableMetaImpl.class);
/*     */ 
/*     */   public JvmMemPoolTableMetaImpl(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/* 130 */     super(paramSnmpMib, paramSnmpStandardObjectServer);
/* 131 */     this.cache = new JvmMemPoolTableCache(((JVM_MANAGEMENT_MIB_IMPL)paramSnmpMib).validity() * 30L);
/*     */   }
/*     */ 
/*     */   protected SnmpOid getNextOid(Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 141 */     return getNextOid(null, paramObject);
/*     */   }
/*     */ 
/*     */   protected SnmpOid getNextOid(SnmpOid paramSnmpOid, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 147 */     boolean bool = log.isDebugOn();
/*     */     try {
/* 149 */       if (bool) log.debug("getNextOid", "previous=" + paramSnmpOid);
/*     */ 
/* 154 */       SnmpTableHandler localSnmpTableHandler = getHandler(paramObject);
/* 155 */       if (localSnmpTableHandler == null)
/*     */       {
/* 159 */         if (bool) log.debug("getNextOid", "handler is null!");
/* 160 */         throw new SnmpStatusException(224);
/*     */       }
/*     */ 
/* 166 */       SnmpOid localSnmpOid = localSnmpTableHandler.getNext(paramSnmpOid);
/* 167 */       if (bool) log.debug("getNextOid", "next=" + localSnmpOid);
/*     */ 
/* 171 */       if (localSnmpOid == null) {
/* 172 */         throw new SnmpStatusException(224);
/*     */       }
/*     */ 
/* 175 */       return localSnmpOid;
/*     */     } catch (SnmpStatusException localSnmpStatusException) {
/* 177 */       if (bool) log.debug("getNextOid", "End of MIB View: " + localSnmpStatusException);
/* 178 */       throw localSnmpStatusException;
/*     */     } catch (RuntimeException localRuntimeException) {
/* 180 */       if (bool) log.debug("getNextOid", "Unexpected exception: " + localRuntimeException);
/* 181 */       if (bool) log.debug("getNextOid", localRuntimeException);
/* 182 */       throw localRuntimeException;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean contains(SnmpOid paramSnmpOid, Object paramObject)
/*     */   {
/* 192 */     SnmpTableHandler localSnmpTableHandler = getHandler(paramObject);
/*     */ 
/* 196 */     if (localSnmpTableHandler == null) {
/* 197 */       return false;
/*     */     }
/* 199 */     return localSnmpTableHandler.contains(paramSnmpOid);
/*     */   }
/*     */ 
/*     */   public Object getEntry(SnmpOid paramSnmpOid)
/*     */     throws SnmpStatusException
/*     */   {
/* 206 */     if (paramSnmpOid == null) {
/* 207 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 211 */     Map localMap = (Map)Util.cast(JvmContextFactory.getUserData());
/*     */ 
/* 216 */     long l = paramSnmpOid.getOidArc(0);
/*     */ 
/* 224 */     String str = "JvmMemPoolTable.entry." + l;
/*     */ 
/* 229 */     if (localMap != null) {
/* 230 */       localObject1 = localMap.get(str);
/* 231 */       if (localObject1 != null) return localObject1;
/*     */ 
/*     */     }
/*     */ 
/* 238 */     Object localObject1 = getHandler(localMap);
/*     */ 
/* 242 */     if (localObject1 == null) {
/* 243 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 247 */     Object localObject2 = ((SnmpTableHandler)localObject1).getData(paramSnmpOid);
/*     */ 
/* 251 */     if (localObject2 == null) {
/* 252 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 257 */     if (log.isDebugOn())
/* 258 */       log.debug("getEntry", "data is a: " + localObject2.getClass().getName());
/* 259 */     JvmMemPoolEntryImpl localJvmMemPoolEntryImpl = new JvmMemPoolEntryImpl((MemoryPoolMXBean)localObject2, (int)l);
/*     */ 
/* 265 */     if ((localMap != null) && (localJvmMemPoolEntryImpl != null)) {
/* 266 */       localMap.put(str, localJvmMemPoolEntryImpl);
/*     */     }
/*     */ 
/* 269 */     return localJvmMemPoolEntryImpl;
/*     */   }
/*     */ 
/*     */   protected SnmpTableHandler getHandler(Object paramObject)
/*     */   {
/* 286 */     Map localMap;
/* 286 */     if ((paramObject instanceof Map)) localMap = (Map)Util.cast(paramObject); else {
/* 287 */       localMap = null;
/*     */     }
/*     */ 
/* 290 */     if (localMap != null) {
/* 291 */       localSnmpTableHandler = (SnmpTableHandler)localMap.get("JvmMemPoolTable.handler");
/*     */ 
/* 293 */       if (localSnmpTableHandler != null) return localSnmpTableHandler;
/*     */ 
/*     */     }
/*     */ 
/* 297 */     SnmpTableHandler localSnmpTableHandler = this.cache.getTableHandler();
/*     */ 
/* 299 */     if ((localMap != null) && (localSnmpTableHandler != null)) {
/* 300 */       localMap.put("JvmMemPoolTable.handler", localSnmpTableHandler);
/*     */     }
/* 302 */     return localSnmpTableHandler;
/*     */   }
/*     */ 
/*     */   private static class JvmMemPoolTableCache extends SnmpNamedListTableCache
/*     */   {
/*     */     JvmMemPoolTableCache(long paramLong)
/*     */     {
/*  74 */       this.validity = paramLong;
/*     */     }
/*     */ 
/*     */     protected String getKey(Object paramObject1, List paramList, int paramInt, Object paramObject2)
/*     */     {
/*  91 */       if (paramObject2 == null) return null;
/*  92 */       String str = ((MemoryPoolMXBean)paramObject2).getName();
/*  93 */       JvmMemPoolTableMetaImpl.log.debug("getKey", "key=" + str);
/*  94 */       return str;
/*     */     }
/*     */ 
/*     */     public SnmpTableHandler getTableHandler()
/*     */     {
/* 101 */       Map localMap = JvmContextFactory.getUserData();
/* 102 */       return getTableDatas(localMap);
/*     */     }
/*     */ 
/*     */     protected String getRawDatasKey()
/*     */     {
/* 109 */       return "JvmMemManagerTable.getMemoryPools";
/*     */     }
/*     */ 
/*     */     protected List loadRawDatas(Map paramMap)
/*     */     {
/* 117 */       return ManagementFactory.getMemoryPoolMXBeans();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmMemPoolTableMetaImpl
 * JD-Core Version:    0.6.2
 */