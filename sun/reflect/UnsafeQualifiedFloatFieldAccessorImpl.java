/*     */ package sun.reflect;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class UnsafeQualifiedFloatFieldAccessorImpl extends UnsafeQualifiedFieldAccessorImpl
/*     */ {
/*     */   UnsafeQualifiedFloatFieldAccessorImpl(Field paramField, boolean paramBoolean)
/*     */   {
/*  34 */     super(paramField, paramBoolean);
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject) throws IllegalArgumentException {
/*  38 */     return new Float(getFloat(paramObject));
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
/*  66 */     ensureObj(paramObject);
/*  67 */     return unsafe.getFloatVolatile(paramObject, this.fieldOffset);
/*     */   }
/*     */ 
/*     */   public double getDouble(Object paramObject) throws IllegalArgumentException {
/*  71 */     return getFloat(paramObject);
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
/*  84 */     if ((paramObject2 instanceof Byte)) {
/*  85 */       unsafe.putFloatVolatile(paramObject1, this.fieldOffset, ((Byte)paramObject2).byteValue());
/*  86 */       return;
/*     */     }
/*  88 */     if ((paramObject2 instanceof Short)) {
/*  89 */       unsafe.putFloatVolatile(paramObject1, this.fieldOffset, ((Short)paramObject2).shortValue());
/*  90 */       return;
/*     */     }
/*  92 */     if ((paramObject2 instanceof Character)) {
/*  93 */       unsafe.putFloatVolatile(paramObject1, this.fieldOffset, ((Character)paramObject2).charValue());
/*  94 */       return;
/*     */     }
/*  96 */     if ((paramObject2 instanceof Integer)) {
/*  97 */       unsafe.putFloatVolatile(paramObject1, this.fieldOffset, ((Integer)paramObject2).intValue());
/*  98 */       return;
/*     */     }
/* 100 */     if ((paramObject2 instanceof Long)) {
/* 101 */       unsafe.putFloatVolatile(paramObject1, this.fieldOffset, (float)((Long)paramObject2).longValue());
/* 102 */       return;
/*     */     }
/* 104 */     if ((paramObject2 instanceof Float)) {
/* 105 */       unsafe.putFloatVolatile(paramObject1, this.fieldOffset, ((Float)paramObject2).floatValue());
/* 106 */       return;
/*     */     }
/* 108 */     throwSetIllegalArgumentException(paramObject2);
/*     */   }
/*     */ 
/*     */   public void setBoolean(Object paramObject, boolean paramBoolean)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 114 */     throwSetIllegalArgumentException(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void setByte(Object paramObject, byte paramByte)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 120 */     setFloat(paramObject, paramByte);
/*     */   }
/*     */ 
/*     */   public void setChar(Object paramObject, char paramChar)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 126 */     setFloat(paramObject, paramChar);
/*     */   }
/*     */ 
/*     */   public void setShort(Object paramObject, short paramShort)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 132 */     setFloat(paramObject, paramShort);
/*     */   }
/*     */ 
/*     */   public void setInt(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 138 */     setFloat(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public void setLong(Object paramObject, long paramLong)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 144 */     setFloat(paramObject, (float)paramLong);
/*     */   }
/*     */ 
/*     */   public void setFloat(Object paramObject, float paramFloat)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 150 */     ensureObj(paramObject);
/* 151 */     if (this.isReadOnly) {
/* 152 */       throwFinalFieldIllegalAccessException(paramFloat);
/*     */     }
/* 154 */     unsafe.putFloatVolatile(paramObject, this.fieldOffset, paramFloat);
/*     */   }
/*     */ 
/*     */   public void setDouble(Object paramObject, double paramDouble)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 160 */     throwSetIllegalArgumentException(paramDouble);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.UnsafeQualifiedFloatFieldAccessorImpl
 * JD-Core Version:    0.6.2
 */