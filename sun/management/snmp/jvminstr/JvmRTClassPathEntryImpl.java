/*    */ package sun.management.snmp.jvminstr;
/*    */ 
/*    */ import com.sun.jmx.snmp.SnmpStatusException;
/*    */ import java.io.Serializable;
/*    */ import sun.management.snmp.jvmmib.JvmRTClassPathEntryMBean;
/*    */ 
/*    */ public class JvmRTClassPathEntryImpl
/*    */   implements JvmRTClassPathEntryMBean, Serializable
/*    */ {
/*    */   private final String item;
/*    */   private final int index;
/*    */ 
/*    */   public JvmRTClassPathEntryImpl(String paramString, int paramInt)
/*    */   {
/* 54 */     this.item = validPathElementTC(paramString);
/* 55 */     this.index = paramInt;
/*    */   }
/*    */ 
/*    */   private String validPathElementTC(String paramString) {
/* 59 */     return JVM_MANAGEMENT_MIB_IMPL.validPathElementTC(paramString);
/*    */   }
/*    */ 
/*    */   public String getJvmRTClassPathItem()
/*    */     throws SnmpStatusException
/*    */   {
/* 66 */     return this.item;
/*    */   }
/*    */ 
/*    */   public Integer getJvmRTClassPathIndex()
/*    */     throws SnmpStatusException
/*    */   {
/* 73 */     return new Integer(this.index);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmRTClassPathEntryImpl
 * JD-Core Version:    0.6.2
 */