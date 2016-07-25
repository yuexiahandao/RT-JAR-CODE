/*    */ package sun.management.snmp.jvminstr;
/*    */ 
/*    */ import com.sun.jmx.snmp.SnmpStatusException;
/*    */ import java.io.Serializable;
/*    */ import sun.management.snmp.jvmmib.JvmRTLibraryPathEntryMBean;
/*    */ 
/*    */ public class JvmRTLibraryPathEntryImpl
/*    */   implements JvmRTLibraryPathEntryMBean, Serializable
/*    */ {
/*    */   private final String item;
/*    */   private final int index;
/*    */ 
/*    */   public JvmRTLibraryPathEntryImpl(String paramString, int paramInt)
/*    */   {
/* 54 */     this.item = validPathElementTC(paramString);
/* 55 */     this.index = paramInt;
/*    */   }
/*    */ 
/*    */   private String validPathElementTC(String paramString) {
/* 59 */     return JVM_MANAGEMENT_MIB_IMPL.validPathElementTC(paramString);
/*    */   }
/*    */ 
/*    */   public String getJvmRTLibraryPathItem()
/*    */     throws SnmpStatusException
/*    */   {
/* 66 */     return this.item;
/*    */   }
/*    */ 
/*    */   public Integer getJvmRTLibraryPathIndex()
/*    */     throws SnmpStatusException
/*    */   {
/* 73 */     return new Integer(this.index);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmRTLibraryPathEntryImpl
 * JD-Core Version:    0.6.2
 */