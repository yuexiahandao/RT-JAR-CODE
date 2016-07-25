/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import com.sun.jmx.snmp.agent.SnmpStandardObjectServer;
/*     */ import java.util.Map;
/*     */ import sun.management.snmp.jvmmib.JvmRTBootClassPathTableMeta;
/*     */ import sun.management.snmp.util.JvmContextFactory;
/*     */ import sun.management.snmp.util.MibLogger;
/*     */ import sun.management.snmp.util.SnmpCachedData;
/*     */ import sun.management.snmp.util.SnmpTableCache;
/*     */ import sun.management.snmp.util.SnmpTableHandler;
/*     */ 
/*     */ public class JvmRTBootClassPathTableMetaImpl extends JvmRTBootClassPathTableMeta
/*     */ {
/*     */   private SnmpTableCache cache;
/* 298 */   static final MibLogger log = new MibLogger(JvmRTBootClassPathTableMetaImpl.class);
/*     */ 
/*     */   public JvmRTBootClassPathTableMetaImpl(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/* 132 */     super(paramSnmpMib, paramSnmpStandardObjectServer);
/* 133 */     this.cache = new JvmRTBootClassPathTableCache(this, -1L);
/*     */   }
/*     */ 
/*     */   protected SnmpOid getNextOid(Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 140 */     return getNextOid(null, paramObject);
/*     */   }
/*     */ 
/*     */   protected SnmpOid getNextOid(SnmpOid paramSnmpOid, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 146 */     boolean bool = log.isDebugOn();
/* 147 */     if (bool) log.debug("getNextOid", "previous=" + paramSnmpOid);
/*     */ 
/* 152 */     SnmpTableHandler localSnmpTableHandler = getHandler(paramObject);
/* 153 */     if (localSnmpTableHandler == null)
/*     */     {
/* 157 */       if (bool) log.debug("getNextOid", "handler is null!");
/* 158 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 163 */     SnmpOid localSnmpOid = localSnmpTableHandler.getNext(paramSnmpOid);
/* 164 */     if (bool) log.debug("*** **** **** **** getNextOid", "next=" + localSnmpOid);
/*     */ 
/* 168 */     if (localSnmpOid == null) {
/* 169 */       throw new SnmpStatusException(224);
/*     */     }
/* 171 */     return localSnmpOid;
/*     */   }
/*     */ 
/*     */   protected boolean contains(SnmpOid paramSnmpOid, Object paramObject)
/*     */   {
/* 180 */     SnmpTableHandler localSnmpTableHandler = getHandler(paramObject);
/*     */ 
/* 184 */     if (localSnmpTableHandler == null) {
/* 185 */       return false;
/*     */     }
/* 187 */     return localSnmpTableHandler.contains(paramSnmpOid);
/*     */   }
/*     */ 
/*     */   public Object getEntry(SnmpOid paramSnmpOid)
/*     */     throws SnmpStatusException
/*     */   {
/* 193 */     boolean bool = log.isDebugOn();
/* 194 */     if (bool) log.debug("getEntry", "oid [" + paramSnmpOid + "]");
/* 195 */     if ((paramSnmpOid == null) || (paramSnmpOid.getLength() != 1)) {
/* 196 */       if (bool) log.debug("getEntry", "Invalid oid [" + paramSnmpOid + "]");
/* 197 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 202 */     Map localMap = JvmContextFactory.getUserData();
/*     */ 
/* 210 */     String str = "JvmRTBootClassPathTable.entry." + paramSnmpOid.toString();
/*     */ 
/* 216 */     if (localMap != null) {
/* 217 */       localObject1 = localMap.get(str);
/* 218 */       if (localObject1 != null) {
/* 219 */         if (bool)
/* 220 */           log.debug("getEntry", "Entry is already in the cache");
/* 221 */         return localObject1;
/*     */       }
/* 223 */       if (bool) log.debug("getEntry", "Entry is not in the cache");
/*     */ 
/*     */     }
/*     */ 
/* 230 */     Object localObject1 = getHandler(localMap);
/*     */ 
/* 234 */     if (localObject1 == null) {
/* 235 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 239 */     Object localObject2 = ((SnmpTableHandler)localObject1).getData(paramSnmpOid);
/*     */ 
/* 243 */     if (localObject2 == null) {
/* 244 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 249 */     if (bool)
/* 250 */       log.debug("getEntry", "data is a: " + localObject2.getClass().getName());
/* 251 */     JvmRTBootClassPathEntryImpl localJvmRTBootClassPathEntryImpl = new JvmRTBootClassPathEntryImpl((String)localObject2, (int)paramSnmpOid.getOidArc(0));
/*     */ 
/* 258 */     if ((localMap != null) && (localJvmRTBootClassPathEntryImpl != null)) {
/* 259 */       localMap.put(str, localJvmRTBootClassPathEntryImpl);
/*     */     }
/*     */ 
/* 262 */     return localJvmRTBootClassPathEntryImpl;
/*     */   }
/*     */ 
/*     */   protected SnmpTableHandler getHandler(Object paramObject)
/*     */   {
/* 279 */     Map localMap;
/* 279 */     if ((paramObject instanceof Map)) localMap = (Map)Util.cast(paramObject); else {
/* 280 */       localMap = null;
/*     */     }
/*     */ 
/* 283 */     if (localMap != null) {
/* 284 */       localSnmpTableHandler = (SnmpTableHandler)localMap.get("JvmRTBootClassPathTable.handler");
/*     */ 
/* 286 */       if (localSnmpTableHandler != null) return localSnmpTableHandler;
/*     */ 
/*     */     }
/*     */ 
/* 290 */     SnmpTableHandler localSnmpTableHandler = this.cache.getTableHandler();
/*     */ 
/* 292 */     if ((localMap != null) && (localSnmpTableHandler != null)) {
/* 293 */       localMap.put("JvmRTBootClassPathTable.handler", localSnmpTableHandler);
/*     */     }
/* 295 */     return localSnmpTableHandler;
/*     */   }
/*     */ 
/*     */   private static class JvmRTBootClassPathTableCache extends SnmpTableCache
/*     */   {
/*     */     private JvmRTBootClassPathTableMetaImpl meta;
/*     */ 
/*     */     JvmRTBootClassPathTableCache(JvmRTBootClassPathTableMetaImpl paramJvmRTBootClassPathTableMetaImpl, long paramLong)
/*     */     {
/*  85 */       this.meta = paramJvmRTBootClassPathTableMetaImpl;
/*  86 */       this.validity = paramLong;
/*     */     }
/*     */ 
/*     */     public SnmpTableHandler getTableHandler()
/*     */     {
/*  93 */       Map localMap = JvmContextFactory.getUserData();
/*  94 */       return getTableDatas(localMap);
/*     */     }
/*     */ 
/*     */     protected SnmpCachedData updateCachedDatas(Object paramObject)
/*     */     {
/* 106 */       String[] arrayOfString = JvmRuntimeImpl.getBootClassPath(paramObject);
/*     */ 
/* 110 */       long l = System.currentTimeMillis();
/* 111 */       int i = arrayOfString.length;
/*     */ 
/* 113 */       SnmpOid[] arrayOfSnmpOid = new SnmpOid[i];
/*     */ 
/* 115 */       for (int j = 0; j < i; j++) {
/* 116 */         arrayOfSnmpOid[j] = new SnmpOid(j + 1);
/*     */       }
/*     */ 
/* 119 */       return new SnmpCachedData(l, arrayOfSnmpOid, arrayOfString);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmRTBootClassPathTableMetaImpl
 * JD-Core Version:    0.6.2
 */