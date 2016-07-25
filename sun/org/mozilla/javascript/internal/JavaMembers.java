/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ class JavaMembers
/*     */ {
/*     */   private Class<?> cl;
/*     */   private Map<String, Object> members;
/*     */   private Map<String, FieldAndMethods> fieldAndMethods;
/*     */   private Map<String, Object> staticMembers;
/*     */   private Map<String, FieldAndMethods> staticFieldAndMethods;
/*     */   MemberBox[] ctors;
/*     */   private boolean includePrivate;
/*     */ 
/*     */   JavaMembers(Scriptable paramScriptable, Class<?> paramClass)
/*     */   {
/*  59 */     this(paramScriptable, paramClass, false);
/*     */   }
/*     */ 
/*     */   JavaMembers(Scriptable paramScriptable, Class<?> paramClass, boolean paramBoolean)
/*     */   {
/*     */     try {
/*  65 */       Context localContext = ContextFactory.getGlobal().enterContext();
/*  66 */       ClassShutter localClassShutter = localContext.getClassShutter();
/*  67 */       if ((localClassShutter != null) && (!localClassShutter.visibleToScripts(paramClass.getName()))) {
/*  68 */         throw Context.reportRuntimeError1("msg.access.prohibited", paramClass.getName());
/*     */       }
/*     */ 
/*  71 */       this.includePrivate = localContext.hasFeature(13);
/*     */ 
/*  73 */       this.members = new HashMap();
/*  74 */       this.staticMembers = new HashMap();
/*  75 */       this.cl = paramClass;
/*  76 */       reflect(paramScriptable, paramBoolean);
/*     */     } finally {
/*  78 */       Context.exit();
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean has(String paramString, boolean paramBoolean)
/*     */   {
/*  84 */     Map localMap = paramBoolean ? this.staticMembers : this.members;
/*  85 */     Object localObject = localMap.get(paramString);
/*  86 */     if (localObject != null) {
/*  87 */       return true;
/*     */     }
/*  89 */     return findExplicitFunction(paramString, paramBoolean) != null;
/*     */   }
/*     */ 
/*     */   Object get(Scriptable paramScriptable, String paramString, Object paramObject, boolean paramBoolean)
/*     */   {
/*  95 */     Map localMap = paramBoolean ? this.staticMembers : this.members;
/*  96 */     Object localObject1 = localMap.get(paramString);
/*  97 */     if ((!paramBoolean) && (localObject1 == null))
/*     */     {
/*  99 */       localObject1 = this.staticMembers.get(paramString);
/*     */     }
/* 101 */     if (localObject1 == null) {
/* 102 */       localObject1 = getExplicitFunction(paramScriptable, paramString, paramObject, paramBoolean);
/*     */ 
/* 104 */       if (localObject1 == null)
/* 105 */         return Scriptable.NOT_FOUND;
/*     */     }
/* 107 */     if ((localObject1 instanceof Scriptable)) {
/* 108 */       return localObject1;
/*     */     }
/* 110 */     Context localContext = Context.getContext();
/*     */     Object localObject2;
/*     */     Class localClass;
/*     */     try
/*     */     {
/*     */       Object localObject3;
/* 114 */       if ((localObject1 instanceof BeanProperty)) {
/* 115 */         localObject3 = (BeanProperty)localObject1;
/* 116 */         if (((BeanProperty)localObject3).getter == null)
/* 117 */           return Scriptable.NOT_FOUND;
/* 118 */         localObject2 = ((BeanProperty)localObject3).getter.invoke(paramObject, Context.emptyArgs);
/* 119 */         localClass = ((BeanProperty)localObject3).getter.method().getReturnType();
/*     */       } else {
/* 121 */         localObject3 = (Field)localObject1;
/* 122 */         localObject2 = ((Field)localObject3).get(paramBoolean ? null : paramObject);
/* 123 */         localClass = ((Field)localObject3).getType();
/*     */       }
/*     */     } catch (Exception localException) {
/* 126 */       throw Context.throwAsScriptRuntimeEx(localException);
/*     */     }
/*     */ 
/* 129 */     paramScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
/* 130 */     return localContext.getWrapFactory().wrap(localContext, paramScriptable, localObject2, localClass);
/*     */   }
/*     */ 
/*     */   void put(Scriptable paramScriptable, String paramString, Object paramObject1, Object paramObject2, boolean paramBoolean)
/*     */   {
/* 136 */     Map localMap = paramBoolean ? this.staticMembers : this.members;
/* 137 */     Object localObject1 = localMap.get(paramString);
/* 138 */     if ((!paramBoolean) && (localObject1 == null))
/*     */     {
/* 140 */       localObject1 = this.staticMembers.get(paramString);
/*     */     }
/* 142 */     if (localObject1 == null)
/* 143 */       throw reportMemberNotFound(paramString);
/*     */     Object localObject2;
/* 144 */     if ((localObject1 instanceof FieldAndMethods)) {
/* 145 */       localObject2 = (FieldAndMethods)localMap.get(paramString);
/* 146 */       localObject1 = ((FieldAndMethods)localObject2).field;
/*     */     }
/*     */     Object localObject3;
/* 150 */     if ((localObject1 instanceof BeanProperty)) {
/* 151 */       localObject2 = (BeanProperty)localObject1;
/* 152 */       if (((BeanProperty)localObject2).setter == null) {
/* 153 */         throw reportMemberNotFound(paramString);
/*     */       }
/*     */ 
/* 158 */       if ((((BeanProperty)localObject2).setters == null) || (paramObject2 == null)) {
/* 159 */         localObject3 = localObject2.setter.argTypes[0];
/* 160 */         Object[] arrayOfObject = { Context.jsToJava(paramObject2, (Class)localObject3) };
/*     */         try {
/* 162 */           ((BeanProperty)localObject2).setter.invoke(paramObject1, arrayOfObject);
/*     */         } catch (Exception localException) {
/* 164 */           throw Context.throwAsScriptRuntimeEx(localException);
/*     */         }
/*     */       } else {
/* 167 */         localObject3 = new Object[] { paramObject2 };
/* 168 */         ((BeanProperty)localObject2).setters.call(Context.getContext(), ScriptableObject.getTopLevelScope(paramScriptable), paramScriptable, (Object[])localObject3);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 174 */       if (!(localObject1 instanceof Field)) {
/* 175 */         localObject2 = localObject1 == null ? "msg.java.internal.private" : "msg.java.method.assign";
/*     */ 
/* 177 */         throw Context.reportRuntimeError1((String)localObject2, paramString);
/*     */       }
/* 179 */       localObject2 = (Field)localObject1;
/* 180 */       localObject3 = Context.jsToJava(paramObject2, ((Field)localObject2).getType());
/*     */       try {
/* 182 */         ((Field)localObject2).set(paramObject1, localObject3);
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/* 184 */         if ((((Field)localObject2).getModifiers() & 0x10) != 0)
/*     */         {
/* 186 */           return;
/*     */         }
/* 188 */         throw Context.throwAsScriptRuntimeEx(localIllegalAccessException);
/*     */       } catch (IllegalArgumentException localIllegalArgumentException) {
/* 190 */         throw Context.reportRuntimeError3("msg.java.internal.field.type", paramObject2.getClass().getName(), localObject2, paramObject1.getClass().getName());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   Object[] getIds(boolean paramBoolean)
/*     */   {
/* 200 */     Map localMap = paramBoolean ? this.staticMembers : this.members;
/* 201 */     return localMap.keySet().toArray(new Object[localMap.size()]);
/*     */   }
/*     */ 
/*     */   static String javaSignature(Class<?> paramClass)
/*     */   {
/* 206 */     if (!paramClass.isArray()) {
/* 207 */       return paramClass.getName();
/*     */     }
/* 209 */     int i = 0;
/*     */     do {
/* 211 */       i++;
/* 212 */       paramClass = paramClass.getComponentType();
/* 213 */     }while (paramClass.isArray());
/* 214 */     String str1 = paramClass.getName();
/* 215 */     String str2 = "[]";
/* 216 */     if (i == 1) {
/* 217 */       return str1.concat(str2);
/*     */     }
/* 219 */     int j = str1.length() + i * str2.length();
/* 220 */     StringBuffer localStringBuffer = new StringBuffer(j);
/* 221 */     localStringBuffer.append(str1);
/* 222 */     while (i != 0) {
/* 223 */       i--;
/* 224 */       localStringBuffer.append(str2);
/*     */     }
/* 226 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static String liveConnectSignature(Class<?>[] paramArrayOfClass)
/*     */   {
/* 233 */     int i = paramArrayOfClass.length;
/* 234 */     if (i == 0) return "()";
/* 235 */     StringBuffer localStringBuffer = new StringBuffer();
/* 236 */     localStringBuffer.append('(');
/* 237 */     for (int j = 0; j != i; j++) {
/* 238 */       if (j != 0) {
/* 239 */         localStringBuffer.append(',');
/*     */       }
/* 241 */       localStringBuffer.append(javaSignature(paramArrayOfClass[j]));
/*     */     }
/* 243 */     localStringBuffer.append(')');
/* 244 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private MemberBox findExplicitFunction(String paramString, boolean paramBoolean)
/*     */   {
/* 249 */     int i = paramString.indexOf('(');
/* 250 */     if (i < 0) return null;
/*     */ 
/* 252 */     Map localMap = paramBoolean ? this.staticMembers : this.members;
/* 253 */     MemberBox[] arrayOfMemberBox = null;
/* 254 */     int j = (paramBoolean) && (i == 0) ? 1 : 0;
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 256 */     if (j != 0)
/*     */     {
/* 258 */       arrayOfMemberBox = this.ctors;
/*     */     }
/*     */     else {
/* 261 */       String str = paramString.substring(0, i);
/* 262 */       localObject1 = localMap.get(str);
/* 263 */       if ((!paramBoolean) && (localObject1 == null))
/*     */       {
/* 265 */         localObject1 = this.staticMembers.get(str);
/*     */       }
/* 267 */       if ((localObject1 instanceof NativeJavaMethod)) {
/* 268 */         localObject2 = (NativeJavaMethod)localObject1;
/* 269 */         arrayOfMemberBox = ((NativeJavaMethod)localObject2).methods;
/*     */       }
/*     */     }
/*     */ 
/* 273 */     if (arrayOfMemberBox != null) {
/* 274 */       for (int k = 0; k < arrayOfMemberBox.length; k++) {
/* 275 */         localObject1 = arrayOfMemberBox[k].argTypes;
/* 276 */         localObject2 = liveConnectSignature((Class[])localObject1);
/* 277 */         if ((i + ((String)localObject2).length() == paramString.length()) && (paramString.regionMatches(i, (String)localObject2, 0, ((String)localObject2).length())))
/*     */         {
/* 280 */           return arrayOfMemberBox[k];
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 285 */     return null;
/*     */   }
/*     */ 
/*     */   private Object getExplicitFunction(Scriptable paramScriptable, String paramString, Object paramObject, boolean paramBoolean)
/*     */   {
/* 291 */     Map localMap = paramBoolean ? this.staticMembers : this.members;
/* 292 */     Object localObject1 = null;
/* 293 */     MemberBox localMemberBox = findExplicitFunction(paramString, paramBoolean);
/*     */ 
/* 295 */     if (localMemberBox != null) {
/* 296 */       Scriptable localScriptable = ScriptableObject.getFunctionPrototype(paramScriptable);
/*     */       Object localObject2;
/* 299 */       if (localMemberBox.isCtor()) {
/* 300 */         localObject2 = new NativeJavaConstructor(localMemberBox);
/*     */ 
/* 302 */         ((NativeJavaConstructor)localObject2).setPrototype(localScriptable);
/* 303 */         localObject1 = localObject2;
/* 304 */         localMap.put(paramString, localObject2);
/*     */       } else {
/* 306 */         localObject2 = localMemberBox.getName();
/* 307 */         localObject1 = localMap.get(localObject2);
/*     */ 
/* 309 */         if (((localObject1 instanceof NativeJavaMethod)) && (((NativeJavaMethod)localObject1).methods.length > 1))
/*     */         {
/* 311 */           NativeJavaMethod localNativeJavaMethod = new NativeJavaMethod(localMemberBox, paramString);
/*     */ 
/* 313 */           localNativeJavaMethod.setPrototype(localScriptable);
/* 314 */           localMap.put(paramString, localNativeJavaMethod);
/* 315 */           localObject1 = localNativeJavaMethod;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 320 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private static Method[] discoverAccessibleMethods(Class<?> paramClass, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 334 */     HashMap localHashMap = new HashMap();
/* 335 */     discoverAccessibleMethods(paramClass, localHashMap, paramBoolean1, paramBoolean2);
/* 336 */     return (Method[])localHashMap.values().toArray(new Method[localHashMap.size()]);
/*     */   }
/*     */ 
/*     */   private static void discoverAccessibleMethods(Class<?> paramClass, Map<MethodSignature, Method> paramMap, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 343 */     if ((Modifier.isPublic(paramClass.getModifiers())) || (paramBoolean2)) {
/*     */       try
/*     */       {
/*     */         Object localObject;
/* 345 */         if ((paramBoolean1) || (paramBoolean2)) while (true) {
/* 346 */             if (paramClass == null) break label273;
/*     */             try {
/* 348 */               Method[] arrayOfMethod1 = paramClass.getDeclaredMethods();
/* 349 */               for (int i = 0; i < arrayOfMethod1.length; i++) {
/* 350 */                 Method localMethod1 = arrayOfMethod1[i];
/* 351 */                 int m = localMethod1.getModifiers();
/*     */ 
/* 353 */                 if ((Modifier.isPublic(m)) || (Modifier.isProtected(m)) || (paramBoolean2))
/*     */                 {
/* 357 */                   localMethodSignature = new MethodSignature(localMethod1);
/* 358 */                   if (!paramMap.containsKey(localMethodSignature)) {
/* 359 */                     if ((paramBoolean2) && (!localMethod1.isAccessible()))
/* 360 */                       localMethod1.setAccessible(true);
/* 361 */                     paramMap.put(localMethodSignature, localMethod1);
/*     */                   }
/*     */                 }
/*     */               }
/* 365 */               paramClass = paramClass.getSuperclass();
/*     */             }
/*     */             catch (SecurityException localSecurityException1)
/*     */             {
/*     */               MethodSignature localMethodSignature;
/* 370 */               Method[] arrayOfMethod3 = paramClass.getMethods();
/* 371 */               for (int k = 0; k < arrayOfMethod3.length; k++) {
/* 372 */                 localObject = arrayOfMethod3[k];
/* 373 */                 localMethodSignature = new MethodSignature((Method)localObject);
/*     */ 
/* 375 */                 if (!paramMap.containsKey(localMethodSignature))
/* 376 */                   paramMap.put(localMethodSignature, localObject);
/*     */               }
/* 378 */               break label273;
/*     */             }
/*     */           }
/*     */ 
/*     */ 
/* 383 */         Method[] arrayOfMethod2 = paramClass.getMethods();
/* 384 */         for (j = 0; j < arrayOfMethod2.length; j++) {
/* 385 */           Method localMethod2 = arrayOfMethod2[j];
/* 386 */           localObject = new MethodSignature(localMethod2);
/*     */ 
/* 388 */           if (!paramMap.containsKey(localObject)) {
/* 389 */             paramMap.put(localObject, localMethod2);
/*     */           }
/*     */         }
/* 392 */         label273: return;
/*     */       } catch (SecurityException localSecurityException2) {
/* 394 */         Context.reportWarning("Could not discover accessible methods of class " + paramClass.getName() + " due to lack of privileges, " + "attemping superclasses/interfaces.");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 403 */     Class[] arrayOfClass = paramClass.getInterfaces();
/* 404 */     for (int j = 0; j < arrayOfClass.length; j++) {
/* 405 */       discoverAccessibleMethods(arrayOfClass[j], paramMap, paramBoolean1, paramBoolean2);
/*     */     }
/*     */ 
/* 408 */     Class localClass = paramClass.getSuperclass();
/* 409 */     if (localClass != null)
/* 410 */       discoverAccessibleMethods(localClass, paramMap, paramBoolean1, paramBoolean2);
/*     */   }
/*     */ 
/*     */   private void reflect(Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/* 455 */     Method[] arrayOfMethod = discoverAccessibleMethods(this.cl, paramBoolean, this.includePrivate);
/*     */     Object localObject3;
/*     */     Object localObject4;
/*     */     Object localObject5;
/*     */     Object localObject6;
/* 457 */     for (int i = 0; i < arrayOfMethod.length; i++) {
/* 458 */       Method localMethod = arrayOfMethod[i];
/* 459 */       int k = localMethod.getModifiers();
/* 460 */       boolean bool1 = Modifier.isStatic(k);
/* 461 */       localObject3 = bool1 ? this.staticMembers : this.members;
/* 462 */       localObject4 = localMethod.getName();
/* 463 */       localObject5 = ((Map)localObject3).get(localObject4);
/* 464 */       if (localObject5 == null) {
/* 465 */         ((Map)localObject3).put(localObject4, localMethod);
/*     */       }
/*     */       else {
/* 468 */         if ((localObject5 instanceof ObjArray)) {
/* 469 */           localObject6 = (ObjArray)localObject5;
/*     */         } else {
/* 471 */           if (!(localObject5 instanceof Method)) Kit.codeBug();
/*     */ 
/* 474 */           localObject6 = new ObjArray();
/* 475 */           ((ObjArray)localObject6).add(localObject5);
/* 476 */           ((Map)localObject3).put(localObject4, localObject6);
/*     */         }
/* 478 */         ((ObjArray)localObject6).add(localMethod);
/*     */       }
/*     */     }
/*     */     Object localObject1;
/*     */     Object localObject2;
/*     */     Object localObject9;
/* 484 */     for (i = 0; i != 2; i++) {
/* 485 */       j = i == 0 ? 1 : 0;
/* 486 */       localObject1 = j != 0 ? this.staticMembers : this.members;
/* 487 */       for (localObject2 = ((Map)localObject1).entrySet().iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (Map.Entry)((Iterator)localObject2).next();
/*     */ 
/* 489 */         localObject5 = ((Map.Entry)localObject3).getValue();
/* 490 */         if ((localObject5 instanceof Method)) {
/* 491 */           localObject4 = new MemberBox[1];
/* 492 */           localObject4[0] = new MemberBox((Method)localObject5);
/*     */         } else {
/* 494 */           localObject6 = (ObjArray)localObject5;
/* 495 */           int i1 = ((ObjArray)localObject6).size();
/* 496 */           if (i1 < 2) Kit.codeBug();
/* 497 */           localObject4 = new MemberBox[i1];
/* 498 */           for (int i2 = 0; i2 != i1; i2++) {
/* 499 */             localObject9 = (Method)((ObjArray)localObject6).get(i2);
/* 500 */             localObject4[i2] = new MemberBox((Method)localObject9);
/*     */           }
/*     */         }
/* 503 */         localObject6 = new NativeJavaMethod((MemberBox[])localObject4);
/* 504 */         if (paramScriptable != null) {
/* 505 */           ScriptRuntime.setFunctionProtoAndParent((BaseFunction)localObject6, paramScriptable);
/*     */         }
/* 507 */         ((Map)localObject1).put(((Map.Entry)localObject3).getKey(), localObject6);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 512 */     Field[] arrayOfField = getAccessibleFields();
/* 513 */     for (int j = 0; j < arrayOfField.length; j++) {
/* 514 */       localObject1 = arrayOfField[j];
/* 515 */       localObject2 = ((Field)localObject1).getName();
/* 516 */       int n = ((Field)localObject1).getModifiers();
/* 517 */       if ((this.includePrivate) || (Modifier.isPublic(n)))
/*     */       {
/*     */         try
/*     */         {
/* 521 */           boolean bool2 = Modifier.isStatic(n);
/* 522 */           localObject5 = bool2 ? this.staticMembers : this.members;
/* 523 */           localObject6 = ((Map)localObject5).get(localObject2);
/* 524 */           if (localObject6 == null) {
/* 525 */             ((Map)localObject5).put(localObject2, localObject1);
/*     */           }
/*     */           else
/*     */           {
/*     */             Object localObject8;
/* 526 */             if ((localObject6 instanceof NativeJavaMethod)) {
/* 527 */               localObject8 = (NativeJavaMethod)localObject6;
/* 528 */               FieldAndMethods localFieldAndMethods = new FieldAndMethods(paramScriptable, ((NativeJavaMethod)localObject8).methods, (Field)localObject1);
/*     */ 
/* 530 */               localObject9 = bool2 ? this.staticFieldAndMethods : this.fieldAndMethods;
/*     */ 
/* 532 */               if (localObject9 == null) {
/* 533 */                 localObject9 = new HashMap();
/* 534 */                 if (bool2)
/* 535 */                   this.staticFieldAndMethods = ((Map)localObject9);
/*     */                 else {
/* 537 */                   this.fieldAndMethods = ((Map)localObject9);
/*     */                 }
/*     */               }
/* 540 */               ((Map)localObject9).put(localObject2, localFieldAndMethods);
/* 541 */               ((Map)localObject5).put(localObject2, localFieldAndMethods);
/* 542 */             } else if ((localObject6 instanceof Field)) {
/* 543 */               localObject8 = (Field)localObject6;
/*     */ 
/* 550 */               if (((Field)localObject8).getDeclaringClass().isAssignableFrom(((Field)localObject1).getDeclaringClass()))
/*     */               {
/* 553 */                 ((Map)localObject5).put(localObject2, localObject1);
/*     */               }
/*     */             }
/*     */             else {
/* 557 */               Kit.codeBug();
/*     */             }
/*     */           }
/*     */         } catch (SecurityException localSecurityException) {
/* 561 */           Context.reportWarning("Could not access field " + (String)localObject2 + " of class " + this.cl.getName() + " due to lack of privileges.");
/*     */         }
/*     */       }
/*     */     }
/*     */     HashMap localHashMap;
/*     */     Iterator localIterator;
/* 569 */     for (j = 0; j != 2; j++) {
/* 570 */       m = j == 0 ? 1 : 0;
/* 571 */       localObject2 = m != 0 ? this.staticMembers : this.members;
/*     */ 
/* 573 */       localHashMap = new HashMap();
/*     */ 
/* 576 */       for (localIterator = ((Map)localObject2).keySet().iterator(); localIterator.hasNext(); ) { localObject5 = (String)localIterator.next();
/*     */ 
/* 578 */         boolean bool3 = ((String)localObject5).startsWith("get");
/* 579 */         boolean bool4 = ((String)localObject5).startsWith("set");
/* 580 */         boolean bool5 = ((String)localObject5).startsWith("is");
/* 581 */         if ((bool3) || (bool5) || (bool4))
/*     */         {
/* 584 */           localObject9 = ((String)localObject5).substring(bool5 ? 2 : 3);
/*     */ 
/* 586 */           if (((String)localObject9).length() != 0)
/*     */           {
/* 590 */             Object localObject10 = localObject9;
/* 591 */             char c1 = ((String)localObject9).charAt(0);
/* 592 */             if (Character.isUpperCase(c1)) {
/* 593 */               if (((String)localObject9).length() == 1) {
/* 594 */                 localObject10 = ((String)localObject9).toLowerCase();
/*     */               } else {
/* 596 */                 char c2 = ((String)localObject9).charAt(1);
/* 597 */                 if (!Character.isUpperCase(c2)) {
/* 598 */                   localObject10 = Character.toLowerCase(c1) + ((String)localObject9).substring(1);
/*     */                 }
/*     */ 
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/* 606 */             if (!localHashMap.containsKey(localObject10))
/*     */             {
/* 608 */               Object localObject11 = ((Map)localObject2).get(localObject10);
/* 609 */               if ((localObject11 == null) || (
/* 611 */                 (this.includePrivate) && ((localObject11 instanceof Member)) && (Modifier.isPrivate(((Member)localObject11).getModifiers()))))
/*     */               {
/* 621 */                 MemberBox localMemberBox1 = null;
/* 622 */                 localMemberBox1 = findGetter(m, (Map)localObject2, "get", (String)localObject9);
/*     */ 
/* 624 */                 if (localMemberBox1 == null) {
/* 625 */                   localMemberBox1 = findGetter(m, (Map)localObject2, "is", (String)localObject9);
/*     */                 }
/*     */ 
/* 629 */                 MemberBox localMemberBox2 = null;
/* 630 */                 Object localObject12 = null;
/* 631 */                 String str = "set".concat((String)localObject9);
/*     */ 
/* 633 */                 if (((Map)localObject2).containsKey(str))
/*     */                 {
/* 635 */                   localObject13 = ((Map)localObject2).get(str);
/* 636 */                   if ((localObject13 instanceof NativeJavaMethod)) {
/* 637 */                     NativeJavaMethod localNativeJavaMethod = (NativeJavaMethod)localObject13;
/* 638 */                     if (localMemberBox1 != null)
/*     */                     {
/* 641 */                       Class localClass = localMemberBox1.method().getReturnType();
/* 642 */                       localMemberBox2 = extractSetMethod(localClass, localNativeJavaMethod.methods, m);
/*     */                     }
/*     */                     else
/*     */                     {
/* 646 */                       localMemberBox2 = extractSetMethod(localNativeJavaMethod.methods, m);
/*     */                     }
/*     */ 
/* 649 */                     if (localNativeJavaMethod.methods.length > 1) {
/* 650 */                       localObject12 = localNativeJavaMethod;
/*     */                     }
/*     */                   }
/*     */                 }
/*     */ 
/* 655 */                 Object localObject13 = new BeanProperty(localMemberBox1, localMemberBox2, localObject12);
/*     */ 
/* 657 */                 localHashMap.put(localObject10, localObject13);
/*     */               }
/*     */             }
/*     */           }
/*     */         } }
/* 662 */       for (localIterator = localHashMap.keySet().iterator(); localIterator.hasNext(); ) { localObject5 = (String)localIterator.next();
/* 663 */         Object localObject7 = localHashMap.get(localObject5);
/* 664 */         ((Map)localObject2).put(localObject5, localObject7);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 669 */     Constructor[] arrayOfConstructor = getAccessibleConstructors();
/* 670 */     this.ctors = new MemberBox[arrayOfConstructor.length];
/* 671 */     for (int m = 0; m != arrayOfConstructor.length; m++)
/* 672 */       this.ctors[m] = new MemberBox(arrayOfConstructor[m]);
/*     */   }
/*     */ 
/*     */   private Constructor<?>[] getAccessibleConstructors()
/*     */   {
/* 680 */     if ((this.includePrivate) && (this.cl != ScriptRuntime.ClassClass)) {
/*     */       try {
/* 682 */         Constructor[] arrayOfConstructor = this.cl.getDeclaredConstructors();
/* 683 */         AccessibleObject.setAccessible(arrayOfConstructor, true);
/*     */ 
/* 685 */         return arrayOfConstructor;
/*     */       }
/*     */       catch (SecurityException localSecurityException) {
/* 688 */         Context.reportWarning("Could not access constructor  of class " + this.cl.getName() + " due to lack of privileges.");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 693 */     return this.cl.getConstructors();
/*     */   }
/*     */ 
/*     */   private Field[] getAccessibleFields() {
/* 697 */     if (this.includePrivate)
/*     */       try {
/* 699 */         ArrayList localArrayList = new ArrayList();
/* 700 */         Class localClass = this.cl;
/*     */ 
/* 702 */         while (localClass != null)
/*     */         {
/* 705 */           Field[] arrayOfField = localClass.getDeclaredFields();
/* 706 */           for (int i = 0; i < arrayOfField.length; i++) {
/* 707 */             arrayOfField[i].setAccessible(true);
/* 708 */             localArrayList.add(arrayOfField[i]);
/*     */           }
/*     */ 
/* 712 */           localClass = localClass.getSuperclass();
/*     */         }
/*     */ 
/* 715 */         return (Field[])localArrayList.toArray(new Field[localArrayList.size()]);
/*     */       }
/*     */       catch (SecurityException localSecurityException)
/*     */       {
/*     */       }
/* 720 */     return this.cl.getFields();
/*     */   }
/*     */ 
/*     */   private MemberBox findGetter(boolean paramBoolean, Map<String, Object> paramMap, String paramString1, String paramString2)
/*     */   {
/* 726 */     String str = paramString1.concat(paramString2);
/* 727 */     if (paramMap.containsKey(str))
/*     */     {
/* 729 */       Object localObject = paramMap.get(str);
/* 730 */       if ((localObject instanceof NativeJavaMethod)) {
/* 731 */         NativeJavaMethod localNativeJavaMethod = (NativeJavaMethod)localObject;
/* 732 */         return extractGetMethod(localNativeJavaMethod.methods, paramBoolean);
/*     */       }
/*     */     }
/* 735 */     return null;
/*     */   }
/*     */ 
/*     */   private static MemberBox extractGetMethod(MemberBox[] paramArrayOfMemberBox, boolean paramBoolean)
/*     */   {
/* 743 */     for (int i = 0; i < paramArrayOfMemberBox.length; i++) {
/* 744 */       MemberBox localMemberBox = paramArrayOfMemberBox[i];
/*     */ 
/* 747 */       if ((localMemberBox.argTypes.length == 0) && ((!paramBoolean) || (localMemberBox.isStatic())))
/*     */       {
/* 750 */         Class localClass = localMemberBox.method().getReturnType();
/* 751 */         if (localClass == Void.TYPE) break;
/* 752 */         return localMemberBox;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 757 */     return null;
/*     */   }
/*     */ 
/*     */   private static MemberBox extractSetMethod(Class<?> paramClass, MemberBox[] paramArrayOfMemberBox, boolean paramBoolean)
/*     */   {
/* 771 */     for (int i = 1; i <= 2; i++) {
/* 772 */       for (int j = 0; j < paramArrayOfMemberBox.length; j++) {
/* 773 */         MemberBox localMemberBox = paramArrayOfMemberBox[j];
/* 774 */         if ((!paramBoolean) || (localMemberBox.isStatic())) {
/* 775 */           Class[] arrayOfClass = localMemberBox.argTypes;
/* 776 */           if (arrayOfClass.length == 1) {
/* 777 */             if (i == 1) {
/* 778 */               if (arrayOfClass[0] == paramClass)
/* 779 */                 return localMemberBox;
/*     */             }
/*     */             else {
/* 782 */               if (i != 2) Kit.codeBug();
/* 783 */               if (arrayOfClass[0].isAssignableFrom(paramClass)) {
/* 784 */                 return localMemberBox;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 791 */     return null;
/*     */   }
/*     */ 
/*     */   private static MemberBox extractSetMethod(MemberBox[] paramArrayOfMemberBox, boolean paramBoolean)
/*     */   {
/* 798 */     for (int i = 0; i < paramArrayOfMemberBox.length; i++) {
/* 799 */       MemberBox localMemberBox = paramArrayOfMemberBox[i];
/* 800 */       if (((!paramBoolean) || (localMemberBox.isStatic())) && 
/* 801 */         (localMemberBox.method().getReturnType() == Void.TYPE) && 
/* 802 */         (localMemberBox.argTypes.length == 1)) {
/* 803 */         return localMemberBox;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 808 */     return null;
/*     */   }
/*     */ 
/*     */   Map<String, FieldAndMethods> getFieldAndMethodsObjects(Scriptable paramScriptable, Object paramObject, boolean paramBoolean)
/*     */   {
/* 814 */     Map localMap = paramBoolean ? this.staticFieldAndMethods : this.fieldAndMethods;
/* 815 */     if (localMap == null)
/* 816 */       return null;
/* 817 */     int i = localMap.size();
/* 818 */     HashMap localHashMap = new HashMap(i);
/* 819 */     for (FieldAndMethods localFieldAndMethods1 : localMap.values()) {
/* 820 */       FieldAndMethods localFieldAndMethods2 = new FieldAndMethods(paramScriptable, localFieldAndMethods1.methods, localFieldAndMethods1.field);
/*     */ 
/* 822 */       localFieldAndMethods2.javaObject = paramObject;
/* 823 */       localHashMap.put(localFieldAndMethods1.field.getName(), localFieldAndMethods2);
/*     */     }
/* 825 */     return localHashMap;
/*     */   }
/*     */   static JavaMembers lookupClass(Scriptable paramScriptable, Class<?> paramClass1, Class<?> paramClass2, boolean paramBoolean) {
/* 832 */     ClassCache localClassCache = ClassCache.get(paramScriptable);
/* 833 */     Map localMap = localClassCache.getClassCacheMap();
/*     */ 
/* 835 */     Object localObject = paramClass1;
/*     */     JavaMembers localJavaMembers;
/*     */     while (true) {
/* 837 */       localJavaMembers = (JavaMembers)localMap.get(localObject);
/* 838 */       if (localJavaMembers != null) {
/* 839 */         if (localObject != paramClass1)
/*     */         {
/* 842 */           localMap.put(paramClass1, localJavaMembers);
/*     */         }
/* 844 */         return localJavaMembers;
/*     */       }
/*     */       try {
/* 847 */         localJavaMembers = new JavaMembers(localClassCache.getAssociatedScope(), (Class)localObject, paramBoolean);
/*     */       }
/*     */       catch (SecurityException localSecurityException)
/*     */       {
/* 855 */         if ((paramClass2 != null) && (paramClass2.isInterface())) {
/* 856 */           localObject = paramClass2;
/* 857 */           paramClass2 = null;
/*     */         } else {
/* 859 */           Class localClass = ((Class)localObject).getSuperclass();
/* 860 */           if (localClass == null) {
/* 861 */             if (((Class)localObject).isInterface())
/*     */             {
/* 863 */               localClass = ScriptRuntime.ObjectClass;
/*     */             }
/* 865 */             else throw localSecurityException;
/*     */           }
/*     */ 
/* 868 */           localObject = localClass;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 873 */     if (localClassCache.isCachingEnabled()) {
/* 874 */       localMap.put(localObject, localJavaMembers);
/* 875 */       if (localObject != paramClass1)
/*     */       {
/* 878 */         localMap.put(paramClass1, localJavaMembers);
/*     */       }
/*     */     }
/* 881 */     return localJavaMembers;
/*     */   }
/*     */ 
/*     */   RuntimeException reportMemberNotFound(String paramString)
/*     */   {
/* 886 */     return Context.reportRuntimeError2("msg.java.member.not.found", this.cl.getName(), paramString);
/*     */   }
/*     */ 
/*     */   private static final class MethodSignature
/*     */   {
/*     */     private final String name;
/*     */     private final Class<?>[] args;
/*     */ 
/*     */     private MethodSignature(String paramString, Class<?>[] paramArrayOfClass)
/*     */     {
/* 422 */       this.name = paramString;
/* 423 */       this.args = paramArrayOfClass;
/*     */     }
/*     */ 
/*     */     MethodSignature(Method paramMethod)
/*     */     {
/* 428 */       this(paramMethod.getName(), paramMethod.getParameterTypes());
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 434 */       if ((paramObject instanceof MethodSignature))
/*     */       {
/* 436 */         MethodSignature localMethodSignature = (MethodSignature)paramObject;
/* 437 */         return (localMethodSignature.name.equals(this.name)) && (Arrays.equals(this.args, localMethodSignature.args));
/*     */       }
/* 439 */       return false;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 445 */       return this.name.hashCode() ^ this.args.length;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.JavaMembers
 * JD-Core Version:    0.6.2
 */