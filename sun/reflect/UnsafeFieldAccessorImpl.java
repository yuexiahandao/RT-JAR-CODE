/*     */ package sun.reflect;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ abstract class UnsafeFieldAccessorImpl extends FieldAccessorImpl
/*     */ {
/*  40 */   static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */   protected final Field field;
/*     */   protected final int fieldOffset;
/*     */   protected final boolean isFinal;
/*     */ 
/*     */   UnsafeFieldAccessorImpl(Field paramField)
/*     */   {
/*  47 */     this.field = paramField;
/*  48 */     this.fieldOffset = unsafe.fieldOffset(paramField);
/*  49 */     this.isFinal = Modifier.isFinal(paramField.getModifiers());
/*     */   }
/*     */ 
/*     */   protected void ensureObj(Object paramObject)
/*     */   {
/*  54 */     if (!this.field.getDeclaringClass().isAssignableFrom(paramObject.getClass()))
/*  55 */       throwSetIllegalArgumentException(paramObject);
/*     */   }
/*     */ 
/*     */   private String getQualifiedFieldName()
/*     */   {
/*  60 */     return this.field.getDeclaringClass().getName() + "." + this.field.getName();
/*     */   }
/*     */ 
/*     */   protected IllegalArgumentException newGetIllegalArgumentException(String paramString) {
/*  64 */     return new IllegalArgumentException("Attempt to get " + this.field.getType().getName() + " field \"" + getQualifiedFieldName() + "\" with illegal data type conversion to " + paramString);
/*     */   }
/*     */ 
/*     */   protected void throwFinalFieldIllegalAccessException(String paramString1, String paramString2)
/*     */     throws IllegalAccessException
/*     */   {
/*  73 */     throw new IllegalAccessException(getSetMessage(paramString1, paramString2));
/*     */   }
/*     */ 
/*     */   protected void throwFinalFieldIllegalAccessException(Object paramObject) throws IllegalAccessException {
/*  77 */     throwFinalFieldIllegalAccessException(paramObject != null ? paramObject.getClass().getName() : "", "");
/*     */   }
/*     */ 
/*     */   protected void throwFinalFieldIllegalAccessException(boolean paramBoolean) throws IllegalAccessException {
/*  81 */     throwFinalFieldIllegalAccessException("boolean", Boolean.toString(paramBoolean));
/*     */   }
/*     */ 
/*     */   protected void throwFinalFieldIllegalAccessException(char paramChar) throws IllegalAccessException {
/*  85 */     throwFinalFieldIllegalAccessException("char", Character.toString(paramChar));
/*     */   }
/*     */ 
/*     */   protected void throwFinalFieldIllegalAccessException(byte paramByte) throws IllegalAccessException {
/*  89 */     throwFinalFieldIllegalAccessException("byte", Byte.toString(paramByte));
/*     */   }
/*     */ 
/*     */   protected void throwFinalFieldIllegalAccessException(short paramShort) throws IllegalAccessException {
/*  93 */     throwFinalFieldIllegalAccessException("short", Short.toString(paramShort));
/*     */   }
/*     */ 
/*     */   protected void throwFinalFieldIllegalAccessException(int paramInt) throws IllegalAccessException {
/*  97 */     throwFinalFieldIllegalAccessException("int", Integer.toString(paramInt));
/*     */   }
/*     */ 
/*     */   protected void throwFinalFieldIllegalAccessException(long paramLong) throws IllegalAccessException {
/* 101 */     throwFinalFieldIllegalAccessException("long", Long.toString(paramLong));
/*     */   }
/*     */ 
/*     */   protected void throwFinalFieldIllegalAccessException(float paramFloat) throws IllegalAccessException {
/* 105 */     throwFinalFieldIllegalAccessException("float", Float.toString(paramFloat));
/*     */   }
/*     */ 
/*     */   protected void throwFinalFieldIllegalAccessException(double paramDouble) throws IllegalAccessException {
/* 109 */     throwFinalFieldIllegalAccessException("double", Double.toString(paramDouble));
/*     */   }
/*     */ 
/*     */   protected IllegalArgumentException newGetBooleanIllegalArgumentException() {
/* 113 */     return newGetIllegalArgumentException("boolean");
/*     */   }
/*     */ 
/*     */   protected IllegalArgumentException newGetByteIllegalArgumentException() {
/* 117 */     return newGetIllegalArgumentException("byte");
/*     */   }
/*     */ 
/*     */   protected IllegalArgumentException newGetCharIllegalArgumentException() {
/* 121 */     return newGetIllegalArgumentException("char");
/*     */   }
/*     */ 
/*     */   protected IllegalArgumentException newGetShortIllegalArgumentException() {
/* 125 */     return newGetIllegalArgumentException("short");
/*     */   }
/*     */ 
/*     */   protected IllegalArgumentException newGetIntIllegalArgumentException() {
/* 129 */     return newGetIllegalArgumentException("int");
/*     */   }
/*     */ 
/*     */   protected IllegalArgumentException newGetLongIllegalArgumentException() {
/* 133 */     return newGetIllegalArgumentException("long");
/*     */   }
/*     */ 
/*     */   protected IllegalArgumentException newGetFloatIllegalArgumentException() {
/* 137 */     return newGetIllegalArgumentException("float");
/*     */   }
/*     */ 
/*     */   protected IllegalArgumentException newGetDoubleIllegalArgumentException() {
/* 141 */     return newGetIllegalArgumentException("double");
/*     */   }
/*     */ 
/*     */   protected String getSetMessage(String paramString1, String paramString2) {
/* 145 */     String str = "Can not set";
/* 146 */     if (Modifier.isStatic(this.field.getModifiers()))
/* 147 */       str = str + " static";
/* 148 */     if (this.isFinal)
/* 149 */       str = str + " final";
/* 150 */     str = str + " " + this.field.getType().getName() + " field " + getQualifiedFieldName() + " to ";
/* 151 */     if (paramString2.length() > 0) {
/* 152 */       str = str + "(" + paramString1 + ")" + paramString2;
/*     */     }
/* 154 */     else if (paramString1.length() > 0)
/* 155 */       str = str + paramString1;
/*     */     else {
/* 157 */       str = str + "null value";
/*     */     }
/* 159 */     return str;
/*     */   }
/*     */ 
/*     */   protected void throwSetIllegalArgumentException(String paramString1, String paramString2)
/*     */   {
/* 164 */     throw new IllegalArgumentException(getSetMessage(paramString1, paramString2));
/*     */   }
/*     */ 
/*     */   protected void throwSetIllegalArgumentException(Object paramObject) {
/* 168 */     throwSetIllegalArgumentException(paramObject != null ? paramObject.getClass().getName() : "", "");
/*     */   }
/*     */ 
/*     */   protected void throwSetIllegalArgumentException(boolean paramBoolean) {
/* 172 */     throwSetIllegalArgumentException("boolean", Boolean.toString(paramBoolean));
/*     */   }
/*     */ 
/*     */   protected void throwSetIllegalArgumentException(byte paramByte) {
/* 176 */     throwSetIllegalArgumentException("byte", Byte.toString(paramByte));
/*     */   }
/*     */ 
/*     */   protected void throwSetIllegalArgumentException(char paramChar) {
/* 180 */     throwSetIllegalArgumentException("char", Character.toString(paramChar));
/*     */   }
/*     */ 
/*     */   protected void throwSetIllegalArgumentException(short paramShort) {
/* 184 */     throwSetIllegalArgumentException("short", Short.toString(paramShort));
/*     */   }
/*     */ 
/*     */   protected void throwSetIllegalArgumentException(int paramInt) {
/* 188 */     throwSetIllegalArgumentException("int", Integer.toString(paramInt));
/*     */   }
/*     */ 
/*     */   protected void throwSetIllegalArgumentException(long paramLong) {
/* 192 */     throwSetIllegalArgumentException("long", Long.toString(paramLong));
/*     */   }
/*     */ 
/*     */   protected void throwSetIllegalArgumentException(float paramFloat) {
/* 196 */     throwSetIllegalArgumentException("float", Float.toString(paramFloat));
/*     */   }
/*     */ 
/*     */   protected void throwSetIllegalArgumentException(double paramDouble) {
/* 200 */     throwSetIllegalArgumentException("double", Double.toString(paramDouble));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.UnsafeFieldAccessorImpl
 * JD-Core Version:    0.6.2
 */