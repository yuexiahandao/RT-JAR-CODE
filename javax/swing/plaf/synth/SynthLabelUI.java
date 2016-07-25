/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicHTML;
/*     */ import javax.swing.plaf.basic.BasicLabelUI;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ public class SynthLabelUI extends BasicLabelUI
/*     */   implements SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  56 */     return new SynthLabelUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults(JLabel paramJLabel)
/*     */   {
/*  64 */     updateStyle(paramJLabel);
/*     */   }
/*     */ 
/*     */   void updateStyle(JLabel paramJLabel) {
/*  68 */     SynthContext localSynthContext = getContext(paramJLabel, 1);
/*  69 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*  70 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(JLabel paramJLabel)
/*     */   {
/*  78 */     SynthContext localSynthContext = getContext(paramJLabel, 1);
/*     */ 
/*  80 */     this.style.uninstallDefaults(localSynthContext);
/*  81 */     localSynthContext.dispose();
/*  82 */     this.style = null;
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/*  90 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/*  94 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/*  99 */     int i = SynthLookAndFeel.getComponentState(paramJComponent);
/* 100 */     if ((SynthLookAndFeel.getSelectedUI() == this) && (i == 1))
/*     */     {
/* 102 */       i = SynthLookAndFeel.getSelectedUIState() | 0x1;
/*     */     }
/* 104 */     return i;
/*     */   }
/*     */ 
/*     */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 112 */     if (paramJComponent == null) {
/* 113 */       throw new NullPointerException("Component must be non-null");
/*     */     }
/* 115 */     if ((paramInt1 < 0) || (paramInt2 < 0)) {
/* 116 */       throw new IllegalArgumentException("Width and height must be >= 0");
/*     */     }
/*     */ 
/* 119 */     JLabel localJLabel = (JLabel)paramJComponent;
/* 120 */     String str = localJLabel.getText();
/* 121 */     if ((str == null) || ("".equals(str))) {
/* 122 */       return -1;
/*     */     }
/* 124 */     Insets localInsets = localJLabel.getInsets();
/* 125 */     Rectangle localRectangle1 = new Rectangle();
/* 126 */     Rectangle localRectangle2 = new Rectangle();
/* 127 */     Rectangle localRectangle3 = new Rectangle();
/* 128 */     localRectangle1.x = localInsets.left;
/* 129 */     localRectangle1.y = localInsets.top;
/* 130 */     localRectangle1.width = (paramInt1 - (localInsets.right + localRectangle1.x));
/* 131 */     localRectangle1.height = (paramInt2 - (localInsets.bottom + localRectangle1.y));
/*     */ 
/* 134 */     SynthContext localSynthContext = getContext(localJLabel);
/* 135 */     FontMetrics localFontMetrics = localSynthContext.getComponent().getFontMetrics(localSynthContext.getStyle().getFont(localSynthContext));
/*     */ 
/* 137 */     localSynthContext.getStyle().getGraphicsUtils(localSynthContext).layoutText(localSynthContext, localFontMetrics, localJLabel.getText(), localJLabel.getIcon(), localJLabel.getHorizontalAlignment(), localJLabel.getVerticalAlignment(), localJLabel.getHorizontalTextPosition(), localJLabel.getVerticalTextPosition(), localRectangle1, localRectangle3, localRectangle2, localJLabel.getIconTextGap());
/*     */ 
/* 142 */     View localView = (View)localJLabel.getClientProperty("html");
/*     */     int i;
/* 144 */     if (localView != null) {
/* 145 */       i = BasicHTML.getHTMLBaseline(localView, localRectangle2.width, localRectangle2.height);
/*     */ 
/* 147 */       if (i >= 0)
/* 148 */         i += localRectangle2.y;
/*     */     }
/*     */     else
/*     */     {
/* 152 */       i = localRectangle2.y + localFontMetrics.getAscent();
/*     */     }
/* 154 */     localSynthContext.dispose();
/* 155 */     return i;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 172 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 174 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 175 */     localSynthContext.getPainter().paintLabelBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 177 */     paint(localSynthContext, paramGraphics);
/* 178 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 192 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 194 */     paint(localSynthContext, paramGraphics);
/* 195 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 206 */     JLabel localJLabel = (JLabel)paramSynthContext.getComponent();
/* 207 */     Icon localIcon = localJLabel.isEnabled() ? localJLabel.getIcon() : localJLabel.getDisabledIcon();
/*     */ 
/* 210 */     paramGraphics.setColor(paramSynthContext.getStyle().getColor(paramSynthContext, ColorType.TEXT_FOREGROUND));
/*     */ 
/* 212 */     paramGraphics.setFont(this.style.getFont(paramSynthContext));
/* 213 */     paramSynthContext.getStyle().getGraphicsUtils(paramSynthContext).paintText(paramSynthContext, paramGraphics, localJLabel.getText(), localIcon, localJLabel.getHorizontalAlignment(), localJLabel.getVerticalAlignment(), localJLabel.getHorizontalTextPosition(), localJLabel.getVerticalTextPosition(), localJLabel.getIconTextGap(), localJLabel.getDisplayedMnemonicIndex(), 0);
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 226 */     paramSynthContext.getPainter().paintLabelBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 234 */     JLabel localJLabel = (JLabel)paramJComponent;
/* 235 */     Icon localIcon = localJLabel.isEnabled() ? localJLabel.getIcon() : localJLabel.getDisabledIcon();
/*     */ 
/* 237 */     SynthContext localSynthContext = getContext(paramJComponent);
/* 238 */     Dimension localDimension = localSynthContext.getStyle().getGraphicsUtils(localSynthContext).getPreferredSize(localSynthContext, localSynthContext.getStyle().getFont(localSynthContext), localJLabel.getText(), localIcon, localJLabel.getHorizontalAlignment(), localJLabel.getVerticalAlignment(), localJLabel.getHorizontalTextPosition(), localJLabel.getVerticalTextPosition(), localJLabel.getIconTextGap(), localJLabel.getDisplayedMnemonicIndex());
/*     */ 
/* 246 */     localSynthContext.dispose();
/* 247 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 255 */     JLabel localJLabel = (JLabel)paramJComponent;
/* 256 */     Icon localIcon = localJLabel.isEnabled() ? localJLabel.getIcon() : localJLabel.getDisabledIcon();
/*     */ 
/* 258 */     SynthContext localSynthContext = getContext(paramJComponent);
/* 259 */     Dimension localDimension = localSynthContext.getStyle().getGraphicsUtils(localSynthContext).getMinimumSize(localSynthContext, localSynthContext.getStyle().getFont(localSynthContext), localJLabel.getText(), localIcon, localJLabel.getHorizontalAlignment(), localJLabel.getVerticalAlignment(), localJLabel.getHorizontalTextPosition(), localJLabel.getVerticalTextPosition(), localJLabel.getIconTextGap(), localJLabel.getDisplayedMnemonicIndex());
/*     */ 
/* 267 */     localSynthContext.dispose();
/* 268 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 276 */     JLabel localJLabel = (JLabel)paramJComponent;
/* 277 */     Icon localIcon = localJLabel.isEnabled() ? localJLabel.getIcon() : localJLabel.getDisabledIcon();
/*     */ 
/* 279 */     SynthContext localSynthContext = getContext(paramJComponent);
/* 280 */     Dimension localDimension = localSynthContext.getStyle().getGraphicsUtils(localSynthContext).getMaximumSize(localSynthContext, localSynthContext.getStyle().getFont(localSynthContext), localJLabel.getText(), localIcon, localJLabel.getHorizontalAlignment(), localJLabel.getVerticalAlignment(), localJLabel.getHorizontalTextPosition(), localJLabel.getVerticalTextPosition(), localJLabel.getIconTextGap(), localJLabel.getDisplayedMnemonicIndex());
/*     */ 
/* 288 */     localSynthContext.dispose();
/* 289 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 297 */     super.propertyChange(paramPropertyChangeEvent);
/* 298 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 299 */       updateStyle((JLabel)paramPropertyChangeEvent.getSource());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthLabelUI
 * JD-Core Version:    0.6.2
 */