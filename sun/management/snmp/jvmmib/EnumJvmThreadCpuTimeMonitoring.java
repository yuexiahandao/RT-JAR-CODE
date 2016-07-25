/*    */ package sun.management.snmp.jvmmib;
/*    */ 
/*    */ import com.sun.jmx.snmp.Enumerated;
/*    */ import java.io.Serializable;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public class EnumJvmThreadCpuTimeMonitoring extends Enumerated
/*    */   implements Serializable
/*    */ {
/* 46 */   protected static Hashtable<Integer, String> intTable = new Hashtable();
/*    */ 
/* 48 */   protected static Hashtable<String, Integer> stringTable = new Hashtable();
/*    */ 
/*    */   public EnumJvmThreadCpuTimeMonitoring(int paramInt)
/*    */     throws IllegalArgumentException
/*    */   {
/* 60 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   public EnumJvmThreadCpuTimeMonitoring(Integer paramInteger) throws IllegalArgumentException {
/* 64 */     super(paramInteger);
/*    */   }
/*    */ 
/*    */   public EnumJvmThreadCpuTimeMonitoring() throws IllegalArgumentException
/*    */   {
/*    */   }
/*    */ 
/*    */   public EnumJvmThreadCpuTimeMonitoring(String paramString) throws IllegalArgumentException {
/* 72 */     super(paramString);
/*    */   }
/*    */ 
/*    */   protected Hashtable getIntTable() {
/* 76 */     return intTable;
/*    */   }
/*    */ 
/*    */   protected Hashtable getStringTable() {
/* 80 */     return stringTable;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 51 */     intTable.put(new Integer(3), "enabled");
/* 52 */     intTable.put(new Integer(4), "disabled");
/* 53 */     intTable.put(new Integer(1), "unsupported");
/* 54 */     stringTable.put("enabled", new Integer(3));
/* 55 */     stringTable.put("disabled", new Integer(4));
/* 56 */     stringTable.put("unsupported", new Integer(1));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.EnumJvmThreadCpuTimeMonitoring
 * JD-Core Version:    0.6.2
 */