/*     */ package sun.reflect;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class UnsafeLongFieldAccessorImpl extends UnsafeFieldAccessorImpl
/*     */ {
/*     */   UnsafeLongFieldAccessorImpl(Field paramField)
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
/*  60 */     ensureObj(paramObject);
/*  61 */     return unsafe.getLong(paramObject, this.fieldOffset);
/*     */   }
/*     */ 
/*     */   public float getFloat(Object paramObject) throws IllegalArgumentException {
/*  65 */     return (float)getLong(paramObject);
/*     */   }
/*     */ 
/*     */   public double getDouble(Object paramObject) throws IllegalArgumentException {
/*  69 */     return getLong(paramObject);
/*     */   }
/*     */ 
/*     */   public void set(Object paramObject1, Object paramObject2)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/*  75 */     ensureObj(paramObject1);
/*  76 */     if (this.isFinal) {
/*  77 */       throwFinalFieldIllegalAccessException(paramObject2);
/*     */     }
/*  79 */     if (paramObject2 == null) {
/*  80 */       throwSetIllegalArgumentException(paramObject2);
/*     */     }
/*  82 */     if ((paramObject2 instanceof Byte)) {
/*  83 */       unsafe.putLong(paramObject1, this.fieldOffset, ((Byte)paramObject2).byteValue());
/*  84 */       return;
/*     */     }
/*  86 */     if ((paramObject2 instanceof Short)) {
/*  87 */       unsafe.putLong(paramObject1, this.fieldOffset, ((Short)paramObject2).shortValue());
/*  88 */       return;
/*     */     }
/*  90 */     if ((paramObject2 instanceof Character)) {
/*  91 */       unsafe.putLong(paramObject1, this.fieldOffset, ((Character)paramObject2).charValue());
/*  92 */       return;
/*     */     }
/*  94 */     if ((paramObject2 instanceof Integer)) {
/*  95 */       unsafe.putLong(paramObject1, this.fieldOffset, ((Integer)paramObject2).intValue());
/*  96 */       return;
/*     */     }
/*  98 */     if ((paramObject2 instanceof Long)) {
/*  99 */       unsafe.putLong(paramObject1, this.fieldOffset, ((Long)paramObject2).longValue());
/* 100 */       return;
/*     */     }
/* 102 */     throwSetIllegalArgumentException(paramObject2);
/*     */   }
/*     */ 
/*     */   public void setBoolean(Object paramObject, boolean paramBoolean)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 108 */     throwSetIllegalArgumentException(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void setByte(Object paramObject, byte paramByte)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 114 */     setLong(paramObject, paramByte);
/*     */   }
/*     */ 
/*     */   public void setChar(Object paramObject, char paramChar)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 120 */     setLong(paramObject, paramChar);
/*     */   }
/*     */ 
/*     */   public void setShort(Object paramObject, short paramShort)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 126 */     setLong(paramObject, paramShort);
/*     */   }
/*     */ 
/*     */   public void setInt(Object paramObject, int paramInt)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 132 */     setLong(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public void setLong(Object paramObject, long paramLong)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 138 */     ensureObj(paramObject);
/* 139 */     if (this.isFinal) {
/* 140 */       throwFinalFieldIllegalAccessException(paramLong);
/*     */     }
/* 142 */     unsafe.putLong(paramObject, this.fieldOffset, paramLong);
/*     */   }
/*     */ 
/*     */   public void setFloat(Object paramObject, float paramFloat)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 148 */     throwSetIllegalArgumentException(paramFloat);
/*     */   }
/*     */ 
/*     */   public void setDouble(Object paramObject, double paramDouble)
/*     */     throws IllegalArgumentException, IllegalAccessException
/*     */   {
/* 154 */     throwSetIllegalArgumentException(paramDouble);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.UnsafeLongFieldAccessorImpl
 * JD-Core Version:    0.6.2
 */