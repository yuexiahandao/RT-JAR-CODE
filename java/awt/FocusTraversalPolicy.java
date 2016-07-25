/*     */ package java.awt;
/*     */ 
/*     */ public abstract class FocusTraversalPolicy
/*     */ {
/*     */   public abstract Component getComponentAfter(Container paramContainer, Component paramComponent);
/*     */ 
/*     */   public abstract Component getComponentBefore(Container paramContainer, Component paramComponent);
/*     */ 
/*     */   public abstract Component getFirstComponent(Container paramContainer);
/*     */ 
/*     */   public abstract Component getLastComponent(Container paramContainer);
/*     */ 
/*     */   public abstract Component getDefaultComponent(Container paramContainer);
/*     */ 
/*     */   public Component getInitialComponent(Window paramWindow)
/*     */   {
/* 166 */     if (paramWindow == null) {
/* 167 */       throw new IllegalArgumentException("window cannot be equal to null.");
/*     */     }
/* 169 */     Object localObject = getDefaultComponent(paramWindow);
/* 170 */     if ((localObject == null) && (paramWindow.isFocusableWindow())) {
/* 171 */       localObject = paramWindow;
/*     */     }
/* 173 */     return localObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.FocusTraversalPolicy
 * JD-Core Version:    0.6.2
 */