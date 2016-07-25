/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public class BoundFunction extends BaseFunction
/*     */ {
/*     */   private final Callable targetFunction;
/*     */   private final Scriptable boundThis;
/*     */   private final Object[] boundArgs;
/*     */   private final int length;
/*     */ 
/*     */   public BoundFunction(Context paramContext, Scriptable paramScriptable1, Callable paramCallable, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/*  56 */     this.targetFunction = paramCallable;
/*  57 */     this.boundThis = paramScriptable2;
/*  58 */     this.boundArgs = paramArrayOfObject;
/*  59 */     if ((paramCallable instanceof BaseFunction))
/*  60 */       this.length = Math.max(0, ((BaseFunction)paramCallable).getLength() - paramArrayOfObject.length);
/*     */     else {
/*  62 */       this.length = 0;
/*     */     }
/*     */ 
/*  65 */     ScriptRuntime.setFunctionProtoAndParent(this, paramScriptable1);
/*     */ 
/*  67 */     BaseFunction localBaseFunction = ScriptRuntime.typeErrorThrower();
/*  68 */     NativeObject localNativeObject = new NativeObject();
/*  69 */     localNativeObject.put("get", localNativeObject, localBaseFunction);
/*  70 */     localNativeObject.put("set", localNativeObject, localBaseFunction);
/*  71 */     localNativeObject.put("enumerable", localNativeObject, Boolean.valueOf(false));
/*  72 */     localNativeObject.put("configurable", localNativeObject, Boolean.valueOf(false));
/*  73 */     localNativeObject.preventExtensions();
/*     */ 
/*  75 */     defineOwnProperty(paramContext, "caller", localNativeObject);
/*  76 */     defineOwnProperty(paramContext, "arguments", localNativeObject);
/*     */   }
/*     */ 
/*     */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/*  82 */     Scriptable localScriptable = this.boundThis != null ? this.boundThis : ScriptRuntime.getTopCallScope(paramContext);
/*  83 */     return this.targetFunction.call(paramContext, paramScriptable1, localScriptable, concat(this.boundArgs, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*     */   {
/*  88 */     if ((this.targetFunction instanceof Function)) {
/*  89 */       return ((Function)this.targetFunction).construct(paramContext, paramScriptable, concat(this.boundArgs, paramArrayOfObject));
/*     */     }
/*  91 */     throw ScriptRuntime.typeError0("msg.not.ctor");
/*     */   }
/*     */ 
/*     */   public boolean hasInstance(Scriptable paramScriptable)
/*     */   {
/*  96 */     if ((this.targetFunction instanceof Function)) {
/*  97 */       return ((Function)this.targetFunction).hasInstance(paramScriptable);
/*     */     }
/*  99 */     throw ScriptRuntime.typeError0("msg.not.ctor");
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 104 */     return this.length;
/*     */   }
/*     */ 
/*     */   private Object[] concat(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2) {
/* 108 */     Object[] arrayOfObject = new Object[paramArrayOfObject1.length + paramArrayOfObject2.length];
/* 109 */     System.arraycopy(paramArrayOfObject1, 0, arrayOfObject, 0, paramArrayOfObject1.length);
/* 110 */     System.arraycopy(paramArrayOfObject2, 0, arrayOfObject, paramArrayOfObject1.length, paramArrayOfObject2.length);
/* 111 */     return arrayOfObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.BoundFunction
 * JD-Core Version:    0.6.2
 */