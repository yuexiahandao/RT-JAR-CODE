/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class NativeJavaPackage extends ScriptableObject
/*     */ {
/*     */   private String packageName;
/*     */   private ClassLoader classLoader;
/* 215 */   private Set<String> negativeCache = null;
/*     */ 
/*     */   NativeJavaPackage(boolean paramBoolean, String paramString, ClassLoader paramClassLoader)
/*     */   {
/*  64 */     this.packageName = paramString;
/*  65 */     this.classLoader = paramClassLoader;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public NativeJavaPackage(String paramString, ClassLoader paramClassLoader)
/*     */   {
/*  73 */     this(false, paramString, paramClassLoader);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public NativeJavaPackage(String paramString)
/*     */   {
/*  81 */     this(false, paramString, Context.getCurrentContext().getApplicationClassLoader());
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  87 */     return "JavaPackage";
/*     */   }
/*     */ 
/*     */   public boolean has(String paramString, Scriptable paramScriptable)
/*     */   {
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean has(int paramInt, Scriptable paramScriptable)
/*     */   {
/*  97 */     return false;
/*     */   }
/*     */ 
/*     */   public void put(String paramString, Scriptable paramScriptable, Object paramObject)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void put(int paramInt, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 107 */     throw Context.reportRuntimeError0("msg.pkg.int");
/*     */   }
/*     */ 
/*     */   public Object get(String paramString, Scriptable paramScriptable)
/*     */   {
/* 112 */     return getPkgProperty(paramString, paramScriptable, true);
/*     */   }
/*     */ 
/*     */   public Object get(int paramInt, Scriptable paramScriptable)
/*     */   {
/* 117 */     return NOT_FOUND;
/*     */   }
/*     */ 
/*     */   NativeJavaPackage forcePackage(String paramString, Scriptable paramScriptable)
/*     */   {
/* 124 */     Object localObject = super.get(paramString, this);
/* 125 */     if ((localObject != null) && ((localObject instanceof NativeJavaPackage))) {
/* 126 */       return (NativeJavaPackage)localObject;
/*     */     }
/* 128 */     String str = this.packageName + "." + paramString;
/*     */ 
/* 131 */     NativeJavaPackage localNativeJavaPackage = new NativeJavaPackage(true, str, this.classLoader);
/* 132 */     ScriptRuntime.setObjectProtoAndParent(localNativeJavaPackage, paramScriptable);
/* 133 */     super.put(paramString, this, localNativeJavaPackage);
/* 134 */     return localNativeJavaPackage;
/*     */   }
/*     */ 
/*     */   synchronized Object getPkgProperty(String paramString, Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/* 141 */     Object localObject1 = super.get(paramString, paramScriptable);
/* 142 */     if (localObject1 != NOT_FOUND)
/* 143 */       return localObject1;
/* 144 */     if ((this.negativeCache != null) && (this.negativeCache.contains(paramString)))
/*     */     {
/* 146 */       return null;
/*     */     }
/*     */ 
/* 149 */     String str = this.packageName + '.' + paramString;
/*     */ 
/* 151 */     Context localContext = Context.getContext();
/* 152 */     ClassShutter localClassShutter = localContext.getClassShutter();
/* 153 */     Object localObject2 = null;
/*     */     Object localObject3;
/* 154 */     if ((localClassShutter == null) || (localClassShutter.visibleToScripts(str))) {
/* 155 */       localObject3 = null;
/* 156 */       if (this.classLoader != null)
/* 157 */         localObject3 = Kit.classOrNull(this.classLoader, str);
/*     */       else {
/* 159 */         localObject3 = Kit.classOrNull(str);
/*     */       }
/* 161 */       if (localObject3 != null) {
/* 162 */         localObject2 = new NativeJavaClass(getTopLevelScope(this), (Class)localObject3);
/* 163 */         ((Scriptable)localObject2).setPrototype(getPrototype());
/*     */       }
/*     */     }
/* 166 */     if (localObject2 == null) {
/* 167 */       if (paramBoolean)
/*     */       {
/* 169 */         localObject3 = new NativeJavaPackage(true, str, this.classLoader);
/* 170 */         ScriptRuntime.setObjectProtoAndParent((ScriptableObject)localObject3, getParentScope());
/* 171 */         localObject2 = localObject3;
/*     */       }
/*     */       else {
/* 174 */         if (this.negativeCache == null)
/* 175 */           this.negativeCache = new HashSet();
/* 176 */         this.negativeCache.add(paramString);
/*     */       }
/*     */     }
/* 179 */     if (localObject2 != null)
/*     */     {
/* 182 */       super.put(paramString, paramScriptable, localObject2);
/*     */     }
/* 184 */     return localObject2;
/*     */   }
/*     */ 
/*     */   public Object getDefaultValue(Class<?> paramClass)
/*     */   {
/* 189 */     return toString();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 194 */     return "[JavaPackage " + this.packageName + "]";
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 199 */     if ((paramObject instanceof NativeJavaPackage)) {
/* 200 */       NativeJavaPackage localNativeJavaPackage = (NativeJavaPackage)paramObject;
/* 201 */       return (this.packageName.equals(localNativeJavaPackage.packageName)) && (this.classLoader == localNativeJavaPackage.classLoader);
/*     */     }
/*     */ 
/* 204 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 209 */     return this.packageName.hashCode() ^ (this.classLoader == null ? 0 : this.classLoader.hashCode());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeJavaPackage
 * JD-Core Version:    0.6.2
 */