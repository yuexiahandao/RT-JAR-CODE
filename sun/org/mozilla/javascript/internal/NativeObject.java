/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class NativeObject extends IdScriptableObject
/*     */   implements Map
/*     */ {
/*  58 */   private static final Object OBJECT_TAG = "Object";
/*     */   private static final int ConstructorId_getPrototypeOf = -1;
/*     */   private static final int ConstructorId_keys = -2;
/*     */   private static final int ConstructorId_getOwnPropertyNames = -3;
/*     */   private static final int ConstructorId_getOwnPropertyDescriptor = -4;
/*     */   private static final int ConstructorId_defineProperty = -5;
/*     */   private static final int ConstructorId_isExtensible = -6;
/*     */   private static final int ConstructorId_preventExtensions = -7;
/*     */   private static final int ConstructorId_defineProperties = -8;
/*     */   private static final int ConstructorId_create = -9;
/*     */   private static final int ConstructorId_isSealed = -10;
/*     */   private static final int ConstructorId_isFrozen = -11;
/*     */   private static final int ConstructorId_seal = -12;
/*     */   private static final int ConstructorId_freeze = -13;
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_toString = 2;
/*     */   private static final int Id_toLocaleString = 3;
/*     */   private static final int Id_valueOf = 4;
/*     */   private static final int Id_hasOwnProperty = 5;
/*     */   private static final int Id_propertyIsEnumerable = 6;
/*     */   private static final int Id_isPrototypeOf = 7;
/*     */   private static final int Id_toSource = 8;
/*     */   private static final int Id___defineGetter__ = 9;
/*     */   private static final int Id___defineSetter__ = 10;
/*     */   private static final int Id___lookupGetter__ = 11;
/*     */   private static final int Id___lookupSetter__ = 12;
/*     */   private static final int MAX_PROTOTYPE_ID = 12;
/*     */ 
/*     */   static void init(Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/*  62 */     NativeObject localNativeObject = new NativeObject();
/*  63 */     localNativeObject.exportAsJSClass(12, paramScriptable, paramBoolean);
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  69 */     return "Object";
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  75 */     return ScriptRuntime.defaultObjectToString(this);
/*     */   }
/*     */ 
/*     */   protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject)
/*     */   {
/*  81 */     addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -1, "getPrototypeOf", 1);
/*     */ 
/*  83 */     addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -2, "keys", 1);
/*     */ 
/*  85 */     addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -3, "getOwnPropertyNames", 1);
/*     */ 
/*  87 */     addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -4, "getOwnPropertyDescriptor", 2);
/*     */ 
/*  89 */     addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -5, "defineProperty", 3);
/*     */ 
/*  91 */     addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -6, "isExtensible", 1);
/*     */ 
/*  93 */     addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -7, "preventExtensions", 1);
/*     */ 
/*  95 */     addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -8, "defineProperties", 2);
/*     */ 
/*  97 */     addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -9, "create", 2);
/*     */ 
/*  99 */     addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -10, "isSealed", 1);
/*     */ 
/* 101 */     addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -11, "isFrozen", 1);
/*     */ 
/* 103 */     addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -12, "seal", 1);
/*     */ 
/* 105 */     addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -13, "freeze", 1);
/*     */ 
/* 107 */     super.fillConstructorProperties(paramIdFunctionObject);
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*     */     int i;
/*     */     String str;
/* 115 */     switch (paramInt) { case 1:
/* 116 */       i = 1; str = "constructor"; break;
/*     */     case 2:
/* 117 */       i = 0; str = "toString"; break;
/*     */     case 3:
/* 118 */       i = 0; str = "toLocaleString"; break;
/*     */     case 4:
/* 119 */       i = 0; str = "valueOf"; break;
/*     */     case 5:
/* 120 */       i = 1; str = "hasOwnProperty"; break;
/*     */     case 6:
/* 122 */       i = 1; str = "propertyIsEnumerable"; break;
/*     */     case 7:
/* 123 */       i = 1; str = "isPrototypeOf"; break;
/*     */     case 8:
/* 124 */       i = 0; str = "toSource"; break;
/*     */     case 9:
/* 126 */       i = 2; str = "__defineGetter__"; break;
/*     */     case 10:
/* 128 */       i = 2; str = "__defineSetter__"; break;
/*     */     case 11:
/* 130 */       i = 1; str = "__lookupGetter__"; break;
/*     */     case 12:
/* 132 */       i = 1; str = "__lookupSetter__"; break;
/*     */     default:
/* 133 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */     }
/* 135 */     initPrototypeMethod(OBJECT_TAG, paramInt, str, i);
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 142 */     if (!paramIdFunctionObject.hasTag(OBJECT_TAG)) {
/* 143 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 145 */     int i = paramIdFunctionObject.methodId();
/*     */     boolean bool;
/*     */     Object localObject2;
/*     */     int k;
/*     */     int i4;
/*     */     Object localObject1;
/*     */     int m;
/*     */     int i1;
/*     */     Object localObject5;
/*     */     Scriptable localScriptable;
/*     */     Object localObject3;
/*     */     Object localObject4;
/*     */     Object localObject6;
/* 146 */     switch (i) {
/*     */     case 1:
/* 148 */       if (paramScriptable2 != null)
/*     */       {
/* 150 */         return paramIdFunctionObject.construct(paramContext, paramScriptable1, paramArrayOfObject);
/*     */       }
/* 152 */       if ((paramArrayOfObject.length == 0) || (paramArrayOfObject[0] == null) || (paramArrayOfObject[0] == Undefined.instance))
/*     */       {
/* 155 */         return new NativeObject();
/*     */       }
/* 157 */       return ScriptRuntime.toObject(paramContext, paramScriptable1, paramArrayOfObject[0]);
/*     */     case 2:
/*     */     case 3:
/* 162 */       if (paramContext.hasFeature(4)) {
/* 163 */         String str = ScriptRuntime.defaultObjectToSource(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */ 
/* 165 */         int j = str.length();
/* 166 */         if ((j != 0) && (str.charAt(0) == '(') && (str.charAt(j - 1) == ')'))
/*     */         {
/* 168 */           str = str.substring(1, j - 1);
/*     */         }
/* 170 */         return str;
/*     */       }
/* 172 */       return ScriptRuntime.defaultObjectToString(paramScriptable2);
/*     */     case 4:
/* 176 */       return paramScriptable2;
/*     */     case 5:
/* 180 */       if (paramArrayOfObject.length == 0) {
/* 181 */         bool = false;
/*     */       } else {
/* 183 */         localObject2 = ScriptRuntime.toStringIdOrIndex(paramContext, paramArrayOfObject[0]);
/* 184 */         if (localObject2 == null) {
/* 185 */           k = ScriptRuntime.lastIndexResult(paramContext);
/* 186 */           bool = paramScriptable2.has(k, paramScriptable2);
/*     */         } else {
/* 188 */           bool = paramScriptable2.has((String)localObject2, paramScriptable2);
/*     */         }
/*     */       }
/* 191 */       return ScriptRuntime.wrapBoolean(bool);
/*     */     case 6:
/* 196 */       if (paramArrayOfObject.length == 0) {
/* 197 */         bool = false;
/*     */       } else {
/* 199 */         localObject2 = ScriptRuntime.toStringIdOrIndex(paramContext, paramArrayOfObject[0]);
/* 200 */         if (localObject2 == null) {
/* 201 */           k = ScriptRuntime.lastIndexResult(paramContext);
/* 202 */           bool = paramScriptable2.has(k, paramScriptable2);
/* 203 */           if ((bool) && ((paramScriptable2 instanceof ScriptableObject))) {
/* 204 */             ScriptableObject localScriptableObject2 = (ScriptableObject)paramScriptable2;
/* 205 */             i4 = localScriptableObject2.getAttributes(k);
/* 206 */             bool = (i4 & 0x2) == 0;
/*     */           }
/*     */         } else {
/* 209 */           bool = paramScriptable2.has((String)localObject2, paramScriptable2);
/* 210 */           if ((bool) && ((paramScriptable2 instanceof ScriptableObject))) {
/* 211 */             ScriptableObject localScriptableObject1 = (ScriptableObject)paramScriptable2;
/* 212 */             int n = localScriptableObject1.getAttributes((String)localObject2);
/* 213 */             bool = (n & 0x2) == 0;
/*     */           }
/*     */         }
/*     */       }
/* 217 */       return ScriptRuntime.wrapBoolean(bool);
/*     */     case 7:
/* 221 */       bool = false;
/* 222 */       if ((paramArrayOfObject.length != 0) && ((paramArrayOfObject[0] instanceof Scriptable))) {
/* 223 */         localObject2 = (Scriptable)paramArrayOfObject[0];
/*     */         do {
/* 225 */           localObject2 = ((Scriptable)localObject2).getPrototype();
/* 226 */           if (localObject2 == paramScriptable2) {
/* 227 */             bool = true;
/* 228 */             break;
/*     */           }
/*     */         }
/* 230 */         while (localObject2 != null);
/*     */       }
/* 232 */       return ScriptRuntime.wrapBoolean(bool);
/*     */     case 8:
/* 236 */       return ScriptRuntime.defaultObjectToSource(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     case 9:
/*     */     case 10:
/* 241 */       if ((paramArrayOfObject.length < 2) || (!(paramArrayOfObject[1] instanceof Callable))) {
/* 242 */         localObject1 = paramArrayOfObject.length >= 2 ? paramArrayOfObject[1] : Undefined.instance;
/*     */ 
/* 244 */         throw ScriptRuntime.notFunctionError(localObject1);
/*     */       }
/* 246 */       if (!(paramScriptable2 instanceof ScriptableObject)) {
/* 247 */         throw Context.reportRuntimeError2("msg.extend.scriptable", paramScriptable2.getClass().getName(), String.valueOf(paramArrayOfObject[0]));
/*     */       }
/*     */ 
/* 252 */       localObject1 = (ScriptableObject)paramScriptable2;
/* 253 */       localObject2 = ScriptRuntime.toStringIdOrIndex(paramContext, paramArrayOfObject[0]);
/* 254 */       m = localObject2 != null ? 0 : ScriptRuntime.lastIndexResult(paramContext);
/*     */ 
/* 256 */       Callable localCallable = (Callable)paramArrayOfObject[1];
/* 257 */       i4 = i == 10 ? 1 : 0;
/* 258 */       ((ScriptableObject)localObject1).setGetterOrSetter((String)localObject2, m, localCallable, i4);
/* 259 */       if ((localObject1 instanceof NativeArray)) {
/* 260 */         ((NativeArray)localObject1).setDenseOnly(false);
/*     */       }
/* 262 */       return Undefined.instance;
/*     */     case 11:
/*     */     case 12:
/* 267 */       if ((paramArrayOfObject.length < 1) || (!(paramScriptable2 instanceof ScriptableObject)))
/*     */       {
/* 269 */         return Undefined.instance;
/*     */       }
/* 271 */       localObject1 = (ScriptableObject)paramScriptable2;
/* 272 */       localObject2 = ScriptRuntime.toStringIdOrIndex(paramContext, paramArrayOfObject[0]);
/* 273 */       m = localObject2 != null ? 0 : ScriptRuntime.lastIndexResult(paramContext);
/*     */ 
/* 275 */       i1 = i == 12 ? 1 : 0;
/*     */       while (true)
/*     */       {
/* 278 */         localObject5 = ((ScriptableObject)localObject1).getGetterOrSetter((String)localObject2, m, i1);
/* 279 */         if (localObject5 != null)
/*     */         {
/*     */           break;
/*     */         }
/* 283 */         localScriptable = ((ScriptableObject)localObject1).getPrototype();
/* 284 */         if (localScriptable == null)
/*     */           break;
/* 286 */         if (!(localScriptable instanceof ScriptableObject)) break;
/* 287 */         localObject1 = (ScriptableObject)localScriptable;
/*     */       }
/*     */ 
/* 291 */       if (localObject5 != null) {
/* 292 */         return localObject5;
/*     */       }
/* 294 */       return Undefined.instance;
/*     */     case -1:
/* 298 */       localObject1 = paramArrayOfObject.length < 1 ? Undefined.instance : paramArrayOfObject[0];
/* 299 */       localObject2 = ensureScriptable(localObject1);
/* 300 */       return ((Scriptable)localObject2).getPrototype();
/*     */     case -2:
/* 304 */       localObject1 = paramArrayOfObject.length < 1 ? Undefined.instance : paramArrayOfObject[0];
/* 305 */       localObject2 = ensureScriptable(localObject1);
/* 306 */       localObject3 = ((Scriptable)localObject2).getIds();
/* 307 */       for (i1 = 0; i1 < localObject3.length; i1++) {
/* 308 */         localObject3[i1] = ScriptRuntime.toString(localObject3[i1]);
/*     */       }
/* 310 */       return paramContext.newArray(paramScriptable1, (Object[])localObject3);
/*     */     case -3:
/* 314 */       localObject1 = paramArrayOfObject.length < 1 ? Undefined.instance : paramArrayOfObject[0];
/* 315 */       localObject2 = ensureScriptableObject(localObject1);
/* 316 */       localObject3 = ((ScriptableObject)localObject2).getAllIds();
/* 317 */       for (int i2 = 0; i2 < localObject3.length; i2++) {
/* 318 */         localObject3[i2] = ScriptRuntime.toString(localObject3[i2]);
/*     */       }
/* 320 */       return paramContext.newArray(paramScriptable1, (Object[])localObject3);
/*     */     case -4:
/* 324 */       localObject1 = paramArrayOfObject.length < 1 ? Undefined.instance : paramArrayOfObject[0];
/*     */ 
/* 328 */       localObject2 = ensureScriptableObject(localObject1);
/* 329 */       localObject3 = paramArrayOfObject.length < 2 ? Undefined.instance : paramArrayOfObject[1];
/* 330 */       localObject4 = ScriptRuntime.toString(localObject3);
/* 331 */       localObject5 = ((ScriptableObject)localObject2).getOwnPropertyDescriptor(paramContext, localObject4);
/* 332 */       return localObject5 == null ? Undefined.instance : localObject5;
/*     */     case -5:
/* 336 */       localObject1 = paramArrayOfObject.length < 1 ? Undefined.instance : paramArrayOfObject[0];
/* 337 */       localObject2 = ensureScriptableObject(localObject1);
/* 338 */       localObject3 = paramArrayOfObject.length < 2 ? Undefined.instance : paramArrayOfObject[1];
/* 339 */       localObject4 = paramArrayOfObject.length < 3 ? Undefined.instance : paramArrayOfObject[2];
/* 340 */       localObject5 = ensureScriptableObject(localObject4);
/* 341 */       ((ScriptableObject)localObject2).defineOwnProperty(paramContext, localObject3, (ScriptableObject)localObject5);
/* 342 */       return localObject2;
/*     */     case -6:
/* 346 */       localObject1 = paramArrayOfObject.length < 1 ? Undefined.instance : paramArrayOfObject[0];
/* 347 */       localObject2 = ensureScriptableObject(localObject1);
/* 348 */       return Boolean.valueOf(((ScriptableObject)localObject2).isExtensible());
/*     */     case -7:
/* 352 */       localObject1 = paramArrayOfObject.length < 1 ? Undefined.instance : paramArrayOfObject[0];
/* 353 */       localObject2 = ensureScriptableObject(localObject1);
/* 354 */       ((ScriptableObject)localObject2).preventExtensions();
/* 355 */       return localObject2;
/*     */     case -8:
/* 359 */       localObject1 = paramArrayOfObject.length < 1 ? Undefined.instance : paramArrayOfObject[0];
/* 360 */       localObject2 = ensureScriptableObject(localObject1);
/* 361 */       localObject3 = paramArrayOfObject.length < 2 ? Undefined.instance : paramArrayOfObject[1];
/* 362 */       localObject4 = Context.toObject(localObject3, getParentScope());
/* 363 */       ((ScriptableObject)localObject2).defineOwnProperties(paramContext, ensureScriptableObject(localObject4));
/* 364 */       return localObject2;
/*     */     case -9:
/* 368 */       localObject1 = paramArrayOfObject.length < 1 ? Undefined.instance : paramArrayOfObject[0];
/* 369 */       localObject2 = localObject1 == null ? null : ensureScriptable(localObject1);
/*     */ 
/* 371 */       localObject3 = new NativeObject();
/* 372 */       ((ScriptableObject)localObject3).setParentScope(getParentScope());
/* 373 */       ((ScriptableObject)localObject3).setPrototype((Scriptable)localObject2);
/*     */ 
/* 375 */       if ((paramArrayOfObject.length > 1) && (paramArrayOfObject[1] != Undefined.instance)) {
/* 376 */         localObject4 = Context.toObject(paramArrayOfObject[1], getParentScope());
/* 377 */         ((ScriptableObject)localObject3).defineOwnProperties(paramContext, ensureScriptableObject(localObject4));
/*     */       }
/*     */ 
/* 380 */       return localObject3;
/*     */     case -10:
/* 385 */       localObject1 = paramArrayOfObject.length < 1 ? Undefined.instance : paramArrayOfObject[0];
/* 386 */       localObject2 = ensureScriptableObject(localObject1);
/*     */ 
/* 388 */       if (((ScriptableObject)localObject2).isExtensible()) return Boolean.valueOf(false);
/*     */ 
/* 390 */       for (localScriptable : ((ScriptableObject)localObject2).getAllIds()) {
/* 391 */         localObject6 = ((ScriptableObject)localObject2).getOwnPropertyDescriptor(paramContext, localScriptable).get("configurable");
/* 392 */         if (Boolean.TRUE.equals(localObject6)) {
/* 393 */           return Boolean.valueOf(false);
/*     */         }
/*     */       }
/* 396 */       return Boolean.valueOf(true);
/*     */     case -11:
/* 400 */       localObject1 = paramArrayOfObject.length < 1 ? Undefined.instance : paramArrayOfObject[0];
/* 401 */       localObject2 = ensureScriptableObject(localObject1);
/*     */ 
/* 403 */       if (((ScriptableObject)localObject2).isExtensible()) return Boolean.valueOf(false);
/*     */ 
/* 405 */       for (localScriptable : ((ScriptableObject)localObject2).getAllIds()) {
/* 406 */         localObject6 = ((ScriptableObject)localObject2).getOwnPropertyDescriptor(paramContext, localScriptable);
/* 407 */         if (Boolean.TRUE.equals(((ScriptableObject)localObject6).get("configurable")))
/* 408 */           return Boolean.valueOf(false);
/* 409 */         if ((isDataDescriptor((ScriptableObject)localObject6)) && (Boolean.TRUE.equals(((ScriptableObject)localObject6).get("writable")))) {
/* 410 */           return Boolean.valueOf(false);
/*     */         }
/*     */       }
/* 413 */       return Boolean.valueOf(true);
/*     */     case -12:
/* 417 */       localObject1 = paramArrayOfObject.length < 1 ? Undefined.instance : paramArrayOfObject[0];
/* 418 */       localObject2 = ensureScriptableObject(localObject1);
/*     */ 
/* 420 */       for (localScriptable : ((ScriptableObject)localObject2).getAllIds()) {
/* 421 */         localObject6 = ((ScriptableObject)localObject2).getOwnPropertyDescriptor(paramContext, localScriptable);
/* 422 */         if (Boolean.TRUE.equals(((ScriptableObject)localObject6).get("configurable"))) {
/* 423 */           ((ScriptableObject)localObject6).put("configurable", (Scriptable)localObject6, Boolean.valueOf(false));
/* 424 */           ((ScriptableObject)localObject2).defineOwnProperty(paramContext, localScriptable, (ScriptableObject)localObject6);
/*     */         }
/*     */       }
/* 427 */       ((ScriptableObject)localObject2).preventExtensions();
/*     */ 
/* 429 */       return localObject2;
/*     */     case -13:
/* 433 */       localObject1 = paramArrayOfObject.length < 1 ? Undefined.instance : paramArrayOfObject[0];
/* 434 */       localObject2 = ensureScriptableObject(localObject1);
/*     */ 
/* 436 */       for (localScriptable : ((ScriptableObject)localObject2).getAllIds()) {
/* 437 */         localObject6 = ((ScriptableObject)localObject2).getOwnPropertyDescriptor(paramContext, localScriptable);
/* 438 */         if ((isDataDescriptor((ScriptableObject)localObject6)) && (Boolean.TRUE.equals(((ScriptableObject)localObject6).get("writable"))))
/* 439 */           ((ScriptableObject)localObject6).put("writable", (Scriptable)localObject6, Boolean.valueOf(false));
/* 440 */         if (Boolean.TRUE.equals(((ScriptableObject)localObject6).get("configurable")))
/* 441 */           ((ScriptableObject)localObject6).put("configurable", (Scriptable)localObject6, Boolean.valueOf(false));
/* 442 */         ((ScriptableObject)localObject2).defineOwnProperty(paramContext, localScriptable, (ScriptableObject)localObject6);
/*     */       }
/* 444 */       ((ScriptableObject)localObject2).preventExtensions();
/*     */ 
/* 446 */       return localObject2;
/*     */     case 0:
/*     */     }
/*     */ 
/* 451 */     throw new IllegalArgumentException(String.valueOf(i));
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Object paramObject)
/*     */   {
/* 458 */     if ((paramObject instanceof String))
/* 459 */       return has((String)paramObject, this);
/* 460 */     if ((paramObject instanceof Number)) {
/* 461 */       return has(((Number)paramObject).intValue(), this);
/*     */     }
/* 463 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean containsValue(Object paramObject) {
/* 467 */     for (Iterator localIterator = values().iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 468 */       if ((paramObject == localObject) || ((paramObject != null) && (paramObject.equals(localObject))))
/*     */       {
/* 470 */         return true;
/*     */       }
/*     */     }
/* 473 */     return false;
/*     */   }
/*     */ 
/*     */   public Object remove(Object paramObject) {
/* 477 */     Object localObject = get(paramObject);
/* 478 */     if ((paramObject instanceof String))
/* 479 */       delete((String)paramObject);
/* 480 */     else if ((paramObject instanceof Number)) {
/* 481 */       delete(((Number)paramObject).intValue());
/*     */     }
/* 483 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Set<Object> keySet()
/*     */   {
/* 488 */     return new KeySet();
/*     */   }
/*     */ 
/*     */   public Collection<Object> values() {
/* 492 */     return new ValueCollection();
/*     */   }
/*     */ 
/*     */   public Set<Map.Entry<Object, Object>> entrySet() {
/* 496 */     return new EntrySet();
/*     */   }
/*     */ 
/*     */   public Object put(Object paramObject1, Object paramObject2) {
/* 500 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void putAll(Map paramMap) {
/* 504 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 508 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 655 */     int i = 0; String str = null;
/*     */     int j;
/* 656 */     switch (paramString.length()) { case 7:
/* 657 */       str = "valueOf"; i = 4; break;
/*     */     case 8:
/* 658 */       j = paramString.charAt(3);
/* 659 */       if (j == 111) { str = "toSource"; i = 8;
/* 660 */       } else if (j == 116) { str = "toString"; i = 2; } break;
/*     */     case 11:
/* 662 */       str = "constructor"; i = 1; break;
/*     */     case 13:
/* 663 */       str = "isPrototypeOf"; i = 7; break;
/*     */     case 14:
/* 664 */       j = paramString.charAt(0);
/* 665 */       if (j == 104) { str = "hasOwnProperty"; i = 5;
/* 666 */       } else if (j == 116) { str = "toLocaleString"; i = 3; } break;
/*     */     case 16:
/* 668 */       j = paramString.charAt(2);
/* 669 */       if (j == 100) {
/* 670 */         j = paramString.charAt(8);
/* 671 */         if (j == 71) { str = "__defineGetter__"; i = 9;
/* 672 */         } else if (j == 83) { str = "__defineSetter__"; i = 10; }
/*     */       }
/* 674 */       else if (j == 108) {
/* 675 */         j = paramString.charAt(8);
/* 676 */         if (j == 71) { str = "__lookupGetter__"; i = 11;
/* 677 */         } else if (j == 83) { str = "__lookupSetter__"; i = 12; }  } break;
/*     */     case 20:
/* 680 */       str = "propertyIsEnumerable"; i = 6; break;
/*     */     case 9:
/*     */     case 10:
/*     */     case 12:
/*     */     case 15:
/*     */     case 17:
/*     */     case 18:
/* 682 */     case 19: } if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 686 */     return i;
/*     */   }
/*     */ 
/*     */   class EntrySet extends AbstractSet<Map.Entry<Object, Object>>
/*     */   {
/*     */     EntrySet()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Iterator<Map.Entry<Object, Object>> iterator()
/*     */     {
/* 515 */       return new Iterator() {
/* 516 */         Object[] ids = NativeObject.this.getIds();
/* 517 */         Object key = null;
/* 518 */         int index = 0;
/*     */ 
/*     */         public boolean hasNext() {
/* 521 */           return this.index < this.ids.length;
/*     */         }
/*     */ 
/*     */         public Map.Entry<Object, Object> next() {
/* 525 */           final Object localObject1 = this.key = this.ids[(this.index++)];
/* 526 */           final Object localObject2 = NativeObject.this.get(this.key);
/* 527 */           return new Map.Entry() {
/*     */             public Object getKey() {
/* 529 */               return localObject1;
/*     */             }
/*     */ 
/*     */             public Object getValue() {
/* 533 */               return localObject2;
/*     */             }
/*     */ 
/*     */             public Object setValue(Object paramAnonymous2Object) {
/* 537 */               throw new UnsupportedOperationException();
/*     */             }
/*     */ 
/*     */             public boolean equals(Object paramAnonymous2Object) {
/* 541 */               if (!(paramAnonymous2Object instanceof Map.Entry)) {
/* 542 */                 return false;
/*     */               }
/* 544 */               Map.Entry localEntry = (Map.Entry)paramAnonymous2Object;
/* 545 */               return (localObject1 == null ? localEntry.getKey() == null : localObject1.equals(localEntry.getKey())) && (localObject2 == null ? localEntry.getValue() == null : localObject2.equals(localEntry.getValue()));
/*     */             }
/*     */ 
/*     */             public int hashCode()
/*     */             {
/* 550 */               return (localObject1 == null ? 0 : localObject1.hashCode()) ^ (localObject2 == null ? 0 : localObject2.hashCode());
/*     */             }
/*     */ 
/*     */             public String toString()
/*     */             {
/* 555 */               return localObject1 + "=" + localObject2;
/*     */             }
/*     */           };
/*     */         }
/*     */ 
/*     */         public void remove() {
/* 561 */           if (this.key == null) {
/* 562 */             throw new IllegalStateException();
/*     */           }
/* 564 */           NativeObject.this.remove(this.key);
/* 565 */           this.key = null;
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*     */     public int size()
/*     */     {
/* 572 */       return NativeObject.this.size();
/*     */     }
/*     */   }
/*     */ 
/*     */   class KeySet extends AbstractSet<Object> {
/*     */     KeySet() {
/*     */     }
/*     */ 
/* 580 */     public boolean contains(Object paramObject) { return NativeObject.this.containsKey(paramObject); }
/*     */ 
/*     */ 
/*     */     public Iterator<Object> iterator()
/*     */     {
/* 585 */       return new Iterator() {
/* 586 */         Object[] ids = NativeObject.this.getIds();
/*     */         Object key;
/* 588 */         int index = 0;
/*     */ 
/*     */         public boolean hasNext() {
/* 591 */           return this.index < this.ids.length;
/*     */         }
/*     */ 
/*     */         public Object next() {
/* 595 */           return this.key = this.ids[(this.index++)];
/*     */         }
/*     */ 
/*     */         public void remove() {
/* 599 */           if (this.key == null) {
/* 600 */             throw new IllegalStateException();
/*     */           }
/* 602 */           NativeObject.this.remove(this.key);
/* 603 */           this.key = null;
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*     */     public int size()
/*     */     {
/* 610 */       return NativeObject.this.size();
/*     */     }
/*     */   }
/*     */ 
/*     */   class ValueCollection extends AbstractCollection<Object> {
/*     */     ValueCollection() {
/*     */     }
/*     */ 
/* 618 */     public Iterator<Object> iterator() { return new Iterator() {
/* 619 */         Object[] ids = NativeObject.this.getIds();
/*     */         Object key;
/* 621 */         int index = 0;
/*     */ 
/*     */         public boolean hasNext() {
/* 624 */           return this.index < this.ids.length;
/*     */         }
/*     */ 
/*     */         public Object next() {
/* 628 */           return NativeObject.this.get(this.key = this.ids[(this.index++)]);
/*     */         }
/*     */ 
/*     */         public void remove() {
/* 632 */           if (this.key == null) {
/* 633 */             throw new IllegalStateException();
/*     */           }
/* 635 */           NativeObject.this.remove(this.key);
/* 636 */           this.key = null;
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*     */     public int size()
/*     */     {
/* 643 */       return NativeObject.this.size();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeObject
 * JD-Core Version:    0.6.2
 */