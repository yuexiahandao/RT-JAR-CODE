/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicScrollPaneUI;
/*     */ 
/*     */ public class MotifScrollPaneUI extends BasicScrollPaneUI
/*     */ {
/*  50 */   private static final Border vsbMarginBorderR = new EmptyBorder(0, 4, 0, 0);
/*  51 */   private static final Border vsbMarginBorderL = new EmptyBorder(0, 0, 0, 4);
/*  52 */   private static final Border hsbMarginBorder = new EmptyBorder(4, 0, 0, 0);
/*     */   private CompoundBorder vsbBorder;
/*     */   private CompoundBorder hsbBorder;
/*     */   private PropertyChangeListener propertyChangeHandler;
/*     */ 
/*     */   protected void installListeners(JScrollPane paramJScrollPane)
/*     */   {
/*  60 */     super.installListeners(paramJScrollPane);
/*  61 */     this.propertyChangeHandler = createPropertyChangeHandler();
/*  62 */     paramJScrollPane.addPropertyChangeListener(this.propertyChangeHandler);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners(JScrollPane paramJScrollPane) {
/*  66 */     super.uninstallListeners(paramJScrollPane);
/*  67 */     paramJScrollPane.removePropertyChangeListener(this.propertyChangeHandler);
/*     */   }
/*     */ 
/*     */   private PropertyChangeListener createPropertyChangeHandler() {
/*  71 */     return new PropertyChangeListener() {
/*     */       public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent) {
/*  73 */         String str = paramAnonymousPropertyChangeEvent.getPropertyName();
/*     */ 
/*  75 */         if (str.equals("componentOrientation")) {
/*  76 */           JScrollPane localJScrollPane = (JScrollPane)paramAnonymousPropertyChangeEvent.getSource();
/*  77 */           JScrollBar localJScrollBar = localJScrollPane.getVerticalScrollBar();
/*  78 */           if ((localJScrollBar != null) && (MotifScrollPaneUI.this.vsbBorder != null) && (localJScrollBar.getBorder() == MotifScrollPaneUI.this.vsbBorder))
/*     */           {
/*  82 */             if (MotifGraphicsUtils.isLeftToRight(localJScrollPane)) {
/*  83 */               MotifScrollPaneUI.this.vsbBorder = new CompoundBorder(MotifScrollPaneUI.vsbMarginBorderR, MotifScrollPaneUI.this.vsbBorder.getInsideBorder());
/*     */             }
/*     */             else {
/*  86 */               MotifScrollPaneUI.this.vsbBorder = new CompoundBorder(MotifScrollPaneUI.vsbMarginBorderL, MotifScrollPaneUI.this.vsbBorder.getInsideBorder());
/*     */             }
/*     */ 
/*  89 */             localJScrollBar.setBorder(MotifScrollPaneUI.this.vsbBorder);
/*     */           }
/*     */         }
/*     */       } } ;
/*     */   }
/*     */ 
/*     */   protected void installDefaults(JScrollPane paramJScrollPane) {
/*  96 */     super.installDefaults(paramJScrollPane);
/*     */ 
/*  98 */     JScrollBar localJScrollBar1 = paramJScrollPane.getVerticalScrollBar();
/*  99 */     if (localJScrollBar1 != null) {
/* 100 */       if (MotifGraphicsUtils.isLeftToRight(paramJScrollPane)) {
/* 101 */         this.vsbBorder = new CompoundBorder(vsbMarginBorderR, localJScrollBar1.getBorder());
/*     */       }
/*     */       else
/*     */       {
/* 105 */         this.vsbBorder = new CompoundBorder(vsbMarginBorderL, localJScrollBar1.getBorder());
/*     */       }
/*     */ 
/* 108 */       localJScrollBar1.setBorder(this.vsbBorder);
/*     */     }
/*     */ 
/* 111 */     JScrollBar localJScrollBar2 = paramJScrollPane.getHorizontalScrollBar();
/* 112 */     if (localJScrollBar2 != null) {
/* 113 */       this.hsbBorder = new CompoundBorder(hsbMarginBorder, localJScrollBar2.getBorder());
/* 114 */       localJScrollBar2.setBorder(this.hsbBorder);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(JScrollPane paramJScrollPane)
/*     */   {
/* 120 */     super.uninstallDefaults(paramJScrollPane);
/*     */ 
/* 122 */     JScrollBar localJScrollBar1 = this.scrollpane.getVerticalScrollBar();
/* 123 */     if (localJScrollBar1 != null) {
/* 124 */       if (localJScrollBar1.getBorder() == this.vsbBorder) {
/* 125 */         localJScrollBar1.setBorder(null);
/*     */       }
/* 127 */       this.vsbBorder = null;
/*     */     }
/*     */ 
/* 130 */     JScrollBar localJScrollBar2 = this.scrollpane.getHorizontalScrollBar();
/* 131 */     if (localJScrollBar2 != null) {
/* 132 */       if (localJScrollBar2.getBorder() == this.hsbBorder) {
/* 133 */         localJScrollBar2.setBorder(null);
/*     */       }
/* 135 */       this.hsbBorder = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 141 */     return new MotifScrollPaneUI();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifScrollPaneUI
 * JD-Core Version:    0.6.2
 */