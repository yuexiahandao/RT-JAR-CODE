/*     */
package java.lang.invoke;
/*     */ 
/*     */

import java.io.ObjectStreamClass;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.security.AccessController;
/*     */ import java.sql.DriverManager;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*     */ import java.util.concurrent.atomic.AtomicLongFieldUpdater;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ import java.util.logging.Logger;
/*     */ import javax.sql.rowset.serial.SerialJavaObject;
/*     */ import sun.misc.Unsafe;
/*     */ import sun.reflect.Reflection;

/*     */
/*     */ class MethodHandleNatives
/*     */ {
    /*     */   static final boolean COUNT_GWT;
    /*     */   private static String[] REFERENCE_KIND_NAME;

    /*     */
/*     */
    static native void init(MemberName paramMemberName, Object paramObject);

    /*     */
/*     */
    static native void expand(MemberName paramMemberName);

    /*     */
/*     */
    static native MemberName resolve(MemberName paramMemberName, Class<?> paramClass)
/*     */     throws LinkageError;

    /*     */
/*     */
    static native int getMembers(Class<?> paramClass1, String paramString1, String paramString2, int paramInt1, Class<?> paramClass2, int paramInt2, MemberName[] paramArrayOfMemberName);

    /*     */
/*     */
    static native long objectFieldOffset(MemberName paramMemberName);

    /*     */
/*     */
    static native long staticFieldOffset(MemberName paramMemberName);

    /*     */
/*     */
    static native Object staticFieldBase(MemberName paramMemberName);

    /*     */
/*     */
    static native Object getMemberVMInfo(MemberName paramMemberName);

    /*     */
/*     */
    static native int getConstant(int paramInt);

    /*     */
/*     */
    static native void setCallSiteTargetNormal(CallSite paramCallSite, MethodHandle paramMethodHandle);

    /*     */
/*     */
    static native void setCallSiteTargetVolatile(CallSite paramCallSite, MethodHandle paramMethodHandle);

    /*     */
/*     */
    private static native void registerNatives();

    /*     */
/*     */
    static boolean refKindIsValid(int paramInt)
/*     */ {
/* 193 */
        return (paramInt > 0) && (paramInt < 10);
/*     */
    }

    /*     */
    static boolean refKindIsField(byte paramByte) {
/* 196 */
        assert (refKindIsValid(paramByte));
/* 197 */
        return paramByte <= 4;
/*     */
    }

    /*     */
    static boolean refKindIsGetter(byte paramByte) {
/* 200 */
        assert (refKindIsValid(paramByte));
/* 201 */
        return paramByte <= 2;
/*     */
    }

    /*     */
    static boolean refKindIsSetter(byte paramByte) {
/* 204 */
        return (refKindIsField(paramByte)) && (!refKindIsGetter(paramByte));
/*     */
    }

    /*     */
    static boolean refKindIsMethod(byte paramByte) {
/* 207 */
        return (!refKindIsField(paramByte)) && (paramByte != 8);
/*     */
    }

    /*     */
    static boolean refKindHasReceiver(byte paramByte) {
/* 210 */
        assert (refKindIsValid(paramByte));
/* 211 */
        return (paramByte & 0x1) != 0;
/*     */
    }

    /*     */
    static boolean refKindIsStatic(byte paramByte) {
/* 214 */
        return (!refKindHasReceiver(paramByte)) && (paramByte != 8);
/*     */
    }

    /*     */
    static boolean refKindDoesDispatch(byte paramByte) {
/* 217 */
        assert (refKindIsValid(paramByte));
/* 218 */
        return (paramByte == 5) || (paramByte == 9);
/*     */
    }

    /*     */
/*     */
    static String refKindName(byte paramByte)
/*     */ {
/* 233 */
        assert (refKindIsValid(paramByte));
/* 234 */
        return REFERENCE_KIND_NAME[paramByte];
/*     */
    }

    /*     */
/*     */
    private static native int getNamedCon(int paramInt, Object[] paramArrayOfObject);

    /*     */
/*     */
    static boolean verifyConstants()
/*     */ {
/* 251 */
        Object[] arrayOfObject = {null};
/* 252 */
        for (int i = 0; ; i++) {
/* 253 */
            arrayOfObject[0] = null;
/* 254 */
            int j = getNamedCon(i, arrayOfObject);
/* 255 */
            if (arrayOfObject[0] == null) break;
/* 256 */
            String str1 = (String) arrayOfObject[0];
/*     */
            try {
/* 258 */
                Field localField = Constants.class.getDeclaredField(str1);
/* 259 */
                int k = localField.getInt(null);
/* 260 */
                if (k != j) {
/* 261 */
                    String str3 = str1 + ": JVM has " + j + " while Java has " + k;
/* 262 */
                    if (str1.equals("CONV_OP_LIMIT")) {
/* 263 */
                        System.err.println("warning: " + str3);
/*     */
                    }
/*     */
                    else
/* 266 */             throw new InternalError(str3); 
/*     */
                }
/*     */
            } catch (NoSuchFieldException | IllegalAccessException localNoSuchFieldException) {
                String str2 = str1 + ": JVM has " + j + " which Java does not define";
            }
/*     */ 
/*     */ 
/*     */
        }
/*     */ 
/* 274 */
        return true;
/*     */
    }

    /*     */
/*     */
    static MemberName linkCallSite(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object[] paramArrayOfObject)
/*     */ {
/* 291 */
        MethodHandle localMethodHandle = (MethodHandle) paramObject2;
/* 292 */
        Class localClass = (Class) paramObject1;
/* 293 */
        String str = paramObject3.toString().intern();
/* 294 */
        MethodType localMethodType = (MethodType) paramObject4;
/* 295 */
        paramArrayOfObject[0] = CallSite.makeSite(localMethodHandle, str, localMethodType, paramObject5, localClass);
/*     */ 
/* 300 */
        return Invokers.linkToCallSiteMethod(localMethodType);
/*     */
    }

    /*     */
/*     */
    static MethodType findMethodHandleType(Class<?> paramClass, Class<?>[] paramArrayOfClass)
/*     */ {
/* 307 */
        return MethodType.makeImpl(paramClass, paramArrayOfClass, true);
/*     */
    }

    /*     */
/*     */
    static MemberName linkMethod(Class<?> paramClass1, int paramInt, Class<?> paramClass2, String paramString, Object paramObject, Object[] paramArrayOfObject)
/*     */ {
/* 322 */
        if (!MethodHandleStatics.TRACE_METHOD_LINKAGE)
/* 323 */ return linkMethodImpl(paramClass1, paramInt, paramClass2, paramString, paramObject, paramArrayOfObject);
/* 324 */
        return linkMethodTracing(paramClass1, paramInt, paramClass2, paramString, paramObject, paramArrayOfObject);
/*     */
    }

    /*     */
/*     */
    static MemberName linkMethodImpl(Class<?> paramClass1, int paramInt, Class<?> paramClass2, String paramString, Object paramObject, Object[] paramArrayOfObject)
/*     */ {
/*     */
        try {
/* 330 */
            if ((paramClass2 == MethodHandle.class) && (paramInt == 5))
/* 331 */ switch (paramString) {
/*     */
                case "invoke":
/* 333 */
                    return Invokers.genericInvokerMethod(fixMethodType(paramClass1, paramObject), paramArrayOfObject);
/*     */
                case "invokeExact":
/* 335 */
                    return Invokers.exactInvokerMethod(fixMethodType(paramClass1, paramObject), paramArrayOfObject);
/*     */
            }
/*     */
        }
/*     */ catch (Throwable localThrowable) {
/* 339 */
            if ((localThrowable instanceof LinkageError)) {
/* 340 */
                throw ((LinkageError) localThrowable);
/*     */
            }
/* 342 */
            throw new LinkageError(localThrowable.getMessage(), localThrowable);
/*     */
        }
/* 344 */
        throw new LinkageError("no such method " + paramClass2.getName() + "." + paramString + paramObject);
/*     */
    }

    /*     */
    private static MethodType fixMethodType(Class<?> paramClass, Object paramObject) {
/* 347 */
        if ((paramObject instanceof MethodType)) {
/* 348 */
            return (MethodType) paramObject;
/*     */
        }
/* 350 */
        return MethodType.fromMethodDescriptorString((String) paramObject, paramClass.getClassLoader());
/*     */
    }

    /*     */
/*     */
    static MemberName linkMethodTracing(Class<?> paramClass1, int paramInt, Class<?> paramClass2, String paramString, Object paramObject, Object[] paramArrayOfObject)
/*     */ {
/* 356 */
        System.out.println("linkMethod " + paramClass2.getName() + "." + paramString + paramObject + "/" + Integer.toHexString(paramInt));
/*     */
        try
/*     */ {
/* 359 */
            MemberName localMemberName = linkMethodImpl(paramClass1, paramInt, paramClass2, paramString, paramObject, paramArrayOfObject);
/* 360 */
            System.out.println("linkMethod => " + localMemberName + " + " + paramArrayOfObject[0]);
/* 361 */
            return localMemberName;
/*     */
        } catch (Throwable localThrowable) {
/* 363 */
            System.out.println("linkMethod => throw " + localThrowable);
/* 364 */
            throw localThrowable;
/*     */
        }
/*     */
    }

    /*     */
/*     */
    static MethodHandle linkMethodHandleConstant(Class<?> paramClass1, int paramInt, Class<?> paramClass2, String paramString, Object paramObject)
/*     */ {
/*     */
        try
/*     */ {
/* 380 */
            MethodHandles.Lookup localLookup = MethodHandles.Lookup.IMPL_LOOKUP.in(paramClass1);
/* 381 */
            assert (refKindIsValid(paramInt));
/* 382 */
            return localLookup.linkMethodHandleConstant((byte) paramInt, paramClass2, paramString, paramObject);
/*     */
        } catch (ReflectiveOperationException localReflectiveOperationException) {
/* 384 */
            IncompatibleClassChangeError localIncompatibleClassChangeError = new IncompatibleClassChangeError();
/* 385 */
            localIncompatibleClassChangeError.initCause(localReflectiveOperationException);
/* 386 */
            throw localIncompatibleClassChangeError;
/*     */
        }
/*     */
    }

    /*     */
/*     */
    static boolean isCallerSensitive(MemberName paramMemberName)
/*     */ {
/* 396 */
        if (!paramMemberName.isMethod()) return false;
/*     */ 
/* 399 */
        return (isCallerSensitiveMethod(paramMemberName.getDeclaringClass(), paramMemberName.getName())) || (canBeCalledVirtual(paramMemberName));
/*     */
    }

    /*     */
/*     */
    private static boolean isCallerSensitiveMethod(Class<?> paramClass, String paramString)
/*     */ {
/* 406 */
        switch (paramString) {
/*     */
            case "doPrivileged":
/*     */
            case "doPrivilegedWithCombiner":
/* 409 */
                return paramClass == AccessController.class;
/*     */
            case "checkMemberAccess":
/* 411 */
                return paramClass == SecurityManager.class;
/*     */
            case "getUnsafe":
/* 413 */
                return paramClass == Unsafe.class;
/*     */
            case "lookup":
/* 415 */
                return paramClass == MethodHandles.class;
/*     */
            case "invoke":
/* 417 */
                return paramClass == Method.class;
/*     */
            case "get":
/*     */
            case "getBoolean":
/*     */
            case "getByte":
/*     */
            case "getChar":
/*     */
            case "getDouble":
/*     */
            case "getFloat":
/*     */
            case "getInt":
/*     */
            case "getLong":
/*     */
            case "getShort":
/*     */
            case "set":
/*     */
            case "setBoolean":
/*     */
            case "setByte":
/*     */
            case "setChar":
/*     */
            case "setDouble":
/*     */
            case "setFloat":
/*     */
            case "setInt":
/*     */
            case "setLong":
/*     */
            case "setShort":
/* 436 */
                return paramClass == Field.class;
/*     */
            case "newInstance":
/* 438 */
                if (paramClass == Constructor.class) return true;
/* 439 */
                if (paramClass == Class.class) return true;
/*     */
                break;
/*     */
            case "getFields":
/* 442 */
                return (paramClass == Class.class) || (paramClass == SerialJavaObject.class);
/*     */
            case "forName":
/*     */
            case "getClassLoader":
/*     */
            case "getClasses":
/*     */
            case "getConstructor":
/*     */
            case "getConstructors":
/*     */
            case "getDeclaredClasses":
/*     */
            case "getDeclaredConstructor":
/*     */
            case "getDeclaredConstructors":
/*     */
            case "getDeclaredField":
/*     */
            case "getDeclaredFields":
/*     */
            case "getDeclaredMethod":
/*     */
            case "getDeclaredMethods":
/*     */
            case "getDeclaringClass":
/*     */
            case "getEnclosingClass":
/*     */
            case "getEnclosingConstructor":
/*     */
            case "getEnclosingMethod":
/*     */
            case "getField":
/*     */
            case "getMethod":
/*     */
            case "getMethods":
/* 463 */
                return paramClass == Class.class;
/*     */
            case "deregisterDriver":
/*     */
            case "getConnection":
/*     */
            case "getDriver":
/*     */
            case "getDrivers":
/* 468 */
                return paramClass == DriverManager.class;
/*     */
            case "newUpdater":
/* 470 */
                if (paramClass == AtomicIntegerFieldUpdater.class) return true;
/* 471 */
                if (paramClass == AtomicLongFieldUpdater.class) return true;
/* 472 */
                if (paramClass == AtomicReferenceFieldUpdater.class) return true;
/*     */
                break;
/*     */
            case "getContextClassLoader":
/* 475 */
                return paramClass == Thread.class;
/*     */
            case "getPackage":
/*     */
            case "getPackages":
/* 478 */
                return paramClass == Package.class;
/*     */
            case "getParent":
/*     */
            case "getSystemClassLoader":
/* 481 */
                return paramClass == ClassLoader.class;
/*     */
            case "load":
/*     */
            case "loadLibrary":
/* 484 */
                if (paramClass == Runtime.class) return true;
/* 485 */
                if (paramClass == System.class) return true;
/*     */
                break;
/*     */
            case "getCallerClass":
/* 488 */
                if (paramClass == Reflection.class) return true;
/* 489 */
                if (paramClass == System.class) return true;
/*     */
                break;
/*     */
            case "getCallerClassLoader":
/* 492 */
                return paramClass == ClassLoader.class;
/*     */
            case "registerAsParallelCapable":
/* 494 */
                return paramClass == ClassLoader.class;
/*     */
            case "getInvocationHandler":
/*     */
            case "getProxyClass":
/*     */
            case "newProxyInstance":
/* 498 */
                return paramClass == Proxy.class;
/*     */
            case "asInterfaceInstance":
/* 500 */
                return paramClass == MethodHandleProxies.class;
/*     */
            case "clearCache":
/*     */
            case "getBundle":
/* 503 */
                return paramClass == ResourceBundle.class;
/*     */
            case "getType":
/* 505 */
                return paramClass == ObjectStreamField.class;
/*     */
            case "forClass":
/* 507 */
                return paramClass == ObjectStreamClass.class;
/*     */
            case "getLogger":
/* 509 */
                return paramClass == Logger.class;
/*     */
            case "getAnonymousLogger":
/* 511 */
                return paramClass == Logger.class;
/*     */
        }
/* 513 */
        return false;
/*     */
    }

    /*     */
/*     */
    private static boolean canBeCalledVirtual(MemberName paramMemberName)
/*     */ {
/* 518 */
        assert (paramMemberName.isInvocable());
/* 519 */
        Class localClass = paramMemberName.getDeclaringClass();
/* 520 */
        switch (paramMemberName.getName()) {
/*     */
            case "checkMemberAccess":
/* 522 */
                return canBeCalledVirtual(paramMemberName, SecurityManager.class);
/*     */
            case "getContextClassLoader":
/* 524 */
                return canBeCalledVirtual(paramMemberName, Thread.class);
/*     */
        }
/* 526 */
        return false;
/*     */
    }

    /*     */
/*     */
    static boolean canBeCalledVirtual(MemberName paramMemberName, Class<?> paramClass) {
/* 530 */
        Class localClass = paramMemberName.getDeclaringClass();
/* 531 */
        if (localClass == paramClass) return true;
/* 532 */
        if ((paramMemberName.isStatic()) || (paramMemberName.isPrivate())) return false;
/* 533 */
        return (paramClass.isAssignableFrom(localClass)) || (localClass.isInterface());
/*     */
    }

    /*     */
/*     */   static
/*     */ {
/*  77 */
        registerNatives();
/*  78 */
        COUNT_GWT = getConstant(4) != 0;
/*     */ 
/*  81 */
        MethodHandleImpl.initStatics();
/*     */ 
/* 228 */
        for (byte b = 1; b < 10; b = (byte) (b + 1)) {
/* 229 */
            if (!$assertionsDisabled) if (refKindHasReceiver(b) != ((1 << b & 0x2AA) != 0)) throw new AssertionError(b);
/*     */ 
/*     */ 
/*     */
        }
/*     */ 
/* 236 */
        REFERENCE_KIND_NAME = new String[]{null, "getField", "getStatic", "putField", "putStatic", "invokeVirtual", "invokeStatic", "invokeSpecial", "newInvokeSpecial", "invokeInterface"};
/*     */ 
/* 277 */
        assert (verifyConstants());
/*     */
    }

    /*     */
/*     */   static class Constants
/*     */ {
        /*     */     static final int GC_COUNT_GWT = 4;
        /*     */     static final int GC_LAMBDA_SUPPORT = 5;
        /*     */     static final int MN_IS_METHOD = 65536;
        /*     */     static final int MN_IS_CONSTRUCTOR = 131072;
        /*     */     static final int MN_IS_FIELD = 262144;
        /*     */     static final int MN_IS_TYPE = 524288;
        /*     */     static final int MN_CALLER_SENSITIVE = 1048576;
        /*     */     static final int MN_REFERENCE_KIND_SHIFT = 24;
        /*     */     static final int MN_REFERENCE_KIND_MASK = 15;
        /*     */     static final int MN_SEARCH_SUPERCLASSES = 1048576;
        /*     */     static final int MN_SEARCH_INTERFACES = 2097152;
        /*     */     static final int T_BOOLEAN = 4;
        /*     */     static final int T_CHAR = 5;
        /*     */     static final int T_FLOAT = 6;
        /*     */     static final int T_DOUBLE = 7;
        /*     */     static final int T_BYTE = 8;
        /*     */     static final int T_SHORT = 9;
        /*     */     static final int T_INT = 10;
        /*     */     static final int T_LONG = 11;
        /*     */     static final int T_OBJECT = 12;
        /*     */     static final int T_VOID = 14;
        /*     */     static final int T_ILLEGAL = 99;
        /*     */     static final byte CONSTANT_Utf8 = 1;
        /*     */     static final byte CONSTANT_Integer = 3;
        /*     */     static final byte CONSTANT_Float = 4;
        /*     */     static final byte CONSTANT_Long = 5;
        /*     */     static final byte CONSTANT_Double = 6;
        /*     */     static final byte CONSTANT_Class = 7;
        /*     */     static final byte CONSTANT_String = 8;
        /*     */     static final byte CONSTANT_Fieldref = 9;
        /*     */     static final byte CONSTANT_Methodref = 10;
        /*     */     static final byte CONSTANT_InterfaceMethodref = 11;
        /*     */     static final byte CONSTANT_NameAndType = 12;
        /*     */     static final byte CONSTANT_MethodHandle = 15;
        /*     */     static final byte CONSTANT_MethodType = 16;
        /*     */     static final byte CONSTANT_InvokeDynamic = 18;
        /*     */     static final byte CONSTANT_LIMIT = 19;
        /*     */     static final char ACC_PUBLIC = '\001';
        /*     */     static final char ACC_PRIVATE = '\002';
        /*     */     static final char ACC_PROTECTED = '\004';
        /*     */     static final char ACC_STATIC = '\b';
        /*     */     static final char ACC_FINAL = '\020';
        /*     */     static final char ACC_SYNCHRONIZED = ' ';
        /*     */     static final char ACC_VOLATILE = '@';
        /*     */     static final char ACC_TRANSIENT = '';
        /*     */     static final char ACC_NATIVE = 'Ā';
        /*     */     static final char ACC_INTERFACE = 'Ȁ';
        /*     */     static final char ACC_ABSTRACT = 'Ѐ';
        /*     */     static final char ACC_STRICT = 'ࠀ';
        /*     */     static final char ACC_SYNTHETIC = 'က';
        /*     */     static final char ACC_ANNOTATION = ' ';
        /*     */     static final char ACC_ENUM = '䀀';
        /*     */     static final char ACC_SUPER = ' ';
        /*     */     static final char ACC_BRIDGE = '@';
        /*     */     static final char ACC_VARARGS = '';
        /*     */     static final byte REF_NONE = 0;
        /*     */     static final byte REF_getField = 1;
        /*     */     static final byte REF_getStatic = 2;
        /*     */     static final byte REF_putField = 3;
        /*     */     static final byte REF_putStatic = 4;
        /*     */     static final byte REF_invokeVirtual = 5;
        /*     */     static final byte REF_invokeStatic = 6;
        /*     */     static final byte REF_invokeSpecial = 7;
        /*     */     static final byte REF_newInvokeSpecial = 8;
        /*     */     static final byte REF_invokeInterface = 9;
        /*     */     static final byte REF_LIMIT = 10;
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.MethodHandleNatives
 * JD-Core Version:    0.6.2
 */