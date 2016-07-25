/*     */ package sun.reflect;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class UnsafeQualifiedCharacterFieldAccessorImpl extends UnsafeQualifiedFieldAccessorImpl
/*     */ {
/*     */   UnsafeQualifiedCharacterFieldAccessorImpl(Field paramField, boolean paramBoolean)
/*     */   {
/*  34 */     super(paramField, paramBoolean);
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject) throws IllegalArgumentException {
/*  38 */     return new Character(getChar(paramObject));
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
/*  50 */     ensureObj(paramObject);
/*  51 */     return unsafe.getCharVolatile(paramObject, this.fieldOffset);
/*     */   }
/*     */ 
/*     */   public short getShort(Object paramObject) throws IllegalArgumentException {
/*  55 */     throw newGetShortIllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public int getInt(Object paramObject) throws IllegalArgumentException {
/*  59 */     return getChar(paramObject);
/*     */   }
/*     */ 
/*     */   public long getLong(Object paramObject) throws IllegalArgumentException {
/*  63 */     return getChar(paramObject);
/*     */   }
/*     */ 
/*     */   public float getFloat(Object paramObject) throws IllegalArgumentException {
/*  67 */     return getChar(paramObject);
/*     */   }
/*     */ 
/*     */   public double getDouble(Object paramObject) throws IllegalArgumentException {
/*  71 */     return getChar(paramObject);
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
/*  84 */     if ((paramObject2 instanceof Character)) {
/*  85 */       unsafe.putCharVolatile(paramObject1, this.fieldOffset, ((Character)paramObject2).charValue());
/*  86 */       return;
/*     */     }
/*  88 */     throwSetIllegalArgumentException(paramObject2);
/*     */   }
/*     */ 
/*     */   public void setBoolean(Object paramObject, boolean paramBoolean)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/*  94 */     throwSetIllegalArgumentException(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void setByte(Object paramObject, byte paramByte)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 100 */     throwSetIllegalArgumentException(paramByte);
/*     */   }
/*     */ 
/*     */   public void setChar(Object paramObject, char paramChar)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 106 */     ensureObj(paramObject);
/* 107 */     if (this.isReadOnly) {
/* 108 */       throwFinalFieldIllegalAccessException(paramChar);
/*     */     }
/* 110 */     unsafe.putCharVolatile(paramObject, this.fieldOffset, paramChar);
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
 * Qualified Name:     sun.reflect.UnsafeQualifiedCharacterFieldAccessorImpl
 * JD-Core Version:    0.6.2
 */