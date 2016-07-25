/*    */ package sun.management.snmp.jvminstr;
/*    */ 
/*    */ import com.sun.jmx.snmp.SnmpStatusException;
/*    */ import java.io.Serializable;
/*    */ import sun.management.snmp.jvmmib.JvmRTInputArgsEntryMBean;
/*    */ 
/*    */ public class JvmRTInputArgsEntryImpl
/*    */   implements JvmRTInputArgsEntryMBean, Serializable
/*    */ {
/*    */   private final String item;
/*    */   private final int index;
/*    */ 
/*    */   public JvmRTInputArgsEntryImpl(String paramString, int paramInt)
/*    */   {
/* 54 */     this.item = validArgValueTC(paramString);
/* 55 */     this.index = paramInt;
/*    */   }
/*    */ 
/*    */   private String validArgValueTC(String paramString) {
/* 59 */     return JVM_MANAGEMENT_MIB_IMPL.validArgValueTC(paramString);
/*    */   }
/*    */ 
/*    */   public String getJvmRTInputArgsItem()
/*    */     throws SnmpStatusException
/*    */   {
/* 66 */     return this.item;
/*    */   }
/*    */ 
/*    */   public Integer getJvmRTInputArgsIndex()
/*    */     throws SnmpStatusException
/*    */   {
/* 73 */     return new Integer(this.index);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmRTInputArgsEntryImpl
 * JD-Core Version:    0.6.2
 */