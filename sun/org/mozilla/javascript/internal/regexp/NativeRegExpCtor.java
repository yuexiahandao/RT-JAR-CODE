/*     */ package sun.org.mozilla.javascript.internal.regexp;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.BaseFunction;
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.ScriptRuntime;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.Undefined;
/*     */ 
/*     */ class NativeRegExpCtor extends BaseFunction
/*     */ {
/*     */   static final long serialVersionUID = -5733330028285400526L;
/*     */   private static final int Id_multiline = 1;
/*     */   private static final int Id_STAR = 2;
/*     */   private static final int Id_input = 3;
/*     */   private static final int Id_UNDERSCORE = 4;
/*     */   private static final int Id_lastMatch = 5;
/*     */   private static final int Id_AMPERSAND = 6;
/*     */   private static final int Id_lastParen = 7;
/*     */   private static final int Id_PLUS = 8;
/*     */   private static final int Id_leftContext = 9;
/*     */   private static final int Id_BACK_QUOTE = 10;
/*     */   private static final int Id_rightContext = 11;
/*     */   private static final int Id_QUOTE = 12;
/*     */   private static final int DOLLAR_ID_BASE = 12;
/*     */   private static final int Id_DOLLAR_1 = 13;
/*     */   private static final int Id_DOLLAR_2 = 14;
/*     */   private static final int Id_DOLLAR_3 = 15;
/*     */   private static final int Id_DOLLAR_4 = 16;
/*     */   private static final int Id_DOLLAR_5 = 17;
/*     */   private static final int Id_DOLLAR_6 = 18;
/*     */   private static final int Id_DOLLAR_7 = 19;
/*     */   private static final int Id_DOLLAR_8 = 20;
/*     */   private static final int Id_DOLLAR_9 = 21;
/*     */   private static final int MAX_INSTANCE_ID = 21;
/*     */ 
/*     */   public String getFunctionName()
/*     */   {
/*  69 */     return "RegExp";
/*     */   }
/*     */ 
/*     */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/*  76 */     if ((paramArrayOfObject.length > 0) && ((paramArrayOfObject[0] instanceof NativeRegExp)) && ((paramArrayOfObject.length == 1) || (paramArrayOfObject[1] == Undefined.instance)))
/*     */     {
/*  79 */       return paramArrayOfObject[0];
/*     */     }
/*  81 */     return construct(paramContext, paramScriptable1, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*     */   {
/*  87 */     NativeRegExp localNativeRegExp = new NativeRegExp();
/*  88 */     localNativeRegExp.compile(paramContext, paramScriptable, paramArrayOfObject);
/*  89 */     ScriptRuntime.setObjectProtoAndParent(localNativeRegExp, paramScriptable);
/*  90 */     return localNativeRegExp;
/*     */   }
/*     */ 
/*     */   private static RegExpImpl getImpl()
/*     */   {
/*  95 */     Context localContext = Context.getCurrentContext();
/*  96 */     return (RegExpImpl)ScriptRuntime.getRegExpProxy(localContext);
/*     */   }
/*     */ 
/*     */   protected int getMaxInstanceId()
/*     */   {
/* 138 */     return super.getMaxInstanceId() + 21;
/*     */   }
/*     */ 
/*     */   protected int findInstanceIdInfo(String paramString)
/*     */   {
/* 145 */     int i = 0; String str = null;
/* 146 */     switch (paramString.length()) { case 2:
/* 147 */       switch (paramString.charAt(1)) { case '&':
/* 148 */         if (paramString.charAt(0) == '$') i = 6; break;
/*     */       case '\'':
/* 149 */         if (paramString.charAt(0) == '$') i = 12; break;
/*     */       case '*':
/* 150 */         if (paramString.charAt(0) == '$') i = 2; break;
/*     */       case '+':
/* 151 */         if (paramString.charAt(0) == '$') i = 8; break;
/*     */       case '1':
/* 152 */         if (paramString.charAt(0) == '$') i = 13; break;
/*     */       case '2':
/* 153 */         if (paramString.charAt(0) == '$') i = 14; break;
/*     */       case '3':
/* 154 */         if (paramString.charAt(0) == '$') i = 15; break;
/*     */       case '4':
/* 155 */         if (paramString.charAt(0) == '$') i = 16; break;
/*     */       case '5':
/* 156 */         if (paramString.charAt(0) == '$') i = 17; break;
/*     */       case '6':
/* 157 */         if (paramString.charAt(0) == '$') i = 18; break;
/*     */       case '7':
/* 158 */         if (paramString.charAt(0) == '$') i = 19; break;
/*     */       case '8':
/* 159 */         if (paramString.charAt(0) == '$') i = 20; break;
/*     */       case '9':
/* 160 */         if (paramString.charAt(0) == '$') i = 21; break;
/*     */       case '_':
/* 161 */         if (paramString.charAt(0) == '$') i = 4; break;
/*     */       case '`':
/* 162 */         if (paramString.charAt(0) == '$') { i = 10; break label663; } break;
/*     */       case '(':
/*     */       case ')':
/*     */       case ',':
/*     */       case '-':
/*     */       case '.':
/*     */       case '/':
/*     */       case '0':
/*     */       case ':':
/*     */       case ';':
/*     */       case '<':
/*     */       case '=':
/*     */       case '>':
/*     */       case '?':
/*     */       case '@':
/*     */       case 'A':
/*     */       case 'B':
/*     */       case 'C':
/*     */       case 'D':
/*     */       case 'E':
/*     */       case 'F':
/*     */       case 'G':
/*     */       case 'H':
/*     */       case 'I':
/*     */       case 'J':
/*     */       case 'K':
/*     */       case 'L':
/*     */       case 'M':
/*     */       case 'N':
/*     */       case 'O':
/*     */       case 'P':
/*     */       case 'Q':
/*     */       case 'R':
/*     */       case 'S':
/*     */       case 'T':
/*     */       case 'U':
/*     */       case 'V':
/*     */       case 'W':
/*     */       case 'X':
/*     */       case 'Y':
/*     */       case 'Z':
/*     */       case '[':
/*     */       case '\\':
/*     */       case ']':
/* 163 */       case '^': } break;
/*     */     case 5:
/* 164 */       str = "input"; i = 3; break;
/*     */     case 9:
/* 165 */       int k = paramString.charAt(4);
/* 166 */       if (k == 77) { str = "lastMatch"; i = 5;
/* 167 */       } else if (k == 80) { str = "lastParen"; i = 7;
/* 168 */       } else if (k == 105) { str = "multiline"; i = 1; } break;
/*     */     case 11:
/* 170 */       str = "leftContext"; i = 9; break;
/*     */     case 12:
/* 171 */       str = "rightContext"; i = 11; break;
/*     */     case 3:
/*     */     case 4:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/* 173 */     case 10: } if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 177 */     label663: if (i == 0) return super.findInstanceIdInfo(paramString);
/*     */     int j;
/* 180 */     switch (i) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/* 185 */       j = 4;
/* 186 */       break;
/*     */     default:
/* 188 */       j = 5;
/*     */     }
/*     */ 
/* 192 */     return instanceIdInfo(j, super.getMaxInstanceId() + i);
/*     */   }
/*     */ 
/*     */   protected String getInstanceIdName(int paramInt)
/*     */   {
/* 200 */     int i = paramInt - super.getMaxInstanceId();
/* 201 */     if ((1 <= i) && (i <= 21)) {
/* 202 */       switch (i) { case 1:
/* 203 */         return "multiline";
/*     */       case 2:
/* 204 */         return "$*";
/*     */       case 3:
/* 206 */         return "input";
/*     */       case 4:
/* 207 */         return "$_";
/*     */       case 5:
/* 209 */         return "lastMatch";
/*     */       case 6:
/* 210 */         return "$&";
/*     */       case 7:
/* 212 */         return "lastParen";
/*     */       case 8:
/* 213 */         return "$+";
/*     */       case 9:
/* 215 */         return "leftContext";
/*     */       case 10:
/* 216 */         return "$`";
/*     */       case 11:
/* 218 */         return "rightContext";
/*     */       case 12:
/* 219 */         return "$'";
/*     */       }
/*     */ 
/* 222 */       int j = i - 12 - 1;
/* 223 */       char[] arrayOfChar = { '$', (char)(49 + j) };
/* 224 */       return new String(arrayOfChar);
/*     */     }
/* 226 */     return super.getInstanceIdName(paramInt);
/*     */   }
/*     */ 
/*     */   protected Object getInstanceIdValue(int paramInt)
/*     */   {
/* 232 */     int i = paramInt - super.getMaxInstanceId();
/* 233 */     if ((1 <= i) && (i <= 21)) {
/* 234 */       RegExpImpl localRegExpImpl = getImpl();
/*     */       Object localObject;
/* 236 */       switch (i) {
/*     */       case 1:
/*     */       case 2:
/* 239 */         return ScriptRuntime.wrapBoolean(localRegExpImpl.multiline);
/*     */       case 3:
/*     */       case 4:
/* 243 */         localObject = localRegExpImpl.input;
/* 244 */         break;
/*     */       case 5:
/*     */       case 6:
/* 248 */         localObject = localRegExpImpl.lastMatch;
/* 249 */         break;
/*     */       case 7:
/*     */       case 8:
/* 253 */         localObject = localRegExpImpl.lastParen;
/* 254 */         break;
/*     */       case 9:
/*     */       case 10:
/* 258 */         localObject = localRegExpImpl.leftContext;
/* 259 */         break;
/*     */       case 11:
/*     */       case 12:
/* 263 */         localObject = localRegExpImpl.rightContext;
/* 264 */         break;
/*     */       default:
/* 269 */         int j = i - 12 - 1;
/* 270 */         localObject = localRegExpImpl.getParenSubString(j);
/* 271 */         break;
/*     */       }
/*     */ 
/* 274 */       return localObject == null ? "" : localObject.toString();
/*     */     }
/* 276 */     return super.getInstanceIdValue(paramInt);
/*     */   }
/*     */ 
/*     */   protected void setInstanceIdValue(int paramInt, Object paramObject)
/*     */   {
/* 282 */     int i = paramInt - super.getMaxInstanceId();
/* 283 */     switch (i) {
/*     */     case 1:
/*     */     case 2:
/* 286 */       getImpl().multiline = ScriptRuntime.toBoolean(paramObject);
/* 287 */       return;
/*     */     case 3:
/*     */     case 4:
/* 291 */       getImpl().input = ScriptRuntime.toString(paramObject);
/* 292 */       return;
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/* 302 */       return;
/*     */     }
/* 304 */     int j = i - 12 - 1;
/* 305 */     if ((0 <= j) && (j <= 8)) {
/* 306 */       return;
/*     */     }
/*     */ 
/* 309 */     super.setInstanceIdValue(paramInt, paramObject);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.regexp.NativeRegExpCtor
 * JD-Core Version:    0.6.2
 */