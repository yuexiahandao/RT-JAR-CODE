/*     */
package java.lang.invoke;
/*     */ 
/*     */

import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Array;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import sun.invoke.empty.Empty;
/*     */ import sun.invoke.util.ValueConversions;
/*     */ import sun.invoke.util.VerifyType;
/*     */ import sun.invoke.util.Wrapper;
/*     */ import sun.misc.Unsafe;
/*     */ import sun.reflect.CallerSensitive;
/*     */ import sun.reflect.Reflection;

/*     */
/*     */ abstract class MethodHandleImpl
/*     */ {
    /*     */   static MethodHandle SELECT_ALTERNATIVE;
    /*     */   static MethodHandle THROW_EXCEPTION;
    /*     */   static MethodHandle FAKE_METHOD_HANDLE_INVOKE;

    /*     */
/*     */
    static void initStatics()
/*     */ {
/*  53 */
        MemberName.Factory.INSTANCE.getClass();
/*     */
    }

    /*     */
/*     */
    static MethodHandle makeArrayElementAccessor(Class<?> paramClass, boolean paramBoolean) {
/*  57 */
        if (!paramClass.isArray())
/*  58 */ throw MethodHandleStatics.newIllegalArgumentException("not an array: " + paramClass);
/*  59 */
        MethodHandle localMethodHandle = ArrayAccessor.getAccessor(paramClass, paramBoolean);
/*  60 */
        MethodType localMethodType1 = localMethodHandle.type().erase();
/*  61 */
        MethodType localMethodType2 = localMethodType1.invokerType();
/*  62 */
        LambdaForm.Name[] arrayOfName1 = LambdaForm.arguments(1, localMethodType2);
/*  63 */
        LambdaForm.Name[] arrayOfName2 = (LambdaForm.Name[]) Arrays.copyOfRange(arrayOfName1, 1, 1 + localMethodType1.parameterCount());
/*  64 */
        arrayOfName1[(arrayOfName1.length - 1)] = new LambdaForm.Name(localMethodHandle.asType(localMethodType1), (Object[]) arrayOfName2);
/*  65 */
        LambdaForm localLambdaForm = new LambdaForm("getElement", localMethodType2.parameterCount(), arrayOfName1);
/*  66 */
        Object localObject = SimpleMethodHandle.make(localMethodType1, localLambdaForm);
/*  67 */
        if (ArrayAccessor.needCast(paramClass)) {
/*  68 */
            localObject = ((MethodHandle) localObject).bindTo(paramClass);
/*     */
        }
/*  70 */
        localObject = ((MethodHandle) localObject).asType(ArrayAccessor.correctType(paramClass, paramBoolean));
/*  71 */
        return localObject;
/*     */
    }

    /*     */
/*     */
    static MethodHandle makePairwiseConvert(MethodHandle paramMethodHandle, MethodType paramMethodType, int paramInt)
/*     */ {
/* 170 */
        assert ((paramInt >= 0) && (paramInt <= 2));
/* 171 */
        MethodType localMethodType1 = paramMethodHandle.type();
/* 172 */
        assert (localMethodType1.parameterCount() == paramMethodHandle.type().parameterCount());
/* 173 */
        if (paramMethodType == localMethodType1) {
/* 174 */
            return paramMethodHandle;
/*     */
        }
/*     */ 
/* 178 */
        int i = paramMethodType.parameterCount();
/* 179 */
        int j = 0;
/* 180 */
        boolean[] arrayOfBoolean = new boolean[1 + i];
/* 181 */
        for (int k = 0; k <= i; k++) {
/* 182 */
            Class localClass1 = k == i ? localMethodType1.returnType() : paramMethodType.parameterType(k);
/* 183 */
            Class localClass2 = k == i ? paramMethodType.returnType() : localMethodType1.parameterType(k);
/* 184 */
            if ((!VerifyType.isNullConversion(localClass1, localClass2)) || ((paramInt <= 1) && (localClass2.isInterface()) && (!localClass2.isAssignableFrom(localClass1))))
/*     */ {
/* 186 */
                arrayOfBoolean[k] = true;
/* 187 */
                j++;
/*     */
            }
/*     */
        }
/* 190 */
        k = arrayOfBoolean[i];
/*     */ 
/* 194 */
        int m = 1 + i;
/* 195 */
        int n = m + j + 1;
/* 196 */
        int i1 = k == 0 ? -1 : n - 1;
/* 197 */
        int i2 = (k == 0 ? n : i1) - 1;
/*     */ 
/* 200 */
        MethodType localMethodType2 = paramMethodType.basicType().invokerType();
/* 201 */
        LambdaForm.Name[] arrayOfName = LambdaForm.arguments(n - m, localMethodType2);
/*     */ 
/* 205 */
        Object[] arrayOfObject = new Object[0 + i];
/*     */ 
/* 207 */
        int i3 = m;
/*     */
        Class localClass3;
/*     */
        Object localObject2;
/*     */
        Object localObject3;
/*     */
        Object localObject4;
/*     */
        Object localObject5;
/* 208 */
        for (int i4 = 0; i4 < i; i4++) {
/* 209 */
            localClass3 = paramMethodType.parameterType(i4);
/* 210 */
            localObject2 = localMethodType1.parameterType(i4);
/*     */ 
/* 212 */
            if (arrayOfBoolean[i4] == 0)
/*     */ {
/* 214 */
                arrayOfObject[(0 + i4)] = arrayOfName[(1 + i4)];
/*     */
            }
/*     */
            else
/*     */ {
/* 219 */
                localObject3 = null;
/* 220 */
                if (localClass3.isPrimitive()) {
/* 221 */
                    if (((Class) localObject2).isPrimitive()) {
/* 222 */
                        localObject3 = ValueConversions.convertPrimitive(localClass3, (Class) localObject2);
/*     */
                    } else {
/* 224 */
                        localObject4 = Wrapper.forPrimitiveType(localClass3);
/* 225 */
                        localObject5 = ValueConversions.box((Wrapper) localObject4);
/* 226 */
                        if (localObject2 == ((Wrapper) localObject4).wrapperType())
/* 227 */ localObject3 = localObject5;
/*     */
                        else
/* 229 */
                            localObject3 = ((MethodHandle) localObject5).asType(MethodType.methodType((Class) localObject2, localClass3));
/*     */
                    }
/*     */
                }
/* 232 */
                else if (((Class) localObject2).isPrimitive())
/*     */ {
/* 234 */
                    localObject4 = Wrapper.forPrimitiveType((Class) localObject2);
/* 235 */
                    if ((paramInt == 0) || (VerifyType.isNullConversion(localClass3, ((Wrapper) localObject4).wrapperType()))) {
/* 236 */
                        localObject3 = ValueConversions.unbox((Class) localObject2);
/* 237 */
                    } else if ((localClass3 == Object.class) || (!Wrapper.isWrapperType(localClass3)))
/*     */ {
/* 241 */
                        localObject5 = paramInt == 1 ? ValueConversions.unbox((Class) localObject2) : ValueConversions.unboxCast((Class) localObject2);
/*     */ 
/* 244 */
                        localObject3 = localObject5;
/*     */
                    }
/*     */
                    else
/*     */ {
/* 248 */
                        localObject5 = Wrapper.forWrapperType(localClass3).primitiveType();
/* 249 */
                        MethodHandle localMethodHandle = ValueConversions.unbox((Class) localObject5);
/*     */ 
/* 251 */
                        localObject3 = localMethodHandle.asType(MethodType.methodType((Class) localObject2, localClass3));
/*     */
                    }
/*     */ 
/*     */
                }
/*     */
                else
/*     */ {
/* 258 */
                    localObject3 = ValueConversions.cast((Class) localObject2);
/*     */
                }
/*     */ 
/* 261 */
                localObject4 = new LambdaForm.Name((MethodHandle) localObject3, new Object[]{arrayOfName[(1 + i4)]});
/* 262 */
                assert (arrayOfName[i3] == null);
/* 263 */
                arrayOfName[(i3++)] = localObject4;
/* 264 */
                assert (arrayOfObject[(0 + i4)] == null);
/* 265 */
                arrayOfObject[(0 + i4)] = localObject4;
/*     */
            }
/*     */
        }
/*     */ 
/* 269 */
        assert (i3 == i2);
/* 270 */
        arrayOfName[i2] = new LambdaForm.Name(paramMethodHandle, arrayOfObject);
/*     */ 
/* 272 */
        if (i1 < 0) {
/* 273 */
            if ((!$assertionsDisabled) && (i2 != arrayOfName.length - 1)) throw new AssertionError();
/*     */
        }
/* 275 */
        else {
            localObject1 = paramMethodType.returnType();
/* 276 */
            localClass3 = localMethodType1.returnType();
/*     */ 
/* 278 */
            localObject3 = new Object[]{arrayOfName[i2]};
/* 279 */
            if (localClass3 == Void.TYPE)
/*     */ {
/* 281 */
                localObject4 = Wrapper.forBasicType((Class) localObject1).zero();
/* 282 */
                localObject2 = MethodHandles.constant((Class) localObject1, localObject4);
/* 283 */
                localObject3 = new Object[0];
/*     */
            } else {
/* 285 */
                localObject4 = MethodHandles.identity((Class) localObject1);
/* 286 */
                localObject5 = ((MethodHandle) localObject4).type().changeParameterType(0, localClass3);
/* 287 */
                localObject2 = makePairwiseConvert((MethodHandle) localObject4, (MethodType) localObject5, paramInt);
/*     */
            }
/* 289 */
            assert (arrayOfName[i1] == null);
/* 290 */
            arrayOfName[i1] = new LambdaForm.Name((MethodHandle) localObject2, (Object[]) localObject3);
/* 291 */
            assert (i1 == arrayOfName.length - 1);
/*     */
        }
/*     */ 
/* 294 */
        Object localObject1 = new LambdaForm("convert", localMethodType2.parameterCount(), arrayOfName);
/* 295 */
        return SimpleMethodHandle.make(paramMethodType, (LambdaForm) localObject1);
/*     */
    }

    /*     */
/*     */
    static MethodHandle makeReferenceIdentity(Class<?> paramClass) {
/* 299 */
        MethodType localMethodType = MethodType.genericMethodType(1).invokerType();
/* 300 */
        LambdaForm.Name[] arrayOfName = LambdaForm.arguments(1, localMethodType);
/* 301 */
        arrayOfName[(arrayOfName.length - 1)] = new LambdaForm.Name(ValueConversions.identity(), new Object[]{arrayOfName[1]});
/* 302 */
        LambdaForm localLambdaForm = new LambdaForm("identity", localMethodType.parameterCount(), arrayOfName);
/* 303 */
        return SimpleMethodHandle.make(MethodType.methodType(paramClass, paramClass), localLambdaForm);
/*     */
    }

    /*     */
/*     */
    static MethodHandle makeVarargsCollector(MethodHandle paramMethodHandle, Class<?> paramClass) {
/* 307 */
        MethodType localMethodType = paramMethodHandle.type();
/* 308 */
        int i = localMethodType.parameterCount() - 1;
/* 309 */
        if (localMethodType.parameterType(i) != paramClass)
/* 310 */ paramMethodHandle = paramMethodHandle.asType(localMethodType.changeParameterType(i, paramClass));
/* 311 */
        paramMethodHandle = paramMethodHandle.asFixedArity();
/* 312 */
        return new AsVarargsCollector(paramMethodHandle, paramMethodHandle.type(), paramClass);
/*     */
    }

    /*     */
/*     */
    static MethodHandle makeSpreadArguments(MethodHandle paramMethodHandle, Class<?> paramClass, int paramInt1, int paramInt2)
/*     */ {
/* 410 */
        MethodType localMethodType1 = paramMethodHandle.type();
/*     */ 
/* 412 */
        for (int i = 0; i < paramInt2; i++) {
/* 413 */
            localObject = VerifyType.spreadArgElementType(paramClass, i);
/* 414 */
            if (localObject == null) localObject = Object.class;
/* 415 */
            localMethodType1 = localMethodType1.changeParameterType(paramInt1 + i, (Class) localObject);
/*     */
        }
/* 417 */
        paramMethodHandle = paramMethodHandle.asType(localMethodType1);
/*     */ 
/* 419 */
        MethodType localMethodType2 = localMethodType1.replaceParameterTypes(paramInt1, paramInt1 + paramInt2, new Class[]{paramClass});
/*     */ 
/* 422 */
        Object localObject = localMethodType2.invokerType();
/* 423 */
        LambdaForm.Name[] arrayOfName1 = LambdaForm.arguments(paramInt2 + 2, (MethodType) localObject);
/* 424 */
        int j = ((MethodType) localObject).parameterCount();
/* 425 */
        int[] arrayOfInt = new int[localMethodType1.parameterCount()];
/*     */ 
/* 427 */
        int k = 0;
        for (int m = 1; k < localMethodType1.parameterCount() + 1; m++) {
/* 428 */
            Class localClass = ((MethodType) localObject).parameterType(k);
/* 429 */
            if (k == paramInt1)
/*     */ {
/* 431 */
                MethodHandle localMethodHandle = MethodHandles.arrayElementGetter(paramClass);
/* 432 */
                LambdaForm.Name localName = arrayOfName1[m];
/* 433 */
                arrayOfName1[(j++)] = new LambdaForm.Name(Lazy.NF_checkSpreadArgument, new Object[]{localName, Integer.valueOf(paramInt2)});
/* 434 */
                for (int i1 = 0; i1 < paramInt2; i1++) {
/* 435 */
                    arrayOfInt[k] = j;
/* 436 */
                    arrayOfName1[(j++)] = new LambdaForm.Name(localMethodHandle, new Object[]{localName, Integer.valueOf(i1)});
/*     */ 
/* 434 */
                    k++;
/*     */
                }
/*     */ 
/*     */
            }
/* 438 */
            else if (k < arrayOfInt.length) {
/* 439 */
                arrayOfInt[k] = m;
/*     */
            }
/* 427 */
            k++;
/*     */
        }
/*     */ 
/* 442 */
        assert (j == arrayOfName1.length - 1);
/*     */ 
/* 445 */
        LambdaForm.Name[] arrayOfName2 = new LambdaForm.Name[localMethodType1.parameterCount()];
/* 446 */
        for (m = 0; m < localMethodType1.parameterCount(); m++) {
/* 447 */
            int n = arrayOfInt[m];
/* 448 */
            arrayOfName2[m] = arrayOfName1[n];
/*     */
        }
/* 450 */
        arrayOfName1[(arrayOfName1.length - 1)] = new LambdaForm.Name(paramMethodHandle, (Object[]) arrayOfName2);
/*     */ 
/* 452 */
        LambdaForm localLambdaForm = new LambdaForm("spread", ((MethodType) localObject).parameterCount(), arrayOfName1);
/* 453 */
        return SimpleMethodHandle.make(localMethodType2, localLambdaForm);
/*     */
    }

    /*     */
/*     */
    static void checkSpreadArgument(Object paramObject, int paramInt) {
/* 457 */
        if (paramObject == null) {
/* 458 */
            if (paramInt != 0) ;
/*     */
        }
/*     */
        else
/*     */ {
/*     */
            int i;
/* 459 */
            if ((paramObject instanceof Object[])) {
/* 460 */
                i = ((Object[]) paramObject).length;
/* 461 */
                if (i == paramInt) return;
/*     */
            }
/* 463 */
            else {
                i = Array.getLength(paramObject);
/* 464 */
                if (i == paramInt) return;
/*     */
            }
/*     */
        }
/* 467 */
        throw MethodHandleStatics.newIllegalArgumentException("array is not of length " + paramInt);
/*     */
    }

    /*     */
/*     */
    static MethodHandle makeCollectArguments(MethodHandle paramMethodHandle1, MethodHandle paramMethodHandle2, int paramInt, boolean paramBoolean)
/*     */ {
/* 490 */
        MethodType localMethodType1 = paramMethodHandle1.type();
/* 491 */
        MethodType localMethodType2 = paramMethodHandle2.type();
/* 492 */
        int i = localMethodType2.parameterCount();
/* 493 */
        Class localClass = localMethodType2.returnType();
/* 494 */
        int j = localClass == Void.TYPE ? 0 : 1;
/* 495 */
        MethodType localMethodType3 = localMethodType1.dropParameterTypes(paramInt, paramInt + j);
/*     */ 
/* 497 */
        if (!paramBoolean) {
/* 498 */
            localMethodType3 = localMethodType3.insertParameterTypes(paramInt, localMethodType2.parameterList());
/*     */
        }
/*     */ 
/* 505 */
        MethodType localMethodType4 = localMethodType3.invokerType();
/* 506 */
        LambdaForm.Name[] arrayOfName1 = LambdaForm.arguments(2, localMethodType4);
/* 507 */
        int k = arrayOfName1.length - 2;
/* 508 */
        int m = arrayOfName1.length - 1;
/*     */ 
/* 510 */
        LambdaForm.Name[] arrayOfName2 = (LambdaForm.Name[]) Arrays.copyOfRange(arrayOfName1, 1 + paramInt, 1 + paramInt + i);
/* 511 */
        arrayOfName1[k] = new LambdaForm.Name(paramMethodHandle2, (Object[]) arrayOfName2);
/*     */ 
/* 516 */
        LambdaForm.Name[] arrayOfName3 = new LambdaForm.Name[localMethodType1.parameterCount()];
/* 517 */
        int n = 1;
/* 518 */
        int i1 = 0;
/* 519 */
        int i2 = paramInt;
/* 520 */
        System.arraycopy(arrayOfName1, n, arrayOfName3, i1, i2);
/* 521 */
        n += i2;
/* 522 */
        i1 += i2;
/* 523 */
        if (localClass != Void.TYPE) {
/* 524 */
            arrayOfName3[(i1++)] = arrayOfName1[k];
/*     */
        }
/* 526 */
        i2 = i;
/* 527 */
        if (paramBoolean) {
/* 528 */
            System.arraycopy(arrayOfName1, n, arrayOfName3, i1, i2);
/* 529 */
            i1 += i2;
/*     */
        }
/* 531 */
        n += i2;
/* 532 */
        i2 = arrayOfName3.length - i1;
/* 533 */
        System.arraycopy(arrayOfName1, n, arrayOfName3, i1, i2);
/* 534 */
        assert (n + i2 == k);
/* 535 */
        arrayOfName1[m] = new LambdaForm.Name(paramMethodHandle1, (Object[]) arrayOfName3);
/*     */ 
/* 537 */
        LambdaForm localLambdaForm = new LambdaForm("collect", localMethodType4.parameterCount(), arrayOfName1);
/* 538 */
        return SimpleMethodHandle.make(localMethodType3, localLambdaForm);
/*     */
    }

    /*     */
/*     */
    static MethodHandle selectAlternative(boolean paramBoolean, MethodHandle paramMethodHandle1, MethodHandle paramMethodHandle2)
/*     */ {
/* 543 */
        return paramBoolean ? paramMethodHandle1 : paramMethodHandle2;
/*     */
    }

    /*     */
/*     */
    static MethodHandle selectAlternative()
/*     */ {
/* 548 */
        if (SELECT_ALTERNATIVE != null) return SELECT_ALTERNATIVE;
        try
/*     */ {
/* 550 */
            SELECT_ALTERNATIVE = MethodHandles.Lookup.IMPL_LOOKUP.findStatic(MethodHandleImpl.class, "selectAlternative", MethodType.methodType(MethodHandle.class, Boolean.TYPE, new Class[]{MethodHandle.class, MethodHandle.class}));
/*     */
        }
/*     */ catch (ReflectiveOperationException localReflectiveOperationException)
/*     */ {
/* 554 */
            throw new RuntimeException(localReflectiveOperationException);
/*     */
        }
/* 556 */
        return SELECT_ALTERNATIVE;
/*     */
    }

    /*     */
/*     */
    static MethodHandle makeGuardWithTest(MethodHandle paramMethodHandle1, MethodHandle paramMethodHandle2, MethodHandle paramMethodHandle3)
/*     */ {
/* 563 */
        MethodType localMethodType1 = paramMethodHandle2.type().basicType();
/* 564 */
        MethodHandle localMethodHandle = MethodHandles.basicInvoker(localMethodType1);
/* 565 */
        int i = localMethodType1.parameterCount();
/* 566 */
        int j = 3;
/* 567 */
        MethodType localMethodType2 = localMethodType1.invokerType();
/* 568 */
        LambdaForm.Name[] arrayOfName = LambdaForm.arguments(j, localMethodType2);
/*     */ 
/* 570 */
        Object[] arrayOfObject1 = Arrays.copyOfRange(arrayOfName, 1, 1 + i,[Ljava.lang.Object.class);
/* 571 */
        Object[] arrayOfObject2 = Arrays.copyOfRange(arrayOfName, 0, 1 + i,[Ljava.lang.Object.class);
/*     */ 
/* 574 */
        arrayOfName[(i + 1)] = new LambdaForm.Name(paramMethodHandle1, arrayOfObject1);
/*     */ 
/* 577 */
        Object[] arrayOfObject3 = {arrayOfName[(i + 1)], paramMethodHandle2, paramMethodHandle3};
/* 578 */
        arrayOfName[(i + 2)] = new LambdaForm.Name(selectAlternative(), arrayOfObject3);
/* 579 */
        arrayOfObject2[0] = arrayOfName[(i + 2)];
/*     */ 
/* 582 */
        arrayOfName[(i + 3)] = new LambdaForm.Name(new LambdaForm.NamedFunction(localMethodHandle), arrayOfObject2);
/*     */ 
/* 584 */
        LambdaForm localLambdaForm = new LambdaForm("guard", localMethodType2.parameterCount(), arrayOfName);
/* 585 */
        return SimpleMethodHandle.make(paramMethodHandle2.type(), localLambdaForm);
/*     */
    }

    /*     */
/*     */
    static MethodHandle makeGuardWithCatch(MethodHandle paramMethodHandle1, Class<? extends Throwable> paramClass, MethodHandle paramMethodHandle2)
/*     */ {
/* 723 */
        MethodType localMethodType1 = paramMethodHandle1.type();
/* 724 */
        MethodType localMethodType2 = paramMethodHandle2.type();
/* 725 */
        int i = localMethodType1.parameterCount();
/* 726 */
        if (i < GuardWithCatch.INVOKES.length) {
/* 727 */
            localObject1 = localMethodType1.generic();
/* 728 */
            localObject2 = ((MethodType) localObject1).insertParameterTypes(0, new Class[]{Throwable.class});
/*     */ 
/* 730 */
            localObject3 = makePairwiseConvert(paramMethodHandle1, (MethodType) localObject1, 2);
/* 731 */
            localMethodHandle1 = makePairwiseConvert(paramMethodHandle2, (MethodType) localObject2, 2);
/* 732 */
            localObject4 = new GuardWithCatch((MethodHandle) localObject3, paramClass, localMethodHandle1);
/* 733 */
            if ((localObject3 == null) || (localMethodHandle1 == null)) throw new InternalError();
/* 734 */
            MethodHandle localMethodHandle2 = GuardWithCatch.INVOKES[i].bindReceiver(localObject4);
/* 735 */
            return makePairwiseConvert(localMethodHandle2, localMethodType1, 2);
/*     */
        }
/* 737 */
        Object localObject1 = makeSpreadArguments(paramMethodHandle1,[Ljava.lang.Object.class, 0, i);
/* 738 */
        paramMethodHandle2 = paramMethodHandle2.asType(localMethodType2.changeParameterType(0, Throwable.class));
/* 739 */
        Object localObject2 = makeSpreadArguments(paramMethodHandle2,[Ljava.lang.Object.class, 1, i);
/* 740 */
        Object localObject3 = new GuardWithCatch((MethodHandle) localObject1, paramClass, (MethodHandle) localObject2);
/* 741 */
        if ((localObject1 == null) || (localObject2 == null)) throw new InternalError();
/* 742 */
        MethodHandle localMethodHandle1 = GuardWithCatch.VARARGS_INVOKE.bindReceiver(localObject3);
/* 743 */
        Object localObject4 = makeCollectArguments(localMethodHandle1, ValueConversions.varargsArray(i), 0, false);
/* 744 */
        return makePairwiseConvert((MethodHandle) localObject4, localMethodType1, 2);
/*     */
    }

    /*     */
/*     */
    static MethodHandle throwException(MethodType paramMethodType)
/*     */ {
/* 750 */
        assert (Throwable.class.isAssignableFrom(paramMethodType.parameterType(0)));
/* 751 */
        int i = paramMethodType.parameterCount();
/* 752 */
        if (i > 1) {
/* 753 */
            return throwException(paramMethodType.dropParameterTypes(1, i)).dropArguments(paramMethodType, 1, i - 1);
/*     */
        }
/* 755 */
        return makePairwiseConvert(throwException(), paramMethodType, 2);
/*     */
    }

    /*     */
/*     */
    static MethodHandle throwException()
/*     */ {
/* 760 */
        MethodHandle localMethodHandle = THROW_EXCEPTION;
/* 761 */
        if (localMethodHandle != null) return localMethodHandle;
        try
/*     */ {
/* 763 */
            localMethodHandle = MethodHandles.Lookup.IMPL_LOOKUP.findStatic(MethodHandleImpl.class, "throwException", MethodType.methodType(Empty.class, Throwable.class));
/*     */
        }
/*     */ catch (ReflectiveOperationException localReflectiveOperationException)
/*     */ {
/* 767 */
            throw new RuntimeException(localReflectiveOperationException);
/*     */
        }
/* 769 */
        THROW_EXCEPTION = localMethodHandle;
/* 770 */
        return localMethodHandle;
/*     */
    }

    /* 772 */
    static <T extends Throwable> Empty throwException(T paramT) throws Throwable {
        throw paramT;
    }

    /*     */
/*     */ 
/*     */
    static MethodHandle fakeMethodHandleInvoke(MemberName paramMemberName)
/*     */ {
/* 777 */
        MethodType localMethodType = paramMemberName.getInvocationType();
/* 778 */
        assert (localMethodType.equals(MethodType.methodType(Object.class,[Ljava.lang.Object.class)));
/* 779 */
        MethodHandle localMethodHandle = FAKE_METHOD_HANDLE_INVOKE;
/* 780 */
        if (localMethodHandle != null) return localMethodHandle;
/* 781 */
        localMethodHandle = throwException(localMethodType.insertParameterTypes(0, new Class[]{UnsupportedOperationException.class}));
/* 782 */
        localMethodHandle = localMethodHandle.bindTo(new UnsupportedOperationException("cannot reflectively invoke MethodHandle"));
/* 783 */
        FAKE_METHOD_HANDLE_INVOKE = localMethodHandle;
/* 784 */
        return localMethodHandle;
/*     */
    }

    /*     */
/*     */
    static MethodHandle bindCaller(MethodHandle paramMethodHandle, Class<?> paramClass)
/*     */ {
/* 797 */
        return BindCaller.bindCaller(paramMethodHandle, paramClass);
/*     */
    }

    /*     */
/*     */   static final class ArrayAccessor
/*     */ {
        /*  76 */     static final HashMap<Class<?>, MethodHandle> GETTER_CACHE = new HashMap();
        /*  77 */     static final HashMap<Class<?>, MethodHandle> SETTER_CACHE = new HashMap();
        /*     */     static final boolean USE_WEAKLY_TYPED_ARRAY_ACCESSORS = false;

        /*     */
/*     */
        static int getElementI(int[] paramArrayOfInt, int paramInt)
/*     */ {
/*  79 */
            return paramArrayOfInt[paramInt];
        }

        /*  80 */
        static long getElementJ(long[] paramArrayOfLong, int paramInt) {
            return paramArrayOfLong[paramInt];
        }

        /*  81 */
        static float getElementF(float[] paramArrayOfFloat, int paramInt) {
            return paramArrayOfFloat[paramInt];
        }

        /*  82 */
        static double getElementD(double[] paramArrayOfDouble, int paramInt) {
            return paramArrayOfDouble[paramInt];
        }

        /*  83 */
        static boolean getElementZ(boolean[] paramArrayOfBoolean, int paramInt) {
            return paramArrayOfBoolean[paramInt];
        }

        /*  84 */
        static byte getElementB(byte[] paramArrayOfByte, int paramInt) {
            return paramArrayOfByte[paramInt];
        }

        /*  85 */
        static short getElementS(short[] paramArrayOfShort, int paramInt) {
            return paramArrayOfShort[paramInt];
        }

        /*  86 */
        static char getElementC(char[] paramArrayOfChar, int paramInt) {
            return paramArrayOfChar[paramInt];
        }

        /*  87 */
        static Object getElementL(Object[] paramArrayOfObject, int paramInt) {
            return paramArrayOfObject[paramInt];
        }

        /*     */
        static void setElementI(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
/*  89 */
            paramArrayOfInt[paramInt1] = paramInt2;
        }

        /*  90 */
        static void setElementJ(long[] paramArrayOfLong, int paramInt, long paramLong) {
            paramArrayOfLong[paramInt] = paramLong;
        }

        /*  91 */
        static void setElementF(float[] paramArrayOfFloat, int paramInt, float paramFloat) {
            paramArrayOfFloat[paramInt] = paramFloat;
        }

        /*  92 */
        static void setElementD(double[] paramArrayOfDouble, int paramInt, double paramDouble) {
            paramArrayOfDouble[paramInt] = paramDouble;
        }

        /*  93 */
        static void setElementZ(boolean[] paramArrayOfBoolean, int paramInt, boolean paramBoolean) {
            paramArrayOfBoolean[paramInt] = paramBoolean;
        }

        /*  94 */
        static void setElementB(byte[] paramArrayOfByte, int paramInt, byte paramByte) {
            paramArrayOfByte[paramInt] = paramByte;
        }

        /*  95 */
        static void setElementS(short[] paramArrayOfShort, int paramInt, short paramShort) {
            paramArrayOfShort[paramInt] = paramShort;
        }

        /*  96 */
        static void setElementC(char[] paramArrayOfChar, int paramInt, char paramChar) {
            paramArrayOfChar[paramInt] = paramChar;
        }

        /*  97 */
        static void setElementL(Object[] paramArrayOfObject, int paramInt, Object paramObject) {
            paramArrayOfObject[paramInt] = paramObject;
        }

        /*     */
        static Object getElementL(Class<?> paramClass, Object[] paramArrayOfObject, int paramInt) {
/*  99 */
            paramClass.cast(paramArrayOfObject);
            return paramArrayOfObject[paramInt];
        }

        /* 100 */
        static void setElementL(Class<?> paramClass, Object[] paramArrayOfObject, int paramInt, Object paramObject) {
            paramClass.cast(paramArrayOfObject);
            paramArrayOfObject[paramInt] = paramObject;
        }

        /*     */
/*     */
        static Object getElementL(Object paramObject, int paramInt) {
/* 103 */
            return getElementL((Object[]) paramObject, paramInt);
        }

        /* 104 */
        static void setElementL(Object paramObject1, int paramInt, Object paramObject2) {
            setElementL((Object[]) paramObject1, paramInt, paramObject2);
        }

        /* 105 */
        static Object getElementL(Object paramObject1, Object paramObject2, int paramInt) {
            return getElementL((Class) paramObject1, (Object[]) paramObject2, paramInt);
        }

        /* 106 */
        static void setElementL(Object paramObject1, Object paramObject2, int paramInt, Object paramObject3) {
            setElementL((Class) paramObject1, (Object[]) paramObject2, paramInt, paramObject3);
        }

        /*     */
/*     */
        static boolean needCast(Class<?> paramClass) {
/* 109 */
            Class localClass = paramClass.getComponentType();
/* 110 */
            return (!localClass.isPrimitive()) && (localClass != Object.class);
/*     */
        }

        /*     */
        static String name(Class<?> paramClass, boolean paramBoolean) {
/* 113 */
            Class localClass = paramClass.getComponentType();
/* 114 */
            if (localClass == null) throw new IllegalArgumentException();
/* 115 */
            return (!paramBoolean ? "getElement" : "setElement") + Wrapper.basicTypeChar(localClass);
/*     */
        }

        /*     */
/*     */
        static MethodType type(Class<?> paramClass, boolean paramBoolean) {
/* 119 */
            Class localClass1 = paramClass.getComponentType();
/* 120 */
            Object localObject = paramClass;
/* 121 */
            if (!localClass1.isPrimitive()) {
/* 122 */
                localObject =[Ljava.lang.Object.class;
/*     */
            }
/*     */ 
/* 126 */
            if (!needCast(paramClass)) {
/* 127 */
                return !paramBoolean ? MethodType.methodType(localClass1, (Class) localObject, new Class[]{Integer.TYPE}) : MethodType.methodType(Void.TYPE, (Class) localObject, new Class[]{Integer.TYPE, localClass1});
/*     */
            }
/*     */ 
/* 131 */
            Class localClass2 = Class.class;
/*     */ 
/* 134 */
            return !paramBoolean ? MethodType.methodType(Object.class, localClass2, new Class[]{localObject, Integer.TYPE}) : MethodType.methodType(Void.TYPE, localClass2, new Class[]{localObject, Integer.TYPE, Object.class});
/*     */
        }

        /*     */
/*     */
        static MethodType correctType(Class<?> paramClass, boolean paramBoolean)
/*     */ {
/* 140 */
            Class localClass = paramClass.getComponentType();
/* 141 */
            return !paramBoolean ? MethodType.methodType(localClass, paramClass, new Class[]{Integer.TYPE}) : MethodType.methodType(Void.TYPE, paramClass, new Class[]{Integer.TYPE, localClass});
/*     */
        }

        /*     */
/*     */
        static MethodHandle getAccessor(Class<?> paramClass, boolean paramBoolean)
/*     */ {
/* 146 */
            String str = name(paramClass, paramBoolean);
/* 147 */
            MethodType localMethodType = type(paramClass, paramBoolean);
/*     */
            try {
/* 149 */
                return MethodHandles.Lookup.IMPL_LOOKUP.findStatic(ArrayAccessor.class, str, localMethodType);
/*     */
            } catch (ReflectiveOperationException localReflectiveOperationException) {
/* 151 */
                throw MethodHandleStatics.uncaughtException(localReflectiveOperationException);
/*     */
            }
/*     */
        }
/*     */
    }

    /*     */
/*     */   static class AsVarargsCollector extends MethodHandle
/*     */ {
        /*     */     private final MethodHandle target;
        /*     */     private final Class<?> arrayType;
        /*     */     private MethodHandle cache;

        /*     */
/*     */     AsVarargsCollector(MethodHandle paramMethodHandle, MethodType paramMethodType, Class<?> paramClass)
/*     */ {
/* 321 */
            super(reinvokerForm(paramMethodType));
/* 322 */
            this.target = paramMethodHandle;
/* 323 */
            this.arrayType = paramClass;
/* 324 */
            this.cache = paramMethodHandle.asCollector(paramClass, 0);
/*     */
        }

        /*     */     MethodHandle reinvokerTarget() {
/* 327 */
            return this.target;
/*     */
        }

        /*     */
/*     */
        public boolean isVarargsCollector() {
/* 331 */
            return true;
/*     */
        }

        /*     */
/*     */
        public MethodHandle asFixedArity()
/*     */ {
/* 336 */
            return this.target;
/*     */
        }

        /*     */
/*     */
        public MethodHandle asType(MethodType paramMethodType)
/*     */ {
/* 341 */
            MethodType localMethodType = type();
/* 342 */
            int i = localMethodType.parameterCount() - 1;
/* 343 */
            int j = paramMethodType.parameterCount();
/* 344 */
            if ((j == i + 1) && (localMethodType.parameterType(i).isAssignableFrom(paramMethodType.parameterType(i))))
/*     */ {
/* 347 */
                return asFixedArity().asType(paramMethodType);
/*     */
            }
/*     */ 
/* 350 */
            if (this.cache.type().parameterCount() == j) {
/* 351 */
                return this.cache.asType(paramMethodType);
/* 353 */
            }
            int k = j - i;
/*     */
            MethodHandle localMethodHandle;
/*     */
            try {
/* 356 */
                localMethodHandle = asFixedArity().asCollector(this.arrayType, k);
/* 357 */
                if ((!$assertionsDisabled) && (localMethodHandle.type().parameterCount() != j))
                    throw new AssertionError("newArity=" + j + " but collector=" + localMethodHandle);
/*     */
            }
/* 359 */ catch (IllegalArgumentException localIllegalArgumentException) {
                throw new WrongMethodTypeException("cannot build collector", localIllegalArgumentException);
            }
/*     */ 
/* 361 */
            this.cache = localMethodHandle;
/* 362 */
            return localMethodHandle.asType(paramMethodType);
/*     */
        }

        /*     */
/*     */     MethodHandle setVarargs(MemberName paramMemberName)
/*     */ {
/* 367 */
            if (paramMemberName.isVarargs()) return this;
/* 368 */
            return asFixedArity();
/*     */
        }

        /*     */
/*     */     MethodHandle viewAsType(MethodType paramMethodType)
/*     */ {
/* 373 */
            MethodHandle localMethodHandle = super.viewAsType(paramMethodType);
/*     */ 
/* 375 */
            MethodType localMethodType = localMethodHandle.type();
/* 376 */
            int i = localMethodType.parameterCount();
/* 377 */
            return localMethodHandle.asVarargsCollector(localMethodType.parameterType(i - 1));
/*     */
        }

        /*     */
/*     */     MemberName internalMemberName()
/*     */ {
/* 382 */
            return asFixedArity().internalMemberName();
/*     */
        }

        /*     */
/*     */     MethodHandle bindArgument(int paramInt, char paramChar, Object paramObject)
/*     */ {
/* 388 */
            return asFixedArity().bindArgument(paramInt, paramChar, paramObject);
/*     */
        }

        /*     */
/*     */     MethodHandle bindReceiver(Object paramObject)
/*     */ {
/* 393 */
            return asFixedArity().bindReceiver(paramObject);
/*     */
        }

        /*     */
/*     */     MethodHandle dropArguments(MethodType paramMethodType, int paramInt1, int paramInt2)
/*     */ {
/* 398 */
            return asFixedArity().dropArguments(paramMethodType, paramInt1, paramInt2);
/*     */
        }

        /*     */
/*     */     MethodHandle permuteArguments(MethodType paramMethodType, int[] paramArrayOfInt)
/*     */ {
/* 403 */
            return asFixedArity().permuteArguments(paramMethodType, paramArrayOfInt);
/*     */
        }
/*     */
    }

    /*     */
/*     */   private static class BindCaller
/*     */ {
        /*     */     private static final Unsafe UNSAFE;
        /*     */     private static ClassValue<MethodHandle> CV_makeInjectedInvoker;
        /*     */     private static final MethodHandle MH_checkCallerClass;
        /* 926 */     private static final byte[] T_BYTES = (byte[]) localObject[0];

        /*     */
/*     */
        static MethodHandle bindCaller(MethodHandle paramMethodHandle, Class<?> paramClass)
/*     */ {
/* 806 */
            if ((paramClass == null) || (paramClass.isArray()) || (paramClass.isPrimitive()) || (paramClass.getName().startsWith("java.")) || (paramClass.getName().startsWith("sun.")))
/*     */ {
/* 811 */
                throw new InternalError();
/*     */
            }
/*     */ 
/* 814 */
            MethodHandle localMethodHandle1 = prepareForInvoker(paramMethodHandle);
/*     */ 
/* 816 */
            MethodHandle localMethodHandle2 = (MethodHandle) CV_makeInjectedInvoker.get(paramClass);
/* 817 */
            return restoreToType(localMethodHandle2.bindTo(localMethodHandle1), paramMethodHandle.type());
/*     */
        }

        /*     */
/*     */
        private static MethodHandle makeInjectedInvoker(Class<?> paramClass)
/*     */ {
/* 823 */
            Class localClass = UNSAFE.defineAnonymousClass(paramClass, T_BYTES, null);
/* 824 */
            if (paramClass.getClassLoader() != localClass.getClassLoader())
/* 825 */ throw new InternalError(paramClass.getName() + " (CL)");
/*     */
            try {
/* 827 */
                if (paramClass.getProtectionDomain() != localClass.getProtectionDomain())
/* 828 */ throw new InternalError(paramClass.getName() + " (PD)");
/*     */
            }
/*     */ catch (SecurityException localSecurityException)
/*     */ {
/*     */
            }
/*     */
            try {
/* 834 */
                MethodHandle localMethodHandle1 = MethodHandles.Lookup.IMPL_LOOKUP.findStatic(localClass, "init", MethodType.methodType(Void.TYPE));
/* 835 */
                localMethodHandle1.invokeExact();
/*     */
            } catch (Throwable localThrowable1) {
/* 837 */
                throw MethodHandleStatics.uncaughtException(localThrowable1);
/*     */
            }
/*     */
            MethodHandle localMethodHandle2;
/*     */
            try {
/* 841 */
                MethodType localMethodType = MethodType.methodType(Object.class, MethodHandle.class, new Class[]{[Ljava.lang.Object.class})
                ;
/* 842 */
                localMethodHandle2 = MethodHandles.Lookup.IMPL_LOOKUP.findStatic(localClass, "invoke_V", localMethodType);
/*     */
            } catch (ReflectiveOperationException localReflectiveOperationException) {
/* 844 */
                throw MethodHandleStatics.uncaughtException(localReflectiveOperationException);
/*     */
            }
/*     */
            try
/*     */ {
/* 848 */
                MethodHandle localMethodHandle3 = prepareForInvoker(MH_checkCallerClass);
/* 849 */
                Object localObject = localMethodHandle2.invokeExact(localMethodHandle3, new Object[]{paramClass, localClass});
/*     */
            } catch (Throwable localThrowable2) {
/* 851 */
                throw MethodHandleStatics.newInternalError(localThrowable2);
/*     */
            }
/* 853 */
            return localMethodHandle2;
/*     */
        }

        /*     */
/*     */
        private static MethodHandle prepareForInvoker(MethodHandle paramMethodHandle)
/*     */ {
/* 863 */
            paramMethodHandle = paramMethodHandle.asFixedArity();
/* 864 */
            MethodType localMethodType = paramMethodHandle.type();
/* 865 */
            int i = localMethodType.parameterCount();
/* 866 */
            MethodHandle localMethodHandle = paramMethodHandle.asType(localMethodType.generic());
/* 867 */
            localMethodHandle.internalForm().compileToBytecode();
/* 868 */
            localMethodHandle = localMethodHandle.asSpreader([Ljava.lang.Object.class, i);
/* 869 */
            localMethodHandle.internalForm().compileToBytecode();
/* 870 */
            return localMethodHandle;
/*     */
        }

        /*     */
/*     */
        private static MethodHandle restoreToType(MethodHandle paramMethodHandle, MethodType paramMethodType)
/*     */ {
/* 875 */
            return paramMethodHandle.asCollector([Ljava.lang.Object.class,paramMethodType.parameterCount()).
            asType(paramMethodType);
/*     */
        }

        /*     */
/*     */
        @CallerSensitive
/*     */ private static boolean checkCallerClass(Class<?> paramClass1, Class<?> paramClass2)
/*     */ {
/* 896 */
            Class localClass = Reflection.getCallerClass();
/* 897 */
            if ((localClass != paramClass1) && (localClass != paramClass2)) {
/* 898 */
                throw new InternalError("found " + localClass.getName() + ", expected " + paramClass1.getName() + (paramClass1 == paramClass2 ? "" : new StringBuilder().append(", or else ").append(paramClass2.getName()).toString()));
/*     */
            }
/* 900 */
            return true;
/*     */
        }

        /*     */
/*     */     static
/*     */ {
/* 820 */
            UNSAFE = Unsafe.getUnsafe();
/*     */ 
/* 855 */
            CV_makeInjectedInvoker = new ClassValue() {
                /*     */
                protected MethodHandle computeValue(Class<?> paramAnonymousClass) {
/* 857 */
                    return MethodHandleImpl.BindCaller.makeInjectedInvoker(paramAnonymousClass);
/*     */
                }
/*     */
            };
/* 880 */
            Object localObject = BindCaller.class;
/* 881 */
            assert (checkCallerClass((Class) localObject, (Class) localObject));
/*     */
            try {
/* 883 */
                MH_checkCallerClass = MethodHandles.Lookup.IMPL_LOOKUP.findStatic((Class) localObject, "checkCallerClass", MethodType.methodType(Boolean.TYPE, Class.class, new Class[]{Class.class}));
/*     */ 
/* 886 */
                if ((!$assertionsDisabled) && (!MH_checkCallerClass.invokeExact((Class) localObject, (Class) localObject)))
                    throw new AssertionError();
/*     */
            }
/* 888 */ catch (Throwable localThrowable) {
                throw MethodHandleStatics.newInternalError(localThrowable);
            }
/*     */ 
/*     */ 
/* 905 */
            localObject = new Object[]{null};
/* 906 */
            AccessController.doPrivileged(new PrivilegedAction() {
                /*     */
                public Void run() {
/*     */
                    try {
/* 909 */
                        MethodHandleImpl.BindCaller.T localT = MethodHandleImpl.BindCaller.T.class;
/* 910 */
                        String str1 = localT.getName();
/* 911 */
                        String str2 = str1.substring(str1.lastIndexOf('.') + 1) + ".class";
/* 912 */
                        URLConnection localURLConnection = localT.getResource(str2).openConnection();
/* 913 */
                        int i = localURLConnection.getContentLength();
/* 914 */
                        byte[] arrayOfByte = new byte[i];
/* 915 */
                        InputStream localInputStream = localURLConnection.getInputStream();
                        Object localObject1 = null;
/*     */
                        try {
                            int j = localInputStream.read(arrayOfByte);
/* 917 */
                            if (j != i) throw new IOException(str2);
/*     */
                        }
/*     */ catch (Throwable localThrowable2)
/*     */ {
/* 915 */
                            localObject1 = localThrowable2;
                            throw localThrowable2;
/*     */
                        }
/*     */ finally {
/* 918 */
                            if (localInputStream != null) if (localObject1 != null) try {
                                localInputStream.close();
                            } catch (Throwable localThrowable3) {
                                localObject1.addSuppressed(localThrowable3);
                            }
                            else localInputStream.close();
/*     */
                        }
/* 919 */
                        this.val$values[0] = arrayOfByte;
/*     */
                    } catch (IOException localIOException) {
/* 921 */
                        throw MethodHandleStatics.newInternalError(localIOException);
/*     */
                    }
/* 923 */
                    return null;
/*     */
                }
/*     */
            });
/*     */
        }

        /*     */
/*     */     private static class T {
            /*     */
            static void init() {
/*     */
            }

            /*     */
/*     */
            static Object invoke_V(MethodHandle paramMethodHandle, Object[] paramArrayOfObject) throws Throwable {
/* 933 */
                return paramMethodHandle.invokeExact(paramArrayOfObject);
/*     */
            }
/*     */
        }
/*     */
    }

    /*     */
/*     */   private static class GuardWithCatch
/*     */ {
        /*     */     private final MethodHandle target;
        /*     */     private final Class<? extends Throwable> exType;
        /*     */     private final MethodHandle catcher;
        /*     */     static final MethodHandle[] INVOKES;
        /*     */     static final MethodHandle VARARGS_INVOKE;

        /*     */
/*     */     GuardWithCatch(MethodHandle paramMethodHandle1, Class<? extends Throwable> paramClass, MethodHandle paramMethodHandle2)
/*     */ {
/* 594 */
            this.target = paramMethodHandle1;
/* 595 */
            this.exType = paramClass;
/* 596 */
            this.catcher = paramMethodHandle2;
/*     */
        }

        /*     */
        @LambdaForm.Hidden
/*     */ private Object invoke_V(Object[] paramArrayOfObject) throws Throwable {
/*     */
            try {
/* 601 */
                return this.target.invokeExact(paramArrayOfObject);
/*     */
            } catch (Throwable localThrowable) {
/* 603 */
                if (!this.exType.isInstance(localThrowable)) throw localThrowable;
/* 604 */
                return this.catcher.invokeExact(localThrowable, paramArrayOfObject);
/*     */
            }
/*     */
        }

        /*     */
/*     */
        @LambdaForm.Hidden
/*     */ private Object invoke_L0() throws Throwable {
/*     */
            try {
                return this.target.invokeExact();
/*     */
            } catch (Throwable localThrowable) {
/* 612 */
                if (!this.exType.isInstance(localThrowable)) throw localThrowable;
/* 613 */
                return this.catcher.invokeExact(localThrowable);
/*     */
            }
/*     */
        }

        /*     */
/*     */
        @LambdaForm.Hidden
/*     */ private Object invoke_L1(Object paramObject) throws Throwable {
/*     */
            try {
                return this.target.invokeExact(paramObject);
/*     */
            } catch (Throwable localThrowable) {
/* 621 */
                if (!this.exType.isInstance(localThrowable)) throw localThrowable;
/* 622 */
                return this.catcher.invokeExact(localThrowable, paramObject);
/*     */
            }
/*     */
        }

        /*     */
/*     */
        @LambdaForm.Hidden
/*     */ private Object invoke_L2(Object paramObject1, Object paramObject2) throws Throwable {
/*     */
            try {
                return this.target.invokeExact(paramObject1, paramObject2);
/*     */
            } catch (Throwable localThrowable) {
/* 630 */
                if (!this.exType.isInstance(localThrowable)) throw localThrowable;
/* 631 */
                return this.catcher.invokeExact(localThrowable, paramObject1, paramObject2);
/*     */
            }
/*     */
        }

        /*     */
/*     */
        @LambdaForm.Hidden
/*     */ private Object invoke_L3(Object paramObject1, Object paramObject2, Object paramObject3) throws Throwable {
/*     */
            try {
                return this.target.invokeExact(paramObject1, paramObject2, paramObject3);
/*     */
            } catch (Throwable localThrowable) {
/* 639 */
                if (!this.exType.isInstance(localThrowable)) throw localThrowable;
/* 640 */
                return this.catcher.invokeExact(localThrowable, paramObject1, paramObject2, paramObject3);
/*     */
            }
/*     */
        }

        /*     */
/*     */
        @LambdaForm.Hidden
/*     */ private Object invoke_L4(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4) throws Throwable {
/*     */
            try {
                return this.target.invokeExact(paramObject1, paramObject2, paramObject3, paramObject4);
/*     */
            } catch (Throwable localThrowable) {
/* 648 */
                if (!this.exType.isInstance(localThrowable)) throw localThrowable;
/* 649 */
                return this.catcher.invokeExact(localThrowable, paramObject1, paramObject2, paramObject3, paramObject4);
/*     */
            }
/*     */
        }

        /*     */
/*     */
        @LambdaForm.Hidden
/*     */ private Object invoke_L5(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5) throws Throwable {
/*     */
            try {
                return this.target.invokeExact(paramObject1, paramObject2, paramObject3, paramObject4, paramObject5);
/*     */
            } catch (Throwable localThrowable) {
/* 657 */
                if (!this.exType.isInstance(localThrowable)) throw localThrowable;
/* 658 */
                return this.catcher.invokeExact(localThrowable, paramObject1, paramObject2, paramObject3, paramObject4, paramObject5);
/*     */
            }
/*     */
        }

        /*     */
/*     */
        @LambdaForm.Hidden
/*     */ private Object invoke_L6(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6) throws Throwable {
/*     */
            try {
                return this.target.invokeExact(paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6);
/*     */
            } catch (Throwable localThrowable) {
/* 666 */
                if (!this.exType.isInstance(localThrowable)) throw localThrowable;
/* 667 */
                return this.catcher.invokeExact(localThrowable, paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6);
/*     */
            }
/*     */
        }

        /*     */
/*     */
        @LambdaForm.Hidden
/*     */ private Object invoke_L7(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7) throws Throwable {
/*     */
            try {
                return this.target.invokeExact(paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7);
/*     */
            } catch (Throwable localThrowable) {
/* 675 */
                if (!this.exType.isInstance(localThrowable)) throw localThrowable;
/* 676 */
                return this.catcher.invokeExact(localThrowable, paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7);
/*     */
            }
/*     */
        }

        /*     */
/*     */
        @LambdaForm.Hidden
/*     */ private Object invoke_L8(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8) throws Throwable {
/*     */
            try {
                return this.target.invokeExact(paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7, paramObject8);
/*     */
            } catch (Throwable localThrowable) {
/* 684 */
                if (!this.exType.isInstance(localThrowable)) throw localThrowable;
/* 685 */
                return this.catcher.invokeExact(localThrowable, paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7, paramObject8);
/*     */
            }
/*     */
        }

        /*     */
/* 689 */
        static MethodHandle[] makeInvokes() {
            ArrayList localArrayList = new ArrayList();
/* 690 */
            MethodHandles.Lookup localLookup = MethodHandles.Lookup.IMPL_LOOKUP;
/*     */
            while (true) {
/* 692 */
                int i = localArrayList.size();
/* 693 */
                String str = "invoke_L" + i;
/* 694 */
                MethodHandle localMethodHandle = null;
/*     */
                try {
/* 696 */
                    localMethodHandle = localLookup.findVirtual(GuardWithCatch.class, str, MethodType.genericMethodType(i));
/*     */
                } catch (ReflectiveOperationException localReflectiveOperationException) {
/*     */
                }
/* 699 */
                if (localMethodHandle == null) break;
/* 700 */
                localArrayList.add(localMethodHandle);
/*     */
            }
/* 702 */
            assert (localArrayList.size() == 9);
/* 703 */
            return (MethodHandle[]) localArrayList.toArray(new MethodHandle[0]);
        }

        /*     */     static {
/* 705 */
            INVOKES = makeInvokes();
/*     */
            try
/*     */ {
/* 711 */
                VARARGS_INVOKE = MethodHandles.Lookup.IMPL_LOOKUP.findVirtual(GuardWithCatch.class, "invoke_V", MethodType.genericMethodType(0, true));
/*     */
            } catch (ReflectiveOperationException localReflectiveOperationException) {
/* 713 */
                throw MethodHandleStatics.uncaughtException(localReflectiveOperationException);
/*     */
            }
/*     */
        }
/*     */
    }

    /*     */
/*     */   private static class Lazy
/*     */ {
        /*     */     static final LambdaForm.NamedFunction NF_checkSpreadArgument;

        /*     */
/*     */     static
/*     */ {
/*     */
            try
/*     */ {
/* 478 */
                NF_checkSpreadArgument = new LambdaForm.NamedFunction(MethodHandleImpl.class.getDeclaredMethod("checkSpreadArgument", new Class[]{Object.class, Integer.TYPE}));
/*     */ 
/* 480 */
                NF_checkSpreadArgument.resolve();
/*     */
            } catch (ReflectiveOperationException localReflectiveOperationException) {
/* 482 */
                throw MethodHandleStatics.newInternalError(localReflectiveOperationException);
/*     */
            }
/*     */
        }
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.MethodHandleImpl
 * JD-Core Version:    0.6.2
 */