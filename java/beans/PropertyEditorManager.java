/*     */ package java.beans;
/*     */ 
/*     */ import com.sun.beans.finder.PropertyEditorFinder;
/*     */ 
/*     */ public class PropertyEditorManager
/*     */ {
/*     */   public static void registerEditor(Class<?> paramClass1, Class<?> paramClass2)
/*     */   {
/*  75 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  76 */     if (localSecurityManager != null) {
/*  77 */       localSecurityManager.checkPropertiesAccess();
/*     */     }
/*  79 */     ThreadGroupContext.getContext().getPropertyEditorFinder().register(paramClass1, paramClass2);
/*     */   }
/*     */ 
/*     */   public static PropertyEditor findEditor(Class<?> paramClass)
/*     */   {
/*  90 */     return ThreadGroupContext.getContext().getPropertyEditorFinder().find(paramClass);
/*     */   }
/*     */ 
/*     */   public static String[] getEditorSearchPath()
/*     */   {
/* 102 */     return ThreadGroupContext.getContext().getPropertyEditorFinder().getPackages();
/*     */   }
/*     */ 
/*     */   public static void setEditorSearchPath(String[] paramArrayOfString)
/*     */   {
/* 119 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 120 */     if (localSecurityManager != null) {
/* 121 */       localSecurityManager.checkPropertiesAccess();
/*     */     }
/* 123 */     ThreadGroupContext.getContext().getPropertyEditorFinder().setPackages(paramArrayOfString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.PropertyEditorManager
 * JD-Core Version:    0.6.2
 */