/*    */ package sun.management.counter.perf;
/*    */ 
/*    */ import sun.management.counter.AbstractCounter;
/*    */ import sun.management.counter.LongCounter;
/*    */ import sun.management.counter.Units;
/*    */ import sun.management.counter.Variability;
/*    */ 
/*    */ class LongCounterSnapshot extends AbstractCounter
/*    */   implements LongCounter
/*    */ {
/*    */   long value;
/*    */   private static final long serialVersionUID = 2054263861474565758L;
/*    */ 
/*    */   LongCounterSnapshot(String paramString, Units paramUnits, Variability paramVariability, int paramInt, long paramLong)
/*    */   {
/* 41 */     super(paramString, paramUnits, paramVariability, paramInt);
/* 42 */     this.value = paramLong;
/*    */   }
/*    */ 
/*    */   public Object getValue() {
/* 46 */     return new Long(this.value);
/*    */   }
/*    */ 
/*    */   public long longValue()
/*    */   {
/* 53 */     return this.value;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.counter.perf.LongCounterSnapshot
 * JD-Core Version:    0.6.2
 */