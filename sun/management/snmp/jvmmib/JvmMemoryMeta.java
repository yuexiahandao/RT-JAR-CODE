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
/*     */ public class JvmMemoryMeta extends SnmpMibGroup
/*     */   implements Serializable, SnmpStandardMetaServer
/*     */ {
/*     */   private static final long serialVersionUID = 9047644262627149214L;
/*     */   protected JvmMemoryMBean node;
/* 650 */   protected SnmpStandardObjectServer objectserver = null;
/* 651 */   protected JvmMemMgrPoolRelTableMeta tableJvmMemMgrPoolRelTable = null;
/* 652 */   protected JvmMemPoolTableMeta tableJvmMemPoolTable = null;
/* 653 */   protected JvmMemGCTableMeta tableJvmMemGCTable = null;
/* 654 */   protected JvmMemManagerTableMeta tableJvmMemManagerTable = null;
/*     */ 
/*     */   public JvmMemoryMeta(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/*  78 */     this.objectserver = paramSnmpStandardObjectServer;
/*     */     try {
/*  80 */       registerObject(120L);
/*  81 */       registerObject(23L);
/*  82 */       registerObject(22L);
/*  83 */       registerObject(21L);
/*  84 */       registerObject(110L);
/*  85 */       registerObject(20L);
/*  86 */       registerObject(13L);
/*  87 */       registerObject(12L);
/*  88 */       registerObject(3L);
/*  89 */       registerObject(11L);
/*  90 */       registerObject(2L);
/*  91 */       registerObject(101L);
/*  92 */       registerObject(10L);
/*  93 */       registerObject(1L);
/*  94 */       registerObject(100L);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/*  96 */       throw new RuntimeException(localIllegalAccessException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public SnmpValue get(long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 105 */     switch ((int)paramLong) {
/*     */     case 120:
/* 107 */       throw new SnmpStatusException(224);
/*     */     case 23:
/* 111 */       return new SnmpCounter64(this.node.getJvmMemoryNonHeapMaxSize());
/*     */     case 22:
/* 114 */       return new SnmpCounter64(this.node.getJvmMemoryNonHeapCommitted());
/*     */     case 21:
/* 117 */       return new SnmpCounter64(this.node.getJvmMemoryNonHeapUsed());
/*     */     case 110:
/* 120 */       throw new SnmpStatusException(224);
/*     */     case 20:
/* 124 */       return new SnmpCounter64(this.node.getJvmMemoryNonHeapInitSize());
/*     */     case 13:
/* 127 */       return new SnmpCounter64(this.node.getJvmMemoryHeapMaxSize());
/*     */     case 12:
/* 130 */       return new SnmpCounter64(this.node.getJvmMemoryHeapCommitted());
/*     */     case 3:
/* 133 */       return new SnmpInt(this.node.getJvmMemoryGCCall());
/*     */     case 11:
/* 136 */       return new SnmpCounter64(this.node.getJvmMemoryHeapUsed());
/*     */     case 2:
/* 139 */       return new SnmpInt(this.node.getJvmMemoryGCVerboseLevel());
/*     */     case 101:
/* 142 */       throw new SnmpStatusException(224);
/*     */     case 10:
/* 146 */       return new SnmpCounter64(this.node.getJvmMemoryHeapInitSize());
/*     */     case 1:
/* 149 */       return new SnmpGauge(this.node.getJvmMemoryPendingFinalCount());
/*     */     case 100:
/* 152 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 158 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public SnmpValue set(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 166 */     switch ((int)paramLong) {
/*     */     case 120:
/* 168 */       throw new SnmpStatusException(17);
/*     */     case 23:
/* 172 */       throw new SnmpStatusException(17);
/*     */     case 22:
/* 175 */       throw new SnmpStatusException(17);
/*     */     case 21:
/* 178 */       throw new SnmpStatusException(17);
/*     */     case 110:
/* 181 */       throw new SnmpStatusException(17);
/*     */     case 20:
/* 185 */       throw new SnmpStatusException(17);
/*     */     case 13:
/* 188 */       throw new SnmpStatusException(17);
/*     */     case 12:
/* 191 */       throw new SnmpStatusException(17);
/*     */     case 3:
/* 194 */       if ((paramSnmpValue instanceof SnmpInt)) {
/*     */         try {
/* 196 */           this.node.setJvmMemoryGCCall(new EnumJvmMemoryGCCall(((SnmpInt)paramSnmpValue).toInteger()));
/*     */         } catch (IllegalArgumentException localIllegalArgumentException1) {
/* 198 */           throw new SnmpStatusException(10);
/*     */         }
/* 200 */         return new SnmpInt(this.node.getJvmMemoryGCCall());
/*     */       }
/* 202 */       throw new SnmpStatusException(7);
/*     */     case 11:
/* 206 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 209 */       if ((paramSnmpValue instanceof SnmpInt)) {
/*     */         try {
/* 211 */           this.node.setJvmMemoryGCVerboseLevel(new EnumJvmMemoryGCVerboseLevel(((SnmpInt)paramSnmpValue).toInteger()));
/*     */         } catch (IllegalArgumentException localIllegalArgumentException2) {
/* 213 */           throw new SnmpStatusException(10);
/*     */         }
/* 215 */         return new SnmpInt(this.node.getJvmMemoryGCVerboseLevel());
/*     */       }
/* 217 */       throw new SnmpStatusException(7);
/*     */     case 101:
/* 221 */       throw new SnmpStatusException(17);
/*     */     case 10:
/* 225 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 228 */       throw new SnmpStatusException(17);
/*     */     case 100:
/* 231 */       throw new SnmpStatusException(17);
/*     */     }
/*     */ 
/* 237 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   public void check(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 245 */     switch ((int)paramLong) {
/*     */     case 120:
/* 247 */       throw new SnmpStatusException(17);
/*     */     case 23:
/* 251 */       throw new SnmpStatusException(17);
/*     */     case 22:
/* 254 */       throw new SnmpStatusException(17);
/*     */     case 21:
/* 257 */       throw new SnmpStatusException(17);
/*     */     case 110:
/* 260 */       throw new SnmpStatusException(17);
/*     */     case 20:
/* 264 */       throw new SnmpStatusException(17);
/*     */     case 13:
/* 267 */       throw new SnmpStatusException(17);
/*     */     case 12:
/* 270 */       throw new SnmpStatusException(17);
/*     */     case 3:
/* 273 */       if ((paramSnmpValue instanceof SnmpInt))
/*     */         try {
/* 275 */           this.node.checkJvmMemoryGCCall(new EnumJvmMemoryGCCall(((SnmpInt)paramSnmpValue).toInteger()));
/*     */         } catch (IllegalArgumentException localIllegalArgumentException1) {
/* 277 */           throw new SnmpStatusException(10);
/*     */         }
/*     */       else {
/* 280 */         throw new SnmpStatusException(7);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 11:
/* 285 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 288 */       if ((paramSnmpValue instanceof SnmpInt))
/*     */         try {
/* 290 */           this.node.checkJvmMemoryGCVerboseLevel(new EnumJvmMemoryGCVerboseLevel(((SnmpInt)paramSnmpValue).toInteger()));
/*     */         } catch (IllegalArgumentException localIllegalArgumentException2) {
/* 292 */           throw new SnmpStatusException(10);
/*     */         }
/*     */       else {
/* 295 */         throw new SnmpStatusException(7);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 101:
/* 300 */       throw new SnmpStatusException(17);
/*     */     case 10:
/* 304 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 307 */       throw new SnmpStatusException(17);
/*     */     case 100:
/* 310 */       throw new SnmpStatusException(17);
/*     */     default:
/* 314 */       throw new SnmpStatusException(17);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setInstance(JvmMemoryMBean paramJvmMemoryMBean)
/*     */   {
/* 322 */     this.node = paramJvmMemoryMBean;
/*     */   }
/*     */ 
/*     */   public void get(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 335 */     this.objectserver.get(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void set(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 348 */     this.objectserver.set(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void check(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 361 */     this.objectserver.check(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isVariable(long paramLong)
/*     */   {
/* 369 */     switch ((int)paramLong) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 20:
/*     */     case 21:
/*     */     case 22:
/*     */     case 23:
/* 381 */       return true;
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 14:
/*     */     case 15:
/*     */     case 16:
/*     */     case 17:
/*     */     case 18:
/* 385 */     case 19: } return false;
/*     */   }
/*     */ 
/*     */   public boolean isReadable(long paramLong)
/*     */   {
/* 393 */     switch ((int)paramLong) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 20:
/*     */     case 21:
/*     */     case 22:
/*     */     case 23:
/* 405 */       return true;
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 14:
/*     */     case 15:
/*     */     case 16:
/*     */     case 17:
/*     */     case 18:
/* 409 */     case 19: } return false;
/*     */   }
/*     */ 
/*     */   public boolean skipVariable(long paramLong, Object paramObject, int paramInt)
/*     */   {
/* 421 */     switch ((int)paramLong) {
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 20:
/*     */     case 21:
/*     */     case 22:
/*     */     case 23:
/* 430 */       if (paramInt == 0) return true; break;
/*     */     case 14:
/*     */     case 15:
/*     */     case 16:
/*     */     case 17:
/*     */     case 18:
/* 435 */     case 19: } return super.skipVariable(paramLong, paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public String getAttributeName(long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 443 */     switch ((int)paramLong) {
/*     */     case 120:
/* 445 */       throw new SnmpStatusException(224);
/*     */     case 23:
/* 449 */       return "JvmMemoryNonHeapMaxSize";
/*     */     case 22:
/* 452 */       return "JvmMemoryNonHeapCommitted";
/*     */     case 21:
/* 455 */       return "JvmMemoryNonHeapUsed";
/*     */     case 110:
/* 458 */       throw new SnmpStatusException(224);
/*     */     case 20:
/* 462 */       return "JvmMemoryNonHeapInitSize";
/*     */     case 13:
/* 465 */       return "JvmMemoryHeapMaxSize";
/*     */     case 12:
/* 468 */       return "JvmMemoryHeapCommitted";
/*     */     case 3:
/* 471 */       return "JvmMemoryGCCall";
/*     */     case 11:
/* 474 */       return "JvmMemoryHeapUsed";
/*     */     case 2:
/* 477 */       return "JvmMemoryGCVerboseLevel";
/*     */     case 101:
/* 480 */       throw new SnmpStatusException(224);
/*     */     case 10:
/* 484 */       return "JvmMemoryHeapInitSize";
/*     */     case 1:
/* 487 */       return "JvmMemoryPendingFinalCount";
/*     */     case 100:
/* 490 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 496 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public boolean isTable(long paramLong)
/*     */   {
/* 504 */     switch ((int)paramLong) {
/*     */     case 120:
/* 506 */       return true;
/*     */     case 110:
/* 508 */       return true;
/*     */     case 101:
/* 510 */       return true;
/*     */     case 100:
/* 512 */       return true;
/*     */     }
/*     */ 
/* 516 */     return false;
/*     */   }
/*     */ 
/*     */   public SnmpMibTable getTable(long paramLong)
/*     */   {
/* 524 */     switch ((int)paramLong) {
/*     */     case 120:
/* 526 */       return this.tableJvmMemMgrPoolRelTable;
/*     */     case 110:
/* 528 */       return this.tableJvmMemPoolTable;
/*     */     case 101:
/* 530 */       return this.tableJvmMemGCTable;
/*     */     case 100:
/* 532 */       return this.tableJvmMemManagerTable;
/*     */     }
/*     */ 
/* 536 */     return null;
/*     */   }
/*     */ 
/*     */   public void registerTableNodes(SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 543 */     this.tableJvmMemMgrPoolRelTable = createJvmMemMgrPoolRelTableMetaNode("JvmMemMgrPoolRelTable", "JvmMemory", paramSnmpMib, paramMBeanServer);
/* 544 */     if (this.tableJvmMemMgrPoolRelTable != null) {
/* 545 */       this.tableJvmMemMgrPoolRelTable.registerEntryNode(paramSnmpMib, paramMBeanServer);
/* 546 */       paramSnmpMib.registerTableMeta("JvmMemMgrPoolRelTable", this.tableJvmMemMgrPoolRelTable);
/*     */     }
/*     */ 
/* 549 */     this.tableJvmMemPoolTable = createJvmMemPoolTableMetaNode("JvmMemPoolTable", "JvmMemory", paramSnmpMib, paramMBeanServer);
/* 550 */     if (this.tableJvmMemPoolTable != null) {
/* 551 */       this.tableJvmMemPoolTable.registerEntryNode(paramSnmpMib, paramMBeanServer);
/* 552 */       paramSnmpMib.registerTableMeta("JvmMemPoolTable", this.tableJvmMemPoolTable);
/*     */     }
/*     */ 
/* 555 */     this.tableJvmMemGCTable = createJvmMemGCTableMetaNode("JvmMemGCTable", "JvmMemory", paramSnmpMib, paramMBeanServer);
/* 556 */     if (this.tableJvmMemGCTable != null) {
/* 557 */       this.tableJvmMemGCTable.registerEntryNode(paramSnmpMib, paramMBeanServer);
/* 558 */       paramSnmpMib.registerTableMeta("JvmMemGCTable", this.tableJvmMemGCTable);
/*     */     }
/*     */ 
/* 561 */     this.tableJvmMemManagerTable = createJvmMemManagerTableMetaNode("JvmMemManagerTable", "JvmMemory", paramSnmpMib, paramMBeanServer);
/* 562 */     if (this.tableJvmMemManagerTable != null) {
/* 563 */       this.tableJvmMemManagerTable.registerEntryNode(paramSnmpMib, paramMBeanServer);
/* 564 */       paramSnmpMib.registerTableMeta("JvmMemManagerTable", this.tableJvmMemManagerTable);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected JvmMemMgrPoolRelTableMeta createJvmMemMgrPoolRelTableMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 586 */     return new JvmMemMgrPoolRelTableMeta(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected JvmMemPoolTableMeta createJvmMemPoolTableMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 606 */     return new JvmMemPoolTableMeta(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected JvmMemGCTableMeta createJvmMemGCTableMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 626 */     return new JvmMemGCTableMeta(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected JvmMemManagerTableMeta createJvmMemManagerTableMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 646 */     return new JvmMemManagerTableMeta(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmMemoryMeta
 * JD-Core Version:    0.6.2
 */