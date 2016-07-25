/*     */ package java.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import sun.util.ResourceBundleEnumeration;
/*     */ 
/*     */ public class PropertyResourceBundle extends ResourceBundle
/*     */ {
/*     */   private Map<String, Object> lookup;
/*     */ 
/*     */   public PropertyResourceBundle(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 129 */     Properties localProperties = new Properties();
/* 130 */     localProperties.load(paramInputStream);
/* 131 */     this.lookup = new HashMap(localProperties);
/*     */   }
/*     */ 
/*     */   public PropertyResourceBundle(Reader paramReader)
/*     */     throws IOException
/*     */   {
/* 147 */     Properties localProperties = new Properties();
/* 148 */     localProperties.load(paramReader);
/* 149 */     this.lookup = new HashMap(localProperties);
/*     */   }
/*     */ 
/*     */   public Object handleGetObject(String paramString)
/*     */   {
/* 154 */     if (paramString == null) {
/* 155 */       throw new NullPointerException();
/*     */     }
/* 157 */     return this.lookup.get(paramString);
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getKeys()
/*     */   {
/* 169 */     ResourceBundle localResourceBundle = this.parent;
/* 170 */     return new ResourceBundleEnumeration(this.lookup.keySet(), localResourceBundle != null ? localResourceBundle.getKeys() : null);
/*     */   }
/*     */ 
/*     */   protected Set<String> handleKeySet()
/*     */   {
/* 184 */     return this.lookup.keySet();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.PropertyResourceBundle
 * JD-Core Version:    0.6.2
 */