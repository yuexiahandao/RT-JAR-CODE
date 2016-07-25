/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ class SpecialRef extends Ref
/*     */ {
/*     */   private static final int SPECIAL_NONE = 0;
/*     */   private static final int SPECIAL_PROTO = 1;
/*     */   private static final int SPECIAL_PARENT = 2;
/*     */   private Scriptable target;
/*     */   private int type;
/*     */   private String name;
/*     */ 
/*     */   private SpecialRef(Scriptable paramScriptable, int paramInt, String paramString)
/*     */   {
/*  53 */     this.target = paramScriptable;
/*  54 */     this.type = paramInt;
/*  55 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   static Ref createSpecial(Context paramContext, Object paramObject, String paramString)
/*     */   {
/*  60 */     Scriptable localScriptable = ScriptRuntime.toObjectOrNull(paramContext, paramObject);
/*  61 */     if (localScriptable == null)
/*  62 */       throw ScriptRuntime.undefReadError(paramObject, paramString);
/*     */     int i;
/*  66 */     if (paramString.equals("__proto__"))
/*  67 */       i = 1;
/*  68 */     else if (paramString.equals("__parent__"))
/*  69 */       i = 2;
/*     */     else {
/*  71 */       throw new IllegalArgumentException(paramString);
/*     */     }
/*     */ 
/*  74 */     if (!paramContext.hasFeature(5))
/*     */     {
/*  76 */       i = 0;
/*     */     }
/*     */ 
/*  79 */     return new SpecialRef(localScriptable, i, paramString);
/*     */   }
/*     */ 
/*     */   public Object get(Context paramContext)
/*     */   {
/*  85 */     switch (this.type) {
/*     */     case 0:
/*  87 */       return ScriptRuntime.getObjectProp(this.target, this.name, paramContext);
/*     */     case 1:
/*  89 */       return this.target.getPrototype();
/*     */     case 2:
/*  91 */       return this.target.getParentScope();
/*     */     }
/*  93 */     throw Kit.codeBug();
/*     */   }
/*     */ 
/*     */   public Object set(Context paramContext, Object paramObject)
/*     */   {
/* 100 */     switch (this.type) {
/*     */     case 0:
/* 102 */       return ScriptRuntime.setObjectProp(this.target, this.name, paramObject, paramContext);
/*     */     case 1:
/*     */     case 2:
/* 106 */       Scriptable localScriptable1 = ScriptRuntime.toObjectOrNull(paramContext, paramObject);
/* 107 */       if (localScriptable1 != null)
/*     */       {
/* 110 */         Scriptable localScriptable2 = localScriptable1;
/*     */         do {
/* 112 */           if (localScriptable2 == this.target) {
/* 113 */             throw Context.reportRuntimeError1("msg.cyclic.value", this.name);
/*     */           }
/*     */ 
/* 116 */           if (this.type == 1)
/* 117 */             localScriptable2 = localScriptable2.getPrototype();
/*     */           else
/* 119 */             localScriptable2 = localScriptable2.getParentScope();
/*     */         }
/* 121 */         while (localScriptable2 != null);
/*     */       }
/* 123 */       if (this.type == 1)
/* 124 */         this.target.setPrototype(localScriptable1);
/*     */       else {
/* 126 */         this.target.setParentScope(localScriptable1);
/*     */       }
/* 128 */       return localScriptable1;
/*     */     }
/*     */ 
/* 131 */     throw Kit.codeBug();
/*     */   }
/*     */ 
/*     */   public boolean has(Context paramContext)
/*     */   {
/* 138 */     if (this.type == 0) {
/* 139 */       return ScriptRuntime.hasObjectElem(this.target, this.name, paramContext);
/*     */     }
/* 141 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean delete(Context paramContext)
/*     */   {
/* 147 */     if (this.type == 0) {
/* 148 */       return ScriptRuntime.deleteObjectElem(this.target, this.name, paramContext);
/*     */     }
/* 150 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.SpecialRef
 * JD-Core Version:    0.6.2
 */