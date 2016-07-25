/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public abstract class IdScriptableObject extends ScriptableObject
/*     */   implements IdFunctionCall
/*     */ {
/*     */   private volatile transient PrototypeValues prototypeValues;
/*     */ 
/*     */   public IdScriptableObject()
/*     */   {
/*     */   }
/*     */ 
/*     */   public IdScriptableObject(Scriptable paramScriptable1, Scriptable paramScriptable2)
/*     */   {
/* 337 */     super(paramScriptable1, paramScriptable2);
/*     */   }
/*     */ 
/*     */   protected final Object defaultGet(String paramString)
/*     */   {
/* 342 */     return super.get(paramString, this);
/*     */   }
/*     */ 
/*     */   protected final void defaultPut(String paramString, Object paramObject)
/*     */   {
/* 347 */     super.put(paramString, this, paramObject);
/*     */   }
/*     */ 
/*     */   public boolean has(String paramString, Scriptable paramScriptable)
/*     */   {
/* 353 */     int i = findInstanceIdInfo(paramString);
/*     */     int j;
/* 354 */     if (i != 0) {
/* 355 */       j = i >>> 16;
/* 356 */       if ((j & 0x4) != 0) {
/* 357 */         return true;
/*     */       }
/* 359 */       int k = i & 0xFFFF;
/* 360 */       return NOT_FOUND != getInstanceIdValue(k);
/*     */     }
/* 362 */     if (this.prototypeValues != null) {
/* 363 */       j = this.prototypeValues.findId(paramString);
/* 364 */       if (j != 0) {
/* 365 */         return this.prototypeValues.has(j);
/*     */       }
/*     */     }
/* 368 */     return super.has(paramString, paramScriptable);
/*     */   }
/*     */ 
/*     */   public Object get(String paramString, Scriptable paramScriptable)
/*     */   {
/* 374 */     int i = findInstanceIdInfo(paramString);
/*     */     int j;
/*     */     Object localObject;
/* 375 */     if (i != 0) {
/* 376 */       j = i & 0xFFFF;
/* 377 */       localObject = getInstanceIdValue(j);
/* 378 */       if (localObject != NOT_FOUND) return localObject;
/*     */     }
/* 380 */     if (this.prototypeValues != null) {
/* 381 */       j = this.prototypeValues.findId(paramString);
/* 382 */       if (j != 0) {
/* 383 */         localObject = this.prototypeValues.get(j);
/* 384 */         if (localObject != NOT_FOUND) return localObject;
/*     */       }
/*     */     }
/* 387 */     return super.get(paramString, paramScriptable);
/*     */   }
/*     */ 
/*     */   public void put(String paramString, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 393 */     int i = findInstanceIdInfo(paramString);
/*     */     int j;
/* 394 */     if (i != 0) {
/* 395 */       if ((paramScriptable == this) && (isSealed())) {
/* 396 */         throw Context.reportRuntimeError1("msg.modify.sealed", paramString);
/*     */       }
/*     */ 
/* 399 */       j = i >>> 16;
/* 400 */       if ((j & 0x1) == 0) {
/* 401 */         if (paramScriptable == this) {
/* 402 */           int k = i & 0xFFFF;
/* 403 */           setInstanceIdValue(k, paramObject);
/*     */         }
/*     */         else {
/* 406 */           paramScriptable.put(paramString, paramScriptable, paramObject);
/*     */         }
/*     */       }
/* 409 */       return;
/*     */     }
/* 411 */     if (this.prototypeValues != null) {
/* 412 */       j = this.prototypeValues.findId(paramString);
/* 413 */       if (j != 0) {
/* 414 */         if ((paramScriptable == this) && (isSealed())) {
/* 415 */           throw Context.reportRuntimeError1("msg.modify.sealed", paramString);
/*     */         }
/*     */ 
/* 418 */         this.prototypeValues.set(j, paramScriptable, paramObject);
/* 419 */         return;
/*     */       }
/*     */     }
/* 422 */     super.put(paramString, paramScriptable, paramObject);
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */   {
/* 428 */     int i = findInstanceIdInfo(paramString);
/*     */     int j;
/* 429 */     if (i != 0)
/*     */     {
/* 431 */       if (!isSealed()) {
/* 432 */         j = i >>> 16;
/* 433 */         if ((j & 0x4) == 0) {
/* 434 */           int k = i & 0xFFFF;
/* 435 */           setInstanceIdValue(k, NOT_FOUND);
/*     */         }
/* 437 */         return;
/*     */       }
/*     */     }
/* 440 */     if (this.prototypeValues != null) {
/* 441 */       j = this.prototypeValues.findId(paramString);
/* 442 */       if (j != 0) {
/* 443 */         if (!isSealed()) {
/* 444 */           this.prototypeValues.delete(j);
/*     */         }
/* 446 */         return;
/*     */       }
/*     */     }
/* 449 */     super.delete(paramString);
/*     */   }
/*     */ 
/*     */   public int getAttributes(String paramString)
/*     */   {
/* 455 */     int i = findInstanceIdInfo(paramString);
/*     */     int j;
/* 456 */     if (i != 0) {
/* 457 */       j = i >>> 16;
/* 458 */       return j;
/*     */     }
/* 460 */     if (this.prototypeValues != null) {
/* 461 */       j = this.prototypeValues.findId(paramString);
/* 462 */       if (j != 0) {
/* 463 */         return this.prototypeValues.getAttributes(j);
/*     */       }
/*     */     }
/* 466 */     return super.getAttributes(paramString);
/*     */   }
/*     */ 
/*     */   public void setAttributes(String paramString, int paramInt)
/*     */   {
/* 472 */     ScriptableObject.checkValidAttributes(paramInt);
/* 473 */     int i = findInstanceIdInfo(paramString);
/*     */     int j;
/* 474 */     if (i != 0) {
/* 475 */       j = i >>> 16;
/* 476 */       if (paramInt != j) {
/* 477 */         throw new RuntimeException("Change of attributes for this id is not supported");
/*     */       }
/*     */ 
/* 480 */       return;
/*     */     }
/* 482 */     if (this.prototypeValues != null) {
/* 483 */       j = this.prototypeValues.findId(paramString);
/* 484 */       if (j != 0) {
/* 485 */         this.prototypeValues.setAttributes(j, paramInt);
/* 486 */         return;
/*     */       }
/*     */     }
/* 489 */     super.setAttributes(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   Object[] getIds(boolean paramBoolean)
/*     */   {
/* 495 */     Object localObject = super.getIds(paramBoolean);
/*     */ 
/* 497 */     if (this.prototypeValues != null) {
/* 498 */       localObject = this.prototypeValues.getNames(paramBoolean, (Object[])localObject);
/*     */     }
/*     */ 
/* 501 */     int i = getMaxInstanceId();
/* 502 */     if (i != 0) {
/* 503 */       Object[] arrayOfObject1 = null;
/* 504 */       int j = 0;
/*     */ 
/* 506 */       for (int k = i; k != 0; k--) {
/* 507 */         String str = getInstanceIdName(k);
/* 508 */         int m = findInstanceIdInfo(str);
/* 509 */         if (m != 0) {
/* 510 */           int n = m >>> 16;
/* 511 */           if (((n & 0x4) != 0) || 
/* 512 */             (NOT_FOUND != getInstanceIdValue(k)))
/*     */           {
/* 516 */             if ((paramBoolean) || ((n & 0x2) == 0)) {
/* 517 */               if (j == 0)
/*     */               {
/* 519 */                 arrayOfObject1 = new Object[k];
/*     */               }
/* 521 */               arrayOfObject1[(j++)] = str;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 525 */       if (j != 0) {
/* 526 */         if ((localObject.length == 0) && (arrayOfObject1.length == j)) {
/* 527 */           localObject = arrayOfObject1;
/*     */         }
/*     */         else {
/* 530 */           Object[] arrayOfObject2 = new Object[localObject.length + j];
/* 531 */           System.arraycopy(localObject, 0, arrayOfObject2, 0, localObject.length);
/* 532 */           System.arraycopy(arrayOfObject1, 0, arrayOfObject2, localObject.length, j);
/* 533 */           localObject = arrayOfObject2;
/*     */         }
/*     */       }
/*     */     }
/* 537 */     return localObject;
/*     */   }
/*     */ 
/*     */   protected int getMaxInstanceId()
/*     */   {
/* 545 */     return 0;
/*     */   }
/*     */ 
/*     */   protected static int instanceIdInfo(int paramInt1, int paramInt2)
/*     */   {
/* 550 */     return paramInt1 << 16 | paramInt2;
/*     */   }
/*     */ 
/*     */   protected int findInstanceIdInfo(String paramString)
/*     */   {
/* 560 */     return 0;
/*     */   }
/*     */ 
/*     */   protected String getInstanceIdName(int paramInt)
/*     */   {
/* 567 */     throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   protected Object getInstanceIdValue(int paramInt)
/*     */   {
/* 578 */     throw new IllegalStateException(String.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   protected void setInstanceIdValue(int paramInt, Object paramObject)
/*     */   {
/* 587 */     throw new IllegalStateException(String.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 595 */     throw paramIdFunctionObject.unknown();
/*     */   }
/*     */ 
/*     */   public final IdFunctionObject exportAsJSClass(int paramInt, Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/* 603 */     if ((paramScriptable != this) && (paramScriptable != null)) {
/* 604 */       setParentScope(paramScriptable);
/* 605 */       setPrototype(getObjectPrototype(paramScriptable));
/*     */     }
/*     */ 
/* 608 */     activatePrototypeMap(paramInt);
/* 609 */     IdFunctionObject localIdFunctionObject = this.prototypeValues.createPrecachedConstructor();
/* 610 */     if (paramBoolean) {
/* 611 */       sealObject();
/*     */     }
/* 613 */     fillConstructorProperties(localIdFunctionObject);
/* 614 */     if (paramBoolean) {
/* 615 */       localIdFunctionObject.sealObject();
/*     */     }
/* 617 */     localIdFunctionObject.exportAsScopeProperty();
/* 618 */     return localIdFunctionObject;
/*     */   }
/*     */ 
/*     */   public final boolean hasPrototypeMap()
/*     */   {
/* 623 */     return this.prototypeValues != null;
/*     */   }
/*     */ 
/*     */   public final void activatePrototypeMap(int paramInt)
/*     */   {
/* 628 */     PrototypeValues localPrototypeValues = new PrototypeValues(this, paramInt);
/* 629 */     synchronized (this) {
/* 630 */       if (this.prototypeValues != null)
/* 631 */         throw new IllegalStateException();
/* 632 */       this.prototypeValues = localPrototypeValues;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void initPrototypeMethod(Object paramObject, int paramInt1, String paramString, int paramInt2)
/*     */   {
/* 639 */     Scriptable localScriptable = ScriptableObject.getTopLevelScope(this);
/* 640 */     IdFunctionObject localIdFunctionObject = newIdFunction(paramObject, paramInt1, paramString, paramInt2, localScriptable);
/* 641 */     this.prototypeValues.initValue(paramInt1, paramString, localIdFunctionObject, 2);
/*     */   }
/*     */ 
/*     */   public final void initPrototypeConstructor(IdFunctionObject paramIdFunctionObject)
/*     */   {
/* 646 */     int i = this.prototypeValues.constructorId;
/* 647 */     if (i == 0)
/* 648 */       throw new IllegalStateException();
/* 649 */     if (paramIdFunctionObject.methodId() != i)
/* 650 */       throw new IllegalArgumentException();
/* 651 */     if (isSealed()) paramIdFunctionObject.sealObject();
/* 652 */     this.prototypeValues.initValue(i, "constructor", paramIdFunctionObject, 2);
/*     */   }
/*     */ 
/*     */   public final void initPrototypeValue(int paramInt1, String paramString, Object paramObject, int paramInt2)
/*     */   {
/* 658 */     this.prototypeValues.initValue(paramInt1, paramString, paramObject, paramInt2);
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/* 663 */     throw new IllegalStateException(String.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 668 */     throw new IllegalStateException(paramString);
/*     */   }
/*     */ 
/*     */   protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void addIdFunctionProperty(Scriptable paramScriptable, Object paramObject, int paramInt1, String paramString, int paramInt2)
/*     */   {
/* 678 */     Scriptable localScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
/* 679 */     IdFunctionObject localIdFunctionObject = newIdFunction(paramObject, paramInt1, paramString, paramInt2, localScriptable);
/* 680 */     localIdFunctionObject.addAsProperty(paramScriptable);
/*     */   }
/*     */ 
/*     */   protected static EcmaError incompatibleCallError(IdFunctionObject paramIdFunctionObject)
/*     */   {
/* 706 */     throw ScriptRuntime.typeError1("msg.incompat.call", paramIdFunctionObject.getFunctionName());
/*     */   }
/*     */ 
/*     */   private IdFunctionObject newIdFunction(Object paramObject, int paramInt1, String paramString, int paramInt2, Scriptable paramScriptable)
/*     */   {
/* 713 */     IdFunctionObject localIdFunctionObject = new IdFunctionObject(this, paramObject, paramInt1, paramString, paramInt2, paramScriptable);
/*     */ 
/* 715 */     if (isSealed()) localIdFunctionObject.sealObject();
/* 716 */     return localIdFunctionObject;
/*     */   }
/*     */ 
/*     */   public void defineOwnProperty(Context paramContext, Object paramObject, ScriptableObject paramScriptableObject)
/*     */   {
/* 721 */     if ((paramObject instanceof String)) {
/* 722 */       String str = (String)paramObject;
/* 723 */       int i = findInstanceIdInfo(str);
/*     */       int j;
/*     */       int k;
/*     */       Object localObject;
/* 724 */       if (i != 0) {
/* 725 */         j = i & 0xFFFF;
/* 726 */         if (isAccessorDescriptor(paramScriptableObject)) {
/* 727 */           delete(j);
/*     */         } else {
/* 729 */           k = i >>> 16;
/* 730 */           localObject = getProperty(paramScriptableObject, "value");
/* 731 */           setInstanceIdValue(j, localObject == NOT_FOUND ? Undefined.instance : localObject);
/* 732 */           setAttributes(j, applyDescriptorToAttributeBitset(k, paramScriptableObject));
/* 733 */           return;
/*     */         }
/*     */       }
/* 736 */       if (this.prototypeValues != null) {
/* 737 */         j = this.prototypeValues.findId(str);
/* 738 */         if (j != 0) {
/* 739 */           if (isAccessorDescriptor(paramScriptableObject)) {
/* 740 */             this.prototypeValues.delete(j);
/*     */           } else {
/* 742 */             k = this.prototypeValues.getAttributes(j);
/* 743 */             localObject = getProperty(paramScriptableObject, "value");
/* 744 */             this.prototypeValues.set(j, this, localObject == NOT_FOUND ? Undefined.instance : localObject);
/* 745 */             this.prototypeValues.setAttributes(j, applyDescriptorToAttributeBitset(k, paramScriptableObject));
/* 746 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 751 */     super.defineOwnProperty(paramContext, paramObject, paramScriptableObject);
/*     */   }
/*     */ 
/*     */   protected ScriptableObject getOwnPropertyDescriptor(Context paramContext, Object paramObject)
/*     */   {
/* 757 */     ScriptableObject localScriptableObject = super.getOwnPropertyDescriptor(paramContext, paramObject);
/* 758 */     if ((localScriptableObject == null) && ((paramObject instanceof String))) {
/* 759 */       localScriptableObject = getBuiltInDescriptor((String)paramObject);
/*     */     }
/* 761 */     return localScriptableObject;
/*     */   }
/*     */ 
/*     */   private ScriptableObject getBuiltInDescriptor(String paramString) {
/* 765 */     Object localObject1 = null;
/* 766 */     int i = 0;
/*     */ 
/* 768 */     Object localObject2 = getParentScope();
/* 769 */     if (localObject2 == null) {
/* 770 */       localObject2 = this;
/*     */     }
/*     */ 
/* 773 */     int j = findInstanceIdInfo(paramString);
/*     */     int k;
/* 774 */     if (j != 0) {
/* 775 */       k = j & 0xFFFF;
/* 776 */       localObject1 = getInstanceIdValue(k);
/* 777 */       i = j >>> 16;
/* 778 */       return buildDataDescriptor((Scriptable)localObject2, localObject1, i);
/*     */     }
/* 780 */     if (this.prototypeValues != null) {
/* 781 */       k = this.prototypeValues.findId(paramString);
/* 782 */       if (k != 0) {
/* 783 */         localObject1 = this.prototypeValues.get(k);
/* 784 */         i = this.prototypeValues.getAttributes(k);
/* 785 */         return buildDataDescriptor((Scriptable)localObject2, localObject1, i);
/*     */       }
/*     */     }
/* 788 */     return null;
/*     */   }
/*     */ 
/*     */   private static final class PrototypeValues
/*     */   {
/*     */     private static final int VALUE_SLOT = 0;
/*     */     private static final int NAME_SLOT = 1;
/*     */     private static final int SLOT_SPAN = 2;
/*     */     private IdScriptableObject obj;
/*     */     private int maxId;
/*     */     private volatile Object[] valueArray;
/*     */     private volatile short[] attributeArray;
/*  77 */     private volatile int lastFoundId = 1;
/*     */     int constructorId;
/*     */     private IdFunctionObject constructor;
/*     */     private short constructorAttrs;
/*     */ 
/*     */     PrototypeValues(IdScriptableObject paramIdScriptableObject, int paramInt)
/*     */     {
/*  87 */       if (paramIdScriptableObject == null) throw new IllegalArgumentException();
/*  88 */       if (paramInt < 1) throw new IllegalArgumentException();
/*  89 */       this.obj = paramIdScriptableObject;
/*  90 */       this.maxId = paramInt;
/*     */     }
/*     */ 
/*     */     final int getMaxId()
/*     */     {
/*  95 */       return this.maxId;
/*     */     }
/*     */ 
/*     */     final void initValue(int paramInt1, String paramString, Object paramObject, int paramInt2)
/*     */     {
/* 100 */       if ((1 > paramInt1) || (paramInt1 > this.maxId))
/* 101 */         throw new IllegalArgumentException();
/* 102 */       if (paramString == null)
/* 103 */         throw new IllegalArgumentException();
/* 104 */       if (paramObject == Scriptable.NOT_FOUND)
/* 105 */         throw new IllegalArgumentException();
/* 106 */       ScriptableObject.checkValidAttributes(paramInt2);
/* 107 */       if (this.obj.findPrototypeId(paramString) != paramInt1) {
/* 108 */         throw new IllegalArgumentException(paramString);
/*     */       }
/* 110 */       if (paramInt1 == this.constructorId) {
/* 111 */         if (!(paramObject instanceof IdFunctionObject)) {
/* 112 */           throw new IllegalArgumentException("consructor should be initialized with IdFunctionObject");
/*     */         }
/* 114 */         this.constructor = ((IdFunctionObject)paramObject);
/* 115 */         this.constructorAttrs = ((short)paramInt2);
/* 116 */         return;
/*     */       }
/*     */ 
/* 119 */       initSlot(paramInt1, paramString, paramObject, paramInt2);
/*     */     }
/*     */ 
/*     */     private void initSlot(int paramInt1, String paramString, Object paramObject, int paramInt2)
/*     */     {
/* 125 */       Object[] arrayOfObject = this.valueArray;
/* 126 */       if (arrayOfObject == null) {
/* 127 */         throw new IllegalStateException();
/*     */       }
/* 129 */       if (paramObject == null) {
/* 130 */         paramObject = UniqueTag.NULL_VALUE;
/*     */       }
/* 132 */       int i = (paramInt1 - 1) * 2;
/* 133 */       synchronized (this) {
/* 134 */         Object localObject1 = arrayOfObject[(i + 0)];
/* 135 */         if (localObject1 == null) {
/* 136 */           arrayOfObject[(i + 0)] = paramObject;
/* 137 */           arrayOfObject[(i + 1)] = paramString;
/* 138 */           this.attributeArray[(paramInt1 - 1)] = ((short)paramInt2);
/*     */         }
/* 140 */         else if (!paramString.equals(arrayOfObject[(i + 1)])) {
/* 141 */           throw new IllegalStateException();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     final IdFunctionObject createPrecachedConstructor()
/*     */     {
/* 148 */       if (this.constructorId != 0) throw new IllegalStateException();
/* 149 */       this.constructorId = this.obj.findPrototypeId("constructor");
/* 150 */       if (this.constructorId == 0) {
/* 151 */         throw new IllegalStateException("No id for constructor property");
/*     */       }
/*     */ 
/* 154 */       this.obj.initPrototypeId(this.constructorId);
/* 155 */       if (this.constructor == null) {
/* 156 */         throw new IllegalStateException(this.obj.getClass().getName() + ".initPrototypeId() did not " + "initialize id=" + this.constructorId);
/*     */       }
/*     */ 
/* 160 */       this.constructor.initFunction(this.obj.getClassName(), ScriptableObject.getTopLevelScope(this.obj));
/*     */ 
/* 162 */       this.constructor.markAsConstructor(this.obj);
/* 163 */       return this.constructor;
/*     */     }
/*     */ 
/*     */     final int findId(String paramString)
/*     */     {
/* 168 */       Object[] arrayOfObject = this.valueArray;
/* 169 */       if (arrayOfObject == null) {
/* 170 */         return this.obj.findPrototypeId(paramString);
/*     */       }
/* 172 */       int i = this.lastFoundId;
/* 173 */       if (paramString == arrayOfObject[((i - 1) * 2 + 1)]) {
/* 174 */         return i;
/*     */       }
/* 176 */       i = this.obj.findPrototypeId(paramString);
/* 177 */       if (i != 0) {
/* 178 */         int j = (i - 1) * 2 + 1;
/*     */ 
/* 180 */         arrayOfObject[j] = paramString;
/* 181 */         this.lastFoundId = i;
/*     */       }
/* 183 */       return i;
/*     */     }
/*     */ 
/*     */     final boolean has(int paramInt)
/*     */     {
/* 188 */       Object[] arrayOfObject = this.valueArray;
/* 189 */       if (arrayOfObject == null)
/*     */       {
/* 191 */         return true;
/*     */       }
/* 193 */       int i = (paramInt - 1) * 2 + 0;
/* 194 */       Object localObject = arrayOfObject[i];
/* 195 */       if (localObject == null)
/*     */       {
/* 197 */         return true;
/*     */       }
/* 199 */       return localObject != Scriptable.NOT_FOUND;
/*     */     }
/*     */ 
/*     */     final Object get(int paramInt)
/*     */     {
/* 204 */       Object localObject = ensureId(paramInt);
/* 205 */       if (localObject == UniqueTag.NULL_VALUE) {
/* 206 */         localObject = null;
/*     */       }
/* 208 */       return localObject;
/*     */     }
/*     */ 
/*     */     final void set(int paramInt, Scriptable paramScriptable, Object paramObject)
/*     */     {
/* 213 */       if (paramObject == Scriptable.NOT_FOUND) throw new IllegalArgumentException();
/* 214 */       ensureId(paramInt);
/* 215 */       int i = this.attributeArray[(paramInt - 1)];
/* 216 */       if ((i & 0x1) == 0)
/*     */       {
/*     */         int j;
/* 217 */         if (paramScriptable == this.obj) {
/* 218 */           if (paramObject == null) {
/* 219 */             paramObject = UniqueTag.NULL_VALUE;
/*     */           }
/* 221 */           j = (paramInt - 1) * 2 + 0;
/* 222 */           synchronized (this) {
/* 223 */             this.valueArray[j] = paramObject;
/*     */           }
/*     */         }
/*     */         else {
/* 227 */           j = (paramInt - 1) * 2 + 1;
/* 228 */           ??? = (String)this.valueArray[j];
/* 229 */           paramScriptable.put((String)???, paramScriptable, paramObject);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     final void delete(int paramInt)
/*     */     {
/* 236 */       ensureId(paramInt);
/* 237 */       int i = this.attributeArray[(paramInt - 1)];
/* 238 */       if ((i & 0x4) == 0) {
/* 239 */         int j = (paramInt - 1) * 2 + 0;
/* 240 */         synchronized (this) {
/* 241 */           this.valueArray[j] = Scriptable.NOT_FOUND;
/* 242 */           this.attributeArray[(paramInt - 1)] = 0;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     final int getAttributes(int paramInt)
/*     */     {
/* 249 */       ensureId(paramInt);
/* 250 */       return this.attributeArray[(paramInt - 1)];
/*     */     }
/*     */ 
/*     */     final void setAttributes(int paramInt1, int paramInt2)
/*     */     {
/* 255 */       ScriptableObject.checkValidAttributes(paramInt2);
/* 256 */       ensureId(paramInt1);
/* 257 */       synchronized (this) {
/* 258 */         this.attributeArray[(paramInt1 - 1)] = ((short)paramInt2);
/*     */       }
/*     */     }
/*     */ 
/*     */     final Object[] getNames(boolean paramBoolean, Object[] paramArrayOfObject)
/*     */     {
/* 264 */       Object localObject1 = null;
/* 265 */       int i = 0;
/* 266 */       for (int j = 1; j <= this.maxId; j++) {
/* 267 */         localObject2 = ensureId(j);
/* 268 */         if (((paramBoolean) || ((this.attributeArray[(j - 1)] & 0x2) == 0)) && 
/* 269 */           (localObject2 != Scriptable.NOT_FOUND)) {
/* 270 */           int m = (j - 1) * 2 + 1;
/* 271 */           String str = (String)this.valueArray[m];
/* 272 */           if (localObject1 == null) {
/* 273 */             localObject1 = new Object[this.maxId];
/*     */           }
/* 275 */           localObject1[(i++)] = str;
/*     */         }
/*     */       }
/*     */ 
/* 279 */       if (i == 0)
/* 280 */         return paramArrayOfObject;
/* 281 */       if ((paramArrayOfObject == null) || (paramArrayOfObject.length == 0)) {
/* 282 */         if (i != localObject1.length) {
/* 283 */           Object[] arrayOfObject = new Object[i];
/* 284 */           System.arraycopy(localObject1, 0, arrayOfObject, 0, i);
/* 285 */           localObject1 = arrayOfObject;
/*     */         }
/* 287 */         return localObject1;
/*     */       }
/* 289 */       int k = paramArrayOfObject.length;
/* 290 */       Object localObject2 = new Object[k + i];
/* 291 */       System.arraycopy(paramArrayOfObject, 0, localObject2, 0, k);
/* 292 */       System.arraycopy(localObject1, 0, localObject2, k, i);
/* 293 */       return localObject2;
/*     */     }
/*     */ 
/*     */     private Object ensureId(int paramInt)
/*     */     {
/* 299 */       Object[] arrayOfObject = this.valueArray;
/* 300 */       if (arrayOfObject == null) {
/* 301 */         synchronized (this) {
/* 302 */           arrayOfObject = this.valueArray;
/* 303 */           if (arrayOfObject == null) {
/* 304 */             arrayOfObject = new Object[this.maxId * 2];
/* 305 */             this.valueArray = arrayOfObject;
/* 306 */             this.attributeArray = new short[this.maxId];
/*     */           }
/*     */         }
/*     */       }
/* 310 */       int i = (paramInt - 1) * 2 + 0;
/* 311 */       Object localObject2 = arrayOfObject[i];
/* 312 */       if (localObject2 == null) {
/* 313 */         if (paramInt == this.constructorId) {
/* 314 */           initSlot(this.constructorId, "constructor", this.constructor, this.constructorAttrs);
/*     */ 
/* 316 */           this.constructor = null;
/*     */         } else {
/* 318 */           this.obj.initPrototypeId(paramInt);
/*     */         }
/* 320 */         localObject2 = arrayOfObject[i];
/* 321 */         if (localObject2 == null) {
/* 322 */           throw new IllegalStateException(this.obj.getClass().getName() + ".initPrototypeId(int id) " + "did not initialize id=" + paramInt);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 327 */       return localObject2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.IdScriptableObject
 * JD-Core Version:    0.6.2
 */