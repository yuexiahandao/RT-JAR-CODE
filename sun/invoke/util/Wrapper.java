/*     */ package sun.invoke.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public enum Wrapper
/*     */ {
/*     */   private final Class<?> wrapperType;
/*     */   private final Class<?> primitiveType;
/*     */   private final char basicTypeChar;
/*     */   private final Object zero;
/*     */   private final Object emptyArray;
/*     */   private final int format;
/*     */   private final String wrapperSimpleName;
/*     */   private final String primitiveSimpleName;
/*     */   private static final Wrapper[] FROM_PRIM;
/*     */   private static final Wrapper[] FROM_WRAP;
/*     */   private static final Wrapper[] FROM_CHAR;
/*     */ 
/*     */   private Wrapper(Class<?> paramClass1, Class<?> paramClass2, char paramChar, Object paramObject1, Object paramObject2, int paramInt)
/*     */   {
/*  54 */     this.wrapperType = paramClass1;
/*  55 */     this.primitiveType = paramClass2;
/*  56 */     this.basicTypeChar = paramChar;
/*  57 */     this.zero = paramObject1;
/*  58 */     this.emptyArray = paramObject2;
/*  59 */     this.format = paramInt;
/*  60 */     this.wrapperSimpleName = paramClass1.getSimpleName();
/*  61 */     this.primitiveSimpleName = paramClass2.getSimpleName();
/*     */   }
/*     */ 
/*     */   public String detailString()
/*     */   {
/*  66 */     return this.wrapperSimpleName + Arrays.asList(new Object[] { this.wrapperType, this.primitiveType, Character.valueOf(this.basicTypeChar), this.zero, "0x" + Integer.toHexString(this.format) });
/*     */   }
/*     */ 
/*     */   public int bitWidth()
/*     */   {
/* 110 */     return this.format >> 2 & 0x3FF;
/*     */   }
/* 112 */   public int stackSlots() { return this.format >> 0 & 0x3; } 
/*     */   public boolean isSingleWord() {
/* 114 */     return (this.format & 0x1) != 0;
/*     */   }
/* 116 */   public boolean isDoubleWord() { return (this.format & 0x2) != 0; } 
/*     */   public boolean isNumeric() {
/* 118 */     return (this.format & 0xFFFFFFFC) != 0;
/*     */   }
/* 120 */   public boolean isIntegral() { return (isNumeric()) && (this.format < 4225); } 
/*     */   public boolean isSubwordOrInt() {
/* 122 */     return (isIntegral()) && (isSingleWord());
/*     */   }
/* 124 */   public boolean isSigned() { return this.format < 0; } 
/*     */   public boolean isUnsigned() {
/* 126 */     return (this.format >= 5) && (this.format < 4225);
/*     */   }
/* 128 */   public boolean isFloating() { return this.format >= 4225; } 
/*     */   public boolean isOther() {
/* 130 */     return (this.format & 0xFFFFFFFC) == 0;
/*     */   }
/*     */ 
/*     */   public boolean isConvertibleFrom(Wrapper paramWrapper)
/*     */   {
/* 143 */     if (this == paramWrapper) return true;
/* 144 */     if (compareTo(paramWrapper) < 0)
/*     */     {
/* 146 */       return false;
/*     */     }
/*     */ 
/* 150 */     int i = (this.format & paramWrapper.format & 0xFFFFF000) != 0 ? 1 : 0;
/* 151 */     if (i == 0) {
/* 152 */       if (isOther()) return true;
/*     */ 
/* 154 */       if (paramWrapper.format == 65) return true;
/*     */ 
/* 156 */       return false;
/*     */     }
/*     */ 
/* 159 */     assert ((isFloating()) || (isSigned()));
/* 160 */     assert ((paramWrapper.isFloating()) || (paramWrapper.isSigned()));
/* 161 */     return true;
/*     */   }
/*     */ 
/*     */   private static boolean checkConvertibleFrom()
/*     */   {
/* 167 */     for (Wrapper localWrapper1 : values()) {
/* 168 */       assert (localWrapper1.isConvertibleFrom(localWrapper1));
/* 169 */       assert (VOID.isConvertibleFrom(localWrapper1));
/* 170 */       if (localWrapper1 != VOID) {
/* 171 */         assert (OBJECT.isConvertibleFrom(localWrapper1));
/* 172 */         assert (!localWrapper1.isConvertibleFrom(VOID));
/*     */       }
/*     */ 
/* 175 */       if (localWrapper1 != CHAR) {
/* 176 */         assert (!CHAR.isConvertibleFrom(localWrapper1));
/* 177 */         if ((!localWrapper1.isConvertibleFrom(INT)) && 
/* 178 */           (!$assertionsDisabled) && (localWrapper1.isConvertibleFrom(CHAR))) throw new AssertionError();
/*     */       }
/* 180 */       if (localWrapper1 != BOOLEAN) {
/* 181 */         assert (!BOOLEAN.isConvertibleFrom(localWrapper1));
/* 182 */         if ((localWrapper1 != VOID) && (localWrapper1 != OBJECT) && 
/* 183 */           (!$assertionsDisabled) && (localWrapper1.isConvertibleFrom(BOOLEAN))) throw new AssertionError();
/*     */       }
/*     */       Wrapper localWrapper2;
/* 186 */       if (localWrapper1.isSigned()) {
/* 187 */         for (localWrapper2 : values()) {
/* 188 */           if (localWrapper1 != localWrapper2) {
/* 189 */             if (localWrapper2.isFloating()) {
/* 190 */               if ((!$assertionsDisabled) && (localWrapper1.isConvertibleFrom(localWrapper2))) throw new AssertionError(); 
/*     */             }
/* 191 */             else if (localWrapper2.isSigned()) {
/* 192 */               if (localWrapper1.compareTo(localWrapper2) < 0) {
/* 193 */                 if ((!$assertionsDisabled) && (localWrapper1.isConvertibleFrom(localWrapper2))) throw new AssertionError(); 
/*     */               }
/*     */               else
/* 195 */                 assert (localWrapper1.isConvertibleFrom(localWrapper2));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 200 */       if (localWrapper1.isFloating()) {
/* 201 */         for (localWrapper2 : values()) {
/* 202 */           if (localWrapper1 != localWrapper2) {
/* 203 */             if (localWrapper2.isSigned()) {
/* 204 */               if ((!$assertionsDisabled) && (!localWrapper1.isConvertibleFrom(localWrapper2))) throw new AssertionError(); 
/*     */             }
/* 205 */             else if (localWrapper2.isFloating())
/* 206 */               if (localWrapper1.compareTo(localWrapper2) < 0) {
/* 207 */                 if ((!$assertionsDisabled) && (localWrapper1.isConvertibleFrom(localWrapper2))) throw new AssertionError(); 
/*     */               }
/*     */               else
/* 209 */                 assert (localWrapper1.isConvertibleFrom(localWrapper2));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 214 */     return true;
/*     */   }
/*     */ 
/*     */   public Object zero()
/*     */   {
/* 225 */     return this.zero;
/*     */   }
/*     */ 
/*     */   public <T> T zero(Class<T> paramClass)
/*     */   {
/* 231 */     return convert(this.zero, paramClass);
/*     */   }
/*     */ 
/*     */   public static Wrapper forPrimitiveType(Class<?> paramClass)
/*     */   {
/* 247 */     Wrapper localWrapper = findPrimitiveType(paramClass);
/* 248 */     if (localWrapper != null) return localWrapper;
/* 249 */     if (paramClass.isPrimitive())
/* 250 */       throw new InternalError();
/* 251 */     throw newIllegalArgumentException("not primitive: " + paramClass);
/*     */   }
/*     */ 
/*     */   static Wrapper findPrimitiveType(Class<?> paramClass) {
/* 255 */     Wrapper localWrapper = FROM_PRIM[hashPrim(paramClass)];
/* 256 */     if ((localWrapper != null) && (localWrapper.primitiveType == paramClass)) {
/* 257 */       return localWrapper;
/*     */     }
/* 259 */     return null;
/*     */   }
/*     */ 
/*     */   public static Wrapper forWrapperType(Class<?> paramClass)
/*     */   {
/* 269 */     Wrapper localWrapper1 = findWrapperType(paramClass);
/* 270 */     if (localWrapper1 != null) return localWrapper1;
/* 271 */     for (Wrapper localWrapper2 : values())
/* 272 */       if (localWrapper2.wrapperType == paramClass)
/* 273 */         throw new InternalError();
/* 274 */     throw newIllegalArgumentException("not wrapper: " + paramClass);
/*     */   }
/*     */ 
/*     */   static Wrapper findWrapperType(Class<?> paramClass) {
/* 278 */     Wrapper localWrapper = FROM_WRAP[hashWrap(paramClass)];
/* 279 */     if ((localWrapper != null) && (localWrapper.wrapperType == paramClass)) {
/* 280 */       return localWrapper;
/*     */     }
/* 282 */     return null;
/*     */   }
/*     */ 
/*     */   public static Wrapper forBasicType(char paramChar)
/*     */   {
/* 290 */     Wrapper localWrapper1 = FROM_CHAR[hashChar(paramChar)];
/* 291 */     if ((localWrapper1 != null) && (localWrapper1.basicTypeChar == paramChar)) {
/* 292 */       return localWrapper1;
/*     */     }
/* 294 */     for (Wrapper localWrapper2 : values())
/* 295 */       if (localWrapper1.basicTypeChar == paramChar)
/* 296 */         throw new InternalError();
/* 297 */     throw newIllegalArgumentException("not basic type char: " + paramChar);
/*     */   }
/*     */ 
/*     */   public static Wrapper forBasicType(Class<?> paramClass)
/*     */   {
/* 304 */     if (paramClass.isPrimitive())
/* 305 */       return forPrimitiveType(paramClass);
/* 306 */     return OBJECT;
/*     */   }
/*     */ 
/*     */   private static int hashPrim(Class<?> paramClass)
/*     */   {
/* 319 */     String str = paramClass.getName();
/* 320 */     if (str.length() < 3) return 0;
/* 321 */     return (str.charAt(0) + str.charAt(2)) % 16;
/*     */   }
/*     */   private static int hashWrap(Class<?> paramClass) {
/* 324 */     String str = paramClass.getName();
/* 325 */     assert (10 == "java.lang.".length());
/* 326 */     if (str.length() < 13) return 0;
/* 327 */     return ('\003' * str.charAt(11) + str.charAt(12)) % 16;
/*     */   }
/*     */   private static int hashChar(char paramChar) {
/* 330 */     return (paramChar + (paramChar >> '\001')) % 16;
/*     */   }
/*     */ 
/*     */   public Class<?> primitiveType()
/*     */   {
/* 348 */     return this.primitiveType;
/*     */   }
/*     */   public Class<?> wrapperType() {
/* 351 */     return this.wrapperType;
/*     */   }
/*     */ 
/*     */   public <T> Class<T> wrapperType(Class<T> paramClass)
/*     */   {
/* 361 */     if (paramClass == this.wrapperType)
/* 362 */       return paramClass;
/* 363 */     if ((paramClass == this.primitiveType) || (this.wrapperType == Object.class) || (paramClass.isInterface()))
/*     */     {
/* 366 */       return forceType(this.wrapperType, paramClass);
/*     */     }
/* 368 */     throw newClassCastException(paramClass, this.primitiveType);
/*     */   }
/*     */ 
/*     */   private static ClassCastException newClassCastException(Class<?> paramClass1, Class<?> paramClass2) {
/* 372 */     return new ClassCastException(paramClass1 + " is not compatible with " + paramClass2);
/*     */   }
/*     */ 
/*     */   public static <T> Class<T> asWrapperType(Class<T> paramClass)
/*     */   {
/* 379 */     if (paramClass.isPrimitive()) {
/* 380 */       return forPrimitiveType(paramClass).wrapperType(paramClass);
/*     */     }
/* 382 */     return paramClass;
/*     */   }
/*     */ 
/*     */   public static <T> Class<T> asPrimitiveType(Class<T> paramClass)
/*     */   {
/* 389 */     Wrapper localWrapper = findWrapperType(paramClass);
/* 390 */     if (localWrapper != null) {
/* 391 */       return forceType(localWrapper.primitiveType(), paramClass);
/*     */     }
/* 393 */     return paramClass;
/*     */   }
/*     */ 
/*     */   public static boolean isWrapperType(Class<?> paramClass)
/*     */   {
/* 398 */     return findWrapperType(paramClass) != null;
/*     */   }
/*     */ 
/*     */   public static boolean isPrimitiveType(Class<?> paramClass)
/*     */   {
/* 403 */     return paramClass.isPrimitive();
/*     */   }
/*     */ 
/*     */   public static char basicTypeChar(Class<?> paramClass)
/*     */   {
/* 410 */     if (!paramClass.isPrimitive()) {
/* 411 */       return 'L';
/*     */     }
/* 413 */     return forPrimitiveType(paramClass).basicTypeChar();
/*     */   }
/*     */ 
/*     */   public char basicTypeChar()
/*     */   {
/* 419 */     return this.basicTypeChar;
/*     */   }
/*     */ 
/*     */   public String wrapperSimpleName() {
/* 423 */     return this.wrapperSimpleName;
/*     */   }
/*     */ 
/*     */   public String primitiveSimpleName() {
/* 427 */     return this.primitiveSimpleName;
/*     */   }
/*     */ 
/*     */   public <T> T cast(Object paramObject, Class<T> paramClass)
/*     */   {
/* 447 */     return convert(paramObject, paramClass, true);
/*     */   }
/*     */ 
/*     */   public <T> T convert(Object paramObject, Class<T> paramClass)
/*     */   {
/* 456 */     return convert(paramObject, paramClass, false);
/*     */   }
/*     */ 
/*     */   private <T> T convert(Object paramObject, Class<T> paramClass, boolean paramBoolean) {
/* 460 */     if (this == OBJECT)
/*     */     {
/* 465 */       assert (!paramClass.isPrimitive());
/* 466 */       if (!paramClass.isInterface()) {
/* 467 */         paramClass.cast(paramObject);
/*     */       }
/* 469 */       localObject1 = paramObject;
/* 470 */       return localObject1;
/*     */     }
/* 472 */     Object localObject1 = wrapperType(paramClass);
/* 473 */     if (((Class)localObject1).isInstance(paramObject)) {
/* 474 */       return ((Class)localObject1).cast(paramObject);
/*     */     }
/* 476 */     if (!paramBoolean) {
/* 477 */       localObject2 = paramObject.getClass();
/* 478 */       Wrapper localWrapper = findWrapperType((Class)localObject2);
/* 479 */       if ((localWrapper == null) || (!isConvertibleFrom(localWrapper)))
/* 480 */         throw newClassCastException((Class)localObject1, (Class)localObject2);
/*     */     }
/* 482 */     else if (paramObject == null)
/*     */     {
/* 484 */       localObject2 = this.zero;
/* 485 */       return localObject2;
/*     */     }
/*     */ 
/* 488 */     Object localObject2 = wrap(paramObject);
/* 489 */     if (!$assertionsDisabled) if ((localObject2 == null ? Void.class : localObject2.getClass()) != localObject1) throw new AssertionError();
/* 490 */     return localObject2;
/*     */   }
/*     */ 
/*     */   static <T> Class<T> forceType(Class<?> paramClass, Class<T> paramClass1)
/*     */   {
/* 499 */     int i = (paramClass == paramClass1) || ((paramClass.isPrimitive()) && (forPrimitiveType(paramClass) == findWrapperType(paramClass1))) || ((paramClass1.isPrimitive()) && (forPrimitiveType(paramClass1) == findWrapperType(paramClass))) || ((paramClass == Object.class) && (!paramClass1.isPrimitive())) ? 1 : 0;
/*     */ 
/* 503 */     if (i == 0)
/* 504 */       System.out.println(paramClass + " <= " + paramClass1);
/* 505 */     assert ((paramClass == paramClass1) || ((paramClass.isPrimitive()) && (forPrimitiveType(paramClass) == findWrapperType(paramClass1))) || ((paramClass1.isPrimitive()) && (forPrimitiveType(paramClass1) == findWrapperType(paramClass))) || ((paramClass == Object.class) && (!paramClass1.isPrimitive())));
/*     */ 
/* 510 */     Class<?> localClass = paramClass;
/* 511 */     return localClass;
/*     */   }
/*     */ 
/*     */   public Object wrap(Object paramObject)
/*     */   {
/* 524 */     switch (this.basicTypeChar) { case 'L':
/* 525 */       return paramObject;
/*     */     case 'V':
/* 526 */       return null;
/*     */     }
/* 528 */     Number localNumber = numberValue(paramObject);
/* 529 */     switch (this.basicTypeChar) { case 'I':
/* 530 */       return Integer.valueOf(localNumber.intValue());
/*     */     case 'J':
/* 531 */       return Long.valueOf(localNumber.longValue());
/*     */     case 'F':
/* 532 */       return Float.valueOf(localNumber.floatValue());
/*     */     case 'D':
/* 533 */       return Double.valueOf(localNumber.doubleValue());
/*     */     case 'S':
/* 534 */       return Short.valueOf((short)localNumber.intValue());
/*     */     case 'B':
/* 535 */       return Byte.valueOf((byte)localNumber.intValue());
/*     */     case 'C':
/* 536 */       return Character.valueOf((char)localNumber.intValue());
/*     */     case 'Z':
/* 537 */       return Boolean.valueOf(boolValue(localNumber.byteValue()));
/*     */     case 'E':
/*     */     case 'G':
/*     */     case 'H':
/*     */     case 'K':
/*     */     case 'L':
/*     */     case 'M':
/*     */     case 'N':
/*     */     case 'O':
/*     */     case 'P':
/*     */     case 'Q':
/*     */     case 'R':
/*     */     case 'T':
/*     */     case 'U':
/*     */     case 'V':
/*     */     case 'W':
/*     */     case 'X':
/* 539 */     case 'Y': } throw new InternalError("bad wrapper");
/*     */   }
/*     */ 
/*     */   public Object wrap(int paramInt)
/*     */   {
/* 549 */     if (this.basicTypeChar == 'L') return Integer.valueOf(paramInt);
/* 550 */     switch (this.basicTypeChar) { case 'L':
/* 551 */       throw newIllegalArgumentException("cannot wrap to object type");
/*     */     case 'V':
/* 552 */       return null;
/*     */     case 'I':
/* 553 */       return Integer.valueOf(paramInt);
/*     */     case 'J':
/* 554 */       return Long.valueOf(paramInt);
/*     */     case 'F':
/* 555 */       return Float.valueOf(paramInt);
/*     */     case 'D':
/* 556 */       return Double.valueOf(paramInt);
/*     */     case 'S':
/* 557 */       return Short.valueOf((short)paramInt);
/*     */     case 'B':
/* 558 */       return Byte.valueOf((byte)paramInt);
/*     */     case 'C':
/* 559 */       return Character.valueOf((char)paramInt);
/*     */     case 'Z':
/* 560 */       return Boolean.valueOf(boolValue((byte)paramInt));
/*     */     case 'E':
/*     */     case 'G':
/*     */     case 'H':
/*     */     case 'K':
/*     */     case 'M':
/*     */     case 'N':
/*     */     case 'O':
/*     */     case 'P':
/*     */     case 'Q':
/*     */     case 'R':
/*     */     case 'T':
/*     */     case 'U':
/*     */     case 'W':
/*     */     case 'X':
/* 562 */     case 'Y': } throw new InternalError("bad wrapper");
/*     */   }
/*     */ 
/*     */   private static Number numberValue(Object paramObject) {
/* 566 */     if ((paramObject instanceof Number)) return (Number)paramObject;
/* 567 */     if ((paramObject instanceof Character)) return Integer.valueOf(((Character)paramObject).charValue());
/* 568 */     if ((paramObject instanceof Boolean)) return Integer.valueOf(((Boolean)paramObject).booleanValue() ? 1 : 0);
/*     */ 
/* 570 */     return (Number)paramObject;
/*     */   }
/*     */ 
/*     */   private static boolean boolValue(byte paramByte)
/*     */   {
/* 577 */     paramByte = (byte)(paramByte & 0x1);
/* 578 */     return paramByte != 0;
/*     */   }
/*     */ 
/*     */   private static RuntimeException newIllegalArgumentException(String paramString, Object paramObject) {
/* 582 */     return newIllegalArgumentException(paramString + paramObject);
/*     */   }
/*     */   private static RuntimeException newIllegalArgumentException(String paramString) {
/* 585 */     return new IllegalArgumentException(paramString);
/*     */   }
/*     */ 
/*     */   public Object makeArray(int paramInt)
/*     */   {
/* 590 */     return Array.newInstance(this.primitiveType, paramInt);
/*     */   }
/*     */   public Class<?> arrayType() {
/* 593 */     return this.emptyArray.getClass();
/*     */   }
/*     */   public void copyArrayUnboxing(Object[] paramArrayOfObject, int paramInt1, Object paramObject, int paramInt2, int paramInt3) {
/* 596 */     if (paramObject.getClass() != arrayType())
/* 597 */       arrayType().cast(paramObject);
/* 598 */     for (int i = 0; i < paramInt3; i++) {
/* 599 */       Object localObject = paramArrayOfObject[(i + paramInt1)];
/* 600 */       localObject = convert(localObject, this.primitiveType);
/* 601 */       Array.set(paramObject, i + paramInt2, localObject);
/*     */     }
/*     */   }
/*     */ 
/* 605 */   public void copyArrayBoxing(Object paramObject, int paramInt1, Object[] paramArrayOfObject, int paramInt2, int paramInt3) { if (paramObject.getClass() != arrayType())
/* 606 */       arrayType().cast(paramObject);
/* 607 */     for (int i = 0; i < paramInt3; i++) {
/* 608 */       Object localObject = Array.get(paramObject, i + paramInt1);
/*     */ 
/* 610 */       assert (localObject.getClass() == this.wrapperType);
/* 611 */       paramArrayOfObject[(i + paramInt2)] = localObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  29 */     BOOLEAN = new Wrapper("BOOLEAN", 0, Boolean.class, Boolean.TYPE, 'Z', Boolean.valueOf(false), new boolean[0], Format.unsigned(1));
/*     */ 
/*  31 */     BYTE = new Wrapper("BYTE", 1, Byte.class, Byte.TYPE, 'B', Byte.valueOf((byte)0), new byte[0], Format.signed(8));
/*  32 */     SHORT = new Wrapper("SHORT", 2, Short.class, Short.TYPE, 'S', Short.valueOf((short)0), new short[0], Format.signed(16));
/*  33 */     CHAR = new Wrapper("CHAR", 3, Character.class, Character.TYPE, 'C', Character.valueOf('\000'), new char[0], Format.unsigned(16));
/*  34 */     INT = new Wrapper("INT", 4, Integer.class, Integer.TYPE, 'I', Integer.valueOf(0), new int[0], Format.signed(32));
/*  35 */     LONG = new Wrapper("LONG", 5, Long.class, Long.TYPE, 'J', Long.valueOf(0L), new long[0], Format.signed(64));
/*  36 */     FLOAT = new Wrapper("FLOAT", 6, Float.class, Float.TYPE, 'F', Float.valueOf(0.0F), new float[0], Format.floating(32));
/*  37 */     DOUBLE = new Wrapper("DOUBLE", 7, Double.class, Double.TYPE, 'D', Double.valueOf(0.0D), new double[0], Format.floating(64));
/*     */ 
/*  39 */     OBJECT = new Wrapper("OBJECT", 8, Object.class, Object.class, 'L', null, new Object[0], Format.other(1));
/*     */ 
/*  41 */     VOID = new Wrapper("VOID", 9, Void.class, Void.TYPE, 'V', null, null, Format.other(0));
/*     */ 
/*  28 */     $VALUES = new Wrapper[] { BOOLEAN, BYTE, SHORT, CHAR, INT, LONG, FLOAT, DOUBLE, OBJECT, VOID };
/*     */ 
/* 164 */     assert (checkConvertibleFrom());
/*     */ 
/* 315 */     FROM_PRIM = new Wrapper[16];
/* 316 */     FROM_WRAP = new Wrapper[16];
/* 317 */     FROM_CHAR = new Wrapper[16];
/*     */ 
/* 333 */     for (Wrapper localWrapper : values()) {
/* 334 */       int k = hashPrim(localWrapper.primitiveType);
/* 335 */       int m = hashWrap(localWrapper.wrapperType);
/* 336 */       int n = hashChar(localWrapper.basicTypeChar);
/* 337 */       assert (FROM_PRIM[k] == null);
/* 338 */       assert (FROM_WRAP[m] == null);
/* 339 */       assert (FROM_CHAR[n] == null);
/* 340 */       FROM_PRIM[k] = localWrapper;
/* 341 */       FROM_WRAP[m] = localWrapper;
/* 342 */       FROM_CHAR[n] = localWrapper;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract class Format
/*     */   {
/*     */     static final int SLOT_SHIFT = 0;
/*     */     static final int SIZE_SHIFT = 2;
/*     */     static final int KIND_SHIFT = 12;
/*     */     static final int SIGNED = -4096;
/*     */     static final int UNSIGNED = 0;
/*     */     static final int FLOATING = 4096;
/*     */     static final int SLOT_MASK = 3;
/*     */     static final int SIZE_MASK = 1023;
/*     */     static final int INT = -3967;
/*     */     static final int SHORT = -4031;
/*     */     static final int BOOLEAN = 5;
/*     */     static final int CHAR = 65;
/*     */     static final int FLOAT = 4225;
/*     */     static final int VOID = 0;
/*     */     static final int NUM_MASK = -4;
/*     */ 
/*     */     static int format(int paramInt1, int paramInt2, int paramInt3)
/*     */     {
/*  82 */       assert (paramInt1 >> 12 << 12 == paramInt1);
/*  83 */       assert ((paramInt2 & paramInt2 - 1) == 0);
/*  84 */       assert (paramInt1 == -4096 ? paramInt2 > 0 : paramInt1 == 0 ? paramInt2 > 0 : (paramInt1 == 4096) && ((paramInt2 == 32) || (paramInt2 == 64)));
/*     */ 
/*  88 */       assert (paramInt3 == 2 ? paramInt2 != 64 : (paramInt3 == 1) && (paramInt2 <= 32));
/*     */ 
/*  91 */       return paramInt1 | paramInt2 << 2 | paramInt3 << 0;
/*     */     }
/*     */ 
/*     */     static int signed(int paramInt)
/*     */     {
/* 101 */       return format(-4096, paramInt, paramInt > 32 ? 2 : 1); } 
/* 102 */     static int unsigned(int paramInt) { return format(0, paramInt, paramInt > 32 ? 2 : 1); } 
/* 103 */     static int floating(int paramInt) { return format(4096, paramInt, paramInt > 32 ? 2 : 1); } 
/* 104 */     static int other(int paramInt) { return paramInt << 0; }
/*     */ 
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.invoke.util.Wrapper
 * JD-Core Version:    0.6.2
 */