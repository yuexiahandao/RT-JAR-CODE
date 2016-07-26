/*     */
package java.lang.invoke;
/*     */ 
/*     */

import sun.invoke.util.ValueConversions;
/*     */ import sun.invoke.util.VerifyType;

/*     */
/*     */ class InvokeGeneric
/*     */ {
    /*     */   private final MethodType erasedCallerType;
    /*     */   private final MethodHandle initialInvoker;
    /*  77 */   private static final Class<?>[] EXTRA_ARGS = {MethodType.class, MethodHandle.class};
    /*     */   static final boolean USE_AS_TYPE_PATH = true;

    /*     */
/*     */   InvokeGeneric(MethodType paramMethodType)
/*     */     throws ReflectiveOperationException
/*     */ {
/*  46 */
        assert (paramMethodType.equals(paramMethodType.erase()));
/*  47 */
        this.erasedCallerType = paramMethodType;
/*  48 */
        this.initialInvoker = makeInitialInvoker();
/*     */ 
/*  51 */
        if (!$assertionsDisabled)
            if (!this.initialInvoker.type().equals(paramMethodType.insertParameterTypes(0, new Class[]{MethodType.class, MethodHandle.class})))
                throw new AssertionError(this.initialInvoker.type());
/*     */
    }

    /*     */
/*     */
    private static MethodHandles.Lookup lookup()
/*     */ {
/*  55 */
        return MethodHandles.Lookup.IMPL_LOOKUP;
/*     */
    }

    /*     */
/*     */
    static MethodHandle generalInvokerOf(MethodType paramMethodType) throws ReflectiveOperationException
/*     */ {
/*  60 */
        InvokeGeneric localInvokeGeneric = new InvokeGeneric(paramMethodType);
/*  61 */
        return localInvokeGeneric.initialInvoker;
/*     */
    }

    /*     */
/*     */
    private MethodHandle makeInitialInvoker() throws ReflectiveOperationException
/*     */ {
/*  66 */
        MethodHandle localMethodHandle1 = makePostDispatchInvoker();
/*     */
        MethodHandle localMethodHandle2;
/*  68 */
        if (returnConversionPossible()) {
/*  69 */
            localMethodHandle2 = MethodHandles.foldArguments(localMethodHandle1, dispatcher("dispatchWithConversion"));
/*     */
        }
/*     */
        else {
/*  72 */
            localMethodHandle2 = MethodHandles.foldArguments(localMethodHandle1, dispatcher("dispatch"));
/*     */
        }
/*  74 */
        return localMethodHandle2;
/*     */
    }

    /*     */
/*     */
    private MethodHandle makePostDispatchInvoker()
/*     */ {
/*  80 */
        MethodType localMethodType = this.erasedCallerType.insertParameterTypes(0, EXTRA_ARGS);
/*  81 */
        return localMethodType.invokers().exactInvoker();
/*     */
    }

    /*     */
    private MethodHandle dropDispatchArguments(MethodHandle paramMethodHandle) {
/*  84 */
        assert (paramMethodHandle.type().parameterType(0) == MethodHandle.class);
/*  85 */
        return MethodHandles.dropArguments(paramMethodHandle, 1, EXTRA_ARGS);
/*     */
    }

    /*     */
/*     */
    private MethodHandle dispatcher(String paramString) throws ReflectiveOperationException {
/*  89 */
        return lookup().bind(this, paramString, MethodType.methodType(MethodHandle.class, MethodType.class, new Class[]{MethodHandle.class}));
/*     */
    }

    /*     */
/*     */
    private MethodHandle dispatch(MethodType paramMethodType, MethodHandle paramMethodHandle)
/*     */ {
/* 101 */
        MethodType localMethodType = paramMethodHandle.type();
/*     */ 
/* 103 */
        MethodHandle localMethodHandle1 = paramMethodHandle.asType(paramMethodType);
/* 104 */
        localMethodType = paramMethodType;
/* 105 */
        Invokers localInvokers = localMethodType.invokers();
/* 106 */
        MethodHandle localMethodHandle2 = localInvokers.erasedInvokerWithDrops;
/* 107 */
        if (localMethodHandle2 == null) {
/* 108 */
            localInvokers.erasedInvokerWithDrops = (localMethodHandle2 = dropDispatchArguments(localInvokers.erasedInvoker()));
/*     */
        }
/*     */ 
/* 111 */
        return localMethodHandle2.bindTo(localMethodHandle1);
/*     */
    }

    /*     */
/*     */
    private MethodHandle dispatchWithConversion(MethodType paramMethodType, MethodHandle paramMethodHandle)
/*     */ {
/* 117 */
        MethodHandle localMethodHandle = dispatch(paramMethodType, paramMethodHandle);
/* 118 */
        if (returnConversionNeeded(paramMethodType, paramMethodHandle))
/* 119 */ localMethodHandle = addReturnConversion(localMethodHandle, paramMethodType.returnType());
/* 120 */
        return localMethodHandle;
/*     */
    }

    /*     */
/*     */
    private boolean returnConversionPossible() {
/* 124 */
        Class localClass = this.erasedCallerType.returnType();
/* 125 */
        return !localClass.isPrimitive();
/*     */
    }

    /*     */
    private boolean returnConversionNeeded(MethodType paramMethodType, MethodHandle paramMethodHandle) {
/* 128 */
        Class localClass1 = paramMethodType.returnType();
/* 129 */
        if (localClass1 == this.erasedCallerType.returnType())
/* 130 */ return false;
/* 131 */
        Class localClass2 = paramMethodHandle.type().returnType();
/* 132 */
        if ((VerifyType.isNullConversion(localClass2, localClass1)) && (!localClass1.isInterface()))
/* 133 */ return false;
/* 134 */
        return true;
/*     */
    }

    /*     */
/*     */
    private MethodHandle addReturnConversion(MethodHandle paramMethodHandle, Class<?> paramClass) {
/* 138 */
        MethodType localMethodType = paramMethodHandle.type();
/* 139 */
        MethodHandle localMethodHandle = ValueConversions.identity(paramClass);
/* 140 */
        localMethodHandle = localMethodHandle.asType(localMethodHandle.type().changeParameterType(0, localMethodType.returnType()));
/* 141 */
        paramMethodHandle = MethodHandles.filterReturnValue(paramMethodHandle, localMethodHandle);
/* 142 */
        return paramMethodHandle.asType(localMethodType);
/*     */
    }

    /*     */
/*     */
    public String toString() {
/* 146 */
        return "InvokeGeneric" + this.erasedCallerType;
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.InvokeGeneric
 * JD-Core Version:    0.6.2
 */