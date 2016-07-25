/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ class NativeScript extends BaseFunction
/*     */ {
/*  60 */   private static final Object SCRIPT_TAG = "Script";
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_toString = 2;
/*     */   private static final int Id_compile = 3;
/*     */   private static final int Id_exec = 4;
/*     */   private static final int MAX_PROTOTYPE_ID = 4;
/*     */   private Script script;
/*     */ 
/*     */   static void init(Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/*  64 */     NativeScript localNativeScript = new NativeScript(null);
/*  65 */     localNativeScript.exportAsJSClass(4, paramScriptable, paramBoolean);
/*     */   }
/*     */ 
/*     */   private NativeScript(Script paramScript)
/*     */   {
/*  70 */     this.script = paramScript;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  79 */     return "Script";
/*     */   }
/*     */ 
/*     */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/*  86 */     if (this.script != null) {
/*  87 */       return this.script.exec(paramContext, paramScriptable1);
/*     */     }
/*  89 */     return Undefined.instance;
/*     */   }
/*     */ 
/*     */   public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*     */   {
/*  95 */     throw Context.reportRuntimeError0("msg.script.is.not.constructor");
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 101 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getArity()
/*     */   {
/* 107 */     return 0;
/*     */   }
/*     */ 
/*     */   String decompile(int paramInt1, int paramInt2)
/*     */   {
/* 113 */     if ((this.script instanceof NativeFunction)) {
/* 114 */       return ((NativeFunction)this.script).decompile(paramInt1, paramInt2);
/*     */     }
/* 116 */     return super.decompile(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*     */     int i;
/*     */     String str;
/* 124 */     switch (paramInt) { case 1:
/* 125 */       i = 1; str = "constructor"; break;
/*     */     case 2:
/* 126 */       i = 0; str = "toString"; break;
/*     */     case 4:
/* 127 */       i = 0; str = "exec"; break;
/*     */     case 3:
/* 128 */       i = 1; str = "compile"; break;
/*     */     default:
/* 129 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */     }
/* 131 */     initPrototypeMethod(SCRIPT_TAG, paramInt, str, i);
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 138 */     if (!paramIdFunctionObject.hasTag(SCRIPT_TAG)) {
/* 139 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 141 */     int i = paramIdFunctionObject.methodId();
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 142 */     switch (i) {
/*     */     case 1:
/* 144 */       localObject1 = paramArrayOfObject.length == 0 ? "" : ScriptRuntime.toString(paramArrayOfObject[0]);
/*     */ 
/* 147 */       localObject2 = compile(paramContext, (String)localObject1);
/* 148 */       NativeScript localNativeScript = new NativeScript((Script)localObject2);
/* 149 */       ScriptRuntime.setObjectProtoAndParent(localNativeScript, paramScriptable1);
/* 150 */       return localNativeScript;
/*     */     case 2:
/* 154 */       localObject1 = realThis(paramScriptable2, paramIdFunctionObject);
/* 155 */       localObject2 = ((NativeScript)localObject1).script;
/* 156 */       if (localObject2 == null) return "";
/* 157 */       return paramContext.decompileScript((Script)localObject2, 0);
/*     */     case 4:
/* 161 */       throw Context.reportRuntimeError1("msg.cant.call.indirect", "exec");
/*     */     case 3:
/* 166 */       localObject1 = realThis(paramScriptable2, paramIdFunctionObject);
/* 167 */       localObject2 = ScriptRuntime.toString(paramArrayOfObject, 0);
/* 168 */       ((NativeScript)localObject1).script = compile(paramContext, (String)localObject2);
/* 169 */       return localObject1;
/*     */     }
/*     */ 
/* 172 */     throw new IllegalArgumentException(String.valueOf(i));
/*     */   }
/*     */ 
/*     */   private static NativeScript realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject)
/*     */   {
/* 177 */     if (!(paramScriptable instanceof NativeScript))
/* 178 */       throw incompatibleCallError(paramIdFunctionObject);
/* 179 */     return (NativeScript)paramScriptable;
/*     */   }
/*     */ 
/*     */   private static Script compile(Context paramContext, String paramString)
/*     */   {
/* 184 */     int[] arrayOfInt = { 0 };
/* 185 */     String str = Context.getSourcePositionFromStack(arrayOfInt);
/* 186 */     if (str == null) {
/* 187 */       str = "<Script object>";
/* 188 */       arrayOfInt[0] = 1;
/*     */     }
/*     */ 
/* 191 */     ErrorReporter localErrorReporter = DefaultErrorReporter.forEval(paramContext.getErrorReporter());
/* 192 */     return paramContext.compileString(paramString, null, localErrorReporter, str, arrayOfInt[0], null);
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 203 */     int i = 0; String str = null;
/* 204 */     switch (paramString.length()) { case 4:
/* 205 */       str = "exec"; i = 4; break;
/*     */     case 7:
/* 206 */       str = "compile"; i = 3; break;
/*     */     case 8:
/* 207 */       str = "toString"; i = 2; break;
/*     */     case 11:
/* 208 */       str = "constructor"; i = 1; break;
/*     */     case 5:
/*     */     case 6:
/*     */     case 9:
/* 210 */     case 10: } if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 214 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeScript
 * JD-Core Version:    0.6.2
 */