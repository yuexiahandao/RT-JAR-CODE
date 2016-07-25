/*    */ package sun.reflect;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Member;
/*    */ 
/*    */ public class ConstantPool
/*    */ {
/*    */   private Object constantPoolOop;
/*    */ 
/*    */   public int getSize()
/*    */   {
/* 36 */     return getSize0(this.constantPoolOop); } 
/* 37 */   public Class getClassAt(int paramInt) { return getClassAt0(this.constantPoolOop, paramInt); } 
/* 38 */   public Class getClassAtIfLoaded(int paramInt) { return getClassAtIfLoaded0(this.constantPoolOop, paramInt); }
/*    */ 
/*    */   public Member getMethodAt(int paramInt) {
/* 41 */     return getMethodAt0(this.constantPoolOop, paramInt); } 
/* 42 */   public Member getMethodAtIfLoaded(int paramInt) { return getMethodAtIfLoaded0(this.constantPoolOop, paramInt); } 
/* 43 */   public Field getFieldAt(int paramInt) { return getFieldAt0(this.constantPoolOop, paramInt); } 
/* 44 */   public Field getFieldAtIfLoaded(int paramInt) { return getFieldAtIfLoaded0(this.constantPoolOop, paramInt); }
/*    */ 
/*    */   public String[] getMemberRefInfoAt(int paramInt) {
/* 47 */     return getMemberRefInfoAt0(this.constantPoolOop, paramInt); } 
/* 48 */   public int getIntAt(int paramInt) { return getIntAt0(this.constantPoolOop, paramInt); } 
/* 49 */   public long getLongAt(int paramInt) { return getLongAt0(this.constantPoolOop, paramInt); } 
/* 50 */   public float getFloatAt(int paramInt) { return getFloatAt0(this.constantPoolOop, paramInt); } 
/* 51 */   public double getDoubleAt(int paramInt) { return getDoubleAt0(this.constantPoolOop, paramInt); } 
/* 52 */   public String getStringAt(int paramInt) { return getStringAt0(this.constantPoolOop, paramInt); } 
/* 53 */   public String getUTF8At(int paramInt) { return getUTF8At0(this.constantPoolOop, paramInt); } 
/*    */   private native int getSize0(Object paramObject);
/*    */ 
/*    */   private native Class getClassAt0(Object paramObject, int paramInt);
/*    */ 
/*    */   private native Class getClassAtIfLoaded0(Object paramObject, int paramInt);
/*    */ 
/*    */   private native Member getMethodAt0(Object paramObject, int paramInt);
/*    */ 
/*    */   private native Member getMethodAtIfLoaded0(Object paramObject, int paramInt);
/*    */ 
/*    */   private native Field getFieldAt0(Object paramObject, int paramInt);
/*    */ 
/*    */   private native Field getFieldAtIfLoaded0(Object paramObject, int paramInt);
/*    */ 
/*    */   private native String[] getMemberRefInfoAt0(Object paramObject, int paramInt);
/*    */ 
/*    */   private native int getIntAt0(Object paramObject, int paramInt);
/*    */ 
/*    */   private native long getLongAt0(Object paramObject, int paramInt);
/*    */ 
/*    */   private native float getFloatAt0(Object paramObject, int paramInt);
/*    */ 
/*    */   private native double getDoubleAt0(Object paramObject, int paramInt);
/*    */ 
/*    */   private native String getStringAt0(Object paramObject, int paramInt);
/*    */ 
/*    */   private native String getUTF8At0(Object paramObject, int paramInt);
/*    */ 
/* 60 */   static { Reflection.registerFieldsToFilter(ConstantPool.class, new String[] { "constantPoolOop" }); }
/*    */ 
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.ConstantPool
 * JD-Core Version:    0.6.2
 */