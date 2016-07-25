/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Container;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public abstract class LayoutStyle
/*     */ {
/*     */   public static void setInstance(LayoutStyle paramLayoutStyle)
/*     */   {
/*  52 */     synchronized (LayoutStyle.class) {
/*  53 */       if (paramLayoutStyle == null) {
/*  54 */         AppContext.getAppContext().remove(LayoutStyle.class);
/*     */       }
/*     */       else
/*  57 */         AppContext.getAppContext().put(LayoutStyle.class, paramLayoutStyle);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static LayoutStyle getInstance()
/*     */   {
/*     */     LayoutStyle localLayoutStyle;
/*  72 */     synchronized (LayoutStyle.class) {
/*  73 */       localLayoutStyle = (LayoutStyle)AppContext.getAppContext().get(LayoutStyle.class);
/*     */     }
/*     */ 
/*  76 */     if (localLayoutStyle == null) {
/*  77 */       return UIManager.getLookAndFeel().getLayoutStyle();
/*     */     }
/*  79 */     return localLayoutStyle;
/*     */   }
/*     */ 
/*     */   public abstract int getPreferredGap(JComponent paramJComponent1, JComponent paramJComponent2, ComponentPlacement paramComponentPlacement, int paramInt, Container paramContainer);
/*     */ 
/*     */   public abstract int getContainerGap(JComponent paramJComponent, int paramInt, Container paramContainer);
/*     */ 
/*     */   public static enum ComponentPlacement
/*     */   {
/* 103 */     RELATED, 
/*     */ 
/* 111 */     UNRELATED, 
/*     */ 
/* 123 */     INDENT;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.LayoutStyle
 * JD-Core Version:    0.6.2
 */