/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import com.sun.jmx.snmp.agent.SnmpStandardObjectServer;
/*     */ import javax.management.MBeanServer;
/*     */ import sun.management.snmp.jvmmib.JvmMemGCTableMeta;
/*     */ import sun.management.snmp.jvmmib.JvmMemManagerTableMeta;
/*     */ import sun.management.snmp.jvmmib.JvmMemMgrPoolRelTableMeta;
/*     */ import sun.management.snmp.jvmmib.JvmMemPoolTableMeta;
/*     */ import sun.management.snmp.jvmmib.JvmMemoryMeta;
/*     */ 
/*     */ public class JvmMemoryMetaImpl extends JvmMemoryMeta
/*     */ {
/*     */   public JvmMemoryMetaImpl(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/*  57 */     super(paramSnmpMib, paramSnmpStandardObjectServer);
/*     */   }
/*     */ 
/*     */   protected JvmMemManagerTableMeta createJvmMemManagerTableMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/*  78 */     return new JvmMemManagerTableMetaImpl(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected JvmMemGCTableMeta createJvmMemGCTableMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 100 */     return new JvmMemGCTableMetaImpl(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected JvmMemPoolTableMeta createJvmMemPoolTableMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 123 */     return new JvmMemPoolTableMetaImpl(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected JvmMemMgrPoolRelTableMeta createJvmMemMgrPoolRelTableMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 146 */     return new JvmMemMgrPoolRelTableMetaImpl(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmMemoryMetaImpl
 * JD-Core Version:    0.6.2
 */