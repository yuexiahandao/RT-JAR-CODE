/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public class NativeJavaTopPackage extends NativeJavaPackage
/*     */   implements Function, IdFunctionCall
/*     */ {
/*  61 */   private static final String[][] commonPackages = { { "java", "lang", "reflect" }, { "java", "io" }, { "java", "math" }, { "java", "net" }, { "java", "util", "zip" }, { "java", "text", "resources" }, { "java", "applet" }, { "javax", "swing" } };
/*     */ 
/* 183 */   private static final Object FTAG = "JavaTopPackage";
/*     */   private static final int Id_getClass = 1;
/*     */ 
/*     */   NativeJavaTopPackage(ClassLoader paramClassLoader)
/*     */   {
/*  74 */     super(true, "", paramClassLoader);
/*     */   }
/*     */ 
/*     */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/*  80 */     return construct(paramContext, paramScriptable1, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*     */   {
/*  85 */     ClassLoader localClassLoader = null;
/*  86 */     if (paramArrayOfObject.length != 0) {
/*  87 */       localObject = paramArrayOfObject[0];
/*  88 */       if ((localObject instanceof Wrapper)) {
/*  89 */         localObject = ((Wrapper)localObject).unwrap();
/*     */       }
/*  91 */       if ((localObject instanceof ClassLoader)) {
/*  92 */         localClassLoader = (ClassLoader)localObject;
/*     */       }
/*     */     }
/*  95 */     if (localClassLoader == null) {
/*  96 */       Context.reportRuntimeError0("msg.not.classloader");
/*  97 */       return null;
/*     */     }
/*  99 */     Object localObject = new NativeJavaPackage(true, "", localClassLoader);
/* 100 */     ScriptRuntime.setObjectProtoAndParent((ScriptableObject)localObject, paramScriptable);
/* 101 */     return localObject;
/*     */   }
/*     */ 
/*     */   public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/* 106 */     ClassLoader localClassLoader = paramContext.getApplicationClassLoader();
/* 107 */     NativeJavaTopPackage localNativeJavaTopPackage = new NativeJavaTopPackage(localClassLoader);
/* 108 */     localNativeJavaTopPackage.setPrototype(getObjectPrototype(paramScriptable));
/* 109 */     localNativeJavaTopPackage.setParentScope(paramScriptable);
/*     */ 
/* 111 */     for (int i = 0; i != commonPackages.length; i++) {
/* 112 */       localObject = localNativeJavaTopPackage;
/* 113 */       for (int j = 0; j != commonPackages[i].length; j++) {
/* 114 */         localObject = ((NativeJavaPackage)localObject).forcePackage(commonPackages[i][j], paramScriptable);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 119 */     IdFunctionObject localIdFunctionObject = new IdFunctionObject(localNativeJavaTopPackage, FTAG, 1, "getClass", 1, paramScriptable);
/*     */ 
/* 125 */     Object localObject = { "java", "javax", "org", "com", "edu", "net" };
/* 126 */     NativeJavaPackage[] arrayOfNativeJavaPackage = new NativeJavaPackage[localObject.length];
/* 127 */     for (int k = 0; k < localObject.length; k++) {
/* 128 */       arrayOfNativeJavaPackage[k] = ((NativeJavaPackage)localNativeJavaTopPackage.get(localObject[k], localNativeJavaTopPackage));
/*     */     }
/*     */ 
/* 133 */     ScriptableObject localScriptableObject = (ScriptableObject)paramScriptable;
/*     */ 
/* 135 */     if (paramBoolean) {
/* 136 */       localIdFunctionObject.sealObject();
/*     */     }
/* 138 */     localIdFunctionObject.exportAsScopeProperty();
/* 139 */     localScriptableObject.defineProperty("Packages", localNativeJavaTopPackage, 2);
/* 140 */     for (int m = 0; m < localObject.length; m++)
/* 141 */       localScriptableObject.defineProperty(localObject[m], arrayOfNativeJavaPackage[m], 2);
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 149 */     if ((paramIdFunctionObject.hasTag(FTAG)) && 
/* 150 */       (paramIdFunctionObject.methodId() == 1)) {
/* 151 */       return js_getClass(paramContext, paramScriptable1, paramArrayOfObject);
/*     */     }
/*     */ 
/* 154 */     throw paramIdFunctionObject.unknown();
/*     */   }
/*     */ 
/*     */   private Scriptable js_getClass(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*     */   {
/* 159 */     if ((paramArrayOfObject.length > 0) && ((paramArrayOfObject[0] instanceof Wrapper))) {
/* 160 */       Object localObject1 = this;
/* 161 */       Class localClass = ((Wrapper)paramArrayOfObject[0]).unwrap().getClass();
/*     */ 
/* 164 */       String str1 = localClass.getName();
/* 165 */       int i = 0;
/*     */       while (true) {
/* 167 */         int j = str1.indexOf('.', i);
/* 168 */         String str2 = j == -1 ? str1.substring(i) : str1.substring(i, j);
/*     */ 
/* 171 */         Object localObject2 = ((Scriptable)localObject1).get(str2, (Scriptable)localObject1);
/* 172 */         if (!(localObject2 instanceof Scriptable))
/*     */           break;
/* 174 */         localObject1 = (Scriptable)localObject2;
/* 175 */         if (j == -1)
/* 176 */           return localObject1;
/* 177 */         i = j + 1;
/*     */       }
/*     */     }
/* 180 */     throw Context.reportRuntimeError0("msg.not.java.obj");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeJavaTopPackage
 * JD-Core Version:    0.6.2
 */