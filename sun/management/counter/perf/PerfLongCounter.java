/*    */ package sun.management.counter.perf;
/*    */ 
/*    */ import java.io.ObjectStreamException;
/*    */ import java.nio.LongBuffer;
/*    */ import sun.management.counter.AbstractCounter;
/*    */ import sun.management.counter.LongCounter;
/*    */ import sun.management.counter.Units;
/*    */ import sun.management.counter.Variability;
/*    */ 
/*    */ public class PerfLongCounter extends AbstractCounter
/*    */   implements LongCounter
/*    */ {
/*    */   LongBuffer lb;
/*    */   private static final long serialVersionUID = 857711729279242948L;
/*    */ 
/*    */   PerfLongCounter(String paramString, Units paramUnits, Variability paramVariability, int paramInt, LongBuffer paramLongBuffer)
/*    */   {
/* 40 */     super(paramString, paramUnits, paramVariability, paramInt);
/* 41 */     this.lb = paramLongBuffer;
/*    */   }
/*    */ 
/*    */   public Object getValue() {
/* 45 */     return new Long(this.lb.get(0));
/*    */   }
/*    */ 
/*    */   public long longValue()
/*    */   {
/* 52 */     return this.lb.get(0);
/*    */   }
/*    */ 
/*    */   protected Object writeReplace()
/*    */     throws ObjectStreamException
/*    */   {
/* 59 */     return new LongCounterSnapshot(getName(), getUnits(), getVariability(), getFlags(), longValue());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.counter.perf.PerfLongCounter
 * JD-Core Version:    0.6.2
 */