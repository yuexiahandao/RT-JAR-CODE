/*     */ package java.lang.invoke;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import sun.invoke.util.ValueConversions;
/*     */ import sun.invoke.util.VerifyAccess;
/*     */ import sun.invoke.util.VerifyType;
/*     */ import sun.invoke.util.Wrapper;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class DirectMethodHandle extends MethodHandle
/*     */ {
/*     */   final MemberName member;
/* 154 */   private static final MemberName.Factory IMPL_NAMES = MemberName.getFactory();
/*     */ 
/* 478 */   private static byte AF_GETFIELD = 0;
/* 479 */   private static byte AF_PUTFIELD = 1;
/* 480 */   private static byte AF_GETSTATIC = 2;
/* 481 */   private static byte AF_PUTSTATIC = 3;
/* 482 */   private static byte AF_GETSTATIC_INIT = 4;
/* 483 */   private static byte AF_PUTSTATIC_INIT = 5;
/* 484 */   private static byte AF_LIMIT = 6;
/*     */ 
/* 488 */   private static int FT_LAST_WRAPPER = Wrapper.values().length - 1;
/* 489 */   private static int FT_UNCHECKED_REF = Wrapper.OBJECT.ordinal();
/* 490 */   private static int FT_CHECKED_REF = FT_LAST_WRAPPER + 1;
/* 491 */   private static int FT_LIMIT = FT_LAST_WRAPPER + 2;
/*     */ 
/* 497 */   private static final LambdaForm[] ACCESSOR_FORMS = new LambdaForm[afIndex(AF_LIMIT, false, 0)];
/*     */ 
/*     */   private DirectMethodHandle(MethodType paramMethodType, LambdaForm paramLambdaForm, MemberName paramMemberName)
/*     */   {
/*  52 */     super(paramMethodType, paramLambdaForm);
/*  53 */     if (!paramMemberName.isResolved()) {
/*  54 */       throw new InternalError();
/*     */     }
/*  56 */     if ((paramMemberName.getDeclaringClass().isInterface()) && (paramMemberName.isMethod()) && (!paramMemberName.isAbstract()))
/*     */     {
/*  59 */       MemberName localMemberName = new MemberName(Object.class, paramMemberName.getName(), paramMemberName.getMethodType(), paramMemberName.getReferenceKind());
/*  60 */       localMemberName = MemberName.getFactory().resolveOrNull(localMemberName.getReferenceKind(), localMemberName, null);
/*  61 */       if ((localMemberName != null) && (localMemberName.isPublic())) {
/*  62 */         paramMemberName = localMemberName;
/*     */       }
/*     */     }
/*     */ 
/*  66 */     this.member = paramMemberName;
/*     */   }
/*     */ 
/*     */   static DirectMethodHandle make(Class<?> paramClass, MemberName paramMemberName)
/*     */   {
/*  72 */     MethodType localMethodType = paramMemberName.getMethodOrFieldType();
/*  73 */     if (!paramMemberName.isStatic()) {
/*  74 */       if ((!paramMemberName.getDeclaringClass().isAssignableFrom(paramClass)) || (paramMemberName.isConstructor()))
/*  75 */         throw new InternalError(paramMemberName.toString());
/*  76 */       localMethodType = localMethodType.insertParameterTypes(0, new Class[] { paramClass });
/*     */     }
/*  78 */     if (!paramMemberName.isField()) {
/*  79 */       localLambdaForm = preparedLambdaForm(paramMemberName);
/*  80 */       return new DirectMethodHandle(localMethodType, localLambdaForm, paramMemberName);
/*     */     }
/*  82 */     LambdaForm localLambdaForm = preparedFieldLambdaForm(paramMemberName);
/*  83 */     if (paramMemberName.isStatic()) {
/*  84 */       l = MethodHandleNatives.staticFieldOffset(paramMemberName);
/*  85 */       Object localObject = MethodHandleNatives.staticFieldBase(paramMemberName);
/*  86 */       return new StaticAccessor(localMethodType, localLambdaForm, paramMemberName, localObject, l, null);
/*     */     }
/*  88 */     long l = MethodHandleNatives.objectFieldOffset(paramMemberName);
/*  89 */     assert (l == (int)l);
/*  90 */     return new Accessor(localMethodType, localLambdaForm, paramMemberName, (int)l, null);
/*     */   }
/*     */ 
/*     */   static DirectMethodHandle make(MemberName paramMemberName)
/*     */   {
/*  95 */     if (paramMemberName.isConstructor())
/*  96 */       return makeAllocator(paramMemberName);
/*  97 */     return make(paramMemberName.getDeclaringClass(), paramMemberName);
/*     */   }
/*     */   static DirectMethodHandle make(Method paramMethod) {
/* 100 */     return make(paramMethod.getDeclaringClass(), new MemberName(paramMethod));
/*     */   }
/*     */   static DirectMethodHandle make(Field paramField) {
/* 103 */     return make(paramField.getDeclaringClass(), new MemberName(paramField));
/*     */   }
/*     */   private static DirectMethodHandle makeAllocator(MemberName paramMemberName) {
/* 106 */     assert ((paramMemberName.isConstructor()) && (paramMemberName.getName().equals("<init>")));
/* 107 */     Class localClass = paramMemberName.getDeclaringClass();
/* 108 */     paramMemberName = paramMemberName.asConstructor();
/* 109 */     assert ((paramMemberName.isConstructor()) && (paramMemberName.getReferenceKind() == 8)) : paramMemberName;
/* 110 */     MethodType localMethodType = paramMemberName.getMethodType().changeReturnType(localClass);
/* 111 */     LambdaForm localLambdaForm = preparedLambdaForm(paramMemberName);
/* 112 */     MemberName localMemberName = paramMemberName.asSpecial();
/* 113 */     assert (localMemberName.getMethodType().returnType() == Void.TYPE);
/* 114 */     return new Constructor(localMethodType, localLambdaForm, paramMemberName, localMemberName, localClass, null);
/*     */   }
/*     */ 
/*     */   MethodHandle copyWith(MethodType paramMethodType, LambdaForm paramLambdaForm)
/*     */   {
/* 119 */     return new DirectMethodHandle(paramMethodType, paramLambdaForm, this.member);
/*     */   }
/*     */ 
/*     */   String internalProperties()
/*     */   {
/* 124 */     return "/DMH=" + this.member.toString();
/*     */   }
/*     */ 
/*     */   @ForceInline
/*     */   MemberName internalMemberName()
/*     */   {
/* 131 */     return this.member;
/*     */   }
/*     */ 
/*     */   MethodHandle bindArgument(int paramInt, char paramChar, Object paramObject)
/*     */   {
/* 137 */     if ((paramInt == 0) && (paramChar == 'L')) {
/* 138 */       DirectMethodHandle localDirectMethodHandle = maybeRebind(paramObject);
/* 139 */       if (localDirectMethodHandle != null)
/* 140 */         return localDirectMethodHandle.bindReceiver(paramObject);
/*     */     }
/* 142 */     return super.bindArgument(paramInt, paramChar, paramObject);
/*     */   }
/*     */ 
/*     */   MethodHandle bindReceiver(Object paramObject)
/*     */   {
/* 148 */     DirectMethodHandle localDirectMethodHandle = maybeRebind(paramObject);
/* 149 */     if (localDirectMethodHandle != null)
/* 150 */       return localDirectMethodHandle.bindReceiver(paramObject);
/* 151 */     return super.bindReceiver(paramObject);
/*     */   }
/*     */ 
/*     */   private DirectMethodHandle maybeRebind(Object paramObject)
/*     */   {
/* 157 */     if (paramObject != null) {
/* 158 */       switch (this.member.getReferenceKind())
/*     */       {
/*     */       case 5:
/*     */       case 9:
/* 162 */         Class localClass = paramObject.getClass();
/* 163 */         MemberName localMemberName = new MemberName(localClass, this.member.getName(), this.member.getMethodType(), (byte)7);
/* 164 */         localMemberName = IMPL_NAMES.resolveOrNull((byte)7, localMemberName, localClass);
/* 165 */         if (localMemberName != null)
/* 166 */           return new DirectMethodHandle(type(), preparedLambdaForm(localMemberName), localMemberName);
/*     */         break;
/*     */       }
/*     */     }
/* 170 */     return null;
/*     */   }
/*     */ 
/*     */   private static LambdaForm preparedLambdaForm(MemberName paramMemberName)
/*     */   {
/* 179 */     assert (paramMemberName.isInvocable()) : paramMemberName;
/* 180 */     MethodType localMethodType = paramMemberName.getInvocationType().basicType();
/* 181 */     assert ((!paramMemberName.isMethodHandleInvoke()) || ("invokeBasic".equals(paramMemberName.getName()))) : paramMemberName;
/*     */     int i;
/* 183 */     switch (paramMemberName.getReferenceKind()) { case 5:
/* 184 */       i = 0; break;
/*     */     case 6:
/* 185 */       i = 1; break;
/*     */     case 7:
/* 186 */       i = 2; break;
/*     */     case 9:
/* 187 */       i = 4; break;
/*     */     case 8:
/* 188 */       i = 3; break;
/*     */     default:
/* 189 */       throw new InternalError(paramMemberName.toString());
/*     */     }
/* 191 */     if ((i == 1) && (shouldBeInitialized(paramMemberName)))
/*     */     {
/* 193 */       preparedLambdaForm(localMethodType, i);
/* 194 */       i = 5;
/*     */     }
/* 196 */     LambdaForm localLambdaForm = preparedLambdaForm(localMethodType, i);
/* 197 */     maybeCompile(localLambdaForm, paramMemberName);
/*     */ 
/* 200 */     if ((!$assertionsDisabled) && (!localLambdaForm.methodType().dropParameterTypes(0, 1).equals(paramMemberName.getInvocationType().basicType()))) throw new AssertionError(Arrays.asList(new Object[] { paramMemberName, paramMemberName.getInvocationType().basicType(), localLambdaForm, localLambdaForm.methodType() }));
/* 201 */     return localLambdaForm;
/*     */   }
/*     */ 
/*     */   private static LambdaForm preparedLambdaForm(MethodType paramMethodType, int paramInt) {
/* 205 */     LambdaForm localLambdaForm = paramMethodType.form().cachedLambdaForm(paramInt);
/* 206 */     if (localLambdaForm != null) return localLambdaForm;
/* 207 */     localLambdaForm = makePreparedLambdaForm(paramMethodType, paramInt);
/* 208 */     return paramMethodType.form().setCachedLambdaForm(paramInt, localLambdaForm);
/*     */   }
/*     */ 
/*     */   private static LambdaForm makePreparedLambdaForm(MethodType paramMethodType, int paramInt) {
/* 212 */     int i = paramInt == 5 ? 1 : 0;
/* 213 */     int j = paramInt == 3 ? 1 : 0;
/*     */     String str1;
/* 215 */     switch (paramInt) { case 0:
/* 216 */       str1 = "linkToVirtual"; str2 = "DMH.invokeVirtual"; break;
/*     */     case 1:
/* 217 */       str1 = "linkToStatic"; str2 = "DMH.invokeStatic"; break;
/*     */     case 5:
/* 218 */       str1 = "linkToStatic"; str2 = "DMH.invokeStaticInit"; break;
/*     */     case 2:
/* 219 */       str1 = "linkToSpecial"; str2 = "DMH.invokeSpecial"; break;
/*     */     case 4:
/* 220 */       str1 = "linkToInterface"; str2 = "DMH.invokeInterface"; break;
/*     */     case 3:
/* 221 */       str1 = "linkToSpecial"; str2 = "DMH.newInvokeSpecial"; break;
/*     */     default:
/* 222 */       throw new InternalError("which=" + paramInt);
/*     */     }
/* 224 */     MethodType localMethodType = paramMethodType.appendParameterTypes(new Class[] { MemberName.class });
/* 225 */     if (j != 0) {
/* 226 */       localMethodType = localMethodType.insertParameterTypes(0, new Class[] { Object.class }).changeReturnType(Void.TYPE);
/*     */     }
/*     */ 
/* 229 */     MemberName localMemberName = new MemberName(MethodHandle.class, str1, localMethodType, (byte)6);
/*     */     try {
/* 231 */       localMemberName = IMPL_NAMES.resolveOrFail((byte)6, localMemberName, null, NoSuchMethodException.class);
/*     */     } catch (ReflectiveOperationException localReflectiveOperationException) {
/* 233 */       throw MethodHandleStatics.newInternalError(localReflectiveOperationException);
/*     */     }
/*     */ 
/* 237 */     int k = 1 + paramMethodType.parameterCount();
/* 238 */     int m = k;
/* 239 */     int n = j != 0 ? m++ : -1;
/* 240 */     int i1 = m++;
/* 241 */     int i2 = m++;
/* 242 */     LambdaForm.Name[] arrayOfName = LambdaForm.arguments(m - k, paramMethodType.invokerType());
/* 243 */     assert (arrayOfName.length == m);
/* 244 */     if (j != 0)
/*     */     {
/* 246 */       arrayOfName[n] = new LambdaForm.Name(Lazy.NF_allocateInstance, new Object[] { arrayOfName[0] });
/* 247 */       arrayOfName[i1] = new LambdaForm.Name(Lazy.NF_constructorMethod, new Object[] { arrayOfName[0] });
/* 248 */     } else if (i != 0) {
/* 249 */       arrayOfName[i1] = new LambdaForm.Name(Lazy.NF_internalMemberNameEnsureInit, new Object[] { arrayOfName[0] });
/*     */     } else {
/* 251 */       arrayOfName[i1] = new LambdaForm.Name(Lazy.NF_internalMemberName, new Object[] { arrayOfName[0] });
/*     */     }
/* 253 */     Object[] arrayOfObject = Arrays.copyOfRange(arrayOfName, 1, i1 + 1, [Ljava.lang.Object.class);
/* 254 */     assert (arrayOfObject[(arrayOfObject.length - 1)] == arrayOfName[i1]);
/* 255 */     int i3 = -2;
/* 256 */     if (j != 0) {
/* 257 */       assert (arrayOfObject[(arrayOfObject.length - 2)] == arrayOfName[n]);
/* 258 */       System.arraycopy(arrayOfObject, 0, arrayOfObject, 1, arrayOfObject.length - 2);
/* 259 */       arrayOfObject[0] = arrayOfName[n];
/* 260 */       i3 = n;
/*     */     }
/* 262 */     arrayOfName[i2] = new LambdaForm.Name(localMemberName, arrayOfObject);
/* 263 */     String str2 = str2 + "_" + LambdaForm.basicTypeSignature(paramMethodType);
/* 264 */     LambdaForm localLambdaForm = new LambdaForm(str2, k, arrayOfName, i3);
/*     */ 
/* 266 */     localLambdaForm.compileToBytecode();
/* 267 */     return localLambdaForm;
/*     */   }
/*     */ 
/*     */   private static void maybeCompile(LambdaForm paramLambdaForm, MemberName paramMemberName) {
/* 271 */     if (VerifyAccess.isSamePackage(paramMemberName.getDeclaringClass(), MethodHandle.class))
/*     */     {
/* 273 */       paramLambdaForm.compileToBytecode();
/*     */     }
/*     */   }
/*     */ 
/*     */   @ForceInline
/*     */   static Object internalMemberName(Object paramObject) {
/* 279 */     return ((DirectMethodHandle)paramObject).member;
/*     */   }
/*     */ 
/*     */   static Object internalMemberNameEnsureInit(Object paramObject)
/*     */   {
/* 286 */     DirectMethodHandle localDirectMethodHandle = (DirectMethodHandle)paramObject;
/* 287 */     localDirectMethodHandle.ensureInitialized();
/* 288 */     return localDirectMethodHandle.member;
/*     */   }
/*     */ 
/*     */   static boolean shouldBeInitialized(MemberName paramMemberName)
/*     */   {
/* 293 */     switch (paramMemberName.getReferenceKind()) {
/*     */     case 2:
/*     */     case 4:
/*     */     case 6:
/*     */     case 8:
/* 298 */       break;
/*     */     case 3:
/*     */     case 5:
/*     */     case 7:
/*     */     default:
/* 301 */       return false;
/*     */     }
/* 303 */     Class localClass = paramMemberName.getDeclaringClass();
/* 304 */     if ((localClass == ValueConversions.class) || (localClass == MethodHandleImpl.class) || (localClass == Invokers.class))
/*     */     {
/* 309 */       return false;
/*     */     }
/* 311 */     if ((VerifyAccess.isSamePackage(MethodHandle.class, localClass)) || (VerifyAccess.isSamePackage(ValueConversions.class, localClass)))
/*     */     {
/* 315 */       if (MethodHandleStatics.UNSAFE.shouldBeInitialized(localClass)) {
/* 316 */         MethodHandleStatics.UNSAFE.ensureClassInitialized(localClass);
/*     */       }
/* 318 */       return false;
/*     */     }
/* 320 */     return MethodHandleStatics.UNSAFE.shouldBeInitialized(localClass);
/*     */   }
/*     */ 
/*     */   private void ensureInitialized()
/*     */   {
/* 337 */     if (checkInitialized(this.member))
/*     */     {
/* 339 */       if (this.member.isField())
/* 340 */         updateForm(preparedFieldLambdaForm(this.member));
/*     */       else
/* 342 */         updateForm(preparedLambdaForm(this.member)); 
/*     */     }
/*     */   }
/*     */ 
/* 346 */   private static boolean checkInitialized(MemberName paramMemberName) { Class localClass = paramMemberName.getDeclaringClass();
/* 347 */     WeakReference localWeakReference = (WeakReference)EnsureInitialized.INSTANCE.get(localClass);
/* 348 */     if (localWeakReference == null) {
/* 349 */       return true;
/*     */     }
/* 351 */     Thread localThread = (Thread)localWeakReference.get();
/*     */ 
/* 353 */     if (localThread == Thread.currentThread())
/*     */     {
/* 355 */       if (MethodHandleStatics.UNSAFE.shouldBeInitialized(localClass))
/*     */       {
/* 357 */         return false;
/*     */       }
/*     */     }
/* 360 */     else MethodHandleStatics.UNSAFE.ensureClassInitialized(localClass);
/*     */ 
/* 362 */     assert (!MethodHandleStatics.UNSAFE.shouldBeInitialized(localClass));
/*     */ 
/* 364 */     EnsureInitialized.INSTANCE.remove(localClass);
/* 365 */     return true; }
/*     */ 
/*     */   static void ensureInitialized(Object paramObject)
/*     */   {
/* 369 */     ((DirectMethodHandle)paramObject).ensureInitialized();
/*     */   }
/*     */ 
/*     */   static Object constructorMethod(Object paramObject)
/*     */   {
/* 387 */     Constructor localConstructor = (Constructor)paramObject;
/* 388 */     return localConstructor.initMethod;
/*     */   }
/*     */ 
/*     */   static Object allocateInstance(Object paramObject) throws InstantiationException {
/* 392 */     Constructor localConstructor = (Constructor)paramObject;
/* 393 */     return MethodHandleStatics.UNSAFE.allocateInstance(localConstructor.instanceClass);
/*     */   }
/*     */ 
/*     */   @ForceInline
/*     */   static long fieldOffset(Object paramObject)
/*     */   {
/* 416 */     return ((Accessor)paramObject).fieldOffset;
/*     */   }
/*     */ 
/*     */   @ForceInline
/*     */   static Object checkBase(Object paramObject)
/*     */   {
/* 428 */     paramObject.getClass();
/* 429 */     return paramObject;
/*     */   }
/*     */ 
/*     */   @ForceInline
/*     */   static Object nullCheck(Object paramObject)
/*     */   {
/* 453 */     paramObject.getClass();
/* 454 */     return paramObject;
/*     */   }
/*     */ 
/*     */   @ForceInline
/*     */   static Object staticBase(Object paramObject) {
/* 459 */     return ((StaticAccessor)paramObject).staticBase;
/*     */   }
/*     */ 
/*     */   @ForceInline
/*     */   static long staticOffset(Object paramObject) {
/* 464 */     return ((StaticAccessor)paramObject).staticOffset;
/*     */   }
/*     */ 
/*     */   @ForceInline
/*     */   static Object checkCast(Object paramObject1, Object paramObject2) {
/* 469 */     return ((DirectMethodHandle)paramObject1).checkCast(paramObject2);
/*     */   }
/*     */ 
/*     */   Object checkCast(Object paramObject) {
/* 473 */     return this.member.getReturnType().cast(paramObject);
/*     */   }
/*     */ 
/*     */   private static int afIndex(byte paramByte, boolean paramBoolean, int paramInt)
/*     */   {
/* 493 */     return paramByte * FT_LIMIT * 2 + (paramBoolean ? FT_LIMIT : 0) + paramInt;
/*     */   }
/*     */ 
/*     */   private static int ftypeKind(Class<?> paramClass)
/*     */   {
/* 500 */     if (paramClass.isPrimitive())
/* 501 */       return Wrapper.forPrimitiveType(paramClass).ordinal();
/* 502 */     if (VerifyType.isNullReferenceConversion(Object.class, paramClass)) {
/* 503 */       return FT_UNCHECKED_REF;
/*     */     }
/* 505 */     return FT_CHECKED_REF;
/*     */   }
/*     */ 
/*     */   private static LambdaForm preparedFieldLambdaForm(MemberName paramMemberName)
/*     */   {
/* 514 */     Class localClass = paramMemberName.getFieldType();
/* 515 */     boolean bool = paramMemberName.isVolatile();
/*     */     int i;
/* 517 */     switch (paramMemberName.getReferenceKind()) { case 1:
/* 518 */       i = AF_GETFIELD; break;
/*     */     case 3:
/* 519 */       i = AF_PUTFIELD; break;
/*     */     case 2:
/* 520 */       i = AF_GETSTATIC; break;
/*     */     case 4:
/* 521 */       i = AF_PUTSTATIC; break;
/*     */     default:
/* 522 */       throw new InternalError(paramMemberName.toString());
/*     */     }
/*     */     byte b;
/* 524 */     if (shouldBeInitialized(paramMemberName))
/*     */     {
/* 526 */       preparedFieldLambdaForm(i, bool, localClass);
/* 527 */       assert (AF_GETSTATIC_INIT - AF_GETSTATIC == AF_PUTSTATIC_INIT - AF_PUTSTATIC);
/*     */ 
/* 529 */       b = (byte)(i + (AF_GETSTATIC_INIT - AF_GETSTATIC));
/*     */     }
/* 531 */     LambdaForm localLambdaForm = preparedFieldLambdaForm(b, bool, localClass);
/* 532 */     maybeCompile(localLambdaForm, paramMemberName);
/*     */ 
/* 535 */     if ((!$assertionsDisabled) && (!localLambdaForm.methodType().dropParameterTypes(0, 1).equals(paramMemberName.getInvocationType().basicType()))) throw new AssertionError(Arrays.asList(new Object[] { paramMemberName, paramMemberName.getInvocationType().basicType(), localLambdaForm, localLambdaForm.methodType() }));
/* 536 */     return localLambdaForm;
/*     */   }
/*     */   private static LambdaForm preparedFieldLambdaForm(byte paramByte, boolean paramBoolean, Class<?> paramClass) {
/* 539 */     int i = afIndex(paramByte, paramBoolean, ftypeKind(paramClass));
/* 540 */     LambdaForm localLambdaForm = ACCESSOR_FORMS[i];
/* 541 */     if (localLambdaForm != null) return localLambdaForm;
/* 542 */     localLambdaForm = makePreparedFieldLambdaForm(paramByte, paramBoolean, ftypeKind(paramClass));
/* 543 */     ACCESSOR_FORMS[i] = localLambdaForm;
/* 544 */     return localLambdaForm;
/*     */   }
/*     */ 
/*     */   private static LambdaForm makePreparedFieldLambdaForm(byte paramByte, boolean paramBoolean, int paramInt) {
/* 548 */     int i = (paramByte & 0x1) == (AF_GETFIELD & 0x1) ? 1 : 0;
/* 549 */     int j = paramByte >= AF_GETSTATIC ? 1 : 0;
/* 550 */     int k = paramByte >= AF_GETSTATIC_INIT ? 1 : 0;
/* 551 */     int m = paramInt == FT_CHECKED_REF ? 1 : 0;
/* 552 */     Wrapper localWrapper = m != 0 ? Wrapper.OBJECT : Wrapper.values()[paramInt];
/* 553 */     Class localClass = localWrapper.primitiveType();
/* 554 */     if (!$assertionsDisabled) if (ftypeKind(m != 0 ? String.class : localClass) != paramInt) throw new AssertionError();
/* 555 */     String str1 = localWrapper.primitiveSimpleName();
/* 556 */     String str2 = Character.toUpperCase(str1.charAt(0)) + str1.substring(1);
/* 557 */     if (paramBoolean) str2 = str2 + "Volatile";
/* 558 */     String str3 = i != 0 ? "get" : "put";
/* 559 */     String str4 = str3 + str2;
/*     */     MethodType localMethodType1;
/* 561 */     if (i != 0)
/* 562 */       localMethodType1 = MethodType.methodType(localClass, Object.class, new Class[] { Long.TYPE });
/*     */     else
/* 564 */       localMethodType1 = MethodType.methodType(Void.TYPE, Object.class, new Class[] { Long.TYPE, localClass });
/* 565 */     MemberName localMemberName = new MemberName(Unsafe.class, str4, localMethodType1, (byte)5);
/*     */     try {
/* 567 */       localMemberName = IMPL_NAMES.resolveOrFail((byte)5, localMemberName, null, NoSuchMethodException.class);
/*     */     } catch (ReflectiveOperationException localReflectiveOperationException) {
/* 569 */       throw MethodHandleStatics.newInternalError(localReflectiveOperationException);
/*     */     }
/*     */ 
/* 574 */     if (i != 0)
/* 575 */       localMethodType2 = MethodType.methodType(localClass);
/*     */     else
/* 577 */       localMethodType2 = MethodType.methodType(Void.TYPE, localClass);
/* 578 */     MethodType localMethodType2 = localMethodType2.basicType();
/* 579 */     if (j == 0) {
/* 580 */       localMethodType2 = localMethodType2.insertParameterTypes(0, new Class[] { Object.class });
/*     */     }
/*     */ 
/* 583 */     int n = 1 + localMethodType2.parameterCount();
/*     */ 
/* 585 */     int i1 = j != 0 ? -1 : 1;
/*     */ 
/* 587 */     int i2 = i != 0 ? -1 : n - 1;
/* 588 */     int i3 = n;
/* 589 */     int i4 = j != 0 ? i3++ : -1;
/* 590 */     int i5 = i3++;
/* 591 */     int i6 = i1 >= 0 ? i3++ : -1;
/* 592 */     int i7 = k != 0 ? i3++ : -1;
/* 593 */     int i8 = (m != 0) && (i == 0) ? i3++ : -1;
/* 594 */     int i9 = i3++;
/* 595 */     int i10 = (m != 0) && (i != 0) ? i3++ : -1;
/* 596 */     int i11 = i3 - 1;
/* 597 */     LambdaForm.Name[] arrayOfName = LambdaForm.arguments(i3 - n, localMethodType2.invokerType());
/* 598 */     if (k != 0)
/* 599 */       arrayOfName[i7] = new LambdaForm.Name(Lazy.NF_ensureInitialized, new Object[] { arrayOfName[0] });
/* 600 */     if ((m != 0) && (i == 0))
/* 601 */       arrayOfName[i8] = new LambdaForm.Name(Lazy.NF_checkCast, new Object[] { arrayOfName[0], arrayOfName[i2] });
/* 602 */     Object[] arrayOfObject = new Object[1 + localMethodType1.parameterCount()];
/* 603 */     if (!$assertionsDisabled) if (arrayOfObject.length != (i != 0 ? 3 : 4)) throw new AssertionError();
/* 604 */     arrayOfObject[0] = MethodHandleStatics.UNSAFE;
/* 605 */     if (j != 0)
/*     */     {
/*     */       void tmp686_683 = new LambdaForm.Name(Lazy.NF_staticBase, new Object[] { arrayOfName[0] }); arrayOfName[i4] = tmp686_683; arrayOfObject[1] = tmp686_683;
/*     */       void tmp717_714 = new LambdaForm.Name(Lazy.NF_staticOffset, new Object[] { arrayOfName[0] }); arrayOfName[i5] = tmp717_714; arrayOfObject[2] = tmp717_714;
/*     */     }
/*     */     else
/*     */     {
/*     */       void tmp752_749 = new LambdaForm.Name(Lazy.NF_checkBase, new Object[] { arrayOfName[i1] }); arrayOfName[i6] = tmp752_749; arrayOfObject[1] = tmp752_749;
/*     */       void tmp783_780 = new LambdaForm.Name(Lazy.NF_fieldOffset, new Object[] { arrayOfName[0] }); arrayOfName[i5] = tmp783_780; arrayOfObject[2] = tmp783_780;
/*     */     }
/* 612 */     if (i == 0)
/* 613 */       arrayOfObject[3] = (m != 0 ? arrayOfName[i8] : arrayOfName[i2]);
/* 615 */     Object localObject2;
/* 615 */     for (localObject2 : arrayOfObject) assert (localObject2 != null);
/* 616 */     arrayOfName[i9] = new LambdaForm.Name(localMemberName, arrayOfObject);
/* 617 */     if ((m != 0) && (i != 0))
/* 618 */       arrayOfName[i10] = new LambdaForm.Name(Lazy.NF_checkCast, new Object[] { arrayOfName[0], arrayOfName[i9] });
/* 619 */     for (localObject2 : arrayOfName) assert (localObject2 != null);
/* 620 */     ??? = j != 0 ? "Static" : "Field";
/* 621 */     String str5 = str4 + (String)???;
/* 622 */     if (m != 0) str5 = str5 + "Cast";
/* 623 */     if (k != 0) str5 = str5 + "Init";
/* 624 */     return new LambdaForm(str5, n, arrayOfName, i11);
/*     */   }
/*     */ 
/*     */   static class Accessor extends DirectMethodHandle
/*     */   {
/*     */     final Class<?> fieldType;
/*     */     final int fieldOffset;
/*     */ 
/*     */     private Accessor(MethodType paramMethodType, LambdaForm paramLambdaForm, MemberName paramMemberName, int paramInt)
/*     */     {
/* 402 */       super(paramLambdaForm, paramMemberName, null);
/* 403 */       this.fieldType = paramMemberName.getFieldType();
/* 404 */       this.fieldOffset = paramInt;
/*     */     }
/*     */ 
/*     */     Object checkCast(Object paramObject) {
/* 408 */       return this.fieldType.cast(paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Constructor extends DirectMethodHandle
/*     */   {
/*     */     final MemberName initMethod;
/*     */     final Class<?> instanceClass;
/*     */ 
/*     */     private Constructor(MethodType paramMethodType, LambdaForm paramLambdaForm, MemberName paramMemberName1, MemberName paramMemberName2, Class<?> paramClass)
/*     */     {
/* 379 */       super(paramLambdaForm, paramMemberName1, null);
/* 380 */       this.initMethod = paramMemberName2;
/* 381 */       this.instanceClass = paramClass;
/* 382 */       assert (paramMemberName2.isResolved());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class EnsureInitialized extends ClassValue<WeakReference<Thread>>
/*     */   {
/* 333 */     static final EnsureInitialized INSTANCE = new EnsureInitialized();
/*     */ 
/*     */     protected WeakReference<Thread> computeValue(Class<?> paramClass)
/*     */     {
/* 326 */       MethodHandleStatics.UNSAFE.ensureClassInitialized(paramClass);
/* 327 */       if (MethodHandleStatics.UNSAFE.shouldBeInitialized(paramClass))
/*     */       {
/* 330 */         return new WeakReference(Thread.currentThread());
/* 331 */       }return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Lazy
/*     */   {
/*     */     static final LambdaForm.NamedFunction NF_internalMemberName;
/*     */     static final LambdaForm.NamedFunction NF_internalMemberNameEnsureInit;
/*     */     static final LambdaForm.NamedFunction NF_ensureInitialized;
/*     */     static final LambdaForm.NamedFunction NF_fieldOffset;
/*     */     static final LambdaForm.NamedFunction NF_checkBase;
/*     */     static final LambdaForm.NamedFunction NF_staticBase;
/*     */     static final LambdaForm.NamedFunction NF_staticOffset;
/*     */     static final LambdaForm.NamedFunction NF_checkCast;
/*     */     static final LambdaForm.NamedFunction NF_allocateInstance;
/*     */     static final LambdaForm.NamedFunction NF_constructorMethod;
/*     */ 
/*     */     static
/*     */     {
/*     */       try
/*     */       {
/*     */         LambdaForm.NamedFunction[] tmp22_19 = new LambdaForm.NamedFunction[10];
/*     */         void tmp49_46 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("internalMemberName", new Class[] { Object.class })); NF_internalMemberName = tmp49_46; tmp22_19[0] = tmp49_46;
/*     */         LambdaForm.NamedFunction[] tmp54_22 = tmp22_19;
/*     */         void tmp81_78 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("internalMemberNameEnsureInit", new Class[] { Object.class })); NF_internalMemberNameEnsureInit = tmp81_78; tmp54_22[1] = tmp81_78;
/*     */         LambdaForm.NamedFunction[] tmp86_54 = tmp54_22;
/*     */         void tmp113_110 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("ensureInitialized", new Class[] { Object.class })); NF_ensureInitialized = tmp113_110; tmp86_54[2] = tmp113_110;
/*     */         LambdaForm.NamedFunction[] tmp118_86 = tmp86_54;
/*     */         void tmp145_142 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("fieldOffset", new Class[] { Object.class })); NF_fieldOffset = tmp145_142; tmp118_86[3] = tmp145_142;
/*     */         LambdaForm.NamedFunction[] tmp150_118 = tmp118_86;
/*     */         void tmp177_174 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("checkBase", new Class[] { Object.class })); NF_checkBase = tmp177_174; tmp150_118[4] = tmp177_174;
/*     */         LambdaForm.NamedFunction[] tmp182_150 = tmp150_118;
/*     */         void tmp209_206 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("staticBase", new Class[] { Object.class })); NF_staticBase = tmp209_206; tmp182_150[5] = tmp209_206;
/*     */         LambdaForm.NamedFunction[] tmp214_182 = tmp182_150;
/*     */         void tmp242_239 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("staticOffset", new Class[] { Object.class })); NF_staticOffset = tmp242_239; tmp214_182[6] = tmp242_239;
/*     */         LambdaForm.NamedFunction[] tmp247_214 = tmp214_182;
/*     */         void tmp281_278 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("checkCast", new Class[] { Object.class, Object.class })); NF_checkCast = tmp281_278; tmp247_214[7] = tmp281_278;
/*     */         LambdaForm.NamedFunction[] tmp286_247 = tmp247_214;
/*     */         void tmp314_311 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("allocateInstance", new Class[] { Object.class })); NF_allocateInstance = tmp314_311; tmp286_247[8] = tmp314_311;
/*     */         LambdaForm.NamedFunction[] tmp319_286 = tmp286_247;
/*     */         void tmp347_344 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("constructorMethod", new Class[] { Object.class })); NF_constructorMethod = tmp347_344; tmp319_286[9] = tmp347_344; LambdaForm.NamedFunction[] arrayOfNamedFunction1 = tmp319_286;
/*     */ 
/* 667 */         for (LambdaForm.NamedFunction localNamedFunction : arrayOfNamedFunction1)
/*     */         {
/* 669 */           assert (InvokerBytecodeGenerator.isStaticallyInvocable(localNamedFunction.member)) : localNamedFunction;
/* 670 */           localNamedFunction.resolve();
/*     */         }
/*     */       } catch (ReflectiveOperationException localReflectiveOperationException) {
/* 673 */         throw MethodHandleStatics.newInternalError(localReflectiveOperationException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class StaticAccessor extends DirectMethodHandle
/*     */   {
/*     */     private final Class<?> fieldType;
/*     */     private final Object staticBase;
/*     */     private final long staticOffset;
/*     */ 
/*     */     private StaticAccessor(MethodType paramMethodType, LambdaForm paramLambdaForm, MemberName paramMemberName, Object paramObject, long paramLong)
/*     */     {
/* 440 */       super(paramLambdaForm, paramMemberName, null);
/* 441 */       this.fieldType = paramMemberName.getFieldType();
/* 442 */       this.staticBase = paramObject;
/* 443 */       this.staticOffset = paramLong;
/*     */     }
/*     */ 
/*     */     Object checkCast(Object paramObject) {
/* 447 */       return this.fieldType.cast(paramObject);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.DirectMethodHandle
 * JD-Core Version:    0.6.2
 */