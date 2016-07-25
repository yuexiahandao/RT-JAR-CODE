/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicPopupMenuUI;
/*     */ 
/*     */ public class SynthPopupMenuUI extends BasicPopupMenuUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  55 */     return new SynthPopupMenuUI();
/*     */   }
/*     */ 
/*     */   public void installDefaults()
/*     */   {
/*  63 */     if ((this.popupMenu.getLayout() == null) || ((this.popupMenu.getLayout() instanceof UIResource)))
/*     */     {
/*  65 */       this.popupMenu.setLayout(new SynthMenuLayout(this.popupMenu, 1));
/*     */     }
/*  67 */     updateStyle(this.popupMenu);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JComponent paramJComponent) {
/*  71 */     SynthContext localSynthContext = getContext(paramJComponent, 1);
/*  72 */     SynthStyle localSynthStyle = this.style;
/*  73 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*  74 */     if ((this.style != localSynthStyle) && 
/*  75 */       (localSynthStyle != null)) {
/*  76 */       uninstallKeyboardActions();
/*  77 */       installKeyboardActions();
/*     */     }
/*     */ 
/*  80 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/*  88 */     super.installListeners();
/*  89 */     this.popupMenu.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/*  97 */     SynthContext localSynthContext = getContext(this.popupMenu, 1);
/*     */ 
/*  99 */     this.style.uninstallDefaults(localSynthContext);
/* 100 */     localSynthContext.dispose();
/* 101 */     this.style = null;
/*     */ 
/* 103 */     if ((this.popupMenu.getLayout() instanceof UIResource))
/* 104 */       this.popupMenu.setLayout(null);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 113 */     super.uninstallListeners();
/* 114 */     this.popupMenu.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 122 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 126 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/* 131 */     return SynthLookAndFeel.getComponentState(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 148 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 150 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 151 */     localSynthContext.getPainter().paintPopupMenuBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 153 */     paint(localSynthContext, paramGraphics);
/* 154 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 168 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 170 */     paint(localSynthContext, paramGraphics);
/* 171 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 190 */     paramSynthContext.getPainter().paintPopupMenuBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 198 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 199 */       updateStyle(this.popupMenu);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthPopupMenuUI
 * JD-Core Version:    0.6.2
 */