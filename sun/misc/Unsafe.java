/*     */ package sun.misc;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.security.ProtectionDomain;
/*     */ import sun.reflect.CallerSensitive;
/*     */ import sun.reflect.Reflection;
/*     */ 
/*     */ public final class Unsafe
/*     */ {
/*  54 */   private static final Unsafe theUnsafe = new Unsafe();
/*     */   public static final int INVALID_FIELD_OFFSET = -1;
/* 712 */   public static final int ARRAY_BOOLEAN_BASE_OFFSET = theUnsafe.arrayBaseOffset([Z.class);
/*     */ 
/* 716 */   public static final int ARRAY_BYTE_BASE_OFFSET = theUnsafe.arrayBaseOffset([B.class);
/*     */ 
/* 720 */   public static final int ARRAY_SHORT_BASE_OFFSET = theUnsafe.arrayBaseOffset([S.class);
/*     */ 
/* 724 */   public static final int ARRAY_CHAR_BASE_OFFSET = theUnsafe.arrayBaseOffset([C.class);
/*     */ 
/* 728 */   public static final int ARRAY_INT_BASE_OFFSET = theUnsafe.arrayBaseOffset([I.class);
/*     */ 
/* 732 */   public static final int ARRAY_LONG_BASE_OFFSET = theUnsafe.arrayBaseOffset([J.class);
/*     */ 
/* 736 */   public static final int ARRAY_FLOAT_BASE_OFFSET = theUnsafe.arrayBaseOffset([F.class);
/*     */ 
/* 740 */   public static final int ARRAY_DOUBLE_BASE_OFFSET = theUnsafe.arrayBaseOffset([D.class);
/*     */ 
/* 744 */   public static final int ARRAY_OBJECT_BASE_OFFSET = theUnsafe.arrayBaseOffset([Ljava.lang.Object.class);
/*     */ 
/* 761 */   public static final int ARRAY_BOOLEAN_INDEX_SCALE = theUnsafe.arrayIndexScale([Z.class);
/*     */ 
/* 765 */   public static final int ARRAY_BYTE_INDEX_SCALE = theUnsafe.arrayIndexScale([B.class);
/*     */ 
/* 769 */   public static final int ARRAY_SHORT_INDEX_SCALE = theUnsafe.arrayIndexScale([S.class);
/*     */ 
/* 773 */   public static final int ARRAY_CHAR_INDEX_SCALE = theUnsafe.arrayIndexScale([C.class);
/*     */ 
/* 777 */   public static final int ARRAY_INT_INDEX_SCALE = theUnsafe.arrayIndexScale([I.class);
/*     */ 
/* 781 */   public static final int ARRAY_LONG_INDEX_SCALE = theUnsafe.arrayIndexScale([J.class);
/*     */ 
/* 785 */   public static final int ARRAY_FLOAT_INDEX_SCALE = theUnsafe.arrayIndexScale([F.class);
/*     */ 
/* 789 */   public static final int ARRAY_DOUBLE_INDEX_SCALE = theUnsafe.arrayIndexScale([D.class);
/*     */ 
/* 793 */   public static final int ARRAY_OBJECT_INDEX_SCALE = theUnsafe.arrayIndexScale([Ljava.lang.Object.class);
/*     */ 
/* 805 */   public static final int ADDRESS_SIZE = theUnsafe.addressSize();
/*     */ 
/*     */   private static native void registerNatives();
/*     */ 
/*     */   @CallerSensitive
/*     */   public static Unsafe getUnsafe()
/*     */   {
/*  88 */     Class localClass = Reflection.getCallerClass();
/*  89 */     if (localClass.getClassLoader() != null)
/*  90 */       throw new SecurityException("Unsafe");
/*  91 */     return theUnsafe;
/*     */   }
/*     */ 
/*     */   public native int getInt(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putInt(Object paramObject, long paramLong, int paramInt);
/*     */ 
/*     */   public native Object getObject(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putObject(Object paramObject1, long paramLong, Object paramObject2);
/*     */ 
/*     */   public native boolean getBoolean(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putBoolean(Object paramObject, long paramLong, boolean paramBoolean);
/*     */ 
/*     */   public native byte getByte(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putByte(Object paramObject, long paramLong, byte paramByte);
/*     */ 
/*     */   public native short getShort(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putShort(Object paramObject, long paramLong, short paramShort);
/*     */ 
/*     */   public native char getChar(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putChar(Object paramObject, long paramLong, char paramChar);
/*     */ 
/*     */   public native long getLong(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putLong(Object paramObject, long paramLong1, long paramLong2);
/*     */ 
/*     */   public native float getFloat(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putFloat(Object paramObject, long paramLong, float paramFloat);
/*     */ 
/*     */   public native double getDouble(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putDouble(Object paramObject, long paramLong, double paramDouble);
/*     */ 
/*     */   @Deprecated
/*     */   public int getInt(Object paramObject, int paramInt)
/*     */   {
/* 235 */     return getInt(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void putInt(Object paramObject, int paramInt1, int paramInt2)
/*     */   {
/* 244 */     putInt(paramObject, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public Object getObject(Object paramObject, int paramInt)
/*     */   {
/* 253 */     return getObject(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void putObject(Object paramObject1, int paramInt, Object paramObject2)
/*     */   {
/* 262 */     putObject(paramObject1, paramInt, paramObject2);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public boolean getBoolean(Object paramObject, int paramInt)
/*     */   {
/* 271 */     return getBoolean(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void putBoolean(Object paramObject, int paramInt, boolean paramBoolean)
/*     */   {
/* 280 */     putBoolean(paramObject, paramInt, paramBoolean);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public byte getByte(Object paramObject, int paramInt)
/*     */   {
/* 289 */     return getByte(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void putByte(Object paramObject, int paramInt, byte paramByte)
/*     */   {
/* 298 */     putByte(paramObject, paramInt, paramByte);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public short getShort(Object paramObject, int paramInt)
/*     */   {
/* 307 */     return getShort(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void putShort(Object paramObject, int paramInt, short paramShort)
/*     */   {
/* 316 */     putShort(paramObject, paramInt, paramShort);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public char getChar(Object paramObject, int paramInt)
/*     */   {
/* 325 */     return getChar(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void putChar(Object paramObject, int paramInt, char paramChar)
/*     */   {
/* 334 */     putChar(paramObject, paramInt, paramChar);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public long getLong(Object paramObject, int paramInt)
/*     */   {
/* 343 */     return getLong(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void putLong(Object paramObject, int paramInt, long paramLong)
/*     */   {
/* 352 */     putLong(paramObject, paramInt, paramLong);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public float getFloat(Object paramObject, int paramInt)
/*     */   {
/* 361 */     return getFloat(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void putFloat(Object paramObject, int paramInt, float paramFloat)
/*     */   {
/* 370 */     putFloat(paramObject, paramInt, paramFloat);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public double getDouble(Object paramObject, int paramInt)
/*     */   {
/* 379 */     return getDouble(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void putDouble(Object paramObject, int paramInt, double paramDouble)
/*     */   {
/* 388 */     putDouble(paramObject, paramInt, paramDouble);
/*     */   }
/*     */ 
/*     */   public native byte getByte(long paramLong);
/*     */ 
/*     */   public native void putByte(long paramLong, byte paramByte);
/*     */ 
/*     */   public native short getShort(long paramLong);
/*     */ 
/*     */   public native void putShort(long paramLong, short paramShort);
/*     */ 
/*     */   public native char getChar(long paramLong);
/*     */ 
/*     */   public native void putChar(long paramLong, char paramChar);
/*     */ 
/*     */   public native int getInt(long paramLong);
/*     */ 
/*     */   public native void putInt(long paramLong, int paramInt);
/*     */ 
/*     */   public native long getLong(long paramLong);
/*     */ 
/*     */   public native void putLong(long paramLong1, long paramLong2);
/*     */ 
/*     */   public native float getFloat(long paramLong);
/*     */ 
/*     */   public native void putFloat(long paramLong, float paramFloat);
/*     */ 
/*     */   public native double getDouble(long paramLong);
/*     */ 
/*     */   public native void putDouble(long paramLong, double paramDouble);
/*     */ 
/*     */   public native long getAddress(long paramLong);
/*     */ 
/*     */   public native void putAddress(long paramLong1, long paramLong2);
/*     */ 
/*     */   public native long allocateMemory(long paramLong);
/*     */ 
/*     */   public native long reallocateMemory(long paramLong1, long paramLong2);
/*     */ 
/*     */   public native void setMemory(Object paramObject, long paramLong1, long paramLong2, byte paramByte);
/*     */ 
/*     */   public void setMemory(long paramLong1, long paramLong2, byte paramByte)
/*     */   {
/* 529 */     setMemory(null, paramLong1, paramLong2, paramByte);
/*     */   }
/*     */ 
/*     */   public native void copyMemory(Object paramObject1, long paramLong1, Object paramObject2, long paramLong2, long paramLong3);
/*     */ 
/*     */   public void copyMemory(long paramLong1, long paramLong2, long paramLong3)
/*     */   {
/* 560 */     copyMemory(null, paramLong1, null, paramLong2, paramLong3);
/*     */   }
/*     */ 
/*     */   public native void freeMemory(long paramLong);
/*     */ 
/*     */   @Deprecated
/*     */   public int fieldOffset(Field paramField)
/*     */   {
/* 597 */     if (Modifier.isStatic(paramField.getModifiers())) {
/* 598 */       return (int)staticFieldOffset(paramField);
/*     */     }
/* 600 */     return (int)objectFieldOffset(paramField);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public Object staticFieldBase(Class paramClass)
/*     */   {
/* 624 */     Field[] arrayOfField = paramClass.getDeclaredFields();
/* 625 */     for (int i = 0; i < arrayOfField.length; i++) {
/* 626 */       if (Modifier.isStatic(arrayOfField[i].getModifiers())) {
/* 627 */         return staticFieldBase(arrayOfField[i]);
/*     */       }
/*     */     }
/* 630 */     return null;
/*     */   }
/*     */ 
/*     */   public native long staticFieldOffset(Field paramField);
/*     */ 
/*     */   public native long objectFieldOffset(Field paramField);
/*     */ 
/*     */   public native Object staticFieldBase(Field paramField);
/*     */ 
/*     */   public native boolean shouldBeInitialized(Class<?> paramClass);
/*     */ 
/*     */   public native void ensureClassInitialized(Class paramClass);
/*     */ 
/*     */   public native int arrayBaseOffset(Class paramClass);
/*     */ 
/*     */   public native int arrayIndexScale(Class paramClass);
/*     */ 
/*     */   public native int addressSize();
/*     */ 
/*     */   public native int pageSize();
/*     */ 
/*     */   public native Class defineClass(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2, ClassLoader paramClassLoader, ProtectionDomain paramProtectionDomain);
/*     */ 
/*     */   @Deprecated
/*     */   @CallerSensitive
/*     */   public native Class defineClass(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
/*     */ 
/*     */   public native Class defineAnonymousClass(Class paramClass, byte[] paramArrayOfByte, Object[] paramArrayOfObject);
/*     */ 
/*     */   public native Object allocateInstance(Class paramClass)
/*     */     throws InstantiationException;
/*     */ 
/*     */   public native void monitorEnter(Object paramObject);
/*     */ 
/*     */   public native void monitorExit(Object paramObject);
/*     */ 
/*     */   public native boolean tryMonitorEnter(Object paramObject);
/*     */ 
/*     */   public native void throwException(Throwable paramThrowable);
/*     */ 
/*     */   public final native boolean compareAndSwapObject(Object paramObject1, long paramLong, Object paramObject2, Object paramObject3);
/*     */ 
/*     */   public final native boolean compareAndSwapInt(Object paramObject, long paramLong, int paramInt1, int paramInt2);
/*     */ 
/*     */   public final native boolean compareAndSwapLong(Object paramObject, long paramLong1, long paramLong2, long paramLong3);
/*     */ 
/*     */   public native Object getObjectVolatile(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putObjectVolatile(Object paramObject1, long paramLong, Object paramObject2);
/*     */ 
/*     */   public native int getIntVolatile(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putIntVolatile(Object paramObject, long paramLong, int paramInt);
/*     */ 
/*     */   public native boolean getBooleanVolatile(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putBooleanVolatile(Object paramObject, long paramLong, boolean paramBoolean);
/*     */ 
/*     */   public native byte getByteVolatile(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putByteVolatile(Object paramObject, long paramLong, byte paramByte);
/*     */ 
/*     */   public native short getShortVolatile(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putShortVolatile(Object paramObject, long paramLong, short paramShort);
/*     */ 
/*     */   public native char getCharVolatile(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putCharVolatile(Object paramObject, long paramLong, char paramChar);
/*     */ 
/*     */   public native long getLongVolatile(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putLongVolatile(Object paramObject, long paramLong1, long paramLong2);
/*     */ 
/*     */   public native float getFloatVolatile(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putFloatVolatile(Object paramObject, long paramLong, float paramFloat);
/*     */ 
/*     */   public native double getDoubleVolatile(Object paramObject, long paramLong);
/*     */ 
/*     */   public native void putDoubleVolatile(Object paramObject, long paramLong, double paramDouble);
/*     */ 
/*     */   public native void putOrderedObject(Object paramObject1, long paramLong, Object paramObject2);
/*     */ 
/*     */   public native void putOrderedInt(Object paramObject, long paramLong, int paramInt);
/*     */ 
/*     */   public native void putOrderedLong(Object paramObject, long paramLong1, long paramLong2);
/*     */ 
/*     */   public native void unpark(Object paramObject);
/*     */ 
/*     */   public native void park(boolean paramBoolean, long paramLong);
/*     */ 
/*     */   public native int getLoadAverage(double[] paramArrayOfDouble, int paramInt);
/*     */ 
/*     */   static
/*     */   {
/*  48 */     registerNatives();
/*  49 */     Reflection.registerMethodsToFilter(Unsafe.class, new String[] { "getUnsafe" });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.Unsafe
 * JD-Core Version:    0.6.2
 */