/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public class NativeWith
/*     */   implements Scriptable, IdFunctionCall
/*     */ {
/* 196 */   private static final Object FTAG = "With";
/*     */   private static final int Id_constructor = 1;
/*     */   protected Scriptable prototype;
/*     */   protected Scriptable parent;
/*     */ 
/*     */   static void init(Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/*  52 */     NativeWith localNativeWith = new NativeWith();
/*     */ 
/*  54 */     localNativeWith.setParentScope(paramScriptable);
/*  55 */     localNativeWith.setPrototype(ScriptableObject.getObjectPrototype(paramScriptable));
/*     */ 
/*  57 */     IdFunctionObject localIdFunctionObject = new IdFunctionObject(localNativeWith, FTAG, 1, "With", 0, paramScriptable);
/*     */ 
/*  59 */     localIdFunctionObject.markAsConstructor(localNativeWith);
/*  60 */     if (paramBoolean) {
/*  61 */       localIdFunctionObject.sealObject();
/*     */     }
/*  63 */     localIdFunctionObject.exportAsScopeProperty();
/*     */   }
/*     */ 
/*     */   private NativeWith() {
/*     */   }
/*     */ 
/*     */   protected NativeWith(Scriptable paramScriptable1, Scriptable paramScriptable2) {
/*  70 */     this.parent = paramScriptable1;
/*  71 */     this.prototype = paramScriptable2;
/*     */   }
/*     */ 
/*     */   public String getClassName() {
/*  75 */     return "With";
/*     */   }
/*     */ 
/*     */   public boolean has(String paramString, Scriptable paramScriptable)
/*     */   {
/*  80 */     return this.prototype.has(paramString, this.prototype);
/*     */   }
/*     */ 
/*     */   public boolean has(int paramInt, Scriptable paramScriptable)
/*     */   {
/*  85 */     return this.prototype.has(paramInt, this.prototype);
/*     */   }
/*     */ 
/*     */   public Object get(String paramString, Scriptable paramScriptable)
/*     */   {
/*  90 */     if (paramScriptable == this)
/*  91 */       paramScriptable = this.prototype;
/*  92 */     return this.prototype.get(paramString, paramScriptable);
/*     */   }
/*     */ 
/*     */   public Object get(int paramInt, Scriptable paramScriptable)
/*     */   {
/*  97 */     if (paramScriptable == this)
/*  98 */       paramScriptable = this.prototype;
/*  99 */     return this.prototype.get(paramInt, paramScriptable);
/*     */   }
/*     */ 
/*     */   public void put(String paramString, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 104 */     if (paramScriptable == this)
/* 105 */       paramScriptable = this.prototype;
/* 106 */     this.prototype.put(paramString, paramScriptable, paramObject);
/*     */   }
/*     */ 
/*     */   public void put(int paramInt, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 111 */     if (paramScriptable == this)
/* 112 */       paramScriptable = this.prototype;
/* 113 */     this.prototype.put(paramInt, paramScriptable, paramObject);
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */   {
/* 118 */     this.prototype.delete(paramString);
/*     */   }
/*     */ 
/*     */   public void delete(int paramInt)
/*     */   {
/* 123 */     this.prototype.delete(paramInt);
/*     */   }
/*     */ 
/*     */   public Scriptable getPrototype() {
/* 127 */     return this.prototype;
/*     */   }
/*     */ 
/*     */   public void setPrototype(Scriptable paramScriptable) {
/* 131 */     this.prototype = paramScriptable;
/*     */   }
/*     */ 
/*     */   public Scriptable getParentScope() {
/* 135 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public void setParentScope(Scriptable paramScriptable) {
/* 139 */     this.parent = paramScriptable;
/*     */   }
/*     */ 
/*     */   public Object[] getIds() {
/* 143 */     return this.prototype.getIds();
/*     */   }
/*     */ 
/*     */   public Object getDefaultValue(Class<?> paramClass) {
/* 147 */     return this.prototype.getDefaultValue(paramClass);
/*     */   }
/*     */ 
/*     */   public boolean hasInstance(Scriptable paramScriptable) {
/* 151 */     return this.prototype.hasInstance(paramScriptable);
/*     */   }
/*     */ 
/*     */   protected Object updateDotQuery(boolean paramBoolean)
/*     */   {
/* 160 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 166 */     if ((paramIdFunctionObject.hasTag(FTAG)) && 
/* 167 */       (paramIdFunctionObject.methodId() == 1)) {
/* 168 */       throw Context.reportRuntimeError1("msg.cant.call.indirect", "With");
/*     */     }
/*     */ 
/* 171 */     throw paramIdFunctionObject.unknown();
/*     */   }
/*     */ 
/*     */   static boolean isWithFunction(Object paramObject)
/*     */   {
/* 176 */     if ((paramObject instanceof IdFunctionObject)) {
/* 177 */       IdFunctionObject localIdFunctionObject = (IdFunctionObject)paramObject;
/* 178 */       return (localIdFunctionObject.hasTag(FTAG)) && (localIdFunctionObject.methodId() == 1);
/*     */     }
/* 180 */     return false;
/*     */   }
/*     */ 
/*     */   static Object newWithSpecial(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*     */   {
/* 185 */     ScriptRuntime.checkDeprecated(paramContext, "With");
/* 186 */     paramScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
/* 187 */     NativeWith localNativeWith = new NativeWith();
/* 188 */     localNativeWith.setPrototype(paramArrayOfObject.length == 0 ? ScriptableObject.getClassPrototype(paramScriptable, "Object") : ScriptRuntime.toObject(paramContext, paramScriptable, paramArrayOfObject[0]));
/*     */ 
/* 192 */     localNativeWith.setParentScope(paramScriptable);
/* 193 */     return localNativeWith;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeWith
 * JD-Core Version:    0.6.2
 */