/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ final class Arguments extends IdScriptableObject
/*     */ {
/*     */   private static final String FTAG = "Arguments";
/*     */   private static final int Id_callee = 1;
/*     */   private static final int Id_length = 2;
/*     */   private static final int Id_caller = 3;
/*     */   private static final int Id_constructor = 4;
/*     */   private static final int MAX_INSTANCE_ID = 4;
/*     */   private Object callerObj;
/*     */   private Object calleeObj;
/*     */   private Object lengthObj;
/*     */   private Object constructor;
/*     */   private NativeCall activation;
/*     */   private BaseFunction objectCtor;
/*     */   private Object[] args;
/*     */ 
/*     */   public Arguments(NativeCall paramNativeCall)
/*     */   {
/*  56 */     this.activation = paramNativeCall;
/*     */ 
/*  58 */     Scriptable localScriptable1 = paramNativeCall.getParentScope();
/*  59 */     setParentScope(localScriptable1);
/*  60 */     setPrototype(ScriptableObject.getObjectPrototype(localScriptable1));
/*     */ 
/*  62 */     this.args = paramNativeCall.originalArgs;
/*  63 */     this.lengthObj = Integer.valueOf(this.args.length);
/*     */ 
/*  65 */     NativeFunction localNativeFunction = paramNativeCall.function;
/*  66 */     this.calleeObj = localNativeFunction;
/*     */ 
/*  68 */     Scriptable localScriptable2 = getTopLevelScope(localScriptable1);
/*  69 */     this.objectCtor = ((BaseFunction)getProperty(localScriptable2, "Object"));
/*     */ 
/*  71 */     this.constructor = this.objectCtor;
/*     */ 
/*  73 */     int i = localNativeFunction.getLanguageVersion();
/*  74 */     if ((i <= 130) && (i != 0))
/*     */     {
/*  77 */       this.callerObj = null;
/*     */     }
/*  79 */     else this.callerObj = NOT_FOUND;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  86 */     return "Arguments";
/*     */   }
/*     */ 
/*     */   private Object arg(int paramInt) {
/*  90 */     if ((paramInt < 0) || (this.args.length <= paramInt)) return NOT_FOUND;
/*  91 */     return this.args[paramInt];
/*     */   }
/*     */ 
/*     */   private void putIntoActivation(int paramInt, Object paramObject)
/*     */   {
/*  97 */     String str = this.activation.function.getParamOrVarName(paramInt);
/*  98 */     this.activation.put(str, this.activation, paramObject);
/*     */   }
/*     */ 
/*     */   private Object getFromActivation(int paramInt) {
/* 102 */     String str = this.activation.function.getParamOrVarName(paramInt);
/* 103 */     return this.activation.get(str, this.activation);
/*     */   }
/*     */ 
/*     */   private void replaceArg(int paramInt, Object paramObject) {
/* 107 */     if (sharedWithActivation(paramInt)) {
/* 108 */       putIntoActivation(paramInt, paramObject);
/*     */     }
/* 110 */     synchronized (this) {
/* 111 */       if (this.args == this.activation.originalArgs) {
/* 112 */         this.args = ((Object[])this.args.clone());
/*     */       }
/* 114 */       this.args[paramInt] = paramObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void removeArg(int paramInt) {
/* 119 */     synchronized (this) {
/* 120 */       if (this.args[paramInt] != NOT_FOUND) {
/* 121 */         if (this.args == this.activation.originalArgs) {
/* 122 */           this.args = ((Object[])this.args.clone());
/*     */         }
/* 124 */         this.args[paramInt] = NOT_FOUND;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean has(int paramInt, Scriptable paramScriptable)
/*     */   {
/* 134 */     if (arg(paramInt) != NOT_FOUND) {
/* 135 */       return true;
/*     */     }
/* 137 */     return super.has(paramInt, paramScriptable);
/*     */   }
/*     */ 
/*     */   public Object get(int paramInt, Scriptable paramScriptable)
/*     */   {
/* 143 */     Object localObject = arg(paramInt);
/* 144 */     if (localObject == NOT_FOUND) {
/* 145 */       return super.get(paramInt, paramScriptable);
/*     */     }
/* 147 */     if (sharedWithActivation(paramInt)) {
/* 148 */       return getFromActivation(paramInt);
/*     */     }
/* 150 */     return localObject;
/*     */   }
/*     */ 
/*     */   private boolean sharedWithActivation(int paramInt)
/*     */   {
/* 157 */     NativeFunction localNativeFunction = this.activation.function;
/* 158 */     int i = localNativeFunction.getParamCount();
/* 159 */     if (paramInt < i)
/*     */     {
/* 162 */       if (paramInt < i - 1) {
/* 163 */         String str = localNativeFunction.getParamOrVarName(paramInt);
/* 164 */         for (int j = paramInt + 1; j < i; j++) {
/* 165 */           if (str.equals(localNativeFunction.getParamOrVarName(j))) {
/* 166 */             return false;
/*     */           }
/*     */         }
/*     */       }
/* 170 */       return true;
/*     */     }
/* 172 */     return false;
/*     */   }
/*     */ 
/*     */   public void put(int paramInt, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 178 */     if (arg(paramInt) == NOT_FOUND)
/* 179 */       super.put(paramInt, paramScriptable, paramObject);
/*     */     else
/* 181 */       replaceArg(paramInt, paramObject);
/*     */   }
/*     */ 
/*     */   public void delete(int paramInt)
/*     */   {
/* 188 */     if ((0 <= paramInt) && (paramInt < this.args.length)) {
/* 189 */       removeArg(paramInt);
/*     */     }
/* 191 */     super.delete(paramInt);
/*     */   }
/*     */ 
/*     */   protected int getMaxInstanceId()
/*     */   {
/* 207 */     return 4;
/*     */   }
/*     */ 
/*     */   protected int findInstanceIdInfo(String paramString)
/*     */   {
/* 215 */     int i = 0; String str = null;
/* 216 */     int m = paramString.length();
/* 217 */     if (m == 6) {
/* 218 */       int k = paramString.charAt(5);
/* 219 */       if (k == 101) { str = "callee"; i = 1;
/* 220 */       } else if (k == 104) { str = "length"; i = 2;
/* 221 */       } else if (k == 114) { str = "caller"; i = 3; }
/*     */     }
/* 223 */     else if (m == 11) { str = "constructor"; i = 4; }
/* 224 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 229 */     if (i == 0) return super.findInstanceIdInfo(paramString);
/*     */     int j;
/* 232 */     switch (i) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/* 237 */       j = 2;
/* 238 */       break;
/*     */     default:
/* 239 */       throw new IllegalStateException();
/*     */     }
/* 241 */     return instanceIdInfo(j, i);
/*     */   }
/*     */ 
/*     */   protected String getInstanceIdName(int paramInt)
/*     */   {
/* 249 */     switch (paramInt) { case 1:
/* 250 */       return "callee";
/*     */     case 2:
/* 251 */       return "length";
/*     */     case 3:
/* 252 */       return "caller";
/*     */     case 4:
/* 253 */       return "constructor";
/*     */     }
/* 255 */     return null;
/*     */   }
/*     */ 
/*     */   protected Object getInstanceIdValue(int paramInt)
/*     */   {
/* 261 */     switch (paramInt) { case 1:
/* 262 */       return this.calleeObj;
/*     */     case 2:
/* 263 */       return this.lengthObj;
/*     */     case 3:
/* 265 */       Object localObject = this.callerObj;
/* 266 */       if (localObject == UniqueTag.NULL_VALUE) { localObject = null;
/* 267 */       } else if (localObject == null) {
/* 268 */         NativeCall localNativeCall = this.activation.parentActivationCall;
/* 269 */         if (localNativeCall != null) {
/* 270 */           localObject = localNativeCall.get("arguments", localNativeCall);
/*     */         }
/*     */       }
/* 273 */       return localObject;
/*     */     case 4:
/* 276 */       return this.constructor;
/*     */     }
/* 278 */     return super.getInstanceIdValue(paramInt);
/*     */   }
/*     */ 
/*     */   protected void setInstanceIdValue(int paramInt, Object paramObject)
/*     */   {
/* 284 */     switch (paramInt) { case 1:
/* 285 */       this.calleeObj = paramObject; return;
/*     */     case 2:
/* 286 */       this.lengthObj = paramObject; return;
/*     */     case 3:
/* 288 */       this.callerObj = (paramObject != null ? paramObject : UniqueTag.NULL_VALUE);
/* 289 */       return;
/*     */     case 4:
/* 290 */       this.constructor = paramObject; return;
/*     */     }
/* 292 */     super.setInstanceIdValue(paramInt, paramObject);
/*     */   }
/*     */ 
/*     */   Object[] getIds(boolean paramBoolean)
/*     */   {
/* 298 */     Object localObject1 = super.getIds(paramBoolean);
/* 299 */     if (this.args.length != 0) {
/* 300 */       boolean[] arrayOfBoolean = new boolean[this.args.length];
/* 301 */       int i = this.args.length;
/*     */       int m;
/* 302 */       for (int j = 0; j != localObject1.length; j++) {
/* 303 */         Object localObject2 = localObject1[j];
/* 304 */         if ((localObject2 instanceof Integer)) {
/* 305 */           m = ((Integer)localObject2).intValue();
/* 306 */           if ((0 <= m) && (m < this.args.length) && 
/* 307 */             (arrayOfBoolean[m] == 0)) {
/* 308 */             arrayOfBoolean[m] = true;
/* 309 */             i--;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 314 */       if (!paramBoolean) {
/* 315 */         for (j = 0; j < arrayOfBoolean.length; j++) {
/* 316 */           if ((arrayOfBoolean[j] == 0) && (super.has(j, this))) {
/* 317 */             arrayOfBoolean[j] = true;
/* 318 */             i--;
/*     */           }
/*     */         }
/*     */       }
/* 322 */       if (i != 0) {
/* 323 */         Object[] arrayOfObject = new Object[i + localObject1.length];
/* 324 */         System.arraycopy(localObject1, 0, arrayOfObject, i, localObject1.length);
/* 325 */         localObject1 = arrayOfObject;
/* 326 */         int k = 0;
/* 327 */         for (m = 0; m != this.args.length; m++) {
/* 328 */           if ((arrayOfBoolean == null) || (arrayOfBoolean[m] == 0)) {
/* 329 */             localObject1[k] = Integer.valueOf(m);
/* 330 */             k++;
/*     */           }
/*     */         }
/* 333 */         if (k != i) Kit.codeBug();
/*     */       }
/*     */     }
/* 336 */     return localObject1;
/*     */   }
/*     */ 
/*     */   protected ScriptableObject getOwnPropertyDescriptor(Context paramContext, Object paramObject)
/*     */   {
/* 341 */     double d = ScriptRuntime.toNumber(paramObject);
/* 342 */     int i = (int)d;
/* 343 */     if (d != i) {
/* 344 */       return super.getOwnPropertyDescriptor(paramContext, paramObject);
/*     */     }
/* 346 */     Object localObject1 = arg(i);
/* 347 */     if (localObject1 == NOT_FOUND) {
/* 348 */       return super.getOwnPropertyDescriptor(paramContext, paramObject);
/*     */     }
/* 350 */     if (sharedWithActivation(i)) {
/* 351 */       localObject1 = getFromActivation(i);
/*     */     }
/* 353 */     if (super.has(i, this)) {
/* 354 */       localObject2 = super.getOwnPropertyDescriptor(paramContext, paramObject);
/* 355 */       ((ScriptableObject)localObject2).put("value", (Scriptable)localObject2, localObject1);
/* 356 */       return localObject2;
/*     */     }
/* 358 */     Object localObject2 = getParentScope();
/* 359 */     if (localObject2 == null) localObject2 = this;
/* 360 */     return buildDataDescriptor((Scriptable)localObject2, localObject1, 0);
/*     */   }
/*     */ 
/*     */   public void defineOwnProperty(Context paramContext, Object paramObject, ScriptableObject paramScriptableObject)
/*     */   {
/* 366 */     super.defineOwnProperty(paramContext, paramObject, paramScriptableObject);
/*     */ 
/* 368 */     double d = ScriptRuntime.toNumber(paramObject);
/* 369 */     int i = (int)d;
/* 370 */     if (d != i) return;
/*     */ 
/* 372 */     Object localObject1 = arg(i);
/* 373 */     if (localObject1 == NOT_FOUND) return;
/*     */ 
/* 375 */     if (isAccessorDescriptor(paramScriptableObject)) {
/* 376 */       removeArg(i);
/* 377 */       return;
/*     */     }
/*     */ 
/* 380 */     Object localObject2 = getProperty(paramScriptableObject, "value");
/* 381 */     if (localObject2 == NOT_FOUND) return;
/*     */ 
/* 383 */     replaceArg(i, localObject2);
/*     */ 
/* 385 */     if (isFalse(getProperty(paramScriptableObject, "writable")))
/* 386 */       removeArg(i);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.Arguments
 * JD-Core Version:    0.6.2
 */