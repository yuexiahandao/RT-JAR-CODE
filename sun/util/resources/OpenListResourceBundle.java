/*     */ package sun.util.resources;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import sun.util.ResourceBundleEnumeration;
/*     */ 
/*     */ public abstract class OpenListResourceBundle extends ResourceBundle
/*     */ {
/* 147 */   private Map lookup = null;
/*     */ 
/*     */   public Object handleGetObject(String paramString)
/*     */   {
/*  70 */     if (paramString == null) {
/*  71 */       throw new NullPointerException();
/*     */     }
/*     */ 
/*  74 */     loadLookupTablesIfNecessary();
/*  75 */     return this.lookup.get(paramString);
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getKeys()
/*     */   {
/*  82 */     ResourceBundle localResourceBundle = this.parent;
/*  83 */     return new ResourceBundleEnumeration(handleGetKeys(), localResourceBundle != null ? localResourceBundle.getKeys() : null);
/*     */   }
/*     */ 
/*     */   public Set<String> handleGetKeys()
/*     */   {
/*  91 */     loadLookupTablesIfNecessary();
/*     */ 
/*  93 */     return this.lookup.keySet();
/*     */   }
/*     */ 
/*     */   public OpenListResourceBundle getParent()
/*     */   {
/* 100 */     return (OpenListResourceBundle)this.parent;
/*     */   }
/*     */ 
/*     */   protected abstract Object[][] getContents();
/*     */ 
/*     */   void loadLookupTablesIfNecessary()
/*     */   {
/* 112 */     if (this.lookup == null)
/* 113 */       loadLookup();
/*     */   }
/*     */ 
/*     */   private synchronized void loadLookup()
/*     */   {
/* 122 */     if (this.lookup != null) {
/* 123 */       return;
/*     */     }
/* 125 */     Object[][] arrayOfObject = getContents();
/* 126 */     Map localMap = createMap(arrayOfObject.length);
/* 127 */     for (int i = 0; i < arrayOfObject.length; i++)
/*     */     {
/* 129 */       String str = (String)arrayOfObject[i][0];
/* 130 */       Object localObject = arrayOfObject[i][1];
/* 131 */       if ((str == null) || (localObject == null)) {
/* 132 */         throw new NullPointerException();
/*     */       }
/* 134 */       localMap.put(str, localObject);
/*     */     }
/* 136 */     this.lookup = localMap;
/*     */   }
/*     */ 
/*     */   protected Map createMap(int paramInt)
/*     */   {
/* 144 */     return new HashMap(paramInt);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.resources.OpenListResourceBundle
 * JD-Core Version:    0.6.2
 */