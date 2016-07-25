/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public final class NativeGenerator extends IdScriptableObject
/*     */ {
/*  40 */   private static final Object GENERATOR_TAG = "Generator";
/*     */   public static final int GENERATOR_SEND = 0;
/*     */   public static final int GENERATOR_THROW = 1;
/*     */   public static final int GENERATOR_CLOSE = 2;
/*     */   private static final int Id_close = 1;
/*     */   private static final int Id_next = 2;
/*     */   private static final int Id_send = 3;
/*     */   private static final int Id_throw = 4;
/*     */   private static final int Id___iterator__ = 5;
/*     */   private static final int MAX_PROTOTYPE_ID = 5;
/*     */   private NativeFunction function;
/*     */   private Object savedState;
/*     */   private String lineSource;
/*     */   private int lineNumber;
/* 280 */   private boolean firstTime = true;
/*     */   private boolean locked;
/*     */ 
/*     */   static NativeGenerator init(ScriptableObject paramScriptableObject, boolean paramBoolean)
/*     */   {
/*  47 */     NativeGenerator localNativeGenerator = new NativeGenerator();
/*  48 */     if (paramScriptableObject != null) {
/*  49 */       localNativeGenerator.setParentScope(paramScriptableObject);
/*  50 */       localNativeGenerator.setPrototype(getObjectPrototype(paramScriptableObject));
/*     */     }
/*  52 */     localNativeGenerator.activatePrototypeMap(5);
/*  53 */     if (paramBoolean) {
/*  54 */       localNativeGenerator.sealObject();
/*     */     }
/*     */ 
/*  61 */     if (paramScriptableObject != null) {
/*  62 */       paramScriptableObject.associateValue(GENERATOR_TAG, localNativeGenerator);
/*     */     }
/*     */ 
/*  65 */     return localNativeGenerator;
/*     */   }
/*     */ 
/*     */   private NativeGenerator()
/*     */   {
/*     */   }
/*     */ 
/*     */   public NativeGenerator(Scriptable paramScriptable, NativeFunction paramNativeFunction, Object paramObject)
/*     */   {
/*  76 */     this.function = paramNativeFunction;
/*  77 */     this.savedState = paramObject;
/*     */ 
/*  81 */     Scriptable localScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
/*  82 */     setParentScope(localScriptable);
/*  83 */     NativeGenerator localNativeGenerator = (NativeGenerator)ScriptableObject.getTopScopeValue(localScriptable, GENERATOR_TAG);
/*     */ 
/*  85 */     setPrototype(localNativeGenerator);
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  94 */     return "Generator";
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */     throws Throwable
/*     */   {
/* 102 */     if (this.savedState != null)
/*     */     {
/* 107 */       Context localContext = Context.getCurrentContext();
/* 108 */       ContextFactory localContextFactory = localContext != null ? localContext.getFactory() : ContextFactory.getGlobal();
/*     */ 
/* 110 */       localContextFactory.call(new CloseGeneratorAction(this));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*     */     int i;
/*     */     String str;
/* 139 */     switch (paramInt) { case 1:
/* 140 */       i = 1; str = "close"; break;
/*     */     case 2:
/* 141 */       i = 1; str = "next"; break;
/*     */     case 3:
/* 142 */       i = 0; str = "send"; break;
/*     */     case 4:
/* 143 */       i = 0; str = "throw"; break;
/*     */     case 5:
/* 144 */       i = 1; str = "__iterator__"; break;
/*     */     default:
/* 145 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */     }
/* 147 */     initPrototypeMethod(GENERATOR_TAG, paramInt, str, i);
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 154 */     if (!paramIdFunctionObject.hasTag(GENERATOR_TAG)) {
/* 155 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 157 */     int i = paramIdFunctionObject.methodId();
/*     */ 
/* 159 */     if (!(paramScriptable2 instanceof NativeGenerator)) {
/* 160 */       throw incompatibleCallError(paramIdFunctionObject);
/*     */     }
/* 162 */     NativeGenerator localNativeGenerator = (NativeGenerator)paramScriptable2;
/*     */ 
/* 164 */     switch (i)
/*     */     {
/*     */     case 1:
/* 168 */       return localNativeGenerator.resume(paramContext, paramScriptable1, 2, new GeneratorClosedException());
/*     */     case 2:
/* 173 */       localNativeGenerator.firstTime = false;
/* 174 */       return localNativeGenerator.resume(paramContext, paramScriptable1, 0, Undefined.instance);
/*     */     case 3:
/* 178 */       Object localObject = paramArrayOfObject.length > 0 ? paramArrayOfObject[0] : Undefined.instance;
/* 179 */       if ((localNativeGenerator.firstTime) && (!localObject.equals(Undefined.instance))) {
/* 180 */         throw ScriptRuntime.typeError0("msg.send.newborn");
/*     */       }
/* 182 */       return localNativeGenerator.resume(paramContext, paramScriptable1, 0, localObject);
/*     */     case 4:
/* 186 */       return localNativeGenerator.resume(paramContext, paramScriptable1, 1, paramArrayOfObject.length > 0 ? paramArrayOfObject[0] : Undefined.instance);
/*     */     case 5:
/* 190 */       return paramScriptable2;
/*     */     }
/*     */ 
/* 193 */     throw new IllegalArgumentException(String.valueOf(i));
/*     */   }
/*     */ 
/*     */   private Object resume(Context paramContext, Scriptable paramScriptable, int paramInt, Object paramObject)
/*     */   {
/* 200 */     if (this.savedState == null) {
/* 201 */       if (paramInt == 2)
/* 202 */         return Undefined.instance;
/*     */       Object localObject1;
/* 204 */       if (paramInt == 1)
/* 205 */         localObject1 = paramObject;
/*     */       else {
/* 207 */         localObject1 = NativeIterator.getStopIterationObject(paramScriptable);
/*     */       }
/* 209 */       throw new JavaScriptException(localObject1, this.lineSource, this.lineNumber);
/*     */     }
/*     */     try {
/* 212 */       synchronized (this)
/*     */       {
/* 216 */         if (this.locked)
/* 217 */           throw ScriptRuntime.typeError0("msg.already.exec.gen");
/* 218 */         this.locked = true;
/*     */       }
/* 220 */       return this.function.resumeGenerator(paramContext, paramScriptable, paramInt, this.savedState, paramObject);
/*     */     }
/*     */     catch (GeneratorClosedException localGeneratorClosedException)
/*     */     {
/* 226 */       return Undefined.instance;
/*     */     } catch (RhinoException localRhinoException) {
/* 228 */       this.lineNumber = localRhinoException.lineNumber();
/* 229 */       this.lineSource = localRhinoException.lineSource();
/* 230 */       this.savedState = null;
/* 231 */       throw localRhinoException;
/*     */     } finally {
/* 233 */       synchronized (this) {
/* 234 */         this.locked = false;
/*     */       }
/* 236 */       if (paramInt == 2)
/* 237 */         this.savedState = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 247 */     int i = 0; String str = null;
/* 248 */     int k = paramString.length();
/*     */     int j;
/* 249 */     if (k == 4) {
/* 250 */       j = paramString.charAt(0);
/* 251 */       if (j == 110) { str = "next"; i = 2;
/* 252 */       } else if (j == 115) { str = "send"; i = 3; }
/*     */     }
/* 254 */     else if (k == 5) {
/* 255 */       j = paramString.charAt(0);
/* 256 */       if (j == 99) { str = "close"; i = 1;
/* 257 */       } else if (j == 116) { str = "throw"; i = 4; }
/*     */     }
/* 259 */     else if (k == 12) { str = "__iterator__"; i = 5; }
/* 260 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 264 */     return i;
/*     */   }
/*     */ 
/*     */   private static class CloseGeneratorAction
/*     */     implements ContextAction
/*     */   {
/*     */     private NativeGenerator generator;
/*     */ 
/*     */     CloseGeneratorAction(NativeGenerator paramNativeGenerator)
/*     */     {
/* 118 */       this.generator = paramNativeGenerator;
/*     */     }
/*     */ 
/*     */     public Object run(Context paramContext) {
/* 122 */       Scriptable localScriptable = ScriptableObject.getTopLevelScope(this.generator);
/* 123 */       Callable local1 = new Callable()
/*     */       {
/*     */         public Object call(Context paramAnonymousContext, Scriptable paramAnonymousScriptable1, Scriptable paramAnonymousScriptable2, Object[] paramAnonymousArrayOfObject) {
/* 126 */           return ((NativeGenerator)paramAnonymousScriptable2).resume(paramAnonymousContext, paramAnonymousScriptable1, 2, new NativeGenerator.GeneratorClosedException());
/*     */         }
/*     */       };
/* 130 */       return ScriptRuntime.doTopCall(local1, paramContext, localScriptable, this.generator, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class GeneratorClosedException extends RuntimeException
/*     */   {
/*     */     private static final long serialVersionUID = 2561315658662379681L;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeGenerator
 * JD-Core Version:    0.6.2
 */