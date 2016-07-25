/*     */ package sun.org.mozilla.javascript.internal.xmlimpl;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.IdFunctionObject;
/*     */ import sun.org.mozilla.javascript.internal.IdScriptableObject;
/*     */ import sun.org.mozilla.javascript.internal.ScriptRuntime;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.Undefined;
/*     */ 
/*     */ final class QName extends IdScriptableObject
/*     */ {
/*     */   static final long serialVersionUID = 416745167693026750L;
/*  53 */   private static final Object QNAME_TAG = "QName";
/*     */   private XMLLibImpl lib;
/*     */   private QName prototype;
/*     */   private XmlNode.QName delegate;
/*     */   private static final int Id_localName = 1;
/*     */   private static final int Id_uri = 2;
/*     */   private static final int MAX_INSTANCE_ID = 2;
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_toString = 2;
/*     */   private static final int Id_toSource = 3;
/*     */   private static final int MAX_PROTOTYPE_ID = 3;
/*     */ 
/*     */   static QName create(XMLLibImpl paramXMLLibImpl, Scriptable paramScriptable, QName paramQName, XmlNode.QName paramQName1)
/*     */   {
/*  65 */     QName localQName = new QName();
/*  66 */     localQName.lib = paramXMLLibImpl;
/*  67 */     localQName.setParentScope(paramScriptable);
/*  68 */     localQName.prototype = paramQName;
/*  69 */     localQName.setPrototype(paramQName);
/*  70 */     localQName.delegate = paramQName1;
/*  71 */     return localQName;
/*     */   }
/*     */ 
/*     */   void exportAsJSClass(boolean paramBoolean)
/*     */   {
/*  80 */     exportAsJSClass(3, getParentScope(), paramBoolean);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  86 */     if (this.delegate.getNamespace() == null)
/*  87 */       return "*::" + localName();
/*  88 */     if (this.delegate.getNamespace().isGlobal())
/*     */     {
/*  90 */       return localName();
/*     */     }
/*  92 */     return uri() + "::" + localName();
/*     */   }
/*     */ 
/*     */   public String localName()
/*     */   {
/*  97 */     if (this.delegate.getLocalName() == null) return "*";
/*  98 */     return this.delegate.getLocalName();
/*     */   }
/*     */ 
/*     */   String prefix()
/*     */   {
/* 106 */     if (this.delegate.getNamespace() == null) return null;
/* 107 */     return this.delegate.getNamespace().getPrefix();
/*     */   }
/*     */ 
/*     */   String uri() {
/* 111 */     if (this.delegate.getNamespace() == null) return null;
/* 112 */     return this.delegate.getNamespace().getUri();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   final XmlNode.QName toNodeQname() {
/* 117 */     return this.delegate;
/*     */   }
/*     */ 
/*     */   final XmlNode.QName getDelegate() {
/* 121 */     return this.delegate;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 126 */     if (!(paramObject instanceof QName)) return false;
/* 127 */     return equals((QName)paramObject);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 132 */     return this.delegate.hashCode();
/*     */   }
/*     */ 
/*     */   protected Object equivalentValues(Object paramObject)
/*     */   {
/* 138 */     if (!(paramObject instanceof QName)) return Scriptable.NOT_FOUND;
/* 139 */     boolean bool = equals((QName)paramObject);
/* 140 */     return bool ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */ 
/*     */   private boolean equals(QName paramQName) {
/* 144 */     return this.delegate.equals(paramQName.delegate);
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 149 */     return "QName";
/*     */   }
/*     */ 
/*     */   public Object getDefaultValue(Class<?> paramClass)
/*     */   {
/* 154 */     return toString();
/*     */   }
/*     */ 
/*     */   protected int getMaxInstanceId()
/*     */   {
/* 166 */     return super.getMaxInstanceId() + 2;
/*     */   }
/*     */ 
/*     */   protected int findInstanceIdInfo(String paramString)
/*     */   {
/* 174 */     int i = 0; String str = null;
/* 175 */     int k = paramString.length();
/* 176 */     if (k == 3) { str = "uri"; i = 2;
/* 177 */     } else if (k == 9) { str = "localName"; i = 1; }
/* 178 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 183 */     if (i == 0) return super.findInstanceIdInfo(paramString);
/*     */     int j;
/* 186 */     switch (i) {
/*     */     case 1:
/*     */     case 2:
/* 189 */       j = 5;
/* 190 */       break;
/*     */     default:
/* 191 */       throw new IllegalStateException();
/*     */     }
/* 193 */     return instanceIdInfo(j, super.getMaxInstanceId() + i);
/*     */   }
/*     */ 
/*     */   protected String getInstanceIdName(int paramInt)
/*     */   {
/* 200 */     switch (paramInt - super.getMaxInstanceId()) { case 1:
/* 201 */       return "localName";
/*     */     case 2:
/* 202 */       return "uri";
/*     */     }
/* 204 */     return super.getInstanceIdName(paramInt);
/*     */   }
/*     */ 
/*     */   protected Object getInstanceIdValue(int paramInt)
/*     */   {
/* 210 */     switch (paramInt - super.getMaxInstanceId()) { case 1:
/* 211 */       return localName();
/*     */     case 2:
/* 212 */       return uri();
/*     */     }
/* 214 */     return super.getInstanceIdValue(paramInt);
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 229 */     int i = 0; String str = null;
/* 230 */     int k = paramString.length();
/* 231 */     if (k == 8) {
/* 232 */       int j = paramString.charAt(3);
/* 233 */       if (j == 111) { str = "toSource"; i = 3;
/* 234 */       } else if (j == 116) { str = "toString"; i = 2; }
/*     */     }
/* 236 */     else if (k == 11) { str = "constructor"; i = 1; }
/* 237 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 241 */     return i;
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*     */     int i;
/*     */     String str;
/* 250 */     switch (paramInt) { case 1:
/* 251 */       i = 2; str = "constructor"; break;
/*     */     case 2:
/* 252 */       i = 0; str = "toString"; break;
/*     */     case 3:
/* 253 */       i = 0; str = "toSource"; break;
/*     */     default:
/* 254 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */     }
/* 256 */     initPrototypeMethod(QNAME_TAG, paramInt, str, i);
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 266 */     if (!paramIdFunctionObject.hasTag(QNAME_TAG)) {
/* 267 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 269 */     int i = paramIdFunctionObject.methodId();
/* 270 */     switch (i) {
/*     */     case 1:
/* 272 */       return jsConstructor(paramContext, paramScriptable2 == null, paramArrayOfObject);
/*     */     case 2:
/* 274 */       return realThis(paramScriptable2, paramIdFunctionObject).toString();
/*     */     case 3:
/* 276 */       return realThis(paramScriptable2, paramIdFunctionObject).js_toSource();
/*     */     }
/* 278 */     throw new IllegalArgumentException(String.valueOf(i));
/*     */   }
/*     */ 
/*     */   private QName realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject)
/*     */   {
/* 283 */     if (!(paramScriptable instanceof QName))
/* 284 */       throw incompatibleCallError(paramIdFunctionObject);
/* 285 */     return (QName)paramScriptable;
/*     */   }
/*     */ 
/*     */   QName newQName(XMLLibImpl paramXMLLibImpl, String paramString1, String paramString2, String paramString3) {
/* 289 */     QName localQName = this.prototype;
/* 290 */     if (localQName == null) {
/* 291 */       localQName = this;
/*     */     }
/* 293 */     XmlNode.Namespace localNamespace = null;
/* 294 */     if (paramString3 != null)
/* 295 */       localNamespace = XmlNode.Namespace.create(paramString1, paramString3);
/* 296 */     else if (paramString1 != null)
/* 297 */       localNamespace = XmlNode.Namespace.create(paramString1);
/*     */     else {
/* 299 */       localNamespace = null;
/*     */     }
/* 301 */     if ((paramString2 != null) && (paramString2.equals("*"))) paramString2 = null;
/* 302 */     return create(paramXMLLibImpl, getParentScope(), localQName, XmlNode.QName.create(localNamespace, paramString2));
/*     */   }
/*     */ 
/*     */   QName constructQName(XMLLibImpl paramXMLLibImpl, Context paramContext, Object paramObject1, Object paramObject2)
/*     */   {
/* 307 */     String str1 = null;
/* 308 */     if ((paramObject2 instanceof QName)) {
/* 309 */       if (paramObject1 == Undefined.instance) {
/* 310 */         return (QName)paramObject2;
/*     */       }
/* 312 */       str1 = ((QName)paramObject2).localName();
/*     */     }
/*     */ 
/* 315 */     if (paramObject2 == Undefined.instance)
/* 316 */       str1 = "";
/*     */     else {
/* 318 */       str1 = ScriptRuntime.toString(paramObject2);
/*     */     }
/*     */ 
/* 321 */     if (paramObject1 == Undefined.instance) {
/* 322 */       if ("*".equals(str1))
/* 323 */         paramObject1 = null;
/*     */       else {
/* 325 */         paramObject1 = paramXMLLibImpl.getDefaultNamespace(paramContext);
/*     */       }
/*     */     }
/* 328 */     Namespace localNamespace = null;
/* 329 */     if (paramObject1 != null)
/*     */     {
/* 331 */       if ((paramObject1 instanceof Namespace))
/* 332 */         localNamespace = (Namespace)paramObject1;
/*     */       else
/* 334 */         localNamespace = paramXMLLibImpl.newNamespace(ScriptRuntime.toString(paramObject1));
/*     */     }
/* 336 */     String str2 = str1;
/*     */     String str3;
/*     */     String str4;
/* 339 */     if (paramObject1 == null) {
/* 340 */       str3 = null;
/* 341 */       str4 = null;
/*     */     } else {
/* 343 */       str3 = localNamespace.uri();
/* 344 */       str4 = localNamespace.prefix();
/*     */     }
/* 346 */     return newQName(paramXMLLibImpl, str3, str2, str4);
/*     */   }
/*     */ 
/*     */   QName constructQName(XMLLibImpl paramXMLLibImpl, Context paramContext, Object paramObject) {
/* 350 */     return constructQName(paramXMLLibImpl, paramContext, Undefined.instance, paramObject);
/*     */   }
/*     */ 
/*     */   QName castToQName(XMLLibImpl paramXMLLibImpl, Context paramContext, Object paramObject) {
/* 354 */     if ((paramObject instanceof QName)) {
/* 355 */       return (QName)paramObject;
/*     */     }
/* 357 */     return constructQName(paramXMLLibImpl, paramContext, paramObject);
/*     */   }
/*     */ 
/*     */   private Object jsConstructor(Context paramContext, boolean paramBoolean, Object[] paramArrayOfObject)
/*     */   {
/* 362 */     if ((!paramBoolean) && (paramArrayOfObject.length == 1)) {
/* 363 */       return castToQName(this.lib, paramContext, paramArrayOfObject[0]);
/*     */     }
/* 365 */     if (paramArrayOfObject.length == 0)
/* 366 */       return constructQName(this.lib, paramContext, Undefined.instance);
/* 367 */     if (paramArrayOfObject.length == 1) {
/* 368 */       return constructQName(this.lib, paramContext, paramArrayOfObject[0]);
/*     */     }
/* 370 */     return constructQName(this.lib, paramContext, paramArrayOfObject[0], paramArrayOfObject[1]);
/*     */   }
/*     */ 
/*     */   private String js_toSource()
/*     */   {
/* 375 */     StringBuffer localStringBuffer = new StringBuffer();
/* 376 */     localStringBuffer.append('(');
/* 377 */     toSourceImpl(uri(), localName(), prefix(), localStringBuffer);
/* 378 */     localStringBuffer.append(')');
/* 379 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static void toSourceImpl(String paramString1, String paramString2, String paramString3, StringBuffer paramStringBuffer) {
/* 383 */     paramStringBuffer.append("new QName(");
/* 384 */     if ((paramString1 == null) && (paramString3 == null)) {
/* 385 */       if (!"*".equals(paramString2))
/* 386 */         paramStringBuffer.append("null, ");
/*     */     }
/*     */     else {
/* 389 */       Namespace.toSourceImpl(paramString3, paramString1, paramStringBuffer);
/* 390 */       paramStringBuffer.append(", ");
/*     */     }
/* 392 */     paramStringBuffer.append('\'');
/* 393 */     paramStringBuffer.append(ScriptRuntime.escapeString(paramString2, '\''));
/* 394 */     paramStringBuffer.append("')");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.xmlimpl.QName
 * JD-Core Version:    0.6.2
 */