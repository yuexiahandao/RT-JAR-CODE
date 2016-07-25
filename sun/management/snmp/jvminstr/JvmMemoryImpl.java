/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.MemoryMXBean;
/*     */ import java.lang.management.MemoryType;
/*     */ import java.lang.management.MemoryUsage;
/*     */ import java.util.Map;
/*     */ import javax.management.MBeanServer;
/*     */ import sun.management.snmp.jvmmib.EnumJvmMemoryGCCall;
/*     */ import sun.management.snmp.jvmmib.EnumJvmMemoryGCVerboseLevel;
/*     */ import sun.management.snmp.jvmmib.JvmMemoryMBean;
/*     */ import sun.management.snmp.util.JvmContextFactory;
/*     */ import sun.management.snmp.util.MibLogger;
/*     */ 
/*     */ public class JvmMemoryImpl
/*     */   implements JvmMemoryMBean
/*     */ {
/*  99 */   static final EnumJvmMemoryGCCall JvmMemoryGCCallSupported = new EnumJvmMemoryGCCall("supported");
/*     */ 
/* 101 */   static final EnumJvmMemoryGCCall JvmMemoryGCCallStart = new EnumJvmMemoryGCCall("start");
/*     */ 
/* 103 */   static final EnumJvmMemoryGCCall JvmMemoryGCCallFailed = new EnumJvmMemoryGCCall("failed");
/*     */ 
/* 105 */   static final EnumJvmMemoryGCCall JvmMemoryGCCallStarted = new EnumJvmMemoryGCCall("started");
/*     */ 
/* 121 */   static final EnumJvmMemoryGCVerboseLevel JvmMemoryGCVerboseLevelVerbose = new EnumJvmMemoryGCVerboseLevel("verbose");
/*     */ 
/* 123 */   static final EnumJvmMemoryGCVerboseLevel JvmMemoryGCVerboseLevelSilent = new EnumJvmMemoryGCVerboseLevel("silent");
/*     */   static final String heapMemoryTag = "jvmMemory.getHeapMemoryUsage";
/*     */   static final String nonHeapMemoryTag = "jvmMemory.getNonHeapMemoryUsage";
/* 225 */   static final Long Long0 = new Long(0L);
/*     */ 
/* 390 */   static final MibLogger log = new MibLogger(JvmMemoryImpl.class);
/*     */ 
/*     */   public JvmMemoryImpl(SnmpMib paramSnmpMib)
/*     */   {
/*     */   }
/*     */ 
/*     */   public JvmMemoryImpl(SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/*     */   }
/*     */ 
/*     */   private MemoryUsage getMemoryUsage(MemoryType paramMemoryType)
/*     */   {
/* 148 */     if (paramMemoryType == MemoryType.HEAP) {
/* 149 */       return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
/*     */     }
/* 151 */     return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
/*     */   }
/*     */ 
/*     */   MemoryUsage getNonHeapMemoryUsage()
/*     */   {
/*     */     try {
/* 157 */       Map localMap = JvmContextFactory.getUserData();
/*     */ 
/* 159 */       if (localMap != null) {
/* 160 */         MemoryUsage localMemoryUsage1 = (MemoryUsage)localMap.get("jvmMemory.getNonHeapMemoryUsage");
/*     */ 
/* 162 */         if (localMemoryUsage1 != null) {
/* 163 */           log.debug("getNonHeapMemoryUsage", "jvmMemory.getNonHeapMemoryUsage found in cache.");
/*     */ 
/* 165 */           return localMemoryUsage1;
/*     */         }
/*     */ 
/* 168 */         MemoryUsage localMemoryUsage2 = getMemoryUsage(MemoryType.NON_HEAP);
/*     */ 
/* 174 */         localMap.put("jvmMemory.getNonHeapMemoryUsage", localMemoryUsage2);
/* 175 */         return localMemoryUsage2;
/*     */       }
/*     */ 
/* 179 */       log.trace("getNonHeapMemoryUsage", "ERROR: should never come here!");
/*     */ 
/* 181 */       return getMemoryUsage(MemoryType.NON_HEAP);
/*     */     } catch (RuntimeException localRuntimeException) {
/* 183 */       log.trace("getNonHeapMemoryUsage", "Failed to get NonHeapMemoryUsage: " + localRuntimeException);
/*     */ 
/* 185 */       log.debug("getNonHeapMemoryUsage", localRuntimeException);
/* 186 */       throw localRuntimeException;
/*     */     }
/*     */   }
/*     */ 
/*     */   MemoryUsage getHeapMemoryUsage()
/*     */   {
/*     */     try {
/* 193 */       Map localMap = JvmContextFactory.getUserData();
/*     */ 
/* 195 */       if (localMap != null) {
/* 196 */         MemoryUsage localMemoryUsage1 = (MemoryUsage)localMap.get("jvmMemory.getHeapMemoryUsage");
/* 197 */         if (localMemoryUsage1 != null) {
/* 198 */           log.debug("getHeapMemoryUsage", "jvmMemory.getHeapMemoryUsage found in cache.");
/*     */ 
/* 200 */           return localMemoryUsage1;
/*     */         }
/*     */ 
/* 203 */         MemoryUsage localMemoryUsage2 = getMemoryUsage(MemoryType.HEAP);
/*     */ 
/* 209 */         localMap.put("jvmMemory.getHeapMemoryUsage", localMemoryUsage2);
/* 210 */         return localMemoryUsage2;
/*     */       }
/*     */ 
/* 215 */       log.trace("getHeapMemoryUsage", "ERROR: should never come here!");
/* 216 */       return getMemoryUsage(MemoryType.HEAP);
/*     */     } catch (RuntimeException localRuntimeException) {
/* 218 */       log.trace("getHeapMemoryUsage", "Failed to get HeapMemoryUsage: " + localRuntimeException);
/*     */ 
/* 220 */       log.debug("getHeapMemoryUsage", localRuntimeException);
/* 221 */       throw localRuntimeException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Long getJvmMemoryNonHeapMaxSize()
/*     */     throws SnmpStatusException
/*     */   {
/* 232 */     long l = getNonHeapMemoryUsage().getMax();
/* 233 */     if (l > -1L) return new Long(l);
/* 234 */     return Long0;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemoryNonHeapCommitted()
/*     */     throws SnmpStatusException
/*     */   {
/* 241 */     long l = getNonHeapMemoryUsage().getCommitted();
/* 242 */     if (l > -1L) return new Long(l);
/* 243 */     return Long0;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemoryNonHeapUsed()
/*     */     throws SnmpStatusException
/*     */   {
/* 250 */     long l = getNonHeapMemoryUsage().getUsed();
/* 251 */     if (l > -1L) return new Long(l);
/* 252 */     return Long0;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemoryNonHeapInitSize()
/*     */     throws SnmpStatusException
/*     */   {
/* 259 */     long l = getNonHeapMemoryUsage().getInit();
/* 260 */     if (l > -1L) return new Long(l);
/* 261 */     return Long0;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemoryHeapMaxSize()
/*     */     throws SnmpStatusException
/*     */   {
/* 268 */     long l = getHeapMemoryUsage().getMax();
/* 269 */     if (l > -1L) return new Long(l);
/* 270 */     return Long0;
/*     */   }
/*     */ 
/*     */   public EnumJvmMemoryGCCall getJvmMemoryGCCall()
/*     */     throws SnmpStatusException
/*     */   {
/* 278 */     Map localMap = JvmContextFactory.getUserData();
/*     */ 
/* 280 */     if (localMap != null) {
/* 281 */       EnumJvmMemoryGCCall localEnumJvmMemoryGCCall = (EnumJvmMemoryGCCall)localMap.get("jvmMemory.getJvmMemoryGCCall");
/*     */ 
/* 283 */       if (localEnumJvmMemoryGCCall != null) return localEnumJvmMemoryGCCall;
/*     */     }
/* 285 */     return JvmMemoryGCCallSupported;
/*     */   }
/*     */ 
/*     */   public void setJvmMemoryGCCall(EnumJvmMemoryGCCall paramEnumJvmMemoryGCCall)
/*     */     throws SnmpStatusException
/*     */   {
/* 293 */     if (paramEnumJvmMemoryGCCall.intValue() == JvmMemoryGCCallStart.intValue()) {
/* 294 */       Map localMap = JvmContextFactory.getUserData();
/*     */       try
/*     */       {
/* 297 */         ManagementFactory.getMemoryMXBean().gc();
/* 298 */         if (localMap != null) localMap.put("jvmMemory.getJvmMemoryGCCall", JvmMemoryGCCallStarted); 
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 301 */         if (localMap != null) localMap.put("jvmMemory.getJvmMemoryGCCall", JvmMemoryGCCallFailed);
/*     */       }
/*     */ 
/* 304 */       return;
/*     */     }
/* 306 */     throw new SnmpStatusException(10);
/*     */   }
/*     */ 
/*     */   public void checkJvmMemoryGCCall(EnumJvmMemoryGCCall paramEnumJvmMemoryGCCall)
/*     */     throws SnmpStatusException
/*     */   {
/* 314 */     if (paramEnumJvmMemoryGCCall.intValue() != JvmMemoryGCCallStart.intValue())
/* 315 */       throw new SnmpStatusException(10);
/*     */   }
/*     */ 
/*     */   public Long getJvmMemoryHeapCommitted()
/*     */     throws SnmpStatusException
/*     */   {
/* 322 */     long l = getHeapMemoryUsage().getCommitted();
/* 323 */     if (l > -1L) return new Long(l);
/* 324 */     return Long0;
/*     */   }
/*     */ 
/*     */   public EnumJvmMemoryGCVerboseLevel getJvmMemoryGCVerboseLevel()
/*     */     throws SnmpStatusException
/*     */   {
/* 332 */     if (ManagementFactory.getMemoryMXBean().isVerbose()) {
/* 333 */       return JvmMemoryGCVerboseLevelVerbose;
/*     */     }
/* 335 */     return JvmMemoryGCVerboseLevelSilent;
/*     */   }
/*     */ 
/*     */   public void setJvmMemoryGCVerboseLevel(EnumJvmMemoryGCVerboseLevel paramEnumJvmMemoryGCVerboseLevel)
/*     */     throws SnmpStatusException
/*     */   {
/* 343 */     if (JvmMemoryGCVerboseLevelVerbose.intValue() == paramEnumJvmMemoryGCVerboseLevel.intValue())
/* 344 */       ManagementFactory.getMemoryMXBean().setVerbose(true);
/*     */     else
/* 346 */       ManagementFactory.getMemoryMXBean().setVerbose(false);
/*     */   }
/*     */ 
/*     */   public void checkJvmMemoryGCVerboseLevel(EnumJvmMemoryGCVerboseLevel paramEnumJvmMemoryGCVerboseLevel)
/*     */     throws SnmpStatusException
/*     */   {
/*     */   }
/*     */ 
/*     */   public Long getJvmMemoryHeapUsed()
/*     */     throws SnmpStatusException
/*     */   {
/* 361 */     long l = getHeapMemoryUsage().getUsed();
/* 362 */     if (l > -1L) return new Long(l);
/* 363 */     return Long0;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemoryHeapInitSize()
/*     */     throws SnmpStatusException
/*     */   {
/* 370 */     long l = getHeapMemoryUsage().getInit();
/* 371 */     if (l > -1L) return new Long(l);
/* 372 */     return Long0;
/*     */   }
/*     */ 
/*     */   public Long getJvmMemoryPendingFinalCount()
/*     */     throws SnmpStatusException
/*     */   {
/* 380 */     long l = ManagementFactory.getMemoryMXBean().getObjectPendingFinalizationCount();
/*     */ 
/* 383 */     if (l > -1L) return new Long((int)l);
/*     */ 
/* 387 */     return new Long(0L);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmMemoryImpl
 * JD-Core Version:    0.6.2
 */