/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public class IdFunctionObject extends BaseFunction
/*     */ {
/*     */   private final IdFunctionCall idcall;
/*     */   private final Object tag;
/*     */   private final int methodId;
/*     */   private int arity;
/*     */   private boolean useCallAsConstructor;
/*     */   private String functionName;
/*     */ 
/*     */   public IdFunctionObject(IdFunctionCall paramIdFunctionCall, Object paramObject, int paramInt1, int paramInt2)
/*     */   {
/*  47 */     if (paramInt2 < 0) {
/*  48 */       throw new IllegalArgumentException();
/*     */     }
/*  50 */     this.idcall = paramIdFunctionCall;
/*  51 */     this.tag = paramObject;
/*  52 */     this.methodId = paramInt1;
/*  53 */     this.arity = paramInt2;
/*  54 */     if (paramInt2 < 0) throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public IdFunctionObject(IdFunctionCall paramIdFunctionCall, Object paramObject, int paramInt1, String paramString, int paramInt2, Scriptable paramScriptable)
/*     */   {
/*  60 */     super(paramScriptable, null);
/*     */ 
/*  62 */     if (paramInt2 < 0)
/*  63 */       throw new IllegalArgumentException();
/*  64 */     if (paramString == null) {
/*  65 */       throw new IllegalArgumentException();
/*     */     }
/*  67 */     this.idcall = paramIdFunctionCall;
/*  68 */     this.tag = paramObject;
/*  69 */     this.methodId = paramInt1;
/*  70 */     this.arity = paramInt2;
/*  71 */     this.functionName = paramString;
/*     */   }
/*     */ 
/*     */   public void initFunction(String paramString, Scriptable paramScriptable)
/*     */   {
/*  76 */     if (paramString == null) throw new IllegalArgumentException();
/*  77 */     if (paramScriptable == null) throw new IllegalArgumentException();
/*  78 */     this.functionName = paramString;
/*  79 */     setParentScope(paramScriptable);
/*     */   }
/*     */ 
/*     */   public final boolean hasTag(Object paramObject)
/*     */   {
/*  84 */     return this.tag == paramObject;
/*     */   }
/*     */ 
/*     */   public final int methodId()
/*     */   {
/*  89 */     return this.methodId;
/*     */   }
/*     */ 
/*     */   public final void markAsConstructor(Scriptable paramScriptable)
/*     */   {
/*  94 */     this.useCallAsConstructor = true;
/*  95 */     setImmunePrototypeProperty(paramScriptable);
/*     */   }
/*     */ 
/*     */   public final void addAsProperty(Scriptable paramScriptable)
/*     */   {
/* 100 */     ScriptableObject.defineProperty(paramScriptable, this.functionName, this, 2);
/*     */   }
/*     */ 
/*     */   public void exportAsScopeProperty()
/*     */   {
/* 106 */     addAsProperty(getParentScope());
/*     */   }
/*     */ 
/*     */   public Scriptable getPrototype()
/*     */   {
/* 114 */     Scriptable localScriptable = super.getPrototype();
/* 115 */     if (localScriptable == null) {
/* 116 */       localScriptable = getFunctionPrototype(getParentScope());
/* 117 */       setPrototype(localScriptable);
/*     */     }
/* 119 */     return localScriptable;
/*     */   }
/*     */ 
/*     */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 126 */     return this.idcall.execIdCall(this, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public Scriptable createObject(Context paramContext, Scriptable paramScriptable)
/*     */   {
/* 132 */     if (this.useCallAsConstructor) {
/* 133 */       return null;
/*     */     }
/*     */ 
/* 139 */     throw ScriptRuntime.typeError1("msg.not.ctor", this.functionName);
/*     */   }
/*     */ 
/*     */   String decompile(int paramInt1, int paramInt2)
/*     */   {
/* 145 */     StringBuffer localStringBuffer = new StringBuffer();
/* 146 */     int i = 0 != (paramInt2 & 0x1) ? 1 : 0;
/* 147 */     if (i == 0) {
/* 148 */       localStringBuffer.append("function ");
/* 149 */       localStringBuffer.append(getFunctionName());
/* 150 */       localStringBuffer.append("() { ");
/*     */     }
/* 152 */     localStringBuffer.append("[native code for ");
/* 153 */     if ((this.idcall instanceof Scriptable)) {
/* 154 */       Scriptable localScriptable = (Scriptable)this.idcall;
/* 155 */       localStringBuffer.append(localScriptable.getClassName());
/* 156 */       localStringBuffer.append('.');
/*     */     }
/* 158 */     localStringBuffer.append(getFunctionName());
/* 159 */     localStringBuffer.append(", arity=");
/* 160 */     localStringBuffer.append(getArity());
/* 161 */     localStringBuffer.append(i != 0 ? "]\n" : "] }\n");
/* 162 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public int getArity()
/*     */   {
/* 168 */     return this.arity;
/*     */   }
/*     */ 
/*     */   public int getLength() {
/* 172 */     return getArity();
/*     */   }
/*     */ 
/*     */   public String getFunctionName()
/*     */   {
/* 177 */     return this.functionName == null ? "" : this.functionName;
/*     */   }
/*     */ 
/*     */   public final RuntimeException unknown()
/*     */   {
/* 183 */     return new IllegalArgumentException("BAD FUNCTION ID=" + this.methodId + " MASTER=" + this.idcall);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.IdFunctionObject
 * JD-Core Version:    0.6.2
 */