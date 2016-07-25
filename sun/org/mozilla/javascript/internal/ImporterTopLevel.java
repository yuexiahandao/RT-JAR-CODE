/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public class ImporterTopLevel extends IdScriptableObject
/*     */ {
/*  77 */   private static final Object IMPORTER_TAG = "Importer";
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_importClass = 2;
/*     */   private static final int Id_importPackage = 3;
/*     */   private static final int MAX_PROTOTYPE_ID = 3;
/* 320 */   private ObjArray importedPackages = new ObjArray();
/*     */   private boolean topScopeFlag;
/*     */ 
/*     */   public ImporterTopLevel()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ImporterTopLevel(Context paramContext)
/*     */   {
/*  82 */     this(paramContext, false);
/*     */   }
/*     */ 
/*     */   public ImporterTopLevel(Context paramContext, boolean paramBoolean)
/*     */   {
/*  87 */     initStandardObjects(paramContext, paramBoolean);
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  93 */     return this.topScopeFlag ? "global" : "JavaImporter";
/*     */   }
/*     */ 
/*     */   public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/*  98 */     ImporterTopLevel localImporterTopLevel = new ImporterTopLevel();
/*  99 */     localImporterTopLevel.exportAsJSClass(3, paramScriptable, paramBoolean);
/*     */   }
/*     */ 
/*     */   public void initStandardObjects(Context paramContext, boolean paramBoolean)
/*     */   {
/* 106 */     paramContext.initStandardObjects(this, paramBoolean);
/* 107 */     this.topScopeFlag = true;
/*     */ 
/* 111 */     IdFunctionObject localIdFunctionObject = exportAsJSClass(3, this, false);
/* 112 */     if (paramBoolean) {
/* 113 */       localIdFunctionObject.sealObject();
/*     */     }
/*     */ 
/* 118 */     delete("constructor");
/*     */   }
/*     */ 
/*     */   public boolean has(String paramString, Scriptable paramScriptable)
/*     */   {
/* 123 */     return (super.has(paramString, paramScriptable)) || (getPackageProperty(paramString, paramScriptable) != NOT_FOUND);
/*     */   }
/*     */ 
/*     */   public Object get(String paramString, Scriptable paramScriptable)
/*     */   {
/* 129 */     Object localObject = super.get(paramString, paramScriptable);
/* 130 */     if (localObject != NOT_FOUND)
/* 131 */       return localObject;
/* 132 */     localObject = getPackageProperty(paramString, paramScriptable);
/* 133 */     return localObject;
/*     */   }
/*     */ 
/*     */   private Object getPackageProperty(String paramString, Scriptable paramScriptable) {
/* 137 */     Object localObject1 = NOT_FOUND;
/*     */     Object[] arrayOfObject;
/* 139 */     synchronized (this.importedPackages) {
/* 140 */       arrayOfObject = this.importedPackages.toArray();
/*     */     }
/* 142 */     for (int i = 0; i < arrayOfObject.length; i++) {
/* 143 */       NativeJavaPackage localNativeJavaPackage = (NativeJavaPackage)arrayOfObject[i];
/* 144 */       Object localObject3 = localNativeJavaPackage.getPkgProperty(paramString, paramScriptable, false);
/* 145 */       if ((localObject3 != null) && (!(localObject3 instanceof NativeJavaPackage))) {
/* 146 */         if (localObject1 == NOT_FOUND)
/* 147 */           localObject1 = localObject3;
/*     */         else {
/* 149 */           throw Context.reportRuntimeError2("msg.ambig.import", localObject1.toString(), localObject3.toString());
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 154 */     return localObject1;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void importPackage(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction)
/*     */   {
/* 163 */     js_importPackage(paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   private Object js_construct(Scriptable paramScriptable, Object[] paramArrayOfObject)
/*     */   {
/* 168 */     ImporterTopLevel localImporterTopLevel = new ImporterTopLevel();
/* 169 */     for (int i = 0; i != paramArrayOfObject.length; i++) {
/* 170 */       Object localObject = paramArrayOfObject[i];
/* 171 */       if ((localObject instanceof NativeJavaClass))
/* 172 */         localImporterTopLevel.importClass((NativeJavaClass)localObject);
/* 173 */       else if ((localObject instanceof NativeJavaPackage))
/* 174 */         localImporterTopLevel.importPackage((NativeJavaPackage)localObject);
/*     */       else {
/* 176 */         throw Context.reportRuntimeError1("msg.not.class.not.pkg", Context.toString(localObject));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 185 */     localImporterTopLevel.setParentScope(paramScriptable);
/* 186 */     localImporterTopLevel.setPrototype(this);
/* 187 */     return localImporterTopLevel;
/*     */   }
/*     */ 
/*     */   private Object js_importClass(Object[] paramArrayOfObject)
/*     */   {
/* 192 */     for (int i = 0; i != paramArrayOfObject.length; i++) {
/* 193 */       Object localObject = paramArrayOfObject[i];
/* 194 */       if (!(localObject instanceof NativeJavaClass)) {
/* 195 */         throw Context.reportRuntimeError1("msg.not.class", Context.toString(localObject));
/*     */       }
/*     */ 
/* 198 */       importClass((NativeJavaClass)localObject);
/*     */     }
/* 200 */     return Undefined.instance;
/*     */   }
/*     */ 
/*     */   private Object js_importPackage(Object[] paramArrayOfObject)
/*     */   {
/* 205 */     for (int i = 0; i != paramArrayOfObject.length; i++) {
/* 206 */       Object localObject = paramArrayOfObject[i];
/* 207 */       if (!(localObject instanceof NativeJavaPackage)) {
/* 208 */         throw Context.reportRuntimeError1("msg.not.pkg", Context.toString(localObject));
/*     */       }
/*     */ 
/* 211 */       importPackage((NativeJavaPackage)localObject);
/*     */     }
/* 213 */     return Undefined.instance;
/*     */   }
/*     */ 
/*     */   private void importPackage(NativeJavaPackage paramNativeJavaPackage)
/*     */   {
/* 218 */     if (paramNativeJavaPackage == null) {
/* 219 */       return;
/*     */     }
/* 221 */     synchronized (this.importedPackages) {
/* 222 */       for (int i = 0; i != this.importedPackages.size(); i++) {
/* 223 */         if (paramNativeJavaPackage.equals(this.importedPackages.get(i))) {
/* 224 */           return;
/*     */         }
/*     */       }
/* 227 */       this.importedPackages.add(paramNativeJavaPackage);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void importClass(NativeJavaClass paramNativeJavaClass)
/*     */   {
/* 233 */     String str1 = paramNativeJavaClass.getClassObject().getName();
/* 234 */     String str2 = str1.substring(str1.lastIndexOf('.') + 1);
/* 235 */     Object localObject = get(str2, this);
/* 236 */     if ((localObject != NOT_FOUND) && (localObject != paramNativeJavaClass)) {
/* 237 */       throw Context.reportRuntimeError1("msg.prop.defined", str2);
/*     */     }
/*     */ 
/* 240 */     put(str2, this, paramNativeJavaClass);
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*     */     int i;
/*     */     String str;
/* 248 */     switch (paramInt) { case 1:
/* 249 */       i = 0; str = "constructor"; break;
/*     */     case 2:
/* 250 */       i = 1; str = "importClass"; break;
/*     */     case 3:
/* 251 */       i = 1; str = "importPackage"; break;
/*     */     default:
/* 252 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */     }
/* 254 */     initPrototypeMethod(IMPORTER_TAG, paramInt, str, i);
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 261 */     if (!paramIdFunctionObject.hasTag(IMPORTER_TAG)) {
/* 262 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 264 */     int i = paramIdFunctionObject.methodId();
/* 265 */     switch (i) {
/*     */     case 1:
/* 267 */       return js_construct(paramScriptable1, paramArrayOfObject);
/*     */     case 2:
/* 270 */       return realThis(paramScriptable2, paramIdFunctionObject).js_importClass(paramArrayOfObject);
/*     */     case 3:
/* 273 */       return realThis(paramScriptable2, paramIdFunctionObject).js_importPackage(paramArrayOfObject);
/*     */     }
/* 275 */     throw new IllegalArgumentException(String.valueOf(i));
/*     */   }
/*     */ 
/*     */   private ImporterTopLevel realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject)
/*     */   {
/* 280 */     if (this.topScopeFlag)
/*     */     {
/* 283 */       return this;
/*     */     }
/* 285 */     if (!(paramScriptable instanceof ImporterTopLevel))
/* 286 */       throw incompatibleCallError(paramIdFunctionObject);
/* 287 */     return (ImporterTopLevel)paramScriptable;
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 297 */     int i = 0; String str = null;
/* 298 */     int k = paramString.length();
/* 299 */     if (k == 11) {
/* 300 */       int j = paramString.charAt(0);
/* 301 */       if (j == 99) { str = "constructor"; i = 1;
/* 302 */       } else if (j == 105) { str = "importClass"; i = 2; }
/*     */     }
/* 304 */     else if (k == 13) { str = "importPackage"; i = 3; }
/* 305 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 309 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ImporterTopLevel
 * JD-Core Version:    0.6.2
 */