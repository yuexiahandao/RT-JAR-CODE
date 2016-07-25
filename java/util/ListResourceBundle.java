/*     */ package java.util;
/*     */ 
/*     */ import sun.util.ResourceBundleEnumeration;
/*     */ 
/*     */ public abstract class ListResourceBundle extends ResourceBundle
/*     */ {
/* 203 */   private Map<String, Object> lookup = null;
/*     */ 
/*     */   public final Object handleGetObject(String paramString)
/*     */   {
/* 123 */     if (this.lookup == null) {
/* 124 */       loadLookup();
/*     */     }
/* 126 */     if (paramString == null) {
/* 127 */       throw new NullPointerException();
/*     */     }
/* 129 */     return this.lookup.get(paramString);
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getKeys()
/*     */   {
/* 142 */     if (this.lookup == null) {
/* 143 */       loadLookup();
/*     */     }
/*     */ 
/* 146 */     ResourceBundle localResourceBundle = this.parent;
/* 147 */     return new ResourceBundleEnumeration(this.lookup.keySet(), localResourceBundle != null ? localResourceBundle.getKeys() : null);
/*     */   }
/*     */ 
/*     */   protected Set<String> handleKeySet()
/*     */   {
/* 161 */     if (this.lookup == null) {
/* 162 */       loadLookup();
/*     */     }
/* 164 */     return this.lookup.keySet();
/*     */   }
/*     */ 
/*     */   protected abstract Object[][] getContents();
/*     */ 
/*     */   private synchronized void loadLookup()
/*     */   {
/* 186 */     if (this.lookup != null) {
/* 187 */       return;
/*     */     }
/* 189 */     Object[][] arrayOfObject = getContents();
/* 190 */     HashMap localHashMap = new HashMap(arrayOfObject.length);
/* 191 */     for (int i = 0; i < arrayOfObject.length; i++)
/*     */     {
/* 193 */       String str = (String)arrayOfObject[i][0];
/* 194 */       Object localObject = arrayOfObject[i][1];
/* 195 */       if ((str == null) || (localObject == null)) {
/* 196 */         throw new NullPointerException();
/*     */       }
/* 198 */       localHashMap.put(str, localObject);
/*     */     }
/* 200 */     this.lookup = localHashMap;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.ListResourceBundle
 * JD-Core Version:    0.6.2
 */