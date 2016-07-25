/*    */ package sun.management.counter.perf;
/*    */ 
/*    */ import java.io.ObjectStreamException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.charset.Charset;
/*    */ import sun.management.counter.StringCounter;
/*    */ import sun.management.counter.Units;
/*    */ import sun.management.counter.Variability;
/*    */ 
/*    */ public class PerfStringCounter extends PerfByteArrayCounter
/*    */   implements StringCounter
/*    */ {
/* 36 */   private static Charset defaultCharset = Charset.defaultCharset();
/*    */   private static final long serialVersionUID = 6802913433363692452L;
/*    */ 
/*    */   PerfStringCounter(String paramString, Variability paramVariability, int paramInt, ByteBuffer paramByteBuffer)
/*    */   {
/* 39 */     this(paramString, paramVariability, paramInt, paramByteBuffer.limit(), paramByteBuffer);
/*    */   }
/*    */ 
/*    */   PerfStringCounter(String paramString, Variability paramVariability, int paramInt1, int paramInt2, ByteBuffer paramByteBuffer)
/*    */   {
/* 45 */     super(paramString, Units.STRING, paramVariability, paramInt1, paramInt2, paramByteBuffer);
/*    */   }
/*    */ 
/*    */   public boolean isVector()
/*    */   {
/* 50 */     return false;
/*    */   }
/*    */ 
/*    */   public int getVectorLength() {
/* 54 */     return 0;
/*    */   }
/*    */ 
/*    */   public Object getValue() {
/* 58 */     return stringValue();
/*    */   }
/*    */ 
/*    */   public String stringValue()
/*    */   {
/* 63 */     String str = "";
/* 64 */     byte[] arrayOfByte = byteArrayValue();
/*    */ 
/* 66 */     if ((arrayOfByte == null) || (arrayOfByte.length <= 1)) {
/* 67 */       return str;
/*    */     }
/*    */ 
/* 71 */     for (int i = 0; (i < arrayOfByte.length) && (arrayOfByte[i] != 0); i++);
/* 75 */     return new String(arrayOfByte, 0, i, defaultCharset);
/*    */   }
/*    */ 
/*    */   protected Object writeReplace()
/*    */     throws ObjectStreamException
/*    */   {
/* 82 */     return new StringCounterSnapshot(getName(), getUnits(), getVariability(), getFlags(), stringValue());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.counter.perf.PerfStringCounter
 * JD-Core Version:    0.6.2
 */