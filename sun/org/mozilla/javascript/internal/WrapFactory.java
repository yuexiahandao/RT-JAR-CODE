/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public class WrapFactory
/*     */ {
/* 181 */   private boolean javaPrimitiveWrap = true;
/*     */ 
/*     */   public Object wrap(Context paramContext, Scriptable paramScriptable, Object paramObject, Class<?> paramClass)
/*     */   {
/*  80 */     if ((paramObject == null) || (paramObject == Undefined.instance) || ((paramObject instanceof Scriptable)))
/*     */     {
/*  83 */       return paramObject;
/*     */     }
/*  85 */     if ((paramClass != null) && (paramClass.isPrimitive())) {
/*  86 */       if (paramClass == Void.TYPE)
/*  87 */         return Undefined.instance;
/*  88 */       if (paramClass == Character.TYPE)
/*  89 */         return Integer.valueOf(((Character)paramObject).charValue());
/*  90 */       return paramObject;
/*     */     }
/*  92 */     if (!isJavaPrimitiveWrap()) {
/*  93 */       if (((paramObject instanceof String)) || ((paramObject instanceof Number)) || ((paramObject instanceof Boolean)))
/*     */       {
/*  96 */         return paramObject;
/*  97 */       }if ((paramObject instanceof Character)) {
/*  98 */         return String.valueOf(((Character)paramObject).charValue());
/*     */       }
/*     */     }
/* 101 */     Class localClass = paramObject.getClass();
/* 102 */     if (localClass.isArray()) {
/* 103 */       return NativeJavaArray.wrap(paramScriptable, paramObject);
/*     */     }
/* 105 */     return wrapAsJavaObject(paramContext, paramScriptable, paramObject, paramClass);
/*     */   }
/*     */ 
/*     */   public Scriptable wrapNewObject(Context paramContext, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 117 */     if ((paramObject instanceof Scriptable)) {
/* 118 */       return (Scriptable)paramObject;
/*     */     }
/* 120 */     Class localClass = paramObject.getClass();
/* 121 */     if (localClass.isArray()) {
/* 122 */       return NativeJavaArray.wrap(paramScriptable, paramObject);
/*     */     }
/* 124 */     return wrapAsJavaObject(paramContext, paramScriptable, paramObject, null);
/*     */   }
/*     */ 
/*     */   public Scriptable wrapAsJavaObject(Context paramContext, Scriptable paramScriptable, Object paramObject, Class<?> paramClass)
/*     */   {
/* 149 */     NativeJavaObject localNativeJavaObject = new NativeJavaObject(paramScriptable, paramObject, paramClass);
/* 150 */     return localNativeJavaObject;
/*     */   }
/*     */ 
/*     */   public final boolean isJavaPrimitiveWrap()
/*     */   {
/* 166 */     return this.javaPrimitiveWrap;
/*     */   }
/*     */ 
/*     */   public final void setJavaPrimitiveWrap(boolean paramBoolean)
/*     */   {
/* 174 */     Context localContext = Context.getCurrentContext();
/* 175 */     if ((localContext != null) && (localContext.isSealed())) {
/* 176 */       Context.onSealedMutation();
/*     */     }
/* 178 */     this.javaPrimitiveWrap = paramBoolean;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.WrapFactory
 * JD-Core Version:    0.6.2
 */