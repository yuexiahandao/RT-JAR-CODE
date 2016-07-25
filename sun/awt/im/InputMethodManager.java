/*     */ package sun.awt.im;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public abstract class InputMethodManager
/*     */ {
/*     */   private static final String threadName = "AWT-InputMethodManager";
/* 141 */   private static final Object LOCK = new Object();
/*     */   private static InputMethodManager inputMethodManager;
/*     */ 
/*     */   public static final InputMethodManager getInstance()
/*     */   {
/* 156 */     if (inputMethodManager != null) {
/* 157 */       return inputMethodManager;
/*     */     }
/* 159 */     synchronized (LOCK) {
/* 160 */       if (inputMethodManager == null) {
/* 161 */         ExecutableInputMethodManager localExecutableInputMethodManager = new ExecutableInputMethodManager();
/*     */ 
/* 166 */         if (localExecutableInputMethodManager.hasMultipleInputMethods()) {
/* 167 */           localExecutableInputMethodManager.initialize();
/* 168 */           Thread localThread = new Thread(localExecutableInputMethodManager, "AWT-InputMethodManager");
/* 169 */           localThread.setDaemon(true);
/* 170 */           localThread.setPriority(6);
/* 171 */           localThread.start();
/*     */         }
/* 173 */         inputMethodManager = localExecutableInputMethodManager;
/*     */       }
/*     */     }
/* 176 */     return inputMethodManager;
/*     */   }
/*     */ 
/*     */   public abstract String getTriggerMenuString();
/*     */ 
/*     */   public abstract void notifyChangeRequest(Component paramComponent);
/*     */ 
/*     */   public abstract void notifyChangeRequestByHotKey(Component paramComponent);
/*     */ 
/*     */   abstract void setInputContext(InputContext paramInputContext);
/*     */ 
/*     */   abstract InputMethodLocator findInputMethod(Locale paramLocale);
/*     */ 
/*     */   abstract Locale getDefaultKeyboardLocale();
/*     */ 
/*     */   abstract boolean hasMultipleInputMethods();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.im.InputMethodManager
 * JD-Core Version:    0.6.2
 */