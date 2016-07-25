/*     */ package sun.reflect;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class UnsafeStaticDoubleFieldAccessorImpl extends UnsafeStaticFieldAccessorImpl
/*     */ {
/*     */   UnsafeStaticDoubleFieldAccessorImpl(Field paramField)
/*     */   {
/*  32 */     super(paramField);
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject) throws IllegalArgumentException {
/*  36 */     return new Double(getDouble(paramObject));
/*     */   }
/*     */ 
/*     */   public boolean getBoolean(Object paramObject) throws IllegalArgumentException {
/*  40 */     throw newGetBooleanIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public byte getByte(Object paramObject) throws IllegalArgumentException {
/*  44 */     throw newGetByteIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public char getChar(Object paramObject) throws IllegalArgumentException {
/*  48 */     throw newGetCharIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public short getShort(Object paramObject) throws IllegalArgumentException {
/*  52 */     throw newGetShortIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public int getInt(Object paramObject) throws IllegalArgumentException {
/*  56 */     throw newGetIntIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public long getLong(Object paramObject) throws IllegalArgumentException {
/*  60 */     throw newGetLongIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public float getFloat(Object paramObject) throws IllegalArgumentException {
/*  64 */     throw newGetFloatIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public double getDouble(Object paramObject) throws IllegalArgumentException {
/*  68 */     return unsafe.getDouble(this.base, this.fieldOffset);
/*     */   }
/*     */ 
/*     */   public void set(Object paramObject1, Object paramObject2)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/*  74 */     if (this.isFinal) {
/*  75 */       throwFinalFieldIllegalAccessException(paramObject2);
/*     */     }
/*  77 */     if (paramObject2 == null) {
/*  78 */       throwSetIllegalArgumentException(paramObject2);
/*     */     }
/*  80 */     if ((paramObject2 instanceof Byte)) {
/*  81 */       unsafe.putDouble(this.base, this.fieldOffset, ((Byte)paramObject2).byteValue());
/*  82 */       return;
/*     */     }
/*  84 */     if ((paramObject2 instanceof Short)) {
/*  85 */       unsafe.putDouble(this.base, this.fieldOffset, ((Short)paramObject2).shortValue());
/*  86 */       return;
/*     */     }
/*  88 */     if ((paramObject2 instanceof Character)) {
/*  89 */       unsafe.putDouble(this.base, this.fieldOffset, ((Character)paramObject2).charValue());
/*  90 */       return;
/*     */     }
/*  92 */     if ((paramObject2 instanceof Integer)) {
/*  93 */       unsafe.putDouble(this.base, this.fieldOffset, ((Integer)paramObject2).intValue());
/*  94 */       return;
/*     */     }
/*  96 */     if ((paramObject2 instanceof Long)) {
/*  97 */       unsafe.putDouble(this.base, this.fieldOffset, ((Long)paramObject2).longValue());
/*  98 */       return;
/*     */     }
/* 100 */     if ((paramObject2 instanceof Float)) {
/* 101 */       unsafe.putDouble(this.base, this.fieldOffset, ((Float)paramObject2).floatValue());
/* 102 */       return;
/*     */     }
/* 104 */     if ((paramObject2 instanceof Double)) {
/* 105 */       unsafe.putDouble(this.base, this.fieldOffset, ((Double)paramObject2).doubleValue());
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
/* 120 */     setDouble(paramObject, paramByte);
/*     */   }
/*     */ 
/*     */   public void setChar(Object paramObject, char paramChar)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 126 */     setDouble(paramObject, paramChar);
/*     */   }
/*     */ 
/*     */   public void setShort(Object paramObject, short paramShort)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 132 */     setDouble(paramObject, paramShort);
/*     */   }
/*     */ 
/*     */   public void setInt(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 138 */     setDouble(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public void setLong(Object paramObject, long paramLong)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 144 */     setDouble(paramObject, paramLong);
/*     */   }
/*     */ 
/*     */   public void setFloat(Object paramObject, float paramFloat)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 150 */     setDouble(paramObject, paramFloat);
/*     */   }
/*     */ 
/*     */   public void setDouble(Object paramObject, double paramDouble)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 156 */     if (this.isFinal) {
/* 157 */       throwFinalFieldIllegalAccessException(paramDouble);
/*     */     }
/* 159 */     unsafe.putDouble(this.base, this.fieldOffset, paramDouble);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.UnsafeStaticDoubleFieldAccessorImpl
 * JD-Core Version:    0.6.2
 */