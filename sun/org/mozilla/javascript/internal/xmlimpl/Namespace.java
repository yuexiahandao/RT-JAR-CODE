/*     */ package sun.org.mozilla.javascript.internal.xmlimpl;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.IdFunctionObject;
/*     */ import sun.org.mozilla.javascript.internal.IdScriptableObject;
/*     */ import sun.org.mozilla.javascript.internal.ScriptRuntime;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.Undefined;
/*     */ 
/*     */ class Namespace extends IdScriptableObject
/*     */ {
/*     */   static final long serialVersionUID = -5765755238131301744L;
/*  53 */   private static final Object NAMESPACE_TAG = "Namespace";
/*     */   private Namespace prototype;
/*     */   private XmlNode.Namespace ns;
/*     */   private static final int Id_prefix = 1;
/*     */   private static final int Id_uri = 2;
/*     */   private static final int MAX_INSTANCE_ID = 2;
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_toString = 2;
/*     */   private static final int Id_toSource = 3;
/*     */   private static final int MAX_PROTOTYPE_ID = 3;
/*     */ 
/*     */   static Namespace create(Scriptable paramScriptable, Namespace paramNamespace, XmlNode.Namespace paramNamespace1)
/*     */   {
/*  62 */     Namespace localNamespace = new Namespace();
/*  63 */     localNamespace.setParentScope(paramScriptable);
/*  64 */     localNamespace.prototype = paramNamespace;
/*  65 */     localNamespace.setPrototype(paramNamespace);
/*  66 */     localNamespace.ns = paramNamespace1;
/*  67 */     return localNamespace;
/*     */   }
/*     */ 
/*     */   final XmlNode.Namespace getDelegate() {
/*  71 */     return this.ns;
/*     */   }
/*     */ 
/*     */   public void exportAsJSClass(boolean paramBoolean) {
/*  75 */     exportAsJSClass(3, getParentScope(), paramBoolean);
/*     */   }
/*     */ 
/*     */   public String uri() {
/*  79 */     return this.ns.getUri();
/*     */   }
/*     */ 
/*     */   public String prefix() {
/*  83 */     return this.ns.getPrefix();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  88 */     return uri();
/*     */   }
/*     */ 
/*     */   public String toLocaleString() {
/*  92 */     return toString();
/*     */   }
/*     */ 
/*     */   private boolean equals(Namespace paramNamespace) {
/*  96 */     return uri().equals(paramNamespace.uri());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 101 */     if (!(paramObject instanceof Namespace)) return false;
/* 102 */     return equals((Namespace)paramObject);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 107 */     return uri().hashCode();
/*     */   }
/*     */ 
/*     */   protected Object equivalentValues(Object paramObject)
/*     */   {
/* 112 */     if (!(paramObject instanceof Namespace)) return Scriptable.NOT_FOUND;
/* 113 */     boolean bool = equals((Namespace)paramObject);
/* 114 */     return bool ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 119 */     return "Namespace";
/*     */   }
/*     */ 
/*     */   public Object getDefaultValue(Class<?> paramClass)
/*     */   {
/* 124 */     return uri();
/*     */   }
/*     */ 
/*     */   protected int getMaxInstanceId()
/*     */   {
/* 136 */     return super.getMaxInstanceId() + 2;
/*     */   }
/*     */ 
/*     */   protected int findInstanceIdInfo(String paramString)
/*     */   {
/* 144 */     int i = 0; String str = null;
/* 145 */     int k = paramString.length();
/* 146 */     if (k == 3) { str = "uri"; i = 2;
/* 147 */     } else if (k == 6) { str = "prefix"; i = 1; }
/* 148 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 153 */     if (i == 0) return super.findInstanceIdInfo(paramString);
/*     */     int j;
/* 156 */     switch (i) {
/*     */     case 1:
/*     */     case 2:
/* 159 */       j = 5;
/* 160 */       break;
/*     */     default:
/* 161 */       throw new IllegalStateException();
/*     */     }
/* 163 */     return instanceIdInfo(j, super.getMaxInstanceId() + i);
/*     */   }
/*     */ 
/*     */   protected String getInstanceIdName(int paramInt)
/*     */   {
/* 170 */     switch (paramInt - super.getMaxInstanceId()) { case 1:
/* 171 */       return "prefix";
/*     */     case 2:
/* 172 */       return "uri";
/*     */     }
/* 174 */     return super.getInstanceIdName(paramInt);
/*     */   }
/*     */ 
/*     */   protected Object getInstanceIdValue(int paramInt)
/*     */   {
/* 180 */     switch (paramInt - super.getMaxInstanceId()) {
/*     */     case 1:
/* 182 */       if (this.ns.getPrefix() == null) return Undefined.instance;
/* 183 */       return this.ns.getPrefix();
/*     */     case 2:
/* 185 */       return this.ns.getUri();
/*     */     }
/* 187 */     return super.getInstanceIdValue(paramInt);
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 203 */     int i = 0; String str = null;
/* 204 */     int k = paramString.length();
/* 205 */     if (k == 8) {
/* 206 */       int j = paramString.charAt(3);
/* 207 */       if (j == 111) { str = "toSource"; i = 3;
/* 208 */       } else if (j == 116) { str = "toString"; i = 2; }
/*     */     }
/* 210 */     else if (k == 11) { str = "constructor"; i = 1; }
/* 211 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 215 */     return i;
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*     */     int i;
/*     */     String str;
/* 224 */     switch (paramInt) { case 1:
/* 225 */       i = 2; str = "constructor"; break;
/*     */     case 2:
/* 226 */       i = 0; str = "toString"; break;
/*     */     case 3:
/* 227 */       i = 0; str = "toSource"; break;
/*     */     default:
/* 228 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */     }
/* 230 */     initPrototypeMethod(NAMESPACE_TAG, paramInt, str, i);
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 240 */     if (!paramIdFunctionObject.hasTag(NAMESPACE_TAG)) {
/* 241 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 243 */     int i = paramIdFunctionObject.methodId();
/* 244 */     switch (i) {
/*     */     case 1:
/* 246 */       return jsConstructor(paramContext, paramScriptable2 == null, paramArrayOfObject);
/*     */     case 2:
/* 248 */       return realThis(paramScriptable2, paramIdFunctionObject).toString();
/*     */     case 3:
/* 250 */       return realThis(paramScriptable2, paramIdFunctionObject).js_toSource();
/*     */     }
/* 252 */     throw new IllegalArgumentException(String.valueOf(i));
/*     */   }
/*     */ 
/*     */   private Namespace realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
/* 256 */     if (!(paramScriptable instanceof Namespace))
/* 257 */       throw incompatibleCallError(paramIdFunctionObject);
/* 258 */     return (Namespace)paramScriptable;
/*     */   }
/*     */ 
/*     */   Namespace newNamespace(String paramString) {
/* 262 */     Namespace localNamespace = this.prototype == null ? this : this.prototype;
/* 263 */     return create(getParentScope(), localNamespace, XmlNode.Namespace.create(paramString));
/*     */   }
/*     */ 
/*     */   Namespace newNamespace(String paramString1, String paramString2) {
/* 267 */     if (paramString1 == null) return newNamespace(paramString2);
/* 268 */     Namespace localNamespace = this.prototype == null ? this : this.prototype;
/* 269 */     return create(getParentScope(), localNamespace, XmlNode.Namespace.create(paramString1, paramString2));
/*     */   }
/*     */ 
/*     */   Namespace constructNamespace(Object paramObject)
/*     */   {
/*     */     Object localObject;
/*     */     String str1;
/*     */     String str2;
/* 276 */     if ((paramObject instanceof Namespace)) {
/* 277 */       localObject = (Namespace)paramObject;
/* 278 */       str1 = ((Namespace)localObject).prefix();
/* 279 */       str2 = ((Namespace)localObject).uri();
/* 280 */     } else if ((paramObject instanceof QName)) {
/* 281 */       localObject = (QName)paramObject;
/* 282 */       str2 = ((QName)localObject).uri();
/* 283 */       if (str2 != null)
/*     */       {
/* 285 */         str1 = ((QName)localObject).prefix();
/*     */       } else {
/* 287 */         str2 = ((QName)localObject).toString();
/* 288 */         str1 = null;
/*     */       }
/*     */     } else {
/* 291 */       str2 = ScriptRuntime.toString(paramObject);
/* 292 */       str1 = str2.length() == 0 ? "" : null;
/*     */     }
/*     */ 
/* 295 */     return newNamespace(str1, str2);
/*     */   }
/*     */ 
/*     */   Namespace castToNamespace(Object paramObject) {
/* 299 */     if ((paramObject instanceof Namespace)) {
/* 300 */       return (Namespace)paramObject;
/*     */     }
/* 302 */     return constructNamespace(paramObject);
/*     */   }
/*     */ 
/*     */   private Namespace constructNamespace(Object paramObject1, Object paramObject2)
/*     */   {
/*     */     String str2;
/* 309 */     if ((paramObject2 instanceof QName)) {
/* 310 */       QName localQName = (QName)paramObject2;
/* 311 */       str2 = localQName.uri();
/* 312 */       if (str2 == null)
/* 313 */         str2 = localQName.toString();
/*     */     }
/*     */     else {
/* 316 */       str2 = ScriptRuntime.toString(paramObject2);
/*     */     }
/*     */     String str1;
/* 319 */     if (str2.length() == 0) {
/* 320 */       if (paramObject1 == Undefined.instance) {
/* 321 */         str1 = "";
/*     */       } else {
/* 323 */         str1 = ScriptRuntime.toString(paramObject1);
/* 324 */         if (str1.length() != 0) {
/* 325 */           throw ScriptRuntime.typeError("Illegal prefix '" + str1 + "' for 'no namespace'.");
/*     */         }
/*     */       }
/*     */     }
/* 329 */     else if (paramObject1 == Undefined.instance)
/* 330 */       str1 = "";
/* 331 */     else if (!XMLName.accept(paramObject1))
/* 332 */       str1 = "";
/*     */     else {
/* 334 */       str1 = ScriptRuntime.toString(paramObject1);
/*     */     }
/*     */ 
/* 337 */     return newNamespace(str1, str2);
/*     */   }
/*     */ 
/*     */   private Namespace constructNamespace() {
/* 341 */     return newNamespace("", "");
/*     */   }
/*     */ 
/*     */   private Object jsConstructor(Context paramContext, boolean paramBoolean, Object[] paramArrayOfObject)
/*     */   {
/* 346 */     if ((!paramBoolean) && (paramArrayOfObject.length == 1)) {
/* 347 */       return castToNamespace(paramArrayOfObject[0]);
/*     */     }
/*     */ 
/* 350 */     if (paramArrayOfObject.length == 0)
/* 351 */       return constructNamespace();
/* 352 */     if (paramArrayOfObject.length == 1) {
/* 353 */       return constructNamespace(paramArrayOfObject[0]);
/*     */     }
/* 355 */     return constructNamespace(paramArrayOfObject[0], paramArrayOfObject[1]);
/*     */   }
/*     */ 
/*     */   private String js_toSource()
/*     */   {
/* 361 */     StringBuffer localStringBuffer = new StringBuffer();
/* 362 */     localStringBuffer.append('(');
/* 363 */     toSourceImpl(this.ns.getPrefix(), this.ns.getUri(), localStringBuffer);
/* 364 */     localStringBuffer.append(')');
/* 365 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static void toSourceImpl(String paramString1, String paramString2, StringBuffer paramStringBuffer)
/*     */   {
/* 370 */     paramStringBuffer.append("new Namespace(");
/* 371 */     if (paramString2.length() == 0) {
/* 372 */       if (!"".equals(paramString1)) throw new IllegalArgumentException(paramString1); 
/*     */     }
/* 374 */     else { paramStringBuffer.append('\'');
/* 375 */       if (paramString1 != null) {
/* 376 */         paramStringBuffer.append(ScriptRuntime.escapeString(paramString1, '\''));
/* 377 */         paramStringBuffer.append("', '");
/*     */       }
/* 379 */       paramStringBuffer.append(ScriptRuntime.escapeString(paramString2, '\''));
/* 380 */       paramStringBuffer.append('\'');
/*     */     }
/* 382 */     paramStringBuffer.append(')');
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.xmlimpl.Namespace
 * JD-Core Version:    0.6.2
 */