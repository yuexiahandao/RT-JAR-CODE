/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public class BaseFunction extends IdScriptableObject
/*     */   implements Function
/*     */ {
/*  51 */   private static final Object FUNCTION_TAG = "Function";
/*     */   private static final int Id_length = 1;
/*     */   private static final int Id_arity = 2;
/*     */   private static final int Id_name = 3;
/*     */   private static final int Id_prototype = 4;
/*     */   private static final int Id_arguments = 5;
/*     */   private static final int MAX_INSTANCE_ID = 5;
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_toString = 2;
/*     */   private static final int Id_toSource = 3;
/*     */   private static final int Id_apply = 4;
/*     */   private static final int Id_call = 5;
/*     */   private static final int Id_bind = 6;
/*     */   private static final int MAX_PROTOTYPE_ID = 6;
/*     */   private Object prototypeProperty;
/* 601 */   private int prototypePropertyAttributes = 6;
/*     */ 
/*     */   static void init(Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/*  55 */     BaseFunction localBaseFunction = new BaseFunction();
/*     */ 
/*  57 */     localBaseFunction.prototypePropertyAttributes = 7;
/*  58 */     localBaseFunction.exportAsJSClass(6, paramScriptable, paramBoolean);
/*     */   }
/*     */ 
/*     */   public BaseFunction()
/*     */   {
/*     */   }
/*     */ 
/*     */   public BaseFunction(Scriptable paramScriptable1, Scriptable paramScriptable2)
/*     */   {
/*  67 */     super(paramScriptable1, paramScriptable2);
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  72 */     return "Function";
/*     */   }
/*     */ 
/*     */   public String getTypeOf()
/*     */   {
/*  83 */     return avoidObjectDetection() ? "undefined" : "function";
/*     */   }
/*     */ 
/*     */   public boolean hasInstance(Scriptable paramScriptable)
/*     */   {
/* 103 */     Object localObject = ScriptableObject.getProperty(this, "prototype");
/* 104 */     if ((localObject instanceof Scriptable)) {
/* 105 */       return ScriptRuntime.jsDelegatesTo(paramScriptable, (Scriptable)localObject);
/*     */     }
/* 107 */     throw ScriptRuntime.typeError1("msg.instanceof.bad.prototype", getFunctionName());
/*     */   }
/*     */ 
/*     */   protected int getMaxInstanceId()
/*     */   {
/* 125 */     return 5;
/*     */   }
/*     */ 
/*     */   protected int findInstanceIdInfo(String paramString)
/*     */   {
/* 133 */     int i = 0; String str = null;
/* 134 */     switch (paramString.length()) { case 4:
/* 135 */       str = "name"; i = 3; break;
/*     */     case 5:
/* 136 */       str = "arity"; i = 2; break;
/*     */     case 6:
/* 137 */       str = "length"; i = 1; break;
/*     */     case 9:
/* 138 */       int k = paramString.charAt(0);
/* 139 */       if (k == 97) { str = "arguments"; i = 5;
/* 140 */       } else if (k == 112) { str = "prototype"; i = 4; } break;
/*     */     case 7:
/*     */     case 8: }
/* 143 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 149 */     if (i == 0) return super.findInstanceIdInfo(paramString);
/*     */     int j;
/* 152 */     switch (i) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/* 156 */       j = 7;
/* 157 */       break;
/*     */     case 4:
/* 159 */       j = this.prototypePropertyAttributes;
/* 160 */       break;
/*     */     case 5:
/* 162 */       j = 6;
/* 163 */       break;
/*     */     default:
/* 164 */       throw new IllegalStateException();
/*     */     }
/* 166 */     return instanceIdInfo(j, i);
/*     */   }
/*     */ 
/*     */   protected String getInstanceIdName(int paramInt)
/*     */   {
/* 172 */     switch (paramInt) { case 1:
/* 173 */       return "length";
/*     */     case 2:
/* 174 */       return "arity";
/*     */     case 3:
/* 175 */       return "name";
/*     */     case 4:
/* 176 */       return "prototype";
/*     */     case 5:
/* 177 */       return "arguments";
/*     */     }
/* 179 */     return super.getInstanceIdName(paramInt);
/*     */   }
/*     */ 
/*     */   protected Object getInstanceIdValue(int paramInt)
/*     */   {
/* 185 */     switch (paramInt) { case 1:
/* 186 */       return ScriptRuntime.wrapInt(getLength());
/*     */     case 2:
/* 187 */       return ScriptRuntime.wrapInt(getArity());
/*     */     case 3:
/* 188 */       return getFunctionName();
/*     */     case 4:
/* 189 */       return getPrototypeProperty();
/*     */     case 5:
/* 190 */       return getArguments();
/*     */     }
/* 192 */     return super.getInstanceIdValue(paramInt);
/*     */   }
/*     */ 
/*     */   protected void setInstanceIdValue(int paramInt, Object paramObject)
/*     */   {
/* 198 */     switch (paramInt) {
/*     */     case 4:
/* 200 */       if ((this.prototypePropertyAttributes & 0x1) == 0) {
/* 201 */         this.prototypeProperty = (paramObject != null ? paramObject : UniqueTag.NULL_VALUE);
/*     */       }
/*     */ 
/* 204 */       return;
/*     */     case 5:
/* 206 */       if (paramObject == NOT_FOUND)
/*     */       {
/* 208 */         Kit.codeBug();
/*     */       }
/* 210 */       defaultPut("arguments", paramObject);
/* 211 */       return;
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/* 215 */       return;
/*     */     }
/* 217 */     super.setInstanceIdValue(paramInt, paramObject);
/*     */   }
/*     */ 
/*     */   protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject)
/*     */   {
/* 226 */     paramIdFunctionObject.setPrototype(this);
/* 227 */     super.fillConstructorProperties(paramIdFunctionObject);
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*     */     int i;
/*     */     String str;
/* 235 */     switch (paramInt) { case 1:
/* 236 */       i = 1; str = "constructor"; break;
/*     */     case 2:
/* 237 */       i = 1; str = "toString"; break;
/*     */     case 3:
/* 238 */       i = 1; str = "toSource"; break;
/*     */     case 4:
/* 239 */       i = 2; str = "apply"; break;
/*     */     case 5:
/* 240 */       i = 1; str = "call"; break;
/*     */     case 6:
/* 241 */       i = 1; str = "bind"; break;
/*     */     default:
/* 242 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */     }
/* 244 */     initPrototypeMethod(FUNCTION_TAG, paramInt, str, i);
/*     */   }
/*     */ 
/*     */   static boolean isApply(IdFunctionObject paramIdFunctionObject) {
/* 248 */     return (paramIdFunctionObject.hasTag(FUNCTION_TAG)) && (paramIdFunctionObject.methodId() == 4);
/*     */   }
/*     */ 
/*     */   static boolean isApplyOrCall(IdFunctionObject paramIdFunctionObject) {
/* 252 */     if (paramIdFunctionObject.hasTag(FUNCTION_TAG)) {
/* 253 */       switch (paramIdFunctionObject.methodId()) {
/*     */       case 4:
/*     */       case 5:
/* 256 */         return true;
/*     */       }
/*     */     }
/* 259 */     return false;
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 266 */     if (!paramIdFunctionObject.hasTag(FUNCTION_TAG)) {
/* 267 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 269 */     int i = paramIdFunctionObject.methodId();
/*     */     Object localObject;
/*     */     int j;
/* 270 */     switch (i) {
/*     */     case 1:
/* 272 */       return jsConstructor(paramContext, paramScriptable1, paramArrayOfObject);
/*     */     case 2:
/* 275 */       localObject = realFunction(paramScriptable2, paramIdFunctionObject);
/* 276 */       j = ScriptRuntime.toInt32(paramArrayOfObject, 0);
/* 277 */       return ((BaseFunction)localObject).decompile(j, 0);
/*     */     case 3:
/* 281 */       localObject = realFunction(paramScriptable2, paramIdFunctionObject);
/* 282 */       j = 0;
/* 283 */       int k = 2;
/* 284 */       if (paramArrayOfObject.length != 0) {
/* 285 */         j = ScriptRuntime.toInt32(paramArrayOfObject[0]);
/* 286 */         if (j >= 0)
/* 287 */           k = 0;
/*     */         else {
/* 289 */           j = 0;
/*     */         }
/*     */       }
/* 292 */       return ((BaseFunction)localObject).decompile(j, k);
/*     */     case 4:
/*     */     case 5:
/* 297 */       return ScriptRuntime.applyOrCall(i == 4, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     case 6:
/* 301 */       if (!(paramScriptable2 instanceof Callable)) {
/* 302 */         throw ScriptRuntime.notFunctionError(paramScriptable2);
/*     */       }
/* 304 */       localObject = (Callable)paramScriptable2;
/* 305 */       j = paramArrayOfObject.length;
/*     */       Scriptable localScriptable;
/*     */       Object[] arrayOfObject;
/* 308 */       if (j > 0) {
/* 309 */         localScriptable = ScriptRuntime.toObjectOrNull(paramContext, paramArrayOfObject[0], paramScriptable1);
/* 310 */         arrayOfObject = new Object[j - 1];
/* 311 */         System.arraycopy(paramArrayOfObject, 1, arrayOfObject, 0, j - 1);
/*     */       } else {
/* 313 */         localScriptable = null;
/* 314 */         arrayOfObject = ScriptRuntime.emptyArgs;
/*     */       }
/* 316 */       return new BoundFunction(paramContext, paramScriptable1, (Callable)localObject, localScriptable, arrayOfObject);
/*     */     }
/* 318 */     throw new IllegalArgumentException(String.valueOf(i));
/*     */   }
/*     */ 
/*     */   private BaseFunction realFunction(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject)
/*     */   {
/* 323 */     Object localObject = paramScriptable.getDefaultValue(ScriptRuntime.FunctionClass);
/* 324 */     if ((localObject instanceof BaseFunction)) {
/* 325 */       return (BaseFunction)localObject;
/*     */     }
/* 327 */     throw ScriptRuntime.typeError1("msg.incompat.call", paramIdFunctionObject.getFunctionName());
/*     */   }
/*     */ 
/*     */   public void setImmunePrototypeProperty(Object paramObject)
/*     */   {
/* 337 */     if ((this.prototypePropertyAttributes & 0x1) != 0) {
/* 338 */       throw new IllegalStateException();
/*     */     }
/* 340 */     this.prototypeProperty = (paramObject != null ? paramObject : UniqueTag.NULL_VALUE);
/* 341 */     this.prototypePropertyAttributes = 7;
/*     */   }
/*     */ 
/*     */   protected Scriptable getClassPrototype()
/*     */   {
/* 346 */     Object localObject = getPrototypeProperty();
/* 347 */     if ((localObject instanceof Scriptable)) {
/* 348 */       return (Scriptable)localObject;
/*     */     }
/* 350 */     return getClassPrototype(this, "Object");
/*     */   }
/*     */ 
/*     */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 359 */     return Undefined.instance;
/*     */   }
/*     */ 
/*     */   public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*     */   {
/* 364 */     Scriptable localScriptable1 = createObject(paramContext, paramScriptable);
/*     */     Object localObject;
/* 365 */     if (localScriptable1 != null) {
/* 366 */       localObject = call(paramContext, paramScriptable, localScriptable1, paramArrayOfObject);
/* 367 */       if ((localObject instanceof Scriptable))
/* 368 */         localScriptable1 = (Scriptable)localObject;
/*     */     }
/*     */     else {
/* 371 */       localObject = call(paramContext, paramScriptable, null, paramArrayOfObject);
/* 372 */       if (!(localObject instanceof Scriptable))
/*     */       {
/* 375 */         throw new IllegalStateException("Bad implementaion of call as constructor, name=" + getFunctionName() + " in " + getClass().getName());
/*     */       }
/*     */ 
/* 379 */       localScriptable1 = (Scriptable)localObject;
/* 380 */       if (localScriptable1.getPrototype() == null) {
/* 381 */         localScriptable1.setPrototype(getClassPrototype());
/*     */       }
/* 383 */       if (localScriptable1.getParentScope() == null) {
/* 384 */         Scriptable localScriptable2 = getParentScope();
/* 385 */         if (localScriptable1 != localScriptable2) {
/* 386 */           localScriptable1.setParentScope(localScriptable2);
/*     */         }
/*     */       }
/*     */     }
/* 390 */     return localScriptable1;
/*     */   }
/*     */ 
/*     */   public Scriptable createObject(Context paramContext, Scriptable paramScriptable)
/*     */   {
/* 405 */     NativeObject localNativeObject = new NativeObject();
/* 406 */     localNativeObject.setPrototype(getClassPrototype());
/* 407 */     localNativeObject.setParentScope(getParentScope());
/* 408 */     return localNativeObject;
/*     */   }
/*     */ 
/*     */   String decompile(int paramInt1, int paramInt2)
/*     */   {
/* 421 */     StringBuffer localStringBuffer = new StringBuffer();
/* 422 */     int i = 0 != (paramInt2 & 0x1) ? 1 : 0;
/* 423 */     if (i == 0) {
/* 424 */       localStringBuffer.append("function ");
/* 425 */       localStringBuffer.append(getFunctionName());
/* 426 */       localStringBuffer.append("() {\n\t");
/*     */     }
/* 428 */     localStringBuffer.append("[native code, arity=");
/* 429 */     localStringBuffer.append(getArity());
/* 430 */     localStringBuffer.append("]\n");
/* 431 */     if (i == 0) {
/* 432 */       localStringBuffer.append("}\n");
/*     */     }
/* 434 */     return localStringBuffer.toString();
/*     */   }
/*     */   public int getArity() {
/* 437 */     return 0;
/*     */   }
/* 439 */   public int getLength() { return 0; }
/*     */ 
/*     */   public String getFunctionName()
/*     */   {
/* 443 */     return "";
/*     */   }
/*     */ 
/*     */   final Object getPrototypeProperty() {
/* 447 */     Object localObject1 = this.prototypeProperty;
/* 448 */     if (localObject1 == null) {
/* 449 */       synchronized (this) {
/* 450 */         localObject1 = this.prototypeProperty;
/* 451 */         if (localObject1 == null) {
/* 452 */           setupDefaultPrototype();
/* 453 */           localObject1 = this.prototypeProperty;
/*     */         }
/*     */       }
/*     */     }
/* 457 */     else if (localObject1 == UniqueTag.NULL_VALUE) localObject1 = null;
/* 458 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private void setupDefaultPrototype()
/*     */   {
/* 463 */     NativeObject localNativeObject = new NativeObject();
/*     */ 
/* 465 */     localNativeObject.defineProperty("constructor", this, 2);
/*     */ 
/* 469 */     this.prototypeProperty = localNativeObject;
/* 470 */     Scriptable localScriptable = getObjectPrototype(this);
/* 471 */     if (localScriptable != localNativeObject)
/*     */     {
/* 473 */       localNativeObject.setPrototype(localScriptable);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Object getArguments()
/*     */   {
/* 482 */     Object localObject = defaultGet("arguments");
/* 483 */     if (localObject != NOT_FOUND)
/*     */     {
/* 489 */       return localObject;
/*     */     }
/* 491 */     Context localContext = Context.getContext();
/* 492 */     NativeCall localNativeCall = ScriptRuntime.findFunctionActivation(localContext, this);
/* 493 */     return localNativeCall == null ? null : localNativeCall.get("arguments", localNativeCall);
/*     */   }
/*     */ 
/*     */   private static Object jsConstructor(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*     */   {
/* 501 */     int i = paramArrayOfObject.length;
/* 502 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 504 */     localStringBuffer.append("function ");
/*     */ 
/* 511 */     if (paramContext.getLanguageVersion() != 120) {
/* 512 */       localStringBuffer.append("anonymous");
/*     */     }
/* 514 */     localStringBuffer.append('(');
/*     */ 
/* 517 */     for (int j = 0; j < i - 1; j++) {
/* 518 */       if (j > 0) {
/* 519 */         localStringBuffer.append(',');
/*     */       }
/* 521 */       localStringBuffer.append(ScriptRuntime.toString(paramArrayOfObject[j]));
/*     */     }
/* 523 */     localStringBuffer.append(") {");
/* 524 */     if (i != 0)
/*     */     {
/* 526 */       str1 = ScriptRuntime.toString(paramArrayOfObject[(i - 1)]);
/* 527 */       localStringBuffer.append(str1);
/*     */     }
/* 529 */     localStringBuffer.append("\n}");
/* 530 */     String str1 = localStringBuffer.toString();
/*     */ 
/* 532 */     int[] arrayOfInt = new int[1];
/* 533 */     String str2 = Context.getSourcePositionFromStack(arrayOfInt);
/* 534 */     if (str2 == null) {
/* 535 */       str2 = "<eval'ed string>";
/* 536 */       arrayOfInt[0] = 1;
/*     */     }
/*     */ 
/* 539 */     String str3 = ScriptRuntime.makeUrlForGeneratedScript(false, str2, arrayOfInt[0]);
/*     */ 
/* 542 */     Scriptable localScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
/*     */ 
/* 545 */     ErrorReporter localErrorReporter = DefaultErrorReporter.forEval(paramContext.getErrorReporter());
/*     */ 
/* 547 */     Evaluator localEvaluator = Context.createInterpreter();
/* 548 */     if (localEvaluator == null) {
/* 549 */       throw new JavaScriptException("Interpreter not present", str2, arrayOfInt[0]);
/*     */     }
/*     */ 
/* 555 */     return paramContext.compileFunction(localScriptable, str1, localEvaluator, localErrorReporter, str3, 1, null);
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 565 */     int i = 0; String str = null;
/*     */     int j;
/* 566 */     switch (paramString.length()) { case 4:
/* 567 */       j = paramString.charAt(0);
/* 568 */       if (j == 98) { str = "bind"; i = 6;
/* 569 */       } else if (j == 99) { str = "call"; i = 5; } break;
/*     */     case 5:
/* 571 */       str = "apply"; i = 4; break;
/*     */     case 8:
/* 572 */       j = paramString.charAt(3);
/* 573 */       if (j == 111) { str = "toSource"; i = 3;
/* 574 */       } else if (j == 116) { str = "toString"; i = 2; } break;
/*     */     case 11:
/* 576 */       str = "constructor"; i = 1; break;
/*     */     case 6:
/*     */     case 7:
/*     */     case 9:
/* 578 */     case 10: } if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 582 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.BaseFunction
 * JD-Core Version:    0.6.2
 */