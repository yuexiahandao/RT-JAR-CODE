/*     */ package sun.reflect;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class UnsafeStaticIntegerFieldAccessorImpl extends UnsafeStaticFieldAccessorImpl
/*     */ {
/*     */   UnsafeStaticIntegerFieldAccessorImpl(Field paramField)
/*     */   {
/*  32 */     super(paramField);
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject) throws IllegalArgumentException {
/*  36 */     return new Integer(getInt(paramObject));
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
/*  56 */     return unsafe.getInt(this.base, this.fieldOffset);
/*     */   }
/*     */ 
/*     */   public long getLong(Object paramObject) throws IllegalArgumentException {
/*  60 */     return getInt(paramObject);
/*     */   }
/*     */ 
/*     */   public float getFloat(Object paramObject) throws IllegalArgumentException {
/*  64 */     return getInt(paramObject);
/*     */   }
/*     */ 
/*     */   public double getDouble(Object paramObject) throws IllegalArgumentException {
/*  68 */     return getInt(paramObject);
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
/*  81 */       unsafe.putInt(this.base, this.fieldOffset, ((Byte)paramObject2).byteValue());
/*  82 */       return;
/*     */     }
/*  84 */     if ((paramObject2 instanceof Short)) {
/*  85 */       unsafe.putInt(this.base, this.fieldOffset, ((Short)paramObject2).shortValue());
/*  86 */       return;
/*     */     }
/*  88 */     if ((paramObject2 instanceof Character)) {
/*  89 */       unsafe.putInt(this.base, this.fieldOffset, ((Character)paramObject2).charValue());
/*  90 */       return;
/*     */     }
/*  92 */     if ((paramObject2 instanceof Integer)) {
/*  93 */       unsafe.putInt(this.base, this.fieldOffset, ((Integer)paramObject2).intValue());
/*  94 */       return;
/*     */     }
/*  96 */     throwSetIllegalArgumentException(paramObject2);
/*     */   }
/*     */ 
/*     */   public void setBoolean(Object paramObject, boolean paramBoolean)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 102 */     throwSetIllegalArgumentException(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void setByte(Object paramObject, byte paramByte)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 108 */     setInt(paramObject, paramByte);
/*     */   }
/*     */ 
/*     */   public void setChar(Object paramObject, char paramChar)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 114 */     setInt(paramObject, paramChar);
/*     */   }
/*     */ 
/*     */   public void setShort(Object paramObject, short paramShort)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 120 */     setInt(paramObject, paramShort);
/*     */   }
/*     */ 
/*     */   public void setInt(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 126 */     if (this.isFinal) {
/* 127 */       throwFinalFieldIllegalAccessException(paramInt);
/*     */     }
/* 129 */     unsafe.putInt(this.base, this.fieldOffset, paramInt);
/*     */   }
/*     */ 
/*     */   public void setLong(Object paramObject, long paramLong)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 135 */     throwSetIllegalArgumentException(paramLong);
/*     */   }
/*     */ 
/*     */   public void setFloat(Object paramObject, float paramFloat)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 141 */     throwSetIllegalArgumentException(paramFloat);
/*     */   }
/*     */ 
/*     */   public void setDouble(Object paramObject, double paramDouble)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 147 */     throwSetIllegalArgumentException(paramDouble);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.UnsafeStaticIntegerFieldAccessorImpl
 * JD-Core Version:    0.6.2
 */