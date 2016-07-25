/*     */ package javax.swing.plaf.basic;
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
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.ToolTipUI;
/*     */ import javax.swing.text.View;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class BasicToolTipUI extends ToolTipUI
/*     */ {
/*  50 */   static BasicToolTipUI sharedInstance = new BasicToolTipUI();
/*     */   private static PropertyChangeListener sharedPropertyChangedListener;
/*     */   private PropertyChangeListener propertyChangeListener;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  60 */     return sharedInstance;
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/*  68 */     installDefaults(paramJComponent);
/*  69 */     installComponents(paramJComponent);
/*  70 */     installListeners(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/*  75 */     uninstallDefaults(paramJComponent);
/*  76 */     uninstallComponents(paramJComponent);
/*  77 */     uninstallListeners(paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void installDefaults(JComponent paramJComponent) {
/*  81 */     LookAndFeel.installColorsAndFont(paramJComponent, "ToolTip.background", "ToolTip.foreground", "ToolTip.font");
/*     */ 
/*  84 */     LookAndFeel.installProperty(paramJComponent, "opaque", Boolean.TRUE);
/*  85 */     componentChanged(paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(JComponent paramJComponent) {
/*  89 */     LookAndFeel.uninstallBorder(paramJComponent);
/*     */   }
/*     */ 
/*     */   private void installComponents(JComponent paramJComponent)
/*     */   {
/*  95 */     BasicHTML.updateRenderer(paramJComponent, ((JToolTip)paramJComponent).getTipText());
/*     */   }
/*     */ 
/*     */   private void uninstallComponents(JComponent paramJComponent)
/*     */   {
/* 101 */     BasicHTML.updateRenderer(paramJComponent, "");
/*     */   }
/*     */ 
/*     */   protected void installListeners(JComponent paramJComponent) {
/* 105 */     this.propertyChangeListener = createPropertyChangeListener(paramJComponent);
/*     */ 
/* 107 */     paramJComponent.addPropertyChangeListener(this.propertyChangeListener);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners(JComponent paramJComponent) {
/* 111 */     paramJComponent.removePropertyChangeListener(this.propertyChangeListener);
/*     */ 
/* 113 */     this.propertyChangeListener = null;
/*     */   }
/*     */ 
/*     */   private PropertyChangeListener createPropertyChangeListener(JComponent paramJComponent)
/*     */   {
/* 119 */     if (sharedPropertyChangedListener == null) {
/* 120 */       sharedPropertyChangedListener = new PropertyChangeHandler(null);
/*     */     }
/* 122 */     return sharedPropertyChangedListener;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent) {
/* 126 */     Font localFont = paramJComponent.getFont();
/* 127 */     FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(paramJComponent, paramGraphics, localFont);
/* 128 */     Dimension localDimension = paramJComponent.getSize();
/*     */ 
/* 130 */     paramGraphics.setColor(paramJComponent.getForeground());
/*     */ 
/* 132 */     String str = ((JToolTip)paramJComponent).getTipText();
/* 133 */     if (str == null) {
/* 134 */       str = "";
/*     */     }
/*     */ 
/* 137 */     Insets localInsets = paramJComponent.getInsets();
/* 138 */     Rectangle localRectangle = new Rectangle(localInsets.left + 3, localInsets.top, localDimension.width - (localInsets.left + localInsets.right) - 6, localDimension.height - (localInsets.top + localInsets.bottom));
/*     */ 
/* 143 */     View localView = (View)paramJComponent.getClientProperty("html");
/* 144 */     if (localView != null) {
/* 145 */       localView.paint(paramGraphics, localRectangle);
/*     */     } else {
/* 147 */       paramGraphics.setFont(localFont);
/* 148 */       SwingUtilities2.drawString(paramJComponent, paramGraphics, str, localRectangle.x, localRectangle.y + localFontMetrics.getAscent());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 154 */     Font localFont = paramJComponent.getFont();
/* 155 */     FontMetrics localFontMetrics = paramJComponent.getFontMetrics(localFont);
/* 156 */     Insets localInsets = paramJComponent.getInsets();
/*     */ 
/* 158 */     Dimension localDimension = new Dimension(localInsets.left + localInsets.right, localInsets.top + localInsets.bottom);
/*     */ 
/* 160 */     String str = ((JToolTip)paramJComponent).getTipText();
/*     */ 
/* 162 */     if ((str == null) || (str.equals(""))) {
/* 163 */       str = "";
/*     */     }
/*     */     else {
/* 166 */       Object localObject = paramJComponent != null ? (View)paramJComponent.getClientProperty("html") : null;
/* 167 */       if (localObject != null) {
/* 168 */         localDimension.width += (int)localObject.getPreferredSpan(0) + 6;
/* 169 */         localDimension.height += (int)localObject.getPreferredSpan(1);
/*     */       } else {
/* 171 */         localDimension.width += SwingUtilities2.stringWidth(paramJComponent, localFontMetrics, str) + 6;
/* 172 */         localDimension.height += localFontMetrics.getHeight();
/*     */       }
/*     */     }
/* 175 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent) {
/* 179 */     Dimension localDimension = getPreferredSize(paramJComponent);
/* 180 */     View localView = (View)paramJComponent.getClientProperty("html");
/* 181 */     if (localView != null)
/*     */     {
/*     */       Dimension tmp21_20 = localDimension; tmp21_20.width = ((int)(tmp21_20.width - (localView.getPreferredSpan(0) - localView.getMinimumSpan(0))));
/*     */     }
/* 184 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent) {
/* 188 */     Dimension localDimension = getPreferredSize(paramJComponent);
/* 189 */     View localView = (View)paramJComponent.getClientProperty("html");
/* 190 */     if (localView != null)
/*     */     {
/*     */       Dimension tmp21_20 = localDimension; tmp21_20.width = ((int)(tmp21_20.width + (localView.getMaximumSpan(0) - localView.getPreferredSpan(0))));
/*     */     }
/* 193 */     return localDimension;
/*     */   }
/*     */ 
/*     */   private void componentChanged(JComponent paramJComponent)
/*     */   {
/* 204 */     JComponent localJComponent = ((JToolTip)paramJComponent).getComponent();
/*     */ 
/* 206 */     if ((localJComponent != null) && (!localJComponent.isEnabled()))
/*     */     {
/* 209 */       if (UIManager.getBorder("ToolTip.borderInactive") != null) {
/* 210 */         LookAndFeel.installBorder(paramJComponent, "ToolTip.borderInactive");
/*     */       }
/*     */       else {
/* 213 */         LookAndFeel.installBorder(paramJComponent, "ToolTip.border");
/*     */       }
/* 215 */       if (UIManager.getColor("ToolTip.backgroundInactive") != null) {
/* 216 */         LookAndFeel.installColors(paramJComponent, "ToolTip.backgroundInactive", "ToolTip.foregroundInactive");
/*     */       }
/*     */       else
/*     */       {
/* 220 */         LookAndFeel.installColors(paramJComponent, "ToolTip.background", "ToolTip.foreground");
/*     */       }
/*     */     }
/*     */     else {
/* 224 */       LookAndFeel.installBorder(paramJComponent, "ToolTip.border");
/* 225 */       LookAndFeel.installColors(paramJComponent, "ToolTip.background", "ToolTip.foreground");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class PropertyChangeHandler
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */     {
/* 234 */       String str1 = paramPropertyChangeEvent.getPropertyName();
/*     */       JToolTip localJToolTip;
/* 235 */       if ((str1.equals("tiptext")) || ("font".equals(str1)) || ("foreground".equals(str1)))
/*     */       {
/* 240 */         localJToolTip = (JToolTip)paramPropertyChangeEvent.getSource();
/* 241 */         String str2 = localJToolTip.getTipText();
/* 242 */         BasicHTML.updateRenderer(localJToolTip, str2);
/*     */       }
/* 244 */       else if ("component".equals(str1)) {
/* 245 */         localJToolTip = (JToolTip)paramPropertyChangeEvent.getSource();
/*     */ 
/* 247 */         if ((localJToolTip.getUI() instanceof BasicToolTipUI))
/* 248 */           ((BasicToolTipUI)localJToolTip.getUI()).componentChanged(localJToolTip);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicToolTipUI
 * JD-Core Version:    0.6.2
 */