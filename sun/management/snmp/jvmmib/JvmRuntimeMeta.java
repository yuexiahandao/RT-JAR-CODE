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
/*     */ public class JvmRuntimeMeta extends SnmpMibGroup
/*     */   implements Serializable, SnmpStandardMetaServer
/*     */ {
/*     */   protected JvmRuntimeMBean node;
/* 622 */   protected SnmpStandardObjectServer objectserver = null;
/* 623 */   protected JvmRTLibraryPathTableMeta tableJvmRTLibraryPathTable = null;
/* 624 */   protected JvmRTClassPathTableMeta tableJvmRTClassPathTable = null;
/* 625 */   protected JvmRTBootClassPathTableMeta tableJvmRTBootClassPathTable = null;
/* 626 */   protected JvmRTInputArgsTableMeta tableJvmRTInputArgsTable = null;
/*     */ 
/*     */   public JvmRuntimeMeta(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/*  77 */     this.objectserver = paramSnmpStandardObjectServer;
/*     */     try {
/*  79 */       registerObject(23L);
/*  80 */       registerObject(22L);
/*  81 */       registerObject(21L);
/*  82 */       registerObject(9L);
/*  83 */       registerObject(20L);
/*  84 */       registerObject(8L);
/*  85 */       registerObject(7L);
/*  86 */       registerObject(6L);
/*  87 */       registerObject(5L);
/*  88 */       registerObject(4L);
/*  89 */       registerObject(3L);
/*  90 */       registerObject(12L);
/*  91 */       registerObject(11L);
/*  92 */       registerObject(2L);
/*  93 */       registerObject(1L);
/*  94 */       registerObject(10L);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/*  96 */       throw new RuntimeException(localIllegalAccessException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public SnmpValue get(long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 105 */     switch ((int)paramLong) {
/*     */     case 23:
/* 107 */       throw new SnmpStatusException(224);
/*     */     case 22:
/* 111 */       throw new SnmpStatusException(224);
/*     */     case 21:
/* 115 */       throw new SnmpStatusException(224);
/*     */     case 9:
/* 119 */       return new SnmpInt(this.node.getJvmRTBootClassPathSupport());
/*     */     case 20:
/* 122 */       throw new SnmpStatusException(224);
/*     */     case 8:
/* 126 */       return new SnmpString(this.node.getJvmRTManagementSpecVersion());
/*     */     case 7:
/* 129 */       return new SnmpString(this.node.getJvmRTSpecVersion());
/*     */     case 6:
/* 132 */       return new SnmpString(this.node.getJvmRTSpecVendor());
/*     */     case 5:
/* 135 */       return new SnmpString(this.node.getJvmRTSpecName());
/*     */     case 4:
/* 138 */       return new SnmpString(this.node.getJvmRTVMVersion());
/*     */     case 3:
/* 141 */       return new SnmpString(this.node.getJvmRTVMVendor());
/*     */     case 12:
/* 144 */       return new SnmpCounter64(this.node.getJvmRTStartTimeMs());
/*     */     case 11:
/* 147 */       return new SnmpCounter64(this.node.getJvmRTUptimeMs());
/*     */     case 2:
/* 150 */       return new SnmpString(this.node.getJvmRTVMName());
/*     */     case 1:
/* 153 */       return new SnmpString(this.node.getJvmRTName());
/*     */     case 10:
/* 156 */       return new SnmpInt(this.node.getJvmRTInputArgsCount());
/*     */     case 13:
/*     */     case 14:
/*     */     case 15:
/*     */     case 16:
/*     */     case 17:
/*     */     case 18:
/* 161 */     case 19: } throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public SnmpValue set(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 169 */     switch ((int)paramLong) {
/*     */     case 23:
/* 171 */       throw new SnmpStatusException(17);
/*     */     case 22:
/* 175 */       throw new SnmpStatusException(17);
/*     */     case 21:
/* 179 */       throw new SnmpStatusException(17);
/*     */     case 9:
/* 183 */       throw new SnmpStatusException(17);
/*     */     case 20:
/* 186 */       throw new SnmpStatusException(17);
/*     */     case 8:
/* 190 */       throw new SnmpStatusException(17);
/*     */     case 7:
/* 193 */       throw new SnmpStatusException(17);
/*     */     case 6:
/* 196 */       throw new SnmpStatusException(17);
/*     */     case 5:
/* 199 */       throw new SnmpStatusException(17);
/*     */     case 4:
/* 202 */       throw new SnmpStatusException(17);
/*     */     case 3:
/* 205 */       throw new SnmpStatusException(17);
/*     */     case 12:
/* 208 */       throw new SnmpStatusException(17);
/*     */     case 11:
/* 211 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 214 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 217 */       throw new SnmpStatusException(17);
/*     */     case 10:
/* 220 */       throw new SnmpStatusException(17);
/*     */     case 13:
/*     */     case 14:
/*     */     case 15:
/*     */     case 16:
/*     */     case 17:
/*     */     case 18:
/* 225 */     case 19: } throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   public void check(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 233 */     switch ((int)paramLong) {
/*     */     case 23:
/* 235 */       throw new SnmpStatusException(17);
/*     */     case 22:
/* 239 */       throw new SnmpStatusException(17);
/*     */     case 21:
/* 243 */       throw new SnmpStatusException(17);
/*     */     case 9:
/* 247 */       throw new SnmpStatusException(17);
/*     */     case 20:
/* 250 */       throw new SnmpStatusException(17);
/*     */     case 8:
/* 254 */       throw new SnmpStatusException(17);
/*     */     case 7:
/* 257 */       throw new SnmpStatusException(17);
/*     */     case 6:
/* 260 */       throw new SnmpStatusException(17);
/*     */     case 5:
/* 263 */       throw new SnmpStatusException(17);
/*     */     case 4:
/* 266 */       throw new SnmpStatusException(17);
/*     */     case 3:
/* 269 */       throw new SnmpStatusException(17);
/*     */     case 12:
/* 272 */       throw new SnmpStatusException(17);
/*     */     case 11:
/* 275 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 278 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 281 */       throw new SnmpStatusException(17);
/*     */     case 10:
/* 284 */       throw new SnmpStatusException(17);
/*     */     case 13:
/*     */     case 14:
/*     */     case 15:
/*     */     case 16:
/*     */     case 17:
/*     */     case 18:
/* 287 */     case 19: } throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   protected void setInstance(JvmRuntimeMBean paramJvmRuntimeMBean)
/*     */   {
/* 295 */     this.node = paramJvmRuntimeMBean;
/*     */   }
/*     */ 
/*     */   public void get(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 308 */     this.objectserver.get(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void set(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 321 */     this.objectserver.set(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void check(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 334 */     this.objectserver.check(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isVariable(long paramLong)
/*     */   {
/* 342 */     switch ((int)paramLong) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/* 355 */       return true;
/*     */     }
/*     */ 
/* 359 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isReadable(long paramLong)
/*     */   {
/* 367 */     switch ((int)paramLong) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/* 380 */       return true;
/*     */     }
/*     */ 
/* 384 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean skipVariable(long paramLong, Object paramObject, int paramInt)
/*     */   {
/* 396 */     switch ((int)paramLong) {
/*     */     case 11:
/*     */     case 12:
/* 399 */       if (paramInt == 0) return true;
/*     */ 
/*     */       break;
/*     */     }
/*     */ 
/* 404 */     return super.skipVariable(paramLong, paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public String getAttributeName(long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 412 */     switch ((int)paramLong) {
/*     */     case 23:
/* 414 */       throw new SnmpStatusException(224);
/*     */     case 22:
/* 418 */       throw new SnmpStatusException(224);
/*     */     case 21:
/* 422 */       throw new SnmpStatusException(224);
/*     */     case 9:
/* 426 */       return "JvmRTBootClassPathSupport";
/*     */     case 20:
/* 429 */       throw new SnmpStatusException(224);
/*     */     case 8:
/* 433 */       return "JvmRTManagementSpecVersion";
/*     */     case 7:
/* 436 */       return "JvmRTSpecVersion";
/*     */     case 6:
/* 439 */       return "JvmRTSpecVendor";
/*     */     case 5:
/* 442 */       return "JvmRTSpecName";
/*     */     case 4:
/* 445 */       return "JvmRTVMVersion";
/*     */     case 3:
/* 448 */       return "JvmRTVMVendor";
/*     */     case 12:
/* 451 */       return "JvmRTStartTimeMs";
/*     */     case 11:
/* 454 */       return "JvmRTUptimeMs";
/*     */     case 2:
/* 457 */       return "JvmRTVMName";
/*     */     case 1:
/* 460 */       return "JvmRTName";
/*     */     case 10:
/* 463 */       return "JvmRTInputArgsCount";
/*     */     case 13:
/*     */     case 14:
/*     */     case 15:
/*     */     case 16:
/*     */     case 17:
/*     */     case 18:
/* 468 */     case 19: } throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public boolean isTable(long paramLong)
/*     */   {
/* 476 */     switch ((int)paramLong) {
/*     */     case 23:
/* 478 */       return true;
/*     */     case 22:
/* 480 */       return true;
/*     */     case 21:
/* 482 */       return true;
/*     */     case 20:
/* 484 */       return true;
/*     */     }
/*     */ 
/* 488 */     return false;
/*     */   }
/*     */ 
/*     */   public SnmpMibTable getTable(long paramLong)
/*     */   {
/* 496 */     switch ((int)paramLong) {
/*     */     case 23:
/* 498 */       return this.tableJvmRTLibraryPathTable;
/*     */     case 22:
/* 500 */       return this.tableJvmRTClassPathTable;
/*     */     case 21:
/* 502 */       return this.tableJvmRTBootClassPathTable;
/*     */     case 20:
/* 504 */       return this.tableJvmRTInputArgsTable;
/*     */     }
/*     */ 
/* 508 */     return null;
/*     */   }
/*     */ 
/*     */   public void registerTableNodes(SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 515 */     this.tableJvmRTLibraryPathTable = createJvmRTLibraryPathTableMetaNode("JvmRTLibraryPathTable", "JvmRuntime", paramSnmpMib, paramMBeanServer);
/* 516 */     if (this.tableJvmRTLibraryPathTable != null) {
/* 517 */       this.tableJvmRTLibraryPathTable.registerEntryNode(paramSnmpMib, paramMBeanServer);
/* 518 */       paramSnmpMib.registerTableMeta("JvmRTLibraryPathTable", this.tableJvmRTLibraryPathTable);
/*     */     }
/*     */ 
/* 521 */     this.tableJvmRTClassPathTable = createJvmRTClassPathTableMetaNode("JvmRTClassPathTable", "JvmRuntime", paramSnmpMib, paramMBeanServer);
/* 522 */     if (this.tableJvmRTClassPathTable != null) {
/* 523 */       this.tableJvmRTClassPathTable.registerEntryNode(paramSnmpMib, paramMBeanServer);
/* 524 */       paramSnmpMib.registerTableMeta("JvmRTClassPathTable", this.tableJvmRTClassPathTable);
/*     */     }
/*     */ 
/* 527 */     this.tableJvmRTBootClassPathTable = createJvmRTBootClassPathTableMetaNode("JvmRTBootClassPathTable", "JvmRuntime", paramSnmpMib, paramMBeanServer);
/* 528 */     if (this.tableJvmRTBootClassPathTable != null) {
/* 529 */       this.tableJvmRTBootClassPathTable.registerEntryNode(paramSnmpMib, paramMBeanServer);
/* 530 */       paramSnmpMib.registerTableMeta("JvmRTBootClassPathTable", this.tableJvmRTBootClassPathTable);
/*     */     }
/*     */ 
/* 533 */     this.tableJvmRTInputArgsTable = createJvmRTInputArgsTableMetaNode("JvmRTInputArgsTable", "JvmRuntime", paramSnmpMib, paramMBeanServer);
/* 534 */     if (this.tableJvmRTInputArgsTable != null) {
/* 535 */       this.tableJvmRTInputArgsTable.registerEntryNode(paramSnmpMib, paramMBeanServer);
/* 536 */       paramSnmpMib.registerTableMeta("JvmRTInputArgsTable", this.tableJvmRTInputArgsTable);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected JvmRTLibraryPathTableMeta createJvmRTLibraryPathTableMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 558 */     return new JvmRTLibraryPathTableMeta(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected JvmRTClassPathTableMeta createJvmRTClassPathTableMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 578 */     return new JvmRTClassPathTableMeta(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected JvmRTBootClassPathTableMeta createJvmRTBootClassPathTableMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 598 */     return new JvmRTBootClassPathTableMeta(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected JvmRTInputArgsTableMeta createJvmRTInputArgsTableMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 618 */     return new JvmRTInputArgsTableMeta(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmRuntimeMeta
 * JD-Core Version:    0.6.2
 */