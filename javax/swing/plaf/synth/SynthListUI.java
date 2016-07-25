/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.DefaultListCellRenderer.UIResource;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicListUI;
/*     */ 
/*     */ public class SynthListUI extends BasicListUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */   private boolean useListColors;
/*     */   private boolean useUIBorder;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  56 */     return new SynthListUI();
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/*  73 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/*  75 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/*  76 */     localSynthContext.getPainter().paintListBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/*  78 */     localSynthContext.dispose();
/*  79 */     paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  88 */     paramSynthContext.getPainter().paintListBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/*  96 */     super.installListeners();
/*  97 */     this.list.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 105 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 106 */       updateStyle((JList)paramPropertyChangeEvent.getSource());
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 115 */     super.uninstallListeners();
/* 116 */     this.list.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/* 124 */     if ((this.list.getCellRenderer() == null) || ((this.list.getCellRenderer() instanceof UIResource)))
/*     */     {
/* 126 */       this.list.setCellRenderer(new SynthListCellRenderer(null));
/*     */     }
/* 128 */     updateStyle(this.list);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JComponent paramJComponent) {
/* 132 */     SynthContext localSynthContext = getContext(this.list, 1);
/* 133 */     SynthStyle localSynthStyle = this.style;
/*     */ 
/* 135 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*     */ 
/* 137 */     if (this.style != localSynthStyle) {
/* 138 */       localSynthContext.setComponentState(512);
/* 139 */       Color localColor1 = this.list.getSelectionBackground();
/* 140 */       if ((localColor1 == null) || ((localColor1 instanceof UIResource))) {
/* 141 */         this.list.setSelectionBackground(this.style.getColor(localSynthContext, ColorType.TEXT_BACKGROUND));
/*     */       }
/*     */ 
/* 145 */       Color localColor2 = this.list.getSelectionForeground();
/* 146 */       if ((localColor2 == null) || ((localColor2 instanceof UIResource))) {
/* 147 */         this.list.setSelectionForeground(this.style.getColor(localSynthContext, ColorType.TEXT_FOREGROUND));
/*     */       }
/*     */ 
/* 151 */       this.useListColors = this.style.getBoolean(localSynthContext, "List.rendererUseListColors", true);
/*     */ 
/* 153 */       this.useUIBorder = this.style.getBoolean(localSynthContext, "List.rendererUseUIBorder", true);
/*     */ 
/* 156 */       int i = this.style.getInt(localSynthContext, "List.cellHeight", -1);
/* 157 */       if (i != -1) {
/* 158 */         this.list.setFixedCellHeight(i);
/*     */       }
/* 160 */       if (localSynthStyle != null) {
/* 161 */         uninstallKeyboardActions();
/* 162 */         installKeyboardActions();
/*     */       }
/*     */     }
/* 165 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 173 */     super.uninstallDefaults();
/*     */ 
/* 175 */     SynthContext localSynthContext = getContext(this.list, 1);
/*     */ 
/* 177 */     this.style.uninstallDefaults(localSynthContext);
/* 178 */     localSynthContext.dispose();
/* 179 */     this.style = null;
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 187 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 191 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/* 196 */     return SynthLookAndFeel.getComponentState(paramJComponent);
/*     */   }
/*     */   private class SynthListCellRenderer extends DefaultListCellRenderer.UIResource {
/*     */     private SynthListCellRenderer() {
/*     */     }
/*     */     public String getName() {
/* 202 */       return "List.cellRenderer";
/*     */     }
/*     */ 
/*     */     public void setBorder(Border paramBorder) {
/* 206 */       if ((SynthListUI.this.useUIBorder) || ((paramBorder instanceof SynthBorder)))
/* 207 */         super.setBorder(paramBorder);
/*     */     }
/*     */ 
/*     */     public Component getListCellRendererComponent(JList paramJList, Object paramObject, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*     */     {
/* 213 */       if ((!SynthListUI.this.useListColors) && ((paramBoolean1) || (paramBoolean2))) {
/* 214 */         SynthLookAndFeel.setSelectedUI((SynthLabelUI)SynthLookAndFeel.getUIOfType(getUI(), SynthLabelUI.class), paramBoolean1, paramBoolean2, paramJList.isEnabled(), false);
/*     */       }
/*     */       else
/*     */       {
/* 219 */         SynthLookAndFeel.resetSelectedUI();
/*     */       }
/*     */ 
/* 222 */       super.getListCellRendererComponent(paramJList, paramObject, paramInt, paramBoolean1, paramBoolean2);
/*     */ 
/* 224 */       return this;
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics) {
/* 228 */       super.paint(paramGraphics);
/* 229 */       SynthLookAndFeel.resetSelectedUI();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthListUI
 * JD-Core Version:    0.6.2
 */