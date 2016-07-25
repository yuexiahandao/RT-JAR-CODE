/*     */ package sun.management.snmp.jvmmib;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpCounter64;
/*     */ import com.sun.jmx.snmp.SnmpInt;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.SnmpString;
/*     */ import com.sun.jmx.snmp.SnmpValue;
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import com.sun.jmx.snmp.agent.SnmpMibGroup;
/*     */ import com.sun.jmx.snmp.agent.SnmpMibSubRequest;
/*     */ import com.sun.jmx.snmp.agent.SnmpMibTable;
/*     */ import com.sun.jmx.snmp.agent.SnmpStandardMetaServer;
/*     */ import com.sun.jmx.snmp.agent.SnmpStandardObjectServer;
/*     */ import java.io.Serializable;
/*     */ import javax.management.MBeanServer;
/*     */ 
/*     */ public class JvmCompilationMeta extends SnmpMibGroup
/*     */   implements Serializable, SnmpStandardMetaServer
/*     */ {
/*     */   protected JvmCompilationMBean node;
/* 293 */   protected SnmpStandardObjectServer objectserver = null;
/*     */ 
/*     */   public JvmCompilationMeta(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/*  77 */     this.objectserver = paramSnmpStandardObjectServer;
/*     */     try {
/*  79 */       registerObject(3L);
/*  80 */       registerObject(2L);
/*  81 */       registerObject(1L);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/*  83 */       throw new RuntimeException(localIllegalAccessException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public SnmpValue get(long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/*  92 */     switch ((int)paramLong) {
/*     */     case 3:
/*  94 */       return new SnmpInt(this.node.getJvmJITCompilerTimeMonitoring());
/*     */     case 2:
/*  97 */       return new SnmpCounter64(this.node.getJvmJITCompilerTimeMs());
/*     */     case 1:
/* 100 */       return new SnmpString(this.node.getJvmJITCompilerName());
/*     */     }
/*     */ 
/* 105 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public SnmpValue set(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 113 */     switch ((int)paramLong) {
/*     */     case 3:
/* 115 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 118 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 121 */       throw new SnmpStatusException(17);
/*     */     }
/*     */ 
/* 126 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   public void check(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 134 */     switch ((int)paramLong) {
/*     */     case 3:
/* 136 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 139 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 142 */       throw new SnmpStatusException(17);
/*     */     }
/*     */ 
/* 145 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   protected void setInstance(JvmCompilationMBean paramJvmCompilationMBean)
/*     */   {
/* 153 */     this.node = paramJvmCompilationMBean;
/*     */   }
/*     */ 
/*     */   public void get(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 166 */     this.objectserver.get(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void set(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 179 */     this.objectserver.set(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void check(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 192 */     this.objectserver.check(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isVariable(long paramLong)
/*     */   {
/* 200 */     switch ((int)paramLong) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/* 204 */       return true;
/*     */     }
/*     */ 
/* 208 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isReadable(long paramLong)
/*     */   {
/* 216 */     switch ((int)paramLong) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/* 220 */       return true;
/*     */     }
/*     */ 
/* 224 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean skipVariable(long paramLong, Object paramObject, int paramInt)
/*     */   {
/* 236 */     switch ((int)paramLong) {
/*     */     case 2:
/* 238 */       if (paramInt == 0) return true;
/*     */ 
/*     */       break;
/*     */     }
/*     */ 
/* 243 */     return super.skipVariable(paramLong, paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public String getAttributeName(long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 251 */     switch ((int)paramLong) {
/*     */     case 3:
/* 253 */       return "JvmJITCompilerTimeMonitoring";
/*     */     case 2:
/* 256 */       return "JvmJITCompilerTimeMs";
/*     */     case 1:
/* 259 */       return "JvmJITCompilerName";
/*     */     }
/*     */ 
/* 264 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public boolean isTable(long paramLong)
/*     */   {
/* 272 */     switch ((int)paramLong)
/*     */     {
/*     */     }
/*     */ 
/* 276 */     return false;
/*     */   }
/*     */ 
/*     */   public SnmpMibTable getTable(long paramLong)
/*     */   {
/* 283 */     return null;
/*     */   }
/*     */ 
/*     */   public void registerTableNodes(SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmCompilationMeta
 * JD-Core Version:    0.6.2
 */