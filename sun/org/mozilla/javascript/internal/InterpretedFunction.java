/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.debug.DebuggableScript;
/*     */ 
/*     */ final class InterpretedFunction extends NativeFunction
/*     */   implements Script
/*     */ {
/*     */   InterpreterData idata;
/*     */   SecurityController securityController;
/*     */   Object securityDomain;
/*     */   Scriptable[] functionRegExps;
/*     */ 
/*     */   private InterpretedFunction(InterpreterData paramInterpreterData, Object paramObject)
/*     */   {
/*  55 */     this.idata = paramInterpreterData;
/*     */ 
/*  60 */     Context localContext = Context.getContext();
/*  61 */     SecurityController localSecurityController = localContext.getSecurityController();
/*     */     Object localObject;
/*  63 */     if (localSecurityController != null) {
/*  64 */       localObject = localSecurityController.getDynamicSecurityDomain(paramObject);
/*     */     } else {
/*  66 */       if (paramObject != null) {
/*  67 */         throw new IllegalArgumentException();
/*     */       }
/*  69 */       localObject = null;
/*     */     }
/*     */ 
/*  72 */     this.securityController = localSecurityController;
/*  73 */     this.securityDomain = localObject;
/*     */   }
/*     */ 
/*     */   private InterpretedFunction(InterpretedFunction paramInterpretedFunction, int paramInt)
/*     */   {
/*  78 */     this.idata = paramInterpretedFunction.idata.itsNestedFunctions[paramInt];
/*  79 */     this.securityController = paramInterpretedFunction.securityController;
/*  80 */     this.securityDomain = paramInterpretedFunction.securityDomain;
/*     */   }
/*     */ 
/*     */   static InterpretedFunction createScript(InterpreterData paramInterpreterData, Object paramObject)
/*     */   {
/*  90 */     InterpretedFunction localInterpretedFunction = new InterpretedFunction(paramInterpreterData, paramObject);
/*  91 */     return localInterpretedFunction;
/*     */   }
/*     */ 
/*     */   static InterpretedFunction createFunction(Context paramContext, Scriptable paramScriptable, InterpreterData paramInterpreterData, Object paramObject)
/*     */   {
/* 102 */     InterpretedFunction localInterpretedFunction = new InterpretedFunction(paramInterpreterData, paramObject);
/* 103 */     localInterpretedFunction.initInterpretedFunction(paramContext, paramScriptable);
/* 104 */     return localInterpretedFunction;
/*     */   }
/*     */ 
/*     */   static InterpretedFunction createFunction(Context paramContext, Scriptable paramScriptable, InterpretedFunction paramInterpretedFunction, int paramInt)
/*     */   {
/* 114 */     InterpretedFunction localInterpretedFunction = new InterpretedFunction(paramInterpretedFunction, paramInt);
/* 115 */     localInterpretedFunction.initInterpretedFunction(paramContext, paramScriptable);
/* 116 */     return localInterpretedFunction;
/*     */   }
/*     */ 
/*     */   Scriptable[] createRegExpWraps(Context paramContext, Scriptable paramScriptable)
/*     */   {
/* 121 */     if (this.idata.itsRegExpLiterals == null) Kit.codeBug();
/*     */ 
/* 123 */     RegExpProxy localRegExpProxy = ScriptRuntime.checkRegExpProxy(paramContext);
/* 124 */     int i = this.idata.itsRegExpLiterals.length;
/* 125 */     Scriptable[] arrayOfScriptable = new Scriptable[i];
/* 126 */     for (int j = 0; j != i; j++) {
/* 127 */       arrayOfScriptable[j] = localRegExpProxy.wrapRegExp(paramContext, paramScriptable, this.idata.itsRegExpLiterals[j]);
/*     */     }
/* 129 */     return arrayOfScriptable;
/*     */   }
/*     */ 
/*     */   private void initInterpretedFunction(Context paramContext, Scriptable paramScriptable)
/*     */   {
/* 134 */     initScriptFunction(paramContext, paramScriptable);
/* 135 */     if (this.idata.itsRegExpLiterals != null)
/* 136 */       this.functionRegExps = createRegExpWraps(paramContext, paramScriptable);
/*     */   }
/*     */ 
/*     */   public String getFunctionName()
/*     */   {
/* 143 */     return this.idata.itsName == null ? "" : this.idata.itsName;
/*     */   }
/*     */ 
/*     */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 159 */     if (!ScriptRuntime.hasTopCall(paramContext)) {
/* 160 */       return ScriptRuntime.doTopCall(this, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 162 */     return Interpreter.interpret(this, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public Object exec(Context paramContext, Scriptable paramScriptable)
/*     */   {
/* 167 */     if (!isScript())
/*     */     {
/* 169 */       throw new IllegalStateException();
/*     */     }
/* 171 */     if (!ScriptRuntime.hasTopCall(paramContext))
/*     */     {
/* 173 */       return ScriptRuntime.doTopCall(this, paramContext, paramScriptable, paramScriptable, ScriptRuntime.emptyArgs);
/*     */     }
/*     */ 
/* 176 */     return Interpreter.interpret(this, paramContext, paramScriptable, paramScriptable, ScriptRuntime.emptyArgs);
/*     */   }
/*     */ 
/*     */   public boolean isScript()
/*     */   {
/* 181 */     return this.idata.itsFunctionType == 0;
/*     */   }
/*     */ 
/*     */   public String getEncodedSource()
/*     */   {
/* 187 */     return Interpreter.getEncodedSource(this.idata);
/*     */   }
/*     */ 
/*     */   public DebuggableScript getDebuggableView()
/*     */   {
/* 193 */     return this.idata;
/*     */   }
/*     */ 
/*     */   public Object resumeGenerator(Context paramContext, Scriptable paramScriptable, int paramInt, Object paramObject1, Object paramObject2)
/*     */   {
/* 200 */     return Interpreter.resumeGenerator(paramContext, paramScriptable, paramInt, paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   protected int getLanguageVersion()
/*     */   {
/* 206 */     return this.idata.languageVersion;
/*     */   }
/*     */ 
/*     */   protected int getParamCount()
/*     */   {
/* 212 */     return this.idata.argCount;
/*     */   }
/*     */ 
/*     */   protected int getParamAndVarCount()
/*     */   {
/* 218 */     return this.idata.argNames.length;
/*     */   }
/*     */ 
/*     */   protected String getParamOrVarName(int paramInt)
/*     */   {
/* 224 */     return this.idata.argNames[paramInt];
/*     */   }
/*     */ 
/*     */   protected boolean getParamOrVarConst(int paramInt)
/*     */   {
/* 230 */     return this.idata.argIsConst[paramInt];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.InterpretedFunction
 * JD-Core Version:    0.6.2
 */