/*      */ package java.lang.invoke;
/*      */ 
/*      */ import com.sun.xml.internal.ws.org.objectweb.asm.ClassWriter;
/*      */ import com.sun.xml.internal.ws.org.objectweb.asm.Label;
/*      */ import com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import sun.invoke.util.VerifyAccess;
/*      */ import sun.invoke.util.VerifyType;
/*      */ import sun.invoke.util.Wrapper;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ class InvokerBytecodeGenerator
/*      */ {
/*      */   private static final String MH = "java/lang/invoke/MethodHandle";
/*      */   private static final String BMH = "java/lang/invoke/BoundMethodHandle";
/*      */   private static final String LF = "java/lang/invoke/LambdaForm";
/*      */   private static final String LFN = "java/lang/invoke/LambdaForm$Name";
/*      */   private static final String CLS = "java/lang/Class";
/*      */   private static final String OBJ = "java/lang/Object";
/*      */   private static final String OBJARY = "[Ljava/lang/Object;";
/*      */   private static final String LF_SIG = "Ljava/lang/invoke/LambdaForm;";
/*      */   private static final String LFN_SIG = "Ljava/lang/invoke/LambdaForm$Name;";
/*      */   private static final String LL_SIG = "(Ljava/lang/Object;)Ljava/lang/Object;";
/*      */   private static final String superName = "java/lang/invoke/LambdaForm";
/*      */   private final String className;
/*      */   private final String sourceFile;
/*      */   private final LambdaForm lambdaForm;
/*      */   private final String invokerName;
/*      */   private final MethodType invokerType;
/*      */   private final int[] localsMap;
/*      */   private ClassWriter cw;
/*      */   private MethodVisitor mv;
/*      */   private static final MemberName.Factory MEMBERNAME_FACTORY;
/*      */   private static final Class<?> HOST_CLASS;
/*      */   private static final HashMap<String, Integer> DUMP_CLASS_FILES_COUNTERS;
/*      */   private static final File DUMP_CLASS_FILES_DIR;
/*  201 */   Map<Object, CpPatch> cpPatches = new HashMap();
/*      */ 
/*  203 */   int cph = 0;
/*      */ 
/*  598 */   private static Class<?>[] STATICALLY_INVOCABLE_PACKAGES = { Object.class, Arrays.class, Unsafe.class };
/*      */ 
/* 1015 */   static int nfi = 0;
/*      */ 
/*      */   private InvokerBytecodeGenerator(LambdaForm paramLambdaForm, int paramInt, String paramString1, String paramString2, MethodType paramMethodType)
/*      */   {
/*   88 */     if (paramString2.contains(".")) {
/*   89 */       int i = paramString2.indexOf(".");
/*   90 */       paramString1 = paramString2.substring(0, i);
/*   91 */       paramString2 = paramString2.substring(i + 1);
/*      */     }
/*   93 */     if (MethodHandleStatics.DUMP_CLASS_FILES) {
/*   94 */       paramString1 = makeDumpableClassName(paramString1);
/*      */     }
/*   96 */     this.className = ("java/lang/invoke/LambdaForm$" + paramString1);
/*   97 */     this.sourceFile = ("LambdaForm$" + paramString1);
/*   98 */     this.lambdaForm = paramLambdaForm;
/*   99 */     this.invokerName = paramString2;
/*  100 */     this.invokerType = paramMethodType;
/*  101 */     this.localsMap = new int[paramInt];
/*      */   }
/*      */ 
/*      */   private InvokerBytecodeGenerator(String paramString1, String paramString2, MethodType paramMethodType) {
/*  105 */     this(null, paramMethodType.parameterCount(), paramString1, paramString2, paramMethodType);
/*      */ 
/*  108 */     for (int i = 0; i < this.localsMap.length; i++)
/*  109 */       this.localsMap[i] = (paramMethodType.parameterSlotCount() - paramMethodType.parameterSlotDepth(i));
/*      */   }
/*      */ 
/*      */   private InvokerBytecodeGenerator(String paramString, LambdaForm paramLambdaForm, MethodType paramMethodType)
/*      */   {
/*  114 */     this(paramLambdaForm, paramLambdaForm.names.length, paramString, paramLambdaForm.debugName, paramMethodType);
/*      */ 
/*  117 */     LambdaForm.Name[] arrayOfName = paramLambdaForm.names;
/*  118 */     int i = 0; for (int j = 0; i < this.localsMap.length; i++) {
/*  119 */       this.localsMap[i] = j;
/*  120 */       j += Wrapper.forBasicType(arrayOfName[i].type).stackSlots();
/*      */     }
/*      */   }
/*      */ 
/*      */   static void maybeDump(String paramString, final byte[] paramArrayOfByte)
/*      */   {
/*  150 */     if (MethodHandleStatics.DUMP_CLASS_FILES) {
/*  151 */       System.out.println("dump: " + paramString);
/*  152 */       AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Void run() {
/*      */           try {
/*  156 */             String str = this.val$className;
/*      */ 
/*  158 */             File localFile = new File(InvokerBytecodeGenerator.DUMP_CLASS_FILES_DIR, str + ".class");
/*  159 */             localFile.getParentFile().mkdirs();
/*  160 */             FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
/*  161 */             localFileOutputStream.write(paramArrayOfByte);
/*  162 */             localFileOutputStream.close();
/*  163 */             return null;
/*      */           } catch (IOException localIOException) {
/*  165 */             throw MethodHandleStatics.newInternalError(localIOException);
/*      */           }
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */ 
/*      */   private static String makeDumpableClassName(String paramString)
/*      */   {
/*      */     Integer localInteger;
/*  175 */     synchronized (DUMP_CLASS_FILES_COUNTERS) {
/*  176 */       localInteger = (Integer)DUMP_CLASS_FILES_COUNTERS.get(paramString);
/*  177 */       if (localInteger == null) localInteger = Integer.valueOf(0);
/*  178 */       DUMP_CLASS_FILES_COUNTERS.put(paramString, Integer.valueOf(localInteger.intValue() + 1));
/*      */     }
/*  180 */     ??? = localInteger.toString();
/*  181 */     while (((String)???).length() < 3)
/*  182 */       ??? = "0" + (String)???;
/*  183 */     paramString = paramString + (String)???;
/*  184 */     return paramString;
/*      */   }
/*      */ 
/*      */   String constantPlaceholder(Object paramObject)
/*      */   {
/*  206 */     String str = "CONSTANT_PLACEHOLDER_" + this.cph++;
/*  207 */     if (MethodHandleStatics.DUMP_CLASS_FILES) str = str + " <<" + paramObject.toString() + ">>";
/*  208 */     if (this.cpPatches.containsKey(str)) {
/*  209 */       throw new InternalError("observed CP placeholder twice: " + str);
/*      */     }
/*      */ 
/*  212 */     int i = this.cw.newConst(str);
/*  213 */     this.cpPatches.put(str, new CpPatch(i, str, paramObject));
/*  214 */     return str;
/*      */   }
/*      */ 
/*      */   Object[] cpPatches(byte[] paramArrayOfByte) {
/*  218 */     int i = getConstantPoolSize(paramArrayOfByte);
/*  219 */     Object[] arrayOfObject = new Object[i];
/*  220 */     for (CpPatch localCpPatch : this.cpPatches.values()) {
/*  221 */       if (localCpPatch.index >= i)
/*  222 */         throw new InternalError("in cpool[" + i + "]: " + localCpPatch + "\n" + Arrays.toString(Arrays.copyOf(paramArrayOfByte, 20)));
/*  223 */       arrayOfObject[localCpPatch.index] = localCpPatch.value;
/*      */     }
/*  225 */     return arrayOfObject;
/*      */   }
/*      */ 
/*      */   private static int getConstantPoolSize(byte[] paramArrayOfByte)
/*      */   {
/*  240 */     return (paramArrayOfByte[8] & 0xFF) << 8 | paramArrayOfByte[9] & 0xFF;
/*      */   }
/*      */ 
/*      */   private MemberName loadMethod(byte[] paramArrayOfByte)
/*      */   {
/*  250 */     Class localClass = loadAndInitializeInvokerClass(paramArrayOfByte, cpPatches(paramArrayOfByte));
/*  251 */     return resolveInvokerMember(localClass, this.invokerName, this.invokerType);
/*      */   }
/*      */ 
/*      */   private static Class<?> loadAndInitializeInvokerClass(byte[] paramArrayOfByte, Object[] paramArrayOfObject)
/*      */   {
/*  262 */     Class localClass = MethodHandleStatics.UNSAFE.defineAnonymousClass(HOST_CLASS, paramArrayOfByte, paramArrayOfObject);
/*  263 */     MethodHandleStatics.UNSAFE.ensureClassInitialized(localClass);
/*  264 */     return localClass;
/*      */   }
/*      */ 
/*      */   private static MemberName resolveInvokerMember(Class<?> paramClass, String paramString, MethodType paramMethodType)
/*      */   {
/*  276 */     MemberName localMemberName = new MemberName(paramClass, paramString, paramMethodType, (byte)6);
/*      */     try
/*      */     {
/*  280 */       localMemberName = MEMBERNAME_FACTORY.resolveOrFail((byte)6, localMemberName, HOST_CLASS, ReflectiveOperationException.class);
/*      */     } catch (ReflectiveOperationException localReflectiveOperationException) {
/*  282 */       throw MethodHandleStatics.newInternalError(localReflectiveOperationException);
/*      */     }
/*      */ 
/*  285 */     return localMemberName;
/*      */   }
/*      */ 
/*      */   private void classFilePrologue()
/*      */   {
/*  293 */     this.cw = new ClassWriter(3);
/*  294 */     this.cw.visit(50, 48, this.className, null, "java/lang/invoke/LambdaForm", null);
/*  295 */     this.cw.visitSource(this.sourceFile, null);
/*      */ 
/*  297 */     String str = this.invokerType.toMethodDescriptorString();
/*  298 */     this.mv = this.cw.visitMethod(8, this.invokerName, str, null, null);
/*      */   }
/*      */ 
/*      */   private void classFileEpilogue()
/*      */   {
/*  305 */     this.mv.visitMaxs(0, 0);
/*  306 */     this.mv.visitEnd();
/*      */   }
/*      */ 
/*      */   private void emitConst(Object paramObject)
/*      */   {
/*  313 */     if (paramObject == null) {
/*  314 */       this.mv.visitInsn(1);
/*  315 */       return;
/*      */     }
/*  317 */     if ((paramObject instanceof Integer)) {
/*  318 */       emitIconstInsn(((Integer)paramObject).intValue());
/*  319 */       return;
/*      */     }
/*  321 */     if ((paramObject instanceof Long)) {
/*  322 */       long l = ((Long)paramObject).longValue();
/*  323 */       if (l == (short)(int)l) {
/*  324 */         emitIconstInsn((int)l);
/*  325 */         this.mv.visitInsn(133);
/*  326 */         return;
/*      */       }
/*      */     }
/*  329 */     if ((paramObject instanceof Float)) {
/*  330 */       float f = ((Float)paramObject).floatValue();
/*  331 */       if (f == (short)(int)f) {
/*  332 */         emitIconstInsn((int)f);
/*  333 */         this.mv.visitInsn(134);
/*  334 */         return;
/*      */       }
/*      */     }
/*  337 */     if ((paramObject instanceof Double)) {
/*  338 */       double d = ((Double)paramObject).doubleValue();
/*  339 */       if (d == (short)(int)d) {
/*  340 */         emitIconstInsn((int)d);
/*  341 */         this.mv.visitInsn(135);
/*  342 */         return;
/*      */       }
/*      */     }
/*  345 */     if ((paramObject instanceof Boolean)) {
/*  346 */       emitIconstInsn(((Boolean)paramObject).booleanValue() ? 1 : 0);
/*  347 */       return;
/*      */     }
/*      */ 
/*  350 */     this.mv.visitLdcInsn(paramObject);
/*      */   }
/*      */ 
/*      */   private void emitIconstInsn(int paramInt)
/*      */   {
/*      */     int i;
/*  355 */     switch (paramInt) { case 0:
/*  356 */       i = 3; break;
/*      */     case 1:
/*  357 */       i = 4; break;
/*      */     case 2:
/*  358 */       i = 5; break;
/*      */     case 3:
/*  359 */       i = 6; break;
/*      */     case 4:
/*  360 */       i = 7; break;
/*      */     case 5:
/*  361 */       i = 8; break;
/*      */     default:
/*  363 */       if (paramInt == (byte)paramInt)
/*  364 */         this.mv.visitIntInsn(16, paramInt & 0xFF);
/*  365 */       else if (paramInt == (short)paramInt)
/*  366 */         this.mv.visitIntInsn(17, (char)paramInt);
/*      */       else {
/*  368 */         this.mv.visitLdcInsn(Integer.valueOf(paramInt));
/*      */       }
/*  370 */       return;
/*      */     }
/*  372 */     this.mv.visitInsn(i);
/*      */   }
/*      */ 
/*      */   private void emitLoadInsn(char paramChar, int paramInt)
/*      */   {
/*      */     int i;
/*  380 */     switch (paramChar) { case 'I':
/*  381 */       i = 21; break;
/*      */     case 'J':
/*  382 */       i = 22; break;
/*      */     case 'F':
/*  383 */       i = 23; break;
/*      */     case 'D':
/*  384 */       i = 24; break;
/*      */     case 'L':
/*  385 */       i = 25; break;
/*      */     case 'E':
/*      */     case 'G':
/*      */     case 'H':
/*      */     case 'K':
/*      */     default:
/*  387 */       throw new InternalError("unknown type: " + paramChar);
/*      */     }
/*  389 */     this.mv.visitVarInsn(i, this.localsMap[paramInt]);
/*      */   }
/*      */   private void emitAloadInsn(int paramInt) {
/*  392 */     emitLoadInsn('L', paramInt);
/*      */   }
/*      */ 
/*      */   private void emitStoreInsn(char paramChar, int paramInt)
/*      */   {
/*      */     int i;
/*  397 */     switch (paramChar) { case 'I':
/*  398 */       i = 54; break;
/*      */     case 'J':
/*  399 */       i = 55; break;
/*      */     case 'F':
/*  400 */       i = 56; break;
/*      */     case 'D':
/*  401 */       i = 57; break;
/*      */     case 'L':
/*  402 */       i = 58; break;
/*      */     case 'E':
/*      */     case 'G':
/*      */     case 'H':
/*      */     case 'K':
/*      */     default:
/*  404 */       throw new InternalError("unknown type: " + paramChar);
/*      */     }
/*  406 */     this.mv.visitVarInsn(i, this.localsMap[paramInt]);
/*      */   }
/*      */   private void emitAstoreInsn(int paramInt) {
/*  409 */     emitStoreInsn('L', paramInt);
/*      */   }
/*      */ 
/*      */   private void emitBoxing(Class<?> paramClass)
/*      */   {
/*  418 */     Wrapper localWrapper = Wrapper.forPrimitiveType(paramClass);
/*  419 */     String str1 = "java/lang/" + localWrapper.wrapperType().getSimpleName();
/*  420 */     String str2 = "valueOf";
/*  421 */     String str3 = "(" + localWrapper.basicTypeChar() + ")L" + str1 + ";";
/*  422 */     this.mv.visitMethodInsn(184, str1, str2, str3);
/*      */   }
/*      */ 
/*      */   private void emitUnboxing(Class<?> paramClass)
/*      */   {
/*  431 */     Wrapper localWrapper = Wrapper.forWrapperType(paramClass);
/*  432 */     String str1 = "java/lang/" + localWrapper.wrapperType().getSimpleName();
/*  433 */     String str2 = localWrapper.primitiveSimpleName() + "Value";
/*  434 */     String str3 = "()" + localWrapper.basicTypeChar();
/*  435 */     this.mv.visitTypeInsn(192, str1);
/*  436 */     this.mv.visitMethodInsn(182, str1, str2, str3);
/*      */   }
/*      */ 
/*      */   private void emitImplicitConversion(char paramChar, Class<?> paramClass)
/*      */   {
/*  446 */     switch (paramChar) {
/*      */     case 'L':
/*  448 */       if (VerifyType.isNullConversion(Object.class, paramClass))
/*  449 */         return;
/*  450 */       if (isStaticallyNameable(paramClass)) {
/*  451 */         this.mv.visitTypeInsn(192, getInternalName(paramClass));
/*      */       } else {
/*  453 */         this.mv.visitLdcInsn(constantPlaceholder(paramClass));
/*  454 */         this.mv.visitTypeInsn(192, "java/lang/Class");
/*  455 */         this.mv.visitInsn(95);
/*  456 */         this.mv.visitMethodInsn(182, "java/lang/Class", "cast", "(Ljava/lang/Object;)Ljava/lang/Object;");
/*  457 */         if (paramClass.isArray())
/*  458 */           this.mv.visitTypeInsn(192, "[Ljava/lang/Object;");
/*      */       }
/*  460 */       return;
/*      */     case 'I':
/*  462 */       if (!VerifyType.isNullConversion(Integer.TYPE, paramClass))
/*  463 */         emitPrimCast(paramChar, Wrapper.basicTypeChar(paramClass));
/*  464 */       return;
/*      */     case 'J':
/*  466 */       assert (paramClass == Long.TYPE);
/*  467 */       return;
/*      */     case 'F':
/*  469 */       assert (paramClass == Float.TYPE);
/*  470 */       return;
/*      */     case 'D':
/*  472 */       assert (paramClass == Double.TYPE);
/*  473 */       return;
/*      */     case 'E':
/*      */     case 'G':
/*      */     case 'H':
/*  475 */     case 'K': } throw new InternalError("bad implicit conversion: tc=" + paramChar + ": " + paramClass);
/*      */   }
/*      */ 
/*      */   private void emitReturnInsn(Class<?> paramClass)
/*      */   {
/*      */     int i;
/*  483 */     switch (Wrapper.basicTypeChar(paramClass)) { case 'I':
/*  484 */       i = 172; break;
/*      */     case 'J':
/*  485 */       i = 173; break;
/*      */     case 'F':
/*  486 */       i = 174; break;
/*      */     case 'D':
/*  487 */       i = 175; break;
/*      */     case 'L':
/*  488 */       i = 176; break;
/*      */     case 'V':
/*  489 */       i = 177; break;
/*      */     case 'E':
/*      */     case 'G':
/*      */     case 'H':
/*      */     case 'K':
/*      */     case 'M':
/*      */     case 'N':
/*      */     case 'O':
/*      */     case 'P':
/*      */     case 'Q':
/*      */     case 'R':
/*      */     case 'S':
/*      */     case 'T':
/*      */     case 'U':
/*      */     default:
/*  491 */       throw new InternalError("unknown return type: " + paramClass);
/*      */     }
/*  493 */     this.mv.visitInsn(i);
/*      */   }
/*      */ 
/*      */   private static String getInternalName(Class<?> paramClass) {
/*  497 */     assert (VerifyAccess.isTypeVisible(paramClass, Object.class));
/*  498 */     return paramClass.getName().replace('.', '/');
/*      */   }
/*      */ 
/*      */   static MemberName generateCustomizedCode(LambdaForm paramLambdaForm, MethodType paramMethodType)
/*      */   {
/*  509 */     InvokerBytecodeGenerator localInvokerBytecodeGenerator = new InvokerBytecodeGenerator("MH", paramLambdaForm, paramMethodType);
/*  510 */     return localInvokerBytecodeGenerator.loadMethod(localInvokerBytecodeGenerator.generateCustomizedCodeBytes());
/*      */   }
/*      */ 
/*      */   private byte[] generateCustomizedCodeBytes()
/*      */   {
/*  517 */     classFilePrologue();
/*      */ 
/*  520 */     this.mv.visitAnnotation("Ljava/lang/invoke/LambdaForm$Hidden;", true);
/*      */ 
/*  523 */     this.mv.visitAnnotation("Ljava/lang/invoke/LambdaForm$Compiled;", true);
/*      */ 
/*  526 */     this.mv.visitAnnotation("Ljava/lang/invoke/ForceInline;", true);
/*      */ 
/*  530 */     for (int i = this.lambdaForm.arity; i < this.lambdaForm.names.length; i++) {
/*  531 */       LambdaForm.Name localName = this.lambdaForm.names[i];
/*  532 */       MemberName localMemberName = localName.function.member();
/*      */ 
/*  534 */       if (isSelectAlternative(localMemberName))
/*      */       {
/*  537 */         emitSelectAlternative(localName, this.lambdaForm.names[(i + 1)]);
/*  538 */         i++;
/*  539 */       } else if (isStaticallyInvocable(localMemberName)) {
/*  540 */         emitStaticInvoke(localMemberName, localName);
/*      */       } else {
/*  542 */         emitInvoke(localName);
/*      */       }
/*      */ 
/*  548 */       if ((i != this.lambdaForm.names.length - 1) || (i != this.lambdaForm.result))
/*      */       {
/*  550 */         if (localName.type != 'V')
/*      */         {
/*  552 */           emitStoreInsn(localName.type, localName.index());
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  557 */     emitReturn();
/*      */ 
/*  559 */     classFileEpilogue();
/*  560 */     bogusMethod(new Object[] { this.lambdaForm });
/*      */ 
/*  562 */     byte[] arrayOfByte = this.cw.toByteArray();
/*  563 */     maybeDump(this.className, arrayOfByte);
/*  564 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   void emitInvoke(LambdaForm.Name paramName)
/*      */   {
/*  575 */     MethodHandle localMethodHandle = paramName.function.resolvedHandle;
/*  576 */     assert (localMethodHandle != null) : paramName.exprString();
/*  577 */     this.mv.visitLdcInsn(constantPlaceholder(localMethodHandle));
/*  578 */     this.mv.visitTypeInsn(192, "java/lang/invoke/MethodHandle");
/*      */ 
/*  589 */     for (int i = 0; i < paramName.arguments.length; i++) {
/*  590 */       emitPushArgument(paramName, i);
/*      */     }
/*      */ 
/*  594 */     MethodType localMethodType = paramName.function.methodType();
/*  595 */     this.mv.visitMethodInsn(182, "java/lang/invoke/MethodHandle", "invokeBasic", localMethodType.basicType().toMethodDescriptorString());
/*      */   }
/*      */ 
/*      */   static boolean isStaticallyInvocable(MemberName paramMemberName)
/*      */   {
/*  607 */     if (paramMemberName == null) return false;
/*  608 */     if (paramMemberName.isConstructor()) return false;
/*  609 */     Class localClass1 = paramMemberName.getDeclaringClass();
/*  610 */     if ((localClass1.isArray()) || (localClass1.isPrimitive()))
/*  611 */       return false;
/*  612 */     if ((localClass1.isAnonymousClass()) || (localClass1.isLocalClass()))
/*  613 */       return false;
/*  614 */     if (localClass1.getClassLoader() != MethodHandle.class.getClassLoader())
/*  615 */       return false;
/*  616 */     MethodType localMethodType = paramMemberName.getMethodOrFieldType();
/*  617 */     if (!isStaticallyNameable(localMethodType.returnType()))
/*  618 */       return false;
/*  619 */     for (Class localClass2 : localMethodType.parameterArray())
/*  620 */       if (!isStaticallyNameable(localClass2))
/*  621 */         return false;
/*  622 */     if ((!paramMemberName.isPrivate()) && (VerifyAccess.isSamePackage(MethodHandle.class, localClass1)))
/*  623 */       return true;
/*  624 */     if ((paramMemberName.isPublic()) && (isStaticallyNameable(localClass1)))
/*  625 */       return true;
/*  626 */     return false;
/*      */   }
/*      */ 
/*      */   static boolean isStaticallyNameable(Class<?> paramClass) {
/*  630 */     while (paramClass.isArray())
/*  631 */       paramClass = paramClass.getComponentType();
/*  632 */     if (paramClass.isPrimitive())
/*  633 */       return true;
/*  634 */     if (paramClass.getClassLoader() != Object.class.getClassLoader())
/*  635 */       return false;
/*  636 */     if (VerifyAccess.isSamePackage(MethodHandle.class, paramClass))
/*  637 */       return true;
/*  638 */     if (!Modifier.isPublic(paramClass.getModifiers()))
/*  639 */       return false;
/*  640 */     for (Class localClass : STATICALLY_INVOCABLE_PACKAGES) {
/*  641 */       if (VerifyAccess.isSamePackage(localClass, paramClass))
/*  642 */         return true;
/*      */     }
/*  644 */     return false;
/*      */   }
/*      */ 
/*      */   void emitStaticInvoke(MemberName paramMemberName, LambdaForm.Name paramName)
/*      */   {
/*  653 */     assert (paramMemberName.equals(paramName.function.member()));
/*  654 */     String str1 = getInternalName(paramMemberName.getDeclaringClass());
/*  655 */     String str2 = paramMemberName.getName();
/*      */ 
/*  657 */     byte b = paramMemberName.getReferenceKind();
/*  658 */     if (b == 7)
/*      */     {
/*  660 */       assert (paramMemberName.canBeStaticallyBound()) : paramMemberName;
/*  661 */       b = 5;
/*      */     }
/*      */ 
/*  664 */     if ((paramMemberName.getDeclaringClass().isInterface()) && (b == 5))
/*      */     {
/*  667 */       b = 9;
/*      */     }
/*      */ 
/*  671 */     for (int i = 0; i < paramName.arguments.length; i++)
/*  672 */       emitPushArgument(paramName, i);
/*      */     String str3;
/*  676 */     if (paramMemberName.isMethod()) {
/*  677 */       str3 = paramMemberName.getMethodType().toMethodDescriptorString();
/*  678 */       this.mv.visitMethodInsn(refKindOpcode(b), str1, str2, str3);
/*      */     } else {
/*  680 */       str3 = MethodType.toFieldDescriptorString(paramMemberName.getFieldType());
/*  681 */       this.mv.visitFieldInsn(refKindOpcode(b), str1, str2, str3);
/*      */     }
/*      */   }
/*      */ 
/*  685 */   int refKindOpcode(byte paramByte) { switch (paramByte) { case 5:
/*  686 */       return 182;
/*      */     case 6:
/*  687 */       return 184;
/*      */     case 7:
/*  688 */       return 183;
/*      */     case 9:
/*  689 */       return 185;
/*      */     case 1:
/*  690 */       return 180;
/*      */     case 3:
/*  691 */       return 181;
/*      */     case 2:
/*  692 */       return 178;
/*      */     case 4:
/*  693 */       return 179;
/*      */     case 8: }
/*  695 */     throw new InternalError("refKind=" + paramByte);
/*      */   }
/*      */ 
/*      */   private boolean isSelectAlternative(MemberName paramMemberName)
/*      */   {
/*  705 */     return (paramMemberName != null) && (paramMemberName.getDeclaringClass() == MethodHandleImpl.class) && (paramMemberName.getName().equals("selectAlternative"));
/*      */   }
/*      */ 
/*      */   private void emitSelectAlternative(LambdaForm.Name paramName1, LambdaForm.Name paramName2)
/*      */   {
/*  724 */     MethodType localMethodType = paramName1.function.methodType();
/*      */ 
/*  726 */     LambdaForm.Name localName = (LambdaForm.Name)paramName2.arguments[0];
/*      */ 
/*  728 */     Label localLabel1 = new Label();
/*  729 */     Label localLabel2 = new Label();
/*      */ 
/*  732 */     emitPushArgument(paramName1, 0);
/*  733 */     this.mv.visitInsn(4);
/*      */ 
/*  736 */     this.mv.visitJumpInsn(160, localLabel1);
/*      */ 
/*  739 */     MethodHandle localMethodHandle1 = (MethodHandle)paramName1.arguments[1];
/*  740 */     emitPushArgument(paramName1, 1);
/*  741 */     emitAstoreInsn(localName.index());
/*  742 */     emitInvoke(paramName2);
/*      */ 
/*  745 */     this.mv.visitJumpInsn(167, localLabel2);
/*      */ 
/*  748 */     this.mv.visitLabel(localLabel1);
/*      */ 
/*  751 */     MethodHandle localMethodHandle2 = (MethodHandle)paramName1.arguments[2];
/*  752 */     emitPushArgument(paramName1, 2);
/*  753 */     emitAstoreInsn(localName.index());
/*  754 */     emitInvoke(paramName2);
/*      */ 
/*  757 */     this.mv.visitLabel(localLabel2);
/*      */   }
/*      */ 
/*      */   private void emitPushArgument(LambdaForm.Name paramName, int paramInt)
/*      */   {
/*  766 */     Object localObject = paramName.arguments[paramInt];
/*  767 */     int i = paramName.function.parameterType(paramInt);
/*  768 */     MethodType localMethodType = paramName.function.methodType();
/*  769 */     if ((localObject instanceof LambdaForm.Name)) {
/*  770 */       LambdaForm.Name localName = (LambdaForm.Name)localObject;
/*  771 */       emitLoadInsn(localName.type, localName.index());
/*  772 */       emitImplicitConversion(localName.type, localMethodType.parameterType(paramInt));
/*  773 */     } else if (((localObject == null) || ((localObject instanceof String))) && (i == 76)) {
/*  774 */       emitConst(localObject);
/*      */     }
/*  776 */     else if ((Wrapper.isWrapperType(localObject.getClass())) && (i != 76)) {
/*  777 */       emitConst(localObject);
/*      */     } else {
/*  779 */       this.mv.visitLdcInsn(constantPlaceholder(localObject));
/*  780 */       emitImplicitConversion('L', localMethodType.parameterType(paramInt));
/*      */     }
/*      */   }
/*      */ 
/*      */   private void emitReturn()
/*      */   {
/*  790 */     if (this.lambdaForm.result == -1)
/*      */     {
/*  792 */       this.mv.visitInsn(177);
/*      */     } else {
/*  794 */       LambdaForm.Name localName = this.lambdaForm.names[this.lambdaForm.result];
/*  795 */       char c1 = Wrapper.basicTypeChar(this.invokerType.returnType());
/*      */ 
/*  798 */       if (this.lambdaForm.result != this.lambdaForm.names.length - 1) {
/*  799 */         emitLoadInsn(localName.type, this.lambdaForm.result);
/*      */       }
/*      */ 
/*  805 */       if (c1 != localName.type)
/*      */       {
/*  807 */         if (c1 == 'L')
/*      */         {
/*  809 */           char c2 = Wrapper.forWrapperType(this.invokerType.returnType()).basicTypeChar();
/*  810 */           if (c2 != localName.type) {
/*  811 */             emitPrimCast(localName.type, c2);
/*      */           }
/*      */ 
/*  814 */           emitBoxing(this.invokerType.returnType());
/*      */         }
/*  817 */         else if (localName.type != 'L')
/*      */         {
/*  819 */           emitPrimCast(localName.type, c1);
/*      */         }
/*      */         else {
/*  822 */           throw new InternalError("no ref-to-prim (unboxing) casts supported right now");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  828 */       emitReturnInsn(this.invokerType.returnType());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void emitPrimCast(char paramChar1, char paramChar2)
/*      */   {
/*  848 */     if (paramChar1 == paramChar2)
/*      */     {
/*  850 */       return;
/*      */     }
/*  852 */     Wrapper localWrapper1 = Wrapper.forBasicType(paramChar1);
/*  853 */     Wrapper localWrapper2 = Wrapper.forBasicType(paramChar2);
/*  854 */     if (localWrapper1.isSubwordOrInt())
/*      */     {
/*  856 */       emitI2X(paramChar2);
/*      */     }
/*  859 */     else if (localWrapper2.isSubwordOrInt())
/*      */     {
/*  861 */       emitX2I(paramChar1);
/*  862 */       if (localWrapper2.bitWidth() < 32)
/*      */       {
/*  864 */         emitI2X(paramChar2);
/*      */       }
/*      */     }
/*      */     else {
/*  868 */       int i = 0;
/*  869 */       switch (paramChar1) {
/*      */       case 'J':
/*  871 */         if (paramChar2 == 'F') this.mv.visitInsn(137);
/*  872 */         else if (paramChar2 == 'D') this.mv.visitInsn(138); else
/*  873 */           i = 1;
/*  874 */         break;
/*      */       case 'F':
/*  876 */         if (paramChar2 == 'J') this.mv.visitInsn(140);
/*  877 */         else if (paramChar2 == 'D') this.mv.visitInsn(141); else
/*  878 */           i = 1;
/*  879 */         break;
/*      */       case 'D':
/*  881 */         if (paramChar2 == 'J') this.mv.visitInsn(143);
/*  882 */         else if (paramChar2 == 'F') this.mv.visitInsn(144); else
/*  883 */           i = 1;
/*  884 */         break;
/*      */       default:
/*  886 */         i = 1;
/*      */       }
/*      */ 
/*  889 */       if (i != 0)
/*  890 */         throw new IllegalStateException("unhandled prim cast: " + paramChar1 + "2" + paramChar2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void emitI2X(char paramChar)
/*      */   {
/*  897 */     switch (paramChar) { case 'B':
/*  898 */       this.mv.visitInsn(145); break;
/*      */     case 'S':
/*  899 */       this.mv.visitInsn(147); break;
/*      */     case 'C':
/*  900 */       this.mv.visitInsn(146); break;
/*      */     case 'I':
/*  901 */       break;
/*      */     case 'J':
/*  902 */       this.mv.visitInsn(133); break;
/*      */     case 'F':
/*  903 */       this.mv.visitInsn(134); break;
/*      */     case 'D':
/*  904 */       this.mv.visitInsn(135); break;
/*      */     case 'Z':
/*  907 */       this.mv.visitInsn(4);
/*  908 */       this.mv.visitInsn(126);
/*  909 */       break;
/*      */     case 'E':
/*      */     case 'G':
/*      */     case 'H':
/*      */     case 'K':
/*      */     case 'L':
/*      */     case 'M':
/*      */     case 'N':
/*      */     case 'O':
/*      */     case 'P':
/*      */     case 'Q':
/*      */     case 'R':
/*      */     case 'T':
/*      */     case 'U':
/*      */     case 'V':
/*      */     case 'W':
/*      */     case 'X':
/*      */     case 'Y':
/*      */     default:
/*  910 */       throw new InternalError("unknown type: " + paramChar); }
/*      */   }
/*      */ 
/*      */   private void emitX2I(char paramChar)
/*      */   {
/*  915 */     switch (paramChar) { case 'J':
/*  916 */       this.mv.visitInsn(136); break;
/*      */     case 'F':
/*  917 */       this.mv.visitInsn(139); break;
/*      */     case 'D':
/*  918 */       this.mv.visitInsn(142); break;
/*      */     default:
/*  919 */       throw new InternalError("unknown type: " + paramChar); }
/*      */   }
/*      */ 
/*      */   private static String basicTypeCharSignature(String paramString, MethodType paramMethodType)
/*      */   {
/*  924 */     StringBuilder localStringBuilder = new StringBuilder(paramString);
/*  925 */     for (Class localClass : paramMethodType.parameterList())
/*  926 */       localStringBuilder.append(Wrapper.forBasicType(localClass).basicTypeChar());
/*  927 */     localStringBuilder.append('_').append(Wrapper.forBasicType(paramMethodType.returnType()).basicTypeChar());
/*  928 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   static MemberName generateLambdaFormInterpreterEntryPoint(String paramString)
/*      */   {
/*  938 */     assert (LambdaForm.isValidSignature(paramString));
/*      */ 
/*  942 */     char c = LambdaForm.signatureReturn(paramString);
/*  943 */     MethodType localMethodType = MethodType.methodType(LambdaForm.typeClass(c), MethodHandle.class);
/*      */ 
/*  945 */     int i = LambdaForm.signatureArity(paramString);
/*  946 */     for (int j = 1; j < i; j++) {
/*  947 */       localMethodType = localMethodType.appendParameterTypes(new Class[] { LambdaForm.typeClass(paramString.charAt(j)) });
/*      */     }
/*  949 */     InvokerBytecodeGenerator localInvokerBytecodeGenerator = new InvokerBytecodeGenerator("LFI", "interpret_" + c, localMethodType);
/*  950 */     return localInvokerBytecodeGenerator.loadMethod(localInvokerBytecodeGenerator.generateLambdaFormInterpreterEntryPointBytes());
/*      */   }
/*      */ 
/*      */   private byte[] generateLambdaFormInterpreterEntryPointBytes() {
/*  954 */     classFilePrologue();
/*      */ 
/*  957 */     this.mv.visitAnnotation("Ljava/lang/invoke/LambdaForm$Hidden;", true);
/*      */ 
/*  960 */     this.mv.visitAnnotation("Ljava/lang/invoke/DontInline;", true);
/*      */ 
/*  963 */     emitIconstInsn(this.invokerType.parameterCount());
/*  964 */     this.mv.visitTypeInsn(189, "java/lang/Object");
/*      */ 
/*  967 */     for (int i = 0; i < this.invokerType.parameterCount(); i++) {
/*  968 */       localObject = this.invokerType.parameterType(i);
/*  969 */       this.mv.visitInsn(89);
/*  970 */       emitIconstInsn(i);
/*  971 */       emitLoadInsn(Wrapper.basicTypeChar((Class)localObject), i);
/*      */ 
/*  973 */       if (((Class)localObject).isPrimitive()) {
/*  974 */         emitBoxing((Class)localObject);
/*      */       }
/*  976 */       this.mv.visitInsn(83);
/*      */     }
/*      */ 
/*  979 */     emitAloadInsn(0);
/*  980 */     this.mv.visitFieldInsn(180, "java/lang/invoke/MethodHandle", "form", "Ljava/lang/invoke/LambdaForm;");
/*  981 */     this.mv.visitInsn(95);
/*  982 */     this.mv.visitMethodInsn(182, "java/lang/invoke/LambdaForm", "interpretWithArguments", "([Ljava/lang/Object;)Ljava/lang/Object;");
/*      */ 
/*  985 */     Class localClass = this.invokerType.returnType();
/*  986 */     if ((localClass.isPrimitive()) && (localClass != Void.TYPE)) {
/*  987 */       emitUnboxing(Wrapper.asWrapperType(localClass));
/*      */     }
/*      */ 
/*  991 */     emitReturnInsn(localClass);
/*      */ 
/*  993 */     classFileEpilogue();
/*  994 */     bogusMethod(new Object[] { this.invokerType });
/*      */ 
/*  996 */     Object localObject = this.cw.toByteArray();
/*  997 */     maybeDump(this.className, (byte[])localObject);
/*  998 */     return localObject;
/*      */   }
/*      */ 
/*      */   static MemberName generateNamedFunctionInvoker(MethodTypeForm paramMethodTypeForm)
/*      */   {
/* 1009 */     MethodType localMethodType = LambdaForm.NamedFunction.INVOKER_METHOD_TYPE;
/* 1010 */     String str = basicTypeCharSignature("invoke_", paramMethodTypeForm.erasedType());
/* 1011 */     InvokerBytecodeGenerator localInvokerBytecodeGenerator = new InvokerBytecodeGenerator("NFI", str, localMethodType);
/* 1012 */     return localInvokerBytecodeGenerator.loadMethod(localInvokerBytecodeGenerator.generateNamedFunctionInvokerImpl(paramMethodTypeForm));
/*      */   }
/*      */ 
/*      */   private byte[] generateNamedFunctionInvokerImpl(MethodTypeForm paramMethodTypeForm)
/*      */   {
/* 1018 */     MethodType localMethodType = paramMethodTypeForm.erasedType();
/* 1019 */     classFilePrologue();
/*      */ 
/* 1022 */     this.mv.visitAnnotation("Ljava/lang/invoke/LambdaForm$Hidden;", true);
/*      */ 
/* 1025 */     this.mv.visitAnnotation("Ljava/lang/invoke/ForceInline;", true);
/*      */ 
/* 1028 */     emitAloadInsn(0);
/*      */     Object localObject2;
/* 1031 */     for (int i = 0; i < localMethodType.parameterCount(); i++) {
/* 1032 */       emitAloadInsn(1);
/* 1033 */       emitIconstInsn(i);
/* 1034 */       this.mv.visitInsn(50);
/*      */ 
/* 1037 */       localClass = localMethodType.parameterType(i);
/* 1038 */       if (localClass.isPrimitive()) {
/* 1039 */         localObject1 = localMethodType.basicType().wrap().parameterType(i);
/* 1040 */         localObject2 = Wrapper.forBasicType(localClass);
/* 1041 */         Object localObject3 = ((Wrapper)localObject2).isSubwordOrInt() ? Wrapper.INT : localObject2;
/* 1042 */         emitUnboxing(localObject3.wrapperType());
/* 1043 */         emitPrimCast(localObject3.basicTypeChar(), ((Wrapper)localObject2).basicTypeChar());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1048 */     String str = localMethodType.basicType().toMethodDescriptorString();
/* 1049 */     this.mv.visitMethodInsn(182, "java/lang/invoke/MethodHandle", "invokeBasic", str);
/*      */ 
/* 1052 */     Class localClass = localMethodType.returnType();
/* 1053 */     if ((localClass != Void.TYPE) && (localClass.isPrimitive())) {
/* 1054 */       localObject1 = Wrapper.forBasicType(localClass);
/* 1055 */       localObject2 = ((Wrapper)localObject1).isSubwordOrInt() ? Wrapper.INT : localObject1;
/*      */ 
/* 1057 */       emitPrimCast(((Wrapper)localObject1).basicTypeChar(), ((Wrapper)localObject2).basicTypeChar());
/* 1058 */       emitBoxing(((Wrapper)localObject2).primitiveType());
/*      */     }
/*      */ 
/* 1062 */     if (localClass == Void.TYPE) {
/* 1063 */       this.mv.visitInsn(1);
/*      */     }
/* 1065 */     emitReturnInsn(Object.class);
/*      */ 
/* 1067 */     classFileEpilogue();
/* 1068 */     bogusMethod(new Object[] { localMethodType });
/*      */ 
/* 1070 */     Object localObject1 = this.cw.toByteArray();
/* 1071 */     maybeDump(this.className, (byte[])localObject1);
/* 1072 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private void bogusMethod(Object[] paramArrayOfObject)
/*      */   {
/* 1080 */     if (MethodHandleStatics.DUMP_CLASS_FILES) {
/* 1081 */       this.mv = this.cw.visitMethod(8, "dummy", "()V", null, null);
/* 1082 */       for (Object localObject : paramArrayOfObject) {
/* 1083 */         this.mv.visitLdcInsn(localObject.toString());
/* 1084 */         this.mv.visitInsn(87);
/*      */       }
/* 1086 */       this.mv.visitInsn(177);
/* 1087 */       this.mv.visitMaxs(0, 0);
/* 1088 */       this.mv.visitEnd();
/*      */     }
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   83 */     MEMBERNAME_FACTORY = MemberName.getFactory();
/*   84 */     HOST_CLASS = LambdaForm.class;
/*      */ 
/*  131 */     if (MethodHandleStatics.DUMP_CLASS_FILES) {
/*  132 */       DUMP_CLASS_FILES_COUNTERS = new HashMap();
/*      */       try {
/*  134 */         File localFile = new File("DUMP_CLASS_FILES");
/*  135 */         if (!localFile.exists()) {
/*  136 */           localFile.mkdirs();
/*      */         }
/*  138 */         DUMP_CLASS_FILES_DIR = localFile;
/*  139 */         System.out.println("Dumping class files to " + DUMP_CLASS_FILES_DIR + "/...");
/*      */       } catch (Exception localException) {
/*  141 */         throw MethodHandleStatics.newInternalError(localException);
/*      */       }
/*      */     } else {
/*  144 */       DUMP_CLASS_FILES_COUNTERS = null;
/*  145 */       DUMP_CLASS_FILES_DIR = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   class CpPatch
/*      */   {
/*      */     final int index;
/*      */     final String placeholder;
/*      */     final Object value;
/*      */ 
/*      */     CpPatch(int paramString, String paramObject, Object arg4)
/*      */     {
/*  192 */       this.index = paramString;
/*  193 */       this.placeholder = paramObject;
/*      */       Object localObject;
/*  194 */       this.value = localObject;
/*      */     }
/*      */     public String toString() {
/*  197 */       return "CpPatch/index=" + this.index + ",placeholder=" + this.placeholder + ",value=" + this.value;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.InvokerBytecodeGenerator
 * JD-Core Version:    0.6.2
 */