/*    */ package sun.management.counter.perf;
/*    */ 
/*    */ import sun.management.counter.AbstractCounter;
/*    */ import sun.management.counter.ByteArrayCounter;
/*    */ import sun.management.counter.Units;
/*    */ import sun.management.counter.Variability;
/*    */ 
/*    */ class ByteArrayCounterSnapshot extends AbstractCounter
/*    */   implements ByteArrayCounter
/*    */ {
/*    */   byte[] value;
/*    */   private static final long serialVersionUID = 1444793459838438979L;
/*    */ 
/*    */   ByteArrayCounterSnapshot(String paramString, Units paramUnits, Variability paramVariability, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
/*    */   {
/* 41 */     super(paramString, paramUnits, paramVariability, paramInt1, paramInt2);
/* 42 */     this.value = paramArrayOfByte;
/*    */   }
/*    */ 
/*    */   public Object getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */ 
/*    */   public byte[] byteArrayValue() {
/* 50 */     return this.value;
/*    */   }
/*    */ 
/*    */   public byte byteAt(int paramInt) {
/* 54 */     return this.value[paramInt];
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.counter.perf.ByteArrayCounterSnapshot
 * JD-Core Version:    0.6.2
 */