/*     */
package java.lang.invoke;
/*     */ 
/*     */

import sun.invoke.util.Wrapper;

/*     */
/*     */ final class MethodTypeForm
/*     */ {
    /*     */   final int[] argToSlotTable;
    /*     */   final int[] slotToArgTable;
    /*     */   final long argCounts;
    /*     */   final long primCounts;
    /*     */   final int vmslots;
    /*     */   final MethodType erasedType;
    /*     */   final MethodType basicType;
    /*     */ MethodHandle genericInvoker;
    /*     */ MethodHandle basicInvoker;
    /*     */ MethodHandle namedFunctionInvoker;
    /*     */   final LambdaForm[] lambdaForms;
    /*     */   static final int LF_INVVIRTUAL = 0;
    /*     */   static final int LF_INVSTATIC = 1;
    /*     */   static final int LF_INVSPECIAL = 2;
    /*     */   static final int LF_NEWINVSPECIAL = 3;
    /*     */   static final int LF_INVINTERFACE = 4;
    /*     */   static final int LF_INVSTATIC_INIT = 5;
    /*     */   static final int LF_INTERPRET = 6;
    /*     */   static final int LF_COUNTER = 7;
    /*     */   static final int LF_REINVOKE = 8;
    /*     */   static final int LF_EX_LINKER = 9;
    /*     */   static final int LF_EX_INVOKER = 10;
    /*     */   static final int LF_GEN_LINKER = 11;
    /*     */   static final int LF_GEN_INVOKER = 12;
    /*     */   static final int LF_CS_LINKER = 13;
    /*     */   static final int LF_LIMIT = 14;
    /*     */   public static final int NO_CHANGE = 0;
    /*     */   public static final int ERASE = 1;
    /*     */   public static final int WRAP = 2;
    /*     */   public static final int UNWRAP = 3;
    /*     */   public static final int INTS = 4;
    /*     */   public static final int LONGS = 5;
    /*     */   public static final int RAW_RETURN = 6;

    /*     */
/*     */
    public MethodType erasedType()
/*     */ {
/*  79 */
        return this.erasedType;
/*     */
    }

    /*     */
/*     */
    public MethodType basicType() {
/*  83 */
        return this.basicType;
/*     */
    }

    /*     */
/*     */
    public LambdaForm cachedLambdaForm(int paramInt) {
/*  87 */
        return this.lambdaForms[paramInt];
/*     */
    }

    /*     */
/*     */
    public LambdaForm setCachedLambdaForm(int paramInt, LambdaForm paramLambdaForm)
/*     */ {
/*  92 */
        return this.lambdaForms[paramInt] = =paramLambdaForm;
/*     */
    }

    /*     */
/*     */
    public MethodHandle basicInvoker() {
/*  96 */
        assert (this.erasedType == this.basicType) : ("erasedType: " + this.erasedType + " != basicType: " + this.basicType);
/*  97 */
        MethodHandle localMethodHandle = this.basicInvoker;
/*  98 */
        if (localMethodHandle != null) return localMethodHandle;
/*  99 */
        localMethodHandle = this.basicType.invokers().makeBasicInvoker();
/* 100 */
        this.basicInvoker = localMethodHandle;
/* 101 */
        return localMethodHandle;
/*     */
    }

    /*     */
/*     */
    protected MethodTypeForm(MethodType paramMethodType)
/*     */ {
/* 110 */
        this.erasedType = paramMethodType;
/*     */ 
/* 112 */
        Class[] arrayOfClass1 = paramMethodType.ptypes();
/* 113 */
        int i = arrayOfClass1.length;
/* 114 */
        int j = i;
/* 115 */
        int k = 1;
/* 116 */
        int m = 1;
/*     */ 
/* 118 */
        int[] arrayOfInt1 = null;
        int[] arrayOfInt2 = null;
/*     */ 
/* 121 */
        int n = 0;
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
/* 122 */
        Class[] arrayOfClass2 = arrayOfClass1;
/* 123 */
        Class[] arrayOfClass3 = arrayOfClass2;
/*     */
        Wrapper localWrapper1;
/* 124 */
        for (int i4 = 0; i4 < arrayOfClass2.length; i4++) {
/* 125 */
            localClass2 = arrayOfClass2[i4];
/* 126 */
            if (localClass2 != Object.class) {
/* 127 */
                n++;
/* 128 */
                localWrapper1 = Wrapper.forPrimitiveType(localClass2);
/* 129 */
                if (localWrapper1.isDoubleWord()) i1++;
/* 130 */
                if ((localWrapper1.isSubwordOrInt()) && (localClass2 != Integer.TYPE)) {
/* 131 */
                    if (arrayOfClass3 == arrayOfClass2)
/* 132 */ arrayOfClass3 = (Class[]) arrayOfClass3.clone();
/* 133 */
                    arrayOfClass3[i4] = Integer.TYPE;
/*     */
                }
/*     */
            }
/*     */
        }
/* 137 */
        j += i1;
/* 138 */
        Class localClass1 = paramMethodType.returnType();
/* 139 */
        Class localClass2 = localClass1;
/* 140 */
        if (localClass1 != Object.class) {
/* 141 */
            i2++;
/* 142 */
            localWrapper1 = Wrapper.forPrimitiveType(localClass1);
/* 143 */
            if (localWrapper1.isDoubleWord()) i3++;
/* 144 */
            if ((localWrapper1.isSubwordOrInt()) && (localClass1 != Integer.TYPE)) {
/* 145 */
                localClass2 = Integer.TYPE;
/*     */
            }
/* 147 */
            if (localClass1 == Void.TYPE)
/* 148 */ k = m = 0;
/*     */
            else
/* 150 */         m += i3;
/*     */
        }
/* 152 */
        if ((arrayOfClass2 == arrayOfClass3) && (localClass2 == localClass1))
/* 153 */ this.basicType = paramMethodType;
/*     */
        else
/* 155 */       this.basicType = MethodType.makeImpl(localClass2, arrayOfClass3, true);
/*     */
        int i5;
/*     */
        int i6;
/* 157 */
        if (i1 != 0) {
/* 158 */
            i5 = i + i1;
/* 159 */
            arrayOfInt2 = new int[i5 + 1];
/* 160 */
            arrayOfInt1 = new int[1 + i];
/* 161 */
            arrayOfInt1[0] = i5;
/* 162 */
            for (i6 = 0; i6 < arrayOfClass2.length; i6++) {
/* 163 */
                Class localClass3 = arrayOfClass2[i6];
/* 164 */
                Wrapper localWrapper2 = Wrapper.forBasicType(localClass3);
/* 165 */
                if (localWrapper2.isDoubleWord()) i5--;
/* 166 */
                i5--;
/* 167 */
                arrayOfInt2[i5] = (i6 + 1);
/* 168 */
                arrayOfInt1[(1 + i6)] = i5;
/*     */
            }
/* 170 */
            assert (i5 == 0);
/*     */
        }
/* 172 */
        this.primCounts = pack(i3, i2, i1, n);
/* 173 */
        this.argCounts = pack(m, k, j, i);
/* 174 */
        if (arrayOfInt2 == null) {
/* 175 */
            i5 = i;
/* 176 */
            arrayOfInt2 = new int[i5 + 1];
/* 177 */
            arrayOfInt1 = new int[1 + i];
/* 178 */
            arrayOfInt1[0] = i5;
/* 179 */
            for (i6 = 0; i6 < i; i6++) {
/* 180 */
                i5--;
/* 181 */
                arrayOfInt2[i5] = (i6 + 1);
/* 182 */
                arrayOfInt1[(1 + i6)] = i5;
/*     */
            }
/*     */
        }
/* 185 */
        this.argToSlotTable = arrayOfInt1;
/* 186 */
        this.slotToArgTable = arrayOfInt2;
/*     */ 
/* 188 */
        if (j >= 256) throw MethodHandleStatics.newIllegalArgumentException("too many arguments");
/*     */ 
/* 191 */
        this.vmslots = parameterSlotCount();
/*     */ 
/* 193 */
        if (this.basicType == paramMethodType)
/* 194 */ this.lambdaForms = new LambdaForm[14];
/*     */
        else
/* 196 */       this.lambdaForms = null;
/*     */
    }

    /*     */
/*     */
    private static long pack(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */ {
/* 201 */
        assert (((paramInt1 | paramInt2 | paramInt3 | paramInt4) & 0xFFFF0000) == 0);
/* 202 */
        long l1 = paramInt1 << 16 | paramInt2;
        long l2 = paramInt3 << 16 | paramInt4;
/* 203 */
        return l1 << 32 | l2;
/*     */
    }

    /*     */
    private static char unpack(long paramLong, int paramInt) {
/* 206 */
        assert (paramInt <= 3);
/* 207 */
        return (char) (int) (paramLong >> (3 - paramInt) * 16);
/*     */
    }

    /*     */
/*     */
    public int parameterCount() {
/* 211 */
        return unpack(this.argCounts, 3);
/*     */
    }

    /*     */
    public int parameterSlotCount() {
/* 214 */
        return unpack(this.argCounts, 2);
/*     */
    }

    /*     */
    public int returnCount() {
/* 217 */
        return unpack(this.argCounts, 1);
/*     */
    }

    /*     */
    public int returnSlotCount() {
/* 220 */
        return unpack(this.argCounts, 0);
/*     */
    }

    /*     */
    public int primitiveParameterCount() {
/* 223 */
        return unpack(this.primCounts, 3);
/*     */
    }

    /*     */
    public int longPrimitiveParameterCount() {
/* 226 */
        return unpack(this.primCounts, 2);
/*     */
    }

    /*     */
    public int primitiveReturnCount() {
/* 229 */
        return unpack(this.primCounts, 1);
/*     */
    }

    /*     */
    public int longPrimitiveReturnCount() {
/* 232 */
        return unpack(this.primCounts, 0);
/*     */
    }

    /*     */
    public boolean hasPrimitives() {
/* 235 */
        return this.primCounts != 0L;
/*     */
    }

    /*     */
    public boolean hasNonVoidPrimitives() {
/* 238 */
        if (this.primCounts == 0L) return false;
/* 239 */
        if (primitiveParameterCount() != 0) return true;
/* 240 */
        return (primitiveReturnCount() != 0) && (returnCount() != 0);
/*     */
    }

    /*     */
    public boolean hasLongPrimitives() {
/* 243 */
        return (longPrimitiveParameterCount() | longPrimitiveReturnCount()) != 0;
/*     */
    }

    /*     */
    public int parameterToArgSlot(int paramInt) {
/* 246 */
        return this.argToSlotTable[(1 + paramInt)];
/*     */
    }

    /*     */
/*     */
    public int argSlotToParameter(int paramInt)
/*     */ {
/* 252 */
        return this.slotToArgTable[paramInt] - 1;
/*     */
    }

    /*     */
/*     */
    static MethodTypeForm findForm(MethodType paramMethodType) {
/* 256 */
        MethodType localMethodType = canonicalize(paramMethodType, 1, 1);
/* 257 */
        if (localMethodType == null)
/*     */ {
/* 259 */
            return new MethodTypeForm(paramMethodType);
/*     */
        }
/*     */ 
/* 262 */
        return localMethodType.form();
/*     */
    }

    /*     */
/*     */
    public static MethodType canonicalize(MethodType paramMethodType, int paramInt1, int paramInt2)
/*     */ {
/* 283 */
        Class[] arrayOfClass1 = paramMethodType.ptypes();
/* 284 */
        Class[] arrayOfClass2 = canonicalizes(arrayOfClass1, paramInt2);
/* 285 */
        Class localClass1 = paramMethodType.returnType();
/* 286 */
        Class localClass2 = canonicalize(localClass1, paramInt1);
/* 287 */
        if ((arrayOfClass2 == null) && (localClass2 == null))
/*     */ {
/* 289 */
            return null;
/*     */
        }
/*     */ 
/* 292 */
        if (localClass2 == null) localClass2 = localClass1;
/* 293 */
        if (arrayOfClass2 == null) arrayOfClass2 = arrayOfClass1;
/* 294 */
        return MethodType.makeImpl(localClass2, arrayOfClass2, true);
/*     */
    }

    /*     */
/*     */
    static Class<?> canonicalize(Class<?> paramClass, int paramInt)
/*     */ {
/* 302 */
        if (paramClass != Object.class)
/*     */ {
/* 304 */
            if (!paramClass.isPrimitive())
/* 305 */ switch (paramInt) {
/*     */
                case 3:
/* 307 */
                    Class localClass = Wrapper.asPrimitiveType(paramClass);
/* 308 */
                    if (localClass != paramClass) return localClass;
/*     */
                    break;
/*     */
                case 1:
/*     */
                case 6:
/* 312 */
                    return Object.class;
/*     */
            }
/* 314 */
            else if (paramClass == Void.TYPE)
/*     */ {
/* 316 */
                switch (paramInt) {
/*     */
                    case 6:
/* 318 */
                        return Integer.TYPE;
/*     */
                    case 2:
/* 320 */
                        return Void.class;
/*     */
                }
/*     */
            }
/*     */
            else
/* 324 */         switch (paramInt) {
/*     */
                    case 2:
/* 326 */
                        return Wrapper.asWrapperType(paramClass);
/*     */
                    case 4:
/* 328 */
                        if ((paramClass == Integer.TYPE) || (paramClass == Long.TYPE))
/* 329 */ return null;
/* 330 */
                        if (paramClass == Double.TYPE)
/* 331 */ return Long.TYPE;
/* 332 */
                        return Integer.TYPE;
/*     */
                    case 5:
/* 334 */
                        if (paramClass == Long.TYPE)
/* 335 */ return null;
/* 336 */
                        return Long.TYPE;
/*     */
                    case 6:
/* 338 */
                        if ((paramClass == Integer.TYPE) || (paramClass == Long.TYPE) || (paramClass == Float.TYPE) || (paramClass == Double.TYPE))
/*     */ {
/* 340 */
                            return null;
/*     */
                        }
/* 342 */
                        return Integer.TYPE;
/*     */
                    case 3:
/*     */
                }
/*     */
        }
/* 346 */
        return null;
/*     */
    }

    /*     */
/*     */
    static Class<?>[] canonicalizes(Class<?>[] paramArrayOfClass, int paramInt)
/*     */ {
/* 353 */
        Class[] arrayOfClass = null;
/* 354 */
        int i = paramArrayOfClass.length;
        for (int j = 0; j < i; j++) {
/* 355 */
            Class localClass = canonicalize(paramArrayOfClass[j], paramInt);
/* 356 */
            if (localClass == Void.TYPE)
/* 357 */ localClass = null;
/* 358 */
            if (localClass != null) {
/* 359 */
                if (arrayOfClass == null)
/* 360 */ arrayOfClass = (Class[]) paramArrayOfClass.clone();
/* 361 */
                arrayOfClass[j] = localClass;
/*     */
            }
/*     */
        }
/* 364 */
        return arrayOfClass;
/*     */
    }

    /*     */
/*     */
    public String toString()
/*     */ {
/* 369 */
        return "Form" + this.erasedType;
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.MethodTypeForm
 * JD-Core Version:    0.6.2
 */