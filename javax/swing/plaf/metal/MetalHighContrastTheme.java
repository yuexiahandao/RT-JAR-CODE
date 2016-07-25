/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ import javax.swing.border.LineBorder;
/*     */ import javax.swing.plaf.BorderUIResource;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.basic.BasicBorders.MarginBorder;
/*     */ 
/*     */ class MetalHighContrastTheme extends DefaultMetalTheme
/*     */ {
/*  42 */   private static final ColorUIResource primary1 = new ColorUIResource(0, 0, 0);
/*     */ 
/*  44 */   private static final ColorUIResource primary2 = new ColorUIResource(204, 204, 204);
/*     */ 
/*  46 */   private static final ColorUIResource primary3 = new ColorUIResource(255, 255, 255);
/*     */ 
/*  48 */   private static final ColorUIResource primaryHighlight = new ColorUIResource(102, 102, 102);
/*     */ 
/*  50 */   private static final ColorUIResource secondary2 = new ColorUIResource(204, 204, 204);
/*     */ 
/*  52 */   private static final ColorUIResource secondary3 = new ColorUIResource(255, 255, 255);
/*     */ 
/*  54 */   private static final ColorUIResource controlHighlight = new ColorUIResource(102, 102, 102);
/*     */ 
/*     */   public String getName()
/*     */   {
/*  61 */     return "Contrast";
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getPrimary1() {
/*  65 */     return primary1;
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getPrimary2() {
/*  69 */     return primary2;
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getPrimary3() {
/*  73 */     return primary3;
/*     */   }
/*     */ 
/*     */   public ColorUIResource getPrimaryControlHighlight() {
/*  77 */     return primaryHighlight;
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getSecondary2() {
/*  81 */     return secondary2;
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getSecondary3() {
/*  85 */     return secondary3;
/*     */   }
/*     */ 
/*     */   public ColorUIResource getControlHighlight()
/*     */   {
/*  90 */     return secondary2;
/*     */   }
/*     */ 
/*     */   public ColorUIResource getFocusColor() {
/*  94 */     return getBlack();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getTextHighlightColor() {
/*  98 */     return getBlack();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getHighlightedTextColor() {
/* 102 */     return getWhite();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getMenuSelectedBackground() {
/* 106 */     return getBlack();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getMenuSelectedForeground() {
/* 110 */     return getWhite();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getAcceleratorForeground() {
/* 114 */     return getBlack();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getAcceleratorSelectedForeground() {
/* 118 */     return getWhite();
/*     */   }
/*     */ 
/*     */   public void addCustomEntriesToTable(UIDefaults paramUIDefaults) {
/* 122 */     BorderUIResource localBorderUIResource1 = new BorderUIResource(new LineBorder(getBlack()));
/*     */ 
/* 124 */     BorderUIResource localBorderUIResource2 = new BorderUIResource(new LineBorder(getWhite()));
/*     */ 
/* 126 */     BorderUIResource localBorderUIResource3 = new BorderUIResource(new CompoundBorder(localBorderUIResource1, new BasicBorders.MarginBorder()));
/*     */ 
/* 129 */     Object[] arrayOfObject = { "ToolTip.border", localBorderUIResource1, "TitledBorder.border", localBorderUIResource1, "TextField.border", localBorderUIResource3, "PasswordField.border", localBorderUIResource3, "TextArea.border", localBorderUIResource3, "TextPane.border", localBorderUIResource3, "EditorPane.border", localBorderUIResource3, "ComboBox.background", getWindowBackground(), "ComboBox.foreground", getUserTextColor(), "ComboBox.selectionBackground", getTextHighlightColor(), "ComboBox.selectionForeground", getHighlightedTextColor(), "ProgressBar.foreground", getUserTextColor(), "ProgressBar.background", getWindowBackground(), "ProgressBar.selectionForeground", getWindowBackground(), "ProgressBar.selectionBackground", getUserTextColor(), "OptionPane.errorDialog.border.background", getPrimary1(), "OptionPane.errorDialog.titlePane.foreground", getPrimary3(), "OptionPane.errorDialog.titlePane.background", getPrimary1(), "OptionPane.errorDialog.titlePane.shadow", getPrimary2(), "OptionPane.questionDialog.border.background", getPrimary1(), "OptionPane.questionDialog.titlePane.foreground", getPrimary3(), "OptionPane.questionDialog.titlePane.background", getPrimary1(), "OptionPane.questionDialog.titlePane.shadow", getPrimary2(), "OptionPane.warningDialog.border.background", getPrimary1(), "OptionPane.warningDialog.titlePane.foreground", getPrimary3(), "OptionPane.warningDialog.titlePane.background", getPrimary1(), "OptionPane.warningDialog.titlePane.shadow", getPrimary2() };
/*     */ 
/* 180 */     paramUIDefaults.putDefaults(arrayOfObject);
/*     */   }
/*     */ 
/*     */   boolean isSystemTheme()
/*     */   {
/* 187 */     return getClass() == MetalHighContrastTheme.class;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalHighContrastTheme
 * JD-Core Version:    0.6.2
 */