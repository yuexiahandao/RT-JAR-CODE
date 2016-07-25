/*    */ package sun.management.snmp.jvmmib;
/*    */ 
/*    */ import com.sun.jmx.snmp.Enumerated;
/*    */ import java.io.Serializable;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public class EnumJvmJITCompilerTimeMonitoring extends Enumerated
/*    */   implements Serializable
/*    */ {
/* 46 */   protected static Hashtable<Integer, String> intTable = new Hashtable();
/*    */ 
/* 48 */   protected static Hashtable<String, Integer> stringTable = new Hashtable();
/*    */ 
/*    */   public EnumJvmJITCompilerTimeMonitoring(int paramInt)
/*    */     throws IllegalArgumentException
/*    */   {
/* 58 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   public EnumJvmJITCompilerTimeMonitoring(Integer paramInteger) throws IllegalArgumentException {
/* 62 */     super(paramInteger);
/*    */   }
/*    */ 
/*    */   public EnumJvmJITCompilerTimeMonitoring() throws IllegalArgumentException
/*    */   {
/*    */   }
/*    */ 
/*    */   public EnumJvmJITCompilerTimeMonitoring(String paramString) throws IllegalArgumentException {
/* 70 */     super(paramString);
/*    */   }
/*    */ 
/*    */   protected Hashtable getIntTable() {
/* 74 */     return intTable;
/*    */   }
/*    */ 
/*    */   protected Hashtable getStringTable() {
/* 78 */     return stringTable;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 51 */     intTable.put(new Integer(2), "supported");
/* 52 */     intTable.put(new Integer(1), "unsupported");
/* 53 */     stringTable.put("supported", new Integer(2));
/* 54 */     stringTable.put("unsupported", new Integer(1));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.EnumJvmJITCompilerTimeMonitoring
 * JD-Core Version:    0.6.2
 */