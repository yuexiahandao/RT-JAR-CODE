/*     */ package sun.org.mozilla.javascript.internal.xmlimpl;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.IdFunctionObject;
/*     */ import sun.org.mozilla.javascript.internal.ScriptRuntime;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.ScriptableObject;
/*     */ import sun.org.mozilla.javascript.internal.Undefined;
/*     */ 
/*     */ class XMLCtor extends IdFunctionObject
/*     */ {
/*     */   static final long serialVersionUID = -8708195078359817341L;
/*  48 */   private static final Object XMLCTOR_TAG = "XMLCtor";
/*     */   private XmlProcessor options;
/*     */   private static final int Id_ignoreComments = 1;
/*     */   private static final int Id_ignoreProcessingInstructions = 2;
/*     */   private static final int Id_ignoreWhitespace = 3;
/*     */   private static final int Id_prettyIndent = 4;
/*     */   private static final int Id_prettyPrinting = 5;
/*     */   private static final int MAX_INSTANCE_ID = 5;
/*     */   private static final int Id_defaultSettings = 1;
/*     */   private static final int Id_settings = 2;
/*     */   private static final int Id_setSettings = 3;
/*     */   private static final int MAX_FUNCTION_ID = 3;
/*     */ 
/*     */   XMLCtor(XML paramXML, Object paramObject, int paramInt1, int paramInt2)
/*     */   {
/*  55 */     super(paramXML, paramObject, paramInt1, paramInt2);
/*     */ 
/*  57 */     this.options = paramXML.getProcessor();
/*  58 */     activatePrototypeMap(3);
/*     */   }
/*     */ 
/*     */   private void writeSetting(Scriptable paramScriptable)
/*     */   {
/*  63 */     for (int i = 1; i <= 5; i++) {
/*  64 */       int j = super.getMaxInstanceId() + i;
/*  65 */       String str = getInstanceIdName(j);
/*  66 */       Object localObject = getInstanceIdValue(j);
/*  67 */       ScriptableObject.putProperty(paramScriptable, str, localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readSettings(Scriptable paramScriptable)
/*     */   {
/*  73 */     for (int i = 1; i <= 5; i++) {
/*  74 */       int j = super.getMaxInstanceId() + i;
/*  75 */       String str = getInstanceIdName(j);
/*  76 */       Object localObject = ScriptableObject.getProperty(paramScriptable, str);
/*  77 */       if (localObject != Scriptable.NOT_FOUND)
/*     */       {
/*  80 */         switch (i) {
/*     */         case 1:
/*     */         case 2:
/*     */         case 3:
/*     */         case 5:
/*  85 */           if ((localObject instanceof Boolean)) break;
/*  86 */           break;
/*     */         case 4:
/*  90 */           if ((localObject instanceof Number)) break;
/*  91 */           break;
/*     */         default:
/*  95 */           throw new IllegalStateException();
/*     */         }
/*  97 */         setInstanceIdValue(j, localObject);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected int getMaxInstanceId()
/*     */   {
/* 115 */     return super.getMaxInstanceId() + 5;
/*     */   }
/*     */ 
/*     */   protected int findInstanceIdInfo(String paramString)
/*     */   {
/* 122 */     int i = 0; String str = null;
/* 123 */     switch (paramString.length()) { case 12:
/* 124 */       str = "prettyIndent"; i = 4; break;
/*     */     case 14:
/* 125 */       int k = paramString.charAt(0);
/* 126 */       if (k == 105) { str = "ignoreComments"; i = 1;
/* 127 */       } else if (k == 112) { str = "prettyPrinting"; i = 5; } break;
/*     */     case 16:
/* 129 */       str = "ignoreWhitespace"; i = 3; break;
/*     */     case 28:
/* 130 */       str = "ignoreProcessingInstructions"; i = 2; break;
/*     */     }
/* 132 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 137 */     if (i == 0) return super.findInstanceIdInfo(paramString);
/*     */     int j;
/* 140 */     switch (i) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/* 146 */       j = 6;
/* 147 */       break;
/*     */     default:
/* 148 */       throw new IllegalStateException();
/*     */     }
/* 150 */     return instanceIdInfo(j, super.getMaxInstanceId() + i);
/*     */   }
/*     */ 
/*     */   protected String getInstanceIdName(int paramInt)
/*     */   {
/* 158 */     switch (paramInt - super.getMaxInstanceId()) { case 1:
/* 159 */       return "ignoreComments";
/*     */     case 2:
/* 160 */       return "ignoreProcessingInstructions";
/*     */     case 3:
/* 161 */       return "ignoreWhitespace";
/*     */     case 4:
/* 162 */       return "prettyIndent";
/*     */     case 5:
/* 163 */       return "prettyPrinting";
/*     */     }
/* 165 */     return super.getInstanceIdName(paramInt);
/*     */   }
/*     */ 
/*     */   protected Object getInstanceIdValue(int paramInt)
/*     */   {
/* 171 */     switch (paramInt - super.getMaxInstanceId()) {
/*     */     case 1:
/* 173 */       return ScriptRuntime.wrapBoolean(this.options.isIgnoreComments());
/*     */     case 2:
/* 175 */       return ScriptRuntime.wrapBoolean(this.options.isIgnoreProcessingInstructions());
/*     */     case 3:
/* 177 */       return ScriptRuntime.wrapBoolean(this.options.isIgnoreWhitespace());
/*     */     case 4:
/* 179 */       return ScriptRuntime.wrapInt(this.options.getPrettyIndent());
/*     */     case 5:
/* 181 */       return ScriptRuntime.wrapBoolean(this.options.isPrettyPrinting());
/*     */     }
/* 183 */     return super.getInstanceIdValue(paramInt);
/*     */   }
/*     */ 
/*     */   protected void setInstanceIdValue(int paramInt, Object paramObject)
/*     */   {
/* 188 */     switch (paramInt - super.getMaxInstanceId()) {
/*     */     case 1:
/* 190 */       this.options.setIgnoreComments(ScriptRuntime.toBoolean(paramObject));
/* 191 */       return;
/*     */     case 2:
/* 193 */       this.options.setIgnoreProcessingInstructions(ScriptRuntime.toBoolean(paramObject));
/* 194 */       return;
/*     */     case 3:
/* 196 */       this.options.setIgnoreWhitespace(ScriptRuntime.toBoolean(paramObject));
/* 197 */       return;
/*     */     case 4:
/* 199 */       this.options.setPrettyIndent(ScriptRuntime.toInt32(paramObject));
/* 200 */       return;
/*     */     case 5:
/* 202 */       this.options.setPrettyPrinting(ScriptRuntime.toBoolean(paramObject));
/* 203 */       return;
/*     */     }
/* 205 */     super.setInstanceIdValue(paramInt, paramObject);
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 220 */     int i = 0; String str = null;
/* 221 */     int j = paramString.length();
/* 222 */     if (j == 8) { str = "settings"; i = 2;
/* 223 */     } else if (j == 11) { str = "setSettings"; i = 3;
/* 224 */     } else if (j == 15) { str = "defaultSettings"; i = 1; }
/* 225 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 229 */     return i;
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*     */     int i;
/*     */     String str;
/* 238 */     switch (paramInt) { case 1:
/* 239 */       i = 0; str = "defaultSettings"; break;
/*     */     case 2:
/* 240 */       i = 0; str = "settings"; break;
/*     */     case 3:
/* 241 */       i = 1; str = "setSettings"; break;
/*     */     default:
/* 242 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */     }
/* 244 */     initPrototypeMethod(XMLCTOR_TAG, paramInt, str, i);
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 251 */     if (!paramIdFunctionObject.hasTag(XMLCTOR_TAG)) {
/* 252 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 254 */     int i = paramIdFunctionObject.methodId();
/*     */     Scriptable localScriptable;
/* 255 */     switch (i) {
/*     */     case 1:
/* 257 */       this.options.setDefault();
/* 258 */       localScriptable = paramContext.newObject(paramScriptable1);
/* 259 */       writeSetting(localScriptable);
/* 260 */       return localScriptable;
/*     */     case 2:
/* 263 */       localScriptable = paramContext.newObject(paramScriptable1);
/* 264 */       writeSetting(localScriptable);
/* 265 */       return localScriptable;
/*     */     case 3:
/* 268 */       if ((paramArrayOfObject.length == 0) || (paramArrayOfObject[0] == null) || (paramArrayOfObject[0] == Undefined.instance))
/*     */       {
/* 272 */         this.options.setDefault();
/* 273 */       } else if ((paramArrayOfObject[0] instanceof Scriptable)) {
/* 274 */         readSettings((Scriptable)paramArrayOfObject[0]);
/*     */       }
/* 276 */       return Undefined.instance;
/*     */     }
/*     */ 
/* 279 */     throw new IllegalArgumentException(String.valueOf(i));
/*     */   }
/*     */ 
/*     */   public boolean hasInstance(Scriptable paramScriptable)
/*     */   {
/* 287 */     return ((paramScriptable instanceof XML)) || ((paramScriptable instanceof XMLList));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.xmlimpl.XMLCtor
 * JD-Core Version:    0.6.2
 */