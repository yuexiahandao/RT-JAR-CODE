/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import com.sun.jmx.snmp.agent.SnmpStandardObjectServer;
/*     */ import java.util.Map;
/*     */ import sun.management.snmp.jvmmib.JvmRTClassPathTableMeta;
/*     */ import sun.management.snmp.util.JvmContextFactory;
/*     */ import sun.management.snmp.util.MibLogger;
/*     */ import sun.management.snmp.util.SnmpCachedData;
/*     */ import sun.management.snmp.util.SnmpTableCache;
/*     */ import sun.management.snmp.util.SnmpTableHandler;
/*     */ 
/*     */ public class JvmRTClassPathTableMetaImpl extends JvmRTClassPathTableMeta
/*     */ {
/*     */   private SnmpTableCache cache;
/* 296 */   static final MibLogger log = new MibLogger(JvmRTClassPathTableMetaImpl.class);
/*     */ 
/*     */   public JvmRTClassPathTableMetaImpl(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/* 131 */     super(paramSnmpMib, paramSnmpStandardObjectServer);
/* 132 */     this.cache = new JvmRTClassPathTableCache(this, -1L);
/*     */   }
/*     */ 
/*     */   protected SnmpOid getNextOid(Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 139 */     return getNextOid(null, paramObject);
/*     */   }
/*     */ 
/*     */   protected SnmpOid getNextOid(SnmpOid paramSnmpOid, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 145 */     boolean bool = log.isDebugOn();
/* 146 */     if (bool) log.debug("getNextOid", "previous=" + paramSnmpOid);
/*     */ 
/* 151 */     SnmpTableHandler localSnmpTableHandler = getHandler(paramObject);
/* 152 */     if (localSnmpTableHandler == null)
/*     */     {
/* 156 */       if (bool) log.debug("getNextOid", "handler is null!");
/* 157 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 162 */     SnmpOid localSnmpOid = localSnmpTableHandler.getNext(paramSnmpOid);
/* 163 */     if (bool) log.debug("*** **** **** **** getNextOid", "next=" + localSnmpOid);
/*     */ 
/* 167 */     if (localSnmpOid == null) {
/* 168 */       throw new SnmpStatusException(224);
/*     */     }
/* 170 */     return localSnmpOid;
/*     */   }
/*     */ 
/*     */   protected boolean contains(SnmpOid paramSnmpOid, Object paramObject)
/*     */   {
/* 179 */     SnmpTableHandler localSnmpTableHandler = getHandler(paramObject);
/*     */ 
/* 183 */     if (localSnmpTableHandler == null) {
/* 184 */       return false;
/*     */     }
/* 186 */     return localSnmpTableHandler.contains(paramSnmpOid);
/*     */   }
/*     */ 
/*     */   public Object getEntry(SnmpOid paramSnmpOid)
/*     */     throws SnmpStatusException
/*     */   {
/* 192 */     boolean bool = log.isDebugOn();
/* 193 */     if (bool) log.debug("getEntry", "oid [" + paramSnmpOid + "]");
/* 194 */     if ((paramSnmpOid == null) || (paramSnmpOid.getLength() != 1)) {
/* 195 */       if (bool) log.debug("getEntry", "Invalid oid [" + paramSnmpOid + "]");
/* 196 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 201 */     Map localMap = JvmContextFactory.getUserData();
/*     */ 
/* 209 */     String str = "JvmRTClassPathTable.entry." + paramSnmpOid.toString();
/*     */ 
/* 215 */     if (localMap != null) {
/* 216 */       localObject1 = localMap.get(str);
/* 217 */       if (localObject1 != null) {
/* 218 */         if (bool)
/* 219 */           log.debug("getEntry", "Entry is already in the cache");
/* 220 */         return localObject1;
/*     */       }
/* 222 */       if (bool) log.debug("getEntry", "Entry is not in the cache");
/*     */ 
/*     */     }
/*     */ 
/* 229 */     Object localObject1 = getHandler(localMap);
/*     */ 
/* 233 */     if (localObject1 == null) {
/* 234 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 238 */     Object localObject2 = ((SnmpTableHandler)localObject1).getData(paramSnmpOid);
/*     */ 
/* 242 */     if (localObject2 == null) {
/* 243 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 248 */     if (bool)
/* 249 */       log.debug("getEntry", "data is a: " + localObject2.getClass().getName());
/* 250 */     JvmRTClassPathEntryImpl localJvmRTClassPathEntryImpl = new JvmRTClassPathEntryImpl((String)localObject2, (int)paramSnmpOid.getOidArc(0));
/*     */ 
/* 256 */     if ((localMap != null) && (localJvmRTClassPathEntryImpl != null)) {
/* 257 */       localMap.put(str, localJvmRTClassPathEntryImpl);
/*     */     }
/*     */ 
/* 260 */     return localJvmRTClassPathEntryImpl;
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
/* 282 */       localSnmpTableHandler = (SnmpTableHandler)localMap.get("JvmRTClassPathTable.handler");
/*     */ 
/* 284 */       if (localSnmpTableHandler != null) return localSnmpTableHandler;
/*     */ 
/*     */     }
/*     */ 
/* 288 */     SnmpTableHandler localSnmpTableHandler = this.cache.getTableHandler();
/*     */ 
/* 290 */     if ((localMap != null) && (localSnmpTableHandler != null)) {
/* 291 */       localMap.put("JvmRTClassPathTable.handler", localSnmpTableHandler);
/*     */     }
/* 293 */     return localSnmpTableHandler;
/*     */   }
/*     */ 
/*     */   private static class JvmRTClassPathTableCache extends SnmpTableCache
/*     */   {
/*     */     private JvmRTClassPathTableMetaImpl meta;
/*     */ 
/*     */     JvmRTClassPathTableCache(JvmRTClassPathTableMetaImpl paramJvmRTClassPathTableMetaImpl, long paramLong)
/*     */     {
/*  84 */       this.meta = paramJvmRTClassPathTableMetaImpl;
/*  85 */       this.validity = paramLong;
/*     */     }
/*     */ 
/*     */     public SnmpTableHandler getTableHandler()
/*     */     {
/*  92 */       Map localMap = JvmContextFactory.getUserData();
/*  93 */       return getTableDatas(localMap);
/*     */     }
/*     */ 
/*     */     protected SnmpCachedData updateCachedDatas(Object paramObject)
/*     */     {
/* 105 */       String[] arrayOfString = JvmRuntimeImpl.getClassPath(paramObject);
/*     */ 
/* 109 */       long l = System.currentTimeMillis();
/* 110 */       int i = arrayOfString.length;
/*     */ 
/* 112 */       SnmpOid[] arrayOfSnmpOid = new SnmpOid[i];
/*     */ 
/* 114 */       for (int j = 0; j < i; j++) {
/* 115 */         arrayOfSnmpOid[j] = new SnmpOid(j + 1);
/*     */       }
/*     */ 
/* 118 */       return new SnmpCachedData(l, arrayOfSnmpOid, arrayOfString);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmRTClassPathTableMetaImpl
 * JD-Core Version:    0.6.2
 */