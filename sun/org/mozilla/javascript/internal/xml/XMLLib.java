/*     */ package sun.org.mozilla.javascript.internal.xml;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.Ref;
/*     */ import sun.org.mozilla.javascript.internal.ScriptRuntime;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.ScriptableObject;
/*     */ 
/*     */ public abstract class XMLLib
/*     */ {
/*  45 */   private static final Object XML_LIB_KEY = new Object();
/*     */ 
/*     */   public static XMLLib extractFromScopeOrNull(Scriptable paramScriptable)
/*     */   {
/*  71 */     ScriptableObject localScriptableObject = ScriptRuntime.getLibraryScopeOrNull(paramScriptable);
/*  72 */     if (localScriptableObject == null)
/*     */     {
/*  74 */       return null;
/*     */     }
/*     */ 
/*  79 */     ScriptableObject.getProperty(localScriptableObject, "XML");
/*     */ 
/*  81 */     return (XMLLib)localScriptableObject.getAssociatedValue(XML_LIB_KEY);
/*     */   }
/*     */ 
/*     */   public static XMLLib extractFromScope(Scriptable paramScriptable)
/*     */   {
/*  86 */     XMLLib localXMLLib = extractFromScopeOrNull(paramScriptable);
/*  87 */     if (localXMLLib != null) {
/*  88 */       return localXMLLib;
/*     */     }
/*  90 */     String str = ScriptRuntime.getMessage0("msg.XML.not.available");
/*  91 */     throw Context.reportRuntimeError(str);
/*     */   }
/*     */ 
/*     */   protected final XMLLib bindToScope(Scriptable paramScriptable)
/*     */   {
/*  96 */     ScriptableObject localScriptableObject = ScriptRuntime.getLibraryScopeOrNull(paramScriptable);
/*  97 */     if (localScriptableObject == null)
/*     */     {
/*  99 */       throw new IllegalStateException();
/*     */     }
/* 101 */     return (XMLLib)localScriptableObject.associateValue(XML_LIB_KEY, this);
/*     */   }
/*     */ 
/*     */   public abstract boolean isXMLName(Context paramContext, Object paramObject);
/*     */ 
/*     */   public abstract Ref nameRef(Context paramContext, Object paramObject, Scriptable paramScriptable, int paramInt);
/*     */ 
/*     */   public abstract Ref nameRef(Context paramContext, Object paramObject1, Object paramObject2, Scriptable paramScriptable, int paramInt);
/*     */ 
/*     */   public abstract String escapeAttributeValue(Object paramObject);
/*     */ 
/*     */   public abstract String escapeTextValue(Object paramObject);
/*     */ 
/*     */   public abstract Object toDefaultXmlNamespace(Context paramContext, Object paramObject);
/*     */ 
/*     */   public void setIgnoreComments(boolean paramBoolean)
/*     */   {
/* 135 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void setIgnoreWhitespace(boolean paramBoolean) {
/* 139 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void setIgnoreProcessingInstructions(boolean paramBoolean) {
/* 143 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void setPrettyPrinting(boolean paramBoolean) {
/* 147 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void setPrettyIndent(int paramInt) {
/* 151 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public boolean isIgnoreComments() {
/* 155 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public boolean isIgnoreProcessingInstructions() {
/* 159 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public boolean isIgnoreWhitespace() {
/* 163 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public boolean isPrettyPrinting() {
/* 167 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public int getPrettyIndent() {
/* 171 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public static abstract class Factory
/*     */   {
/*     */     public static Factory create(String paramString)
/*     */     {
/*  58 */       return new Factory()
/*     */       {
/*     */         public String getImplementationClassName() {
/*  61 */           return this.val$className;
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*     */     public abstract String getImplementationClassName();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.xml.XMLLib
 * JD-Core Version:    0.6.2
 */