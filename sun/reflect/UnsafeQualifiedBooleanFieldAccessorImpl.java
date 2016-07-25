/*     */ package sun.reflect;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class UnsafeQualifiedBooleanFieldAccessorImpl extends UnsafeQualifiedFieldAccessorImpl
/*     */ {
/*     */   UnsafeQualifiedBooleanFieldAccessorImpl(Field paramField, boolean paramBoolean)
/*     */   {
/*  34 */     super(paramField, paramBoolean);
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject) throws IllegalArgumentException {
/*  38 */     return new Boolean(getBoolean(paramObject));
/*     */   }
/*     */ 
/*     */   public boolean getBoolean(Object paramObject) throws IllegalArgumentException {
/*  42 */     ensureObj(paramObject);
/*  43 */     return unsafe.getBooleanVolatile(paramObject, this.fieldOffset);
/*     */   }
/*     */ 
/*     */   public byte getByte(Object paramObject) throws IllegalArgumentException {
/*  47 */     throw newGetByteIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public char getChar(Object paramObject) throws IllegalArgumentException {
/*  51 */     throw newGetCharIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public short getShort(Object paramObject) throws IllegalArgumentException {
/*  55 */     throw newGetShortIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public int getInt(Object paramObject) throws IllegalArgumentException {
/*  59 */     throw newGetIntIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public long getLong(Object paramObject) throws IllegalArgumentException {
/*  63 */     throw newGetLongIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public float getFloat(Object paramObject) throws IllegalArgumentException {
/*  67 */     throw newGetFloatIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public double getDouble(Object paramObject) throws IllegalArgumentException {
/*  71 */     throw newGetDoubleIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public void set(Object paramObject1, Object paramObject2)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/*  77 */     ensureObj(paramObject1);
/*  78 */     if (this.isReadOnly) {
/*  79 */       throwFinalFieldIllegalAccessException(paramObject2);
/*     */     }
/*  81 */     if (paramObject2 == null) {
/*  82 */       throwSetIllegalArgumentException(paramObject2);
/*     */     }
/*  84 */     if ((paramObject2 instanceof Boolean)) {
/*  85 */       unsafe.putBooleanVolatile(paramObject1, this.fieldOffset, ((Boolean)paramObject2).booleanValue());
/*  86 */       return;
/*     */     }
/*  88 */     throwSetIllegalArgumentException(paramObject2);
/*     */   }
/*     */ 
/*     */   public void setBoolean(Object paramObject, boolean paramBoolean)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/*  94 */     ensureObj(paramObject);
/*  95 */     if (this.isReadOnly) {
/*  96 */       throwFinalFieldIllegalAccessException(paramBoolean);
/*     */     }
/*  98 */     unsafe.putBooleanVolatile(paramObject, this.fieldOffset, paramBoolean);
/*     */   }
/*     */ 
/*     */   public void setByte(Object paramObject, byte paramByte)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 104 */     throwSetIllegalArgumentException(paramByte);
/*     */   }
/*     */ 
/*     */   public void setChar(Object paramObject, char paramChar)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 110 */     throwSetIllegalArgumentException(paramChar);
/*     */   }
/*     */ 
/*     */   public void setShort(Object paramObject, short paramShort)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 116 */     throwSetIllegalArgumentException(paramShort);
/*     */   }
/*     */ 
/*     */   public void setInt(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 122 */     throwSetIllegalArgumentException(paramInt);
/*     */   }
/*     */ 
/*     */   public void setLong(Object paramObject, long paramLong)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 128 */     throwSetIllegalArgumentException(paramLong);
/*     */   }
/*     */ 
/*     */   public void setFloat(Object paramObject, float paramFloat)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 134 */     throwSetIllegalArgumentException(paramFloat);
/*     */   }
/*     */ 
/*     */   public void setDouble(Object paramObject, double paramDouble)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 140 */     throwSetIllegalArgumentException(paramDouble);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.UnsafeQualifiedBooleanFieldAccessorImpl
 * JD-Core Version:    0.6.2
 */