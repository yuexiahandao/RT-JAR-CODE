/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.plaf.basic.BasicSplitPaneDivider;
/*     */ import javax.swing.plaf.basic.BasicSplitPaneUI;
/*     */ import sun.swing.DefaultLookup;
/*     */ 
/*     */ class SynthSplitPaneDivider extends BasicSplitPaneDivider
/*     */ {
/*     */   public SynthSplitPaneDivider(BasicSplitPaneUI paramBasicSplitPaneUI)
/*     */   {
/*  41 */     super(paramBasicSplitPaneUI);
/*     */   }
/*     */ 
/*     */   protected void setMouseOver(boolean paramBoolean) {
/*  45 */     if (isMouseOver() != paramBoolean) {
/*  46 */       repaint();
/*     */     }
/*  48 */     super.setMouseOver(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/*  52 */     super.propertyChange(paramPropertyChangeEvent);
/*  53 */     if ((paramPropertyChangeEvent.getSource() == this.splitPane) && 
/*  54 */       (paramPropertyChangeEvent.getPropertyName() == "orientation")) {
/*  55 */       if ((this.leftButton instanceof SynthArrowButton)) {
/*  56 */         ((SynthArrowButton)this.leftButton).setDirection(mapDirection(true));
/*     */       }
/*     */ 
/*  59 */       if ((this.rightButton instanceof SynthArrowButton))
/*  60 */         ((SynthArrowButton)this.rightButton).setDirection(mapDirection(false));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics)
/*     */   {
/*  68 */     Graphics localGraphics1 = paramGraphics.create();
/*     */ 
/*  70 */     SynthContext localSynthContext = ((SynthSplitPaneUI)this.splitPaneUI).getContext(this.splitPane, Region.SPLIT_PANE_DIVIDER);
/*     */ 
/*  72 */     Rectangle localRectangle1 = getBounds();
/*  73 */     localRectangle1.x = (localRectangle1.y = 0);
/*  74 */     SynthLookAndFeel.updateSubregion(localSynthContext, paramGraphics, localRectangle1);
/*  75 */     localSynthContext.getPainter().paintSplitPaneDividerBackground(localSynthContext, paramGraphics, 0, 0, localRectangle1.width, localRectangle1.height, this.splitPane.getOrientation());
/*     */ 
/*  79 */     Object localObject = null;
/*     */ 
/*  81 */     localSynthContext.getPainter().paintSplitPaneDividerForeground(localSynthContext, paramGraphics, 0, 0, getWidth(), getHeight(), this.splitPane.getOrientation());
/*     */ 
/*  83 */     localSynthContext.dispose();
/*     */ 
/*  86 */     for (int i = 0; i < getComponentCount(); i++) {
/*  87 */       Component localComponent = getComponent(i);
/*  88 */       Rectangle localRectangle2 = localComponent.getBounds();
/*  89 */       Graphics localGraphics2 = paramGraphics.create(localRectangle2.x, localRectangle2.y, localRectangle2.width, localRectangle2.height);
/*     */ 
/*  91 */       localComponent.paint(localGraphics2);
/*  92 */       localGraphics2.dispose();
/*     */     }
/*  94 */     localGraphics1.dispose();
/*     */   }
/*     */ 
/*     */   private int mapDirection(boolean paramBoolean) {
/*  98 */     if (paramBoolean) {
/*  99 */       if (this.splitPane.getOrientation() == 1) {
/* 100 */         return 7;
/*     */       }
/* 102 */       return 1;
/*     */     }
/* 104 */     if (this.splitPane.getOrientation() == 1) {
/* 105 */       return 3;
/*     */     }
/* 107 */     return 5;
/*     */   }
/*     */ 
/*     */   protected JButton createLeftOneTouchButton()
/*     */   {
/* 116 */     SynthArrowButton localSynthArrowButton = new SynthArrowButton(1);
/* 117 */     int i = lookupOneTouchSize();
/*     */ 
/* 119 */     localSynthArrowButton.setName("SplitPaneDivider.leftOneTouchButton");
/* 120 */     localSynthArrowButton.setMinimumSize(new Dimension(i, i));
/* 121 */     localSynthArrowButton.setCursor(Cursor.getPredefinedCursor(0));
/* 122 */     localSynthArrowButton.setFocusPainted(false);
/* 123 */     localSynthArrowButton.setBorderPainted(false);
/* 124 */     localSynthArrowButton.setRequestFocusEnabled(false);
/* 125 */     localSynthArrowButton.setDirection(mapDirection(true));
/* 126 */     return localSynthArrowButton;
/*     */   }
/*     */ 
/*     */   private int lookupOneTouchSize() {
/* 130 */     return DefaultLookup.getInt(this.splitPaneUI.getSplitPane(), this.splitPaneUI, "SplitPaneDivider.oneTouchButtonSize", 6);
/*     */   }
/*     */ 
/*     */   protected JButton createRightOneTouchButton()
/*     */   {
/* 139 */     SynthArrowButton localSynthArrowButton = new SynthArrowButton(1);
/* 140 */     int i = lookupOneTouchSize();
/*     */ 
/* 142 */     localSynthArrowButton.setName("SplitPaneDivider.rightOneTouchButton");
/* 143 */     localSynthArrowButton.setMinimumSize(new Dimension(i, i));
/* 144 */     localSynthArrowButton.setCursor(Cursor.getPredefinedCursor(0));
/* 145 */     localSynthArrowButton.setFocusPainted(false);
/* 146 */     localSynthArrowButton.setBorderPainted(false);
/* 147 */     localSynthArrowButton.setRequestFocusEnabled(false);
/* 148 */     localSynthArrowButton.setDirection(mapDirection(false));
/* 149 */     return localSynthArrowButton;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthSplitPaneDivider
 * JD-Core Version:    0.6.2
 */