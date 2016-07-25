/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import com.sun.jmx.snmp.agent.SnmpStandardObjectServer;
/*     */ import java.util.Map;
/*     */ import sun.management.snmp.jvmmib.JvmRTInputArgsTableMeta;
/*     */ import sun.management.snmp.util.JvmContextFactory;
/*     */ import sun.management.snmp.util.MibLogger;
/*     */ import sun.management.snmp.util.SnmpCachedData;
/*     */ import sun.management.snmp.util.SnmpTableCache;
/*     */ import sun.management.snmp.util.SnmpTableHandler;
/*     */ 
/*     */ public class JvmRTInputArgsTableMetaImpl extends JvmRTInputArgsTableMeta
/*     */ {
/*     */   private SnmpTableCache cache;
/* 292 */   static final MibLogger log = new MibLogger(JvmRTInputArgsTableMetaImpl.class);
/*     */ 
/*     */   public JvmRTInputArgsTableMetaImpl(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/* 128 */     super(paramSnmpMib, paramSnmpStandardObjectServer);
/* 129 */     this.cache = new JvmRTInputArgsTableCache(this, -1L);
/*     */   }
/*     */ 
/*     */   protected SnmpOid getNextOid(Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 136 */     return getNextOid(null, paramObject);
/*     */   }
/*     */ 
/*     */   protected SnmpOid getNextOid(SnmpOid paramSnmpOid, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 142 */     boolean bool = log.isDebugOn();
/* 143 */     if (bool) log.debug("getNextOid", "previous=" + paramSnmpOid);
/*     */ 
/* 148 */     SnmpTableHandler localSnmpTableHandler = getHandler(paramObject);
/* 149 */     if (localSnmpTableHandler == null)
/*     */     {
/* 153 */       if (bool) log.debug("getNextOid", "handler is null!");
/* 154 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 159 */     SnmpOid localSnmpOid = localSnmpTableHandler.getNext(paramSnmpOid);
/* 160 */     if (bool) log.debug("*** **** **** **** getNextOid", "next=" + localSnmpOid);
/*     */ 
/* 164 */     if (localSnmpOid == null) {
/* 165 */       throw new SnmpStatusException(224);
/*     */     }
/* 167 */     return localSnmpOid;
/*     */   }
/*     */ 
/*     */   protected boolean contains(SnmpOid paramSnmpOid, Object paramObject)
/*     */   {
/* 176 */     SnmpTableHandler localSnmpTableHandler = getHandler(paramObject);
/*     */ 
/* 180 */     if (localSnmpTableHandler == null) {
/* 181 */       return false;
/*     */     }
/* 183 */     return localSnmpTableHandler.contains(paramSnmpOid);
/*     */   }
/*     */ 
/*     */   public Object getEntry(SnmpOid paramSnmpOid)
/*     */     throws SnmpStatusException
/*     */   {
/* 189 */     boolean bool = log.isDebugOn();
/* 190 */     if (bool) log.debug("getEntry", "oid [" + paramSnmpOid + "]");
/* 191 */     if ((paramSnmpOid == null) || (paramSnmpOid.getLength() != 1)) {
/* 192 */       if (bool) log.debug("getEntry", "Invalid oid [" + paramSnmpOid + "]");
/* 193 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 198 */     Map localMap = JvmContextFactory.getUserData();
/*     */ 
/* 206 */     String str = "JvmRTInputArgsTable.entry." + paramSnmpOid.toString();
/*     */ 
/* 212 */     if (localMap != null) {
/* 213 */       localObject1 = localMap.get(str);
/* 214 */       if (localObject1 != null) {
/* 215 */         if (bool)
/* 216 */           log.debug("getEntry", "Entry is already in the cache");
/* 217 */         return localObject1;
/* 218 */       }if (bool) log.debug("getEntry", "Entry is not in the cache");
/*     */ 
/*     */     }
/*     */ 
/* 225 */     Object localObject1 = getHandler(localMap);
/*     */ 
/* 229 */     if (localObject1 == null) {
/* 230 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 234 */     Object localObject2 = ((SnmpTableHandler)localObject1).getData(paramSnmpOid);
/*     */ 
/* 238 */     if (localObject2 == null) {
/* 239 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 244 */     if (bool) log.debug("getEntry", "data is a: " + localObject2.getClass().getName());
/*     */ 
/* 246 */     JvmRTInputArgsEntryImpl localJvmRTInputArgsEntryImpl = new JvmRTInputArgsEntryImpl((String)localObject2, (int)paramSnmpOid.getOidArc(0));
/*     */ 
/* 252 */     if ((localMap != null) && (localJvmRTInputArgsEntryImpl != null)) {
/* 253 */       localMap.put(str, localJvmRTInputArgsEntryImpl);
/*     */     }
/*     */ 
/* 256 */     return localJvmRTInputArgsEntryImpl;
/*     */   }
/*     */ 
/*     */   protected SnmpTableHandler getHandler(Object paramObject)
/*     */   {
/* 273 */     Map localMap;
/* 273 */     if ((paramObject instanceof Map)) localMap = (Map)Util.cast(paramObject); else {
/* 274 */       localMap = null;
/*     */     }
/*     */ 
/* 277 */     if (localMap != null) {
/* 278 */       localSnmpTableHandler = (SnmpTableHandler)localMap.get("JvmRTInputArgsTable.handler");
/*     */ 
/* 280 */       if (localSnmpTableHandler != null) return localSnmpTableHandler;
/*     */ 
/*     */     }
/*     */ 
/* 284 */     SnmpTableHandler localSnmpTableHandler = this.cache.getTableHandler();
/*     */ 
/* 286 */     if ((localMap != null) && (localSnmpTableHandler != null)) {
/* 287 */       localMap.put("JvmRTInputArgsTable.handler", localSnmpTableHandler);
/*     */     }
/* 289 */     return localSnmpTableHandler;
/*     */   }
/*     */ 
/*     */   private static class JvmRTInputArgsTableCache extends SnmpTableCache
/*     */   {
/*     */     private JvmRTInputArgsTableMetaImpl meta;
/*     */ 
/*     */     JvmRTInputArgsTableCache(JvmRTInputArgsTableMetaImpl paramJvmRTInputArgsTableMetaImpl, long paramLong)
/*     */     {
/*  84 */       this.meta = paramJvmRTInputArgsTableMetaImpl;
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
/* 105 */       String[] arrayOfString = JvmRuntimeImpl.getInputArguments(paramObject);
/*     */ 
/* 108 */       long l = System.currentTimeMillis();
/* 109 */       SnmpOid[] arrayOfSnmpOid = new SnmpOid[arrayOfString.length];
/*     */ 
/* 111 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 112 */         arrayOfSnmpOid[i] = new SnmpOid(i + 1);
/*     */       }
/*     */ 
/* 115 */       return new SnmpCachedData(l, arrayOfSnmpOid, arrayOfString);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmRTInputArgsTableMetaImpl
 * JD-Core Version:    0.6.2
 */