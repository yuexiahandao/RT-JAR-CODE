/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public class Delegator
/*     */   implements Function
/*     */ {
/*  59 */   protected Scriptable obj = null;
/*     */ 
/*     */   public Delegator()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Delegator(Scriptable paramScriptable)
/*     */   {
/*  80 */     this.obj = paramScriptable;
/*     */   }
/*     */ 
/*     */   protected Delegator newInstance()
/*     */   {
/*     */     try
/*     */     {
/*  92 */       return (Delegator)getClass().newInstance();
/*     */     } catch (Exception localException) {
/*  94 */       throw Context.throwAsScriptRuntimeEx(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Scriptable getDelegee()
/*     */   {
/* 104 */     return this.obj;
/*     */   }
/*     */ 
/*     */   public void setDelegee(Scriptable paramScriptable)
/*     */   {
/* 113 */     this.obj = paramScriptable;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 119 */     return this.obj.getClassName();
/*     */   }
/*     */ 
/*     */   public Object get(String paramString, Scriptable paramScriptable)
/*     */   {
/* 125 */     return this.obj.get(paramString, paramScriptable);
/*     */   }
/*     */ 
/*     */   public Object get(int paramInt, Scriptable paramScriptable)
/*     */   {
/* 131 */     return this.obj.get(paramInt, paramScriptable);
/*     */   }
/*     */ 
/*     */   public boolean has(String paramString, Scriptable paramScriptable)
/*     */   {
/* 137 */     return this.obj.has(paramString, paramScriptable);
/*     */   }
/*     */ 
/*     */   public boolean has(int paramInt, Scriptable paramScriptable)
/*     */   {
/* 143 */     return this.obj.has(paramInt, paramScriptable);
/*     */   }
/*     */ 
/*     */   public void put(String paramString, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 149 */     this.obj.put(paramString, paramScriptable, paramObject);
/*     */   }
/*     */ 
/*     */   public void put(int paramInt, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 155 */     this.obj.put(paramInt, paramScriptable, paramObject);
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */   {
/* 161 */     this.obj.delete(paramString);
/*     */   }
/*     */ 
/*     */   public void delete(int paramInt)
/*     */   {
/* 167 */     this.obj.delete(paramInt);
/*     */   }
/*     */ 
/*     */   public Scriptable getPrototype()
/*     */   {
/* 173 */     return this.obj.getPrototype();
/*     */   }
/*     */ 
/*     */   public void setPrototype(Scriptable paramScriptable)
/*     */   {
/* 179 */     this.obj.setPrototype(paramScriptable);
/*     */   }
/*     */ 
/*     */   public Scriptable getParentScope()
/*     */   {
/* 185 */     return this.obj.getParentScope();
/*     */   }
/*     */ 
/*     */   public void setParentScope(Scriptable paramScriptable)
/*     */   {
/* 191 */     this.obj.setParentScope(paramScriptable);
/*     */   }
/*     */ 
/*     */   public Object[] getIds()
/*     */   {
/* 197 */     return this.obj.getIds();
/*     */   }
/*     */ 
/*     */   public Object getDefaultValue(Class<?> paramClass)
/*     */   {
/* 212 */     return (paramClass == null) || (paramClass == ScriptRuntime.ScriptableClass) || (paramClass == ScriptRuntime.FunctionClass) ? this : this.obj.getDefaultValue(paramClass);
/*     */   }
/*     */ 
/*     */   public boolean hasInstance(Scriptable paramScriptable)
/*     */   {
/* 221 */     return this.obj.hasInstance(paramScriptable);
/*     */   }
/*     */ 
/*     */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 229 */     return ((Function)this.obj).call(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*     */   {
/* 249 */     if (this.obj == null)
/*     */     {
/* 252 */       Delegator localDelegator = newInstance();
/*     */       Object localObject;
/* 254 */       if (paramArrayOfObject.length == 0)
/* 255 */         localObject = new NativeObject();
/*     */       else {
/* 257 */         localObject = ScriptRuntime.toObject(paramContext, paramScriptable, paramArrayOfObject[0]);
/*     */       }
/* 259 */       localDelegator.setDelegee((Scriptable)localObject);
/* 260 */       return localDelegator;
/*     */     }
/*     */ 
/* 263 */     return ((Function)this.obj).construct(paramContext, paramScriptable, paramArrayOfObject);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.Delegator
 * JD-Core Version:    0.6.2
 */