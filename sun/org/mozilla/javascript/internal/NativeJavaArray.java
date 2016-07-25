/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ 
/*     */ public class NativeJavaArray extends NativeJavaObject
/*     */ {
/*     */   Object array;
/*     */   int length;
/*     */   Class<?> cls;
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  60 */     return "JavaArray";
/*     */   }
/*     */ 
/*     */   public static NativeJavaArray wrap(Scriptable paramScriptable, Object paramObject) {
/*  64 */     return new NativeJavaArray(paramScriptable, paramObject);
/*     */   }
/*     */ 
/*     */   public Object unwrap()
/*     */   {
/*  69 */     return this.array;
/*     */   }
/*     */ 
/*     */   public NativeJavaArray(Scriptable paramScriptable, Object paramObject) {
/*  73 */     super(paramScriptable, null, ScriptRuntime.ObjectClass);
/*  74 */     Class localClass = paramObject.getClass();
/*  75 */     if (!localClass.isArray()) {
/*  76 */       throw new RuntimeException("Array expected");
/*     */     }
/*  78 */     this.array = paramObject;
/*  79 */     this.length = Array.getLength(paramObject);
/*  80 */     this.cls = localClass.getComponentType();
/*     */   }
/*     */ 
/*     */   public boolean has(String paramString, Scriptable paramScriptable)
/*     */   {
/*  85 */     return (paramString.equals("length")) || (super.has(paramString, paramScriptable));
/*     */   }
/*     */ 
/*     */   public boolean has(int paramInt, Scriptable paramScriptable)
/*     */   {
/*  90 */     return (0 <= paramInt) && (paramInt < this.length);
/*     */   }
/*     */ 
/*     */   public Object get(String paramString, Scriptable paramScriptable)
/*     */   {
/*  95 */     if (paramString.equals("length"))
/*  96 */       return Integer.valueOf(this.length);
/*  97 */     Object localObject = super.get(paramString, paramScriptable);
/*  98 */     if ((localObject == NOT_FOUND) && (!ScriptableObject.hasProperty(getPrototype(), paramString)))
/*     */     {
/* 101 */       throw Context.reportRuntimeError2("msg.java.member.not.found", this.array.getClass().getName(), paramString);
/*     */     }
/*     */ 
/* 104 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Object get(int paramInt, Scriptable paramScriptable)
/*     */   {
/* 109 */     if ((0 <= paramInt) && (paramInt < this.length)) {
/* 110 */       Context localContext = Context.getContext();
/* 111 */       Object localObject = Array.get(this.array, paramInt);
/* 112 */       return localContext.getWrapFactory().wrap(localContext, this, localObject, this.cls);
/*     */     }
/* 114 */     return Undefined.instance;
/*     */   }
/*     */ 
/*     */   public void put(String paramString, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 120 */     if (!paramString.equals("length"))
/* 121 */       throw Context.reportRuntimeError1("msg.java.array.member.not.found", paramString);
/*     */   }
/*     */ 
/*     */   public void put(int paramInt, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 127 */     if ((0 <= paramInt) && (paramInt < this.length)) {
/* 128 */       Array.set(this.array, paramInt, Context.jsToJava(paramObject, this.cls));
/*     */     }
/*     */     else
/* 131 */       throw Context.reportRuntimeError2("msg.java.array.index.out.of.bounds", String.valueOf(paramInt), String.valueOf(this.length - 1));
/*     */   }
/*     */ 
/*     */   public Object getDefaultValue(Class<?> paramClass)
/*     */   {
/* 139 */     if ((paramClass == null) || (paramClass == ScriptRuntime.StringClass))
/* 140 */       return this.array.toString();
/* 141 */     if (paramClass == ScriptRuntime.BooleanClass)
/* 142 */       return Boolean.TRUE;
/* 143 */     if (paramClass == ScriptRuntime.NumberClass)
/* 144 */       return ScriptRuntime.NaNobj;
/* 145 */     return this;
/*     */   }
/*     */ 
/*     */   public Object[] getIds()
/*     */   {
/* 150 */     Object[] arrayOfObject = new Object[this.length];
/* 151 */     int i = this.length;
/*     */     while (true) { i--; if (i < 0) break;
/* 153 */       arrayOfObject[i] = Integer.valueOf(i); }
/* 154 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public boolean hasInstance(Scriptable paramScriptable)
/*     */   {
/* 159 */     if (!(paramScriptable instanceof Wrapper))
/* 160 */       return false;
/* 161 */     Object localObject = ((Wrapper)paramScriptable).unwrap();
/* 162 */     return this.cls.isInstance(localObject);
/*     */   }
/*     */ 
/*     */   public Scriptable getPrototype()
/*     */   {
/* 167 */     if (this.prototype == null) {
/* 168 */       this.prototype = ScriptableObject.getClassPrototype(getParentScope(), "Array");
/*     */     }
/*     */ 
/* 172 */     return this.prototype;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeJavaArray
 * JD-Core Version:    0.6.2
 */