/*     */ package sun.reflect;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ class MethodAccessorGenerator extends AccessorGenerator
/*     */ {
/*     */   private static final short NUM_BASE_CPOOL_ENTRIES = 12;
/*     */   private static final short NUM_METHODS = 2;
/*     */   private static final short NUM_SERIALIZATION_CPOOL_ENTRIES = 2;
/*  49 */   private static volatile int methodSymnum = 0;
/*  50 */   private static volatile int constructorSymnum = 0;
/*  51 */   private static volatile int serializationConstructorSymnum = 0;
/*     */   private Class declaringClass;
/*     */   private Class[] parameterTypes;
/*     */   private Class returnType;
/*     */   private boolean isConstructor;
/*     */   private boolean forSerialization;
/*     */   private short targetMethodRef;
/*     */   private short invokeIdx;
/*     */   private short invokeDescriptorIdx;
/*     */   private short nonPrimitiveParametersBaseIdx;
/*     */ 
/*     */   public MethodAccessor generateMethod(Class paramClass1, String paramString, Class[] paramArrayOfClass1, Class paramClass2, Class[] paramArrayOfClass2, int paramInt)
/*     */   {
/*  77 */     return (MethodAccessor)generate(paramClass1, paramString, paramArrayOfClass1, paramClass2, paramArrayOfClass2, paramInt, false, false, null);
/*     */   }
/*     */ 
/*     */   public ConstructorAccessor generateConstructor(Class paramClass, Class[] paramArrayOfClass1, Class[] paramArrayOfClass2, int paramInt)
/*     */   {
/*  94 */     return (ConstructorAccessor)generate(paramClass, "<init>", paramArrayOfClass1, Void.TYPE, paramArrayOfClass2, paramInt, true, false, null);
/*     */   }
/*     */ 
/*     */   public SerializationConstructorAccessorImpl generateSerializationConstructor(Class paramClass1, Class[] paramArrayOfClass1, Class[] paramArrayOfClass2, int paramInt, Class paramClass2)
/*     */   {
/* 113 */     return (SerializationConstructorAccessorImpl)generate(paramClass1, "<init>", paramArrayOfClass1, Void.TYPE, paramArrayOfClass2, paramInt, true, true, paramClass2);
/*     */   }
/*     */ 
/*     */   private MagicAccessorImpl generate(final Class paramClass1, String paramString, Class[] paramArrayOfClass1, Class paramClass2, Class[] paramArrayOfClass2, int paramInt, boolean paramBoolean1, boolean paramBoolean2, Class paramClass3)
/*     */   {
/* 136 */     ByteVector localByteVector = ByteVectorFactory.create();
/* 137 */     this.asm = new ClassFileAssembler(localByteVector);
/* 138 */     this.declaringClass = paramClass1;
/* 139 */     this.parameterTypes = paramArrayOfClass1;
/* 140 */     this.returnType = paramClass2;
/* 141 */     this.modifiers = paramInt;
/* 142 */     this.isConstructor = paramBoolean1;
/* 143 */     this.forSerialization = paramBoolean2;
/*     */ 
/* 145 */     this.asm.emitMagicAndVersion();
/*     */ 
/* 273 */     short s1 = 42;
/* 274 */     boolean bool = usesPrimitiveTypes();
/* 275 */     if (bool) {
/* 276 */       s1 = (short)(s1 + 72);
/*     */     }
/* 278 */     if (paramBoolean2) {
/* 279 */       s1 = (short)(s1 + 2);
/*     */     }
/*     */ 
/* 284 */     s1 = (short)(s1 + (short)(2 * numNonPrimitiveParameterTypes()));
/*     */ 
/* 286 */     this.asm.emitShort(add(s1, (short)1));
/*     */ 
/* 288 */     final String str = generateName(paramBoolean1, paramBoolean2);
/* 289 */     this.asm.emitConstantPoolUTF8(str);
/* 290 */     this.asm.emitConstantPoolClass(this.asm.cpi());
/* 291 */     this.thisClass = this.asm.cpi();
/* 292 */     if (paramBoolean1) {
/* 293 */       if (paramBoolean2) {
/* 294 */         this.asm.emitConstantPoolUTF8("sun/reflect/SerializationConstructorAccessorImpl");
/*     */       }
/*     */       else
/* 297 */         this.asm.emitConstantPoolUTF8("sun/reflect/ConstructorAccessorImpl");
/*     */     }
/*     */     else {
/* 300 */       this.asm.emitConstantPoolUTF8("sun/reflect/MethodAccessorImpl");
/*     */     }
/* 302 */     this.asm.emitConstantPoolClass(this.asm.cpi());
/* 303 */     this.superClass = this.asm.cpi();
/* 304 */     this.asm.emitConstantPoolUTF8(getClassName(paramClass1, false));
/* 305 */     this.asm.emitConstantPoolClass(this.asm.cpi());
/* 306 */     this.targetClass = this.asm.cpi();
/* 307 */     short s2 = 0;
/* 308 */     if (paramBoolean2) {
/* 309 */       this.asm.emitConstantPoolUTF8(getClassName(paramClass3, false));
/* 310 */       this.asm.emitConstantPoolClass(this.asm.cpi());
/* 311 */       s2 = this.asm.cpi();
/*     */     }
/* 313 */     this.asm.emitConstantPoolUTF8(paramString);
/* 314 */     this.asm.emitConstantPoolUTF8(buildInternalSignature());
/* 315 */     this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short)1), this.asm.cpi());
/* 316 */     if (isInterface()) {
/* 317 */       this.asm.emitConstantPoolInterfaceMethodref(this.targetClass, this.asm.cpi());
/*     */     }
/* 319 */     else if (paramBoolean2)
/* 320 */       this.asm.emitConstantPoolMethodref(s2, this.asm.cpi());
/*     */     else {
/* 322 */       this.asm.emitConstantPoolMethodref(this.targetClass, this.asm.cpi());
/*     */     }
/*     */ 
/* 325 */     this.targetMethodRef = this.asm.cpi();
/* 326 */     if (paramBoolean1)
/* 327 */       this.asm.emitConstantPoolUTF8("newInstance");
/*     */     else {
/* 329 */       this.asm.emitConstantPoolUTF8("invoke");
/*     */     }
/* 331 */     this.invokeIdx = this.asm.cpi();
/* 332 */     if (paramBoolean1)
/* 333 */       this.asm.emitConstantPoolUTF8("([Ljava/lang/Object;)Ljava/lang/Object;");
/*     */     else {
/* 335 */       this.asm.emitConstantPoolUTF8("(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;");
/*     */     }
/*     */ 
/* 338 */     this.invokeDescriptorIdx = this.asm.cpi();
/*     */ 
/* 341 */     this.nonPrimitiveParametersBaseIdx = add(this.asm.cpi(), (short)2);
/* 342 */     for (int i = 0; i < paramArrayOfClass1.length; i++) {
/* 343 */       Class localClass = paramArrayOfClass1[i];
/* 344 */       if (!isPrimitive(localClass)) {
/* 345 */         this.asm.emitConstantPoolUTF8(getClassName(localClass, false));
/* 346 */         this.asm.emitConstantPoolClass(this.asm.cpi());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 351 */     emitCommonConstantPoolEntries();
/*     */ 
/* 354 */     if (bool) {
/* 355 */       emitBoxingContantPoolEntries();
/*     */     }
/*     */ 
/* 358 */     if (this.asm.cpi() != s1) {
/* 359 */       throw new InternalError("Adjust this code (cpi = " + this.asm.cpi() + ", numCPEntries = " + s1 + ")");
/*     */     }
/*     */ 
/* 364 */     this.asm.emitShort((short)1);
/*     */ 
/* 367 */     this.asm.emitShort(this.thisClass);
/*     */ 
/* 370 */     this.asm.emitShort(this.superClass);
/*     */ 
/* 373 */     this.asm.emitShort((short)0);
/*     */ 
/* 376 */     this.asm.emitShort((short)0);
/*     */ 
/* 379 */     this.asm.emitShort((short)2);
/*     */ 
/* 381 */     emitConstructor();
/* 382 */     emitInvoke();
/*     */ 
/* 385 */     this.asm.emitShort((short)0);
/*     */ 
/* 388 */     localByteVector.trim();
/* 389 */     final byte[] arrayOfByte = localByteVector.getData();
/*     */ 
/* 395 */     return (MagicAccessorImpl)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public MagicAccessorImpl run() {
/*     */         try {
/* 399 */           return (MagicAccessorImpl)ClassDefiner.defineClass(str, arrayOfByte, 0, arrayOfByte.length, paramClass1.getClassLoader()).newInstance();
/*     */         }
/*     */         catch (InstantiationException localInstantiationException)
/*     */         {
/* 407 */           throw ((InternalError)new InternalError().initCause(localInstantiationException));
/*     */         }
/*     */         catch (IllegalAccessException localIllegalAccessException) {
/* 410 */           throw ((InternalError)new InternalError().initCause(localIllegalAccessException));
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void emitInvoke()
/*     */   {
/* 422 */     if (this.parameterTypes.length > 65535) {
/* 423 */       throw new InternalError("Can't handle more than 65535 parameters");
/*     */     }
/*     */ 
/* 427 */     ClassFileAssembler localClassFileAssembler = new ClassFileAssembler();
/* 428 */     if (this.isConstructor)
/*     */     {
/* 430 */       localClassFileAssembler.setMaxLocals(2);
/*     */     }
/*     */     else {
/* 433 */       localClassFileAssembler.setMaxLocals(3);
/*     */     }
/*     */ 
/* 436 */     short s1 = 0;
/*     */ 
/* 438 */     if (this.isConstructor)
/*     */     {
/* 442 */       localClassFileAssembler.opc_new(this.targetClass);
/* 443 */       localClassFileAssembler.opc_dup();
/*     */     }
/*     */     else {
/* 446 */       if (isPrimitive(this.returnType))
/*     */       {
/* 452 */         localClassFileAssembler.opc_new(indexForPrimitiveType(this.returnType));
/* 453 */         localClassFileAssembler.opc_dup();
/*     */       }
/*     */ 
/* 461 */       if (!isStatic())
/*     */       {
/* 471 */         localClassFileAssembler.opc_aload_1();
/* 472 */         localLabel1 = new Label();
/* 473 */         localClassFileAssembler.opc_ifnonnull(localLabel1);
/* 474 */         localClassFileAssembler.opc_new(this.nullPointerClass);
/* 475 */         localClassFileAssembler.opc_dup();
/* 476 */         localClassFileAssembler.opc_invokespecial(this.nullPointerCtorIdx, 0, 0);
/* 477 */         localClassFileAssembler.opc_athrow();
/* 478 */         localLabel1.bind();
/* 479 */         s1 = localClassFileAssembler.getLength();
/* 480 */         localClassFileAssembler.opc_aload_1();
/* 481 */         localClassFileAssembler.opc_checkcast(this.targetClass);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 501 */     Label localLabel1 = new Label();
/* 502 */     if (this.parameterTypes.length == 0) {
/* 503 */       if (this.isConstructor)
/* 504 */         localClassFileAssembler.opc_aload_1();
/*     */       else {
/* 506 */         localClassFileAssembler.opc_aload_2();
/*     */       }
/* 508 */       localClassFileAssembler.opc_ifnull(localLabel1);
/*     */     }
/* 510 */     if (this.isConstructor)
/* 511 */       localClassFileAssembler.opc_aload_1();
/*     */     else {
/* 513 */       localClassFileAssembler.opc_aload_2();
/*     */     }
/* 515 */     localClassFileAssembler.opc_arraylength();
/* 516 */     localClassFileAssembler.opc_sipush((short)this.parameterTypes.length);
/* 517 */     localClassFileAssembler.opc_if_icmpeq(localLabel1);
/* 518 */     localClassFileAssembler.opc_new(this.illegalArgumentClass);
/* 519 */     localClassFileAssembler.opc_dup();
/* 520 */     localClassFileAssembler.opc_invokespecial(this.illegalArgumentCtorIdx, 0, 0);
/* 521 */     localClassFileAssembler.opc_athrow();
/* 522 */     localLabel1.bind();
/*     */ 
/* 528 */     short s2 = this.nonPrimitiveParametersBaseIdx;
/* 529 */     Label localLabel2 = null;
/* 530 */     byte b = 1;
/*     */ 
/* 532 */     for (int i = 0; i < this.parameterTypes.length; i++) {
/* 533 */       Class localClass = this.parameterTypes[i];
/* 534 */       b = (byte)(b + (byte)typeSizeInStackSlots(localClass));
/* 535 */       if (localLabel2 != null) {
/* 536 */         localLabel2.bind();
/* 537 */         localLabel2 = null;
/*     */       }
/*     */ 
/* 542 */       if (this.isConstructor)
/* 543 */         localClassFileAssembler.opc_aload_1();
/*     */       else {
/* 545 */         localClassFileAssembler.opc_aload_2();
/*     */       }
/* 547 */       localClassFileAssembler.opc_sipush((short)i);
/* 548 */       localClassFileAssembler.opc_aaload();
/* 549 */       if (isPrimitive(localClass))
/*     */       {
/* 553 */         if (this.isConstructor)
/* 554 */           localClassFileAssembler.opc_astore_2();
/*     */         else {
/* 556 */           localClassFileAssembler.opc_astore_3();
/*     */         }
/*     */ 
/* 576 */         Label localLabel3 = null;
/* 577 */         localLabel2 = new Label();
/*     */ 
/* 579 */         for (j = 0; j < primitiveTypes.length; j++) {
/* 580 */           localObject = primitiveTypes[j];
/* 581 */           if (canWidenTo((Class)localObject, localClass)) {
/* 582 */             if (localLabel3 != null) {
/* 583 */               localLabel3.bind();
/*     */             }
/*     */ 
/* 586 */             if (this.isConstructor)
/* 587 */               localClassFileAssembler.opc_aload_2();
/*     */             else {
/* 589 */               localClassFileAssembler.opc_aload_3();
/*     */             }
/* 591 */             localClassFileAssembler.opc_instanceof(indexForPrimitiveType((Class)localObject));
/* 592 */             localLabel3 = new Label();
/* 593 */             localClassFileAssembler.opc_ifeq(localLabel3);
/* 594 */             if (this.isConstructor)
/* 595 */               localClassFileAssembler.opc_aload_2();
/*     */             else {
/* 597 */               localClassFileAssembler.opc_aload_3();
/*     */             }
/* 599 */             localClassFileAssembler.opc_checkcast(indexForPrimitiveType((Class)localObject));
/* 600 */             localClassFileAssembler.opc_invokevirtual(unboxingMethodForPrimitiveType((Class)localObject), 0, typeSizeInStackSlots((Class)localObject));
/*     */ 
/* 603 */             emitWideningBytecodeForPrimitiveConversion(localClassFileAssembler, (Class)localObject, localClass);
/*     */ 
/* 606 */             localClassFileAssembler.opc_goto(localLabel2);
/*     */           }
/*     */         }
/*     */ 
/* 610 */         if (localLabel3 == null) {
/* 611 */           throw new InternalError("Must have found at least identity conversion");
/*     */         }
/*     */ 
/* 619 */         localLabel3.bind();
/* 620 */         localClassFileAssembler.opc_new(this.illegalArgumentClass);
/* 621 */         localClassFileAssembler.opc_dup();
/* 622 */         localClassFileAssembler.opc_invokespecial(this.illegalArgumentCtorIdx, 0, 0);
/* 623 */         localClassFileAssembler.opc_athrow();
/*     */       }
/*     */       else {
/* 626 */         localClassFileAssembler.opc_checkcast(s2);
/* 627 */         s2 = add(s2, (short)2);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 632 */     if (localLabel2 != null) {
/* 633 */       localLabel2.bind();
/*     */     }
/*     */ 
/* 636 */     i = localClassFileAssembler.getLength();
/*     */ 
/* 639 */     if (this.isConstructor) {
/* 640 */       localClassFileAssembler.opc_invokespecial(this.targetMethodRef, b, 0);
/*     */     }
/* 642 */     else if (isStatic()) {
/* 643 */       localClassFileAssembler.opc_invokestatic(this.targetMethodRef, b, typeSizeInStackSlots(this.returnType));
/*     */     }
/* 647 */     else if (isInterface()) {
/* 648 */       localClassFileAssembler.opc_invokeinterface(this.targetMethodRef, b, b, typeSizeInStackSlots(this.returnType));
/*     */     }
/*     */     else
/*     */     {
/* 653 */       localClassFileAssembler.opc_invokevirtual(this.targetMethodRef, b, typeSizeInStackSlots(this.returnType));
/*     */     }
/*     */ 
/* 660 */     short s3 = localClassFileAssembler.getLength();
/*     */ 
/* 662 */     if (!this.isConstructor)
/*     */     {
/* 664 */       if (isPrimitive(this.returnType)) {
/* 665 */         localClassFileAssembler.opc_invokespecial(ctorIndexForPrimitiveType(this.returnType), typeSizeInStackSlots(this.returnType), 0);
/*     */       }
/* 668 */       else if (this.returnType == Void.TYPE) {
/* 669 */         localClassFileAssembler.opc_aconst_null();
/*     */       }
/*     */     }
/* 672 */     localClassFileAssembler.opc_areturn();
/*     */ 
/* 680 */     short s4 = localClassFileAssembler.getLength();
/*     */ 
/* 683 */     localClassFileAssembler.setStack(1);
/* 684 */     localClassFileAssembler.opc_invokespecial(this.toStringIdx, 0, 1);
/* 685 */     localClassFileAssembler.opc_new(this.illegalArgumentClass);
/* 686 */     localClassFileAssembler.opc_dup_x1();
/* 687 */     localClassFileAssembler.opc_swap();
/* 688 */     localClassFileAssembler.opc_invokespecial(this.illegalArgumentStringCtorIdx, 1, 0);
/* 689 */     localClassFileAssembler.opc_athrow();
/*     */ 
/* 691 */     int j = localClassFileAssembler.getLength();
/*     */ 
/* 694 */     localClassFileAssembler.setStack(1);
/* 695 */     localClassFileAssembler.opc_new(this.invocationTargetClass);
/* 696 */     localClassFileAssembler.opc_dup_x1();
/* 697 */     localClassFileAssembler.opc_swap();
/* 698 */     localClassFileAssembler.opc_invokespecial(this.invocationTargetCtorIdx, 1, 0);
/* 699 */     localClassFileAssembler.opc_athrow();
/*     */ 
/* 705 */     Object localObject = new ClassFileAssembler();
/*     */ 
/* 707 */     ((ClassFileAssembler)localObject).emitShort(s1);
/* 708 */     ((ClassFileAssembler)localObject).emitShort(i);
/* 709 */     ((ClassFileAssembler)localObject).emitShort(s4);
/* 710 */     ((ClassFileAssembler)localObject).emitShort(this.classCastClass);
/*     */ 
/* 712 */     ((ClassFileAssembler)localObject).emitShort(s1);
/* 713 */     ((ClassFileAssembler)localObject).emitShort(i);
/* 714 */     ((ClassFileAssembler)localObject).emitShort(s4);
/* 715 */     ((ClassFileAssembler)localObject).emitShort(this.nullPointerClass);
/*     */ 
/* 717 */     ((ClassFileAssembler)localObject).emitShort(i);
/* 718 */     ((ClassFileAssembler)localObject).emitShort(s3);
/* 719 */     ((ClassFileAssembler)localObject).emitShort(j);
/* 720 */     ((ClassFileAssembler)localObject).emitShort(this.throwableClass);
/*     */ 
/* 722 */     emitMethod(this.invokeIdx, localClassFileAssembler.getMaxLocals(), localClassFileAssembler, (ClassFileAssembler)localObject, new short[] { this.invocationTargetClass });
/*     */   }
/*     */ 
/*     */   private boolean usesPrimitiveTypes()
/*     */   {
/* 730 */     if (this.returnType.isPrimitive()) {
/* 731 */       return true;
/*     */     }
/* 733 */     for (int i = 0; i < this.parameterTypes.length; i++) {
/* 734 */       if (this.parameterTypes[i].isPrimitive()) {
/* 735 */         return true;
/*     */       }
/*     */     }
/* 738 */     return false;
/*     */   }
/*     */ 
/*     */   private int numNonPrimitiveParameterTypes() {
/* 742 */     int i = 0;
/* 743 */     for (int j = 0; j < this.parameterTypes.length; j++) {
/* 744 */       if (!this.parameterTypes[j].isPrimitive()) {
/* 745 */         i++;
/*     */       }
/*     */     }
/* 748 */     return i;
/*     */   }
/*     */ 
/*     */   private boolean isInterface() {
/* 752 */     return this.declaringClass.isInterface();
/*     */   }
/*     */ 
/*     */   private String buildInternalSignature() {
/* 756 */     StringBuffer localStringBuffer = new StringBuffer();
/* 757 */     localStringBuffer.append("(");
/* 758 */     for (int i = 0; i < this.parameterTypes.length; i++) {
/* 759 */       localStringBuffer.append(getClassName(this.parameterTypes[i], true));
/*     */     }
/* 761 */     localStringBuffer.append(")");
/* 762 */     localStringBuffer.append(getClassName(this.returnType, true));
/* 763 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static synchronized String generateName(boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 769 */     if (paramBoolean1) {
/* 770 */       if (paramBoolean2) {
/* 771 */         i = ++serializationConstructorSymnum;
/* 772 */         return "sun/reflect/GeneratedSerializationConstructorAccessor" + i;
/*     */       }
/* 774 */       i = ++constructorSymnum;
/* 775 */       return "sun/reflect/GeneratedConstructorAccessor" + i;
/*     */     }
/*     */ 
/* 778 */     int i = ++methodSymnum;
/* 779 */     return "sun/reflect/GeneratedMethodAccessor" + i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.MethodAccessorGenerator
 * JD-Core Version:    0.6.2
 */