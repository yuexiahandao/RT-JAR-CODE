/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import sun.org.mozilla.javascript.internal.json.JsonParser;
/*     */ import sun.org.mozilla.javascript.internal.json.JsonParser.ParseException;
/*     */ 
/*     */ final class NativeJSON extends IdScriptableObject
/*     */ {
/*  59 */   private static final Object JSON_TAG = "JSON";
/*     */   private static final int MAX_STRINGIFY_GAP_LENGTH = 10;
/*     */   private static final int Id_toSource = 1;
/*     */   private static final int Id_parse = 2;
/*     */   private static final int Id_stringify = 3;
/*     */   private static final int LAST_METHOD_ID = 3;
/*     */   private static final int MAX_ID = 3;
/*     */ 
/*     */   static void init(Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/*  65 */     NativeJSON localNativeJSON = new NativeJSON();
/*  66 */     localNativeJSON.activatePrototypeMap(3);
/*  67 */     localNativeJSON.setPrototype(getObjectPrototype(paramScriptable));
/*  68 */     localNativeJSON.setParentScope(paramScriptable);
/*  69 */     if (paramBoolean) localNativeJSON.sealObject();
/*  70 */     ScriptableObject.defineProperty(paramScriptable, "JSON", localNativeJSON, 2);
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  79 */     return "JSON";
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*  84 */     if (paramInt <= 3)
/*     */     {
/*     */       int i;
/*     */       String str;
/*  87 */       switch (paramInt) { case 1:
/*  88 */         i = 0; str = "toSource"; break;
/*     */       case 2:
/*  89 */         i = 2; str = "parse"; break;
/*     */       case 3:
/*  90 */         i = 3; str = "stringify"; break;
/*     */       default:
/*  91 */         throw new IllegalStateException(String.valueOf(paramInt));
/*     */       }
/*  93 */       initPrototypeMethod(JSON_TAG, paramInt, str, i);
/*     */     } else {
/*  95 */       throw new IllegalStateException(String.valueOf(paramInt));
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 103 */     if (!paramIdFunctionObject.hasTag(JSON_TAG)) {
/* 104 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 106 */     int i = paramIdFunctionObject.methodId();
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 107 */     switch (i) {
/*     */     case 1:
/* 109 */       return "JSON";
/*     */     case 2:
/* 112 */       localObject1 = ScriptRuntime.toString(paramArrayOfObject, 0);
/* 113 */       localObject2 = null;
/* 114 */       if (paramArrayOfObject.length > 1) {
/* 115 */         localObject2 = paramArrayOfObject[1];
/*     */       }
/* 117 */       if ((localObject2 instanceof Callable)) {
/* 118 */         return parse(paramContext, paramScriptable1, (String)localObject1, (Callable)localObject2);
/*     */       }
/* 120 */       return parse(paramContext, paramScriptable1, (String)localObject1);
/*     */     case 3:
/* 125 */       localObject1 = null; localObject2 = null; Object localObject3 = null;
/* 126 */       switch (paramArrayOfObject.length) { case 3:
/*     */       default:
/* 128 */         localObject3 = paramArrayOfObject[2];
/*     */       case 2:
/* 129 */         localObject2 = paramArrayOfObject[1];
/*     */       case 1:
/* 130 */         localObject1 = paramArrayOfObject[0];
/*     */       case 0:
/*     */       }
/* 133 */       return stringify(paramContext, paramScriptable1, localObject1, localObject2, localObject3);
/*     */     }
/*     */ 
/* 136 */     throw new IllegalStateException(String.valueOf(i));
/*     */   }
/*     */ 
/*     */   private static Object parse(Context paramContext, Scriptable paramScriptable, String paramString)
/*     */   {
/*     */     try {
/* 142 */       return new JsonParser(paramContext, paramScriptable).parseValue(paramString);
/*     */     } catch (JsonParser.ParseException localParseException) {
/* 144 */       throw ScriptRuntime.constructError("SyntaxError", localParseException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Object parse(Context paramContext, Scriptable paramScriptable, String paramString, Callable paramCallable)
/*     */   {
/* 151 */     Object localObject = parse(paramContext, paramScriptable, paramString);
/* 152 */     Scriptable localScriptable = paramContext.newObject(paramScriptable);
/* 153 */     localScriptable.put("", localScriptable, localObject);
/* 154 */     return walk(paramContext, paramScriptable, paramCallable, localScriptable, "");
/*     */   }
/*     */ 
/*     */   private static Object walk(Context paramContext, Scriptable paramScriptable1, Callable paramCallable, Scriptable paramScriptable2, Object paramObject)
/*     */   {
/*     */     Object localObject1;
/* 161 */     if ((paramObject instanceof Number))
/* 162 */       localObject1 = paramScriptable2.get(((Number)paramObject).intValue(), paramScriptable2);
/*     */     else {
/* 164 */       localObject1 = paramScriptable2.get((String)paramObject, paramScriptable2);
/*     */     }
/*     */ 
/* 167 */     if ((localObject1 instanceof Scriptable)) {
/* 168 */       Scriptable localScriptable = (Scriptable)localObject1;
/* 169 */       if ((localScriptable instanceof NativeArray)) {
/* 170 */         int i = (int)((NativeArray)localScriptable).getLength();
/* 171 */         for (int j = 0; j < i; j++) {
/* 172 */           Object localObject2 = walk(paramContext, paramScriptable1, paramCallable, localScriptable, Integer.valueOf(j));
/* 173 */           if (localObject2 == Undefined.instance)
/* 174 */             localScriptable.delete(j);
/*     */           else
/* 176 */             localScriptable.put(j, localScriptable, localObject2);
/*     */         }
/*     */       }
/*     */       else {
/* 180 */         Object[] arrayOfObject1 = localScriptable.getIds();
/* 181 */         for (Object localObject3 : arrayOfObject1) {
/* 182 */           Object localObject4 = walk(paramContext, paramScriptable1, paramCallable, localScriptable, localObject3);
/* 183 */           if (localObject4 == Undefined.instance) {
/* 184 */             if ((localObject3 instanceof Number))
/* 185 */               localScriptable.delete(((Number)localObject3).intValue());
/*     */             else
/* 187 */               localScriptable.delete((String)localObject3);
/*     */           }
/* 189 */           else if ((localObject3 instanceof Number))
/* 190 */             localScriptable.put(((Number)localObject3).intValue(), localScriptable, localObject4);
/*     */           else {
/* 192 */             localScriptable.put((String)localObject3, localScriptable, localObject4);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 198 */     return paramCallable.call(paramContext, paramScriptable1, paramScriptable2, new Object[] { paramObject, localObject1 });
/*     */   }
/*     */ 
/*     */   private static String repeat(char paramChar, int paramInt) {
/* 202 */     char[] arrayOfChar = new char[paramInt];
/* 203 */     Arrays.fill(arrayOfChar, paramChar);
/* 204 */     return new String(arrayOfChar);
/*     */   }
/*     */ 
/*     */   public static Object stringify(Context paramContext, Scriptable paramScriptable, Object paramObject1, Object paramObject2, Object paramObject3)
/*     */   {
/* 236 */     String str1 = "";
/* 237 */     String str2 = "";
/*     */ 
/* 239 */     LinkedList localLinkedList = null;
/* 240 */     Callable localCallable = null;
/*     */ 
/* 242 */     if ((paramObject2 instanceof Callable)) {
/* 243 */       localCallable = (Callable)paramObject2;
/* 244 */     } else if ((paramObject2 instanceof NativeArray)) {
/* 245 */       localLinkedList = new LinkedList();
/* 246 */       NativeArray localNativeArray = (NativeArray)paramObject2;
/* 247 */       localObject1 = localNativeArray.getIndexIds(); int j = localObject1.length; for (int k = 0; k < j; k++) { int m = localObject1[k].intValue();
/* 248 */         Object localObject2 = localNativeArray.get(m, localNativeArray);
/* 249 */         if (((localObject2 instanceof String)) || ((localObject2 instanceof Number)))
/* 250 */           localLinkedList.add(localObject2);
/* 251 */         else if (((localObject2 instanceof NativeString)) || ((localObject2 instanceof NativeNumber))) {
/* 252 */           localLinkedList.add(ScriptRuntime.toString(localObject2));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 257 */     if ((paramObject3 instanceof NativeNumber))
/* 258 */       paramObject3 = Double.valueOf(ScriptRuntime.toNumber(paramObject3));
/* 259 */     else if ((paramObject3 instanceof NativeString)) {
/* 260 */       paramObject3 = ScriptRuntime.toString(paramObject3);
/*     */     }
/*     */ 
/* 263 */     if ((paramObject3 instanceof Number)) {
/* 264 */       int i = (int)ScriptRuntime.toInteger(paramObject3);
/* 265 */       i = Math.min(10, i);
/* 266 */       str2 = i > 0 ? repeat(' ', i) : "";
/* 267 */       paramObject3 = Integer.valueOf(i);
/* 268 */     } else if ((paramObject3 instanceof String)) {
/* 269 */       str2 = (String)paramObject3;
/* 270 */       if (str2.length() > 10) {
/* 271 */         str2 = str2.substring(0, 10);
/*     */       }
/*     */     }
/*     */ 
/* 275 */     StringifyState localStringifyState = new StringifyState(paramContext, paramScriptable, str1, str2, localCallable, localLinkedList, paramObject3);
/*     */ 
/* 282 */     Object localObject1 = new NativeObject();
/* 283 */     ((ScriptableObject)localObject1).setParentScope(paramScriptable);
/* 284 */     ((ScriptableObject)localObject1).setPrototype(ScriptableObject.getObjectPrototype(paramScriptable));
/* 285 */     ((ScriptableObject)localObject1).defineProperty("", paramObject1, 0);
/* 286 */     return str("", (Scriptable)localObject1, localStringifyState);
/*     */   }
/*     */ 
/*     */   private static Object str(Object paramObject, Scriptable paramScriptable, StringifyState paramStringifyState)
/*     */   {
/* 292 */     Object localObject1 = null;
/* 293 */     if ((paramObject instanceof String))
/* 294 */       localObject1 = getProperty(paramScriptable, (String)paramObject);
/*     */     else {
/* 296 */       localObject1 = getProperty(paramScriptable, ((Number)paramObject).intValue());
/*     */     }
/*     */ 
/* 299 */     if ((localObject1 instanceof Scriptable)) {
/* 300 */       Object localObject2 = getProperty((Scriptable)localObject1, "toJSON");
/* 301 */       if ((localObject2 instanceof Callable)) {
/* 302 */         localObject1 = callMethod(paramStringifyState.cx, (Scriptable)localObject1, "toJSON", new Object[] { paramObject });
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 307 */     if (paramStringifyState.replacer != null) {
/* 308 */       localObject1 = paramStringifyState.replacer.call(paramStringifyState.cx, paramStringifyState.scope, paramScriptable, new Object[] { paramObject, localObject1 });
/*     */     }
/*     */ 
/* 313 */     if ((localObject1 instanceof NativeNumber))
/* 314 */       localObject1 = Double.valueOf(ScriptRuntime.toNumber(localObject1));
/* 315 */     else if ((localObject1 instanceof NativeString))
/* 316 */       localObject1 = ScriptRuntime.toString(localObject1);
/* 317 */     else if ((localObject1 instanceof NativeBoolean)) {
/* 318 */       localObject1 = ((NativeBoolean)localObject1).getDefaultValue(ScriptRuntime.BooleanClass);
/*     */     }
/*     */ 
/* 321 */     if (localObject1 == null) return "null";
/* 322 */     if (localObject1.equals(Boolean.TRUE)) return "true";
/* 323 */     if (localObject1.equals(Boolean.FALSE)) return "false";
/*     */ 
/* 325 */     if ((localObject1 instanceof String)) {
/* 326 */       return quote((String)localObject1);
/*     */     }
/*     */ 
/* 329 */     if ((localObject1 instanceof Number)) {
/* 330 */       double d = ((Number)localObject1).doubleValue();
/* 331 */       if ((d == d) && (d != (1.0D / 0.0D)) && (d != (-1.0D / 0.0D)))
/*     */       {
/* 334 */         return ScriptRuntime.toString(localObject1);
/*     */       }
/* 336 */       return "null";
/*     */     }
/*     */ 
/* 340 */     if (((localObject1 instanceof Scriptable)) && (!(localObject1 instanceof Callable))) {
/* 341 */       if ((localObject1 instanceof NativeArray)) {
/* 342 */         return ja((NativeArray)localObject1, paramStringifyState);
/*     */       }
/* 344 */       return jo((Scriptable)localObject1, paramStringifyState);
/*     */     }
/*     */ 
/* 347 */     return Undefined.instance;
/*     */   }
/*     */ 
/*     */   private static String join(Collection<Object> paramCollection, String paramString) {
/* 351 */     if ((paramCollection == null) || (paramCollection.isEmpty())) {
/* 352 */       return "";
/*     */     }
/* 354 */     Iterator localIterator = paramCollection.iterator();
/* 355 */     if (!localIterator.hasNext()) return "";
/* 356 */     StringBuilder localStringBuilder = new StringBuilder(localIterator.next().toString());
/* 357 */     while (localIterator.hasNext()) {
/* 358 */       localStringBuilder.append(paramString).append(localIterator.next().toString());
/*     */     }
/* 360 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static String jo(Scriptable paramScriptable, StringifyState paramStringifyState) {
/* 364 */     if (paramStringifyState.stack.search(paramScriptable) != -1) {
/* 365 */       throw ScriptRuntime.typeError0("msg.cyclic.value");
/*     */     }
/* 367 */     paramStringifyState.stack.push(paramScriptable);
/*     */ 
/* 369 */     String str1 = paramStringifyState.indent;
/* 370 */     paramStringifyState.indent += paramStringifyState.gap;
/* 371 */     Object[] arrayOfObject = null;
/* 372 */     if (paramStringifyState.propertyList != null)
/* 373 */       arrayOfObject = paramStringifyState.propertyList.toArray();
/*     */     else {
/* 375 */       arrayOfObject = paramScriptable.getIds();
/*     */     }
/*     */ 
/* 378 */     LinkedList localLinkedList = new LinkedList();
/*     */ 
/* 380 */     for (Object localObject2 : arrayOfObject) {
/* 381 */       Object localObject3 = str(localObject2, paramScriptable, paramStringifyState);
/* 382 */       if (localObject3 != Undefined.instance) {
/* 383 */         String str4 = quote(localObject2.toString()) + ":";
/* 384 */         if (paramStringifyState.gap.length() > 0) {
/* 385 */           str4 = str4 + " ";
/*     */         }
/* 387 */         str4 = str4 + localObject3;
/* 388 */         localLinkedList.add(str4);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 394 */     if (localLinkedList.isEmpty()) {
/* 395 */       ??? = "{}";
/*     */     }
/* 397 */     else if (paramStringifyState.gap.length() == 0) {
/* 398 */       ??? = '{' + join(localLinkedList, ",") + '}';
/*     */     } else {
/* 400 */       String str2 = ",\n" + paramStringifyState.indent;
/* 401 */       String str3 = join(localLinkedList, str2);
/* 402 */       ??? = "{\n" + paramStringifyState.indent + str3 + '\n' + str1 + '}';
/*     */     }
/*     */ 
/* 407 */     paramStringifyState.stack.pop();
/* 408 */     paramStringifyState.indent = str1;
/* 409 */     return ???;
/*     */   }
/*     */ 
/*     */   private static String ja(NativeArray paramNativeArray, StringifyState paramStringifyState) {
/* 413 */     if (paramStringifyState.stack.search(paramNativeArray) != -1) {
/* 414 */       throw ScriptRuntime.typeError0("msg.cyclic.value");
/*     */     }
/* 416 */     paramStringifyState.stack.push(paramNativeArray);
/*     */ 
/* 418 */     String str1 = paramStringifyState.indent;
/* 419 */     paramStringifyState.indent += paramStringifyState.gap;
/* 420 */     LinkedList localLinkedList = new LinkedList();
/*     */ 
/* 422 */     int i = (int)paramNativeArray.getLength();
/*     */     Object localObject;
/* 423 */     for (int j = 0; j < i; j++) {
/* 424 */       localObject = str(Integer.valueOf(j), paramNativeArray, paramStringifyState);
/* 425 */       if (localObject == Undefined.instance)
/* 426 */         localLinkedList.add("null");
/*     */       else
/* 428 */         localLinkedList.add(localObject);
/*     */     }
/*     */     String str2;
/* 434 */     if (localLinkedList.isEmpty()) {
/* 435 */       str2 = "[]";
/*     */     }
/* 437 */     else if (paramStringifyState.gap.length() == 0) {
/* 438 */       str2 = '[' + join(localLinkedList, ",") + ']';
/*     */     } else {
/* 440 */       localObject = ",\n" + paramStringifyState.indent;
/* 441 */       String str3 = join(localLinkedList, (String)localObject);
/* 442 */       str2 = "[\n" + paramStringifyState.indent + str3 + '\n' + str1 + ']';
/*     */     }
/*     */ 
/* 446 */     paramStringifyState.stack.pop();
/* 447 */     paramStringifyState.indent = str1;
/* 448 */     return str2;
/*     */   }
/*     */ 
/*     */   private static String quote(String paramString) {
/* 452 */     StringBuffer localStringBuffer = new StringBuffer(paramString.length() + 2);
/* 453 */     localStringBuffer.append('"');
/* 454 */     int i = paramString.length();
/* 455 */     for (int j = 0; j < i; j++) {
/* 456 */       char c = paramString.charAt(j);
/* 457 */       switch (c) {
/*     */       case '"':
/* 459 */         localStringBuffer.append("\\\"");
/* 460 */         break;
/*     */       case '\\':
/* 462 */         localStringBuffer.append("\\\\");
/* 463 */         break;
/*     */       case '\b':
/* 465 */         localStringBuffer.append("\\b");
/* 466 */         break;
/*     */       case '\f':
/* 468 */         localStringBuffer.append("\\f");
/* 469 */         break;
/*     */       case '\n':
/* 471 */         localStringBuffer.append("\\n");
/* 472 */         break;
/*     */       case '\r':
/* 474 */         localStringBuffer.append("\\r");
/* 475 */         break;
/*     */       case '\t':
/* 477 */         localStringBuffer.append("\\t");
/* 478 */         break;
/*     */       default:
/* 480 */         if (c < ' ') {
/* 481 */           localStringBuffer.append("\\u");
/* 482 */           String str = String.format("%04x", new Object[] { Integer.valueOf(c) });
/* 483 */           localStringBuffer.append(str);
/*     */         }
/*     */         else {
/* 486 */           localStringBuffer.append(c);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 491 */     localStringBuffer.append('"');
/* 492 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 502 */     int i = 0; String str = null;
/* 503 */     switch (paramString.length()) { case 5:
/* 504 */       str = "parse"; i = 2; break;
/*     */     case 8:
/* 505 */       str = "toSource"; i = 1; break;
/*     */     case 9:
/* 506 */       str = "stringify"; i = 3; break;
/*     */     case 6:
/* 508 */     case 7: } if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 511 */     return i;
/*     */   }
/*     */ 
/*     */   private static class StringifyState
/*     */   {
/* 222 */     Stack<Scriptable> stack = new Stack();
/*     */     String indent;
/*     */     String gap;
/*     */     Callable replacer;
/*     */     List<Object> propertyList;
/*     */     Object space;
/*     */     Context cx;
/*     */     Scriptable scope;
/*     */ 
/*     */     StringifyState(Context paramContext, Scriptable paramScriptable, String paramString1, String paramString2, Callable paramCallable, List<Object> paramList, Object paramObject)
/*     */     {
/* 212 */       this.cx = paramContext;
/* 213 */       this.scope = paramScriptable;
/*     */ 
/* 215 */       this.indent = paramString1;
/* 216 */       this.gap = paramString2;
/* 217 */       this.replacer = paramCallable;
/* 218 */       this.propertyList = paramList;
/* 219 */       this.space = paramObject;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeJSON
 * JD-Core Version:    0.6.2
 */