/*    */ package sun.management.snmp.jvmmib;
/*    */ 
/*    */ import com.sun.jmx.snmp.Enumerated;
/*    */ import java.io.Serializable;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public class EnumJvmMemPoolType extends Enumerated
/*    */   implements Serializable
/*    */ {
/* 46 */   protected static Hashtable<Integer, String> intTable = new Hashtable();
/*    */ 
/* 48 */   protected static Hashtable<String, Integer> stringTable = new Hashtable();
/*    */ 
/*    */   public EnumJvmMemPoolType(int paramInt)
/*    */     throws IllegalArgumentException
/*    */   {
/* 58 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   public EnumJvmMemPoolType(Integer paramInteger) throws IllegalArgumentException {
/* 62 */     super(paramInteger);
/*    */   }
/*    */ 
/*    */   public EnumJvmMemPoolType() throws IllegalArgumentException
/*    */   {
/*    */   }
/*    */ 
/*    */   public EnumJvmMemPoolType(String paramString) throws IllegalArgumentException {
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
/* 51 */     intTable.put(new Integer(2), "heap");
/* 52 */     intTable.put(new Integer(1), "nonheap");
/* 53 */     stringTable.put("heap", new Integer(2));
/* 54 */     stringTable.put("nonheap", new Integer(1));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.EnumJvmMemPoolType
 * JD-Core Version:    0.6.2
 */