/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.KeyEvent;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JToolTip;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicHTML;
/*     */ import javax.swing.plaf.basic.BasicToolTipUI;
/*     */ import javax.swing.text.View;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class MetalToolTipUI extends BasicToolTipUI
/*     */ {
/*  56 */   static MetalToolTipUI sharedInstance = new MetalToolTipUI();
/*     */   private Font smallFont;
/*     */   private JToolTip tip;
/*     */   public static final int padSpaceBetweenStrings = 12;
/*     */   private String acceleratorDelimiter;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  68 */     return sharedInstance;
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent) {
/*  72 */     super.installUI(paramJComponent);
/*  73 */     this.tip = ((JToolTip)paramJComponent);
/*  74 */     Font localFont = paramJComponent.getFont();
/*  75 */     this.smallFont = new Font(localFont.getName(), localFont.getStyle(), localFont.getSize() - 2);
/*  76 */     this.acceleratorDelimiter = UIManager.getString("MenuItem.acceleratorDelimiter");
/*  77 */     if (this.acceleratorDelimiter == null) this.acceleratorDelimiter = "-"; 
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/*  81 */     super.uninstallUI(paramJComponent);
/*  82 */     this.tip = null;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent) {
/*  86 */     JToolTip localJToolTip = (JToolTip)paramJComponent;
/*  87 */     Font localFont = paramJComponent.getFont();
/*  88 */     FontMetrics localFontMetrics1 = SwingUtilities2.getFontMetrics(paramJComponent, paramGraphics, localFont);
/*  89 */     Dimension localDimension = paramJComponent.getSize();
/*     */ 
/*  92 */     paramGraphics.setColor(paramJComponent.getForeground());
/*     */ 
/*  94 */     String str1 = localJToolTip.getTipText();
/*  95 */     if (str1 == null) {
/*  96 */       str1 = "";
/*     */     }
/*     */ 
/*  99 */     String str2 = getAcceleratorString(localJToolTip);
/* 100 */     FontMetrics localFontMetrics2 = SwingUtilities2.getFontMetrics(paramJComponent, paramGraphics, this.smallFont);
/* 101 */     int j = calcAccelSpacing(paramJComponent, localFontMetrics2, str2);
/*     */ 
/* 103 */     Insets localInsets = localJToolTip.getInsets();
/* 104 */     Rectangle localRectangle = new Rectangle(localInsets.left + 3, localInsets.top, localDimension.width - (localInsets.left + localInsets.right) - 6 - j, localDimension.height - (localInsets.top + localInsets.bottom));
/*     */ 
/* 109 */     View localView = (View)paramJComponent.getClientProperty("html");
/*     */     int i;
/* 110 */     if (localView != null) {
/* 111 */       localView.paint(paramGraphics, localRectangle);
/* 112 */       i = BasicHTML.getHTMLBaseline(localView, localRectangle.width, localRectangle.height);
/*     */     }
/*     */     else {
/* 115 */       paramGraphics.setFont(localFont);
/* 116 */       SwingUtilities2.drawString(localJToolTip, paramGraphics, str1, localRectangle.x, localRectangle.y + localFontMetrics1.getAscent());
/*     */ 
/* 118 */       i = localFontMetrics1.getAscent();
/*     */     }
/*     */ 
/* 121 */     if (!str2.equals("")) {
/* 122 */       paramGraphics.setFont(this.smallFont);
/* 123 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/* 124 */       SwingUtilities2.drawString(localJToolTip, paramGraphics, str2, localJToolTip.getWidth() - 1 - localInsets.right - j + 12 - 3, localRectangle.y + i);
/*     */     }
/*     */   }
/*     */ 
/*     */   private int calcAccelSpacing(JComponent paramJComponent, FontMetrics paramFontMetrics, String paramString)
/*     */   {
/* 134 */     return paramString.equals("") ? 0 : 12 + SwingUtilities2.stringWidth(paramJComponent, paramFontMetrics, paramString);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 141 */     Dimension localDimension = super.getPreferredSize(paramJComponent);
/*     */ 
/* 143 */     String str = getAcceleratorString((JToolTip)paramJComponent);
/* 144 */     if (!str.equals("")) {
/* 145 */       localDimension.width += calcAccelSpacing(paramJComponent, paramJComponent.getFontMetrics(this.smallFont), str);
/*     */     }
/* 147 */     return localDimension;
/*     */   }
/*     */ 
/*     */   protected boolean isAcceleratorHidden() {
/* 151 */     Boolean localBoolean = (Boolean)UIManager.get("ToolTip.hideAccelerator");
/* 152 */     return (localBoolean != null) && (localBoolean.booleanValue());
/*     */   }
/*     */ 
/*     */   private String getAcceleratorString(JToolTip paramJToolTip) {
/* 156 */     this.tip = paramJToolTip;
/*     */ 
/* 158 */     String str = getAcceleratorString();
/*     */ 
/* 160 */     this.tip = null;
/* 161 */     return str;
/*     */   }
/*     */ 
/*     */   public String getAcceleratorString()
/*     */   {
/* 171 */     if ((this.tip == null) || (isAcceleratorHidden())) {
/* 172 */       return "";
/*     */     }
/* 174 */     JComponent localJComponent = this.tip.getComponent();
/* 175 */     if (!(localJComponent instanceof AbstractButton)) {
/* 176 */       return "";
/*     */     }
/*     */ 
/* 179 */     KeyStroke[] arrayOfKeyStroke = localJComponent.getInputMap(2).keys();
/* 180 */     if (arrayOfKeyStroke == null) {
/* 181 */       return "";
/*     */     }
/*     */ 
/* 184 */     String str = "";
/*     */ 
/* 186 */     int i = 0; if (i < arrayOfKeyStroke.length) {
/* 187 */       int j = arrayOfKeyStroke[i].getModifiers();
/* 188 */       str = KeyEvent.getKeyModifiersText(j) + this.acceleratorDelimiter + KeyEvent.getKeyText(arrayOfKeyStroke[i].getKeyCode());
/*     */     }
/*     */ 
/* 194 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalToolTipUI
 * JD-Core Version:    0.6.2
 */