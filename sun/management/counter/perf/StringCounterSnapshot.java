/*    */ package sun.management.counter.perf;
/*    */ 
/*    */ import sun.management.counter.AbstractCounter;
/*    */ import sun.management.counter.StringCounter;
/*    */ import sun.management.counter.Units;
/*    */ import sun.management.counter.Variability;
/*    */ 
/*    */ class StringCounterSnapshot extends AbstractCounter
/*    */   implements StringCounter
/*    */ {
/*    */   String value;
/*    */   private static final long serialVersionUID = 1132921539085572034L;
/*    */ 
/*    */   StringCounterSnapshot(String paramString1, Units paramUnits, Variability paramVariability, int paramInt, String paramString2)
/*    */   {
/* 41 */     super(paramString1, paramUnits, paramVariability, paramInt);
/* 42 */     this.value = paramString2;
/*    */   }
/*    */ 
/*    */   public Object getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */ 
/*    */   public String stringValue() {
/* 50 */     return this.value;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.counter.perf.StringCounterSnapshot
 * JD-Core Version:    0.6.2
 */