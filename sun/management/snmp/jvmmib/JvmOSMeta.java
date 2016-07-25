/*     */ package sun.management.snmp.jvmmib;
/*     */ 
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
/*     */ public class JvmOSMeta extends SnmpMibGroup
/*     */   implements Serializable, SnmpStandardMetaServer
/*     */ {
/*     */   protected JvmOSMBean node;
/* 301 */   protected SnmpStandardObjectServer objectserver = null;
/*     */ 
/*     */   public JvmOSMeta(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/*  77 */     this.objectserver = paramSnmpStandardObjectServer;
/*     */     try {
/*  79 */       registerObject(4L);
/*  80 */       registerObject(3L);
/*  81 */       registerObject(2L);
/*  82 */       registerObject(1L);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/*  84 */       throw new RuntimeException(localIllegalAccessException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public SnmpValue get(long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/*  93 */     switch ((int)paramLong) {
/*     */     case 4:
/*  95 */       return new SnmpInt(this.node.getJvmOSProcessorCount());
/*     */     case 3:
/*  98 */       return new SnmpString(this.node.getJvmOSVersion());
/*     */     case 2:
/* 101 */       return new SnmpString(this.node.getJvmOSArch());
/*     */     case 1:
/* 104 */       return new SnmpString(this.node.getJvmOSName());
/*     */     }
/*     */ 
/* 109 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public SnmpValue set(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 117 */     switch ((int)paramLong) {
/*     */     case 4:
/* 119 */       throw new SnmpStatusException(17);
/*     */     case 3:
/* 122 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 125 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 128 */       throw new SnmpStatusException(17);
/*     */     }
/*     */ 
/* 133 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   public void check(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 141 */     switch ((int)paramLong) {
/*     */     case 4:
/* 143 */       throw new SnmpStatusException(17);
/*     */     case 3:
/* 146 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 149 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 152 */       throw new SnmpStatusException(17);
/*     */     }
/*     */ 
/* 155 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   protected void setInstance(JvmOSMBean paramJvmOSMBean)
/*     */   {
/* 163 */     this.node = paramJvmOSMBean;
/*     */   }
/*     */ 
/*     */   public void get(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 176 */     this.objectserver.get(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void set(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 189 */     this.objectserver.set(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void check(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 202 */     this.objectserver.check(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isVariable(long paramLong)
/*     */   {
/* 210 */     switch ((int)paramLong) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/* 215 */       return true;
/*     */     }
/*     */ 
/* 219 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isReadable(long paramLong)
/*     */   {
/* 227 */     switch ((int)paramLong) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/* 232 */       return true;
/*     */     }
/*     */ 
/* 236 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean skipVariable(long paramLong, Object paramObject, int paramInt)
/*     */   {
/* 248 */     return super.skipVariable(paramLong, paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public String getAttributeName(long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 256 */     switch ((int)paramLong) {
/*     */     case 4:
/* 258 */       return "JvmOSProcessorCount";
/*     */     case 3:
/* 261 */       return "JvmOSVersion";
/*     */     case 2:
/* 264 */       return "JvmOSArch";
/*     */     case 1:
/* 267 */       return "JvmOSName";
/*     */     }
/*     */ 
/* 272 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public boolean isTable(long paramLong)
/*     */   {
/* 280 */     switch ((int)paramLong)
/*     */     {
/*     */     }
/*     */ 
/* 284 */     return false;
/*     */   }
/*     */ 
/*     */   public SnmpMibTable getTable(long paramLong)
/*     */   {
/* 291 */     return null;
/*     */   }
/*     */ 
/*     */   public void registerTableNodes(SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmOSMeta
 * JD-Core Version:    0.6.2
 */