/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import java.lang.management.MemoryPoolMXBean;
/*     */ import java.lang.management.MemoryType;
/*     */ import java.lang.management.MemoryUsage;
/*     */ import java.util.Map;
/*     */ import sun.management.snmp.jvmmib.EnumJvmMemPoolCollectThreshdSupport;
/*     */ import sun.management.snmp.jvmmib.EnumJvmMemPoolState;
/*     */ import sun.management.snmp.jvmmib.EnumJvmMemPoolThreshdSupport;
/*     */ import sun.management.snmp.jvmmib.EnumJvmMemPoolType;
/*     */ import sun.management.snmp.jvmmib.JvmMemPoolEntryMBean;
/*     */ import sun.management.snmp.util.JvmContextFactory;
/*     */ import sun.management.snmp.util.MibLogger;
/*     */ 
/*     */ public class JvmMemPoolEntryImpl
/*     */   implements JvmMemPoolEntryMBean
/*     */ {
/*     */   protected final int jvmMemPoolIndex;
/*     */   static final String memoryTag = "jvmMemPoolEntry.getUsage";
/*     */   static final String peakMemoryTag = "jvmMemPoolEntry.getPeakUsage";
/*     */   static final String collectMemoryTag = "jvmMemPoolEntry.getCollectionUsage";
/*  71 */   static final MemoryUsage ZEROS = new MemoryUsage(0L, 0L, 0L, 0L);
/*     */   final String entryMemoryTag;
/*     */   final String entryPeakMemoryTag;
/*     */   final String entryCollectMemoryTag;
/*     */   final MemoryPoolMXBean pool;
/* 478 */   private long jvmMemPoolPeakReset = 0L;
/*     */ 
/* 480 */   private static final EnumJvmMemPoolState JvmMemPoolStateValid = new EnumJvmMemPoolState("valid");
/*     */ 
/* 482 */   private static final EnumJvmMemPoolState JvmMemPoolStateInvalid = new EnumJvmMemPoolState("invalid");
/*     */ 
/* 485 */   private static final EnumJvmMemPoolType EnumJvmMemPoolTypeHeap = new EnumJvmMemPoolType("heap");
/*     */ 
/* 487 */   private static final EnumJvmMemPoolType EnumJvmMemPoolTypeNonHeap = new EnumJvmMemPoolType("nonheap");
/*     */ 
/* 491 */   private static final EnumJvmMemPoolThreshdSupport EnumJvmMemPoolThreshdSupported = new EnumJvmMemPoolThreshdSupport("supported");
/*     */ 
/* 494 */   private static final EnumJvmMemPoolThreshdSupport EnumJvmMemPoolThreshdUnsupported = new EnumJvmMemPoolThreshdSupport("unsupported");
/*     */ 
/* 498 */   private static final EnumJvmMemPoolCollectThreshdSupport EnumJvmMemPoolCollectThreshdSupported = new EnumJvmMemPoolCollectThreshdSupport("supported");
/*     */ 
/* 501 */   private static final EnumJvmMemPoolCollectThreshdSupport EnumJvmMemPoolCollectThreshdUnsupported = new EnumJvmMemPoolCollectThreshdSupport("unsupported");
/*     */ 
/* 505 */   static final MibLogger log = new MibLogger(JvmMemPoolEntryImpl.class);
/*     */ 
/*     */   MemoryUsage getMemoryUsage()
/*     */   {
/*     */     try
/*     */     {
/*  79 */       Map localMap = JvmContextFactory.getUserData();
/*     */ 
/*  81 */       if (localMap != null) {
/*  82 */         MemoryUsage localMemoryUsage1 = (MemoryUsage)localMap.get(this.entryMemoryTag);
/*     */ 
/*  84 */         if (localMemoryUsage1 != null) {
/*  85 */           log.debug("getMemoryUsage", this.entryMemoryTag + " found in cache.");
/*     */ 
/*  87 */           return localMemoryUsage1;
/*     */         }
/*     */ 
/*  90 */         MemoryUsage localMemoryUsage2 = this.pool.getUsage();
/*  91 */         if (localMemoryUsage2 == null) localMemoryUsage2 = ZEROS;
/*     */ 
/*  93 */         localMap.put(this.entryMemoryTag, localMemoryUsage2);
/*  94 */         return localMemoryUsage2;
/*     */       }
/*     */ 
/*  98 */       log.trace("getMemoryUsage", "ERROR: should never come here!");
/*  99 */       return this.pool.getUsage();
/*     */     } catch (RuntimeException localRuntimeException) {
/* 101 */       log.trace("getMemoryUsage", "Failed to get MemoryUsage: " + localRuntimeException);
/*     */ 
/* 103 */       log.debug("getMemoryUsage", localRuntimeException);
/* 104 */       throw localRuntimeException;
/*     */     }
/*     */   }
/*     */ 
/*     */   MemoryUsage getPeakMemoryUsage()
/*     */   {
/*     */     try {
/* 111 */       Map localMap = JvmContextFactory.getUserData();
/*     */ 
/* 113 */       if (localMap != null) {
/* 114 */         MemoryUsage localMemoryUsage1 = (MemoryUsage)localMap.get(this.entryPeakMemoryTag);
/*     */ 
/* 116 */         if (localMemoryUsage1 != null) {
/* 117 */           if (log.isDebugOn()) {
/* 118 */             log.debug("getPeakMemoryUsage", this.entryPeakMemoryTag + " found in cache.");
/*     */           }
/* 120 */           return localMemoryUsage1;
/*     */         }
/*     */ 
/* 123 */         MemoryUsage localMemoryUsage2 = this.pool.getPeakUsage();
/* 124 */         if (localMemoryUsage2 == null) localMemoryUsage2 = ZEROS;
/*     */ 
/* 126 */         localMap.put(this.entryPeakMemoryTag, localMemoryUsage2);
/* 127 */         return localMemoryUsage2;
/*     */       }
/*     */ 
/* 131 */       log.trace("getPeakMemoryUsage", "ERROR: should never come here!");
/* 132 */       return ZEROS;
/*     */     } catch (RuntimeException localRuntimeException) {
/* 134 */       log.trace("getPeakMemoryUsage", "Failed to get MemoryUsage: " + localRuntimeException);
/*     */ 
/* 136 */       log.debug("getPeakMemoryUsage", localRuntimeException);
/* 137 */       throw localRuntimeException;
/*     */     }
/*     */   }
/*     */ 
/*     */   MemoryUsage getCollectMemoryUsage()
/*     */   {
/*     */     try {
/* 144 */       Map localMap = JvmContextFactory.getUserData();
/*     */ 
/* 146 */       if (localMap != null) {
/* 147 */         MemoryUsage localMemoryUsage1 = (MemoryUsage)localMap.get(this.entryCollectMemoryTag);
/*     */ 
/* 149 */         if (localMemoryUsage1 != null) {
/* 150 */           if (log.isDebugOn()) {
/* 151 */             log.debug("getCollectMemoryUsage", this.entryCollectMemoryTag + " found in cache.");
/*     */           }
/* 153 */           return localMemoryUsage1;
/*     */         }
/*     */ 
/* 156 */         MemoryUsage localMemoryUsage2 = this.pool.getCollectionUsage();
/* 157 */         if (localMemoryUsage2 == null) localMemoryUsage2 = ZEROS;
/*     */ 
/* 159 */         localMap.put(this.entryCollectMemoryTag, localMemoryUsage2);
/* 160 */         return localMemoryUsage2;
/*     */       }
/*     */ 
/* 164 */       log.trace("getCollectMemoryUsage", "ERROR: should never come here!");
/*     */ 
/* 166 */       return ZEROS;
/*     */     } catch (RuntimeException localRuntimeException) {
/* 168 */       log.trace("getPeakMemoryUsage", "Failed to get MemoryUsage: " + localRuntimeException);
/*     */ 
/* 170 */       log.debug("getPeakMemoryUsage", localRuntimeException);
/* 171 */       throw localRuntimeException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public JvmMemPoolEntryImpl(MemoryPoolMXBean paramMemoryPoolMXBean, int paramInt)
/*     */   {
/* 182 */     this.pool = paramMemoryPoolMXBean;
/* 183 */     this.jvmMemPoolIndex = paramInt;
/* 184 */     this.entryMemoryTag = ("jvmMemPoolEntry.getUsage." + paramInt);
/* 185 */     this.entryPeakMemoryTag = ("jvmMemPoolEntry.getPeakUsage." + paramInt);
/* 186 */     this.entryCollectMemoryTag = ("jvmMemPoolEntry.getCollectionUsage." + paramInt);
/*     */   }
/*     */ 
/*     */   public Long getJvmMemPoolMaxSize()
/*     */     throws SnmpStatusException
/*     */   {
/* 193 */     long l = getMemoryUsage().getMax();
/* 194 */     if (l > -1L) return new Long(l);
/* 195 */     return JvmMemoryImpl.Long0;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemPoolUsed()
/*     */     throws SnmpStatusException
/*     */   {
/* 202 */     long l = getMemoryUsage().getUsed();
/* 203 */     if (l > -1L) return new Long(l);
/* 204 */     return JvmMemoryImpl.Long0;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemPoolInitSize()
/*     */     throws SnmpStatusException
/*     */   {
/* 211 */     long l = getMemoryUsage().getInit();
/* 212 */     if (l > -1L) return new Long(l);
/* 213 */     return JvmMemoryImpl.Long0;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemPoolCommitted()
/*     */     throws SnmpStatusException
/*     */   {
/* 220 */     long l = getMemoryUsage().getCommitted();
/* 221 */     if (l > -1L) return new Long(l);
/* 222 */     return JvmMemoryImpl.Long0;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemPoolPeakMaxSize()
/*     */     throws SnmpStatusException
/*     */   {
/* 229 */     long l = getPeakMemoryUsage().getMax();
/* 230 */     if (l > -1L) return new Long(l);
/* 231 */     return JvmMemoryImpl.Long0;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemPoolPeakUsed()
/*     */     throws SnmpStatusException
/*     */   {
/* 238 */     long l = getPeakMemoryUsage().getUsed();
/* 239 */     if (l > -1L) return new Long(l);
/* 240 */     return JvmMemoryImpl.Long0;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemPoolPeakCommitted()
/*     */     throws SnmpStatusException
/*     */   {
/* 247 */     long l = getPeakMemoryUsage().getCommitted();
/* 248 */     if (l > -1L) return new Long(l);
/* 249 */     return JvmMemoryImpl.Long0;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemPoolCollectMaxSize()
/*     */     throws SnmpStatusException
/*     */   {
/* 256 */     long l = getCollectMemoryUsage().getMax();
/* 257 */     if (l > -1L) return new Long(l);
/* 258 */     return JvmMemoryImpl.Long0;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemPoolCollectUsed()
/*     */     throws SnmpStatusException
/*     */   {
/* 265 */     long l = getCollectMemoryUsage().getUsed();
/* 266 */     if (l > -1L) return new Long(l);
/* 267 */     return JvmMemoryImpl.Long0;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemPoolCollectCommitted()
/*     */     throws SnmpStatusException
/*     */   {
/* 274 */     long l = getCollectMemoryUsage().getCommitted();
/* 275 */     if (l > -1L) return new Long(l);
/* 276 */     return JvmMemoryImpl.Long0;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemPoolThreshold()
/*     */     throws SnmpStatusException
/*     */   {
/* 283 */     if (!this.pool.isUsageThresholdSupported())
/* 284 */       return JvmMemoryImpl.Long0;
/* 285 */     long l = this.pool.getUsageThreshold();
/* 286 */     if (l > -1L) return new Long(l);
/* 287 */     return JvmMemoryImpl.Long0;
/*     */   }
/*     */ 
/*     */   public void setJvmMemPoolThreshold(Long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 294 */     long l = paramLong.longValue();
/* 295 */     if (l < 0L) {
/* 296 */       throw new SnmpStatusException(10);
/*     */     }
/*     */ 
/* 300 */     this.pool.setUsageThreshold(l);
/*     */   }
/*     */ 
/*     */   public void checkJvmMemPoolThreshold(Long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 310 */     if (!this.pool.isUsageThresholdSupported()) {
/* 311 */       throw new SnmpStatusException(12);
/*     */     }
/* 313 */     long l = paramLong.longValue();
/* 314 */     if (l < 0L)
/* 315 */       throw new SnmpStatusException(10);
/*     */   }
/*     */ 
/*     */   public EnumJvmMemPoolThreshdSupport getJvmMemPoolThreshdSupport()
/*     */     throws SnmpStatusException
/*     */   {
/* 323 */     if (this.pool.isUsageThresholdSupported()) {
/* 324 */       return EnumJvmMemPoolThreshdSupported;
/*     */     }
/* 326 */     return EnumJvmMemPoolThreshdUnsupported;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemPoolThreshdCount()
/*     */     throws SnmpStatusException
/*     */   {
/* 334 */     if (!this.pool.isUsageThresholdSupported())
/* 335 */       return JvmMemoryImpl.Long0;
/* 336 */     long l = this.pool.getUsageThresholdCount();
/* 337 */     if (l > -1L) return new Long(l);
/* 338 */     return JvmMemoryImpl.Long0;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemPoolCollectThreshold()
/*     */     throws SnmpStatusException
/*     */   {
/* 345 */     if (!this.pool.isCollectionUsageThresholdSupported())
/* 346 */       return JvmMemoryImpl.Long0;
/* 347 */     long l = this.pool.getCollectionUsageThreshold();
/* 348 */     if (l > -1L) return new Long(l);
/* 349 */     return JvmMemoryImpl.Long0;
/*     */   }
/*     */ 
/*     */   public void setJvmMemPoolCollectThreshold(Long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 357 */     long l = paramLong.longValue();
/* 358 */     if (l < 0L) {
/* 359 */       throw new SnmpStatusException(10);
/*     */     }
/*     */ 
/* 363 */     this.pool.setCollectionUsageThreshold(l);
/*     */   }
/*     */ 
/*     */   public void checkJvmMemPoolCollectThreshold(Long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 374 */     if (!this.pool.isCollectionUsageThresholdSupported()) {
/* 375 */       throw new SnmpStatusException(12);
/*     */     }
/* 377 */     long l = paramLong.longValue();
/* 378 */     if (l < 0L)
/* 379 */       throw new SnmpStatusException(10);
/*     */   }
/*     */ 
/*     */   public EnumJvmMemPoolCollectThreshdSupport getJvmMemPoolCollectThreshdSupport()
/*     */     throws SnmpStatusException
/*     */   {
/* 388 */     if (this.pool.isCollectionUsageThresholdSupported()) {
/* 389 */       return EnumJvmMemPoolCollectThreshdSupported;
/*     */     }
/* 391 */     return EnumJvmMemPoolCollectThreshdUnsupported;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemPoolCollectThreshdCount()
/*     */     throws SnmpStatusException
/*     */   {
/* 399 */     if (!this.pool.isCollectionUsageThresholdSupported())
/* 400 */       return JvmMemoryImpl.Long0;
/* 401 */     long l = this.pool.getCollectionUsageThresholdCount();
/* 402 */     if (l > -1L) return new Long(l);
/* 403 */     return JvmMemoryImpl.Long0;
/*     */   }
/*     */ 
/*     */   public static EnumJvmMemPoolType jvmMemPoolType(MemoryType paramMemoryType) throws SnmpStatusException
/*     */   {
/* 408 */     if (paramMemoryType.equals(MemoryType.HEAP))
/* 409 */       return EnumJvmMemPoolTypeHeap;
/* 410 */     if (paramMemoryType.equals(MemoryType.NON_HEAP))
/* 411 */       return EnumJvmMemPoolTypeNonHeap;
/* 412 */     throw new SnmpStatusException(10);
/*     */   }
/*     */ 
/*     */   public EnumJvmMemPoolType getJvmMemPoolType()
/*     */     throws SnmpStatusException
/*     */   {
/* 419 */     return jvmMemPoolType(this.pool.getType());
/*     */   }
/*     */ 
/*     */   public String getJvmMemPoolName()
/*     */     throws SnmpStatusException
/*     */   {
/* 426 */     return JVM_MANAGEMENT_MIB_IMPL.validJavaObjectNameTC(this.pool.getName());
/*     */   }
/*     */ 
/*     */   public Integer getJvmMemPoolIndex()
/*     */     throws SnmpStatusException
/*     */   {
/* 433 */     return new Integer(this.jvmMemPoolIndex);
/*     */   }
/*     */ 
/*     */   public EnumJvmMemPoolState getJvmMemPoolState()
/*     */     throws SnmpStatusException
/*     */   {
/* 442 */     if (this.pool.isValid()) {
/* 443 */       return JvmMemPoolStateValid;
/*     */     }
/* 445 */     return JvmMemPoolStateInvalid;
/*     */   }
/*     */ 
/*     */   public synchronized Long getJvmMemPoolPeakReset()
/*     */     throws SnmpStatusException
/*     */   {
/* 453 */     return new Long(this.jvmMemPoolPeakReset);
/*     */   }
/*     */ 
/*     */   public synchronized void setJvmMemPoolPeakReset(Long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 461 */     long l1 = paramLong.longValue();
/* 462 */     if (l1 > this.jvmMemPoolPeakReset) {
/* 463 */       long l2 = System.currentTimeMillis();
/* 464 */       this.pool.resetPeakUsage();
/* 465 */       this.jvmMemPoolPeakReset = l2;
/* 466 */       log.debug("setJvmMemPoolPeakReset", "jvmMemPoolPeakReset=" + l2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void checkJvmMemPoolPeakReset(Long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmMemPoolEntryImpl
 * JD-Core Version:    0.6.2
 */