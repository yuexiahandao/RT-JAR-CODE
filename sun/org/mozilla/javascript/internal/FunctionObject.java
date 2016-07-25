/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ 
/*     */ public class FunctionObject extends BaseFunction
/*     */ {
/*     */   private static final short VARARGS_METHOD = -1;
/*     */   private static final short VARARGS_CTOR = -2;
/*     */   private static boolean sawSecurityException;
/*     */   public static final int JAVA_UNSUPPORTED_TYPE = 0;
/*     */   public static final int JAVA_STRING_TYPE = 1;
/*     */   public static final int JAVA_INT_TYPE = 2;
/*     */   public static final int JAVA_BOOLEAN_TYPE = 3;
/*     */   public static final int JAVA_DOUBLE_TYPE = 4;
/*     */   public static final int JAVA_SCRIPTABLE_TYPE = 5;
/*     */   public static final int JAVA_OBJECT_TYPE = 6;
/*     */   MemberBox member;
/*     */   private String functionName;
/*     */   private transient byte[] typeTags;
/*     */   private int parmsLength;
/*     */   private transient boolean hasVoidReturn;
/*     */   private transient int returnTypeTag;
/*     */   private boolean isStatic;
/*     */ 
/*     */   public FunctionObject(String paramString, Member paramMember, Scriptable paramScriptable)
/*     */   {
/* 118 */     if ((paramMember instanceof Constructor)) {
/* 119 */       this.member = new MemberBox((Constructor)paramMember);
/* 120 */       this.isStatic = true;
/*     */     } else {
/* 122 */       this.member = new MemberBox((Method)paramMember);
/* 123 */       this.isStatic = this.member.isStatic();
/*     */     }
/* 125 */     String str = this.member.getName();
/* 126 */     this.functionName = paramString;
/* 127 */     Class[] arrayOfClass = this.member.argTypes;
/* 128 */     int i = arrayOfClass.length;
/* 129 */     if ((i == 4) && ((arrayOfClass[1].isArray()) || (arrayOfClass[2].isArray())))
/*     */     {
/* 131 */       if (arrayOfClass[1].isArray()) {
/* 132 */         if ((!this.isStatic) || (arrayOfClass[0] != ScriptRuntime.ContextClass) || (arrayOfClass[1].getComponentType() != ScriptRuntime.ObjectClass) || (arrayOfClass[2] != ScriptRuntime.FunctionClass) || (arrayOfClass[3] != Boolean.TYPE))
/*     */         {
/* 138 */           throw Context.reportRuntimeError1("msg.varargs.ctor", str);
/*     */         }
/*     */ 
/* 141 */         this.parmsLength = -2;
/*     */       } else {
/* 143 */         if ((!this.isStatic) || (arrayOfClass[0] != ScriptRuntime.ContextClass) || (arrayOfClass[1] != ScriptRuntime.ScriptableClass) || (arrayOfClass[2].getComponentType() != ScriptRuntime.ObjectClass) || (arrayOfClass[3] != ScriptRuntime.FunctionClass))
/*     */         {
/* 149 */           throw Context.reportRuntimeError1("msg.varargs.fun", str);
/*     */         }
/*     */ 
/* 152 */         this.parmsLength = -1;
/*     */       }
/*     */     } else {
/* 155 */       this.parmsLength = i;
/* 156 */       if (i > 0) {
/* 157 */         this.typeTags = new byte[i];
/* 158 */         for (int j = 0; j != i; j++) {
/* 159 */           int k = getTypeTag(arrayOfClass[j]);
/* 160 */           if (k == 0) {
/* 161 */             throw Context.reportRuntimeError2("msg.bad.parms", arrayOfClass[j].getName(), str);
/*     */           }
/*     */ 
/* 164 */           this.typeTags[j] = ((byte)k);
/*     */         }
/*     */       }
/*     */     }
/*     */     Object localObject;
/* 169 */     if (this.member.isMethod()) {
/* 170 */       localObject = this.member.method();
/* 171 */       Class localClass = ((Method)localObject).getReturnType();
/* 172 */       if (localClass == Void.TYPE)
/* 173 */         this.hasVoidReturn = true;
/*     */       else
/* 175 */         this.returnTypeTag = getTypeTag(localClass);
/*     */     }
/*     */     else {
/* 178 */       localObject = this.member.getDeclaringClass();
/* 179 */       if (!ScriptRuntime.ScriptableClass.isAssignableFrom((Class)localObject)) {
/* 180 */         throw Context.reportRuntimeError1("msg.bad.ctor.return", ((Class)localObject).getName());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 185 */     ScriptRuntime.setFunctionProtoAndParent(this, paramScriptable);
/*     */   }
/*     */ 
/*     */   public static int getTypeTag(Class<?> paramClass)
/*     */   {
/* 195 */     if (paramClass == ScriptRuntime.StringClass)
/* 196 */       return 1;
/* 197 */     if ((paramClass == ScriptRuntime.IntegerClass) || (paramClass == Integer.TYPE))
/* 198 */       return 2;
/* 199 */     if ((paramClass == ScriptRuntime.BooleanClass) || (paramClass == Boolean.TYPE))
/* 200 */       return 3;
/* 201 */     if ((paramClass == ScriptRuntime.DoubleClass) || (paramClass == Double.TYPE))
/* 202 */       return 4;
/* 203 */     if (ScriptRuntime.ScriptableClass.isAssignableFrom(paramClass))
/* 204 */       return 5;
/* 205 */     if (paramClass == ScriptRuntime.ObjectClass) {
/* 206 */       return 6;
/*     */     }
/*     */ 
/* 211 */     return 0;
/*     */   }
/*     */ 
/*     */   public static Object convertArg(Context paramContext, Scriptable paramScriptable, Object paramObject, int paramInt)
/*     */   {
/* 217 */     switch (paramInt) {
/*     */     case 1:
/* 219 */       if ((paramObject instanceof String))
/* 220 */         return paramObject;
/* 221 */       return ScriptRuntime.toString(paramObject);
/*     */     case 2:
/* 223 */       if ((paramObject instanceof Integer))
/* 224 */         return paramObject;
/* 225 */       return Integer.valueOf(ScriptRuntime.toInt32(paramObject));
/*     */     case 3:
/* 227 */       if ((paramObject instanceof Boolean))
/* 228 */         return paramObject;
/* 229 */       return ScriptRuntime.toBoolean(paramObject) ? Boolean.TRUE : Boolean.FALSE;
/*     */     case 4:
/* 232 */       if ((paramObject instanceof Double))
/* 233 */         return paramObject;
/* 234 */       return new Double(ScriptRuntime.toNumber(paramObject));
/*     */     case 5:
/* 236 */       return ScriptRuntime.toObjectOrNull(paramContext, paramObject, paramScriptable);
/*     */     case 6:
/* 238 */       return paramObject;
/*     */     }
/* 240 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public int getArity()
/*     */   {
/* 251 */     return this.parmsLength < 0 ? 1 : this.parmsLength;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 259 */     return getArity();
/*     */   }
/*     */ 
/*     */   public String getFunctionName()
/*     */   {
/* 265 */     return this.functionName == null ? "" : this.functionName;
/*     */   }
/*     */ 
/*     */   public Member getMethodOrConstructor()
/*     */   {
/* 273 */     if (this.member.isMethod()) {
/* 274 */       return this.member.method();
/*     */     }
/* 276 */     return this.member.ctor();
/*     */   }
/*     */ 
/*     */   static Method findSingleMethod(Method[] paramArrayOfMethod, String paramString)
/*     */   {
/* 282 */     Object localObject = null;
/* 283 */     int i = 0; for (int j = paramArrayOfMethod.length; i != j; i++) {
/* 284 */       Method localMethod = paramArrayOfMethod[i];
/* 285 */       if ((localMethod != null) && (paramString.equals(localMethod.getName()))) {
/* 286 */         if (localObject != null) {
/* 287 */           throw Context.reportRuntimeError2("msg.no.overload", paramString, localMethod.getDeclaringClass().getName());
/*     */         }
/*     */ 
/* 291 */         localObject = localMethod;
/*     */       }
/*     */     }
/* 294 */     return localObject;
/*     */   }
/*     */ 
/*     */   static Method[] getMethodList(Class<?> paramClass)
/*     */   {
/* 306 */     Method[] arrayOfMethod1 = null;
/*     */     try
/*     */     {
/* 310 */       if (!sawSecurityException)
/* 311 */         arrayOfMethod1 = paramClass.getDeclaredMethods();
/*     */     }
/*     */     catch (SecurityException localSecurityException) {
/* 314 */       sawSecurityException = true;
/*     */     }
/* 316 */     if (arrayOfMethod1 == null) {
/* 317 */       arrayOfMethod1 = paramClass.getMethods();
/*     */     }
/* 319 */     int i = 0;
/* 320 */     for (int j = 0; j < arrayOfMethod1.length; j++) {
/* 321 */       if (sawSecurityException ? arrayOfMethod1[j].getDeclaringClass() != paramClass : !Modifier.isPublic(arrayOfMethod1[j].getModifiers()))
/*     */       {
/* 325 */         arrayOfMethod1[j] = null;
/*     */       }
/* 327 */       else i++;
/*     */     }
/*     */ 
/* 330 */     Method[] arrayOfMethod2 = new Method[i];
/* 331 */     int k = 0;
/* 332 */     for (int m = 0; m < arrayOfMethod1.length; m++) {
/* 333 */       if (arrayOfMethod1[m] != null)
/* 334 */         arrayOfMethod2[(k++)] = arrayOfMethod1[m];
/*     */     }
/* 336 */     return arrayOfMethod2;
/*     */   }
/*     */ 
/*     */   public void addAsConstructor(Scriptable paramScriptable1, Scriptable paramScriptable2)
/*     */   {
/* 357 */     initAsConstructor(paramScriptable1, paramScriptable2);
/* 358 */     defineProperty(paramScriptable1, paramScriptable2.getClassName(), this, 2);
/*     */   }
/*     */ 
/*     */   void initAsConstructor(Scriptable paramScriptable1, Scriptable paramScriptable2)
/*     */   {
/* 364 */     ScriptRuntime.setFunctionProtoAndParent(this, paramScriptable1);
/* 365 */     setImmunePrototypeProperty(paramScriptable2);
/*     */ 
/* 367 */     paramScriptable2.setParentScope(this);
/*     */ 
/* 369 */     defineProperty(paramScriptable2, "constructor", this, 7);
/*     */ 
/* 373 */     setParentScope(paramScriptable1);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public static Object convertArg(Context paramContext, Scriptable paramScriptable, Object paramObject, Class<?> paramClass)
/*     */   {
/* 384 */     int i = getTypeTag(paramClass);
/* 385 */     if (i == 0) {
/* 386 */       throw Context.reportRuntimeError1("msg.cant.convert", paramClass.getName());
/*     */     }
/*     */ 
/* 389 */     return convertArg(paramContext, paramScriptable, paramObject, i);
/*     */   }
/*     */ 
/*     */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 406 */     int i = 0;
/*     */     Object localObject1;
/*     */     Object localObject3;
/* 408 */     if (this.parmsLength < 0) {
/* 409 */       if (this.parmsLength == -1) {
/* 410 */         Object[] arrayOfObject = { paramContext, paramScriptable2, paramArrayOfObject, this };
/* 411 */         localObject1 = this.member.invoke(null, arrayOfObject);
/* 412 */         i = 1;
/*     */       } else {
/* 414 */         int j = paramScriptable2 == null ? 1 : 0;
/* 415 */         Boolean localBoolean = j != 0 ? Boolean.TRUE : Boolean.FALSE;
/* 416 */         localObject3 = new Object[] { paramContext, paramArrayOfObject, this, localBoolean };
/* 417 */         localObject1 = this.member.isCtor() ? this.member.newInstance((Object[])localObject3) : this.member.invoke(null, (Object[])localObject3);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       Object localObject2;
/*     */       int k;
/* 423 */       if (!this.isStatic) {
/* 424 */         localObject2 = this.member.getDeclaringClass();
/* 425 */         if (!((Class)localObject2).isInstance(paramScriptable2)) {
/* 426 */           k = 0;
/* 427 */           if (paramScriptable2 == paramScriptable1) {
/* 428 */             localObject3 = getParentScope();
/* 429 */             if (paramScriptable1 != localObject3)
/*     */             {
/* 432 */               k = ((Class)localObject2).isInstance(localObject3);
/* 433 */               if (k != 0) {
/* 434 */                 paramScriptable2 = (Scriptable)localObject3;
/*     */               }
/*     */             }
/*     */           }
/* 438 */           if (k == 0)
/*     */           {
/* 440 */             throw ScriptRuntime.typeError1("msg.incompat.call", this.functionName);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 447 */       if (this.parmsLength == paramArrayOfObject.length)
/*     */       {
/* 450 */         localObject2 = paramArrayOfObject;
/* 451 */         for (k = 0; k != this.parmsLength; k++) {
/* 452 */           localObject3 = paramArrayOfObject[k];
/* 453 */           Object localObject4 = convertArg(paramContext, paramScriptable1, localObject3, this.typeTags[k]);
/* 454 */           if (localObject3 != localObject4) {
/* 455 */             if (localObject2 == paramArrayOfObject) {
/* 456 */               localObject2 = (Object[])paramArrayOfObject.clone();
/*     */             }
/* 458 */             localObject2[k] = localObject4;
/*     */           }
/*     */         }
/* 461 */       } else if (this.parmsLength == 0) {
/* 462 */         localObject2 = ScriptRuntime.emptyArgs;
/*     */       } else {
/* 464 */         localObject2 = new Object[this.parmsLength];
/* 465 */         for (int m = 0; m != this.parmsLength; m++) {
/* 466 */           localObject3 = m < paramArrayOfObject.length ? paramArrayOfObject[m] : Undefined.instance;
/*     */ 
/* 469 */           localObject2[m] = convertArg(paramContext, paramScriptable1, localObject3, this.typeTags[m]);
/*     */         }
/*     */       }
/*     */ 
/* 473 */       if (this.member.isMethod()) {
/* 474 */         localObject1 = this.member.invoke(paramScriptable2, (Object[])localObject2);
/* 475 */         i = 1;
/*     */       } else {
/* 477 */         localObject1 = this.member.newInstance((Object[])localObject2);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 482 */     if (i != 0) {
/* 483 */       if (this.hasVoidReturn)
/* 484 */         localObject1 = Undefined.instance;
/* 485 */       else if (this.returnTypeTag == 0) {
/* 486 */         localObject1 = paramContext.getWrapFactory().wrap(paramContext, paramScriptable1, localObject1, null);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 494 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public Scriptable createObject(Context paramContext, Scriptable paramScriptable)
/*     */   {
/* 505 */     if ((this.member.isCtor()) || (this.parmsLength == -2))
/* 506 */       return null;
/*     */     Scriptable localScriptable;
/*     */     try
/*     */     {
/* 510 */       localScriptable = (Scriptable)this.member.getDeclaringClass().newInstance();
/*     */     } catch (Exception localException) {
/* 512 */       throw Context.throwAsScriptRuntimeEx(localException);
/*     */     }
/*     */ 
/* 515 */     localScriptable.setPrototype(getClassPrototype());
/* 516 */     localScriptable.setParentScope(getParentScope());
/* 517 */     return localScriptable;
/*     */   }
/*     */ 
/*     */   boolean isVarArgsMethod() {
/* 521 */     return this.parmsLength == -1;
/*     */   }
/*     */ 
/*     */   boolean isVarArgsConstructor() {
/* 525 */     return this.parmsLength == -2;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 531 */     paramObjectInputStream.defaultReadObject();
/*     */     Object localObject;
/* 532 */     if (this.parmsLength > 0) {
/* 533 */       localObject = this.member.argTypes;
/* 534 */       this.typeTags = new byte[this.parmsLength];
/* 535 */       for (int i = 0; i != this.parmsLength; i++) {
/* 536 */         this.typeTags[i] = ((byte)getTypeTag(localObject[i]));
/*     */       }
/*     */     }
/* 539 */     if (this.member.isMethod()) {
/* 540 */       localObject = this.member.method();
/* 541 */       Class localClass = ((Method)localObject).getReturnType();
/* 542 */       if (localClass == Void.TYPE)
/* 543 */         this.hasVoidReturn = true;
/*     */       else
/* 545 */         this.returnTypeTag = getTypeTag(localClass);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.FunctionObject
 * JD-Core Version:    0.6.2
 */