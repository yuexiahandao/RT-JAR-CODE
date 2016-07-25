/*     */ package sun.reflect;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class UnsafeQualifiedStaticBooleanFieldAccessorImpl extends UnsafeQualifiedStaticFieldAccessorImpl
/*     */ {
/*     */   UnsafeQualifiedStaticBooleanFieldAccessorImpl(Field paramField, boolean paramBoolean)
/*     */   {
/*  34 */     super(paramField, paramBoolean);
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject) throws IllegalArgumentException {
/*  38 */     return new Boolean(getBoolean(paramObject));
/*     */   }
/*     */ 
/*     */   public boolean getBoolean(Object paramObject) throws IllegalArgumentException {
/*  42 */     return unsafe.getBooleanVolatile(this.base, this.fieldOffset);
/*     */   }
/*     */ 
/*     */   public byte getByte(Object paramObject) throws IllegalArgumentException {
/*  46 */     throw newGetByteIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public char getChar(Object paramObject) throws IllegalArgumentException {
/*  50 */     throw newGetCharIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public short getShort(Object paramObject) throws IllegalArgumentException {
/*  54 */     throw newGetShortIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public int getInt(Object paramObject) throws IllegalArgumentException {
/*  58 */     throw newGetIntIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public long getLong(Object paramObject) throws IllegalArgumentException {
/*  62 */     throw newGetLongIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public float getFloat(Object paramObject) throws IllegalArgumentException {
/*  66 */     throw newGetFloatIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public double getDouble(Object paramObject) throws IllegalArgumentException {
/*  70 */     throw newGetDoubleIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public void set(Object paramObject1, Object paramObject2)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/*  76 */     if (this.isReadOnly) {
/*  77 */       throwFinalFieldIllegalAccessException(paramObject2);
/*     */     }
/*  79 */     if (paramObject2 == null) {
/*  80 */       throwSetIllegalArgumentException(paramObject2);
/*     */     }
/*  82 */     if ((paramObject2 instanceof Boolean)) {
/*  83 */       unsafe.putBooleanVolatile(this.base, this.fieldOffset, ((Boolean)paramObject2).booleanValue());
/*  84 */       return;
/*     */     }
/*  86 */     throwSetIllegalArgumentException(paramObject2);
/*     */   }
/*     */ 
/*     */   public void setBoolean(Object paramObject, boolean paramBoolean)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/*  92 */     if (this.isReadOnly) {
/*  93 */       throwFinalFieldIllegalAccessException(paramBoolean);
/*     */     }
/*  95 */     unsafe.putBooleanVolatile(this.base, this.fieldOffset, paramBoolean);
/*     */   }
/*     */ 
/*     */   public void setByte(Object paramObject, byte paramByte)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 101 */     throwSetIllegalArgumentException(paramByte);
/*     */   }
/*     */ 
/*     */   public void setChar(Object paramObject, char paramChar)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 107 */     throwSetIllegalArgumentException(paramChar);
/*     */   }
/*     */ 
/*     */   public void setShort(Object paramObject, short paramShort)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 113 */     throwSetIllegalArgumentException(paramShort);
/*     */   }
/*     */ 
/*     */   public void setInt(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 119 */     throwSetIllegalArgumentException(paramInt);
/*     */   }
/*     */ 
/*     */   public void setLong(Object paramObject, long paramLong)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 125 */     throwSetIllegalArgumentException(paramLong);
/*     */   }
/*     */ 
/*     */   public void setFloat(Object paramObject, float paramFloat)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 131 */     throwSetIllegalArgumentException(paramFloat);
/*     */   }
/*     */ 
/*     */   public void setDouble(Object paramObject, double paramDouble)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 137 */     throwSetIllegalArgumentException(paramDouble);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.UnsafeQualifiedStaticBooleanFieldAccessorImpl
 * JD-Core Version:    0.6.2
 */