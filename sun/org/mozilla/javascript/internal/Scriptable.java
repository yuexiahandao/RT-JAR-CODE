/*    */ package sun.org.mozilla.javascript.internal;
/*    */ 
/*    */ public abstract interface Scriptable
/*    */ {
/* 76 */   public static final Object NOT_FOUND = UniqueTag.NOT_FOUND;
/*    */ 
/*    */   public abstract String getClassName();
/*    */ 
/*    */   public abstract Object get(String paramString, Scriptable paramScriptable);
/*    */ 
/*    */   public abstract Object get(int paramInt, Scriptable paramScriptable);
/*    */ 
/*    */   public abstract boolean has(String paramString, Scriptable paramScriptable);
/*    */ 
/*    */   public abstract boolean has(int paramInt, Scriptable paramScriptable);
/*    */ 
/*    */   public abstract void put(String paramString, Scriptable paramScriptable, Object paramObject);
/*    */ 
/*    */   public abstract void put(int paramInt, Scriptable paramScriptable, Object paramObject);
/*    */ 
/*    */   public abstract void delete(String paramString);
/*    */ 
/*    */   public abstract void delete(int paramInt);
/*    */ 
/*    */   public abstract Scriptable getPrototype();
/*    */ 
/*    */   public abstract void setPrototype(Scriptable paramScriptable);
/*    */ 
/*    */   public abstract Scriptable getParentScope();
/*    */ 
/*    */   public abstract void setParentScope(Scriptable paramScriptable);
/*    */ 
/*    */   public abstract Object[] getIds();
/*    */ 
/*    */   public abstract Object getDefaultValue(Class<?> paramClass);
/*    */ 
/*    */   public abstract boolean hasInstance(Scriptable paramScriptable);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.Scriptable
 * JD-Core Version:    0.6.2
 */