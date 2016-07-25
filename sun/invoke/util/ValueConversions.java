/*      */ package sun.invoke.util;
/*      */ 
/*      */ import java.lang.invoke.MethodHandle;
/*      */ import java.lang.invoke.MethodHandles;
/*      */ import java.lang.invoke.MethodHandles.Lookup;
/*      */ import java.lang.invoke.MethodType;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumMap;
/*      */ import java.util.List;
/*      */ 
/*      */ public class ValueConversions
/*      */ {
/*      */   private static final Class<?> THIS_CLASS;
/*      */   private static final int MAX_ARITY;
/*      */   private static final MethodHandles.Lookup IMPL_LOOKUP;
/*      */   private static final EnumMap<Wrapper, MethodHandle>[] UNBOX_CONVERSIONS;
/*      */   private static final Integer ZERO_INT;
/*      */   private static final Integer ONE_INT;
/*      */   private static final EnumMap<Wrapper, MethodHandle>[] BOX_CONVERSIONS;
/*      */   private static final EnumMap<Wrapper, MethodHandle>[] CONSTANT_FUNCTIONS;
/*      */   private static final MethodHandle IDENTITY;
/*      */   private static final MethodHandle CAST_REFERENCE;
/*      */   private static final MethodHandle ZERO_OBJECT;
/*      */   private static final MethodHandle IGNORE;
/*      */   private static final MethodHandle EMPTY;
/*      */   private static final MethodHandle ARRAY_IDENTITY;
/*      */   private static final MethodHandle FILL_NEW_TYPED_ARRAY;
/*      */   private static final MethodHandle FILL_NEW_ARRAY;
/*  547 */   private static final MethodHandle COLLECT_ARGUMENTS = localMethodHandle;
/*      */ 
/*  550 */   private static final EnumMap<Wrapper, MethodHandle>[] WRAPPER_CASTS = newWrapperCaches(1);
/*      */ 
/*  821 */   private static final EnumMap<Wrapper, MethodHandle>[] CONVERT_PRIMITIVE_FUNCTIONS = newWrapperCaches(Wrapper.values().length);
/*      */ 
/*  912 */   private static final Object[] NO_ARGS_ARRAY = new Object[0];
/*      */ 
/*  953 */   private static final MethodHandle[] ARRAYS = makeArrays();
/*      */ 
/* 1010 */   private static final MethodHandle[] FILL_ARRAYS = makeFillArrays();
/*      */ 
/* 1062 */   private static final int LEFT_ARGS = FILL_ARRAYS.length - 1;
/* 1063 */   private static final MethodHandle[] FILL_ARRAY_TO_RIGHT = new MethodHandle[MAX_ARITY + 1];
/*      */ 
/* 1108 */   private static final ClassValue<MethodHandle[]> TYPED_COLLECTORS = new ClassValue()
/*      */   {
/*      */     protected MethodHandle[] computeValue(Class<?> paramAnonymousClass)
/*      */     {
/* 1112 */       return new MethodHandle[256];
/*      */     }
/* 1108 */   };
/*      */   static final int MAX_JVM_ARITY = 255;
/* 1169 */   private static final List<Object> NO_ARGS_LIST = Arrays.asList(NO_ARGS_ARRAY);
/*      */ 
/* 1210 */   private static final MethodHandle[] LISTS = makeLists();
/*      */ 
/*      */   private static EnumMap<Wrapper, MethodHandle>[] newWrapperCaches(int paramInt)
/*      */   {
/*   60 */     EnumMap[] arrayOfEnumMap = (EnumMap[])new EnumMap[paramInt];
/*      */ 
/*   62 */     for (int i = 0; i < paramInt; i++)
/*   63 */       arrayOfEnumMap[i] = new EnumMap(Wrapper.class);
/*   64 */     return arrayOfEnumMap;
/*      */   }
/*      */ 
/*      */   static int unboxInteger(Object paramObject, boolean paramBoolean)
/*      */   {
/*   75 */     if ((paramObject instanceof Integer))
/*   76 */       return ((Integer)paramObject).intValue();
/*   77 */     return primitiveConversion(Wrapper.INT, paramObject, paramBoolean).intValue();
/*      */   }
/*      */ 
/*      */   static byte unboxByte(Object paramObject, boolean paramBoolean) {
/*   81 */     if ((paramObject instanceof Byte))
/*   82 */       return ((Byte)paramObject).byteValue();
/*   83 */     return primitiveConversion(Wrapper.BYTE, paramObject, paramBoolean).byteValue();
/*      */   }
/*      */ 
/*      */   static short unboxShort(Object paramObject, boolean paramBoolean) {
/*   87 */     if ((paramObject instanceof Short))
/*   88 */       return ((Short)paramObject).shortValue();
/*   89 */     return primitiveConversion(Wrapper.SHORT, paramObject, paramBoolean).shortValue();
/*      */   }
/*      */ 
/*      */   static boolean unboxBoolean(Object paramObject, boolean paramBoolean) {
/*   93 */     if ((paramObject instanceof Boolean))
/*   94 */       return ((Boolean)paramObject).booleanValue();
/*   95 */     return (primitiveConversion(Wrapper.BOOLEAN, paramObject, paramBoolean).intValue() & 0x1) != 0;
/*      */   }
/*      */ 
/*      */   static char unboxCharacter(Object paramObject, boolean paramBoolean) {
/*   99 */     if ((paramObject instanceof Character))
/*  100 */       return ((Character)paramObject).charValue();
/*  101 */     return (char)primitiveConversion(Wrapper.CHAR, paramObject, paramBoolean).intValue();
/*      */   }
/*      */ 
/*      */   static long unboxLong(Object paramObject, boolean paramBoolean) {
/*  105 */     if ((paramObject instanceof Long))
/*  106 */       return ((Long)paramObject).longValue();
/*  107 */     return primitiveConversion(Wrapper.LONG, paramObject, paramBoolean).longValue();
/*      */   }
/*      */ 
/*      */   static float unboxFloat(Object paramObject, boolean paramBoolean) {
/*  111 */     if ((paramObject instanceof Float))
/*  112 */       return ((Float)paramObject).floatValue();
/*  113 */     return primitiveConversion(Wrapper.FLOAT, paramObject, paramBoolean).floatValue();
/*      */   }
/*      */ 
/*      */   static double unboxDouble(Object paramObject, boolean paramBoolean) {
/*  117 */     if ((paramObject instanceof Double))
/*  118 */       return ((Double)paramObject).doubleValue();
/*  119 */     return primitiveConversion(Wrapper.DOUBLE, paramObject, paramBoolean).doubleValue();
/*      */   }
/*      */ 
/*      */   private static MethodType unboxType(Wrapper paramWrapper) {
/*  123 */     return MethodType.methodType(paramWrapper.primitiveType(), Object.class, new Class[] { Boolean.TYPE });
/*      */   }
/*      */ 
/*      */   private static MethodHandle unbox(Wrapper paramWrapper, boolean paramBoolean)
/*      */   {
/*  130 */     EnumMap localEnumMap = UNBOX_CONVERSIONS[0];
/*  131 */     MethodHandle localMethodHandle = (MethodHandle)localEnumMap.get(paramWrapper);
/*  132 */     if (localMethodHandle != null) {
/*  133 */       return localMethodHandle;
/*      */     }
/*      */ 
/*  136 */     switch (4.$SwitchMap$sun$invoke$util$Wrapper[paramWrapper.ordinal()]) {
/*      */     case 1:
/*  138 */       localMethodHandle = IDENTITY; break;
/*      */     case 2:
/*  140 */       localMethodHandle = IGNORE;
/*      */     }
/*  142 */     if (localMethodHandle != null) {
/*  143 */       localEnumMap.put(paramWrapper, localMethodHandle);
/*  144 */       return localMethodHandle;
/*      */     }
/*      */ 
/*  147 */     String str = "unbox" + paramWrapper.wrapperSimpleName();
/*  148 */     MethodType localMethodType = unboxType(paramWrapper);
/*      */     try {
/*  150 */       localMethodHandle = IMPL_LOOKUP.findStatic(THIS_CLASS, str, localMethodType);
/*      */     } catch (ReflectiveOperationException localReflectiveOperationException) {
/*  152 */       localMethodHandle = null;
/*      */     }
/*  154 */     if (localMethodHandle != null) {
/*  155 */       localMethodHandle = MethodHandles.insertArguments(localMethodHandle, 1, new Object[] { Boolean.valueOf(paramBoolean) });
/*  156 */       localEnumMap.put(paramWrapper, localMethodHandle);
/*  157 */       return localMethodHandle;
/*      */     }
/*  159 */     throw new IllegalArgumentException("cannot find unbox adapter for " + paramWrapper + (paramBoolean ? " (cast)" : ""));
/*      */   }
/*      */ 
/*      */   public static MethodHandle unboxCast(Wrapper paramWrapper)
/*      */   {
/*  164 */     return unbox(paramWrapper, true);
/*      */   }
/*      */ 
/*      */   public static MethodHandle unbox(Class<?> paramClass) {
/*  168 */     return unbox(Wrapper.forPrimitiveType(paramClass), false);
/*      */   }
/*      */ 
/*      */   public static MethodHandle unboxCast(Class<?> paramClass) {
/*  172 */     return unbox(Wrapper.forPrimitiveType(paramClass), true);
/*      */   }
/*      */ 
/*      */   public static Number primitiveConversion(Wrapper paramWrapper, Object paramObject, boolean paramBoolean)
/*      */   {
/*  187 */     if (paramObject == null) {
/*  188 */       if (!paramBoolean) return null;
/*  189 */       return ZERO_INT;
/*      */     }
/*      */     Object localObject;
/*  191 */     if ((paramObject instanceof Number))
/*  192 */       localObject = (Number)paramObject;
/*  193 */     else if ((paramObject instanceof Boolean))
/*  194 */       localObject = ((Boolean)paramObject).booleanValue() ? ONE_INT : ZERO_INT;
/*  195 */     else if ((paramObject instanceof Character)) {
/*  196 */       localObject = Integer.valueOf(((Character)paramObject).charValue());
/*      */     }
/*      */     else {
/*  199 */       localObject = (Number)paramObject;
/*      */     }
/*  201 */     Wrapper localWrapper = Wrapper.findWrapperType(paramObject.getClass());
/*  202 */     if ((localWrapper == null) || ((!paramBoolean) && (!paramWrapper.isConvertibleFrom(localWrapper))))
/*      */     {
/*  204 */       return (Number)paramWrapper.wrapperType().cast(paramObject);
/*  205 */     }return localObject;
/*      */   }
/*      */ 
/*      */   public static int widenSubword(Object paramObject)
/*      */   {
/*  214 */     if ((paramObject instanceof Integer))
/*  215 */       return ((Integer)paramObject).intValue();
/*  216 */     if ((paramObject instanceof Boolean))
/*  217 */       return fromBoolean(((Boolean)paramObject).booleanValue());
/*  218 */     if ((paramObject instanceof Character))
/*  219 */       return ((Character)paramObject).charValue();
/*  220 */     if ((paramObject instanceof Short))
/*  221 */       return ((Short)paramObject).shortValue();
/*  222 */     if ((paramObject instanceof Byte)) {
/*  223 */       return ((Byte)paramObject).byteValue();
/*      */     }
/*      */ 
/*  226 */     return ((Integer)paramObject).intValue();
/*      */   }
/*      */ 
/*      */   static Integer boxInteger(int paramInt)
/*      */   {
/*  232 */     return Integer.valueOf(paramInt);
/*      */   }
/*      */ 
/*      */   static Byte boxByte(byte paramByte) {
/*  236 */     return Byte.valueOf(paramByte);
/*      */   }
/*      */ 
/*      */   static Short boxShort(short paramShort) {
/*  240 */     return Short.valueOf(paramShort);
/*      */   }
/*      */ 
/*      */   static Boolean boxBoolean(boolean paramBoolean) {
/*  244 */     return Boolean.valueOf(paramBoolean);
/*      */   }
/*      */ 
/*      */   static Character boxCharacter(char paramChar) {
/*  248 */     return Character.valueOf(paramChar);
/*      */   }
/*      */ 
/*      */   static Long boxLong(long paramLong) {
/*  252 */     return Long.valueOf(paramLong);
/*      */   }
/*      */ 
/*      */   static Float boxFloat(float paramFloat) {
/*  256 */     return Float.valueOf(paramFloat);
/*      */   }
/*      */ 
/*      */   static Double boxDouble(double paramDouble) {
/*  260 */     return Double.valueOf(paramDouble);
/*      */   }
/*      */ 
/*      */   private static MethodType boxType(Wrapper paramWrapper)
/*      */   {
/*  265 */     Class localClass = paramWrapper.wrapperType();
/*  266 */     return MethodType.methodType(localClass, paramWrapper.primitiveType());
/*      */   }
/*      */ 
/*      */   private static MethodHandle box(Wrapper paramWrapper, boolean paramBoolean)
/*      */   {
/*  273 */     EnumMap localEnumMap = BOX_CONVERSIONS[0];
/*  274 */     MethodHandle localMethodHandle = (MethodHandle)localEnumMap.get(paramWrapper);
/*  275 */     if (localMethodHandle != null) {
/*  276 */       return localMethodHandle;
/*      */     }
/*      */ 
/*  279 */     switch (4.$SwitchMap$sun$invoke$util$Wrapper[paramWrapper.ordinal()]) {
/*      */     case 1:
/*  281 */       localMethodHandle = IDENTITY; break;
/*      */     case 2:
/*  283 */       localMethodHandle = ZERO_OBJECT;
/*      */     }
/*      */ 
/*  286 */     if (localMethodHandle != null) {
/*  287 */       localEnumMap.put(paramWrapper, localMethodHandle);
/*  288 */       return localMethodHandle;
/*      */     }
/*      */ 
/*  291 */     String str = "box" + paramWrapper.wrapperSimpleName();
/*  292 */     MethodType localMethodType = boxType(paramWrapper);
/*  293 */     if (paramBoolean)
/*      */       try {
/*  295 */         localMethodHandle = IMPL_LOOKUP.findStatic(THIS_CLASS, str, localMethodType);
/*      */       } catch (ReflectiveOperationException localReflectiveOperationException) {
/*  297 */         localMethodHandle = null;
/*      */       }
/*      */     else {
/*  300 */       localMethodHandle = box(paramWrapper, !paramBoolean).asType(localMethodType.erase());
/*      */     }
/*  302 */     if (localMethodHandle != null) {
/*  303 */       localEnumMap.put(paramWrapper, localMethodHandle);
/*  304 */       return localMethodHandle;
/*      */     }
/*  306 */     throw new IllegalArgumentException("cannot find box adapter for " + paramWrapper + (paramBoolean ? " (exact)" : ""));
/*      */   }
/*      */ 
/*      */   public static MethodHandle box(Class<?> paramClass)
/*      */   {
/*  311 */     boolean bool = false;
/*      */ 
/*  314 */     return box(Wrapper.forPrimitiveType(paramClass), bool);
/*      */   }
/*      */ 
/*      */   public static MethodHandle box(Wrapper paramWrapper) {
/*  318 */     boolean bool = false;
/*  319 */     return box(paramWrapper, bool);
/*      */   }
/*      */ 
/*      */   static void ignore(Object paramObject)
/*      */   {
/*      */   }
/*      */ 
/*      */   static void empty()
/*      */   {
/*      */   }
/*      */ 
/*      */   static Object zeroObject()
/*      */   {
/*  332 */     return null;
/*      */   }
/*      */ 
/*      */   static int zeroInteger() {
/*  336 */     return 0;
/*      */   }
/*      */ 
/*      */   static long zeroLong() {
/*  340 */     return 0L;
/*      */   }
/*      */ 
/*      */   static float zeroFloat() {
/*  344 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   static double zeroDouble() {
/*  348 */     return 0.0D;
/*      */   }
/*      */ 
/*      */   public static MethodHandle zeroConstantFunction(Wrapper paramWrapper)
/*      */   {
/*  355 */     EnumMap localEnumMap = CONSTANT_FUNCTIONS[0];
/*  356 */     MethodHandle localMethodHandle = (MethodHandle)localEnumMap.get(paramWrapper);
/*  357 */     if (localMethodHandle != null) {
/*  358 */       return localMethodHandle;
/*      */     }
/*      */ 
/*  361 */     MethodType localMethodType = MethodType.methodType(paramWrapper.primitiveType());
/*  362 */     switch (4.$SwitchMap$sun$invoke$util$Wrapper[paramWrapper.ordinal()]) {
/*      */     case 2:
/*  364 */       localMethodHandle = EMPTY;
/*  365 */       break;
/*      */     case 1:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */       try { localMethodHandle = IMPL_LOOKUP.findStatic(THIS_CLASS, "zero" + paramWrapper.wrapperSimpleName(), localMethodType);
/*      */       } catch (ReflectiveOperationException localReflectiveOperationException) {
/*  371 */         localMethodHandle = null;
/*      */       }
/*      */     }
/*      */ 
/*  375 */     if (localMethodHandle != null) {
/*  376 */       localEnumMap.put(paramWrapper, localMethodHandle);
/*  377 */       return localMethodHandle;
/*      */     }
/*      */ 
/*  381 */     if ((paramWrapper.isSubwordOrInt()) && (paramWrapper != Wrapper.INT)) {
/*  382 */       localMethodHandle = MethodHandles.explicitCastArguments(zeroConstantFunction(Wrapper.INT), localMethodType);
/*  383 */       localEnumMap.put(paramWrapper, localMethodHandle);
/*  384 */       return localMethodHandle;
/*      */     }
/*  386 */     throw new IllegalArgumentException("cannot find zero constant for " + paramWrapper);
/*      */   }
/*      */ 
/*      */   static <T> T identity(T paramT)
/*      */   {
/*  397 */     return paramT;
/*      */   }
/*      */ 
/*      */   static <T> T[] identity(T[] paramArrayOfT) {
/*  401 */     return paramArrayOfT;
/*      */   }
/*      */ 
/*      */   static int identity(int paramInt)
/*      */   {
/*  410 */     return paramInt;
/*      */   }
/*      */ 
/*      */   static byte identity(byte paramByte) {
/*  414 */     return paramByte;
/*      */   }
/*      */ 
/*      */   static short identity(short paramShort) {
/*  418 */     return paramShort;
/*      */   }
/*      */ 
/*      */   static boolean identity(boolean paramBoolean) {
/*  422 */     return paramBoolean;
/*      */   }
/*      */ 
/*      */   static char identity(char paramChar) {
/*  426 */     return paramChar;
/*      */   }
/*      */ 
/*      */   static long identity(long paramLong)
/*      */   {
/*  435 */     return paramLong;
/*      */   }
/*      */ 
/*      */   static float identity(float paramFloat) {
/*  439 */     return paramFloat;
/*      */   }
/*      */ 
/*      */   static double identity(double paramDouble) {
/*  443 */     return paramDouble;
/*      */   }
/*      */ 
/*      */   static <T, U> T castReference(Class<? extends T> paramClass, U paramU)
/*      */   {
/*  455 */     if ((paramU != null) && (!paramClass.isInstance(paramU)))
/*  456 */       throw newClassCastException(paramClass, paramU);
/*  457 */     return paramU;
/*      */   }
/*      */ 
/*      */   private static ClassCastException newClassCastException(Class<?> paramClass, Object paramObject) {
/*  461 */     return new ClassCastException("Cannot cast " + paramObject.getClass().getName() + " to " + paramClass.getName());
/*      */   }
/*      */ 
/*      */   static MethodHandle collectArguments(MethodHandle paramMethodHandle1, int paramInt, MethodHandle paramMethodHandle2)
/*      */   {
/*  510 */     if (COLLECT_ARGUMENTS != null) {
/*      */       try {
/*  512 */         return COLLECT_ARGUMENTS.invokeExact(paramMethodHandle1, paramInt, paramMethodHandle2);
/*      */       }
/*      */       catch (Throwable localThrowable) {
/*  515 */         if ((localThrowable instanceof RuntimeException))
/*  516 */           throw ((RuntimeException)localThrowable);
/*  517 */         if ((localThrowable instanceof Error))
/*  518 */           throw ((Error)localThrowable);
/*  519 */         throw new Error(localThrowable.getMessage(), localThrowable);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  525 */     paramMethodHandle1 = MethodHandles.dropArguments(paramMethodHandle1, 1, paramMethodHandle2.type().parameterList());
/*  526 */     paramMethodHandle1 = MethodHandles.foldArguments(paramMethodHandle1, paramMethodHandle2);
/*  527 */     return paramMethodHandle1;
/*      */   }
/*      */ 
/*      */   public static MethodHandle cast(Class<?> paramClass)
/*      */   {
/*  557 */     if (paramClass.isPrimitive()) throw new IllegalArgumentException("cannot cast primitive type " + paramClass);
/*      */ 
/*  559 */     Wrapper localWrapper = null;
/*  560 */     EnumMap localEnumMap = null;
/*  561 */     if (Wrapper.isWrapperType(paramClass)) {
/*  562 */       localWrapper = Wrapper.forWrapperType(paramClass);
/*  563 */       localEnumMap = WRAPPER_CASTS[0];
/*  564 */       localMethodHandle = (MethodHandle)localEnumMap.get(localWrapper);
/*  565 */       if (localMethodHandle != null) return localMethodHandle;
/*      */     }
/*  567 */     MethodHandle localMethodHandle = MethodHandles.insertArguments(CAST_REFERENCE, 0, new Object[] { paramClass });
/*  568 */     if (localEnumMap != null)
/*  569 */       localEnumMap.put(localWrapper, localMethodHandle);
/*  570 */     return localMethodHandle;
/*      */   }
/*      */ 
/*      */   public static MethodHandle identity() {
/*  574 */     return IDENTITY;
/*      */   }
/*      */ 
/*      */   public static MethodHandle identity(Class<?> paramClass) {
/*  578 */     if (!paramClass.isPrimitive())
/*      */     {
/*  580 */       return MethodHandles.identity(paramClass);
/*  581 */     }return identity(Wrapper.findPrimitiveType(paramClass));
/*      */   }
/*      */ 
/*      */   public static MethodHandle identity(Wrapper paramWrapper) {
/*  585 */     EnumMap localEnumMap = CONSTANT_FUNCTIONS[1];
/*  586 */     MethodHandle localMethodHandle = (MethodHandle)localEnumMap.get(paramWrapper);
/*  587 */     if (localMethodHandle != null) {
/*  588 */       return localMethodHandle;
/*      */     }
/*      */ 
/*  591 */     MethodType localMethodType = MethodType.methodType(paramWrapper.primitiveType());
/*  592 */     if (paramWrapper != Wrapper.VOID)
/*  593 */       localMethodType = localMethodType.appendParameterTypes(new Class[] { paramWrapper.primitiveType() });
/*      */     try {
/*  595 */       localMethodHandle = IMPL_LOOKUP.findStatic(THIS_CLASS, "identity", localMethodType);
/*      */     } catch (ReflectiveOperationException localReflectiveOperationException) {
/*  597 */       localMethodHandle = null;
/*      */     }
/*  599 */     if ((localMethodHandle == null) && (paramWrapper == Wrapper.VOID)) {
/*  600 */       localMethodHandle = EMPTY;
/*      */     }
/*  602 */     if (localMethodHandle != null) {
/*  603 */       localEnumMap.put(paramWrapper, localMethodHandle);
/*  604 */       return localMethodHandle;
/*      */     }
/*      */ 
/*  607 */     if (localMethodHandle != null) {
/*  608 */       localEnumMap.put(paramWrapper, localMethodHandle);
/*  609 */       return localMethodHandle;
/*      */     }
/*  611 */     throw new IllegalArgumentException("cannot find identity for " + paramWrapper);
/*      */   }
/*      */ 
/*      */   static float doubleToFloat(double paramDouble)
/*      */   {
/*  622 */     return (float)paramDouble;
/*      */   }
/*      */   static long doubleToLong(double paramDouble) {
/*  625 */     return ()paramDouble;
/*      */   }
/*      */   static int doubleToInt(double paramDouble) {
/*  628 */     return (int)paramDouble;
/*      */   }
/*      */   static short doubleToShort(double paramDouble) {
/*  631 */     return (short)(int)paramDouble;
/*      */   }
/*      */   static char doubleToChar(double paramDouble) {
/*  634 */     return (char)(int)paramDouble;
/*      */   }
/*      */   static byte doubleToByte(double paramDouble) {
/*  637 */     return (byte)(int)paramDouble;
/*      */   }
/*      */   static boolean doubleToBoolean(double paramDouble) {
/*  640 */     return toBoolean((byte)(int)paramDouble);
/*      */   }
/*      */ 
/*      */   static double floatToDouble(float paramFloat)
/*      */   {
/*  645 */     return paramFloat;
/*      */   }
/*      */ 
/*      */   static long floatToLong(float paramFloat) {
/*  649 */     return ()paramFloat;
/*      */   }
/*      */   static int floatToInt(float paramFloat) {
/*  652 */     return (int)paramFloat;
/*      */   }
/*      */   static short floatToShort(float paramFloat) {
/*  655 */     return (short)(int)paramFloat;
/*      */   }
/*      */   static char floatToChar(float paramFloat) {
/*  658 */     return (char)(int)paramFloat;
/*      */   }
/*      */   static byte floatToByte(float paramFloat) {
/*  661 */     return (byte)(int)paramFloat;
/*      */   }
/*      */   static boolean floatToBoolean(float paramFloat) {
/*  664 */     return toBoolean((byte)(int)paramFloat);
/*      */   }
/*      */ 
/*      */   static double longToDouble(long paramLong)
/*      */   {
/*  669 */     return paramLong;
/*      */   }
/*      */   static float longToFloat(long paramLong) {
/*  672 */     return (float)paramLong;
/*      */   }
/*      */ 
/*      */   static int longToInt(long paramLong) {
/*  676 */     return (int)paramLong;
/*      */   }
/*      */   static short longToShort(long paramLong) {
/*  679 */     return (short)(int)paramLong;
/*      */   }
/*      */   static char longToChar(long paramLong) {
/*  682 */     return (char)(int)paramLong;
/*      */   }
/*      */   static byte longToByte(long paramLong) {
/*  685 */     return (byte)(int)paramLong;
/*      */   }
/*      */   static boolean longToBoolean(long paramLong) {
/*  688 */     return toBoolean((byte)(int)paramLong);
/*      */   }
/*      */ 
/*      */   static double intToDouble(int paramInt)
/*      */   {
/*  693 */     return paramInt;
/*      */   }
/*      */   static float intToFloat(int paramInt) {
/*  696 */     return paramInt;
/*      */   }
/*      */   static long intToLong(int paramInt) {
/*  699 */     return paramInt;
/*      */   }
/*      */ 
/*      */   static short intToShort(int paramInt) {
/*  703 */     return (short)paramInt;
/*      */   }
/*      */   static char intToChar(int paramInt) {
/*  706 */     return (char)paramInt;
/*      */   }
/*      */   static byte intToByte(int paramInt) {
/*  709 */     return (byte)paramInt;
/*      */   }
/*      */   static boolean intToBoolean(int paramInt) {
/*  712 */     return toBoolean((byte)paramInt);
/*      */   }
/*      */ 
/*      */   static double shortToDouble(short paramShort)
/*      */   {
/*  717 */     return paramShort;
/*      */   }
/*      */   static float shortToFloat(short paramShort) {
/*  720 */     return paramShort;
/*      */   }
/*      */   static long shortToLong(short paramShort) {
/*  723 */     return paramShort;
/*      */   }
/*      */   static int shortToInt(short paramShort) {
/*  726 */     return paramShort;
/*      */   }
/*      */ 
/*      */   static char shortToChar(short paramShort) {
/*  730 */     return (char)paramShort;
/*      */   }
/*      */   static byte shortToByte(short paramShort) {
/*  733 */     return (byte)paramShort;
/*      */   }
/*      */   static boolean shortToBoolean(short paramShort) {
/*  736 */     return toBoolean((byte)paramShort);
/*      */   }
/*      */ 
/*      */   static double charToDouble(char paramChar)
/*      */   {
/*  741 */     return paramChar;
/*      */   }
/*      */   static float charToFloat(char paramChar) {
/*  744 */     return paramChar;
/*      */   }
/*      */   static long charToLong(char paramChar) {
/*  747 */     return paramChar;
/*      */   }
/*      */   static int charToInt(char paramChar) {
/*  750 */     return paramChar;
/*      */   }
/*      */ 
/*      */   static short charToShort(char paramChar) {
/*  754 */     return (short)paramChar;
/*      */   }
/*      */   static byte charToByte(char paramChar) {
/*  757 */     return (byte)paramChar;
/*      */   }
/*      */   static boolean charToBoolean(char paramChar) {
/*  760 */     return toBoolean((byte)paramChar);
/*      */   }
/*      */ 
/*      */   static double byteToDouble(byte paramByte)
/*      */   {
/*  765 */     return paramByte;
/*      */   }
/*      */   static float byteToFloat(byte paramByte) {
/*  768 */     return paramByte;
/*      */   }
/*      */   static long byteToLong(byte paramByte) {
/*  771 */     return paramByte;
/*      */   }
/*      */   static int byteToInt(byte paramByte) {
/*  774 */     return paramByte;
/*      */   }
/*      */   static short byteToShort(byte paramByte) {
/*  777 */     return (short)paramByte;
/*      */   }
/*      */   static char byteToChar(byte paramByte) {
/*  780 */     return (char)paramByte;
/*      */   }
/*      */ 
/*      */   static boolean byteToBoolean(byte paramByte) {
/*  784 */     return toBoolean(paramByte);
/*      */   }
/*      */ 
/*      */   static double booleanToDouble(boolean paramBoolean)
/*      */   {
/*  789 */     return fromBoolean(paramBoolean);
/*      */   }
/*      */   static float booleanToFloat(boolean paramBoolean) {
/*  792 */     return fromBoolean(paramBoolean);
/*      */   }
/*      */   static long booleanToLong(boolean paramBoolean) {
/*  795 */     return fromBoolean(paramBoolean);
/*      */   }
/*      */   static int booleanToInt(boolean paramBoolean) {
/*  798 */     return fromBoolean(paramBoolean);
/*      */   }
/*      */   static short booleanToShort(boolean paramBoolean) {
/*  801 */     return (short)fromBoolean(paramBoolean);
/*      */   }
/*      */   static char booleanToChar(boolean paramBoolean) {
/*  804 */     return (char)fromBoolean(paramBoolean);
/*      */   }
/*      */   static byte booleanToByte(boolean paramBoolean) {
/*  807 */     return fromBoolean(paramBoolean);
/*      */   }
/*      */ 
/*      */   static boolean toBoolean(byte paramByte)
/*      */   {
/*  813 */     return (paramByte & 0x1) != 0;
/*      */   }
/*      */ 
/*      */   static byte fromBoolean(boolean paramBoolean) {
/*  817 */     return paramBoolean ? 1 : 0;
/*      */   }
/*      */ 
/*      */   public static MethodHandle convertPrimitive(Wrapper paramWrapper1, Wrapper paramWrapper2)
/*      */   {
/*  824 */     EnumMap localEnumMap = CONVERT_PRIMITIVE_FUNCTIONS[paramWrapper1.ordinal()];
/*  825 */     MethodHandle localMethodHandle = (MethodHandle)localEnumMap.get(paramWrapper2);
/*  826 */     if (localMethodHandle != null) {
/*  827 */       return localMethodHandle;
/*      */     }
/*      */ 
/*  830 */     Class localClass1 = paramWrapper1.primitiveType();
/*  831 */     Class localClass2 = paramWrapper2.primitiveType();
/*  832 */     MethodType localMethodType = localClass1 == Void.TYPE ? MethodType.methodType(localClass2) : MethodType.methodType(localClass2, localClass1);
/*  833 */     if (paramWrapper1 == paramWrapper2) {
/*  834 */       localMethodHandle = identity(localClass1);
/*  835 */     } else if (paramWrapper1 == Wrapper.VOID) {
/*  836 */       localMethodHandle = zeroConstantFunction(paramWrapper2);
/*  837 */     } else if (paramWrapper2 == Wrapper.VOID) {
/*  838 */       localMethodHandle = MethodHandles.dropArguments(EMPTY, 0, new Class[] { localClass1 });
/*  839 */     } else if (paramWrapper1 == Wrapper.OBJECT) {
/*  840 */       localMethodHandle = unboxCast(localClass2);
/*  841 */     } else if (paramWrapper2 == Wrapper.OBJECT) {
/*  842 */       localMethodHandle = box(localClass1);
/*      */     } else {
/*  844 */       assert ((localClass1.isPrimitive()) && (localClass2.isPrimitive()));
/*      */       try {
/*  846 */         localMethodHandle = IMPL_LOOKUP.findStatic(THIS_CLASS, localClass1.getSimpleName() + "To" + capitalize(localClass2.getSimpleName()), localMethodType);
/*      */       } catch (ReflectiveOperationException localReflectiveOperationException) {
/*  848 */         localMethodHandle = null;
/*      */       }
/*      */     }
/*  851 */     if (localMethodHandle != null) {
/*  852 */       assert (localMethodHandle.type() == localMethodType) : localMethodHandle;
/*  853 */       localEnumMap.put(paramWrapper2, localMethodHandle);
/*  854 */       return localMethodHandle;
/*      */     }
/*      */ 
/*  857 */     throw new IllegalArgumentException("cannot find primitive conversion function for " + localClass1.getSimpleName() + " -> " + localClass2.getSimpleName());
/*      */   }
/*      */ 
/*      */   public static MethodHandle convertPrimitive(Class<?> paramClass1, Class<?> paramClass2)
/*      */   {
/*  862 */     return convertPrimitive(Wrapper.forPrimitiveType(paramClass1), Wrapper.forPrimitiveType(paramClass2));
/*      */   }
/*      */ 
/*      */   private static String capitalize(String paramString) {
/*  866 */     return Character.toUpperCase(paramString.charAt(0)) + paramString.substring(1);
/*      */   }
/*      */ 
/*      */   public static Object convertArrayElements(Class<?> paramClass, Object paramObject)
/*      */   {
/*  872 */     Class localClass1 = paramObject.getClass().getComponentType();
/*  873 */     Class localClass2 = paramClass.getComponentType();
/*  874 */     if ((localClass1 == null) || (localClass2 == null)) throw new IllegalArgumentException("not array type");
/*  875 */     Object localObject1 = localClass1.isPrimitive() ? Wrapper.forPrimitiveType(localClass1) : null;
/*  876 */     Object localObject2 = localClass2.isPrimitive() ? Wrapper.forPrimitiveType(localClass2) : null;
/*      */     Object[] arrayOfObject;
/*  878 */     if (localObject1 == null) {
/*  879 */       arrayOfObject = (Object[])paramObject;
/*  880 */       i = arrayOfObject.length;
/*  881 */       if (localObject2 == null)
/*  882 */         return Arrays.copyOf(arrayOfObject, i, paramClass.asSubclass([Ljava.lang.Object.class));
/*  883 */       localObject3 = localObject2.makeArray(i);
/*  884 */       localObject2.copyArrayUnboxing(arrayOfObject, 0, localObject3, 0, i);
/*  885 */       return localObject3;
/*      */     }
/*  887 */     int i = Array.getLength(paramObject);
/*      */ 
/*  889 */     if (localObject2 == null)
/*  890 */       arrayOfObject = Arrays.copyOf(NO_ARGS_ARRAY, i, paramClass.asSubclass([Ljava.lang.Object.class));
/*      */     else {
/*  892 */       arrayOfObject = new Object[i];
/*      */     }
/*  894 */     localObject1.copyArrayBoxing(paramObject, 0, arrayOfObject, 0, i);
/*  895 */     if (localObject2 == null) return arrayOfObject;
/*  896 */     Object localObject3 = localObject2.makeArray(i);
/*  897 */     localObject2.copyArrayUnboxing(arrayOfObject, 0, localObject3, 0, i);
/*  898 */     return localObject3;
/*      */   }
/*      */ 
/*      */   private static MethodHandle findCollector(String paramString, int paramInt, Class<?> paramClass, Class<?>[] paramArrayOfClass) {
/*  902 */     MethodType localMethodType = MethodType.genericMethodType(paramInt).changeReturnType(paramClass).insertParameterTypes(0, paramArrayOfClass);
/*      */     try
/*      */     {
/*  906 */       return IMPL_LOOKUP.findStatic(THIS_CLASS, paramString, localMethodType); } catch (ReflectiveOperationException localReflectiveOperationException) {
/*      */     }
/*  908 */     return null;
/*      */   }
/*      */ 
/*      */   private static Object[] makeArray(Object[] paramArrayOfObject)
/*      */   {
/*  913 */     return paramArrayOfObject; } 
/*  914 */   private static Object[] array() { return NO_ARGS_ARRAY; } 
/*      */   private static Object[] array(Object paramObject) {
/*  916 */     return makeArray(new Object[] { paramObject });
/*      */   }
/*  918 */   private static Object[] array(Object paramObject1, Object paramObject2) { return makeArray(new Object[] { paramObject1, paramObject2 }); } 
/*      */   private static Object[] array(Object paramObject1, Object paramObject2, Object paramObject3) {
/*  920 */     return makeArray(new Object[] { paramObject1, paramObject2, paramObject3 });
/*      */   }
/*  922 */   private static Object[] array(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4) { return makeArray(new Object[] { paramObject1, paramObject2, paramObject3, paramObject4 }); }
/*      */ 
/*      */   private static Object[] array(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5) {
/*  925 */     return makeArray(new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5 });
/*      */   }
/*      */   private static Object[] array(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6) {
/*  928 */     return makeArray(new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6 });
/*      */   }
/*      */   private static Object[] array(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7) {
/*  931 */     return makeArray(new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7 });
/*      */   }
/*      */   private static Object[] array(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8) {
/*  934 */     return makeArray(new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7, paramObject8 });
/*      */   }
/*      */ 
/*      */   private static Object[] array(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9) {
/*  938 */     return makeArray(new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7, paramObject8, paramObject9 });
/*      */   }
/*      */ 
/*      */   private static Object[] array(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10) {
/*  942 */     return makeArray(new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7, paramObject8, paramObject9, paramObject10 });
/*      */   }
/*  944 */   private static MethodHandle[] makeArrays() { ArrayList localArrayList = new ArrayList();
/*      */     while (true) {
/*  946 */       MethodHandle localMethodHandle = findCollector("array", localArrayList.size(), [Ljava.lang.Object.class, new Class[0]);
/*  947 */       if (localMethodHandle == null) break;
/*  948 */       localArrayList.add(localMethodHandle);
/*      */     }
/*  950 */     assert (localArrayList.size() == 11);
/*  951 */     return (MethodHandle[])localArrayList.toArray(new MethodHandle[MAX_ARITY + 1]);
/*      */   }
/*      */ 
/*      */   private static Object[] fillNewArray(Integer paramInteger, Object[] paramArrayOfObject)
/*      */   {
/*  958 */     Object[] arrayOfObject = new Object[paramInteger.intValue()];
/*  959 */     fillWithArguments(arrayOfObject, 0, paramArrayOfObject);
/*  960 */     return arrayOfObject;
/*      */   }
/*      */   private static Object[] fillNewTypedArray(Object[] paramArrayOfObject1, Integer paramInteger, Object[] paramArrayOfObject2) {
/*  963 */     Object[] arrayOfObject = Arrays.copyOf(paramArrayOfObject1, paramInteger.intValue());
/*  964 */     fillWithArguments(arrayOfObject, 0, paramArrayOfObject2);
/*  965 */     return arrayOfObject;
/*      */   }
/*      */   private static void fillWithArguments(Object[] paramArrayOfObject1, int paramInt, Object[] paramArrayOfObject2) {
/*  968 */     System.arraycopy(paramArrayOfObject2, 0, paramArrayOfObject1, paramInt, paramArrayOfObject2.length);
/*      */   }
/*      */ 
/*      */   private static Object[] fillArray(Integer paramInteger, Object[] paramArrayOfObject, Object paramObject) {
/*  972 */     fillWithArguments(paramArrayOfObject, paramInteger.intValue(), new Object[] { paramObject }); return paramArrayOfObject;
/*      */   }
/*  974 */   private static Object[] fillArray(Integer paramInteger, Object[] paramArrayOfObject, Object paramObject1, Object paramObject2) { fillWithArguments(paramArrayOfObject, paramInteger.intValue(), new Object[] { paramObject1, paramObject2 }); return paramArrayOfObject; } 
/*      */   private static Object[] fillArray(Integer paramInteger, Object[] paramArrayOfObject, Object paramObject1, Object paramObject2, Object paramObject3) {
/*  976 */     fillWithArguments(paramArrayOfObject, paramInteger.intValue(), new Object[] { paramObject1, paramObject2, paramObject3 }); return paramArrayOfObject;
/*      */   }
/*  978 */   private static Object[] fillArray(Integer paramInteger, Object[] paramArrayOfObject, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4) { fillWithArguments(paramArrayOfObject, paramInteger.intValue(), new Object[] { paramObject1, paramObject2, paramObject3, paramObject4 }); return paramArrayOfObject; }
/*      */ 
/*      */   private static Object[] fillArray(Integer paramInteger, Object[] paramArrayOfObject, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5) {
/*  981 */     fillWithArguments(paramArrayOfObject, paramInteger.intValue(), new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5 }); return paramArrayOfObject;
/*      */   }
/*      */   private static Object[] fillArray(Integer paramInteger, Object[] paramArrayOfObject, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6) {
/*  984 */     fillWithArguments(paramArrayOfObject, paramInteger.intValue(), new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6 }); return paramArrayOfObject;
/*      */   }
/*      */   private static Object[] fillArray(Integer paramInteger, Object[] paramArrayOfObject, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7) {
/*  987 */     fillWithArguments(paramArrayOfObject, paramInteger.intValue(), new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7 }); return paramArrayOfObject;
/*      */   }
/*      */   private static Object[] fillArray(Integer paramInteger, Object[] paramArrayOfObject, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8) {
/*  990 */     fillWithArguments(paramArrayOfObject, paramInteger.intValue(), new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7, paramObject8 }); return paramArrayOfObject;
/*      */   }
/*      */ 
/*      */   private static Object[] fillArray(Integer paramInteger, Object[] paramArrayOfObject, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9) {
/*  994 */     fillWithArguments(paramArrayOfObject, paramInteger.intValue(), new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7, paramObject8, paramObject9 }); return paramArrayOfObject;
/*      */   }
/*      */ 
/*      */   private static Object[] fillArray(Integer paramInteger, Object[] paramArrayOfObject, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10) {
/*  998 */     fillWithArguments(paramArrayOfObject, paramInteger.intValue(), new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7, paramObject8, paramObject9, paramObject10 }); return paramArrayOfObject;
/*      */   }
/* 1000 */   private static MethodHandle[] makeFillArrays() { ArrayList localArrayList = new ArrayList();
/* 1001 */     localArrayList.add(null);
/*      */     while (true) {
/* 1003 */       MethodHandle localMethodHandle = findCollector("fillArray", localArrayList.size(), [Ljava.lang.Object.class, new Class[] { Integer.class, [Ljava.lang.Object.class });
/* 1004 */       if (localMethodHandle == null) break;
/* 1005 */       localArrayList.add(localMethodHandle);
/*      */     }
/* 1007 */     assert (localArrayList.size() == 11);
/* 1008 */     return (MethodHandle[])localArrayList.toArray(new MethodHandle[0]);
/*      */   }
/*      */ 
/*      */   private static Object[] copyAsReferenceArray(Class<? extends Object[]> paramClass, Object[] paramArrayOfObject)
/*      */   {
/* 1013 */     return Arrays.copyOf(paramArrayOfObject, paramArrayOfObject.length, paramClass);
/*      */   }
/*      */   private static Object copyAsPrimitiveArray(Wrapper paramWrapper, Object[] paramArrayOfObject) {
/* 1016 */     Object localObject = paramWrapper.makeArray(paramArrayOfObject.length);
/* 1017 */     paramWrapper.copyArrayUnboxing(paramArrayOfObject, 0, localObject, 0, paramArrayOfObject.length);
/* 1018 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static MethodHandle varargsArray(int paramInt)
/*      */   {
/* 1025 */     MethodHandle localMethodHandle = ARRAYS[paramInt];
/* 1026 */     if (localMethodHandle != null) return localMethodHandle;
/* 1027 */     localMethodHandle = findCollector("array", paramInt, [Ljava.lang.Object.class, new Class[0]);
/* 1028 */     if (localMethodHandle != null) return ARRAYS[paramInt] =  = localMethodHandle;
/* 1029 */     localMethodHandle = buildVarargsArray(FILL_NEW_ARRAY, ARRAY_IDENTITY, paramInt);
/* 1030 */     assert (assertCorrectArity(localMethodHandle, paramInt));
/* 1031 */     return ARRAYS[paramInt] =  = localMethodHandle;
/*      */   }
/*      */ 
/*      */   private static boolean assertCorrectArity(MethodHandle paramMethodHandle, int paramInt) {
/* 1035 */     assert (paramMethodHandle.type().parameterCount() == paramInt) : ("arity != " + paramInt + ": " + paramMethodHandle);
/* 1036 */     return true;
/*      */   }
/*      */ 
/*      */   private static MethodHandle buildVarargsArray(MethodHandle paramMethodHandle1, MethodHandle paramMethodHandle2, int paramInt)
/*      */   {
/* 1043 */     int i = Math.min(paramInt, LEFT_ARGS);
/* 1044 */     int j = paramInt - i;
/* 1045 */     MethodHandle localMethodHandle1 = paramMethodHandle1.bindTo(Integer.valueOf(paramInt));
/* 1046 */     localMethodHandle1 = localMethodHandle1.asCollector([Ljava.lang.Object.class, i);
/* 1047 */     Object localObject = paramMethodHandle2;
/* 1048 */     if (j > 0) {
/* 1049 */       MethodHandle localMethodHandle2 = fillToRight(LEFT_ARGS + j);
/* 1050 */       if (localObject == ARRAY_IDENTITY)
/* 1051 */         localObject = localMethodHandle2;
/*      */       else
/* 1053 */         localObject = collectArguments((MethodHandle)localObject, 0, localMethodHandle2);
/*      */     }
/* 1055 */     if (localObject == ARRAY_IDENTITY)
/* 1056 */       localObject = localMethodHandle1;
/*      */     else
/* 1058 */       localObject = collectArguments((MethodHandle)localObject, 0, localMethodHandle1);
/* 1059 */     return localObject;
/*      */   }
/*      */ 
/*      */   private static MethodHandle fillToRight(int paramInt)
/*      */   {
/* 1069 */     MethodHandle localMethodHandle = FILL_ARRAY_TO_RIGHT[paramInt];
/* 1070 */     if (localMethodHandle != null) return localMethodHandle;
/* 1071 */     localMethodHandle = buildFiller(paramInt);
/* 1072 */     assert (assertCorrectArity(localMethodHandle, paramInt - LEFT_ARGS + 1));
/* 1073 */     return FILL_ARRAY_TO_RIGHT[paramInt] =  = localMethodHandle;
/*      */   }
/*      */   private static MethodHandle buildFiller(int paramInt) {
/* 1076 */     if (paramInt <= LEFT_ARGS) {
/* 1077 */       return ARRAY_IDENTITY;
/*      */     }
/* 1079 */     int i = LEFT_ARGS;
/* 1080 */     int j = paramInt % i;
/* 1081 */     int k = paramInt - j;
/* 1082 */     if (j == 0) {
/* 1083 */       k = paramInt - (j = i);
/* 1084 */       if (FILL_ARRAY_TO_RIGHT[k] == null)
/*      */       {
/* 1086 */         for (int m = LEFT_ARGS % i; m < k; m += i)
/* 1087 */           if (m > LEFT_ARGS) fillToRight(m);
/*      */       }
/*      */     }
/* 1090 */     if (k < LEFT_ARGS) j = paramInt - (k = LEFT_ARGS);
/* 1091 */     assert (j > 0);
/* 1092 */     MethodHandle localMethodHandle1 = fillToRight(k);
/* 1093 */     MethodHandle localMethodHandle2 = FILL_ARRAYS[j].bindTo(Integer.valueOf(k));
/* 1094 */     assert (localMethodHandle1.type().parameterCount() == 1 + k - LEFT_ARGS);
/* 1095 */     assert (localMethodHandle2.type().parameterCount() == 1 + j);
/*      */ 
/* 1101 */     if (k == LEFT_ARGS) {
/* 1102 */       return localMethodHandle2;
/*      */     }
/* 1104 */     return collectArguments(localMethodHandle2, 0, localMethodHandle1);
/*      */   }
/*      */ 
/*      */   public static MethodHandle varargsArray(Class<?> paramClass, int paramInt)
/*      */   {
/* 1123 */     Class localClass = paramClass.getComponentType();
/* 1124 */     if (localClass == null) throw new IllegalArgumentException("not an array: " + paramClass);
/*      */ 
/* 1126 */     if (paramInt >= 126) {
/* 1127 */       int i = paramInt;
/*      */ 
/* 1129 */       if ((paramClass == [D.class) || (paramClass == [J.class))
/* 1130 */         i *= 2;
/* 1131 */       if (i > 254)
/* 1132 */         throw new IllegalArgumentException("too many arguments: " + paramClass.getSimpleName() + ", length " + paramInt);
/*      */     }
/* 1134 */     if (localClass == Object.class) {
/* 1135 */       return varargsArray(paramInt);
/*      */     }
/* 1137 */     MethodHandle[] arrayOfMethodHandle = (MethodHandle[])TYPED_COLLECTORS.get(localClass);
/* 1138 */     MethodHandle localMethodHandle1 = paramInt < arrayOfMethodHandle.length ? arrayOfMethodHandle[paramInt] : null;
/* 1139 */     if (localMethodHandle1 != null) return localMethodHandle1;
/*      */     Object localObject1;
/*      */     Object localObject2;
/* 1140 */     if (localClass.isPrimitive()) {
/* 1141 */       localObject1 = FILL_NEW_ARRAY;
/* 1142 */       localObject2 = buildArrayProducer(paramClass);
/* 1143 */       localMethodHandle1 = buildVarargsArray((MethodHandle)localObject1, (MethodHandle)localObject2, paramInt);
/*      */     }
/*      */     else {
/* 1146 */       localObject1 = paramClass;
/* 1147 */       localObject2 = Arrays.copyOf(NO_ARGS_ARRAY, 0, (Class)localObject1);
/* 1148 */       MethodHandle localMethodHandle2 = FILL_NEW_TYPED_ARRAY.bindTo(localObject2);
/* 1149 */       MethodHandle localMethodHandle3 = ARRAY_IDENTITY;
/* 1150 */       localMethodHandle1 = buildVarargsArray(localMethodHandle2, localMethodHandle3, paramInt);
/*      */     }
/* 1152 */     localMethodHandle1 = localMethodHandle1.asType(MethodType.methodType(paramClass, Collections.nCopies(paramInt, localClass)));
/* 1153 */     assert (assertCorrectArity(localMethodHandle1, paramInt));
/* 1154 */     if (paramInt < arrayOfMethodHandle.length)
/* 1155 */       arrayOfMethodHandle[paramInt] = localMethodHandle1;
/* 1156 */     return localMethodHandle1;
/*      */   }
/*      */ 
/*      */   private static MethodHandle buildArrayProducer(Class<?> paramClass) {
/* 1160 */     Class localClass = paramClass.getComponentType();
/* 1161 */     if (localClass.isPrimitive()) {
/* 1162 */       return LazyStatics.COPY_AS_PRIMITIVE_ARRAY.bindTo(Wrapper.forPrimitiveType(localClass));
/*      */     }
/* 1164 */     return LazyStatics.COPY_AS_REFERENCE_ARRAY.bindTo(paramClass);
/*      */   }
/*      */ 
/*      */   private static List<Object> makeList(Object[] paramArrayOfObject)
/*      */   {
/* 1170 */     return Arrays.asList(paramArrayOfObject); } 
/* 1171 */   private static List<Object> list() { return NO_ARGS_LIST; } 
/*      */   private static List<Object> list(Object paramObject) {
/* 1173 */     return makeList(new Object[] { paramObject });
/*      */   }
/* 1175 */   private static List<Object> list(Object paramObject1, Object paramObject2) { return makeList(new Object[] { paramObject1, paramObject2 }); } 
/*      */   private static List<Object> list(Object paramObject1, Object paramObject2, Object paramObject3) {
/* 1177 */     return makeList(new Object[] { paramObject1, paramObject2, paramObject3 });
/*      */   }
/* 1179 */   private static List<Object> list(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4) { return makeList(new Object[] { paramObject1, paramObject2, paramObject3, paramObject4 }); }
/*      */ 
/*      */   private static List<Object> list(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5) {
/* 1182 */     return makeList(new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5 });
/*      */   }
/*      */   private static List<Object> list(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6) {
/* 1185 */     return makeList(new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6 });
/*      */   }
/*      */   private static List<Object> list(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7) {
/* 1188 */     return makeList(new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7 });
/*      */   }
/*      */   private static List<Object> list(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8) {
/* 1191 */     return makeList(new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7, paramObject8 });
/*      */   }
/*      */ 
/*      */   private static List<Object> list(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9) {
/* 1195 */     return makeList(new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7, paramObject8, paramObject9 });
/*      */   }
/*      */ 
/*      */   private static List<Object> list(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10) {
/* 1199 */     return makeList(new Object[] { paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7, paramObject8, paramObject9, paramObject10 });
/*      */   }
/* 1201 */   private static MethodHandle[] makeLists() { ArrayList localArrayList = new ArrayList();
/*      */     while (true) {
/* 1203 */       MethodHandle localMethodHandle = findCollector("list", localArrayList.size(), List.class, new Class[0]);
/* 1204 */       if (localMethodHandle == null) break;
/* 1205 */       localArrayList.add(localMethodHandle);
/*      */     }
/* 1207 */     assert (localArrayList.size() == 11);
/* 1208 */     return (MethodHandle[])localArrayList.toArray(new MethodHandle[MAX_ARITY + 1]);
/*      */   }
/*      */ 
/*      */   public static MethodHandle varargsList(int paramInt)
/*      */   {
/* 1216 */     MethodHandle localMethodHandle = LISTS[paramInt];
/* 1217 */     if (localMethodHandle != null) return localMethodHandle;
/* 1218 */     localMethodHandle = findCollector("list", paramInt, List.class, new Class[0]);
/* 1219 */     if (localMethodHandle != null) return LISTS[paramInt] =  = localMethodHandle;
/* 1220 */     return LISTS[paramInt] =  = buildVarargsList(paramInt);
/*      */   }
/*      */   private static MethodHandle buildVarargsList(int paramInt) {
/* 1223 */     return MethodHandles.filterReturnValue(varargsArray(paramInt), LazyStatics.MAKE_LIST);
/*      */   }
/*      */ 
/*      */   private static InternalError newInternalError(String paramString, Throwable paramThrowable)
/*      */   {
/* 1228 */     InternalError localInternalError = new InternalError(paramString);
/* 1229 */     localInternalError.initCause(paramThrowable);
/* 1230 */     return localInternalError;
/*      */   }
/*      */   private static InternalError newInternalError(Throwable paramThrowable) {
/* 1233 */     InternalError localInternalError = new InternalError();
/* 1234 */     localInternalError.initCause(paramThrowable);
/* 1235 */     return localInternalError;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   41 */     THIS_CLASS = ValueConversions.class;
/*      */ 
/*   45 */     Object localObject1 = { Integer.valueOf(255) };
/*   46 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Void run() {
/*   49 */         this.val$values[0] = Integer.getInteger(ValueConversions.THIS_CLASS.getName() + ".MAX_ARITY", 255);
/*   50 */         return null;
/*      */       }
/*      */ 
/*      */     });
/*   53 */     MAX_ARITY = ((Integer)localObject1[0]).intValue();
/*      */ 
/*   56 */     IMPL_LOOKUP = MethodHandles.lookup();
/*      */ 
/*  127 */     UNBOX_CONVERSIONS = newWrapperCaches(2);
/*      */ 
/*  175 */     ZERO_INT = Integer.valueOf(0); ONE_INT = Integer.valueOf(1);
/*      */ 
/*  270 */     BOX_CONVERSIONS = newWrapperCaches(2);
/*      */ 
/*  352 */     CONSTANT_FUNCTIONS = newWrapperCaches(2);
/*      */     Object localObject2;
/*      */     try
/*      */     {
/*  468 */       localObject1 = MethodType.genericMethodType(1);
/*  469 */       localObject2 = ((MethodType)localObject1).insertParameterTypes(0, new Class[] { Class.class });
/*  470 */       MethodType localMethodType1 = ((MethodType)localObject1).changeReturnType(Void.TYPE);
/*  471 */       MethodType localMethodType2 = MethodType.genericMethodType(0);
/*  472 */       IDENTITY = IMPL_LOOKUP.findStatic(THIS_CLASS, "identity", (MethodType)localObject1);
/*      */ 
/*  474 */       CAST_REFERENCE = IMPL_LOOKUP.findStatic(THIS_CLASS, "castReference", (MethodType)localObject2);
/*  475 */       ZERO_OBJECT = IMPL_LOOKUP.findStatic(THIS_CLASS, "zeroObject", localMethodType2);
/*  476 */       IGNORE = IMPL_LOOKUP.findStatic(THIS_CLASS, "ignore", localMethodType1);
/*  477 */       EMPTY = IMPL_LOOKUP.findStatic(THIS_CLASS, "empty", localMethodType1.dropParameterTypes(0, 1));
/*  478 */       ARRAY_IDENTITY = IMPL_LOOKUP.findStatic(THIS_CLASS, "identity", MethodType.methodType([Ljava.lang.Object.class, [Ljava.lang.Object.class));
/*  479 */       FILL_NEW_ARRAY = IMPL_LOOKUP.findStatic(THIS_CLASS, "fillNewArray", MethodType.methodType([Ljava.lang.Object.class, Integer.class, new Class[] { [Ljava.lang.Object.class }));
/*      */ 
/*  482 */       FILL_NEW_TYPED_ARRAY = IMPL_LOOKUP.findStatic(THIS_CLASS, "fillNewTypedArray", MethodType.methodType([Ljava.lang.Object.class, [Ljava.lang.Object.class, new Class[] { Integer.class, [Ljava.lang.Object.class }));
/*      */     }
/*      */     catch (NoSuchMethodException|IllegalAccessException localNoSuchMethodException)
/*      */     {
/*  486 */       throw newInternalError("uncaught exception", localNoSuchMethodException);
/*      */     }
/*      */ 
/*  531 */     MethodHandle localMethodHandle = null;
/*      */     try {
/*  533 */       localObject2 = MethodHandles.class.getDeclaredMethod("collectArguments", new Class[] { MethodHandle.class, Integer.TYPE, MethodHandle.class });
/*      */ 
/*  536 */       AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Void run() {
/*  539 */           this.val$m.setAccessible(true);
/*  540 */           return null;
/*      */         }
/*      */       });
/*  543 */       localMethodHandle = IMPL_LOOKUP.unreflect((Method)localObject2);
/*      */     } catch (ReflectiveOperationException localReflectiveOperationException) {
/*  545 */       throw newInternalError(localReflectiveOperationException);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class LazyStatics
/*      */   {
/*      */     private static final MethodHandle COPY_AS_REFERENCE_ARRAY;
/*      */     private static final MethodHandle COPY_AS_PRIMITIVE_ARRAY;
/*      */     private static final MethodHandle MAKE_LIST;
/*      */ 
/*      */     static
/*      */     {
/*      */       try
/*      */       {
/*  496 */         COPY_AS_REFERENCE_ARRAY = ValueConversions.IMPL_LOOKUP.findStatic(ValueConversions.THIS_CLASS, "copyAsReferenceArray", MethodType.methodType([Ljava.lang.Object.class, Class.class, new Class[] { [Ljava.lang.Object.class }));
/*  497 */         COPY_AS_PRIMITIVE_ARRAY = ValueConversions.IMPL_LOOKUP.findStatic(ValueConversions.THIS_CLASS, "copyAsPrimitiveArray", MethodType.methodType(Object.class, Wrapper.class, new Class[] { [Ljava.lang.Object.class }));
/*  498 */         MAKE_LIST = ValueConversions.IMPL_LOOKUP.findStatic(ValueConversions.THIS_CLASS, "makeList", MethodType.methodType(List.class, [Ljava.lang.Object.class));
/*      */       } catch (ReflectiveOperationException localReflectiveOperationException) {
/*  500 */         throw ValueConversions.newInternalError("uncaught exception", localReflectiveOperationException);
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.invoke.util.ValueConversions
 * JD-Core Version:    0.6.2
 */