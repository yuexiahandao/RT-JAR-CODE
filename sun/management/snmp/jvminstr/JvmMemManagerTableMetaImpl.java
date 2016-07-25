/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import com.sun.jmx.snmp.agent.SnmpStandardObjectServer;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.MemoryManagerMXBean;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import sun.management.snmp.jvmmib.JvmMemManagerTableMeta;
/*     */ import sun.management.snmp.util.JvmContextFactory;
/*     */ import sun.management.snmp.util.MibLogger;
/*     */ import sun.management.snmp.util.SnmpNamedListTableCache;
/*     */ import sun.management.snmp.util.SnmpTableCache;
/*     */ import sun.management.snmp.util.SnmpTableHandler;
/*     */ 
/*     */ public class JvmMemManagerTableMetaImpl extends JvmMemManagerTableMeta
/*     */ {
/*     */   protected SnmpTableCache cache;
/* 296 */   static final MibLogger log = new MibLogger(JvmMemManagerTableMetaImpl.class);
/*     */ 
/*     */   public JvmMemManagerTableMetaImpl(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/* 135 */     super(paramSnmpMib, paramSnmpStandardObjectServer);
/* 136 */     this.cache = new JvmMemManagerTableCache(((JVM_MANAGEMENT_MIB_IMPL)paramSnmpMib).validity());
/*     */   }
/*     */ 
/*     */   protected SnmpOid getNextOid(Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 145 */     return getNextOid(null, paramObject);
/*     */   }
/*     */ 
/*     */   protected SnmpOid getNextOid(SnmpOid paramSnmpOid, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 151 */     boolean bool = log.isDebugOn();
/* 152 */     if (bool) log.debug("getNextOid", "previous=" + paramSnmpOid);
/*     */ 
/* 157 */     SnmpTableHandler localSnmpTableHandler = getHandler(paramObject);
/* 158 */     if (localSnmpTableHandler == null)
/*     */     {
/* 162 */       if (bool) log.debug("getNextOid", "handler is null!");
/* 163 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 168 */     SnmpOid localSnmpOid = localSnmpTableHandler.getNext(paramSnmpOid);
/* 169 */     if (bool) log.debug("getNextOid", "next=" + localSnmpOid);
/*     */ 
/* 173 */     if (localSnmpOid == null) {
/* 174 */       throw new SnmpStatusException(224);
/*     */     }
/* 176 */     return localSnmpOid;
/*     */   }
/*     */ 
/*     */   protected boolean contains(SnmpOid paramSnmpOid, Object paramObject)
/*     */   {
/* 185 */     SnmpTableHandler localSnmpTableHandler = getHandler(paramObject);
/*     */ 
/* 189 */     if (localSnmpTableHandler == null) {
/* 190 */       return false;
/*     */     }
/* 192 */     return localSnmpTableHandler.contains(paramSnmpOid);
/*     */   }
/*     */ 
/*     */   public Object getEntry(SnmpOid paramSnmpOid)
/*     */     throws SnmpStatusException
/*     */   {
/* 199 */     if (paramSnmpOid == null) {
/* 200 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 204 */     Map localMap = JvmContextFactory.getUserData();
/*     */ 
/* 209 */     long l = paramSnmpOid.getOidArc(0);
/*     */ 
/* 217 */     String str = "JvmMemManagerTable.entry." + l;
/*     */ 
/* 222 */     if (localMap != null) {
/* 223 */       localObject1 = localMap.get(str);
/* 224 */       if (localObject1 != null) return localObject1;
/*     */ 
/*     */     }
/*     */ 
/* 231 */     Object localObject1 = getHandler(localMap);
/*     */ 
/* 235 */     if (localObject1 == null) {
/* 236 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 240 */     Object localObject2 = ((SnmpTableHandler)localObject1).getData(paramSnmpOid);
/*     */ 
/* 244 */     if (localObject2 == null) {
/* 245 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 250 */     JvmMemManagerEntryImpl localJvmMemManagerEntryImpl = new JvmMemManagerEntryImpl((MemoryManagerMXBean)localObject2, (int)l);
/*     */ 
/* 256 */     if ((localMap != null) && (localJvmMemManagerEntryImpl != null)) {
/* 257 */       localMap.put(str, localJvmMemManagerEntryImpl);
/*     */     }
/*     */ 
/* 260 */     return localJvmMemManagerEntryImpl;
/*     */   }
/*     */ 
/*     */   protected SnmpTableHandler getHandler(Object paramObject)
/*     */   {
/* 277 */     Map localMap;
/* 277 */     if ((paramObject instanceof Map)) localMap = (Map)Util.cast(paramObject); else {
/* 278 */       localMap = null;
/*     */     }
/*     */ 
/* 281 */     if (localMap != null) {
/* 282 */       localSnmpTableHandler = (SnmpTableHandler)localMap.get("JvmMemManagerTable.handler");
/*     */ 
/* 284 */       if (localSnmpTableHandler != null) return localSnmpTableHandler;
/*     */ 
/*     */     }
/*     */ 
/* 288 */     SnmpTableHandler localSnmpTableHandler = this.cache.getTableHandler();
/*     */ 
/* 290 */     if ((localMap != null) && (localSnmpTableHandler != null)) {
/* 291 */       localMap.put("JvmMemManagerTable.handler", localSnmpTableHandler);
/*     */     }
/* 293 */     return localSnmpTableHandler;
/*     */   }
/*     */ 
/*     */   private static class JvmMemManagerTableCache extends SnmpNamedListTableCache
/*     */   {
/*     */     JvmMemManagerTableCache(long paramLong)
/*     */     {
/*  75 */       this.validity = paramLong;
/*     */     }
/*     */ 
/*     */     protected String getKey(Object paramObject1, List paramList, int paramInt, Object paramObject2)
/*     */     {
/*  92 */       if (paramObject2 == null) return null;
/*  93 */       String str = ((MemoryManagerMXBean)paramObject2).getName();
/*  94 */       JvmMemManagerTableMetaImpl.log.debug("getKey", "key=" + str);
/*  95 */       return str;
/*     */     }
/*     */ 
/*     */     public SnmpTableHandler getTableHandler()
/*     */     {
/* 102 */       Map localMap = JvmContextFactory.getUserData();
/* 103 */       return getTableDatas(localMap);
/*     */     }
/*     */ 
/*     */     protected String getRawDatasKey()
/*     */     {
/* 110 */       return "JvmMemManagerTable.getMemoryManagers";
/*     */     }
/*     */ 
/*     */     protected List loadRawDatas(Map paramMap)
/*     */     {
/* 118 */       return ManagementFactory.getMemoryManagerMXBeans();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmMemManagerTableMetaImpl
 * JD-Core Version:    0.6.2
 */