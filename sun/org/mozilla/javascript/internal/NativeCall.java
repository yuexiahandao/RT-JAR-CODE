/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public final class NativeCall extends IdScriptableObject
/*     */ {
/*  52 */   private static final Object CALL_TAG = "Call";
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int MAX_PROTOTYPE_ID = 1;
/*     */   NativeFunction function;
/*     */   Object[] originalArgs;
/*     */   transient NativeCall parentActivationCall;
/*     */ 
/*     */   static void init(Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/*  56 */     NativeCall localNativeCall = new NativeCall();
/*  57 */     localNativeCall.exportAsJSClass(1, paramScriptable, paramBoolean);
/*     */   }
/*     */ 
/*     */   NativeCall() {
/*     */   }
/*     */ 
/*     */   NativeCall(NativeFunction paramNativeFunction, Scriptable paramScriptable, Object[] paramArrayOfObject) {
/*  64 */     this.function = paramNativeFunction;
/*     */ 
/*  66 */     setParentScope(paramScriptable);
/*     */ 
/*  69 */     this.originalArgs = (paramArrayOfObject == null ? ScriptRuntime.emptyArgs : paramArrayOfObject);
/*     */ 
/*  72 */     int i = paramNativeFunction.getParamAndVarCount();
/*  73 */     int j = paramNativeFunction.getParamCount();
/*     */     int k;
/*     */     String str;
/*  74 */     if (i != 0) {
/*  75 */       for (k = 0; k < j; k++) {
/*  76 */         str = paramNativeFunction.getParamOrVarName(k);
/*  77 */         Object localObject = k < paramArrayOfObject.length ? paramArrayOfObject[k] : Undefined.instance;
/*     */ 
/*  79 */         defineProperty(str, localObject, 4);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  85 */     if (!super.has("arguments", this)) {
/*  86 */       defineProperty("arguments", new Arguments(this), 4);
/*     */     }
/*     */ 
/*  89 */     if (i != 0)
/*  90 */       for (k = j; k < i; k++) {
/*  91 */         str = paramNativeFunction.getParamOrVarName(k);
/*  92 */         if (!super.has(str, this))
/*  93 */           if (paramNativeFunction.getParamOrVarConst(k))
/*  94 */             defineProperty(str, Undefined.instance, 13);
/*     */           else
/*  96 */             defineProperty(str, Undefined.instance, 4);
/*     */       }
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 105 */     return "Call";
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 111 */     return paramString.equals("constructor") ? 1 : 0;
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*     */     int i;
/*     */     String str;
/* 119 */     if (paramInt == 1) {
/* 120 */       i = 1; str = "constructor";
/*     */     } else {
/* 122 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */     }
/* 124 */     initPrototypeMethod(CALL_TAG, paramInt, str, i);
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 131 */     if (!paramIdFunctionObject.hasTag(CALL_TAG)) {
/* 132 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 134 */     int i = paramIdFunctionObject.methodId();
/* 135 */     if (i == 1) {
/* 136 */       if (paramScriptable2 != null) {
/* 137 */         throw Context.reportRuntimeError1("msg.only.from.new", "Call");
/*     */       }
/* 139 */       ScriptRuntime.checkDeprecated(paramContext, "Call");
/* 140 */       NativeCall localNativeCall = new NativeCall();
/* 141 */       localNativeCall.setPrototype(getObjectPrototype(paramScriptable1));
/* 142 */       return localNativeCall;
/*     */     }
/* 144 */     throw new IllegalArgumentException(String.valueOf(i));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeCall
 * JD-Core Version:    0.6.2
 */