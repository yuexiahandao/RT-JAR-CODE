/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpOidRecord;
/*     */ import com.sun.jmx.snmp.SnmpOidTable;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.management.ThreadInfo;
/*     */ import java.lang.management.ThreadMXBean;
/*     */ import sun.management.snmp.jvmmib.JVM_MANAGEMENT_MIBOidTable;
/*     */ import sun.management.snmp.jvmmib.JvmThreadInstanceEntryMBean;
/*     */ import sun.management.snmp.util.MibLogger;
/*     */ 
/*     */ public class JvmThreadInstanceEntryImpl
/*     */   implements JvmThreadInstanceEntryMBean, Serializable
/*     */ {
/*     */   private final ThreadInfo info;
/*     */   private final Byte[] index;
/* 158 */   private static String jvmThreadInstIndexOid = null;
/*     */ 
/* 326 */   static final MibLogger log = new MibLogger(JvmThreadInstanceEntryImpl.class);
/*     */ 
/*     */   public JvmThreadInstanceEntryImpl(ThreadInfo paramThreadInfo, Byte[] paramArrayOfByte)
/*     */   {
/* 153 */     this.info = paramThreadInfo;
/* 154 */     this.index = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public static String getJvmThreadInstIndexOid()
/*     */     throws SnmpStatusException
/*     */   {
/* 161 */     if (jvmThreadInstIndexOid == null) {
/* 162 */       JVM_MANAGEMENT_MIBOidTable localJVM_MANAGEMENT_MIBOidTable = new JVM_MANAGEMENT_MIBOidTable();
/* 163 */       SnmpOidRecord localSnmpOidRecord = localJVM_MANAGEMENT_MIBOidTable.resolveVarName("jvmThreadInstIndex");
/*     */ 
/* 165 */       jvmThreadInstIndexOid = localSnmpOidRecord.getOid();
/*     */     }
/* 167 */     return jvmThreadInstIndexOid;
/*     */   }
/*     */ 
/*     */   public String getJvmThreadInstLockOwnerPtr()
/*     */     throws SnmpStatusException
/*     */   {
/* 176 */     long l = this.info.getLockOwnerId();
/*     */ 
/* 178 */     if (l == -1L) {
/* 179 */       return new String("0.0");
/*     */     }
/* 181 */     SnmpOid localSnmpOid = JvmThreadInstanceTableMetaImpl.makeOid(l);
/*     */ 
/* 183 */     return getJvmThreadInstIndexOid() + "." + localSnmpOid.toString();
/*     */   }
/*     */ 
/*     */   private String validDisplayStringTC(String paramString) {
/* 187 */     return JVM_MANAGEMENT_MIB_IMPL.validDisplayStringTC(paramString);
/*     */   }
/*     */ 
/*     */   private String validJavaObjectNameTC(String paramString) {
/* 191 */     return JVM_MANAGEMENT_MIB_IMPL.validJavaObjectNameTC(paramString);
/*     */   }
/*     */ 
/*     */   private String validPathElementTC(String paramString) {
/* 195 */     return JVM_MANAGEMENT_MIB_IMPL.validPathElementTC(paramString);
/*     */   }
/*     */ 
/*     */   public String getJvmThreadInstLockName()
/*     */     throws SnmpStatusException
/*     */   {
/* 202 */     return validJavaObjectNameTC(this.info.getLockName());
/*     */   }
/*     */ 
/*     */   public String getJvmThreadInstName()
/*     */     throws SnmpStatusException
/*     */   {
/* 209 */     return validJavaObjectNameTC(this.info.getThreadName());
/*     */   }
/*     */ 
/*     */   public Long getJvmThreadInstCpuTimeNs()
/*     */     throws SnmpStatusException
/*     */   {
/* 216 */     long l = 0L;
/* 217 */     ThreadMXBean localThreadMXBean = JvmThreadingImpl.getThreadMXBean();
/*     */     try
/*     */     {
/* 220 */       if (localThreadMXBean.isThreadCpuTimeSupported()) {
/* 221 */         l = localThreadMXBean.getThreadCpuTime(this.info.getThreadId());
/* 222 */         log.debug("getJvmThreadInstCpuTimeNs", "Cpu time ns : " + l);
/*     */ 
/* 225 */         if (l == -1L) l = 0L; 
/*     */       }
/*     */     }
/*     */     catch (UnsatisfiedLinkError localUnsatisfiedLinkError)
/*     */     {
/* 229 */       log.debug("getJvmThreadInstCpuTimeNs", "Operation not supported: " + localUnsatisfiedLinkError);
/*     */     }
/*     */ 
/* 232 */     return new Long(l);
/*     */   }
/*     */ 
/*     */   public Long getJvmThreadInstBlockTimeMs()
/*     */     throws SnmpStatusException
/*     */   {
/* 239 */     long l = 0L;
/*     */ 
/* 241 */     ThreadMXBean localThreadMXBean = JvmThreadingImpl.getThreadMXBean();
/*     */ 
/* 243 */     if (localThreadMXBean.isThreadContentionMonitoringSupported()) {
/* 244 */       l = this.info.getBlockedTime();
/*     */ 
/* 247 */       if (l == -1L) l = 0L;
/*     */     }
/* 249 */     return new Long(l);
/*     */   }
/*     */ 
/*     */   public Long getJvmThreadInstBlockCount()
/*     */     throws SnmpStatusException
/*     */   {
/* 256 */     return new Long(this.info.getBlockedCount());
/*     */   }
/*     */ 
/*     */   public Long getJvmThreadInstWaitTimeMs()
/*     */     throws SnmpStatusException
/*     */   {
/* 263 */     long l = 0L;
/*     */ 
/* 265 */     ThreadMXBean localThreadMXBean = JvmThreadingImpl.getThreadMXBean();
/*     */ 
/* 267 */     if (localThreadMXBean.isThreadContentionMonitoringSupported()) {
/* 268 */       l = this.info.getWaitedTime();
/*     */ 
/* 271 */       if (l == -1L) l = 0L;
/*     */     }
/* 273 */     return new Long(l);
/*     */   }
/*     */ 
/*     */   public Long getJvmThreadInstWaitCount()
/*     */     throws SnmpStatusException
/*     */   {
/* 280 */     return new Long(this.info.getWaitedCount());
/*     */   }
/*     */ 
/*     */   public Byte[] getJvmThreadInstState()
/*     */     throws SnmpStatusException
/*     */   {
/* 288 */     return ThreadStateMap.getState(this.info);
/*     */   }
/*     */ 
/*     */   public Long getJvmThreadInstId()
/*     */     throws SnmpStatusException
/*     */   {
/* 295 */     return new Long(this.info.getThreadId());
/*     */   }
/*     */ 
/*     */   public Byte[] getJvmThreadInstIndex()
/*     */     throws SnmpStatusException
/*     */   {
/* 302 */     return this.index;
/*     */   }
/*     */ 
/*     */   private String getJvmThreadInstStackTrace()
/*     */     throws SnmpStatusException
/*     */   {
/* 309 */     StackTraceElement[] arrayOfStackTraceElement = this.info.getStackTrace();
/*     */ 
/* 312 */     StringBuffer localStringBuffer = new StringBuffer();
/* 313 */     int i = arrayOfStackTraceElement.length;
/* 314 */     log.debug("getJvmThreadInstStackTrace", "Stack size : " + i);
/* 315 */     for (int j = 0; j < i; j++) {
/* 316 */       log.debug("getJvmThreadInstStackTrace", "Append " + arrayOfStackTraceElement[j].toString());
/*     */ 
/* 318 */       localStringBuffer.append(arrayOfStackTraceElement[j].toString());
/*     */ 
/* 320 */       if (j < i) {
/* 321 */         localStringBuffer.append("\n");
/*     */       }
/*     */     }
/* 324 */     return validPathElementTC(localStringBuffer.toString());
/*     */   }
/*     */ 
/*     */   public static final class ThreadStateMap
/*     */   {
/*     */     public static final byte mask0 = 63;
/*     */     public static final byte mask1 = -128;
/*     */ 
/*     */     private static void setBit(byte[] paramArrayOfByte, int paramInt, byte paramByte)
/*     */     {
/*  82 */       paramArrayOfByte[paramInt] = ((byte)(paramArrayOfByte[paramInt] | paramByte));
/*     */     }
/*     */     public static void setNative(byte[] paramArrayOfByte) {
/*  85 */       setBit(paramArrayOfByte, 0, (byte)-128);
/*     */     }
/*     */     public static void setSuspended(byte[] paramArrayOfByte) {
/*  88 */       setBit(paramArrayOfByte, 0, (byte)64);
/*     */     }
/*     */     public static void setState(byte[] paramArrayOfByte, Thread.State paramState) {
/*  91 */       switch (JvmThreadInstanceEntryImpl.1.$SwitchMap$java$lang$Thread$State[paramState.ordinal()]) {
/*     */       case 1:
/*  93 */         setBit(paramArrayOfByte, 0, (byte)8);
/*  94 */         return;
/*     */       case 2:
/*  96 */         setBit(paramArrayOfByte, 0, (byte)32);
/*  97 */         return;
/*     */       case 3:
/*  99 */         setBit(paramArrayOfByte, 0, (byte)16);
/* 100 */         return;
/*     */       case 4:
/* 102 */         setBit(paramArrayOfByte, 0, (byte)4);
/* 103 */         return;
/*     */       case 5:
/* 105 */         setBit(paramArrayOfByte, 0, (byte)1);
/* 106 */         return;
/*     */       case 6:
/* 108 */         setBit(paramArrayOfByte, 0, (byte)2);
/* 109 */         return;
/*     */       }
/*     */     }
/*     */ 
/*     */     public static void checkOther(byte[] paramArrayOfByte) {
/* 114 */       if (((paramArrayOfByte[0] & 0x3F) == 0) && ((paramArrayOfByte[1] & 0xFFFFFF80) == 0))
/*     */       {
/* 116 */         setBit(paramArrayOfByte, 1, (byte)-128);
/*     */       }
/*     */     }
/*     */ 
/* 120 */     public static Byte[] getState(ThreadInfo paramThreadInfo) { byte[] arrayOfByte = { 0, 0 };
/*     */       try {
/* 122 */         Thread.State localState = paramThreadInfo.getThreadState();
/* 123 */         boolean bool1 = paramThreadInfo.isInNative();
/* 124 */         boolean bool2 = paramThreadInfo.isSuspended();
/* 125 */         JvmThreadInstanceEntryImpl.log.debug("getJvmThreadInstState", "[State=" + localState + ",isInNative=" + bool1 + ",isSuspended=" + bool2 + "]");
/*     */ 
/* 129 */         setState(arrayOfByte, localState);
/* 130 */         if (bool1) setNative(arrayOfByte);
/* 131 */         if (bool2) setSuspended(arrayOfByte);
/* 132 */         checkOther(arrayOfByte);
/*     */       } catch (RuntimeException localRuntimeException) {
/* 134 */         arrayOfByte[0] = 0;
/* 135 */         arrayOfByte[1] = -128;
/* 136 */         JvmThreadInstanceEntryImpl.log.trace("getJvmThreadInstState", "Unexpected exception: " + localRuntimeException);
/*     */ 
/* 138 */         JvmThreadInstanceEntryImpl.log.debug("getJvmThreadInstState", localRuntimeException);
/*     */       }
/* 140 */       Byte[] arrayOfByte1 = { new Byte(arrayOfByte[0]), new Byte(arrayOfByte[1]) };
/* 141 */       return arrayOfByte1;
/*     */     }
/*     */ 
/*     */     public static final class Byte0
/*     */     {
/*     */       public static final byte inNative = -128;
/*     */       public static final byte suspended = 64;
/*     */       public static final byte newThread = 32;
/*     */       public static final byte runnable = 16;
/*     */       public static final byte blocked = 8;
/*     */       public static final byte terminated = 4;
/*     */       public static final byte waiting = 2;
/*     */       public static final byte timedWaiting = 1;
/*     */     }
/*     */ 
/*     */     public static final class Byte1
/*     */     {
/*     */       public static final byte other = -128;
/*     */       public static final byte reserved10 = 64;
/*     */       public static final byte reserved11 = 32;
/*     */       public static final byte reserved12 = 16;
/*     */       public static final byte reserved13 = 8;
/*     */       public static final byte reserved14 = 4;
/*     */       public static final byte reserved15 = 2;
/*     */       public static final byte reserved16 = 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl
 * JD-Core Version:    0.6.2
 */