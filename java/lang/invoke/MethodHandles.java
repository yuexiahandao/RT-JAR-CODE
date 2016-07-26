/*      */
package java.lang.invoke;
/*      */ 
/*      */

import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import sun.invoke.util.ValueConversions;
/*      */ import sun.invoke.util.VerifyAccess;
/*      */ import sun.invoke.util.Wrapper;
/*      */ import sun.reflect.CallerSensitive;
/*      */ import sun.reflect.Reflection;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ import sun.security.util.SecurityConstants;

/*      */
/*      */ public class MethodHandles
/*      */ {
    /*      */   private static final MemberName.Factory IMPL_NAMES;

    /*      */
/*      */
    @CallerSensitive
/*      */ public static Lookup lookup()
/*      */ {
/*   76 */
        return new Lookup(Reflection.getCallerClass());
/*      */
    }

    /*      */
/*      */
    public static Lookup publicLookup()
/*      */ {
/*   93 */
        return Lookup.PUBLIC_LOOKUP;
/*      */
    }

    /*      */
/*      */
    public static MethodHandle arrayElementGetter(Class<?> paramClass)
/*      */     throws IllegalArgumentException
/*      */ {
/* 1496 */
        return MethodHandleImpl.makeArrayElementAccessor(paramClass, false);
/*      */
    }

    /*      */
/*      */
    public static MethodHandle arrayElementSetter(Class<?> paramClass)
/*      */     throws IllegalArgumentException
/*      */ {
/* 1510 */
        return MethodHandleImpl.makeArrayElementAccessor(paramClass, true);
/*      */
    }

    /*      */
/*      */
    public static MethodHandle spreadInvoker(MethodType paramMethodType, int paramInt)
/*      */ {
/* 1562 */
        if ((paramInt < 0) || (paramInt > paramMethodType.parameterCount()))
/* 1563 */ throw new IllegalArgumentException("bad argument count " + paramInt);
/* 1564 */
        return paramMethodType.invokers().spreadInvoker(paramInt);
/*      */
    }

    /*      */
/*      */
    public static MethodHandle exactInvoker(MethodType paramMethodType)
/*      */ {
/* 1604 */
        return paramMethodType.invokers().exactInvoker();
/*      */
    }

    /*      */
/*      */
    public static MethodHandle invoker(MethodType paramMethodType)
/*      */ {
/* 1637 */
        return paramMethodType.invokers().generalInvoker();
/*      */
    }

    /*      */
/*      */
    static MethodHandle basicInvoker(MethodType paramMethodType)
/*      */ {
/* 1642 */
        return paramMethodType.form().basicInvoker();
/*      */
    }

    /*      */
/*      */
    public static MethodHandle explicitCastArguments(MethodHandle paramMethodHandle, MethodType paramMethodType)
/*      */ {
/* 1693 */
        if (!paramMethodHandle.type().isCastableTo(paramMethodType)) {
/* 1694 */
            throw new WrongMethodTypeException("cannot explicitly cast " + paramMethodHandle + " to " + paramMethodType);
/*      */
        }
/* 1696 */
        return MethodHandleImpl.makePairwiseConvert(paramMethodHandle, paramMethodType, 2);
/*      */
    }

    /*      */
/*      */
    public static MethodHandle permuteArguments(MethodHandle paramMethodHandle, MethodType paramMethodType, int[] paramArrayOfInt)
/*      */ {
/* 1760 */
        paramArrayOfInt = (int[]) paramArrayOfInt.clone();
/* 1761 */
        checkReorder(paramArrayOfInt, paramMethodType, paramMethodHandle.type());
/* 1762 */
        return paramMethodHandle.permuteArguments(paramMethodType, paramArrayOfInt);
/*      */
    }

    /*      */
/*      */
    private static void checkReorder(int[] paramArrayOfInt, MethodType paramMethodType1, MethodType paramMethodType2) {
/* 1766 */
        if (paramMethodType1.returnType() != paramMethodType2.returnType()) {
/* 1767 */
            throw MethodHandleStatics.newIllegalArgumentException("return types do not match", paramMethodType2, paramMethodType1);
/*      */
        }
/* 1769 */
        if (paramArrayOfInt.length == paramMethodType2.parameterCount()) {
/* 1770 */
            int i = paramMethodType1.parameterCount();
/* 1771 */
            int j = 0;
/* 1772 */
            for (int k = 0; k < paramArrayOfInt.length; k++) {
/* 1773 */
                int m = paramArrayOfInt[k];
/* 1774 */
                if ((m < 0) || (m >= i)) {
/* 1775 */
                    j = 1;
                    break;
/*      */
                }
/* 1777 */
                Class localClass1 = paramMethodType1.parameterType(m);
/* 1778 */
                Class localClass2 = paramMethodType2.parameterType(k);
/* 1779 */
                if (localClass1 != localClass2) {
/* 1780 */
                    throw MethodHandleStatics.newIllegalArgumentException("parameter types do not match after reorder", paramMethodType2, paramMethodType1);
/*      */
                }
/*      */
            }
/* 1783 */
            if (j == 0) return;
/*      */
        }
/* 1785 */
        throw MethodHandleStatics.newIllegalArgumentException("bad reorder array: " + Arrays.toString(paramArrayOfInt));
/*      */
    }

    /*      */
/*      */
    public static MethodHandle constant(Class<?> paramClass, Object paramObject)
/*      */ {
/* 1805 */
        if (paramClass.isPrimitive()) {
/* 1806 */
            if (paramClass == Void.TYPE)
/* 1807 */ throw MethodHandleStatics.newIllegalArgumentException("void type");
/* 1808 */
            Wrapper localWrapper = Wrapper.forPrimitiveType(paramClass);
/* 1809 */
            return insertArguments(identity(paramClass), 0, new Object[]{localWrapper.convert(paramObject, paramClass)});
/*      */
        }
/* 1811 */
        return identity(paramClass).bindTo(paramClass.cast(paramObject));
/*      */
    }

    /*      */
/*      */
    public static MethodHandle identity(Class<?> paramClass)
/*      */ {
/* 1824 */
        if (paramClass == Void.TYPE)
/* 1825 */ throw MethodHandleStatics.newIllegalArgumentException("void type");
/* 1826 */
        if (paramClass == Object.class)
/* 1827 */ return ValueConversions.identity();
/* 1828 */
        if (paramClass.isPrimitive()) {
/* 1829 */
            return ValueConversions.identity(Wrapper.forPrimitiveType(paramClass));
/*      */
        }
/* 1831 */
        return MethodHandleImpl.makeReferenceIdentity(paramClass);
/*      */
    }

    /*      */
/*      */
    public static MethodHandle insertArguments(MethodHandle paramMethodHandle, int paramInt, Object[] paramArrayOfObject)
/*      */ {
/* 1866 */
        int i = paramArrayOfObject.length;
/* 1867 */
        MethodType localMethodType = paramMethodHandle.type();
/* 1868 */
        int j = localMethodType.parameterCount();
/* 1869 */
        int k = j - i;
/* 1870 */
        if (k < 0)
/* 1871 */ throw MethodHandleStatics.newIllegalArgumentException("too many values to insert");
/* 1872 */
        if ((paramInt < 0) || (paramInt > k))
/* 1873 */ throw MethodHandleStatics.newIllegalArgumentException("no argument type to append");
/* 1874 */
        MethodHandle localMethodHandle = paramMethodHandle;
/* 1875 */
        for (int m = 0; m < i; m++) {
/* 1876 */
            Object localObject = paramArrayOfObject[m];
/* 1877 */
            Class localClass = localMethodType.parameterType(paramInt + m);
/* 1878 */
            if (localClass.isPrimitive()) {
/* 1879 */
                char c = 'I';
/* 1880 */
                Wrapper localWrapper = Wrapper.forPrimitiveType(localClass);
/* 1881 */
                switch (1. $SwitchMap$sun$invoke$util$Wrapper[localWrapper.ordinal()]){
                    case 1:
/* 1882 */
                        c = 'J';
                        break;
/*      */
                    case 2:
/* 1883 */
                        c = 'F';
                        break;
/*      */
                    case 3:
/* 1884 */
                        c = 'D';
/*      */
                }
/*      */ 
/* 1887 */
                localObject = localWrapper.convert(localObject, localClass);
/* 1888 */
                localMethodHandle = localMethodHandle.bindArgument(paramInt, c, localObject);
/*      */
            }
/*      */
            else {
/* 1891 */
                localObject = localClass.cast(localObject);
/* 1892 */
                if (paramInt == 0)
/* 1893 */ localMethodHandle = localMethodHandle.bindReceiver(localObject);
/*      */
                else
/* 1895 */           localMethodHandle = localMethodHandle.bindArgument(paramInt, 'L', localObject);
/*      */
            }
/*      */
        }
/* 1898 */
        return localMethodHandle;
/*      */
    }

    /*      */
/*      */
    public static MethodHandle dropArguments(MethodHandle paramMethodHandle, int paramInt, List<Class<?>> paramList)
/*      */ {
/* 1945 */
        MethodType localMethodType1 = paramMethodHandle.type();
/* 1946 */
        int i = paramList.size();
/* 1947 */
        MethodType.checkSlotCount(i);
/* 1948 */
        if (i == 0) return paramMethodHandle;
/* 1949 */
        int j = localMethodType1.parameterCount();
/* 1950 */
        int k = j + i;
/* 1951 */
        if ((paramInt < 0) || (paramInt >= k))
/* 1952 */ throw MethodHandleStatics.newIllegalArgumentException("no argument type to remove");
/* 1953 */
        ArrayList localArrayList = new ArrayList(localMethodType1.parameterList());
/* 1954 */
        localArrayList.addAll(paramInt, paramList);
/* 1955 */
        if (localArrayList.size() != k) throw MethodHandleStatics.newIllegalArgumentException("valueTypes");
/* 1956 */
        MethodType localMethodType2 = MethodType.methodType(localMethodType1.returnType(), localArrayList);
/* 1957 */
        return paramMethodHandle.dropArguments(localMethodType2, paramInt, i);
/*      */
    }

    /*      */
/*      */
    public static MethodHandle dropArguments(MethodHandle paramMethodHandle, int paramInt, Class<?>[] paramArrayOfClass)
/*      */ {
/* 2008 */
        return dropArguments(paramMethodHandle, paramInt, Arrays.asList(paramArrayOfClass));
/*      */
    }

    /*      */
/*      */
    public static MethodHandle filterArguments(MethodHandle paramMethodHandle, int paramInt, MethodHandle[] paramArrayOfMethodHandle)
/*      */ {
/* 2076 */
        MethodType localMethodType1 = paramMethodHandle.type();
/* 2077 */
        MethodHandle localMethodHandle1 = paramMethodHandle;
/* 2078 */
        MethodType localMethodType2 = null;
/* 2079 */
        assert ((localMethodType2 = localMethodType1) != null);
/* 2080 */
        int i = localMethodType1.parameterCount();
/* 2081 */
        if (paramInt + paramArrayOfMethodHandle.length > i)
/* 2082 */ throw MethodHandleStatics.newIllegalArgumentException("too many filters");
/* 2083 */
        int j = paramInt - 1;
/* 2084 */
        for (MethodHandle localMethodHandle2 : paramArrayOfMethodHandle) {
/* 2085 */
            j++;
/* 2086 */
            if (localMethodHandle2 != null) {
/* 2087 */
                localMethodHandle1 = filterArgument(localMethodHandle1, j, localMethodHandle2);
/* 2088 */
                assert ((localMethodType2 = localMethodType2.changeParameterType(j, localMethodHandle2.type().parameterType(0))) != null);
/*      */
            }
/*      */
        }
/* 2090 */
        assert (localMethodType2.equals(localMethodHandle1.type()));
/* 2091 */
        return localMethodHandle1;
/*      */
    }

    /*      */
/*      */
    static MethodHandle filterArgument(MethodHandle paramMethodHandle1, int paramInt, MethodHandle paramMethodHandle2)
/*      */ {
/* 2096 */
        MethodType localMethodType1 = paramMethodHandle1.type();
/* 2097 */
        MethodType localMethodType2 = paramMethodHandle2.type();
/* 2098 */
        if ((localMethodType2.parameterCount() != 1) || (localMethodType2.returnType() != localMethodType1.parameterType(paramInt)))
/*      */ {
/* 2100 */
            throw MethodHandleStatics.newIllegalArgumentException("target and filter types do not match", localMethodType1, localMethodType2);
/* 2101 */
        }
        return MethodHandleImpl.makeCollectArguments(paramMethodHandle1, paramMethodHandle2, paramInt, false);
/*      */
    }

    /*      */
/*      */
    static MethodHandle collectArguments(MethodHandle paramMethodHandle1, int paramInt, MethodHandle paramMethodHandle2)
/*      */ {
/* 2107 */
        MethodType localMethodType1 = paramMethodHandle1.type();
/* 2108 */
        MethodType localMethodType2 = paramMethodHandle2.type();
/* 2109 */
        if ((localMethodType2.returnType() != Void.TYPE) && (localMethodType2.returnType() != localMethodType1.parameterType(paramInt)))
/*      */ {
/* 2111 */
            throw MethodHandleStatics.newIllegalArgumentException("target and filter types do not match", localMethodType1, localMethodType2);
/* 2112 */
        }
        return MethodHandleImpl.makeCollectArguments(paramMethodHandle1, paramMethodHandle2, paramInt, false);
/*      */
    }

    /*      */
/*      */
    public static MethodHandle filterReturnValue(MethodHandle paramMethodHandle1, MethodHandle paramMethodHandle2)
/*      */ {
/* 2174 */
        MethodType localMethodType1 = paramMethodHandle1.type();
/* 2175 */
        MethodType localMethodType2 = paramMethodHandle2.type();
/* 2176 */
        Class localClass = localMethodType1.returnType();
/* 2177 */
        int i = localMethodType2.parameterCount();
/* 2178 */
        if (i == 0 ? localClass != Void.TYPE : localClass != localMethodType2.parameterType(0))
/*      */ {
/* 2181 */
            throw MethodHandleStatics.newIllegalArgumentException("target and filter types do not match", paramMethodHandle1, paramMethodHandle2);
/*      */
        }
/*      */ 
/* 2184 */
        return MethodHandleImpl.makeCollectArguments(paramMethodHandle2, paramMethodHandle1, 0, false);
/*      */
    }

    /*      */
/*      */
    public static MethodHandle foldArguments(MethodHandle paramMethodHandle1, MethodHandle paramMethodHandle2)
/*      */ {
/* 2265 */
        int i = 0;
/* 2266 */
        MethodType localMethodType1 = paramMethodHandle1.type();
/* 2267 */
        MethodType localMethodType2 = paramMethodHandle2.type();
/* 2268 */
        int j = i;
/* 2269 */
        int k = localMethodType2.parameterCount();
/* 2270 */
        int m = localMethodType2.returnType() == Void.TYPE ? 0 : 1;
/* 2271 */
        int n = j + m;
/* 2272 */
        int i1 = localMethodType1.parameterCount() >= n + k ? 1 : 0;
/* 2273 */
        if ((i1 != 0) && (!localMethodType2.parameterList().equals(localMethodType1.parameterList().subList(n, n + k))))
/*      */ {
/* 2276 */
            i1 = 0;
/* 2277 */
        }
        if ((i1 != 0) && (m != 0) && (!localMethodType2.returnType().equals(localMethodType1.parameterType(0))))
/* 2278 */ i1 = 0;
/* 2279 */
        if (i1 == 0)
/* 2280 */ throw misMatchedTypes("target and combiner types", localMethodType1, localMethodType2);
/* 2281 */
        MethodType localMethodType3 = localMethodType1.dropParameterTypes(j, n);
/* 2282 */
        return MethodHandleImpl.makeCollectArguments(paramMethodHandle1, paramMethodHandle2, j, true);
/*      */
    }

    /*      */
/*      */
    public static MethodHandle guardWithTest(MethodHandle paramMethodHandle1, MethodHandle paramMethodHandle2, MethodHandle paramMethodHandle3)
/*      */ {
/* 2321 */
        MethodType localMethodType1 = paramMethodHandle1.type();
/* 2322 */
        MethodType localMethodType2 = paramMethodHandle2.type();
/* 2323 */
        MethodType localMethodType3 = paramMethodHandle3.type();
/* 2324 */
        if (!localMethodType2.equals(localMethodType3))
/* 2325 */ throw misMatchedTypes("target and fallback types", localMethodType2, localMethodType3);
/* 2326 */
        if (localMethodType1.returnType() != Boolean.TYPE)
/* 2327 */ throw MethodHandleStatics.newIllegalArgumentException("guard type is not a predicate " + localMethodType1);
/* 2328 */
        List localList1 = localMethodType2.parameterList();
/* 2329 */
        List localList2 = localMethodType1.parameterList();
/* 2330 */
        if (!localList1.equals(localList2)) {
/* 2331 */
            int i = localList2.size();
            int j = localList1.size();
/* 2332 */
            if ((i >= j) || (!localList1.subList(0, i).equals(localList2)))
/* 2333 */ throw misMatchedTypes("target and test types", localMethodType2, localMethodType1);
/* 2334 */
            paramMethodHandle1 = dropArguments(paramMethodHandle1, i, localList1.subList(i, j));
/* 2335 */
            localMethodType1 = paramMethodHandle1.type();
/*      */
        }
/* 2337 */
        return MethodHandleImpl.makeGuardWithTest(paramMethodHandle1, paramMethodHandle2, paramMethodHandle3);
/*      */
    }

    /*      */
/*      */
    static RuntimeException misMatchedTypes(String paramString, MethodType paramMethodType1, MethodType paramMethodType2) {
/* 2341 */
        return MethodHandleStatics.newIllegalArgumentException(paramString + " must match: " + paramMethodType1 + " != " + paramMethodType2);
/*      */
    }

    /*      */
/*      */
    public static MethodHandle catchException(MethodHandle paramMethodHandle1, Class<? extends Throwable> paramClass, MethodHandle paramMethodHandle2)
/*      */ {
/* 2391 */
        MethodType localMethodType1 = paramMethodHandle1.type();
/* 2392 */
        MethodType localMethodType2 = paramMethodHandle2.type();
/* 2393 */
        if ((localMethodType2.parameterCount() < 1) || (!localMethodType2.parameterType(0).isAssignableFrom(paramClass)))
/*      */ {
/* 2395 */
            throw MethodHandleStatics.newIllegalArgumentException("handler does not accept exception type " + paramClass);
/* 2396 */
        }
        if (localMethodType2.returnType() != localMethodType1.returnType())
/* 2397 */ throw misMatchedTypes("target and handler return types", localMethodType1, localMethodType2);
/* 2398 */
        List localList1 = localMethodType1.parameterList();
/* 2399 */
        List localList2 = localMethodType2.parameterList();
/* 2400 */
        localList2 = localList2.subList(1, localList2.size());
/* 2401 */
        if (!localList1.equals(localList2)) {
/* 2402 */
            int i = localList2.size();
            int j = localList1.size();
/* 2403 */
            if ((i >= j) || (!localList1.subList(0, i).equals(localList2)))
/* 2404 */ throw misMatchedTypes("target and handler types", localMethodType1, localMethodType2);
/* 2405 */
            paramMethodHandle2 = dropArguments(paramMethodHandle2, 1 + i, localList1.subList(i, j));
/* 2406 */
            localMethodType2 = paramMethodHandle2.type();
/*      */
        }
/* 2408 */
        return MethodHandleImpl.makeGuardWithCatch(paramMethodHandle1, paramClass, paramMethodHandle2);
/*      */
    }

    /*      */
/*      */
    public static MethodHandle throwException(Class<?> paramClass, Class<? extends Throwable> paramClass1)
/*      */ {
/* 2423 */
        if (!Throwable.class.isAssignableFrom(paramClass1))
/* 2424 */ throw new ClassCastException(paramClass1.getName());
/* 2425 */
        return MethodHandleImpl.throwException(MethodType.methodType(paramClass, paramClass1));
/*      */
    }

    /*      */
/*      */   static
/*      */ {
/*   61 */
        IMPL_NAMES = MemberName.getFactory();
/*   62 */
        MethodHandleImpl.initStatics();
/*      */
    }

    /*      */
/*      */   public static final class Lookup
/*      */ {
        /*      */     private final Class<?> lookupClass;
        /*      */     private final int allowedModes;
        /*      */     public static final int PUBLIC = 1;
        /*      */     public static final int PRIVATE = 2;
        /*      */     public static final int PROTECTED = 4;
        /*      */     public static final int PACKAGE = 8;
        /*      */     private static final int ALL_MODES = 15;
        /*      */     private static final int TRUSTED = -1;
        /*  506 */     static final Lookup PUBLIC_LOOKUP = new Lookup(Object.class, 1);
        /*      */
/*  509 */     static final Lookup IMPL_LOOKUP = new Lookup(Object.class, -1);
        /*      */     private static final boolean ALLOW_NESTMATE_ACCESS = false;

        /*      */
/*      */
        private static int fixmods(int paramInt)
/*      */ {
/*  388 */
            paramInt &= 7;
/*  389 */
            return paramInt != 0 ? paramInt : 8;
/*      */
        }

        /*      */
/*      */
        public Class<?> lookupClass()
/*      */ {
/*  401 */
            return this.lookupClass;
/*      */
        }

        /*      */
/*      */
        private Class<?> lookupClassOrNull()
/*      */ {
/*  406 */
            return this.allowedModes == -1 ? null : this.lookupClass;
/*      */
        }

        /*      */
/*      */
        public int lookupModes()
/*      */ {
/*  427 */
            return this.allowedModes & 0xF;
/*      */
        }

        /*      */
/*      */     Lookup(Class<?> paramClass)
/*      */ {
/*  440 */
            this(paramClass, 15);
/*  441 */
            checkUnprivilegedlookupClass(paramClass, 15);
/*      */
        }

        /*      */
/*      */
        private Lookup(Class<?> paramClass, int paramInt) {
/*  445 */
            this.lookupClass = paramClass;
/*  446 */
            this.allowedModes = paramInt;
/*      */
        }

        /*      */
/*      */
        public Lookup in(Class<?> paramClass)
/*      */ {
/*  474 */
            paramClass.getClass();
/*  475 */
            if (this.allowedModes == -1)
/*  476 */ return new Lookup(paramClass, 15);
/*  477 */
            if (paramClass == this.lookupClass)
/*  478 */ return this;
/*  479 */
            int i = this.allowedModes & 0xB;
/*  480 */
            if (((i & 0x8) != 0) && (!VerifyAccess.isSamePackage(this.lookupClass, paramClass)))
/*      */ {
/*  482 */
                i &= -11;
/*      */
            }
/*      */ 
/*  485 */
            if (((i & 0x2) != 0) && (!VerifyAccess.isSamePackageMember(this.lookupClass, paramClass)))
/*      */ {
/*  487 */
                i &= -3;
/*      */
            }
/*  489 */
            if (((i & 0x1) != 0) && (!VerifyAccess.isClassAccessible(paramClass, this.lookupClass, this.allowedModes)))
/*      */ {
/*  493 */
                i = 0;
/*      */
            }
/*  495 */
            checkUnprivilegedlookupClass(paramClass, i);
/*  496 */
            return new Lookup(paramClass, i);
/*      */
        }

        /*      */
/*      */
        private static void checkUnprivilegedlookupClass(Class<?> paramClass, int paramInt)
/*      */ {
/*  512 */
            String str = paramClass.getName();
/*  513 */
            if (str.startsWith("java.lang.invoke.")) {
/*  514 */
                throw MethodHandleStatics.newIllegalArgumentException("illegal lookupClass: " + paramClass);
/*      */
            }
/*      */ 
/*  518 */
            if ((paramInt == 15) && (paramClass.getClassLoader() == null) && (
/*  519 */         (str.startsWith("java.")) || ((str.startsWith("sun.")) && (!str.startsWith("sun.invoke.")))))
/*      */ {
/*  521 */
                throw MethodHandleStatics.newIllegalArgumentException("illegal lookupClass: " + paramClass);
/*      */
            }
/*      */
        }

        /*      */
/*      */
        public String toString()
/*      */ {
/*  556 */
            String str = this.lookupClass.getName();
/*  557 */
            switch (this.allowedModes) {
/*      */
                case 0:
/*  559 */
                    return str + "/noaccess";
/*      */
                case 1:
/*  561 */
                    return str + "/public";
/*      */
                case 9:
/*  563 */
                    return str + "/package";
/*      */
                case 11:
/*  565 */
                    return str + "/private";
/*      */
                case 15:
/*  567 */
                    return str;
/*      */
                case -1:
/*  569 */
                    return "/trusted";
/*      */
                case 2:
/*      */
                case 3:
/*      */
                case 4:
/*      */
                case 5:
/*      */
                case 6:
/*      */
                case 7:
/*      */
                case 8:
/*      */
                case 10:
/*      */
                case 12:
/*      */
                case 13:
/*  571 */
                case 14:
            }
            str = str + "/" + Integer.toHexString(this.allowedModes);
/*  572 */
            if (!$assertionsDisabled) throw new AssertionError(str);
/*  573 */
            return str;
/*      */
        }

        /*      */
/*      */
        public MethodHandle findStatic(Class<?> paramClass, String paramString, MethodType paramMethodType)
/*      */       throws NoSuchMethodException, IllegalAccessException
/*      */ {
/*  614 */
            MemberName localMemberName = resolveOrFail((byte) 6, paramClass, paramString, paramMethodType);
/*  615 */
            checkSecurityManager(paramClass, localMemberName);
/*  616 */
            return getDirectMethod((byte) 6, paramClass, localMemberName, findBoundCallerClass(localMemberName));
/*      */
        }

        /*      */
/*      */
        public MethodHandle findVirtual(Class<?> paramClass, String paramString, MethodType paramMethodType)
/*      */       throws NoSuchMethodException, IllegalAccessException
/*      */ {
/*  691 */
            if (paramClass == MethodHandle.class) {
/*  692 */
                MethodHandle localMethodHandle = findVirtualForMH(paramString, paramMethodType);
/*  693 */
                if (localMethodHandle != null) return localMethodHandle;
/*      */
            }
/*  695 */
            byte b = paramClass.isInterface() ? 9 : 5;
/*  696 */
            MemberName localMemberName = resolveOrFail(b, paramClass, paramString, paramMethodType);
/*  697 */
            checkSecurityManager(paramClass, localMemberName);
/*  698 */
            return getDirectMethod(b, paramClass, localMemberName, findBoundCallerClass(localMemberName));
/*      */
        }

        /*      */
/*      */
        private MethodHandle findVirtualForMH(String paramString, MethodType paramMethodType) {
/*  702 */
            if ("invoke".equals(paramString))
/*  703 */ return MethodHandles.invoker(paramMethodType);
/*  704 */
            if ("invokeExact".equals(paramString))
/*  705 */ return MethodHandles.exactInvoker(paramMethodType);
/*  706 */
            return null;
/*      */
        }

        /*      */
/*      */
        public MethodHandle findConstructor(Class<?> paramClass, MethodType paramMethodType)
/*      */       throws NoSuchMethodException, IllegalAccessException
/*      */ {
/*  754 */
            String str = "<init>";
/*  755 */
            MemberName localMemberName = resolveOrFail((byte) 8, paramClass, str, paramMethodType);
/*  756 */
            checkSecurityManager(paramClass, localMemberName);
/*  757 */
            return getDirectConstructor(paramClass, localMemberName);
/*      */
        }

        /*      */
/*      */
        public MethodHandle findSpecial(Class<?> paramClass1, String paramString, MethodType paramMethodType, Class<?> paramClass2)
/*      */       throws NoSuchMethodException, IllegalAccessException
/*      */ {
/*  835 */
            checkSpecialCaller(paramClass2);
/*  836 */
            Lookup localLookup = in(paramClass2);
/*  837 */
            MemberName localMemberName = localLookup.resolveOrFail((byte) 7, paramClass1, paramString, paramMethodType);
/*  838 */
            checkSecurityManager(paramClass1, localMemberName);
/*  839 */
            return localLookup.getDirectMethod((byte) 7, paramClass1, localMemberName, findBoundCallerClass(localMemberName));
/*      */
        }

        /*      */
/*      */
        public MethodHandle findGetter(Class<?> paramClass1, String paramString, Class<?> paramClass2)
/*      */       throws NoSuchFieldException, IllegalAccessException
/*      */ {
/*  860 */
            MemberName localMemberName = resolveOrFail((byte) 1, paramClass1, paramString, paramClass2);
/*  861 */
            checkSecurityManager(paramClass1, localMemberName);
/*  862 */
            return getDirectField((byte) 1, paramClass1, localMemberName);
/*      */
        }

        /*      */
/*      */
        public MethodHandle findSetter(Class<?> paramClass1, String paramString, Class<?> paramClass2)
/*      */       throws NoSuchFieldException, IllegalAccessException
/*      */ {
/*  883 */
            MemberName localMemberName = resolveOrFail((byte) 3, paramClass1, paramString, paramClass2);
/*  884 */
            checkSecurityManager(paramClass1, localMemberName);
/*  885 */
            return getDirectField((byte) 3, paramClass1, localMemberName);
/*      */
        }

        /*      */
/*      */
        public MethodHandle findStaticGetter(Class<?> paramClass1, String paramString, Class<?> paramClass2)
/*      */       throws NoSuchFieldException, IllegalAccessException
/*      */ {
/*  905 */
            MemberName localMemberName = resolveOrFail((byte) 2, paramClass1, paramString, paramClass2);
/*  906 */
            checkSecurityManager(paramClass1, localMemberName);
/*  907 */
            return getDirectField((byte) 2, paramClass1, localMemberName);
/*      */
        }

        /*      */
/*      */
        public MethodHandle findStaticSetter(Class<?> paramClass1, String paramString, Class<?> paramClass2)
/*      */       throws NoSuchFieldException, IllegalAccessException
/*      */ {
/*  927 */
            MemberName localMemberName = resolveOrFail((byte) 4, paramClass1, paramString, paramClass2);
/*  928 */
            checkSecurityManager(paramClass1, localMemberName);
/*  929 */
            return getDirectField((byte) 4, paramClass1, localMemberName);
/*      */
        }

        /*      */
/*      */
        public MethodHandle bind(Object paramObject, String paramString, MethodType paramMethodType)
/*      */       throws NoSuchMethodException, IllegalAccessException
/*      */ {
/*  979 */
            Class localClass = paramObject.getClass();
/*  980 */
            MemberName localMemberName = resolveOrFail((byte) 7, localClass, paramString, paramMethodType);
/*  981 */
            checkSecurityManager(localClass, localMemberName);
/*  982 */
            MethodHandle localMethodHandle = getDirectMethodNoRestrict((byte) 7, localClass, localMemberName, findBoundCallerClass(localMemberName));
/*  983 */
            return localMethodHandle.bindReceiver(paramObject).setVarargs(localMemberName);
/*      */
        }

        /*      */
/*      */
        public MethodHandle unreflect(Method paramMethod)
/*      */       throws IllegalAccessException
/*      */ {
/* 1008 */
            MemberName localMemberName = new MemberName(paramMethod);
/* 1009 */
            byte b = localMemberName.getReferenceKind();
/* 1010 */
            if (b == 7)
/* 1011 */ b = 5;
/* 1012 */
            assert (localMemberName.isMethod());
/* 1013 */
            Lookup localLookup = paramMethod.isAccessible() ? IMPL_LOOKUP : this;
/* 1014 */
            return localLookup.getDirectMethod(b, localMemberName.getDeclaringClass(), localMemberName, findBoundCallerClass(localMemberName));
/*      */
        }

        /*      */
/*      */
        public MethodHandle unreflectSpecial(Method paramMethod, Class<?> paramClass)
/*      */       throws IllegalAccessException
/*      */ {
/* 1039 */
            checkSpecialCaller(paramClass);
/* 1040 */
            Lookup localLookup = in(paramClass);
/* 1041 */
            MemberName localMemberName = new MemberName(paramMethod, true);
/* 1042 */
            assert (localMemberName.isMethod());
/*      */ 
/* 1044 */
            return localLookup.getDirectMethod((byte) 7, localMemberName.getDeclaringClass(), localMemberName, findBoundCallerClass(localMemberName));
/*      */
        }

        /*      */
/*      */
        public MethodHandle unreflectConstructor(Constructor paramConstructor)
/*      */       throws IllegalAccessException
/*      */ {
/* 1070 */
            MemberName localMemberName = new MemberName(paramConstructor);
/* 1071 */
            assert (localMemberName.isConstructor());
/* 1072 */
            Lookup localLookup = paramConstructor.isAccessible() ? IMPL_LOOKUP : this;
/* 1073 */
            return localLookup.getDirectConstructor(localMemberName.getDeclaringClass(), localMemberName);
/*      */
        }

        /*      */
/*      */
        public MethodHandle unreflectGetter(Field paramField)
/*      */       throws IllegalAccessException
/*      */ {
/* 1091 */
            return unreflectField(paramField, false);
/*      */
        }

        /*      */
        private MethodHandle unreflectField(Field paramField, boolean paramBoolean) throws IllegalAccessException {
/* 1094 */
            MemberName localMemberName = new MemberName(paramField, paramBoolean);
/* 1095 */
            assert (paramBoolean ? MethodHandleNatives.refKindIsSetter(localMemberName.getReferenceKind()) : MethodHandleNatives.refKindIsGetter(localMemberName.getReferenceKind()));
/*      */ 
/* 1098 */
            Lookup localLookup = paramField.isAccessible() ? IMPL_LOOKUP : this;
/* 1099 */
            return localLookup.getDirectField(localMemberName.getReferenceKind(), paramField.getDeclaringClass(), localMemberName);
/*      */
        }

        /*      */
/*      */
        public MethodHandle unreflectSetter(Field paramField)
/*      */       throws IllegalAccessException
/*      */ {
/* 1117 */
            return unreflectField(paramField, true);
/*      */
        }

        /*      */
/*      */     MemberName resolveOrFail(byte paramByte, Class<?> paramClass1, String paramString, Class<?> paramClass2)
/*      */       throws NoSuchFieldException, IllegalAccessException
/*      */ {
/* 1123 */
            paramString.getClass();
            paramClass2.getClass();
/* 1124 */
            checkSymbolicClass(paramClass1);
/* 1125 */
            return MethodHandles.IMPL_NAMES.resolveOrFail(paramByte, new MemberName(paramClass1, paramString, paramClass2, paramByte), lookupClassOrNull(), NoSuchFieldException.class);
/*      */
        }

        /*      */
/*      */     MemberName resolveOrFail(byte paramByte, Class<?> paramClass, String paramString, MethodType paramMethodType) throws NoSuchMethodException, IllegalAccessException
/*      */ {
/* 1130 */
            paramMethodType.getClass();
/* 1131 */
            checkSymbolicClass(paramClass);
/* 1132 */
            checkMethodName(paramByte, paramString);
/* 1133 */
            return MethodHandles.IMPL_NAMES.resolveOrFail(paramByte, new MemberName(paramClass, paramString, paramMethodType, paramByte), lookupClassOrNull(), NoSuchMethodException.class);
/*      */
        }

        /*      */
/*      */     void checkSymbolicClass(Class<?> paramClass) throws IllegalAccessException
/*      */ {
/* 1138 */
            Class localClass = lookupClassOrNull();
/* 1139 */
            if ((localClass != null) && (!VerifyAccess.isClassAccessible(paramClass, localClass, this.allowedModes)))
/* 1140 */ throw new MemberName(paramClass).makeAccessException("symbolic reference class is not public", this);
/*      */
        }

        /*      */
/*      */     void checkMethodName(byte paramByte, String paramString) throws NoSuchMethodException {
/* 1144 */
            if ((paramString.startsWith("<")) && (paramByte != 8))
/* 1145 */ throw new NoSuchMethodException("illegal method name: " + paramString);
/*      */
        }

        /*      */
/*      */     Class<?> findBoundCallerClass(MemberName paramMemberName)
/*      */       throws IllegalAccessException
/*      */ {
/* 1155 */
            Class localClass = null;
/* 1156 */
            if (MethodHandleNatives.isCallerSensitive(paramMemberName))
/*      */ {
/* 1158 */
                if (isFullPowerLookup())
/* 1159 */ localClass = this.lookupClass;
/*      */
                else {
/* 1161 */
                    throw new IllegalAccessException("Attempt to lookup caller-sensitive method using restricted lookup object");
/*      */
                }
/*      */
            }
/* 1164 */
            return localClass;
/*      */
        }

        /*      */
/*      */
        private boolean isFullPowerLookup() {
/* 1168 */
            return (this.allowedModes & 0x2) != 0;
/*      */
        }

        /*      */
/*      */
        private boolean isCheckMemberAccessOverridden(SecurityManager paramSecurityManager)
/*      */ {
/* 1176 */
            Class localClass = paramSecurityManager.getClass();
/* 1177 */
            if (localClass == SecurityManager.class) return false;
/*      */
            try
/*      */ {
/* 1180 */
                return localClass.getMethod("checkMemberAccess", new Class[]{Class.class, Integer.TYPE}).getDeclaringClass() != SecurityManager.class;
/*      */
            } catch (NoSuchMethodException localNoSuchMethodException) {
/*      */
            }
/* 1183 */
            throw new InternalError("should not reach here");
/*      */
        }

        /*      */
/*      */     void checkSecurityManager(Class<?> paramClass, MemberName paramMemberName)
/*      */ {
/* 1192 */
            SecurityManager localSecurityManager = System.getSecurityManager();
/* 1193 */
            if (localSecurityManager == null) return;
/* 1194 */
            if (this.allowedModes == -1) return;
/*      */ 
/* 1196 */
            boolean bool = isCheckMemberAccessOverridden(localSecurityManager);
/*      */ 
/* 1202 */
            Class<?> localClass = paramClass;
/* 1203 */
            if (bool)
/*      */ {
/* 1206 */
                localSecurityManager.checkMemberAccess(localClass, 0);
/*      */
            }
/*      */ 
/* 1211 */
            if ((!isFullPowerLookup()) || (!VerifyAccess.classLoaderIsAncestor(this.lookupClass, paramClass)))
/*      */ {
/* 1213 */
                ReflectUtil.checkPackageAccess(paramClass);
/*      */
            }
/*      */ 
/* 1217 */
            if (paramMemberName.isPublic()) return;
/* 1218 */
            Class localClass1 = paramMemberName.getDeclaringClass();
/*      */ 
/* 1222 */
            Class localClass2 = localClass1;
/* 1223 */
            if (!bool) {
/* 1224 */
                if (!isFullPowerLookup()) {
/* 1225 */
                    localSecurityManager.checkPermission(SecurityConstants.CHECK_MEMBER_ACCESS_PERMISSION);
/*      */
                }
/*      */
            }
/*      */
            else
/*      */ {
/* 1230 */
                localSecurityManager.checkMemberAccess(localClass2, 1);
/*      */
            }
/*      */ 
/* 1235 */
            if (localClass1 != paramClass)
/* 1236 */ ReflectUtil.checkPackageAccess(localClass1);
/*      */
        }

        /*      */
/*      */     void checkMethod(byte paramByte, Class<?> paramClass, MemberName paramMemberName) throws IllegalAccessException
/*      */ {
/* 1241 */
            int i = paramByte == 6 ? 1 : 0;
/*      */
            String str;
/* 1243 */
            if (paramMemberName.isConstructor()) {
/* 1244 */
                str = "expected a method, not a constructor";
/* 1245 */
            } else if (!paramMemberName.isMethod()) {
/* 1246 */
                str = "expected a method";
/* 1247 */
            } else if (i != paramMemberName.isStatic()) {
/* 1248 */
                str = i != 0 ? "expected a static method" : "expected a non-static method";
/*      */
            } else {
/* 1250 */
                checkAccess(paramByte, paramClass, paramMemberName);
                return;
/* 1251 */
            }
            throw paramMemberName.makeAccessException(str, this);
/*      */
        }

        /*      */
/*      */     void checkField(byte paramByte, Class<?> paramClass, MemberName paramMemberName) throws IllegalAccessException {
/* 1255 */
            int i = !MethodHandleNatives.refKindHasReceiver(paramByte) ? 1 : 0;
/*      */
            String str;
/* 1257 */
            if (i != paramMemberName.isStatic()) {
/* 1258 */
                str = i != 0 ? "expected a static field" : "expected a non-static field";
/*      */
            } else {
/* 1260 */
                checkAccess(paramByte, paramClass, paramMemberName);
                return;
/* 1261 */
            }
            throw paramMemberName.makeAccessException(str, this);
/*      */
        }

        /*      */
/*      */     void checkAccess(byte paramByte, Class<?> paramClass, MemberName paramMemberName) throws IllegalAccessException {
/* 1265 */
            assert ((paramMemberName.referenceKindIsConsistentWith(paramByte)) && (MethodHandleNatives.refKindIsValid(paramByte)) && (MethodHandleNatives.refKindIsField(paramByte) == paramMemberName.isField()));
/*      */ 
/* 1268 */
            int i = this.allowedModes;
/* 1269 */
            if (i == -1) return;
/* 1270 */
            int j = paramMemberName.getModifiers();
/* 1271 */
            if (Modifier.isProtected(j)) {
/* 1272 */
                if ((paramByte == 5) && (paramMemberName.getDeclaringClass() == Object.class) && (paramMemberName.getName().equals("clone")) && (paramClass.isArray()))
/*      */ {
/* 1289 */
                    j ^= 5;
/*      */
                }
/* 1291 */
                if (paramByte == 8)
/*      */ {
/* 1293 */
                    j ^= 4;
/*      */
                }
/*      */
            }
/* 1296 */
            if ((Modifier.isFinal(j)) && (MethodHandleNatives.refKindIsSetter(paramByte)))
/*      */ {
/* 1298 */
                throw paramMemberName.makeAccessException("unexpected set of a final field", this);
/* 1299 */
            }
            if ((Modifier.isPublic(j)) && (Modifier.isPublic(paramClass.getModifiers())) && (i != 0))
/* 1300 */ return;
/* 1301 */
            int k = fixmods(j);
/* 1302 */
            if ((k & i) != 0)
/*      */ {
/* 1303 */
                if (!VerifyAccess.isMemberAccessible(paramClass, paramMemberName.getDeclaringClass(), j, lookupClass(), i))
                    ;
/*      */
            }
/* 1308 */
            else if (((k & 0x4) != 0) && ((i & 0x8) != 0) && (VerifyAccess.isSamePackage(paramMemberName.getDeclaringClass(), lookupClass())))
/*      */ {
/* 1310 */
                return;
/*      */
            }
/* 1312 */
            throw paramMemberName.makeAccessException(accessFailedMessage(paramClass, paramMemberName), this);
/*      */
        }

        /*      */
/*      */     String accessFailedMessage(Class<?> paramClass, MemberName paramMemberName) {
/* 1316 */
            Class localClass = paramMemberName.getDeclaringClass();
/* 1317 */
            int i = paramMemberName.getModifiers();
/*      */ 
/* 1319 */
            int j = (Modifier.isPublic(localClass.getModifiers())) && ((localClass == paramClass) || (Modifier.isPublic(paramClass.getModifiers()))) ? 1 : 0;
/*      */ 
/* 1322 */
            if ((j == 0) && ((this.allowedModes & 0x8) != 0)) {
/* 1323 */
                j = (VerifyAccess.isClassAccessible(localClass, lookupClass(), 15)) && ((localClass == paramClass) || (VerifyAccess.isClassAccessible(paramClass, lookupClass(), 15))) ? 1 : 0;
/*      */
            }
/*      */ 
/* 1327 */
            if (j == 0)
/* 1328 */ return "class is not public";
/* 1329 */
            if (Modifier.isPublic(i))
/* 1330 */ return "access to public member failed";
/* 1331 */
            if (Modifier.isPrivate(i))
/* 1332 */ return "member is private";
/* 1333 */
            if (Modifier.isProtected(i))
/* 1334 */ return "member is protected";
/* 1335 */
            return "member is private to package";
/*      */
        }

        /*      */
/*      */
        private void checkSpecialCaller(Class<?> paramClass)
/*      */       throws IllegalAccessException
/*      */ {
/* 1341 */
            int i = this.allowedModes;
/* 1342 */
            if (i == -1) return;
/* 1343 */
            if (((i & 0x2) == 0) || (paramClass != lookupClass()))
/*      */ {
/* 1347 */
                throw new MemberName(paramClass).makeAccessException("no private access for invokespecial", this);
/*      */
            }
/*      */
        }

        /*      */
/*      */
        private boolean restrictProtectedReceiver(MemberName paramMemberName)
/*      */ {
/* 1354 */
            if ((!paramMemberName.isProtected()) || (paramMemberName.isStatic()) || (this.allowedModes == -1) || (paramMemberName.getDeclaringClass() == lookupClass()) || (VerifyAccess.isSamePackage(paramMemberName.getDeclaringClass(), lookupClass())))
/*      */ {
/* 1360 */
                return false;
/* 1361 */
            }
            return true;
/*      */
        }

        /*      */
        private MethodHandle restrictReceiver(MemberName paramMemberName, MethodHandle paramMethodHandle, Class<?> paramClass) throws IllegalAccessException {
/* 1364 */
            assert (!paramMemberName.isStatic());
/*      */ 
/* 1366 */
            if (!paramMemberName.getDeclaringClass().isAssignableFrom(paramClass)) {
/* 1367 */
                throw paramMemberName.makeAccessException("caller class must be a subclass below the method", paramClass);
/*      */
            }
/* 1369 */
            MethodType localMethodType1 = paramMethodHandle.type();
/* 1370 */
            if (localMethodType1.parameterType(0) == paramClass) return paramMethodHandle;
/* 1371 */
            MethodType localMethodType2 = localMethodType1.changeParameterType(0, paramClass);
/* 1372 */
            return paramMethodHandle.viewAsType(localMethodType2);
/*      */
        }

        /*      */
/*      */
        private MethodHandle getDirectMethod(byte paramByte, Class<?> paramClass1, MemberName paramMemberName, Class<?> paramClass2) throws IllegalAccessException {
/* 1376 */
            return getDirectMethodCommon(paramByte, paramClass1, paramMemberName, (paramByte == 7) || ((MethodHandleNatives.refKindHasReceiver(paramByte)) && (restrictProtectedReceiver(paramMemberName))), paramClass2);
/*      */
        }

        /*      */
/*      */
        private MethodHandle getDirectMethodNoRestrict(byte paramByte, Class<?> paramClass1, MemberName paramMemberName, Class<?> paramClass2)
/*      */       throws IllegalAccessException
/*      */ {
/* 1382 */
            return getDirectMethodCommon(paramByte, paramClass1, paramMemberName, false, paramClass2);
/*      */
        }

        /*      */
/*      */
        private MethodHandle getDirectMethodCommon(byte paramByte, Class<?> paramClass1, MemberName paramMemberName, boolean paramBoolean, Class<?> paramClass2) throws IllegalAccessException {
/* 1386 */
            checkMethod(paramByte, paramClass1, paramMemberName);
/* 1387 */
            if (paramMemberName.isMethodHandleInvoke())
/* 1388 */ return fakeMethodHandleInvoke(paramMemberName);
/*      */
            Class localClass;
/* 1391 */
            if ((paramByte == 7) && (paramClass1 != lookupClass()) && (paramClass1 != (localClass = lookupClass().getSuperclass())) && (paramClass1.isAssignableFrom(lookupClass())))
/*      */ {
/* 1395 */
                assert (!paramMemberName.getName().equals("<init>"));
/*      */ 
/* 1401 */
                localObject = new MemberName(localClass, paramMemberName.getName(), paramMemberName.getMethodType(), (byte) 7);
/*      */ 
/* 1405 */
                localObject = MethodHandles.IMPL_NAMES.resolveOrNull(paramByte, (MemberName) localObject, lookupClassOrNull());
/* 1406 */
                if (localObject == null) throw new InternalError(paramMemberName.toString());
/* 1407 */
                paramMemberName = (MemberName) localObject;
/* 1408 */
                paramClass1 = localClass;
/*      */ 
/* 1410 */
                checkMethod(paramByte, paramClass1, paramMemberName);
/*      */
            }
/*      */ 
/* 1413 */
            Object localObject = DirectMethodHandle.make(paramClass1, paramMemberName);
/* 1414 */
            localObject = maybeBindCaller(paramMemberName, (MethodHandle) localObject, paramClass2);
/* 1415 */
            localObject = ((MethodHandle) localObject).setVarargs(paramMemberName);
/* 1416 */
            if (paramBoolean)
/* 1417 */ localObject = restrictReceiver(paramMemberName, (MethodHandle) localObject, lookupClass());
/* 1418 */
            return localObject;
/*      */
        }

        /*      */
        private MethodHandle fakeMethodHandleInvoke(MemberName paramMemberName) {
/* 1421 */
            return MethodHandles.throwException(paramMemberName.getReturnType(), UnsupportedOperationException.class);
/*      */
        }

        /*      */
/*      */
        private MethodHandle maybeBindCaller(MemberName paramMemberName, MethodHandle paramMethodHandle, Class<?> paramClass) throws IllegalAccessException
/*      */ {
/* 1426 */
            if ((this.allowedModes == -1) || (!MethodHandleNatives.isCallerSensitive(paramMemberName)))
/* 1427 */ return paramMethodHandle;
/* 1428 */
            Object localObject = this.lookupClass;
/* 1429 */
            if ((this.allowedModes & 0x2) == 0)
/* 1430 */ localObject = paramClass;
/* 1431 */
            MethodHandle localMethodHandle = MethodHandleImpl.bindCaller(paramMethodHandle, (Class) localObject);
/*      */ 
/* 1433 */
            return localMethodHandle;
/*      */
        }

        /*      */
        private MethodHandle getDirectField(byte paramByte, Class<?> paramClass, MemberName paramMemberName) throws IllegalAccessException {
/* 1436 */
            checkField(paramByte, paramClass, paramMemberName);
/* 1437 */
            Object localObject = DirectMethodHandle.make(paramClass, paramMemberName);
/* 1438 */
            int i = (MethodHandleNatives.refKindHasReceiver(paramByte)) && (restrictProtectedReceiver(paramMemberName)) ? 1 : 0;
/*      */ 
/* 1440 */
            if (i != 0)
/* 1441 */ localObject = restrictReceiver(paramMemberName, (MethodHandle) localObject, lookupClass());
/* 1442 */
            return localObject;
/*      */
        }

        /*      */
        private MethodHandle getDirectConstructor(Class<?> paramClass, MemberName paramMemberName) throws IllegalAccessException {
/* 1445 */
            assert (paramMemberName.isConstructor());
/* 1446 */
            checkAccess((byte) 8, paramClass, paramMemberName);
/* 1447 */
            assert (!MethodHandleNatives.isCallerSensitive(paramMemberName));
/* 1448 */
            return DirectMethodHandle.make(paramMemberName).setVarargs(paramMemberName);
/*      */
        }

        /*      */
/*      */     MethodHandle linkMethodHandleConstant(byte paramByte, Class<?> paramClass, String paramString, Object paramObject)
/*      */       throws ReflectiveOperationException
/*      */ {
/* 1455 */
            MemberName localMemberName = null;
/* 1456 */
            if ((paramObject instanceof MemberName)) {
/* 1457 */
                localMemberName = (MemberName) paramObject;
/* 1458 */
                if (!localMemberName.isResolved()) throw new InternalError("unresolved MemberName");
/* 1459 */
                assert ((paramString == null) || (paramString.equals(localMemberName.getName())));
/*      */
            }
/*      */
            Object localObject;
/* 1461 */
            if (MethodHandleNatives.refKindIsField(paramByte)) {
/* 1462 */
                localObject = localMemberName != null ? localMemberName : resolveOrFail(paramByte, paramClass, paramString, (Class) paramObject);
/*      */ 
/* 1464 */
                return getDirectField(paramByte, paramClass, (MemberName) localObject);
/* 1465 */
            }
            if (MethodHandleNatives.refKindIsMethod(paramByte)) {
/* 1466 */
                if ((paramClass == MethodHandle.class) && (paramByte == 5)) {
/* 1467 */
                    localObject = findVirtualForMH(paramString, (MethodType) paramObject);
/* 1468 */
                    if (localObject != null) return localObject;
/*      */
                }
/* 1470 */
                localObject = localMemberName != null ? localMemberName : resolveOrFail(paramByte, paramClass, paramString, (MethodType) paramObject);
/*      */ 
/* 1472 */
                return getDirectMethod(paramByte, paramClass, (MemberName) localObject, this.lookupClass);
/* 1473 */
            }
            if (paramByte == 8) {
/* 1474 */
                assert ((paramString == null) || (paramString.equals("<init>")));
/* 1475 */
                localObject = localMemberName != null ? localMemberName : resolveOrFail((byte) 8, paramClass, paramString, (MethodType) paramObject);
/*      */ 
/* 1477 */
                return getDirectConstructor(paramClass, (MemberName) localObject);
/*      */
            }
/*      */ 
/* 1480 */
            throw new ReflectiveOperationException("bad MethodHandle constant #" + paramByte + " " + paramString + " : " + paramObject);
/*      */
        }

        /*      */
/*      */     static
/*      */ {
/*  500 */
            MethodHandles.IMPL_NAMES.getClass();
/*      */
        }
/*      */
    }
/*      */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.MethodHandles
 * JD-Core Version:    0.6.2
 */