/*     */ package java.lang.reflect;
/*     */ 
/*     */ public final class Array
/*     */ {
/*     */   public static Object newInstance(Class<?> paramClass, int paramInt)
/*     */     throws NegativeArraySizeException
/*     */   {
/*  70 */     return newArray(paramClass, paramInt);
/*     */   }
/*     */ 
/*     */   public static Object newInstance(Class<?> paramClass, int[] paramArrayOfInt)
/*     */     throws IllegalArgumentException, NegativeArraySizeException
/*     */   {
/* 108 */     return multiNewArray(paramClass, paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   public static native int getLength(Object paramObject)
/*     */     throws IllegalArgumentException;
/*     */ 
/*     */   public static native Object get(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   public static native boolean getBoolean(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   public static native byte getByte(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   public static native char getChar(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   public static native short getShort(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   public static native int getInt(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   public static native long getLong(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   public static native float getFloat(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   public static native double getDouble(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   public static native void set(Object paramObject1, int paramInt, Object paramObject2)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   public static native void setBoolean(Object paramObject, int paramInt, boolean paramBoolean)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   public static native void setByte(Object paramObject, int paramInt, byte paramByte)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   public static native void setChar(Object paramObject, int paramInt, char paramChar)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   public static native void setShort(Object paramObject, int paramInt, short paramShort)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   public static native void setInt(Object paramObject, int paramInt1, int paramInt2)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   public static native void setLong(Object paramObject, int paramInt, long paramLong)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   public static native void setFloat(Object paramObject, int paramInt, float paramFloat)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   public static native void setDouble(Object paramObject, int paramInt, double paramDouble)
/*     */     throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
/*     */ 
/*     */   private static native Object newArray(Class paramClass, int paramInt)
/*     */     throws NegativeArraySizeException;
/*     */ 
/*     */   private static native Object multiNewArray(Class paramClass, int[] paramArrayOfInt)
/*     */     throws IllegalArgumentException, NegativeArraySizeException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.reflect.Array
 * JD-Core Version:    0.6.2
 */