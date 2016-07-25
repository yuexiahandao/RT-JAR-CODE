/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ final class NativeError extends IdScriptableObject
/*     */ {
/*  51 */   private static final Object ERROR_TAG = "Error";
/*     */   private RhinoException stackProvider;
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_toString = 2;
/*     */   private static final int Id_toSource = 3;
/*     */   private static final int MAX_PROTOTYPE_ID = 3;
/*     */ 
/*     */   static void init(Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/*  57 */     NativeError localNativeError = new NativeError();
/*  58 */     ScriptableObject.putProperty(localNativeError, "name", "Error");
/*  59 */     ScriptableObject.putProperty(localNativeError, "message", "");
/*  60 */     ScriptableObject.putProperty(localNativeError, "fileName", "");
/*  61 */     ScriptableObject.putProperty(localNativeError, "lineNumber", Integer.valueOf(0));
/*  62 */     localNativeError.exportAsJSClass(3, paramScriptable, paramBoolean);
/*     */   }
/*     */ 
/*     */   static NativeError make(Context paramContext, Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject, Object[] paramArrayOfObject)
/*     */   {
/*  68 */     Scriptable localScriptable = (Scriptable)paramIdFunctionObject.get("prototype", paramIdFunctionObject);
/*     */ 
/*  70 */     NativeError localNativeError = new NativeError();
/*  71 */     localNativeError.setPrototype(localScriptable);
/*  72 */     localNativeError.setParentScope(paramScriptable);
/*     */ 
/*  74 */     int i = paramArrayOfObject.length;
/*  75 */     if (i >= 1) {
/*  76 */       ScriptableObject.putProperty(localNativeError, "message", ScriptRuntime.toString(paramArrayOfObject[0]));
/*     */ 
/*  78 */       if (i >= 2) {
/*  79 */         ScriptableObject.putProperty(localNativeError, "fileName", paramArrayOfObject[1]);
/*  80 */         if (i >= 3) {
/*  81 */           int j = ScriptRuntime.toInt32(paramArrayOfObject[2]);
/*  82 */           ScriptableObject.putProperty(localNativeError, "lineNumber", Integer.valueOf(j));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  87 */     return localNativeError;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  93 */     return "Error";
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 100 */     Object localObject = js_toString(this);
/* 101 */     return (localObject instanceof String) ? (String)localObject : super.toString();
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*     */     int i;
/*     */     String str;
/* 109 */     switch (paramInt) { case 1:
/* 110 */       i = 1; str = "constructor"; break;
/*     */     case 2:
/* 111 */       i = 0; str = "toString"; break;
/*     */     case 3:
/* 112 */       i = 0; str = "toSource"; break;
/*     */     default:
/* 113 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */     }
/* 115 */     initPrototypeMethod(ERROR_TAG, paramInt, str, i);
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 122 */     if (!paramIdFunctionObject.hasTag(ERROR_TAG)) {
/* 123 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 125 */     int i = paramIdFunctionObject.methodId();
/* 126 */     switch (i) {
/*     */     case 1:
/* 128 */       return make(paramContext, paramScriptable1, paramIdFunctionObject, paramArrayOfObject);
/*     */     case 2:
/* 131 */       return js_toString(paramScriptable2);
/*     */     case 3:
/* 134 */       return js_toSource(paramContext, paramScriptable1, paramScriptable2);
/*     */     }
/* 136 */     throw new IllegalArgumentException(String.valueOf(i));
/*     */   }
/*     */ 
/*     */   public void setStackProvider(RhinoException paramRhinoException)
/*     */   {
/* 144 */     if (this.stackProvider == null) {
/* 145 */       this.stackProvider = paramRhinoException;
/*     */       try {
/* 147 */         defineProperty("stack", null, NativeError.class.getMethod("getStack", new Class[0]), NativeError.class.getMethod("setStack", new Class[] { Object.class }), 0);
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException)
/*     */       {
/* 152 */         throw new RuntimeException(localNoSuchMethodException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getStack() {
/* 158 */     String str = this.stackProvider == null ? NOT_FOUND : this.stackProvider.getScriptStackTrace();
/*     */ 
/* 162 */     setStack(str);
/* 163 */     return str;
/*     */   }
/*     */ 
/*     */   public void setStack(Object paramObject) {
/* 167 */     if (this.stackProvider != null) {
/* 168 */       this.stackProvider = null;
/* 169 */       delete("stack");
/*     */     }
/* 171 */     put("stack", this, paramObject);
/*     */   }
/*     */ 
/*     */   private static Object js_toString(Scriptable paramScriptable) {
/* 175 */     Object localObject1 = ScriptableObject.getProperty(paramScriptable, "name");
/* 176 */     if ((localObject1 == NOT_FOUND) || (localObject1 == Undefined.instance))
/* 177 */       localObject1 = "Error";
/*     */     else {
/* 179 */       localObject1 = ScriptRuntime.toString(localObject1);
/*     */     }
/* 181 */     Object localObject2 = ScriptableObject.getProperty(paramScriptable, "message");
/*     */     Object localObject3;
/* 183 */     if ((localObject2 == NOT_FOUND) || (localObject2 == Undefined.instance))
/* 184 */       localObject3 = Undefined.instance;
/*     */     else {
/* 186 */       localObject3 = (String)localObject1 + ": " + ScriptRuntime.toString(localObject2);
/*     */     }
/* 188 */     return localObject3;
/*     */   }
/*     */ 
/*     */   private static String js_toSource(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2)
/*     */   {
/* 195 */     Object localObject1 = ScriptableObject.getProperty(paramScriptable2, "name");
/* 196 */     Object localObject2 = ScriptableObject.getProperty(paramScriptable2, "message");
/* 197 */     Object localObject3 = ScriptableObject.getProperty(paramScriptable2, "fileName");
/* 198 */     Object localObject4 = ScriptableObject.getProperty(paramScriptable2, "lineNumber");
/*     */ 
/* 200 */     StringBuffer localStringBuffer = new StringBuffer();
/* 201 */     localStringBuffer.append("(new ");
/* 202 */     if (localObject1 == NOT_FOUND) {
/* 203 */       localObject1 = Undefined.instance;
/*     */     }
/* 205 */     localStringBuffer.append(ScriptRuntime.toString(localObject1));
/* 206 */     localStringBuffer.append("(");
/* 207 */     if ((localObject2 != NOT_FOUND) || (localObject3 != NOT_FOUND) || (localObject4 != NOT_FOUND))
/*     */     {
/* 211 */       if (localObject2 == NOT_FOUND) {
/* 212 */         localObject2 = "";
/*     */       }
/* 214 */       localStringBuffer.append(ScriptRuntime.uneval(paramContext, paramScriptable1, localObject2));
/* 215 */       if ((localObject3 != NOT_FOUND) || (localObject4 != NOT_FOUND)) {
/* 216 */         localStringBuffer.append(", ");
/* 217 */         if (localObject3 == NOT_FOUND) {
/* 218 */           localObject3 = "";
/*     */         }
/* 220 */         localStringBuffer.append(ScriptRuntime.uneval(paramContext, paramScriptable1, localObject3));
/* 221 */         if (localObject4 != NOT_FOUND) {
/* 222 */           int i = ScriptRuntime.toInt32(localObject4);
/* 223 */           if (i != 0) {
/* 224 */             localStringBuffer.append(", ");
/* 225 */             localStringBuffer.append(ScriptRuntime.toString(i));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 230 */     localStringBuffer.append("))");
/* 231 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static String getString(Scriptable paramScriptable, String paramString)
/*     */   {
/* 236 */     Object localObject = ScriptableObject.getProperty(paramScriptable, paramString);
/* 237 */     if (localObject == NOT_FOUND) return "";
/* 238 */     return ScriptRuntime.toString(localObject);
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 247 */     int i = 0; String str = null;
/* 248 */     int k = paramString.length();
/* 249 */     if (k == 8) {
/* 250 */       int j = paramString.charAt(3);
/* 251 */       if (j == 111) { str = "toSource"; i = 3;
/* 252 */       } else if (j == 116) { str = "toString"; i = 2; }
/*     */     }
/* 254 */     else if (k == 11) { str = "constructor"; i = 1; }
/* 255 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 259 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeError
 * JD-Core Version:    0.6.2
 */