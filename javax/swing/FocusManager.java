/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.DefaultFocusTraversalPolicy;
/*     */ import java.awt.DefaultKeyboardFocusManager;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ 
/*     */ public abstract class FocusManager extends DefaultKeyboardFocusManager
/*     */ {
/*     */   public static final String FOCUS_MANAGER_CLASS_PROPERTY = "FocusManagerClassName";
/*  64 */   private static boolean enabled = true;
/*     */ 
/*     */   public static FocusManager getCurrentManager()
/*     */   {
/*  74 */     KeyboardFocusManager localKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/*     */ 
/*  76 */     if ((localKeyboardFocusManager instanceof FocusManager)) {
/*  77 */       return (FocusManager)localKeyboardFocusManager;
/*     */     }
/*  79 */     return new DelegatingDefaultFocusManager(localKeyboardFocusManager);
/*     */   }
/*     */ 
/*     */   public static void setCurrentManager(FocusManager paramFocusManager)
/*     */     throws SecurityException
/*     */   {
/* 112 */     FocusManager localFocusManager = (paramFocusManager instanceof DelegatingDefaultFocusManager) ? ((DelegatingDefaultFocusManager)paramFocusManager).getDelegate() : paramFocusManager;
/*     */ 
/* 116 */     KeyboardFocusManager.setCurrentKeyboardFocusManager(localFocusManager);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static void disableSwingFocusManager()
/*     */   {
/* 131 */     if (enabled) {
/* 132 */       enabled = false;
/* 133 */       KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalPolicy(new DefaultFocusTraversalPolicy());
/*     */     }
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static boolean isFocusManagerEnabled()
/*     */   {
/* 149 */     return enabled;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.FocusManager
 * JD-Core Version:    0.6.2
 */