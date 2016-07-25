/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ final class NativeBoolean extends IdScriptableObject
/*     */ {
/*  50 */   private static final Object BOOLEAN_TAG = "Boolean";
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_toString = 2;
/*     */   private static final int Id_toSource = 3;
/*     */   private static final int Id_valueOf = 4;
/*     */   private static final int MAX_PROTOTYPE_ID = 4;
/*     */   private boolean booleanValue;
/*     */ 
/*     */   static void init(Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/*  54 */     NativeBoolean localNativeBoolean = new NativeBoolean(false);
/*  55 */     localNativeBoolean.exportAsJSClass(4, paramScriptable, paramBoolean);
/*     */   }
/*     */ 
/*     */   private NativeBoolean(boolean paramBoolean)
/*     */   {
/*  60 */     this.booleanValue = paramBoolean;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  66 */     return "Boolean";
/*     */   }
/*     */ 
/*     */   public Object getDefaultValue(Class<?> paramClass)
/*     */   {
/*  73 */     if (paramClass == ScriptRuntime.BooleanClass)
/*  74 */       return ScriptRuntime.wrapBoolean(this.booleanValue);
/*  75 */     return super.getDefaultValue(paramClass);
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*     */     int i;
/*     */     String str;
/*  83 */     switch (paramInt) { case 1:
/*  84 */       i = 1; str = "constructor"; break;
/*     */     case 2:
/*  85 */       i = 0; str = "toString"; break;
/*     */     case 3:
/*  86 */       i = 0; str = "toSource"; break;
/*     */     case 4:
/*  87 */       i = 0; str = "valueOf"; break;
/*     */     default:
/*  88 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */     }
/*  90 */     initPrototypeMethod(BOOLEAN_TAG, paramInt, str, i);
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/*  97 */     if (!paramIdFunctionObject.hasTag(BOOLEAN_TAG)) {
/*  98 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 100 */     int i = paramIdFunctionObject.methodId();
/*     */ 
/* 102 */     if (i == 1)
/*     */     {
/* 104 */       if (paramArrayOfObject.length == 0)
/* 105 */         bool = false;
/*     */       else {
/* 107 */         bool = ((paramArrayOfObject[0] instanceof ScriptableObject)) && (((ScriptableObject)paramArrayOfObject[0]).avoidObjectDetection()) ? true : ScriptRuntime.toBoolean(paramArrayOfObject[0]);
/*     */       }
/*     */ 
/* 112 */       if (paramScriptable2 == null)
/*     */       {
/* 114 */         return new NativeBoolean(bool);
/*     */       }
/*     */ 
/* 117 */       return ScriptRuntime.wrapBoolean(bool);
/*     */     }
/*     */ 
/* 122 */     if (!(paramScriptable2 instanceof NativeBoolean))
/* 123 */       throw incompatibleCallError(paramIdFunctionObject);
/* 124 */     boolean bool = ((NativeBoolean)paramScriptable2).booleanValue;
/*     */ 
/* 126 */     switch (i)
/*     */     {
/*     */     case 2:
/* 129 */       return bool ? "true" : "false";
/*     */     case 3:
/* 132 */       return bool ? "(new Boolean(true))" : "(new Boolean(false))";
/*     */     case 4:
/* 135 */       return ScriptRuntime.wrapBoolean(bool);
/*     */     }
/* 137 */     throw new IllegalArgumentException(String.valueOf(i));
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 147 */     int i = 0; String str = null;
/* 148 */     int k = paramString.length();
/* 149 */     if (k == 7) { str = "valueOf"; i = 4;
/* 150 */     } else if (k == 8) {
/* 151 */       int j = paramString.charAt(3);
/* 152 */       if (j == 111) { str = "toSource"; i = 3;
/* 153 */       } else if (j == 116) { str = "toString"; i = 2; }
/*     */     }
/* 155 */     else if (k == 11) { str = "constructor"; i = 1; }
/* 156 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 160 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeBoolean
 * JD-Core Version:    0.6.2
 */