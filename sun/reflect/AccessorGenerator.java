/*     */ package sun.reflect;
/*     */ 
/*     */ import java.lang.reflect.Modifier;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class AccessorGenerator
/*     */   implements ClassFileConstants
/*     */ {
/*  34 */   static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */   protected static final short S0 = 0;
/*     */   protected static final short S1 = 1;
/*     */   protected static final short S2 = 2;
/*     */   protected static final short S3 = 3;
/*     */   protected static final short S4 = 4;
/*     */   protected static final short S5 = 5;
/*     */   protected static final short S6 = 6;
/*     */   protected ClassFileAssembler asm;
/*     */   protected int modifiers;
/*     */   protected short thisClass;
/*     */   protected short superClass;
/*     */   protected short targetClass;
/*     */   protected short throwableClass;
/*     */   protected short classCastClass;
/*     */   protected short nullPointerClass;
/*     */   protected short illegalArgumentClass;
/*     */   protected short invocationTargetClass;
/*     */   protected short initIdx;
/*     */   protected short initNameAndTypeIdx;
/*     */   protected short initStringNameAndTypeIdx;
/*     */   protected short nullPointerCtorIdx;
/*     */   protected short illegalArgumentCtorIdx;
/*     */   protected short illegalArgumentStringCtorIdx;
/*     */   protected short invocationTargetCtorIdx;
/*     */   protected short superCtorIdx;
/*     */   protected short objectClass;
/*     */   protected short toStringIdx;
/*     */   protected short codeIdx;
/*     */   protected short exceptionsIdx;
/*     */   protected short booleanIdx;
/*     */   protected short booleanCtorIdx;
/*     */   protected short booleanUnboxIdx;
/*     */   protected short byteIdx;
/*     */   protected short byteCtorIdx;
/*     */   protected short byteUnboxIdx;
/*     */   protected short characterIdx;
/*     */   protected short characterCtorIdx;
/*     */   protected short characterUnboxIdx;
/*     */   protected short doubleIdx;
/*     */   protected short doubleCtorIdx;
/*     */   protected short doubleUnboxIdx;
/*     */   protected short floatIdx;
/*     */   protected short floatCtorIdx;
/*     */   protected short floatUnboxIdx;
/*     */   protected short integerIdx;
/*     */   protected short integerCtorIdx;
/*     */   protected short integerUnboxIdx;
/*     */   protected short longIdx;
/*     */   protected short longCtorIdx;
/*     */   protected short longUnboxIdx;
/*     */   protected short shortIdx;
/*     */   protected short shortCtorIdx;
/*     */   protected short shortUnboxIdx;
/*  97 */   protected final short NUM_COMMON_CPOOL_ENTRIES = 30;
/*  98 */   protected final short NUM_BOXING_CPOOL_ENTRIES = 72;
/*     */ 
/* 674 */   protected static final Class[] primitiveTypes = { Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE };
/*     */   private ClassFileAssembler illegalArgumentCodeBuffer;
/*     */ 
/*     */   protected void emitCommonConstantPoolEntries()
/*     */   {
/* 132 */     this.asm.emitConstantPoolUTF8("java/lang/Throwable");
/* 133 */     this.asm.emitConstantPoolClass(this.asm.cpi());
/* 134 */     this.throwableClass = this.asm.cpi();
/* 135 */     this.asm.emitConstantPoolUTF8("java/lang/ClassCastException");
/* 136 */     this.asm.emitConstantPoolClass(this.asm.cpi());
/* 137 */     this.classCastClass = this.asm.cpi();
/* 138 */     this.asm.emitConstantPoolUTF8("java/lang/NullPointerException");
/* 139 */     this.asm.emitConstantPoolClass(this.asm.cpi());
/* 140 */     this.nullPointerClass = this.asm.cpi();
/* 141 */     this.asm.emitConstantPoolUTF8("java/lang/IllegalArgumentException");
/* 142 */     this.asm.emitConstantPoolClass(this.asm.cpi());
/* 143 */     this.illegalArgumentClass = this.asm.cpi();
/* 144 */     this.asm.emitConstantPoolUTF8("java/lang/reflect/InvocationTargetException");
/* 145 */     this.asm.emitConstantPoolClass(this.asm.cpi());
/* 146 */     this.invocationTargetClass = this.asm.cpi();
/* 147 */     this.asm.emitConstantPoolUTF8("<init>");
/* 148 */     this.initIdx = this.asm.cpi();
/* 149 */     this.asm.emitConstantPoolUTF8("()V");
/* 150 */     this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
/* 151 */     this.initNameAndTypeIdx = this.asm.cpi();
/* 152 */     this.asm.emitConstantPoolMethodref(this.nullPointerClass, this.initNameAndTypeIdx);
/* 153 */     this.nullPointerCtorIdx = this.asm.cpi();
/* 154 */     this.asm.emitConstantPoolMethodref(this.illegalArgumentClass, this.initNameAndTypeIdx);
/* 155 */     this.illegalArgumentCtorIdx = this.asm.cpi();
/* 156 */     this.asm.emitConstantPoolUTF8("(Ljava/lang/String;)V");
/* 157 */     this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
/* 158 */     this.initStringNameAndTypeIdx = this.asm.cpi();
/* 159 */     this.asm.emitConstantPoolMethodref(this.illegalArgumentClass, this.initStringNameAndTypeIdx);
/* 160 */     this.illegalArgumentStringCtorIdx = this.asm.cpi();
/* 161 */     this.asm.emitConstantPoolUTF8("(Ljava/lang/Throwable;)V");
/* 162 */     this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
/* 163 */     this.asm.emitConstantPoolMethodref(this.invocationTargetClass, this.asm.cpi());
/* 164 */     this.invocationTargetCtorIdx = this.asm.cpi();
/* 165 */     this.asm.emitConstantPoolMethodref(this.superClass, this.initNameAndTypeIdx);
/* 166 */     this.superCtorIdx = this.asm.cpi();
/* 167 */     this.asm.emitConstantPoolUTF8("java/lang/Object");
/* 168 */     this.asm.emitConstantPoolClass(this.asm.cpi());
/* 169 */     this.objectClass = this.asm.cpi();
/* 170 */     this.asm.emitConstantPoolUTF8("toString");
/* 171 */     this.asm.emitConstantPoolUTF8("()Ljava/lang/String;");
/* 172 */     this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short)1), this.asm.cpi());
/* 173 */     this.asm.emitConstantPoolMethodref(this.objectClass, this.asm.cpi());
/* 174 */     this.toStringIdx = this.asm.cpi();
/* 175 */     this.asm.emitConstantPoolUTF8("Code");
/* 176 */     this.codeIdx = this.asm.cpi();
/* 177 */     this.asm.emitConstantPoolUTF8("Exceptions");
/* 178 */     this.exceptionsIdx = this.asm.cpi();
/*     */   }
/*     */ 
/*     */   protected void emitBoxingContantPoolEntries()
/*     */   {
/* 257 */     this.asm.emitConstantPoolUTF8("java/lang/Boolean");
/* 258 */     this.asm.emitConstantPoolClass(this.asm.cpi());
/* 259 */     this.booleanIdx = this.asm.cpi();
/* 260 */     this.asm.emitConstantPoolUTF8("(Z)V");
/* 261 */     this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
/* 262 */     this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short)2), this.asm.cpi());
/* 263 */     this.booleanCtorIdx = this.asm.cpi();
/* 264 */     this.asm.emitConstantPoolUTF8("booleanValue");
/* 265 */     this.asm.emitConstantPoolUTF8("()Z");
/* 266 */     this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short)1), this.asm.cpi());
/* 267 */     this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short)6), this.asm.cpi());
/* 268 */     this.booleanUnboxIdx = this.asm.cpi();
/*     */ 
/* 271 */     this.asm.emitConstantPoolUTF8("java/lang/Byte");
/* 272 */     this.asm.emitConstantPoolClass(this.asm.cpi());
/* 273 */     this.byteIdx = this.asm.cpi();
/* 274 */     this.asm.emitConstantPoolUTF8("(B)V");
/* 275 */     this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
/* 276 */     this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short)2), this.asm.cpi());
/* 277 */     this.byteCtorIdx = this.asm.cpi();
/* 278 */     this.asm.emitConstantPoolUTF8("byteValue");
/* 279 */     this.asm.emitConstantPoolUTF8("()B");
/* 280 */     this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short)1), this.asm.cpi());
/* 281 */     this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short)6), this.asm.cpi());
/* 282 */     this.byteUnboxIdx = this.asm.cpi();
/*     */ 
/* 285 */     this.asm.emitConstantPoolUTF8("java/lang/Character");
/* 286 */     this.asm.emitConstantPoolClass(this.asm.cpi());
/* 287 */     this.characterIdx = this.asm.cpi();
/* 288 */     this.asm.emitConstantPoolUTF8("(C)V");
/* 289 */     this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
/* 290 */     this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short)2), this.asm.cpi());
/* 291 */     this.characterCtorIdx = this.asm.cpi();
/* 292 */     this.asm.emitConstantPoolUTF8("charValue");
/* 293 */     this.asm.emitConstantPoolUTF8("()C");
/* 294 */     this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short)1), this.asm.cpi());
/* 295 */     this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short)6), this.asm.cpi());
/* 296 */     this.characterUnboxIdx = this.asm.cpi();
/*     */ 
/* 299 */     this.asm.emitConstantPoolUTF8("java/lang/Double");
/* 300 */     this.asm.emitConstantPoolClass(this.asm.cpi());
/* 301 */     this.doubleIdx = this.asm.cpi();
/* 302 */     this.asm.emitConstantPoolUTF8("(D)V");
/* 303 */     this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
/* 304 */     this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short)2), this.asm.cpi());
/* 305 */     this.doubleCtorIdx = this.asm.cpi();
/* 306 */     this.asm.emitConstantPoolUTF8("doubleValue");
/* 307 */     this.asm.emitConstantPoolUTF8("()D");
/* 308 */     this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short)1), this.asm.cpi());
/* 309 */     this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short)6), this.asm.cpi());
/* 310 */     this.doubleUnboxIdx = this.asm.cpi();
/*     */ 
/* 313 */     this.asm.emitConstantPoolUTF8("java/lang/Float");
/* 314 */     this.asm.emitConstantPoolClass(this.asm.cpi());
/* 315 */     this.floatIdx = this.asm.cpi();
/* 316 */     this.asm.emitConstantPoolUTF8("(F)V");
/* 317 */     this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
/* 318 */     this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short)2), this.asm.cpi());
/* 319 */     this.floatCtorIdx = this.asm.cpi();
/* 320 */     this.asm.emitConstantPoolUTF8("floatValue");
/* 321 */     this.asm.emitConstantPoolUTF8("()F");
/* 322 */     this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short)1), this.asm.cpi());
/* 323 */     this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short)6), this.asm.cpi());
/* 324 */     this.floatUnboxIdx = this.asm.cpi();
/*     */ 
/* 327 */     this.asm.emitConstantPoolUTF8("java/lang/Integer");
/* 328 */     this.asm.emitConstantPoolClass(this.asm.cpi());
/* 329 */     this.integerIdx = this.asm.cpi();
/* 330 */     this.asm.emitConstantPoolUTF8("(I)V");
/* 331 */     this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
/* 332 */     this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short)2), this.asm.cpi());
/* 333 */     this.integerCtorIdx = this.asm.cpi();
/* 334 */     this.asm.emitConstantPoolUTF8("intValue");
/* 335 */     this.asm.emitConstantPoolUTF8("()I");
/* 336 */     this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short)1), this.asm.cpi());
/* 337 */     this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short)6), this.asm.cpi());
/* 338 */     this.integerUnboxIdx = this.asm.cpi();
/*     */ 
/* 341 */     this.asm.emitConstantPoolUTF8("java/lang/Long");
/* 342 */     this.asm.emitConstantPoolClass(this.asm.cpi());
/* 343 */     this.longIdx = this.asm.cpi();
/* 344 */     this.asm.emitConstantPoolUTF8("(J)V");
/* 345 */     this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
/* 346 */     this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short)2), this.asm.cpi());
/* 347 */     this.longCtorIdx = this.asm.cpi();
/* 348 */     this.asm.emitConstantPoolUTF8("longValue");
/* 349 */     this.asm.emitConstantPoolUTF8("()J");
/* 350 */     this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short)1), this.asm.cpi());
/* 351 */     this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short)6), this.asm.cpi());
/* 352 */     this.longUnboxIdx = this.asm.cpi();
/*     */ 
/* 355 */     this.asm.emitConstantPoolUTF8("java/lang/Short");
/* 356 */     this.asm.emitConstantPoolClass(this.asm.cpi());
/* 357 */     this.shortIdx = this.asm.cpi();
/* 358 */     this.asm.emitConstantPoolUTF8("(S)V");
/* 359 */     this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
/* 360 */     this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short)2), this.asm.cpi());
/* 361 */     this.shortCtorIdx = this.asm.cpi();
/* 362 */     this.asm.emitConstantPoolUTF8("shortValue");
/* 363 */     this.asm.emitConstantPoolUTF8("()S");
/* 364 */     this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short)1), this.asm.cpi());
/* 365 */     this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short)6), this.asm.cpi());
/* 366 */     this.shortUnboxIdx = this.asm.cpi();
/*     */   }
/*     */ 
/*     */   protected static short add(short paramShort1, short paramShort2)
/*     */   {
/* 371 */     return (short)(paramShort1 + paramShort2);
/*     */   }
/*     */ 
/*     */   protected static short sub(short paramShort1, short paramShort2) {
/* 375 */     return (short)(paramShort1 - paramShort2);
/*     */   }
/*     */ 
/*     */   protected boolean isStatic() {
/* 379 */     return Modifier.isStatic(this.modifiers);
/*     */   }
/*     */ 
/*     */   protected static String getClassName(Class paramClass, boolean paramBoolean)
/*     */   {
/* 387 */     if (paramClass.isPrimitive()) {
/* 388 */       if (paramClass == Boolean.TYPE)
/* 389 */         return "Z";
/* 390 */       if (paramClass == Byte.TYPE)
/* 391 */         return "B";
/* 392 */       if (paramClass == Character.TYPE)
/* 393 */         return "C";
/* 394 */       if (paramClass == Double.TYPE)
/* 395 */         return "D";
/* 396 */       if (paramClass == Float.TYPE)
/* 397 */         return "F";
/* 398 */       if (paramClass == Integer.TYPE)
/* 399 */         return "I";
/* 400 */       if (paramClass == Long.TYPE)
/* 401 */         return "J";
/* 402 */       if (paramClass == Short.TYPE)
/* 403 */         return "S";
/* 404 */       if (paramClass == Void.TYPE) {
/* 405 */         return "V";
/*     */       }
/* 407 */       throw new InternalError("Should have found primitive type");
/* 408 */     }if (paramClass.isArray()) {
/* 409 */       return "[" + getClassName(paramClass.getComponentType(), true);
/*     */     }
/* 411 */     if (paramBoolean) {
/* 412 */       return internalize("L" + paramClass.getName() + ";");
/*     */     }
/* 414 */     return internalize(paramClass.getName());
/*     */   }
/*     */ 
/*     */   private static String internalize(String paramString)
/*     */   {
/* 420 */     return paramString.replace('.', '/');
/*     */   }
/*     */ 
/*     */   protected void emitConstructor()
/*     */   {
/* 425 */     ClassFileAssembler localClassFileAssembler = new ClassFileAssembler();
/*     */ 
/* 427 */     localClassFileAssembler.setMaxLocals(1);
/* 428 */     localClassFileAssembler.opc_aload_0();
/* 429 */     localClassFileAssembler.opc_invokespecial(this.superCtorIdx, 0, 0);
/* 430 */     localClassFileAssembler.opc_return();
/*     */ 
/* 433 */     emitMethod(this.initIdx, localClassFileAssembler.getMaxLocals(), localClassFileAssembler, null, null);
/*     */   }
/*     */ 
/*     */   protected void emitMethod(short paramShort, int paramInt, ClassFileAssembler paramClassFileAssembler1, ClassFileAssembler paramClassFileAssembler2, short[] paramArrayOfShort)
/*     */   {
/* 449 */     int i = paramClassFileAssembler1.getLength();
/* 450 */     int j = 0;
/* 451 */     if (paramClassFileAssembler2 != null) {
/* 452 */       j = paramClassFileAssembler2.getLength();
/* 453 */       if (j % 8 != 0) {
/* 454 */         throw new IllegalArgumentException("Illegal exception table");
/*     */       }
/*     */     }
/* 457 */     int k = 12 + i + j;
/* 458 */     j /= 8;
/*     */ 
/* 460 */     this.asm.emitShort((short)1);
/* 461 */     this.asm.emitShort(paramShort);
/* 462 */     this.asm.emitShort(add(paramShort, (short)1));
/* 463 */     if (paramArrayOfShort == null)
/*     */     {
/* 465 */       this.asm.emitShort((short)1);
/*     */     }
/*     */     else {
/* 468 */       this.asm.emitShort((short)2);
/*     */     }
/*     */ 
/* 471 */     this.asm.emitShort(this.codeIdx);
/* 472 */     this.asm.emitInt(k);
/* 473 */     this.asm.emitShort(paramClassFileAssembler1.getMaxStack());
/* 474 */     this.asm.emitShort((short)Math.max(paramInt, paramClassFileAssembler1.getMaxLocals()));
/* 475 */     this.asm.emitInt(i);
/* 476 */     this.asm.append(paramClassFileAssembler1);
/* 477 */     this.asm.emitShort((short)j);
/* 478 */     if (paramClassFileAssembler2 != null) {
/* 479 */       this.asm.append(paramClassFileAssembler2);
/*     */     }
/* 481 */     this.asm.emitShort((short)0);
/* 482 */     if (paramArrayOfShort != null)
/*     */     {
/* 484 */       this.asm.emitShort(this.exceptionsIdx);
/* 485 */       this.asm.emitInt(2 + 2 * paramArrayOfShort.length);
/* 486 */       this.asm.emitShort((short)paramArrayOfShort.length);
/* 487 */       for (int m = 0; m < paramArrayOfShort.length; m++)
/* 488 */         this.asm.emitShort(paramArrayOfShort[m]);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected short indexForPrimitiveType(Class paramClass)
/*     */   {
/* 494 */     if (paramClass == Boolean.TYPE)
/* 495 */       return this.booleanIdx;
/* 496 */     if (paramClass == Byte.TYPE)
/* 497 */       return this.byteIdx;
/* 498 */     if (paramClass == Character.TYPE)
/* 499 */       return this.characterIdx;
/* 500 */     if (paramClass == Double.TYPE)
/* 501 */       return this.doubleIdx;
/* 502 */     if (paramClass == Float.TYPE)
/* 503 */       return this.floatIdx;
/* 504 */     if (paramClass == Integer.TYPE)
/* 505 */       return this.integerIdx;
/* 506 */     if (paramClass == Long.TYPE)
/* 507 */       return this.longIdx;
/* 508 */     if (paramClass == Short.TYPE) {
/* 509 */       return this.shortIdx;
/*     */     }
/* 511 */     throw new InternalError("Should have found primitive type");
/*     */   }
/*     */ 
/*     */   protected short ctorIndexForPrimitiveType(Class paramClass) {
/* 515 */     if (paramClass == Boolean.TYPE)
/* 516 */       return this.booleanCtorIdx;
/* 517 */     if (paramClass == Byte.TYPE)
/* 518 */       return this.byteCtorIdx;
/* 519 */     if (paramClass == Character.TYPE)
/* 520 */       return this.characterCtorIdx;
/* 521 */     if (paramClass == Double.TYPE)
/* 522 */       return this.doubleCtorIdx;
/* 523 */     if (paramClass == Float.TYPE)
/* 524 */       return this.floatCtorIdx;
/* 525 */     if (paramClass == Integer.TYPE)
/* 526 */       return this.integerCtorIdx;
/* 527 */     if (paramClass == Long.TYPE)
/* 528 */       return this.longCtorIdx;
/* 529 */     if (paramClass == Short.TYPE) {
/* 530 */       return this.shortCtorIdx;
/*     */     }
/* 532 */     throw new InternalError("Should have found primitive type");
/*     */   }
/*     */ 
/*     */   protected static boolean canWidenTo(Class paramClass1, Class paramClass2)
/*     */   {
/* 538 */     if (!paramClass1.isPrimitive()) {
/* 539 */       return false;
/*     */     }
/*     */ 
/* 550 */     if (paramClass1 == Boolean.TYPE) {
/* 551 */       if (paramClass2 == Boolean.TYPE)
/* 552 */         return true;
/*     */     }
/* 554 */     else if (paramClass1 == Byte.TYPE) {
/* 555 */       if ((paramClass2 == Byte.TYPE) || (paramClass2 == Short.TYPE) || (paramClass2 == Integer.TYPE) || (paramClass2 == Long.TYPE) || (paramClass2 == Float.TYPE) || (paramClass2 == Double.TYPE))
/*     */       {
/* 561 */         return true;
/*     */       }
/* 563 */     } else if (paramClass1 == Short.TYPE) {
/* 564 */       if ((paramClass2 == Short.TYPE) || (paramClass2 == Integer.TYPE) || (paramClass2 == Long.TYPE) || (paramClass2 == Float.TYPE) || (paramClass2 == Double.TYPE))
/*     */       {
/* 569 */         return true;
/*     */       }
/* 571 */     } else if (paramClass1 == Character.TYPE) {
/* 572 */       if ((paramClass2 == Character.TYPE) || (paramClass2 == Integer.TYPE) || (paramClass2 == Long.TYPE) || (paramClass2 == Float.TYPE) || (paramClass2 == Double.TYPE))
/*     */       {
/* 577 */         return true;
/*     */       }
/* 579 */     } else if (paramClass1 == Integer.TYPE) {
/* 580 */       if ((paramClass2 == Integer.TYPE) || (paramClass2 == Long.TYPE) || (paramClass2 == Float.TYPE) || (paramClass2 == Double.TYPE))
/*     */       {
/* 584 */         return true;
/*     */       }
/* 586 */     } else if (paramClass1 == Long.TYPE) {
/* 587 */       if ((paramClass2 == Long.TYPE) || (paramClass2 == Float.TYPE) || (paramClass2 == Double.TYPE))
/*     */       {
/* 590 */         return true;
/*     */       }
/* 592 */     } else if (paramClass1 == Float.TYPE) {
/* 593 */       if ((paramClass2 == Float.TYPE) || (paramClass2 == Double.TYPE))
/*     */       {
/* 595 */         return true;
/*     */       }
/* 597 */     } else if ((paramClass1 == Double.TYPE) && 
/* 598 */       (paramClass2 == Double.TYPE)) {
/* 599 */       return true;
/*     */     }
/*     */ 
/* 603 */     return false;
/*     */   }
/*     */ 
/*     */   protected static void emitWideningBytecodeForPrimitiveConversion(ClassFileAssembler paramClassFileAssembler, Class paramClass1, Class paramClass2)
/*     */   {
/* 627 */     if ((paramClass1 == Byte.TYPE) || (paramClass1 == Short.TYPE) || (paramClass1 == Character.TYPE) || (paramClass1 == Integer.TYPE))
/*     */     {
/* 631 */       if (paramClass2 == Long.TYPE)
/* 632 */         paramClassFileAssembler.opc_i2l();
/* 633 */       else if (paramClass2 == Float.TYPE)
/* 634 */         paramClassFileAssembler.opc_i2f();
/* 635 */       else if (paramClass2 == Double.TYPE)
/* 636 */         paramClassFileAssembler.opc_i2d();
/*     */     }
/* 638 */     else if (paramClass1 == Long.TYPE) {
/* 639 */       if (paramClass2 == Float.TYPE)
/* 640 */         paramClassFileAssembler.opc_l2f();
/* 641 */       else if (paramClass2 == Double.TYPE)
/* 642 */         paramClassFileAssembler.opc_l2d();
/*     */     }
/* 644 */     else if ((paramClass1 == Float.TYPE) && 
/* 645 */       (paramClass2 == Double.TYPE))
/* 646 */       paramClassFileAssembler.opc_f2d();
/*     */   }
/*     */ 
/*     */   protected short unboxingMethodForPrimitiveType(Class paramClass)
/*     */   {
/* 654 */     if (paramClass == Boolean.TYPE)
/* 655 */       return this.booleanUnboxIdx;
/* 656 */     if (paramClass == Byte.TYPE)
/* 657 */       return this.byteUnboxIdx;
/* 658 */     if (paramClass == Character.TYPE)
/* 659 */       return this.characterUnboxIdx;
/* 660 */     if (paramClass == Short.TYPE)
/* 661 */       return this.shortUnboxIdx;
/* 662 */     if (paramClass == Integer.TYPE)
/* 663 */       return this.integerUnboxIdx;
/* 664 */     if (paramClass == Long.TYPE)
/* 665 */       return this.longUnboxIdx;
/* 666 */     if (paramClass == Float.TYPE)
/* 667 */       return this.floatUnboxIdx;
/* 668 */     if (paramClass == Double.TYPE) {
/* 669 */       return this.doubleUnboxIdx;
/*     */     }
/* 671 */     throw new InternalError("Illegal primitive type " + paramClass.getName());
/*     */   }
/*     */ 
/*     */   protected static boolean isPrimitive(Class paramClass)
/*     */   {
/* 687 */     return (paramClass.isPrimitive()) && (paramClass != Void.TYPE);
/*     */   }
/*     */ 
/*     */   protected int typeSizeInStackSlots(Class paramClass) {
/* 691 */     if (paramClass == Void.TYPE) {
/* 692 */       return 0;
/*     */     }
/* 694 */     if ((paramClass == Long.TYPE) || (paramClass == Double.TYPE)) {
/* 695 */       return 2;
/*     */     }
/* 697 */     return 1;
/*     */   }
/*     */ 
/*     */   protected ClassFileAssembler illegalArgumentCodeBuffer()
/*     */   {
/* 702 */     if (this.illegalArgumentCodeBuffer == null) {
/* 703 */       this.illegalArgumentCodeBuffer = new ClassFileAssembler();
/* 704 */       this.illegalArgumentCodeBuffer.opc_new(this.illegalArgumentClass);
/* 705 */       this.illegalArgumentCodeBuffer.opc_dup();
/* 706 */       this.illegalArgumentCodeBuffer.opc_invokespecial(this.illegalArgumentCtorIdx, 0, 0);
/* 707 */       this.illegalArgumentCodeBuffer.opc_athrow();
/*     */     }
/*     */ 
/* 710 */     return this.illegalArgumentCodeBuffer;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.AccessorGenerator
 * JD-Core Version:    0.6.2
 */