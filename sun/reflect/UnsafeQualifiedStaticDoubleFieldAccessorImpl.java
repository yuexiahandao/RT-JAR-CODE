/*     */ package sun.reflect;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class UnsafeQualifiedStaticDoubleFieldAccessorImpl extends UnsafeQualifiedStaticFieldAccessorImpl
/*     */ {
/*     */   UnsafeQualifiedStaticDoubleFieldAccessorImpl(Field paramField, boolean paramBoolean)
/*     */   {
/*  34 */     super(paramField, paramBoolean);
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject) throws IllegalArgumentException {
/*  38 */     return new Double(getDouble(paramObject));
/*     */   }
/*     */ 
/*     */   public boolean getBoolean(Object paramObject) throws IllegalArgumentException {
/*  42 */     throw newGetBooleanIllegalArgumentException();
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
/*  70 */     return unsafe.getDoubleVolatile(this.base, this.fieldOffset);
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
/*  82 */     if ((paramObject2 instanceof Byte)) {
/*  83 */       unsafe.putDoubleVolatile(this.base, this.fieldOffset, ((Byte)paramObject2).byteValue());
/*  84 */       return;
/*     */     }
/*  86 */     if ((paramObject2 instanceof Short)) {
/*  87 */       unsafe.putDoubleVolatile(this.base, this.fieldOffset, ((Short)paramObject2).shortValue());
/*  88 */       return;
/*     */     }
/*  90 */     if ((paramObject2 instanceof Character)) {
/*  91 */       unsafe.putDoubleVolatile(this.base, this.fieldOffset, ((Character)paramObject2).charValue());
/*  92 */       return;
/*     */     }
/*  94 */     if ((paramObject2 instanceof Integer)) {
/*  95 */       unsafe.putDoubleVolatile(this.base, this.fieldOffset, ((Integer)paramObject2).intValue());
/*  96 */       return;
/*     */     }
/*  98 */     if ((paramObject2 instanceof Long)) {
/*  99 */       unsafe.putDoubleVolatile(this.base, this.fieldOffset, ((Long)paramObject2).longValue());
/* 100 */       return;
/*     */     }
/* 102 */     if ((paramObject2 instanceof Float)) {
/* 103 */       unsafe.putDoubleVolatile(this.base, this.fieldOffset, ((Float)paramObject2).floatValue());
/* 104 */       return;
/*     */     }
/* 106 */     if ((paramObject2 instanceof Double)) {
/* 107 */       unsafe.putDoubleVolatile(this.base, this.fieldOffset, ((Double)paramObject2).doubleValue());
/* 108 */       return;
/*     */     }
/* 110 */     throwSetIllegalArgumentException(paramObject2);
/*     */   }
/*     */ 
/*     */   public void setBoolean(Object paramObject, boolean paramBoolean)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 116 */     throwSetIllegalArgumentException(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void setByte(Object paramObject, byte paramByte)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 122 */     setDouble(paramObject, paramByte);
/*     */   }
/*     */ 
/*     */   public void setChar(Object paramObject, char paramChar)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 128 */     setDouble(paramObject, paramChar);
/*     */   }
/*     */ 
/*     */   public void setShort(Object paramObject, short paramShort)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 134 */     setDouble(paramObject, paramShort);
/*     */   }
/*     */ 
/*     */   public void setInt(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 140 */     setDouble(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public void setLong(Object paramObject, long paramLong)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 146 */     setDouble(paramObject, paramLong);
/*     */   }
/*     */ 
/*     */   public void setFloat(Object paramObject, float paramFloat)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 152 */     setDouble(paramObject, paramFloat);
/*     */   }
/*     */ 
/*     */   public void setDouble(Object paramObject, double paramDouble)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 158 */     if (this.isReadOnly) {
/* 159 */       throwFinalFieldIllegalAccessException(paramDouble);
/*     */     }
/* 161 */     unsafe.putDoubleVolatile(this.base, this.fieldOffset, paramDouble);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.UnsafeQualifiedStaticDoubleFieldAccessorImpl
 * JD-Core Version:    0.6.2
 */