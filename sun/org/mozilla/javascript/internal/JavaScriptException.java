/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public class JavaScriptException extends RhinoException
/*     */ {
/*     */   static final long serialVersionUID = -7666130513694669293L;
/*     */   private Object value;
/*     */ 
/*     */   /** @deprecated */
/*     */   public JavaScriptException(Object paramObject)
/*     */   {
/*  62 */     this(paramObject, "", 0);
/*     */   }
/*     */ 
/*     */   public JavaScriptException(Object paramObject, String paramString, int paramInt)
/*     */   {
/*  72 */     recordErrorOrigin(paramString, paramInt, null, 0);
/*  73 */     this.value = paramObject;
/*     */ 
/*  76 */     if (((paramObject instanceof NativeError)) && (Context.getContext().hasFeature(10)))
/*     */     {
/*  78 */       NativeError localNativeError = (NativeError)paramObject;
/*  79 */       if (!localNativeError.has("fileName", localNativeError)) {
/*  80 */         localNativeError.put("fileName", localNativeError, paramString);
/*     */       }
/*  82 */       if (!localNativeError.has("lineNumber", localNativeError)) {
/*  83 */         localNativeError.put("lineNumber", localNativeError, Integer.valueOf(paramInt));
/*     */       }
/*     */ 
/*  86 */       localNativeError.setStackProvider(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String details()
/*     */   {
/*  93 */     if (this.value == null)
/*  94 */       return "null";
/*  95 */     if ((this.value instanceof NativeError))
/*  96 */       return this.value.toString();
/*     */     try
/*     */     {
/*  99 */       return ScriptRuntime.toString(this.value);
/*     */     }
/*     */     catch (RuntimeException localRuntimeException) {
/* 102 */       if ((this.value instanceof Scriptable))
/* 103 */         return ScriptRuntime.defaultObjectToString((Scriptable)this.value);
/*     */     }
/* 105 */     return this.value.toString();
/*     */   }
/*     */ 
/*     */   public Object getValue()
/*     */   {
/* 115 */     return this.value;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public String getSourceName()
/*     */   {
/* 123 */     return sourceName();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int getLineNumber()
/*     */   {
/* 131 */     return lineNumber();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.JavaScriptException
 * JD-Core Version:    0.6.2
 */