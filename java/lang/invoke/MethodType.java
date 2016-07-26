/*      */
package java.lang.invoke;
/*      */ 
/*      */

import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.ObjectStreamField;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import sun.invoke.util.BytecodeDescriptor;
/*      */ import sun.invoke.util.VerifyType;
/*      */ import sun.invoke.util.Wrapper;
/*      */ import sun.misc.Unsafe;

/*      */
/*      */ public final class MethodType
/*      */ implements Serializable
/*      */ {
    /*      */   private static final long serialVersionUID = 292L;
    /*      */   private final Class<?> rtype;
    /*      */   private final Class<?>[] ptypes;
    /*      */   private MethodTypeForm form;
    /*      */   private MethodType wrapAlt;
    /*      */   private Invokers invokers;
    /*      */   static final int MAX_JVM_ARITY = 255;
    /*      */   static final int MAX_MH_ARITY = 254;
    /*      */   static final int MAX_MH_INVOKER_ARITY = 253;
    /*      */   static final WeakInternSet internTable;
    /*      */   static final Class<?>[] NO_PTYPES;
    /*      */   private static final MethodType[] objectOnlyTypes;
    /*      */   private static final ObjectStreamField[] serialPersistentFields;
    /*      */   private static final long rtypeOffset;
    /*      */   private static final long ptypesOffset;

    /*      */
/*      */
    private MethodType(Class<?> paramClass, Class<?>[] paramArrayOfClass)
/*      */ {
/*  102 */
        checkRtype(paramClass);
/*  103 */
        checkPtypes(paramArrayOfClass);
/*  104 */
        this.rtype = paramClass;
/*  105 */
        this.ptypes = paramArrayOfClass;
/*      */
    }

    /*      */   MethodTypeForm form() {
/*  108 */
        return this.form;
    }

    /*  109 */   Class<?> rtype() {
        return this.rtype;
    }

    /*  110 */   Class<?>[] ptypes() {
        return this.ptypes;
    }

    /*      */   void setForm(MethodTypeForm paramMethodTypeForm) {
/*  112 */
        this.form = paramMethodTypeForm;
/*      */
    }

    /*      */
/*      */
    private static void checkRtype(Class<?> paramClass)
/*      */ {
/*  145 */
        paramClass.equals(paramClass);
/*      */
    }

    /*      */
    private static int checkPtype(Class<?> paramClass) {
/*  148 */
        paramClass.getClass();
/*  149 */
        if (paramClass == Void.TYPE)
/*  150 */ throw MethodHandleStatics.newIllegalArgumentException("parameter type cannot be void");
/*  151 */
        if ((paramClass == Double.TYPE) || (paramClass == Long.TYPE)) return 1;
/*  152 */
        return 0;
/*      */
    }

    /*      */
/*      */
    private static int checkPtypes(Class<?>[] paramArrayOfClass) {
/*  156 */
        int i = 0;
/*  157 */
        for (Class<?> localClass : paramArrayOfClass) {
/*  158 */
            i += checkPtype(localClass);
/*      */
        }
/*  160 */
        checkSlotCount(paramArrayOfClass.length + i);
/*  161 */
        return i;
/*      */
    }

    /*      */
/*      */
    static void checkSlotCount(int paramInt)
/*      */ {
/*  166 */
        if ((paramInt & 0xFF) != paramInt)
/*  167 */ throw MethodHandleStatics.newIllegalArgumentException("bad parameter count " + paramInt);
/*      */
    }

    /*      */
/*  170 */
    private static IndexOutOfBoundsException newIndexOutOfBoundsException(Object paramObject) {
        if ((paramObject instanceof Integer)) paramObject = "bad index: " + paramObject;
/*  171 */
        return new IndexOutOfBoundsException(paramObject.toString());
/*      */
    }

    /*      */
/*      */
    public static MethodType methodType(Class<?> paramClass, Class<?>[] paramArrayOfClass)
/*      */ {
/*  188 */
        return makeImpl(paramClass, paramArrayOfClass, false);
/*      */
    }

    /*      */
/*      */
    public static MethodType methodType(Class<?> paramClass, List<Class<?>> paramList)
/*      */ {
/*  200 */
        boolean bool = false;
/*  201 */
        return makeImpl(paramClass, listToArray(paramList), bool);
/*      */
    }

    /*      */
/*      */
    private static Class<?>[] listToArray(List<Class<?>> paramList)
/*      */ {
/*  206 */
        checkSlotCount(paramList.size());
/*  207 */
        return (Class[]) paramList.toArray(NO_PTYPES);
/*      */
    }

    /*      */
/*      */
    public static MethodType methodType(Class<?> paramClass1, Class<?> paramClass2, Class<?>[] paramArrayOfClass)
/*      */ {
/*  220 */
        Class[] arrayOfClass = new Class[1 + paramArrayOfClass.length];
/*  221 */
        arrayOfClass[0] = paramClass2;
/*  222 */
        System.arraycopy(paramArrayOfClass, 0, arrayOfClass, 1, paramArrayOfClass.length);
/*  223 */
        return makeImpl(paramClass1, arrayOfClass, true);
/*      */
    }

    /*      */
/*      */
    public static MethodType methodType(Class<?> paramClass)
/*      */ {
/*  235 */
        return makeImpl(paramClass, NO_PTYPES, true);
/*      */
    }

    /*      */
/*      */
    public static MethodType methodType(Class<?> paramClass1, Class<?> paramClass2)
/*      */ {
/*  248 */
        return makeImpl(paramClass1, new Class[]{paramClass2}, true);
/*      */
    }

    /*      */
/*      */
    public static MethodType methodType(Class<?> paramClass, MethodType paramMethodType)
/*      */ {
/*  260 */
        return makeImpl(paramClass, paramMethodType.ptypes, true);
/*      */
    }

    /*      */
/*      */
    static MethodType makeImpl(Class<?> paramClass, Class<?>[] paramArrayOfClass, boolean paramBoolean)
/*      */ {
/*  272 */
        if (paramArrayOfClass.length == 0) {
/*  273 */
            paramArrayOfClass = NO_PTYPES;
            paramBoolean = true;
/*      */
        }
/*  275 */
        MethodType localMethodType1 = new MethodType(paramClass, paramArrayOfClass);
/*  276 */
        MethodType localMethodType2 = internTable.get(localMethodType1);
/*  277 */
        if (localMethodType2 != null)
/*  278 */ return localMethodType2;
/*  279 */
        if (!paramBoolean)
/*      */ {
/*  281 */
            localMethodType1 = new MethodType(paramClass, (Class[]) paramArrayOfClass.clone());
/*      */
        }
/*  283 */
        MethodTypeForm localMethodTypeForm = MethodTypeForm.findForm(localMethodType1);
/*  284 */
        localMethodType1.form = localMethodTypeForm;
/*  285 */
        return internTable.add(localMethodType1);
/*      */
    }

    /*      */
/*      */
    public static MethodType genericMethodType(int paramInt, boolean paramBoolean)
/*      */ {
/*  303 */
        checkSlotCount(paramInt);
/*  304 */
        int i = !paramBoolean ? 0 : 1;
/*  305 */
        int j = paramInt * 2 + i;
/*  306 */
        if (j < objectOnlyTypes.length) {
/*  307 */
            localMethodType = objectOnlyTypes[j];
/*  308 */
            if (localMethodType != null) return localMethodType;
/*      */
        }
/*  310 */
        Class[] arrayOfClass = new Class[paramInt + i];
/*  311 */
        Arrays.fill(arrayOfClass, Object.class);
/*  312 */
        if (i != 0) arrayOfClass[paramInt] =[Ljava.lang.Object.class;
/*  313 */
        MethodType localMethodType = makeImpl(Object.class, arrayOfClass, true);
/*  314 */
        if (j < objectOnlyTypes.length) {
/*  315 */
            objectOnlyTypes[j] = localMethodType;
/*      */
        }
/*  317 */
        return localMethodType;
/*      */
    }

    /*      */
/*      */
    public static MethodType genericMethodType(int paramInt)
/*      */ {
/*  331 */
        return genericMethodType(paramInt, false);
/*      */
    }

    /*      */
/*      */
    public MethodType changeParameterType(int paramInt, Class<?> paramClass)
/*      */ {
/*  345 */
        if (parameterType(paramInt) == paramClass) return this;
/*  346 */
        checkPtype(paramClass);
/*  347 */
        Class[] arrayOfClass = (Class[]) this.ptypes.clone();
/*  348 */
        arrayOfClass[paramInt] = paramClass;
/*  349 */
        return makeImpl(this.rtype, arrayOfClass, true);
/*      */
    }

    /*      */
/*      */
    public MethodType insertParameterTypes(int paramInt, Class<?>[] paramArrayOfClass)
/*      */ {
/*  364 */
        int i = this.ptypes.length;
/*  365 */
        if ((paramInt < 0) || (paramInt > i))
/*  366 */ throw newIndexOutOfBoundsException(Integer.valueOf(paramInt));
/*  367 */
        int j = checkPtypes(paramArrayOfClass);
/*  368 */
        checkSlotCount(parameterSlotCount() + paramArrayOfClass.length + j);
/*  369 */
        int k = paramArrayOfClass.length;
/*  370 */
        if (k == 0) return this;
/*  371 */
        Class[] arrayOfClass = (Class[]) Arrays.copyOfRange(this.ptypes, 0, i + k);
/*  372 */
        System.arraycopy(arrayOfClass, paramInt, arrayOfClass, paramInt + k, i - paramInt);
/*  373 */
        System.arraycopy(paramArrayOfClass, 0, arrayOfClass, paramInt, k);
/*  374 */
        return makeImpl(this.rtype, arrayOfClass, true);
/*      */
    }

    /*      */
/*      */
    public MethodType appendParameterTypes(Class<?>[] paramArrayOfClass)
/*      */ {
/*  387 */
        return insertParameterTypes(parameterCount(), paramArrayOfClass);
/*      */
    }

    /*      */
/*      */
    public MethodType insertParameterTypes(int paramInt, List<Class<?>> paramList)
/*      */ {
/*  402 */
        return insertParameterTypes(paramInt, listToArray(paramList));
/*      */
    }

    /*      */
/*      */
    public MethodType appendParameterTypes(List<Class<?>> paramList)
/*      */ {
/*  415 */
        return insertParameterTypes(parameterCount(), paramList);
/*      */
    }

    /*      */
/*      */   MethodType replaceParameterTypes(int paramInt1, int paramInt2, Class<?>[] paramArrayOfClass)
/*      */ {
/*  433 */
        if (paramInt1 == paramInt2)
/*  434 */ return insertParameterTypes(paramInt1, paramArrayOfClass);
/*  435 */
        int i = this.ptypes.length;
/*  436 */
        if ((0 > paramInt1) || (paramInt1 > paramInt2) || (paramInt2 > i))
/*  437 */ throw newIndexOutOfBoundsException("start=" + paramInt1 + " end=" + paramInt2);
/*  438 */
        int j = paramArrayOfClass.length;
/*  439 */
        if (j == 0)
/*  440 */ return dropParameterTypes(paramInt1, paramInt2);
/*  441 */
        return dropParameterTypes(paramInt1, paramInt2).insertParameterTypes(paramInt1, paramArrayOfClass);
/*      */
    }

    /*      */
/*      */
    public MethodType dropParameterTypes(int paramInt1, int paramInt2)
/*      */ {
/*  455 */
        int i = this.ptypes.length;
/*  456 */
        if ((0 > paramInt1) || (paramInt1 > paramInt2) || (paramInt2 > i))
/*  457 */ throw newIndexOutOfBoundsException("start=" + paramInt1 + " end=" + paramInt2);
/*  458 */
        if (paramInt1 == paramInt2) return this;
/*      */
        Class[] arrayOfClass;
/*  460 */
        if (paramInt1 == 0) {
/*  461 */
            if (paramInt2 == i)
/*      */ {
/*  463 */
                arrayOfClass = NO_PTYPES;
/*      */
            }
/*      */
            else {
/*  466 */
                arrayOfClass = (Class[]) Arrays.copyOfRange(this.ptypes, paramInt2, i);
/*      */
            }
/*      */
        }
/*  469 */
        else if (paramInt2 == i)
/*      */ {
/*  471 */
            arrayOfClass = (Class[]) Arrays.copyOfRange(this.ptypes, 0, paramInt1);
/*      */
        } else {
/*  473 */
            int j = i - paramInt2;
/*  474 */
            arrayOfClass = (Class[]) Arrays.copyOfRange(this.ptypes, 0, paramInt1 + j);
/*  475 */
            System.arraycopy(this.ptypes, paramInt2, arrayOfClass, paramInt1, j);
/*      */
        }
/*      */ 
/*  478 */
        return makeImpl(this.rtype, arrayOfClass, true);
/*      */
    }

    /*      */
/*      */
    public MethodType changeReturnType(Class<?> paramClass)
/*      */ {
/*  489 */
        if (returnType() == paramClass) return this;
/*  490 */
        return makeImpl(paramClass, this.ptypes, true);
/*      */
    }

    /*      */
/*      */
    public boolean hasPrimitives()
/*      */ {
/*  499 */
        return this.form.hasPrimitives();
/*      */
    }

    /*      */
/*      */
    public boolean hasWrappers()
/*      */ {
/*  510 */
        return unwrap() != this;
/*      */
    }

    /*      */
/*      */
    public MethodType erase()
/*      */ {
/*  520 */
        return this.form.erasedType();
/*      */
    }

    /*      */
/*      */   MethodType basicType()
/*      */ {
/*  530 */
        return this.form.basicType();
/*      */
    }

    /*      */
/*      */   MethodType invokerType()
/*      */ {
/*  537 */
        return insertParameterTypes(0, new Class[]{MethodHandle.class});
/*      */
    }

    /*      */
/*      */
    public MethodType generic()
/*      */ {
/*  548 */
        return genericMethodType(parameterCount());
/*      */
    }

    /*      */
/*      */
    public MethodType wrap()
/*      */ {
/*  561 */
        return hasPrimitives() ? wrapWithPrims(this) : this;
/*      */
    }

    /*      */
/*      */
    public MethodType unwrap()
/*      */ {
/*  572 */
        MethodType localMethodType = !hasPrimitives() ? this : wrapWithPrims(this);
/*  573 */
        return unwrapWithNoPrims(localMethodType);
/*      */
    }

    /*      */
/*      */
    private static MethodType wrapWithPrims(MethodType paramMethodType) {
/*  577 */
        assert (paramMethodType.hasPrimitives());
/*  578 */
        MethodType localMethodType = paramMethodType.wrapAlt;
/*  579 */
        if (localMethodType == null)
/*      */ {
/*  581 */
            localMethodType = MethodTypeForm.canonicalize(paramMethodType, 2, 2);
/*  582 */
            assert (localMethodType != null);
/*  583 */
            paramMethodType.wrapAlt = localMethodType;
/*      */
        }
/*  585 */
        return localMethodType;
/*      */
    }

    /*      */
/*      */
    private static MethodType unwrapWithNoPrims(MethodType paramMethodType) {
/*  589 */
        assert (!paramMethodType.hasPrimitives());
/*  590 */
        MethodType localMethodType = paramMethodType.wrapAlt;
/*  591 */
        if (localMethodType == null)
/*      */ {
/*  593 */
            localMethodType = MethodTypeForm.canonicalize(paramMethodType, 3, 3);
/*  594 */
            if (localMethodType == null)
/*  595 */ localMethodType = paramMethodType;
/*  596 */
            paramMethodType.wrapAlt = localMethodType;
/*      */
        }
/*  598 */
        return localMethodType;
/*      */
    }

    /*      */
/*      */
    public Class<?> parameterType(int paramInt)
/*      */ {
/*  608 */
        return this.ptypes[paramInt];
/*      */
    }

    /*      */
/*      */
    public int parameterCount()
/*      */ {
/*  615 */
        return this.ptypes.length;
/*      */
    }

    /*      */
/*      */
    public Class<?> returnType()
/*      */ {
/*  622 */
        return this.rtype;
/*      */
    }

    /*      */
/*      */
    public List<Class<?>> parameterList()
/*      */ {
/*  631 */
        return Collections.unmodifiableList(Arrays.asList((Object[]) this.ptypes.clone()));
/*      */
    }

    /*      */
/*      */   Class<?> lastParameterType() {
/*  635 */
        int i = this.ptypes.length;
/*  636 */
        return i == 0 ? Void.TYPE : this.ptypes[(i - 1)];
/*      */
    }

    /*      */
/*      */
    public Class<?>[] parameterArray()
/*      */ {
/*  645 */
        return (Class[]) this.ptypes.clone();
/*      */
    }

    /*      */
/*      */
    public boolean equals(Object paramObject)
/*      */ {
/*  657 */
        return (this == paramObject) || (((paramObject instanceof MethodType)) && (equals((MethodType) paramObject)));
/*      */
    }

    /*      */
/*      */
    private boolean equals(MethodType paramMethodType) {
/*  661 */
        return (this.rtype == paramMethodType.rtype) && (Arrays.equals(this.ptypes, paramMethodType.ptypes));
/*      */
    }

    /*      */
/*      */
    public int hashCode()
/*      */ {
/*  677 */
        int i = 31 + this.rtype.hashCode();
/*  678 */
        for (Class localClass : this.ptypes)
/*  679 */
            i = 31 * i + localClass.hashCode();
/*  680 */
        return i;
/*      */
    }

    /*      */
/*      */
    public String toString()
/*      */ {
/*  695 */
        StringBuilder localStringBuilder = new StringBuilder();
/*  696 */
        localStringBuilder.append("(");
/*  697 */
        for (int i = 0; i < this.ptypes.length; i++) {
/*  698 */
            if (i > 0) localStringBuilder.append(",");
/*  699 */
            localStringBuilder.append(this.ptypes[i].getSimpleName());
/*      */
        }
/*  701 */
        localStringBuilder.append(")");
/*  702 */
        localStringBuilder.append(this.rtype.getSimpleName());
/*  703 */
        return localStringBuilder.toString();
/*      */
    }

    /*      */
/*      */   boolean isViewableAs(MethodType paramMethodType)
/*      */ {
/*  709 */
        if (!VerifyType.isNullConversion(returnType(), paramMethodType.returnType()))
/*  710 */ return false;
/*  711 */
        int i = parameterCount();
/*  712 */
        if (i != paramMethodType.parameterCount())
/*  713 */ return false;
/*  714 */
        for (int j = 0; j < i; j++) {
/*  715 */
            if (!VerifyType.isNullConversion(paramMethodType.parameterType(j), parameterType(j)))
/*  716 */ return false;
/*      */
        }
/*  718 */
        return true;
/*      */
    }

    /*      */
/*      */   boolean isCastableTo(MethodType paramMethodType) {
/*  722 */
        int i = parameterCount();
/*  723 */
        if (i != paramMethodType.parameterCount())
/*  724 */ return false;
/*  725 */
        return true;
/*      */
    }

    /*      */
/*      */   boolean isConvertibleTo(MethodType paramMethodType) {
/*  729 */
        if (!canConvert(returnType(), paramMethodType.returnType()))
/*  730 */ return false;
/*  731 */
        int i = parameterCount();
/*  732 */
        if (i != paramMethodType.parameterCount())
/*  733 */ return false;
/*  734 */
        for (int j = 0; j < i; j++) {
/*  735 */
            if (!canConvert(paramMethodType.parameterType(j), parameterType(j)))
/*  736 */ return false;
/*      */
        }
/*  738 */
        return true;
/*      */
    }

    /*      */
/*      */
    static boolean canConvert(Class<?> paramClass1, Class<?> paramClass2)
/*      */ {
/*  743 */
        if ((paramClass1 == paramClass2) || (paramClass2 == Object.class)) return true;
/*      */
        Wrapper localWrapper;
/*  745 */
        if (paramClass1.isPrimitive())
/*      */ {
/*  748 */
            if (paramClass1 == Void.TYPE) return true;
/*  749 */
            localWrapper = Wrapper.forPrimitiveType(paramClass1);
/*  750 */
            if (paramClass2.isPrimitive())
/*      */ {
/*  752 */
                return Wrapper.forPrimitiveType(paramClass2).isConvertibleFrom(localWrapper);
/*      */
            }
/*      */ 
/*  755 */
            return paramClass2.isAssignableFrom(localWrapper.wrapperType());
/*      */
        }
/*  757 */
        if (paramClass2.isPrimitive())
/*      */ {
/*  759 */
            if (paramClass2 == Void.TYPE) return true;
/*  760 */
            localWrapper = Wrapper.forPrimitiveType(paramClass2);
/*      */ 
/*  767 */
            if (paramClass1.isAssignableFrom(localWrapper.wrapperType())) {
/*  768 */
                return true;
/*      */
            }
/*      */ 
/*  774 */
            if ((Wrapper.isWrapperType(paramClass1)) && (localWrapper.isConvertibleFrom(Wrapper.forWrapperType(paramClass1))))
/*      */ {
/*  777 */
                return true;
/*      */
            }
/*      */ 
/*  786 */
            return false;
/*      */
        }
/*      */ 
/*  789 */
        return true;
/*      */
    }

    /*      */
/*      */   int parameterSlotCount()
/*      */ {
/*  806 */
        return this.form.parameterSlotCount();
/*      */
    }

    /*      */
/*      */   Invokers invokers() {
/*  810 */
        Invokers localInvokers = this.invokers;
/*  811 */
        if (localInvokers != null) return localInvokers;
/*  812 */
        this.invokers = (localInvokers = new Invokers(this));
/*  813 */
        return localInvokers;
/*      */
    }

    /*      */
/*      */   int parameterSlotDepth(int paramInt)
/*      */ {
/*  840 */
        if ((paramInt < 0) || (paramInt > this.ptypes.length))
/*  841 */ parameterType(paramInt);
/*  842 */
        return this.form.parameterToArgSlot(paramInt - 1);
/*      */
    }

    /*      */
/*      */   int returnSlotCount()
/*      */ {
/*  856 */
        return this.form.returnSlotCount();
/*      */
    }

    /*      */
/*      */
    public static MethodType fromMethodDescriptorString(String paramString, ClassLoader paramClassLoader)
/*      */     throws IllegalArgumentException, TypeNotPresentException
/*      */ {
/*  882 */
        if ((!paramString.startsWith("(")) || (paramString.indexOf(')') < 0) || (paramString.indexOf('.') >= 0))
/*      */ {
/*  885 */
            throw new IllegalArgumentException("not a method descriptor: " + paramString);
/*  886 */
        }
        List localList = BytecodeDescriptor.parseMethod(paramString, paramClassLoader);
/*  887 */
        Class localClass = (Class) localList.remove(localList.size() - 1);
/*  888 */
        checkSlotCount(localList.size());
/*  889 */
        Class[] arrayOfClass = listToArray(localList);
/*  890 */
        return makeImpl(localClass, arrayOfClass, true);
/*      */
    }

    /*      */
/*      */
    public String toMethodDescriptorString()
/*      */ {
/*  907 */
        return BytecodeDescriptor.unparse(this);
/*      */
    }

    /*      */
/*      */
    static String toFieldDescriptorString(Class<?> paramClass) {
/*  911 */
        return BytecodeDescriptor.unparse(paramClass);
/*      */
    }

    /*      */
/*      */
    private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */ {
/*  941 */
        paramObjectOutputStream.defaultWriteObject();
/*  942 */
        paramObjectOutputStream.writeObject(returnType());
/*  943 */
        paramObjectOutputStream.writeObject(parameterArray());
/*      */
    }

    /*      */
/*      */
    private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */ {
/*  959 */
        paramObjectInputStream.defaultReadObject();
/*      */ 
/*  961 */
        Class localClass = (Class) paramObjectInputStream.readObject();
/*  962 */
        Class[] arrayOfClass = (Class[]) paramObjectInputStream.readObject();
/*      */ 
/*  966 */
        checkRtype(localClass);
/*  967 */
        checkPtypes(arrayOfClass);
/*      */ 
/*  969 */
        arrayOfClass = (Class[]) arrayOfClass.clone();
/*  970 */
        MethodType_init(localClass, arrayOfClass);
/*      */
    }

    /*      */
/*      */
    private MethodType()
/*      */ {
/*  978 */
        this.rtype = null;
/*  979 */
        this.ptypes = null;
/*      */
    }

    /*      */
/*      */
    private void MethodType_init(Class<?> paramClass, Class<?>[] paramArrayOfClass)
/*      */ {
/*  984 */
        checkRtype(paramClass);
/*  985 */
        checkPtypes(paramArrayOfClass);
/*  986 */
        MethodHandleStatics.UNSAFE.putObject(this, rtypeOffset, paramClass);
/*  987 */
        MethodHandleStatics.UNSAFE.putObject(this, ptypesOffset, paramArrayOfClass);
/*      */
    }

    /*      */
/*      */
    private Object readResolve()
/*      */ {
/* 1012 */
        return methodType(this.rtype, this.ptypes);
/*      */
    }

    /*      */
/*      */   static
/*      */ {
/*  174 */
        internTable = new WeakInternSet();
/*      */ 
/*  176 */
        NO_PTYPES = new Class[0];
/*      */ 
/*  287 */
        objectOnlyTypes = new MethodType[20];
/*      */ 
/*  919 */
        serialPersistentFields = new ObjectStreamField[0];
/*      */
        try
/*      */ {
/*  994 */
            rtypeOffset = MethodHandleStatics.UNSAFE.objectFieldOffset(MethodType.class.getDeclaredField("rtype"));
/*      */ 
/*  996 */
            ptypesOffset = MethodHandleStatics.UNSAFE.objectFieldOffset(MethodType.class.getDeclaredField("ptypes"));
/*      */
        }
/*      */ catch (Exception localException) {
/*  999 */
            throw new Error(localException);
/*      */
        }
/*      */
    }

    /*      */
/*      */   private static class WeakInternSet
/*      */ {
        /*      */     private static final int DEFAULT_INITIAL_CAPACITY = 16;
        /*      */     private static final int MAXIMUM_CAPACITY = 1073741824;
        /*      */     private static final float DEFAULT_LOAD_FACTOR = 0.75F;
        /*      */     private Entry[] table;
        /*      */     private int size;
        /*      */     private int threshold;
        /*      */     private final float loadFactor;
        /* 1051 */     private final ReferenceQueue<Object> queue = new ReferenceQueue();

        /*      */
/*      */
        private Entry[] newTable(int paramInt) {
/* 1054 */
            return new Entry[paramInt];
/*      */
        }

        /*      */
/*      */     WeakInternSet()
/*      */ {
/* 1062 */
            this.loadFactor = 0.75F;
/* 1063 */
            this.threshold = 16;
/* 1064 */
            this.table = newTable(16);
/*      */
        }

        /*      */
/*      */
        private static int hash(int paramInt)
/*      */ {
/* 1080 */
            paramInt ^= paramInt >>> 20 ^ paramInt >>> 12;
/* 1081 */
            return paramInt ^ paramInt >>> 7 ^ paramInt >>> 4;
/*      */
        }

        /*      */
/*      */
        private static boolean eq(Object paramObject1, Object paramObject2)
/*      */ {
/* 1092 */
            return (paramObject1 == paramObject2) || (paramObject1.equals(paramObject2));
/*      */
        }

        /*      */
/*      */
        private static int indexFor(int paramInt1, int paramInt2)
/*      */ {
/* 1102 */
            return paramInt1 & paramInt2 - 1;
/*      */
        }

        /*      */
/*      */
        private void expungeStaleEntries()
/*      */ {
/*      */
            Reference localReference;
/* 1109 */
            while ((localReference = this.queue.poll()) != null)
/* 1110 */ synchronized (this.queue) {
/* 1111 */
                Entry localEntry1 = (Entry) localReference;
/* 1112 */
                int i = indexFor(localEntry1.hash, this.table.length);
/* 1113 */
                Object localObject1 = this.table[i];
/* 1114 */
                Object localObject2 = localObject1;
/* 1115 */
                while (localObject2 != null) {
/* 1116 */
                    Entry localEntry2 = localObject2.next;
/* 1117 */
                    if (localObject2 == localEntry1) {
/* 1118 */
                        if (localObject1 == localEntry1)
/* 1119 */ this.table[i] = localEntry2;
/*      */
                        else
/* 1121 */                 ((Entry) localObject1).next = localEntry2;
/* 1122 */
                        localEntry1.next = null;
/* 1123 */
                        this.size -= 1;
/* 1124 */
                        break;
/*      */
                    }
/* 1126 */
                    localObject1 = localObject2;
/* 1127 */
                    localObject2 = localEntry2;
/*      */
                }
/*      */
            }
/*      */
        }

        /*      */
/*      */
        private Entry[] getTable()
/*      */ {
/* 1138 */
            expungeStaleEntries();
/* 1139 */
            return this.table;
/*      */
        }

        /*      */
/*      */
        synchronized MethodType get(MethodType paramMethodType)
/*      */ {
/* 1155 */
            int i = hash(paramMethodType.hashCode());
/* 1156 */
            Entry[] arrayOfEntry = getTable();
/* 1157 */
            int j = indexFor(i, arrayOfEntry.length);
/* 1158 */
            Entry localEntry = arrayOfEntry[j];
/*      */ 
/* 1160 */
            while (localEntry != null)
/*      */ {
/*      */
                MethodType localMethodType;
/* 1161 */
                if ((localEntry.hash == i) && (eq(paramMethodType, localMethodType = (MethodType) localEntry.get())))
/* 1162 */ return localMethodType;
/* 1163 */
                localEntry = localEntry.next;
/*      */
            }
/* 1165 */
            return null;
/*      */
        }

        /*      */
/*      */
        synchronized MethodType add(MethodType paramMethodType)
/*      */ {
/* 1178 */
            int i = hash(paramMethodType.hashCode());
/* 1179 */
            Entry[] arrayOfEntry = getTable();
/* 1180 */
            int j = indexFor(i, arrayOfEntry.length);
/*      */ 
/* 1182 */
            for (Entry localEntry = arrayOfEntry[j]; localEntry != null; localEntry = localEntry.next)
/*      */ {
/*      */
                MethodType localMethodType;
/* 1183 */
                if ((i == localEntry.hash) && (eq(paramMethodType, localMethodType = (MethodType) localEntry.get()))) {
/* 1184 */
                    return localMethodType;
/*      */
                }
/*      */
            }
/* 1187 */
            localEntry = arrayOfEntry[j];
/* 1188 */
            arrayOfEntry[j] = new Entry(paramMethodType, this.queue, i, localEntry);
/* 1189 */
            if (++this.size >= this.threshold)
/* 1190 */ resize(arrayOfEntry.length * 2);
/* 1191 */
            return paramMethodType;
/*      */
        }

        /*      */
/*      */
        private void resize(int paramInt)
/*      */ {
/* 1209 */
            Entry[] arrayOfEntry1 = getTable();
/* 1210 */
            int i = arrayOfEntry1.length;
/* 1211 */
            if (i == 1073741824) {
/* 1212 */
                this.threshold = 2147483647;
/* 1213 */
                return;
/*      */
            }
/*      */ 
/* 1216 */
            Entry[] arrayOfEntry2 = newTable(paramInt);
/* 1217 */
            transfer(arrayOfEntry1, arrayOfEntry2);
/* 1218 */
            this.table = arrayOfEntry2;
/*      */ 
/* 1225 */
            if (this.size >= this.threshold / 2) {
/* 1226 */
                this.threshold = ((int) (paramInt * this.loadFactor));
/*      */
            } else {
/* 1228 */
                expungeStaleEntries();
/* 1229 */
                transfer(arrayOfEntry2, arrayOfEntry1);
/* 1230 */
                this.table = arrayOfEntry1;
/*      */
            }
/*      */
        }

        /*      */
/*      */
        private void transfer(Entry[] paramArrayOfEntry1, Entry[] paramArrayOfEntry2)
/*      */ {
/* 1240 */
            for (int i = 0; i < paramArrayOfEntry1.length; i++) {
/* 1241 */
                Object localObject = paramArrayOfEntry1[i];
/* 1242 */
                paramArrayOfEntry1[i] = null;
/* 1243 */
                while (localObject != null) {
/* 1244 */
                    Entry localEntry = ((Entry) localObject).next;
/* 1245 */
                    MethodType localMethodType = (MethodType) ((Entry) localObject).get();
/* 1246 */
                    if (localMethodType == null) {
/* 1247 */
                        ((Entry) localObject).next = null;
/* 1248 */
                        this.size -= 1;
/*      */
                    } else {
/* 1250 */
                        int j = indexFor(((Entry) localObject).hash, paramArrayOfEntry2.length);
/* 1251 */
                        ((Entry) localObject).next = paramArrayOfEntry2[j];
/* 1252 */
                        paramArrayOfEntry2[j] = localObject;
/*      */
                    }
/* 1254 */
                    localObject = localEntry;
/*      */
                }
/*      */
            }
/*      */
        }

        /*      */
/*      */     private static class Entry extends WeakReference<MethodType>
/*      */ {
            /*      */       final int hash;
            /*      */ Entry next;

            /*      */
/*      */       Entry(MethodType paramMethodType, ReferenceQueue<Object> paramReferenceQueue, int paramInt, Entry paramEntry)
/*      */ {
/* 1273 */
                super(paramReferenceQueue);
/* 1274 */
                this.hash = paramInt;
/* 1275 */
                this.next = paramEntry;
/*      */
            }
/*      */
        }
/*      */
    }
/*      */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.MethodType
 * JD-Core Version:    0.6.2
 */