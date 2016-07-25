/*     */ package sun.reflect;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class UnsafeQualifiedLongFieldAccessorImpl extends UnsafeQualifiedFieldAccessorImpl
/*     */ {
/*     */   UnsafeQualifiedLongFieldAccessorImpl(Field paramField, boolean paramBoolean)
/*     */   {
/*  34 */     super(paramField, paramBoolean);
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject) throws IllegalArgumentException {
/*  38 */     return new Long(getLong(paramObject));
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
/*  62 */     ensureObj(paramObject);
/*  63 */     return unsafe.getLongVolatile(paramObject, this.fieldOffset);
/*     */   }
/*     */ 
/*     */   public float getFloat(Object paramObject) throws IllegalArgumentException {
/*  67 */     return (float)getLong(paramObject);
/*     */   }
/*     */ 
/*     */   public double getDouble(Object paramObject) throws IllegalArgumentException {
/*  71 */     return getLong(paramObject);
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
/*  85 */       unsafe.putLongVolatile(paramObject1, this.fieldOffset, ((Byte)paramObject2).byteValue());
/*  86 */       return;
/*     */     }
/*  88 */     if ((paramObject2 instanceof Short)) {
/*  89 */       unsafe.putLongVolatile(paramObject1, this.fieldOffset, ((Short)paramObject2).shortValue());
/*  90 */       return;
/*     */     }
/*  92 */     if ((paramObject2 instanceof Character)) {
/*  93 */       unsafe.putLongVolatile(paramObject1, this.fieldOffset, ((Character)paramObject2).charValue());
/*  94 */       return;
/*     */     }
/*  96 */     if ((paramObject2 instanceof Integer)) {
/*  97 */       unsafe.putLongVolatile(paramObject1, this.fieldOffset, ((Integer)paramObject2).intValue());
/*  98 */       return;
/*     */     }
/* 100 */     if ((paramObject2 instanceof Long)) {
/* 101 */       unsafe.putLongVolatile(paramObject1, this.fieldOffset, ((Long)paramObject2).longValue());
/* 102 */       return;
/*     */     }
/* 104 */     throwSetIllegalArgumentException(paramObject2);
/*     */   }
/*     */ 
/*     */   public void setBoolean(Object paramObject, boolean paramBoolean)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 110 */     throwSetIllegalArgumentException(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void setByte(Object paramObject, byte paramByte)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 116 */     setLong(paramObject, paramByte);
/*     */   }
/*     */ 
/*     */   public void setChar(Object paramObject, char paramChar)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 122 */     setLong(paramObject, paramChar);
/*     */   }
/*     */ 
/*     */   public void setShort(Object paramObject, short paramShort)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 128 */     setLong(paramObject, paramShort);
/*     */   }
/*     */ 
/*     */   public void setInt(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 134 */     setLong(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public void setLong(Object paramObject, long paramLong)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 140 */     ensureObj(paramObject);
/* 141 */     if (this.isReadOnly) {
/* 142 */       throwFinalFieldIllegalAccessException(paramLong);
/*     */     }
/* 144 */     unsafe.putLongVolatile(paramObject, this.fieldOffset, paramLong);
/*     */   }
/*     */ 
/*     */   public void setFloat(Object paramObject, float paramFloat)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 150 */     throwSetIllegalArgumentException(paramFloat);
/*     */   }
/*     */ 
/*     */   public void setDouble(Object paramObject, double paramDouble)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 156 */     throwSetIllegalArgumentException(paramDouble);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.UnsafeQualifiedLongFieldAccessorImpl
 * JD-Core Version:    0.6.2
 */