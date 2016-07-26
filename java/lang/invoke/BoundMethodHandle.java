/*     */
package java.lang.invoke;
/*     */ 
/*     */

import com.sun.xml.internal.ws.org.objectweb.asm.ClassWriter;
/*     */ import com.sun.xml.internal.ws.org.objectweb.asm.FieldVisitor;
/*     */ import com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor;
/*     */ import com.sun.xml.internal.ws.org.objectweb.asm.Type;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import sun.invoke.util.ValueConversions;
/*     */ import sun.invoke.util.Wrapper;
/*     */ import sun.misc.Unsafe;

/*     */
/*     */ abstract class BoundMethodHandle extends MethodHandle
/*     */ {
    /*     */   static final String EXTENSION_TYPES = "LIJFD";
    /*     */   static final byte INDEX_L = 0;
    /*     */   static final byte INDEX_I = 1;
    /*     */   static final byte INDEX_J = 2;
    /*     */   static final byte INDEX_F = 3;
    /*     */   static final byte INDEX_D = 4;
    /* 852 */   private static final MethodHandles.Lookup LOOKUP = MethodHandles.Lookup.IMPL_LOOKUP;
    /*     */
/* 857 */   static final SpeciesData SPECIES_DATA = SpeciesData.EMPTY;

    /*     */
/*     */   BoundMethodHandle(MethodType paramMethodType, LambdaForm paramLambdaForm)
/*     */ {
/*  57 */
        super(paramMethodType, paramLambdaForm);
/*     */
    }

    /*     */
/*     */
    static MethodHandle bindSingle(MethodType paramMethodType, LambdaForm paramLambdaForm, char paramChar, Object paramObject)
/*     */ {
/*     */
        try
/*     */ {
/*  67 */
            switch (paramChar) {
/*     */
                case 'L':
/*  69 */
                    return bindSingle(paramMethodType, paramLambdaForm, paramObject);
/*     */
                case 'I':
/*  72 */
                    return SpeciesData.EMPTY.extendWithType('I').constructor[0].invokeBasic(paramMethodType, paramLambdaForm, ValueConversions.widenSubword(paramObject));
/*     */
                case 'J':
/*  74 */
                    return SpeciesData.EMPTY.extendWithType('J').constructor[0].invokeBasic(paramMethodType, paramLambdaForm, ((Long) paramObject).longValue());
/*     */
                case 'F':
/*  76 */
                    return SpeciesData.EMPTY.extendWithType('F').constructor[0].invokeBasic(paramMethodType, paramLambdaForm, ((Float) paramObject).floatValue());
/*     */
                case 'D':
/*  78 */
                    return SpeciesData.EMPTY.extendWithType('D').constructor[0].invokeBasic(paramMethodType, paramLambdaForm, ((Double) paramObject).doubleValue());
/*     */
                case 'E':
/*     */
                case 'G':
/*     */
                case 'H':
/*  79 */
                case 'K':
            }
            throw new InternalError("unexpected xtype: " + paramChar);
/*     */
        }
/*     */ catch (Throwable localThrowable) {
/*  82 */
            throw MethodHandleStatics.newInternalError(localThrowable);
/*     */
        }
/*     */
    }

    /*     */
/*     */
    static MethodHandle bindSingle(MethodType paramMethodType, LambdaForm paramLambdaForm, Object paramObject) {
/*  87 */
        return new Species_L(paramMethodType, paramLambdaForm, paramObject);
/*     */
    }

    /*     */
/*     */   MethodHandle cloneExtend(MethodType paramMethodType, LambdaForm paramLambdaForm, char paramChar, Object paramObject) {
/*     */
        try {
/*  92 */
            switch (paramChar) {
                case 'L':
/*  93 */
                    return cloneExtendL(paramMethodType, paramLambdaForm, paramObject);
/*     */
                case 'I':
/*  94 */
                    return cloneExtendI(paramMethodType, paramLambdaForm, ValueConversions.widenSubword(paramObject));
/*     */
                case 'J':
/*  95 */
                    return cloneExtendJ(paramMethodType, paramLambdaForm, ((Long) paramObject).longValue());
/*     */
                case 'F':
/*  96 */
                    return cloneExtendF(paramMethodType, paramLambdaForm, ((Float) paramObject).floatValue());
/*     */
                case 'D':
/*  97 */
                    return cloneExtendD(paramMethodType, paramLambdaForm, ((Double) paramObject).doubleValue());
/*     */
                case 'E':
/*     */
                case 'G':
/*     */
                case 'H':
/* 100 */
                case 'K':
            }
        } catch (Throwable localThrowable) {
            throw MethodHandleStatics.newInternalError(localThrowable);
        }
/*     */ 
/* 102 */
        throw new InternalError("unexpected type: " + paramChar);
/*     */
    }

    /*     */
/*     */   MethodHandle bindArgument(int paramInt, char paramChar, Object paramObject)
/*     */ {
/* 107 */
        MethodType localMethodType = type().dropParameterTypes(paramInt, paramInt + 1);
/* 108 */
        LambdaForm localLambdaForm = internalForm().bind(1 + paramInt, speciesData());
/* 109 */
        return cloneExtend(localMethodType, localLambdaForm, paramChar, paramObject);
/*     */
    }

    /*     */
/*     */   MethodHandle dropArguments(MethodType paramMethodType, int paramInt1, int paramInt2)
/*     */ {
/* 114 */
        LambdaForm localLambdaForm = internalForm().addArguments(paramInt1, paramMethodType.parameterList().subList(paramInt1, paramInt1 + paramInt2));
/*     */
        try {
/* 116 */
            return clone(paramMethodType, localLambdaForm);
/*     */
        } catch (Throwable localThrowable) {
/* 118 */
            throw MethodHandleStatics.newInternalError(localThrowable);
/*     */
        }
/*     */
    }

    /*     */
/*     */   MethodHandle permuteArguments(MethodType paramMethodType, int[] paramArrayOfInt)
/*     */ {
/*     */
        try {
/* 125 */
            return clone(paramMethodType, this.form.permuteArguments(1, paramArrayOfInt, LambdaForm.basicTypes(paramMethodType.parameterList())));
/*     */
        } catch (Throwable localThrowable) {
/* 127 */
            throw MethodHandleStatics.newInternalError(localThrowable);
/*     */
        }
/*     */
    }

    /*     */
/*     */
    static byte extensionIndex(char paramChar)
/*     */ {
/* 134 */
        int i = "LIJFD".indexOf(paramChar);
/* 135 */
        if (i < 0) throw new InternalError();
/* 136 */
        return (byte) i;
/*     */
    }

    /*     */
/*     */
    abstract SpeciesData speciesData();

    /*     */
/*     */
    final Object internalProperties()
/*     */ {
/* 147 */
        return "/BMH=" + internalValues();
/*     */
    }

    /*     */
/*     */
    final Object internalValues()
/*     */ {
/* 152 */
        Object[] arrayOfObject = new Object[speciesData().fieldCount()];
/* 153 */
        for (int i = 0; i < arrayOfObject.length; i++) {
/* 154 */
            arrayOfObject[i] = arg(i);
/*     */
        }
/* 156 */
        return Arrays.asList(arrayOfObject);
/*     */
    }

    /*     */
/*     */
    final Object arg(int paramInt) {
/*     */
        try {
/* 161 */
            switch (speciesData().fieldType(paramInt)) {
                case 'L':
/* 162 */
                    return argL(paramInt);
/*     */
                case 'I':
/* 163 */
                    return Integer.valueOf(argI(paramInt));
/*     */
                case 'F':
/* 164 */
                    return Float.valueOf(argF(paramInt));
/*     */
                case 'D':
/* 165 */
                    return Double.valueOf(argD(paramInt));
/*     */
                case 'J':
/* 166 */
                    return Long.valueOf(argJ(paramInt));
/*     */
                case 'E':
/*     */
                case 'G':
/*     */
                case 'H':
/* 169 */
                case 'K':
            }
        } catch (Throwable localThrowable) {
            throw MethodHandleStatics.newInternalError(localThrowable);
        }
/*     */ 
/* 171 */
        throw new InternalError("unexpected type: " + speciesData().types + "." + paramInt);
/*     */
    }

    /* 173 */
    final Object argL(int paramInt) throws Throwable {
        return speciesData().getters[paramInt].invokeBasic(this);
    }

    /* 174 */
    final int argI(int paramInt) throws Throwable {
        return speciesData().getters[paramInt].invokeBasic(this);
    }

    /* 175 */
    final float argF(int paramInt) throws Throwable {
        return speciesData().getters[paramInt].invokeBasic(this);
    }

    /* 176 */
    final double argD(int paramInt) throws Throwable {
        return speciesData().getters[paramInt].invokeBasic(this);
    }

    /* 177 */
    final long argJ(int paramInt) throws Throwable {
        return speciesData().getters[paramInt].invokeBasic(this);
    }

    /*     */
/*     */
    abstract BoundMethodHandle clone(MethodType paramMethodType, LambdaForm paramLambdaForm) throws Throwable;

    /*     */
/*     */
    abstract BoundMethodHandle cloneExtendL(MethodType paramMethodType, LambdaForm paramLambdaForm, Object paramObject) throws Throwable;

    /*     */
/*     */
    abstract BoundMethodHandle cloneExtendI(MethodType paramMethodType, LambdaForm paramLambdaForm, int paramInt) throws Throwable;

    /*     */
/*     */
    abstract BoundMethodHandle cloneExtendJ(MethodType paramMethodType, LambdaForm paramLambdaForm, long paramLong) throws Throwable;

    /*     */
/*     */
    abstract BoundMethodHandle cloneExtendF(MethodType paramMethodType, LambdaForm paramLambdaForm, float paramFloat) throws Throwable;

    /*     */
/*     */
    abstract BoundMethodHandle cloneExtendD(MethodType paramMethodType, LambdaForm paramLambdaForm, double paramDouble) throws Throwable;

    /*     */
/*     */   MethodHandle reinvokerTarget() {
/*     */
        try {
/* 193 */
            return (MethodHandle) argL(0);
/*     */
        } catch (Throwable localThrowable) {
/* 195 */
            throw MethodHandleStatics.newInternalError(localThrowable);
/*     */
        }
/*     */
    }

    /*     */
/*     */
    static SpeciesData getSpeciesData(String paramString)
/*     */ {
/* 480 */
        return SpeciesData.get(paramString);
/*     */
    }

    /*     */
/*     */   static class Factory
/*     */ {
        /*     */     static final String JLO_SIG = "Ljava/lang/Object;";
        /*     */     static final String JLS_SIG = "Ljava/lang/String;";
        /*     */     static final String JLC_SIG = "Ljava/lang/Class;";
        /*     */     static final String MH = "java/lang/invoke/MethodHandle";
        /*     */     static final String MH_SIG = "Ljava/lang/invoke/MethodHandle;";
        /*     */     static final String BMH = "java/lang/invoke/BoundMethodHandle";
        /*     */     static final String BMH_SIG = "Ljava/lang/invoke/BoundMethodHandle;";
        /*     */     static final String SPECIES_DATA = "java/lang/invoke/BoundMethodHandle$SpeciesData";
        /*     */     static final String SPECIES_DATA_SIG = "Ljava/lang/invoke/BoundMethodHandle$SpeciesData;";
        /*     */     static final String SPECIES_PREFIX_NAME = "Species_";
        /*     */     static final String SPECIES_PREFIX_PATH = "java/lang/invoke/BoundMethodHandle$Species_";
        /*     */     static final String BMHSPECIES_DATA_EWI_SIG = "(B)Ljava/lang/invoke/BoundMethodHandle$SpeciesData;";
        /*     */     static final String BMHSPECIES_DATA_GFC_SIG = "(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/invoke/BoundMethodHandle$SpeciesData;";
        /*     */     static final String MYSPECIES_DATA_SIG = "()Ljava/lang/invoke/BoundMethodHandle$SpeciesData;";
        /*     */     static final String VOID_SIG = "()V";
        /*     */     static final String SIG_INCIPIT = "(Ljava/lang/invoke/MethodType;Ljava/lang/invoke/LambdaForm;";
        /* 517 */     static final Class<?>[] TYPES = {Object.class, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};
        /*     */
/* 519 */     static final String[] E_THROWABLE = {"java/lang/Throwable"};

        /*     */
/*     */
        static Class<? extends BoundMethodHandle> generateConcreteBMHClass(String paramString)
/*     */ {
/* 582 */
            ClassWriter localClassWriter = new ClassWriter(3);
/*     */ 
/* 584 */
            String str1 = "java/lang/invoke/BoundMethodHandle$Species_" + paramString;
/* 585 */
            String str2 = "Species_" + paramString;
/*     */ 
/* 587 */
            localClassWriter.visit(50, 48, str1, null, "java/lang/invoke/BoundMethodHandle", null);
/* 588 */
            localClassWriter.visitSource(str2, null);
/*     */ 
/* 591 */
            localClassWriter.visitField(8, "SPECIES_DATA", "Ljava/lang/invoke/BoundMethodHandle$SpeciesData;", null, null).visitEnd();
/*     */ 
/* 594 */
            for (int i = 0; i < paramString.length(); i++) {
/* 595 */
                j = paramString.charAt(i);
/* 596 */
                String str3 = makeFieldName(paramString, i);
/* 597 */
                String str4 = j == 76 ? "Ljava/lang/Object;" : String.valueOf(j);
/* 598 */
                localClassWriter.visitField(16, str3, str4, null, null).visitEnd();
/*     */
            }
/*     */ 
/* 604 */
            MethodVisitor localMethodVisitor = localClassWriter.visitMethod(0, "<init>", makeSignature(paramString, true), null, null);
/* 605 */
            localMethodVisitor.visitCode();
/* 606 */
            localMethodVisitor.visitVarInsn(25, 0);
/* 607 */
            localMethodVisitor.visitVarInsn(25, 1);
/* 608 */
            localMethodVisitor.visitVarInsn(25, 2);
/*     */ 
/* 610 */
            localMethodVisitor.visitMethodInsn(183, "java/lang/invoke/BoundMethodHandle", "<init>", makeSignature("", true));
/*     */ 
/* 612 */
            int j = 0;
/*     */
            int m;
/* 612 */
            for (int k = 0; j < paramString.length(); k++)
/*     */ {
/* 614 */
                m = paramString.charAt(j);
/* 615 */
                localMethodVisitor.visitVarInsn(25, 0);
/* 616 */
                localMethodVisitor.visitVarInsn(typeLoadOp(m), k + 3);
/* 617 */
                localMethodVisitor.visitFieldInsn(181, str1, makeFieldName(paramString, j), typeSig(m));
/* 618 */
                if ((m == 74) || (m == 68))
/* 619 */ k++;
/* 612 */
                j++;
/*     */
            }
/*     */ 
/* 623 */
            localMethodVisitor.visitInsn(177);
/* 624 */
            localMethodVisitor.visitMaxs(0, 0);
/* 625 */
            localMethodVisitor.visitEnd();
/*     */ 
/* 628 */
            localMethodVisitor = localClassWriter.visitMethod(16, "reinvokerTarget", "()Ljava/lang/invoke/MethodHandle;", null, null);
/* 629 */
            localMethodVisitor.visitCode();
/* 630 */
            localMethodVisitor.visitVarInsn(25, 0);
/* 631 */
            localMethodVisitor.visitFieldInsn(180, str1, "argL0", "Ljava/lang/Object;");
/* 632 */
            localMethodVisitor.visitTypeInsn(192, "java/lang/invoke/MethodHandle");
/* 633 */
            localMethodVisitor.visitInsn(176);
/* 634 */
            localMethodVisitor.visitMaxs(0, 0);
/* 635 */
            localMethodVisitor.visitEnd();
/*     */ 
/* 638 */
            localMethodVisitor = localClassWriter.visitMethod(16, "speciesData", "()Ljava/lang/invoke/BoundMethodHandle$SpeciesData;", null, null);
/* 639 */
            localMethodVisitor.visitCode();
/* 640 */
            localMethodVisitor.visitFieldInsn(178, str1, "SPECIES_DATA", "Ljava/lang/invoke/BoundMethodHandle$SpeciesData;");
/* 641 */
            localMethodVisitor.visitInsn(176);
/* 642 */
            localMethodVisitor.visitMaxs(0, 0);
/* 643 */
            localMethodVisitor.visitEnd();
/*     */ 
/* 646 */
            localMethodVisitor = localClassWriter.visitMethod(16, "clone", makeSignature("", false), null, E_THROWABLE);
/* 647 */
            localMethodVisitor.visitCode();
/*     */ 
/* 650 */
            localMethodVisitor.visitVarInsn(25, 0);
/* 651 */
            localMethodVisitor.visitFieldInsn(178, str1, "SPECIES_DATA", "Ljava/lang/invoke/BoundMethodHandle$SpeciesData;");
/* 652 */
            localMethodVisitor.visitFieldInsn(180, "java/lang/invoke/BoundMethodHandle$SpeciesData", "constructor", "[Ljava/lang/invoke/MethodHandle;");
/* 653 */
            localMethodVisitor.visitInsn(3);
/* 654 */
            localMethodVisitor.visitInsn(50);
/*     */ 
/* 656 */
            localMethodVisitor.visitVarInsn(25, 1);
/* 657 */
            localMethodVisitor.visitVarInsn(25, 2);
/*     */ 
/* 659 */
            emitPushFields(paramString, str1, localMethodVisitor);
/*     */ 
/* 661 */
            localMethodVisitor.visitMethodInsn(182, "java/lang/invoke/MethodHandle", "invokeBasic", makeSignature(paramString, false));
/* 662 */
            localMethodVisitor.visitInsn(176);
/* 663 */
            localMethodVisitor.visitMaxs(0, 0);
/* 664 */
            localMethodVisitor.visitEnd();
/*     */ 
/* 667 */
            for (Class localClass2 : TYPES) {
/* 668 */
                char c = Wrapper.basicTypeChar(localClass2);
/* 669 */
                localMethodVisitor = localClassWriter.visitMethod(16, "cloneExtend" + c, makeSignature(String.valueOf(c), false), null, E_THROWABLE);
/* 670 */
                localMethodVisitor.visitCode();
/*     */ 
/* 673 */
                localMethodVisitor.visitFieldInsn(178, str1, "SPECIES_DATA", "Ljava/lang/invoke/BoundMethodHandle$SpeciesData;");
/* 674 */
                int n = 3 + BoundMethodHandle.extensionIndex(c);
/* 675 */
                assert (n <= 8);
/* 676 */
                localMethodVisitor.visitInsn(n);
/* 677 */
                localMethodVisitor.visitMethodInsn(182, "java/lang/invoke/BoundMethodHandle$SpeciesData", "extendWithIndex", "(B)Ljava/lang/invoke/BoundMethodHandle$SpeciesData;");
/* 678 */
                localMethodVisitor.visitFieldInsn(180, "java/lang/invoke/BoundMethodHandle$SpeciesData", "constructor", "[Ljava/lang/invoke/MethodHandle;");
/* 679 */
                localMethodVisitor.visitInsn(3);
/* 680 */
                localMethodVisitor.visitInsn(50);
/*     */ 
/* 682 */
                localMethodVisitor.visitVarInsn(25, 1);
/* 683 */
                localMethodVisitor.visitVarInsn(25, 2);
/*     */ 
/* 685 */
                emitPushFields(paramString, str1, localMethodVisitor);
/*     */ 
/* 687 */
                localMethodVisitor.visitVarInsn(typeLoadOp(c), 3);
/*     */ 
/* 689 */
                localMethodVisitor.visitMethodInsn(182, "java/lang/invoke/MethodHandle", "invokeBasic", makeSignature(paramString + c, false));
/* 690 */
                localMethodVisitor.visitInsn(176);
/* 691 */
                localMethodVisitor.visitMaxs(0, 0);
/* 692 */
                localMethodVisitor.visitEnd();
/*     */
            }
/*     */ 
/* 696 */
            localMethodVisitor = localClassWriter.visitMethod(8, "<clinit>", "()V", null, null);
/* 697 */
            localMethodVisitor.visitCode();
/* 698 */
            localMethodVisitor.visitLdcInsn(paramString);
/* 699 */
            localMethodVisitor.visitLdcInsn(Type.getObjectType(str1));
/* 700 */
            localMethodVisitor.visitMethodInsn(184, "java/lang/invoke/BoundMethodHandle$SpeciesData", "getForClass", "(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/invoke/BoundMethodHandle$SpeciesData;");
/* 701 */
            localMethodVisitor.visitFieldInsn(179, str1, "SPECIES_DATA", "Ljava/lang/invoke/BoundMethodHandle$SpeciesData;");
/* 702 */
            localMethodVisitor.visitInsn(177);
/* 703 */
            localMethodVisitor.visitMaxs(0, 0);
/* 704 */
            localMethodVisitor.visitEnd();
/*     */ 
/* 706 */
            localClassWriter.visitEnd();
/*     */ 
/* 709 */
            ???=localClassWriter.toByteArray();
/* 710 */
            InvokerBytecodeGenerator.maybeDump(str1, (byte[])???);
/* 711 */
            Class localClass1 = MethodHandleStatics.UNSAFE.defineClass(str1, (byte[])???,0,???.length).
            asSubclass(BoundMethodHandle.class);
/*     */ 
/* 714 */
            MethodHandleStatics.UNSAFE.ensureClassInitialized(localClass1);
/*     */ 
/* 716 */
            return localClass1;
/*     */
        }

        /*     */
/*     */
        private static int typeLoadOp(char paramChar) {
/* 720 */
            switch (paramChar) {
                case 'L':
/* 721 */
                    return 25;
/*     */
                case 'I':
/* 722 */
                    return 21;
/*     */
                case 'J':
/* 723 */
                    return 22;
/*     */
                case 'F':
/* 724 */
                    return 23;
/*     */
                case 'D':
/* 725 */
                    return 24;
/*     */
                case 'E':
/*     */
                case 'G':
/*     */
                case 'H':
/* 726 */
                case 'K':
            }
            throw new InternalError("unrecognized type " + paramChar);
/*     */
        }

        /*     */
/*     */
        private static void emitPushFields(String paramString1, String paramString2, MethodVisitor paramMethodVisitor)
/*     */ {
/* 731 */
            for (int i = 0; i < paramString1.length(); i++) {
/* 732 */
                char c = paramString1.charAt(i);
/* 733 */
                paramMethodVisitor.visitVarInsn(25, 0);
/* 734 */
                paramMethodVisitor.visitFieldInsn(180, paramString2, makeFieldName(paramString1, i), typeSig(c));
/*     */
            }
/*     */
        }

        /*     */
/*     */
        static String typeSig(char paramChar) {
/* 739 */
            return paramChar == 'L' ? "Ljava/lang/Object;" : String.valueOf(paramChar);
/*     */
        }

        /*     */
/*     */
        private static MethodHandle makeGetter(Class<?> paramClass, String paramString, int paramInt)
/*     */ {
/* 747 */
            String str = makeFieldName(paramString, paramInt);
/* 748 */
            Class localClass = Wrapper.forBasicType(paramString.charAt(paramInt)).primitiveType();
/*     */
            try {
/* 750 */
                return BoundMethodHandle.LOOKUP.findGetter(paramClass, str, localClass);
/*     */
            } catch (NoSuchFieldException | IllegalAccessException localNoSuchFieldException) {
/* 752 */
                throw MethodHandleStatics.newInternalError(localNoSuchFieldException);
/*     */
            }
/*     */
        }

        /*     */
/*     */
        static MethodHandle[] makeGetters(Class<?> paramClass, String paramString, MethodHandle[] paramArrayOfMethodHandle) {
/* 757 */
            if (paramArrayOfMethodHandle == null) paramArrayOfMethodHandle = new MethodHandle[paramString.length()];
/* 758 */
            for (int i = 0; i < paramArrayOfMethodHandle.length; i++) {
/* 759 */
                paramArrayOfMethodHandle[i] = makeGetter(paramClass, paramString, i);
/* 760 */
                assert (paramArrayOfMethodHandle[i].internalMemberName().getDeclaringClass() == paramClass);
/*     */
            }
/* 762 */
            return paramArrayOfMethodHandle;
/*     */
        }

        /*     */
/*     */
        static MethodHandle[] makeCtors(Class<? extends BoundMethodHandle> paramClass, String paramString, MethodHandle[] paramArrayOfMethodHandle) {
/* 766 */
            if (paramArrayOfMethodHandle == null) paramArrayOfMethodHandle = new MethodHandle[1];
/* 767 */
            paramArrayOfMethodHandle[0] = makeCbmhCtor(paramClass, paramString);
/* 768 */
            return paramArrayOfMethodHandle;
/*     */
        }

        /*     */
/*     */
        static BoundMethodHandle.SpeciesData speciesDataFromConcreteBMHClass(Class<? extends BoundMethodHandle> paramClass)
/*     */ {
/*     */
            try
/*     */ {
/* 777 */
                Field localField = paramClass.getDeclaredField("SPECIES_DATA");
/* 778 */
                return (BoundMethodHandle.SpeciesData) localField.get(null);
/*     */
            } catch (ReflectiveOperationException localReflectiveOperationException) {
/* 780 */
                throw MethodHandleStatics.newInternalError(localReflectiveOperationException);
/*     */
            }
/*     */
        }

        /*     */
/*     */
        private static String makeFieldName(String paramString, int paramInt)
/*     */ {
/* 790 */
            assert ((paramInt >= 0) && (paramInt < paramString.length()));
/* 791 */
            return "arg" + paramString.charAt(paramInt) + paramInt;
/*     */
        }

        /*     */
/*     */
        private static String makeSignature(String paramString, boolean paramBoolean) {
/* 795 */
            StringBuilder localStringBuilder = new StringBuilder("(Ljava/lang/invoke/MethodType;Ljava/lang/invoke/LambdaForm;");
/* 796 */
            for (char c : paramString.toCharArray()) {
/* 797 */
                localStringBuilder.append(typeSig(c));
/*     */
            }
/* 799 */
            return ')' + (paramBoolean ? "V" : "Ljava/lang/invoke/BoundMethodHandle;");
/*     */
        }

        /*     */
/*     */
        static MethodHandle makeCbmhCtor(Class<? extends BoundMethodHandle> paramClass, String paramString) {
/*     */
            try {
/* 804 */
                return linkConstructor(BoundMethodHandle.LOOKUP.findConstructor(paramClass, MethodType.fromMethodDescriptorString(makeSignature(paramString, true), null)));
/*     */
            } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | TypeNotPresentException localNoSuchMethodException) {
/* 806 */
                throw MethodHandleStatics.newInternalError(localNoSuchMethodException);
/*     */
            }
/*     */
        }

        /*     */
/*     */
        private static MethodHandle linkConstructor(MethodHandle paramMethodHandle)
/*     */ {
/* 823 */
            LambdaForm localLambdaForm = paramMethodHandle.form;
/* 824 */
            int i = localLambdaForm.names.length - 1;
/* 825 */
            LambdaForm.Name localName1 = localLambdaForm.names[i];
/* 826 */
            MemberName localMemberName1 = localName1.function.member;
/* 827 */
            MethodType localMethodType1 = localMemberName1.getInvocationType();
/*     */ 
/* 831 */
            MethodType localMethodType2 = localMethodType1.changeParameterType(0, BoundMethodHandle.class).appendParameterTypes(new Class[]{MemberName.class});
/* 832 */
            MemberName localMemberName2 = new MemberName(MethodHandle.class, "linkToSpecial", localMethodType2, (byte) 6);
/*     */
            try {
/* 834 */
                localMemberName2 = MemberName.getFactory().resolveOrFail((byte) 6, localMemberName2, null, NoSuchMethodException.class);
/* 835 */
                if ((!$assertionsDisabled) && (!localMemberName2.isStatic())) throw new AssertionError();
/*     */
            }
/* 837 */ catch (ReflectiveOperationException localReflectiveOperationException) {
                throw MethodHandleStatics.newInternalError(localReflectiveOperationException);
            }
/*     */ 
/*     */ 
/* 840 */
            Object[] arrayOfObject = Arrays.copyOf(localName1.arguments, localName1.arguments.length + 1);
/* 841 */
            arrayOfObject[(arrayOfObject.length - 1)] = localMemberName1;
/*     */ 
/* 843 */
            LambdaForm.NamedFunction localNamedFunction = new LambdaForm.NamedFunction(localMemberName2);
/* 844 */
            LambdaForm.Name localName2 = new LambdaForm.Name(localNamedFunction, arrayOfObject);
/* 845 */
            localName2.initIndex(i);
/* 846 */
            localLambdaForm.names[i] = localName2;
/* 847 */
            return paramMethodHandle;
/*     */
        }
/*     */
    }

    /*     */
/*     */   static class SpeciesData
/*     */ {
        /*     */     final String types;
        /*     */     final Class<? extends BoundMethodHandle> clazz;
        /*     */     final MethodHandle[] constructor;
        /*     */     final MethodHandle[] getters;
        /*     */     final SpeciesData[] extensions;
        /*     */     static final SpeciesData EMPTY;
        /*     */     private static final HashMap<String, SpeciesData> CACHE;
        /* 475 */     private static final boolean INIT_DONE = Boolean.TRUE.booleanValue();

        /*     */
/*     */     int fieldCount()
/*     */ {
/* 342 */
            return this.types.length();
/*     */
        }

        /*     */     char fieldType(int paramInt) {
/* 345 */
            return this.types.charAt(paramInt);
/*     */
        }

        /*     */
/*     */
        public String toString() {
/* 349 */
            return "SpeciesData[" + (isPlaceholder() ? "<placeholder>" : this.clazz.getSimpleName()) + ":" + this.types + "]";
/*     */
        }

        /*     */
/*     */     LambdaForm.Name getterName(LambdaForm.Name paramName, int paramInt)
/*     */ {
/* 358 */
            MethodHandle localMethodHandle = this.getters[paramInt];
/* 359 */
            assert (localMethodHandle != null) : (this + "." + paramInt);
/* 360 */
            return new LambdaForm.Name(localMethodHandle, new Object[]{paramName});
/*     */
        }

        /*     */
/*     */
        private SpeciesData(String paramString, Class<? extends BoundMethodHandle> paramClass)
/*     */ {
/* 366 */
            this.types = paramString;
/* 367 */
            this.clazz = paramClass;
/* 368 */
            if (!INIT_DONE) {
/* 369 */
                this.constructor = new MethodHandle[1];
/* 370 */
                this.getters = new MethodHandle[paramString.length()];
/*     */
            } else {
/* 372 */
                this.constructor = BoundMethodHandle.Factory.makeCtors(paramClass, paramString, null);
/* 373 */
                this.getters = BoundMethodHandle.Factory.makeGetters(paramClass, paramString, null);
/*     */
            }
/* 375 */
            this.extensions = new SpeciesData["LIJFD".length()];
/*     */
        }

        /*     */
/*     */
        private void initForBootstrap() {
/* 379 */
            assert (!INIT_DONE);
/* 380 */
            if (this.constructor[0] == null) {
/* 381 */
                BoundMethodHandle.Factory.makeCtors(this.clazz, this.types, this.constructor);
/* 382 */
                BoundMethodHandle.Factory.makeGetters(this.clazz, this.types, this.getters);
/*     */
            }
/*     */
        }

        /*     */
/*     */
        private SpeciesData(String paramString)
/*     */ {
/* 388 */
            this.types = paramString;
/* 389 */
            this.clazz = null;
/* 390 */
            this.constructor = null;
/* 391 */
            this.getters = null;
/* 392 */
            this.extensions = null;
/*     */
        }

        /* 394 */
        private boolean isPlaceholder() {
            return this.clazz == null;
        }

        /*     */
/*     */ 
/*     */     SpeciesData extendWithType(char paramChar)
/*     */ {
/* 400 */
            int i = BoundMethodHandle.extensionIndex(paramChar);
/* 401 */
            SpeciesData localSpeciesData = this.extensions[i];
/* 402 */
            if (localSpeciesData != null) return localSpeciesData;
/*     */
            SpeciesData tmp47_44 = get(this.types + paramChar);
            localSpeciesData = tmp47_44;
            this.extensions[i] = tmp47_44;
/* 404 */
            return localSpeciesData;
/*     */
        }

        /*     */
/*     */     SpeciesData extendWithIndex(byte paramByte) {
/* 408 */
            SpeciesData localSpeciesData = this.extensions[paramByte];
/* 409 */
            if (localSpeciesData != null) return localSpeciesData;
/*     */
            SpeciesData tmp47_44 = get(this.types + "LIJFD".charAt(paramByte));
            localSpeciesData = tmp47_44;
            this.extensions[paramByte] = tmp47_44;
/* 411 */
            return localSpeciesData;
/*     */
        }

        /*     */
/*     */
        private static SpeciesData get(String paramString)
/*     */ {
/* 416 */
            SpeciesData localSpeciesData = lookupCache(paramString);
/* 417 */
            if (!localSpeciesData.isPlaceholder())
/* 418 */ return localSpeciesData;
/* 419 */
            synchronized (localSpeciesData)
/*     */ {
/* 422 */
                if (lookupCache(paramString).isPlaceholder()) {
/* 423 */
                    BoundMethodHandle.Factory.generateConcreteBMHClass(paramString);
/*     */
                }
/*     */
            }
/* 426 */
            localSpeciesData = lookupCache(paramString);
/*     */ 
/* 428 */
            assert ((localSpeciesData != null) && (!localSpeciesData.isPlaceholder()));
/* 429 */
            return localSpeciesData;
/*     */
        }

        /*     */
/*     */
        static SpeciesData getForClass(String paramString, Class<? extends BoundMethodHandle> paramClass) {
/* 433 */
            return updateCache(paramString, new SpeciesData(paramString, paramClass));
/*     */
        }

        /*     */
        private static synchronized SpeciesData lookupCache(String paramString) {
/* 436 */
            SpeciesData localSpeciesData = (SpeciesData) CACHE.get(paramString);
/* 437 */
            if (localSpeciesData != null) return localSpeciesData;
/* 438 */
            localSpeciesData = new SpeciesData(paramString);
/* 439 */
            assert (localSpeciesData.isPlaceholder());
/* 440 */
            CACHE.put(paramString, localSpeciesData);
/* 441 */
            return localSpeciesData;
/*     */
        }

        /*     */
/*     */
        private static synchronized SpeciesData updateCache(String paramString, SpeciesData paramSpeciesData)
/*     */ {
/*     */
            SpeciesData localSpeciesData;
/* 445 */
            assert (((localSpeciesData = (SpeciesData) CACHE.get(paramString)) == null) || (localSpeciesData.isPlaceholder()));
/* 446 */
            assert (!paramSpeciesData.isPlaceholder());
/* 447 */
            CACHE.put(paramString, paramSpeciesData);
/* 448 */
            return paramSpeciesData;
/*     */
        }

        /*     */
/*     */     static
/*     */ {
/* 363 */
            EMPTY = new SpeciesData("", BoundMethodHandle.class);
/*     */ 
/* 396 */
            CACHE = new HashMap();
/*     */ 
/* 453 */
            BoundMethodHandle localBoundMethodHandle = BoundMethodHandle.class;
/* 454 */
            SpeciesData localSpeciesData1 = BoundMethodHandle.SPECIES_DATA;
/* 455 */
            assert ((localSpeciesData1 == null) || (localSpeciesData1 == lookupCache(""))) : localSpeciesData1;
/*     */
            try {
/* 457 */
                for (Class localClass1 : localBoundMethodHandle.getDeclaredClasses())
/* 458 */
                    if (localBoundMethodHandle.isAssignableFrom(localClass1)) {
/* 459 */
                        Class localClass2 = localClass1.asSubclass(BoundMethodHandle.class);
/* 460 */
                        SpeciesData localSpeciesData3 = BoundMethodHandle.Factory.speciesDataFromConcreteBMHClass(localClass2);
/* 461 */
                        assert (localSpeciesData3 != null) : localClass2.getName();
/* 462 */
                        assert (localSpeciesData3.clazz == localClass2);
/* 463 */
                        assert (localSpeciesData3 == lookupCache(localSpeciesData3.types));
/*     */
                    }
/*     */
            }
/*     */ catch (Throwable localThrowable) {
/* 467 */
                throw MethodHandleStatics.newInternalError(localThrowable);
/*     */
            }
/*     */ 
/* 470 */
            for (SpeciesData localSpeciesData2 : CACHE.values())
/* 471 */
                localSpeciesData2.initForBootstrap();
/*     */
        }
/*     */
    }

    /*     */
/*     */   private static final class Species_L extends BoundMethodHandle
/*     */ {
        /*     */     final Object argL0;
        /* 216 */     static final BoundMethodHandle.SpeciesData SPECIES_DATA = BoundMethodHandle.SpeciesData.getForClass("L", Species_L.class);

        /*     */
/*     */     Species_L(MethodType paramMethodType, LambdaForm paramLambdaForm, Object paramObject)
/*     */ {
/* 207 */
            super(paramLambdaForm);
/* 208 */
            this.argL0 = paramObject;
/*     */
        }

        /*     */     MethodHandle reinvokerTarget() {
/* 211 */
            return (MethodHandle) this.argL0;
/*     */
        }

        /*     */     BoundMethodHandle.SpeciesData speciesData() {
/* 214 */
            return SPECIES_DATA;
/*     */
        }

        /*     */
/*     */
        final BoundMethodHandle clone(MethodType paramMethodType, LambdaForm paramLambdaForm) throws Throwable
/*     */ {
/* 219 */
            return new Species_L(paramMethodType, paramLambdaForm, this.argL0);
/*     */
        }

        /*     */
/*     */
        final BoundMethodHandle cloneExtendL(MethodType paramMethodType, LambdaForm paramLambdaForm, Object paramObject) throws Throwable {
/* 223 */
            return SPECIES_DATA.extendWithIndex(0).constructor[0].invokeBasic(paramMethodType, paramLambdaForm, this.argL0, paramObject);
/*     */
        }

        /*     */
/*     */
        final BoundMethodHandle cloneExtendI(MethodType paramMethodType, LambdaForm paramLambdaForm, int paramInt) throws Throwable {
/* 227 */
            return SPECIES_DATA.extendWithIndex(1).constructor[0].invokeBasic(paramMethodType, paramLambdaForm, this.argL0, paramInt);
/*     */
        }

        /*     */
/*     */
        final BoundMethodHandle cloneExtendJ(MethodType paramMethodType, LambdaForm paramLambdaForm, long paramLong) throws Throwable {
/* 231 */
            return SPECIES_DATA.extendWithIndex(2).constructor[0].invokeBasic(paramMethodType, paramLambdaForm, this.argL0, paramLong);
/*     */
        }

        /*     */
/*     */
        final BoundMethodHandle cloneExtendF(MethodType paramMethodType, LambdaForm paramLambdaForm, float paramFloat) throws Throwable {
/* 235 */
            return SPECIES_DATA.extendWithIndex(3).constructor[0].invokeBasic(paramMethodType, paramLambdaForm, this.argL0, paramFloat);
/*     */
        }

        /*     */
/*     */
        final BoundMethodHandle cloneExtendD(MethodType paramMethodType, LambdaForm paramLambdaForm, double paramDouble) throws Throwable {
/* 239 */
            return SPECIES_DATA.extendWithIndex(4).constructor[0].invokeBasic(paramMethodType, paramLambdaForm, this.argL0, paramDouble);
/*     */
        }
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.BoundMethodHandle
 * JD-Core Version:    0.6.2
 */