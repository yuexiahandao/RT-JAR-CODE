/*    */ package sun.management.counter.perf;
/*    */ 
/*    */ import java.io.ObjectStreamException;
/*    */ import java.nio.ByteBuffer;
/*    */ import sun.management.counter.AbstractCounter;
/*    */ import sun.management.counter.ByteArrayCounter;
/*    */ import sun.management.counter.Units;
/*    */ import sun.management.counter.Variability;
/*    */ 
/*    */ public class PerfByteArrayCounter extends AbstractCounter
/*    */   implements ByteArrayCounter
/*    */ {
/*    */   ByteBuffer bb;
/*    */   private static final long serialVersionUID = 2545474036937279921L;
/*    */ 
/*    */   PerfByteArrayCounter(String paramString, Units paramUnits, Variability paramVariability, int paramInt1, int paramInt2, ByteBuffer paramByteBuffer)
/*    */   {
/* 40 */     super(paramString, paramUnits, paramVariability, paramInt1, paramInt2);
/* 41 */     this.bb = paramByteBuffer;
/*    */   }
/*    */ 
/*    */   public Object getValue() {
/* 45 */     return byteArrayValue();
/*    */   }
/*    */ 
/*    */   public byte[] byteArrayValue()
/*    */   {
/* 53 */     this.bb.position(0);
/* 54 */     byte[] arrayOfByte = new byte[this.bb.limit()];
/*    */ 
/* 57 */     this.bb.get(arrayOfByte);
/*    */ 
/* 59 */     return arrayOfByte;
/*    */   }
/*    */ 
/*    */   public byte byteAt(int paramInt)
/*    */   {
/* 66 */     this.bb.position(paramInt);
/* 67 */     return this.bb.get();
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 71 */     String str = getName() + ": " + new String(byteArrayValue()) + " " + getUnits();
/*    */ 
/* 73 */     if (isInternal()) {
/* 74 */       return str + " [INTERNAL]";
/*    */     }
/* 76 */     return str;
/*    */   }
/*    */ 
/*    */   protected Object writeReplace()
/*    */     throws ObjectStreamException
/*    */   {
/* 84 */     return new ByteArrayCounterSnapshot(getName(), getUnits(), getVariability(), getFlags(), getVectorLength(), byteArrayValue());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.counter.perf.PerfByteArrayCounter
 * JD-Core Version:    0.6.2
 */