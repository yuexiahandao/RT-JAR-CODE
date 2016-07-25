/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public final class NativeIterator extends IdScriptableObject
/*     */ {
/*  42 */   private static final Object ITERATOR_TAG = "Iterator";
/*     */   private static final String STOP_ITERATION = "StopIteration";
/*     */   public static final String ITERATOR_PROPERTY_NAME = "__iterator__";
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_next = 2;
/*     */   private static final int Id___iterator__ = 3;
/*     */   private static final int MAX_PROTOTYPE_ID = 3;
/*     */   private Object objectIterator;
/*     */ 
/*     */   static void init(ScriptableObject paramScriptableObject, boolean paramBoolean)
/*     */   {
/*  46 */     NativeIterator localNativeIterator = new NativeIterator();
/*  47 */     localNativeIterator.exportAsJSClass(3, paramScriptableObject, paramBoolean);
/*     */ 
/*  50 */     NativeGenerator.init(paramScriptableObject, paramBoolean);
/*     */ 
/*  53 */     StopIteration localStopIteration = new StopIteration();
/*  54 */     localStopIteration.setPrototype(getObjectPrototype(paramScriptableObject));
/*  55 */     localStopIteration.setParentScope(paramScriptableObject);
/*  56 */     if (paramBoolean) localStopIteration.sealObject();
/*  57 */     ScriptableObject.defineProperty(paramScriptableObject, "StopIteration", localStopIteration, 2);
/*     */ 
/*  62 */     paramScriptableObject.associateValue(ITERATOR_TAG, localStopIteration);
/*     */   }
/*     */ 
/*     */   private NativeIterator()
/*     */   {
/*     */   }
/*     */ 
/*     */   private NativeIterator(Object paramObject)
/*     */   {
/*  72 */     this.objectIterator = paramObject;
/*     */   }
/*     */ 
/*     */   public static Object getStopIterationObject(Scriptable paramScriptable)
/*     */   {
/*  84 */     Scriptable localScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
/*  85 */     return ScriptableObject.getTopScopeValue(localScriptable, ITERATOR_TAG);
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 108 */     return "Iterator";
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*     */     int i;
/*     */     String str;
/* 115 */     switch (paramInt) { case 1:
/* 116 */       i = 2; str = "constructor"; break;
/*     */     case 2:
/* 117 */       i = 0; str = "next"; break;
/*     */     case 3:
/* 118 */       i = 1; str = "__iterator__"; break;
/*     */     default:
/* 119 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */     }
/* 121 */     initPrototypeMethod(ITERATOR_TAG, paramInt, str, i);
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 128 */     if (!paramIdFunctionObject.hasTag(ITERATOR_TAG)) {
/* 129 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 131 */     int i = paramIdFunctionObject.methodId();
/*     */ 
/* 133 */     if (i == 1) {
/* 134 */       return jsConstructor(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/*     */ 
/* 137 */     if (!(paramScriptable2 instanceof NativeIterator)) {
/* 138 */       throw incompatibleCallError(paramIdFunctionObject);
/*     */     }
/* 140 */     NativeIterator localNativeIterator = (NativeIterator)paramScriptable2;
/*     */ 
/* 142 */     switch (i)
/*     */     {
/*     */     case 2:
/* 145 */       return localNativeIterator.next(paramContext, paramScriptable1);
/*     */     case 3:
/* 149 */       return paramScriptable2;
/*     */     }
/*     */ 
/* 152 */     throw new IllegalArgumentException(String.valueOf(i));
/*     */   }
/*     */ 
/*     */   private static Object jsConstructor(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 160 */     if ((paramArrayOfObject.length == 0) || (paramArrayOfObject[0] == null) || (paramArrayOfObject[0] == Undefined.instance))
/*     */     {
/* 163 */       localObject1 = paramArrayOfObject.length == 0 ? Undefined.instance : paramArrayOfObject[0];
/* 164 */       throw ScriptRuntime.typeError1("msg.no.properties", ScriptRuntime.toString(localObject1));
/*     */     }
/*     */ 
/* 167 */     Object localObject1 = ScriptRuntime.toObject(paramScriptable1, paramArrayOfObject[0]);
/* 168 */     boolean bool = (paramArrayOfObject.length > 1) && (ScriptRuntime.toBoolean(paramArrayOfObject[1]));
/* 169 */     if (paramScriptable2 != null)
/*     */     {
/* 175 */       localObject2 = VMBridge.instance.getJavaIterator(paramContext, paramScriptable1, localObject1);
/*     */ 
/* 177 */       if (localObject2 != null) {
/* 178 */         paramScriptable1 = ScriptableObject.getTopLevelScope(paramScriptable1);
/* 179 */         return paramContext.getWrapFactory().wrap(paramContext, paramScriptable1, new WrappedJavaIterator((Iterator)localObject2, paramScriptable1), WrappedJavaIterator.class);
/*     */       }
/*     */ 
/* 185 */       localObject3 = ScriptRuntime.toIterator(paramContext, paramScriptable1, (Scriptable)localObject1, bool);
/*     */ 
/* 187 */       if (localObject3 != null) {
/* 188 */         return localObject3;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 194 */     Object localObject2 = ScriptRuntime.enumInit(localObject1, paramContext, bool ? 3 : 5);
/*     */ 
/* 197 */     ScriptRuntime.setEnumNumbers(localObject2, true);
/* 198 */     Object localObject3 = new NativeIterator(localObject2);
/* 199 */     ((NativeIterator)localObject3).setPrototype(ScriptableObject.getClassPrototype(paramScriptable1, ((NativeIterator)localObject3).getClassName()));
/*     */ 
/* 201 */     ((NativeIterator)localObject3).setParentScope(paramScriptable1);
/* 202 */     return localObject3;
/*     */   }
/*     */ 
/*     */   private Object next(Context paramContext, Scriptable paramScriptable) {
/* 206 */     Boolean localBoolean = ScriptRuntime.enumNext(this.objectIterator);
/* 207 */     if (!localBoolean.booleanValue())
/*     */     {
/* 209 */       throw new JavaScriptException(getStopIterationObject(paramScriptable), null, 0);
/*     */     }
/*     */ 
/* 212 */     return ScriptRuntime.enumId(this.objectIterator, paramContext);
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 245 */     int i = 0; String str = null;
/* 246 */     int j = paramString.length();
/* 247 */     if (j == 4) { str = "next"; i = 2;
/* 248 */     } else if (j == 11) { str = "constructor"; i = 1;
/* 249 */     } else if (j == 12) { str = "__iterator__"; i = 3; }
/* 250 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 254 */     return i;
/*     */   }
/*     */ 
/*     */   static class StopIteration extends NativeObject
/*     */   {
/*     */     public String getClassName()
/*     */     {
/*  94 */       return "StopIteration";
/*     */     }
/*     */ 
/*     */     public boolean hasInstance(Scriptable paramScriptable)
/*     */     {
/* 102 */       return paramScriptable instanceof StopIteration;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class WrappedJavaIterator
/*     */   {
/*     */     private Iterator<?> iterator;
/*     */     private Scriptable scope;
/*     */ 
/*     */     WrappedJavaIterator(Iterator<?> paramIterator, Scriptable paramScriptable)
/*     */     {
/* 218 */       this.iterator = paramIterator;
/* 219 */       this.scope = paramScriptable;
/*     */     }
/*     */ 
/*     */     public Object next() {
/* 223 */       if (!this.iterator.hasNext())
/*     */       {
/* 225 */         throw new JavaScriptException(NativeIterator.getStopIterationObject(this.scope), null, 0);
/*     */       }
/*     */ 
/* 228 */       return this.iterator.next();
/*     */     }
/*     */ 
/*     */     public Object __iterator__(boolean paramBoolean) {
/* 232 */       return this;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeIterator
 * JD-Core Version:    0.6.2
 */