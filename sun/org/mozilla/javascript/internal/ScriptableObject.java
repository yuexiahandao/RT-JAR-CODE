/*      */ package sun.org.mozilla.javascript.internal;
/*      */ 
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.AccessibleObject;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Member;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import sun.org.mozilla.javascript.internal.annotations.JSConstructor;
/*      */ import sun.org.mozilla.javascript.internal.annotations.JSFunction;
/*      */ import sun.org.mozilla.javascript.internal.annotations.JSGetter;
/*      */ import sun.org.mozilla.javascript.internal.annotations.JSSetter;
/*      */ import sun.org.mozilla.javascript.internal.annotations.JSStaticFunction;
/*      */ import sun.org.mozilla.javascript.internal.debug.DebuggableObject;
/*      */ 
/*      */ public abstract class ScriptableObject
/*      */   implements Scriptable, DebuggableObject, ConstProperties
/*      */ {
/*      */   public static final int EMPTY = 0;
/*      */   public static final int READONLY = 1;
/*      */   public static final int DONTENUM = 2;
/*      */   public static final int PERMANENT = 4;
/*      */   public static final int UNINITIALIZED_CONST = 8;
/*      */   public static final int CONST = 13;
/*      */   private Scriptable prototypeObject;
/*      */   private Scriptable parentScopeObject;
/*  145 */   private static final Slot REMOVED = new Slot(null, 0, 1);
/*      */   private transient Slot[] slots;
/*      */   private int count;
/*      */   private transient Slot firstAdded;
/*      */   private transient Slot lastAdded;
/*  161 */   private transient Slot lastAccess = REMOVED;
/*      */   private volatile Map<Object, Object> associatedValues;
/*      */   private static final int SLOT_QUERY = 1;
/*      */   private static final int SLOT_MODIFY = 2;
/*      */   private static final int SLOT_REMOVE = 3;
/*      */   private static final int SLOT_MODIFY_GETTER_SETTER = 4;
/*      */   private static final int SLOT_MODIFY_CONST = 5;
/*      */   private static final int SLOT_CONVERT_ACCESSOR_TO_DATA = 6;
/*  172 */   private boolean isExtensible = true;
/*      */ 
/*      */   protected static ScriptableObject buildDataDescriptor(Scriptable paramScriptable, Object paramObject, int paramInt)
/*      */   {
/*  221 */     NativeObject localNativeObject = new NativeObject();
/*  222 */     ScriptRuntime.setObjectProtoAndParent(localNativeObject, paramScriptable);
/*      */ 
/*  224 */     localNativeObject.defineProperty("value", paramObject, 0);
/*  225 */     localNativeObject.defineProperty("writable", Boolean.valueOf((paramInt & 0x1) == 0), 0);
/*  226 */     localNativeObject.defineProperty("enumerable", Boolean.valueOf((paramInt & 0x2) == 0), 0);
/*  227 */     localNativeObject.defineProperty("configurable", Boolean.valueOf((paramInt & 0x4) == 0), 0);
/*  228 */     return localNativeObject;
/*      */   }
/*      */ 
/*      */   static void checkValidAttributes(int paramInt)
/*      */   {
/*  256 */     if ((paramInt & 0xFFFFFFF0) != 0)
/*  257 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*      */   }
/*      */ 
/*      */   public ScriptableObject()
/*      */   {
/*  263 */     Context.checkRhinoDisabled();
/*      */   }
/*      */ 
/*      */   public ScriptableObject(Scriptable paramScriptable1, Scriptable paramScriptable2)
/*      */   {
/*  268 */     Context.checkRhinoDisabled();
/*  269 */     if (paramScriptable1 == null) {
/*  270 */       throw new IllegalArgumentException();
/*      */     }
/*  272 */     this.parentScopeObject = paramScriptable1;
/*  273 */     this.prototypeObject = paramScriptable2;
/*      */   }
/*      */ 
/*      */   public String getTypeOf()
/*      */   {
/*  282 */     return avoidObjectDetection() ? "undefined" : "object";
/*      */   }
/*      */ 
/*      */   public abstract String getClassName();
/*      */ 
/*      */   public boolean has(String paramString, Scriptable paramScriptable)
/*      */   {
/*  303 */     return null != getSlot(paramString, 0, 1);
/*      */   }
/*      */ 
/*      */   public boolean has(int paramInt, Scriptable paramScriptable)
/*      */   {
/*  315 */     return null != getSlot(null, paramInt, 1);
/*      */   }
/*      */ 
/*      */   public Object get(String paramString, Scriptable paramScriptable)
/*      */   {
/*  330 */     return getImpl(paramString, 0, paramScriptable);
/*      */   }
/*      */ 
/*      */   public Object get(int paramInt, Scriptable paramScriptable)
/*      */   {
/*  342 */     return getImpl(null, paramInt, paramScriptable);
/*      */   }
/*      */ 
/*      */   public void put(String paramString, Scriptable paramScriptable, Object paramObject)
/*      */   {
/*  362 */     if (putImpl(paramString, 0, paramScriptable, paramObject, 0)) {
/*  363 */       return;
/*      */     }
/*  365 */     if (paramScriptable == this) throw Kit.codeBug();
/*  366 */     paramScriptable.put(paramString, paramScriptable, paramObject);
/*      */   }
/*      */ 
/*      */   public void put(int paramInt, Scriptable paramScriptable, Object paramObject)
/*      */   {
/*  378 */     if (putImpl(null, paramInt, paramScriptable, paramObject, 0)) {
/*  379 */       return;
/*      */     }
/*  381 */     if (paramScriptable == this) throw Kit.codeBug();
/*  382 */     paramScriptable.put(paramInt, paramScriptable, paramObject);
/*      */   }
/*      */ 
/*      */   public void delete(String paramString)
/*      */   {
/*  395 */     checkNotSealed(paramString, 0);
/*  396 */     accessSlot(paramString, 0, 3);
/*      */   }
/*      */ 
/*      */   public void delete(int paramInt)
/*      */   {
/*  409 */     checkNotSealed(null, paramInt);
/*  410 */     accessSlot(null, paramInt, 3);
/*      */   }
/*      */ 
/*      */   public void putConst(String paramString, Scriptable paramScriptable, Object paramObject)
/*      */   {
/*  430 */     if (putImpl(paramString, 0, paramScriptable, paramObject, 1)) {
/*  431 */       return;
/*      */     }
/*  433 */     if (paramScriptable == this) throw Kit.codeBug();
/*  434 */     if ((paramScriptable instanceof ConstProperties))
/*  435 */       ((ConstProperties)paramScriptable).putConst(paramString, paramScriptable, paramObject);
/*      */     else
/*  437 */       paramScriptable.put(paramString, paramScriptable, paramObject);
/*      */   }
/*      */ 
/*      */   public void defineConst(String paramString, Scriptable paramScriptable)
/*      */   {
/*  442 */     if (putImpl(paramString, 0, paramScriptable, Undefined.instance, 8)) {
/*  443 */       return;
/*      */     }
/*  445 */     if (paramScriptable == this) throw Kit.codeBug();
/*  446 */     if ((paramScriptable instanceof ConstProperties))
/*  447 */       ((ConstProperties)paramScriptable).defineConst(paramString, paramScriptable);
/*      */   }
/*      */ 
/*      */   public boolean isConst(String paramString)
/*      */   {
/*  457 */     Slot localSlot = getSlot(paramString, 0, 1);
/*  458 */     if (localSlot == null) {
/*  459 */       return false;
/*      */     }
/*  461 */     return (localSlot.getAttributes() & 0x5) == 5;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public final int getAttributes(String paramString, Scriptable paramScriptable)
/*      */   {
/*  471 */     return getAttributes(paramString);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public final int getAttributes(int paramInt, Scriptable paramScriptable)
/*      */   {
/*  480 */     return getAttributes(paramInt);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public final void setAttributes(String paramString, Scriptable paramScriptable, int paramInt)
/*      */   {
/*  490 */     setAttributes(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public void setAttributes(int paramInt1, Scriptable paramScriptable, int paramInt2)
/*      */   {
/*  500 */     setAttributes(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public int getAttributes(String paramString)
/*      */   {
/*  520 */     return findAttributeSlot(paramString, 0, 1).getAttributes();
/*      */   }
/*      */ 
/*      */   public int getAttributes(int paramInt)
/*      */   {
/*  538 */     return findAttributeSlot(null, paramInt, 1).getAttributes();
/*      */   }
/*      */ 
/*      */   public void setAttributes(String paramString, int paramInt)
/*      */   {
/*  564 */     checkNotSealed(paramString, 0);
/*  565 */     findAttributeSlot(paramString, 0, 2).setAttributes(paramInt);
/*      */   }
/*      */ 
/*      */   public void setAttributes(int paramInt1, int paramInt2)
/*      */   {
/*  582 */     checkNotSealed(null, paramInt1);
/*  583 */     findAttributeSlot(null, paramInt1, 2).setAttributes(paramInt2);
/*      */   }
/*      */ 
/*      */   public void setGetterOrSetter(String paramString, int paramInt, Callable paramCallable, boolean paramBoolean)
/*      */   {
/*  592 */     setGetterOrSetter(paramString, paramInt, paramCallable, paramBoolean, false);
/*      */   }
/*      */ 
/*      */   private void setGetterOrSetter(String paramString, int paramInt, Callable paramCallable, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  597 */     if ((paramString != null) && (paramInt != 0)) {
/*  598 */       throw new IllegalArgumentException(paramString);
/*      */     }
/*  600 */     if (!paramBoolean2)
/*  601 */       checkNotSealed(paramString, paramInt);
/*      */     GetterSlot localGetterSlot;
/*  605 */     if (isExtensible()) {
/*  606 */       localGetterSlot = (GetterSlot)getSlot(paramString, paramInt, 4);
/*      */     } else {
/*  608 */       localGetterSlot = (GetterSlot)getSlot(paramString, paramInt, 1);
/*  609 */       if (localGetterSlot == null) {
/*  610 */         return;
/*      */       }
/*      */     }
/*  613 */     if (!paramBoolean2) {
/*  614 */       localGetterSlot.checkNotReadonly();
/*      */     }
/*  616 */     if (paramBoolean1)
/*  617 */       localGetterSlot.setter = paramCallable;
/*      */     else {
/*  619 */       localGetterSlot.getter = paramCallable;
/*      */     }
/*  621 */     localGetterSlot.value = Undefined.instance;
/*      */   }
/*      */ 
/*      */   public Object getGetterOrSetter(String paramString, int paramInt, boolean paramBoolean)
/*      */   {
/*  639 */     if ((paramString != null) && (paramInt != 0))
/*  640 */       throw new IllegalArgumentException(paramString);
/*  641 */     Slot localSlot = getSlot(paramString, paramInt, 1);
/*  642 */     if (localSlot == null)
/*  643 */       return null;
/*  644 */     if ((localSlot instanceof GetterSlot)) {
/*  645 */       GetterSlot localGetterSlot = (GetterSlot)localSlot;
/*  646 */       Object localObject = paramBoolean ? localGetterSlot.setter : localGetterSlot.getter;
/*  647 */       return localObject != null ? localObject : Undefined.instance;
/*      */     }
/*  649 */     return Undefined.instance;
/*      */   }
/*      */ 
/*      */   protected boolean isGetterOrSetter(String paramString, int paramInt, boolean paramBoolean)
/*      */   {
/*  660 */     Slot localSlot = getSlot(paramString, paramInt, 1);
/*  661 */     if ((localSlot instanceof GetterSlot)) {
/*  662 */       if ((paramBoolean) && (((GetterSlot)localSlot).setter != null)) return true;
/*  663 */       if ((!paramBoolean) && (((GetterSlot)localSlot).getter != null)) return true;
/*      */     }
/*  665 */     return false;
/*      */   }
/*      */ 
/*      */   void addLazilyInitializedValue(String paramString, int paramInt1, LazilyLoadedCtor paramLazilyLoadedCtor, int paramInt2)
/*      */   {
/*  671 */     if ((paramString != null) && (paramInt1 != 0))
/*  672 */       throw new IllegalArgumentException(paramString);
/*  673 */     checkNotSealed(paramString, paramInt1);
/*  674 */     GetterSlot localGetterSlot = (GetterSlot)getSlot(paramString, paramInt1, 4);
/*      */ 
/*  676 */     localGetterSlot.setAttributes(paramInt2);
/*  677 */     localGetterSlot.getter = null;
/*  678 */     localGetterSlot.setter = null;
/*  679 */     localGetterSlot.value = paramLazilyLoadedCtor;
/*      */   }
/*      */ 
/*      */   public Scriptable getPrototype()
/*      */   {
/*  687 */     return this.prototypeObject;
/*      */   }
/*      */ 
/*      */   public void setPrototype(Scriptable paramScriptable)
/*      */   {
/*  695 */     this.prototypeObject = paramScriptable;
/*      */   }
/*      */ 
/*      */   public Scriptable getParentScope()
/*      */   {
/*  703 */     return this.parentScopeObject;
/*      */   }
/*      */ 
/*      */   public void setParentScope(Scriptable paramScriptable)
/*      */   {
/*  711 */     this.parentScopeObject = paramScriptable;
/*      */   }
/*      */ 
/*      */   public Object[] getIds()
/*      */   {
/*  726 */     return getIds(false);
/*      */   }
/*      */ 
/*      */   public Object[] getAllIds()
/*      */   {
/*  741 */     return getIds(true);
/*      */   }
/*      */ 
/*      */   public Object getDefaultValue(Class<?> paramClass)
/*      */   {
/*  760 */     return getDefaultValue(this, paramClass);
/*      */   }
/*      */ 
/*      */   public static Object getDefaultValue(Scriptable paramScriptable, Class<?> paramClass)
/*      */   {
/*  765 */     Context localContext = null;
/*  766 */     for (int i = 0; i < 2; i++)
/*      */     {
/*      */       int j;
/*  768 */       if (paramClass == ScriptRuntime.StringClass)
/*  769 */         j = i == 0 ? 1 : 0;
/*      */       else
/*  771 */         j = i == 1 ? 1 : 0;
/*      */       String str2;
/*      */       Object[] arrayOfObject;
/*  776 */       if (j != 0) {
/*  777 */         str2 = "toString";
/*  778 */         arrayOfObject = ScriptRuntime.emptyArgs;
/*      */       } else {
/*  780 */         str2 = "valueOf";
/*  781 */         arrayOfObject = new Object[1];
/*      */ 
/*  783 */         if (paramClass == null)
/*  784 */           localObject1 = "undefined";
/*  785 */         else if (paramClass == ScriptRuntime.StringClass)
/*  786 */           localObject1 = "string";
/*  787 */         else if (paramClass == ScriptRuntime.ScriptableClass)
/*  788 */           localObject1 = "object";
/*  789 */         else if (paramClass == ScriptRuntime.FunctionClass)
/*  790 */           localObject1 = "function";
/*  791 */         else if ((paramClass == ScriptRuntime.BooleanClass) || (paramClass == Boolean.TYPE))
/*      */         {
/*  794 */           localObject1 = "boolean";
/*  795 */         } else if ((paramClass == ScriptRuntime.NumberClass) || (paramClass == ScriptRuntime.ByteClass) || (paramClass == Byte.TYPE) || (paramClass == ScriptRuntime.ShortClass) || (paramClass == Short.TYPE) || (paramClass == ScriptRuntime.IntegerClass) || (paramClass == Integer.TYPE) || (paramClass == ScriptRuntime.FloatClass) || (paramClass == Float.TYPE) || (paramClass == ScriptRuntime.DoubleClass) || (paramClass == Double.TYPE))
/*      */         {
/*  807 */           localObject1 = "number";
/*      */         }
/*  809 */         else throw Context.reportRuntimeError1("msg.invalid.type", paramClass.toString());
/*      */ 
/*  812 */         arrayOfObject[0] = localObject1;
/*      */       }
/*  814 */       Object localObject1 = getProperty(paramScriptable, str2);
/*  815 */       if ((localObject1 instanceof Function))
/*      */       {
/*  817 */         Function localFunction = (Function)localObject1;
/*  818 */         if (localContext == null)
/*  819 */           localContext = Context.getContext();
/*  820 */         localObject1 = localFunction.call(localContext, localFunction.getParentScope(), paramScriptable, arrayOfObject);
/*  821 */         if (localObject1 != null) {
/*  822 */           if (!(localObject1 instanceof ConstProperties)) {
/*  823 */             return localObject1;
/*      */           }
/*  825 */           if ((paramClass == ScriptRuntime.ScriptableClass) || (paramClass == ScriptRuntime.FunctionClass))
/*      */           {
/*  828 */             return localObject1;
/*      */           }
/*  830 */           if ((j != 0) && ((localObject1 instanceof Wrapper)))
/*      */           {
/*  833 */             Object localObject2 = ((Wrapper)localObject1).unwrap();
/*  834 */             if ((localObject2 instanceof String))
/*  835 */               return localObject2;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  840 */     String str1 = paramClass == null ? "undefined" : paramClass.getName();
/*  841 */     throw ScriptRuntime.typeError1("msg.default.value", str1);
/*      */   }
/*      */ 
/*      */   public boolean hasInstance(Scriptable paramScriptable)
/*      */   {
/*  859 */     return ScriptRuntime.jsDelegatesTo(paramScriptable, this);
/*      */   }
/*      */ 
/*      */   public boolean avoidObjectDetection()
/*      */   {
/*  874 */     return false;
/*      */   }
/*      */ 
/*      */   protected Object equivalentValues(Object paramObject)
/*      */   {
/*  892 */     return this == paramObject ? Boolean.TRUE : ConstProperties.NOT_FOUND;
/*      */   }
/*      */ 
/*      */   public static <T extends Scriptable> void defineClass(Scriptable paramScriptable, Class<T> paramClass)
/*      */     throws IllegalAccessException, InstantiationException, InvocationTargetException
/*      */   {
/*  993 */     defineClass(paramScriptable, paramClass, false, false);
/*      */   }
/*      */ 
/*      */   public static <T extends Scriptable> void defineClass(Scriptable paramScriptable, Class<T> paramClass, boolean paramBoolean)
/*      */     throws IllegalAccessException, InstantiationException, InvocationTargetException
/*      */   {
/* 1024 */     defineClass(paramScriptable, paramClass, paramBoolean, false);
/*      */   }
/*      */ 
/*      */   public static <T extends Scriptable> String defineClass(Scriptable paramScriptable, Class<T> paramClass, boolean paramBoolean1, boolean paramBoolean2)
/*      */     throws IllegalAccessException, InstantiationException, InvocationTargetException
/*      */   {
/* 1060 */     BaseFunction localBaseFunction = buildClassCtor(paramScriptable, paramClass, paramBoolean1, paramBoolean2);
/*      */ 
/* 1062 */     if (localBaseFunction == null)
/* 1063 */       return null;
/* 1064 */     String str = localBaseFunction.getClassPrototype().getClassName();
/* 1065 */     defineProperty(paramScriptable, str, localBaseFunction, 2);
/* 1066 */     return str;
/*      */   }
/*      */ 
/*      */   static <T extends Scriptable> BaseFunction buildClassCtor(Scriptable paramScriptable, Class<T> paramClass, boolean paramBoolean1, boolean paramBoolean2)
/*      */     throws IllegalAccessException, InstantiationException, InvocationTargetException
/*      */   {
/* 1076 */     Method[] arrayOfMethod = FunctionObject.getMethodList(paramClass);
/* 1077 */     for (int i = 0; i < arrayOfMethod.length; i++) {
/* 1078 */       localObject1 = arrayOfMethod[i];
/* 1079 */       if (((Method)localObject1).getName().equals("init"))
/*      */       {
/* 1081 */         Class[] arrayOfClass = ((Method)localObject1).getParameterTypes();
/* 1082 */         if ((arrayOfClass.length == 3) && (arrayOfClass[0] == ScriptRuntime.ContextClass) && (arrayOfClass[1] == ScriptRuntime.ScriptableClass) && (arrayOfClass[2] == Boolean.TYPE) && (Modifier.isStatic(((Method)localObject1).getModifiers())))
/*      */         {
/* 1088 */           localObject2 = new Object[] { Context.getContext(), paramScriptable, paramBoolean1 ? Boolean.TRUE : Boolean.FALSE };
/*      */ 
/* 1090 */           ((Method)localObject1).invoke(null, (Object[])localObject2);
/* 1091 */           return null;
/*      */         }
/* 1093 */         if ((arrayOfClass.length == 1) && (arrayOfClass[0] == ScriptRuntime.ScriptableClass) && (Modifier.isStatic(((Method)localObject1).getModifiers())))
/*      */         {
/* 1097 */           localObject2 = new Object[] { paramScriptable };
/* 1098 */           ((Method)localObject1).invoke(null, (Object[])localObject2);
/* 1099 */           return null;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1107 */     Constructor[] arrayOfConstructor = paramClass.getConstructors();
/* 1108 */     Object localObject1 = null;
/* 1109 */     for (int j = 0; j < arrayOfConstructor.length; j++) {
/* 1110 */       if (arrayOfConstructor[j].getParameterTypes().length == 0) {
/* 1111 */         localObject1 = arrayOfConstructor[j];
/* 1112 */         break;
/*      */       }
/*      */     }
/* 1115 */     if (localObject1 == null) {
/* 1116 */       throw Context.reportRuntimeError1("msg.zero.arg.ctor", paramClass.getName());
/*      */     }
/*      */ 
/* 1120 */     Scriptable localScriptable1 = (ConstProperties)((Constructor)localObject1).newInstance(ScriptRuntime.emptyArgs);
/* 1121 */     Object localObject2 = localScriptable1.getClassName();
/*      */ 
/* 1125 */     Scriptable localScriptable2 = null;
/* 1126 */     if (paramBoolean2) {
/* 1127 */       Class localClass1 = paramClass.getSuperclass();
/* 1128 */       if ((ScriptRuntime.ScriptableClass.isAssignableFrom(localClass1)) && (!Modifier.isAbstract(localClass1.getModifiers())))
/*      */       {
/* 1131 */         Class localClass2 = extendsScriptable(localClass1);
/*      */ 
/* 1133 */         String str1 = defineClass(paramScriptable, localClass2, paramBoolean1, paramBoolean2);
/*      */ 
/* 1135 */         if (str1 != null) {
/* 1136 */           localScriptable2 = getClassPrototype(paramScriptable, str1);
/*      */         }
/*      */       }
/*      */     }
/* 1140 */     if (localScriptable2 == null) {
/* 1141 */       localScriptable2 = getObjectPrototype(paramScriptable);
/*      */     }
/* 1143 */     localScriptable1.setPrototype(localScriptable2);
/*      */ 
/* 1154 */     Object localObject3 = findAnnotatedMember(arrayOfMethod, JSConstructor.class);
/* 1155 */     if (localObject3 == null) {
/* 1156 */       localObject3 = findAnnotatedMember(arrayOfConstructor, JSConstructor.class);
/*      */     }
/* 1158 */     if (localObject3 == null) {
/* 1159 */       localObject3 = FunctionObject.findSingleMethod(arrayOfMethod, "jsConstructor");
/*      */     }
/* 1161 */     if (localObject3 == null) {
/* 1162 */       if (arrayOfConstructor.length == 1)
/* 1163 */         localObject3 = arrayOfConstructor[0];
/* 1164 */       else if (arrayOfConstructor.length == 2) {
/* 1165 */         if (arrayOfConstructor[0].getParameterTypes().length == 0)
/* 1166 */           localObject3 = arrayOfConstructor[1];
/* 1167 */         else if (arrayOfConstructor[1].getParameterTypes().length == 0)
/* 1168 */           localObject3 = arrayOfConstructor[0];
/*      */       }
/* 1170 */       if (localObject3 == null) {
/* 1171 */         throw Context.reportRuntimeError1("msg.ctor.multiple.parms", paramClass.getName());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1176 */     FunctionObject localFunctionObject1 = new FunctionObject((String)localObject2, (Member)localObject3, paramScriptable);
/* 1177 */     if (localFunctionObject1.isVarArgsMethod()) {
/* 1178 */       throw Context.reportRuntimeError1("msg.varargs.ctor", ((Member)localObject3).getName());
/*      */     }
/*      */ 
/* 1181 */     localFunctionObject1.initAsConstructor(paramScriptable, localScriptable1);
/*      */ 
/* 1183 */     Object localObject4 = null;
/* 1184 */     HashSet localHashSet = new HashSet(arrayOfMethod.length);
/* 1185 */     for (Method localMethod1 : arrayOfMethod)
/* 1186 */       if (localMethod1 != localObject3)
/*      */       {
/* 1189 */         Object localObject6 = localMethod1.getName();
/*      */         Object localObject7;
/* 1190 */         if (((String)localObject6).equals("finishInit")) {
/* 1191 */           localObject7 = localMethod1.getParameterTypes();
/* 1192 */           if ((localObject7.length == 3) && (localObject7[0] == ScriptRuntime.ScriptableClass) && (localObject7[1] == FunctionObject.class) && (localObject7[2] == ScriptRuntime.ScriptableClass) && (Modifier.isStatic(localMethod1.getModifiers())))
/*      */           {
/* 1198 */             localObject4 = localMethod1;
/* 1199 */             continue;
/*      */           }
/*      */         }
/*      */ 
/* 1203 */         if (((String)localObject6).indexOf('$') == -1)
/*      */         {
/* 1205 */           if (!((String)localObject6).equals("jsConstructor"))
/*      */           {
/* 1208 */             localObject7 = null;
/* 1209 */             String str2 = null;
/* 1210 */             if (localMethod1.isAnnotationPresent(JSFunction.class))
/* 1211 */               localObject7 = localMethod1.getAnnotation(JSFunction.class);
/* 1212 */             else if (localMethod1.isAnnotationPresent(JSStaticFunction.class))
/* 1213 */               localObject7 = localMethod1.getAnnotation(JSStaticFunction.class);
/* 1214 */             else if (localMethod1.isAnnotationPresent(JSGetter.class))
/* 1215 */               localObject7 = localMethod1.getAnnotation(JSGetter.class);
/* 1216 */             else if (localMethod1.isAnnotationPresent(JSSetter.class))
/*      */               {
/*      */                 continue;
/*      */               }
/* 1220 */             if (localObject7 == null) {
/* 1221 */               if (((String)localObject6).startsWith("jsFunction_"))
/* 1222 */                 str2 = "jsFunction_";
/* 1223 */               else if (((String)localObject6).startsWith("jsStaticFunction_"))
/* 1224 */                 str2 = "jsStaticFunction_";
/* 1225 */               else if (((String)localObject6).startsWith("jsGet_"))
/* 1226 */                 str2 = "jsGet_";
/* 1227 */               else if (localObject7 == null)
/*      */                 {
/*      */                   continue;
/*      */                 }
/*      */             }
/*      */ 
/* 1233 */             String str3 = getPropertyName((String)localObject6, str2, (Annotation)localObject7);
/* 1234 */             if (localHashSet.contains(str3)) {
/* 1235 */               throw Context.reportRuntimeError2("duplicate.defineClass.name", localObject6, str3);
/*      */             }
/*      */ 
/* 1238 */             localHashSet.add(str3);
/* 1239 */             localObject6 = str3;
/* 1240 */             if (((localObject7 instanceof JSGetter)) || (str2 == "jsGet_")) {
/* 1241 */               if (!(localScriptable1 instanceof ScriptableObject)) {
/* 1242 */                 throw Context.reportRuntimeError2("msg.extend.scriptable", localScriptable1.getClass().toString(), localObject6);
/*      */               }
/*      */ 
/* 1246 */               Method localMethod2 = findSetterMethod(arrayOfMethod, (String)localObject6, "jsSet_");
/* 1247 */               int i1 = 0x6 | (localMethod2 != null ? 0 : 1);
/*      */ 
/* 1251 */               ((ScriptableObject)localScriptable1).defineProperty((String)localObject6, null, localMethod1, localMethod2, i1);
/*      */             }
/*      */             else
/*      */             {
/* 1257 */               int n = ((localObject7 instanceof JSStaticFunction)) || (str2 == "jsStaticFunction_") ? 1 : 0;
/*      */ 
/* 1259 */               if ((n != 0) && (!Modifier.isStatic(localMethod1.getModifiers()))) {
/* 1260 */                 throw Context.reportRuntimeError("jsStaticFunction must be used with static method.");
/*      */               }
/*      */ 
/* 1264 */               FunctionObject localFunctionObject2 = new FunctionObject((String)localObject6, localMethod1, localScriptable1);
/* 1265 */               if (localFunctionObject2.isVarArgsConstructor()) {
/* 1266 */                 throw Context.reportRuntimeError1("msg.varargs.fun", ((Member)localObject3).getName());
/*      */               }
/*      */ 
/* 1269 */               defineProperty(n != 0 ? localFunctionObject1 : localScriptable1, (String)localObject6, localFunctionObject2, 2);
/* 1270 */               if (paramBoolean1)
/* 1271 */                 localFunctionObject2.sealObject();
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1276 */     if (localObject4 != null) {
/* 1277 */       ??? = new Object[] { paramScriptable, localFunctionObject1, localScriptable1 };
/* 1278 */       localObject4.invoke(null, (Object[])???);
/*      */     }
/*      */ 
/* 1282 */     if (paramBoolean1) {
/* 1283 */       localFunctionObject1.sealObject();
/* 1284 */       if ((localScriptable1 instanceof ScriptableObject)) {
/* 1285 */         ((ScriptableObject)localScriptable1).sealObject();
/*      */       }
/*      */     }
/*      */ 
/* 1289 */     return localFunctionObject1;
/*      */   }
/*      */ 
/*      */   private static Member findAnnotatedMember(AccessibleObject[] paramArrayOfAccessibleObject, Class<? extends Annotation> paramClass)
/*      */   {
/* 1294 */     for (AccessibleObject localAccessibleObject : paramArrayOfAccessibleObject) {
/* 1295 */       if (localAccessibleObject.isAnnotationPresent(paramClass)) {
/* 1296 */         return (Member)localAccessibleObject;
/*      */       }
/*      */     }
/* 1299 */     return null;
/*      */   }
/*      */ 
/*      */   private static Method findSetterMethod(Method[] paramArrayOfMethod, String paramString1, String paramString2)
/*      */   {
/* 1305 */     String str = "set" + Character.toUpperCase(paramString1.charAt(0)) + paramString1.substring(1);
/*      */     Object localObject2;
/* 1308 */     for (Method localMethod : paramArrayOfMethod) {
/* 1309 */       localObject2 = (JSSetter)localMethod.getAnnotation(JSSetter.class);
/* 1310 */       if ((localObject2 != null) && (
/* 1311 */         (paramString1.equals(((JSSetter)localObject2).value())) || (("".equals(((JSSetter)localObject2).value())) && (str.equals(localMethod.getName())))))
/*      */       {
/* 1313 */         return localMethod;
/*      */       }
/*      */     }
/*      */ 
/* 1317 */     ??? = paramString2 + paramString1;
/* 1318 */     for (localObject2 : paramArrayOfMethod) {
/* 1319 */       if (((String)???).equals(((Method)localObject2).getName())) {
/* 1320 */         return localObject2;
/*      */       }
/*      */     }
/* 1323 */     return null;
/*      */   }
/*      */ 
/*      */   private static String getPropertyName(String paramString1, String paramString2, Annotation paramAnnotation)
/*      */   {
/* 1329 */     if (paramString2 != null) {
/* 1330 */       return paramString1.substring(paramString2.length());
/*      */     }
/* 1332 */     String str = null;
/* 1333 */     if ((paramAnnotation instanceof JSGetter)) {
/* 1334 */       str = ((JSGetter)paramAnnotation).value();
/* 1335 */       if (((str == null) || (str.length() == 0)) && 
/* 1336 */         (paramString1.length() > 3) && (paramString1.startsWith("get"))) {
/* 1337 */         str = paramString1.substring(3);
/* 1338 */         if (Character.isUpperCase(str.charAt(0))) {
/* 1339 */           if (str.length() == 1)
/* 1340 */             str = str.toLowerCase();
/* 1341 */           else if (!Character.isUpperCase(str.charAt(1))) {
/* 1342 */             str = Character.toLowerCase(str.charAt(0)) + str.substring(1);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/* 1348 */     else if ((paramAnnotation instanceof JSFunction)) {
/* 1349 */       str = ((JSFunction)paramAnnotation).value();
/* 1350 */     } else if ((paramAnnotation instanceof JSStaticFunction)) {
/* 1351 */       str = ((JSStaticFunction)paramAnnotation).value();
/*      */     }
/* 1353 */     if ((str == null) || (str.length() == 0)) {
/* 1354 */       str = paramString1;
/*      */     }
/* 1356 */     return str;
/*      */   }
/*      */ 
/*      */   private static <T extends Scriptable> Class<T> extendsScriptable(Class<?> paramClass)
/*      */   {
/* 1362 */     if (ScriptRuntime.ScriptableClass.isAssignableFrom(paramClass))
/* 1363 */       return paramClass;
/* 1364 */     return null;
/*      */   }
/*      */ 
/*      */   public void defineProperty(String paramString, Object paramObject, int paramInt)
/*      */   {
/* 1380 */     checkNotSealed(paramString, 0);
/* 1381 */     put(paramString, this, paramObject);
/* 1382 */     setAttributes(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   public static void defineProperty(Scriptable paramScriptable, String paramString, Object paramObject, int paramInt)
/*      */   {
/* 1395 */     if (!(paramScriptable instanceof ScriptableObject)) {
/* 1396 */       paramScriptable.put(paramString, paramScriptable, paramObject);
/* 1397 */       return;
/*      */     }
/* 1399 */     ScriptableObject localScriptableObject = (ScriptableObject)paramScriptable;
/* 1400 */     localScriptableObject.defineProperty(paramString, paramObject, paramInt);
/*      */   }
/*      */ 
/*      */   public static void defineConstProperty(Scriptable paramScriptable, String paramString)
/*      */   {
/* 1412 */     if ((paramScriptable instanceof ConstProperties)) {
/* 1413 */       ConstProperties localConstProperties = (ConstProperties)paramScriptable;
/* 1414 */       localConstProperties.defineConst(paramString, paramScriptable);
/*      */     } else {
/* 1416 */       defineProperty(paramScriptable, paramString, Undefined.instance, 13);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void defineProperty(String paramString, Class<?> paramClass, int paramInt)
/*      */   {
/* 1440 */     int i = paramString.length();
/* 1441 */     if (i == 0) throw new IllegalArgumentException();
/* 1442 */     char[] arrayOfChar = new char[3 + i];
/* 1443 */     paramString.getChars(0, i, arrayOfChar, 3);
/* 1444 */     arrayOfChar[3] = Character.toUpperCase(arrayOfChar[3]);
/* 1445 */     arrayOfChar[0] = 'g';
/* 1446 */     arrayOfChar[1] = 'e';
/* 1447 */     arrayOfChar[2] = 't';
/* 1448 */     String str1 = new String(arrayOfChar);
/* 1449 */     arrayOfChar[0] = 's';
/* 1450 */     String str2 = new String(arrayOfChar);
/*      */ 
/* 1452 */     Method[] arrayOfMethod = FunctionObject.getMethodList(paramClass);
/* 1453 */     Method localMethod1 = FunctionObject.findSingleMethod(arrayOfMethod, str1);
/* 1454 */     Method localMethod2 = FunctionObject.findSingleMethod(arrayOfMethod, str2);
/* 1455 */     if (localMethod2 == null)
/* 1456 */       paramInt |= 1;
/* 1457 */     defineProperty(paramString, null, localMethod1, localMethod2 == null ? null : localMethod2, paramInt);
/*      */   }
/*      */ 
/*      */   public void defineProperty(String paramString, Object paramObject, Method paramMethod1, Method paramMethod2, int paramInt)
/*      */   {
/* 1505 */     MemberBox localMemberBox1 = null;
/*      */     Object localObject;
/*      */     Class[] arrayOfClass;
/* 1506 */     if (paramMethod1 != null) {
/* 1507 */       localMemberBox1 = new MemberBox(paramMethod1);
/*      */       int i;
/* 1510 */       if (!Modifier.isStatic(paramMethod1.getModifiers())) {
/* 1511 */         i = paramObject != null ? 1 : 0;
/* 1512 */         localMemberBox1.delegateTo = paramObject;
/*      */       } else {
/* 1514 */         i = 1;
/*      */ 
/* 1517 */         localMemberBox1.delegateTo = Void.TYPE;
/*      */       }
/*      */ 
/* 1520 */       String str = null;
/* 1521 */       localObject = paramMethod1.getParameterTypes();
/* 1522 */       if (localObject.length == 0) {
/* 1523 */         if (i != 0)
/* 1524 */           str = "msg.obj.getter.parms";
/*      */       }
/* 1526 */       else if (localObject.length == 1) {
/* 1527 */         arrayOfClass = localObject[0];
/*      */ 
/* 1529 */         if ((arrayOfClass != ScriptRuntime.ScriptableClass) && (arrayOfClass != ScriptRuntime.ScriptableObjectClass))
/*      */         {
/* 1532 */           str = "msg.bad.getter.parms";
/* 1533 */         } else if (i == 0)
/* 1534 */           str = "msg.bad.getter.parms";
/*      */       }
/*      */       else {
/* 1537 */         str = "msg.bad.getter.parms";
/*      */       }
/* 1539 */       if (str != null) {
/* 1540 */         throw Context.reportRuntimeError1(str, paramMethod1.toString());
/*      */       }
/*      */     }
/*      */ 
/* 1544 */     MemberBox localMemberBox2 = null;
/* 1545 */     if (paramMethod2 != null) {
/* 1546 */       if (paramMethod2.getReturnType() != Void.TYPE) {
/* 1547 */         throw Context.reportRuntimeError1("msg.setter.return", paramMethod2.toString());
/*      */       }
/*      */ 
/* 1550 */       localMemberBox2 = new MemberBox(paramMethod2);
/*      */       int j;
/* 1553 */       if (!Modifier.isStatic(paramMethod2.getModifiers())) {
/* 1554 */         j = paramObject != null ? 1 : 0;
/* 1555 */         localMemberBox2.delegateTo = paramObject;
/*      */       } else {
/* 1557 */         j = 1;
/*      */ 
/* 1560 */         localMemberBox2.delegateTo = Void.TYPE;
/*      */       }
/*      */ 
/* 1563 */       localObject = null;
/* 1564 */       arrayOfClass = paramMethod2.getParameterTypes();
/* 1565 */       if (arrayOfClass.length == 1) {
/* 1566 */         if (j != 0)
/* 1567 */           localObject = "msg.setter2.expected";
/*      */       }
/* 1569 */       else if (arrayOfClass.length == 2) {
/* 1570 */         Class localClass = arrayOfClass[0];
/*      */ 
/* 1572 */         if ((localClass != ScriptRuntime.ScriptableClass) && (localClass != ScriptRuntime.ScriptableObjectClass))
/*      */         {
/* 1575 */           localObject = "msg.setter2.parms";
/* 1576 */         } else if (j == 0)
/* 1577 */           localObject = "msg.setter1.parms";
/*      */       }
/*      */       else {
/* 1580 */         localObject = "msg.setter.parms";
/*      */       }
/* 1582 */       if (localObject != null) {
/* 1583 */         throw Context.reportRuntimeError1((String)localObject, paramMethod2.toString());
/*      */       }
/*      */     }
/*      */ 
/* 1587 */     GetterSlot localGetterSlot = (GetterSlot)getSlot(paramString, 0, 4);
/*      */ 
/* 1589 */     localGetterSlot.setAttributes(paramInt);
/* 1590 */     localGetterSlot.getter = localMemberBox1;
/* 1591 */     localGetterSlot.setter = localMemberBox2;
/*      */   }
/*      */ 
/*      */   public void defineOwnProperties(Context paramContext, ScriptableObject paramScriptableObject) {
/* 1595 */     Object[] arrayOfObject1 = paramScriptableObject.getIds();
/*      */     Object localObject1;
/*      */     String str;
/*      */     Object localObject2;
/* 1596 */     for (localObject1 : arrayOfObject1) {
/* 1597 */       str = ScriptRuntime.toString(localObject1);
/* 1598 */       localObject2 = paramScriptableObject.get(localObject1);
/* 1599 */       ScriptableObject localScriptableObject = ensureScriptableObject(localObject2);
/* 1600 */       checkValidPropertyDefinition(getSlot(str, 0, 1), localScriptableObject);
/*      */     }
/* 1602 */     for (localObject1 : arrayOfObject1) {
/* 1603 */       str = ScriptRuntime.toString(localObject1);
/* 1604 */       localObject2 = (ScriptableObject)paramScriptableObject.get(localObject1);
/* 1605 */       defineOwnProperty(paramContext, str, (ScriptableObject)localObject2, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void defineOwnProperty(Context paramContext, Object paramObject, ScriptableObject paramScriptableObject)
/*      */   {
/* 1619 */     defineOwnProperty(paramContext, paramObject, paramScriptableObject, true);
/*      */   }
/*      */ 
/*      */   private void defineOwnProperty(Context paramContext, Object paramObject, ScriptableObject paramScriptableObject, boolean paramBoolean) {
/* 1623 */     Slot localSlot = getSlot(paramContext, paramObject, 1);
/*      */ 
/* 1625 */     if (paramBoolean)
/* 1626 */       checkValidPropertyDefinition(localSlot, paramScriptableObject);
/*      */     int i;
/* 1629 */     if (localSlot == null) {
/* 1630 */       localSlot = getSlot(paramContext, paramObject, 2);
/* 1631 */       i = applyDescriptorToAttributeBitset(7, paramScriptableObject);
/*      */     } else {
/* 1633 */       i = applyDescriptorToAttributeBitset(localSlot.getAttributes(), paramScriptableObject);
/*      */     }
/*      */ 
/* 1636 */     defineOwnProperty(paramContext, localSlot, paramScriptableObject, i);
/*      */   }
/*      */ 
/*      */   private void defineOwnProperty(Context paramContext, Slot paramSlot, ScriptableObject paramScriptableObject, int paramInt) {
/* 1640 */     String str = paramSlot.name;
/* 1641 */     int i = paramSlot.indexOrHash;
/*      */     Object localObject1;
/* 1643 */     if (isAccessorDescriptor(paramScriptableObject)) {
/* 1644 */       if (!(paramSlot instanceof GetterSlot)) {
/* 1645 */         paramSlot = getSlot(paramContext, str != null ? str : Integer.valueOf(i), 4);
/*      */       }
/*      */ 
/* 1648 */       localObject1 = (GetterSlot)paramSlot;
/*      */ 
/* 1650 */       Object localObject2 = getProperty(paramScriptableObject, "get");
/* 1651 */       if (localObject2 != NOT_FOUND) {
/* 1652 */         ((GetterSlot)localObject1).getter = localObject2;
/*      */       }
/* 1654 */       Object localObject3 = getProperty(paramScriptableObject, "set");
/* 1655 */       if (localObject3 != NOT_FOUND) {
/* 1656 */         ((GetterSlot)localObject1).setter = localObject3;
/*      */       }
/*      */ 
/* 1659 */       ((GetterSlot)localObject1).value = Undefined.instance;
/* 1660 */       ((GetterSlot)localObject1).setAttributes(paramInt);
/*      */     } else {
/* 1662 */       if (((paramSlot instanceof GetterSlot)) && (isDataDescriptor(paramScriptableObject))) {
/* 1663 */         paramSlot = getSlot(paramContext, str != null ? str : Integer.valueOf(i), 6);
/*      */       }
/*      */ 
/* 1666 */       localObject1 = getProperty(paramScriptableObject, "value");
/* 1667 */       if (localObject1 != NOT_FOUND) {
/* 1668 */         paramSlot.value = localObject1;
/*      */       }
/* 1670 */       paramSlot.setAttributes(paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void checkValidPropertyDefinition(Slot paramSlot, ScriptableObject paramScriptableObject) {
/* 1675 */     Object localObject1 = getProperty(paramScriptableObject, "get");
/* 1676 */     if ((localObject1 != NOT_FOUND) && (localObject1 != Undefined.instance) && (!(localObject1 instanceof Callable))) {
/* 1677 */       throw ScriptRuntime.notFunctionError(localObject1);
/*      */     }
/* 1679 */     Object localObject2 = getProperty(paramScriptableObject, "set");
/* 1680 */     if ((localObject2 != NOT_FOUND) && (localObject2 != Undefined.instance) && (!(localObject2 instanceof Callable))) {
/* 1681 */       throw ScriptRuntime.notFunctionError(localObject2);
/*      */     }
/* 1683 */     if ((isDataDescriptor(paramScriptableObject)) && (isAccessorDescriptor(paramScriptableObject))) {
/* 1684 */       throw ScriptRuntime.typeError0("msg.both.data.and.accessor.desc");
/*      */     }
/*      */ 
/* 1687 */     if (paramSlot == null) {
/* 1688 */       if (!isExtensible()) throw ScriptRuntime.typeError("msg.not.extensible"); 
/*      */     }
/* 1690 */     else { String str = paramSlot.name;
/* 1691 */       ScriptableObject localScriptableObject = getOwnPropertyDescriptor(Context.getContext(), str);
/* 1692 */       if (isFalse(localScriptableObject.get("configurable", localScriptableObject))) {
/* 1693 */         if (isTrue(getProperty(paramScriptableObject, "configurable")))
/* 1694 */           throw ScriptRuntime.typeError1("msg.change.configurable.false.to.true", str);
/* 1695 */         if (isTrue(localScriptableObject.get("enumerable", localScriptableObject)) != isTrue(getProperty(paramScriptableObject, "enumerable"))) {
/* 1696 */           throw ScriptRuntime.typeError1("msg.change.enumerable.with.configurable.false", str);
/*      */         }
/* 1698 */         if (!isGenericDescriptor(paramScriptableObject))
/*      */         {
/* 1700 */           if ((isDataDescriptor(paramScriptableObject)) && (isDataDescriptor(localScriptableObject))) {
/* 1701 */             if (isFalse(localScriptableObject.get("writable", localScriptableObject))) {
/* 1702 */               if (isTrue(getProperty(paramScriptableObject, "writable"))) {
/* 1703 */                 throw ScriptRuntime.typeError1("msg.change.writable.false.to.true.with.configurable.false", str);
/*      */               }
/* 1705 */               if (changes(localScriptableObject.get("value", localScriptableObject), getProperty(paramScriptableObject, "value")))
/* 1706 */                 throw ScriptRuntime.typeError1("msg.change.value.with.writable.false", str);
/*      */             }
/* 1708 */           } else if ((isAccessorDescriptor(paramScriptableObject)) && (isAccessorDescriptor(localScriptableObject))) {
/* 1709 */             if (changes(localScriptableObject.get("set", localScriptableObject), localObject2)) {
/* 1710 */               throw ScriptRuntime.typeError1("msg.change.setter.with.configurable.false", str);
/*      */             }
/* 1712 */             if (changes(localScriptableObject.get("get", localScriptableObject), localObject1))
/* 1713 */               throw ScriptRuntime.typeError1("msg.change.getter.with.configurable.false", str);
/*      */           } else {
/* 1715 */             if (isDataDescriptor(localScriptableObject)) {
/* 1716 */               throw ScriptRuntime.typeError1("msg.change.property.data.to.accessor.with.configurable.false", str);
/*      */             }
/* 1718 */             throw ScriptRuntime.typeError1("msg.change.property.accessor.to.data.with.configurable.false", str);
/*      */           }
/*      */         }
/*      */       } }
/*      */   }
/*      */ 
/*      */   protected static boolean isTrue(Object paramObject) {
/* 1725 */     return paramObject == NOT_FOUND ? false : ScriptRuntime.toBoolean(paramObject);
/*      */   }
/*      */ 
/*      */   protected static boolean isFalse(Object paramObject) {
/* 1729 */     return !isTrue(paramObject);
/*      */   }
/*      */ 
/*      */   private boolean changes(Object paramObject1, Object paramObject2) {
/* 1733 */     if (paramObject2 == NOT_FOUND) return false;
/* 1734 */     if (paramObject1 == NOT_FOUND) {
/* 1735 */       paramObject1 = Undefined.instance;
/*      */     }
/* 1737 */     return !ScriptRuntime.shallowEq(paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   protected int applyDescriptorToAttributeBitset(int paramInt, ScriptableObject paramScriptableObject)
/*      */   {
/* 1743 */     Object localObject1 = getProperty(paramScriptableObject, "enumerable");
/* 1744 */     if (localObject1 != NOT_FOUND) {
/* 1745 */       paramInt = ScriptRuntime.toBoolean(localObject1) ? paramInt & 0xFFFFFFFD : paramInt | 0x2;
/*      */     }
/*      */ 
/* 1749 */     Object localObject2 = getProperty(paramScriptableObject, "writable");
/* 1750 */     if (localObject2 != NOT_FOUND) {
/* 1751 */       paramInt = ScriptRuntime.toBoolean(localObject2) ? paramInt & 0xFFFFFFFE : paramInt | 0x1;
/*      */     }
/*      */ 
/* 1755 */     Object localObject3 = getProperty(paramScriptableObject, "configurable");
/* 1756 */     if (localObject3 != NOT_FOUND) {
/* 1757 */       paramInt = ScriptRuntime.toBoolean(localObject3) ? paramInt & 0xFFFFFFFB : paramInt | 0x4;
/*      */     }
/*      */ 
/* 1761 */     return paramInt;
/*      */   }
/*      */ 
/*      */   protected boolean isDataDescriptor(ScriptableObject paramScriptableObject) {
/* 1765 */     return (hasProperty(paramScriptableObject, "value")) || (hasProperty(paramScriptableObject, "writable"));
/*      */   }
/*      */ 
/*      */   protected boolean isAccessorDescriptor(ScriptableObject paramScriptableObject) {
/* 1769 */     return (hasProperty(paramScriptableObject, "get")) || (hasProperty(paramScriptableObject, "set"));
/*      */   }
/*      */ 
/*      */   protected boolean isGenericDescriptor(ScriptableObject paramScriptableObject) {
/* 1773 */     return (!isDataDescriptor(paramScriptableObject)) && (!isAccessorDescriptor(paramScriptableObject));
/*      */   }
/*      */ 
/*      */   protected Scriptable ensureScriptable(Object paramObject) {
/* 1777 */     if (!(paramObject instanceof ConstProperties))
/* 1778 */       throw ScriptRuntime.typeError1("msg.arg.not.object", ScriptRuntime.typeof(paramObject));
/* 1779 */     return (ConstProperties)paramObject;
/*      */   }
/*      */ 
/*      */   protected ScriptableObject ensureScriptableObject(Object paramObject) {
/* 1783 */     if (!(paramObject instanceof ScriptableObject))
/* 1784 */       throw ScriptRuntime.typeError1("msg.arg.not.object", ScriptRuntime.typeof(paramObject));
/* 1785 */     return (ScriptableObject)paramObject;
/*      */   }
/*      */ 
/*      */   public void defineFunctionProperties(String[] paramArrayOfString, Class<?> paramClass, int paramInt)
/*      */   {
/* 1804 */     Method[] arrayOfMethod = FunctionObject.getMethodList(paramClass);
/* 1805 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 1806 */       String str = paramArrayOfString[i];
/* 1807 */       Method localMethod = FunctionObject.findSingleMethod(arrayOfMethod, str);
/* 1808 */       if (localMethod == null) {
/* 1809 */         throw Context.reportRuntimeError2("msg.method.not.found", str, paramClass.getName());
/*      */       }
/*      */ 
/* 1812 */       FunctionObject localFunctionObject = new FunctionObject(str, localMethod, this);
/* 1813 */       defineProperty(str, localFunctionObject, paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static Scriptable getObjectPrototype(Scriptable paramScriptable)
/*      */   {
/* 1822 */     return getClassPrototype(paramScriptable, "Object");
/*      */   }
/*      */ 
/*      */   public static Scriptable getFunctionPrototype(Scriptable paramScriptable)
/*      */   {
/* 1830 */     return getClassPrototype(paramScriptable, "Function");
/*      */   }
/*      */ 
/*      */   public static Scriptable getArrayPrototype(Scriptable paramScriptable) {
/* 1834 */     return getClassPrototype(paramScriptable, "Array");
/*      */   }
/*      */ 
/*      */   public static Scriptable getClassPrototype(Scriptable paramScriptable, String paramString)
/*      */   {
/* 1855 */     paramScriptable = getTopLevelScope(paramScriptable);
/* 1856 */     Object localObject1 = getProperty(paramScriptable, paramString);
/*      */     Object localObject2;
/* 1858 */     if ((localObject1 instanceof BaseFunction)) {
/* 1859 */       localObject2 = ((BaseFunction)localObject1).getPrototypeProperty();
/* 1860 */     } else if ((localObject1 instanceof ConstProperties)) {
/* 1861 */       Scriptable localScriptable = (ConstProperties)localObject1;
/* 1862 */       localObject2 = localScriptable.get("prototype", localScriptable);
/*      */     } else {
/* 1864 */       return null;
/*      */     }
/* 1866 */     if ((localObject2 instanceof ConstProperties)) {
/* 1867 */       return (ConstProperties)localObject2;
/*      */     }
/* 1869 */     return null;
/*      */   }
/*      */ 
/*      */   public static Scriptable getTopLevelScope(Scriptable paramScriptable)
/*      */   {
/*      */     while (true)
/*      */     {
/* 1884 */       Scriptable localScriptable = paramScriptable.getParentScope();
/* 1885 */       if (localScriptable == null) {
/* 1886 */         return paramScriptable;
/*      */       }
/* 1888 */       paramScriptable = localScriptable;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isExtensible() {
/* 1893 */     return this.isExtensible;
/*      */   }
/*      */ 
/*      */   public void preventExtensions() {
/* 1897 */     this.isExtensible = false;
/*      */   }
/*      */ 
/*      */   public synchronized void sealObject()
/*      */   {
/* 1910 */     if (this.count >= 0)
/*      */     {
/* 1912 */       Slot localSlot = this.firstAdded;
/* 1913 */       while (localSlot != null) {
/* 1914 */         if ((localSlot.value instanceof LazilyLoadedCtor)) {
/* 1915 */           LazilyLoadedCtor localLazilyLoadedCtor = (LazilyLoadedCtor)localSlot.value;
/*      */           try {
/* 1917 */             localLazilyLoadedCtor.init();
/*      */           } finally {
/* 1919 */             localSlot.value = localLazilyLoadedCtor.getValue();
/*      */           }
/*      */         }
/* 1922 */         localSlot = localSlot.orderedNext;
/*      */       }
/* 1924 */       this.count ^= -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final boolean isSealed()
/*      */   {
/* 1936 */     return this.count < 0;
/*      */   }
/*      */ 
/*      */   private void checkNotSealed(String paramString, int paramInt)
/*      */   {
/* 1941 */     if (!isSealed()) {
/* 1942 */       return;
/*      */     }
/* 1944 */     String str = paramString != null ? paramString : Integer.toString(paramInt);
/* 1945 */     throw Context.reportRuntimeError1("msg.modify.sealed", str);
/*      */   }
/*      */ 
/*      */   public static Object getProperty(Scriptable paramScriptable, String paramString)
/*      */   {
/* 1962 */     Scriptable localScriptable = paramScriptable;
/*      */     Object localObject;
/*      */     do
/*      */     {
/* 1965 */       localObject = paramScriptable.get(paramString, localScriptable);
/* 1966 */       if (localObject != ConstProperties.NOT_FOUND)
/*      */         break;
/* 1968 */       paramScriptable = paramScriptable.getPrototype();
/* 1969 */     }while (paramScriptable != null);
/* 1970 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static <T> T getTypedProperty(Scriptable paramScriptable, int paramInt, Class<T> paramClass)
/*      */   {
/* 1993 */     Object localObject = getProperty(paramScriptable, paramInt);
/* 1994 */     if (localObject == ConstProperties.NOT_FOUND) {
/* 1995 */       localObject = null;
/*      */     }
/* 1997 */     return paramClass.cast(Context.jsToJava(localObject, paramClass));
/*      */   }
/*      */ 
/*      */   public static Object getProperty(Scriptable paramScriptable, int paramInt)
/*      */   {
/* 2017 */     Scriptable localScriptable = paramScriptable;
/*      */     Object localObject;
/*      */     do
/*      */     {
/* 2020 */       localObject = paramScriptable.get(paramInt, localScriptable);
/* 2021 */       if (localObject != ConstProperties.NOT_FOUND)
/*      */         break;
/* 2023 */       paramScriptable = paramScriptable.getPrototype();
/* 2024 */     }while (paramScriptable != null);
/* 2025 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static <T> T getTypedProperty(Scriptable paramScriptable, String paramString, Class<T> paramClass)
/*      */   {
/* 2045 */     Object localObject = getProperty(paramScriptable, paramString);
/* 2046 */     if (localObject == ConstProperties.NOT_FOUND) {
/* 2047 */       localObject = null;
/*      */     }
/* 2049 */     return paramClass.cast(Context.jsToJava(localObject, paramClass));
/*      */   }
/*      */ 
/*      */   public static boolean hasProperty(Scriptable paramScriptable, String paramString)
/*      */   {
/* 2065 */     return null != getBase(paramScriptable, paramString);
/*      */   }
/*      */ 
/*      */   public static void redefineProperty(Scriptable paramScriptable, String paramString, boolean paramBoolean)
/*      */   {
/* 2080 */     Scriptable localScriptable = getBase(paramScriptable, paramString);
/* 2081 */     if (localScriptable == null)
/* 2082 */       return;
/* 2083 */     if ((localScriptable instanceof ConstProperties)) {
/* 2084 */       ConstProperties localConstProperties = (ConstProperties)localScriptable;
/*      */ 
/* 2086 */       if (localConstProperties.isConst(paramString))
/* 2087 */         throw Context.reportRuntimeError1("msg.const.redecl", paramString);
/*      */     }
/* 2089 */     if (paramBoolean)
/* 2090 */       throw Context.reportRuntimeError1("msg.var.redecl", paramString);
/*      */   }
/*      */ 
/*      */   public static boolean hasProperty(Scriptable paramScriptable, int paramInt)
/*      */   {
/* 2105 */     return null != getBase(paramScriptable, paramInt);
/*      */   }
/*      */ 
/*      */   public static void putProperty(Scriptable paramScriptable, String paramString, Object paramObject)
/*      */   {
/* 2125 */     Scriptable localScriptable = getBase(paramScriptable, paramString);
/* 2126 */     if (localScriptable == null)
/* 2127 */       localScriptable = paramScriptable;
/* 2128 */     localScriptable.put(paramString, paramScriptable, paramObject);
/*      */   }
/*      */ 
/*      */   public static void putConstProperty(Scriptable paramScriptable, String paramString, Object paramObject)
/*      */   {
/* 2148 */     Scriptable localScriptable = getBase(paramScriptable, paramString);
/* 2149 */     if (localScriptable == null)
/* 2150 */       localScriptable = paramScriptable;
/* 2151 */     if ((localScriptable instanceof ConstProperties))
/* 2152 */       ((ConstProperties)localScriptable).putConst(paramString, paramScriptable, paramObject);
/*      */   }
/*      */ 
/*      */   public static void putProperty(Scriptable paramScriptable, int paramInt, Object paramObject)
/*      */   {
/* 2172 */     Scriptable localScriptable = getBase(paramScriptable, paramInt);
/* 2173 */     if (localScriptable == null)
/* 2174 */       localScriptable = paramScriptable;
/* 2175 */     localScriptable.put(paramInt, paramScriptable, paramObject);
/*      */   }
/*      */ 
/*      */   public static boolean deleteProperty(Scriptable paramScriptable, String paramString)
/*      */   {
/* 2191 */     Scriptable localScriptable = getBase(paramScriptable, paramString);
/* 2192 */     if (localScriptable == null)
/* 2193 */       return true;
/* 2194 */     localScriptable.delete(paramString);
/* 2195 */     return !localScriptable.has(paramString, paramScriptable);
/*      */   }
/*      */ 
/*      */   public static boolean deleteProperty(Scriptable paramScriptable, int paramInt)
/*      */   {
/* 2211 */     Scriptable localScriptable = getBase(paramScriptable, paramInt);
/* 2212 */     if (localScriptable == null)
/* 2213 */       return true;
/* 2214 */     localScriptable.delete(paramInt);
/* 2215 */     return !localScriptable.has(paramInt, paramScriptable);
/*      */   }
/*      */ 
/*      */   public static Object[] getPropertyIds(Scriptable paramScriptable)
/*      */   {
/* 2229 */     if (paramScriptable == null) {
/* 2230 */       return ScriptRuntime.emptyArgs;
/*      */     }
/* 2232 */     Object localObject = paramScriptable.getIds();
/* 2233 */     ObjToIntMap localObjToIntMap = null;
/*      */     while (true) {
/* 2235 */       paramScriptable = paramScriptable.getPrototype();
/* 2236 */       if (paramScriptable == null) {
/*      */         break;
/*      */       }
/* 2239 */       Object[] arrayOfObject = paramScriptable.getIds();
/* 2240 */       if (arrayOfObject.length != 0)
/*      */       {
/*      */         int i;
/* 2243 */         if (localObjToIntMap == null) {
/* 2244 */           if (localObject.length == 0) {
/* 2245 */             localObject = arrayOfObject;
/*      */           }
/*      */           else {
/* 2248 */             localObjToIntMap = new ObjToIntMap(localObject.length + arrayOfObject.length);
/* 2249 */             for (i = 0; i != localObject.length; i++) {
/* 2250 */               localObjToIntMap.intern(localObject[i]);
/*      */             }
/* 2252 */             localObject = null;
/*      */           }
/*      */         } else for (i = 0; i != arrayOfObject.length; i++)
/* 2255 */             localObjToIntMap.intern(arrayOfObject[i]);
/*      */       }
/*      */     }
/* 2258 */     if (localObjToIntMap != null) {
/* 2259 */       localObject = localObjToIntMap.getKeys();
/*      */     }
/* 2261 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static Object callMethod(Scriptable paramScriptable, String paramString, Object[] paramArrayOfObject)
/*      */   {
/* 2275 */     return callMethod(null, paramScriptable, paramString, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public static Object callMethod(Context paramContext, Scriptable paramScriptable, String paramString, Object[] paramArrayOfObject)
/*      */   {
/* 2289 */     Object localObject = getProperty(paramScriptable, paramString);
/* 2290 */     if (!(localObject instanceof Function)) {
/* 2291 */       throw ScriptRuntime.notFunctionError(paramScriptable, paramString);
/*      */     }
/* 2293 */     Function localFunction = (Function)localObject;
/*      */ 
/* 2301 */     Scriptable localScriptable = getTopLevelScope(paramScriptable);
/* 2302 */     if (paramContext != null) {
/* 2303 */       return localFunction.call(paramContext, localScriptable, paramScriptable, paramArrayOfObject);
/*      */     }
/* 2305 */     return Context.call(null, localFunction, localScriptable, paramScriptable, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   private static Scriptable getBase(Scriptable paramScriptable, String paramString)
/*      */   {
/*      */     do
/*      */     {
/* 2312 */       if (paramScriptable.has(paramString, paramScriptable))
/*      */         break;
/* 2314 */       paramScriptable = paramScriptable.getPrototype();
/* 2315 */     }while (paramScriptable != null);
/* 2316 */     return paramScriptable;
/*      */   }
/*      */ 
/*      */   private static Scriptable getBase(Scriptable paramScriptable, int paramInt)
/*      */   {
/*      */     do {
/* 2322 */       if (paramScriptable.has(paramInt, paramScriptable))
/*      */         break;
/* 2324 */       paramScriptable = paramScriptable.getPrototype();
/* 2325 */     }while (paramScriptable != null);
/* 2326 */     return paramScriptable;
/*      */   }
/*      */ 
/*      */   public final Object getAssociatedValue(Object paramObject)
/*      */   {
/* 2336 */     Map localMap = this.associatedValues;
/* 2337 */     if (localMap == null)
/* 2338 */       return null;
/* 2339 */     return localMap.get(paramObject);
/*      */   }
/*      */ 
/*      */   public static Object getTopScopeValue(Scriptable paramScriptable, Object paramObject)
/*      */   {
/* 2355 */     paramScriptable = getTopLevelScope(paramScriptable);
/*      */     do {
/* 2357 */       if ((paramScriptable instanceof ScriptableObject)) {
/* 2358 */         ScriptableObject localScriptableObject = (ScriptableObject)paramScriptable;
/* 2359 */         Object localObject = localScriptableObject.getAssociatedValue(paramObject);
/* 2360 */         if (localObject != null) {
/* 2361 */           return localObject;
/*      */         }
/*      */       }
/* 2364 */       paramScriptable = paramScriptable.getPrototype();
/* 2365 */     }while (paramScriptable != null);
/* 2366 */     return null;
/*      */   }
/*      */ 
/*      */   public final synchronized Object associateValue(Object paramObject1, Object paramObject2)
/*      */   {
/* 2385 */     if (paramObject2 == null) throw new IllegalArgumentException();
/* 2386 */     Object localObject = this.associatedValues;
/* 2387 */     if (localObject == null) {
/* 2388 */       localObject = new HashMap();
/* 2389 */       this.associatedValues = ((Map)localObject);
/*      */     }
/* 2391 */     return Kit.initHash((Map)localObject, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   private Object getImpl(String paramString, int paramInt, Scriptable paramScriptable)
/*      */   {
/* 2396 */     Slot localSlot = getSlot(paramString, paramInt, 1);
/* 2397 */     if (localSlot == null) {
/* 2398 */       return ConstProperties.NOT_FOUND;
/*      */     }
/* 2400 */     if (!(localSlot instanceof GetterSlot)) {
/* 2401 */       return localSlot.value;
/*      */     }
/* 2403 */     Object localObject1 = ((GetterSlot)localSlot).getter;
/*      */     Object localObject3;
/* 2404 */     if (localObject1 != null) {
/* 2405 */       if ((localObject1 instanceof MemberBox)) {
/* 2406 */         localObject2 = (MemberBox)localObject1;
/*      */         Object[] arrayOfObject;
/* 2409 */         if (((MemberBox)localObject2).delegateTo == null) {
/* 2410 */           localObject3 = paramScriptable;
/* 2411 */           arrayOfObject = ScriptRuntime.emptyArgs;
/*      */         } else {
/* 2413 */           localObject3 = ((MemberBox)localObject2).delegateTo;
/* 2414 */           arrayOfObject = new Object[] { paramScriptable };
/*      */         }
/* 2416 */         return ((MemberBox)localObject2).invoke(localObject3, arrayOfObject);
/*      */       }
/* 2418 */       localObject2 = (Function)localObject1;
/* 2419 */       localObject3 = Context.getContext();
/* 2420 */       return ((Function)localObject2).call((Context)localObject3, ((Function)localObject2).getParentScope(), paramScriptable, ScriptRuntime.emptyArgs);
/*      */     }
/*      */ 
/* 2424 */     Object localObject2 = localSlot.value;
/* 2425 */     if ((localObject2 instanceof LazilyLoadedCtor)) {
/* 2426 */       localObject3 = (LazilyLoadedCtor)localObject2;
/*      */       try {
/* 2428 */         ((LazilyLoadedCtor)localObject3).init();
/*      */       } finally {
/* 2430 */         localObject2 = ((LazilyLoadedCtor)localObject3).getValue();
/* 2431 */         localSlot.value = localObject2;
/*      */       }
/*      */     }
/* 2434 */     return localObject2;
/*      */   }
/*      */ 
/*      */   private boolean putImpl(String paramString, int paramInt1, Scriptable paramScriptable, Object paramObject, int paramInt2)
/*      */   {
/*      */     Slot localSlot;
/* 2452 */     if (this != paramScriptable) {
/* 2453 */       localSlot = getSlot(paramString, paramInt1, 1);
/* 2454 */       if (localSlot == null)
/* 2455 */         return false;
/*      */     }
/* 2457 */     else if (!isExtensible()) {
/* 2458 */       localSlot = getSlot(paramString, paramInt1, 1);
/* 2459 */       if (localSlot == null)
/* 2460 */         return true;
/*      */     }
/*      */     else {
/* 2463 */       checkNotSealed(paramString, paramInt1);
/*      */ 
/* 2465 */       if (paramInt2 != 0) {
/* 2466 */         localSlot = getSlot(paramString, paramInt1, 5);
/* 2467 */         int i = localSlot.getAttributes();
/* 2468 */         if ((i & 0x1) == 0)
/* 2469 */           throw Context.reportRuntimeError1("msg.var.redecl", paramString);
/* 2470 */         if ((i & 0x8) != 0) {
/* 2471 */           localSlot.value = paramObject;
/*      */ 
/* 2473 */           if (paramInt2 != 8)
/* 2474 */             localSlot.setAttributes(i & 0xFFFFFFF7);
/*      */         }
/* 2476 */         return true;
/*      */       }
/* 2478 */       localSlot = getSlot(paramString, paramInt1, 2);
/*      */     }
/* 2480 */     if ((localSlot instanceof GetterSlot)) {
/* 2481 */       GetterSlot localGetterSlot = (GetterSlot)localSlot;
/* 2482 */       Object localObject1 = localGetterSlot.setter;
/* 2483 */       if (localObject1 == null) {
/* 2484 */         if (localGetterSlot.getter != null) {
/* 2485 */           if (Context.getContext().hasFeature(11))
/*      */           {
/* 2488 */             throw ScriptRuntime.typeError1("msg.set.prop.no.setter", paramString);
/*      */           }
/*      */ 
/* 2492 */           return true;
/*      */         }
/*      */       } else {
/* 2495 */         Context localContext = Context.getContext();
/*      */         Object localObject2;
/* 2496 */         if ((localObject1 instanceof MemberBox)) {
/* 2497 */           localObject2 = (MemberBox)localObject1;
/* 2498 */           Class[] arrayOfClass = ((MemberBox)localObject2).argTypes;
/*      */ 
/* 2501 */           Class localClass = arrayOfClass[(arrayOfClass.length - 1)];
/* 2502 */           int j = FunctionObject.getTypeTag(localClass);
/* 2503 */           Object localObject3 = FunctionObject.convertArg(localContext, paramScriptable, paramObject, j);
/*      */           Object localObject4;
/*      */           Object[] arrayOfObject;
/* 2507 */           if (((MemberBox)localObject2).delegateTo == null) {
/* 2508 */             localObject4 = paramScriptable;
/* 2509 */             arrayOfObject = new Object[] { localObject3 };
/*      */           } else {
/* 2511 */             localObject4 = ((MemberBox)localObject2).delegateTo;
/* 2512 */             arrayOfObject = new Object[] { paramScriptable, localObject3 };
/*      */           }
/* 2514 */           ((MemberBox)localObject2).invoke(localObject4, arrayOfObject);
/*      */         } else {
/* 2516 */           localObject2 = (Function)localObject1;
/* 2517 */           ((Function)localObject2).call(localContext, ((Function)localObject2).getParentScope(), paramScriptable, new Object[] { paramObject });
/*      */         }
/*      */ 
/* 2520 */         return true;
/*      */       }
/* 2522 */     } else if ((localSlot.getAttributes() & 0x1) != 0) {
/* 2523 */       return true;
/*      */     }
/* 2525 */     if (this == paramScriptable) {
/* 2526 */       localSlot.value = paramObject;
/* 2527 */       return true;
/*      */     }
/* 2529 */     return false;
/*      */   }
/*      */ 
/*      */   private Slot findAttributeSlot(String paramString, int paramInt1, int paramInt2)
/*      */   {
/* 2535 */     Slot localSlot = getSlot(paramString, paramInt1, paramInt2);
/* 2536 */     if (localSlot == null) {
/* 2537 */       String str = paramString != null ? paramString : Integer.toString(paramInt1);
/* 2538 */       throw Context.reportRuntimeError1("msg.prop.not.found", str);
/*      */     }
/* 2540 */     return localSlot;
/*      */   }
/*      */ 
/*      */   private Slot getSlot(String paramString, int paramInt1, int paramInt2)
/*      */   {
/* 2556 */     Slot localSlot = this.lastAccess;
/* 2557 */     if (paramString != null ? 
/* 2558 */       paramString == localSlot.name : 
/* 2563 */       (localSlot.name == null) && (paramInt1 == localSlot.indexOrHash))
/*      */     {
/* 2567 */       if (!localSlot.wasDeleted)
/*      */       {
/* 2570 */         if ((paramInt2 != 4) || ((localSlot instanceof GetterSlot)))
/*      */         {
/* 2574 */           if ((paramInt2 != 6) || (!(localSlot instanceof GetterSlot)))
/*      */           {
/* 2578 */             return localSlot;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2581 */     localSlot = accessSlot(paramString, paramInt1, paramInt2);
/* 2582 */     if (localSlot != null)
/*      */     {
/* 2584 */       this.lastAccess = localSlot;
/*      */     }
/* 2586 */     return localSlot;
/*      */   }
/*      */ 
/*      */   private Slot accessSlot(String paramString, int paramInt1, int paramInt2)
/*      */   {
/* 2591 */     int i = paramString != null ? paramString.hashCode() : paramInt1;
/*      */     int k;
/*      */     Object localObject1;
/*      */     Object localObject2;
/* 2593 */     if ((paramInt2 == 1) || (paramInt2 == 2) || (paramInt2 == 5) || (paramInt2 == 4) || (paramInt2 == 6))
/*      */     {
/* 2601 */       Slot[] arrayOfSlot = this.slots;
/* 2602 */       if (arrayOfSlot == null) {
/* 2603 */         if (paramInt2 == 1)
/* 2604 */           return null;
/*      */       } else {
/* 2606 */         int j = arrayOfSlot.length;
/* 2607 */         k = getSlotIndex(j, i);
/* 2608 */         Slot localSlot1 = arrayOfSlot[k];
/* 2609 */         while (localSlot1 != null) {
/* 2610 */           localObject1 = localSlot1.name;
/* 2611 */           if (localObject1 != null) {
/* 2612 */             if (localObject1 == paramString)
/*      */               break;
/* 2614 */             if ((paramString != null) && (i == localSlot1.indexOrHash) && 
/* 2615 */               (paramString.equals(localObject1)))
/*      */             {
/* 2619 */               localSlot1.name = paramString;
/* 2620 */               break;
/*      */             }
/*      */           } else {
/* 2623 */             if ((paramString == null) && (i == localSlot1.indexOrHash)) {
/*      */               break;
/*      */             }
/*      */           }
/* 2627 */           localSlot1 = localSlot1.next;
/*      */         }
/* 2629 */         if (paramInt2 == 1)
/* 2630 */           return localSlot1;
/* 2631 */         if (paramInt2 == 2) {
/* 2632 */           if (localSlot1 != null)
/* 2633 */             return localSlot1;
/* 2634 */         } else if (paramInt2 == 4) {
/* 2635 */           if ((localSlot1 instanceof GetterSlot))
/* 2636 */             return localSlot1;
/* 2637 */         } else if (paramInt2 == 5) {
/* 2638 */           if (localSlot1 != null)
/* 2639 */             return localSlot1;
/* 2640 */         } else if ((paramInt2 == 6) && 
/* 2641 */           (!(localSlot1 instanceof GetterSlot))) {
/* 2642 */           return localSlot1;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2649 */       synchronized (this)
/*      */       {
/* 2651 */         arrayOfSlot = this.slots;
/*      */ 
/* 2653 */         if (this.count == 0)
/*      */         {
/* 2655 */           arrayOfSlot = new Slot[5];
/* 2656 */           this.slots = arrayOfSlot;
/* 2657 */           k = getSlotIndex(arrayOfSlot.length, i);
/*      */         } else {
/* 2659 */           int m = arrayOfSlot.length;
/* 2660 */           k = getSlotIndex(m, i);
/* 2661 */           localObject1 = arrayOfSlot[k];
/* 2662 */           localObject2 = localObject1;
/* 2663 */           while ((localObject2 != null) && (
/* 2664 */             (((Slot)localObject2).indexOrHash != i) || ((((Slot)localObject2).name != paramString) && ((paramString == null) || (!paramString.equals(((Slot)localObject2).name))))))
/*      */           {
/* 2670 */             localObject1 = localObject2;
/* 2671 */             localObject2 = ((Slot)localObject2).next;
/*      */           }
/*      */ 
/* 2674 */           if (localObject2 != null)
/*      */           {
/*      */             Object localObject3;
/* 2685 */             if ((paramInt2 == 4) && (!(localObject2 instanceof GetterSlot))) {
/* 2686 */               localObject3 = new GetterSlot(paramString, i, ((Slot)localObject2).getAttributes());
/* 2687 */             } else if ((paramInt2 == 6) && ((localObject2 instanceof GetterSlot))) {
/* 2688 */               localObject3 = new Slot(paramString, i, ((Slot)localObject2).getAttributes()); } else {
/* 2689 */               if (paramInt2 == 5) {
/* 2690 */                 return null;
/*      */               }
/* 2692 */               return localObject2;
/*      */             }
/*      */ 
/* 2695 */             ((Slot)localObject3).value = ((Slot)localObject2).value;
/* 2696 */             ((Slot)localObject3).next = ((Slot)localObject2).next;
/*      */ 
/* 2698 */             if (this.lastAdded != null)
/* 2699 */               this.lastAdded.orderedNext = ((Slot)localObject3);
/* 2700 */             if (this.firstAdded == null)
/* 2701 */               this.firstAdded = ((Slot)localObject3);
/* 2702 */             this.lastAdded = ((Slot)localObject3);
/*      */ 
/* 2704 */             if (localObject1 == localObject2)
/* 2705 */               arrayOfSlot[k] = localObject3;
/*      */             else {
/* 2707 */               ((Slot)localObject1).next = ((Slot)localObject3);
/*      */             }
/*      */ 
/* 2710 */             ((Slot)localObject2).wasDeleted = true;
/* 2711 */             ((Slot)localObject2).value = null;
/* 2712 */             ((Slot)localObject2).name = null;
/* 2713 */             if (localObject2 == this.lastAccess) {
/* 2714 */               this.lastAccess = REMOVED;
/*      */             }
/* 2716 */             return localObject3;
/*      */           }
/*      */ 
/* 2719 */           if (4 * (this.count + 1) > 3 * arrayOfSlot.length) {
/* 2720 */             arrayOfSlot = new Slot[arrayOfSlot.length * 2 + 1];
/* 2721 */             copyTable(this.slots, arrayOfSlot, this.count);
/* 2722 */             this.slots = arrayOfSlot;
/* 2723 */             k = getSlotIndex(arrayOfSlot.length, i);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2728 */         Slot localSlot2 = paramInt2 == 4 ? new GetterSlot(paramString, i, 0) : new Slot(paramString, i, 0);
/*      */ 
/* 2731 */         if (paramInt2 == 5)
/* 2732 */           localSlot2.setAttributes(13);
/* 2733 */         this.count += 1;
/*      */ 
/* 2735 */         if (this.lastAdded != null)
/* 2736 */           this.lastAdded.orderedNext = localSlot2;
/* 2737 */         if (this.firstAdded == null)
/* 2738 */           this.firstAdded = localSlot2;
/* 2739 */         this.lastAdded = localSlot2;
/*      */ 
/* 2741 */         addKnownAbsentSlot(arrayOfSlot, localSlot2, k);
/* 2742 */         return localSlot2;
/*      */       }
/*      */     }
/* 2745 */     if (paramInt2 == 3) {
/* 2746 */       synchronized (this) {
/* 2747 */         ??? = this.slots;
/* 2748 */         if (this.count != 0) {
/* 2749 */           k = this.slots.length;
/* 2750 */           int n = getSlotIndex(k, i);
/* 2751 */           localObject1 = ???[n];
/* 2752 */           localObject2 = localObject1;
/* 2753 */           while ((localObject2 != null) && (
/* 2754 */             (((Slot)localObject2).indexOrHash != i) || ((((Slot)localObject2).name != paramString) && ((paramString == null) || (!paramString.equals(((Slot)localObject2).name))))))
/*      */           {
/* 2760 */             localObject1 = localObject2;
/* 2761 */             localObject2 = ((Slot)localObject2).next;
/*      */           }
/* 2763 */           if ((localObject2 != null) && ((((Slot)localObject2).getAttributes() & 0x4) == 0)) {
/* 2764 */             this.count -= 1;
/*      */ 
/* 2766 */             if (localObject1 == localObject2)
/* 2767 */               ???[n] = ((Slot)localObject2).next;
/*      */             else {
/* 2769 */               ((Slot)localObject1).next = ((Slot)localObject2).next;
/*      */             }
/*      */ 
/* 2774 */             ((Slot)localObject2).wasDeleted = true;
/* 2775 */             ((Slot)localObject2).value = null;
/* 2776 */             ((Slot)localObject2).name = null;
/* 2777 */             if (localObject2 == this.lastAccess) {
/* 2778 */               this.lastAccess = REMOVED;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 2783 */       return null;
/*      */     }
/*      */ 
/* 2786 */     throw Kit.codeBug();
/*      */   }
/*      */ 
/*      */   private static int getSlotIndex(int paramInt1, int paramInt2)
/*      */   {
/* 2792 */     return (paramInt2 & 0x7FFFFFFF) % paramInt1;
/*      */   }
/*      */ 
/*      */   private static void copyTable(Slot[] paramArrayOfSlot1, Slot[] paramArrayOfSlot2, int paramInt)
/*      */   {
/* 2798 */     if (paramInt == 0) throw Kit.codeBug();
/*      */ 
/* 2800 */     int i = paramArrayOfSlot2.length;
/* 2801 */     int j = paramArrayOfSlot1.length;
/*      */     while (true) {
/* 2803 */       j--;
/* 2804 */       Object localObject = paramArrayOfSlot1[j];
/* 2805 */       while (localObject != null) {
/* 2806 */         int k = getSlotIndex(i, ((Slot)localObject).indexOrHash);
/* 2807 */         Slot localSlot = ((Slot)localObject).next;
/* 2808 */         addKnownAbsentSlot(paramArrayOfSlot2, (Slot)localObject, k);
/* 2809 */         ((Slot)localObject).next = null;
/* 2810 */         localObject = localSlot;
/* 2811 */         paramInt--; if (paramInt == 0)
/* 2812 */           return;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void addKnownAbsentSlot(Slot[] paramArrayOfSlot, Slot paramSlot, int paramInt)
/*      */   {
/* 2825 */     if (paramArrayOfSlot[paramInt] == null) {
/* 2826 */       paramArrayOfSlot[paramInt] = paramSlot;
/*      */     } else {
/* 2828 */       Slot localSlot = paramArrayOfSlot[paramInt];
/* 2829 */       while (localSlot.next != null) {
/* 2830 */         localSlot = localSlot.next;
/*      */       }
/* 2832 */       localSlot.next = paramSlot;
/*      */     }
/*      */   }
/*      */ 
/*      */   Object[] getIds(boolean paramBoolean) {
/* 2837 */     Slot[] arrayOfSlot = this.slots;
/* 2838 */     Object[] arrayOfObject = ScriptRuntime.emptyArgs;
/* 2839 */     if (arrayOfSlot == null)
/* 2840 */       return arrayOfObject;
/* 2841 */     int i = 0;
/* 2842 */     Object localObject1 = this.firstAdded;
/* 2843 */     while ((localObject1 != null) && (((Slot)localObject1).wasDeleted))
/*      */     {
/* 2846 */       localObject1 = ((Slot)localObject1).orderedNext;
/*      */     }
/* 2848 */     this.firstAdded = ((Slot)localObject1);
/* 2849 */     if (localObject1 != null) {
/*      */       while (true) {
/* 2851 */         if ((paramBoolean) || ((((Slot)localObject1).getAttributes() & 0x2) == 0)) {
/* 2852 */           if (i == 0)
/* 2853 */             arrayOfObject = new Object[arrayOfSlot.length];
/* 2854 */           arrayOfObject[(i++)] = (((Slot)localObject1).name != null ? ((Slot)localObject1).name : Integer.valueOf(((Slot)localObject1).indexOrHash));
/*      */         }
/*      */ 
/* 2858 */         localObject2 = ((Slot)localObject1).orderedNext;
/* 2859 */         while ((localObject2 != null) && (((Slot)localObject2).wasDeleted))
/*      */         {
/* 2861 */           localObject2 = ((Slot)localObject2).orderedNext;
/*      */         }
/* 2863 */         ((Slot)localObject1).orderedNext = ((Slot)localObject2);
/* 2864 */         if (localObject2 == null) {
/*      */           break;
/*      */         }
/* 2867 */         localObject1 = localObject2;
/*      */       }
/*      */     }
/* 2870 */     this.lastAdded = ((Slot)localObject1);
/* 2871 */     if (i == arrayOfObject.length)
/* 2872 */       return arrayOfObject;
/* 2873 */     Object localObject2 = new Object[i];
/* 2874 */     System.arraycopy(arrayOfObject, 0, localObject2, 0, i);
/* 2875 */     return localObject2;
/*      */   }
/*      */ 
/*      */   protected ScriptableObject getOwnPropertyDescriptor(Context paramContext, Object paramObject) {
/* 2879 */     Slot localSlot = getSlot(paramContext, paramObject, 1);
/* 2880 */     if (localSlot == null) return null;
/* 2881 */     Scriptable localScriptable = getParentScope();
/* 2882 */     return localSlot.getPropertyDescriptor(paramContext, localScriptable == null ? this : localScriptable);
/*      */   }
/*      */ 
/*      */   protected Slot getSlot(Context paramContext, Object paramObject, int paramInt)
/*      */   {
/* 2887 */     String str = ScriptRuntime.toStringIdOrIndex(paramContext, paramObject);
/*      */     Slot localSlot;
/* 2888 */     if (str == null) {
/* 2889 */       int i = ScriptRuntime.lastIndexResult(paramContext);
/* 2890 */       localSlot = getSlot(null, i, paramInt);
/*      */     } else {
/* 2892 */       localSlot = getSlot(str, 0, paramInt);
/*      */     }
/* 2894 */     return localSlot;
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/* 2901 */     return this.count;
/*      */   }
/*      */ 
/*      */   public boolean isEmpty() {
/* 2905 */     return this.count == 0;
/*      */   }
/*      */ 
/*      */   public Object get(Object paramObject)
/*      */   {
/* 2910 */     Object localObject = null;
/* 2911 */     if ((paramObject instanceof String))
/* 2912 */       localObject = get((String)paramObject, this);
/* 2913 */     else if ((paramObject instanceof Number)) {
/* 2914 */       localObject = get(((Number)paramObject).intValue(), this);
/*      */     }
/* 2916 */     if ((localObject == ConstProperties.NOT_FOUND) || (localObject == Undefined.instance))
/* 2917 */       return null;
/* 2918 */     if ((localObject instanceof Wrapper)) {
/* 2919 */       return ((Wrapper)localObject).unwrap();
/*      */     }
/* 2921 */     return localObject;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  148 */     REMOVED.wasDeleted = true;
/*      */   }
/*      */ 
/*      */   private static final class GetterSlot extends ScriptableObject.Slot
/*      */   {
/*      */     Object getter;
/*      */     Object setter;
/*      */ 
/*      */     GetterSlot(String paramString, int paramInt1, int paramInt2)
/*      */     {
/*  239 */       super(paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     ScriptableObject getPropertyDescriptor(Context paramContext, Scriptable paramScriptable)
/*      */     {
/*  244 */       ScriptableObject localScriptableObject = super.getPropertyDescriptor(paramContext, paramScriptable);
/*  245 */       localScriptableObject.delete("value");
/*  246 */       localScriptableObject.delete("writable");
/*  247 */       if (this.getter != null) localScriptableObject.defineProperty("get", this.getter, 0);
/*  248 */       if (this.setter != null) localScriptableObject.defineProperty("set", this.setter, 0);
/*  249 */       return localScriptableObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Slot
/*      */   {
/*      */     String name;
/*      */     int indexOrHash;
/*      */     private volatile short attributes;
/*      */     volatile transient boolean wasDeleted;
/*      */     volatile Object value;
/*      */     volatile transient Slot next;
/*      */     volatile transient Slot orderedNext;
/*      */ 
/*      */     Slot(String paramString, int paramInt1, int paramInt2)
/*      */     {
/*  186 */       this.name = paramString;
/*  187 */       this.indexOrHash = paramInt1;
/*  188 */       this.attributes = ((short)paramInt2);
/*      */     }
/*      */ 
/*      */     final int getAttributes()
/*      */     {
/*  193 */       return this.attributes;
/*      */     }
/*      */ 
/*      */     final synchronized void setAttributes(int paramInt)
/*      */     {
/*  198 */       ScriptableObject.checkValidAttributes(paramInt);
/*  199 */       this.attributes = ((short)paramInt);
/*      */     }
/*      */ 
/*      */     final void checkNotReadonly()
/*      */     {
/*  204 */       if ((this.attributes & 0x1) != 0) {
/*  205 */         String str = this.name != null ? this.name : Integer.toString(this.indexOrHash);
/*      */ 
/*  207 */         throw Context.reportRuntimeError1("msg.modify.readonly", str);
/*      */       }
/*      */     }
/*      */ 
/*      */     ScriptableObject getPropertyDescriptor(Context paramContext, Scriptable paramScriptable) {
/*  212 */       return ScriptableObject.buildDataDescriptor(paramScriptable, this.value == null ? Undefined.instance : this.value, this.attributes);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ScriptableObject
 * JD-Core Version:    0.6.2
 */