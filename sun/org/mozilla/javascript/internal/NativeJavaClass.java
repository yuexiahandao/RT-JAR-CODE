/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class NativeJavaClass extends NativeJavaObject
/*     */   implements Function
/*     */ {
/*     */   static final String javaClassPropertyName = "__javaObject__";
/*     */   private Map<String, FieldAndMethods> staticFieldAndMethods;
/*     */ 
/*     */   public NativeJavaClass()
/*     */   {
/*     */   }
/*     */ 
/*     */   public NativeJavaClass(Scriptable paramScriptable, Class<?> paramClass)
/*     */   {
/*  74 */     this.parent = paramScriptable;
/*  75 */     this.javaObject = paramClass;
/*  76 */     initMembers();
/*     */   }
/*     */ 
/*     */   protected void initMembers()
/*     */   {
/*  81 */     Class localClass = (Class)this.javaObject;
/*  82 */     this.members = JavaMembers.lookupClass(this.parent, localClass, localClass, false);
/*  83 */     this.staticFieldAndMethods = this.members.getFieldAndMethodsObjects(this, localClass, true);
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  89 */     return "JavaClass";
/*     */   }
/*     */ 
/*     */   public boolean has(String paramString, Scriptable paramScriptable)
/*     */   {
/*  94 */     return (this.members.has(paramString, true)) || ("__javaObject__".equals(paramString));
/*     */   }
/*     */ 
/*     */   public Object get(String paramString, Scriptable paramScriptable)
/*     */   {
/* 103 */     if (paramString.equals("prototype")) {
/* 104 */       return null;
/*     */     }
/* 106 */     if (this.staticFieldAndMethods != null) {
/* 107 */       localObject1 = this.staticFieldAndMethods.get(paramString);
/* 108 */       if (localObject1 != null) {
/* 109 */         return localObject1;
/*     */       }
/*     */     }
/* 112 */     if (this.members.has(paramString, true))
/* 113 */       return this.members.get(this, paramString, this.javaObject, true);
/*     */     Object localObject2;
/* 116 */     if ("__javaObject__".equals(paramString)) {
/* 117 */       localObject1 = Context.getContext();
/* 118 */       localObject2 = ScriptableObject.getTopLevelScope(paramScriptable);
/* 119 */       return ((Context)localObject1).getWrapFactory().wrap((Context)localObject1, (Scriptable)localObject2, this.javaObject, ScriptRuntime.ClassClass);
/*     */     }
/*     */ 
/* 125 */     Object localObject1 = findNestedClass(getClassObject(), paramString);
/* 126 */     if (localObject1 != null) {
/* 127 */       localObject2 = new NativeJavaClass(ScriptableObject.getTopLevelScope(this), (Class)localObject1);
/*     */ 
/* 129 */       ((NativeJavaClass)localObject2).setParentScope(this);
/* 130 */       return localObject2;
/*     */     }
/*     */ 
/* 133 */     throw this.members.reportMemberNotFound(paramString);
/*     */   }
/*     */ 
/*     */   public void put(String paramString, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 138 */     this.members.put(this, paramString, this.javaObject, paramObject, true);
/*     */   }
/*     */ 
/*     */   public Object[] getIds()
/*     */   {
/* 143 */     return this.members.getIds(true);
/*     */   }
/*     */ 
/*     */   public Class<?> getClassObject() {
/* 147 */     return (Class)super.unwrap();
/*     */   }
/*     */ 
/*     */   public Object getDefaultValue(Class<?> paramClass)
/*     */   {
/* 152 */     if ((paramClass == null) || (paramClass == ScriptRuntime.StringClass))
/* 153 */       return toString();
/* 154 */     if (paramClass == ScriptRuntime.BooleanClass)
/* 155 */       return Boolean.TRUE;
/* 156 */     if (paramClass == ScriptRuntime.NumberClass)
/* 157 */       return ScriptRuntime.NaNobj;
/* 158 */     return this;
/*     */   }
/*     */ 
/*     */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 167 */     if ((paramArrayOfObject.length == 1) && ((paramArrayOfObject[0] instanceof Scriptable))) {
/* 168 */       Class localClass = getClassObject();
/* 169 */       Scriptable localScriptable = (Scriptable)paramArrayOfObject[0];
/*     */       do {
/* 171 */         if ((localScriptable instanceof Wrapper)) {
/* 172 */           Object localObject = ((Wrapper)localScriptable).unwrap();
/* 173 */           if (localClass.isInstance(localObject))
/* 174 */             return localScriptable;
/*     */         }
/* 176 */         localScriptable = localScriptable.getPrototype();
/* 177 */       }while (localScriptable != null);
/*     */     }
/* 179 */     return construct(paramContext, paramScriptable1, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*     */   {
/* 184 */     Class localClass = getClassObject();
/* 185 */     int i = localClass.getModifiers();
/*     */     Object localObject3;
/* 186 */     if ((!Modifier.isInterface(i)) && (!Modifier.isAbstract(i)))
/*     */     {
/* 189 */       localObject1 = this.members.ctors;
/* 190 */       int j = NativeJavaMethod.findFunction(paramContext, (MemberBox[])localObject1, paramArrayOfObject);
/* 191 */       if (j < 0) {
/* 192 */         localObject3 = NativeJavaMethod.scriptSignature(paramArrayOfObject);
/* 193 */         throw Context.reportRuntimeError2("msg.no.java.ctor", localClass.getName(), localObject3);
/*     */       }
/*     */ 
/* 198 */       return constructSpecific(paramContext, paramScriptable, paramArrayOfObject, localObject1[j]);
/*     */     }
/* 200 */     Object localObject1 = ScriptableObject.getTopLevelScope(this);
/* 201 */     Object localObject2 = "";
/*     */     try
/*     */     {
/* 206 */       localObject3 = ((Scriptable)localObject1).get("JavaAdapter", (Scriptable)localObject1);
/* 207 */       if (localObject3 != NOT_FOUND) {
/* 208 */         localObject4 = (Function)localObject3;
/*     */ 
/* 210 */         Object[] arrayOfObject = { this, paramArrayOfObject[0] };
/* 211 */         return ((Function)localObject4).construct(paramContext, (Scriptable)localObject1, arrayOfObject);
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {
/* 215 */       Object localObject4 = localException.getMessage();
/* 216 */       if (localObject4 != null)
/* 217 */         localObject2 = localObject4;
/*     */     }
/* 219 */     throw Context.reportRuntimeError2("msg.cant.instantiate", localObject2, localClass.getName());
/*     */   }
/*     */ 
/*     */   static Scriptable constructSpecific(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, MemberBox paramMemberBox)
/*     */   {
/* 227 */     Scriptable localScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
/* 228 */     Class[] arrayOfClass = paramMemberBox.argTypes;
/*     */     Object localObject4;
/* 230 */     if (paramMemberBox.vararg)
/*     */     {
/* 232 */       localObject1 = new Object[arrayOfClass.length];
/* 233 */       for (int i = 0; i < arrayOfClass.length - 1; i++)
/* 234 */         localObject1[i] = Context.jsToJava(paramArrayOfObject[i], arrayOfClass[i]);
/*     */       Object localObject2;
/* 241 */       if ((paramArrayOfObject.length == arrayOfClass.length) && ((paramArrayOfObject[(paramArrayOfObject.length - 1)] == null) || ((paramArrayOfObject[(paramArrayOfObject.length - 1)] instanceof NativeArray)) || ((paramArrayOfObject[(paramArrayOfObject.length - 1)] instanceof NativeJavaArray))))
/*     */       {
/* 247 */         localObject2 = Context.jsToJava(paramArrayOfObject[(paramArrayOfObject.length - 1)], arrayOfClass[(arrayOfClass.length - 1)]);
/*     */       }
/*     */       else
/*     */       {
/* 251 */         localObject4 = arrayOfClass[(arrayOfClass.length - 1)].getComponentType();
/*     */ 
/* 253 */         localObject2 = Array.newInstance((Class)localObject4, paramArrayOfObject.length - arrayOfClass.length + 1);
/*     */ 
/* 255 */         for (int m = 0; m < Array.getLength(localObject2); m++) {
/* 256 */           Object localObject6 = Context.jsToJava(paramArrayOfObject[(arrayOfClass.length - 1 + m)], (Class)localObject4);
/*     */ 
/* 258 */           Array.set(localObject2, m, localObject6);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 263 */       localObject1[(arrayOfClass.length - 1)] = localObject2;
/*     */ 
/* 265 */       paramArrayOfObject = (Object[])localObject1;
/*     */     } else {
/* 267 */       localObject1 = paramArrayOfObject;
/* 268 */       for (int j = 0; j < paramArrayOfObject.length; j++) {
/* 269 */         localObject4 = paramArrayOfObject[j];
/* 270 */         Object localObject5 = Context.jsToJava(localObject4, arrayOfClass[j]);
/* 271 */         if (localObject5 != localObject4) {
/* 272 */           if (paramArrayOfObject == localObject1) {
/* 273 */             paramArrayOfObject = (Object[])((Object[])localObject1).clone();
/*     */           }
/* 275 */           paramArrayOfObject[j] = localObject5;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 282 */     Object localObject1 = System.getSecurityManager();
/* 283 */     if (localObject1 != null) {
/* 284 */       localObject3 = paramMemberBox.getName();
/* 285 */       int k = ((String)localObject3).lastIndexOf('.');
/* 286 */       if (k != -1) {
/* 287 */         ((SecurityManager)localObject1).checkPackageAccess(((String)localObject3).substring(0, k));
/*     */       }
/*     */     }
/*     */ 
/* 291 */     Object localObject3 = paramMemberBox.newInstance(paramArrayOfObject);
/*     */ 
/* 294 */     return paramContext.getWrapFactory().wrapNewObject(paramContext, localScriptable, localObject3);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 299 */     return "[JavaClass " + getClassObject().getName() + "]";
/*     */   }
/*     */ 
/*     */   public boolean hasInstance(Scriptable paramScriptable)
/*     */   {
/* 313 */     if (((paramScriptable instanceof Wrapper)) && (!(paramScriptable instanceof NativeJavaClass)))
/*     */     {
/* 315 */       Object localObject = ((Wrapper)paramScriptable).unwrap();
/*     */ 
/* 317 */       return getClassObject().isInstance(localObject);
/*     */     }
/*     */ 
/* 321 */     return false;
/*     */   }
/*     */ 
/*     */   private static Class<?> findNestedClass(Class<?> paramClass, String paramString) {
/* 325 */     String str = paramClass.getName() + '$' + paramString;
/* 326 */     ClassLoader localClassLoader = paramClass.getClassLoader();
/* 327 */     if (localClassLoader == null)
/*     */     {
/* 332 */       return Kit.classOrNull(str);
/*     */     }
/* 334 */     return Kit.classOrNull(localClassLoader, str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeJavaClass
 * JD-Core Version:    0.6.2
 */