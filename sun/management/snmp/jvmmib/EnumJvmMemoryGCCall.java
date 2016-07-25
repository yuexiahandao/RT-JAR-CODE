/*    */ package sun.management.snmp.jvmmib;
/*    */ 
/*    */ import com.sun.jmx.snmp.Enumerated;
/*    */ import java.io.Serializable;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public class EnumJvmMemoryGCCall extends Enumerated
/*    */   implements Serializable
/*    */ {
/* 46 */   protected static Hashtable<Integer, String> intTable = new Hashtable();
/*    */ 
/* 48 */   protected static Hashtable<String, Integer> stringTable = new Hashtable();
/*    */ 
/*    */   public EnumJvmMemoryGCCall(int paramInt)
/*    */     throws IllegalArgumentException
/*    */   {
/* 64 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   public EnumJvmMemoryGCCall(Integer paramInteger) throws IllegalArgumentException {
/* 68 */     super(paramInteger);
/*    */   }
/*    */ 
/*    */   public EnumJvmMemoryGCCall() throws IllegalArgumentException
/*    */   {
/*    */   }
/*    */ 
/*    */   public EnumJvmMemoryGCCall(String paramString) throws IllegalArgumentException {
/* 76 */     super(paramString);
/*    */   }
/*    */ 
/*    */   protected Hashtable getIntTable() {
/* 80 */     return intTable;
/*    */   }
/*    */ 
/*    */   protected Hashtable getStringTable() {
/* 84 */     return stringTable;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 51 */     intTable.put(new Integer(2), "supported");
/* 52 */     intTable.put(new Integer(5), "failed");
/* 53 */     intTable.put(new Integer(4), "started");
/* 54 */     intTable.put(new Integer(1), "unsupported");
/* 55 */     intTable.put(new Integer(3), "start");
/* 56 */     stringTable.put("supported", new Integer(2));
/* 57 */     stringTable.put("failed", new Integer(5));
/* 58 */     stringTable.put("started", new Integer(4));
/* 59 */     stringTable.put("unsupported", new Integer(1));
/* 60 */     stringTable.put("start", new Integer(3));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.EnumJvmMemoryGCCall
 * JD-Core Version:    0.6.2
 */