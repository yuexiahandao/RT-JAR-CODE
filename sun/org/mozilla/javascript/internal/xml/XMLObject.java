/*     */ package sun.org.mozilla.javascript.internal.xml;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.IdScriptableObject;
/*     */ import sun.org.mozilla.javascript.internal.NativeWith;
/*     */ import sun.org.mozilla.javascript.internal.Ref;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ 
/*     */ public abstract class XMLObject extends IdScriptableObject
/*     */ {
/*     */   public XMLObject()
/*     */   {
/*     */   }
/*     */ 
/*     */   public XMLObject(Scriptable paramScriptable1, Scriptable paramScriptable2)
/*     */   {
/*  58 */     super(paramScriptable1, paramScriptable2);
/*     */   }
/*     */ 
/*     */   public abstract boolean ecmaHas(Context paramContext, Object paramObject);
/*     */ 
/*     */   public abstract Object ecmaGet(Context paramContext, Object paramObject);
/*     */ 
/*     */   public abstract void ecmaPut(Context paramContext, Object paramObject1, Object paramObject2);
/*     */ 
/*     */   public abstract boolean ecmaDelete(Context paramContext, Object paramObject);
/*     */ 
/*     */   public abstract Scriptable getExtraMethodSource(Context paramContext);
/*     */ 
/*     */   public abstract Ref memberRef(Context paramContext, Object paramObject, int paramInt);
/*     */ 
/*     */   public abstract Ref memberRef(Context paramContext, Object paramObject1, Object paramObject2, int paramInt);
/*     */ 
/*     */   public abstract NativeWith enterWith(Scriptable paramScriptable);
/*     */ 
/*     */   public abstract NativeWith enterDotQuery(Scriptable paramScriptable);
/*     */ 
/*     */   public Object addValues(Context paramContext, boolean paramBoolean, Object paramObject)
/*     */   {
/* 125 */     return Scriptable.NOT_FOUND;
/*     */   }
/*     */ 
/*     */   public String getTypeOf()
/*     */   {
/* 136 */     return avoidObjectDetection() ? "undefined" : "xml";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.xml.XMLObject
 * JD-Core Version:    0.6.2
 */