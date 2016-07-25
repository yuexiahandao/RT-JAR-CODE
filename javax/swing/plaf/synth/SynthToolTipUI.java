/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JToolTip;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicHTML;
/*     */ import javax.swing.plaf.basic.BasicToolTipUI;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ public class SynthToolTipUI extends BasicToolTipUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  57 */     return new SynthToolTipUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults(JComponent paramJComponent)
/*     */   {
/*  65 */     updateStyle(paramJComponent);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JComponent paramJComponent) {
/*  69 */     SynthContext localSynthContext = getContext(paramJComponent, 1);
/*  70 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*  71 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(JComponent paramJComponent)
/*     */   {
/*  79 */     SynthContext localSynthContext = getContext(paramJComponent, 1);
/*  80 */     this.style.uninstallDefaults(localSynthContext);
/*  81 */     localSynthContext.dispose();
/*  82 */     this.style = null;
/*     */   }
/*     */ 
/*     */   protected void installListeners(JComponent paramJComponent)
/*     */   {
/*  90 */     paramJComponent.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners(JComponent paramJComponent)
/*     */   {
/*  98 */     paramJComponent.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 106 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 110 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/* 115 */     JComponent localJComponent = ((JToolTip)paramJComponent).getComponent();
/*     */ 
/* 117 */     if ((localJComponent != null) && (!localJComponent.isEnabled())) {
/* 118 */       return 8;
/*     */     }
/* 120 */     return SynthLookAndFeel.getComponentState(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 137 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 139 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 140 */     localSynthContext.getPainter().paintToolTipBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 142 */     paint(localSynthContext, paramGraphics);
/* 143 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 152 */     paramSynthContext.getPainter().paintToolTipBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 166 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 168 */     paint(localSynthContext, paramGraphics);
/* 169 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 180 */     JToolTip localJToolTip = (JToolTip)paramSynthContext.getComponent();
/*     */ 
/* 182 */     Insets localInsets = localJToolTip.getInsets();
/* 183 */     View localView = (View)localJToolTip.getClientProperty("html");
/* 184 */     if (localView != null) {
/* 185 */       Rectangle localRectangle = new Rectangle(localInsets.left, localInsets.top, localJToolTip.getWidth() - (localInsets.left + localInsets.right), localJToolTip.getHeight() - (localInsets.top + localInsets.bottom));
/*     */ 
/* 188 */       localView.paint(paramGraphics, localRectangle);
/*     */     } else {
/* 190 */       paramGraphics.setColor(paramSynthContext.getStyle().getColor(paramSynthContext, ColorType.TEXT_FOREGROUND));
/*     */ 
/* 192 */       paramGraphics.setFont(this.style.getFont(paramSynthContext));
/* 193 */       paramSynthContext.getStyle().getGraphicsUtils(paramSynthContext).paintText(paramSynthContext, paramGraphics, localJToolTip.getTipText(), localInsets.left, localInsets.top, -1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 203 */     SynthContext localSynthContext = getContext(paramJComponent);
/* 204 */     Insets localInsets = paramJComponent.getInsets();
/* 205 */     Dimension localDimension = new Dimension(localInsets.left + localInsets.right, localInsets.top + localInsets.bottom);
/*     */ 
/* 207 */     String str = ((JToolTip)paramJComponent).getTipText();
/*     */ 
/* 209 */     if (str != null) {
/* 210 */       Object localObject = paramJComponent != null ? (View)paramJComponent.getClientProperty("html") : null;
/* 211 */       if (localObject != null) {
/* 212 */         localDimension.width += (int)localObject.getPreferredSpan(0);
/* 213 */         localDimension.height += (int)localObject.getPreferredSpan(1);
/*     */       } else {
/* 215 */         Font localFont = localSynthContext.getStyle().getFont(localSynthContext);
/* 216 */         FontMetrics localFontMetrics = paramJComponent.getFontMetrics(localFont);
/* 217 */         localDimension.width += localSynthContext.getStyle().getGraphicsUtils(localSynthContext).computeStringWidth(localSynthContext, localFont, localFontMetrics, str);
/*     */ 
/* 219 */         localDimension.height += localFontMetrics.getHeight();
/*     */       }
/*     */     }
/* 222 */     localSynthContext.dispose();
/* 223 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 231 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent)) {
/* 232 */       updateStyle((JToolTip)paramPropertyChangeEvent.getSource());
/*     */     }
/* 234 */     String str1 = paramPropertyChangeEvent.getPropertyName();
/* 235 */     if ((str1.equals("tiptext")) || ("font".equals(str1)) || ("foreground".equals(str1)))
/*     */     {
/* 240 */       JToolTip localJToolTip = (JToolTip)paramPropertyChangeEvent.getSource();
/* 241 */       String str2 = localJToolTip.getTipText();
/* 242 */       BasicHTML.updateRenderer(localJToolTip, str2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthToolTipUI
 * JD-Core Version:    0.6.2
 */