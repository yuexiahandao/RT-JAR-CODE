/*     */ package sun.reflect.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.AnnotationFormatError;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.lang.reflect.Type;
/*     */ import java.nio.BufferUnderflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import sun.reflect.ConstantPool;
/*     */ import sun.reflect.generics.factory.CoreReflectionFactory;
/*     */ import sun.reflect.generics.parser.SignatureParser;
/*     */ import sun.reflect.generics.scope.ClassScope;
/*     */ import sun.reflect.generics.tree.TypeSignature;
/*     */ import sun.reflect.generics.visitor.Reifier;
/*     */ 
/*     */ public class AnnotationParser
/*     */ {
/* 194 */   private static final Annotation[] EMPTY_ANNOTATIONS_ARRAY = new Annotation[0];
/*     */ 
/* 854 */   private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
/*     */ 
/*     */   public static Map<Class<? extends Annotation>, Annotation> parseAnnotations(byte[] paramArrayOfByte, ConstantPool paramConstantPool, Class<?> paramClass)
/*     */   {
/*  66 */     if (paramArrayOfByte == null)
/*  67 */       return Collections.emptyMap();
/*     */     try
/*     */     {
/*  70 */       return parseAnnotations2(paramArrayOfByte, paramConstantPool, paramClass, null);
/*     */     } catch (BufferUnderflowException localBufferUnderflowException) {
/*  72 */       throw new AnnotationFormatError("Unexpected end of annotations.");
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException) {
/*  75 */       throw new AnnotationFormatError(localIllegalArgumentException);
/*     */     }
/*     */   }
/*     */ 
/*     */   @SafeVarargs
/*     */   static Map<Class<? extends Annotation>, Annotation> parseSelectAnnotations(byte[] paramArrayOfByte, ConstantPool paramConstantPool, Class<?> paramClass, Class<? extends Annotation>[] paramArrayOfClass)
/*     */   {
/*  94 */     if (paramArrayOfByte == null)
/*  95 */       return Collections.emptyMap();
/*     */     try
/*     */     {
/*  98 */       return parseAnnotations2(paramArrayOfByte, paramConstantPool, paramClass, paramArrayOfClass);
/*     */     } catch (BufferUnderflowException localBufferUnderflowException) {
/* 100 */       throw new AnnotationFormatError("Unexpected end of annotations.");
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException) {
/* 103 */       throw new AnnotationFormatError(localIllegalArgumentException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Map<Class<? extends Annotation>, Annotation> parseAnnotations2(byte[] paramArrayOfByte, ConstantPool paramConstantPool, Class<?> paramClass, Class<? extends Annotation>[] paramArrayOfClass)
/*     */   {
/* 112 */     LinkedHashMap localLinkedHashMap = new LinkedHashMap();
/*     */ 
/* 114 */     ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte);
/* 115 */     int i = localByteBuffer.getShort() & 0xFFFF;
/* 116 */     for (int j = 0; j < i; j++) {
/* 117 */       Annotation localAnnotation = parseAnnotation2(localByteBuffer, paramConstantPool, paramClass, false, paramArrayOfClass);
/* 118 */       if (localAnnotation != null) {
/* 119 */         Class localClass = localAnnotation.annotationType();
/* 120 */         if ((AnnotationType.getInstance(localClass).retention() == RetentionPolicy.RUNTIME) && (localLinkedHashMap.put(localClass, localAnnotation) != null))
/*     */         {
/* 122 */           throw new AnnotationFormatError("Duplicate annotation for class: " + localClass + ": " + localAnnotation);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 127 */     return localLinkedHashMap;
/*     */   }
/*     */ 
/*     */   public static Annotation[][] parseParameterAnnotations(byte[] paramArrayOfByte, ConstantPool paramConstantPool, Class<?> paramClass)
/*     */   {
/*     */     try
/*     */     {
/* 158 */       return parseParameterAnnotations2(paramArrayOfByte, paramConstantPool, paramClass);
/*     */     } catch (BufferUnderflowException localBufferUnderflowException) {
/* 160 */       throw new AnnotationFormatError("Unexpected end of parameter annotations.");
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException)
/*     */     {
/* 164 */       throw new AnnotationFormatError(localIllegalArgumentException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Annotation[][] parseParameterAnnotations2(byte[] paramArrayOfByte, ConstantPool paramConstantPool, Class<?> paramClass)
/*     */   {
/* 172 */     ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte);
/* 173 */     int i = localByteBuffer.get() & 0xFF;
/* 174 */     Annotation[][] arrayOfAnnotation; = new Annotation[i][];
/*     */ 
/* 176 */     for (int j = 0; j < i; j++) {
/* 177 */       int k = localByteBuffer.getShort() & 0xFFFF;
/* 178 */       ArrayList localArrayList = new ArrayList(k);
/*     */ 
/* 180 */       for (int m = 0; m < k; m++) {
/* 181 */         Annotation localAnnotation = parseAnnotation(localByteBuffer, paramConstantPool, paramClass, false);
/* 182 */         if (localAnnotation != null) {
/* 183 */           AnnotationType localAnnotationType = AnnotationType.getInstance(localAnnotation.annotationType());
/*     */ 
/* 185 */           if (localAnnotationType.retention() == RetentionPolicy.RUNTIME)
/* 186 */             localArrayList.add(localAnnotation);
/*     */         }
/*     */       }
/* 189 */       arrayOfAnnotation;[j] = ((Annotation[])localArrayList.toArray(EMPTY_ANNOTATIONS_ARRAY));
/*     */     }
/* 191 */     return arrayOfAnnotation;;
/*     */   }
/*     */ 
/*     */   private static Annotation parseAnnotation(ByteBuffer paramByteBuffer, ConstantPool paramConstantPool, Class<?> paramClass, boolean paramBoolean)
/*     */   {
/* 223 */     return parseAnnotation2(paramByteBuffer, paramConstantPool, paramClass, paramBoolean, null);
/*     */   }
/*     */ 
/*     */   private static Annotation parseAnnotation2(ByteBuffer paramByteBuffer, ConstantPool paramConstantPool, Class<?> paramClass, boolean paramBoolean, Class<? extends Annotation>[] paramArrayOfClass)
/*     */   {
/* 232 */     int i = paramByteBuffer.getShort() & 0xFFFF;
/* 233 */     Class localClass1 = null;
/* 234 */     String str1 = "[unknown]";
/*     */     try {
/*     */       try {
/* 237 */         str1 = paramConstantPool.getUTF8At(i);
/* 238 */         localClass1 = parseSig(str1, paramClass);
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException1) {
/* 241 */         localClass1 = paramConstantPool.getClassAt(i);
/*     */       }
/*     */     } catch (NoClassDefFoundError localNoClassDefFoundError) {
/* 244 */       if (paramBoolean)
/*     */       {
/* 247 */         throw new TypeNotPresentException(str1, localNoClassDefFoundError);
/* 248 */       }skipAnnotation(paramByteBuffer, false);
/* 249 */       return null;
/*     */     }
/*     */     catch (TypeNotPresentException localTypeNotPresentException) {
/* 252 */       if (paramBoolean)
/* 253 */         throw localTypeNotPresentException;
/* 254 */       skipAnnotation(paramByteBuffer, false);
/* 255 */       return null;
/*     */     }
/* 257 */     if ((paramArrayOfClass != null) && (!contains(paramArrayOfClass, localClass1))) {
/* 258 */       skipAnnotation(paramByteBuffer, false);
/* 259 */       return null;
/*     */     }
/* 261 */     AnnotationType localAnnotationType = null;
/*     */     try {
/* 263 */       localAnnotationType = AnnotationType.getInstance(localClass1);
/*     */     } catch (IllegalArgumentException localIllegalArgumentException2) {
/* 265 */       skipAnnotation(paramByteBuffer, false);
/* 266 */       return null;
/*     */     }
/*     */ 
/* 269 */     Map localMap = localAnnotationType.memberTypes();
/* 270 */     LinkedHashMap localLinkedHashMap = new LinkedHashMap(localAnnotationType.memberDefaults());
/*     */ 
/* 273 */     int j = paramByteBuffer.getShort() & 0xFFFF;
/* 274 */     for (int k = 0; k < j; k++) {
/* 275 */       int m = paramByteBuffer.getShort() & 0xFFFF;
/* 276 */       String str2 = paramConstantPool.getUTF8At(m);
/* 277 */       Class localClass2 = (Class)localMap.get(str2);
/*     */ 
/* 279 */       if (localClass2 == null)
/*     */       {
/* 281 */         skipMemberValue(paramByteBuffer);
/*     */       } else {
/* 283 */         Object localObject = parseMemberValue(localClass2, paramByteBuffer, paramConstantPool, paramClass);
/* 284 */         if ((localObject instanceof AnnotationTypeMismatchExceptionProxy)) {
/* 285 */           ((AnnotationTypeMismatchExceptionProxy)localObject).setMember((Method)localAnnotationType.members().get(str2));
/*     */         }
/* 287 */         localLinkedHashMap.put(str2, localObject);
/*     */       }
/*     */     }
/* 290 */     return annotationForMap(localClass1, localLinkedHashMap);
/*     */   }
/*     */ 
/*     */   public static Annotation annotationForMap(Class<? extends Annotation> paramClass, Map<String, Object> paramMap)
/*     */   {
/* 300 */     return (Annotation)Proxy.newProxyInstance(paramClass.getClassLoader(), new Class[] { paramClass }, new AnnotationInvocationHandler(paramClass, paramMap));
/*     */   }
/*     */ 
/*     */   public static Object parseMemberValue(Class<?> paramClass1, ByteBuffer paramByteBuffer, ConstantPool paramConstantPool, Class<?> paramClass2)
/*     */   {
/* 336 */     Object localObject = null;
/* 337 */     int i = paramByteBuffer.get();
/* 338 */     switch (i) {
/*     */     case 101:
/* 340 */       return parseEnumValue(paramClass1, paramByteBuffer, paramConstantPool, paramClass2);
/*     */     case 99:
/* 342 */       localObject = parseClassValue(paramByteBuffer, paramConstantPool, paramClass2);
/* 343 */       break;
/*     */     case 64:
/* 345 */       localObject = parseAnnotation(paramByteBuffer, paramConstantPool, paramClass2, true);
/* 346 */       break;
/*     */     case 91:
/* 348 */       return parseArray(paramClass1, paramByteBuffer, paramConstantPool, paramClass2);
/*     */     default:
/* 350 */       localObject = parseConst(i, paramByteBuffer, paramConstantPool);
/*     */     }
/*     */ 
/* 353 */     if ((!(localObject instanceof ExceptionProxy)) && (!paramClass1.isInstance(localObject)))
/*     */     {
/* 355 */       localObject = new AnnotationTypeMismatchExceptionProxy(localObject.getClass() + "[" + localObject + "]");
/*     */     }
/* 357 */     return localObject;
/*     */   }
/*     */ 
/*     */   private static Object parseConst(int paramInt, ByteBuffer paramByteBuffer, ConstantPool paramConstantPool)
/*     */   {
/* 372 */     int i = paramByteBuffer.getShort() & 0xFFFF;
/* 373 */     switch (paramInt) {
/*     */     case 66:
/* 375 */       return Byte.valueOf((byte)paramConstantPool.getIntAt(i));
/*     */     case 67:
/* 377 */       return Character.valueOf((char)paramConstantPool.getIntAt(i));
/*     */     case 68:
/* 379 */       return Double.valueOf(paramConstantPool.getDoubleAt(i));
/*     */     case 70:
/* 381 */       return Float.valueOf(paramConstantPool.getFloatAt(i));
/*     */     case 73:
/* 383 */       return Integer.valueOf(paramConstantPool.getIntAt(i));
/*     */     case 74:
/* 385 */       return Long.valueOf(paramConstantPool.getLongAt(i));
/*     */     case 83:
/* 387 */       return Short.valueOf((short)paramConstantPool.getIntAt(i));
/*     */     case 90:
/* 389 */       return Boolean.valueOf(paramConstantPool.getIntAt(i) != 0);
/*     */     case 115:
/* 391 */       return paramConstantPool.getUTF8At(i);
/*     */     }
/* 393 */     throw new AnnotationFormatError("Invalid member-value tag in annotation: " + paramInt);
/*     */   }
/*     */ 
/*     */   private static Object parseClassValue(ByteBuffer paramByteBuffer, ConstantPool paramConstantPool, Class<?> paramClass)
/*     */   {
/* 409 */     int i = paramByteBuffer.getShort() & 0xFFFF;
/*     */     try
/*     */     {
/* 412 */       String str = paramConstantPool.getUTF8At(i);
/* 413 */       return parseSig(str, paramClass);
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException) {
/* 416 */       return paramConstantPool.getClassAt(i);
/*     */     }
/*     */     catch (NoClassDefFoundError localNoClassDefFoundError) {
/* 419 */       return new TypeNotPresentExceptionProxy("[unknown]", localNoClassDefFoundError);
/*     */     }
/*     */     catch (TypeNotPresentException localTypeNotPresentException) {
/* 422 */       return new TypeNotPresentExceptionProxy(localTypeNotPresentException.typeName(), localTypeNotPresentException.getCause());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Class<?> parseSig(String paramString, Class<?> paramClass) {
/* 427 */     if (paramString.equals("V")) return Void.TYPE;
/* 428 */     SignatureParser localSignatureParser = SignatureParser.make();
/* 429 */     TypeSignature localTypeSignature = localSignatureParser.parseTypeSig(paramString);
/* 430 */     CoreReflectionFactory localCoreReflectionFactory = CoreReflectionFactory.make(paramClass, ClassScope.make(paramClass));
/* 431 */     Reifier localReifier = Reifier.make(localCoreReflectionFactory);
/* 432 */     localTypeSignature.accept(localReifier);
/* 433 */     Type localType = localReifier.getResult();
/* 434 */     return toClass(localType);
/*     */   }
/*     */   static Class<?> toClass(Type paramType) {
/* 437 */     if ((paramType instanceof GenericArrayType)) {
/* 438 */       return Array.newInstance(toClass(((GenericArrayType)paramType).getGenericComponentType()), 0).getClass();
/*     */     }
/*     */ 
/* 441 */     return (Class)paramType;
/*     */   }
/*     */ 
/*     */   private static Object parseEnumValue(Class<? extends Enum> paramClass, ByteBuffer paramByteBuffer, ConstantPool paramConstantPool, Class<?> paramClass1)
/*     */   {
/* 459 */     int i = paramByteBuffer.getShort() & 0xFFFF;
/* 460 */     String str1 = paramConstantPool.getUTF8At(i);
/* 461 */     int j = paramByteBuffer.getShort() & 0xFFFF;
/* 462 */     String str2 = paramConstantPool.getUTF8At(j);
/*     */ 
/* 464 */     if (!str1.endsWith(";"))
/*     */     {
/* 466 */       if (!paramClass.getName().equals(str1))
/* 467 */         return new AnnotationTypeMismatchExceptionProxy(str1 + "." + str2);
/*     */     }
/* 469 */     else if (paramClass != parseSig(str1, paramClass1)) {
/* 470 */       return new AnnotationTypeMismatchExceptionProxy(str1 + "." + str2);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 475 */       return Enum.valueOf(paramClass, str2); } catch (IllegalArgumentException localIllegalArgumentException) {
/*     */     }
/* 477 */     return new EnumConstantNotPresentExceptionProxy(paramClass, str2);
/*     */   }
/*     */ 
/*     */   private static Object parseArray(Class<?> paramClass1, ByteBuffer paramByteBuffer, ConstantPool paramConstantPool, Class<?> paramClass2)
/*     */   {
/* 500 */     int i = paramByteBuffer.getShort() & 0xFFFF;
/* 501 */     Class localClass = paramClass1.getComponentType();
/*     */ 
/* 503 */     if (localClass == Byte.TYPE)
/* 504 */       return parseByteArray(i, paramByteBuffer, paramConstantPool);
/* 505 */     if (localClass == Character.TYPE)
/* 506 */       return parseCharArray(i, paramByteBuffer, paramConstantPool);
/* 507 */     if (localClass == Double.TYPE)
/* 508 */       return parseDoubleArray(i, paramByteBuffer, paramConstantPool);
/* 509 */     if (localClass == Float.TYPE)
/* 510 */       return parseFloatArray(i, paramByteBuffer, paramConstantPool);
/* 511 */     if (localClass == Integer.TYPE)
/* 512 */       return parseIntArray(i, paramByteBuffer, paramConstantPool);
/* 513 */     if (localClass == Long.TYPE)
/* 514 */       return parseLongArray(i, paramByteBuffer, paramConstantPool);
/* 515 */     if (localClass == Short.TYPE)
/* 516 */       return parseShortArray(i, paramByteBuffer, paramConstantPool);
/* 517 */     if (localClass == Boolean.TYPE)
/* 518 */       return parseBooleanArray(i, paramByteBuffer, paramConstantPool);
/* 519 */     if (localClass == String.class)
/* 520 */       return parseStringArray(i, paramByteBuffer, paramConstantPool);
/* 521 */     if (localClass == Class.class)
/* 522 */       return parseClassArray(i, paramByteBuffer, paramConstantPool, paramClass2);
/* 523 */     if (localClass.isEnum()) {
/* 524 */       return parseEnumArray(i, localClass, paramByteBuffer, paramConstantPool, paramClass2);
/*     */     }
/*     */ 
/* 527 */     assert (localClass.isAnnotation());
/* 528 */     return parseAnnotationArray(i, localClass, paramByteBuffer, paramConstantPool, paramClass2);
/*     */   }
/*     */ 
/*     */   private static Object parseByteArray(int paramInt, ByteBuffer paramByteBuffer, ConstantPool paramConstantPool)
/*     */   {
/* 535 */     byte[] arrayOfByte = new byte[paramInt];
/* 536 */     int i = 0;
/* 537 */     int j = 0;
/*     */ 
/* 539 */     for (int k = 0; k < paramInt; k++) {
/* 540 */       j = paramByteBuffer.get();
/* 541 */       if (j == 66) {
/* 542 */         int m = paramByteBuffer.getShort() & 0xFFFF;
/* 543 */         arrayOfByte[k] = ((byte)paramConstantPool.getIntAt(m));
/*     */       } else {
/* 545 */         skipMemberValue(j, paramByteBuffer);
/* 546 */         i = 1;
/*     */       }
/*     */     }
/* 549 */     return i != 0 ? exceptionProxy(j) : arrayOfByte;
/*     */   }
/*     */ 
/*     */   private static Object parseCharArray(int paramInt, ByteBuffer paramByteBuffer, ConstantPool paramConstantPool)
/*     */   {
/* 554 */     char[] arrayOfChar = new char[paramInt];
/* 555 */     int i = 0;
/* 556 */     int j = 0;
/*     */ 
/* 558 */     for (int k = 0; k < paramInt; k++) {
/* 559 */       j = paramByteBuffer.get();
/* 560 */       if (j == 67) {
/* 561 */         int m = paramByteBuffer.getShort() & 0xFFFF;
/* 562 */         arrayOfChar[k] = ((char)paramConstantPool.getIntAt(m));
/*     */       } else {
/* 564 */         skipMemberValue(j, paramByteBuffer);
/* 565 */         i = 1;
/*     */       }
/*     */     }
/* 568 */     return i != 0 ? exceptionProxy(j) : arrayOfChar;
/*     */   }
/*     */ 
/*     */   private static Object parseDoubleArray(int paramInt, ByteBuffer paramByteBuffer, ConstantPool paramConstantPool)
/*     */   {
/* 573 */     double[] arrayOfDouble = new double[paramInt];
/* 574 */     int i = 0;
/* 575 */     int j = 0;
/*     */ 
/* 577 */     for (int k = 0; k < paramInt; k++) {
/* 578 */       j = paramByteBuffer.get();
/* 579 */       if (j == 68) {
/* 580 */         int m = paramByteBuffer.getShort() & 0xFFFF;
/* 581 */         arrayOfDouble[k] = paramConstantPool.getDoubleAt(m);
/*     */       } else {
/* 583 */         skipMemberValue(j, paramByteBuffer);
/* 584 */         i = 1;
/*     */       }
/*     */     }
/* 587 */     return i != 0 ? exceptionProxy(j) : arrayOfDouble;
/*     */   }
/*     */ 
/*     */   private static Object parseFloatArray(int paramInt, ByteBuffer paramByteBuffer, ConstantPool paramConstantPool)
/*     */   {
/* 592 */     float[] arrayOfFloat = new float[paramInt];
/* 593 */     int i = 0;
/* 594 */     int j = 0;
/*     */ 
/* 596 */     for (int k = 0; k < paramInt; k++) {
/* 597 */       j = paramByteBuffer.get();
/* 598 */       if (j == 70) {
/* 599 */         int m = paramByteBuffer.getShort() & 0xFFFF;
/* 600 */         arrayOfFloat[k] = paramConstantPool.getFloatAt(m);
/*     */       } else {
/* 602 */         skipMemberValue(j, paramByteBuffer);
/* 603 */         i = 1;
/*     */       }
/*     */     }
/* 606 */     return i != 0 ? exceptionProxy(j) : arrayOfFloat;
/*     */   }
/*     */ 
/*     */   private static Object parseIntArray(int paramInt, ByteBuffer paramByteBuffer, ConstantPool paramConstantPool)
/*     */   {
/* 611 */     int[] arrayOfInt = new int[paramInt];
/* 612 */     int i = 0;
/* 613 */     int j = 0;
/*     */ 
/* 615 */     for (int k = 0; k < paramInt; k++) {
/* 616 */       j = paramByteBuffer.get();
/* 617 */       if (j == 73) {
/* 618 */         int m = paramByteBuffer.getShort() & 0xFFFF;
/* 619 */         arrayOfInt[k] = paramConstantPool.getIntAt(m);
/*     */       } else {
/* 621 */         skipMemberValue(j, paramByteBuffer);
/* 622 */         i = 1;
/*     */       }
/*     */     }
/* 625 */     return i != 0 ? exceptionProxy(j) : arrayOfInt;
/*     */   }
/*     */ 
/*     */   private static Object parseLongArray(int paramInt, ByteBuffer paramByteBuffer, ConstantPool paramConstantPool)
/*     */   {
/* 630 */     long[] arrayOfLong = new long[paramInt];
/* 631 */     int i = 0;
/* 632 */     int j = 0;
/*     */ 
/* 634 */     for (int k = 0; k < paramInt; k++) {
/* 635 */       j = paramByteBuffer.get();
/* 636 */       if (j == 74) {
/* 637 */         int m = paramByteBuffer.getShort() & 0xFFFF;
/* 638 */         arrayOfLong[k] = paramConstantPool.getLongAt(m);
/*     */       } else {
/* 640 */         skipMemberValue(j, paramByteBuffer);
/* 641 */         i = 1;
/*     */       }
/*     */     }
/* 644 */     return i != 0 ? exceptionProxy(j) : arrayOfLong;
/*     */   }
/*     */ 
/*     */   private static Object parseShortArray(int paramInt, ByteBuffer paramByteBuffer, ConstantPool paramConstantPool)
/*     */   {
/* 649 */     short[] arrayOfShort = new short[paramInt];
/* 650 */     int i = 0;
/* 651 */     int j = 0;
/*     */ 
/* 653 */     for (int k = 0; k < paramInt; k++) {
/* 654 */       j = paramByteBuffer.get();
/* 655 */       if (j == 83) {
/* 656 */         int m = paramByteBuffer.getShort() & 0xFFFF;
/* 657 */         arrayOfShort[k] = ((short)paramConstantPool.getIntAt(m));
/*     */       } else {
/* 659 */         skipMemberValue(j, paramByteBuffer);
/* 660 */         i = 1;
/*     */       }
/*     */     }
/* 663 */     return i != 0 ? exceptionProxy(j) : arrayOfShort;
/*     */   }
/*     */ 
/*     */   private static Object parseBooleanArray(int paramInt, ByteBuffer paramByteBuffer, ConstantPool paramConstantPool)
/*     */   {
/* 668 */     boolean[] arrayOfBoolean = new boolean[paramInt];
/* 669 */     int i = 0;
/* 670 */     int j = 0;
/*     */ 
/* 672 */     for (int k = 0; k < paramInt; k++) {
/* 673 */       j = paramByteBuffer.get();
/* 674 */       if (j == 90) {
/* 675 */         int m = paramByteBuffer.getShort() & 0xFFFF;
/* 676 */         arrayOfBoolean[k] = (paramConstantPool.getIntAt(m) != 0 ? 1 : false);
/*     */       } else {
/* 678 */         skipMemberValue(j, paramByteBuffer);
/* 679 */         i = 1;
/*     */       }
/*     */     }
/* 682 */     return i != 0 ? exceptionProxy(j) : arrayOfBoolean;
/*     */   }
/*     */ 
/*     */   private static Object parseStringArray(int paramInt, ByteBuffer paramByteBuffer, ConstantPool paramConstantPool)
/*     */   {
/* 687 */     String[] arrayOfString = new String[paramInt];
/* 688 */     int i = 0;
/* 689 */     int j = 0;
/*     */ 
/* 691 */     for (int k = 0; k < paramInt; k++) {
/* 692 */       j = paramByteBuffer.get();
/* 693 */       if (j == 115) {
/* 694 */         int m = paramByteBuffer.getShort() & 0xFFFF;
/* 695 */         arrayOfString[k] = paramConstantPool.getUTF8At(m);
/*     */       } else {
/* 697 */         skipMemberValue(j, paramByteBuffer);
/* 698 */         i = 1;
/*     */       }
/*     */     }
/* 701 */     return i != 0 ? exceptionProxy(j) : arrayOfString;
/*     */   }
/*     */ 
/*     */   private static Object parseClassArray(int paramInt, ByteBuffer paramByteBuffer, ConstantPool paramConstantPool, Class<?> paramClass)
/*     */   {
/* 708 */     Class[] arrayOfClass = new Class[paramInt];
/* 709 */     int i = 0;
/* 710 */     int j = 0;
/*     */ 
/* 712 */     for (int k = 0; k < paramInt; k++) {
/* 713 */       j = paramByteBuffer.get();
/* 714 */       if (j == 99) {
/* 715 */         arrayOfClass[k] = parseClassValue(paramByteBuffer, paramConstantPool, paramClass);
/*     */       } else {
/* 717 */         skipMemberValue(j, paramByteBuffer);
/* 718 */         i = 1;
/*     */       }
/*     */     }
/* 721 */     return i != 0 ? exceptionProxy(j) : arrayOfClass;
/*     */   }
/*     */ 
/*     */   private static Object parseEnumArray(int paramInt, Class<? extends Enum> paramClass, ByteBuffer paramByteBuffer, ConstantPool paramConstantPool, Class<?> paramClass1)
/*     */   {
/* 728 */     Object[] arrayOfObject = (Object[])Array.newInstance(paramClass, paramInt);
/* 729 */     int i = 0;
/* 730 */     int j = 0;
/*     */ 
/* 732 */     for (int k = 0; k < paramInt; k++) {
/* 733 */       j = paramByteBuffer.get();
/* 734 */       if (j == 101) {
/* 735 */         arrayOfObject[k] = parseEnumValue(paramClass, paramByteBuffer, paramConstantPool, paramClass1);
/*     */       } else {
/* 737 */         skipMemberValue(j, paramByteBuffer);
/* 738 */         i = 1;
/*     */       }
/*     */     }
/* 741 */     return i != 0 ? exceptionProxy(j) : arrayOfObject;
/*     */   }
/*     */ 
/*     */   private static Object parseAnnotationArray(int paramInt, Class<? extends Annotation> paramClass, ByteBuffer paramByteBuffer, ConstantPool paramConstantPool, Class<?> paramClass1)
/*     */   {
/* 749 */     Object[] arrayOfObject = (Object[])Array.newInstance(paramClass, paramInt);
/* 750 */     int i = 0;
/* 751 */     int j = 0;
/*     */ 
/* 753 */     for (int k = 0; k < paramInt; k++) {
/* 754 */       j = paramByteBuffer.get();
/* 755 */       if (j == 64) {
/* 756 */         arrayOfObject[k] = parseAnnotation(paramByteBuffer, paramConstantPool, paramClass1, true);
/*     */       } else {
/* 758 */         skipMemberValue(j, paramByteBuffer);
/* 759 */         i = 1;
/*     */       }
/*     */     }
/* 762 */     return i != 0 ? exceptionProxy(j) : arrayOfObject;
/*     */   }
/*     */ 
/*     */   private static ExceptionProxy exceptionProxy(int paramInt)
/*     */   {
/* 770 */     return new AnnotationTypeMismatchExceptionProxy("Array with component tag: " + paramInt);
/*     */   }
/*     */ 
/*     */   private static void skipAnnotation(ByteBuffer paramByteBuffer, boolean paramBoolean)
/*     */   {
/* 784 */     if (paramBoolean)
/* 785 */       paramByteBuffer.getShort();
/* 786 */     int i = paramByteBuffer.getShort() & 0xFFFF;
/* 787 */     for (int j = 0; j < i; j++) {
/* 788 */       paramByteBuffer.getShort();
/* 789 */       skipMemberValue(paramByteBuffer);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void skipMemberValue(ByteBuffer paramByteBuffer)
/*     */   {
/* 799 */     int i = paramByteBuffer.get();
/* 800 */     skipMemberValue(i, paramByteBuffer);
/*     */   }
/*     */ 
/*     */   private static void skipMemberValue(int paramInt, ByteBuffer paramByteBuffer)
/*     */   {
/* 809 */     switch (paramInt) {
/*     */     case 101:
/* 811 */       paramByteBuffer.getInt();
/* 812 */       break;
/*     */     case 64:
/* 814 */       skipAnnotation(paramByteBuffer, true);
/* 815 */       break;
/*     */     case 91:
/* 817 */       skipArray(paramByteBuffer);
/* 818 */       break;
/*     */     default:
/* 821 */       paramByteBuffer.getShort();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void skipArray(ByteBuffer paramByteBuffer)
/*     */   {
/* 831 */     int i = paramByteBuffer.getShort() & 0xFFFF;
/* 832 */     for (int j = 0; j < i; j++)
/* 833 */       skipMemberValue(paramByteBuffer);
/*     */   }
/*     */ 
/*     */   private static boolean contains(Object[] paramArrayOfObject, Object paramObject)
/*     */   {
/* 841 */     for (Object localObject : paramArrayOfObject)
/* 842 */       if (localObject == paramObject)
/* 843 */         return true;
/* 844 */     return false;
/*     */   }
/*     */ 
/*     */   public static Annotation[] toArray(Map<Class<? extends Annotation>, Annotation> paramMap)
/*     */   {
/* 856 */     return (Annotation[])paramMap.values().toArray(EMPTY_ANNOTATION_ARRAY);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.annotation.AnnotationParser
 * JD-Core Version:    0.6.2
 */