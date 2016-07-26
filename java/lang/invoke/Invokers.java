/*     */
package java.lang.invoke;
/*     */ 
/*     */

import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import sun.invoke.empty.Empty;

/*     */
/*     */ class Invokers
/*     */ {
    /*     */   private final MethodType targetType;
    /*     */   private MethodHandle exactInvoker;
    /*     */   private MethodHandle erasedInvoker;
    /*     */ MethodHandle erasedInvokerWithDrops;
    /*     */   private MethodHandle generalInvoker;
    /*     */   private MethodHandle varargsInvoker;
    /*     */   private final MethodHandle[] spreadInvokers;
    /*     */   private MethodHandle uninitializedCallSite;
    /*     */   private static MethodHandle THROW_UCS;
    /*     */   private static final int GENERIC_INVOKER_SLOP = 2;
    /*     */   private static final LambdaForm.NamedFunction NF_checkExactType;
    /*     */   private static final LambdaForm.NamedFunction NF_checkGenericType;
    /*     */   private static final LambdaForm.NamedFunction NF_asType;
    /*     */   private static final LambdaForm.NamedFunction NF_getCallSiteTarget;

    /*     */
/*     */   Invokers(MethodType paramMethodType)
/*     */ {
/*  70 */
        this.targetType = paramMethodType;
/*  71 */
        this.spreadInvokers = new MethodHandle[paramMethodType.parameterCount() + 1];
/*     */
    }

    /*     */
/*     */   MethodHandle exactInvoker() {
/*  75 */
        Object localObject = this.exactInvoker;
/*  76 */
        if (localObject != null) return localObject;
/*  77 */
        MethodType localMethodType1 = this.targetType;
/*  78 */
        MethodType localMethodType2 = localMethodType1.invokerType();
/*     */
        LambdaForm localLambdaForm;
/*  81 */
        if (localMethodType1.parameterSlotCount() <= 252) {
/*  82 */
            localLambdaForm = invokeForm(localMethodType1, false, 10);
/*  83 */
            localObject = BoundMethodHandle.bindSingle(localMethodType2, localLambdaForm, localMethodType1);
/*     */
        }
/*     */
        else
/*     */ {
/*  87 */
            localLambdaForm = invokeForm(localMethodType1, true, 10);
/*  88 */
            localObject = SimpleMethodHandle.make(localMethodType2, localLambdaForm);
/*     */
        }
/*  90 */
        assert (checkInvoker((MethodHandle) localObject));
/*  91 */
        this.exactInvoker = ((MethodHandle) localObject);
/*  92 */
        return localObject;
/*     */
    }

    /*     */
/*     */   MethodHandle generalInvoker() {
/*  96 */
        Object localObject = this.generalInvoker;
/*  97 */
        if (localObject != null) return localObject;
/*  98 */
        MethodType localMethodType1 = this.targetType;
/*  99 */
        MethodType localMethodType2 = localMethodType1.invokerType();
/*     */
        LambdaForm localLambdaForm;
/* 103 */
        if (localMethodType1.parameterSlotCount() <= 251) {
/* 104 */
            prepareForGenericCall(localMethodType1);
/* 105 */
            localLambdaForm = invokeForm(localMethodType1, false, 12);
/* 106 */
            localObject = BoundMethodHandle.bindSingle(localMethodType2, localLambdaForm, localMethodType1);
/*     */
        }
/*     */
        else
/*     */ {
/* 110 */
            localLambdaForm = invokeForm(localMethodType1, true, 12);
/* 111 */
            localObject = SimpleMethodHandle.make(localMethodType2, localLambdaForm);
/*     */
        }
/* 113 */
        assert (checkInvoker((MethodHandle) localObject));
/* 114 */
        this.generalInvoker = ((MethodHandle) localObject);
/* 115 */
        return localObject;
/*     */
    }

    /*     */
/*     */   MethodHandle makeBasicInvoker() {
/* 119 */
        DirectMethodHandle localDirectMethodHandle = DirectMethodHandle.make(invokeBasicMethod(this.targetType));
/* 120 */
        assert (this.targetType == this.targetType.basicType());
/*     */ 
/* 122 */
        assert (checkInvoker(localDirectMethodHandle));
/* 123 */
        return localDirectMethodHandle;
/*     */
    }

    /*     */
/*     */
    static MemberName invokeBasicMethod(MethodType paramMethodType) {
/* 127 */
        paramMethodType = paramMethodType.basicType();
/* 128 */
        String str = "invokeBasic";
/*     */
        try
/*     */ {
/* 131 */
            return MethodHandles.Lookup.IMPL_LOOKUP.resolveOrFail((byte) 5, MethodHandle.class, str, paramMethodType);
/*     */
        } catch (ReflectiveOperationException localReflectiveOperationException) {
/* 133 */
            throw MethodHandleStatics.newInternalError("JVM cannot find invoker for " + paramMethodType, localReflectiveOperationException);
/*     */
        }
/*     */
    }

    /*     */
/*     */
    private boolean checkInvoker(MethodHandle paramMethodHandle)
/*     */ {
/* 139 */
        if ((!$assertionsDisabled) && (!this.targetType.invokerType().equals(paramMethodHandle.type())))
            throw new AssertionError(Arrays.asList(new Object[]{this.targetType, this.targetType.invokerType(), paramMethodHandle}));
/* 140 */
        assert ((paramMethodHandle.internalMemberName() == null) || (paramMethodHandle.internalMemberName().getMethodType().equals(this.targetType)));
/*     */ 
/* 142 */
        assert (!paramMethodHandle.isVarargsCollector());
/* 143 */
        return true;
/*     */
    }

    /*     */
/*     */   MethodHandle erasedInvoker()
/*     */ {
/* 148 */
        MethodHandle localMethodHandle1 = exactInvoker();
/* 149 */
        MethodHandle localMethodHandle2 = this.erasedInvoker;
/* 150 */
        if (localMethodHandle2 != null) return localMethodHandle2;
/* 151 */
        MethodType localMethodType = this.targetType.erase();
/* 152 */
        localMethodHandle2 = localMethodHandle1.asType(localMethodType.invokerType());
/* 153 */
        this.erasedInvoker = localMethodHandle2;
/* 154 */
        return localMethodHandle2;
/*     */
    }

    /*     */
/*     */   MethodHandle spreadInvoker(int paramInt) {
/* 158 */
        MethodHandle localMethodHandle1 = this.spreadInvokers[paramInt];
/* 159 */
        if (localMethodHandle1 != null) return localMethodHandle1;
/* 160 */
        int i = this.targetType.parameterCount() - paramInt;
/* 161 */
        MethodType localMethodType = this.targetType.replaceParameterTypes(paramInt, this.targetType.parameterCount(), new Class[]{[Ljava.lang.Object.class})
        ;
/*     */
        MethodHandle localMethodHandle2;
/* 163 */
        if (this.targetType.parameterSlotCount() <= 253)
/*     */ {
/* 166 */
            localMethodHandle2 = generalInvoker();
/* 167 */
            localMethodHandle1 = localMethodHandle2.asSpreader([Ljava.lang.Object.class, i);
/*     */
        }
/*     */
        else
/*     */ {
/* 172 */
            localMethodHandle2 = MethodHandles.exactInvoker(localMethodType);
/*     */
            try
/*     */ {
/* 175 */
                localMethodHandle3 = MethodHandles.Lookup.IMPL_LOOKUP.findVirtual(MethodHandle.class, "asSpreader", MethodType.methodType(MethodHandle.class, Class.class, new Class[]{Integer.TYPE}));
/*     */
            }
/*     */ catch (ReflectiveOperationException localReflectiveOperationException)
/*     */ {
/* 179 */
                throw MethodHandleStatics.newInternalError(localReflectiveOperationException);
/*     */
            }
/* 181 */
            MethodHandle localMethodHandle3 = MethodHandles.insertArguments(localMethodHandle3, 1, new Object[]{[Ljava.lang.Object.class, Integer.valueOf(i)})
            ;
/* 182 */
            localMethodHandle1 = MethodHandles.filterArgument(localMethodHandle2, 0, localMethodHandle3);
/*     */
        }
/* 184 */
        assert (localMethodHandle1.type().equals(localMethodType.invokerType()));
/* 185 */
        this.spreadInvokers[paramInt] = localMethodHandle1;
/* 186 */
        return localMethodHandle1;
/*     */
    }

    /*     */
/*     */   MethodHandle varargsInvoker() {
/* 190 */
        MethodHandle localMethodHandle = this.varargsInvoker;
/* 191 */
        if (localMethodHandle != null) return localMethodHandle;
/* 192 */
        localMethodHandle = spreadInvoker(0).asType(MethodType.genericMethodType(0, true).invokerType());
/* 193 */
        this.varargsInvoker = localMethodHandle;
/* 194 */
        return localMethodHandle;
/*     */
    }

    /*     */
/*     */   MethodHandle uninitializedCallSite()
/*     */ {
/* 200 */
        MethodHandle localMethodHandle = this.uninitializedCallSite;
/* 201 */
        if (localMethodHandle != null) return localMethodHandle;
/* 202 */
        if (this.targetType.parameterCount() > 0) {
/* 203 */
            MethodType localMethodType = this.targetType.dropParameterTypes(0, this.targetType.parameterCount());
/* 204 */
            Invokers localInvokers = localMethodType.invokers();
/* 205 */
            localMethodHandle = MethodHandles.dropArguments(localInvokers.uninitializedCallSite(), 0, this.targetType.parameterList());
/*     */ 
/* 207 */
            assert (localMethodHandle.type().equals(this.targetType));
/* 208 */
            this.uninitializedCallSite = localMethodHandle;
/* 209 */
            return localMethodHandle;
/*     */
        }
/* 211 */
        localMethodHandle = THROW_UCS;
/* 212 */
        if (localMethodHandle == null) {
/*     */
            try {
/* 214 */
                THROW_UCS = localMethodHandle = MethodHandles.Lookup.IMPL_LOOKUP.findStatic(CallSite.class, "uninitializedCallSite", MethodType.methodType(Empty.class));
/*     */
            }
/*     */ catch (ReflectiveOperationException localReflectiveOperationException)
/*     */ {
/* 218 */
                throw MethodHandleStatics.newInternalError(localReflectiveOperationException);
/*     */
            }
/*     */
        }
/* 221 */
        localMethodHandle = MethodHandles.explicitCastArguments(localMethodHandle, MethodType.methodType(this.targetType.returnType()));
/* 222 */
        localMethodHandle = localMethodHandle.dropArguments(this.targetType, 0, this.targetType.parameterCount());
/* 223 */
        assert (localMethodHandle.type().equals(this.targetType));
/* 224 */
        this.uninitializedCallSite = localMethodHandle;
/* 225 */
        return localMethodHandle;
/*     */
    }

    /*     */
/*     */
    public String toString() {
/* 229 */
        return "Invokers" + this.targetType;
/*     */
    }

    /*     */
/*     */
    static MemberName exactInvokerMethod(MethodType paramMethodType, Object[] paramArrayOfObject)
/*     */ {
/*     */
        LambdaForm localLambdaForm;
/* 235 */
        if (paramMethodType.parameterSlotCount() <= 253) {
/* 236 */
            localLambdaForm = invokeForm(paramMethodType, false, 9);
/* 237 */
            paramArrayOfObject[0] = paramMethodType;
/*     */
        } else {
/* 239 */
            localLambdaForm = invokeForm(paramMethodType, true, 9);
/*     */
        }
/* 241 */
        return localLambdaForm.vmentry;
/*     */
    }

    /*     */
/*     */
    static MemberName genericInvokerMethod(MethodType paramMethodType, Object[] paramArrayOfObject)
/*     */ {
/*     */
        LambdaForm localLambdaForm;
/* 247 */
        if (paramMethodType.parameterSlotCount() <= 251) {
/* 248 */
            localLambdaForm = invokeForm(paramMethodType, false, 11);
/* 249 */
            paramArrayOfObject[0] = paramMethodType;
/* 250 */
            prepareForGenericCall(paramMethodType);
/*     */
        } else {
/* 252 */
            localLambdaForm = invokeForm(paramMethodType, true, 11);
/*     */
        }
/* 254 */
        return localLambdaForm.vmentry;
/*     */
    }

    /*     */
/*     */
    private static LambdaForm invokeForm(MethodType paramMethodType, boolean paramBoolean, int paramInt)
/*     */ {
/*     */
        int i;
/* 259 */
        if (!paramBoolean) {
/* 260 */
            paramMethodType = paramMethodType.basicType();
/* 261 */
            i = 1;
/*     */
        } else {
/* 263 */
            i = 0;
/*     */
        }
/*     */
        int j;
/*     */
        int k;
/*     */
        String str;
/* 267 */
        switch (paramInt) {
            case 9:
/* 268 */
                j = 1;
                k = 0;
                str = "invokeExact_MT";
                break;
/*     */
            case 10:
/* 269 */
                j = 0;
                k = 0;
                str = "exactInvoker";
                break;
/*     */
            case 11:
/* 270 */
                j = 1;
                k = 1;
                str = "invoke_MT";
                break;
/*     */
            case 12:
/* 271 */
                j = 0;
                k = 1;
                str = "invoker";
                break;
/*     */
            default:
/* 272 */
                throw new InternalError();
/*     */
        }
/*     */ 
/* 275 */
        if (i != 0) {
/* 276 */
            localLambdaForm = paramMethodType.form().cachedLambdaForm(paramInt);
/* 277 */
            if (localLambdaForm != null) return localLambdaForm;
/*     */ 
/*     */
        }
/*     */ 
/* 282 */
        int m = 0 + (j != 0 ? 0 : 1);
/* 283 */
        int n = m + 1;
/* 284 */
        int i1 = n + paramMethodType.parameterCount();
/* 285 */
        int i2 = i1 + ((j != 0) && (!paramBoolean) ? 1 : 0);
/* 286 */
        int i3 = i1;
/* 287 */
        int i4 = paramBoolean ? -1 : i3++;
/* 288 */
        int i5 = i3++;
/* 289 */
        int i6 = i3++;
/* 290 */
        MethodType localMethodType1 = paramMethodType.invokerType();
/* 291 */
        if (j != 0) {
/* 292 */
            if (!paramBoolean)
/* 293 */ localMethodType1 = localMethodType1.appendParameterTypes(new Class[]{MemberName.class});
/*     */
        }
/* 295 */
        else localMethodType1 = localMethodType1.invokerType();
/*     */ 
/* 297 */
        LambdaForm.Name[] arrayOfName = LambdaForm.arguments(i3 - i2, localMethodType1);
/*     */ 
/* 299 */
        if ((!$assertionsDisabled) && (arrayOfName.length != i3))
            throw new AssertionError(Arrays.asList(new Serializable[]{paramMethodType, Boolean.valueOf(paramBoolean), Integer.valueOf(paramInt), Integer.valueOf(i3), Integer.valueOf(arrayOfName.length)}));
/* 300 */
        if (i4 >= i2) {
/* 301 */
            assert (arrayOfName[i4] == null);
/* 302 */
            arrayOfName[i4] = BoundMethodHandle.getSpeciesData("L").getterName(arrayOfName[0], 0);
/*     */
        }
/*     */ 
/* 309 */
        LambdaForm.Name localName = paramBoolean ? paramMethodType : arrayOfName[i4];
/*     */
        Object[] arrayOfObject;
/*     */
        MethodType localMethodType2;
/* 310 */
        if (k == 0) {
/* 311 */
            arrayOfName[i5] = new LambdaForm.Name(NF_checkExactType, new Object[]{arrayOfName[m], localName});
/*     */ 
/* 313 */
            arrayOfObject = Arrays.copyOfRange(arrayOfName, m, i1,[Ljava.lang.Object.class);
/* 314 */
            localMethodType2 = paramMethodType;
/* 315 */
        } else if (paramBoolean) {
/* 316 */
            arrayOfName[i5] = new LambdaForm.Name(NF_asType, new Object[]{arrayOfName[m], localName});
/*     */ 
/* 320 */
            arrayOfObject = Arrays.copyOfRange(arrayOfName, m, i1,[Ljava.lang.Object.class);
/* 321 */
            arrayOfObject[0] = arrayOfName[i5];
/* 322 */
            localMethodType2 = paramMethodType;
/*     */
        } else {
/* 324 */
            arrayOfName[i5] = new LambdaForm.Name(NF_checkGenericType, new Object[]{arrayOfName[m], localName});
/*     */ 
/* 330 */
            arrayOfObject = Arrays.copyOfRange(arrayOfName, m, i1 + 2,[Ljava.lang.Object.class);
/*     */ 
/* 332 */
            System.arraycopy(arrayOfObject, 0, arrayOfObject, 2, arrayOfObject.length - 2);
/* 333 */
            arrayOfObject[0] = arrayOfName[i5];
/* 334 */
            arrayOfObject[1] = localName;
/* 335 */
            localMethodType2 = paramMethodType.insertParameterTypes(0, new Class[]{MethodType.class, MethodHandle.class});
/*     */
        }
/* 337 */
        arrayOfName[i6] = new LambdaForm.Name(invokeBasicMethod(localMethodType2), arrayOfObject);
/* 338 */
        LambdaForm localLambdaForm = new LambdaForm(str, i2, arrayOfName);
/* 339 */
        if (j != 0)
/* 340 */ localLambdaForm.compileToBytecode();
/* 341 */
        if (i != 0)
/* 342 */ localLambdaForm = paramMethodType.form().setCachedLambdaForm(paramInt, localLambdaForm);
/* 343 */
        return localLambdaForm;
/*     */
    }

    /*     */
/*     */
    static WrongMethodTypeException newWrongMethodTypeException(MethodType paramMethodType1, MethodType paramMethodType2)
/*     */ {
/* 350 */
        return new WrongMethodTypeException("expected " + paramMethodType2 + " but found " + paramMethodType1);
/*     */
    }

    /*     */
/*     */
    @ForceInline
/*     */ static void checkExactType(Object paramObject1, Object paramObject2)
/*     */ {
/* 357 */
        MethodHandle localMethodHandle = (MethodHandle) paramObject1;
/* 358 */
        MethodType localMethodType1 = (MethodType) paramObject2;
/* 359 */
        MethodType localMethodType2 = localMethodHandle.type();
/* 360 */
        if (localMethodType2 != localMethodType1)
/* 361 */ throw newWrongMethodTypeException(localMethodType1, localMethodType2);
/*     */
    }

    /*     */
/*     */
    @ForceInline
/*     */ static Object checkGenericType(Object paramObject1, Object paramObject2)
/*     */ {
/* 368 */
        MethodHandle localMethodHandle1 = (MethodHandle) paramObject1;
/* 369 */
        MethodType localMethodType = (MethodType) paramObject2;
/*     */ 
/* 371 */
        MethodHandle localMethodHandle2 = localMethodType.form().genericInvoker;
/* 372 */
        if (localMethodHandle2 != null) return localMethodHandle2;
/* 373 */
        return prepareForGenericCall(localMethodType);
/*     */
    }

    /*     */
/*     */
    static MethodHandle prepareForGenericCall(MethodType paramMethodType)
/*     */ {
/* 384 */
        MethodTypeForm localMethodTypeForm = paramMethodType.form();
/* 385 */
        MethodHandle localMethodHandle = localMethodTypeForm.genericInvoker;
/* 386 */
        if (localMethodHandle != null) return localMethodHandle;
/*     */
        try
/*     */ {
/* 389 */
            localMethodHandle = InvokeGeneric.generalInvokerOf(localMethodTypeForm.erasedType);
/* 390 */
            localMethodTypeForm.genericInvoker = localMethodHandle;
/* 391 */
            return localMethodHandle;
/*     */
        } catch (Exception localException) {
/* 393 */
            throw MethodHandleStatics.newInternalError("Exception while resolving inexact invoke", localException);
/*     */
        }
/*     */
    }

    /*     */
/*     */
    static MemberName linkToCallSiteMethod(MethodType paramMethodType) {
/* 398 */
        LambdaForm localLambdaForm = callSiteForm(paramMethodType);
/* 399 */
        return localLambdaForm.vmentry;
/*     */
    }

    /*     */
/*     */
    private static LambdaForm callSiteForm(MethodType paramMethodType) {
/* 403 */
        paramMethodType = paramMethodType.basicType();
/* 404 */
        LambdaForm localLambdaForm = paramMethodType.form().cachedLambdaForm(13);
/* 405 */
        if (localLambdaForm != null) return localLambdaForm;
/*     */ 
/* 409 */
        int i = 0 + paramMethodType.parameterCount();
/* 410 */
        int j = i + 1;
/* 411 */
        int k = i;
/* 412 */
        int m = k++;
/* 413 */
        int n = k++;
/* 414 */
        int i1 = k++;
/* 415 */
        MethodType localMethodType = paramMethodType.appendParameterTypes(new Class[]{CallSite.class});
/* 416 */
        LambdaForm.Name[] arrayOfName = LambdaForm.arguments(k - j, localMethodType);
/* 417 */
        assert (arrayOfName.length == k);
/* 418 */
        assert (arrayOfName[m] != null);
/* 419 */
        arrayOfName[n] = new LambdaForm.Name(NF_getCallSiteTarget, new Object[]{arrayOfName[m]});
/*     */ 
/* 422 */
        Object[] arrayOfObject = Arrays.copyOfRange(arrayOfName, 0, i + 1,[Ljava.lang.Object.class);
/*     */ 
/* 424 */
        System.arraycopy(arrayOfObject, 0, arrayOfObject, 1, arrayOfObject.length - 1);
/* 425 */
        arrayOfObject[0] = arrayOfName[n];
/* 426 */
        arrayOfName[i1] = new LambdaForm.Name(invokeBasicMethod(paramMethodType), arrayOfObject);
/* 427 */
        localLambdaForm = new LambdaForm("linkToCallSite", j, arrayOfName);
/* 428 */
        localLambdaForm.compileToBytecode();
/* 429 */
        localLambdaForm = paramMethodType.form().setCachedLambdaForm(13, localLambdaForm);
/* 430 */
        return localLambdaForm;
/*     */
    }

    /*     */
/*     */
    @ForceInline
/*     */ static Object getCallSiteTarget(Object paramObject)
/*     */ {
/* 437 */
        return ((CallSite) paramObject).getTarget();
/*     */
    }

    /*     */
/*     */   static
/*     */ {
/* 197 */
        THROW_UCS = null;
/*     */
        try
/*     */ {
/* 447 */
            NF_checkExactType = new LambdaForm.NamedFunction(Invokers.class.getDeclaredMethod("checkExactType", new Class[]{Object.class, Object.class}));
/*     */ 
/* 449 */
            NF_checkGenericType = new LambdaForm.NamedFunction(Invokers.class.getDeclaredMethod("checkGenericType", new Class[]{Object.class, Object.class}));
/*     */ 
/* 451 */
            NF_asType = new LambdaForm.NamedFunction(MethodHandle.class.getDeclaredMethod("asType", new Class[]{MethodType.class}));
/*     */ 
/* 453 */
            NF_getCallSiteTarget = new LambdaForm.NamedFunction(Invokers.class.getDeclaredMethod("getCallSiteTarget", new Class[]{Object.class}));
/*     */ 
/* 455 */
            NF_checkExactType.resolve();
/* 456 */
            NF_checkGenericType.resolve();
/* 457 */
            NF_getCallSiteTarget.resolve();
/*     */
        }
/*     */ catch (ReflectiveOperationException localReflectiveOperationException) {
/* 460 */
            throw MethodHandleStatics.newInternalError(localReflectiveOperationException);
/*     */
        }
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.Invokers
 * JD-Core Version:    0.6.2
 */