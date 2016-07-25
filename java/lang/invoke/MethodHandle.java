/*      */ package java.lang.invoke;
/*      */ 
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.annotation.Retention;
/*      */ import java.lang.annotation.RetentionPolicy;
/*      */ import java.lang.annotation.Target;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import sun.invoke.util.ValueConversions;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ public abstract class MethodHandle
/*      */ {
/*      */   private final MethodType type;
/*      */   final LambdaForm form;
/*      */   private static final LambdaForm.NamedFunction NF_reinvokerTarget;
/*      */   private static final long FORM_OFFSET;
/*      */ 
/*      */   public MethodType type()
/*      */   {
/*  430 */     return this.type;
/*      */   }
/*      */ 
/*      */   MethodHandle(MethodType paramMethodType, LambdaForm paramLambdaForm)
/*      */   {
/*  440 */     paramMethodType.getClass();
/*  441 */     paramLambdaForm.getClass();
/*  442 */     this.type = paramMethodType;
/*  443 */     this.form = paramLambdaForm;
/*      */ 
/*  445 */     paramLambdaForm.prepare();
/*      */   }
/*      */ 
/*      */   @PolymorphicSignature
/*      */   public final native Object invokeExact(Object[] paramArrayOfObject)
/*      */     throws Throwable;
/*      */ 
/*      */   @PolymorphicSignature
/*      */   public final native Object invoke(Object[] paramArrayOfObject)
/*      */     throws Throwable;
/*      */ 
/*      */   @PolymorphicSignature
/*      */   final native Object invokeBasic(Object[] paramArrayOfObject)
/*      */     throws Throwable;
/*      */ 
/*      */   @PolymorphicSignature
/*      */   static native Object linkToVirtual(Object[] paramArrayOfObject)
/*      */     throws Throwable;
/*      */ 
/*      */   @PolymorphicSignature
/*      */   static native Object linkToStatic(Object[] paramArrayOfObject)
/*      */     throws Throwable;
/*      */ 
/*      */   @PolymorphicSignature
/*      */   static native Object linkToSpecial(Object[] paramArrayOfObject)
/*      */     throws Throwable;
/*      */ 
/*      */   @PolymorphicSignature
/*      */   static native Object linkToInterface(Object[] paramArrayOfObject)
/*      */     throws Throwable;
/*      */ 
/*      */   public Object invokeWithArguments(Object[] paramArrayOfObject)
/*      */     throws Throwable
/*      */   {
/*  591 */     int i = paramArrayOfObject == null ? 0 : paramArrayOfObject.length;
/*      */ 
/*  593 */     MethodType localMethodType = type();
/*  594 */     if ((localMethodType.parameterCount() != i) || (isVarargsCollector()))
/*      */     {
/*  596 */       return asType(MethodType.genericMethodType(i)).invokeWithArguments(paramArrayOfObject);
/*      */     }
/*  598 */     MethodHandle localMethodHandle = localMethodType.invokers().varargsInvoker();
/*  599 */     return localMethodHandle.invokeExact(this, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public Object invokeWithArguments(List<?> paramList)
/*      */     throws Throwable
/*      */   {
/*  621 */     return invokeWithArguments(paramList.toArray());
/*      */   }
/*      */ 
/*      */   public MethodHandle asType(MethodType paramMethodType)
/*      */   {
/*  723 */     if (!this.type.isConvertibleTo(paramMethodType)) {
/*  724 */       throw new WrongMethodTypeException("cannot convert " + this + " to " + paramMethodType);
/*      */     }
/*  726 */     return convertArguments(paramMethodType);
/*      */   }
/*      */ 
/*      */   public MethodHandle asSpreader(Class<?> paramClass, int paramInt)
/*      */   {
/*  817 */     asSpreaderChecks(paramClass, paramInt);
/*  818 */     int i = this.type.parameterCount() - paramInt;
/*  819 */     return MethodHandleImpl.makeSpreadArguments(this, paramClass, i, paramInt);
/*      */   }
/*      */ 
/*      */   private void asSpreaderChecks(Class<?> paramClass, int paramInt) {
/*  823 */     spreadArrayChecks(paramClass, paramInt);
/*  824 */     int i = type().parameterCount();
/*  825 */     if ((i < paramInt) || (paramInt < 0))
/*  826 */       throw MethodHandleStatics.newIllegalArgumentException("bad spread array length");
/*  827 */     if ((paramClass != [Ljava.lang.Object.class) && (paramInt != 0)) {
/*  828 */       int j = 0;
/*  829 */       Class localClass = paramClass.getComponentType();
/*  830 */       for (int k = i - paramInt; k < i; k++) {
/*  831 */         if (!MethodType.canConvert(localClass, type().parameterType(k))) {
/*  832 */           j = 1;
/*  833 */           break;
/*      */         }
/*      */       }
/*  836 */       if (j != 0) {
/*  837 */         ArrayList localArrayList = new ArrayList(type().parameterList());
/*  838 */         for (int m = i - paramInt; m < i; m++) {
/*  839 */           localArrayList.set(m, localClass);
/*      */         }
/*      */ 
/*  842 */         asType(MethodType.methodType(type().returnType(), localArrayList));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void spreadArrayChecks(Class<?> paramClass, int paramInt) {
/*  848 */     Class localClass = paramClass.getComponentType();
/*  849 */     if (localClass == null)
/*  850 */       throw MethodHandleStatics.newIllegalArgumentException("not an array type", paramClass);
/*  851 */     if ((paramInt & 0x7F) != paramInt) {
/*  852 */       if ((paramInt & 0xFF) != paramInt)
/*  853 */         throw MethodHandleStatics.newIllegalArgumentException("array length is not legal", Integer.valueOf(paramInt));
/*  854 */       assert (paramInt >= 128);
/*  855 */       if ((localClass == Long.TYPE) || (localClass == Double.TYPE))
/*      */       {
/*  857 */         throw MethodHandleStatics.newIllegalArgumentException("array length is not legal for long[] or double[]", Integer.valueOf(paramInt));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public MethodHandle asCollector(Class<?> paramClass, int paramInt)
/*      */   {
/*  931 */     asCollectorChecks(paramClass, paramInt);
/*  932 */     int i = type().parameterCount() - 1;
/*  933 */     MethodHandle localMethodHandle1 = this;
/*  934 */     if (paramClass != type().parameterType(i))
/*  935 */       localMethodHandle1 = convertArguments(type().changeParameterType(i, paramClass));
/*  936 */     MethodHandle localMethodHandle2 = ValueConversions.varargsArray(paramClass, paramInt);
/*  937 */     return MethodHandles.collectArguments(localMethodHandle1, i, localMethodHandle2);
/*      */   }
/*      */ 
/*      */   private boolean asCollectorChecks(Class<?> paramClass, int paramInt)
/*      */   {
/*  942 */     spreadArrayChecks(paramClass, paramInt);
/*  943 */     int i = type().parameterCount();
/*  944 */     if (i != 0) {
/*  945 */       Class localClass = type().parameterType(i - 1);
/*  946 */       if (localClass == paramClass) return true;
/*  947 */       if (localClass.isAssignableFrom(paramClass)) return false;
/*      */     }
/*  949 */     throw MethodHandleStatics.newIllegalArgumentException("array type not assignable to trailing argument", this, paramClass);
/*      */   }
/*      */ 
/*      */   public MethodHandle asVarargsCollector(Class<?> paramClass)
/*      */   {
/* 1103 */     Class localClass = paramClass.getComponentType();
/* 1104 */     boolean bool = asCollectorChecks(paramClass, 0);
/* 1105 */     if ((isVarargsCollector()) && (bool))
/* 1106 */       return this;
/* 1107 */     return MethodHandleImpl.makeVarargsCollector(this, paramClass);
/*      */   }
/*      */ 
/*      */   public boolean isVarargsCollector()
/*      */   {
/* 1126 */     return false;
/*      */   }
/*      */ 
/*      */   public MethodHandle asFixedArity()
/*      */   {
/* 1173 */     assert (!isVarargsCollector());
/* 1174 */     return this;
/*      */   }
/*      */ 
/*      */   public MethodHandle bindTo(Object paramObject)
/*      */   {
/* 1207 */     MethodType localMethodType = type();
/*      */     Class localClass;
/* 1208 */     if ((localMethodType.parameterCount() == 0) || ((localClass = localMethodType.parameterType(0)).isPrimitive()))
/*      */     {
/* 1210 */       throw MethodHandleStatics.newIllegalArgumentException("no leading reference parameter", paramObject);
/* 1211 */     }paramObject = localClass.cast(paramObject);
/* 1212 */     return bindReceiver(paramObject);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1232 */     if (MethodHandleStatics.DEBUG_METHOD_HANDLE_NAMES) return debugString();
/* 1233 */     return standardString();
/*      */   }
/*      */   String standardString() {
/* 1236 */     return "MethodHandle" + this.type;
/*      */   }
/*      */   String debugString() {
/* 1239 */     return standardString() + "/LF=" + internalForm() + internalProperties();
/*      */   }
/*      */ 
/*      */   MethodHandle setVarargs(MemberName paramMemberName)
/*      */     throws IllegalAccessException
/*      */   {
/* 1250 */     if (!paramMemberName.isVarargs()) return this;
/* 1251 */     int i = type().parameterCount();
/* 1252 */     if (i != 0) {
/* 1253 */       Class localClass = type().parameterType(i - 1);
/* 1254 */       if (localClass.isArray()) {
/* 1255 */         return MethodHandleImpl.makeVarargsCollector(this, localClass);
/*      */       }
/*      */     }
/* 1258 */     throw paramMemberName.makeAccessException("cannot make variable arity", null);
/*      */   }
/*      */ 
/*      */   MethodHandle viewAsType(MethodType paramMethodType)
/*      */   {
/* 1263 */     return MethodHandleImpl.makePairwiseConvert(this, paramMethodType, 0);
/*      */   }
/*      */ 
/*      */   LambdaForm internalForm()
/*      */   {
/* 1270 */     return this.form;
/*      */   }
/*      */ 
/*      */   MemberName internalMemberName()
/*      */   {
/* 1275 */     return null;
/*      */   }
/*      */ 
/*      */   Object internalValues()
/*      */   {
/* 1280 */     return null;
/*      */   }
/*      */ 
/*      */   Object internalProperties()
/*      */   {
/* 1286 */     return "";
/*      */   }
/*      */ 
/*      */   MethodHandle convertArguments(MethodType paramMethodType)
/*      */   {
/* 1295 */     return MethodHandleImpl.makePairwiseConvert(this, paramMethodType, 1);
/*      */   }
/*      */ 
/*      */   MethodHandle bindArgument(int paramInt, char paramChar, Object paramObject)
/*      */   {
/* 1301 */     return rebind().bindArgument(paramInt, paramChar, paramObject);
/*      */   }
/*      */ 
/*      */   MethodHandle bindReceiver(Object paramObject)
/*      */   {
/* 1307 */     return bindArgument(0, 'L', paramObject);
/*      */   }
/*      */ 
/*      */   MethodHandle bindImmediate(int paramInt, char paramChar, Object paramObject)
/*      */   {
/* 1318 */     assert ((paramInt == 0) && (paramChar == 'L') && ((paramObject instanceof Unsafe)));
/* 1319 */     MethodType localMethodType = this.type.dropParameterTypes(paramInt, paramInt + 1);
/* 1320 */     LambdaForm localLambdaForm = this.form.bindImmediate(paramInt + 1, paramChar, paramObject);
/* 1321 */     return copyWith(localMethodType, localLambdaForm);
/*      */   }
/*      */ 
/*      */   MethodHandle copyWith(MethodType paramMethodType, LambdaForm paramLambdaForm)
/*      */   {
/* 1326 */     throw new InternalError("copyWith: " + getClass());
/*      */   }
/*      */ 
/*      */   MethodHandle dropArguments(MethodType paramMethodType, int paramInt1, int paramInt2)
/*      */   {
/* 1332 */     return rebind().dropArguments(paramMethodType, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   MethodHandle permuteArguments(MethodType paramMethodType, int[] paramArrayOfInt)
/*      */   {
/* 1338 */     return rebind().permuteArguments(paramMethodType, paramArrayOfInt);
/*      */   }
/*      */ 
/*      */   MethodHandle rebind()
/*      */   {
/* 1344 */     MethodType localMethodType = type();
/* 1345 */     LambdaForm localLambdaForm = reinvokerForm(localMethodType.basicType());
/*      */ 
/* 1347 */     return BoundMethodHandle.bindSingle(localMethodType, localLambdaForm, this);
/*      */   }
/*      */ 
/*      */   MethodHandle reinvokerTarget()
/*      */   {
/* 1352 */     throw new InternalError("not a reinvoker MH: " + getClass().getName() + ": " + this);
/*      */   }
/*      */ 
/*      */   static LambdaForm reinvokerForm(MethodType paramMethodType)
/*      */   {
/* 1359 */     paramMethodType = paramMethodType.basicType();
/* 1360 */     LambdaForm localLambdaForm = paramMethodType.form().cachedLambdaForm(8);
/* 1361 */     if (localLambdaForm != null) return localLambdaForm;
/* 1362 */     MethodHandle localMethodHandle = MethodHandles.basicInvoker(paramMethodType);
/*      */ 
/* 1365 */     int i = 1 + paramMethodType.parameterCount();
/* 1366 */     int j = i;
/* 1367 */     int k = j++;
/* 1368 */     int m = j++;
/* 1369 */     LambdaForm.Name[] arrayOfName = LambdaForm.arguments(j - i, paramMethodType.invokerType());
/* 1370 */     arrayOfName[k] = new LambdaForm.Name(NF_reinvokerTarget, new Object[] { arrayOfName[0] });
/* 1371 */     Object[] arrayOfObject = Arrays.copyOfRange(arrayOfName, 0, i, [Ljava.lang.Object.class);
/* 1372 */     arrayOfObject[0] = arrayOfName[k];
/* 1373 */     arrayOfName[m] = new LambdaForm.Name(localMethodHandle, arrayOfObject);
/* 1374 */     return paramMethodType.form().setCachedLambdaForm(8, new LambdaForm("BMH.reinvoke", i, arrayOfName));
/*      */   }
/*      */ 
/*      */   void updateForm(LambdaForm paramLambdaForm)
/*      */   {
/* 1397 */     if (this.form == paramLambdaForm) return;
/*      */ 
/* 1399 */     MethodHandleStatics.UNSAFE.putObject(this, FORM_OFFSET, paramLambdaForm);
/* 1400 */     this.form.prepare();
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  410 */     MethodHandleImpl.initStatics();
/*      */     try
/*      */     {
/* 1380 */       NF_reinvokerTarget = new LambdaForm.NamedFunction(MethodHandle.class.getDeclaredMethod("reinvokerTarget", new Class[0]));
/*      */     }
/*      */     catch (ReflectiveOperationException localReflectiveOperationException1) {
/* 1383 */       throw MethodHandleStatics.newInternalError(localReflectiveOperationException1);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1406 */       FORM_OFFSET = MethodHandleStatics.UNSAFE.objectFieldOffset(MethodHandle.class.getDeclaredField("form"));
/*      */     } catch (ReflectiveOperationException localReflectiveOperationException2) {
/* 1408 */       throw MethodHandleStatics.newInternalError(localReflectiveOperationException2);
/*      */     }
/*      */   }
/*      */ 
/*      */   @Target({java.lang.annotation.ElementType.METHOD})
/*      */   @Retention(RetentionPolicy.RUNTIME)
/*      */   static @interface PolymorphicSignature
/*      */   {
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.MethodHandle
 * JD-Core Version:    0.6.2
 */