/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import com.sun.jmx.snmp.agent.SnmpStandardObjectServer;
/*     */ import javax.management.MBeanServer;
/*     */ import sun.management.snmp.jvmmib.JvmRTBootClassPathTableMeta;
/*     */ import sun.management.snmp.jvmmib.JvmRTClassPathTableMeta;
/*     */ import sun.management.snmp.jvmmib.JvmRTInputArgsTableMeta;
/*     */ import sun.management.snmp.jvmmib.JvmRTLibraryPathTableMeta;
/*     */ import sun.management.snmp.jvmmib.JvmRuntimeMeta;
/*     */ 
/*     */ public class JvmRuntimeMetaImpl extends JvmRuntimeMeta
/*     */ {
/*     */   public JvmRuntimeMetaImpl(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/*  76 */     super(paramSnmpMib, paramSnmpStandardObjectServer);
/*     */   }
/*     */ 
/*     */   protected JvmRTInputArgsTableMeta createJvmRTInputArgsTableMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/*  98 */     return new JvmRTInputArgsTableMetaImpl(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected JvmRTLibraryPathTableMeta createJvmRTLibraryPathTableMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 122 */     return new JvmRTLibraryPathTableMetaImpl(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected JvmRTClassPathTableMeta createJvmRTClassPathTableMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 145 */     return new JvmRTClassPathTableMetaImpl(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected JvmRTBootClassPathTableMeta createJvmRTBootClassPathTableMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 170 */     return new JvmRTBootClassPathTableMetaImpl(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmRuntimeMetaImpl
 * JD-Core Version:    0.6.2
 */