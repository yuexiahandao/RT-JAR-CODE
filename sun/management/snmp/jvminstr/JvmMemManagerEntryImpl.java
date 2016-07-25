/*    */ package sun.management.snmp.jvminstr;
/*    */ 
/*    */ import com.sun.jmx.snmp.SnmpStatusException;
/*    */ import java.lang.management.MemoryManagerMXBean;
/*    */ import sun.management.snmp.jvmmib.EnumJvmMemManagerState;
/*    */ import sun.management.snmp.jvmmib.JvmMemManagerEntryMBean;
/*    */ 
/*    */ public class JvmMemManagerEntryImpl
/*    */   implements JvmMemManagerEntryMBean
/*    */ {
/*    */   protected final int JvmMemManagerIndex;
/*    */   protected MemoryManagerMXBean manager;
/* 96 */   private static final EnumJvmMemManagerState JvmMemManagerStateValid = new EnumJvmMemManagerState("valid");
/*    */ 
/* 98 */   private static final EnumJvmMemManagerState JvmMemManagerStateInvalid = new EnumJvmMemManagerState("invalid");
/*    */ 
/*    */   public JvmMemManagerEntryImpl(MemoryManagerMXBean paramMemoryManagerMXBean, int paramInt)
/*    */   {
/* 66 */     this.manager = paramMemoryManagerMXBean;
/* 67 */     this.JvmMemManagerIndex = paramInt;
/*    */   }
/*    */ 
/*    */   public String getJvmMemManagerName()
/*    */     throws SnmpStatusException
/*    */   {
/* 74 */     return JVM_MANAGEMENT_MIB_IMPL.validJavaObjectNameTC(this.manager.getName());
/*    */   }
/*    */ 
/*    */   public Integer getJvmMemManagerIndex()
/*    */     throws SnmpStatusException
/*    */   {
/* 82 */     return new Integer(this.JvmMemManagerIndex);
/*    */   }
/*    */ 
/*    */   public EnumJvmMemManagerState getJvmMemManagerState()
/*    */     throws SnmpStatusException
/*    */   {
/* 90 */     if (this.manager.isValid()) {
/* 91 */       return JvmMemManagerStateValid;
/*    */     }
/* 93 */     return JvmMemManagerStateInvalid;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmMemManagerEntryImpl
 * JD-Core Version:    0.6.2
 */