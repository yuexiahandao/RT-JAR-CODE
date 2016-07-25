/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicPopupMenuUI;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class MotifPopupMenuUI extends BasicPopupMenuUI
/*     */ {
/*  63 */   private static Border border = null;
/*  64 */   private Font titleFont = null;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent) {
/*  67 */     return new MotifPopupMenuUI();
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/*  74 */     LayoutManager localLayoutManager = paramJComponent.getLayout();
/*  75 */     Dimension localDimension = localLayoutManager.preferredLayoutSize(paramJComponent);
/*  76 */     String str = ((JPopupMenu)paramJComponent).getLabel();
/*  77 */     if (this.titleFont == null) {
/*  78 */       localObject = UIManager.getLookAndFeelDefaults();
/*  79 */       this.titleFont = ((UIDefaults)localObject).getFont("PopupMenu.font");
/*     */     }
/*  81 */     Object localObject = paramJComponent.getFontMetrics(this.titleFont);
/*  82 */     int i = 0;
/*     */ 
/*  84 */     if (str != null) {
/*  85 */       i += SwingUtilities2.stringWidth(paramJComponent, (FontMetrics)localObject, str);
/*     */     }
/*     */ 
/*  88 */     if (localDimension.width < i) {
/*  89 */       localDimension.width = (i + 8);
/*  90 */       Insets localInsets = paramJComponent.getInsets();
/*  91 */       if (localInsets != null) {
/*  92 */         localDimension.width += localInsets.left + localInsets.right;
/*     */       }
/*  94 */       if (border != null) {
/*  95 */         localInsets = border.getBorderInsets(paramJComponent);
/*  96 */         localDimension.width += localInsets.left + localInsets.right;
/*     */       }
/*     */ 
/*  99 */       return localDimension;
/*     */     }
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */   protected ChangeListener createChangeListener(JPopupMenu paramJPopupMenu) {
/* 105 */     return new ChangeListener() {
/*     */       public void stateChanged(ChangeEvent paramAnonymousChangeEvent) {
/*     */       } } ;
/*     */   }
/*     */ 
/*     */   public boolean isPopupTrigger(MouseEvent paramMouseEvent) {
/* 111 */     return (paramMouseEvent.getID() == 501) && ((paramMouseEvent.getModifiers() & 0x4) != 0);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifPopupMenuUI
 * JD-Core Version:    0.6.2
 */