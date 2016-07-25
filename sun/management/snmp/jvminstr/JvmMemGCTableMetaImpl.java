/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import com.sun.jmx.snmp.agent.SnmpStandardObjectServer;
/*     */ import java.lang.management.GarbageCollectorMXBean;
/*     */ import java.lang.management.MemoryManagerMXBean;
/*     */ import java.util.Map;
/*     */ import sun.management.snmp.jvmmib.JvmMemGCTableMeta;
/*     */ import sun.management.snmp.util.JvmContextFactory;
/*     */ import sun.management.snmp.util.MibLogger;
/*     */ import sun.management.snmp.util.SnmpCachedData;
/*     */ import sun.management.snmp.util.SnmpTableHandler;
/*     */ 
/*     */ public class JvmMemGCTableMetaImpl extends JvmMemGCTableMeta
/*     */ {
/* 184 */   private transient JvmMemManagerTableMetaImpl managers = null;
/* 185 */   private static GCTableFilter filter = new GCTableFilter();
/*     */ 
/* 359 */   static final MibLogger log = new MibLogger(JvmMemGCTableMetaImpl.class);
/*     */ 
/*     */   public JvmMemGCTableMetaImpl(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/* 193 */     super(paramSnmpMib, paramSnmpStandardObjectServer);
/*     */   }
/*     */ 
/*     */   private final JvmMemManagerTableMetaImpl getManagers(SnmpMib paramSnmpMib)
/*     */   {
/* 200 */     if (this.managers == null) {
/* 201 */       this.managers = ((JvmMemManagerTableMetaImpl)paramSnmpMib.getRegisteredTableMeta("JvmMemManagerTable"));
/*     */     }
/*     */ 
/* 204 */     return this.managers;
/*     */   }
/*     */ 
/*     */   protected SnmpTableHandler getHandler(Object paramObject)
/*     */   {
/* 211 */     JvmMemManagerTableMetaImpl localJvmMemManagerTableMetaImpl = getManagers(this.theMib);
/* 212 */     return localJvmMemManagerTableMetaImpl.getHandler(paramObject);
/*     */   }
/*     */ 
/*     */   protected SnmpOid getNextOid(Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 219 */     return getNextOid(null, paramObject);
/*     */   }
/*     */ 
/*     */   protected SnmpOid getNextOid(SnmpOid paramSnmpOid, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 225 */     boolean bool = log.isDebugOn();
/*     */     try {
/* 227 */       if (bool) log.debug("getNextOid", "previous=" + paramSnmpOid);
/*     */ 
/* 231 */       SnmpTableHandler localSnmpTableHandler = getHandler(paramObject);
/* 232 */       if (localSnmpTableHandler == null)
/*     */       {
/* 236 */         if (bool) log.debug("getNextOid", "handler is null!");
/* 237 */         throw new SnmpStatusException(224);
/*     */       }
/*     */ 
/* 244 */       SnmpOid localSnmpOid = filter.getNext(localSnmpTableHandler, paramSnmpOid);
/* 245 */       if (bool) log.debug("getNextOid", "next=" + localSnmpOid);
/*     */ 
/* 249 */       if (localSnmpOid == null) {
/* 250 */         throw new SnmpStatusException(224);
/*     */       }
/*     */ 
/* 253 */       return localSnmpOid;
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/* 257 */       if (bool) log.debug("getNextOid", localRuntimeException);
/* 258 */       throw localRuntimeException;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean contains(SnmpOid paramSnmpOid, Object paramObject)
/*     */   {
/* 267 */     SnmpTableHandler localSnmpTableHandler = getHandler(paramObject);
/*     */ 
/* 271 */     if (localSnmpTableHandler == null)
/* 272 */       return false;
/* 273 */     return filter.contains(localSnmpTableHandler, paramSnmpOid);
/*     */   }
/*     */ 
/*     */   public Object getEntry(SnmpOid paramSnmpOid)
/*     */     throws SnmpStatusException
/*     */   {
/* 280 */     if (paramSnmpOid == null) {
/* 281 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 285 */     Map localMap = JvmContextFactory.getUserData();
/*     */ 
/* 294 */     long l = paramSnmpOid.getOidArc(0);
/*     */ 
/* 302 */     String str = "JvmMemGCTable.entry." + l;
/*     */ 
/* 307 */     if (localMap != null) {
/* 308 */       localObject1 = localMap.get(str);
/* 309 */       if (localObject1 != null) return localObject1;
/*     */ 
/*     */     }
/*     */ 
/* 316 */     Object localObject1 = getHandler(localMap);
/*     */ 
/* 320 */     if (localObject1 == null) {
/* 321 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 325 */     Object localObject2 = filter.getData((SnmpTableHandler)localObject1, paramSnmpOid);
/*     */ 
/* 331 */     if (localObject2 == null) {
/* 332 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 337 */     JvmMemGCEntryImpl localJvmMemGCEntryImpl = new JvmMemGCEntryImpl((GarbageCollectorMXBean)localObject2, (int)l);
/*     */ 
/* 352 */     if ((localMap != null) && (localJvmMemGCEntryImpl != null)) {
/* 353 */       localMap.put(str, localJvmMemGCEntryImpl);
/*     */     }
/*     */ 
/* 356 */     return localJvmMemGCEntryImpl;
/*     */   }
/*     */ 
/*     */   protected static class GCTableFilter
/*     */   {
/*     */     public SnmpOid getNext(SnmpCachedData paramSnmpCachedData, SnmpOid paramSnmpOid)
/*     */     {
/*  86 */       boolean bool = JvmMemGCTableMetaImpl.log.isDebugOn();
/*     */ 
/*  94 */       int i = paramSnmpOid == null ? -1 : paramSnmpCachedData.find(paramSnmpOid);
/*  95 */       if (bool) JvmMemGCTableMetaImpl.log.debug("GCTableFilter", "oid=" + paramSnmpOid + " at insertion=" + i);
/*     */ 
/*  99 */       if (i > -1) j = i + 1;
/* 100 */       for (int j = -i - 1; 
/* 108 */         j < paramSnmpCachedData.indexes.length; j++) {
/* 109 */         if (bool) JvmMemGCTableMetaImpl.log.debug("GCTableFilter", "next=" + j);
/* 110 */         Object localObject = paramSnmpCachedData.datas[j];
/* 111 */         if (bool) JvmMemGCTableMetaImpl.log.debug("GCTableFilter", "value[" + j + "]=" + ((MemoryManagerMXBean)localObject).getName());
/*     */ 
/* 113 */         if ((localObject instanceof GarbageCollectorMXBean))
/*     */         {
/* 115 */           if (bool) JvmMemGCTableMetaImpl.log.debug("GCTableFilter", ((MemoryManagerMXBean)localObject).getName() + " is a  GarbageCollectorMXBean.");
/*     */ 
/* 118 */           return paramSnmpCachedData.indexes[j];
/*     */         }
/* 120 */         if (bool) JvmMemGCTableMetaImpl.log.debug("GCTableFilter", ((MemoryManagerMXBean)localObject).getName() + " is not a  GarbageCollectorMXBean: " + localObject.getClass().getName());
/*     */ 
/*     */       }
/*     */ 
/* 126 */       return null;
/*     */     }
/*     */ 
/*     */     public SnmpOid getNext(SnmpTableHandler paramSnmpTableHandler, SnmpOid paramSnmpOid)
/*     */     {
/* 141 */       if ((paramSnmpTableHandler instanceof SnmpCachedData)) {
/* 142 */         return getNext((SnmpCachedData)paramSnmpTableHandler, paramSnmpOid);
/*     */       }
/*     */ 
/* 145 */       SnmpOid localSnmpOid = paramSnmpOid;
/*     */       do {
/* 147 */         localSnmpOid = paramSnmpTableHandler.getNext(localSnmpOid);
/* 148 */         Object localObject = paramSnmpTableHandler.getData(localSnmpOid);
/* 149 */         if ((localObject instanceof GarbageCollectorMXBean))
/*     */         {
/* 151 */           return localSnmpOid;
/*     */         }
/*     */       }
/* 153 */       while (localSnmpOid != null);
/* 154 */       return null;
/*     */     }
/*     */ 
/*     */     public Object getData(SnmpTableHandler paramSnmpTableHandler, SnmpOid paramSnmpOid)
/*     */     {
/* 164 */       Object localObject = paramSnmpTableHandler.getData(paramSnmpOid);
/* 165 */       if ((localObject instanceof GarbageCollectorMXBean)) return localObject;
/*     */ 
/* 168 */       return null;
/*     */     }
/*     */ 
/*     */     public boolean contains(SnmpTableHandler paramSnmpTableHandler, SnmpOid paramSnmpOid)
/*     */     {
/* 175 */       if ((paramSnmpTableHandler.getData(paramSnmpOid) instanceof GarbageCollectorMXBean)) {
/* 176 */         return true;
/*     */       }
/*     */ 
/* 179 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmMemGCTableMetaImpl
 * JD-Core Version:    0.6.2
 */