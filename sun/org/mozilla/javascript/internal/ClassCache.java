/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class ClassCache
/*     */ {
/*  55 */   private static final Object AKEY = "ClassCache";
/*  56 */   private volatile boolean cachingIsEnabled = true;
/*     */   private transient HashMap<Class<?>, JavaMembers> classTable;
/*     */   private int generatedClassSerial;
/*     */   private Scriptable associatedScope;
/*     */ 
/*     */   public static ClassCache get(Scriptable paramScriptable)
/*     */   {
/*  76 */     ClassCache localClassCache = (ClassCache)ScriptableObject.getTopScopeValue(paramScriptable, AKEY);
/*     */ 
/*  78 */     if (localClassCache == null) {
/*  79 */       throw new RuntimeException("Can't find top level scope for ClassCache.get");
/*     */     }
/*     */ 
/*  82 */     return localClassCache;
/*     */   }
/*     */ 
/*     */   public boolean associate(ScriptableObject paramScriptableObject)
/*     */   {
/*  98 */     if (paramScriptableObject.getParentScope() != null)
/*     */     {
/* 100 */       throw new IllegalArgumentException();
/*     */     }
/* 102 */     if (this == paramScriptableObject.associateValue(AKEY, this)) {
/* 103 */       this.associatedScope = paramScriptableObject;
/* 104 */       return true;
/*     */     }
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized void clearCaches()
/*     */   {
/* 114 */     this.classTable = null;
/*     */   }
/*     */ 
/*     */   public final boolean isCachingEnabled()
/*     */   {
/* 123 */     return this.cachingIsEnabled;
/*     */   }
/*     */ 
/*     */   public synchronized void setCachingEnabled(boolean paramBoolean)
/*     */   {
/* 146 */     if (paramBoolean == this.cachingIsEnabled)
/* 147 */       return;
/* 148 */     if (!paramBoolean)
/* 149 */       clearCaches();
/* 150 */     this.cachingIsEnabled = paramBoolean;
/*     */   }
/*     */ 
/*     */   Map<Class<?>, JavaMembers> getClassCacheMap()
/*     */   {
/* 157 */     if (this.classTable == null) {
/* 158 */       this.classTable = new HashMap();
/*     */     }
/* 160 */     return this.classTable;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public boolean isInvokerOptimizationEnabled()
/*     */   {
/* 170 */     return false;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public synchronized void setInvokerOptimizationEnabled(boolean paramBoolean)
/*     */   {
/*     */   }
/*     */ 
/*     */   public final synchronized int newClassSerialNumber()
/*     */   {
/* 192 */     return ++this.generatedClassSerial;
/*     */   }
/*     */ 
/*     */   Scriptable getAssociatedScope() {
/* 196 */     return this.associatedScope;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ClassCache
 * JD-Core Version:    0.6.2
 */