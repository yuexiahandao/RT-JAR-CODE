/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import sun.management.snmp.jvmmib.JvmMemMgrPoolRelEntryMBean;
/*     */ 
/*     */ public class JvmMemMgrPoolRelEntryImpl
/*     */   implements JvmMemMgrPoolRelEntryMBean
/*     */ {
/*     */   protected final int JvmMemManagerIndex;
/*     */   protected final int JvmMemPoolIndex;
/*     */   protected final String mmmName;
/*     */   protected final String mpmName;
/*     */ 
/*     */   public JvmMemMgrPoolRelEntryImpl(String paramString1, String paramString2, int paramInt1, int paramInt2)
/*     */   {
/*  70 */     this.JvmMemManagerIndex = paramInt1;
/*  71 */     this.JvmMemPoolIndex = paramInt2;
/*     */ 
/*  73 */     this.mmmName = paramString1;
/*  74 */     this.mpmName = paramString2;
/*     */   }
/*     */ 
/*     */   public String getJvmMemMgrRelPoolName()
/*     */     throws SnmpStatusException
/*     */   {
/*  81 */     return JVM_MANAGEMENT_MIB_IMPL.validJavaObjectNameTC(this.mpmName);
/*     */   }
/*     */ 
/*     */   public String getJvmMemMgrRelManagerName()
/*     */     throws SnmpStatusException
/*     */   {
/*  88 */     return JVM_MANAGEMENT_MIB_IMPL.validJavaObjectNameTC(this.mmmName);
/*     */   }
/*     */ 
/*     */   public Integer getJvmMemManagerIndex()
/*     */     throws SnmpStatusException
/*     */   {
/*  95 */     return new Integer(this.JvmMemManagerIndex);
/*     */   }
/*     */ 
/*     */   public Integer getJvmMemPoolIndex()
/*     */     throws SnmpStatusException
/*     */   {
/* 102 */     return new Integer(this.JvmMemPoolIndex);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmMemMgrPoolRelEntryImpl
 * JD-Core Version:    0.6.2
 */