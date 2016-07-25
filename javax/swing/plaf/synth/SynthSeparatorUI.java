/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.JToolBar.Separator;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.DimensionUIResource;
/*     */ import javax.swing.plaf.SeparatorUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ 
/*     */ public class SynthSeparatorUI extends SeparatorUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  57 */     return new SynthSeparatorUI();
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/*  65 */     installDefaults((JSeparator)paramJComponent);
/*  66 */     installListeners((JSeparator)paramJComponent);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/*  74 */     uninstallListeners((JSeparator)paramJComponent);
/*  75 */     uninstallDefaults((JSeparator)paramJComponent);
/*     */   }
/*     */ 
/*     */   public void installDefaults(JSeparator paramJSeparator)
/*     */   {
/*  83 */     updateStyle(paramJSeparator);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JSeparator paramJSeparator) {
/*  87 */     SynthContext localSynthContext = getContext(paramJSeparator, 1);
/*  88 */     SynthStyle localSynthStyle = this.style;
/*     */ 
/*  90 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*     */ 
/*  92 */     if ((this.style != localSynthStyle) && 
/*  93 */       ((paramJSeparator instanceof JToolBar.Separator))) {
/*  94 */       Object localObject = ((JToolBar.Separator)paramJSeparator).getSeparatorSize();
/*  95 */       if ((localObject == null) || ((localObject instanceof UIResource))) {
/*  96 */         localObject = (DimensionUIResource)this.style.get(localSynthContext, "ToolBar.separatorSize");
/*     */ 
/*  98 */         if (localObject == null) {
/*  99 */           localObject = new DimensionUIResource(10, 10);
/*     */         }
/* 101 */         ((JToolBar.Separator)paramJSeparator).setSeparatorSize((Dimension)localObject);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 106 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void uninstallDefaults(JSeparator paramJSeparator)
/*     */   {
/* 114 */     SynthContext localSynthContext = getContext(paramJSeparator, 1);
/*     */ 
/* 116 */     this.style.uninstallDefaults(localSynthContext);
/* 117 */     localSynthContext.dispose();
/* 118 */     this.style = null;
/*     */   }
/*     */ 
/*     */   public void installListeners(JSeparator paramJSeparator)
/*     */   {
/* 126 */     paramJSeparator.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   public void uninstallListeners(JSeparator paramJSeparator)
/*     */   {
/* 134 */     paramJSeparator.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 151 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 153 */     JSeparator localJSeparator = (JSeparator)localSynthContext.getComponent();
/* 154 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 155 */     localSynthContext.getPainter().paintSeparatorBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight(), localJSeparator.getOrientation());
/*     */ 
/* 158 */     paint(localSynthContext, paramGraphics);
/* 159 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 173 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 175 */     paint(localSynthContext, paramGraphics);
/* 176 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 187 */     JSeparator localJSeparator = (JSeparator)paramSynthContext.getComponent();
/* 188 */     paramSynthContext.getPainter().paintSeparatorForeground(paramSynthContext, paramGraphics, 0, 0, localJSeparator.getWidth(), localJSeparator.getHeight(), localJSeparator.getOrientation());
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 199 */     JSeparator localJSeparator = (JSeparator)paramSynthContext.getComponent();
/* 200 */     paramSynthContext.getPainter().paintSeparatorBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, localJSeparator.getOrientation());
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 209 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 211 */     int i = this.style.getInt(localSynthContext, "Separator.thickness", 2);
/* 212 */     Insets localInsets = paramJComponent.getInsets();
/*     */     Dimension localDimension;
/* 215 */     if (((JSeparator)paramJComponent).getOrientation() == 1) {
/* 216 */       localDimension = new Dimension(localInsets.left + localInsets.right + i, localInsets.top + localInsets.bottom);
/*     */     }
/*     */     else {
/* 219 */       localDimension = new Dimension(localInsets.left + localInsets.right, localInsets.top + localInsets.bottom + i);
/*     */     }
/*     */ 
/* 222 */     localSynthContext.dispose();
/* 223 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 231 */     return getPreferredSize(paramJComponent);
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 239 */     return new Dimension(32767, 32767);
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 247 */     return getContext(paramJComponent, SynthLookAndFeel.getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 251 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 256 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 257 */       updateStyle((JSeparator)paramPropertyChangeEvent.getSource());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthSeparatorUI
 * JD-Core Version:    0.6.2
 */