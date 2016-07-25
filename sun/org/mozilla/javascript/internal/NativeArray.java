/*      */ package sun.org.mozilla.javascript.internal;
/*      */ 
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ 
/*      */ public class NativeArray extends IdScriptableObject
/*      */   implements List
/*      */ {
/*   72 */   private static final Object ARRAY_TAG = "Array";
/*   73 */   private static final Integer NEGATIVE_ONE = Integer.valueOf(-1);
/*      */   private static final int Id_length = 1;
/*      */   private static final int MAX_INSTANCE_ID = 1;
/*      */   private static final int Id_constructor = 1;
/*      */   private static final int Id_toString = 2;
/*      */   private static final int Id_toLocaleString = 3;
/*      */   private static final int Id_toSource = 4;
/*      */   private static final int Id_join = 5;
/*      */   private static final int Id_reverse = 6;
/*      */   private static final int Id_sort = 7;
/*      */   private static final int Id_push = 8;
/*      */   private static final int Id_pop = 9;
/*      */   private static final int Id_shift = 10;
/*      */   private static final int Id_unshift = 11;
/*      */   private static final int Id_splice = 12;
/*      */   private static final int Id_concat = 13;
/*      */   private static final int Id_slice = 14;
/*      */   private static final int Id_indexOf = 15;
/*      */   private static final int Id_lastIndexOf = 16;
/*      */   private static final int Id_every = 17;
/*      */   private static final int Id_filter = 18;
/*      */   private static final int Id_forEach = 19;
/*      */   private static final int Id_map = 20;
/*      */   private static final int Id_some = 21;
/*      */   private static final int Id_reduce = 22;
/*      */   private static final int Id_reduceRight = 23;
/*      */   private static final int MAX_PROTOTYPE_ID = 23;
/*      */   private static final int ConstructorId_join = -5;
/*      */   private static final int ConstructorId_reverse = -6;
/*      */   private static final int ConstructorId_sort = -7;
/*      */   private static final int ConstructorId_push = -8;
/*      */   private static final int ConstructorId_pop = -9;
/*      */   private static final int ConstructorId_shift = -10;
/*      */   private static final int ConstructorId_unshift = -11;
/*      */   private static final int ConstructorId_splice = -12;
/*      */   private static final int ConstructorId_concat = -13;
/*      */   private static final int ConstructorId_slice = -14;
/*      */   private static final int ConstructorId_indexOf = -15;
/*      */   private static final int ConstructorId_lastIndexOf = -16;
/*      */   private static final int ConstructorId_every = -17;
/*      */   private static final int ConstructorId_filter = -18;
/*      */   private static final int ConstructorId_forEach = -19;
/*      */   private static final int ConstructorId_map = -20;
/*      */   private static final int ConstructorId_some = -21;
/*      */   private static final int ConstructorId_reduce = -22;
/*      */   private static final int ConstructorId_reduceRight = -23;
/*      */   private static final int ConstructorId_isArray = -24;
/*      */   private long length;
/*      */   private Object[] dense;
/*      */   private boolean denseOnly;
/* 1973 */   private static int maximumInitialCapacity = 10000;
/*      */   private static final int DEFAULT_INITIAL_CAPACITY = 10;
/*      */   private static final double GROW_FACTOR = 1.5D;
/*      */   private static final int MAX_PRE_GROW_SIZE = 1431655764;
/*      */ 
/*      */   static void init(Scriptable paramScriptable, boolean paramBoolean)
/*      */   {
/*   77 */     NativeArray localNativeArray = new NativeArray(0L);
/*   78 */     localNativeArray.exportAsJSClass(23, paramScriptable, paramBoolean);
/*      */   }
/*      */ 
/*      */   static int getMaximumInitialCapacity() {
/*   82 */     return maximumInitialCapacity;
/*      */   }
/*      */ 
/*      */   static void setMaximumInitialCapacity(int paramInt) {
/*   86 */     maximumInitialCapacity = paramInt;
/*      */   }
/*      */ 
/*      */   public NativeArray(long paramLong)
/*      */   {
/*   91 */     this.denseOnly = (paramLong <= maximumInitialCapacity);
/*   92 */     if (this.denseOnly) {
/*   93 */       int i = (int)paramLong;
/*   94 */       if (i < 10)
/*   95 */         i = 10;
/*   96 */       this.dense = new Object[i];
/*   97 */       Arrays.fill(this.dense, Scriptable.NOT_FOUND);
/*      */     }
/*   99 */     this.length = paramLong;
/*      */   }
/*      */ 
/*      */   public NativeArray(Object[] paramArrayOfObject)
/*      */   {
/*  104 */     this.denseOnly = true;
/*  105 */     this.dense = paramArrayOfObject;
/*  106 */     this.length = paramArrayOfObject.length;
/*      */   }
/*      */ 
/*      */   public String getClassName()
/*      */   {
/*  112 */     return "Array";
/*      */   }
/*      */ 
/*      */   protected int getMaxInstanceId()
/*      */   {
/*  122 */     return 1;
/*      */   }
/*      */ 
/*      */   protected int findInstanceIdInfo(String paramString)
/*      */   {
/*  128 */     if (paramString.equals("length")) {
/*  129 */       return instanceIdInfo(6, 1);
/*      */     }
/*  131 */     return super.findInstanceIdInfo(paramString);
/*      */   }
/*      */ 
/*      */   protected String getInstanceIdName(int paramInt)
/*      */   {
/*  137 */     if (paramInt == 1) return "length";
/*  138 */     return super.getInstanceIdName(paramInt);
/*      */   }
/*      */ 
/*      */   protected Object getInstanceIdValue(int paramInt)
/*      */   {
/*  144 */     if (paramInt == 1) {
/*  145 */       return ScriptRuntime.wrapNumber(this.length);
/*      */     }
/*  147 */     return super.getInstanceIdValue(paramInt);
/*      */   }
/*      */ 
/*      */   protected void setInstanceIdValue(int paramInt, Object paramObject)
/*      */   {
/*  153 */     if (paramInt == 1) {
/*  154 */       setLength(paramObject); return;
/*      */     }
/*  156 */     super.setInstanceIdValue(paramInt, paramObject);
/*      */   }
/*      */ 
/*      */   protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject)
/*      */   {
/*  162 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -5, "join", 1);
/*      */ 
/*  164 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -6, "reverse", 0);
/*      */ 
/*  166 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -7, "sort", 1);
/*      */ 
/*  168 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -8, "push", 1);
/*      */ 
/*  170 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -9, "pop", 0);
/*      */ 
/*  172 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -10, "shift", 0);
/*      */ 
/*  174 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -11, "unshift", 1);
/*      */ 
/*  176 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -12, "splice", 2);
/*      */ 
/*  178 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -13, "concat", 1);
/*      */ 
/*  180 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -14, "slice", 2);
/*      */ 
/*  182 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -15, "indexOf", 1);
/*      */ 
/*  184 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -16, "lastIndexOf", 1);
/*      */ 
/*  186 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -17, "every", 1);
/*      */ 
/*  188 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -18, "filter", 1);
/*      */ 
/*  190 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -19, "forEach", 1);
/*      */ 
/*  192 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -20, "map", 1);
/*      */ 
/*  194 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -21, "some", 1);
/*      */ 
/*  196 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -22, "reduce", 1);
/*      */ 
/*  198 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -23, "reduceRight", 1);
/*      */ 
/*  200 */     addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -24, "isArray", 1);
/*      */ 
/*  202 */     super.fillConstructorProperties(paramIdFunctionObject);
/*      */   }
/*      */ 
/*      */   protected void initPrototypeId(int paramInt)
/*      */   {
/*      */     int i;
/*      */     String str;
/*  210 */     switch (paramInt) { case 1:
/*  211 */       i = 1; str = "constructor"; break;
/*      */     case 2:
/*  212 */       i = 0; str = "toString"; break;
/*      */     case 3:
/*  213 */       i = 0; str = "toLocaleString"; break;
/*      */     case 4:
/*  214 */       i = 0; str = "toSource"; break;
/*      */     case 5:
/*  215 */       i = 1; str = "join"; break;
/*      */     case 6:
/*  216 */       i = 0; str = "reverse"; break;
/*      */     case 7:
/*  217 */       i = 1; str = "sort"; break;
/*      */     case 8:
/*  218 */       i = 1; str = "push"; break;
/*      */     case 9:
/*  219 */       i = 0; str = "pop"; break;
/*      */     case 10:
/*  220 */       i = 0; str = "shift"; break;
/*      */     case 11:
/*  221 */       i = 1; str = "unshift"; break;
/*      */     case 12:
/*  222 */       i = 2; str = "splice"; break;
/*      */     case 13:
/*  223 */       i = 1; str = "concat"; break;
/*      */     case 14:
/*  224 */       i = 2; str = "slice"; break;
/*      */     case 15:
/*  225 */       i = 1; str = "indexOf"; break;
/*      */     case 16:
/*  226 */       i = 1; str = "lastIndexOf"; break;
/*      */     case 17:
/*  227 */       i = 1; str = "every"; break;
/*      */     case 18:
/*  228 */       i = 1; str = "filter"; break;
/*      */     case 19:
/*  229 */       i = 1; str = "forEach"; break;
/*      */     case 20:
/*  230 */       i = 1; str = "map"; break;
/*      */     case 21:
/*  231 */       i = 1; str = "some"; break;
/*      */     case 22:
/*  232 */       i = 1; str = "reduce"; break;
/*      */     case 23:
/*  233 */       i = 1; str = "reduceRight"; break;
/*      */     default:
/*  234 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*      */     }
/*  236 */     initPrototypeMethod(ARRAY_TAG, paramInt, str, i);
/*      */   }
/*      */ 
/*      */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*      */   {
/*  243 */     if (!paramIdFunctionObject.hasTag(ARRAY_TAG)) {
/*  244 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*      */     }
/*  246 */     int i = paramIdFunctionObject.methodId();
/*      */     while (true)
/*      */     {
/*  249 */       switch (i) {
/*      */       case -23:
/*      */       case -22:
/*      */       case -21:
/*      */       case -20:
/*      */       case -19:
/*      */       case -18:
/*      */       case -17:
/*      */       case -16:
/*      */       case -15:
/*      */       case -14:
/*      */       case -13:
/*      */       case -12:
/*      */       case -11:
/*      */       case -10:
/*      */       case -9:
/*      */       case -8:
/*      */       case -7:
/*      */       case -6:
/*      */       case -5:
/*  269 */         if (paramArrayOfObject.length > 0) {
/*  270 */           paramScriptable2 = ScriptRuntime.toObject(paramScriptable1, paramArrayOfObject[0]);
/*  271 */           Object[] arrayOfObject = new Object[paramArrayOfObject.length - 1];
/*  272 */           for (int k = 0; k < arrayOfObject.length; k++)
/*  273 */             arrayOfObject[k] = paramArrayOfObject[(k + 1)];
/*  274 */           paramArrayOfObject = arrayOfObject;
/*      */         }
/*  276 */         i = -i;
/*      */       case -24:
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 20:
/*      */       case 21:
/*      */       case 22:
/*      */       case 23:
/*      */       case -4:
/*      */       case -3:
/*      */       case -2:
/*      */       case -1:
/*  281 */       case 0: }  } return Boolean.valueOf((paramArrayOfObject.length > 0) && ((paramArrayOfObject[0] instanceof NativeArray)));
/*      */ 
/*  284 */     int j = paramScriptable2 == null ? 1 : 0;
/*  285 */     if (j == 0)
/*      */     {
/*  287 */       return paramIdFunctionObject.construct(paramContext, paramScriptable1, paramArrayOfObject);
/*      */     }
/*  289 */     return jsConstructor(paramContext, paramScriptable1, paramArrayOfObject);
/*      */ 
/*  293 */     return toStringHelper(paramContext, paramScriptable1, paramScriptable2, paramContext.hasFeature(4), false);
/*      */ 
/*  297 */     return toStringHelper(paramContext, paramScriptable1, paramScriptable2, false, true);
/*      */ 
/*  300 */     return toStringHelper(paramContext, paramScriptable1, paramScriptable2, true, false);
/*      */ 
/*  303 */     return js_join(paramContext, paramScriptable2, paramArrayOfObject);
/*      */ 
/*  306 */     return js_reverse(paramContext, paramScriptable2, paramArrayOfObject);
/*      */ 
/*  309 */     return js_sort(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*      */ 
/*  312 */     return js_push(paramContext, paramScriptable2, paramArrayOfObject);
/*      */ 
/*  315 */     return js_pop(paramContext, paramScriptable2, paramArrayOfObject);
/*      */ 
/*  318 */     return js_shift(paramContext, paramScriptable2, paramArrayOfObject);
/*      */ 
/*  321 */     return js_unshift(paramContext, paramScriptable2, paramArrayOfObject);
/*      */ 
/*  324 */     return js_splice(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*      */ 
/*  327 */     return js_concat(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*      */ 
/*  330 */     return js_slice(paramContext, paramScriptable2, paramArrayOfObject);
/*      */ 
/*  333 */     return indexOfHelper(paramContext, paramScriptable2, paramArrayOfObject, false);
/*      */ 
/*  336 */     return indexOfHelper(paramContext, paramScriptable2, paramArrayOfObject, true);
/*      */ 
/*  343 */     return iterativeMethod(paramContext, i, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*      */ 
/*  346 */     return reduceMethod(paramContext, i, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*      */ 
/*  348 */     throw new IllegalArgumentException(String.valueOf(i));
/*      */   }
/*      */ 
/*      */   public Object get(int paramInt, Scriptable paramScriptable)
/*      */   {
/*  355 */     if ((!this.denseOnly) && (isGetterOrSetter(null, paramInt, false)))
/*  356 */       return super.get(paramInt, paramScriptable);
/*  357 */     if ((this.dense != null) && (0 <= paramInt) && (paramInt < this.dense.length))
/*  358 */       return this.dense[paramInt];
/*  359 */     return super.get(paramInt, paramScriptable);
/*      */   }
/*      */ 
/*      */   public boolean has(int paramInt, Scriptable paramScriptable)
/*      */   {
/*  365 */     if ((!this.denseOnly) && (isGetterOrSetter(null, paramInt, false)))
/*  366 */       return super.has(paramInt, paramScriptable);
/*  367 */     if ((this.dense != null) && (0 <= paramInt) && (paramInt < this.dense.length))
/*  368 */       return this.dense[paramInt] != NOT_FOUND;
/*  369 */     return super.has(paramInt, paramScriptable);
/*      */   }
/*      */ 
/*      */   private static long toArrayIndex(String paramString)
/*      */   {
/*  376 */     double d = ScriptRuntime.toNumber(paramString);
/*  377 */     if (d == d) {
/*  378 */       long l = ScriptRuntime.toUint32(d);
/*  379 */       if ((l == d) && (l != 4294967295L))
/*      */       {
/*  382 */         if (Long.toString(l).equals(paramString)) {
/*  383 */           return l;
/*      */         }
/*      */       }
/*      */     }
/*  387 */     return -1L;
/*      */   }
/*      */ 
/*      */   public void put(String paramString, Scriptable paramScriptable, Object paramObject)
/*      */   {
/*  393 */     super.put(paramString, paramScriptable, paramObject);
/*  394 */     if (paramScriptable == this)
/*      */     {
/*  396 */       long l = toArrayIndex(paramString);
/*  397 */       if (l >= this.length) {
/*  398 */         this.length = (l + 1L);
/*  399 */         this.denseOnly = false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean ensureCapacity(int paramInt)
/*      */   {
/*  406 */     if (paramInt > this.dense.length) {
/*  407 */       if (paramInt > 1431655764) {
/*  408 */         this.denseOnly = false;
/*  409 */         return false;
/*      */       }
/*  411 */       paramInt = Math.max(paramInt, (int)(this.dense.length * 1.5D));
/*  412 */       Object[] arrayOfObject = new Object[paramInt];
/*  413 */       System.arraycopy(this.dense, 0, arrayOfObject, 0, this.dense.length);
/*  414 */       Arrays.fill(arrayOfObject, this.dense.length, arrayOfObject.length, Scriptable.NOT_FOUND);
/*      */ 
/*  416 */       this.dense = arrayOfObject;
/*      */     }
/*  418 */     return true;
/*      */   }
/*      */ 
/*      */   public void put(int paramInt, Scriptable paramScriptable, Object paramObject)
/*      */   {
/*  424 */     if ((paramScriptable == this) && (!isSealed()) && (this.dense != null) && (0 <= paramInt) && ((this.denseOnly) || (!isGetterOrSetter(null, paramInt, true))))
/*      */     {
/*  427 */       if (paramInt < this.dense.length) {
/*  428 */         this.dense[paramInt] = paramObject;
/*  429 */         if (this.length <= paramInt)
/*  430 */           this.length = (paramInt + 1L);
/*  431 */         return;
/*  432 */       }if ((this.denseOnly) && (paramInt < this.dense.length * 1.5D) && (ensureCapacity(paramInt + 1)))
/*      */       {
/*  435 */         this.dense[paramInt] = paramObject;
/*  436 */         this.length = (paramInt + 1L);
/*  437 */         return;
/*      */       }
/*  439 */       this.denseOnly = false;
/*      */     }
/*      */ 
/*  442 */     super.put(paramInt, paramScriptable, paramObject);
/*  443 */     if (paramScriptable == this)
/*      */     {
/*  445 */       if (this.length <= paramInt)
/*      */       {
/*  447 */         this.length = (paramInt + 1L);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void delete(int paramInt)
/*      */   {
/*  455 */     if ((this.dense != null) && (0 <= paramInt) && (paramInt < this.dense.length) && (!isSealed()) && ((this.denseOnly) || (!isGetterOrSetter(null, paramInt, true))))
/*      */     {
/*  458 */       this.dense[paramInt] = NOT_FOUND;
/*      */     }
/*  460 */     else super.delete(paramInt);
/*      */   }
/*      */ 
/*      */   public Object[] getIds()
/*      */   {
/*  467 */     Object[] arrayOfObject1 = super.getIds();
/*  468 */     if (this.dense == null) return arrayOfObject1;
/*  469 */     int i = this.dense.length;
/*  470 */     long l = this.length;
/*  471 */     if (i > l) {
/*  472 */       i = (int)l;
/*      */     }
/*  474 */     if (i == 0) return arrayOfObject1;
/*  475 */     int j = arrayOfObject1.length;
/*  476 */     Object localObject = new Object[i + j];
/*      */ 
/*  478 */     int k = 0;
/*  479 */     for (int m = 0; m != i; m++)
/*      */     {
/*  481 */       if (this.dense[m] != NOT_FOUND) {
/*  482 */         localObject[k] = Integer.valueOf(m);
/*  483 */         k++;
/*      */       }
/*      */     }
/*  486 */     if (k != i)
/*      */     {
/*  488 */       Object[] arrayOfObject2 = new Object[k + j];
/*  489 */       System.arraycopy(localObject, 0, arrayOfObject2, 0, k);
/*  490 */       localObject = arrayOfObject2;
/*      */     }
/*  492 */     System.arraycopy(arrayOfObject1, 0, localObject, k, j);
/*  493 */     return localObject;
/*      */   }
/*      */ 
/*      */   public Object[] getAllIds()
/*      */   {
/*  499 */     LinkedHashSet localLinkedHashSet = new LinkedHashSet(Arrays.asList(getIds()));
/*      */ 
/*  501 */     localLinkedHashSet.addAll(Arrays.asList(super.getAllIds()));
/*  502 */     return localLinkedHashSet.toArray();
/*      */   }
/*      */ 
/*      */   public Integer[] getIndexIds() {
/*  506 */     Object[] arrayOfObject1 = getIds();
/*  507 */     ArrayList localArrayList = new ArrayList(arrayOfObject1.length);
/*  508 */     for (Object localObject : arrayOfObject1) {
/*  509 */       int k = ScriptRuntime.toInt32(localObject);
/*  510 */       if ((k >= 0) && (ScriptRuntime.toString(k).equals(ScriptRuntime.toString(localObject)))) {
/*  511 */         localArrayList.add(Integer.valueOf(k));
/*      */       }
/*      */     }
/*  514 */     return (Integer[])localArrayList.toArray(new Integer[localArrayList.size()]);
/*      */   }
/*      */ 
/*      */   public Object getDefaultValue(Class<?> paramClass)
/*      */   {
/*  520 */     if (paramClass == ScriptRuntime.NumberClass) {
/*  521 */       Context localContext = Context.getContext();
/*  522 */       if (localContext.getLanguageVersion() == 120)
/*  523 */         return Long.valueOf(this.length);
/*      */     }
/*  525 */     return super.getDefaultValue(paramClass);
/*      */   }
/*      */ 
/*      */   private ScriptableObject defaultIndexPropertyDescriptor(Object paramObject) {
/*  529 */     Object localObject = getParentScope();
/*  530 */     if (localObject == null) localObject = this;
/*  531 */     NativeObject localNativeObject = new NativeObject();
/*  532 */     ScriptRuntime.setObjectProtoAndParent(localNativeObject, (Scriptable)localObject);
/*  533 */     localNativeObject.defineProperty("value", paramObject, 0);
/*  534 */     localNativeObject.defineProperty("writable", Boolean.valueOf(true), 0);
/*  535 */     localNativeObject.defineProperty("enumerable", Boolean.valueOf(true), 0);
/*  536 */     localNativeObject.defineProperty("configurable", Boolean.valueOf(true), 0);
/*  537 */     return localNativeObject;
/*      */   }
/*      */ 
/*      */   protected ScriptableObject getOwnPropertyDescriptor(Context paramContext, Object paramObject)
/*      */   {
/*  542 */     if (this.dense != null) {
/*  543 */       int i = toIndex(paramObject);
/*  544 */       if ((0 <= i) && (i < this.length)) {
/*  545 */         Object localObject = this.dense[i];
/*  546 */         return defaultIndexPropertyDescriptor(localObject);
/*      */       }
/*      */     }
/*  549 */     return super.getOwnPropertyDescriptor(paramContext, paramObject);
/*      */   }
/*      */ 
/*      */   public void defineOwnProperty(Context paramContext, Object paramObject, ScriptableObject paramScriptableObject)
/*      */   {
/*  554 */     if (this.dense != null) {
/*  555 */       Object[] arrayOfObject = this.dense;
/*  556 */       this.dense = null;
/*  557 */       this.denseOnly = false;
/*  558 */       for (int j = 0; j < arrayOfObject.length; j++) {
/*  559 */         if (arrayOfObject[j] != NOT_FOUND) {
/*  560 */           put(j, this, arrayOfObject[j]);
/*      */         }
/*      */       }
/*      */     }
/*  564 */     int i = toIndex(paramObject);
/*  565 */     if (i >= this.length) {
/*  566 */       this.length = (i + 1);
/*      */     }
/*  568 */     super.defineOwnProperty(paramContext, paramObject, paramScriptableObject);
/*      */   }
/*      */ 
/*      */   private int toIndex(Object paramObject) {
/*  572 */     if ((paramObject instanceof String))
/*  573 */       return (int)toArrayIndex((String)paramObject);
/*  574 */     if ((paramObject instanceof Number)) {
/*  575 */       return ((Number)paramObject).intValue();
/*      */     }
/*  577 */     return -1;
/*      */   }
/*      */ 
/*      */   private static Object jsConstructor(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*      */   {
/*  587 */     if (paramArrayOfObject.length == 0) {
/*  588 */       return new NativeArray(0L);
/*      */     }
/*      */ 
/*  593 */     if (paramContext.getLanguageVersion() == 120) {
/*  594 */       return new NativeArray(paramArrayOfObject);
/*      */     }
/*  596 */     Object localObject = paramArrayOfObject[0];
/*  597 */     if ((paramArrayOfObject.length > 1) || (!(localObject instanceof Number))) {
/*  598 */       return new NativeArray(paramArrayOfObject);
/*      */     }
/*  600 */     long l = ScriptRuntime.toUint32(localObject);
/*  601 */     if (l != ((Number)localObject).doubleValue()) {
/*  602 */       String str = ScriptRuntime.getMessage0("msg.arraylength.bad");
/*  603 */       throw ScriptRuntime.constructError("RangeError", str);
/*      */     }
/*  605 */     return new NativeArray(l);
/*      */   }
/*      */ 
/*      */   public long getLength()
/*      */   {
/*  611 */     return this.length;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public long jsGet_length() {
/*  616 */     return getLength();
/*      */   }
/*      */ 
/*      */   void setDenseOnly(boolean paramBoolean)
/*      */   {
/*  629 */     if ((paramBoolean) && (!this.denseOnly))
/*  630 */       throw new IllegalArgumentException();
/*  631 */     this.denseOnly = paramBoolean;
/*      */   }
/*      */ 
/*      */   private void setLength(Object paramObject)
/*      */   {
/*  642 */     double d = ScriptRuntime.toNumber(paramObject);
/*  643 */     long l1 = ScriptRuntime.toUint32(d);
/*      */     Object localObject1;
/*  644 */     if (l1 != d) {
/*  645 */       localObject1 = ScriptRuntime.getMessage0("msg.arraylength.bad");
/*  646 */       throw ScriptRuntime.constructError("RangeError", (String)localObject1);
/*      */     }
/*      */ 
/*  649 */     if (this.denseOnly) {
/*  650 */       if (l1 < this.length)
/*      */       {
/*  652 */         Arrays.fill(this.dense, (int)l1, this.dense.length, NOT_FOUND);
/*  653 */         this.length = l1;
/*  654 */         return;
/*  655 */       }if ((l1 < 1431655764L) && (l1 < this.length * 1.5D) && (ensureCapacity((int)l1)))
/*      */       {
/*  659 */         this.length = l1;
/*  660 */         return;
/*      */       }
/*  662 */       this.denseOnly = false;
/*      */     }
/*      */ 
/*  665 */     if (l1 < this.length)
/*      */     {
/*  667 */       if (this.length - l1 > 4096L)
/*      */       {
/*  669 */         localObject1 = getIds();
/*  670 */         for (int i = 0; i < localObject1.length; i++) {
/*  671 */           Object localObject2 = localObject1[i];
/*  672 */           if ((localObject2 instanceof String))
/*      */           {
/*  674 */             String str = (String)localObject2;
/*  675 */             long l3 = toArrayIndex(str);
/*  676 */             if (l3 >= l1)
/*  677 */               delete(str);
/*      */           } else {
/*  679 */             int j = ((Integer)localObject2).intValue();
/*  680 */             if (j >= l1)
/*  681 */               delete(j);
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/*  686 */         for (long l2 = l1; l2 < this.length; l2 += 1L) {
/*  687 */           deleteElem(this, l2);
/*      */         }
/*      */       }
/*      */     }
/*  691 */     this.length = l1;
/*      */   }
/*      */ 
/*      */   static long getLengthProperty(Context paramContext, Scriptable paramScriptable)
/*      */   {
/*  702 */     if ((paramScriptable instanceof NativeString))
/*  703 */       return ((NativeString)paramScriptable).getLength();
/*  704 */     if ((paramScriptable instanceof NativeArray)) {
/*  705 */       return ((NativeArray)paramScriptable).getLength();
/*      */     }
/*  707 */     return ScriptRuntime.toUint32(ScriptRuntime.getObjectProp(paramScriptable, "length", paramContext));
/*      */   }
/*      */ 
/*      */   private static Object setLengthProperty(Context paramContext, Scriptable paramScriptable, long paramLong)
/*      */   {
/*  714 */     return ScriptRuntime.setObjectProp(paramScriptable, "length", ScriptRuntime.wrapNumber(paramLong), paramContext);
/*      */   }
/*      */ 
/*      */   private static void deleteElem(Scriptable paramScriptable, long paramLong)
/*      */   {
/*  724 */     int i = (int)paramLong;
/*  725 */     if (i == paramLong) paramScriptable.delete(i); else
/*  726 */       paramScriptable.delete(Long.toString(paramLong));
/*      */   }
/*      */ 
/*      */   private static Object getElem(Context paramContext, Scriptable paramScriptable, long paramLong)
/*      */   {
/*  731 */     if (paramLong > 2147483647L) {
/*  732 */       String str = Long.toString(paramLong);
/*  733 */       return ScriptRuntime.getObjectProp(paramScriptable, str, paramContext);
/*      */     }
/*  735 */     return ScriptRuntime.getObjectIndex(paramScriptable, (int)paramLong, paramContext);
/*      */   }
/*      */ 
/*      */   private static Object getRawElem(Scriptable paramScriptable, long paramLong)
/*      */   {
/*  741 */     if (paramLong > 2147483647L) {
/*  742 */       return ScriptableObject.getProperty(paramScriptable, Long.toString(paramLong));
/*      */     }
/*  744 */     return ScriptableObject.getProperty(paramScriptable, (int)paramLong);
/*      */   }
/*      */ 
/*      */   private static void setElem(Context paramContext, Scriptable paramScriptable, long paramLong, Object paramObject)
/*      */   {
/*  751 */     if (paramLong > 2147483647L) {
/*  752 */       String str = Long.toString(paramLong);
/*  753 */       ScriptRuntime.setObjectProp(paramScriptable, str, paramObject, paramContext);
/*      */     } else {
/*  755 */       ScriptRuntime.setObjectIndex(paramScriptable, (int)paramLong, paramObject, paramContext);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static String toStringHelper(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  767 */     long l1 = getLengthProperty(paramContext, paramScriptable2);
/*      */ 
/*  769 */     StringBuilder localStringBuilder = new StringBuilder(256);
/*      */     String str;
/*  774 */     if (paramBoolean1) {
/*  775 */       localStringBuilder.append('[');
/*  776 */       str = ", ";
/*      */     } else {
/*  778 */       str = ",";
/*      */     }
/*      */ 
/*  781 */     int i = 0;
/*  782 */     long l2 = 0L;
/*      */     int j;
/*      */     boolean bool;
/*  785 */     if (paramContext.iterating == null) {
/*  786 */       j = 1;
/*  787 */       bool = false;
/*  788 */       paramContext.iterating = new ObjToIntMap(31);
/*      */     } else {
/*  790 */       j = 0;
/*  791 */       bool = paramContext.iterating.has(paramScriptable2);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  797 */       if (!bool) {
/*  798 */         paramContext.iterating.put(paramScriptable2, 0);
/*  799 */         for (l2 = 0L; l2 < l1; l2 += 1L) {
/*  800 */           if (l2 > 0L) localStringBuilder.append(str);
/*  801 */           Object localObject1 = getElem(paramContext, paramScriptable2, l2);
/*  802 */           if ((localObject1 == null) || (localObject1 == Undefined.instance)) {
/*  803 */             i = 0;
/*      */           }
/*      */           else {
/*  806 */             i = 1;
/*      */ 
/*  808 */             if (paramBoolean1) {
/*  809 */               localStringBuilder.append(ScriptRuntime.uneval(paramContext, paramScriptable1, localObject1));
/*      */             }
/*      */             else
/*      */             {
/*      */               Object localObject2;
/*  811 */               if ((localObject1 instanceof String)) {
/*  812 */                 localObject2 = (String)localObject1;
/*  813 */                 if (paramBoolean1) {
/*  814 */                   localStringBuilder.append('"');
/*  815 */                   localStringBuilder.append(ScriptRuntime.escapeString((String)localObject2));
/*  816 */                   localStringBuilder.append('"');
/*      */                 } else {
/*  818 */                   localStringBuilder.append((String)localObject2);
/*      */                 }
/*      */               }
/*      */               else {
/*  822 */                 if (paramBoolean2)
/*      */                 {
/*  826 */                   localObject2 = ScriptRuntime.getPropFunctionAndThis(localObject1, "toLocaleString", paramContext);
/*      */ 
/*  828 */                   Scriptable localScriptable = ScriptRuntime.lastStoredScriptable(paramContext);
/*  829 */                   localObject1 = ((Callable)localObject2).call(paramContext, paramScriptable1, localScriptable, ScriptRuntime.emptyArgs);
/*      */                 }
/*      */ 
/*  832 */                 localStringBuilder.append(ScriptRuntime.toString(localObject1));
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       } } finally { if (j != 0) {
/*  838 */         paramContext.iterating = null;
/*      */       }
/*      */     }
/*      */ 
/*  842 */     if (paramBoolean1)
/*      */     {
/*  844 */       if ((i == 0) && (l2 > 0L))
/*  845 */         localStringBuilder.append(", ]");
/*      */       else
/*  847 */         localStringBuilder.append(']');
/*      */     }
/*  849 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private static String js_join(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*      */   {
/*  858 */     long l = getLengthProperty(paramContext, paramScriptable);
/*  859 */     int i = (int)l;
/*  860 */     if (l != i) {
/*  861 */       throw Context.reportRuntimeError1("msg.arraylength.too.big", String.valueOf(l));
/*      */     }
/*      */ 
/*  865 */     String str1 = (paramArrayOfObject.length < 1) || (paramArrayOfObject[0] == Undefined.instance) ? "," : ScriptRuntime.toString(paramArrayOfObject[0]);
/*      */     Object localObject2;
/*  868 */     if ((paramScriptable instanceof NativeArray)) {
/*  869 */       localObject1 = (NativeArray)paramScriptable;
/*  870 */       if (((NativeArray)localObject1).denseOnly) {
/*  871 */         StringBuilder localStringBuilder1 = new StringBuilder();
/*  872 */         for (k = 0; k < i; k++) {
/*  873 */           if (k != 0) {
/*  874 */             localStringBuilder1.append(str1);
/*      */           }
/*  876 */           if (k < ((NativeArray)localObject1).dense.length) {
/*  877 */             localObject2 = localObject1.dense[k];
/*  878 */             if ((localObject2 != null) && (localObject2 != Undefined.instance) && (localObject2 != Scriptable.NOT_FOUND))
/*      */             {
/*  881 */               localStringBuilder1.append(ScriptRuntime.toString(localObject2));
/*      */             }
/*      */           }
/*      */         }
/*  885 */         return localStringBuilder1.toString();
/*      */       }
/*      */     }
/*  888 */     if (i == 0) {
/*  889 */       return "";
/*      */     }
/*  891 */     Object localObject1 = new String[i];
/*  892 */     int j = 0;
/*      */     String str2;
/*  893 */     for (int k = 0; k != i; k++) {
/*  894 */       localObject2 = getElem(paramContext, paramScriptable, k);
/*  895 */       if ((localObject2 != null) && (localObject2 != Undefined.instance)) {
/*  896 */         str2 = ScriptRuntime.toString(localObject2);
/*  897 */         j += str2.length();
/*  898 */         localObject1[k] = str2;
/*      */       }
/*      */     }
/*  901 */     j += (i - 1) * str1.length();
/*  902 */     StringBuilder localStringBuilder2 = new StringBuilder(j);
/*  903 */     for (int m = 0; m != i; m++) {
/*  904 */       if (m != 0) {
/*  905 */         localStringBuilder2.append(str1);
/*      */       }
/*  907 */       str2 = localObject1[m];
/*  908 */       if (str2 != null)
/*      */       {
/*  910 */         localStringBuilder2.append(str2);
/*      */       }
/*      */     }
/*  913 */     return localStringBuilder2.toString();
/*      */   }
/*      */ 
/*      */   private static Scriptable js_reverse(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*      */   {
/*  922 */     if ((paramScriptable instanceof NativeArray)) {
/*  923 */       NativeArray localNativeArray = (NativeArray)paramScriptable;
/*  924 */       if (localNativeArray.denseOnly) {
/*  925 */         int i = 0; for (int j = (int)localNativeArray.length - 1; i < j; j--) {
/*  926 */           Object localObject1 = localNativeArray.dense[i];
/*  927 */           localNativeArray.dense[i] = localNativeArray.dense[j];
/*  928 */           localNativeArray.dense[j] = localObject1;
/*      */ 
/*  925 */           i++;
/*      */         }
/*      */ 
/*  930 */         return paramScriptable;
/*      */       }
/*      */     }
/*  933 */     long l1 = getLengthProperty(paramContext, paramScriptable);
/*      */ 
/*  935 */     long l2 = l1 / 2L;
/*  936 */     for (long l3 = 0L; l3 < l2; l3 += 1L) {
/*  937 */       long l4 = l1 - l3 - 1L;
/*  938 */       Object localObject2 = getElem(paramContext, paramScriptable, l3);
/*  939 */       Object localObject3 = getElem(paramContext, paramScriptable, l4);
/*  940 */       setElem(paramContext, paramScriptable, l3, localObject3);
/*  941 */       setElem(paramContext, paramScriptable, l4, localObject2);
/*      */     }
/*  943 */     return paramScriptable;
/*      */   }
/*      */ 
/*      */   private static Scriptable js_sort(final Context paramContext, final Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*      */   {
/*      */     Object localObject1;
/*  953 */     if ((paramArrayOfObject.length > 0) && (Undefined.instance != paramArrayOfObject[0])) {
/*  954 */       final Callable localCallable = ScriptRuntime.getValueFunctionAndThis(paramArrayOfObject[0], paramContext);
/*      */ 
/*  956 */       localObject2 = ScriptRuntime.lastStoredScriptable(paramContext);
/*  957 */       Object[] arrayOfObject = new Object[2];
/*  958 */       localObject1 = new Comparator()
/*      */       {
/*      */         public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2) {
/*  961 */           if (paramAnonymousObject1 == paramAnonymousObject2)
/*  962 */             return 0;
/*  963 */           if ((paramAnonymousObject2 == Undefined.instance) || (paramAnonymousObject2 == Scriptable.NOT_FOUND))
/*      */           {
/*  965 */             return -1;
/*  966 */           }if ((paramAnonymousObject1 == Undefined.instance) || (paramAnonymousObject1 == Scriptable.NOT_FOUND))
/*      */           {
/*  968 */             return 1;
/*      */           }
/*      */ 
/*  971 */           this.val$cmpBuf[0] = paramAnonymousObject1;
/*  972 */           this.val$cmpBuf[1] = paramAnonymousObject2;
/*  973 */           Object localObject = localCallable.call(paramContext, paramScriptable1, this.val$funThis, this.val$cmpBuf);
/*      */ 
/*  975 */           double d = ScriptRuntime.toNumber(localObject);
/*  976 */           if (d < 0.0D)
/*  977 */             return -1;
/*  978 */           if (d > 0.0D) {
/*  979 */             return 1;
/*      */           }
/*  981 */           return 0;
/*      */         } } ;
/*      */     }
/*      */     else {
/*  985 */       localObject1 = new Comparator()
/*      */       {
/*      */         public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2) {
/*  988 */           if (paramAnonymousObject1 == paramAnonymousObject2)
/*  989 */             return 0;
/*  990 */           if ((paramAnonymousObject2 == Undefined.instance) || (paramAnonymousObject2 == Scriptable.NOT_FOUND))
/*      */           {
/*  992 */             return -1;
/*  993 */           }if ((paramAnonymousObject1 == Undefined.instance) || (paramAnonymousObject1 == Scriptable.NOT_FOUND))
/*      */           {
/*  995 */             return 1;
/*      */           }
/*      */ 
/*  998 */           String str1 = ScriptRuntime.toString(paramAnonymousObject1);
/*  999 */           String str2 = ScriptRuntime.toString(paramAnonymousObject2);
/* 1000 */           return str1.compareTo(str2);
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/* 1005 */     int i = (int)getLengthProperty(paramContext, paramScriptable2);
/*      */ 
/* 1008 */     Object localObject2 = new Object[i];
/* 1009 */     for (int j = 0; j != i; j++) {
/* 1010 */       localObject2[j] = getElem(paramContext, paramScriptable2, j);
/*      */     }
/*      */ 
/* 1013 */     Arrays.sort((Object[])localObject2, (Comparator)localObject1);
/*      */ 
/* 1016 */     for (j = 0; j < i; j++) {
/* 1017 */       setElem(paramContext, paramScriptable2, j, localObject2[j]);
/*      */     }
/*      */ 
/* 1020 */     return paramScriptable2;
/*      */   }
/*      */ 
/*      */   private static Object js_push(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*      */   {
/* 1030 */     if ((paramScriptable instanceof NativeArray)) {
/* 1031 */       NativeArray localNativeArray = (NativeArray)paramScriptable;
/* 1032 */       if ((localNativeArray.denseOnly) && (localNativeArray.ensureCapacity((int)localNativeArray.length + paramArrayOfObject.length)))
/*      */       {
/* 1035 */         for (int i = 0; i < paramArrayOfObject.length; i++) {
/* 1036 */           localNativeArray.dense[((int)localNativeArray.length++)] = paramArrayOfObject[i];
/*      */         }
/* 1038 */         return ScriptRuntime.wrapNumber(localNativeArray.length);
/*      */       }
/*      */     }
/* 1041 */     long l = getLengthProperty(paramContext, paramScriptable);
/* 1042 */     for (int j = 0; j < paramArrayOfObject.length; j++) {
/* 1043 */       setElem(paramContext, paramScriptable, l + j, paramArrayOfObject[j]);
/*      */     }
/*      */ 
/* 1046 */     l += paramArrayOfObject.length;
/* 1047 */     Object localObject = setLengthProperty(paramContext, paramScriptable, l);
/*      */ 
/* 1053 */     if (paramContext.getLanguageVersion() == 120)
/*      */     {
/* 1055 */       return paramArrayOfObject.length == 0 ? Undefined.instance : paramArrayOfObject[(paramArrayOfObject.length - 1)];
/*      */     }
/*      */ 
/* 1060 */     return localObject;
/*      */   }
/*      */ 
/*      */   private static Object js_pop(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*      */   {
/*      */     Object localObject;
/* 1067 */     if ((paramScriptable instanceof NativeArray)) {
/* 1068 */       NativeArray localNativeArray = (NativeArray)paramScriptable;
/* 1069 */       if ((localNativeArray.denseOnly) && (localNativeArray.length > 0L)) {
/* 1070 */         localNativeArray.length -= 1L;
/* 1071 */         localObject = localNativeArray.dense[((int)localNativeArray.length)];
/* 1072 */         localNativeArray.dense[((int)localNativeArray.length)] = NOT_FOUND;
/* 1073 */         return localObject;
/*      */       }
/*      */     }
/* 1076 */     long l = getLengthProperty(paramContext, paramScriptable);
/* 1077 */     if (l > 0L) {
/* 1078 */       l -= 1L;
/*      */ 
/* 1081 */       localObject = getElem(paramContext, paramScriptable, l);
/*      */     }
/*      */     else
/*      */     {
/* 1086 */       localObject = Undefined.instance;
/*      */     }
/*      */ 
/* 1090 */     setLengthProperty(paramContext, paramScriptable, l);
/*      */ 
/* 1092 */     return localObject;
/*      */   }
/*      */ 
/*      */   private static Object js_shift(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*      */   {
/*      */     Object localObject1;
/* 1098 */     if ((paramScriptable instanceof NativeArray)) {
/* 1099 */       localObject1 = (NativeArray)paramScriptable;
/* 1100 */       if ((((NativeArray)localObject1).denseOnly) && (((NativeArray)localObject1).length > 0L)) {
/* 1101 */         localObject1.length -= 1L;
/* 1102 */         Object localObject2 = localObject1.dense[0];
/* 1103 */         System.arraycopy(((NativeArray)localObject1).dense, 1, ((NativeArray)localObject1).dense, 0, (int)((NativeArray)localObject1).length);
/* 1104 */         ((NativeArray)localObject1).dense[((int)localObject1.length)] = NOT_FOUND;
/* 1105 */         return localObject2;
/*      */       }
/*      */     }
/*      */ 
/* 1109 */     long l1 = getLengthProperty(paramContext, paramScriptable);
/* 1110 */     if (l1 > 0L) {
/* 1111 */       long l2 = 0L;
/* 1112 */       l1 -= 1L;
/*      */ 
/* 1115 */       localObject1 = getElem(paramContext, paramScriptable, l2);
/*      */ 
/* 1121 */       if (l1 > 0L) {
/* 1122 */         for (l2 = 1L; l2 <= l1; l2 += 1L) {
/* 1123 */           Object localObject3 = getElem(paramContext, paramScriptable, l2);
/* 1124 */           setElem(paramContext, paramScriptable, l2 - 1L, localObject3);
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1130 */       localObject1 = Undefined.instance;
/*      */     }
/* 1132 */     setLengthProperty(paramContext, paramScriptable, l1);
/* 1133 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private static Object js_unshift(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*      */   {
/* 1139 */     if ((paramScriptable instanceof NativeArray)) {
/* 1140 */       NativeArray localNativeArray = (NativeArray)paramScriptable;
/* 1141 */       if ((localNativeArray.denseOnly) && (localNativeArray.ensureCapacity((int)localNativeArray.length + paramArrayOfObject.length)))
/*      */       {
/* 1144 */         System.arraycopy(localNativeArray.dense, 0, localNativeArray.dense, paramArrayOfObject.length, (int)localNativeArray.length);
/*      */ 
/* 1146 */         for (int i = 0; i < paramArrayOfObject.length; i++) {
/* 1147 */           localNativeArray.dense[i] = paramArrayOfObject[i];
/*      */         }
/* 1149 */         localNativeArray.length += paramArrayOfObject.length;
/* 1150 */         return ScriptRuntime.wrapNumber(localNativeArray.length);
/*      */       }
/*      */     }
/* 1153 */     long l1 = getLengthProperty(paramContext, paramScriptable);
/* 1154 */     int j = paramArrayOfObject.length;
/*      */ 
/* 1156 */     if (paramArrayOfObject.length > 0)
/*      */     {
/* 1158 */       if (l1 > 0L) {
/* 1159 */         for (long l2 = l1 - 1L; l2 >= 0L; l2 -= 1L) {
/* 1160 */           Object localObject = getElem(paramContext, paramScriptable, l2);
/* 1161 */           setElem(paramContext, paramScriptable, l2 + j, localObject);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1166 */       for (int k = 0; k < paramArrayOfObject.length; k++) {
/* 1167 */         setElem(paramContext, paramScriptable, k, paramArrayOfObject[k]);
/*      */       }
/*      */ 
/* 1171 */       l1 += paramArrayOfObject.length;
/* 1172 */       return setLengthProperty(paramContext, paramScriptable, l1);
/*      */     }
/* 1174 */     return ScriptRuntime.wrapNumber(l1);
/*      */   }
/*      */ 
/*      */   private static Object js_splice(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*      */   {
/* 1180 */     NativeArray localNativeArray = null;
/* 1181 */     boolean bool = false;
/* 1182 */     if ((paramScriptable2 instanceof NativeArray)) {
/* 1183 */       localNativeArray = (NativeArray)paramScriptable2;
/* 1184 */       bool = localNativeArray.denseOnly;
/*      */     }
/*      */ 
/* 1188 */     paramScriptable1 = getTopLevelScope(paramScriptable1);
/* 1189 */     int i = paramArrayOfObject.length;
/* 1190 */     if (i == 0)
/* 1191 */       return ScriptRuntime.newObject(paramContext, paramScriptable1, "Array", null);
/* 1192 */     long l1 = getLengthProperty(paramContext, paramScriptable2);
/*      */ 
/* 1195 */     long l2 = toSliceIndex(ScriptRuntime.toInteger(paramArrayOfObject[0]), l1);
/* 1196 */     i--;
/*      */     long l3;
/* 1200 */     if (paramArrayOfObject.length == 1) {
/* 1201 */       l3 = l1 - l2;
/*      */     } else {
/* 1203 */       double d = ScriptRuntime.toInteger(paramArrayOfObject[1]);
/* 1204 */       if (d < 0.0D)
/* 1205 */         l3 = 0L;
/* 1206 */       else if (d > l1 - l2)
/* 1207 */         l3 = l1 - l2;
/*      */       else {
/* 1209 */         l3 = ()d;
/*      */       }
/* 1211 */       i--;
/*      */     }
/*      */ 
/* 1214 */     long l4 = l2 + l3;
/*      */     Object localObject1;
/* 1218 */     if (l3 != 0L) {
/* 1219 */       if ((l3 == 1L) && (paramContext.getLanguageVersion() == 120))
/*      */       {
/* 1233 */         localObject1 = getElem(paramContext, paramScriptable2, l2);
/*      */       }
/* 1235 */       else if (bool) {
/* 1236 */         int j = (int)(l4 - l2);
/* 1237 */         Object[] arrayOfObject = new Object[j];
/* 1238 */         System.arraycopy(localNativeArray.dense, (int)l2, arrayOfObject, 0, j);
/* 1239 */         localObject1 = paramContext.newArray(paramScriptable1, arrayOfObject);
/*      */       } else {
/* 1241 */         Scriptable localScriptable = ScriptRuntime.newObject(paramContext, paramScriptable1, "Array", null);
/*      */ 
/* 1243 */         for (long l6 = l2; l6 != l4; l6 += 1L) {
/* 1244 */           Object localObject2 = getElem(paramContext, paramScriptable2, l6);
/* 1245 */           setElem(paramContext, localScriptable, l6 - l2, localObject2);
/*      */         }
/* 1247 */         localObject1 = localScriptable;
/*      */       }
/*      */ 
/*      */     }
/* 1251 */     else if (paramContext.getLanguageVersion() == 120)
/*      */     {
/* 1253 */       localObject1 = Undefined.instance;
/*      */     }
/* 1255 */     else localObject1 = ScriptRuntime.newObject(paramContext, paramScriptable1, "Array", null);
/*      */ 
/* 1260 */     long l5 = i - l3;
/* 1261 */     if ((bool) && (l1 + l5 < 2147483647L) && (localNativeArray.ensureCapacity((int)(l1 + l5))))
/*      */     {
/* 1264 */       System.arraycopy(localNativeArray.dense, (int)l4, localNativeArray.dense, (int)(l2 + i), (int)(l1 - l4));
/*      */ 
/* 1266 */       if (i > 0) {
/* 1267 */         System.arraycopy(paramArrayOfObject, 2, localNativeArray.dense, (int)l2, i);
/*      */       }
/* 1269 */       if (l5 < 0L) {
/* 1270 */         Arrays.fill(localNativeArray.dense, (int)(l1 + l5), (int)l1, NOT_FOUND);
/*      */       }
/*      */ 
/* 1273 */       localNativeArray.length = (l1 + l5);
/* 1274 */       return localObject1;
/*      */     }
/*      */     long l7;
/*      */     Object localObject3;
/* 1277 */     if (l5 > 0L)
/* 1278 */       for (l7 = l1 - 1L; l7 >= l4; l7 -= 1L) {
/* 1279 */         localObject3 = getElem(paramContext, paramScriptable2, l7);
/* 1280 */         setElem(paramContext, paramScriptable2, l7 + l5, localObject3);
/*      */       }
/* 1282 */     else if (l5 < 0L) {
/* 1283 */       for (l7 = l4; l7 < l1; l7 += 1L) {
/* 1284 */         localObject3 = getElem(paramContext, paramScriptable2, l7);
/* 1285 */         setElem(paramContext, paramScriptable2, l7 + l5, localObject3);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1290 */     int k = paramArrayOfObject.length - i;
/* 1291 */     for (int m = 0; m < i; m++) {
/* 1292 */       setElem(paramContext, paramScriptable2, l2 + m, paramArrayOfObject[(m + k)]);
/*      */     }
/*      */ 
/* 1296 */     setLengthProperty(paramContext, paramScriptable2, l1 + l5);
/* 1297 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private static Scriptable js_concat(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*      */   {
/* 1307 */     paramScriptable1 = getTopLevelScope(paramScriptable1);
/* 1308 */     Function localFunction = ScriptRuntime.getExistingCtor(paramContext, paramScriptable1, "Array");
/* 1309 */     Scriptable localScriptable1 = localFunction.construct(paramContext, paramScriptable1, ScriptRuntime.emptyArgs);
/* 1310 */     if (((paramScriptable2 instanceof NativeArray)) && ((localScriptable1 instanceof NativeArray))) {
/* 1311 */       NativeArray localNativeArray1 = (NativeArray)paramScriptable2;
/* 1312 */       NativeArray localNativeArray2 = (NativeArray)localScriptable1;
/* 1313 */       if ((localNativeArray1.denseOnly) && (localNativeArray2.denseOnly))
/*      */       {
/* 1315 */         boolean bool = true;
/* 1316 */         int i = (int)localNativeArray1.length;
/* 1317 */         for (int j = 0; (j < paramArrayOfObject.length) && (bool); j++) {
/* 1318 */           if ((paramArrayOfObject[j] instanceof NativeArray))
/*      */           {
/* 1321 */             NativeArray localNativeArray3 = (NativeArray)paramArrayOfObject[j];
/* 1322 */             bool = localNativeArray3.denseOnly;
/* 1323 */             i = (int)(i + localNativeArray3.length);
/*      */           } else {
/* 1325 */             i++;
/*      */           }
/*      */         }
/* 1328 */         if ((bool) && (localNativeArray2.ensureCapacity(i))) {
/* 1329 */           System.arraycopy(localNativeArray1.dense, 0, localNativeArray2.dense, 0, (int)localNativeArray1.length);
/*      */ 
/* 1331 */           j = (int)localNativeArray1.length;
/* 1332 */           for (int m = 0; (m < paramArrayOfObject.length) && (bool); m++) {
/* 1333 */             if ((paramArrayOfObject[m] instanceof NativeArray)) {
/* 1334 */               NativeArray localNativeArray4 = (NativeArray)paramArrayOfObject[m];
/* 1335 */               System.arraycopy(localNativeArray4.dense, 0, localNativeArray2.dense, j, (int)localNativeArray4.length);
/*      */ 
/* 1338 */               j += (int)localNativeArray4.length;
/*      */             } else {
/* 1340 */               localNativeArray2.dense[(j++)] = paramArrayOfObject[m];
/*      */             }
/*      */           }
/* 1343 */           localNativeArray2.length = i;
/* 1344 */           return localScriptable1;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1350 */     long l2 = 0L;
/*      */     long l1;
/* 1355 */     if (ScriptRuntime.instanceOf(paramScriptable2, localFunction, paramContext)) {
/* 1356 */       l1 = getLengthProperty(paramContext, paramScriptable2);
/*      */ 
/* 1359 */       for (l2 = 0L; l2 < l1; l2 += 1L) {
/* 1360 */         Object localObject1 = getElem(paramContext, paramScriptable2, l2);
/* 1361 */         setElem(paramContext, localScriptable1, l2, localObject1);
/*      */       }
/*      */     }
/* 1364 */     setElem(paramContext, localScriptable1, l2++, paramScriptable2);
/*      */ 
/* 1371 */     for (int k = 0; k < paramArrayOfObject.length; k++) {
/* 1372 */       if (ScriptRuntime.instanceOf(paramArrayOfObject[k], localFunction, paramContext))
/*      */       {
/* 1374 */         Scriptable localScriptable2 = (Scriptable)paramArrayOfObject[k];
/* 1375 */         l1 = getLengthProperty(paramContext, localScriptable2);
/* 1376 */         for (long l3 = 0L; l3 < l1; l2 += 1L) {
/* 1377 */           Object localObject2 = getElem(paramContext, localScriptable2, l3);
/* 1378 */           setElem(paramContext, localScriptable1, l2, localObject2);
/*      */ 
/* 1376 */           l3 += 1L;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1381 */         setElem(paramContext, localScriptable1, l2++, paramArrayOfObject[k]);
/*      */       }
/*      */     }
/* 1384 */     return localScriptable1;
/*      */   }
/*      */ 
/*      */   private Scriptable js_slice(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*      */   {
/* 1390 */     Scriptable localScriptable1 = getTopLevelScope(this);
/* 1391 */     Scriptable localScriptable2 = ScriptRuntime.newObject(paramContext, localScriptable1, "Array", null);
/* 1392 */     long l1 = getLengthProperty(paramContext, paramScriptable);
/*      */     long l2;
/*      */     long l3;
/* 1395 */     if (paramArrayOfObject.length == 0) {
/* 1396 */       l2 = 0L;
/* 1397 */       l3 = l1;
/*      */     } else {
/* 1399 */       l2 = toSliceIndex(ScriptRuntime.toInteger(paramArrayOfObject[0]), l1);
/* 1400 */       if (paramArrayOfObject.length == 1)
/* 1401 */         l3 = l1;
/*      */       else {
/* 1403 */         l3 = toSliceIndex(ScriptRuntime.toInteger(paramArrayOfObject[1]), l1);
/*      */       }
/*      */     }
/*      */ 
/* 1407 */     for (long l4 = l2; l4 < l3; l4 += 1L) {
/* 1408 */       Object localObject = getElem(paramContext, paramScriptable, l4);
/* 1409 */       setElem(paramContext, localScriptable2, l4 - l2, localObject);
/*      */     }
/*      */ 
/* 1412 */     return localScriptable2;
/*      */   }
/*      */ 
/*      */   private static long toSliceIndex(double paramDouble, long paramLong)
/*      */   {
/*      */     long l;
/* 1417 */     if (paramDouble < 0.0D) {
/* 1418 */       if (paramDouble + paramLong < 0.0D)
/* 1419 */         l = 0L;
/*      */       else
/* 1421 */         l = ()(paramDouble + paramLong);
/*      */     }
/* 1423 */     else if (paramDouble > paramLong)
/* 1424 */       l = paramLong;
/*      */     else {
/* 1426 */       l = ()paramDouble;
/*      */     }
/* 1428 */     return l;
/*      */   }
/*      */ 
/*      */   private Object indexOfHelper(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, boolean paramBoolean)
/*      */   {
/* 1437 */     Object localObject = paramArrayOfObject.length > 0 ? paramArrayOfObject[0] : Undefined.instance;
/* 1438 */     long l1 = getLengthProperty(paramContext, paramScriptable);
/*      */     long l2;
/* 1440 */     if (paramBoolean)
/*      */     {
/* 1453 */       if (paramArrayOfObject.length < 2)
/*      */       {
/* 1455 */         l2 = l1 - 1L;
/*      */       } else {
/* 1457 */         l2 = ScriptRuntime.toInt32(ScriptRuntime.toNumber(paramArrayOfObject[1]));
/* 1458 */         if (l2 >= l1)
/* 1459 */           l2 = l1 - 1L;
/* 1460 */         else if (l2 < 0L) {
/* 1461 */           l2 += l1;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/* 1477 */     else if (paramArrayOfObject.length < 2)
/*      */     {
/* 1479 */       l2 = 0L;
/*      */     } else {
/* 1481 */       l2 = ScriptRuntime.toInt32(ScriptRuntime.toNumber(paramArrayOfObject[1]));
/* 1482 */       if (l2 < 0L) {
/* 1483 */         l2 += l1;
/* 1484 */         if (l2 < 0L) {
/* 1485 */           l2 = 0L;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1491 */     if ((paramScriptable instanceof NativeArray)) {
/* 1492 */       NativeArray localNativeArray = (NativeArray)paramScriptable;
/* 1493 */       if (localNativeArray.denseOnly)
/*      */       {
/*      */         int i;
/* 1494 */         if (paramBoolean) {
/* 1495 */           for (i = (int)l2; i >= 0; i--)
/* 1496 */             if ((localNativeArray.dense[i] != Scriptable.NOT_FOUND) && (ScriptRuntime.shallowEq(localNativeArray.dense[i], localObject)))
/*      */             {
/* 1499 */               return Long.valueOf(i);
/*      */             }
/*      */         }
/*      */         else {
/* 1503 */           for (i = (int)l2; i < l1; i++) {
/* 1504 */             if ((localNativeArray.dense[i] != Scriptable.NOT_FOUND) && (ScriptRuntime.shallowEq(localNativeArray.dense[i], localObject)))
/*      */             {
/* 1507 */               return Long.valueOf(i);
/*      */             }
/*      */           }
/*      */         }
/* 1511 */         return NEGATIVE_ONE;
/*      */       }
/*      */     }
/*      */     long l3;
/* 1514 */     if (paramBoolean) {
/* 1515 */       for (l3 = l2; l3 >= 0L; l3 -= 1L) {
/* 1516 */         if (ScriptRuntime.shallowEq(getElem(paramContext, paramScriptable, l3), localObject))
/* 1517 */           return Long.valueOf(l3);
/*      */       }
/*      */     }
/*      */     else {
/* 1521 */       for (l3 = l2; l3 < l1; l3 += 1L) {
/* 1522 */         if (ScriptRuntime.shallowEq(getElem(paramContext, paramScriptable, l3), localObject)) {
/* 1523 */           return Long.valueOf(l3);
/*      */         }
/*      */       }
/*      */     }
/* 1527 */     return NEGATIVE_ONE;
/*      */   }
/*      */ 
/*      */   private Object iterativeMethod(Context paramContext, int paramInt, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*      */   {
/* 1536 */     Object localObject1 = paramArrayOfObject.length > 0 ? paramArrayOfObject[0] : Undefined.instance;
/* 1537 */     if ((localObject1 == null) || (!(localObject1 instanceof Function))) {
/* 1538 */       throw ScriptRuntime.notFunctionError(localObject1);
/*      */     }
/* 1540 */     Function localFunction = (Function)localObject1;
/* 1541 */     Scriptable localScriptable1 = ScriptableObject.getTopLevelScope(localFunction);
/*      */     Scriptable localScriptable2;
/* 1543 */     if ((paramArrayOfObject.length < 2) || (paramArrayOfObject[1] == null) || (paramArrayOfObject[1] == Undefined.instance))
/*      */     {
/* 1545 */       localScriptable2 = localScriptable1;
/*      */     }
/* 1547 */     else localScriptable2 = ScriptRuntime.toObject(paramContext, paramScriptable1, paramArrayOfObject[1]);
/*      */ 
/* 1549 */     long l1 = getLengthProperty(paramContext, paramScriptable2);
/* 1550 */     int i = paramInt == 20 ? (int)l1 : 0;
/* 1551 */     Scriptable localScriptable3 = paramContext.newArray(paramScriptable1, i);
/* 1552 */     long l2 = 0L;
/* 1553 */     for (long l3 = 0L; l3 < l1; l3 += 1L) {
/* 1554 */       Object[] arrayOfObject = new Object[3];
/* 1555 */       Object localObject2 = getRawElem(paramScriptable2, l3);
/* 1556 */       if (localObject2 != Scriptable.NOT_FOUND)
/*      */       {
/* 1559 */         arrayOfObject[0] = localObject2;
/* 1560 */         arrayOfObject[1] = Long.valueOf(l3);
/* 1561 */         arrayOfObject[2] = paramScriptable2;
/* 1562 */         Object localObject3 = localFunction.call(paramContext, localScriptable1, localScriptable2, arrayOfObject);
/* 1563 */         switch (paramInt) {
/*      */         case 17:
/* 1565 */           if (!ScriptRuntime.toBoolean(localObject3))
/* 1566 */             return Boolean.FALSE;
/*      */           break;
/*      */         case 18:
/* 1569 */           if (ScriptRuntime.toBoolean(localObject3))
/* 1570 */             setElem(paramContext, localScriptable3, l2++, arrayOfObject[0]); break;
/*      */         case 19:
/* 1573 */           break;
/*      */         case 20:
/* 1575 */           setElem(paramContext, localScriptable3, l3, localObject3);
/* 1576 */           break;
/*      */         case 21:
/* 1578 */           if (ScriptRuntime.toBoolean(localObject3))
/* 1579 */             return Boolean.TRUE; break;
/*      */         }
/*      */       }
/*      */     }
/* 1583 */     switch (paramInt) {
/*      */     case 17:
/* 1585 */       return Boolean.TRUE;
/*      */     case 18:
/*      */     case 20:
/* 1588 */       return localScriptable3;
/*      */     case 21:
/* 1590 */       return Boolean.FALSE;
/*      */     case 19:
/*      */     }
/* 1593 */     return Undefined.instance;
/*      */   }
/*      */ 
/*      */   private Object reduceMethod(Context paramContext, int paramInt, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*      */   {
/* 1603 */     Object localObject1 = paramArrayOfObject.length > 0 ? paramArrayOfObject[0] : Undefined.instance;
/* 1604 */     if ((localObject1 == null) || (!(localObject1 instanceof Function))) {
/* 1605 */       throw ScriptRuntime.notFunctionError(localObject1);
/*      */     }
/* 1607 */     Function localFunction = (Function)localObject1;
/* 1608 */     Scriptable localScriptable = ScriptableObject.getTopLevelScope(localFunction);
/* 1609 */     long l1 = getLengthProperty(paramContext, paramScriptable2);
/*      */ 
/* 1611 */     long l2 = paramInt == 23 ? l1 - 1L : 0L;
/* 1612 */     Object localObject2 = paramArrayOfObject.length > 1 ? paramArrayOfObject[1] : Scriptable.NOT_FOUND;
/* 1613 */     for (long l3 = 0L; l3 < l1; l3 += 1L) {
/* 1614 */       Object localObject3 = getRawElem(paramScriptable2, Math.abs(l3 - l2));
/* 1615 */       if (localObject3 != Scriptable.NOT_FOUND)
/*      */       {
/* 1618 */         if (localObject2 == Scriptable.NOT_FOUND)
/*      */         {
/* 1620 */           localObject2 = localObject3;
/*      */         } else {
/* 1622 */           Object[] arrayOfObject = new Object[4];
/* 1623 */           arrayOfObject[0] = localObject2;
/* 1624 */           arrayOfObject[1] = localObject3;
/* 1625 */           arrayOfObject[2] = Long.valueOf(l3);
/* 1626 */           arrayOfObject[3] = paramScriptable2;
/* 1627 */           localObject2 = localFunction.call(paramContext, localScriptable, localScriptable, arrayOfObject);
/*      */         }
/*      */       }
/*      */     }
/* 1630 */     if (localObject2 == Scriptable.NOT_FOUND)
/*      */     {
/* 1632 */       throw ScriptRuntime.typeError0("msg.empty.array.reduce");
/*      */     }
/* 1634 */     return localObject2;
/*      */   }
/*      */ 
/*      */   public boolean contains(Object paramObject)
/*      */   {
/* 1640 */     return indexOf(paramObject) > -1;
/*      */   }
/*      */ 
/*      */   public Object[] toArray() {
/* 1644 */     return toArray(ScriptRuntime.emptyArgs);
/*      */   }
/*      */ 
/*      */   public Object[] toArray(Object[] paramArrayOfObject) {
/* 1648 */     long l = this.length;
/* 1649 */     if (l > 2147483647L) {
/* 1650 */       throw new IllegalStateException();
/*      */     }
/* 1652 */     int i = (int)l;
/* 1653 */     Object[] arrayOfObject = paramArrayOfObject.length >= i ? paramArrayOfObject : (Object[])Array.newInstance(paramArrayOfObject.getClass().getComponentType(), i);
/*      */ 
/* 1656 */     for (int j = 0; j < i; j++) {
/* 1657 */       arrayOfObject[j] = get(j);
/*      */     }
/* 1659 */     return arrayOfObject;
/*      */   }
/*      */ 
/*      */   public boolean containsAll(Collection paramCollection) {
/* 1663 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 1664 */       if (!contains(localObject))
/* 1665 */         return false; }
/* 1666 */     return true;
/*      */   }
/*      */ 
/*      */   public int size() {
/* 1670 */     long l = this.length;
/* 1671 */     if (l > 2147483647L) {
/* 1672 */       throw new IllegalStateException();
/*      */     }
/* 1674 */     return (int)l;
/*      */   }
/*      */ 
/*      */   public Object get(long paramLong) {
/* 1678 */     if ((paramLong < 0L) || (paramLong >= this.length)) {
/* 1679 */       throw new IndexOutOfBoundsException();
/*      */     }
/* 1681 */     Object localObject = getRawElem(this, paramLong);
/* 1682 */     if ((localObject == Scriptable.NOT_FOUND) || (localObject == Undefined.instance))
/* 1683 */       return null;
/* 1684 */     if ((localObject instanceof Wrapper)) {
/* 1685 */       return ((Wrapper)localObject).unwrap();
/*      */     }
/* 1687 */     return localObject;
/*      */   }
/*      */ 
/*      */   public Object get(int paramInt)
/*      */   {
/* 1692 */     return get(paramInt);
/*      */   }
/*      */ 
/*      */   public int indexOf(Object paramObject) {
/* 1696 */     long l = this.length;
/* 1697 */     if (l > 2147483647L) {
/* 1698 */       throw new IllegalStateException();
/*      */     }
/* 1700 */     int i = (int)l;
/*      */     int j;
/* 1701 */     if (paramObject == null) {
/* 1702 */       for (j = 0; j < i; j++) {
/* 1703 */         if (get(j) == null)
/* 1704 */           return j;
/*      */       }
/*      */     }
/*      */     else {
/* 1708 */       for (j = 0; j < i; j++) {
/* 1709 */         if (paramObject.equals(get(j))) {
/* 1710 */           return j;
/*      */         }
/*      */       }
/*      */     }
/* 1714 */     return -1;
/*      */   }
/*      */ 
/*      */   public int lastIndexOf(Object paramObject) {
/* 1718 */     long l = this.length;
/* 1719 */     if (l > 2147483647L) {
/* 1720 */       throw new IllegalStateException();
/*      */     }
/* 1722 */     int i = (int)l;
/*      */     int j;
/* 1723 */     if (paramObject == null) {
/* 1724 */       for (j = i - 1; j >= 0; j--) {
/* 1725 */         if (get(j) == null)
/* 1726 */           return j;
/*      */       }
/*      */     }
/*      */     else {
/* 1730 */       for (j = i - 1; j >= 0; j--) {
/* 1731 */         if (paramObject.equals(get(j))) {
/* 1732 */           return j;
/*      */         }
/*      */       }
/*      */     }
/* 1736 */     return -1;
/*      */   }
/*      */ 
/*      */   public Iterator iterator() {
/* 1740 */     return listIterator(0);
/*      */   }
/*      */ 
/*      */   public ListIterator listIterator() {
/* 1744 */     return listIterator(0);
/*      */   }
/*      */ 
/*      */   public ListIterator listIterator(final int paramInt) {
/* 1748 */     long l = this.length;
/* 1749 */     if (l > 2147483647L) {
/* 1750 */       throw new IllegalStateException();
/*      */     }
/* 1752 */     final int i = (int)l;
/*      */ 
/* 1754 */     if ((paramInt < 0) || (paramInt > i)) {
/* 1755 */       throw new IndexOutOfBoundsException("Index: " + paramInt);
/*      */     }
/*      */ 
/* 1758 */     return new ListIterator()
/*      */     {
/* 1760 */       int cursor = paramInt;
/*      */ 
/*      */       public boolean hasNext() {
/* 1763 */         return this.cursor < i;
/*      */       }
/*      */ 
/*      */       public Object next() {
/* 1767 */         if (this.cursor == i) {
/* 1768 */           throw new NoSuchElementException();
/*      */         }
/* 1770 */         return NativeArray.this.get(this.cursor++);
/*      */       }
/*      */ 
/*      */       public boolean hasPrevious() {
/* 1774 */         return this.cursor > 0;
/*      */       }
/*      */ 
/*      */       public Object previous() {
/* 1778 */         if (this.cursor == 0) {
/* 1779 */           throw new NoSuchElementException();
/*      */         }
/* 1781 */         return NativeArray.this.get(--this.cursor);
/*      */       }
/*      */ 
/*      */       public int nextIndex() {
/* 1785 */         return this.cursor;
/*      */       }
/*      */ 
/*      */       public int previousIndex() {
/* 1789 */         return this.cursor - 1;
/*      */       }
/*      */ 
/*      */       public void remove() {
/* 1793 */         throw new UnsupportedOperationException();
/*      */       }
/*      */ 
/*      */       public void add(Object paramAnonymousObject) {
/* 1797 */         throw new UnsupportedOperationException();
/*      */       }
/*      */ 
/*      */       public void set(Object paramAnonymousObject) {
/* 1801 */         throw new UnsupportedOperationException();
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public boolean add(Object paramObject) {
/* 1807 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean remove(Object paramObject) {
/* 1811 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean addAll(Collection paramCollection) {
/* 1815 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean removeAll(Collection paramCollection) {
/* 1819 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean retainAll(Collection paramCollection) {
/* 1823 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void clear() {
/* 1827 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void add(int paramInt, Object paramObject) {
/* 1831 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean addAll(int paramInt, Collection paramCollection) {
/* 1835 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Object set(int paramInt, Object paramObject) {
/* 1839 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Object remove(int paramInt) {
/* 1843 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public List subList(int paramInt1, int paramInt2) {
/* 1847 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected int findPrototypeId(String paramString)
/*      */   {
/* 1857 */     int i = 0; String str = null;
/*      */     int j;
/* 1858 */     switch (paramString.length()) { case 3:
/* 1859 */       j = paramString.charAt(0);
/* 1860 */       if (j == 109) { if ((paramString.charAt(2) == 'p') && (paramString.charAt(1) == 'a')) { i = 20; break label561; }
/* 1861 */       } else if ((j == 112) && (paramString.charAt(2) == 'p') && (paramString.charAt(1) == 'o')) i = 9; break;
/*      */     case 4:
/* 1863 */       switch (paramString.charAt(2)) { case 'i':
/* 1864 */         str = "join"; i = 5; break;
/*      */       case 'm':
/* 1865 */         str = "some"; i = 21; break;
/*      */       case 'r':
/* 1866 */         str = "sort"; i = 7; break;
/*      */       case 's':
/* 1867 */         str = "push"; i = 8; }
/* 1868 */       break;
/*      */     case 5:
/* 1869 */       j = paramString.charAt(1);
/* 1870 */       if (j == 104) { str = "shift"; i = 10;
/* 1871 */       } else if (j == 108) { str = "slice"; i = 14;
/* 1872 */       } else if (j == 118) { str = "every"; i = 17; } break;
/*      */     case 6:
/* 1874 */       j = paramString.charAt(0);
/* 1875 */       if (j == 99) { str = "concat"; i = 13;
/* 1876 */       } else if (j == 102) { str = "filter"; i = 18;
/* 1877 */       } else if (j == 115) { str = "splice"; i = 12;
/* 1878 */       } else if (j == 114) { str = "reduce"; i = 22; } break;
/*      */     case 7:
/* 1880 */       switch (paramString.charAt(0)) { case 'f':
/* 1881 */         str = "forEach"; i = 19; break;
/*      */       case 'i':
/* 1882 */         str = "indexOf"; i = 15; break;
/*      */       case 'r':
/* 1883 */         str = "reverse"; i = 6; break;
/*      */       case 'u':
/* 1884 */         str = "unshift"; i = 11; }
/* 1885 */       break;
/*      */     case 8:
/* 1886 */       j = paramString.charAt(3);
/* 1887 */       if (j == 111) { str = "toSource"; i = 4;
/* 1888 */       } else if (j == 116) { str = "toString"; i = 2; } break;
/*      */     case 11:
/* 1890 */       j = paramString.charAt(0);
/* 1891 */       if (j == 99) { str = "constructor"; i = 1;
/* 1892 */       } else if (j == 108) { str = "lastIndexOf"; i = 16;
/* 1893 */       } else if (j == 114) { str = "reduceRight"; i = 23; } break;
/*      */     case 14:
/* 1895 */       str = "toLocaleString"; i = 3; break;
/*      */     case 9:
/*      */     case 10:
/*      */     case 12:
/* 1897 */     case 13: } if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*      */ 
/* 1900 */     label561: return i;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeArray
 * JD-Core Version:    0.6.2
 */