/*     */ package sun.reflect;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class UnsafeStaticLongFieldAccessorImpl extends UnsafeStaticFieldAccessorImpl
/*     */ {
/*     */   UnsafeStaticLongFieldAccessorImpl(Field paramField)
/*     */   {
/*  32 */     super(paramField);
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject) throws IllegalArgumentException {
/*  36 */     return new Long(getLong(paramObject));
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
/*  60 */     return unsafe.getLong(this.base, this.fieldOffset);
/*     */   }
/*     */ 
/*     */   public float getFloat(Object paramObject) throws IllegalArgumentException {
/*  64 */     return (float)getLong(paramObject);
/*     */   }
/*     */ 
/*     */   public double getDouble(Object paramObject) throws IllegalArgumentException {
/*  68 */     return getLong(paramObject);
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
/*  81 */       unsafe.putLong(this.base, this.fieldOffset, ((Byte)paramObject2).byteValue());
/*  82 */       return;
/*     */     }
/*  84 */     if ((paramObject2 instanceof Short)) {
/*  85 */       unsafe.putLong(this.base, this.fieldOffset, ((Short)paramObject2).shortValue());
/*  86 */       return;
/*     */     }
/*  88 */     if ((paramObject2 instanceof Character)) {
/*  89 */       unsafe.putLong(this.base, this.fieldOffset, ((Character)paramObject2).charValue());
/*  90 */       return;
/*     */     }
/*  92 */     if ((paramObject2 instanceof Integer)) {
/*  93 */       unsafe.putLong(this.base, this.fieldOffset, ((Integer)paramObject2).intValue());
/*  94 */       return;
/*     */     }
/*  96 */     if ((paramObject2 instanceof Long)) {
/*  97 */       unsafe.putLong(this.base, this.fieldOffset, ((Long)paramObject2).longValue());
/*  98 */       return;
/*     */     }
/* 100 */     throwSetIllegalArgumentException(paramObject2);
/*     */   }
/*     */ 
/*     */   public void setBoolean(Object paramObject, boolean paramBoolean)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 106 */     throwSetIllegalArgumentException(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void setByte(Object paramObject, byte paramByte)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 112 */     setLong(paramObject, paramByte);
/*     */   }
/*     */ 
/*     */   public void setChar(Object paramObject, char paramChar)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 118 */     setLong(paramObject, paramChar);
/*     */   }
/*     */ 
/*     */   public void setShort(Object paramObject, short paramShort)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 124 */     setLong(paramObject, paramShort);
/*     */   }
/*     */ 
/*     */   public void setInt(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 130 */     setLong(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public void setLong(Object paramObject, long paramLong)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 136 */     if (this.isFinal) {
/* 137 */       throwFinalFieldIllegalAccessException(paramLong);
/*     */     }
/* 139 */     unsafe.putLong(this.base, this.fieldOffset, paramLong);
/*     */   }
/*     */ 
/*     */   public void setFloat(Object paramObject, float paramFloat)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 145 */     throwSetIllegalArgumentException(paramFloat);
/*     */   }
/*     */ 
/*     */   public void setDouble(Object paramObject, double paramDouble)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 151 */     throwSetIllegalArgumentException(paramDouble);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.UnsafeStaticLongFieldAccessorImpl
 * JD-Core Version:    0.6.2
 */