/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public final class NativeContinuation extends IdScriptableObject
/*     */   implements Function
/*     */ {
/*  44 */   private static final Object FTAG = "Continuation";
/*     */   private Object implementation;
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int MAX_PROTOTYPE_ID = 1;
/*     */ 
/*     */   public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/*  50 */     NativeContinuation localNativeContinuation = new NativeContinuation();
/*  51 */     localNativeContinuation.exportAsJSClass(1, paramScriptable, paramBoolean);
/*     */   }
/*     */ 
/*     */   public Object getImplementation()
/*     */   {
/*  56 */     return this.implementation;
/*     */   }
/*     */ 
/*     */   public void initImplementation(Object paramObject)
/*     */   {
/*  61 */     this.implementation = paramObject;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  67 */     return "Continuation";
/*     */   }
/*     */ 
/*     */   public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*     */   {
/*  72 */     throw Context.reportRuntimeError("Direct call is not supported");
/*     */   }
/*     */ 
/*     */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/*  78 */     return Interpreter.restartContinuation(this, paramContext, paramScriptable1, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public static boolean isContinuationConstructor(IdFunctionObject paramIdFunctionObject)
/*     */   {
/*  83 */     if ((paramIdFunctionObject.hasTag(FTAG)) && (paramIdFunctionObject.methodId() == 1)) {
/*  84 */       return true;
/*     */     }
/*  86 */     return false;
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*     */     int i;
/*     */     String str;
/*  94 */     switch (paramInt) { case 1:
/*  95 */       i = 0; str = "constructor"; break;
/*     */     default:
/*  96 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */     }
/*  98 */     initPrototypeMethod(FTAG, paramInt, str, i);
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 105 */     if (!paramIdFunctionObject.hasTag(FTAG)) {
/* 106 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 108 */     int i = paramIdFunctionObject.methodId();
/* 109 */     switch (i) {
/*     */     case 1:
/* 111 */       throw Context.reportRuntimeError("Direct call is not supported");
/*     */     }
/* 113 */     throw new IllegalArgumentException(String.valueOf(i));
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 123 */     int i = 0; String str = null;
/* 124 */     if (paramString.length() == 11) { str = "constructor"; i = 1; }
/* 125 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 129 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeContinuation
 * JD-Core Version:    0.6.2
 */