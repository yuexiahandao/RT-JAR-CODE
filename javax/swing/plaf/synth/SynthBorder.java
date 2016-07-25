/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.text.JTextComponent;
/*     */ 
/*     */ class SynthBorder extends AbstractBorder
/*     */   implements UIResource
/*     */ {
/*     */   private SynthUI ui;
/*     */   private Insets insets;
/*     */ 
/*     */   SynthBorder(SynthUI paramSynthUI, Insets paramInsets)
/*     */   {
/*  44 */     this.ui = paramSynthUI;
/*  45 */     this.insets = paramInsets;
/*     */   }
/*     */ 
/*     */   SynthBorder(SynthUI paramSynthUI) {
/*  49 */     this(paramSynthUI, null);
/*     */   }
/*     */ 
/*     */   public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  54 */     JComponent localJComponent = (JComponent)paramComponent;
/*  55 */     SynthContext localSynthContext = this.ui.getContext(localJComponent);
/*  56 */     SynthStyle localSynthStyle = localSynthContext.getStyle();
/*  57 */     if (localSynthStyle == null) {
/*  58 */       if (!$assertionsDisabled) throw new AssertionError("SynthBorder is being used outside after the UI has been uninstalled");
/*     */ 
/*  60 */       return;
/*     */     }
/*  62 */     this.ui.paintBorder(localSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*  63 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */   {
/*  73 */     if (this.insets != null) {
/*  74 */       if (paramInsets == null) {
/*  75 */         paramInsets = new Insets(this.insets.top, this.insets.left, this.insets.bottom, this.insets.right);
/*     */       }
/*     */       else
/*     */       {
/*  79 */         paramInsets.top = this.insets.top;
/*  80 */         paramInsets.bottom = this.insets.bottom;
/*  81 */         paramInsets.left = this.insets.left;
/*  82 */         paramInsets.right = this.insets.right;
/*     */       }
/*     */     }
/*  85 */     else if (paramInsets == null) {
/*  86 */       paramInsets = new Insets(0, 0, 0, 0);
/*     */     }
/*     */     else {
/*  89 */       paramInsets.top = (paramInsets.bottom = paramInsets.left = paramInsets.right = 0);
/*     */     }
/*  91 */     if ((paramComponent instanceof JComponent)) {
/*  92 */       Region localRegion = Region.getRegion((JComponent)paramComponent);
/*  93 */       Insets localInsets = null;
/*  94 */       if (((localRegion == Region.ARROW_BUTTON) || (localRegion == Region.BUTTON) || (localRegion == Region.CHECK_BOX) || (localRegion == Region.CHECK_BOX_MENU_ITEM) || (localRegion == Region.MENU) || (localRegion == Region.MENU_ITEM) || (localRegion == Region.RADIO_BUTTON) || (localRegion == Region.RADIO_BUTTON_MENU_ITEM) || (localRegion == Region.TOGGLE_BUTTON)) && ((paramComponent instanceof AbstractButton)))
/*     */       {
/* 102 */         localInsets = ((AbstractButton)paramComponent).getMargin();
/*     */       }
/* 104 */       else if (((localRegion == Region.EDITOR_PANE) || (localRegion == Region.FORMATTED_TEXT_FIELD) || (localRegion == Region.PASSWORD_FIELD) || (localRegion == Region.TEXT_AREA) || (localRegion == Region.TEXT_FIELD) || (localRegion == Region.TEXT_PANE)) && ((paramComponent instanceof JTextComponent)))
/*     */       {
/* 111 */         localInsets = ((JTextComponent)paramComponent).getMargin();
/*     */       }
/* 113 */       else if ((localRegion == Region.TOOL_BAR) && ((paramComponent instanceof JToolBar))) {
/* 114 */         localInsets = ((JToolBar)paramComponent).getMargin();
/*     */       }
/* 116 */       else if ((localRegion == Region.MENU_BAR) && ((paramComponent instanceof JMenuBar))) {
/* 117 */         localInsets = ((JMenuBar)paramComponent).getMargin();
/*     */       }
/* 119 */       if (localInsets != null) {
/* 120 */         paramInsets.top += localInsets.top;
/* 121 */         paramInsets.bottom += localInsets.bottom;
/* 122 */         paramInsets.left += localInsets.left;
/* 123 */         paramInsets.right += localInsets.right;
/*     */       }
/*     */     }
/* 126 */     return paramInsets;
/*     */   }
/*     */ 
/*     */   public boolean isBorderOpaque()
/*     */   {
/* 134 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthBorder
 * JD-Core Version:    0.6.2
 */