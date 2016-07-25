/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class NativeJavaObject
/*     */   implements Scriptable, Wrapper
/*     */ {
/*     */   private static final int JSTYPE_UNDEFINED = 0;
/*     */   private static final int JSTYPE_NULL = 1;
/*     */   private static final int JSTYPE_BOOLEAN = 2;
/*     */   private static final int JSTYPE_NUMBER = 3;
/*     */   private static final int JSTYPE_STRING = 4;
/*     */   private static final int JSTYPE_JAVA_CLASS = 5;
/*     */   private static final int JSTYPE_JAVA_OBJECT = 6;
/*     */   private static final int JSTYPE_JAVA_ARRAY = 7;
/*     */   private static final int JSTYPE_OBJECT = 8;
/*     */   static final byte CONVERSION_TRIVIAL = 1;
/*     */   static final byte CONVERSION_NONTRIVIAL = 0;
/*     */   static final byte CONVERSION_NONE = 99;
/*     */   protected Scriptable prototype;
/*     */   protected Scriptable parent;
/*     */   protected transient Object javaObject;
/*     */   protected transient Class<?> staticType;
/*     */   protected transient JavaMembers members;
/*     */   private transient Map<String, FieldAndMethods> fieldAndMethods;
/*     */   private transient boolean isAdapter;
/* 917 */   private static final Object COERCED_INTERFACE_KEY = "Coerced Interface";
/*     */   private static Method adapter_writeAdapterObject;
/*     */   private static Method adapter_readAdapterObject;
/*     */ 
/*     */   public NativeJavaObject()
/*     */   {
/*     */   }
/*     */ 
/*     */   public NativeJavaObject(Scriptable paramScriptable, Object paramObject, Class<?> paramClass)
/*     */   {
/*  68 */     this(paramScriptable, paramObject, paramClass, false);
/*     */   }
/*     */ 
/*     */   public NativeJavaObject(Scriptable paramScriptable, Object paramObject, Class<?> paramClass, boolean paramBoolean)
/*     */   {
/*  74 */     this.parent = paramScriptable;
/*  75 */     this.javaObject = paramObject;
/*  76 */     this.staticType = paramClass;
/*  77 */     this.isAdapter = paramBoolean;
/*  78 */     initMembers();
/*     */   }
/*     */ 
/*     */   protected void initMembers()
/*     */   {
/*     */     Class localClass;
/*  83 */     if (this.javaObject != null)
/*  84 */       localClass = this.javaObject.getClass();
/*     */     else {
/*  86 */       localClass = this.staticType;
/*     */     }
/*  88 */     this.members = JavaMembers.lookupClass(this.parent, localClass, this.staticType, this.isAdapter);
/*     */ 
/*  90 */     this.fieldAndMethods = this.members.getFieldAndMethodsObjects(this, this.javaObject, false);
/*     */   }
/*     */ 
/*     */   public boolean has(String paramString, Scriptable paramScriptable)
/*     */   {
/*  95 */     return this.members.has(paramString, false);
/*     */   }
/*     */ 
/*     */   public boolean has(int paramInt, Scriptable paramScriptable) {
/*  99 */     return false;
/*     */   }
/*     */ 
/*     */   public Object get(String paramString, Scriptable paramScriptable) {
/* 103 */     if (this.fieldAndMethods != null) {
/* 104 */       Object localObject = this.fieldAndMethods.get(paramString);
/* 105 */       if (localObject != null) {
/* 106 */         return localObject;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 111 */     return this.members.get(this, paramString, this.javaObject, false);
/*     */   }
/*     */ 
/*     */   public Object get(int paramInt, Scriptable paramScriptable) {
/* 115 */     throw this.members.reportMemberNotFound(Integer.toString(paramInt));
/*     */   }
/*     */ 
/*     */   public void put(String paramString, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 122 */     if ((this.prototype == null) || (this.members.has(paramString, false)))
/* 123 */       this.members.put(this, paramString, this.javaObject, paramObject, false);
/*     */     else
/* 125 */       this.prototype.put(paramString, this.prototype, paramObject);
/*     */   }
/*     */ 
/*     */   public void put(int paramInt, Scriptable paramScriptable, Object paramObject) {
/* 129 */     throw this.members.reportMemberNotFound(Integer.toString(paramInt));
/*     */   }
/*     */ 
/*     */   public boolean hasInstance(Scriptable paramScriptable)
/*     */   {
/* 134 */     return false;
/*     */   }
/*     */ 
/*     */   public void delete(String paramString) {
/*     */   }
/*     */ 
/*     */   public void delete(int paramInt) {
/*     */   }
/*     */ 
/*     */   public Scriptable getPrototype() {
/* 144 */     if ((this.prototype == null) && ((this.javaObject instanceof String))) {
/* 145 */       return ScriptableObject.getClassPrototype(this.parent, "String");
/*     */     }
/* 147 */     return this.prototype;
/*     */   }
/*     */ 
/*     */   public void setPrototype(Scriptable paramScriptable)
/*     */   {
/* 154 */     this.prototype = paramScriptable;
/*     */   }
/*     */ 
/*     */   public Scriptable getParentScope()
/*     */   {
/* 161 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public void setParentScope(Scriptable paramScriptable)
/*     */   {
/* 168 */     this.parent = paramScriptable;
/*     */   }
/*     */ 
/*     */   public Object[] getIds() {
/* 172 */     return this.members.getIds(false);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public static Object wrap(Scriptable paramScriptable, Object paramObject, Class<?> paramClass)
/*     */   {
/* 181 */     Context localContext = Context.getContext();
/* 182 */     return localContext.getWrapFactory().wrap(localContext, paramScriptable, paramObject, paramClass);
/*     */   }
/*     */ 
/*     */   public Object unwrap() {
/* 186 */     return this.javaObject;
/*     */   }
/*     */ 
/*     */   public String getClassName() {
/* 190 */     return "JavaObject";
/*     */   }
/*     */ 
/*     */   public Object getDefaultValue(Class<?> paramClass)
/*     */   {
/* 196 */     if ((paramClass == null) && 
/* 197 */       ((this.javaObject instanceof Boolean)))
/* 198 */       paramClass = ScriptRuntime.BooleanClass;
/*     */     Object localObject1;
/* 201 */     if ((paramClass == null) || (paramClass == ScriptRuntime.StringClass)) {
/* 202 */       localObject1 = this.javaObject.toString();
/*     */     }
/*     */     else
/*     */     {
/*     */       String str;
/* 205 */       if (paramClass == ScriptRuntime.BooleanClass)
/* 206 */         str = "booleanValue";
/* 207 */       else if (paramClass == ScriptRuntime.NumberClass)
/* 208 */         str = "doubleValue";
/*     */       else {
/* 210 */         throw Context.reportRuntimeError0("msg.default.value");
/*     */       }
/* 212 */       Object localObject2 = get(str, this);
/* 213 */       if ((localObject2 instanceof Function)) {
/* 214 */         Function localFunction = (Function)localObject2;
/* 215 */         localObject1 = localFunction.call(Context.getContext(), localFunction.getParentScope(), this, ScriptRuntime.emptyArgs);
/*     */       }
/* 218 */       else if ((paramClass == ScriptRuntime.NumberClass) && ((this.javaObject instanceof Boolean)))
/*     */       {
/* 221 */         boolean bool = ((Boolean)this.javaObject).booleanValue();
/* 222 */         localObject1 = ScriptRuntime.wrapNumber(bool ? 1.0D : 0.0D);
/*     */       } else {
/* 224 */         localObject1 = this.javaObject.toString();
/*     */       }
/*     */     }
/*     */ 
/* 228 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public static boolean canConvert(Object paramObject, Class<?> paramClass)
/*     */   {
/* 237 */     int i = getConversionWeight(paramObject, paramClass);
/*     */ 
/* 239 */     return i < 99;
/*     */   }
/*     */ 
/*     */   static int getConversionWeight(Object paramObject, Class<?> paramClass)
/*     */   {
/* 266 */     int i = getJSTypeCode(paramObject);
/*     */ 
/* 268 */     switch (i)
/*     */     {
/*     */     case 0:
/* 271 */       if ((paramClass == ScriptRuntime.StringClass) || (paramClass == ScriptRuntime.ObjectClass))
/*     */       {
/* 273 */         return 1;
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 1:
/* 278 */       if (!paramClass.isPrimitive()) {
/* 279 */         return 1;
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 2:
/* 285 */       if (paramClass == Boolean.TYPE) {
/* 286 */         return 1;
/*     */       }
/* 288 */       if (paramClass == ScriptRuntime.BooleanClass) {
/* 289 */         return 2;
/*     */       }
/* 291 */       if (paramClass == ScriptRuntime.ObjectClass) {
/* 292 */         return 3;
/*     */       }
/* 294 */       if (paramClass == ScriptRuntime.StringClass) {
/* 295 */         return 4;
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 3:
/* 300 */       if (paramClass.isPrimitive()) {
/* 301 */         if (paramClass == Double.TYPE) {
/* 302 */           return 1;
/*     */         }
/* 304 */         if (paramClass != Boolean.TYPE)
/* 305 */           return 1 + getSizeRank(paramClass);
/*     */       }
/*     */       else
/*     */       {
/* 309 */         if (paramClass == ScriptRuntime.StringClass)
/*     */         {
/* 311 */           return 9;
/*     */         }
/* 313 */         if (paramClass == ScriptRuntime.ObjectClass) {
/* 314 */           return 10;
/*     */         }
/* 316 */         if (ScriptRuntime.NumberClass.isAssignableFrom(paramClass))
/*     */         {
/* 318 */           return 2;
/*     */         }
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 4:
/* 324 */       if (paramClass == ScriptRuntime.StringClass) {
/* 325 */         return 1;
/*     */       }
/* 327 */       if (paramClass.isInstance(paramObject)) {
/* 328 */         return 2;
/*     */       }
/* 330 */       if (paramClass.isPrimitive()) {
/* 331 */         if (paramClass == Character.TYPE)
/* 332 */           return 3;
/* 333 */         if (paramClass != Boolean.TYPE) {
/* 334 */           return 4;
/*     */         }
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 5:
/* 340 */       if (paramClass == ScriptRuntime.ClassClass) {
/* 341 */         return 1;
/*     */       }
/* 343 */       if (paramClass == ScriptRuntime.ObjectClass) {
/* 344 */         return 3;
/*     */       }
/* 346 */       if (paramClass == ScriptRuntime.StringClass) {
/* 347 */         return 4;
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 6:
/*     */     case 7:
/* 353 */       Object localObject = paramObject;
/* 354 */       if ((localObject instanceof Wrapper)) {
/* 355 */         localObject = ((Wrapper)localObject).unwrap();
/*     */       }
/* 357 */       if (paramClass.isInstance(localObject)) {
/* 358 */         return 0;
/*     */       }
/* 360 */       if (paramClass == ScriptRuntime.StringClass) {
/* 361 */         return 2;
/*     */       }
/* 363 */       if ((paramClass.isPrimitive()) && (paramClass != Boolean.TYPE)) {
/* 364 */         return i == 7 ? 99 : 2 + getSizeRank(paramClass);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 8:
/* 371 */       if ((paramClass != ScriptRuntime.ObjectClass) && (paramClass.isInstance(paramObject)))
/*     */       {
/* 373 */         return 1;
/*     */       }
/* 375 */       if (paramClass.isArray()) {
/* 376 */         if ((paramObject instanceof NativeArray))
/*     */         {
/* 380 */           return 1;
/*     */         }
/*     */       } else {
/* 383 */         if (paramClass == ScriptRuntime.ObjectClass) {
/* 384 */           return 2;
/*     */         }
/* 386 */         if (paramClass == ScriptRuntime.StringClass) {
/* 387 */           return 3;
/*     */         }
/* 389 */         if (paramClass == ScriptRuntime.DateClass) {
/* 390 */           if ((paramObject instanceof NativeDate))
/*     */           {
/* 392 */             return 1;
/*     */           }
/*     */         } else {
/* 395 */           if (paramClass.isInterface()) {
/* 396 */             if ((paramObject instanceof Function))
/*     */             {
/* 398 */               if (paramClass.getMethods().length == 1) {
/* 399 */                 return 1;
/*     */               }
/*     */             }
/* 402 */             return 11;
/*     */           }
/* 404 */           if ((paramClass.isPrimitive()) && (paramClass != Boolean.TYPE))
/* 405 */             return 3 + getSizeRank(paramClass);
/*     */         }
/*     */       }
/*     */       break;
/*     */     }
/* 410 */     return 99;
/*     */   }
/*     */ 
/*     */   static int getSizeRank(Class<?> paramClass) {
/* 414 */     if (paramClass == Double.TYPE) {
/* 415 */       return 1;
/*     */     }
/* 417 */     if (paramClass == Float.TYPE) {
/* 418 */       return 2;
/*     */     }
/* 420 */     if (paramClass == Long.TYPE) {
/* 421 */       return 3;
/*     */     }
/* 423 */     if (paramClass == Integer.TYPE) {
/* 424 */       return 4;
/*     */     }
/* 426 */     if (paramClass == Short.TYPE) {
/* 427 */       return 5;
/*     */     }
/* 429 */     if (paramClass == Character.TYPE) {
/* 430 */       return 6;
/*     */     }
/* 432 */     if (paramClass == Byte.TYPE) {
/* 433 */       return 7;
/*     */     }
/* 435 */     if (paramClass == Boolean.TYPE) {
/* 436 */       return 99;
/*     */     }
/*     */ 
/* 439 */     return 8;
/*     */   }
/*     */ 
/*     */   private static int getJSTypeCode(Object paramObject)
/*     */   {
/* 444 */     if (paramObject == null) {
/* 445 */       return 1;
/*     */     }
/* 447 */     if (paramObject == Undefined.instance) {
/* 448 */       return 0;
/*     */     }
/* 450 */     if ((paramObject instanceof String)) {
/* 451 */       return 4;
/*     */     }
/* 453 */     if ((paramObject instanceof Number)) {
/* 454 */       return 3;
/*     */     }
/* 456 */     if ((paramObject instanceof Boolean)) {
/* 457 */       return 2;
/*     */     }
/* 459 */     if ((paramObject instanceof Wrapper)) {
/* 460 */       if ((paramObject instanceof NativeJavaClass)) {
/* 461 */         return 5;
/*     */       }
/* 463 */       if ((paramObject instanceof NativeJavaArray)) {
/* 464 */         return 7;
/*     */       }
/* 466 */       if ((paramObject instanceof Wrapper)) {
/* 467 */         return 6;
/*     */       }
/*     */ 
/* 470 */       return 8;
/*     */     }
/*     */ 
/* 473 */     if ((paramObject instanceof Class)) {
/* 474 */       return 5;
/*     */     }
/*     */ 
/* 477 */     Class localClass = paramObject.getClass();
/* 478 */     if (localClass.isArray()) {
/* 479 */       return 7;
/*     */     }
/*     */ 
/* 482 */     return 6;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public static Object coerceType(Class<?> paramClass, Object paramObject)
/*     */   {
/* 495 */     return coerceTypeImpl(paramClass, paramObject);
/*     */   }
/*     */ 
/*     */   static Object coerceTypeImpl(Class<?> paramClass, Object paramObject)
/*     */   {
/* 504 */     if ((paramObject != null) && (paramObject.getClass() == paramClass)) {
/* 505 */       return paramObject;
/*     */     }
/*     */ 
/* 508 */     switch (getJSTypeCode(paramObject))
/*     */     {
/*     */     case 1:
/* 512 */       if (paramClass.isPrimitive()) {
/* 513 */         reportConversionError(paramObject, paramClass);
/*     */       }
/* 515 */       return null;
/*     */     case 0:
/* 518 */       if ((paramClass == ScriptRuntime.StringClass) || (paramClass == ScriptRuntime.ObjectClass))
/*     */       {
/* 520 */         return "undefined";
/*     */       }
/*     */ 
/* 523 */       reportConversionError("undefined", paramClass);
/*     */ 
/* 525 */       break;
/*     */     case 2:
/* 529 */       if ((paramClass == Boolean.TYPE) || (paramClass == ScriptRuntime.BooleanClass) || (paramClass == ScriptRuntime.ObjectClass))
/*     */       {
/* 532 */         return paramObject;
/*     */       }
/* 534 */       if (paramClass == ScriptRuntime.StringClass) {
/* 535 */         return paramObject.toString();
/*     */       }
/*     */ 
/* 538 */       reportConversionError(paramObject, paramClass);
/*     */ 
/* 540 */       break;
/*     */     case 3:
/* 543 */       if (paramClass == ScriptRuntime.StringClass) {
/* 544 */         return ScriptRuntime.toString(paramObject);
/*     */       }
/* 546 */       if (paramClass == ScriptRuntime.ObjectClass) {
/* 547 */         return coerceToNumber(Double.TYPE, paramObject);
/*     */       }
/* 549 */       if (((paramClass.isPrimitive()) && (paramClass != Boolean.TYPE)) || (ScriptRuntime.NumberClass.isAssignableFrom(paramClass)))
/*     */       {
/* 551 */         return coerceToNumber(paramClass, paramObject);
/*     */       }
/*     */ 
/* 554 */       reportConversionError(paramObject, paramClass);
/*     */ 
/* 556 */       break;
/*     */     case 4:
/* 559 */       if ((paramClass == ScriptRuntime.StringClass) || (paramClass.isInstance(paramObject))) {
/* 560 */         return paramObject;
/*     */       }
/* 562 */       if ((paramClass == Character.TYPE) || (paramClass == ScriptRuntime.CharacterClass))
/*     */       {
/* 569 */         if (((String)paramObject).length() == 1) {
/* 570 */           return Character.valueOf(((String)paramObject).charAt(0));
/*     */         }
/*     */ 
/* 573 */         return coerceToNumber(paramClass, paramObject);
/*     */       }
/*     */ 
/* 576 */       if (((paramClass.isPrimitive()) && (paramClass != Boolean.TYPE)) || (ScriptRuntime.NumberClass.isAssignableFrom(paramClass)))
/*     */       {
/* 579 */         return coerceToNumber(paramClass, paramObject);
/*     */       }
/*     */ 
/* 582 */       reportConversionError(paramObject, paramClass);
/*     */ 
/* 584 */       break;
/*     */     case 5:
/* 587 */       if ((paramObject instanceof Wrapper)) {
/* 588 */         paramObject = ((Wrapper)paramObject).unwrap();
/*     */       }
/*     */ 
/* 591 */       if ((paramClass == ScriptRuntime.ClassClass) || (paramClass == ScriptRuntime.ObjectClass))
/*     */       {
/* 593 */         return paramObject;
/*     */       }
/* 595 */       if (paramClass == ScriptRuntime.StringClass) {
/* 596 */         return paramObject.toString();
/*     */       }
/*     */ 
/* 599 */       reportConversionError(paramObject, paramClass);
/*     */ 
/* 601 */       break;
/*     */     case 6:
/*     */     case 7:
/* 605 */       if ((paramObject instanceof Wrapper)) {
/* 606 */         paramObject = ((Wrapper)paramObject).unwrap();
/*     */       }
/* 608 */       if (paramClass.isPrimitive()) {
/* 609 */         if (paramClass == Boolean.TYPE) {
/* 610 */           reportConversionError(paramObject, paramClass);
/*     */         }
/* 612 */         return coerceToNumber(paramClass, paramObject);
/*     */       }
/*     */ 
/* 615 */       if (paramClass == ScriptRuntime.StringClass) {
/* 616 */         return paramObject.toString();
/*     */       }
/*     */ 
/* 619 */       if (paramClass.isInstance(paramObject)) {
/* 620 */         return paramObject;
/*     */       }
/*     */ 
/* 623 */       reportConversionError(paramObject, paramClass);
/*     */ 
/* 627 */       break;
/*     */     case 8:
/* 630 */       if (paramClass == ScriptRuntime.StringClass) {
/* 631 */         return ScriptRuntime.toString(paramObject);
/*     */       }
/* 633 */       if (paramClass.isPrimitive()) {
/* 634 */         if (paramClass == Boolean.TYPE) {
/* 635 */           reportConversionError(paramObject, paramClass);
/*     */         }
/* 637 */         return coerceToNumber(paramClass, paramObject);
/*     */       }
/* 639 */       if (paramClass.isInstance(paramObject)) {
/* 640 */         return paramObject;
/*     */       }
/* 642 */       if ((paramClass == ScriptRuntime.DateClass) && ((paramObject instanceof NativeDate)))
/*     */       {
/* 645 */         double d = ((NativeDate)paramObject).getJSTimeValue();
/*     */ 
/* 647 */         return new Date(()d);
/*     */       }
/*     */       Object localObject1;
/*     */       Object localObject4;
/*     */       Object localObject5;
/* 649 */       if ((paramClass.isArray()) && ((paramObject instanceof NativeArray)))
/*     */       {
/* 652 */         localObject1 = (NativeArray)paramObject;
/* 653 */         long l = ((NativeArray)localObject1).getLength();
/* 654 */         localObject4 = paramClass.getComponentType();
/* 655 */         localObject5 = Array.newInstance((Class)localObject4, (int)l);
/* 656 */         for (int i = 0; i < l; i++) {
/*     */           try {
/* 658 */             Array.set(localObject5, i, coerceType((Class)localObject4, ((NativeArray)localObject1).get(i, (Wrapper)localObject1)));
/*     */           }
/*     */           catch (EvaluatorException localEvaluatorException)
/*     */           {
/* 662 */             reportConversionError(paramObject, paramClass);
/*     */           }
/*     */         }
/*     */ 
/* 666 */         return localObject5;
/*     */       }
/* 668 */       if ((paramObject instanceof Wrapper)) {
/* 669 */         paramObject = ((Wrapper)paramObject).unwrap();
/* 670 */         if (paramClass.isInstance(paramObject))
/* 671 */           return paramObject;
/* 672 */         reportConversionError(paramObject, paramClass);
/*     */       }
/* 674 */       else if ((paramClass.isInterface()) && ((paramObject instanceof Callable)))
/*     */       {
/* 682 */         if ((paramObject instanceof ScriptableObject)) {
/* 683 */           localObject1 = (ScriptableObject)paramObject;
/* 684 */           Object localObject2 = Kit.makeHashKeyFromPair(COERCED_INTERFACE_KEY, paramClass);
/*     */ 
/* 686 */           Object localObject3 = ((ScriptableObject)localObject1).getAssociatedValue(localObject2);
/* 687 */           if (localObject3 != null)
/*     */           {
/* 689 */             return localObject3;
/*     */           }
/* 691 */           localObject4 = Context.getContext();
/* 692 */           localObject5 = InterfaceAdapter.create((Context)localObject4, paramClass, (Callable)paramObject);
/*     */ 
/* 695 */           localObject5 = ((ScriptableObject)localObject1).associateValue(localObject2, localObject5);
/* 696 */           return localObject5;
/*     */         }
/* 698 */         reportConversionError(paramObject, paramClass);
/*     */       } else {
/* 700 */         reportConversionError(paramObject, paramClass);
/*     */       }
/*     */       break;
/*     */     }
/*     */ 
/* 705 */     return paramObject;
/*     */   }
/*     */ 
/*     */   private static Object coerceToNumber(Class<?> paramClass, Object paramObject)
/*     */   {
/* 710 */     Class localClass = paramObject.getClass();
/*     */ 
/* 713 */     if ((paramClass == Character.TYPE) || (paramClass == ScriptRuntime.CharacterClass)) {
/* 714 */       if (localClass == ScriptRuntime.CharacterClass) {
/* 715 */         return paramObject;
/*     */       }
/* 717 */       return Character.valueOf((char)(int)toInteger(paramObject, ScriptRuntime.CharacterClass, 0.0D, 65535.0D));
/*     */     }
/*     */ 
/* 724 */     if ((paramClass == ScriptRuntime.ObjectClass) || (paramClass == ScriptRuntime.DoubleClass) || (paramClass == Double.TYPE))
/*     */     {
/* 726 */       return localClass == ScriptRuntime.DoubleClass ? paramObject : new Double(toDouble(paramObject));
/*     */     }
/*     */     double d1;
/*     */     double d2;
/* 731 */     if ((paramClass == ScriptRuntime.FloatClass) || (paramClass == Float.TYPE)) {
/* 732 */       if (localClass == ScriptRuntime.FloatClass) {
/* 733 */         return paramObject;
/*     */       }
/*     */ 
/* 736 */       d1 = toDouble(paramObject);
/* 737 */       if ((Double.isInfinite(d1)) || (Double.isNaN(d1)) || (d1 == 0.0D))
/*     */       {
/* 739 */         return new Float((float)d1);
/*     */       }
/*     */ 
/* 742 */       d2 = Math.abs(d1);
/* 743 */       if (d2 < 1.401298464324817E-045D) {
/* 744 */         return new Float(d1 > 0.0D ? 0.0D : -0.0D);
/*     */       }
/* 746 */       if (d2 > 3.402823466385289E+038D) {
/* 747 */         return new Float(d1 > 0.0D ? (1.0F / 1.0F) : (1.0F / -1.0F));
/*     */       }
/*     */ 
/* 752 */       return new Float((float)d1);
/*     */     }
/*     */ 
/* 759 */     if ((paramClass == ScriptRuntime.IntegerClass) || (paramClass == Integer.TYPE)) {
/* 760 */       if (localClass == ScriptRuntime.IntegerClass) {
/* 761 */         return paramObject;
/*     */       }
/*     */ 
/* 764 */       return Integer.valueOf((int)toInteger(paramObject, ScriptRuntime.IntegerClass, -2147483648.0D, 2147483647.0D));
/*     */     }
/*     */ 
/* 771 */     if ((paramClass == ScriptRuntime.LongClass) || (paramClass == Long.TYPE)) {
/* 772 */       if (localClass == ScriptRuntime.LongClass) {
/* 773 */         return paramObject;
/*     */       }
/*     */ 
/* 782 */       d1 = Double.longBitsToDouble(4890909195324358655L);
/* 783 */       d2 = Double.longBitsToDouble(-4332462841530417152L);
/* 784 */       return Long.valueOf(toInteger(paramObject, ScriptRuntime.LongClass, d2, d1));
/*     */     }
/*     */ 
/* 791 */     if ((paramClass == ScriptRuntime.ShortClass) || (paramClass == Short.TYPE)) {
/* 792 */       if (localClass == ScriptRuntime.ShortClass) {
/* 793 */         return paramObject;
/*     */       }
/*     */ 
/* 796 */       return Short.valueOf((short)(int)toInteger(paramObject, ScriptRuntime.ShortClass, -32768.0D, 32767.0D));
/*     */     }
/*     */ 
/* 803 */     if ((paramClass == ScriptRuntime.ByteClass) || (paramClass == Byte.TYPE)) {
/* 804 */       if (localClass == ScriptRuntime.ByteClass) {
/* 805 */         return paramObject;
/*     */       }
/*     */ 
/* 808 */       return Byte.valueOf((byte)(int)toInteger(paramObject, ScriptRuntime.ByteClass, -128.0D, 127.0D));
/*     */     }
/*     */ 
/* 815 */     return new Double(toDouble(paramObject));
/*     */   }
/*     */ 
/*     */   private static double toDouble(Object paramObject)
/*     */   {
/* 821 */     if ((paramObject instanceof Number)) {
/* 822 */       return ((Number)paramObject).doubleValue();
/*     */     }
/* 824 */     if ((paramObject instanceof String)) {
/* 825 */       return ScriptRuntime.toNumber((String)paramObject);
/*     */     }
/* 827 */     if ((paramObject instanceof Wrapper)) {
/* 828 */       if ((paramObject instanceof Wrapper))
/*     */       {
/* 830 */         return toDouble(((Wrapper)paramObject).unwrap());
/*     */       }
/*     */ 
/* 833 */       return ScriptRuntime.toNumber(paramObject);
/*     */     }
/*     */ 
/*     */     Method localMethod;
/*     */     try
/*     */     {
/* 839 */       localMethod = paramObject.getClass().getMethod("doubleValue", (Class[])null);
/*     */     }
/*     */     catch (NoSuchMethodException localNoSuchMethodException)
/*     */     {
/* 843 */       localMethod = null;
/*     */     }
/*     */     catch (SecurityException localSecurityException) {
/* 846 */       localMethod = null;
/*     */     }
/* 848 */     if (localMethod != null) {
/*     */       try {
/* 850 */         return ((Number)localMethod.invoke(paramObject, (Object[])null)).doubleValue();
/*     */       }
/*     */       catch (IllegalAccessException localIllegalAccessException)
/*     */       {
/* 855 */         reportConversionError(paramObject, Double.TYPE);
/*     */       }
/*     */       catch (InvocationTargetException localInvocationTargetException)
/*     */       {
/* 859 */         reportConversionError(paramObject, Double.TYPE);
/*     */       }
/*     */     }
/* 862 */     return ScriptRuntime.toNumber(paramObject.toString());
/*     */   }
/*     */ 
/*     */   private static long toInteger(Object paramObject, Class<?> paramClass, double paramDouble1, double paramDouble2)
/*     */   {
/* 869 */     double d = toDouble(paramObject);
/*     */ 
/* 871 */     if ((Double.isInfinite(d)) || (Double.isNaN(d)))
/*     */     {
/* 873 */       reportConversionError(ScriptRuntime.toString(paramObject), paramClass);
/*     */     }
/*     */ 
/* 876 */     if (d > 0.0D) {
/* 877 */       d = Math.floor(d);
/*     */     }
/*     */     else {
/* 880 */       d = Math.ceil(d);
/*     */     }
/*     */ 
/* 883 */     if ((d < paramDouble1) || (d > paramDouble2))
/*     */     {
/* 885 */       reportConversionError(ScriptRuntime.toString(paramObject), paramClass);
/*     */     }
/* 887 */     return ()d;
/*     */   }
/*     */ 
/*     */   static void reportConversionError(Object paramObject, Class<?> paramClass)
/*     */   {
/* 894 */     throw Context.reportRuntimeError2("msg.conversion.not.allowed", String.valueOf(paramObject), JavaMembers.javaSignature(paramClass));
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 923 */     Class[] arrayOfClass = new Class[2];
/* 924 */     Class localClass = Kit.classOrNull("sun.org.mozilla.javascript.internal.JavaAdapter");
/* 925 */     if (localClass != null)
/*     */       try {
/* 927 */         arrayOfClass[0] = ScriptRuntime.ObjectClass;
/* 928 */         arrayOfClass[1] = Kit.classOrNull("java.io.ObjectOutputStream");
/* 929 */         adapter_writeAdapterObject = localClass.getMethod("writeAdapterObject", arrayOfClass);
/*     */ 
/* 932 */         arrayOfClass[0] = ScriptRuntime.ScriptableClass;
/* 933 */         arrayOfClass[1] = Kit.classOrNull("java.io.ObjectInputStream");
/* 934 */         adapter_readAdapterObject = localClass.getMethod("readAdapterObject", arrayOfClass);
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException)
/*     */       {
/* 938 */         adapter_writeAdapterObject = null;
/* 939 */         adapter_readAdapterObject = null;
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeJavaObject
 * JD-Core Version:    0.6.2
 */