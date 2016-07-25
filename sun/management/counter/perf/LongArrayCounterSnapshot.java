/*    */ package sun.management.counter.perf;
/*    */ 
/*    */ import sun.management.counter.AbstractCounter;
/*    */ import sun.management.counter.LongArrayCounter;
/*    */ import sun.management.counter.Units;
/*    */ import sun.management.counter.Variability;
/*    */ 
/*    */ class LongArrayCounterSnapshot extends AbstractCounter
/*    */   implements LongArrayCounter
/*    */ {
/*    */   long[] value;
/*    */   private static final long serialVersionUID = 3585870271405924292L;
/*    */ 
/*    */   LongArrayCounterSnapshot(String paramString, Units paramUnits, Variability paramVariability, int paramInt1, int paramInt2, long[] paramArrayOfLong)
/*    */   {
/* 41 */     super(paramString, paramUnits, paramVariability, paramInt1, paramInt2);
/* 42 */     this.value = paramArrayOfLong;
/*    */   }
/*    */ 
/*    */   public Object getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */ 
/*    */   public long[] longArrayValue() {
/* 50 */     return this.value;
/*    */   }
/*    */ 
/*    */   public long longAt(int paramInt) {
/* 54 */     return this.value[paramInt];
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.counter.perf.LongArrayCounterSnapshot
 * JD-Core Version:    0.6.2
 */