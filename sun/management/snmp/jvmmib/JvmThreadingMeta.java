/*     */ package sun.management.snmp.jvmmib;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpCounter;
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
/*     */ public class JvmThreadingMeta extends SnmpMibGroup
/*     */   implements Serializable, SnmpStandardMetaServer
/*     */ {
/*     */   protected JvmThreadingMBean node;
/* 452 */   protected SnmpStandardObjectServer objectserver = null;
/* 453 */   protected JvmThreadInstanceTableMeta tableJvmThreadInstanceTable = null;
/*     */ 
/*     */   public JvmThreadingMeta(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/*  77 */     this.objectserver = paramSnmpStandardObjectServer;
/*     */     try {
/*  79 */       registerObject(6L);
/*  80 */       registerObject(5L);
/*  81 */       registerObject(4L);
/*  82 */       registerObject(3L);
/*  83 */       registerObject(2L);
/*  84 */       registerObject(1L);
/*  85 */       registerObject(10L);
/*  86 */       registerObject(7L);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/*  88 */       throw new RuntimeException(localIllegalAccessException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public SnmpValue get(long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/*  97 */     switch ((int)paramLong) {
/*     */     case 6:
/*  99 */       return new SnmpInt(this.node.getJvmThreadCpuTimeMonitoring());
/*     */     case 5:
/* 102 */       return new SnmpInt(this.node.getJvmThreadContentionMonitoring());
/*     */     case 4:
/* 105 */       return new SnmpCounter64(this.node.getJvmThreadTotalStartedCount());
/*     */     case 3:
/* 108 */       return new SnmpCounter(this.node.getJvmThreadPeakCount());
/*     */     case 2:
/* 111 */       return new SnmpGauge(this.node.getJvmThreadDaemonCount());
/*     */     case 1:
/* 114 */       return new SnmpGauge(this.node.getJvmThreadCount());
/*     */     case 10:
/* 117 */       throw new SnmpStatusException(224);
/*     */     case 7:
/* 121 */       return new SnmpCounter64(this.node.getJvmThreadPeakCountReset());
/*     */     case 8:
/*     */     case 9:
/*     */     }
/*     */ 
/* 126 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public SnmpValue set(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 134 */     switch ((int)paramLong) {
/*     */     case 6:
/* 136 */       if ((paramSnmpValue instanceof SnmpInt)) {
/*     */         try {
/* 138 */           this.node.setJvmThreadCpuTimeMonitoring(new EnumJvmThreadCpuTimeMonitoring(((SnmpInt)paramSnmpValue).toInteger()));
/*     */         } catch (IllegalArgumentException localIllegalArgumentException1) {
/* 140 */           throw new SnmpStatusException(10);
/*     */         }
/* 142 */         return new SnmpInt(this.node.getJvmThreadCpuTimeMonitoring());
/*     */       }
/* 144 */       throw new SnmpStatusException(7);
/*     */     case 5:
/* 148 */       if ((paramSnmpValue instanceof SnmpInt)) {
/*     */         try {
/* 150 */           this.node.setJvmThreadContentionMonitoring(new EnumJvmThreadContentionMonitoring(((SnmpInt)paramSnmpValue).toInteger()));
/*     */         } catch (IllegalArgumentException localIllegalArgumentException2) {
/* 152 */           throw new SnmpStatusException(10);
/*     */         }
/* 154 */         return new SnmpInt(this.node.getJvmThreadContentionMonitoring());
/*     */       }
/* 156 */       throw new SnmpStatusException(7);
/*     */     case 4:
/* 160 */       throw new SnmpStatusException(17);
/*     */     case 3:
/* 163 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 166 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 169 */       throw new SnmpStatusException(17);
/*     */     case 10:
/* 172 */       throw new SnmpStatusException(17);
/*     */     case 7:
/* 176 */       if ((paramSnmpValue instanceof SnmpCounter64)) {
/* 177 */         this.node.setJvmThreadPeakCountReset(((SnmpCounter64)paramSnmpValue).toLong());
/* 178 */         return new SnmpCounter64(this.node.getJvmThreadPeakCountReset());
/*     */       }
/* 180 */       throw new SnmpStatusException(7);
/*     */     case 8:
/*     */     case 9:
/*     */     }
/*     */ 
/* 186 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   public void check(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 194 */     switch ((int)paramLong) {
/*     */     case 6:
/* 196 */       if ((paramSnmpValue instanceof SnmpInt))
/*     */         try {
/* 198 */           this.node.checkJvmThreadCpuTimeMonitoring(new EnumJvmThreadCpuTimeMonitoring(((SnmpInt)paramSnmpValue).toInteger()));
/*     */         } catch (IllegalArgumentException localIllegalArgumentException1) {
/* 200 */           throw new SnmpStatusException(10);
/*     */         }
/*     */       else {
/* 203 */         throw new SnmpStatusException(7);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 5:
/* 208 */       if ((paramSnmpValue instanceof SnmpInt))
/*     */         try {
/* 210 */           this.node.checkJvmThreadContentionMonitoring(new EnumJvmThreadContentionMonitoring(((SnmpInt)paramSnmpValue).toInteger()));
/*     */         } catch (IllegalArgumentException localIllegalArgumentException2) {
/* 212 */           throw new SnmpStatusException(10);
/*     */         }
/*     */       else {
/* 215 */         throw new SnmpStatusException(7);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 4:
/* 220 */       throw new SnmpStatusException(17);
/*     */     case 3:
/* 223 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 226 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 229 */       throw new SnmpStatusException(17);
/*     */     case 10:
/* 232 */       throw new SnmpStatusException(17);
/*     */     case 7:
/* 236 */       if ((paramSnmpValue instanceof SnmpCounter64))
/* 237 */         this.node.checkJvmThreadPeakCountReset(((SnmpCounter64)paramSnmpValue).toLong());
/*     */       else
/* 239 */         throw new SnmpStatusException(7);
/*     */       break;
/*     */     case 8:
/*     */     case 9:
/*     */     default:
/* 244 */       throw new SnmpStatusException(17);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setInstance(JvmThreadingMBean paramJvmThreadingMBean)
/*     */   {
/* 252 */     this.node = paramJvmThreadingMBean;
/*     */   }
/*     */ 
/*     */   public void get(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 265 */     this.objectserver.get(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void set(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 278 */     this.objectserver.set(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void check(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 291 */     this.objectserver.check(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isVariable(long paramLong)
/*     */   {
/* 299 */     switch ((int)paramLong) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/* 307 */       return true;
/*     */     }
/*     */ 
/* 311 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isReadable(long paramLong)
/*     */   {
/* 319 */     switch ((int)paramLong) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/* 327 */       return true;
/*     */     }
/*     */ 
/* 331 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean skipVariable(long paramLong, Object paramObject, int paramInt)
/*     */   {
/* 343 */     switch ((int)paramLong) {
/*     */     case 4:
/*     */     case 7:
/* 346 */       if (paramInt == 0) return true;
/*     */ 
/*     */       break;
/*     */     }
/*     */ 
/* 351 */     return super.skipVariable(paramLong, paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public String getAttributeName(long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 359 */     switch ((int)paramLong) {
/*     */     case 6:
/* 361 */       return "JvmThreadCpuTimeMonitoring";
/*     */     case 5:
/* 364 */       return "JvmThreadContentionMonitoring";
/*     */     case 4:
/* 367 */       return "JvmThreadTotalStartedCount";
/*     */     case 3:
/* 370 */       return "JvmThreadPeakCount";
/*     */     case 2:
/* 373 */       return "JvmThreadDaemonCount";
/*     */     case 1:
/* 376 */       return "JvmThreadCount";
/*     */     case 10:
/* 379 */       throw new SnmpStatusException(224);
/*     */     case 7:
/* 383 */       return "JvmThreadPeakCountReset";
/*     */     case 8:
/*     */     case 9:
/*     */     }
/*     */ 
/* 388 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public boolean isTable(long paramLong)
/*     */   {
/* 396 */     switch ((int)paramLong) {
/*     */     case 10:
/* 398 */       return true;
/*     */     }
/*     */ 
/* 402 */     return false;
/*     */   }
/*     */ 
/*     */   public SnmpMibTable getTable(long paramLong)
/*     */   {
/* 410 */     switch ((int)paramLong) {
/*     */     case 10:
/* 412 */       return this.tableJvmThreadInstanceTable;
/*     */     }
/*     */ 
/* 416 */     return null;
/*     */   }
/*     */ 
/*     */   public void registerTableNodes(SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 423 */     this.tableJvmThreadInstanceTable = createJvmThreadInstanceTableMetaNode("JvmThreadInstanceTable", "JvmThreading", paramSnmpMib, paramMBeanServer);
/* 424 */     if (this.tableJvmThreadInstanceTable != null) {
/* 425 */       this.tableJvmThreadInstanceTable.registerEntryNode(paramSnmpMib, paramMBeanServer);
/* 426 */       paramSnmpMib.registerTableMeta("JvmThreadInstanceTable", this.tableJvmThreadInstanceTable);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected JvmThreadInstanceTableMeta createJvmThreadInstanceTableMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 448 */     return new JvmThreadInstanceTableMeta(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmThreadingMeta
 * JD-Core Version:    0.6.2
 */