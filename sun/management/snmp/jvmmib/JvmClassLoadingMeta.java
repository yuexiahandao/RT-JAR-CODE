/*     */ package sun.management.snmp.jvmmib;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpCounter64;
/*     */ import com.sun.jmx.snmp.SnmpGauge;
/*     */ import com.sun.jmx.snmp.SnmpInt;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
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
/*     */ public class JvmClassLoadingMeta extends SnmpMibGroup
/*     */   implements Serializable, SnmpStandardMetaServer
/*     */ {
/*     */   protected JvmClassLoadingMBean node;
/* 327 */   protected SnmpStandardObjectServer objectserver = null;
/*     */ 
/*     */   public JvmClassLoadingMeta(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
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
/*  95 */       return new SnmpInt(this.node.getJvmClassesVerboseLevel());
/*     */     case 3:
/*  98 */       return new SnmpCounter64(this.node.getJvmClassesUnloadedCount());
/*     */     case 2:
/* 101 */       return new SnmpCounter64(this.node.getJvmClassesTotalLoadedCount());
/*     */     case 1:
/* 104 */       return new SnmpGauge(this.node.getJvmClassesLoadedCount());
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
/* 119 */       if ((paramSnmpValue instanceof SnmpInt)) {
/*     */         try {
/* 121 */           this.node.setJvmClassesVerboseLevel(new EnumJvmClassesVerboseLevel(((SnmpInt)paramSnmpValue).toInteger()));
/*     */         } catch (IllegalArgumentException localIllegalArgumentException) {
/* 123 */           throw new SnmpStatusException(10);
/*     */         }
/* 125 */         return new SnmpInt(this.node.getJvmClassesVerboseLevel());
/*     */       }
/* 127 */       throw new SnmpStatusException(7);
/*     */     case 3:
/* 131 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 134 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 137 */       throw new SnmpStatusException(17);
/*     */     }
/*     */ 
/* 142 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   public void check(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 150 */     switch ((int)paramLong) {
/*     */     case 4:
/* 152 */       if ((paramSnmpValue instanceof SnmpInt))
/*     */         try {
/* 154 */           this.node.checkJvmClassesVerboseLevel(new EnumJvmClassesVerboseLevel(((SnmpInt)paramSnmpValue).toInteger()));
/*     */         } catch (IllegalArgumentException localIllegalArgumentException) {
/* 156 */           throw new SnmpStatusException(10);
/*     */         }
/*     */       else {
/* 159 */         throw new SnmpStatusException(7);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 3:
/* 164 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 167 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 170 */       throw new SnmpStatusException(17);
/*     */     default:
/* 173 */       throw new SnmpStatusException(17);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setInstance(JvmClassLoadingMBean paramJvmClassLoadingMBean)
/*     */   {
/* 181 */     this.node = paramJvmClassLoadingMBean;
/*     */   }
/*     */ 
/*     */   public void get(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 194 */     this.objectserver.get(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void set(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 207 */     this.objectserver.set(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void check(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 220 */     this.objectserver.check(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isVariable(long paramLong)
/*     */   {
/* 228 */     switch ((int)paramLong) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/* 233 */       return true;
/*     */     }
/*     */ 
/* 237 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isReadable(long paramLong)
/*     */   {
/* 245 */     switch ((int)paramLong) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/* 250 */       return true;
/*     */     }
/*     */ 
/* 254 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean skipVariable(long paramLong, Object paramObject, int paramInt)
/*     */   {
/* 266 */     switch ((int)paramLong) {
/*     */     case 2:
/*     */     case 3:
/* 269 */       if (paramInt == 0) return true;
/*     */ 
/*     */       break;
/*     */     }
/*     */ 
/* 274 */     return super.skipVariable(paramLong, paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public String getAttributeName(long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 282 */     switch ((int)paramLong) {
/*     */     case 4:
/* 284 */       return "JvmClassesVerboseLevel";
/*     */     case 3:
/* 287 */       return "JvmClassesUnloadedCount";
/*     */     case 2:
/* 290 */       return "JvmClassesTotalLoadedCount";
/*     */     case 1:
/* 293 */       return "JvmClassesLoadedCount";
/*     */     }
/*     */ 
/* 298 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public boolean isTable(long paramLong)
/*     */   {
/* 306 */     switch ((int)paramLong)
/*     */     {
/*     */     }
/*     */ 
/* 310 */     return false;
/*     */   }
/*     */ 
/*     */   public SnmpMibTable getTable(long paramLong)
/*     */   {
/* 317 */     return null;
/*     */   }
/*     */ 
/*     */   public void registerTableNodes(SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmClassLoadingMeta
 * JD-Core Version:    0.6.2
 */